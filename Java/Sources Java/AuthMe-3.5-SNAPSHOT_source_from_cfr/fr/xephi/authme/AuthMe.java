/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.Acrobot.ChestShop.ChestShop
 *  com.earth2me.essentials.Essentials
 *  com.onarandombox.MultiverseCore.MultiverseCore
 *  com.onarandombox.MultiverseCore.api.MVWorldManager
 *  com.onarandombox.MultiverseCore.api.MultiverseWorld
 *  me.muizers.Notifications.Notifications
 *  net.citizensnpcs.Citizens
 *  net.milkbowl.vault.permission.Permission
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.core.Filter
 *  org.apache.logging.log4j.core.Logger
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicesManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.bukkit.plugin.messaging.PluginMessageListenerRegistration
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme;

import com.Acrobot.ChestShop.ChestShop;
import com.earth2me.essentials.Essentials;
import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import fr.xephi.authme.ConsoleFilter;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.DataManager;
import fr.xephi.authme.Log4JFilter;
import fr.xephi.authme.PerformBackup;
import fr.xephi.authme.SendMailSSL;
import fr.xephi.authme.Utils;
import fr.xephi.authme.api.API;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.commands.AdminCommand;
import fr.xephi.authme.commands.CaptchaCommand;
import fr.xephi.authme.commands.ChangePasswordCommand;
import fr.xephi.authme.commands.ConverterCommand;
import fr.xephi.authme.commands.EmailCommand;
import fr.xephi.authme.commands.LoginCommand;
import fr.xephi.authme.commands.LogoutCommand;
import fr.xephi.authme.commands.PasspartuCommand;
import fr.xephi.authme.commands.RegisterCommand;
import fr.xephi.authme.commands.UnregisterCommand;
import fr.xephi.authme.datasource.CacheDataSource;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.datasource.FlatFileThread;
import fr.xephi.authme.datasource.MySQLThread;
import fr.xephi.authme.datasource.SQLiteThread;
import fr.xephi.authme.listener.AuthMeBlockListener;
import fr.xephi.authme.listener.AuthMeChestShopListener;
import fr.xephi.authme.listener.AuthMeEntityListener;
import fr.xephi.authme.listener.AuthMePlayerListener;
import fr.xephi.authme.listener.AuthMeServerListener;
import fr.xephi.authme.listener.AuthMeSpoutListener;
import fr.xephi.authme.plugin.manager.BungeeCordMessage;
import fr.xephi.authme.plugin.manager.CitizensCommunicator;
import fr.xephi.authme.plugin.manager.CombatTagComunicator;
import fr.xephi.authme.plugin.manager.EssSpawn;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.OtherAccounts;
import fr.xephi.authme.settings.PlayersLogs;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.Spawn;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Filter;
import java.util.zip.GZIPInputStream;
import me.muizers.Notifications.Notifications;
import net.citizensnpcs.Citizens;
import net.milkbowl.vault.permission.Permission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;
import org.bukkit.scheduler.BukkitScheduler;

