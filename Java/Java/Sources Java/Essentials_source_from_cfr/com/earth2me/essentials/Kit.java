/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Item
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kit {
    public static String listKits(IEssentials ess, User user) throws Exception {
        try {
            ConfigurationSection kits = ess.getSettings().getKits();
            StringBuilder list = new StringBuilder();
            for (String kitItem : kits.getKeys(false)) {
                Map<String, Object> kit;
                if (user == null) {
                    list.append(" ").append(I18n.capitalCase(kitItem));
                    continue;
                }
                if (!user.isAuthorized("essentials.kits." + kitItem.toLowerCase(Locale.ENGLISH))) continue;
                String cost = "";
                String name = I18n.capitalCase(kitItem);
                BigDecimal costPrice = new Trade("kit-" + kitItem.toLowerCase(Locale.ENGLISH), ess).getCommandCost(user);
                if (costPrice.signum() > 0) {
                    cost = I18n._("kitCost", NumberUtil.displayCurrency(costPrice, ess));
                }
                if (Kit.getNextUse(user, kitItem, kit = ess.getSettings().getKit(kitItem)) != 0) {
                    name = I18n._("kitDelay", name);
                }
                list.append(" ").append(name).append(cost);
            }
            return list.toString().trim();
        }
        catch (Exception ex) {
            throw new Exception(I18n._("kitError", new Object[0]), ex);
        }
    }

    public static void checkTime(User user, String kitName, Map<String, Object> els) throws Exception {
        GregorianCalendar time = new GregorianCalendar();
        long nextUse = Kit.getNextUse(user, kitName, els);
        if (nextUse != 0) {
            if (nextUse < 0) {
                user.sendMessage(I18n._("kitOnce", new Object[0]));
                throw new NoChargeException();
            }
            user.sendMessage(I18n._("kitTimed", DateUtil.formatDateDiff(nextUse)));
            throw new NoChargeException();
        }
        user.setKitTimestamp(kitName, time.getTimeInMillis());
    }

    public static long getNextUse(User user, String kitName, Map<String, Object> els) throws Exception {
        if (user.isAuthorized("essentials.kit.exemptdelay")) {
            return 0;
        }
        GregorianCalendar time = new GregorianCalendar();
        double delay = 0.0;
        try {
            delay = els.containsKey("delay") ? ((Number)els.get("delay")).doubleValue() : 0.0;
        }
        catch (Exception e) {
            throw new Exception(I18n._("kitError2", new Object[0]));
        }
        long lastTime = user.getKitTimestamp(kitName);
        GregorianCalendar delayTime = new GregorianCalendar();
        delayTime.setTimeInMillis(lastTime);
        delayTime.add(13, (int)delay);
        delayTime.add(14, (int)(delay * 1000.0 % 1000.0));
        if (lastTime == 0 || lastTime > time.getTimeInMillis()) {
            return 0;
        }
        if (delay < 0.0) {
            return -1;
        }
        if (delayTime.before(time)) {
            return 0;
        }
        return delayTime.getTimeInMillis();
    }

    public static List<String> getItems(IEssentials ess, User user, String kitName, Map<String, Object> kit) throws Exception {
        if (kit == null) {
            throw new Exception(I18n._("kitNotFound", new Object[0]));
        }
        try {
            ArrayList<String> itemList = new ArrayList<String>();
            Object kitItems = kit.get("items");
            if (kitItems instanceof List) {
                for (Object item : (List)kitItems) {
                    if (item instanceof String) {
                        itemList.add(item.toString());
                        continue;
                    }
                    throw new Exception("Invalid kit item: " + item.toString());
                }
                return itemList;
            }
            throw new Exception("Invalid item list");
        }
        catch (Exception e) {
            ess.getLogger().log(Level.WARNING, "Error parsing kit " + kitName + ": " + e.getMessage());
            throw new Exception(I18n._("kitError2", new Object[0]), e);
        }
    }

    public static void expandItems(IEssentials ess, User user, List<String> items) throws Exception {
        try {
            SimpleTextInput input = new SimpleTextInput(items);
            KeywordReplacer output = new KeywordReplacer(input, user.getSource(), ess);
            boolean spew = false;
            boolean allowUnsafe = ess.getSettings().allowUnsafeEnchantments();
            for (String kitItem : output.getLines()) {
                String[] parts;
                boolean allowOversizedStacks;
                if (kitItem.startsWith(ess.getSettings().getCurrencySymbol())) {
                    BigDecimal value = new BigDecimal(kitItem.substring(ess.getSettings().getCurrencySymbol().length()).trim());
                    Trade t = new Trade(value, ess);
                    t.pay(user, Trade.OverflowType.DROP);
                    continue;
                }
                ItemStack parseStack = ess.getItemDb().get(parts[0], (parts = kitItem.split(" +")).length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack.getType() == Material.AIR) continue;
                MetaItemStack metaStack = new MetaItemStack(parseStack);
                if (parts.length > 2) {
                    metaStack.parseStringMeta(null, allowUnsafe, parts, 2, ess);
                }
                Map<Integer, ItemStack> overfilled = (allowOversizedStacks = user.isAuthorized("essentials.oversizedstacks")) ? InventoryWorkaround.addOversizedItems((Inventory)user.getInventory(), ess.getSettings().getOversizedStackSize(), metaStack.getItemStack()) : InventoryWorkaround.addItems((Inventory)user.getInventory(), metaStack.getItemStack());
                for (ItemStack itemStack : overfilled.values()) {
                    int spillAmount = itemStack.getAmount();
                    if (!allowOversizedStacks) {
                        itemStack.setAmount(spillAmount < itemStack.getMaxStackSize() ? spillAmount : itemStack.getMaxStackSize());
                    }
                    while (spillAmount > 0) {
                        user.getWorld().dropItemNaturally(user.getLocation(), itemStack);
                        spillAmount -= itemStack.getAmount();
                    }
                    spew = true;
                }
            }
            user.updateInventory();
            if (spew) {
                user.sendMessage(I18n._("kitInvFull", new Object[0]));
            }
        }
        catch (Exception e) {
            user.updateInventory();
            ess.getLogger().log(Level.WARNING, e.getMessage());
            throw new Exception(I18n._("kitError2", new Object[0]), e);
        }
    }
}

