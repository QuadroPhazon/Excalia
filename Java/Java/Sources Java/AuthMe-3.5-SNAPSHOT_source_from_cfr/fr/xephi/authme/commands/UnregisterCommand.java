/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.muizers.Notifications.Notification
 *  me.muizers.Notifications.Notifications
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
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
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.SpawnTeleportEvent;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import java.security.NoSuchAlgorithmException;
import me.muizers.Notifications.Notification;
import me.muizers.Notifications.Notifications;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class UnregisterCommand
implements CommandExecutor {
    private Messages m = Messages.getInstance();
    public AuthMe plugin;
    private DataSource database;
    private FileCache playerCache;

    public UnregisterCommand(AuthMe plugin, DataSource database) {
        this.plugin = plugin;
        this.database = database;
        this.playerCache = new FileCache(plugin);
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!this.plugin.authmePermissible(sender, "authme." + label.toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        Player player = (Player)sender;
        String name = player.getName();
        if (!PlayerCache.getInstance().isAuthenticated(name)) {
            this.m._((CommandSender)player, "not_logged_in");
            return true;
        }
        if (args.length != 1) {
            this.m._((CommandSender)player, "usage_unreg");
            return true;
        }
        try {
            if (PasswordSecurity.comparePasswordWithHash(args[0], PlayerCache.getInstance().getAuth(name).getHash(), player.getName())) {
                if (!this.database.removeAuth(name)) {
                    player.sendMessage("error");
                    return true;
                }
                if (Settings.isForcedRegistrationEnabled.booleanValue()) {
                    if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.noTeleport.booleanValue()) {
                        Location spawn = this.plugin.getSpawnLocation(player);
                        SpawnTeleportEvent tpEvent = new SpawnTeleportEvent(player, player.getLocation(), spawn, false);
                        this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                        if (!tpEvent.isCancelled()) {
                            player.teleport(tpEvent.getTo());
                        }
                    }
                    player.getInventory().setContents(new ItemStack[36]);
                    player.getInventory().setArmorContents(new ItemStack[4]);
                    player.saveData();
                    PlayerCache.getInstance().removePlayer(player.getName());
                    if (!Settings.getRegisteredGroup.isEmpty()) {
                        Utils.getInstance().setGroup(player, Utils.groupType.UNREGISTERED);
                    }
                    LimboCache.getInstance().addLimboPlayer(player);
                    int delay = Settings.getRegistrationTimeout * 20;
                    int interval = Settings.getWarnMessageInterval;
                    BukkitScheduler sched = sender.getServer().getScheduler();
                    if (delay != 0) {
                        int id = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new TimeoutTask(this.plugin, name), (long)delay);
                        LimboCache.getInstance().getLimboPlayer(name).setTimeoutTaskId(id);
                    }
                    LimboCache.getInstance().getLimboPlayer(name).setMessageTaskId(sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, name, this.m._("reg_msg"), interval)));
                    this.m._((CommandSender)player, "unregistered");
                    ConsoleLogger.info(player.getDisplayName() + " unregistered himself");
                    if (this.plugin.notifications != null) {
                        this.plugin.notifications.showNotification(new Notification("[AuthMe] " + player.getName() + " unregistered himself!"));
                    }
                    return true;
                }
                if (!Settings.unRegisteredGroup.isEmpty()) {
                    Utils.getInstance().setGroup(player, Utils.groupType.UNREGISTERED);
                }
                PlayerCache.getInstance().removePlayer(player.getName());
                if (this.playerCache.doesCacheExist(player)) {
                    this.playerCache.removeCache(player);
                }
                if (Settings.applyBlindEffect.booleanValue()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Settings.getRegistrationTimeout * 20, 2));
                }
                this.m._((CommandSender)player, "unregistered");
                ConsoleLogger.info(player.getDisplayName() + " unregistered himself");
                if (this.plugin.notifications != null) {
                    this.plugin.notifications.showNotification(new Notification("[AuthMe] " + player.getName() + " unregistered himself!"));
                }
                if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.noTeleport.booleanValue()) {
                    Location spawn = this.plugin.getSpawnLocation(player);
                    SpawnTeleportEvent tpEvent = new SpawnTeleportEvent(player, player.getLocation(), spawn, false);
                    this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                    if (!tpEvent.isCancelled()) {
                        if (!tpEvent.getTo().getWorld().getChunkAt(tpEvent.getTo()).isLoaded()) {
                            tpEvent.getTo().getWorld().getChunkAt(tpEvent.getTo()).load();
                        }
                        player.teleport(tpEvent.getTo());
                    }
                }
                return true;
            }
            this.m._((CommandSender)player, "wrong_pwd");
        }
        catch (NoSuchAlgorithmException ex) {
            ConsoleLogger.showError(ex.getMessage());
            sender.sendMessage("Internal Error please read the server log");
        }
        return true;
    }
}

