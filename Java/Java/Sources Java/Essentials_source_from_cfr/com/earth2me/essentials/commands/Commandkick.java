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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandkick
extends EssentialsCommand {
    public Commandkick() {
        super("kick");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User target = this.getPlayer(server, args, 0, true, false);
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            if (target.isHidden() && !user.isAuthorized("essentials.vanish.interact")) {
                throw new PlayerNotFoundException();
            }
            if (target.isAuthorized("essentials.kick.exempt")) {
                throw new Exception(I18n._("kickExempt", new Object[0]));
            }
        }
        String kickReason = args.length > 1 ? Commandkick.getFinalArg(args, 1) : I18n._("kickDefault", new Object[0]);
        kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n").replace("|", "\n"));
        target.kickPlayer(kickReason);
        String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
        server.getLogger().log(Level.INFO, I18n._("playerKicked", senderName, target.getName(), kickReason));
        this.ess.broadcastMessage("essentials.kick.notify", I18n._("playerKicked", senderName, target.getName(), kickReason));
    }
}

