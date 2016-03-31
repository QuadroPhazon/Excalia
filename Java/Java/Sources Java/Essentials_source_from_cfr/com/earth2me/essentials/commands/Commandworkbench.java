/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.inventory.InventoryView
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.inventory.InventoryView;

public class Commandworkbench
extends EssentialsCommand {
    public Commandworkbench() {
        super("workbench");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        user.openWorkbench(null, true);
    }
}

