/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.datasource;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.MiniConnectionPoolManager;
import fr.xephi.authme.settings.PlayersLogs;
import fr.xephi.authme.settings.Settings;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SQLiteThread
extends Thread
implements DataSource {
    private String database;
    private String tableName;
    private String columnName;
    private String columnPassword;
    private String columnIp;
    private String columnLastLogin;
    private String columnSalt;
    private String columnGroup;
    private String lastlocX;
    private String lastlocY;
    private String lastlocZ;
    private String lastlocWorld;
    private String columnEmail;
    private String columnID;
    private Connection con;

    @Override
    public void run() {
        this.database = Settings.getMySQLDatabase;
        this.tableName = Settings.getMySQLTablename;
        this.columnName = Settings.getMySQLColumnName;
        this.columnPassword = Settings.getMySQLColumnPassword;
        this.columnIp = Settings.getMySQLColumnIp;
        this.columnLastLogin = Settings.getMySQLColumnLastLogin;
        this.columnSalt = Settings.getMySQLColumnSalt;
        this.columnGroup = Settings.getMySQLColumnGroup;
        this.lastlocX = Settings.getMySQLlastlocX;
        this.lastlocY = Settings.getMySQLlastlocY;
        this.lastlocZ = Settings.getMySQLlastlocZ;
        this.lastlocWorld = Settings.getMySQLlastlocWorld;
        this.columnEmail = Settings.getMySQLColumnEmail;
        this.columnID = Settings.getMySQLColumnId;
        try {
            this.connect();
            this.setup();
        }
        catch (ClassNotFoundException e) {
            ConsoleLogger.showError(e.getMessage());
            if (Settings.isStopEnabled.booleanValue()) {
                ConsoleLogger.showError("Can't use SQLITE... ! SHUTDOWN...");
                AuthMe.getInstance().getServer().shutdown();
            }
            if (!Settings.isStopEnabled.booleanValue()) {
                AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
            }
            return;
        }
        catch (SQLException e) {
            ConsoleLogger.showError(e.getMessage());
            if (Settings.isStopEnabled.booleanValue()) {
                ConsoleLogger.showError("Can't use SQLITE... ! SHUTDOWN...");
                AuthMe.getInstance().getServer().shutdown();
            }
            if (!Settings.isStopEnabled.booleanValue()) {
                AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
            }
            return;
        }
    }

    private synchronized void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        ConsoleLogger.info("SQLite driver loaded");
        this.con = DriverManager.getConnection("jdbc:sqlite:plugins/AuthMe/" + this.database + ".db");
    }

    private synchronized void setup() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = this.con.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.columnID + " INTEGER AUTO_INCREMENT," + this.columnName + " VARCHAR(255) NOT NULL UNIQUE," + this.columnPassword + " VARCHAR(255) NOT NULL," + this.columnIp + " VARCHAR(40) NOT NULL," + this.columnLastLogin + " BIGINT," + this.lastlocX + " DOUBLE NOT NULL DEFAULT '0.0'," + this.lastlocY + " DOUBLE NOT NULL DEFAULT '0.0'," + this.lastlocZ + " DOUBLE NOT NULL DEFAULT '0.0'," + this.lastlocWorld + " VARCHAR(255) DEFAULT 'world'," + this.columnEmail + " VARCHAR(255) DEFAULT 'your@email.com'," + "CONSTRAINT table_const_prim PRIMARY KEY (" + this.columnID + "));");
            rs = this.con.getMetaData().getColumns(null, null, this.tableName, this.columnPassword);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnPassword + " VARCHAR(255) NOT NULL;");
            }
            rs.close();
            rs = this.con.getMetaData().getColumns(null, null, this.tableName, this.columnIp);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnIp + " VARCHAR(40) NOT NULL;");
            }
            rs.close();
            rs = this.con.getMetaData().getColumns(null, null, this.tableName, this.columnLastLogin);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnLastLogin + " BIGINT;");
            }
            rs.close();
            rs = this.con.getMetaData().getColumns(null, null, this.tableName, this.lastlocX);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.lastlocX + " DOUBLE NOT NULL DEFAULT '0.0';");
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.lastlocY + " DOUBLE NOT NULL DEFAULT '0.0';");
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.lastlocZ + " DOUBLE NOT NULL DEFAULT '0.0';");
            }
            rs.close();
            rs = this.con.getMetaData().getColumns(null, null, this.tableName, this.lastlocWorld);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.lastlocWorld + " VARCHAR(255) NOT NULL DEFAULT 'world';");
            }
            rs.close();
            rs = this.con.getMetaData().getColumns(null, null, this.tableName, this.columnEmail);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnEmail + "  VARCHAR(255) DEFAULT 'your@email.com';");
            }
            Object var4_3 = null;
            this.close(rs);
            this.close(st);
        }
        catch (Throwable var3_5) {
            Object var4_4 = null;
            this.close(rs);
            this.close(st);
            throw var3_5;
        }
        ConsoleLogger.info("SQLite Setup finished");
    }

    @Override
    public synchronized boolean isAuthAvailable(String user) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE LOWER(" + this.columnName + ")=LOWER(?);");
            pst.setString(1, user);
            rs = pst.executeQuery();
            boolean bl = rs.next();
            Object var7_6 = null;
            this.close(rs);
            this.close(pst);
            return bl;
        }
        catch (SQLException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var7_7 = null;
                this.close(rs);
                this.close(pst);
                return bl;
            }
            catch (Throwable var6_10) {
                Object var7_8 = null;
                this.close(rs);
                this.close(pst);
                throw var6_10;
            }
        }
    }

    @Override
    public synchronized PlayerAuth getAuth(String user) {
        PreparedStatement pst;
        ResultSet rs;
        block7 : {
            block9 : {
                block8 : {
                    pst = null;
                    rs = null;
                    pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE LOWER(" + this.columnName + ")=LOWER(?);");
                    pst.setString(1, user);
                    rs = pst.executeQuery();
                    if (!rs.next()) break block7;
                    if (!rs.getString(this.columnIp).isEmpty()) break block8;
                    PlayerAuth playerAuth = new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), "198.18.0.1", rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail));
                    Object var7_9 = null;
                    this.close(rs);
                    this.close(pst);
                    return playerAuth;
                }
                if (this.columnSalt.isEmpty()) break block9;
                PlayerAuth playerAuth = new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnSalt), rs.getInt(this.columnGroup), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail));
                Object var7_10 = null;
                this.close(rs);
                this.close(pst);
                return playerAuth;
            }
            PlayerAuth playerAuth = new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail));
            Object var7_11 = null;
            this.close(rs);
            this.close(pst);
            return playerAuth;
        }
        try {
            PlayerAuth playerAuth = null;
            Object var7_12 = null;
            this.close(rs);
            this.close(pst);
            return playerAuth;
        }
        catch (SQLException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                PlayerAuth playerAuth = null;
                Object var7_13 = null;
                this.close(rs);
                this.close(pst);
                return playerAuth;
            }
            catch (Throwable var6_16) {
                Object var7_14 = null;
                this.close(rs);
                this.close(pst);
                throw var6_16;
            }
        }
    }

    @Override
    public synchronized boolean saveAuth(PlayerAuth auth) {
        PreparedStatement pst = null;
        try {
            block5 : {
                try {
                    if (this.columnSalt.isEmpty() && auth.getSalt().isEmpty()) {
                        pst = this.con.prepareStatement("INSERT INTO " + this.tableName + "(" + this.columnName + "," + this.columnPassword + "," + this.columnIp + "," + this.columnLastLogin + ") VALUES (?,?,?,?);");
                        pst.setString(1, auth.getNickname());
                        pst.setString(2, auth.getHash());
                        pst.setString(3, auth.getIp());
                        pst.setLong(4, auth.getLastLogin());
                        pst.executeUpdate();
                        break block5;
                    }
                    pst = this.con.prepareStatement("INSERT INTO " + this.tableName + "(" + this.columnName + "," + this.columnPassword + "," + this.columnIp + "," + this.columnLastLogin + "," + this.columnSalt + ") VALUES (?,?,?,?,?);");
                    pst.setString(1, auth.getNickname());
                    pst.setString(2, auth.getHash());
                    pst.setString(3, auth.getIp());
                    pst.setLong(4, auth.getLastLogin());
                    pst.setString(5, auth.getSalt());
                    pst.executeUpdate();
                }
                catch (SQLException ex) {
                    ConsoleLogger.showError(ex.getMessage());
                    boolean bl = false;
                    Object var6_4 = null;
                    this.close(pst);
                    return bl;
                }
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public synchronized boolean updatePassword(PlayerAuth auth) {
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnPassword + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getHash());
                pst.setString(2, auth.getNickname());
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var6_4 = null;
                this.close(pst);
                return bl;
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public boolean updateSession(PlayerAuth auth) {
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnIp + "=?, " + this.columnLastLogin + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getIp());
                pst.setLong(2, auth.getLastLogin());
                pst.setString(3, auth.getNickname());
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var6_4 = null;
                this.close(pst);
                return bl;
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public int purgeDatabase(long until) {
        PreparedStatement pst = null;
        try {
            pst = this.con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnLastLogin + "<?;");
            pst.setLong(1, until);
            int n = pst.executeUpdate();
            Object var7_5 = null;
            this.close(pst);
            return n;
        }
        catch (SQLException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                int n = 0;
                Object var7_6 = null;
                this.close(pst);
                return n;
            }
            catch (Throwable var6_9) {
                Object var7_7 = null;
                this.close(pst);
                throw var6_9;
            }
        }
    }

    @Override
    public List<String> autoPurgeDatabase(long until) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnLastLogin + "<?;");
            pst.setLong(1, until);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(this.columnName));
            }
            ArrayList<String> arrayList = list;
            Object var9_7 = null;
            this.close(rs);
            this.close(pst);
            return arrayList;
        }
        catch (SQLException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList = new ArrayList<String>();
                Object var9_8 = null;
                this.close(rs);
                this.close(pst);
                return arrayList;
            }
            catch (Throwable var8_11) {
                Object var9_9 = null;
                this.close(rs);
                this.close(pst);
                throw var8_11;
            }
        }
    }

    @Override
    public synchronized boolean removeAuth(String user) {
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                pst.setString(1, user);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var6_4 = null;
                this.close(pst);
                return bl;
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public boolean updateQuitLoc(PlayerAuth auth) {
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("UPDATE " + this.tableName + " SET " + this.lastlocX + "=?, " + this.lastlocY + "=?, " + this.lastlocZ + "=?, " + this.lastlocWorld + "=? WHERE " + this.columnName + "=?;");
                pst.setDouble(1, auth.getQuitLocX());
                pst.setDouble(2, auth.getQuitLocY());
                pst.setDouble(3, auth.getQuitLocZ());
                pst.setString(4, auth.getWorld());
                pst.setString(5, auth.getNickname());
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var6_4 = null;
                this.close(pst);
                return bl;
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public int getIps(String ip) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int countIp = 0;
        try {
            pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnIp + "=?;");
            pst.setString(1, ip);
            rs = pst.executeQuery();
            while (rs.next()) {
                ++countIp;
            }
            int n = countIp;
            Object var8_7 = null;
            this.close(rs);
            this.close(pst);
            return n;
        }
        catch (SQLException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                int n = 0;
                Object var8_8 = null;
                this.close(rs);
                this.close(pst);
                return n;
            }
            catch (Throwable var7_11) {
                Object var8_9 = null;
                this.close(rs);
                this.close(pst);
                throw var7_11;
            }
        }
    }

    @Override
    public boolean updateEmail(PlayerAuth auth) {
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnEmail + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getEmail());
                pst.setString(2, auth.getNickname());
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var6_4 = null;
                this.close(pst);
                return bl;
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public boolean updateSalt(PlayerAuth auth) {
        if (this.columnSalt.isEmpty()) {
            return false;
        }
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnSalt + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getSalt());
                pst.setString(2, auth.getNickname());
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var6_4 = null;
                this.close(pst);
                return bl;
            }
            Object var6_3 = null;
            this.close(pst);
        }
        catch (Throwable var5_8) {
            Object var6_5 = null;
            this.close(pst);
            throw var5_8;
        }
        return true;
    }

    @Override
    public synchronized void close() {
        try {
            this.con.close();
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
        }
    }

    @Override
    public void reload() {
    }

    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<String> getAllAuthsByName(PlayerAuth auth) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> countIp = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnIp + "=?;");
                pst.setString(1, auth.getIp());
                rs = pst.executeQuery();
                while (rs.next()) {
                    countIp.add(rs.getString(this.columnName));
                }
                arrayList = countIp;
                Object var8_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var8_10 = null;
                this.close(rs);
                this.close(pst);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var8_11 = null;
                this.close(rs);
                this.close(pst);
                return arrayList3;
            }
            catch (NullPointerException npe) {
                ArrayList<String> arrayList4 = new ArrayList<String>();
                Object var8_12 = null;
                this.close(rs);
                this.close(pst);
                return arrayList4;
            }
            this.close(rs);
            this.close(pst);
            return arrayList;
        }
        catch (Throwable var7_17) {
            Object var8_13 = null;
            this.close(rs);
            this.close(pst);
            throw var7_17;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<String> getAllAuthsByIp(String ip) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> countIp = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnIp + "=?;");
                pst.setString(1, ip);
                rs = pst.executeQuery();
                while (rs.next()) {
                    countIp.add(rs.getString(this.columnName));
                }
                arrayList = countIp;
                Object var8_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var8_10 = null;
                this.close(rs);
                this.close(pst);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var8_11 = null;
                this.close(rs);
                this.close(pst);
                return arrayList3;
            }
            catch (NullPointerException npe) {
                ArrayList<String> arrayList4 = new ArrayList<String>();
                Object var8_12 = null;
                this.close(rs);
                this.close(pst);
                return arrayList4;
            }
            this.close(rs);
            this.close(pst);
            return arrayList;
        }
        catch (Throwable var7_17) {
            Object var8_13 = null;
            this.close(rs);
            this.close(pst);
            throw var7_17;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<String> getAllAuthsByEmail(String email) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> countEmail = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnEmail + "=?;");
                pst.setString(1, email);
                rs = pst.executeQuery();
                while (rs.next()) {
                    countEmail.add(rs.getString(this.columnName));
                }
                arrayList = countEmail;
                Object var8_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var8_10 = null;
                this.close(rs);
                this.close(pst);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var8_11 = null;
                this.close(rs);
                this.close(pst);
                return arrayList3;
            }
            catch (NullPointerException npe) {
                ArrayList<String> arrayList4 = new ArrayList<String>();
                Object var8_12 = null;
                this.close(rs);
                this.close(pst);
                return arrayList4;
            }
            this.close(rs);
            this.close(pst);
            return arrayList;
        }
        catch (Throwable var7_17) {
            Object var8_13 = null;
            this.close(rs);
            this.close(pst);
            throw var7_17;
        }
    }

    @Override
    public void purgeBanned(List<String> banned) {
        PreparedStatement pst = null;
        try {
            try {
                for (String name : banned) {
                    pst = this.con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                    pst.setString(1, name);
                    pst.executeUpdate();
                }
                Object var6_6 = null;
                this.close(pst);
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_7 = null;
                this.close(pst);
            }
        }
        catch (Throwable var5_9) {
            Object var6_8 = null;
            this.close(pst);
            throw var5_9;
        }
    }

    @Override
    public DataSource.DataSourceType getType() {
        return DataSource.DataSourceType.SQLITE;
    }

    @Override
    public boolean isLogged(String user) {
        return PlayersLogs.getInstance().players.contains(user);
    }

    @Override
    public void setLogged(String user) {
        PlayersLogs.getInstance().addPlayer(user);
    }

    @Override
    public void setUnlogged(String user) {
        PlayersLogs.getInstance().removePlayer(user);
    }

    @Override
    public void purgeLogged() {
        PlayersLogs.getInstance().clear();
    }

    @Override
    public int getAccountsRegistered() {
        int result;
        PreparedStatement pst;
        block6 : {
            result = 0;
            pst = null;
            ResultSet rs = null;
            try {
                pst = this.con.prepareStatement("SELECT COUNT(*) FROM " + this.tableName + ";");
                rs = pst.executeQuery();
                if (rs == null || !rs.next()) break block6;
                result = rs.getInt(1);
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                int n = result;
                Object var7_5 = null;
                this.close(pst);
                return n;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                try {
                    ConsoleLogger.showError(ex.getMessage());
                    int n = result;
                    Object var7_6 = null;
                    this.close(pst);
                    return n;
                }
                catch (Throwable var6_12) {
                    Object var7_7 = null;
                    this.close(pst);
                    throw var6_12;
                }
            }
        }
        Object var7_4 = null;
        this.close(pst);
        return result;
    }

    @Override
    public void updateName(String oldone, String newone) {
        PreparedStatement pst = null;
        try {
            try {
                pst = this.con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnName + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, newone);
                pst.setString(2, oldone);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_5 = null;
                this.close(pst);
                return;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_6 = null;
                this.close(pst);
                return;
            }
            Object var6_4 = null;
            this.close(pst);
        }
        catch (Throwable var5_10) {
            Object var6_7 = null;
            this.close(pst);
            throw var5_10;
        }
    }

    @Override
    public List<PlayerAuth> getAllAuths() {
        ArrayList<PlayerAuth> auths;
        auths = new ArrayList<PlayerAuth>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = this.con.prepareStatement("SELECT * FROM " + this.tableName + ";");
            rs = pst.executeQuery();
            while (rs.next()) {
                PlayerAuth pAuth = null;
                pAuth = rs.getString(this.columnIp).isEmpty() ? new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), "127.0.0.1", rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)) : (!this.columnSalt.isEmpty() ? new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnSalt), rs.getInt(this.columnGroup), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)) : new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)));
                if (pAuth == null) continue;
                auths.add(pAuth);
            }
            Object var7_7 = null;
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
            ArrayList<PlayerAuth> arrayList = auths;
            Object var7_8 = null;
            this.close(pst);
            return arrayList;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<PlayerAuth> arrayList = auths;
                Object var7_9 = null;
                this.close(pst);
                return arrayList;
            }
            catch (Throwable var6_13) {
                Object var7_10 = null;
                this.close(pst);
                throw var6_13;
            }
        }
        this.close(pst);
        return auths;
    }
}

