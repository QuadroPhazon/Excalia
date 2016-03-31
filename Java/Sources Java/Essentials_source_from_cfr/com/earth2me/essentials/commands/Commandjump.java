/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.utils.LocationUtil;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandjump
extends EssentialsCommand {
    public Commandjump() {
        super("jump");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        Location loc;
        if (args.length > 0 && args[0].contains("lock") && user.isAuthorized("essentials.jump.lock")) {
            if (user.isFlyClickJump()) {
                user.setRightClickJump(false);
                user.sendMessage("Flying wizard mode disabled");
            } else {
                user.setRightClickJump(true);
                user.sendMessage("Enabling flying wizard mode");
            }
            return;
        }
        Location cloc = user.getLocation();
        try {
            loc = LocationUtil.getTarget((LivingEntity)user.getBase());
            loc.setYaw(cloc.getYaw());
            loc.setPitch(cloc.getPitch());
            loc.setY(loc.getY() + 1.0);
        }
        catch (NullPointerException ex) {
            throw new Exception(I18n._("jumpError", new Object[0]), ex);
        }
        Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.getTeleport().teleport(loc, charge, PlayerTeleportEvent.TeleportCause.COMMAND);
        throw new NoChargeException();
    }
}

