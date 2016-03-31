/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandweather
extends EssentialsCommand {
    public Commandweather() {
        super("weather");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        boolean isStorm;
        if (args.length < 1) {
            if (commandLabel.equalsIgnoreCase("sun") || commandLabel.equalsIgnoreCase("esun")) {
                isStorm = false;
            } else {
                if (!commandLabel.equalsIgnoreCase("storm") && !commandLabel.equalsIgnoreCase("estorm") && !commandLabel.equalsIgnoreCase("rain") && !commandLabel.equalsIgnoreCase("erain")) throw new NotEnoughArgumentsException();
                isStorm = true;
            }
        } else {
            isStorm = args[0].equalsIgnoreCase("storm");
        }
        World world = user.getWorld();
        if (args.length > 1) {
            world.setStorm(isStorm);
            world.setWeatherDuration(Integer.parseInt(args[1]) * 20);
            user.sendMessage(isStorm ? I18n._("weatherStormFor", world.getName(), args[1]) : I18n._("weatherSunFor", world.getName(), args[1]));
            return;
        } else {
            world.setStorm(isStorm);
            user.sendMessage(isStorm ? I18n._("weatherStorm", world.getName()) : I18n._("weatherSun", world.getName()));
        }
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("When running from console, usage is: /" + commandLabel + " <world> <storm/sun> [duration]");
        }
        boolean isStorm = args[1].equalsIgnoreCase("storm");
        World world = server.getWorld(args[0]);
        if (world == null) {
            throw new Exception(I18n._("weatherInvalidWorldWorld", args[0]));
        }
        if (args.length > 2) {
            world.setStorm(isStorm);
            world.setWeatherDuration(Integer.parseInt(args[2]) * 20);
            sender.sendMessage(isStorm ? I18n._("weatherStormFor", world.getName(), args[2]) : I18n._("weatherSunFor", world.getName(), args[2]));
        } else {
            world.setStorm(isStorm);
            sender.sendMessage(isStorm ? I18n._("weatherStorm", world.getName()) : I18n._("weatherSun", world.getName()));
        }
    }
}

