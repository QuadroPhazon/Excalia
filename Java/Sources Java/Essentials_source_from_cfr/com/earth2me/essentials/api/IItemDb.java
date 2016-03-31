/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.api;

import com.earth2me.essentials.User;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface IItemDb {
    public ItemStack get(String var1, int var2) throws Exception;

    public ItemStack get(String var1) throws Exception;

    public String names(ItemStack var1);

    public String name(ItemStack var1);

    public List<ItemStack> getMatching(User var1, String[] var2) throws Exception;
}

