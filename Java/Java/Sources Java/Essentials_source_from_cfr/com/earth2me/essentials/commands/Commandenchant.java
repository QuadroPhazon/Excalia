/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Enchantments;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandenchant
extends EssentialsCommand {
    public Commandenchant() {
        super("enchant");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        ItemStack stack = user.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            throw new Exception(I18n._("nothingInHand", new Object[0]));
        }
        if (args.length == 0) {
            TreeSet<String> enchantmentslist = new TreeSet<String>();
            for (Map.Entry<String, Enchantment> entry : Enchantments.entrySet()) {
                String enchantmentName = entry.getValue().getName().toLowerCase(Locale.ENGLISH);
                if (!enchantmentslist.contains(enchantmentName) && (!user.isAuthorized("essentials.enchantments." + enchantmentName) || !entry.getValue().canEnchantItem(stack))) continue;
                enchantmentslist.add(entry.getKey());
            }
            throw new NotEnoughArgumentsException(I18n._("enchantments", StringUtil.joinList(enchantmentslist.toArray())));
        }
        int level = -1;
        if (args.length > 1) {
            try {
                level = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ex) {
                level = -1;
            }
        }
        boolean allowUnsafe = this.ess.getSettings().allowUnsafeEnchantments() && user.isAuthorized("essentials.enchantments.allowunsafe");
        MetaItemStack metaStack = new MetaItemStack(stack);
        Enchantment enchantment = metaStack.getEnchantment(user, args[0]);
        metaStack.addEnchantment(user.getSource(), allowUnsafe, enchantment, level);
        user.getInventory().setItemInHand(metaStack.getItemStack());
        user.updateInventory();
        String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
        if (level == 0) {
            user.sendMessage(I18n._("enchantmentRemoved", enchantmentName.replace('_', ' ')));
        } else {
            user.sendMessage(I18n._("enchantmentApplied", enchantmentName.replace('_', ' ')));
        }
    }
}

