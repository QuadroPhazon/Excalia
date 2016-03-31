/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandtpaccept
extends EssentialsCommand {
    public Commandtpaccept() {
        super("tpaccept");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        User requester;
        try {
            requester = this.ess.getUser(user.getTeleportRequest());
        }
        catch (Exception ex) {
            throw new Exception(I18n._("noPendingRequest", new Object[0]));
        }
        if (!requester.isOnline()) {
            throw new Exception(I18n._("noPendingRequest", new Object[0]));
        }
        if (user.isTpRequestHere() && (!requester.isAuthorized("essentials.tpahere") && !requester.isAuthorized("essentials.tpaall") || user.getWorld() != requester.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + user.getWorld().getName()))) {
            throw new Exception(I18n._("noPendingRequest", new Object[0]));
        }
        if (!user.isTpRequestHere() && (!requester.isAuthorized("essentials.tpa") || user.getWorld() != requester.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + requester.getWorld().getName()))) {
            throw new Exception(I18n._("noPendingRequest", new Object[0]));
        }
        if (args.length > 0 && !requester.getName().contains(args[0])) {
            throw new Exception(I18n._("noPendingRequest", new Object[0]));
        }
        long timeout = this.ess.getSettings().getTpaAcceptCancellation();
        if (timeout != 0 && (System.currentTimeMillis() - user.getTeleportRequestTime()) / 1000 > timeout) {
            user.requestTeleport(null, false);
            throw new Exception(I18n._("requestTimedOut", new Object[0]));
        }
        Trade charge = new Trade(this.getName(), this.ess);
        user.sendMessage(I18n._("requestAccepted", new Object[0]));
        requester.sendMessage(I18n._("requestAcceptedFrom", user.getDisplayName()));
        try {
            if (user.isTpRequestHere()) {
                Location loc = user.getTpRequestLocation();
                requester.getTeleport().teleportPlayer((IUser)user, user.getTpRequestLocation(), charge, PlayerTeleportEvent.TeleportCause.COMMAND);
                requester.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            } else {
                requester.getTeleport().teleport(user.getBase(), charge, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
        }
        catch (Exception ex) {
            user.sendMessage(I18n._("pendingTeleportCancelled", new Object[0]));
            this.ess.showError(requester.getSource(), ex, commandLabel);
        }
        user.requestTeleport(null, false);
        throw new NoChargeException();
    }
}

