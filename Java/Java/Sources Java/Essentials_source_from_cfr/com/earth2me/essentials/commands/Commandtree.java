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

import com.earth2me.essentials.Essentials;
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

public class Commandtree
extends EssentialsCommand {
    public Commandtree() {
        super("tree");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        TreeType tree;
        block15 : {
            tree = TreeType.BIRCH;
            try {
                if (args.length < 1) {
                    throw new NotEnoughArgumentsException();
                }
                if (args[0].equalsIgnoreCase("birch")) {
                    tree = TreeType.BIRCH;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("redwood")) {
                    tree = TreeType.REDWOOD;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("tree")) {
                    tree = TreeType.TREE;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("redmushroom")) {
                    tree = TreeType.RED_MUSHROOM;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("brownmushroom")) {
                    tree = TreeType.BROWN_MUSHROOM;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("jungle")) {
                    tree = TreeType.SMALL_JUNGLE;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("junglebush")) {
                    tree = TreeType.JUNGLE_BUSH;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("swamp")) {
                    tree = TreeType.SWAMP;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("acacia")) {
                    tree = TreeType.ACACIA;
                    break block15;
                }
                if (args[0].equalsIgnoreCase("darkoak")) {
                    tree = TreeType.DARK_OAK;
                    break block15;
                }
                throw new NotEnoughArgumentsException();
            }
            catch (NoSuchFieldError e) {
                Essentials.wrongVersion();
            }
        }
        Location loc = LocationUtil.getTarget((LivingEntity)user.getBase());
        Location safeLocation = LocationUtil.getSafeDestination(loc);
        boolean success = user.getWorld().generateTree(safeLocation, tree);
        if (success) {
            user.sendMessage(I18n._("treeSpawned", new Object[0]));
        } else {
            user.sendMessage(I18n._("treeFailure", new Object[0]));
        }
    }
}

