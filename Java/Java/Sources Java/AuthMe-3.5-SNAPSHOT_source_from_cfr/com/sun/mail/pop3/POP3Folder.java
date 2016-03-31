/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.pop3;

import com.sun.mail.pop3.DefaultFolder;
import com.sun.mail.pop3.POP3Message;
import com.sun.mail.pop3.POP3Store;
import com.sun.mail.pop3.Protocol;
import com.sun.mail.pop3.Status;
import com.sun.mail.pop3.TempFile;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailLogger;
import java.io.EOFException;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.MethodNotSupportedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

public class POP3Folder
extends Folder {
    private String name;
    private POP3Store store;
    private volatile Protocol port;
    private int total;
    private int size;
    private boolean exists = false;
    private volatile boolean opened = false;
    private Vector message_cache;
    private boolean doneUidl = false;
    private volatile TempFile fileCache = null;
    MailLogger logger;

    POP3Folder(POP3Store store, String name) {
        super(store);
        this.name = name;
        this.store = store;
        if (name.equalsIgnoreCase("INBOX")) {
            this.exists = true;
        }
        this.logger = new MailLogger(this.getClass(), "DEBUG POP3", store.getSession());
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.name;
    }

    public Folder getParent() {
        return new DefaultFolder(this.store);
    }

    public boolean exists() {
        return this.exists;
    }

    public Folder[] list(String pattern) throws MessagingException {
        throw new MessagingException("not a directory");
    }

    public char getSeparator() {
        return '\u0000';
    }

    public int getType() {
        return 1;
    }

    public boolean create(int type) throws MessagingException {
        return false;
    }

    public boolean hasNewMessages() throws MessagingException {
        return false;
    }

    public Folder getFolder(String name) throws MessagingException {
        throw new MessagingException("not a directory");
    }

    public boolean delete(boolean recurse) throws MessagingException {
        throw new MethodNotSupportedException("delete");
    }

    public boolean renameTo(Folder f) throws MessagingException {
        throw new MethodNotSupportedException("renameTo");
    }

    public synchronized void open(int mode) throws MessagingException {
        this.checkClosed();
        if (!this.exists) {
            throw new FolderNotFoundException(this, "folder is not INBOX");
        }
        try {
            this.port = this.store.getPort(this);
            Status s = this.port.stat();
            this.total = s.total;
            this.size = s.size;
            this.mode = mode;
            if (this.store.useFileCache) {
                try {
                    this.fileCache = new TempFile(this.store.fileCacheDir);
                }
                catch (IOException ex) {
                    this.logger.log(Level.FINE, "failed to create file cache", ex);
                    throw ex;
                }
            }
            this.opened = true;
        }
        catch (IOException ioex) {
            try {
                if (this.port != null) {
                    this.port.quit();
                }
            }
            catch (IOException ioex2) {}
            finally {
                this.port = null;
                this.store.closePort(this);
            }
            throw new MessagingException("Open failed", ioex);
        }
        this.message_cache = new Vector(this.total);
        this.message_cache.setSize(this.total);
        this.doneUidl = false;
        this.notifyConnectionListeners(1);
    }

    public synchronized void close(boolean expunge) throws MessagingException {
        this.checkOpen();
        try {
            int i;
            POP3Message m;
            if (this.store.rsetBeforeQuit) {
                this.port.rset();
            }
            if (expunge && this.mode == 2) {
                for (i = 0; i < this.message_cache.size(); ++i) {
                    m = (POP3Message)this.message_cache.elementAt(i);
                    if (m == null || !m.isSet(Flags.Flag.DELETED)) continue;
                    try {
                        this.port.dele(i + 1);
                        continue;
                    }
                    catch (IOException ioex) {
                        throw new MessagingException("Exception deleting messages during close", ioex);
                    }
                }
            }
            for (i = 0; i < this.message_cache.size(); ++i) {
                m = (POP3Message)this.message_cache.elementAt(i);
                if (m == null) continue;
                m.invalidate(true);
            }
            this.port.quit();
        }
        catch (IOException ex) {}
        finally {
            this.port = null;
            this.store.closePort(this);
            this.message_cache = null;
            this.opened = false;
            this.notifyConnectionListeners(3);
            if (this.fileCache != null) {
                this.fileCache.close();
                this.fileCache = null;
            }
        }
    }

    public synchronized boolean isOpen() {
        if (!this.opened) {
            return false;
        }
        try {
            if (!this.port.noop()) {
                throw new IOException("NOOP failed");
            }
        }
        catch (IOException ioex) {
            try {
                this.close(false);
            }
            catch (MessagingException mex) {}
            finally {
                return false;
            }
        }
        return true;
    }

    public Flags getPermanentFlags() {
        return new Flags();
    }

    public synchronized int getMessageCount() throws MessagingException {
        if (!this.opened) {
            return -1;
        }
        this.checkReadable();
        return this.total;
    }

    public synchronized Message getMessage(int msgno) throws MessagingException {
        this.checkOpen();
        POP3Message m = (POP3Message)this.message_cache.elementAt(msgno - 1);
        if (m == null) {
            m = this.createMessage(this, msgno);
            this.message_cache.setElementAt(m, msgno - 1);
        }
        return m;
    }

    protected POP3Message createMessage(Folder f, int msgno) throws MessagingException {
        POP3Message m = null;
        Constructor cons = this.store.messageConstructor;
        if (cons != null) {
            try {
                Object[] o = new Object[]{this, new Integer(msgno)};
                m = (POP3Message)cons.newInstance(o);
            }
            catch (Exception ex) {
                // empty catch block
            }
        }
        if (m == null) {
            m = new POP3Message(this, msgno);
        }
        return m;
    }

    public void appendMessages(Message[] msgs) throws MessagingException {
        throw new MethodNotSupportedException("Append not supported");
    }

    public Message[] expunge() throws MessagingException {
        throw new MethodNotSupportedException("Expunge not supported");
    }

    public synchronized void fetch(Message[] msgs, FetchProfile fp) throws MessagingException {
        this.checkReadable();
        if (!this.doneUidl && this.store.supportsUidl && fp.contains(UIDFolder.FetchProfileItem.UID)) {
            String[] uids = new String[this.message_cache.size()];
            try {
                if (!this.port.uidl(uids)) {
                    return;
                }
            }
            catch (EOFException eex) {
                this.close(false);
                throw new FolderClosedException(this, eex.toString());
            }
            catch (IOException ex) {
                throw new MessagingException("error getting UIDL", ex);
            }
            for (int i = 0; i < uids.length; ++i) {
                if (uids[i] == null) continue;
                POP3Message m = (POP3Message)this.getMessage(i + 1);
                m.uid = uids[i];
            }
            this.doneUidl = true;
        }
        if (fp.contains(FetchProfile.Item.ENVELOPE)) {
            for (int i = 0; i < msgs.length; ++i) {
                try {
                    POP3Message msg = (POP3Message)msgs[i];
                    msg.getHeader("");
                    msg.getSize();
                    continue;
                }
                catch (MessageRemovedException mex) {
                    // empty catch block
                }
            }
        }
    }

    public synchronized String getUID(Message msg) throws MessagingException {
        this.checkOpen();
        POP3Message m = (POP3Message)msg;
        try {
            if (!this.store.supportsUidl) {
                return null;
            }
            if (m.uid == "UNKNOWN") {
                m.uid = this.port.uidl(m.getMessageNumber());
            }
            return m.uid;
        }
        catch (EOFException eex) {
            this.close(false);
            throw new FolderClosedException(this, eex.toString());
        }
        catch (IOException ex) {
            throw new MessagingException("error getting UIDL", ex);
        }
    }

    public synchronized int getSize() throws MessagingException {
        this.checkOpen();
        return this.size;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public synchronized int[] getSizes() throws MessagingException {
        String line;
        this.checkOpen();
        int[] sizes = new int[this.total];
        InputStream is = null;
        FilterInputStream lis = null;
        is = this.port.list();
        lis = new LineInputStream(is);
        while ((line = lis.readLine()) != null) {
            try {
                StringTokenizer st = new StringTokenizer(line);
                int msgnum = Integer.parseInt(st.nextToken());
                int size = Integer.parseInt(st.nextToken());
                if (msgnum <= 0 || msgnum > this.total) continue;
                sizes[msgnum - 1] = size;
            }
            catch (Exception e) {}
        }
        try {
            if (lis != null) {
                lis.close();
            }
        }
        catch (IOException cex) {
            // empty catch block
        }
        try {
            if (is == null) return sizes;
            is.close();
            return sizes;
        }
        catch (IOException cex) {
            return sizes;
        }
        catch (IOException ex) {
            try {
                if (lis != null) {
                    lis.close();
                }
            }
            catch (IOException cex) {
                // empty catch block
            }
            try {
                if (is == null) return sizes;
                is.close();
                return sizes;
            }
            catch (IOException cex) {
                return sizes;
            }
            catch (Throwable throwable) {
                try {
                    if (lis != null) {
                        lis.close();
                    }
                }
                catch (IOException cex) {
                    // empty catch block
                }
                try {
                    if (is == null) throw throwable;
                    is.close();
                    throw throwable;
                }
                catch (IOException cex) {
                    // empty catch block
                }
                throw throwable;
            }
        }
    }

    public synchronized InputStream listCommand() throws MessagingException, IOException {
        this.checkOpen();
        return this.port.list();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.close(false);
    }

    private void checkOpen() throws IllegalStateException {
        if (!this.opened) {
            throw new IllegalStateException("Folder is not Open");
        }
    }

    private void checkClosed() throws IllegalStateException {
        if (this.opened) {
            throw new IllegalStateException("Folder is Open");
        }
    }

    private void checkReadable() throws IllegalStateException {
        if (!this.opened || this.mode != 1 && this.mode != 2) {
            throw new IllegalStateException("Folder is not Readable");
        }
    }

    Protocol getProtocol() throws MessagingException {
        Protocol p = this.port;
        this.checkOpen();
        return p;
    }

    protected void notifyMessageChangedListeners(int type, Message m) {
        super.notifyMessageChangedListeners(type, m);
    }

    TempFile getFileCache() {
        return this.fileCache;
    }
}

