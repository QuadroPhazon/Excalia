/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import java.util.HashMap;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SignFree
extends EssentialsSign {
    public SignFree() {
        super("Free");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        try {
            ItemStack item = this.getItemStack(sign.getLine(1), 1, ess);
            item = this.getItemMeta(item, sign.getLine(2), ess);
            item = this.getItemMeta(item, sign.getLine(3), ess);
        }
        catch (SignException ex) {
            sign.setLine(1, "\u00a7c<item>");
            throw new SignException(ex.getMessage(), ex);
        }
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        ItemStack itemStack = this.getItemStack(sign.getLine(1), 1, ess);
        ItemStack item = this.getItemMeta(itemStack = this.getItemMeta(itemStack, sign.getLine(2), ess), sign.getLine(3), ess);
        if (item.getType() == Material.AIR) {
            throw new SignException(I18n._("cantSpawnItem", "Air"));
        }
        item.setAmount(item.getType().getMaxStackSize());
        ItemMeta meta = item.getItemMeta();
        String displayName = meta.hasDisplayName() ? meta.getDisplayName() : item.getType().toString();
        Inventory invent = ess.getServer().createInventory((InventoryHolder)player.getBase(), 36, displayName);
        for (int i = 0; i < 36; ++i) {
            invent.addItem(new ItemStack[]{item});
        }
        player.openInventory(invent);
        Trade.log("Sign", "Free", "Interact", username, null, username, new Trade(item, ess), sign.getBlock().getLocation(), ess);
        return true;
    }
}

