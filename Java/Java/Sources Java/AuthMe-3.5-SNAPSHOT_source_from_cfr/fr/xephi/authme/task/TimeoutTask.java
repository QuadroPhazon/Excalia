/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.task;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.listener.AuthMePlayerListener;
import fr.xephi.authme.settings.Messages;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TimeoutTask
implements Runnable {
    private AuthMe plugin;
    private String name;
    private Messages m = Messages.getInstance();
    private FileCache playerCache;

    public TimeoutTask(AuthMe plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.playerCache = new FileCache(plugin);
    }

    public String getName() {
        return this.name;
    }

    public void run() {
        if (PlayerCache.getInstance().isAuthenticated(this.name)) {
            return;
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            GameMode gm;
            if (!player.getName().equals(this.name)) continue;
            if (LimboCache.getInstance().hasLimboPlayer(this.name)) {
                LimboPlayer inv = LimboCache.getInstance().getLimboPlayer(this.name);
                player.getServer().getScheduler().cancelTask(inv.getMessageTaskId());
                player.getServer().getScheduler().cancelTask(inv.getTimeoutTaskId());
                if (this.playerCache.doesCacheExist(player)) {
                    this.playerCache.removeCache(player);
                }
            }
            if ((gm = AuthMePlayerListener.gameMode.get(this.name)) != null) {
                player.setGameMode(gm);
                ConsoleLogger.info("Set " + player.getName() + " to gamemode: " + gm.name());
            }
            player.kickPlayer(this.m._("timeout")[0]);
            break;
        }
    }
}

