/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.craftbukkit;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public final class InventoryWorkaround {
    private InventoryWorkaround() {
    }

    private static int firstPartial(Inventory inventory, ItemStack item, int maxAmount) {
        if (item == null) {
            return -1;
        }
        ItemStack[] stacks = inventory.getContents();
        for (int i = 0; i < stacks.length; ++i) {
            ItemStack cItem = stacks[i];
            if (cItem == null || cItem.getAmount() >= maxAmount || !cItem.isSimilar(item)) continue;
            return i;
        }
        return -1;
    }

    public static /* varargs */ Map<Integer, ItemStack> addAllItems(Inventory inventory, ItemStack ... items) {
        Inventory fakeInventory = Bukkit.getServer().createInventory(null, inventory.getType());
        fakeInventory.setContents(inventory.getContents());
        Map<Integer, ItemStack> overFlow = InventoryWorkaround.addItems(fakeInventory, items);
        if (overFlow.isEmpty()) {
            InventoryWorkaround.addItems(inventory, items);
            return null;
        }
        return InventoryWorkaround.addItems(fakeInventory, items);
    }

    public static /* varargs */ Map<Integer, ItemStack> addItems(Inventory inventory, ItemStack ... items) {
        return InventoryWorkaround.addOversizedItems(inventory, 0, items);
    }

    public static /* varargs */ Map<Integer, ItemStack> addOversizedItems(Inventory inventory, int oversizedStacks, ItemStack ... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();
        ItemStack[] combined = new ItemStack[items.length];
        block0 : for (ItemStack item : items) {
            if (item == null || item.getAmount() < 1) continue;
            for (int j = 0; j < combined.length; ++j) {
                if (combined[j] == null) {
                    combined[j] = item.clone();
                    continue block0;
                }
                if (!combined[j].isSimilar(item)) continue;
                combined[j].setAmount(combined[j].getAmount() + item.getAmount());
                continue block0;
            }
        }
        block2 : for (int i = 0; i < combined.length; ++i) {
            ItemStack item2 = combined[i];
            if (item2 == null || item2.getType() == Material.AIR) continue;
            do {
                int firstPartial;
                int maxAmount;
                int partialAmount;
                if ((firstPartial = InventoryWorkaround.firstPartial(inventory, item2, maxAmount = oversizedStacks > item2.getType().getMaxStackSize() ? oversizedStacks : item2.getType().getMaxStackSize())) == -1) {
                    int firstFree = inventory.firstEmpty();
                    if (firstFree == -1) {
                        leftover.put(i, item2);
                        continue block2;
                    }
                    if (item2.getAmount() > maxAmount) {
                        ItemStack stack = item2.clone();
                        stack.setAmount(maxAmount);
                        inventory.setItem(firstFree, stack);
                        item2.setAmount(item2.getAmount() - maxAmount);
                        continue;
                    }
                    inventory.setItem(firstFree, item2);
                    continue block2;
                }
                ItemStack partialItem = inventory.getItem(firstPartial);
                int amount = item2.getAmount();
                if (amount + (partialAmount = partialItem.getAmount()) <= maxAmount) {
                    partialItem.setAmount(amount + partialAmount);
                    continue block2;
                }
                partialItem.setAmount(maxAmount);
                item2.setAmount(amount + partialAmount - maxAmount);
            } while (true);
        }
        return leftover;
    }
}

