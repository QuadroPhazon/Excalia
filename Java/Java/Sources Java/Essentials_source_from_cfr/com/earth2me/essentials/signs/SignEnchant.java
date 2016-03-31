/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.Enchantments;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import java.util.Locale;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SignEnchant
extends EssentialsSign {
    public SignEnchant() {
        super("Enchant");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        boolean allowUnsafe;
        int level;
        ItemStack stack;
        try {
            stack = sign.getLine(1).equals("*") || sign.getLine(1).equalsIgnoreCase("any") ? null : this.getItemStack(sign.getLine(1), 1, ess);
        }
        catch (SignException e) {
            sign.setLine(1, "\u00a7c<item|any>");
            throw e;
        }
        String[] enchantLevel = sign.getLine(2).split(":");
        if (enchantLevel.length != 2) {
            sign.setLine(2, "\u00a7c<enchant>");
            throw new SignException(I18n._("invalidSignLine", 3));
        }
        Enchantment enchantment = Enchantments.getByName(enchantLevel[0]);
        if (enchantment == null) {
            sign.setLine(2, "\u00a7c<enchant>");
            throw new SignException(I18n._("enchantmentNotFound", new Object[0]));
        }
        try {
            level = Integer.parseInt(enchantLevel[1]);
        }
        catch (NumberFormatException ex) {
            sign.setLine(2, "\u00a7c<enchant>");
            throw new SignException(ex.getMessage(), ex);
        }
        boolean bl = allowUnsafe = ess.getSettings().allowUnsafeEnchantments() && player.isAuthorized("essentials.enchantments.allowunsafe") && player.isAuthorized("essentials.signs.enchant.allowunsafe");
        if (level < 0 || !allowUnsafe && level > enchantment.getMaxLevel()) {
            level = enchantment.getMaxLevel();
            sign.setLine(2, enchantLevel[0] + ":" + level);
        }
        try {
            if (stack != null) {
                if (allowUnsafe) {
                    stack.addUnsafeEnchantment(enchantment, level);
                } else {
                    stack.addEnchantment(enchantment, level);
                }
            }
        }
        catch (Throwable ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        this.getTrade(sign, 3, ess);
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        int level;
        ItemStack search = sign.getLine(1).equals("*") || sign.getLine(1).equalsIgnoreCase("any") ? null : this.getItemStack(sign.getLine(1), 1, ess);
        int slot = -1;
        Trade charge = this.getTrade(sign, 3, ess);
        charge.isAffordableFor(player);
        String[] enchantLevel = sign.getLine(2).split(":");
        if (enchantLevel.length != 2) {
            throw new SignException(I18n._("invalidSignLine", 3));
        }
        Enchantment enchantment = Enchantments.getByName(enchantLevel[0]);
        if (enchantment == null) {
            throw new SignException(I18n._("enchantmentNotFound", new Object[0]));
        }
        try {
            level = Integer.parseInt(enchantLevel[1]);
        }
        catch (NumberFormatException ex) {
            level = enchantment.getMaxLevel();
        }
        ItemStack playerHand = player.getItemInHand();
        if (playerHand == null || playerHand.getAmount() != 1 || playerHand.containsEnchantment(enchantment) && playerHand.getEnchantmentLevel(enchantment) == level) {
            throw new SignException(I18n._("missingItems", 1, sign.getLine(1)));
        }
        if (search != null && playerHand.getType() != search.getType()) {
            throw new SignException(I18n._("missingItems", 1, search.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ')));
        }
        ItemStack toEnchant = playerHand;
        try {
            if (level == 0) {
                toEnchant.removeEnchantment(enchantment);
            } else if (ess.getSettings().allowUnsafeEnchantments() && player.isAuthorized("essentials.signs.enchant.allowunsafe")) {
                toEnchant.addUnsafeEnchantment(enchantment, level);
            } else {
                toEnchant.addEnchantment(enchantment, level);
            }
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
        if (level == 0) {
            player.sendMessage(I18n._("enchantmentRemoved", enchantmentName.replace('_', ' ')));
        } else {
            player.sendMessage(I18n._("enchantmentApplied", enchantmentName.replace('_', ' ')));
        }
        charge.charge(player);
        Trade.log("Sign", "Enchant", "Interact", username, charge, username, charge, sign.getBlock().getLocation(), ess);
        player.updateInventory();
        return true;
    }
}

