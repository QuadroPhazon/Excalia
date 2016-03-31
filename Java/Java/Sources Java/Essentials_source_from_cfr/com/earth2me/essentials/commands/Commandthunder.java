/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandthunder
extends EssentialsCommand {
    public Commandthunder() {
        super("thunder");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        World world = user.getWorld();
        boolean setThunder = args[0].equalsIgnoreCase("true");
        if (args.length > 1) {
            world.setThundering(setThunder);
            world.setThunderDuration(Integer.parseInt(args[1]) * 20);
            Object[] arrobject = new Object[2];
            arrobject[0] = setThunder ? I18n._("enabled", new Object[0]) : I18n._("disabled", new Object[0]);
            arrobject[1] = Integer.parseInt(args[1]);
            user.sendMessage(I18n._("thunderDuration", arrobject));
        } else {
            world.setThundering(setThunder);
            Object[] arrobject = new Object[1];
            arrobject[0] = setThunder ? I18n._("enabled", new Object[0]) : I18n._("disabled", new Object[0]);
            user.sendMessage(I18n._("thunder", arrobject));
        }
    }
}

