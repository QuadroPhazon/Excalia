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
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.DateUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandmute
extends EssentialsCommand {
    public Commandmute() {
        super("mute");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        User user;
        boolean nomatch = false;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        try {
            user = this.getPlayer(server, args, 0, true, true);
        }
        catch (PlayerNotFoundException e) {
            nomatch = true;
            user = this.ess.getUser(new OfflinePlayer(args[0], this.ess));
        }
        if (!user.isOnline()) {
            if (sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.mute.offline")) {
                throw new Exception(I18n._("muteExemptOffline", new Object[0]));
            }
        } else if (user.isAuthorized("essentials.mute.exempt") && sender.isPlayer()) {
            throw new Exception(I18n._("muteExempt", new Object[0]));
        }
        long muteTimestamp = 0;
        if (args.length > 1) {
            String time = Commandmute.getFinalArg(args, 1);
            muteTimestamp = DateUtil.parseDateDiff(time, true);
            user.setMuted(true);
        } else {
            user.setMuted(!user.getMuted());
        }
        user.setMuteTimeout(muteTimestamp);
        boolean muted = user.getMuted();
        String muteTime = DateUtil.formatDateDiff(muteTimestamp);
        if (nomatch) {
            sender.sendMessage(I18n._("userUnknown", user.getName()));
        }
        if (muted) {
            if (muteTimestamp > 0) {
                sender.sendMessage(I18n._("mutedPlayerFor", user.getDisplayName(), muteTime));
                user.sendMessage(I18n._("playerMutedFor", muteTime));
            } else {
                sender.sendMessage(I18n._("mutedPlayer", user.getDisplayName()));
                user.sendMessage(I18n._("playerMuted", new Object[0]));
            }
            this.ess.broadcastMessage("essentials.mute.notify", I18n._("muteNotify", sender.getSender().getName(), user.getName(), muteTime));
        } else {
            sender.sendMessage(I18n._("unmutedPlayer", user.getDisplayName()));
            user.sendMessage(I18n._("playerUnmuted", new Object[0]));
        }
    }
}

