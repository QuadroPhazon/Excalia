/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandtppos
extends EssentialsCommand {
    public Commandtppos() {
        super("tppos");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 3) {
            throw new NotEnoughArgumentsException();
        }
        double x = args[0].startsWith("~") ? user.getLocation().getX() + (double)Integer.parseInt(args[0].substring(1)) : (double)Integer.parseInt(args[0]);
        double y = args[1].startsWith("~") ? user.getLocation().getY() + (double)Integer.parseInt(args[1].substring(1)) : (double)Integer.parseInt(args[1]);
        double z = args[2].startsWith("~") ? user.getLocation().getZ() + (double)Integer.parseInt(args[2].substring(1)) : (double)Integer.parseInt(args[2]);
        Location loc = new Location(user.getWorld(), x, y, z, user.getLocation().getYaw(), user.getLocation().getPitch());
        if (args.length > 3) {
            loc.setYaw((Float.parseFloat(args[3]) + 180.0f + 360.0f) % 360.0f);
        }
        if (args.length > 4) {
            loc.setPitch(Float.parseFloat(args[4]));
        }
        if (x > 3.0E7 || y > 3.0E7 || z > 3.0E7 || x < -3.0E7 || y < -3.0E7 || z < -3.0E7) {
            throw new NotEnoughArgumentsException(I18n._("teleportInvalidLocation", new Object[0]));
        }
        Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        user.getTeleport().teleport(loc, charge, PlayerTeleportEvent.TeleportCause.COMMAND);
        throw new NoChargeException();
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 4) {
            throw new NotEnoughArgumentsException();
        }
        User user = this.getPlayer(server, args, 0, true, false);
        double x = args[1].startsWith("~") ? user.getLocation().getX() + (double)Integer.parseInt(args[1].substring(1)) : (double)Integer.parseInt(args[1]);
        double y = args[2].startsWith("~") ? user.getLocation().getY() + (double)Integer.parseInt(args[2].substring(1)) : (double)Integer.parseInt(args[2]);
        double z = args[3].startsWith("~") ? user.getLocation().getZ() + (double)Integer.parseInt(args[3].substring(1)) : (double)Integer.parseInt(args[3]);
        Location loc = new Location(user.getWorld(), x, y, z, user.getLocation().getYaw(), user.getLocation().getPitch());
        if (args.length > 4) {
            loc.setYaw((Float.parseFloat(args[4]) + 180.0f + 360.0f) % 360.0f);
        }
        if (args.length > 5) {
            loc.setPitch(Float.parseFloat(args[5]));
        }
        if (x > 3.0E7 || y > 3.0E7 || z > 3.0E7 || x < -3.0E7 || y < -3.0E7 || z < -3.0E7) {
            throw new NotEnoughArgumentsException(I18n._("teleportInvalidLocation", new Object[0]));
        }
        sender.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        user.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        user.getTeleport().teleport(loc, null, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}

