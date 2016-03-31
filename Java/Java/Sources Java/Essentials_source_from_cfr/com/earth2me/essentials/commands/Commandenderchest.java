/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class Commandenderchest
extends EssentialsCommand {
    public Commandenderchest() {
        super("enderchest");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.enderchest.others")) {
            User invUser = this.getPlayer(server, user, args, 0);
            user.closeInventory();
            user.openInventory(invUser.getEnderChest());
            user.setEnderSee(true);
        } else {
            user.closeInventory();
            user.openInventory(user.getEnderChest());
            user.setEnderSee(false);
        }
    }
}

