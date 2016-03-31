/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.scheduler.BukkitScheduler
 */
package com.earth2me.essentials;

import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class TNTExplodeListener
implements Listener,
Runnable {
    private final transient IEssentials ess;
    private transient boolean enabled = false;
    private transient int timer = -1;

    public TNTExplodeListener(IEssentials ess) {
        this.ess = ess;
    }

    public void enable() {
        if (!this.enabled) {
            this.enabled = true;
            this.timer = this.ess.scheduleSyncDelayedTask(this, 200);
            return;
        }
        if (this.timer != -1) {
            this.ess.getScheduler().cancelTask(this.timer);
            this.timer = this.ess.scheduleSyncDelayedTask(this, 200);
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!this.enabled) {
            return;
        }
        if (event.getEntity() instanceof LivingEntity) {
            return;
        }
        if (event.blockList().size() < 1) {
            return;
        }
        event.setCancelled(true);
        event.getLocation().getWorld().createExplosion(event.getLocation(), 0.0f);
    }

    @Override
    public void run() {
        this.enabled = false;
    }
}

