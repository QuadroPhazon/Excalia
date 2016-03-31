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

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commanditem
extends EssentialsCommand {
    public Commanditem() {
        super("item");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        ItemStack stack = this.ess.getItemDb().get(args[0]);
        String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
        if (this.ess.getSettings().permissionBasedItemSpawn() ? !user.isAuthorized("essentials.itemspawn.item-all") && !user.isAuthorized("essentials.itemspawn.item-" + itemname) && !user.isAuthorized("essentials.itemspawn.item-" + stack.getTypeId()) : !user.isAuthorized("essentials.itemspawn.exempt") && user.canSpawnItem(stack.getTypeId()) == false) {
            throw new Exception(I18n._("cantSpawnItem", itemname));
        }
        try {
            if (args.length > 1 && Integer.parseInt(args[1]) > 0) {
                stack.setAmount(Integer.parseInt(args[1]));
            } else if (this.ess.getSettings().getDefaultStackSize() > 0) {
                stack.setAmount(this.ess.getSettings().getDefaultStackSize());
            } else if (this.ess.getSettings().getOversizedStackSize() > 0 && user.isAuthorized("essentials.oversizedstacks")) {
                stack.setAmount(this.ess.getSettings().getOversizedStackSize());
            }
        }
        catch (NumberFormatException e) {
            throw new NotEnoughArgumentsException();
        }
        if (args.length > 2) {
            MetaItemStack metaStack = new MetaItemStack(stack);
            boolean allowUnsafe = this.ess.getSettings().allowUnsafeEnchantments() && user.isAuthorized("essentials.enchantments.allowunsafe");
            metaStack.parseStringMeta(user.getSource(), allowUnsafe, args, 2, this.ess);
            stack = metaStack.getItemStack();
        }
        if (stack.getType() == Material.AIR) {
            throw new Exception(I18n._("cantSpawnItem", "Air"));
        }
        String displayName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
        user.sendMessage(I18n._("itemSpawn", stack.getAmount(), displayName));
        if (user.isAuthorized("essentials.oversizedstacks")) {
            InventoryWorkaround.addOversizedItems((Inventory)user.getInventory(), this.ess.getSettings().getOversizedStackSize(), stack);
        } else {
            InventoryWorkaround.addItems((Inventory)user.getInventory(), stack);
        }
        user.updateInventory();
    }
}

