/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandinvsee
extends EssentialsCommand {
    public Commandinvsee() {
        super("invsee");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        PlayerInventory inv;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User invUser = this.getPlayer(server, user, args, 0);
        if (args.length > 1 && user.isAuthorized("essentials.invsee.equip")) {
            inv = server.createInventory((InventoryHolder)invUser.getBase(), 9, "Equipped");
            inv.setContents(invUser.getInventory().getArmorContents());
        } else {
            inv = invUser.getInventory();
        }
        user.closeInventory();
        user.openInventory((Inventory)inv);
        user.setInvSee(true);
    }
}

