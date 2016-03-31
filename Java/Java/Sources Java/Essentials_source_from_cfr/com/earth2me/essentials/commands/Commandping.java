/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.utils.FormatUtil;
import org.bukkit.Server;

public class Commandping
extends EssentialsCommand {
    public Commandping() {
        super("ping");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            sender.sendMessage(I18n._("pong", new Object[0]));
        } else {
            sender.sendMessage(FormatUtil.replaceFormat(Commandping.getFinalArg(args, 0)));
        }
    }
}

