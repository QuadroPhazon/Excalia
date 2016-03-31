/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.DateUtil;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandtogglejail
extends EssentialsCommand {
    public Commandtogglejail() {
        super("togglejail");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User player = this.getPlayer(server, args, 0, true, true);
        if (args.length >= 2 && !player.isJailed()) {
            if (!player.isOnline()) {
                if (sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.togglejail.offline")) {
                    sender.sendMessage(I18n._("mayNotJailOffline", new Object[0]));
                    return;
                }
            } else if (player.isAuthorized("essentials.jail.exempt")) {
                sender.sendMessage(I18n._("mayNotJail", new Object[0]));
                return;
            }
            if (player.isOnline()) {
                this.ess.getJails().sendToJail(player, args[1]);
            } else {
                this.ess.getJails().getJail(args[1]);
            }
            player.setJailed(true);
            player.sendMessage(I18n._("userJailed", new Object[0]));
            player.setJail(null);
            player.setJail(args[1]);
            long timeDiff = 0;
            if (args.length > 2) {
                String time = Commandtogglejail.getFinalArg(args, 2);
                timeDiff = DateUtil.parseDateDiff(time, true);
                player.setJailTimeout(timeDiff);
            }
            sender.sendMessage(timeDiff > 0 ? I18n._("playerJailedFor", player.getName(), DateUtil.formatDateDiff(timeDiff)) : I18n._("playerJailed", player.getName()));
            return;
        }
        if (args.length >= 2 && player.isJailed() && !args[1].equalsIgnoreCase(player.getJail())) {
            sender.sendMessage(I18n._("jailAlreadyIncarcerated", player.getJail()));
            return;
        }
        if (args.length >= 2 && player.isJailed() && args[1].equalsIgnoreCase(player.getJail())) {
            String time = Commandtogglejail.getFinalArg(args, 2);
            long timeDiff = DateUtil.parseDateDiff(time, true);
            player.setJailTimeout(timeDiff);
            sender.sendMessage(I18n._("jailSentenceExtended", DateUtil.formatDateDiff(timeDiff)));
            return;
        }
        if (args.length == 1 || args.length == 2 && args[1].equalsIgnoreCase(player.getJail())) {
            if (!player.isJailed()) {
                throw new NotEnoughArgumentsException();
            }
            player.setJailed(false);
            player.setJailTimeout(0);
            player.sendMessage(I18n._("jailReleasedPlayerNotify", new Object[0]));
            player.setJail(null);
            if (player.isOnline()) {
                player.getTeleport().back();
            }
            sender.sendMessage(I18n._("jailReleased", player.getName()));
        }
    }
}

