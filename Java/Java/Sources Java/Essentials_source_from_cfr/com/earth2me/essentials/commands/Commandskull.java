/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Commandskull
extends EssentialsCommand {
    public Commandskull() {
        super("skull");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        String owner;
        if (args.length > 0 && user.isAuthorized("essentials.skull.others")) {
            if (!args[0].matches("^[A-Za-z0-9_]+$")) {
                throw new IllegalArgumentException(I18n._("alphaNames", new Object[0]));
            }
            owner = args[0];
        } else {
            owner = user.getName();
        }
        ItemStack itemSkull = user.getBase().getItemInHand();
        SkullMeta metaSkull = null;
        boolean spawn = false;
        if (itemSkull != null && itemSkull.getType() == Material.SKULL_ITEM && itemSkull.getDurability() == 3) {
            metaSkull = (SkullMeta)itemSkull.getItemMeta();
        } else if (user.isAuthorized("essentials.skull.spawn")) {
            itemSkull = new ItemStack(Material.SKULL_ITEM, 1, 3);
            metaSkull = (SkullMeta)itemSkull.getItemMeta();
            spawn = true;
        } else {
            throw new Exception(I18n._("invalidSkull", new Object[0]));
        }
        if (metaSkull.hasOwner() && !user.isAuthorized("essentials.skull.modify")) {
            throw new Exception(I18n._("noPermissionSkull", new Object[0]));
        }
        metaSkull.setDisplayName("\u00a7fSkull of " + owner);
        metaSkull.setOwner(owner);
        itemSkull.setItemMeta((ItemMeta)metaSkull);
        if (spawn) {
            InventoryWorkaround.addItems((Inventory)user.getBase().getInventory(), itemSkull);
            user.sendMessage(I18n._("givenSkull", owner));
        } else {
            user.sendMessage(I18n._("skullChanged", owner));
        }
    }
}

