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
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandban
extends EssentialsCommand {
    public Commandban() {
        super("ban");
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
            if (sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.ban.offline")) {
                throw new Exception(I18n._("banExemptOffline", new Object[0]));
            }
        } else if (user.isAuthorized("essentials.ban.exempt") && sender.isPlayer()) {
            throw new Exception(I18n._("banExempt", new Object[0]));
        }
        String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
        String banReason = args.length > 1 ? FormatUtil.replaceFormat(Commandban.getFinalArg(args, 1).replace("\\n", "\n").replace("|", "\n")) : I18n._("defaultBanReason", new Object[0]);
        user.setBanReason(I18n._("banFormat", banReason, senderName));
        user.setBanned(true);
        user.setBanTimeout(0);
        user.kickPlayer(I18n._("banFormat", banReason, senderName));
        server.getLogger().log(Level.INFO, I18n._("playerBanned", senderName, user.getName(), banReason));
        if (nomatch) {
            sender.sendMessage(I18n._("userUnknown", user.getName()));
        }
        this.ess.broadcastMessage("essentials.ban.notify", I18n._("playerBanned", senderName, user.getName(), banReason));
    }
}

