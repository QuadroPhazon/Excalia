/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

public class Commandmore
extends EssentialsCommand {
    public Commandmore() {
        super("more");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        ItemStack stack = user.getItemInHand();
        if (stack == null) {
            throw new Exception(I18n._("cantSpawnItem", "Air"));
        }
        if (stack.getAmount() >= (user.isAuthorized("essentials.oversizedstacks") ? this.ess.getSettings().getOversizedStackSize() : stack.getMaxStackSize())) {
            throw new Exception(I18n._("fullStack", new Object[0]));
        }
        String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
        if (this.ess.getSettings().permissionBasedItemSpawn() ? !user.isAuthorized("essentials.itemspawn.item-all") && !user.isAuthorized("essentials.itemspawn.item-" + itemname) && !user.isAuthorized("essentials.itemspawn.item-" + stack.getTypeId()) : !user.isAuthorized("essentials.itemspawn.exempt") && user.canSpawnItem(stack.getTypeId()) == false) {
            throw new Exception(I18n._("cantSpawnItem", itemname));
        }
        if (user.isAuthorized("essentials.oversizedstacks")) {
            stack.setAmount(this.ess.getSettings().getOversizedStackSize());
        } else {
            stack.setAmount(stack.getMaxStackSize());
        }
        user.updateInventory();
    }
}

