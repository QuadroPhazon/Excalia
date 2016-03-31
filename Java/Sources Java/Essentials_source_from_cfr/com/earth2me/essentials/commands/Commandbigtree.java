/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.TreeType
 *  org.bukkit.World
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Commandbigtree
extends EssentialsCommand {
    public Commandbigtree() {
        super("bigtree");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        TreeType tree;
        if (args.length > 0 && args[0].equalsIgnoreCase("redwood")) {
            tree = TreeType.TALL_REDWOOD;
        } else if (args.length > 0 && args[0].equalsIgnoreCase("tree")) {
            tree = TreeType.BIG_TREE;
        } else if (args.length > 0 && args[0].equalsIgnoreCase("jungle")) {
            tree = TreeType.JUNGLE;
        } else {
            throw new NotEnoughArgumentsException();
        }
        Location loc = LocationUtil.getTarget((LivingEntity)user.getBase());
        Location safeLocation = LocationUtil.getSafeDestination(loc);
        boolean success = user.getWorld().generateTree(safeLocation, tree);
        if (!success) {
            throw new Exception(I18n._("bigTreeFailure", new Object[0]));
        }
        user.sendMessage(I18n._("bigTreeSuccess", new Object[0]));
    }
}

