/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.WorldNameComparator;
import com.earth2me.essentials.utils.DescParseTickFormat;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Commandtime
extends EssentialsCommand {
    public Commandtime() {
        super("time");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        long ticks;
        String setTime;
        boolean add = false;
        ArrayList<String> argList = new ArrayList<String>(Arrays.asList(args));
        if (argList.remove("set") && !argList.isEmpty() && NumberUtil.isInt(argList.get(0))) {
            argList.set(0, argList.get(0) + "t");
        }
        if (argList.remove("add") && !argList.isEmpty() && NumberUtil.isInt(argList.get(0))) {
            add = true;
            argList.set(0, argList.get(0) + "t");
        }
        String[] validArgs = argList.toArray(new String[0]);
        String worldSelector = null;
        if (validArgs.length == 2) {
            worldSelector = validArgs[1];
        }
        Set<World> worlds = this.getWorlds(server, sender, worldSelector);
        if (validArgs.length == 0) {
            if (commandLabel.equalsIgnoreCase("day") || commandLabel.equalsIgnoreCase("eday")) {
                setTime = "day";
            } else {
                if (!commandLabel.equalsIgnoreCase("night") && !commandLabel.equalsIgnoreCase("enight")) {
                    this.getWorldsTime(sender, worlds);
                    return;
                }
                setTime = "night";
            }
        } else {
            setTime = validArgs[0];
        }
        User user = this.ess.getUser(sender.getPlayer());
        if (user != null && !user.isAuthorized("essentials.time.set")) {
            user.sendMessage(I18n._("timeSetPermission", new Object[0]));
            return;
        }
        try {
            ticks = DescParseTickFormat.parse(setTime);
        }
        catch (NumberFormatException e) {
            throw new NotEnoughArgumentsException(e);
        }
        this.setWorldsTime(sender, worlds, ticks, add);
    }

    private void getWorldsTime(CommandSource sender, Collection<World> worlds) {
        if (worlds.size() == 1) {
            Iterator<World> iter = worlds.iterator();
            sender.sendMessage(DescParseTickFormat.format(iter.next().getTime()));
            return;
        }
        for (World world : worlds) {
            sender.sendMessage(I18n._("timeWorldCurrent", world.getName(), DescParseTickFormat.format(world.getTime())));
        }
    }

    private void setWorldsTime(CommandSource sender, Collection<World> worlds, long ticks, boolean add) {
        for (World world : worlds) {
            long time = world.getTime();
            if (!add) {
                time -= time % 24000;
            }
            world.setTime(time + (long)(add ? 0 : 24000) + ticks);
        }
        StringBuilder output = new StringBuilder();
        for (World world2 : worlds) {
            if (output.length() > 0) {
                output.append(", ");
            }
            output.append(world2.getName());
        }
        sender.sendMessage(I18n._("timeWorldSet", DescParseTickFormat.format(ticks), output.toString()));
    }

    private Set<World> getWorlds(Server server, CommandSource sender, String selector) throws Exception {
        TreeSet<World> worlds = new TreeSet<World>(new WorldNameComparator());
        if (selector == null) {
            if (sender.isPlayer()) {
                User user = this.ess.getUser(sender.getPlayer());
                worlds.add(user.getWorld());
            } else {
                worlds.addAll(server.getWorlds());
            }
            return worlds;
        }
        World world = server.getWorld(selector);
        if (world != null) {
            worlds.add(world);
        } else if (selector.equalsIgnoreCase("*") || selector.equalsIgnoreCase("all")) {
            worlds.addAll(server.getWorlds());
        } else {
            throw new Exception(I18n._("invalidWorld", new Object[0]));
        }
        return worlds;
    }
}

