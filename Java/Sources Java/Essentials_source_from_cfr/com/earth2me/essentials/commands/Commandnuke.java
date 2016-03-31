/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.TNTExplodeListener;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class Commandnuke
extends EssentialsCommand {
    public Commandnuke() {
        super("nuke");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws NoSuchFieldException, NotEnoughArgumentsException {
        List targets;
        if (args.length > 0) {
            targets = new ArrayList();
            int pos = 0;
            for (String arg : args) {
                targets.add(this.getPlayer(server, sender, args, pos).getBase());
                ++pos;
            }
        } else {
            targets = Arrays.asList(server.getOnlinePlayers());
        }
        this.ess.getTNTListener().enable();
        for (Player player : targets) {
            if (player == null) continue;
            player.sendMessage(I18n._("nuke", new Object[0]));
            Location loc = player.getLocation();
            World world = loc.getWorld();
            for (int x = -10; x <= 10; x += 5) {
                for (int z = -10; z <= 10; z += 5) {
                    Location tntloc = new Location(world, (double)(loc.getBlockX() + x), (double)(world.getHighestBlockYAt(loc) + 64), (double)(loc.getBlockZ() + z));
                    TNTPrimed tnt = (TNTPrimed)world.spawn(tntloc, (Class)TNTPrimed.class);
                }
            }
        }
    }
}

