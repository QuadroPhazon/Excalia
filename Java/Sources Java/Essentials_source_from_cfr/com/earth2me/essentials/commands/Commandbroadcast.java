/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public class Commandbroadcast
extends EssentialsCommand {
    public Commandbroadcast() {
        super("broadcast");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        this.sendBroadcast(user.getDisplayName(), args);
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.sendBroadcast(sender.getSender().getName(), args);
    }

    private void sendBroadcast(String name, String[] args) throws NotEnoughArgumentsException {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.ess.broadcastMessage(I18n._("broadcast", FormatUtil.replaceFormat(Commandbroadcast.getFinalArg(args, 0)).replace("\\n", "\n"), name));
    }
}

