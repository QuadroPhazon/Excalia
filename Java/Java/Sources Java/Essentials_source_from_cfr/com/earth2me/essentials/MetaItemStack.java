/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  org.bukkit.Color
 *  org.bukkit.DyeColor
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Builder
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.UnsafeValues
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BookMeta
 *  org.bukkit.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.earth2me.essentials;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Enchantments;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Potions;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.BookInput;
import com.earth2me.essentials.textreader.BookPager;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.utils.FormatUtil;
import com.earth2me.essentials.utils.NumberUtil;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MetaItemStack {
    private static final Map<String, DyeColor> colorMap = new HashMap<String, DyeColor>();
    private static final Map<String, FireworkEffect.Type> fireworkShape = new HashMap<String, FireworkEffect.Type>();
    private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");
    private ItemStack stack;
    private FireworkEffect.Builder builder = FireworkEffect.builder();
    private PotionEffectType pEffectType;
    private PotionEffect pEffect;
    private boolean validFirework = false;
    private boolean validPotionEffect = false;
    private boolean validPotionDuration = false;
    private boolean validPotionPower = false;
    private boolean completePotion = false;
    private int power = 1;
    private int duration = 120;

    public MetaItemStack(ItemStack stack) {
        this.stack = stack.clone();
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public boolean isValidFirework() {
        return this.validFirework;
    }

    public boolean isValidPotion() {
        return this.validPotionEffect && this.validPotionDuration && this.validPotionPower;
    }

    public FireworkEffect.Builder getFireworkBuilder() {
        return this.builder;
    }

    public PotionEffect getPotionEffect() {
        return this.pEffect;
    }

    public boolean completePotion() {
        return this.completePotion;
    }

    private void resetPotionMeta() {
        this.pEffect = null;
        this.pEffectType = null;
        this.validPotionEffect = false;
        this.validPotionDuration = false;
        this.validPotionPower = false;
        this.completePotion = true;
    }

    public void parseStringMeta(CommandSource sender, boolean allowUnsafe, String[] string, int fromArg, IEssentials ess) throws Exception {
        if (string[fromArg].startsWith("{")) {
            try {
                this.stack = ess.getServer().getUnsafe().modifyItemStack(this.stack, Joiner.on((char)' ').join(Arrays.asList(string).subList(fromArg, string.length)));
            }
            catch (NoSuchMethodError nsme) {
                throw new Exception(I18n._("noMetaJson", new Object[0]), nsme);
            }
            catch (Throwable throwable) {
                throw new Exception(throwable.getMessage(), throwable);
            }
        } else {
            for (int i = fromArg; i < string.length; ++i) {
                this.addStringMeta(sender, allowUnsafe, string[i], ess);
            }
            if (this.validFirework) {
                if (!this.hasMetaPermission(sender, "firework", true, true, ess)) {
                    throw new Exception(I18n._("noMetaFirework", new Object[0]));
                }
                FireworkEffect effect = this.builder.build();
                FireworkMeta fmeta = (FireworkMeta)this.stack.getItemMeta();
                fmeta.addEffect(effect);
                if (fmeta.getEffects().size() > 1 && !this.hasMetaPermission(sender, "firework-multiple", true, true, ess)) {
                    throw new Exception(I18n._("multipleCharges", new Object[0]));
                }
                this.stack.setItemMeta((ItemMeta)fmeta);
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void addStringMeta(CommandSource sender, boolean allowUnsafe, String string, IEssentials ess) throws Exception {
        String[] split = this.splitPattern.split(string, 2);
        if (split.length < 1) {
            return;
        }
        if (split.length > 1 && split[0].equalsIgnoreCase("name") && this.hasMetaPermission(sender, "name", false, true, ess)) {
            String displayName = FormatUtil.replaceFormat(split[1].replace('_', ' '));
            ItemMeta meta = this.stack.getItemMeta();
            meta.setDisplayName(displayName);
            this.stack.setItemMeta(meta);
            return;
        } else if (split.length > 1 && (split[0].equalsIgnoreCase("lore") || split[0].equalsIgnoreCase("desc")) && this.hasMetaPermission(sender, "lore", false, true, ess)) {
            ArrayList<String> lore = new ArrayList<String>();
            for (String line : split[1].split("\\|")) {
                lore.add(FormatUtil.replaceFormat(line.replace('_', ' ')));
            }
            ItemMeta meta = this.stack.getItemMeta();
            meta.setLore(lore);
            this.stack.setItemMeta(meta);
            return;
        } else if (split.length > 1 && (split[0].equalsIgnoreCase("player") || split[0].equalsIgnoreCase("owner")) && this.stack.getType() == Material.SKULL_ITEM && this.hasMetaPermission(sender, "head", false, true, ess)) {
            if (this.stack.getDurability() != 3) throw new Exception(I18n._("onlyPlayerSkulls", new Object[0]));
            String owner = split[1];
            SkullMeta meta = (SkullMeta)this.stack.getItemMeta();
            meta.setOwner(owner);
            this.stack.setItemMeta((ItemMeta)meta);
            return;
        } else if (split.length > 1 && split[0].equalsIgnoreCase("book") && this.stack.getType() == Material.WRITTEN_BOOK && (this.hasMetaPermission(sender, "book", true, true, ess) || this.hasMetaPermission(sender, "chapter-" + split[1].toLowerCase(Locale.ENGLISH), true, true, ess))) {
            BookMeta meta = (BookMeta)this.stack.getItemMeta();
            BookInput input = new BookInput("book", true, ess);
            BookPager pager = new BookPager(input);
            List<String> pages = pager.getPages(split[1]);
            meta.setPages(pages);
            this.stack.setItemMeta((ItemMeta)meta);
            return;
        } else if (split.length > 1 && split[0].equalsIgnoreCase("author") && this.stack.getType() == Material.WRITTEN_BOOK && this.hasMetaPermission(sender, "author", false, true, ess)) {
            String author = FormatUtil.replaceFormat(split[1]);
            BookMeta meta = (BookMeta)this.stack.getItemMeta();
            meta.setAuthor(author);
            this.stack.setItemMeta((ItemMeta)meta);
            return;
        } else if (split.length > 1 && split[0].equalsIgnoreCase("title") && this.stack.getType() == Material.WRITTEN_BOOK && this.hasMetaPermission(sender, "title", false, true, ess)) {
            String title = FormatUtil.replaceFormat(split[1].replace('_', ' '));
            BookMeta meta = (BookMeta)this.stack.getItemMeta();
            meta.setTitle(title);
            this.stack.setItemMeta((ItemMeta)meta);
            return;
        } else if (split.length > 1 && split[0].equalsIgnoreCase("power") && this.stack.getType() == Material.FIREWORK && this.hasMetaPermission(sender, "firework-power", false, true, ess)) {
            int power = NumberUtil.isInt(split[1]) ? Integer.parseInt(split[1]) : 0;
            FireworkMeta meta = (FireworkMeta)this.stack.getItemMeta();
            meta.setPower(power > 3 ? 4 : power);
            this.stack.setItemMeta((ItemMeta)meta);
            return;
        } else if (this.stack.getType() == Material.FIREWORK) {
            this.addFireworkMeta(sender, false, string, ess);
            return;
        } else if (this.stack.getType() == Material.POTION) {
            this.addPotionMeta(sender, false, string, ess);
            return;
        } else if (split.length > 1 && (split[0].equalsIgnoreCase("color") || split[0].equalsIgnoreCase("colour")) && (this.stack.getType() == Material.LEATHER_BOOTS || this.stack.getType() == Material.LEATHER_CHESTPLATE || this.stack.getType() == Material.LEATHER_HELMET || this.stack.getType() == Material.LEATHER_LEGGINGS)) {
            String[] color = split[1].split("(\\||,)");
            if (color.length != 3) throw new Exception(I18n._("leatherSyntax", new Object[0]));
            int red = NumberUtil.isInt(color[0]) ? Integer.parseInt(color[0]) : 0;
            int green = NumberUtil.isInt(color[1]) ? Integer.parseInt(color[1]) : 0;
            int blue = NumberUtil.isInt(color[2]) ? Integer.parseInt(color[2]) : 0;
            LeatherArmorMeta meta = (LeatherArmorMeta)this.stack.getItemMeta();
            meta.setColor(Color.fromRGB((int)red, (int)green, (int)blue));
            this.stack.setItemMeta((ItemMeta)meta);
            return;
        } else {
            this.parseEnchantmentStrings(sender, allowUnsafe, split, ess);
        }
    }

    public void addFireworkMeta(CommandSource sender, boolean allowShortName, String string, IEssentials ess) throws Exception {
        if (this.stack.getType() == Material.FIREWORK) {
            String[] split = this.splitPattern.split(string, 2);
            if (split.length < 2) {
                return;
            }
            if (split[0].equalsIgnoreCase("color") || split[0].equalsIgnoreCase("colour") || allowShortName && split[0].equalsIgnoreCase("c")) {
                String[] colors;
                if (this.validFirework) {
                    if (!this.hasMetaPermission(sender, "firework", true, true, ess)) {
                        throw new Exception(I18n._("noMetaFirework", new Object[0]));
                    }
                    FireworkEffect effect = this.builder.build();
                    FireworkMeta fmeta = (FireworkMeta)this.stack.getItemMeta();
                    fmeta.addEffect(effect);
                    if (fmeta.getEffects().size() > 1 && !this.hasMetaPermission(sender, "firework-multiple", true, true, ess)) {
                        throw new Exception(I18n._("multipleCharges", new Object[0]));
                    }
                    this.stack.setItemMeta((ItemMeta)fmeta);
                    this.builder = FireworkEffect.builder();
                }
                ArrayList<Color> primaryColors = new ArrayList<Color>();
                for (String color : colors = split[1].split(",")) {
                    if (!colorMap.containsKey(color.toUpperCase())) {
                        throw new Exception(I18n._("invalidFireworkFormat", split[1], split[0]));
                    }
                    this.validFirework = true;
                    primaryColors.add(colorMap.get(color.toUpperCase()).getFireworkColor());
                }
                this.builder.withColor(primaryColors);
            } else if (split[0].equalsIgnoreCase("shape") || split[0].equalsIgnoreCase("type") || allowShortName && (split[0].equalsIgnoreCase("s") || split[0].equalsIgnoreCase("t"))) {
                FireworkEffect.Type finalEffect = null;
                String string2 = split[1] = split[1].equalsIgnoreCase("large") ? "BALL_LARGE" : split[1];
                if (!fireworkShape.containsKey(split[1].toUpperCase())) {
                    throw new Exception(I18n._("invalidFireworkFormat", split[1], split[0]));
                }
                finalEffect = fireworkShape.get(split[1].toUpperCase());
                if (finalEffect != null) {
                    this.builder.with(finalEffect);
                }
            } else if (split[0].equalsIgnoreCase("fade") || allowShortName && split[0].equalsIgnoreCase("f")) {
                String[] colors;
                ArrayList<Color> fadeColors = new ArrayList<Color>();
                for (String color : colors = split[1].split(",")) {
                    if (!colorMap.containsKey(color.toUpperCase())) {
                        throw new Exception(I18n._("invalidFireworkFormat", split[1], split[0]));
                    }
                    fadeColors.add(colorMap.get(color.toUpperCase()).getFireworkColor());
                }
                if (!fadeColors.isEmpty()) {
                    this.builder.withFade(fadeColors);
                }
            } else if (split[0].equalsIgnoreCase("effect") || allowShortName && split[0].equalsIgnoreCase("e")) {
                String[] effects;
                for (String effect : effects = split[1].split(",")) {
                    if (effect.equalsIgnoreCase("twinkle")) {
                        this.builder.flicker(true);
                        continue;
                    }
                    if (effect.equalsIgnoreCase("trail")) {
                        this.builder.trail(true);
                        continue;
                    }
                    throw new Exception(I18n._("invalidFireworkFormat", split[1], split[0]));
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void addPotionMeta(CommandSource sender, boolean allowShortName, String string, IEssentials ess) throws Exception {
        if (this.stack.getType() != Material.POTION) return;
        String[] split = this.splitPattern.split(string, 2);
        if (split.length < 2) {
            return;
        }
        if (split[0].equalsIgnoreCase("effect") || allowShortName && split[0].equalsIgnoreCase("e")) {
            this.pEffectType = Potions.getByName(split[1]);
            if (this.pEffectType == null || this.pEffectType.getName() == null) throw new Exception(I18n._("invalidPotionMeta", split[1]));
            if (!this.hasMetaPermission(sender, "potions." + this.pEffectType.getName().toLowerCase(Locale.ENGLISH), true, false, ess)) throw new Exception(I18n._("noPotionEffectPerm", this.pEffectType.getName().toLowerCase(Locale.ENGLISH)));
            this.validPotionEffect = true;
        } else if (split[0].equalsIgnoreCase("power") || allowShortName && split[0].equalsIgnoreCase("p")) {
            if (!NumberUtil.isInt(split[1])) throw new Exception(I18n._("invalidPotionMeta", split[1]));
            this.validPotionPower = true;
            this.power = Integer.parseInt(split[1]);
            if (this.power > 0 && this.power < 4) {
                --this.power;
            }
        } else if (split[0].equalsIgnoreCase("duration") || allowShortName && split[0].equalsIgnoreCase("d")) {
            if (!NumberUtil.isInt(split[1])) throw new Exception(I18n._("invalidPotionMeta", split[1]));
            this.validPotionDuration = true;
            this.duration = Integer.parseInt(split[1]) * 20;
        }
        if (!this.isValidPotion()) return;
        PotionMeta pmeta = (PotionMeta)this.stack.getItemMeta();
        this.pEffect = this.pEffectType.createEffect(this.duration, this.power);
        if (pmeta.getCustomEffects().size() > 1 && !this.hasMetaPermission(sender, "potions.multiple", true, false, ess)) {
            throw new Exception(I18n._("multiplePotionEffects", new Object[0]));
        }
        pmeta.addCustomEffect(this.pEffect, true);
        this.stack.setItemMeta((ItemMeta)pmeta);
        this.resetPotionMeta();
    }

    private void parseEnchantmentStrings(CommandSource sender, boolean allowUnsafe, String[] split, IEssentials ess) throws Exception {
        Enchantment enchantment = Enchantments.getByName(split[0]);
        if (enchantment == null || !this.hasMetaPermission(sender, "enchantments." + enchantment.getName().toLowerCase(Locale.ENGLISH), false, false, ess)) {
            return;
        }
        int level = -1;
        if (split.length > 1) {
            try {
                level = Integer.parseInt(split[1]);
            }
            catch (NumberFormatException ex) {
                level = -1;
            }
        }
        if (level < 0 || !allowUnsafe && level > enchantment.getMaxLevel()) {
            level = enchantment.getMaxLevel();
        }
        this.addEnchantment(sender, allowUnsafe, enchantment, level);
    }

    public void addEnchantment(CommandSource sender, boolean allowUnsafe, Enchantment enchantment, int level) throws Exception {
        if (enchantment == null) {
            throw new Exception(I18n._("enchantmentNotFound", new Object[0]));
        }
        try {
            if (this.stack.getType().equals((Object)Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.stack.getItemMeta();
                if (level == 0) {
                    meta.removeStoredEnchant(enchantment);
                } else {
                    meta.addStoredEnchant(enchantment, level, allowUnsafe);
                }
                this.stack.setItemMeta((ItemMeta)meta);
            } else if (level == 0) {
                this.stack.removeEnchantment(enchantment);
            } else if (allowUnsafe) {
                this.stack.addUnsafeEnchantment(enchantment, level);
            } else {
                this.stack.addEnchantment(enchantment, level);
            }
        }
        catch (Exception ex) {
            throw new Exception("Enchantment " + enchantment.getName() + ": " + ex.getMessage(), ex);
        }
    }

    public Enchantment getEnchantment(User user, String name) throws Exception {
        Enchantment enchantment = Enchantments.getByName(name);
        if (enchantment == null) {
            return null;
        }
        String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
        if (!this.hasMetaPermission(user, "enchantments." + enchantmentName, true, false)) {
            throw new Exception(I18n._("enchantmentPerm", enchantmentName));
        }
        return enchantment;
    }

    private boolean hasMetaPermission(CommandSource sender, String metaPerm, boolean graceful, boolean includeBase, IEssentials ess) throws Exception {
        User user = sender != null && sender.isPlayer() ? ess.getUser(sender.getPlayer()) : null;
        return this.hasMetaPermission(user, metaPerm, graceful, includeBase);
    }

    private boolean hasMetaPermission(User user, String metaPerm, boolean graceful, boolean includeBase) throws Exception {
        String permBase;
        String string = permBase = includeBase ? "essentials.itemspawn.meta-" : "essentials.";
        if (user == null || user.isAuthorized(permBase + metaPerm)) {
            return true;
        }
        if (graceful) {
            return false;
        }
        throw new Exception(I18n._("noMetaPerm", metaPerm));
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            colorMap.put(color.name(), color);
        }
        for (DyeColor type : FireworkEffect.Type.values()) {
            fireworkShape.put(type.name(), (FireworkEffect.Type)type);
        }
    }
}

