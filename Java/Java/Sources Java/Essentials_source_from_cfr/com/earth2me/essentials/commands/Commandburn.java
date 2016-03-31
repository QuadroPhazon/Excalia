/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;

public class Commandburn
extends EssentialsCommand {
    public Commandburn() {
        super("burn");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        if (args[0].trim().length() < 2) {
            throw new NotEnoughArgumentsException();
        }
        User user = this.getPlayer(server, sender, args, 0);
        user.setFireTicks(Integer.parseInt(args[1]) * 20);
        sender.sendMessage(I18n._("burnMsg", user.getDisplayName(), Integer.parseInt(args[1])));
    }
}

