/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package fr.xephi.authme.commands;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.converter.Converter;
import fr.xephi.authme.converter.CrazyLoginConverter;
import fr.xephi.authme.converter.FlatToSql;
import fr.xephi.authme.converter.FlatToSqlite;
import fr.xephi.authme.converter.RakamakConverter;
import fr.xephi.authme.converter.RoyalAuthConverter;
import fr.xephi.authme.converter.SqlToFlat;
import fr.xephi.authme.converter.vAuthConverter;
import fr.xephi.authme.converter.xAuthConverter;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ConverterCommand
implements CommandExecutor {
    private AuthMe plugin;
    private Messages m = Messages.getInstance();
    private DataSource database;

    public ConverterCommand(AuthMe plugin, DataSource database) {
        this.plugin = plugin;
        this.database = database;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!this.plugin.authmePermissible(sender, "authme.admin.converter")) {
            this.m._(sender, "no_perm");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("Usage : /converter flattosql | flattosqlite | xauth | crazylogin | rakamak | royalauth | vauth | sqltoflat");
            return true;
        }
        ConvertType type = ConvertType.fromName(args[0]);
        if (type == null) {
            this.m._(sender, "error");
            return true;
        }
        Converter converter = null;
        switch (type) {
            case ftsql: {
                converter = new FlatToSql();
                break;
            }
            case ftsqlite: {
                converter = new FlatToSqlite(sender);
                break;
            }
            case xauth: {
                converter = new xAuthConverter(this.plugin, this.database, sender);
                break;
            }
            case crazylogin: {
                converter = new CrazyLoginConverter(this.plugin, this.database, sender);
                break;
            }
            case rakamak: {
                converter = new RakamakConverter(this.plugin, this.database, sender);
                break;
            }
            case royalauth: {
                converter = new RoyalAuthConverter(this.plugin);
                break;
            }
            case vauth: {
                converter = new vAuthConverter(this.plugin, this.database, sender);
                break;
            }
            case sqltoflat: {
                converter = new SqlToFlat(this.plugin, this.database, sender);
                break;
            }
        }
        if (converter == null) {
            this.m._(sender, "error");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, (Runnable)converter);
        sender.sendMessage("[AuthMe] Successfully converted from " + args[0]);
        return true;
    }

    public static enum ConvertType {
        ftsql("flattosql"),
        ftsqlite("flattosqlite"),
        xauth("xauth"),
        crazylogin("crazylogin"),
        rakamak("rakamak"),
        royalauth("royalauth"),
        vauth("vauth"),
        sqltoflat("sqltoflat");
        
        String name;

        private ConvertType(String name) {
            this.name = name;
        }

        String getName() {
            return this.name;
        }

        public static ConvertType fromName(String name) {
            for (ConvertType type : ConvertType.values()) {
                if (!type.getName().equalsIgnoreCase(name)) continue;
                return type;
            }
            return null;
        }
    }

}

