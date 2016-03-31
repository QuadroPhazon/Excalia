/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.commands;

import com.earth2me.essentials.Essentials;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.DataManager;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.SpawnTeleportEvent;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.Spawn;
import fr.xephi.authme.settings.SpoutCfg;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class AdminCommand
implements CommandExecutor {
    public AuthMe plugin;
    private Messages m = Messages.getInstance();
    private SpoutCfg s = SpoutCfg.getInstance();
    public DataSource database;

    public AdminCommand(AuthMe plugin, DataSource database) {
        this.database = database;
        this.plugin = plugin;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /authme reload - Reload the config");
            sender.sendMessage("/authme register <playername> <password> - Register a player");
            sender.sendMessage("/authme changepassword <playername> <password> - Change player password");
            sender.sendMessage("/authme unregister <playername> - Unregister a player");
            sender.sendMessage("/authme purge <days> - Purge Database");
            sender.sendMessage("/authme version - Get AuthMe version infos");
            sender.sendMessage("/authme lastlogin <playername> - Display Date about the Player's LastLogin");
            sender.sendMessage("/authme accounts <playername> - Display all player's accounts");
            sender.sendMessage("/authme setSpawn - Set AuthMe spawn to your current pos");
            sender.sendMessage("/authme spawn - Teleport you to the AuthMe SpawnPoint");
            sender.sendMessage("/authme chgemail <playername> <email> - Change player email");
            sender.sendMessage("/authme getemail <playername> - Get player email");
            sender.sendMessage("/authme purgelastpos <playername> - Purge last position for a player");
            sender.sendMessage("/authme switchantibot on/off - Enable/Disable antibot method");
            return true;
        }
        if (!this.plugin.authmePermissible(sender, "authme.admin." + args[0].toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        if (sender instanceof ConsoleCommandSender && args[0].equalsIgnoreCase("passpartuToken")) {
            if (args.length > 1) {
                System.out.println("[AuthMe] command usage: /authme passpartuToken");
                return true;
            }
            if (Utils.getInstance().obtainToken()) {
                System.out.println("[AuthMe] You have 30s for insert this token ingame with /passpartu [token]");
                return true;
            }
            System.out.println("[AuthMe] Security error on passpartu token, redo it. ");
            return true;
        }
        if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage("AuthMe Version: " + AuthMe.getInstance().getDescription().getVersion());
            return true;
        }
        if (args[0].equalsIgnoreCase("purge")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /authme purge <DAYS>");
                return true;
            }
            try {
                calendar = Calendar.getInstance();
                calendar.add(5, - Integer.parseInt(args[1]));
                until = calendar.getTimeInMillis();
                purged = this.database.autoPurgeDatabase(until);
                sender.sendMessage("Deleted " + purged.size() + " user accounts");
                if (Settings.purgeEssentialsFile.booleanValue() && this.plugin.ess != null) {
                    this.plugin.dataManager.purgeEssentials(purged);
                }
                if (Settings.purgePlayerDat.booleanValue()) {
                    this.plugin.dataManager.purgeDat(purged);
                }
                if (Settings.purgeLimitedCreative.booleanValue()) {
                    this.plugin.dataManager.purgeLimitedCreative(purged);
                }
                if (Settings.purgeAntiXray == false) return true;
                this.plugin.dataManager.purgeAntiXray(purged);
                return true;
            }
            catch (NumberFormatException e) {
                sender.sendMessage("Usage: /authme purge <DAYS>");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("reload")) {
            this.database.reload();
            newConfigFile = new File("plugins" + File.separator + "AuthMe", "config.yml");
            if (!newConfigFile.exists()) {
                fis = this.getClass().getResourceAsStream("" + File.separator + "config.yml");
                fos = null;
                try {
                    try {
                        fos = new FileOutputStream(newConfigFile);
                        buf = new byte[1024];
                        i = 0;
                        while ((i = fis.read(buf)) != -1) {
                            fos.write(buf, 0, i);
                        }
                        var11_55 = null;
                    }
                    catch (Exception e) {
                        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Failed to load config from JAR");
                        var11_56 = null;
                        try {}
                        catch (Exception e) {}
                        if (fis != null) {
                            fis.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                    catch (Exception e) {}
                }
                catch (Throwable var10_63) {
                    var11_57 = null;
                    ** try [egrp 3[TRYBLOCK] [5 : 686->709)] { 
lbl95: // 1 sources:
                    if (fis != null) {
                        fis.close();
                    }
                    if (fos == null) throw var10_63;
                    fos.close();
                    throw var10_63;
lbl100: // 1 sources:
                    catch (Exception e) {
                        // empty catch block
                    }
                    throw var10_63;
                }
            }
            newConfig = YamlConfiguration.loadConfiguration((File)newConfigFile);
            Settings.reloadConfigOptions(newConfig);
            this.m.reLoad();
            this.s.reLoad();
            this.m._(sender, "reload");
            return true;
        }
        if (args[0].equalsIgnoreCase("lastlogin")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /authme lastlogin <playername>");
                return true;
            }
            try {
                if (this.database.getAuth(args[1]) != null) {
                    player = this.database.getAuth(args[1]);
                    lastLogin = player.getLastLogin();
                    d = new Date(lastLogin);
                    diff = System.currentTimeMillis() - lastLogin;
                    msg = "" + (int)(diff / 86400000) + " days " + (int)(diff / 3600000 % 24) + " hours " + (int)(diff / 60000 % 60) + " mins " + (int)(diff / 1000 % 60) + " secs.";
                    lastIP = player.getIp();
                    sender.sendMessage("[AuthMe] " + args[1] + " lastlogin : " + d.toString());
                    sender.sendMessage("[AuthMe] The player : " + player.getNickname() + " is unlogged since " + msg);
                    sender.sendMessage("[AuthMe] LastPlayer IP : " + lastIP);
                    return true;
                }
                this.m._(sender, "unknown_user");
                return true;
            }
            catch (NullPointerException e) {
                this.m._(sender, "unknown_user");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("accounts")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /authme accounts <playername>");
                sender.sendMessage("Or: /authme accounts <ip>");
                return true;
            }
            if (!args[1].contains(".")) {
                fSender = sender;
                arguments = args;
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

                    public void run() {
                        List<String> accountList;
                        PlayerAuth pAuth = null;
                        String message = "[AuthMe] ";
                        try {
                            pAuth = AdminCommand.this.database.getAuth(arguments[1]);
                        }
                        catch (NullPointerException npe) {
                            fSender.sendMessage("[AuthMe] This player is unknown");
                            return;
                        }
                        if (pAuth != null) {
                            accountList = AdminCommand.this.database.getAllAuthsByName(pAuth);
                            if (accountList.isEmpty() || accountList == null) {
                                fSender.sendMessage("[AuthMe] This player is unknown");
                                return;
                            }
                            if (accountList.size() == 1) {
                                fSender.sendMessage("[AuthMe] " + arguments[1] + " is a single account player");
                                return;
                            }
                            int i = 0;
                            for (String account : accountList) {
                                message = message + account;
                                if (++i != accountList.size()) {
                                    message = message + ", ";
                                    continue;
                                }
                                message = message + ".";
                            }
                        } else {
                            fSender.sendMessage("[AuthMe] This player is unknown");
                            return;
                        }
                        fSender.sendMessage("[AuthMe] " + arguments[1] + " has " + String.valueOf(accountList.size()) + " accounts");
                        fSender.sendMessage(message);
                    }
                });
                return true;
            }
            fSender = sender;
            arguments = args;
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

                public void run() {
                    List<String> accountList;
                    String message = "[AuthMe] ";
                    if (arguments[1] != null) {
                        accountList = AdminCommand.this.database.getAllAuthsByIp(arguments[1]);
                        if (accountList.isEmpty() || accountList == null) {
                            fSender.sendMessage("[AuthMe] Please put a valid IP");
                            return;
                        }
                        if (accountList.size() == 1) {
                            fSender.sendMessage("[AuthMe] " + arguments[1] + " is a single account player");
                            return;
                        }
                        int i = 0;
                        for (String account : accountList) {
                            message = message + account;
                            if (++i != accountList.size()) {
                                message = message + ", ";
                                continue;
                            }
                            message = message + ".";
                        }
                    } else {
                        fSender.sendMessage("[AuthMe] Please put a valid IP");
                        return;
                    }
                    fSender.sendMessage("[AuthMe] " + arguments[1] + " has " + String.valueOf(accountList.size()) + " accounts");
                    fSender.sendMessage(message);
                }
            });
            return true;
        }
        if (args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("reg")) {
            if (args.length != 3) {
                sender.sendMessage("Usage: /authme register playername password");
                return true;
            }
            try {
                name = args[1];
                if (this.database.isAuthAvailable(name)) {
                    this.m._(sender, "user_regged");
                    return true;
                }
                hash = PasswordSecurity.getHash(Settings.getPasswordHash, args[2], name);
                auth = new PlayerAuth(name, hash, "198.18.0.1", 0, "your@email.com");
                if (PasswordSecurity.userSalt.containsKey(name) && PasswordSecurity.userSalt.get(name) != null) {
                    auth.setSalt(PasswordSecurity.userSalt.get(name));
                } else {
                    auth.setSalt("");
                }
                if (!this.database.saveAuth(auth)) {
                    this.m._(sender, "error");
                    return true;
                }
                this.m._(sender, "registered");
                ConsoleLogger.info(args[1] + " registered");
                return true;
            }
            catch (NoSuchAlgorithmException ex) {
                ConsoleLogger.showError(ex.getMessage());
                this.m._(sender, "error");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("getemail")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /authme getemail playername");
                return true;
            }
            playername = args[1];
            getAuth = this.database.getAuth(playername);
            if (getAuth == null) {
                this.m._(sender, "unknown_user");
                return true;
            }
            sender.sendMessage("[AuthMe] " + args[1] + " email : " + getAuth.getEmail());
            return true;
        }
        if (args[0].equalsIgnoreCase("chgemail")) {
            if (args.length != 3) {
                sender.sendMessage("Usage: /authme chgemail playername email");
                return true;
            }
            playername = args[1];
            getAuth = this.database.getAuth(playername);
            if (getAuth == null) {
                this.m._(sender, "unknown_user");
                return true;
            }
            getAuth.setEmail(args[2]);
            if (!this.database.updateEmail(getAuth)) {
                this.m._(sender, "error");
                return true;
            }
            if (PlayerCache.getInstance().getAuth(playername) == null) return true;
            PlayerCache.getInstance().updatePlayer(getAuth);
            return true;
        }
        if (args[0].equalsIgnoreCase("setspawn")) {
            try {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("[AuthMe] Please use that command in game");
                    return true;
                }
                if (Spawn.getInstance().setSpawn(((Player)sender).getLocation())) {
                    sender.sendMessage("[AuthMe] Correctly define new spawn");
                    return true;
                }
                sender.sendMessage("[AuthMe] SetSpawn fail , please retry");
                return true;
            }
            catch (NullPointerException ex) {
                ConsoleLogger.showError(ex.getMessage());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("setfirstspawn")) {
            try {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("[AuthMe] Please use that command in game");
                    return true;
                }
                if (Spawn.getInstance().setFirstSpawn(((Player)sender).getLocation())) {
                    sender.sendMessage("[AuthMe] Correctly define new first spawn");
                    return true;
                }
                sender.sendMessage("[AuthMe] SetFirstSpawn fail , please retry");
                return true;
            }
            catch (NullPointerException ex) {
                ConsoleLogger.showError(ex.getMessage());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("purgebannedplayers")) {
            bannedPlayers = new ArrayList<String>();
            for (OfflinePlayer off : this.plugin.getServer().getBannedPlayers()) {
                bannedPlayers.add(off.getName());
            }
            this.database.purgeBanned(bannedPlayers);
            if (Settings.purgeEssentialsFile.booleanValue() && this.plugin.ess != null) {
                this.plugin.dataManager.purgeEssentials(bannedPlayers);
            }
            if (Settings.purgePlayerDat.booleanValue()) {
                this.plugin.dataManager.purgeDat(bannedPlayers);
            }
            if (Settings.purgeLimitedCreative.booleanValue()) {
                this.plugin.dataManager.purgeLimitedCreative(bannedPlayers);
            }
            if (Settings.purgeAntiXray == false) return true;
            this.plugin.dataManager.purgeAntiXray(bannedPlayers);
            return true;
        }
        if (args[0].equalsIgnoreCase("spawn")) {
            try {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("[AuthMe] Please use that command in game");
                    return true;
                }
                if (Spawn.getInstance().getSpawn() != null) {
                    ((Player)sender).teleport(Spawn.getInstance().getSpawn());
                    return true;
                }
                sender.sendMessage("[AuthMe] Spawn fail , please try to define the spawn");
                return true;
            }
            catch (NullPointerException ex) {
                ConsoleLogger.showError(ex.getMessage());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("firstspawn")) {
            try {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("[AuthMe] Please use that command in game");
                    return true;
                }
                if (Spawn.getInstance().getFirstSpawn() != null) {
                    ((Player)sender).teleport(Spawn.getInstance().getFirstSpawn());
                    return true;
                }
                sender.sendMessage("[AuthMe] Spawn fail , please try to define the first spawn");
                return true;
            }
            catch (NullPointerException ex) {
                ConsoleLogger.showError(ex.getMessage());
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("changepassword") || args[0].equalsIgnoreCase("cp")) {
            if (args.length != 3) {
                sender.sendMessage("Usage: /authme changepassword playername newpassword");
                return true;
            }
            try {
                name = args[1];
                hash = PasswordSecurity.getHash(Settings.getPasswordHash, args[2], name);
                auth = null;
                if (PlayerCache.getInstance().isAuthenticated(name)) {
                    auth = PlayerCache.getInstance().getAuth(name);
                } else if (this.database.isAuthAvailable(name)) {
                    auth = this.database.getAuth(name);
                }
                if (auth == null) {
                    this.m._(sender, "unknown_user");
                    return true;
                }
                auth.setHash(hash);
                if (PasswordSecurity.userSalt.containsKey(name)) {
                    auth.setSalt(PasswordSecurity.userSalt.get(name));
                    this.database.updateSalt(auth);
                }
                if (!this.database.updatePassword(auth)) {
                    this.m._(sender, "error");
                    return true;
                }
                sender.sendMessage("pwd_changed");
                ConsoleLogger.info(args[1] + "'s password changed");
                return true;
            }
            catch (NoSuchAlgorithmException ex) {
                ConsoleLogger.showError(ex.getMessage());
                this.m._(sender, "error");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("unregister") || args[0].equalsIgnoreCase("unreg") || args[0].equalsIgnoreCase("del")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /authme unregister playername");
                return true;
            }
            name = args[1];
            if (!this.database.isAuthAvailable(name)) {
                this.m._(sender, "user_unknown");
                return true;
            }
            if (!this.database.removeAuth(name)) {
                this.m._(sender, "error");
                return true;
            }
            target = Bukkit.getPlayer((String)name);
            PlayerCache.getInstance().removePlayer(name);
            Utils.getInstance().setGroup(name, Utils.groupType.UNREGISTERED);
            if (target != null && target.isOnline()) {
                if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.noTeleport.booleanValue()) {
                    spawn = this.plugin.getSpawnLocation(target);
                    tpEvent = new SpawnTeleportEvent(target, target.getLocation(), spawn, false);
                    this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                    if (!tpEvent.isCancelled()) {
                        target.teleport(tpEvent.getTo());
                    }
                }
                LimboCache.getInstance().addLimboPlayer(target);
                delay = Settings.getRegistrationTimeout * 20;
                interval = Settings.getWarnMessageInterval;
                sched = sender.getServer().getScheduler();
                if (delay != 0) {
                    id = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new TimeoutTask(this.plugin, name), (long)delay);
                    LimboCache.getInstance().getLimboPlayer(name).setTimeoutTaskId(id);
                }
                LimboCache.getInstance().getLimboPlayer(name).setMessageTaskId(sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, name, this.m._("reg_msg"), interval)));
                if (Settings.applyBlindEffect.booleanValue()) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Settings.getRegistrationTimeout * 20, 2));
                }
                this.m._((CommandSender)target, "unregistered");
            }
            this.m._(sender, "unregistered");
            ConsoleLogger.info(args[1] + " unregistered");
            return true;
        }
        if (args[0].equalsIgnoreCase("purgelastpos")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /authme purgelastpos playername");
                return true;
            }
            try {
                name = args[1];
                auth = this.database.getAuth(name);
                if (auth == null) {
                    sender.sendMessage("The player " + name + " is not registered ");
                    return true;
                }
                auth.setQuitLocX(0.0);
                auth.setQuitLocY(0.0);
                auth.setQuitLocZ(0.0);
                auth.setWorld("world");
                this.database.updateQuitLoc(auth);
                sender.sendMessage(name + " 's last pos location is now reset");
                return true;
            }
            catch (Exception e) {
                ConsoleLogger.showError("An error occured while trying to reset location or player do not exist, please see below: ");
                ConsoleLogger.showError(e.getMessage());
                if (sender instanceof Player == false) return true;
                sender.sendMessage("An error occured while trying to reset location or player do not exist, please see logs");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("switchantibot")) {
            if (args.length != 2) {
                sender.sendMessage("Usage : /authme switchantibot on/off");
                return true;
            }
            if (args[1].equalsIgnoreCase("on")) {
                this.plugin.switchAntiBotMod(true);
                sender.sendMessage("[AuthMe] AntiBotMod enabled");
                return true;
            }
            if (args[1].equalsIgnoreCase("off")) {
                this.plugin.switchAntiBotMod(false);
                sender.sendMessage("[AuthMe] AntiBotMod disabled");
                return true;
            }
            sender.sendMessage("Usage : /authme switchantibot on/off");
            return true;
        }
        if (args[0].equalsIgnoreCase("getip")) {
            if (args.length < 2) {
                sender.sendMessage("Usage : /authme getip onlinePlayerName");
                return true;
            }
            if (Bukkit.getPlayer((String)args[1]) != null) {
                player = Bukkit.getPlayer((String)args[1]);
                sender.sendMessage(player.getName() + " actual ip is : " + player.getAddress().getAddress().getHostAddress() + ":" + player.getAddress().getPort());
                sender.sendMessage(player.getName() + " real ip is : " + this.plugin.getIP(player));
                return true;
            }
            sender.sendMessage("This player is not actually online");
            sender.sendMessage("Usage : /authme getip onlinePlayerName");
            return true;
        }
        if (!args[0].equalsIgnoreCase("resetposition")) {
            sender.sendMessage("Usage: /authme reload|register playername password|changepassword playername password|unregister playername");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("Usage : /authme resetPosition <playerName>");
            return true;
        }
        auth = this.database.getAuth(args[1]);
        if (auth == null) {
            this.m._(sender, "unknown_user");
            return true;
        }
        auth.setQuitLocX(0.0);
        auth.setQuitLocY(0.0);
        auth.setQuitLocZ(0.0);
        auth.setWorld("world");
        this.database.updateQuitLoc(auth);
        sender.sendMessage("[AuthMe] Successfully reset position for " + auth.getNickname());
        return true;
    }

}

