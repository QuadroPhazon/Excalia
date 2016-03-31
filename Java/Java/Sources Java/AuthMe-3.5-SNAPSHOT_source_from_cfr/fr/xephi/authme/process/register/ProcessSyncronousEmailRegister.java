/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.muizers.Notifications.Notification
 *  me.muizers.Notifications.Notifications
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package fr.xephi.authme.process.register;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.task.MessageTask;
import fr.xephi.authme.task.TimeoutTask;
import me.muizers.Notifications.Notification;
import me.muizers.Notifications.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ProcessSyncronousEmailRegister
implements Runnable {
    protected Player player;
    protected String name;
    private AuthMe plugin;
    private Messages m = Messages.getInstance();

    public ProcessSyncronousEmailRegister(Player player, AuthMe plugin) {
        this.player = player;
        this.name = player.getName();
        this.plugin = plugin;
    }

    public void run() {
        if (!Settings.getRegisteredGroup.isEmpty()) {
            Utils.getInstance().setGroup(this.player, Utils.groupType.REGISTERED);
        }
        this.m._((CommandSender)this.player, "vb_nonActiv");
        int time = Settings.getRegistrationTimeout * 20;
        int msgInterval = Settings.getWarnMessageInterval;
        if (time != 0) {
            Bukkit.getScheduler().cancelTask(LimboCache.getInstance().getLimboPlayer(this.name).getTimeoutTaskId());
            int id = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new TimeoutTask(this.plugin, this.name), (long)time);
            LimboCache.getInstance().getLimboPlayer(this.name).setTimeoutTaskId(id);
        }
        Bukkit.getScheduler().cancelTask(LimboCache.getInstance().getLimboPlayer(this.name).getMessageTaskId());
        int nwMsg = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, this.name, this.m._("login_msg"), msgInterval));
        LimboCache.getInstance().getLimboPlayer(this.name).setMessageTaskId(nwMsg);
        this.player.saveData();
        if (!Settings.noConsoleSpam.booleanValue()) {
            ConsoleLogger.info(this.player.getName() + " registered " + this.plugin.getIP(this.player));
        }
        if (this.plugin.notifications != null) {
            this.plugin.notifications.showNotification(new Notification("[AuthMe] " + this.player.getName() + " has registered by email!"));
        }
    }
}

