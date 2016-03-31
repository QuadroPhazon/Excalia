/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import org.bukkit.Location;
import org.bukkit.Server;

public class Commanddepth
extends EssentialsCommand {
    public Commanddepth() {
        super("depth");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        int depth = user.getLocation().getBlockY() - 63;
        if (depth > 0) {
            user.sendMessage(I18n._("depthAboveSea", depth));
        } else if (depth < 0) {
            user.sendMessage(I18n._("depthBelowSea", - depth));
        } else {
            user.sendMessage(I18n._("depth", new Object[0]));
        }
    }
}

