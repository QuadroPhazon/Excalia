/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.scheduler.BukkitScheduler
 */
package com.earth2me.essentials;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ITarget;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class TimedTeleport
implements Runnable {
    private static final double MOVE_CONSTANT = 0.3;
    private final IUser teleportOwner;
    private final IEssentials ess;
    private final Teleport teleport;
    private final String timer_teleportee;
    private int timer_task = -1;
    private final long timer_started;
    private final long timer_delay;
    private double timer_health;
    private final long timer_initX;
    private final long timer_initY;
    private final long timer_initZ;
    private final ITarget timer_teleportTarget;
    private final boolean timer_respawn;
    private final boolean timer_canMove;
    private final Trade timer_chargeFor;
    private final PlayerTeleportEvent.TeleportCause timer_cause;

    public TimedTeleport(IUser user, IEssentials ess, Teleport teleport, long delay, IUser teleportUser, ITarget target, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause, boolean respawn) {
        this.teleportOwner = user;
        this.ess = ess;
        this.teleport = teleport;
        this.timer_started = System.currentTimeMillis();
        this.timer_delay = delay;
        this.timer_health = teleportUser.getBase().getHealth();
        this.timer_initX = Math.round(teleportUser.getBase().getLocation().getX() * 0.3);
        this.timer_initY = Math.round(teleportUser.getBase().getLocation().getY() * 0.3);
        this.timer_initZ = Math.round(teleportUser.getBase().getLocation().getZ() * 0.3);
        this.timer_teleportee = teleportUser.getName();
        this.timer_teleportTarget = target;
        this.timer_chargeFor = chargeFor;
        this.timer_cause = cause;
        this.timer_respawn = respawn;
        this.timer_canMove = user.isAuthorized("essentials.teleport.timer.move");
        this.timer_task = ess.scheduleSyncRepeatingTask(this, 20, 20);
    }

    @Override
    public void run() {
        if (this.teleportOwner == null || !this.teleportOwner.getBase().isOnline() || this.teleportOwner.getBase().getLocation() == null) {
            this.cancelTimer(false);
            return;
        }
        User teleportUser = this.ess.getUser(this.timer_teleportee);
        if (teleportUser == null || !teleportUser.getBase().isOnline()) {
            this.cancelTimer(false);
            return;
        }
        Location currLocation = teleportUser.getBase().getLocation();
        if (currLocation == null) {
            this.cancelTimer(false);
            return;
        }
        if (!(this.timer_canMove || Math.round(currLocation.getX() * 0.3) == this.timer_initX && Math.round(currLocation.getY() * 0.3) == this.timer_initY && Math.round(currLocation.getZ() * 0.3) == this.timer_initZ && teleportUser.getBase().getHealth() >= this.timer_health)) {
            this.cancelTimer(true);
            return;
        }
        this.timer_health = teleportUser.getBase().getHealth();
        long now = System.currentTimeMillis();
        if (now > this.timer_started + this.timer_delay) {
            block13 : {
                try {
                    this.teleport.cooldown(false);
                }
                catch (Exception ex) {
                    this.teleportOwner.sendMessage(I18n._("cooldownWithMessage", ex.getMessage()));
                    if (this.teleportOwner == teleportUser) break block13;
                    teleportUser.sendMessage(I18n._("cooldownWithMessage", ex.getMessage()));
                }
            }
            try {
                this.cancelTimer(false);
                teleportUser.sendMessage(I18n._("teleportationCommencing", new Object[0]));
                if (this.timer_chargeFor != null) {
                    this.timer_chargeFor.isAffordableFor(this.teleportOwner);
                }
                if (this.timer_respawn) {
                    this.teleport.respawnNow(teleportUser, this.timer_cause);
                } else {
                    this.teleport.now(teleportUser, this.timer_teleportTarget, this.timer_cause);
                }
                if (this.timer_chargeFor != null) {
                    this.timer_chargeFor.charge(this.teleportOwner);
                }
            }
            catch (Exception ex) {
                this.ess.showError(this.teleportOwner.getSource(), ex, "\\ teleport");
            }
        }
    }

    public void cancelTimer(boolean notifyUser) {
        if (this.timer_task == -1) {
            return;
        }
        try {
            this.ess.getServer().getScheduler().cancelTask(this.timer_task);
            if (notifyUser) {
                this.teleportOwner.sendMessage(I18n._("pendingTeleportCancelled", new Object[0]));
                if (this.timer_teleportee != null && !this.timer_teleportee.equals(this.teleportOwner.getName())) {
                    this.ess.getUser(this.timer_teleportee).sendMessage(I18n._("pendingTeleportCancelled", new Object[0]));
                }
            }
        }
        finally {
            this.timer_task = -1;
        }
    }
}

