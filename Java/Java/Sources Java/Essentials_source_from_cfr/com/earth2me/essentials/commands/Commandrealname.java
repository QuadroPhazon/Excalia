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
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandrealname
extends EssentialsCommand {
    public Commandrealname() {
        super("realname");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String whois = args[0].toLowerCase(Locale.ENGLISH);
        boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        boolean foundUser = false;
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            User u = this.ess.getUser(onlinePlayer);
            if (skipHidden && u.isHidden()) continue;
            u.setDisplayNick();
            String displayName = FormatUtil.stripFormat(u.getDisplayName()).toLowerCase(Locale.ENGLISH);
            if (!displayName.contains(whois)) continue;
            foundUser = true;
            sender.sendMessage(u.getDisplayName() + " " + I18n._("is", new Object[0]) + " " + u.getName());
        }
        if (!foundUser) {
            throw new PlayerNotFoundException();
        }
    }
}

