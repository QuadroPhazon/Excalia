/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Console;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandhelpop
extends EssentialsCommand {
    public Commandhelpop() {
        super("helpop");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        user.setDisplayNick();
        this.sendMessage(server, user.getSource(), user.getDisplayName(), args);
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.sendMessage(server, sender, "Console", args);
    }

    private void sendMessage(Server server, CommandSource sender, String from, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String message = I18n._("helpOp", from, FormatUtil.stripFormat(Commandhelpop.getFinalArg(args, 0)));
        CommandSender cs = Console.getCommandSender(server);
        cs.sendMessage(message);
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            User player = this.ess.getUser(onlinePlayer);
            if (!player.isAuthorized("essentials.helpop.receive")) continue;
            player.sendMessage(message);
        }
    }
}

