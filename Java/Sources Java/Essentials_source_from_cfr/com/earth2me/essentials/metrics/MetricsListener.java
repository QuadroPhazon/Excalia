/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.metrics;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.metrics.MetricsStarter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

public class MetricsListener
implements Listener {
    private final transient Server server;
    private final transient IEssentials ess;
    private final transient MetricsStarter starter;

    public MetricsListener(IEssentials parent, MetricsStarter starter) {
        this.ess = parent;
        this.server = parent.getServer();
        this.starter = starter;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        User player = this.ess.getUser(event.getPlayer());
        if (!this.ess.getSettings().isMetricsEnabled() && (player.isAuthorized("essentials.essentials") || player.isAuthorized("bukkit.broadcast.admin"))) {
            player.sendMessage("PluginMetrics collects minimal statistic data, starting in about 5 minutes.");
            player.sendMessage("To opt out, disabling metrics for all plugins, run /essentials opt-out");
            this.ess.getLogger().log(Level.INFO, "[Metrics] Admin join - Starting 5 minute opt-out period.");
            this.ess.getSettings().setMetricsEnabled(true);
            this.ess.runTaskLaterAsynchronously(this.starter, 6000);
        }
    }
}

