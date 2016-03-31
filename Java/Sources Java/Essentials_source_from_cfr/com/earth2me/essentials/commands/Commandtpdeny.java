/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandtpdeny
extends EssentialsCommand {
    public Commandtpdeny() {
        super("tpdeny");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        User player = this.ess.getUser(user.getTeleportRequest());
        if (player == null) {
            throw new Exception(I18n._("noPendingRequest", new Object[0]));
        }
        user.sendMessage(I18n._("requestDenied", new Object[0]));
        player.sendMessage(I18n._("requestDeniedFrom", user.getDisplayName()));
        user.requestTeleport(null, false);
    }
}

