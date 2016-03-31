/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import java.util.List;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandworld
extends EssentialsCommand {
    public Commandworld() {
        super("world");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        World world;
        if (args.length < 1) {
            World nether = null;
            List worlds = server.getWorlds();
            for (World world2 : worlds) {
                if (world2.getEnvironment() != World.Environment.NETHER) continue;
                nether = world2;
                break;
            }
            if (nether == null) {
                return;
            }
            world = user.getWorld() == nether ? (World)worlds.get(0) : nether;
        } else {
            world = this.ess.getWorld(Commandworld.getFinalArg(args, 0));
            if (world == null) {
                user.sendMessage(I18n._("invalidWorld", new Object[0]));
                user.sendMessage(I18n._("possibleWorlds", server.getWorlds().size() - 1));
                user.sendMessage(I18n._("typeWorldName", new Object[0]));
                throw new NoChargeException();
            }
        }
        if (this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + world.getName())) {
            throw new Exception(I18n._("noPerm", "essentials.worlds." + world.getName()));
        }
        double factor = user.getWorld().getEnvironment() == World.Environment.NETHER && world.getEnvironment() == World.Environment.NORMAL ? 8.0 : (user.getWorld().getEnvironment() == World.Environment.NORMAL && world.getEnvironment() == World.Environment.NETHER ? 0.125 : 1.0);
        Location loc = user.getLocation();
        Location target = new Location(world, (double)loc.getBlockX() * factor + 0.5, (double)loc.getBlockY(), (double)loc.getBlockZ() * factor + 0.5);
        Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.getTeleport().teleport(target, charge, PlayerTeleportEvent.TeleportCause.COMMAND);
        throw new NoChargeException();
    }
}

