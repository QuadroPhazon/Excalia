/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.task;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MessageTask
implements Runnable {
    private AuthMe plugin;
    private String name;
    private String[] msg;
    private int interval;

    public MessageTask(AuthMe plugin, String name, String[] strings, int interval) {
        this.plugin = plugin;
        this.name = name;
        this.msg = strings;
        this.interval = interval;
    }

    public void run() {
        if (PlayerCache.getInstance().isAuthenticated(this.name)) {
            return;
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (!player.getName().equals(this.name)) continue;
            for (String ms : this.msg) {
                player.sendMessage(ms);
            }
            BukkitScheduler sched = this.plugin.getServer().getScheduler();
            int late = sched.scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)this, (long)(this.interval * 20));
            if (!LimboCache.getInstance().hasLimboPlayer(this.name)) continue;
            LimboCache.getInstance().getLimboPlayer(this.name).setMessageTaskId(late);
        }
    }
}

