/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

public class Commandworth
extends EssentialsCommand {
    public Commandworth() {
        super("worth");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        BigDecimal totalWorth = BigDecimal.ZERO;
        String type = "";
        List<ItemStack> is = this.ess.getItemDb().getMatching(user, args);
        int count = 0;
        boolean isBulk = is.size() > 1;
        Iterator<ItemStack> i$ = is.iterator();
        while (i$.hasNext()) {
            ItemStack stack = i$.next();
            try {
                if (stack.getAmount() <= 0) continue;
                totalWorth = totalWorth.add(this.itemWorth(user.getSource(), user, stack, args));
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
        if (count > 1) {
            if (args.length > 0 && args[0].equalsIgnoreCase("blocks")) {
                user.sendMessage(I18n._("totalSellableBlocks", type, NumberUtil.displayCurrency(totalWorth, this.ess)));
            } else {
                user.sendMessage(I18n._("totalSellableAll", type, NumberUtil.displayCurrency(totalWorth, this.ess)));
            }
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String type = "";
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        ItemStack stack = this.ess.getItemDb().get(args[0]);
        this.itemWorth(sender, null, stack, args);
    }

    private BigDecimal itemWorth(CommandSource sender, User user, ItemStack is, String[] args) throws Exception {
        BigDecimal worth;
        int amount = 1;
        if (user == null) {
            if (args.length > 1) {
                try {
                    amount = Integer.parseInt(args[1].replaceAll("[^0-9]", ""));
                }
                catch (NumberFormatException ex) {
                    throw new NotEnoughArgumentsException(ex);
                }
            }
        } else {
            amount = this.ess.getWorth().getAmount(this.ess, user, is, args, true);
        }
        if ((worth = this.ess.getWorth().getPrice(is)) == null) {
            throw new Exception(I18n._("itemCannotBeSold", new Object[0]));
        }
        if (amount < 0) {
            amount = 0;
        }
        BigDecimal result = worth.multiply(BigDecimal.valueOf(amount));
        sender.sendMessage(is.getDurability() != 0 ? I18n._("worthMeta", is.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""), is.getDurability(), NumberUtil.displayCurrency(result, this.ess), amount, NumberUtil.displayCurrency(worth, this.ess)) : I18n._("worth", is.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""), NumberUtil.displayCurrency(result, this.ess), amount, NumberUtil.displayCurrency(worth, this.ess)));
        return result;
    }
}

