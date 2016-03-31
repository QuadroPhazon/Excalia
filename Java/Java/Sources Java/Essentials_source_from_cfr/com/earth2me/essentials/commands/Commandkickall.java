/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.utils.FormatUtil;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandkickall
extends EssentialsCommand {
    public Commandkickall() {
        super("kickall");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String kickReason = args.length > 0 ? Commandkickall.getFinalArg(args, 0) : I18n._("kickDefault", new Object[0]);
        kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n").replace("|", "\n"));
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            if (sender.isPlayer() && onlinePlayer.getName().equalsIgnoreCase(sender.getPlayer().getName())) continue;
            onlinePlayer.kickPlayer(kickReason);
        }
        sender.sendMessage(I18n._("kickedAll", new Object[0]));
    }
}

