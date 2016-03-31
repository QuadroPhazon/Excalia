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
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandmsg
extends EssentialsLoopCommand {
    final String translatedMe = I18n._("me", new Object[0]);

    public Commandmsg() {
        super("msg");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        boolean canWildcard;
        if (args.length < 2 || args[0].trim().length() < 2 || args[1].trim().isEmpty()) {
            throw new NotEnoughArgumentsException();
        }
        String message = Commandmsg.getFinalArg(args, 1);
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            if (user.isMuted()) {
                throw new Exception(I18n._("voiceSilenced", new Object[0]));
            }
            message = FormatUtil.formatMessage(user, "essentials.msg", message);
            canWildcard = user.isAuthorized("essentials.msg.multiple");
        } else {
            message = FormatUtil.replaceFormat(message);
            canWildcard = true;
        }
        if (args[0].equalsIgnoreCase("Console")) {
            Console replyTo = sender.isPlayer() ? this.ess.getUser(sender.getPlayer()) : Console.getConsoleReplyTo();
            String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
            sender.sendMessage(I18n._("msgFormat", this.translatedMe, "Console", message));
            CommandSender cs = Console.getCommandSender(server);
            cs.sendMessage(I18n._("msgFormat", senderName, this.translatedMe, message));
            replyTo.setReplyTo(new CommandSource(cs));
            Console.getConsoleReplyTo().setReplyTo(sender);
            return;
        }
        this.loopOnlinePlayers(server, sender, canWildcard, canWildcard, args[0], new String[]{message});
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User matchedUser, String[] args) {
        String senderName;
        Console replyTo = sender.isPlayer() ? this.ess.getUser(sender.getPlayer()) : Console.getConsoleReplyTo();
        String string = senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
        if (matchedUser.isAfk()) {
            sender.sendMessage(I18n._("userAFK", matchedUser.getDisplayName()));
        }
        sender.sendMessage(I18n._("msgFormat", this.translatedMe, matchedUser.getDisplayName(), args[0]));
        if (sender.isPlayer() && matchedUser.isIgnoredPlayer(this.ess.getUser(sender.getPlayer()))) {
            return;
        }
        matchedUser.sendMessage(I18n._("msgFormat", senderName, this.translatedMe, args[0]));
        replyTo.setReplyTo(matchedUser.getSource());
        matchedUser.setReplyTo(sender);
    }
}

