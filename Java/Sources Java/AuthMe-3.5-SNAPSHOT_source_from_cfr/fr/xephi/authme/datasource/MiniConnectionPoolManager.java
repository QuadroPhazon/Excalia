/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.datasource;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public class MiniConnectionPoolManager {
    private ConnectionPoolDataSource dataSource;
    private int maxConnections;
    private long timeoutMs;
    private PrintWriter logWriter;
    private Semaphore semaphore;
    private PoolConnectionEventListener poolConnectionEventListener;
    private LinkedList<PooledConnection> recycledConnections;
    private int activeConnections;
    private boolean isDisposed;
    private boolean doPurgeConnection;
    private PooledConnection connectionInTransition;

    public MiniConnectionPoolManager(ConnectionPoolDataSource dataSource, int maxConnections) {
        this(dataSource, maxConnections, 60);
    }

    public MiniConnectionPoolManager(ConnectionPoolDataSource dataSource, int maxConnections, int timeout) {
        this.dataSource = dataSource;
        this.maxConnections = maxConnections;
        this.timeoutMs = (long)timeout * 1000;
        try {
            this.logWriter = dataSource.getLogWriter();
        }
        catch (SQLException e) {
            // empty catch block
        }
        if (maxConnections < 1) {
            throw new IllegalArgumentException("Invalid maxConnections value.");
        }
        this.semaphore = new Semaphore(maxConnections, true);
        this.recycledConnections = new LinkedList();
        this.poolConnectionEventListener = new PoolConnectionEventListener();
    }

    public synchronized void dispose() throws SQLException {
        if (this.isDisposed) {
            return;
        }
        this.isDisposed = true;
        SQLException e = null;
        while (!this.recycledConnections.isEmpty()) {
            PooledConnection pconn = this.recycledConnections.remove();
            try {
                pconn.close();
            }
            catch (SQLException e2) {
                if (e != null) continue;
                e = e2;
            }
        }
        if (e != null) {
            throw e;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.getConnection2(this.timeoutMs);
    }

    private Connection getConnection2(long timeoutMs) throws SQLException {
        MiniConnectionPoolManager miniConnectionPoolManager = this;
        synchronized (miniConnectionPoolManager) {
            if (this.isDisposed) {
                throw new IllegalStateException("Connection pool has been disposed.");
            }
        }
        try {
            if (!this.semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException();
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for a database connection.", e);
        }
        boolean ok = false;
        try {
            Connection conn = this.getConnection3();
            ok = true;
            Connection connection = conn;
            Object var7_8 = null;
            if (!ok) {
                this.semaphore.release();
            }
            return connection;
        }
        catch (Throwable var6_10) {
            Object var7_9 = null;
            if (!ok) {
                this.semaphore.release();
            }
            throw var6_10;
        }
    }

    private synchronized Connection getConnection3() throws SQLException {
        PooledConnection pconn;
        Connection conn;
        if (this.isDisposed) {
            throw new IllegalStateException("Connection pool has been disposed.");
        }
        if (!this.recycledConnections.isEmpty()) {
            pconn = this.recycledConnections.remove();
        } else {
            pconn = this.dataSource.getPooledConnection();
            pconn.addConnectionEventListener(this.poolConnectionEventListener);
        }
        try {
            this.connectionInTransition = pconn;
            ++this.activeConnections;
            conn = pconn.getConnection();
            Object var4_3 = null;
            this.connectionInTransition = null;
        }
        catch (Throwable var3_5) {
            Object var4_4 = null;
            this.connectionInTransition = null;
            throw var3_5;
        }
        this.assertInnerState();
        return conn;
    }

    public Connection getValidConnection() {
        long time = System.currentTimeMillis();
        long timeoutTime = time + this.timeoutMs;
        int triesWithoutDelay = this.getInactiveConnections() + 1;
        do {
            Connection conn;
            if ((conn = this.getValidConnection2(time, timeoutTime)) != null) {
                return conn;
            }
            if (--triesWithoutDelay > 0) continue;
            triesWithoutDelay = 0;
            try {
                Thread.sleep(250);
                continue;
            }
            catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while waiting for a valid database connection.", e);
            }
        } while ((time = System.currentTimeMillis()) < timeoutTime);
        throw new TimeoutException("Timeout while waiting for a valid database connection.");
    }

    private Connection getValidConnection2(long time, long timeoutTime) {
        Connection conn;
        long rtime = Math.max(1, timeoutTime - time);
        try {
            conn = this.getConnection2(rtime);
        }
        catch (SQLException e) {
            return null;
        }
        rtime = timeoutTime - System.currentTimeMillis();
        int rtimeSecs = Math.max(1, (int)((rtime + 999) / 1000));
        try {
            if (conn.isValid(rtimeSecs)) {
                return conn;
            }
        }
        catch (SQLException e) {
            // empty catch block
        }
        this.purgeConnection(conn);
        return null;
    }

    private synchronized void purgeConnection(Connection conn) {
        try {
            try {
                this.doPurgeConnection = true;
                conn.close();
            }
            catch (SQLException e) {
                Object var4_3 = null;
                this.doPurgeConnection = false;
            }
            Object var4_2 = null;
            this.doPurgeConnection = false;
        }
        catch (Throwable var3_6) {
            Object var4_4 = null;
            this.doPurgeConnection = false;
            throw var3_6;
        }
    }

    private synchronized void recycleConnection(PooledConnection pconn) {
        if (this.isDisposed || this.doPurgeConnection) {
            this.disposeConnection(pconn);
            return;
        }
        if (this.activeConnections <= 0) {
            throw new AssertionError((Object)"AuthMeDatabaseError");
        }
        --this.activeConnections;
        this.semaphore.release();
        this.recycledConnections.add(pconn);
        this.assertInnerState();
    }

    private synchronized void disposeConnection(PooledConnection pconn) {
        pconn.removeConnectionEventListener(this.poolConnectionEventListener);
        if (!this.recycledConnections.remove(pconn) && pconn != this.connectionInTransition) {
            if (this.activeConnections <= 0) {
                throw new AssertionError((Object)"AuthMeDatabaseError");
            }
            --this.activeConnections;
            this.semaphore.release();
        }
        this.closeConnectionAndIgnoreException(pconn);
        this.assertInnerState();
    }

    private void closeConnectionAndIgnoreException(PooledConnection pconn) {
        try {
            pconn.close();
        }
        catch (SQLException e) {
            this.log("Error while closing database connection: " + e.toString());
        }
    }

    private void log(String msg) {
        String s = "MiniConnectionPoolManager: " + msg;
        try {
            if (this.logWriter == null) {
                System.err.println(s);
            } else {
                this.logWriter.println(s);
            }
        }
        catch (Exception e) {
            // empty catch block
        }
    }

    private synchronized void assertInnerState() {
        if (this.activeConnections < 0) {
            throw new AssertionError((Object)"AuthMeDatabaseError");
        }
        if (this.activeConnections + this.recycledConnections.size() > this.maxConnections) {
            throw new AssertionError((Object)"AuthMeDatabaseError");
        }
        if (this.activeConnections + this.semaphore.availablePermits() > this.maxConnections) {
            throw new AssertionError((Object)"AuthMeDatabaseError");
        }
    }

    public synchronized int getActiveConnections() {
        return this.activeConnections;
    }

    public synchronized int getInactiveConnections() {
        return this.recycledConnections.size();
    }

    private class PoolConnectionEventListener
    implements ConnectionEventListener {
        private PoolConnectionEventListener() {
        }

        public void connectionClosed(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection)event.getSource();
            MiniConnectionPoolManager.this.recycleConnection(pconn);
        }

        public void connectionErrorOccurred(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection)event.getSource();
            MiniConnectionPoolManager.this.disposeConnection(pconn);
        }
    }

    public static class TimeoutException
    extends RuntimeException {
        private static final long serialVersionUID = 1;

        public TimeoutException() {
            super("Timeout while waiting for a free database connection.");
        }

        public TimeoutException(String msg) {
            super(msg);
        }
    }

}

