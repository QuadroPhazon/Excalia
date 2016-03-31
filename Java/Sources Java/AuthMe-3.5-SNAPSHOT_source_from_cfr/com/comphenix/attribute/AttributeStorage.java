/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.base.Preconditions
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package com.comphenix.attribute;

import com.comphenix.attribute.Attributes;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AttributeStorage {
    private ItemStack target;
    private final UUID uniqueKey;

    private AttributeStorage(ItemStack target, UUID uniqueKey) {
        this.target = (ItemStack)Preconditions.checkNotNull((Object)target, (Object)"target cannot be NULL");
        this.uniqueKey = (UUID)Preconditions.checkNotNull((Object)uniqueKey, (Object)"uniqueKey cannot be NULL");
    }

    public static AttributeStorage newTarget(ItemStack target, UUID uniqueKey) {
        return new AttributeStorage(target, uniqueKey);
    }

    public String getData(String defaultValue) {
        Attributes.Attribute current = this.getAttribute(new Attributes(this.target), this.uniqueKey);
        return current != null ? current.getName() : defaultValue;
    }

    public boolean hasData() {
        return this.getAttribute(new Attributes(this.target), this.uniqueKey) != null;
    }

    public void setData(String data) {
        Attributes attributes = new Attributes(this.target);
        Attributes.Attribute current = this.getAttribute(attributes, this.uniqueKey);
        if (current == null) {
            attributes.add(Attributes.Attribute.newBuilder().name(data).amount(this.getBaseDamage(this.target)).uuid(this.uniqueKey).operation(Attributes.Operation.ADD_NUMBER).type(Attributes.AttributeType.GENERIC_ATTACK_DAMAGE).build());
        } else {
            current.setName(data);
        }
        this.target = attributes.getStack();
    }

    private int getBaseDamage(ItemStack stack) {
        switch (stack.getType()) {
            case WOOD_SWORD: {
                return 4;
            }
            case GOLD_SWORD: {
                return 4;
            }
            case STONE_SWORD: {
                return 5;
            }
            case IRON_SWORD: {
                return 6;
            }
            case DIAMOND_SWORD: {
                return 7;
            }
            case WOOD_AXE: {
                return 3;
            }
            case GOLD_AXE: {
                return 3;
            }
            case STONE_AXE: {
                return 4;
            }
            case IRON_AXE: {
                return 5;
            }
            case DIAMOND_AXE: {
                return 6;
            }
        }
        return 0;
    }

    public ItemStack getTarget() {
        return this.target;
    }

    private Attributes.Attribute getAttribute(Attributes attributes, UUID id) {
        for (Attributes.Attribute attribute : attributes.values()) {
            if (!Objects.equal((Object)attribute.getUUID(), (Object)id)) continue;
            return attribute;
        }
        return null;
    }

}

