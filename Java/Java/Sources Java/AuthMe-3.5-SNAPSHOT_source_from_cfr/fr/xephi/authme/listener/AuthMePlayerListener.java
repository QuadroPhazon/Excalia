/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.earth2me.essentials.User
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.SignChangeEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerBedEnterEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerGameModeChangeEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.listener;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.Utils;
import fr.xephi.authme.api.API;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.backup.DataFileCache;
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.AuthMeTeleportEvent;
import fr.xephi.authme.events.ProtectInventoryEvent;
import fr.xephi.authme.events.RestoreInventoryEvent;
import fr.xephi.authme.events.SessionEvent;
import fr.xephi.authme.events.SpawnTeleportEvent;
import fr.xephi.authme.plugin.manager.CitizensCommunicator;
import fr.xephi.authme.plugin.manager.CombatTagComunicator;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class AuthMePlayerListener
implements Listener {
    public static GameMode gm = GameMode.SURVIVAL;
    public static HashMap<String, GameMode> gameMode = new HashMap();
    public static HashMap<String, String> joinMessage = new HashMap();
    private Utils utils = Utils.getInstance();
    private Messages m = Messages.getInstance();
    public AuthMe plugin;
    private DataSource data;
    private FileCache playerBackup;
    public static HashMap<String, Boolean> causeByAuthMe = new HashMap();
    private HashMap<String, PlayerLoginEvent> antibot = new HashMap();

    public AuthMePlayerListener(AuthMe plugin, DataSource data) {
        this.plugin = plugin;
        this.data = data;
        this.playerBackup = new FileCache(plugin);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        String msg = event.getMessage();
        if (msg.equalsIgnoreCase("/worldedit cui")) {
            return;
        }
        String cmd = msg.split(" ")[0];
        if (cmd.equalsIgnoreCase("/login") || cmd.equalsIgnoreCase("/register") || cmd.equalsIgnoreCase("/passpartu") || cmd.equalsIgnoreCase("/l") || cmd.equalsIgnoreCase("/reg") || cmd.equalsIgnoreCase("/email") || cmd.equalsIgnoreCase("/captcha")) {
            return;
        }
        if (Settings.useEssentialsMotd.booleanValue() && cmd.equalsIgnoreCase("/motd")) {
            return;
        }
        if (Settings.allowCommands.contains(cmd)) {
            return;
        }
        event.setMessage("/notloggedin");
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerNormalChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        String cmd = event.getMessage().split(" ")[0];
        if (!this.data.isAuthAvailable(name)) {
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
            if (Settings.emailRegistration.booleanValue()) {
                this.m._((CommandSender)player, "reg_email_msg");
                return;
            }
            this.m._((CommandSender)player, "reg_msg");
            return;
        }
        this.m._((CommandSender)player, "login_msg");
        if (!Settings.isChatAllowed.booleanValue() && !Settings.allowCommands.contains(cmd)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerHighChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        String cmd = event.getMessage().split(" ")[0];
        if (!this.data.isAuthAvailable(name)) {
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
            if (Settings.emailRegistration.booleanValue()) {
                this.m._((CommandSender)player, "reg_email_msg");
                return;
            }
            this.m._((CommandSender)player, "reg_msg");
            return;
        }
        this.m._((CommandSender)player, "login_msg");
        if (!Settings.isChatAllowed.booleanValue() && !Settings.allowCommands.contains(cmd)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        String cmd = event.getMessage().split(" ")[0];
        if (!this.data.isAuthAvailable(name)) {
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
            if (Settings.emailRegistration.booleanValue()) {
                this.m._((CommandSender)player, "reg_email_msg");
                return;
            }
            this.m._((CommandSender)player, "reg_msg");
            return;
        }
        this.m._((CommandSender)player, "login_msg");
        if (!Settings.isChatAllowed.booleanValue() && !Settings.allowCommands.contains(cmd)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerHighestChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        String cmd = event.getMessage().split(" ")[0];
        if (!this.data.isAuthAvailable(name)) {
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
            if (Settings.emailRegistration.booleanValue()) {
                this.m._((CommandSender)player, "reg_email_msg");
                return;
            }
            this.m._((CommandSender)player, "reg_msg");
            return;
        }
        this.m._((CommandSender)player, "login_msg");
        if (!Settings.isChatAllowed.booleanValue() && !Settings.allowCommands.contains(cmd)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerEarlyChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        String cmd = event.getMessage().split(" ")[0];
        if (!this.data.isAuthAvailable(name)) {
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
            if (Settings.emailRegistration.booleanValue()) {
                this.m._((CommandSender)player, "reg_email_msg");
                return;
            }
            this.m._((CommandSender)player, "reg_msg");
            return;
        }
        this.m._((CommandSender)player, "login_msg");
        if (!Settings.isChatAllowed.booleanValue() && !Settings.allowCommands.contains(cmd)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerLowChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        String cmd = event.getMessage().split(" ")[0];
        if (this.data.isAuthAvailable(name)) {
            this.m._((CommandSender)player, "login_msg");
        } else {
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
            if (Settings.emailRegistration.booleanValue()) {
                this.m._((CommandSender)player, "reg_email_msg");
            } else {
                this.m._((CommandSender)player, "reg_msg");
            }
        }
        if (!Settings.isChatAllowed.booleanValue() && !Settings.allowCommands.contains(cmd)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        if (!Settings.isMovementAllowed.booleanValue()) {
            event.setTo(event.getFrom());
            return;
        }
        if (Settings.getMovementRadius == 0) {
            return;
        }
        int radius = Settings.getMovementRadius;
        Location spawn = this.plugin.getSpawnLocation(player);
        if (spawn != null && spawn.getWorld() != null && !event.getPlayer().getWorld().equals((Object)spawn.getWorld())) {
            event.getPlayer().teleport(spawn);
            return;
        }
        if (spawn.distance(player.getLocation()) > (double)radius && spawn.getWorld() != null) {
            event.getPlayer().teleport(spawn);
            return;
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player;
        block29 : {
            String name;
            String code;
            player = event.getPlayer();
            String lowname = player.getName().toLowerCase();
            if (!lowname.equals(name = player.getName()) && player.hasPlayedBefore() && !player.isOnline() && this.data.isAuthAvailable(lowname)) {
                if (this.data.getAuth(lowname).getIp().equalsIgnoreCase(player.getAddress().getAddress().getHostAddress())) {
                    this.data.updateName(lowname, name);
                } else {
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(this.m._("same_nick")[0]);
                }
            }
            if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
                return;
            }
            if (!Settings.countriesBlacklist.isEmpty() && ((code = this.plugin.getCountryCode(event.getAddress().getHostAddress())) == null || Settings.countriesBlacklist.contains(code) && !API.isRegistered(name)) && !this.plugin.authmePermissible(player, "authme.bypassantibot")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("country_banned")[0]);
                return;
            }
            if (!(!Settings.enableProtection.booleanValue() || Settings.countries.isEmpty() || (code = this.plugin.getCountryCode(event.getAddress().getHostAddress())) != null && (Settings.countries.contains(code) || API.isRegistered(name)) || this.plugin.authmePermissible(player, "authme.bypassantibot"))) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("country_banned")[0]);
                return;
            }
            if (Settings.isKickNonRegisteredEnabled.booleanValue() && !this.data.isAuthAvailable(name)) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("reg_only")[0]);
                return;
            }
            if (player.isOnline() && Settings.isForceSingleSessionEnabled.booleanValue()) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("same_nick")[0]);
                return;
            }
            if (this.data.isAuthAvailable(name) && !LimboCache.getInstance().hasLimboPlayer(name) && Settings.isSessionsEnabled.booleanValue() && PlayerCache.getInstance().isAuthenticated(name) && !Settings.sessionExpireOnIpChange.booleanValue() && LimboCache.getInstance().hasLimboPlayer(player.getName())) {
                LimboCache.getInstance().deleteLimboPlayer(name);
            }
            if (player.isOnline() && Settings.isForceSingleSessionEnabled.booleanValue()) {
                LimboPlayer limbo = LimboCache.getInstance().getLimboPlayer(player.getName());
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("same_nick")[0]);
                if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
                    this.utils.addNormal(player, limbo.getGroup());
                    LimboCache.getInstance().deleteLimboPlayer(player.getName());
                }
                return;
            }
            int min = Settings.getMinNickLength;
            int max = Settings.getMaxNickLength;
            String regex = Settings.getNickRegex;
            if (name.length() > max || name.length() < min) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("name_len")[0]);
                return;
            }
            try {
                if (player.getName().matches(regex) && !name.equals("Player")) break block29;
                try {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("regex")[0].replace("REG_EX", regex));
                }
                catch (Exception exc) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "allowed char : " + regex);
                }
                return;
            }
            catch (PatternSyntaxException pse) {
                if (regex == null || regex.isEmpty()) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your nickname do not match");
                    return;
                }
                try {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.m._("regex")[0].replace("REG_EX", regex));
                }
                catch (Exception exc) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "allowed char : " + regex);
                }
                return;
            }
        }
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            this.checkAntiBotMod(event);
            if (Settings.bungee.booleanValue()) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("IP");
                }
                catch (IOException e) {
                    // empty catch block
                }
                player.sendPluginMessage((Plugin)this.plugin, "BungeeCord", b.toByteArray());
            }
            if (this.plugin.ess != null) {
                this.kit_manager(player);
            }
            return;
        }
        if (event.getResult() != PlayerLoginEvent.Result.KICK_FULL) {
            return;
        }
        if (player.isBanned()) {
            return;
        }
        if (!this.plugin.authmePermissible(player, "authme.vip")) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, this.m._("kick_fullserver")[0]);
            return;
        }
        if (this.plugin.getServer().getOnlinePlayers().length > this.plugin.getServer().getMaxPlayers()) {
            event.allow();
            return;
        }
        Player pl = this.plugin.generateKickPlayer(this.plugin.getServer().getOnlinePlayers());
        if (pl != null) {
            pl.kickPlayer(this.m._("kick_forvip")[0]);
            event.allow();
            return;
        }
        ConsoleLogger.info("The player " + player.getName() + " wants to join, but the server is full");
        event.disallow(PlayerLoginEvent.Result.KICK_FULL, this.m._("kick_fullserver")[0]);
    }

    private void kit_manager(Player player) {
        if (player.hasPlayedBefore()) {
            return;
        }
    }

    private void checkAntiBotMod(final PlayerLoginEvent event) {
        if (this.plugin.delayedAntiBot || this.plugin.antibotMod) {
            return;
        }
        if (this.plugin.authmePermissible(event.getPlayer(), "authme.bypassantibot")) {
            return;
        }
        if (this.antibot.keySet().size() > Settings.antiBotSensibility) {
            this.plugin.switchAntiBotMod(true);
            for (String s : this.m._("antibot_auto_enabled")) {
                Bukkit.broadcastMessage((String)s);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

                public void run() {
                    if (AuthMePlayerListener.this.plugin.antibotMod) {
                        AuthMePlayerListener.this.plugin.switchAntiBotMod(false);
                        AuthMePlayerListener.this.antibot.clear();
                        for (String s : AuthMePlayerListener.this.m._("antibot_auto_disabled")) {
                            Bukkit.broadcastMessage((String)s.replace("%m", "" + Settings.antiBotDuration));
                        }
                    }
                }
            }, (long)(Settings.antiBotDuration * 1200));
            return;
        }
        this.antibot.put(event.getPlayer().getName(), event);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

            public void run() {
                AuthMePlayerListener.this.antibot.remove(event.getPlayer().getName());
            }
        }, 300);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        SpawnTeleportEvent tpEvent;
        if (event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        Location spawnLoc = this.plugin.getSpawnLocation(player);
        gm = player.getGameMode();
        gameMode.put(name, gm);
        BukkitScheduler sched = this.plugin.getServer().getScheduler();
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (this.plugin.ess != null && Settings.disableSocialSpy.booleanValue()) {
            try {
                this.plugin.ess.getUser(player.getName()).setSocialSpyEnabled(false);
            }
            catch (Exception e) {
            }
            catch (NoSuchMethodError e) {
                // empty catch block
            }
        }
        String ip = this.plugin.getIP(player);
        if (Settings.isAllowRestrictedIp.booleanValue() && !Settings.getRestrictedIp(name, ip).booleanValue()) {
            GameMode gM = gameMode.get(name);
            causeByAuthMe.put(name, true);
            player.setGameMode(gM);
            causeByAuthMe.put(name, false);
            player.kickPlayer("You are not the Owner of this account, please try another name!");
            if (Settings.banUnsafeIp.booleanValue()) {
                this.plugin.getServer().banIP(ip);
            }
            return;
        }
        if (Settings.getMaxJoinPerIp > 0 && !this.plugin.authmePermissible(player, "authme.allow2accounts") && !ip.equalsIgnoreCase("127.0.0.1") && !ip.equalsIgnoreCase("localhost") && this.plugin.hasJoinedIp(player.getName(), ip)) {
            player.kickPlayer("A player with the same IP is already in game!");
            return;
        }
        if (this.data.isAuthAvailable(name)) {
            if (Settings.isSessionsEnabled.booleanValue()) {
                PlayerAuth auth = this.data.getAuth(name);
                long timeout = Settings.getSessionTimeout * 60000;
                long lastLogin = auth.getLastLogin();
                long cur = new Date().getTime();
                if (!(cur - lastLogin >= timeout && timeout != 0 || auth.getIp().equals("198.18.0.1"))) {
                    if (auth.getNickname().equalsIgnoreCase(name) && auth.getIp().equals(ip)) {
                        if (PlayerCache.getInstance().getAuth(name) != null) {
                            PlayerCache.getInstance().updatePlayer(auth);
                        } else {
                            PlayerCache.getInstance().addPlayer(auth);
                            this.data.setLogged(name);
                        }
                        this.m._((CommandSender)player, "valid_session");
                        this.utils.setGroup(player, Utils.groupType.LOGGEDIN);
                        this.plugin.getServer().getPluginManager().callEvent((Event)new SessionEvent(auth, true));
                        return;
                    }
                    if (!Settings.sessionExpireOnIpChange.booleanValue()) {
                        GameMode gM = gameMode.get(name);
                        causeByAuthMe.put(name, true);
                        player.setGameMode(gM);
                        causeByAuthMe.put(name, false);
                        player.kickPlayer(this.m._("unvalid_session")[0]);
                        return;
                    }
                    if (!auth.getNickname().equalsIgnoreCase(name)) {
                        GameMode gM = gameMode.get(name);
                        causeByAuthMe.put(name, true);
                        player.setGameMode(gM);
                        causeByAuthMe.put(name, false);
                        player.kickPlayer(this.m._("unvalid_session")[0]);
                        return;
                    }
                    if (Settings.isForceSurvivalModeEnabled.booleanValue() && !Settings.forceOnlyAfterLogin.booleanValue()) {
                        causeByAuthMe.put(name, true);
                        Utils.forceGM(player);
                        causeByAuthMe.put(name, false);
                    }
                    PlayerCache.getInstance().removePlayer(name);
                    this.data.setUnlogged(name);
                } else {
                    PlayerCache.getInstance().removePlayer(name);
                    this.data.setUnlogged(name);
                }
            }
            if (Settings.isForceSurvivalModeEnabled.booleanValue() && !Settings.forceOnlyAfterLogin.booleanValue()) {
                causeByAuthMe.put(name, true);
                Utils.forceGM(player);
                causeByAuthMe.put(name, false);
            }
            if (!Settings.noTeleport.booleanValue() && (Settings.isTeleportToSpawnEnabled.booleanValue() || Settings.isForceSpawnLocOnJoinEnabled.booleanValue() && Settings.getForcedWorlds.contains(player.getWorld().getName()))) {
                tpEvent = new SpawnTeleportEvent(player, player.getLocation(), spawnLoc, PlayerCache.getInstance().isAuthenticated(name));
                this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                if (!tpEvent.isCancelled() && player != null && player.isOnline() && tpEvent.getTo() != null && tpEvent.getTo().getWorld() != null) {
                    player.teleport(tpEvent.getTo());
                }
            }
            this.placePlayerSafely(player, spawnLoc);
            LimboCache.getInstance().updateLimboPlayer(player);
            DataFileCache dataFile = new DataFileCache(LimboCache.getInstance().getLimboPlayer(name).getInventory(), LimboCache.getInstance().getLimboPlayer(name).getArmour());
            this.playerBackup.createCache(player, dataFile, LimboCache.getInstance().getLimboPlayer(name).getGroup(), LimboCache.getInstance().getLimboPlayer(name).getOperator(), LimboCache.getInstance().getLimboPlayer(name).isFlying());
        } else {
            if (Settings.isForceSurvivalModeEnabled.booleanValue() && !Settings.forceOnlyAfterLogin.booleanValue()) {
                causeByAuthMe.put(name, true);
                Utils.forceGM(player);
                causeByAuthMe.put(name, false);
            }
            if (!Settings.unRegisteredGroup.isEmpty()) {
                this.utils.setGroup(player, Utils.groupType.UNREGISTERED);
            }
            if (!Settings.noTeleport.booleanValue() && (Settings.isTeleportToSpawnEnabled.booleanValue() || Settings.isForceSpawnLocOnJoinEnabled.booleanValue() && Settings.getForcedWorlds.contains(player.getWorld().getName()))) {
                tpEvent = new SpawnTeleportEvent(player, player.getLocation(), spawnLoc, PlayerCache.getInstance().isAuthenticated(name));
                this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                if (!tpEvent.isCancelled() && player != null && player.isOnline() && tpEvent.getTo() != null && tpEvent.getTo().getWorld() != null) {
                    player.teleport(tpEvent.getTo());
                }
            }
            if (!Settings.isForcedRegistrationEnabled.booleanValue()) {
                return;
            }
        }
        if (Settings.protectInventoryBeforeLogInEnabled.booleanValue()) {
            try {
                LimboPlayer limbo = LimboCache.getInstance().getLimboPlayer(player.getName());
                ProtectInventoryEvent ev = new ProtectInventoryEvent(player, limbo.getInventory(), limbo.getArmour());
                this.plugin.getServer().getPluginManager().callEvent((Event)ev);
                if (ev.isCancelled()) {
                    if (!Settings.noConsoleSpam.booleanValue()) {
                        ConsoleLogger.info("ProtectInventoryEvent has been cancelled for " + player.getName() + " ...");
                    }
                } else {
                    API.setPlayerInventory(player, ev.getEmptyInventory(), ev.getEmptyArmor());
                }
            }
            catch (NullPointerException ex) {
                // empty catch block
            }
        }
        String[] msg = Settings.emailRegistration != false ? (this.data.isAuthAvailable(name) ? this.m._("login_msg") : this.m._("reg_email_msg")) : (this.data.isAuthAvailable(name) ? this.m._("login_msg") : this.m._("reg_msg"));
        int time = Settings.getRegistrationTimeout * 20;
        int msgInterval = Settings.getWarnMessageInterval;
        if (time != 0) {
            int id = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new TimeoutTask(this.plugin, name), (long)time);
            if (!LimboCache.getInstance().hasLimboPlayer(name)) {
                LimboCache.getInstance().addLimboPlayer(player);
            }
            LimboCache.getInstance().getLimboPlayer(name).setTimeoutTaskId(id);
        }
        if (!LimboCache.getInstance().hasLimboPlayer(name)) {
            LimboCache.getInstance().addLimboPlayer(player);
        }
        if (this.data.isAuthAvailable(name)) {
            this.utils.setGroup(player, Utils.groupType.NOTLOGGEDIN);
        } else {
            this.utils.setGroup(player, Utils.groupType.UNREGISTERED);
        }
        if (player.isOp()) {
            player.setOp(false);
        }
        if (!Settings.isMovementAllowed.booleanValue()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        int msgT = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, name, msg, msgInterval));
        LimboCache.getInstance().getLimboPlayer(name).setMessageTaskId(msgT);
        player.setNoDamageTicks(Settings.getRegistrationTimeout * 20);
        if (Settings.useEssentialsMotd.booleanValue()) {
            player.performCommand("motd");
        }
        if (Settings.applyBlindEffect.booleanValue()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Settings.getRegistrationTimeout * 20, 2));
        }
        if (Settings.enableProtection.booleanValue() || Settings.delayJoinMessage.booleanValue()) {
            joinMessage.put(name, event.getJoinMessage());
            event.setJoinMessage(null);
        }
    }

    private void placePlayerSafely(Player player, Location spawnLoc) {
        if (!Settings.noTeleport.booleanValue()) {
            return;
        }
        if (Settings.isTeleportToSpawnEnabled.booleanValue() || Settings.isForceSpawnLocOnJoinEnabled.booleanValue() && Settings.getForcedWorlds.contains(player.getWorld().getName())) {
            return;
        }
        Block b = player.getLocation().getBlock();
        if (b.getType() == Material.PORTAL || b.getType() == Material.ENDER_PORTAL || b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA) {
            this.m._((CommandSender)player, "unsafe_spawn");
            if (spawnLoc.getWorld() != null) {
                player.teleport(spawnLoc);
            }
            return;
        }
        Block c = player.getLocation().add(0.0, 1.0, 0.0).getBlock();
        if (c.getType() == Material.PORTAL || c.getType() == Material.ENDER_PORTAL || c.getType() == Material.LAVA || c.getType() == Material.STATIONARY_LAVA) {
            this.m._((CommandSender)player, "unsafe_spawn");
            if (spawnLoc.getWorld() != null) {
                player.teleport(spawnLoc);
            }
            return;
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        Location loc = player.getLocation();
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        String ip = this.plugin.getIP(player);
        if (PlayerCache.getInstance().isAuthenticated(name) && !player.isDead()) {
            PlayerAuth auth;
            if (Settings.isSaveQuitLocationEnabled.booleanValue() && this.data.isAuthAvailable(name)) {
                auth = new PlayerAuth(name, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
                try {
                    this.data.updateQuitLoc(auth);
                }
                catch (NullPointerException npe) {
                    // empty catch block
                }
            }
            auth = new PlayerAuth(name, ip, System.currentTimeMillis());
            this.data.updateSession(auth);
        }
        if (this.data.getAuth(name) != null && !PlayerCache.getInstance().isAuthenticated(name) && Settings.enableProtection.booleanValue()) {
            event.setQuitMessage(null);
        }
        if (LimboCache.getInstance().hasLimboPlayer(name)) {
            LimboPlayer limbo = LimboCache.getInstance().getLimboPlayer(name);
            if (Settings.protectInventoryBeforeLogInEnabled.booleanValue() && player.hasPlayedBefore()) {
                RestoreInventoryEvent ev = new RestoreInventoryEvent(player, limbo.getInventory(), limbo.getArmour());
                this.plugin.getServer().getPluginManager().callEvent((Event)ev);
                if (!ev.isCancelled()) {
                    API.setPlayerInventory(player, ev.getInventory(), ev.getArmor());
                }
            }
            this.utils.addNormal(player, limbo.getGroup());
            player.setOp(limbo.getOperator());
            if (player.getGameMode() != GameMode.CREATIVE && !Settings.isMovementAllowed.booleanValue()) {
                player.setAllowFlight(limbo.isFlying());
                player.setFlying(limbo.isFlying());
            }
            this.plugin.getServer().getScheduler().cancelTask(limbo.getTimeoutTaskId());
            this.plugin.getServer().getScheduler().cancelTask(limbo.getMessageTaskId());
            LimboCache.getInstance().deleteLimboPlayer(name);
            if (this.playerBackup.doesCacheExist(player)) {
                this.playerBackup.removeCache(player);
            }
        }
        PlayerCache.getInstance().removePlayer(name);
        this.data.setUnlogged(name);
        try {
            player.getVehicle().eject();
        }
        catch (NullPointerException ex) {
            // empty catch block
        }
        if (gameMode.containsKey(name)) {
            gameMode.remove(name);
        }
        player.saveData();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (Settings.isForceSingleSessionEnabled.booleanValue() && event.getReason().contains("You logged in from another location")) {
            event.setCancelled(true);
            return;
        }
        String name = player.getName();
        String ip = this.plugin.getIP(player);
        if (PlayerCache.getInstance().isAuthenticated(name) && !player.isDead()) {
            PlayerAuth auth;
            if (Settings.isSaveQuitLocationEnabled.booleanValue() && this.data.isAuthAvailable(name)) {
                auth = new PlayerAuth(name, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
                try {
                    this.data.updateQuitLoc(auth);
                }
                catch (NullPointerException npe) {
                    // empty catch block
                }
            }
            auth = new PlayerAuth(name, ip, System.currentTimeMillis());
            this.data.updateSession(auth);
        }
        if (this.data.getAuth(name) != null && !PlayerCache.getInstance().isAuthenticated(name) && Settings.enableProtection.booleanValue()) {
            event.setLeaveMessage(null);
        }
        if (LimboCache.getInstance().hasLimboPlayer(name)) {
            LimboPlayer limbo = LimboCache.getInstance().getLimboPlayer(name);
            if (Settings.protectInventoryBeforeLogInEnabled.booleanValue()) {
                try {
                    RestoreInventoryEvent ev = new RestoreInventoryEvent(player, limbo.getInventory(), limbo.getArmour());
                    this.plugin.getServer().getPluginManager().callEvent((Event)ev);
                    if (!ev.isCancelled()) {
                        API.setPlayerInventory(player, ev.getInventory(), ev.getArmor());
                    }
                }
                catch (NullPointerException npe) {
                    ConsoleLogger.showError("Problem while restore " + name + " inventory after a kick");
                }
            }
            if (!Settings.noTeleport.booleanValue()) {
                try {
                    AuthMeTeleportEvent tpEvent = new AuthMeTeleportEvent(player, limbo.getLoc());
                    this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                    if (!tpEvent.isCancelled() && player != null && player.isOnline() && tpEvent.getTo() != null && tpEvent.getTo().getWorld() != null) {
                        player.teleport(tpEvent.getTo());
                    }
                }
                catch (NullPointerException npe) {
                    // empty catch block
                }
            }
            this.utils.addNormal(player, limbo.getGroup());
            player.setOp(limbo.getOperator());
            if (player.getGameMode() != GameMode.CREATIVE && !Settings.isMovementAllowed.booleanValue()) {
                player.setAllowFlight(limbo.isFlying());
                player.setFlying(limbo.isFlying());
            }
            this.plugin.getServer().getScheduler().cancelTask(limbo.getTimeoutTaskId());
            this.plugin.getServer().getScheduler().cancelTask(limbo.getMessageTaskId());
            LimboCache.getInstance().deleteLimboPlayer(name);
            if (this.playerBackup.doesCacheExist(player)) {
                this.playerBackup.removeCache(player);
            }
        }
        PlayerCache.getInstance().removePlayer(name);
        this.data.setUnlogged(name);
        if (gameMode.containsKey(name)) {
            gameMode.remove(name);
        }
        try {
            player.getVehicle().eject();
        }
        catch (NullPointerException ex) {
            // empty catch block
        }
        player.saveData();
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
        event.setUseItemInHand(Event.Result.DENY);
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInventoryOpen(InventoryOpenEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = (Player)event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
        player.closeInventory();
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled() || event.getWhoClicked() == null) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin) || Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent event) {
        if (event.isCancelled() || event.getPlayer() == null || event == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer() == null || event == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        Location spawn = this.plugin.getSpawnLocation(player);
        if (Settings.isSaveQuitLocationEnabled.booleanValue() && this.data.isAuthAvailable(name)) {
            PlayerAuth auth = new PlayerAuth(name, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getWorld().getName());
            try {
                this.data.updateQuitLoc(auth);
            }
            catch (NullPointerException npe) {
                // empty catch block
            }
        }
        event.setRespawnLocation(spawn);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPlayer() == null || event == null) {
            return;
        }
        if (!Settings.isForceSurvivalModeEnabled.booleanValue()) {
            return;
        }
        Player player = event.getPlayer();
        if (this.plugin.authmePermissible(player, "authme.bypassforcesurvival")) {
            return;
        }
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (this.plugin.getCitizensCommunicator().isNPC((Entity)player, this.plugin)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        if (causeByAuthMe.containsKey(name) && causeByAuthMe.get(name).booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

}

