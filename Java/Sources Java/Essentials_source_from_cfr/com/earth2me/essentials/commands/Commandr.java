/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Console;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IReplyTo;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandr
extends EssentialsCommand {
    public Commandr() {
        super("r");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String senderName;
        IReplyTo replyTo;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String message = Commandr.getFinalArg(args, 0);
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            message = FormatUtil.formatMessage(user, "essentials.msg", message);
            replyTo = user;
            senderName = user.getDisplayName();
        } else {
            message = FormatUtil.replaceFormat(message);
            replyTo = Console.getConsoleReplyTo();
            senderName = "Console";
        }
        CommandSource target = replyTo.getReplyTo();
        if (target == null || target.isPlayer() && !target.getPlayer().isOnline()) {
            throw new Exception(I18n._("foreverAlone", new Object[0]));
        }
        String targetName = target.isPlayer() ? target.getPlayer().getDisplayName() : "Console";
        sender.sendMessage(I18n._("msgFormat", I18n._("me", new Object[0]), targetName, message));
        if (target.isPlayer()) {
            User player = this.ess.getUser(target.getPlayer());
            if (sender.isPlayer() && player.isIgnoredPlayer(this.ess.getUser(sender.getPlayer()))) {
                return;
            }
        }
        target.sendMessage(I18n._("msgFormat", senderName, I18n._("me", new Object[0]), message));
        replyTo.setReplyTo(target);
        if (target != sender) {
            if (target.isPlayer()) {
                this.ess.getUser(target.getPlayer()).setReplyTo(sender);
            } else {
                Console.getConsoleReplyTo().setReplyTo(sender);
            }
        }
    }
}

