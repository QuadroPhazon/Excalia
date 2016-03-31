/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandhat
extends EssentialsCommand {
    public Commandhat() {
        super("hat");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length > 0 && (args[0].contains("rem") || args[0].contains("off") || args[0].equalsIgnoreCase("0"))) {
            PlayerInventory inv = user.getInventory();
            ItemStack head = inv.getHelmet();
            if (head == null || head.getType() == Material.AIR) {
                user.sendMessage(I18n._("hatEmpty", new Object[0]));
            } else {
                ItemStack air = new ItemStack(Material.AIR);
                inv.setHelmet(air);
                InventoryWorkaround.addItems((Inventory)user.getInventory(), head);
                user.sendMessage(I18n._("hatRemoved", new Object[0]));
            }
        } else if (user.getItemInHand().getType() != Material.AIR) {
            ItemStack hand = user.getItemInHand();
            if (hand.getType().getMaxDurability() == 0) {
                PlayerInventory inv = user.getInventory();
                ItemStack head = inv.getHelmet();
                inv.setHelmet(hand);
                inv.setItemInHand(head);
                user.sendMessage(I18n._("hatPlaced", new Object[0]));
            } else {
                user.sendMessage(I18n._("hatArmor", new Object[0]));
            }
        } else {
            user.sendMessage(I18n._("hatFail", new Object[0]));
        }
    }
}

