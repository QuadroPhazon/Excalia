/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class SignDisposal
extends EssentialsSign {
    public SignDisposal() {
        super("Disposal");
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) {
        player.getBase().openInventory(ess.getServer().createInventory((InventoryHolder)player.getBase(), 36, "Disposal"));
        return true;
    }
}

