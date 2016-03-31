/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandme
extends EssentialsCommand {
    public Commandme() {
        super("me");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (user.isMuted()) {
            throw new Exception(I18n._("voiceSilenced", new Object[0]));
        }
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String message = Commandme.getFinalArg(args, 0);
        message = FormatUtil.formatMessage(user, "essentials.chat", message);
        user.setDisplayNick();
        this.ess.broadcastMessage(user, I18n._("action", user.getDisplayName(), message));
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String message = Commandme.getFinalArg(args, 0);
        message = FormatUtil.replaceFormat(message);
        this.ess.getServer().broadcastMessage(I18n._("action", "@", message));
    }
}

