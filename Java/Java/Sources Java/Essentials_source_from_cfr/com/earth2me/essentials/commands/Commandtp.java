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
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandtp
extends EssentialsCommand {
    public Commandtp() {
        super("tp");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        switch (args.length) {
            case 0: {
                throw new NotEnoughArgumentsException();
            }
            case 1: {
                User player = this.getPlayer(server, user, args, 0);
                if (!player.isTeleportEnabled()) {
                    throw new Exception(I18n._("teleportDisabled", player.getDisplayName()));
                }
                if (user.getWorld() != player.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + player.getWorld().getName())) {
                    throw new Exception(I18n._("noPerm", "essentials.worlds." + player.getWorld().getName()));
                }
                Trade charge = new Trade(this.getName(), this.ess);
                charge.isAffordableFor(user);
                user.getTeleport().teleport(player.getBase(), charge, PlayerTeleportEvent.TeleportCause.COMMAND);
                throw new NoChargeException();
            }
            case 4: {
                double z;
                if (!user.isAuthorized("essentials.tp.others")) {
                    throw new Exception(I18n._("noPerm", "essentials.tp.others"));
                }
                User target2 = this.getPlayer(server, user, args, 0);
                double x = args[1].startsWith("~") ? target2.getLocation().getX() + (double)Integer.parseInt(args[1].substring(1)) : (double)Integer.parseInt(args[1]);
                double y = args[2].startsWith("~") ? target2.getLocation().getY() + (double)Integer.parseInt(args[2].substring(1)) : (double)Integer.parseInt(args[2]);
                double d = z = args[3].startsWith("~") ? target2.getLocation().getZ() + (double)Integer.parseInt(args[3].substring(1)) : (double)Integer.parseInt(args[3]);
                if (x > 3.0E7 || y > 3.0E7 || z > 3.0E7 || x < -3.0E7 || y < -3.0E7 || z < -3.0E7) {
                    throw new NotEnoughArgumentsException(I18n._("teleportInvalidLocation", new Object[0]));
                }
                Location loc = new Location(target2.getWorld(), x, y, z, target2.getLocation().getYaw(), target2.getLocation().getPitch());
                if (!target2.isTeleportEnabled()) {
                    throw new Exception(I18n._("teleportDisabled", target2.getDisplayName()));
                }
                target2.getTeleport().now(loc, false, PlayerTeleportEvent.TeleportCause.COMMAND);
                user.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                target2.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                break;
            }
            default: {
                if (!user.isAuthorized("essentials.tp.others")) {
                    throw new Exception(I18n._("noPerm", "essentials.tp.others"));
                }
                User target = this.getPlayer(server, user, args, 0);
                User toPlayer = this.getPlayer(server, user, args, 1);
                if (!target.isTeleportEnabled()) {
                    throw new Exception(I18n._("teleportDisabled", target.getDisplayName()));
                }
                if (!toPlayer.isTeleportEnabled()) {
                    throw new Exception(I18n._("teleportDisabled", toPlayer.getDisplayName()));
                }
                if (target.getWorld() != toPlayer.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + toPlayer.getWorld().getName())) {
                    throw new Exception(I18n._("noPerm", "essentials.worlds." + toPlayer.getWorld().getName()));
                }
                target.getTeleport().now(toPlayer.getBase(), false, PlayerTeleportEvent.TeleportCause.COMMAND);
                target.sendMessage(I18n._("teleportAtoB", user.getDisplayName(), toPlayer.getDisplayName()));
            }
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        User target = this.getPlayer(server, args, 0, true, false);
        if (args.length == 2) {
            User toPlayer = this.getPlayer(server, args, 1, true, false);
            target.getTeleport().now(toPlayer.getBase(), false, PlayerTeleportEvent.TeleportCause.COMMAND);
            target.sendMessage(I18n._("teleportAtoB", "Console", toPlayer.getDisplayName()));
        } else if (args.length > 3) {
            double z;
            double x = args[1].startsWith("~") ? target.getLocation().getX() + (double)Integer.parseInt(args[1].substring(1)) : (double)Integer.parseInt(args[1]);
            double y = args[2].startsWith("~") ? target.getLocation().getY() + (double)Integer.parseInt(args[2].substring(1)) : (double)Integer.parseInt(args[2]);
            double d = z = args[3].startsWith("~") ? target.getLocation().getZ() + (double)Integer.parseInt(args[3].substring(1)) : (double)Integer.parseInt(args[3]);
            if (x > 3.0E7 || y > 3.0E7 || z > 3.0E7 || x < -3.0E7 || y < -3.0E7 || z < -3.0E7) {
                throw new NotEnoughArgumentsException(I18n._("teleportInvalidLocation", new Object[0]));
            }
            Location loc = new Location(target.getWorld(), x, y, z, target.getLocation().getYaw(), target.getLocation().getPitch());
            target.getTeleport().now(loc, false, PlayerTeleportEvent.TeleportCause.COMMAND);
            target.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            sender.sendMessage(I18n._("teleporting", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        } else {
            throw new NotEnoughArgumentsException();
        }
    }
}

