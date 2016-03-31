/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Objects
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Maps
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  org.bukkit.inventory.ItemStack
 */
package com.comphenix.attribute;

import com.comphenix.attribute.NbtFactory;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Attributes {
    public ItemStack stack;
    private NbtFactory.NbtList attributes;

    public Attributes(ItemStack stack) {
        this.stack = NbtFactory.getCraftItemStack(stack);
        this.loadAttributes(false);
    }

    private void loadAttributes(boolean createIfMissing) {
        if (this.attributes == null) {
            NbtFactory.NbtCompound nbt = NbtFactory.fromItemTag(this.stack);
            this.attributes = nbt.getList("AttributeModifiers", createIfMissing);
        }
    }

    private void removeAttributes() {
        NbtFactory.NbtCompound nbt = NbtFactory.fromItemTag(this.stack);
        nbt.remove("AttributeModifiers");
        this.attributes = null;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int size() {
        return this.attributes != null ? this.attributes.size() : 0;
    }

    public void add(Attribute attribute) {
        Preconditions.checkNotNull((Object)attribute.getName(), (Object)"must specify an attribute name.");
        this.loadAttributes(true);
        this.attributes.add(attribute.data);
    }

    public boolean remove(Attribute attribute) {
        if (this.attributes == null) {
            return false;
        }
        UUID uuid = attribute.getUUID();
        Iterator<Attribute> it = this.values().iterator();
        while (it.hasNext()) {
            if (!Objects.equal((Object)it.next().getUUID(), (Object)uuid)) continue;
            it.remove();
            if (this.size() == 0) {
                this.removeAttributes();
            }
            return true;
        }
        return false;
    }

    public void clear() {
        this.removeAttributes();
    }

    public Attribute get(int index) {
        if (this.size() == 0) {
            throw new IllegalStateException("Attribute list is empty.");
        }
        return new Attribute((NbtFactory.NbtCompound)this.attributes.get(index));
    }

    public Iterable<Attribute> values() {
        return new Iterable<Attribute>(){

            @Override
            public Iterator<Attribute> iterator() {
                if (Attributes.this.size() == 0) {
                    return Collections.emptyList().iterator();
                }
                return Iterators.transform(Attributes.this.attributes.iterator(), (Function)new Function<Object, Attribute>(){

                    public Attribute apply(@Nullable Object element) {
                        return new Attribute((NbtFactory.NbtCompound)element);
                    }
                });
            }

        };
    }

    public static class Attribute {
        private NbtFactory.NbtCompound data;

        public Attribute(Builder builder) {
            this.data = NbtFactory.createCompound();
            this.setAmount(builder.amount);
            this.setOperation(builder.operation);
            this.setAttributeType(builder.type);
            this.setName(builder.name);
            this.setUUID(builder.uuid);
        }

        private Attribute(NbtFactory.NbtCompound data) {
            this.data = data;
        }

        public double getAmount() {
            return this.data.getDouble("Amount", 0.0);
        }

        public void setAmount(double amount) {
            this.data.put("Amount", amount);
        }

        public Operation getOperation() {
            return Operation.fromId(this.data.getInteger("Operation", 0));
        }

        public void setOperation(@Nonnull Operation operation) {
            Preconditions.checkNotNull((Object)((Object)operation), (Object)"operation cannot be NULL.");
            this.data.put("Operation", operation.getId());
        }

        public AttributeType getAttributeType() {
            return AttributeType.fromId(this.data.getString("AttributeName", null));
        }

        public void setAttributeType(@Nonnull AttributeType type) {
            Preconditions.checkNotNull((Object)type, (Object)"type cannot be NULL.");
            this.data.put("AttributeName", type.getMinecraftId());
        }

        public String getName() {
            return this.data.getString("Name", null);
        }

        public void setName(@Nonnull String name) {
            Preconditions.checkNotNull((Object)name, (Object)"name cannot be NULL.");
            this.data.put("Name", name);
        }

        public UUID getUUID() {
            return new UUID(this.data.getLong("UUIDMost", null), this.data.getLong("UUIDLeast", null));
        }

        public void setUUID(@Nonnull UUID id) {
            Preconditions.checkNotNull((Object)"id", (Object)"id cannot be NULL.");
            this.data.put("UUIDLeast", id.getLeastSignificantBits());
            this.data.put("UUIDMost", id.getMostSignificantBits());
        }

        public static Builder newBuilder() {
            return new Builder().uuid(UUID.randomUUID()).operation(Operation.ADD_NUMBER);
        }

        public static class Builder {
            private double amount;
            private Operation operation = Operation.ADD_NUMBER;
            private AttributeType type;
            private String name;
            private UUID uuid;

            private Builder() {
            }

            public Builder(double amount, Operation operation, AttributeType type, String name, UUID uuid) {
                this.amount = amount;
                this.operation = operation;
                this.type = type;
                this.name = name;
                this.uuid = uuid;
            }

            public Builder amount(double amount) {
                this.amount = amount;
                return this;
            }

            public Builder operation(Operation operation) {
                this.operation = operation;
                return this;
            }

            public Builder type(AttributeType type) {
                this.type = type;
                return this;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder uuid(UUID uuid) {
                this.uuid = uuid;
                return this;
            }

            public Attribute build() {
                return new Attribute(this);
            }
        }

    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class AttributeType {
        private static ConcurrentMap<String, AttributeType> LOOKUP = Maps.newConcurrentMap();
        public static final AttributeType GENERIC_MAX_HEALTH = new AttributeType("generic.maxHealth").register();
        public static final AttributeType GENERIC_FOLLOW_RANGE = new AttributeType("generic.followRange").register();
        public static final AttributeType GENERIC_ATTACK_DAMAGE = new AttributeType("generic.attackDamage").register();
        public static final AttributeType GENERIC_MOVEMENT_SPEED = new AttributeType("generic.movementSpeed").register();
        public static final AttributeType GENERIC_KNOCKBACK_RESISTANCE = new AttributeType("generic.knockbackResistance").register();
        private final String minecraftId;

        public AttributeType(String minecraftId) {
            this.minecraftId = minecraftId;
        }

        public String getMinecraftId() {
            return this.minecraftId;
        }

        public AttributeType register() {
            AttributeType old = LOOKUP.putIfAbsent(this.minecraftId, this);
            return old != null ? old : this;
        }

        public static AttributeType fromId(String minecraftId) {
            return LOOKUP.get(minecraftId);
        }

        public static Iterable<AttributeType> values() {
            return LOOKUP.values();
        }
    }

    public static enum Operation {
        ADD_NUMBER(0),
        MULTIPLY_PERCENTAGE(1),
        ADD_PERCENTAGE(2);
        
        private int id;

        private Operation(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static Operation fromId(int id) {
            for (Operation op : Operation.values()) {
                if (op.getId() != id) continue;
                return op;
            }
            throw new IllegalArgumentException("Corrupt operation ID " + id + " detected.");
        }
    }

}

