/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.yaml.snakeyaml.Yaml
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.storage.Comment;
import com.earth2me.essentials.storage.EnchantmentLevel;
import com.earth2me.essentials.storage.IStorageWriter;
import com.earth2me.essentials.storage.StorageObject;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.yaml.snakeyaml.Yaml;

public class YamlStorageWriter
implements IStorageWriter {
    private static final transient Pattern NON_WORD_PATTERN = Pattern.compile("\\W");
    private static final transient Yaml YAML = new Yaml();
    private final transient PrintWriter writer;

    public YamlStorageWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void save(StorageObject object) {
        try {
            this.writeToFile(object, 0, object.getClass());
        }
        catch (IllegalArgumentException ex) {
            Logger.getLogger(YamlStorageWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(YamlStorageWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeToFile(Object object, int depth, Class clazz) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            int modifier = field.getModifiers();
            if (!Modifier.isPrivate(modifier) || Modifier.isTransient(modifier) || Modifier.isStatic(modifier)) continue;
            field.setAccessible(true);
            Object data = field.get(object);
            if (this.writeKey(field, depth, data)) continue;
            if (data instanceof StorageObject) {
                this.writer.println();
                this.writeToFile(data, depth + 1, data.getClass());
                continue;
            }
            if (data instanceof Map) {
                this.writeMap((Map)data, depth + 1);
                continue;
            }
            if (data instanceof Collection) {
                this.writeCollection((Collection)data, depth + 1);
                continue;
            }
            if (data instanceof Location) {
                this.writeLocation((Location)data, depth + 1);
                continue;
            }
            this.writeScalar(data);
            this.writer.println();
        }
    }

    private boolean writeKey(Field field, int depth, Object data) {
        boolean commentPresent = this.writeComment(field, depth);
        if (data == null && !commentPresent) {
            return true;
        }
        this.writeIndention(depth);
        if (data == null && commentPresent) {
            this.writer.print('#');
        }
        String name = field.getName();
        this.writer.print(name);
        this.writer.print(": ");
        if (data == null && commentPresent) {
            this.writer.println();
            this.writer.println();
            return true;
        }
        return false;
    }

    private boolean writeComment(Field field, int depth) {
        boolean commentPresent = field.isAnnotationPresent(Comment.class);
        if (commentPresent) {
            Comment comments = (Comment)field.getAnnotation(Comment.class);
            for (String comment : comments.value()) {
                String trimmed = comment.trim();
                if (trimmed.isEmpty()) continue;
                this.writeIndention(depth);
                this.writer.print("# ");
                this.writer.print(trimmed);
                this.writer.println();
            }
        }
        return commentPresent;
    }

    private void writeCollection(Collection<Object> data, int depth) throws IllegalAccessException {
        this.writer.println();
        if (data.isEmpty()) {
            this.writer.println();
        }
        for (Object entry : data) {
            if (entry == null) continue;
            this.writeIndention(depth);
            this.writer.print("- ");
            if (entry instanceof StorageObject) {
                this.writer.println();
                this.writeToFile(entry, depth + 1, entry.getClass());
                continue;
            }
            if (entry instanceof Location) {
                this.writeLocation((Location)entry, depth + 1);
                continue;
            }
            this.writeScalar(entry);
        }
        this.writer.println();
    }

    private void writeMap(Map<Object, Object> data, int depth) throws IllegalArgumentException, IllegalAccessException {
        this.writer.println();
        if (data.isEmpty()) {
            this.writer.println();
        }
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            if (value == null) continue;
            this.writeIndention(depth);
            this.writeKey(entry.getKey());
            this.writer.print(": ");
            if (value instanceof StorageObject) {
                this.writer.println();
                this.writeToFile(value, depth + 1, value.getClass());
                continue;
            }
            if (value instanceof Collection) {
                this.writeCollection((Collection)value, depth + 1);
                continue;
            }
            if (value instanceof Location) {
                this.writeLocation((Location)value, depth + 1);
                continue;
            }
            this.writeScalar(value);
            this.writer.println();
        }
    }

    private void writeIndention(int depth) {
        for (int i = 0; i < depth; ++i) {
            this.writer.print("  ");
        }
    }

    private void writeScalar(Object data) {
        if (data instanceof String || data instanceof Boolean || data instanceof Number) {
            Yaml yaml = YAML;
            synchronized (yaml) {
                YAML.dumpAll(Collections.singletonList(data).iterator(), (Writer)this.writer);
            }
        } else if (data instanceof Material) {
            this.writeMaterial(data);
            this.writer.println();
        } else if (data instanceof MaterialData) {
            this.writeMaterialData(data);
            this.writer.println();
        } else if (data instanceof ItemStack) {
            this.writeItemStack(data);
            this.writer.println();
        } else if (data instanceof EnchantmentLevel) {
            this.writeEnchantmentLevel(data);
            this.writer.println();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void writeKey(Object data) {
        if (data instanceof String || data instanceof Boolean || data instanceof Number) {
            String output = data.toString();
            if (NON_WORD_PATTERN.matcher(output).find()) {
                this.writer.print('\"');
                this.writer.print(output.replace("\"", "\\\""));
                this.writer.print('\"');
            } else {
                this.writer.print(output);
            }
        } else if (data instanceof Material) {
            this.writeMaterial(data);
        } else if (data instanceof MaterialData) {
            this.writeMaterialData(data);
        } else if (data instanceof EnchantmentLevel) {
            this.writeEnchantmentLevel(data);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void writeMaterial(Object data) {
        this.writer.print(data.toString().toLowerCase(Locale.ENGLISH));
    }

    private void writeMaterialData(Object data) {
        MaterialData matData = (MaterialData)data;
        this.writeMaterial((Object)matData.getItemType());
        if (matData.getData() > 0) {
            this.writer.print(':');
            this.writer.print(matData.getData());
        }
    }

    private void writeItemStack(Object data) {
        ItemStack itemStack = (ItemStack)data;
        this.writeMaterialData((Object)itemStack.getData());
        this.writer.print(' ');
        this.writer.print(itemStack.getAmount());
        for (Map.Entry entry : itemStack.getEnchantments().entrySet()) {
            this.writer.print(' ');
            this.writeEnchantmentLevel(entry);
        }
    }

    private void writeEnchantmentLevel(Object data) {
        Map.Entry enchLevel = (Map.Entry)data;
        this.writer.print(((Enchantment)enchLevel.getKey()).getName().toLowerCase(Locale.ENGLISH));
        this.writer.print(':');
        this.writer.print(enchLevel.getValue());
    }

    private void writeLocation(Location entry, int depth) {
        this.writer.println();
        this.writeIndention(depth);
        this.writer.print("world: ");
        this.writeScalar(entry.getWorld().getName());
        this.writeIndention(depth);
        this.writer.print("x: ");
        this.writeScalar(entry.getX());
        this.writeIndention(depth);
        this.writer.print("y: ");
        this.writeScalar(entry.getY());
        this.writeIndention(depth);
        this.writer.print("z: ");
        this.writeScalar(entry.getZ());
        this.writeIndention(depth);
        this.writer.print("yaw: ");
        this.writeScalar(Float.valueOf(entry.getYaw()));
        this.writeIndention(depth);
        this.writer.print("pitch: ");
        this.writeScalar(Float.valueOf(entry.getPitch()));
    }
}

