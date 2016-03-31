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
import com.earth2me.essentials.utils.NumberUtil;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandgive
extends EssentialsCommand {
    public Commandgive() {
        super("give");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        ItemStack stack = this.ess.getItemDb().get(args[1]);
        String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
        if (sender.isPlayer() && (this.ess.getSettings().permissionBasedItemSpawn() ? !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.itemspawn.item-all") && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.itemspawn.item-" + itemname) && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.itemspawn.item-" + stack.getTypeId()) : !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.itemspawn.exempt") && this.ess.getUser(sender.getPlayer()).canSpawnItem(stack.getTypeId()) == false)) {
            throw new Exception(I18n._("cantSpawnItem", itemname));
        }
        User giveTo = this.getPlayer(server, sender, args, 0);
        try {
            if (args.length > 3 && NumberUtil.isInt(args[2]) && NumberUtil.isInt(args[3])) {
                stack.setAmount(Integer.parseInt(args[2]));
                stack.setDurability(Short.parseShort(args[3]));
            } else if (args.length > 2 && Integer.parseInt(args[2]) > 0) {
                stack.setAmount(Integer.parseInt(args[2]));
            } else if (this.ess.getSettings().getDefaultStackSize() > 0) {
                stack.setAmount(this.ess.getSettings().getDefaultStackSize());
            } else if (this.ess.getSettings().getOversizedStackSize() > 0 && giveTo.isAuthorized("essentials.oversizedstacks")) {
                stack.setAmount(this.ess.getSettings().getOversizedStackSize());
            }
        }
        catch (NumberFormatException e) {
            throw new NotEnoughArgumentsException();
        }
        if (args.length > 3) {
            int metaStart;
            MetaItemStack metaStack = new MetaItemStack(stack);
            boolean allowUnsafe = this.ess.getSettings().allowUnsafeEnchantments();
            if (allowUnsafe && sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.enchantments.allowunsafe")) {
                allowUnsafe = false;
            }
            int n = metaStart = NumberUtil.isInt(args[3]) ? 4 : 3;
            if (args.length > metaStart) {
                metaStack.parseStringMeta(sender, allowUnsafe, args, metaStart, this.ess);
            }
            stack = metaStack.getItemStack();
        }
        if (stack.getType() == Material.AIR) {
            throw new Exception(I18n._("cantSpawnItem", "Air"));
        }
        String itemName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
        sender.sendMessage(I18n._("giveSpawn", stack.getAmount(), itemName, giveTo.getDisplayName()));
        Map<Integer, ItemStack> leftovers = giveTo.isAuthorized("essentials.oversizedstacks") ? InventoryWorkaround.addOversizedItems((Inventory)giveTo.getInventory(), this.ess.getSettings().getOversizedStackSize(), stack) : InventoryWorkaround.addItems((Inventory)giveTo.getInventory(), stack);
        for (ItemStack item : leftovers.values()) {
            sender.sendMessage(I18n._("giveSpawnFailure", item.getAmount(), itemName, giveTo.getDisplayName()));
        }
        giveTo.updateInventory();
    }
}

