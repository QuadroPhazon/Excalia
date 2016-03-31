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
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.DateUtil;
import java.util.GregorianCalendar;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandtempban
extends EssentialsCommand {
    public Commandtempban() {
        super("tempban");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        User user = this.getPlayer(server, args, 0, true, true);
        if (!user.isOnline()) {
            if (sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.tempban.offline")) {
                sender.sendMessage(I18n._("tempbanExemptOffline", new Object[0]));
                return;
            }
        } else if (user.isAuthorized("essentials.tempban.exempt") && sender.isPlayer()) {
            sender.sendMessage(I18n._("tempbanExempt", new Object[0]));
            return;
        }
        String time = Commandtempban.getFinalArg(args, 1);
        long banTimestamp = DateUtil.parseDateDiff(time, true);
        long maxBanLength = this.ess.getSettings().getMaxTempban() * 1000;
        if (maxBanLength > 0 && banTimestamp - GregorianCalendar.getInstance().getTimeInMillis() > maxBanLength && sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.tempban.unlimited")) {
            sender.sendMessage(I18n._("oversizedTempban", new Object[0]));
            throw new NoChargeException();
        }
        String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
        String banReason = I18n._("tempBanned", DateUtil.formatDateDiff(banTimestamp), senderName);
        user.setBanReason(banReason);
        user.setBanTimeout(banTimestamp);
        user.setBanned(true);
        user.kickPlayer(banReason);
        this.ess.broadcastMessage("essentials.ban.notify", I18n._("playerBanned", senderName, user.getName(), banReason, DateUtil.formatDateDiff(banTimestamp)));
    }
}

