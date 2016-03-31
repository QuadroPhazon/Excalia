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

public class Commandcompass
extends EssentialsCommand {
    public Commandcompass() {
        super("compass");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        int bearing = (int)(user.getLocation().getYaw() + 180.0f + 360.0f) % 360;
        String dir = bearing < 23 ? "N" : (bearing < 68 ? "NE" : (bearing < 113 ? "E" : (bearing < 158 ? "SE" : (bearing < 203 ? "S" : (bearing < 248 ? "SW" : (bearing < 293 ? "W" : (bearing < 338 ? "NW" : "N")))))));
        user.sendMessage(I18n._("compassBearing", dir, bearing));
    }
}

