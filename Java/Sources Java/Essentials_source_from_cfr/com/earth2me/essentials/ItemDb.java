/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IConf;
import com.earth2me.essentials.ManagedFile;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import net.ess3.api.IItemDb;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemDb
implements IConf,
IItemDb {
    private final transient IEssentials ess;
    private final transient Map<String, Integer> items = new HashMap<String, Integer>();
    private final transient Map<ItemData, List<String>> names = new HashMap<ItemData, List<String>>();
    private final transient Map<ItemData, String> primaryName = new HashMap<ItemData, String>();
    private final transient Map<String, Short> durabilities = new HashMap<String, Short>();
    private final transient ManagedFile file;
    private final transient Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

    public ItemDb(IEssentials ess) {
        this.ess = ess;
        this.file = new ManagedFile("items.csv", ess);
    }

    @Override
    public void reloadConfig() {
        List<String> lines = this.file.getLines();
        if (lines.isEmpty()) {
            return;
        }
        this.durabilities.clear();
        this.items.clear();
        this.names.clear();
        this.primaryName.clear();
        for (String line : lines) {
            List nameList;
            String[] parts;
            if ((line = line.trim().toLowerCase(Locale.ENGLISH)).length() > 0 && line.charAt(0) == '#' || (parts = line.split("[^a-z0-9]")).length < 2) continue;
            int numeric = Integer.parseInt(parts[1]);
            short data = parts.length > 2 && !parts[2].equals("0") ? Short.parseShort(parts[2]) : 0;
            String itemName = parts[0].toLowerCase(Locale.ENGLISH);
            this.durabilities.put(itemName, data);
            this.items.put(itemName, numeric);
            ItemData itemData = new ItemData(numeric, data);
            if (this.names.containsKey(itemData)) {
                nameList = this.names.get(itemData);
                nameList.add(itemName);
                Collections.sort(nameList, new LengthCompare());
                continue;
            }
            nameList = new ArrayList<String>();
            nameList.add((String)itemName);
            this.names.put(itemData, nameList);
            this.primaryName.put(itemData, itemName);
        }
    }

    @Override
    public ItemStack get(String id, int quantity) throws Exception {
        ItemStack retval = this.get(id.toLowerCase(Locale.ENGLISH));
        retval.setAmount(quantity);
        return retval;
    }

    @Override
    public ItemStack get(String id) throws Exception {
        int itemid = 0;
        String itemname = null;
        short metaData = 0;
        Matcher parts = this.splitPattern.matcher(id);
        if (parts.matches()) {
            itemname = parts.group(2);
            metaData = Short.parseShort(parts.group(3));
        } else {
            itemname = id;
        }
        if (NumberUtil.isInt(itemname)) {
            itemid = Integer.parseInt(itemname);
        } else if (NumberUtil.isInt(id)) {
            itemid = Integer.parseInt(id);
        } else {
            itemname = itemname.toLowerCase(Locale.ENGLISH);
        }
        if (itemid < 1) {
            Material bMaterial;
            if (this.items.containsKey(itemname)) {
                itemid = this.items.get(itemname);
                if (this.durabilities.containsKey(itemname) && metaData == 0) {
                    metaData = this.durabilities.get(itemname);
                }
            } else if (Material.getMaterial((String)itemname.toUpperCase(Locale.ENGLISH)) != null) {
                bMaterial = Material.getMaterial((String)itemname.toUpperCase(Locale.ENGLISH));
                itemid = bMaterial.getId();
            } else {
                try {
                    bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemname.toLowerCase(Locale.ENGLISH));
                    itemid = bMaterial.getId();
                }
                catch (Throwable throwable) {
                    throw new Exception(I18n._("unknownItemName", itemname), throwable);
                }
            }
        }
        if (itemid < 1) {
            throw new Exception(I18n._("unknownItemName", itemname));
        }
        Material mat = Material.getMaterial((int)itemid);
        if (mat == null) {
            throw new Exception(I18n._("unknownItemId", itemid));
        }
        ItemStack retval = new ItemStack(mat);
        retval.setAmount(mat.getMaxStackSize());
        retval.setDurability(metaData);
        return retval;
    }

    @Override
    public List<ItemStack> getMatching(User user, String[] args) throws Exception {
        ArrayList<ItemStack> is = new ArrayList<ItemStack>();
        if (args.length < 1) {
            is.add(user.getItemInHand());
        } else if (args[0].equalsIgnoreCase("hand")) {
            is.add(user.getItemInHand());
        } else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("invent") || args[0].equalsIgnoreCase("all")) {
            for (ItemStack stack : user.getInventory().getContents()) {
                if (stack == null || stack.getType() == Material.AIR) continue;
                is.add(stack);
            }
        } else if (args[0].equalsIgnoreCase("blocks")) {
            for (ItemStack stack : user.getInventory().getContents()) {
                if (stack == null || stack.getTypeId() > 255 || stack.getType() == Material.AIR) continue;
                is.add(stack);
            }
        } else {
            is.add(this.get(args[0]));
        }
        if (is.isEmpty() || ((ItemStack)is.get(0)).getType() == Material.AIR) {
            throw new Exception(I18n._("itemSellAir", new Object[0]));
        }
        return is;
    }

    @Override
    public String names(ItemStack item) {
        ItemData itemData = new ItemData(item.getTypeId(), item.getDurability());
        List<String> nameList = this.names.get(itemData);
        if (nameList == null && (nameList = this.names.get(itemData = new ItemData(item.getTypeId(), 0))) == null) {
            return null;
        }
        if (nameList.size() > 15) {
            nameList = nameList.subList(0, 14);
        }
        return StringUtil.joinList(", ", nameList);
    }

    @Override
    public String name(ItemStack item) {
        ItemData itemData = new ItemData(item.getTypeId(), item.getDurability());
        String name = this.primaryName.get(itemData);
        if (name == null && (name = this.primaryName.get(itemData = new ItemData(item.getTypeId(), 0))) == null) {
            return null;
        }
        return name;
    }

    class LengthCompare
    implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }

    static class ItemData {
        private final int itemNo;
        private final short itemData;

        ItemData(int itemNo, short itemData) {
            this.itemNo = itemNo;
            this.itemData = itemData;
        }

        public int getItemNo() {
            return this.itemNo;
        }

        public short getItemData() {
            return this.itemData;
        }

        public int hashCode() {
            return 31 * this.itemNo ^ this.itemData;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof ItemData)) {
                return false;
            }
            ItemData pairo = (ItemData)o;
            return this.itemNo == pairo.getItemNo() && this.itemData == pairo.getItemData();
        }
    }

}

