/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandignore
extends EssentialsCommand {
    public Commandignore() {
        super("ignore");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            StringBuilder sb = new StringBuilder();
            for (String s : user._getIgnoredPlayers()) {
                sb.append(s).append(" ");
            }
            String ignoredList = sb.toString().trim();
            user.sendMessage(ignoredList.length() > 0 ? I18n._("ignoredList", ignoredList) : I18n._("noIgnored", new Object[0]));
        } else {
            User player;
            try {
                player = this.getPlayer(server, args, 0, true, true);
            }
            catch (PlayerNotFoundException ex) {
                player = this.ess.getOfflineUser(args[0]);
            }
            if (player == null) {
                throw new PlayerNotFoundException();
            }
            if (player.isIgnoreExempt()) {
                user.sendMessage(I18n._("ignoreExempt", new Object[0]));
            } else if (user.isIgnoredPlayer(player)) {
                user.setIgnoredPlayer(player, false);
                user.sendMessage(I18n._("unignorePlayer", player.getName()));
            } else {
                user.setIgnoredPlayer(player, true);
                user.sendMessage(I18n._("ignorePlayer", player.getName()));
            }
        }
    }
}

