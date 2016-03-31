/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.ITarget;
import com.earth2me.essentials.LocationTarget;
import com.earth2me.essentials.PlayerTarget;
import com.earth2me.essentials.TimedTeleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.LocationUtil;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.ITeleport;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;

public class Teleport
implements ITeleport {
    private final IUser teleportOwner;
    private final IEssentials ess;
    private TimedTeleport timedTeleport;

    public Teleport(IUser user, IEssentials ess) {
        this.teleportOwner = user;
        this.ess = ess;
    }

    public void cooldown(boolean check) throws Exception {
        GregorianCalendar time = new GregorianCalendar();
        if (this.teleportOwner.getLastTeleportTimestamp() > 0) {
            double cooldown = this.ess.getSettings().getTeleportCooldown();
            GregorianCalendar earliestTime = new GregorianCalendar();
            earliestTime.add(13, - (int)cooldown);
            earliestTime.add(14, - (int)(cooldown * 1000.0 % 1000.0));
            long earliestLong = earliestTime.getTimeInMillis();
            Long lastTime = this.teleportOwner.getLastTeleportTimestamp();
            if (lastTime > time.getTimeInMillis()) {
                this.teleportOwner.setLastTeleportTimestamp(time.getTimeInMillis());
                return;
            }
            if (lastTime > earliestLong && !this.teleportOwner.isAuthorized("essentials.teleport.cooldown.bypass")) {
                time.setTimeInMillis(lastTime);
                time.add(13, (int)cooldown);
                time.add(14, (int)(cooldown * 1000.0 % 1000.0));
                throw new Exception(I18n._("timeBeforeTeleport", DateUtil.formatDateDiff(time.getTimeInMillis())));
            }
        }
        if (!check) {
            this.teleportOwner.setLastTeleportTimestamp(time.getTimeInMillis());
        }
    }

    private void warnUser(IUser user, double delay) {
        GregorianCalendar c = new GregorianCalendar();
        c.add(13, (int)delay);
        c.add(14, (int)(delay * 1000.0 % 1000.0));
        user.sendMessage(I18n._("dontMoveMessage", DateUtil.formatDateDiff(c.getTimeInMillis())));
    }

    @Override
    public void now(Location loc, boolean cooldown, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        if (cooldown) {
            this.cooldown(false);
        }
        LocationTarget target = new LocationTarget(loc);
        this.now(this.teleportOwner, target, cause);
    }

    @Override
    public void now(Player entity, boolean cooldown, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        if (cooldown) {
            this.cooldown(false);
        }
        PlayerTarget target = new PlayerTarget(entity);
        this.now(this.teleportOwner, target, cause);
        this.teleportOwner.sendMessage(I18n._("teleporting", target.getLocation().getWorld().getName(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected void now(IUser teleportee, ITarget target, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        this.cancel(false);
        teleportee.setLastLocation();
        Location loc = target.getLocation();
        if (LocationUtil.isBlockUnsafe(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
            if (!this.ess.getSettings().isTeleportSafetyEnabled()) throw new Exception(I18n._("unsafeTeleportDestination", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            if (teleportee.getBase().isInsideVehicle()) {
                teleportee.getBase().leaveVehicle();
            }
            teleportee.getBase().teleport(LocationUtil.getSafeDestination(teleportee, loc), cause);
            return;
        } else {
            if (teleportee.getBase().isInsideVehicle()) {
                teleportee.getBase().leaveVehicle();
            }
            teleportee.getBase().teleport(LocationUtil.getRoundedDestination(loc), cause);
        }
    }

    @Deprecated
    @Override
    public void teleport(Location loc, Trade chargeFor) throws Exception {
        this.teleport(loc, chargeFor, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public void teleport(Location loc, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        this.teleport(this.teleportOwner, new LocationTarget(loc), chargeFor, cause);
    }

    @Override
    public void teleport(Player entity, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        PlayerTarget target = new PlayerTarget(entity);
        this.teleport(this.teleportOwner, target, chargeFor, cause);
        this.teleportOwner.sendMessage(I18n._("teleporting", target.getLocation().getWorld().getName(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
    }

    @Override
    public void teleportPlayer(IUser teleportee, Location loc, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        this.teleport(teleportee, new LocationTarget(loc), chargeFor, cause);
    }

    @Override
    public void teleportPlayer(IUser teleportee, Player entity, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        PlayerTarget target = new PlayerTarget(entity);
        this.teleport(teleportee, target, chargeFor, cause);
        teleportee.sendMessage(I18n._("teleporting", target.getLocation().getWorld().getName(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
        this.teleportOwner.sendMessage(I18n._("teleporting", target.getLocation().getWorld().getName(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
    }

    private void teleport(IUser teleportee, ITarget target, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        double delay = this.ess.getSettings().getTeleportDelay();
        Trade cashCharge = chargeFor;
        if (chargeFor != null) {
            chargeFor.isAffordableFor(this.teleportOwner);
            if (!chargeFor.getCommandCost(this.teleportOwner).equals(BigDecimal.ZERO)) {
                cashCharge = new Trade(chargeFor.getCommandCost(this.teleportOwner), this.ess);
            }
        }
        this.cooldown(true);
        if (delay <= 0.0 || this.teleportOwner.isAuthorized("essentials.teleport.timer.bypass") || teleportee.isAuthorized("essentials.teleport.timer.bypass")) {
            this.cooldown(false);
            this.now(teleportee, target, cause);
            if (cashCharge != null) {
                cashCharge.charge(this.teleportOwner);
            }
            return;
        }
        this.cancel(false);
        this.warnUser(teleportee, delay);
        this.initTimer((long)(delay * 1000.0), teleportee, target, cashCharge, cause, false);
    }

    @Override
    public void respawn(Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        double delay = this.ess.getSettings().getTeleportDelay();
        if (chargeFor != null) {
            chargeFor.isAffordableFor(this.teleportOwner);
        }
        this.cooldown(true);
        if (delay <= 0.0 || this.teleportOwner.isAuthorized("essentials.teleport.timer.bypass")) {
            this.cooldown(false);
            this.respawnNow(this.teleportOwner, cause);
            if (chargeFor != null) {
                chargeFor.charge(this.teleportOwner);
            }
            return;
        }
        this.cancel(false);
        this.warnUser(this.teleportOwner, delay);
        this.initTimer((long)(delay * 1000.0), this.teleportOwner, null, chargeFor, cause, true);
    }

    protected void respawnNow(IUser teleportee, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        Player player = teleportee.getBase();
        Location bed = player.getBedSpawnLocation();
        if (bed != null) {
            this.now(teleportee, new LocationTarget(bed), cause);
        } else {
            if (this.ess.getSettings().isDebug()) {
                this.ess.getLogger().info("Could not find bed spawn, forcing respawn event.");
            }
            PlayerRespawnEvent pre = new PlayerRespawnEvent(player, player.getWorld().getSpawnLocation(), false);
            this.ess.getServer().getPluginManager().callEvent((Event)pre);
            this.now(teleportee, new LocationTarget(pre.getRespawnLocation()), cause);
        }
    }

    @Override
    public void warp(IUser teleportee, String warp, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause) throws Exception {
        Location loc = this.ess.getWarps().getWarp(warp);
        teleportee.sendMessage(I18n._("warpingTo", warp, loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        this.teleport(teleportee, new LocationTarget(loc), chargeFor, cause);
    }

    @Override
    public void back(Trade chargeFor) throws Exception {
        Location loc = this.teleportOwner.getLastLocation();
        this.teleportOwner.sendMessage(I18n._("backUsageMsg", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        this.teleport(this.teleportOwner, new LocationTarget(loc), chargeFor, PlayerTeleportEvent.TeleportCause.COMMAND);
    }

    @Override
    public void back() throws Exception {
        this.now(this.teleportOwner, new LocationTarget(this.teleportOwner.getLastLocation()), PlayerTeleportEvent.TeleportCause.COMMAND);
    }

    private void cancel(boolean notifyUser) {
        if (this.timedTeleport != null) {
            this.timedTeleport.cancelTimer(notifyUser);
            this.timedTeleport = null;
        }
    }

    private void initTimer(long delay, IUser teleportUser, ITarget target, Trade chargeFor, PlayerTeleportEvent.TeleportCause cause, boolean respawn) {
        this.timedTeleport = new TimedTeleport(this.teleportOwner, this.ess, this, delay, teleportUser, target, chargeFor, cause, respawn);
    }
}

