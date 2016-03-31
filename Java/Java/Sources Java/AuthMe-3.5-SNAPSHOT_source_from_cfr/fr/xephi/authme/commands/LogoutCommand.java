/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.muizers.Notifications.Notification
 *  me.muizers.Notifications.Notifications
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.commands;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.backup.DataFileCache;
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.AuthMeTeleportEvent;
import fr.xephi.authme.events.LogoutEvent;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import me.muizers.Notifications.Notification;
import me.muizers.Notifications.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class LogoutCommand
implements CommandExecutor {
    private Messages m = Messages.getInstance();
    private AuthMe plugin;
    private DataSource database;
    private Utils utils = Utils.getInstance();
    private FileCache playerBackup;

    public LogoutCommand(AuthMe plugin, DataSource database) {
        this.plugin = plugin;
        this.database = database;
        this.playerBackup = new FileCache(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!this.plugin.authmePermissible(sender, "authme." + label.toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        final Player player = (Player)sender;
        String name = player.getName();
        if (!PlayerCache.getInstance().isAuthenticated(name)) {
            this.m._((CommandSender)player, "not_logged_in");
            return true;
        }
        PlayerAuth auth = PlayerCache.getInstance().getAuth(name);
        if (Settings.isSessionsEnabled.booleanValue()) {
            auth.setLastLogin(0);
        }
        this.database.updateSession(auth);
        auth.setQuitLocX(player.getLocation().getX());
        auth.setQuitLocY(player.getLocation().getY());
        auth.setQuitLocZ(player.getLocation().getZ());
        auth.setWorld(player.getWorld().getName());
        this.database.updateQuitLoc(auth);
        PlayerCache.getInstance().removePlayer(name);
        this.database.setUnlogged(name);
        if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.noTeleport.booleanValue()) {
            Location spawnLoc = this.plugin.getSpawnLocation(player);
            AuthMeTeleportEvent tpEvent = new AuthMeTeleportEvent(player, spawnLoc);
            this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
            if (!tpEvent.isCancelled() && tpEvent.getTo() != null) {
                player.teleport(tpEvent.getTo());
            }
        }
        if (LimboCache.getInstance().hasLimboPlayer(name)) {
            LimboCache.getInstance().deleteLimboPlayer(name);
        }
        LimboCache.getInstance().addLimboPlayer(player);
        this.utils.setGroup(player, Utils.groupType.NOTLOGGEDIN);
        if (Settings.protectInventoryBeforeLogInEnabled.booleanValue()) {
            player.getInventory().clear();
            DataFileCache playerData = new DataFileCache(LimboCache.getInstance().getLimboPlayer(name).getInventory(), LimboCache.getInstance().getLimboPlayer(name).getArmour());
            this.playerBackup.createCache(player, playerData, LimboCache.getInstance().getLimboPlayer(name).getGroup(), LimboCache.getInstance().getLimboPlayer(name).getOperator(), LimboCache.getInstance().getLimboPlayer(name).isFlying());
        }
        int delay = Settings.getRegistrationTimeout * 20;
        int interval = Settings.getWarnMessageInterval;
        BukkitScheduler sched = sender.getServer().getScheduler();
        if (delay != 0) {
            int id = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new TimeoutTask(this.plugin, name), (long)delay);
            LimboCache.getInstance().getLimboPlayer(name).setTimeoutTaskId(id);
        }
        int msgT = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, name, this.m._("login_msg"), interval));
        LimboCache.getInstance().getLimboPlayer(name).setMessageTaskId(msgT);
        try {
            if (player.isInsideVehicle()) {
                player.getVehicle().eject();
            }
        }
        catch (NullPointerException npe) {
            // empty catch block
        }
        if (Settings.applyBlindEffect.booleanValue()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Settings.getRegistrationTimeout * 20, 2));
        }
        player.setOp(false);
        player.setAllowFlight(true);
        player.setFlying(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

            public void run() {
                Bukkit.getServer().getPluginManager().callEvent((Event)new LogoutEvent(player));
            }
        });
        this.m._((CommandSender)player, "logout");
        ConsoleLogger.info(player.getDisplayName() + " logged out");
        if (this.plugin.notifications != null) {
            this.plugin.notifications.showNotification(new Notification("[AuthMe] " + player.getName() + " logged out!"));
        }
        return true;
    }

}

