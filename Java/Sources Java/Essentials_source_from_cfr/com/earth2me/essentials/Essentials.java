/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.BlockCommandSender
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.world.WorldLoadEvent
 *  org.bukkit.event.world.WorldUnloadEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.plugin.InvalidDescriptionException
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginLoader
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.java.JavaPluginLoader
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 *  org.yaml.snakeyaml.error.YAMLException
 */
package com.earth2me.essentials;

import com.earth2me.essentials.AlternativeCommandsHandler;
import com.earth2me.essentials.Backup;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.EssentialsBlockListener;
import com.earth2me.essentials.EssentialsEntityListener;
import com.earth2me.essentials.EssentialsPlayerListener;
import com.earth2me.essentials.EssentialsPluginListener;
import com.earth2me.essentials.EssentialsTimer;
import com.earth2me.essentials.EssentialsUpgrade;
import com.earth2me.essentials.ExecuteTimer;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IConf;
import com.earth2me.essentials.IEssentialsModule;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.ItemDb;
import com.earth2me.essentials.Jails;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.Settings;
import com.earth2me.essentials.TNTExplodeListener;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.Warps;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.QuietAbortException;
import com.earth2me.essentials.metrics.Metrics;
import com.earth2me.essentials.metrics.MetricsListener;
import com.earth2me.essentials.metrics.MetricsStarter;
import com.earth2me.essentials.perm.PermissionsHandler;
import com.earth2me.essentials.register.payment.Methods;
import com.earth2me.essentials.signs.SignBlockListener;
import com.earth2me.essentials.signs.SignEntityListener;
import com.earth2me.essentials.signs.SignPlayerListener;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.earth2me.essentials.utils.DateUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ess3.api.Economy;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.yaml.snakeyaml.error.YAMLException;

