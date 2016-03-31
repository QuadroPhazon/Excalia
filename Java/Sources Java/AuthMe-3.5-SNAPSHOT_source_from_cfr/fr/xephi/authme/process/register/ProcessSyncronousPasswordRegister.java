/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.muizers.Notifications.Notification
 *  me.muizers.Notifications.Notifications
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.process.register;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.events.AuthMeTeleportEvent;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterTeleportEvent;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.PlayersLogs;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import java.util.List;
import me.muizers.Notifications.Notification;
import me.muizers.Notifications.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class ProcessSyncronousPasswordRegister
implements Runnable {
    protected Player player;
    protected String name;
    private AuthMe plugin;
    private Messages m = Messages.getInstance();

    public ProcessSyncronousPasswordRegister(Player player, AuthMe plugin) {
        this.player = player;
        this.name = player.getName();
        this.plugin = plugin;
    }

    protected void forceCommands() {
        for (String command2 : Settings.forceRegisterCommands) {
            try {
                this.player.performCommand(command2.replace("%p", this.player.getName()));
            }
            catch (Exception e) {}
        }
        for (String command2 : Settings.forceRegisterCommandsAsConsole) {
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), command2.replace("%p", this.player.getName()));
        }
    }

    protected void forceLogin(Player player) {
        if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.noTeleport.booleanValue()) {
            Location spawnLoc = this.plugin.getSpawnLocation(player);
            AuthMeTeleportEvent tpEvent = new AuthMeTeleportEvent(player, spawnLoc);
            this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
            if (!tpEvent.isCancelled()) {
                if (!tpEvent.getTo().getWorld().getChunkAt(tpEvent.getTo()).isLoaded()) {
                    tpEvent.getTo().getWorld().getChunkAt(tpEvent.getTo()).load();
                }
                player.teleport(tpEvent.getTo());
            }
        }
        if (LimboCache.getInstance().hasLimboPlayer(this.name)) {
            LimboCache.getInstance().deleteLimboPlayer(this.name);
        }
        LimboCache.getInstance().addLimboPlayer(player);
        int delay = Settings.getRegistrationTimeout * 20;
        int interval = Settings.getWarnMessageInterval;
        BukkitScheduler sched = this.plugin.getServer().getScheduler();
        if (delay != 0) {
            int id = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new TimeoutTask(this.plugin, this.name), (long)delay);
            LimboCache.getInstance().getLimboPlayer(this.name).setTimeoutTaskId(id);
        }
        int msgT = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, this.name, this.m._("login_msg"), interval));
        LimboCache.getInstance().getLimboPlayer(this.name).setMessageTaskId(msgT);
        try {
            this.plugin.pllog.removePlayer(this.name);
            if (player.isInsideVehicle()) {
                player.getVehicle().eject();
            }
        }
        catch (NullPointerException npe) {
            // empty catch block
        }
    }

    public void run() {
        LimboPlayer limbo = LimboCache.getInstance().getLimboPlayer(this.name);
        if (limbo != null) {
            this.player.setGameMode(limbo.getGameMode());
            if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.noTeleport.booleanValue()) {
                Location loca = this.plugin.getSpawnLocation(this.player);
                RegisterTeleportEvent tpEvent = new RegisterTeleportEvent(this.player, loca);
                this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                if (!tpEvent.isCancelled()) {
                    if (!tpEvent.getTo().getWorld().getChunkAt(tpEvent.getTo()).isLoaded()) {
                        tpEvent.getTo().getWorld().getChunkAt(tpEvent.getTo()).load();
                    }
                    this.player.teleport(tpEvent.getTo());
                }
            }
            this.plugin.getServer().getScheduler().cancelTask(limbo.getTimeoutTaskId());
            this.plugin.getServer().getScheduler().cancelTask(limbo.getMessageTaskId());
            LimboCache.getInstance().deleteLimboPlayer(this.name);
        }
        if (!Settings.getRegisteredGroup.isEmpty()) {
            Utils.getInstance().setGroup(this.player, Utils.groupType.REGISTERED);
        }
        this.m._((CommandSender)this.player, "registered");
        if (!Settings.getmailAccount.isEmpty()) {
            this.m._((CommandSender)this.player, "add_email");
        }
        if (this.player.getGameMode() != GameMode.CREATIVE && !Settings.isMovementAllowed.booleanValue()) {
            this.player.setAllowFlight(false);
            this.player.setFlying(false);
        }
        if (Settings.applyBlindEffect.booleanValue()) {
            this.player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        Bukkit.getServer().getPluginManager().callEvent((Event)new LoginEvent(this.player, true));
        this.player.saveData();
        if (!Settings.noConsoleSpam.booleanValue()) {
            ConsoleLogger.info(this.player.getName() + " registered " + this.plugin.getIP(this.player));
        }
        if (this.plugin.notifications != null) {
            this.plugin.notifications.showNotification(new Notification("[AuthMe] " + this.player.getName() + " has registered!"));
        }
        if (Settings.forceRegKick.booleanValue()) {
            this.player.kickPlayer(this.m._("registered")[0]);
            return;
        }
        if (Settings.forceRegLogin.booleanValue()) {
            this.forceLogin(this.player);
            return;
        }
        if (Settings.useWelcomeMessage.booleanValue()) {
            if (Settings.broadcastWelcomeMessage.booleanValue()) {
                for (String s : Settings.welcomeMsg) {
                    Bukkit.getServer().broadcastMessage(this.plugin.replaceAllInfos(s, this.player));
                }
            } else {
                for (String s : Settings.welcomeMsg) {
                    this.player.sendMessage(this.plugin.replaceAllInfos(s, this.player));
                }
            }
        }
        this.forceCommands();
    }
}

