/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandsell
extends EssentialsCommand {
    public Commandsell() {
        super("sell");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        BigDecimal totalWorth = BigDecimal.ZERO;
        String type = "";
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        List<ItemStack> is = this.ess.getItemDb().getMatching(user, args);
        int count = 0;
        boolean isBulk = is.size() > 1;
        Iterator<ItemStack> i$ = is.iterator();
        while (i$.hasNext()) {
            ItemStack stack = i$.next();
            try {
                if (stack.getAmount() <= 0) continue;
                totalWorth = totalWorth.add(this.sellItem(user, stack, args, isBulk));
                stack = stack.clone();
                ++count;
                for (ItemStack zeroStack : is) {
                    if (!zeroStack.isSimilar(stack)) continue;
                    zeroStack.setAmount(0);
                }
                continue;
            }
            catch (Exception e) {
                if (isBulk) continue;
                throw e;
            }
        }
        if (count != 1) {
            if (args[0].equalsIgnoreCase("blocks")) {
                user.sendMessage(I18n._("totalWorthBlocks", type, NumberUtil.displayCurrency(totalWorth, this.ess)));
            } else {
                user.sendMessage(I18n._("totalWorthAll", type, NumberUtil.displayCurrency(totalWorth, this.ess)));
            }
        }
    }

    private BigDecimal sellItem(User user, ItemStack is, String[] args, boolean isBulkSell) throws Exception {
        int amount = this.ess.getWorth().getAmount(this.ess, user, is, args, isBulkSell);
        BigDecimal worth = this.ess.getWorth().getPrice(is);
        if (worth == null) {
            throw new Exception(I18n._("itemCannotBeSold", new Object[0]));
        }
        if (amount <= 0) {
            if (!isBulkSell) {
                user.sendMessage(I18n._("itemSold", NumberUtil.displayCurrency(BigDecimal.ZERO, this.ess), BigDecimal.ZERO, is.getType().toString().toLowerCase(Locale.ENGLISH), NumberUtil.displayCurrency(worth, this.ess)));
            }
            return BigDecimal.ZERO;
        }
        BigDecimal result = worth.multiply(BigDecimal.valueOf(amount));
        ItemStack ris = is.clone();
        ris.setAmount(amount);
        if (!user.getInventory().containsAtLeast(ris, amount)) {
            throw new IllegalStateException("Trying to remove more items than are available.");
        }
        user.getInventory().removeItem(new ItemStack[]{ris});
        user.updateInventory();
        Trade.log("Command", "Sell", "Item", user.getName(), new Trade(ris, this.ess), user.getName(), new Trade(result, this.ess), user.getLocation(), this.ess);
        user.giveMoney(result);
        user.sendMessage(I18n._("itemSold", NumberUtil.displayCurrency(result, this.ess), amount, is.getType().toString().toLowerCase(Locale.ENGLISH), NumberUtil.displayCurrency(worth, this.ess)));
        logger.log(Level.INFO, I18n._("itemSoldConsole", user.getDisplayName(), is.getType().toString().toLowerCase(Locale.ENGLISH), NumberUtil.displayCurrency(result, this.ess), amount, NumberUtil.displayCurrency(worth, this.ess)));
        return result;
    }
}