public class Essentials
extends JavaPlugin
implements IEssentials {
    public static final int BUKKIT_VERSION = 2922;
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private transient net.ess3.api.ISettings settings;
    private final transient TNTExplodeListener tntListener;
    private transient Jails jails;
    private transient Warps warps;
    private transient Worth worth;
    private transient List<IConf> confList;
    private transient Backup backup;
    private transient ItemDb itemDb;
    private final transient Methods paymentMethod;
    private transient PermissionsHandler permissionsHandler;
    private transient AlternativeCommandsHandler alternativeCommandsHandler;
    private transient UserMap userMap;
    private transient ExecuteTimer execTimer;
    private transient I18n i18n;
    private transient Metrics metrics;
    private transient EssentialsTimer timer;
    private transient List<String> vanishedPlayers;

    public Essentials() {
        this.tntListener = new TNTExplodeListener(this);
        this.paymentMethod = new Methods();
        this.vanishedPlayers = new ArrayList<String>();
    }

    public Essentials(Server server) {
        super(new JavaPluginLoader(server), new PluginDescriptionFile("Essentials", "", "com.earth2me.essentials.Essentials"), null, null);
        this.tntListener = new TNTExplodeListener(this);
        this.paymentMethod = new Methods();
        this.vanishedPlayers = new ArrayList<String>();
    }

    @Override
    public net.ess3.api.ISettings getSettings() {
        return this.settings;
    }

    public void setupForTesting(Server server) throws IOException, InvalidDescriptionException {
        File dataFolder = File.createTempFile("essentialstest", "");
        if (!dataFolder.delete()) {
            throw new IOException();
        }
        if (!dataFolder.mkdir()) {
            throw new IOException();
        }
        this.i18n = new I18n(this);
        this.i18n.onEnable();
        LOGGER.log(Level.INFO, I18n._("usingTempFolderForTesting", new Object[0]));
        LOGGER.log(Level.INFO, dataFolder.toString());
        this.initialize(null, server, new PluginDescriptionFile((Reader)new FileReader(new File("src" + File.separator + "plugin.yml"))), dataFolder, null, null);
        this.settings = new Settings(this);
        this.i18n.updateLocale("en");
        this.userMap = new UserMap(this);
        this.permissionsHandler = new PermissionsHandler((Plugin)this, false);
        Economy.setEss(this);
    }

    public void onEnable() {
        try {
            LOGGER.setParent(this.getLogger());
            this.execTimer = new ExecuteTimer();
            this.execTimer.start();
            this.i18n = new I18n(this);
            this.i18n.onEnable();
            this.execTimer.mark("I18n1");
            PluginManager pm = this.getServer().getPluginManager();
            for (Plugin plugin : pm.getPlugins()) {
                if (!plugin.getDescription().getName().startsWith("Essentials") || plugin.getDescription().getVersion().equals(this.getDescription().getVersion()) || plugin.getDescription().getName().equals("EssentialsAntiCheat")) continue;
                LOGGER.log(Level.WARNING, I18n._("versionMismatch", plugin.getDescription().getName()));
            }
            Matcher versionMatch = Pattern.compile("git-Bukkit-(?:(?:[0-9]+)\\.)+[0-9]+-R[\\.0-9]+-(?:[0-9]+-g[0-9a-f]+-)?b([0-9]+)jnks.*").matcher(this.getServer().getVersion());
            if (versionMatch.matches()) {
                int versionNumber = Integer.parseInt(versionMatch.group(1));
                if (versionNumber < 2922 && versionNumber > 100) {
                    Essentials.wrongVersion();
                    this.setEnabled(false);
                    return;
                }
            } else {
                LOGGER.log(Level.INFO, I18n._("bukkitFormatChanged", new Object[0]));
                LOGGER.log(Level.INFO, this.getServer().getVersion());
                LOGGER.log(Level.INFO, this.getServer().getBukkitVersion());
            }
            this.execTimer.mark("BukkitCheck");
            try {
                EssentialsUpgrade upgrade = new EssentialsUpgrade(this);
                upgrade.beforeSettings();
                this.execTimer.mark("Upgrade");
                this.confList = new ArrayList<IConf>();
                this.settings = new Settings(this);
                this.confList.add(this.settings);
                this.execTimer.mark("Settings");
                upgrade.afterSettings();
                this.execTimer.mark("Upgrade2");
                this.i18n.updateLocale(this.settings.getLocale());
                this.userMap = new UserMap(this);
                this.confList.add(this.userMap);
                this.execTimer.mark("Init(Usermap)");
                this.warps = new Warps(this.getServer(), this.getDataFolder());
                this.confList.add(this.warps);
                this.execTimer.mark("Init(Spawn/Warp)");
                this.worth = new Worth(this.getDataFolder());
                this.confList.add(this.worth);
                this.itemDb = new ItemDb(this);
                this.confList.add(this.itemDb);
                this.execTimer.mark("Init(Worth/ItemDB)");
                this.jails = new Jails(this);
                this.confList.add(this.jails);
                this.reload();
            }
            catch (YAMLException exception) {
                if (pm.getPlugin("EssentialsUpdate") != null) {
                    LOGGER.log(Level.SEVERE, I18n._("essentialsHelp2", new Object[0]));
                } else {
                    LOGGER.log(Level.SEVERE, I18n._("essentialsHelp1", new Object[0]));
                }
                this.handleCrash((Throwable)exception);
                return;
            }
            this.backup = new Backup(this);
            this.permissionsHandler = new PermissionsHandler((Plugin)this, this.settings.useBukkitPermissions());
            this.alternativeCommandsHandler = new AlternativeCommandsHandler(this);
            this.timer = new EssentialsTimer(this);
            this.scheduleSyncRepeatingTask(this.timer, 1000, 50);
            Economy.setEss(this);
            this.execTimer.mark("RegHandler");
            MetricsStarter metricsStarter = new MetricsStarter(this);
            if (metricsStarter.getStart() != null && metricsStarter.getStart().booleanValue()) {
                this.runTaskLaterAsynchronously(metricsStarter, 1);
            } else if (metricsStarter.getStart() != null && !metricsStarter.getStart().booleanValue()) {
                MetricsListener metricsListener = new MetricsListener(this, metricsStarter);
                pm.registerEvents((Listener)metricsListener, (Plugin)this);
            }
            String timeroutput = this.execTimer.end();
            if (this.getSettings().isDebug()) {
                LOGGER.log(Level.INFO, "Essentials load " + timeroutput);
            }
        }
        catch (NumberFormatException ex) {
            this.handleCrash(ex);
        }
        catch (Error ex) {
            this.handleCrash(ex);
            throw ex;
        }
    }

    public void saveConfig() {
    }

    private void registerListeners(PluginManager pm) {
        HandlerList.unregisterAll((Plugin)this);
        if (this.getSettings().isDebug()) {
            LOGGER.log(Level.INFO, "Registering Listeners");
        }
        EssentialsPluginListener serverListener = new EssentialsPluginListener(this);
        pm.registerEvents((Listener)serverListener, (Plugin)this);
        this.confList.add(serverListener);
        EssentialsPlayerListener playerListener = new EssentialsPlayerListener(this);
        pm.registerEvents((Listener)playerListener, (Plugin)this);
        EssentialsBlockListener blockListener = new EssentialsBlockListener(this);
        pm.registerEvents((Listener)blockListener, (Plugin)this);
        SignBlockListener signBlockListener = new SignBlockListener(this);
        pm.registerEvents((Listener)signBlockListener, (Plugin)this);
        SignPlayerListener signPlayerListener = new SignPlayerListener(this);
        pm.registerEvents((Listener)signPlayerListener, (Plugin)this);
        SignEntityListener signEntityListener = new SignEntityListener(this);
        pm.registerEvents((Listener)signEntityListener, (Plugin)this);
        EssentialsEntityListener entityListener = new EssentialsEntityListener(this);
        pm.registerEvents((Listener)entityListener, (Plugin)this);
        EssentialsWorldListener worldListener = new EssentialsWorldListener(this);
        pm.registerEvents((Listener)worldListener, (Plugin)this);
        pm.registerEvents((Listener)this.tntListener, (Plugin)this);
        this.jails.resetListener();
    }

    public void onDisable() {
        for (Player p : this.getServer().getOnlinePlayers()) {
            User user = this.getUser(p);
            if (!user.isVanished()) continue;
            user.setVanished(false);
            user.sendMessage(I18n._("unvanishedReload", new Object[0]));
        }
        this.cleanupOpenInventories();
        if (this.i18n != null) {
            this.i18n.onDisable();
        }
        if (this.backup != null) {
            this.backup.stopTask();
        }
        Economy.setEss(null);
        Trade.closeLog();
    }

    @Override
    public void reload() {
        Trade.closeLog();
        for (IConf iConf : this.confList) {
            iConf.reloadConfig();
            this.execTimer.mark("Reload(" + iConf.getClass().getSimpleName() + ")");
        }
        this.i18n.updateLocale(this.settings.getLocale());
        PluginManager pm = this.getServer().getPluginManager();
        this.registerListeners(pm);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args) {
        PluginCommand pc;
        if (!(this.getSettings().isCommandOverridden(command.getName()) || commandLabel.startsWith("e") && !commandLabel.equalsIgnoreCase(command.getName()) || (pc = this.alternativeCommandsHandler.getAlternative(commandLabel)) == null)) {
            try {
                TabCompleter completer = pc.getTabCompleter();
                if (completer != null) {
                    return completer.onTabComplete(sender, command, commandLabel, args);
                }
            }
            catch (Exception ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        return this.onCommandEssentials(sender, command, commandLabel, args, Essentials.class.getClassLoader(), "com.earth2me.essentials.commands.Command", "essentials.", null);
    }

    @Override
    public boolean onCommandEssentials(CommandSender cSender, Command command, String commandLabel, String[] args, ClassLoader classLoader, String commandPath, String permissionPrefix, IEssentialsModule module) {
        PluginCommand pc;
        if (!(this.getSettings().isCommandOverridden(command.getName()) || commandLabel.startsWith("e") && !commandLabel.equalsIgnoreCase(command.getName()) || (pc = this.alternativeCommandsHandler.getAlternative(commandLabel)) == null)) {
            this.alternativeCommandsHandler.executed(commandLabel, pc);
            try {
                return pc.execute(cSender, commandLabel, args);
            }
            catch (Exception ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                cSender.sendMessage((Object)ChatColor.RED + "An internal error occurred while attempting to perform this command");
                return true;
            }
        }
        try {
            List<String> mail;
            IEssentialsCommand cmd;
            User user = null;
            Block bSenderBlock = null;
            if (cSender instanceof Player) {
                user = this.getUser((Player)cSender);
            } else if (cSender instanceof BlockCommandSender) {
                BlockCommandSender bsender = (BlockCommandSender)cSender;
                bSenderBlock = bsender.getBlock();
            }
            if (bSenderBlock != null) {
                Bukkit.getLogger().log(Level.INFO, "CommandBlock at {0},{1},{2} issued server command: /{3} {4}", new Object[]{bSenderBlock.getX(), bSenderBlock.getY(), bSenderBlock.getZ(), commandLabel, EssentialsCommand.getFinalArg(args, 0)});
            } else if (user == null) {
                Bukkit.getLogger().log(Level.INFO, "{0} issued server command: /{1} {2}", new Object[]{cSender.getName(), commandLabel, EssentialsCommand.getFinalArg(args, 0)});
            }
            CommandSource sender = new CommandSource(cSender);
            if (user != null && !this.getSettings().isCommandDisabled("mail") && !command.getName().equals("mail") && user.isAuthorized("essentials.mail") && (mail = user.getMails()) != null && !mail.isEmpty()) {
                user.sendMessage(I18n._("youHaveNewMail", mail.size()));
            }
            if (commandLabel.equalsIgnoreCase("essversion")) {
                sender.sendMessage("This server is running Essentials " + this.getDescription().getVersion());
                return true;
            }
            if (this.getSettings().isCommandDisabled(commandLabel)) {
                return true;
            }
            try {
                cmd = (IEssentialsCommand)classLoader.loadClass(commandPath + command.getName()).newInstance();
                cmd.setEssentials(this);
                cmd.setEssentialsModule(module);
            }
            catch (Exception ex) {
                sender.sendMessage(I18n._("commandNotLoaded", commandLabel));
                LOGGER.log(Level.SEVERE, I18n._("commandNotLoaded", commandLabel), ex);
                return true;
            }
            if (user != null && !user.isAuthorized(cmd, permissionPrefix)) {
                LOGGER.log(Level.INFO, I18n._("deniedAccessCommand", user.getName()));
                user.sendMessage(I18n._("noAccessCommand", new Object[0]));
                return true;
            }
            if (user != null && user.isJailed() && !user.isAuthorized(cmd, "essentials.jail.allow.")) {
                if (user.getJailTimeout() > 0) {
                    user.sendMessage(I18n._("playerJailedFor", user.getName(), DateUtil.formatDateDiff(user.getJailTimeout())));
                } else {
                    user.sendMessage(I18n._("jailMessage", new Object[0]));
                }
                return true;
            }
            try {
                if (user == null) {
                    cmd.run(this.getServer(), sender, commandLabel, command, args);
                } else {
                    cmd.run(this.getServer(), user, commandLabel, command, args);
                }
                return true;
            }
            catch (NoChargeException ex) {
                return true;
            }
            catch (QuietAbortException ex) {
                return true;
            }
            catch (NotEnoughArgumentsException ex) {
                sender.sendMessage(command.getDescription());
                sender.sendMessage(command.getUsage().replaceAll("<command>", commandLabel));
                if (!ex.getMessage().isEmpty()) {
                    sender.sendMessage(ex.getMessage());
                }
                return true;
            }
            catch (Exception ex) {
                this.showError(sender, ex, commandLabel);
                return true;
            }
        }
        catch (Throwable ex) {
            LOGGER.log(Level.SEVERE, I18n._("commandFailed", commandLabel), ex);
            return true;
        }
    }

    public void cleanupOpenInventories() {
        for (Player player : this.getServer().getOnlinePlayers()) {
            User user = this.getUser(player);
            if (user.isRecipeSee()) {
                user.getBase().getOpenInventory().getTopInventory().clear();
                user.getBase().getOpenInventory().close();
                user.setRecipeSee(false);
            }
            if (!user.isInvSee() && !user.isEnderSee()) continue;
            user.getBase().getOpenInventory().close();
            user.setInvSee(false);
            user.setEnderSee(false);
        }
    }

    @Override
    public void showError(CommandSource sender, Throwable exception, String commandLabel) {
        sender.sendMessage(I18n._("errorWithMessage", exception.getMessage()));
        if (this.getSettings().isDebug()) {
            LOGGER.log(Level.WARNING, I18n._("errorCallingCommand", commandLabel), exception);
        }
    }

    public static void wrongVersion() {
        LOGGER.log(Level.SEVERE, " * ! * ! * ! * ! * ! * ! * ! * ! * ! * ! * ! * ! *");
        LOGGER.log(Level.SEVERE, I18n._("notRecommendedBukkit", new Object[0]));
        LOGGER.log(Level.SEVERE, I18n._("requiredBukkit", Integer.toString(2922)));
        LOGGER.log(Level.SEVERE, " * ! * ! * ! * ! * ! * ! * ! * ! * ! * ! * ! * ! *");
    }

    @Override
    public BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }

    @Override
    public net.ess3.api.IJails getJails() {
        return this.jails;
    }

    @Override
    public Warps getWarps() {
        return this.warps;
    }

    @Override
    public Worth getWorth() {
        return this.worth;
    }

    @Override
    public Backup getBackup() {
        return this.backup;
    }

    @Override
    public Metrics getMetrics() {
        return this.metrics;
    }

    @Override
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    @Deprecated
    @Override
    public User getUser(Object base) {
        if (base instanceof Player) {
            return this.getUser((Player)base);
        }
        if (base instanceof String) {
            return this.getOfflineUser((String)base);
        }
        return null;
    }

    @Override
    public User getUser(String base) {
        return this.getOfflineUser(base);
    }

    @Override
    public User getOfflineUser(String name) {
        User user = this.userMap.getUser(name);
        if (user != null && user.getBase() instanceof OfflinePlayer) {
            ((OfflinePlayer)user.getBase()).setName(name);
        }
        return user;
    }

    @Override
    public User getUser(Player base) {
        if (base == null) {
            return null;
        }
        if (this.userMap == null) {
            LOGGER.log(Level.WARNING, "Essentials userMap not initialized");
            return null;
        }
        User user = this.userMap.getUser(base.getName());
        if (user == null) {
            if (this.getSettings().isDebug()) {
                LOGGER.log(Level.INFO, "Constructing new userfile from base player " + base.getName());
            }
            user = new User(base, this);
        } else {
            user.update(base);
        }
        return user;
    }

    private void handleCrash(Throwable exception) {
        PluginManager pm = this.getServer().getPluginManager();
        LOGGER.log(Level.SEVERE, exception.toString());
        pm.registerEvents(new Listener(){

            @EventHandler(priority=EventPriority.LOW)
            public void onPlayerJoin(PlayerJoinEvent event) {
                event.getPlayer().sendMessage("Essentials failed to load, read the log file.");
            }
        }, (Plugin)this);
        for (Player player : this.getServer().getOnlinePlayers()) {
            player.sendMessage("Essentials failed to load, read the log file.");
        }
        this.setEnabled(false);
    }

    @Override
    public World getWorld(String name) {
        int worldId;
        if (name.matches("[0-9]+") && (worldId = Integer.parseInt(name)) < this.getServer().getWorlds().size()) {
            return (World)this.getServer().getWorlds().get(worldId);
        }
        return this.getServer().getWorld(name);
    }

    @Override
    public void addReloadListener(IConf listener) {
        this.confList.add(listener);
    }

    @Override
    public Methods getPaymentMethod() {
        return this.paymentMethod;
    }

    @Override
    public int broadcastMessage(String message) {
        return this.broadcastMessage(null, null, message, true);
    }

    @Override
    public int broadcastMessage(IUser sender, String message) {
        return this.broadcastMessage(sender, null, message, false);
    }

    @Override
    public int broadcastMessage(String permission, String message) {
        return this.broadcastMessage(null, permission, message, false);
    }

    private int broadcastMessage(IUser sender, String permission, String message, boolean keywords) {
        Player[] players;
        if (sender != null && sender.isHidden()) {
            return 0;
        }
        IText broadcast = new SimpleTextInput(message);
        for (Player player : players = this.getServer().getOnlinePlayers()) {
            User user = this.getUser(player);
            if ((permission != null || sender != null && user.isIgnoredPlayer(sender)) && (permission == null || !user.isAuthorized(permission))) continue;
            if (keywords) {
                broadcast = new KeywordReplacer(broadcast, new CommandSource((CommandSender)player), this, false);
            }
            for (String messageText : broadcast.getLines()) {
                user.sendMessage(messageText);
            }
        }
        return players.length;
    }

    @Override
    public BukkitTask runTaskAsynchronously(Runnable run) {
        return this.getScheduler().runTaskAsynchronously((Plugin)this, run);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(Runnable run, long delay) {
        return this.getScheduler().runTaskLaterAsynchronously((Plugin)this, run, delay);
    }

    @Override
    public int scheduleSyncDelayedTask(Runnable run) {
        return this.getScheduler().scheduleSyncDelayedTask((Plugin)this, run);
    }

    @Override
    public int scheduleSyncDelayedTask(Runnable run, long delay) {
        return this.getScheduler().scheduleSyncDelayedTask((Plugin)this, run, delay);
    }

    @Override
    public int scheduleSyncRepeatingTask(Runnable run, long delay, long period) {
        return this.getScheduler().scheduleSyncRepeatingTask((Plugin)this, run, delay, period);
    }

    @Override
    public TNTExplodeListener getTNTListener() {
        return this.tntListener;
    }

    @Override
    public PermissionsHandler getPermissionsHandler() {
        return this.permissionsHandler;
    }

    @Override
    public AlternativeCommandsHandler getAlternativeCommandsHandler() {
        return this.alternativeCommandsHandler;
    }

    @Override
    public net.ess3.api.IItemDb getItemDb() {
        return this.itemDb;
    }

    @Override
    public UserMap getUserMap() {
        return this.userMap;
    }

    @Override
    public I18n getI18n() {
        return this.i18n;
    }

    @Override
    public EssentialsTimer getTimer() {
        return this.timer;
    }

    @Override
    public List<String> getVanishedPlayers() {
        return this.vanishedPlayers;
    }

    private static class EssentialsWorldListener
    implements Listener,
    Runnable {
        private final transient IEssentials ess;

        public EssentialsWorldListener(IEssentials ess) {
            this.ess = ess;
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onWorldLoad(WorldLoadEvent event) {
            this.ess.getJails().onReload();
            this.ess.getWarps().reloadConfig();
            for (IConf iConf : ((Essentials)this.ess).confList) {
                if (!(iConf instanceof IEssentialsModule)) continue;
                iConf.reloadConfig();
            }
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onWorldUnload(WorldUnloadEvent event) {
            this.ess.getJails().onReload();
            this.ess.getWarps().reloadConfig();
            for (IConf iConf : ((Essentials)this.ess).confList) {
                if (!(iConf instanceof IEssentialsModule)) continue;
                iConf.reloadConfig();
            }
        }

        @Override
        public void run() {
            this.ess.reload();
        }
    }

}

