/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 */
package com.earth2me.essentials.storage;

import java.util.Map;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentLevel
implements Map.Entry<Enchantment, Integer> {
    private Enchantment enchantment;
    private int level;

    public EnchantmentLevel(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public Enchantment getKey() {
        return this.enchantment;
    }

    @Override
    public Integer getValue() {
        return this.level;
    }

    @Override
    public Integer setValue(Integer v) {
        int t = this.level;
        this.level = v;
        return t;
    }

    @Override
    public int hashCode() {
        return this.enchantment.hashCode() ^ this.level;
    }

    @Override
    public boolean equals(Object obj) {
        Map.Entry entry;
        if (obj instanceof Map.Entry && (entry = (Map.Entry)obj).getKey() instanceof Enchantment && entry.getValue() instanceof Integer) {
            Enchantment enchant = (Enchantment)entry.getKey();
            Integer lvl = (Integer)entry.getValue();
            return this.enchantment.equals((Object)enchant) && this.level == lvl;
        }
        return false;
    }
}

