/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  de.luricos.bukkit.xAuth.database.DatabaseController
 *  de.luricos.bukkit.xAuth.database.Table
 *  de.luricos.bukkit.xAuth.utils.xAuthLog
 *  de.luricos.bukkit.xAuth.xAuth
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.converter;

import de.luricos.bukkit.xAuth.database.DatabaseController;
import de.luricos.bukkit.xAuth.database.Table;
import de.luricos.bukkit.xAuth.utils.xAuthLog;
import de.luricos.bukkit.xAuth.xAuth;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class newxAuthToFlat {
    public AuthMe instance;
    public DataSource database;
    public CommandSender sender;

    public newxAuthToFlat(AuthMe instance, DataSource database, CommandSender sender) {
        this.instance = instance;
        this.database = database;
        this.sender = sender;
    }

    public boolean convert() {
        List<Integer> players;
        if (this.instance.getServer().getPluginManager().getPlugin("xAuth") == null) {
            this.sender.sendMessage("[AuthMe] xAuth plugin not found");
            return false;
        }
        if (!new File(this.instance.getDataFolder().getParent() + File.separator + "xAuth" + File.separator + "xAuth.h2.db").exists()) {
            this.sender.sendMessage("[AuthMe] xAuth H2 database not found, checking for MySQL or SQLite data...");
        }
        if ((players = this.getXAuthPlayers()) == null || players.isEmpty()) {
            this.sender.sendMessage("[AuthMe] Error while import xAuthPlayers");
            return false;
        }
        this.sender.sendMessage("[AuthMe] Starting import...");
        try {
            Iterator<Integer> i$ = players.iterator();
            while (i$.hasNext()) {
                int id = i$.next();
                String pl = this.getIdPlayer(id);
                String psw = this.getPassword(id);
                if (psw == null || psw.isEmpty() || pl == null) continue;
                PlayerAuth auth = new PlayerAuth(pl, psw, "198.18.0.1", 0, "your@email.com");
                this.database.saveAuth(auth);
            }
            this.sender.sendMessage("[AuthMe] Successfull convert from xAuth database");
        }
        catch (Exception e) {
            this.sender.sendMessage("[AuthMe] An error has been thrown while import xAuth database, the import hadn't fail but can be not complete ");
        }
        return true;
    }

    public String getIdPlayer(int id) {
        String realPass = "";
        Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = String.format("SELECT `playername` FROM `%s` WHERE `id` = ?", xAuth.getPlugin().getDatabaseController().getTable(Table.ACCOUNT));
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (!rs.next()) {
                String string = null;
                return string;
            }
            realPass = rs.getString("playername");
        }
        catch (SQLException e) {
            xAuthLog.severe((String)("Failed to retrieve name for account: " + id), (Throwable)e);
            String string = null;
            return string;
        }
        finally {
            xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
        }
        return realPass;
    }

    public List<Integer> getXAuthPlayers() {
        ArrayList<Integer> xP;
        xP = new ArrayList<Integer>();
        Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = String.format("SELECT * FROM `%s`", xAuth.getPlugin().getDatabaseController().getTable(Table.ACCOUNT));
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                xP.add(rs.getInt("id"));
            }
        }
        catch (SQLException e) {
            xAuthLog.severe((String)"Cannot import xAuthPlayers", (Throwable)e);
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            return arrayList;
        }
        finally {
            xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
        }
        return xP;
    }

    public String getPassword(int accountId) {
        String realPass = "";
        Connection conn = xAuth.getPlugin().getDatabaseController().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = String.format("SELECT `password`, `pwtype` FROM `%s` WHERE `id` = ?", xAuth.getPlugin().getDatabaseController().getTable(Table.ACCOUNT));
            ps = conn.prepareStatement(sql);
            ps.setInt(1, accountId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                String string = null;
                return string;
            }
            realPass = rs.getString("password");
        }
        catch (SQLException e) {
            xAuthLog.severe((String)("Failed to retrieve password hash for account: " + accountId), (Throwable)e);
            String string = null;
            return string;
        }
        finally {
            xAuth.getPlugin().getDatabaseController().close(conn, ps, rs);
        }
        return realPass;
    }
}