public class AuthMe
extends JavaPlugin {
    public DataSource database = null;
    private Settings settings;
    private Messages m;
    public PlayersLogs pllog;
    public OtherAccounts otherAccounts;
    public static Server server;
    public static java.util.logging.Logger authmeLogger;
    public static AuthMe authme;
    public Permission permission;
    private Utils utils = Utils.getInstance();
    private JavaPlugin plugin;
    private FileCache playerBackup;
    public CitizensCommunicator citizens;
    public SendMailSSL mail;
    public int CitizensVersion;
    public int CombatTag;
    public double ChestShop;
    public boolean BungeeCord;
    public Essentials ess;
    public Notifications notifications;
    public API api;
    public Management management;
    public HashMap<String, Integer> captcha;
    public HashMap<String, String> cap;
    public HashMap<String, String> realIp;
    public MultiverseCore multiverse;
    public Location essentialsSpawn;
    public Thread databaseThread;
    public LookupService ls;
    public boolean antibotMod;
    public boolean delayedAntiBot;
    protected static String vgUrl;
    public DataManager dataManager;

    public AuthMe() {
        this.playerBackup = new FileCache(this);
        this.mail = null;
        this.CitizensVersion = 0;
        this.CombatTag = 0;
        this.ChestShop = 0.0;
        this.BungeeCord = false;
        this.captcha = new HashMap();
        this.cap = new HashMap();
        this.realIp = new HashMap();
        this.multiverse = null;
        this.databaseThread = null;
        this.ls = null;
        this.antibotMod = false;
        this.delayedAntiBot = true;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public void onEnable() {
        authme = this;
        authmeLogger.setParent(this.getLogger());
        this.settings = new Settings(this);
        this.settings.loadConfigOptions();
        this.citizens = new CitizensCommunicator(this);
        if (Settings.enableAntiBot.booleanValue()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this, new Runnable(){

                public void run() {
                    AuthMe.this.delayedAntiBot = false;
                }
            }, 2400);
        }
        this.m = Messages.getInstance();
        this.pllog = PlayersLogs.getInstance();
        this.otherAccounts = OtherAccounts.getInstance();
        server = this.getServer();
        this.checkVault();
        if (Settings.removePassword.booleanValue()) {
            this.getLogger().setFilter(new ConsoleFilter());
            Bukkit.getLogger().setFilter(new ConsoleFilter());
            java.util.logging.Logger.getLogger("Minecraft").setFilter(new ConsoleFilter());
            authmeLogger.setFilter(new ConsoleFilter());
            try {
                Class.forName("org.apache.logging.log4j.core.Filter");
                this.setLog4JFilter();
            }
            catch (ClassNotFoundException e) {
                ConsoleLogger.info("You're using Minecraft 1.6.x or older, Log4J support is disabled");
            }
            catch (NoClassDefFoundError e) {
                ConsoleLogger.info("You're using Minecraft 1.6.x or older, Log4J support is disabled");
            }
        }
        if (!Settings.getmailAccount.isEmpty() && !Settings.getmailPassword.isEmpty()) {
            this.mail = new SendMailSSL(this);
        }
        this.citizensVersion();
        this.combatTag();
        this.checkNotifications();
        this.checkMultiverse();
        this.checkChestShop();
        this.checkEssentials();
        if (Settings.isBackupActivated.booleanValue() && Settings.isBackupOnStart.booleanValue()) {
            Boolean Backup = new PerformBackup(this).DoBackup();
            if (Backup.booleanValue()) {
                ConsoleLogger.info("Backup Complete");
            } else {
                ConsoleLogger.showError("Error while making Backup");
            }
        }
        switch (Settings.getDataSource) {
            case FILE: {
                FlatFileThread fileThread = new FlatFileThread();
                fileThread.start();
                this.database = fileThread;
                this.databaseThread = fileThread;
                int a = this.database.getAccountsRegistered();
                if (a < 1000) break;
                ConsoleLogger.showError("YOU'RE USING FILE DATABASE WITH " + a + "+ ACCOUNTS, FOR BETTER PERFORMANCES, PLEASE USE MYSQL!!");
                break;
            }
            case MYSQL: {
                MySQLThread sqlThread = new MySQLThread();
                sqlThread.start();
                this.database = sqlThread;
                this.databaseThread = sqlThread;
                break;
            }
            case SQLITE: {
                SQLiteThread sqliteThread = new SQLiteThread();
                sqliteThread.start();
                this.database = sqliteThread;
                this.databaseThread = sqliteThread;
                int b = this.database.getAccountsRegistered();
                if (b < 2000) break;
                ConsoleLogger.showError("YOU'RE USING SQLITE DATABASE WITH " + b + "+ ACCOUNTS, FOR BETTER PERFORMANCES, PLEASE USE MYSQL!!");
            }
        }
        if (Settings.isCachingEnabled.booleanValue()) {
            this.database = new CacheDataSource(this, this.database);
        }
        this.dataManager = new DataManager(this, this.database);
        this.dataManager.start();
        this.api = new API(this, this.database);
        this.management = new Management(this.database, this);
        this.management.start();
        PluginManager pm = this.getServer().getPluginManager();
        if (Settings.bungee.booleanValue()) {
            Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
            Bukkit.getMessenger().registerIncomingPluginChannel((Plugin)this, "BungeeCord", (PluginMessageListener)new BungeeCordMessage(this));
        }
        if (pm.isPluginEnabled("Spout")) {
            pm.registerEvents((Listener)new AuthMeSpoutListener(this.database), (Plugin)this);
            ConsoleLogger.info("Successfully hook with Spout!");
        }
        pm.registerEvents((Listener)new AuthMePlayerListener(this, this.database), (Plugin)this);
        pm.registerEvents((Listener)new AuthMeBlockListener(this.database, this), (Plugin)this);
        pm.registerEvents((Listener)new AuthMeEntityListener(this.database, this), (Plugin)this);
        pm.registerEvents((Listener)new AuthMeServerListener(this), (Plugin)this);
        if (this.ChestShop != 0.0) {
            pm.registerEvents((Listener)new AuthMeChestShopListener(this.database, this), (Plugin)this);
            ConsoleLogger.info("Successfully hook with ChestShop!");
        }
        this.getCommand("authme").setExecutor((CommandExecutor)new AdminCommand(this, this.database));
        this.getCommand("register").setExecutor((CommandExecutor)new RegisterCommand(this));
        this.getCommand("login").setExecutor((CommandExecutor)new LoginCommand(this));
        this.getCommand("changepassword").setExecutor((CommandExecutor)new ChangePasswordCommand(this.database, this));
        this.getCommand("logout").setExecutor((CommandExecutor)new LogoutCommand(this, this.database));
        this.getCommand("unregister").setExecutor((CommandExecutor)new UnregisterCommand(this, this.database));
        this.getCommand("passpartu").setExecutor((CommandExecutor)new PasspartuCommand(this));
        this.getCommand("email").setExecutor((CommandExecutor)new EmailCommand(this, this.database));
        this.getCommand("captcha").setExecutor((CommandExecutor)new CaptchaCommand(this));
        this.getCommand("converter").setExecutor((CommandExecutor)new ConverterCommand(this, this.database));
        if (!Settings.isForceSingleSessionEnabled.booleanValue()) {
            ConsoleLogger.showError("ATTENTION by disabling ForceSingleSession, your server protection is set to low");
        }
        if (Settings.reloadSupport.booleanValue()) {
            try {
                this.onReload();
                if (server.getOnlinePlayers().length < 1) {
                    try {
                        this.database.purgeLogged();
                    }
                    catch (NullPointerException npe) {}
                }
            }
            catch (NullPointerException ex) {
                // empty catch block
            }
        }
        if (Settings.usePurge.booleanValue()) {
            this.autoPurge();
        }
        this.downloadGeoIp();
        this.recallEmail();
        ConsoleLogger.info("[SPONSOR] AuthMe is sponsored and hook perfectly with server hosting VERYGAMES, rent your server for only 1.99$/months");
        ConsoleLogger.info("[SPONSOR] Look Minecraft and other offers on www.verygames.net ! ");
        ConsoleLogger.info("Authme " + this.getDescription().getVersion() + " enabled");
    }

    private void setLog4JFilter() {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this, new Runnable(){

            public void run() {
                Logger coreLogger = (Logger)LogManager.getRootLogger();
                coreLogger.addFilter((org.apache.logging.log4j.core.Filter)new Log4JFilter());
            }
        });
    }

    public void checkVault() {
        if (this.getServer().getPluginManager().getPlugin("Vault") != null && this.getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            RegisteredServiceProvider permissionProvider = this.getServer().getServicesManager().getRegistration((Class)Permission.class);
            if (permissionProvider != null) {
                this.permission = (Permission)permissionProvider.getProvider();
                ConsoleLogger.info("Vault plugin detected, hook with " + this.permission.getName() + " system");
            } else {
                ConsoleLogger.showError("Vault plugin is detected but not the permissions plugin!");
            }
        } else {
            this.permission = null;
        }
    }

    public void checkChestShop() {
        if (!Settings.chestshop.booleanValue()) {
            this.ChestShop = 0.0;
            return;
        }
        if (this.getServer().getPluginManager().getPlugin("ChestShop") != null && this.getServer().getPluginManager().getPlugin("ChestShop").isEnabled()) {
            try {
                String ver = ChestShop.getVersion();
                try {
                    double version = Double.valueOf(ver.split(" ")[0]);
                    if (version >= 3.5) {
                        this.ChestShop = version;
                    } else {
                        ConsoleLogger.showError("Please Update your ChestShop version!");
                    }
                }
                catch (NumberFormatException nfe) {
                    try {
                        double version = Double.valueOf(ver.split("t")[0]);
                        if (version >= 3.5) {
                            this.ChestShop = version;
                        } else {
                            ConsoleLogger.showError("Please Update your ChestShop version!");
                        }
                    }
                    catch (NumberFormatException nfee) {
                    }
                }
            }
            catch (Exception e) {}
        } else {
            this.ChestShop = 0.0;
        }
    }

    public void checkMultiverse() {
        if (!Settings.multiverse.booleanValue()) {
            this.multiverse = null;
            return;
        }
        if (this.getServer().getPluginManager().getPlugin("Multiverse-Core") != null && this.getServer().getPluginManager().getPlugin("Multiverse-Core").isEnabled()) {
            try {
                this.multiverse = (MultiverseCore)this.getServer().getPluginManager().getPlugin("Multiverse-Core");
                ConsoleLogger.info("Hook with Multiverse-Core for SpawnLocations");
            }
            catch (NullPointerException npe) {
                this.multiverse = null;
            }
            catch (ClassCastException cce) {
                this.multiverse = null;
            }
            catch (NoClassDefFoundError ncdfe) {
                this.multiverse = null;
            }
        } else {
            this.multiverse = null;
        }
    }

    public void checkEssentials() {
        if (this.getServer().getPluginManager().getPlugin("Essentials") != null && this.getServer().getPluginManager().getPlugin("Essentials").isEnabled()) {
            try {
                this.ess = (Essentials)this.getServer().getPluginManager().getPlugin("Essentials");
                ConsoleLogger.info("Hook with Essentials plugin");
            }
            catch (NullPointerException npe) {
                this.ess = null;
            }
            catch (ClassCastException cce) {
                this.ess = null;
            }
            catch (NoClassDefFoundError ncdfe) {
                this.ess = null;
            }
        } else {
            this.ess = null;
        }
        if (this.getServer().getPluginManager().getPlugin("EssentialsSpawn") != null && this.getServer().getPluginManager().getPlugin("EssentialsSpawn").isEnabled()) {
            try {
                this.essentialsSpawn = new EssSpawn().getLocation();
                ConsoleLogger.info("Hook with EssentialsSpawn plugin");
            }
            catch (Exception e) {
                this.essentialsSpawn = null;
                ConsoleLogger.showError("Error while reading /plugins/Essentials/spawn.yml file ");
            }
        } else {
            this.essentialsSpawn = null;
        }
    }

    public void checkNotifications() {
        if (!Settings.notifications.booleanValue()) {
            this.notifications = null;
            return;
        }
        if (this.getServer().getPluginManager().getPlugin("Notifications") != null && this.getServer().getPluginManager().getPlugin("Notifications").isEnabled()) {
            this.notifications = (Notifications)this.getServer().getPluginManager().getPlugin("Notifications");
            ConsoleLogger.info("Successfully hook with Notifications");
        } else {
            this.notifications = null;
        }
    }

    public void combatTag() {
        this.CombatTag = this.getServer().getPluginManager().getPlugin("CombatTag") != null && this.getServer().getPluginManager().getPlugin("CombatTag").isEnabled() ? 1 : 0;
    }

    public void citizensVersion() {
        String ver;
        String[] args;
        Citizens cit;
        this.CitizensVersion = this.getServer().getPluginManager().getPlugin("Citizens") != null && this.getServer().getPluginManager().getPlugin("Citizens").isEnabled() ? ((args = (ver = (cit = (Citizens)this.getServer().getPluginManager().getPlugin("Citizens")).getDescription().getVersion()).split("\\."))[0].contains("1") ? 1 : 2) : 0;
    }

    public void onDisable() {
        if (Bukkit.getOnlinePlayers().length != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                this.savePlayer(player);
            }
        }
        if (this.database != null) {
            this.database.close();
        }
        if (this.databaseThread != null && this.databaseThread.isAlive()) {
            this.databaseThread.interrupt();
        }
        if (this.dataManager != null && this.dataManager.isAlive()) {
            this.dataManager.interrupt();
        }
        if (Settings.isBackupActivated.booleanValue() && Settings.isBackupOnStop.booleanValue()) {
            Boolean Backup = new PerformBackup(this).DoBackup();
            if (Backup.booleanValue()) {
                ConsoleLogger.info("Backup Complete");
            } else {
                ConsoleLogger.showError("Error while making Backup");
            }
        }
        ConsoleLogger.info("Authme " + this.getDescription().getVersion() + " disabled");
    }

    private void onReload() {
        try {
            if (Bukkit.getServer().getOnlinePlayers() != null) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (!this.database.isLogged(player.getName())) continue;
                    String name = player.getName();
                    PlayerAuth pAuth = this.database.getAuth(name);
                    if (pAuth == null) break;
                    PlayerAuth auth = new PlayerAuth(name, pAuth.getHash(), pAuth.getIp(), new Date().getTime(), pAuth.getEmail());
                    this.database.updateSession(auth);
                    PlayerCache.getInstance().addPlayer(auth);
                }
            }
            return;
        }
        catch (NullPointerException ex) {
            return;
        }
    }

    public static AuthMe getInstance() {
        return authme;
    }

    public void savePlayer(Player player) throws IllegalStateException, NullPointerException {
        try {
            if (this.citizens.isNPC((Entity)player, this) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
                return;
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        try {
            String name = player.getName();
            if (PlayerCache.getInstance().isAuthenticated(name) && !player.isDead() && Settings.isSaveQuitLocationEnabled.booleanValue()) {
                PlayerAuth auth = new PlayerAuth(player.getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getWorld().getName());
                this.database.updateQuitLoc(auth);
            }
            if (LimboCache.getInstance().hasLimboPlayer(name)) {
                LimboPlayer limbo = LimboCache.getInstance().getLimboPlayer(name);
                if (Settings.protectInventoryBeforeLogInEnabled.booleanValue()) {
                    player.getInventory().setArmorContents(limbo.getArmour());
                    player.getInventory().setContents(limbo.getInventory());
                }
                if (!Settings.noTeleport.booleanValue()) {
                    player.teleport(limbo.getLoc());
                }
                this.utils.addNormal(player, limbo.getGroup());
                player.setOp(limbo.getOperator());
                this.plugin.getServer().getScheduler().cancelTask(limbo.getTimeoutTaskId());
                LimboCache.getInstance().deleteLimboPlayer(name);
                if (this.playerBackup.doesCacheExist(player)) {
                    this.playerBackup.removeCache(player);
                }
            }
            PlayerCache.getInstance().removePlayer(name);
            this.database.setUnlogged(name);
            player.saveData();
        }
        catch (Exception ex) {
            // empty catch block
        }
    }

    public CitizensCommunicator getCitizensCommunicator() {
        return this.citizens;
    }

    public void setMessages(Messages m) {
        this.m = m;
    }

    public Messages getMessages() {
        return this.m;
    }

    public Player generateKickPlayer(Player[] players) {
        Player player = null;
        for (int i = 0; i <= players.length; ++i) {
            Random rdm = new Random();
            int a = rdm.nextInt(players.length);
            if (this.authmePermissible(players[a], "authme.vip")) continue;
            player = players[a];
            break;
        }
        if (player == null) {
            for (Player p : players) {
                if (this.authmePermissible(p, "authme.vip")) continue;
                player = p;
                break;
            }
        }
        return player;
    }

    public boolean authmePermissible(Player player, String perm) {
        if (player.hasPermission(perm)) {
            return true;
        }
        if (this.permission != null) {
            return this.permission.playerHas(player, perm);
        }
        return false;
    }

    public boolean authmePermissible(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        }
        if (this.permission != null) {
            return this.permission.has(sender, perm);
        }
        return false;
    }

    private void autoPurge() {
        if (!Settings.usePurge.booleanValue()) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, - Settings.purgeDelay);
        long until = calendar.getTimeInMillis();
        List<String> cleared = this.database.autoPurgeDatabase(until);
        if (cleared == null) {
            return;
        }
        if (cleared.isEmpty()) {
            return;
        }
        ConsoleLogger.info("AutoPurgeDatabase : " + cleared.size() + " accounts removed.");
        if (Settings.purgeEssentialsFile.booleanValue() && this.ess != null) {
            this.dataManager.purgeEssentials(cleared);
        }
        if (Settings.purgePlayerDat.booleanValue()) {
            this.dataManager.purgeDat(cleared);
        }
        if (Settings.purgeLimitedCreative.booleanValue()) {
            this.dataManager.purgeLimitedCreative(cleared);
        }
        if (Settings.purgeAntiXray.booleanValue()) {
            this.dataManager.purgeAntiXray(cleared);
        }
    }

    public Location getSpawnLocation(Player player) {
        World world = player.getWorld();
        String[] spawnPriority = Settings.spawnPriority.split(",");
        Location spawnLoc = world.getSpawnLocation();
        for (int i = spawnPriority.length - 1; i >= 0; --i) {
            String s = spawnPriority[i];
            if (s.equalsIgnoreCase("default") && this.getDefaultSpawn(world) != null) {
                spawnLoc = this.getDefaultSpawn(world);
            }
            if (s.equalsIgnoreCase("multiverse") && this.getMultiverseSpawn(world) != null) {
                spawnLoc = this.getMultiverseSpawn(world);
            }
            if (s.equalsIgnoreCase("essentials") && this.getEssentialsSpawn() != null) {
                spawnLoc = this.getEssentialsSpawn();
            }
            if (!s.equalsIgnoreCase("authme") || this.getAuthMeSpawn(player) == null) continue;
            spawnLoc = this.getAuthMeSpawn(player);
        }
        if (spawnLoc == null) {
            spawnLoc = world.getSpawnLocation();
        }
        return spawnLoc;
    }

    private Location getDefaultSpawn(World world) {
        return world.getSpawnLocation();
    }

    private Location getMultiverseSpawn(World world) {
        if (this.multiverse != null && Settings.multiverse.booleanValue()) {
            try {
                return this.multiverse.getMVWorldManager().getMVWorld(world).getSpawnLocation();
            }
            catch (NullPointerException npe) {
            }
            catch (ClassCastException cce) {
            }
            catch (NoClassDefFoundError ncdfe) {
                // empty catch block
            }
        }
        return null;
    }

    private Location getEssentialsSpawn() {
        if (this.essentialsSpawn != null) {
            return this.essentialsSpawn;
        }
        return null;
    }

    private Location getAuthMeSpawn(Player player) {
        if (!(this.database.isAuthAvailable(player.getName()) && player.hasPlayedBefore() || Spawn.getInstance().getFirstSpawn() == null)) {
            return Spawn.getInstance().getFirstSpawn();
        }
        if (Spawn.getInstance().getSpawn() != null) {
            return Spawn.getInstance().getSpawn();
        }
        return null;
    }

    public void downloadGeoIp() {
        ConsoleLogger.info("[LICENSE] This product includes GeoLite data created by MaxMind, available from http://www.maxmind.com");
        File file = new File(this.getDataFolder(), "GeoIP.dat");
        if (!file.exists()) {
            try {
                String url = "http://geolite.maxmind.com/download/geoip/database/GeoLiteCountry/GeoIP.dat.gz";
                URL downloadUrl = new URL(url);
                URLConnection conn = downloadUrl.openConnection();
                conn.setConnectTimeout(10000);
                conn.connect();
                InputStream input = conn.getInputStream();
                if (url.endsWith(".gz")) {
                    input = new GZIPInputStream(input);
                }
                FileOutputStream output = new FileOutputStream(file);
                byte[] buffer = new byte[2048];
                int length = input.read(buffer);
                while (length >= 0) {
                    output.write(buffer, 0, length);
                    length = input.read(buffer);
                }
                output.close();
                input.close();
            }
            catch (Exception e) {
                // empty catch block
            }
        }
    }

    public String getCountryCode(String ip) {
        try {
            String code;
            if (this.ls == null) {
                this.ls = new LookupService(new File(this.getDataFolder(), "GeoIP.dat"));
            }
            if ((code = this.ls.getCountry(ip).getCode()) != null && !code.isEmpty()) {
                return code;
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        return null;
    }

    public String getCountryName(String ip) {
        try {
            String code;
            if (this.ls == null) {
                this.ls = new LookupService(new File(this.getDataFolder(), "GeoIP.dat"));
            }
            if ((code = this.ls.getCountry(ip).getName()) != null && !code.isEmpty()) {
                return code;
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        return null;
    }

    public void switchAntiBotMod(boolean mode) {
        this.antibotMod = mode;
        Settings.switchAntiBotMod(mode);
    }

    private void recallEmail() {
        if (!Settings.recallEmail.booleanValue()) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable(){

            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String email;
                    String name;
                    if (!player.isOnline() || !AuthMe.this.database.isAuthAvailable(name = player.getName()) || !PlayerCache.getInstance().isAuthenticated(name) || (email = AuthMe.this.database.getAuth(name).getEmail()) != null && !email.isEmpty() && !email.equalsIgnoreCase("your@email.com")) continue;
                    AuthMe.this.m._((CommandSender)player, "add_email");
                }
            }
        }, 1, (long)(1200 * Settings.delayRecall));
    }

    public String replaceAllInfos(String message, Player player) {
        try {
            message = message.replace("&", "\u00a7");
            message = message.replace("{PLAYER}", player.getName());
            message = message.replace("{ONLINE}", "" + this.getServer().getOnlinePlayers().length);
            message = message.replace("{MAXPLAYERS}", "" + this.getServer().getMaxPlayers());
            message = message.replace("{IP}", this.getIP(player));
            message = message.replace("{LOGINS}", "" + PlayerCache.getInstance().getLogged());
            message = message.replace("{WORLD}", player.getWorld().getName());
            message = message.replace("{SERVER}", this.getServer().getServerName());
            message = message.replace("{VERSION}", this.getServer().getBukkitVersion());
            message = message.replace("{COUNTRY}", this.getCountryName(this.getIP(player)));
        }
        catch (Exception e) {
            // empty catch block
        }
        return message;
    }

    public String getIP(Player player) {
        String name = player.getName();
        String ip = player.getAddress().getAddress().getHostAddress();
        if (Settings.bungee.booleanValue() && this.realIp.containsKey(name)) {
            ip = this.realIp.get(name);
        }
        if (Settings.checkVeryGames.booleanValue() && this.getVeryGamesIP(player) != null) {
            ip = this.getVeryGamesIP(player);
        }
        return ip;
    }

    public boolean isLoggedIp(String name, String ip) {
        int count = 0;
        for (Player player : this.getServer().getOnlinePlayers()) {
            if (!ip.equalsIgnoreCase(this.getIP(player)) || !this.database.isLogged(player.getName()) || player.getName().equalsIgnoreCase(name)) continue;
            ++count;
        }
        if (count >= Settings.getMaxLoginPerIp) {
            return true;
        }
        return false;
    }

    public boolean hasJoinedIp(String name, String ip) {
        int count = 0;
        for (Player player : this.getServer().getOnlinePlayers()) {
            if (!ip.equalsIgnoreCase(this.getIP(player)) || player.getName().equalsIgnoreCase(name)) continue;
            ++count;
        }
        if (count >= Settings.getMaxJoinPerIp) {
            return true;
        }
        return false;
    }

    public String getVeryGamesIP(Player player) {
        String realIP = player.getAddress().getAddress().getHostAddress();
        String sUrl = vgUrl;
        sUrl = sUrl.replace("%IP%", player.getAddress().getAddress().getHostAddress()).replace("%PORT%", "" + player.getAddress().getPort());
        try {
            URL url = new URL(sUrl);
            URLConnection urlc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String inputLine = in.readLine();
            if (!(inputLine == null || inputLine.isEmpty() || inputLine.equalsIgnoreCase("error") || inputLine.contains("error"))) {
                realIP = inputLine;
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        return realIP;
    }

    static {
        authmeLogger = java.util.logging.Logger.getLogger("AuthMe");
        vgUrl = "http://monitor-1.verygames.net/api/?action=ipclean-real-ip&out=raw&ip=%IP%&port=%PORT%";
    }

}

