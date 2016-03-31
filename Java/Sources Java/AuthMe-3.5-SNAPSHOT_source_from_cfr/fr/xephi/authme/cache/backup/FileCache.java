/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemFactory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package fr.xephi.authme.cache.backup;

import com.comphenix.attribute.Attributes;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.API;
import fr.xephi.authme.cache.backup.DataFileCache;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FileCache {
    private AuthMe plugin;

    public FileCache(AuthMe plugin) {
        this.plugin = plugin;
        File file = new File(plugin.getDataFolder() + File.separator + "cache");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public void createCache(Player player, DataFileCache playerData, String group, boolean operator, boolean flying) {
        String path = "";
        try {
            path = player.getUniqueId().toString();
        }
        catch (Exception e) {
            path = player.getName();
        }
        catch (Error e) {
            path = player.getName();
        }
        File file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "playerdatas.cache");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (file.exists()) {
            return;
        }
        FileWriter writer = null;
        try {
            file.createNewFile();
            writer = new FileWriter(file);
            writer.write(group + API.newline);
            writer.write(String.valueOf(operator) + API.newline);
            writer.write(String.valueOf(flying) + API.newline);
            writer.close();
            file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "inventory");
            file.mkdir();
            ItemStack[] inv = playerData.getInventory();
            for (int i = 0; i < inv.length; ++i) {
                ItemStack item = inv[i];
                file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "inventory" + File.separator + i + ".cache");
                file.createNewFile();
                writer = new FileWriter(file);
                if (item != null) {
                    if (item.getType() == Material.AIR) {
                        writer.write("AIR");
                        writer.close();
                        continue;
                    }
                    writer.write(item.getType().name() + API.newline);
                    writer.write("" + item.getDurability() + API.newline);
                    writer.write("" + item.getAmount() + API.newline);
                    writer.flush();
                    if (item.hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.hasDisplayName()) {
                            writer.write("name=" + meta.getDisplayName() + API.newline);
                        }
                        if (meta.hasLore()) {
                            String lores = "";
                            for (String lore : meta.getLore()) {
                                lores = lore + "%newline%";
                            }
                            writer.write("lore=" + lores + API.newline);
                        }
                        if (meta.hasEnchants()) {
                            for (Enchantment ench : meta.getEnchants().keySet()) {
                                writer.write("metaenchant=" + ench.getName() + ":" + meta.getEnchants().get((Object)ench) + API.newline);
                            }
                        }
                        writer.flush();
                    }
                    for (Enchantment ench : item.getEnchantments().keySet()) {
                        writer.write("enchant=" + ench.getName() + ":" + item.getEnchantments().get((Object)ench) + API.newline);
                    }
                    Attributes attributes = new Attributes(item);
                    if (attributes != null) {
                        while (attributes.values().iterator().hasNext()) {
                            Attributes.Attribute a = attributes.values().iterator().next();
                            if (a == null) continue;
                            try {
                                writer.write("attribute=" + a.getName() + ";" + a.getAttributeType().getMinecraftId() + ";" + a.getAmount() + ";" + a.getOperation().getId() + ";" + a.getUUID().toString());
                            }
                            catch (Exception e) {}
                        }
                    }
                } else {
                    writer.write("AIR");
                }
                writer.close();
            }
            file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "armours");
            file.mkdir();
            ItemStack[] armors = playerData.getArmour();
            for (int i2 = 0; i2 < armors.length; ++i2) {
                ItemStack item = armors[i2];
                file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "armours" + File.separator + i2 + ".cache");
                file.createNewFile();
                writer = new FileWriter(file);
                if (item != null) {
                    if (item.getType() == Material.AIR) {
                        writer.write("AIR");
                        writer.close();
                        continue;
                    }
                    writer.write(item.getType().name() + API.newline);
                    writer.write("" + item.getDurability() + API.newline);
                    writer.write("" + item.getAmount() + API.newline);
                    writer.flush();
                    if (item.hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        if (meta.hasDisplayName()) {
                            writer.write("name=" + meta.getDisplayName() + API.newline);
                        }
                        if (meta.hasLore()) {
                            String lores = "";
                            for (String lore : meta.getLore()) {
                                lores = lore + "%newline%";
                            }
                            writer.write("lore=" + lores + API.newline);
                        }
                        writer.flush();
                    }
                    for (Enchantment ench : item.getEnchantments().keySet()) {
                        writer.write("enchant=" + ench.getName() + ":" + item.getEnchantments().get((Object)ench) + API.newline);
                    }
                    Attributes attributes = new Attributes(item);
                    if (attributes != null) {
                        while (attributes.values().iterator().hasNext()) {
                            Attributes.Attribute a = attributes.values().iterator().next();
                            if (a == null) continue;
                            try {
                                writer.write("attribute=" + a.getName() + ";" + a.getAttributeType().getMinecraftId() + ";" + a.getAmount() + ";" + a.getOperation().getId() + ";" + a.getUUID().toString());
                            }
                            catch (Exception e) {}
                        }
                    }
                } else {
                    writer.write("AIR" + API.newline);
                }
                writer.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataFileCache readCache(Player player) {
        boolean op;
        boolean flying;
        String group;
        ItemStack[] inv;
        ItemStack[] armours;
        String path = "";
        try {
            path = player.getUniqueId().toString();
        }
        catch (Exception e) {
            path = player.getName();
        }
        catch (Error e) {
            path = player.getName();
        }
        File file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + ".playerdatas.cache");
        String playername = player.getName().toLowerCase();
        if (!file.exists()) {
            String group2;
            ItemStack[] stacki;
            boolean op2;
            ItemStack[] stacka;
            boolean flying2;
            file = new File("cache/" + playername + ".cache");
            stacki = new ItemStack[36];
            stacka = new ItemStack[4];
            if (!file.exists()) {
                return new DataFileCache(stacki, stacka);
            }
            group2 = null;
            op2 = false;
            flying2 = false;
            Scanner reader = null;
            try {
                reader = new Scanner(file);
                int i = 0;
                int a = 0;
                while (reader.hasNextLine()) {
                    String[] in;
                    ArrayList<String> loreList;
                    ItemMeta meta;
                    String line = reader.nextLine();
                    if (!line.contains(":")) {
                        String[] playerInfo = line.split(";");
                        group2 = playerInfo[0];
                        op2 = Integer.parseInt(playerInfo[1]) == 1;
                        if (playerInfo.length <= 2) continue;
                        if (Integer.parseInt(playerInfo[2]) == 1) {
                            flying2 = true;
                            continue;
                        }
                        flying2 = false;
                        continue;
                    }
                    if (!line.startsWith("i") && !line.startsWith("w")) continue;
                    String lores = "";
                    String name = "";
                    if (line.split("\\*").length > 1) {
                        lores = line.split("\\*")[1];
                        line = line.split("\\*")[0];
                    }
                    if (line.split(";").length > 1) {
                        name = line.split(";")[1];
                        line = line.split(";")[0];
                    }
                    if ((in = line.split(":"))[0].equals("i")) {
                        stacki[i] = new ItemStack(Material.getMaterial((String)in[1]), Integer.parseInt(in[2]), Short.parseShort(in[3]));
                        if (in.length > 4 && !in[4].isEmpty()) {
                            for (int k = 4; k < in.length - 1; ++k) {
                                stacki[i].addUnsafeEnchantment(Enchantment.getByName((String)in[k]), Integer.parseInt(in[k + 1]));
                                ++k;
                            }
                        }
                        try {
                            meta = this.plugin.getServer().getItemFactory().getItemMeta(stacki[i].getType());
                            if (!name.isEmpty()) {
                                meta.setDisplayName(name);
                            }
                            if (!lores.isEmpty()) {
                                loreList = new ArrayList<String>();
                                for (String s : lores.split("%newline%")) {
                                    loreList.add(s);
                                }
                                meta.setLore(loreList);
                            }
                            if (meta != null) {
                                stacki[i].setItemMeta(meta);
                            }
                        }
                        catch (Exception e) {
                            // empty catch block
                        }
                        ++i;
                        continue;
                    }
                    stacka[a] = new ItemStack(Material.getMaterial((String)in[1]), Integer.parseInt(in[2]), Short.parseShort(in[3]));
                    if (in.length > 4 && !in[4].isEmpty()) {
                        for (int k = 4; k < in.length - 1; ++k) {
                            stacka[a].addUnsafeEnchantment(Enchantment.getByName((String)in[k]), Integer.parseInt(in[k + 1]));
                            ++k;
                        }
                    }
                    try {
                        meta = this.plugin.getServer().getItemFactory().getItemMeta(stacka[a].getType());
                        if (!name.isEmpty()) {
                            meta.setDisplayName(name);
                        }
                        if (!lores.isEmpty()) {
                            loreList = new ArrayList();
                            for (String s : lores.split("%newline%")) {
                                loreList.add(s);
                            }
                            meta.setLore(loreList);
                        }
                        if (meta != null) {
                            stacki[i].setItemMeta(meta);
                        }
                    }
                    catch (Exception e) {
                        // empty catch block
                    }
                    ++a;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
            return new DataFileCache(stacki, stacka, group2, op2, flying2);
        }
        inv = new ItemStack[36];
        armours = new ItemStack[4];
        group = null;
        op = false;
        flying = false;
        Scanner reader = null;
        try {
            ArrayList<String> lore;
            Attributes.Operation operation;
            Attributes attributes;
            int i;
            UUID uuid;
            String name;
            String[] args;
            String line;
            ItemStack item;
            ItemMeta meta;
            Attributes.Attribute attribute;
            boolean v;
            reader = new Scanner(file);
            int count = 1;
            while (reader.hasNextLine()) {
                String line2 = reader.nextLine();
                switch (count) {
                    case 1: {
                        group = line2;
                        break;
                    }
                    case 2: {
                        op = Boolean.parseBoolean(line2);
                        break;
                    }
                    case 3: {
                        flying = Boolean.parseBoolean(line2);
                        break;
                    }
                }
                ++count;
            }
            if (reader != null) {
                reader.close();
            }
            for (i = 0; i < inv.length; ++i) {
                reader = new Scanner(new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "inventory" + File.separator + i + ".cache"));
                item = new ItemStack(Material.AIR);
                meta = null;
                attributes = null;
                count = 1;
                v = true;
                block45 : while (reader.hasNextLine() && v) {
                    line = reader.nextLine();
                    switch (count) {
                        case 1: {
                            item.setType(Material.getMaterial((String)line));
                            if (item.getType() != Material.AIR) continue block45;
                            v = false;
                            continue block45;
                        }
                        case 2: {
                            item.setDurability(Short.parseShort(line));
                            continue block45;
                        }
                        case 3: {
                            item.setAmount(Integer.parseInt(line));
                            continue block45;
                        }
                        case 4: {
                            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
                            break;
                        }
                    }
                    if (line.startsWith("name=")) {
                        line = line.substring(5);
                        meta.setDisplayName(line);
                        item.setItemMeta(meta);
                        continue;
                    }
                    if (line.startsWith("lore=")) {
                        line = line.substring(5);
                        lore = new ArrayList<String>();
                        for (String s : line.split("%newline%")) {
                            lore.add(s);
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        continue;
                    }
                    if (line.startsWith("enchant=")) {
                        line = line.substring(8);
                        item.addEnchantment(Enchantment.getByName((String)line.split(":")[0]), Integer.parseInt(line.split(":")[1]));
                    }
                    if (line.startsWith("attribute=")) {
                        if (attributes == null) {
                            attributes = new Attributes(item);
                        }
                        try {
                            line = line.substring(10);
                            args = line.split(";");
                            if (args.length != 5) continue;
                            name = args[0];
                            Attributes.AttributeType type = Attributes.AttributeType.fromId(args[1]);
                            double amount = Double.parseDouble(args[2]);
                            operation = Attributes.Operation.fromId(Integer.parseInt(args[3]));
                            uuid = UUID.fromString(args[4]);
                            attribute = new Attributes.Attribute(new Attributes.Attribute.Builder(amount, operation, type, name, uuid));
                            attributes.add(attribute);
                        }
                        catch (Exception e) {
                            // empty catch block
                        }
                    }
                    ++count;
                }
                if (reader != null) {
                    reader.close();
                }
                inv[i] = attributes != null ? attributes.getStack() : item;
            }
            for (i = 0; i < armours.length; ++i) {
                reader = new Scanner(new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "armours" + File.separator + i + ".cache"));
                item = new ItemStack(Material.AIR);
                meta = null;
                attributes = null;
                count = 1;
                v = true;
                block48 : while (reader.hasNextLine() && v) {
                    line = reader.nextLine();
                    switch (count) {
                        case 1: {
                            item.setType(Material.getMaterial((String)line));
                            if (item.getType() != Material.AIR) continue block48;
                            v = false;
                            continue block48;
                        }
                        case 2: {
                            item.setDurability(Short.parseShort(line));
                            continue block48;
                        }
                        case 3: {
                            item.setAmount(Integer.parseInt(line));
                            continue block48;
                        }
                        case 4: {
                            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
                            break;
                        }
                    }
                    if (line.startsWith("name=")) {
                        line = line.substring(5);
                        meta.setDisplayName(line);
                        item.setItemMeta(meta);
                        continue;
                    }
                    if (line.startsWith("lore=")) {
                        line = line.substring(5);
                        lore = new ArrayList();
                        for (String s : line.split("%newline%")) {
                            lore.add(s);
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        continue;
                    }
                    if (line.startsWith("enchant=")) {
                        line = line.substring(8);
                        item.addEnchantment(Enchantment.getByName((String)line.split(":")[0]), Integer.parseInt(line.split(":")[1]));
                    }
                    if (line.startsWith("attribute=")) {
                        if (attributes == null) {
                            attributes = new Attributes(item);
                        }
                        try {
                            line = line.substring(10);
                            args = line.split(";");
                            if (args.length != 5) continue;
                            name = args[0];
                            Attributes.AttributeType type = Attributes.AttributeType.fromId(args[1]);
                            double amount = Double.parseDouble(args[2]);
                            operation = Attributes.Operation.fromId(Integer.parseInt(args[3]));
                            uuid = UUID.fromString(args[4]);
                            attribute = new Attributes.Attribute(new Attributes.Attribute.Builder(amount, operation, type, name, uuid));
                            attributes.add(attribute);
                        }
                        catch (Exception e) {
                            // empty catch block
                        }
                    }
                    ++count;
                }
                if (reader != null) {
                    reader.close();
                }
                armours[i] = attributes != null ? attributes.getStack() : item;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }
        return new DataFileCache(inv, armours, group, op, flying);
    }

    public void removeCache(Player player) {
        String path = "";
        try {
            path = player.getUniqueId().toString();
        }
        catch (Exception e) {
            path = player.getName();
        }
        catch (Error e) {
            path = player.getName();
        }
        File file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path);
        if (!file.exists()) {
            file = new File("cache/" + player.getName().toLowerCase() + ".cache");
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    if (f.isDirectory()) {
                        for (File a : f.listFiles()) {
                            a.delete();
                        }
                        f.delete();
                        continue;
                    }
                    f.delete();
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }

    public boolean doesCacheExist(Player player) {
        String path = "";
        try {
            path = player.getUniqueId().toString();
        }
        catch (Exception e) {
            path = player.getName();
        }
        catch (Error e) {
            path = player.getName();
        }
        File file = new File(this.plugin.getDataFolder() + File.separator + "cache" + File.separator + path + File.separator + "playerdatas.cache");
        if (!file.exists()) {
            file = new File("cache/" + player.getName().toLowerCase() + ".cache");
        }
        if (file.exists()) {
            return true;
        }
        return false;
    }
}

