/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource
 *  org.bukkit.Server
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.datasource;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.MiniConnectionPoolManager;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.settings.Settings;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.ConnectionPoolDataSource;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class MySQLThread
extends Thread
implements DataSource {
    private String host;
    private String port;
    private String username;
    private String password;
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
    private String columnLogged;
    private List<String> columnOthers;
    private MiniConnectionPoolManager conPool;

    @Override
    public void run() {
        this.host = Settings.getMySQLHost;
        this.port = Settings.getMySQLPort;
        this.username = Settings.getMySQLUsername;
        this.password = Settings.getMySQLPassword;
        this.database = Settings.getMySQLDatabase;
        this.tableName = Settings.getMySQLTablename;
        this.columnName = Settings.getMySQLColumnName;
        this.columnPassword = Settings.getMySQLColumnPassword;
        this.columnIp = Settings.getMySQLColumnIp;
        this.columnLastLogin = Settings.getMySQLColumnLastLogin;
        this.lastlocX = Settings.getMySQLlastlocX;
        this.lastlocY = Settings.getMySQLlastlocY;
        this.lastlocZ = Settings.getMySQLlastlocZ;
        this.lastlocWorld = Settings.getMySQLlastlocWorld;
        this.columnSalt = Settings.getMySQLColumnSalt;
        this.columnGroup = Settings.getMySQLColumnGroup;
        this.columnEmail = Settings.getMySQLColumnEmail;
        this.columnOthers = Settings.getMySQLOtherUsernameColumn;
        this.columnID = Settings.getMySQLColumnId;
        this.columnLogged = Settings.getMySQLColumnLogged;
        try {
            this.connect();
            this.setup();
        }
        catch (ClassNotFoundException e) {
            ConsoleLogger.showError(e.getMessage());
            if (Settings.isStopEnabled.booleanValue()) {
                ConsoleLogger.showError("Can't use MySQL... Please input correct MySQL informations ! SHUTDOWN...");
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
                ConsoleLogger.showError("Can't use MySQL... Please input correct MySQL informations ! SHUTDOWN...");
                AuthMe.getInstance().getServer().shutdown();
            }
            if (!Settings.isStopEnabled.booleanValue()) {
                AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
            }
            return;
        }
        catch (MiniConnectionPoolManager.TimeoutException e) {
            ConsoleLogger.showError(e.getMessage());
            if (Settings.isStopEnabled.booleanValue()) {
                ConsoleLogger.showError("Can't use MySQL... Please input correct MySQL informations ! SHUTDOWN...");
                AuthMe.getInstance().getServer().shutdown();
            }
            if (!Settings.isStopEnabled.booleanValue()) {
                AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
            }
            return;
        }
    }

    private synchronized void connect() throws ClassNotFoundException, SQLException, MiniConnectionPoolManager.TimeoutException {
        Class.forName("com.mysql.jdbc.Driver");
        ConsoleLogger.info("MySQL driver loaded");
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName(this.database);
        dataSource.setServerName(this.host);
        dataSource.setPort(Integer.parseInt(this.port));
        dataSource.setUser(this.username);
        dataSource.setPassword(this.password);
        this.conPool = new MiniConnectionPoolManager((ConnectionPoolDataSource)dataSource, 10);
        ConsoleLogger.info("Connection pool ready");
    }

    private synchronized void setup() throws SQLException {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = this.makeSureConnectionIsReady();
            st = con.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.columnID + " INTEGER AUTO_INCREMENT," + this.columnName + " VARCHAR(255) NOT NULL UNIQUE," + this.columnPassword + " VARCHAR(255) NOT NULL," + this.columnIp + " VARCHAR(40) NOT NULL DEFAULT '127.0.0.1'," + this.columnLastLogin + " BIGINT NOT NULL DEFAULT '" + System.currentTimeMillis() + "'," + this.lastlocX + " DOUBLE NOT NULL DEFAULT '0.0'," + this.lastlocY + " DOUBLE NOT NULL DEFAULT '0.0'," + this.lastlocZ + " DOUBLE NOT NULL DEFAULT '0.0'," + this.lastlocWorld + " VARCHAR(255) DEFAULT 'world'," + this.columnEmail + " VARCHAR(255) DEFAULT 'your@email.com'," + this.columnLogged + " SMALLINT NOT NULL DEFAULT '0'," + "CONSTRAINT table_const_prim PRIMARY KEY (" + this.columnID + "));");
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.columnPassword);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnPassword + " VARCHAR(255) NOT NULL;");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.columnIp);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnIp + " VARCHAR(40) NOT NULL;");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.columnLastLogin);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnLastLogin + " BIGINT;");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.lastlocX);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.lastlocX + " DOUBLE NOT NULL DEFAULT '0.0' AFTER " + this.columnLastLogin + " , ADD " + this.lastlocY + " DOUBLE NOT NULL DEFAULT '0.0' AFTER " + this.lastlocX + " , ADD " + this.lastlocZ + " DOUBLE NOT NULL DEFAULT '0.0' AFTER " + this.lastlocY + ";");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.lastlocWorld);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.lastlocWorld + " VARCHAR(255) NOT NULL DEFAULT 'world' AFTER " + this.lastlocZ + ";");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.columnEmail);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnEmail + " VARCHAR(255) DEFAULT 'your@email.com' AFTER " + this.lastlocWorld + ";");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.columnLogged);
            if (!rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " ADD COLUMN " + this.columnLogged + " SMALLINT NOT NULL DEFAULT '0' AFTER " + this.columnEmail + ";");
            }
            rs.close();
            rs = con.getMetaData().getColumns(null, null, this.tableName, this.lastlocX);
            if (rs.next()) {
                st.executeUpdate("ALTER TABLE " + this.tableName + " MODIFY " + this.lastlocX + " DOUBLE NOT NULL DEFAULT '0.0', MODIFY " + this.lastlocY + " DOUBLE NOT NULL DEFAULT '0.0', MODIFY " + this.lastlocZ + " DOUBLE NOT NULL DEFAULT '0.0';");
            }
            Object var5_4 = null;
            this.close(rs);
            this.close(st);
            this.close(con);
        }
        catch (Throwable var4_6) {
            Object var5_5 = null;
            this.close(rs);
            this.close(st);
            this.close(con);
            throw var4_6;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized boolean isAuthAvailable(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            boolean bl;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE LOWER(" + this.columnName + ")=LOWER(?);");
                pst.setString(1, user);
                rs = pst.executeQuery();
                bl = rs.next();
                Object var8_8 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl2 = false;
                Object var8_9 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return bl2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl3 = false;
                Object var8_10 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return bl3;
            }
            this.close(rs);
            this.close(pst);
            this.close(con);
            return bl;
        }
        catch (Throwable throwable) {
            Object var8_11 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized PlayerAuth getAuth(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        PlayerAuth pAuth = null;
        int id = -1;
        try {
            block8 : {
                PlayerAuth blob2;
                try {
                    con = this.makeSureConnectionIsReady();
                    pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE LOWER(" + this.columnName + ")=LOWER(?);");
                    pst.setString(1, user);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        id = rs.getInt(this.columnID);
                        pAuth = rs.getString(this.columnIp).isEmpty() && rs.getString(this.columnIp) != null ? new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), "198.18.0.1", rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)) : (!this.columnSalt.isEmpty() ? (!this.columnGroup.isEmpty() ? new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnSalt), rs.getInt(this.columnGroup), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)) : new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnSalt), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail))) : new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)));
                        if (Settings.getPasswordHash == HashAlgorithm.XENFORO) {
                            rs.close();
                            pst = con.prepareStatement("SELECT * FROM xf_user_authenticate WHERE " + this.columnID + "=?;");
                            pst.setInt(1, id);
                            rs = pst.executeQuery();
                            if (rs.next()) {
                                Blob blob2 = rs.getBlob("data");
                                byte[] bytes = blob2.getBytes(1, (int)blob2.length());
                                pAuth.setHash(new String(bytes));
                            }
                        }
                        break block8;
                    }
                    blob2 = null;
                    Object var10_14 = null;
                }
                catch (SQLException ex) {
                    ConsoleLogger.showError(ex.getMessage());
                    PlayerAuth bytes = null;
                    Object var10_16 = null;
                    this.close(rs);
                    this.close(pst);
                    this.close(con);
                    return bytes;
                }
                catch (MiniConnectionPoolManager.TimeoutException ex) {
                    ConsoleLogger.showError(ex.getMessage());
                    PlayerAuth bytes = null;
                    Object var10_17 = null;
                    this.close(rs);
                    this.close(pst);
                    this.close(con);
                    return bytes;
                }
                this.close(rs);
                this.close(pst);
                this.close(con);
                return blob2;
            }
            Object var10_15 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            return pAuth;
        }
        catch (Throwable var9_19) {
            Object var10_18 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw var9_19;
        }
    }

    @Override
    public synchronized boolean saveAuth(PlayerAuth auth) {
        PreparedStatement pst;
        Connection con;
        block14 : {
            con = null;
            pst = null;
            try {
                ResultSet rs;
                con = this.makeSureConnectionIsReady();
                if (this.columnSalt == null || this.columnSalt.isEmpty() || auth.getSalt() == null || auth.getSalt().isEmpty()) {
                    pst = con.prepareStatement("INSERT INTO " + this.tableName + "(" + this.columnName + "," + this.columnPassword + "," + this.columnIp + "," + this.columnLastLogin + ") VALUES (?,?,?,?);");
                    pst.setString(1, auth.getNickname());
                    pst.setString(2, auth.getHash());
                    pst.setString(3, auth.getIp());
                    pst.setLong(4, auth.getLastLogin());
                    pst.executeUpdate();
                } else {
                    pst = con.prepareStatement("INSERT INTO " + this.tableName + "(" + this.columnName + "," + this.columnPassword + "," + this.columnIp + "," + this.columnLastLogin + "," + this.columnSalt + ") VALUES (?,?,?,?,?);");
                    pst.setString(1, auth.getNickname());
                    pst.setString(2, auth.getHash());
                    pst.setString(3, auth.getIp());
                    pst.setLong(4, auth.getLastLogin());
                    pst.setString(5, auth.getSalt());
                    pst.executeUpdate();
                }
                if (!this.columnOthers.isEmpty()) {
                    for (String column : this.columnOthers) {
                        pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + column + "=? WHERE " + this.columnName + "=?;");
                        pst.setString(1, auth.getNickname());
                        pst.setString(2, auth.getNickname());
                        pst.executeUpdate();
                    }
                }
                if (Settings.getPasswordHash == HashAlgorithm.PHPBB) {
                    rs = null;
                    pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                    pst.setString(1, auth.getNickname());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt(this.columnID);
                        pst = con.prepareStatement("INSERT INTO " + Settings.getPhpbbPrefix + "user_group (group_id, user_id, group_leader, user_pending) VALUES (?,?,?,?);");
                        pst.setInt(1, Settings.getPhpbbGroup);
                        pst.setInt(2, id);
                        pst.setInt(3, 0);
                        pst.setInt(4, 0);
                        pst.executeUpdate();
                        pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.tableName + ".group_id=? WHERE " + this.columnName + "=?;");
                        pst.setInt(1, Settings.getPhpbbGroup);
                        pst.setString(2, auth.getNickname());
                        pst.executeUpdate();
                        long time = System.currentTimeMillis() / 1000;
                        pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.tableName + ".user_regdate=? WHERE " + this.columnName + "=?;");
                        pst.setLong(1, time);
                        pst.setString(2, auth.getNickname());
                        pst.executeUpdate();
                        pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.tableName + ".user_lastvisit=? WHERE " + this.columnName + "=?;");
                        pst.setLong(1, time);
                        pst.setString(2, auth.getNickname());
                        pst.executeUpdate();
                    }
                }
                if (Settings.getPasswordHash == HashAlgorithm.WORDPRESS) {
                    rs = null;
                    pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                    pst.setString(1, auth.getNickname());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt(this.columnID);
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "first_name");
                        pst.setString(3, "");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "last_name");
                        pst.setString(3, "");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "nickname");
                        pst.setString(3, auth.getNickname());
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "description");
                        pst.setString(3, "");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "rich_editing");
                        pst.setString(3, "true");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "comment_shortcuts");
                        pst.setString(3, "false");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "admin_color");
                        pst.setString(3, "fresh");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "use_ssl");
                        pst.setString(3, "0");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "show_admin_bar_front");
                        pst.setString(3, "true");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "wp_capabilities");
                        pst.setString(3, "a:1:{s:10:\"subscriber\";b:1;}");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "wp_user_level");
                        pst.setString(3, "0");
                        pst.executeUpdate();
                        pst = con.prepareStatement("INSERT INTO " + Settings.getWordPressPrefix + "usermeta (user_id, meta_key, meta_value) VALUES (?,?,?);");
                        pst.setInt(1, id);
                        pst.setString(2, "default_password_nag");
                        pst.setString(3, "");
                        pst.executeUpdate();
                    }
                }
                if (Settings.getPasswordHash != HashAlgorithm.XENFORO) break block14;
                rs = null;
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getNickname());
                rs = pst.executeQuery();
                if (!rs.next()) break block14;
                int id = rs.getInt(this.columnID);
                pst = con.prepareStatement("INSERT INTO xf_user_authenticate (user_id, scheme_class, data) VALUES (?,?,?);");
                pst.setInt(1, id);
                pst.setString(2, "XenForo_Authentication_Core12");
                byte[] bytes = auth.getHash().getBytes();
                Blob blob = con.createBlob();
                blob.setBytes(1, bytes);
                pst.setBlob(3, blob);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean rs = false;
                Object var9_17 = null;
                this.close(pst);
                this.close(con);
                return rs;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                try {
                    ConsoleLogger.showError(ex.getMessage());
                    boolean rs = false;
                    Object var9_18 = null;
                    this.close(pst);
                    this.close(con);
                    return rs;
                }
                catch (Throwable var8_20) {
                    Object var9_19 = null;
                    this.close(pst);
                    this.close(con);
                    throw var8_20;
                }
            }
        }
        Object var9_16 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    @Override
    public synchronized boolean updatePassword(PlayerAuth auth) {
        PreparedStatement pst;
        Connection con;
        block6 : {
            con = null;
            pst = null;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnPassword + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getHash());
                pst.setString(2, auth.getNickname());
                pst.executeUpdate();
                if (Settings.getPasswordHash != HashAlgorithm.XENFORO) break block6;
                ResultSet rs = null;
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                pst.setString(1, auth.getNickname());
                rs = pst.executeQuery();
                if (!rs.next()) break block6;
                int id = rs.getInt(this.columnID);
                pst = con.prepareStatement("UPDATE xf_user_authenticate SET data=? WHERE " + this.columnID + "=?;");
                byte[] bytes = auth.getHash().getBytes();
                Blob blob = con.createBlob();
                blob.setBytes(1, bytes);
                pst.setBlob(1, blob);
                pst.setInt(2, id);
                pst.executeUpdate();
                pst = con.prepareStatement("UPDATE xf_user_authenticate SET scheme_class=? WHERE " + this.columnID + "=?;");
                pst.setString(1, "XenForo_Authentication_Core12");
                pst.setInt(2, id);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                boolean rs = false;
                Object var9_13 = null;
                this.close(pst);
                this.close(con);
                return rs;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                try {
                    ConsoleLogger.showError(ex.getMessage());
                    boolean rs = false;
                    Object var9_14 = null;
                    this.close(pst);
                    this.close(con);
                    return rs;
                }
                catch (Throwable var8_16) {
                    Object var9_15 = null;
                    this.close(pst);
                    this.close(con);
                    throw var8_16;
                }
            }
        }
        Object var9_12 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    @Override
    public synchronized boolean updateSession(PlayerAuth auth) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.makeSureConnectionIsReady();
            pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnIp + "=?, " + this.columnLastLogin + "=? WHERE " + this.columnName + "=?;");
            pst.setString(1, auth.getIp());
            pst.setLong(2, auth.getLastLogin());
            pst.setString(3, auth.getNickname());
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
            boolean bl = false;
            Object var7_5 = null;
            this.close(pst);
            this.close(con);
            return bl;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var7_6 = null;
                this.close(pst);
                this.close(con);
                return bl;
            }
            catch (Throwable var6_12) {
                Object var7_7 = null;
                this.close(pst);
                this.close(con);
                throw var6_12;
            }
        }
        Object var7_4 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized int purgeDatabase(long until) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            int n;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnLastLogin + "<?;");
                pst.setLong(1, until);
                n = pst.executeUpdate();
                Object var8_7 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                int n2 = 0;
                Object var8_8 = null;
                this.close(pst);
                this.close(con);
                return n2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                int n3 = 0;
                Object var8_9 = null;
                this.close(pst);
                this.close(con);
                return n3;
            }
            this.close(pst);
            this.close(con);
            return n;
        }
        catch (Throwable throwable) {
            Object var8_10 = null;
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized List<String> autoPurgeDatabase(long until) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnLastLogin + "<?;");
                pst.setLong(1, until);
                rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString(this.columnName));
                }
                pst = con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnLastLogin + "<?;");
                pst.setLong(1, until);
                pst.executeUpdate();
                arrayList = list;
                Object var10_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var10_10 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var10_11 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList3;
            }
            this.close(rs);
            this.close(pst);
            this.close(con);
            return arrayList;
        }
        catch (Throwable throwable) {
            Object var10_12 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    @Override
    public synchronized boolean removeAuth(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.makeSureConnectionIsReady();
            if (Settings.getPasswordHash == HashAlgorithm.XENFORO) {
                ResultSet rs = null;
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                pst.setString(1, user);
                rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(this.columnID);
                    pst = con.prepareStatement("DELETE FROM xf_user_authenticate WHERE " + this.columnID + "=?;");
                    pst.setInt(1, id);
                }
            }
            pst = con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
            pst.setString(1, user);
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
            boolean rs = false;
            Object var7_11 = null;
            this.close(pst);
            this.close(con);
            return rs;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                boolean rs = false;
                Object var7_12 = null;
                this.close(pst);
                this.close(con);
                return rs;
            }
            catch (Throwable var6_14) {
                Object var7_13 = null;
                this.close(pst);
                this.close(con);
                throw var6_14;
            }
        }
        Object var7_10 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    @Override
    public synchronized boolean updateQuitLoc(PlayerAuth auth) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.makeSureConnectionIsReady();
            pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.lastlocX + " =?, " + this.lastlocY + "=?, " + this.lastlocZ + "=?, " + this.lastlocWorld + "=? WHERE " + this.columnName + "=?;");
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
            Object var7_5 = null;
            this.close(pst);
            this.close(con);
            return bl;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var7_6 = null;
                this.close(pst);
                this.close(con);
                return bl;
            }
            catch (Throwable var6_12) {
                Object var7_7 = null;
                this.close(pst);
                this.close(con);
                throw var6_12;
            }
        }
        Object var7_4 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized int getIps(String ip) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int countIp = 0;
        try {
            int n;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnIp + "=?;");
                pst.setString(1, ip);
                rs = pst.executeQuery();
                while (rs.next()) {
                    ++countIp;
                }
                n = countIp;
                Object var9_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                int n2 = 0;
                Object var9_10 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return n2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                int n3 = 0;
                Object var9_11 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return n3;
            }
            this.close(rs);
            this.close(pst);
            this.close(con);
            return n;
        }
        catch (Throwable throwable) {
            Object var9_12 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    @Override
    public synchronized boolean updateEmail(PlayerAuth auth) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.makeSureConnectionIsReady();
            pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnEmail + " =? WHERE " + this.columnName + "=?;");
            pst.setString(1, auth.getEmail());
            pst.setString(2, auth.getNickname());
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
            boolean bl = false;
            Object var7_5 = null;
            this.close(pst);
            this.close(con);
            return bl;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var7_6 = null;
                this.close(pst);
                this.close(con);
                return bl;
            }
            catch (Throwable var6_12) {
                Object var7_7 = null;
                this.close(pst);
                this.close(con);
                throw var6_12;
            }
        }
        Object var7_4 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    @Override
    public synchronized boolean updateSalt(PlayerAuth auth) {
        if (this.columnSalt.isEmpty()) {
            return false;
        }
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.makeSureConnectionIsReady();
            pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnSalt + " =? WHERE " + this.columnName + "=?;");
            pst.setString(1, auth.getSalt());
            pst.setString(2, auth.getNickname());
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
            boolean bl = false;
            Object var7_5 = null;
            this.close(pst);
            this.close(con);
            return bl;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                boolean bl = false;
                Object var7_6 = null;
                this.close(pst);
                this.close(con);
                return bl;
            }
            catch (Throwable var6_12) {
                Object var7_7 = null;
                this.close(pst);
                this.close(con);
                throw var6_12;
            }
        }
        Object var7_4 = null;
        this.close(pst);
        this.close(con);
        return true;
    }

    @Override
    public synchronized void close() {
        try {
            this.conPool.dispose();
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
        }
    }

    @Override
    public void reload() {
        block3 : {
            try {
                this.reconnect(true);
            }
            catch (Exception e) {
                ConsoleLogger.showError(e.getMessage());
                if (Settings.isStopEnabled.booleanValue()) {
                    ConsoleLogger.showError("Can't reconnect to MySQL database... Please check your MySQL informations ! SHUTDOWN...");
                    AuthMe.getInstance().getServer().shutdown();
                }
                if (Settings.isStopEnabled.booleanValue()) break block3;
                AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
            }
        }
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

    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
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
    public synchronized List<String> getAllAuthsByName(PlayerAuth auth) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> countIp = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnIp + "=?;");
                pst.setString(1, auth.getIp());
                rs = pst.executeQuery();
                while (rs.next()) {
                    countIp.add(rs.getString(this.columnName));
                }
                arrayList = countIp;
                Object var9_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var9_10 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var9_11 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList3;
            }
            this.close(rs);
            this.close(pst);
            this.close(con);
            return arrayList;
        }
        catch (Throwable throwable) {
            Object var9_12 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized List<String> getAllAuthsByIp(String ip) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> countIp = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnIp + "=?;");
                pst.setString(1, ip);
                rs = pst.executeQuery();
                while (rs.next()) {
                    countIp.add(rs.getString(this.columnName));
                }
                arrayList = countIp;
                Object var9_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var9_10 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var9_11 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList3;
            }
            this.close(rs);
            this.close(pst);
            this.close(con);
            return arrayList;
        }
        catch (Throwable throwable) {
            Object var9_12 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public synchronized List<String> getAllAuthsByEmail(String email) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<String> countEmail = new ArrayList<String>();
        try {
            ArrayList<String> arrayList;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnEmail + "=?;");
                pst.setString(1, email);
                rs = pst.executeQuery();
                while (rs.next()) {
                    countEmail.add(rs.getString(this.columnName));
                }
                arrayList = countEmail;
                Object var9_9 = null;
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList2 = new ArrayList<String>();
                Object var9_10 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList2;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<String> arrayList3 = new ArrayList<String>();
                Object var9_11 = null;
                this.close(rs);
                this.close(pst);
                this.close(con);
                return arrayList3;
            }
            this.close(rs);
            this.close(pst);
            this.close(con);
            return arrayList;
        }
        catch (Throwable throwable) {
            Object var9_12 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw throwable;
        }
    }

    @Override
    public synchronized void purgeBanned(List<String> banned) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            try {
                for (String name : banned) {
                    con = this.makeSureConnectionIsReady();
                    pst = con.prepareStatement("DELETE FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                    pst.setString(1, name);
                    pst.executeUpdate();
                }
                Object var7_7 = null;
                this.close(pst);
                this.close(con);
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var7_8 = null;
                this.close(pst);
                this.close(con);
            }
        }
        catch (Throwable var6_10) {
            Object var7_9 = null;
            this.close(pst);
            this.close(con);
            throw var6_10;
        }
    }

    private synchronized Connection makeSureConnectionIsReady() {
        Connection con;
        block12 : {
            con = null;
            try {
                con = this.conPool.getValidConnection();
            }
            catch (Exception te) {
                try {
                    con = null;
                    this.reconnect(false);
                }
                catch (Exception e) {
                    ConsoleLogger.showError(e.getMessage());
                    if (Settings.isStopEnabled.booleanValue()) {
                        ConsoleLogger.showError("Can't reconnect to MySQL database... Please check your MySQL informations ! SHUTDOWN...");
                        AuthMe.getInstance().getServer().shutdown();
                    }
                    if (!Settings.isStopEnabled.booleanValue()) {
                        AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
                    }
                }
            }
            catch (AssertionError ae) {
                if (!ae.getMessage().equalsIgnoreCase("AuthMeDatabaseError")) {
                    throw new AssertionError((Object)ae.getMessage());
                }
                try {
                    con = null;
                    this.reconnect(false);
                }
                catch (Exception e) {
                    ConsoleLogger.showError(e.getMessage());
                    if (Settings.isStopEnabled.booleanValue()) {
                        ConsoleLogger.showError("Can't reconnect to MySQL database... Please check your MySQL informations ! SHUTDOWN...");
                        AuthMe.getInstance().getServer().shutdown();
                    }
                    if (Settings.isStopEnabled.booleanValue()) break block12;
                    AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
                }
            }
        }
        if (con == null) {
            con = this.conPool.getValidConnection();
        }
        return con;
    }

    private synchronized void reconnect(boolean reload) throws ClassNotFoundException, SQLException, MiniConnectionPoolManager.TimeoutException {
        this.conPool.dispose();
        Class.forName("com.mysql.jdbc.Driver");
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setDatabaseName(this.database);
        dataSource.setServerName(this.host);
        dataSource.setPort(Integer.parseInt(this.port));
        dataSource.setUser(this.username);
        dataSource.setPassword(this.password);
        this.conPool = new MiniConnectionPoolManager((ConnectionPoolDataSource)dataSource, 10);
        if (!reload) {
            ConsoleLogger.info("ConnectionPool was unavailable... Reconnected!");
        }
    }

    @Override
    public DataSource.DataSourceType getType() {
        return DataSource.DataSourceType.MYSQL;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isLogged(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            block5 : {
                boolean bl;
                try {
                    con = this.makeSureConnectionIsReady();
                    pst = con.prepareStatement("SELECT * FROM " + this.tableName + " WHERE " + this.columnName + "=?;");
                    pst.setString(1, user);
                    rs = pst.executeQuery();
                    if (!rs.next()) break block5;
                    bl = rs.getInt(this.columnLogged) == 1;
                    Object var8_8 = null;
                }
                catch (SQLException ex) {
                    ConsoleLogger.showError(ex.getMessage());
                    boolean bl2 = false;
                    Object var8_10 = null;
                    this.close(rs);
                    this.close(pst);
                    this.close(con);
                    return bl2;
                }
                catch (MiniConnectionPoolManager.TimeoutException ex) {
                    ConsoleLogger.showError(ex.getMessage());
                    boolean bl3 = false;
                    Object var8_11 = null;
                    this.close(rs);
                    this.close(pst);
                    this.close(con);
                    return bl3;
                }
                this.close(rs);
                this.close(pst);
                this.close(con);
                return bl;
            }
            Object var8_9 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            return false;
        }
        catch (Throwable var7_15) {
            Object var8_12 = null;
            this.close(rs);
            this.close(pst);
            this.close(con);
            throw var7_15;
        }
    }

    @Override
    public void setLogged(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnLogged + "=? WHERE " + this.columnName + "=?;");
                pst.setInt(1, 1);
                pst.setString(2, user);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_5 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_6 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            Object var6_4 = null;
            this.close(pst);
            this.close(con);
        }
        catch (Throwable var5_10) {
            Object var6_7 = null;
            this.close(pst);
            this.close(con);
            throw var5_10;
        }
    }

    @Override
    public void setUnlogged(String user) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnLogged + "=? WHERE " + this.columnName + "=?;");
                pst.setInt(1, 0);
                pst.setString(2, user);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_5 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var6_6 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            Object var6_4 = null;
            this.close(pst);
            this.close(con);
        }
        catch (Throwable var5_10) {
            Object var6_7 = null;
            this.close(pst);
            this.close(con);
            throw var5_10;
        }
    }

    @Override
    public void purgeLogged() {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnLogged + "=? WHERE " + this.columnLogged + "=?;");
                pst.setInt(1, 0);
                pst.setInt(2, 1);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var5_4 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var5_5 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            Object var5_3 = null;
            this.close(pst);
            this.close(con);
        }
        catch (Throwable var4_9) {
            Object var5_6 = null;
            this.close(pst);
            this.close(con);
            throw var4_9;
        }
    }

    @Override
    public int getAccountsRegistered() {
        int result;
        PreparedStatement pst;
        Connection con;
        block6 : {
            result = 0;
            con = null;
            pst = null;
            ResultSet rs = null;
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("SELECT COUNT(*) FROM " + this.tableName + ";");
                rs = pst.executeQuery();
                if (rs == null || !rs.next()) break block6;
                result = rs.getInt(1);
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                int n = result;
                Object var8_6 = null;
                this.close(pst);
                this.close(con);
                return n;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                try {
                    ConsoleLogger.showError(ex.getMessage());
                    int n = result;
                    Object var8_7 = null;
                    this.close(pst);
                    this.close(con);
                    return n;
                }
                catch (Throwable var7_13) {
                    Object var8_8 = null;
                    this.close(pst);
                    this.close(con);
                    throw var7_13;
                }
            }
        }
        Object var8_5 = null;
        this.close(pst);
        this.close(con);
        return result;
    }

    @Override
    public void updateName(String oldone, String newone) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            try {
                con = this.makeSureConnectionIsReady();
                pst = con.prepareStatement("UPDATE " + this.tableName + " SET " + this.columnName + "=? WHERE " + this.columnName + "=?;");
                pst.setString(1, newone);
                pst.setString(2, oldone);
                pst.executeUpdate();
            }
            catch (SQLException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var7_6 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            catch (MiniConnectionPoolManager.TimeoutException ex) {
                ConsoleLogger.showError(ex.getMessage());
                Object var7_7 = null;
                this.close(pst);
                this.close(con);
                return;
            }
            Object var7_5 = null;
            this.close(pst);
            this.close(con);
        }
        catch (Throwable var6_11) {
            Object var7_8 = null;
            this.close(pst);
            this.close(con);
            throw var6_11;
        }
    }

    @Override
    public List<PlayerAuth> getAllAuths() {
        ArrayList<PlayerAuth> auths;
        auths = new ArrayList<PlayerAuth>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.makeSureConnectionIsReady();
            pst = con.prepareStatement("SELECT * FROM " + this.tableName + ";");
            rs = pst.executeQuery();
            while (rs.next()) {
                PlayerAuth pAuth = null;
                int id = rs.getInt(this.columnID);
                pAuth = rs.getString(this.columnIp).isEmpty() && rs.getString(this.columnIp) != null ? new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), "198.18.0.1", rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)) : (!this.columnSalt.isEmpty() ? (!this.columnGroup.isEmpty() ? new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnSalt), rs.getInt(this.columnGroup), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)) : new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnSalt), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail))) : new PlayerAuth(rs.getString(this.columnName), rs.getString(this.columnPassword), rs.getString(this.columnIp), rs.getLong(this.columnLastLogin), rs.getDouble(this.lastlocX), rs.getDouble(this.lastlocY), rs.getDouble(this.lastlocZ), rs.getString(this.lastlocWorld), rs.getString(this.columnEmail)));
                if (Settings.getPasswordHash == HashAlgorithm.XENFORO) {
                    rs.close();
                    pst = con.prepareStatement("SELECT * FROM xf_user_authenticate WHERE " + this.columnID + "=?;");
                    pst.setInt(1, id);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        Blob blob = rs.getBlob("data");
                        byte[] bytes = blob.getBytes(1, (int)blob.length());
                        pAuth.setHash(new String(bytes));
                    }
                }
                if (pAuth == null) continue;
                auths.add(pAuth);
            }
            Object var10_13 = null;
        }
        catch (SQLException ex) {
            ConsoleLogger.showError(ex.getMessage());
            ArrayList<PlayerAuth> id = auths;
            Object var10_14 = null;
            this.close(pst);
            this.close(con);
            return id;
        }
        catch (MiniConnectionPoolManager.TimeoutException ex) {
            try {
                ConsoleLogger.showError(ex.getMessage());
                ArrayList<PlayerAuth> id = auths;
                Object var10_15 = null;
                this.close(pst);
                this.close(con);
                return id;
            }
            catch (Throwable var9_17) {
                Object var10_16 = null;
                this.close(pst);
                this.close(con);
                throw var9_17;
            }
        }
        this.close(pst);
        this.close(con);
        return auths;
    }
}

