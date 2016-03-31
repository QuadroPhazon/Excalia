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
import org.bukkit.Server;

public class Commandpowertooltoggle
extends EssentialsCommand {
    public Commandpowertooltoggle() {
        super("powertooltoggle");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (!user.hasPowerTools()) {
            user.sendMessage(I18n._("noPowerTools", new Object[0]));
            return;
        }
        user.sendMessage(user.togglePowerToolsEnabled() ? I18n._("powerToolsEnabled", new Object[0]) : I18n._("powerToolsDisabled", new Object[0]));
    }
}

