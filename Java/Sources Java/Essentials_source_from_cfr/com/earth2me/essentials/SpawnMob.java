/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Zombie
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Mob;
import com.earth2me.essentials.MobData;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.LocationUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class SpawnMob {
    public static String mobList(User user) {
        Set<String> mobList = Mob.getMobList();
        HashSet<String> availableList = new HashSet<String>();
        for (String mob : mobList) {
            if (!user.isAuthorized("essentials.spawnmob." + mob.toLowerCase(Locale.ENGLISH))) continue;
            availableList.add(mob);
        }
        if (availableList.isEmpty()) {
            availableList.add(I18n._("none", new Object[0]));
        }
        return StringUtil.joinList(availableList);
    }

    public static List<String> mobParts(String mobString) {
        String[] mobParts = mobString.split(",");
        ArrayList<String> mobs = new ArrayList<String>();
        for (String mobPart : mobParts) {
            String[] mobDatas = mobPart.split(":");
            mobs.add(mobDatas[0]);
        }
        return mobs;
    }

    public static List<String> mobData(String mobString) {
        String[] mobParts = mobString.split(",");
        ArrayList<String> mobData = new ArrayList<String>();
        for (String mobPart : mobParts) {
            String[] mobDatas = mobPart.split(":");
            if (mobDatas.length == 1) {
                if (mobPart.contains(":")) {
                    mobData.add("");
                    continue;
                }
                mobData.add(null);
                continue;
            }
            mobData.add(mobDatas[1]);
        }
        return mobData;
    }

    public static void spawnmob(IEssentials ess, Server server, User user, List<String> parts, List<String> data, int mobCount) throws Exception {
        Block block = LocationUtil.getTarget((LivingEntity)user.getBase()).getBlock();
        if (block == null) {
            throw new Exception(I18n._("unableToSpawnMob", new Object[0]));
        }
        SpawnMob.spawnmob(ess, server, user.getSource(), user, block.getLocation(), parts, data, mobCount);
    }

    public static void spawnmob(IEssentials ess, Server server, CommandSource sender, User target, List<String> parts, List<String> data, int mobCount) throws Exception {
        SpawnMob.spawnmob(ess, server, sender, target, target.getLocation(), parts, data, mobCount);
    }

    public static void spawnmob(IEssentials ess, Server server, CommandSource sender, User target, Location loc, List<String> parts, List<String> data, int mobCount) throws Exception {
        Location sloc = LocationUtil.getSafeDestination(loc);
        for (int i = 0; i < parts.size(); ++i) {
            Mob mob = Mob.fromName(parts.get(i));
            SpawnMob.checkSpawnable(ess, sender, mob);
        }
        int serverLimit = ess.getSettings().getSpawnMobLimit();
        int effectiveLimit = serverLimit / parts.size();
        if (effectiveLimit < 1) {
            effectiveLimit = 1;
            while (parts.size() > serverLimit) {
                parts.remove(serverLimit);
            }
        }
        if (mobCount > effectiveLimit) {
            mobCount = effectiveLimit;
            sender.sendMessage(I18n._("mobSpawnLimit", new Object[0]));
        }
        Mob mob = Mob.fromName(parts.get(0));
        try {
            for (int i2 = 0; i2 < mobCount; ++i2) {
                SpawnMob.spawnMob(ess, server, sender, target, sloc, parts, data);
            }
            sender.sendMessage("" + mobCount * parts.size() + " " + mob.name.toLowerCase(Locale.ENGLISH) + mob.suffix + " " + I18n._("spawned", new Object[0]));
        }
        catch (Mob.MobException e1) {
            throw new Exception(I18n._("unableToSpawnMob", new Object[0]), e1);
        }
        catch (NumberFormatException e2) {
            throw new Exception(I18n._("numberRequired", new Object[0]), e2);
        }
        catch (NullPointerException np) {
            throw new Exception(I18n._("soloMob", new Object[0]), np);
        }
    }

    private static void spawnMob(IEssentials ess, Server server, CommandSource sender, User target, Location sloc, List<String> parts, List<String> data) throws Exception {
        Entity spawnedMob = null;
        for (int i = 0; i < parts.size(); ++i) {
            int next;
            if (i == 0) {
                Mob mob = Mob.fromName(parts.get(i));
                spawnedMob = mob.spawn(sloc.getWorld(), server, sloc);
                SpawnMob.defaultMobData(mob.getType(), spawnedMob);
                if (data.get(i) != null) {
                    SpawnMob.changeMobData(sender, mob.getType(), spawnedMob, data.get(i).toLowerCase(Locale.ENGLISH), target);
                }
            }
            if ((next = i + 1) >= parts.size()) continue;
            Mob mMob = Mob.fromName(parts.get(next));
            Entity spawnedMount = mMob.spawn(sloc.getWorld(), server, sloc);
            SpawnMob.defaultMobData(mMob.getType(), spawnedMount);
            if (data.get(next) != null) {
                SpawnMob.changeMobData(sender, mMob.getType(), spawnedMount, data.get(next).toLowerCase(Locale.ENGLISH), target);
            }
            spawnedMob.setPassenger(spawnedMount);
            spawnedMob = spawnedMount;
        }
    }

    private static void checkSpawnable(IEssentials ess, CommandSource sender, Mob mob) throws Exception {
        if (mob == null) {
            throw new Exception(I18n._("invalidMob", new Object[0]));
        }
        if (ess.getSettings().getProtectPreventSpawn(mob.getType().toString().toLowerCase(Locale.ENGLISH))) {
            throw new Exception(I18n._("disabledToSpawnMob", new Object[0]));
        }
        if (sender.isPlayer() && !ess.getUser(sender.getPlayer()).isAuthorized("essentials.spawnmob." + mob.name.toLowerCase(Locale.ENGLISH))) {
            throw new Exception(I18n._("noPermToSpawnMob", new Object[0]));
        }
    }

    private static void changeMobData(CommandSource sender, EntityType type, Entity spawned, String inputData, User target) throws Exception {
        String data = inputData;
        if (data.isEmpty()) {
            sender.sendMessage(I18n._("mobDataList", StringUtil.joinList(MobData.getValidHelp(spawned))));
        }
        if ((spawned instanceof Zombie || type == EntityType.SKELETON) && (inputData.contains("armor") || inputData.contains("armour"))) {
            EntityEquipment invent = ((LivingEntity)spawned).getEquipment();
            if (inputData.contains("noarmor") || inputData.contains("noarmour")) {
                invent.clear();
            } else if (inputData.contains("diamond")) {
                invent.setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));
                invent.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
                invent.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
                invent.setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1));
            } else if (inputData.contains("gold")) {
                invent.setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
                invent.setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
                invent.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
                invent.setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
            } else if (inputData.contains("leather")) {
                invent.setBoots(new ItemStack(Material.LEATHER_BOOTS, 1));
                invent.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS, 1));
                invent.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
                invent.setHelmet(new ItemStack(Material.LEATHER_HELMET, 1));
            } else {
                invent.setBoots(new ItemStack(Material.IRON_BOOTS, 1));
                invent.setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
                invent.setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
                invent.setHelmet(new ItemStack(Material.IRON_HELMET, 1));
            }
            invent.setBootsDropChance(0.0f);
            invent.setLeggingsDropChance(0.0f);
            invent.setChestplateDropChance(0.0f);
            invent.setHelmetDropChance(0.0f);
        }
        MobData newData = MobData.fromData(spawned, data);
        while (newData != null) {
            newData.setData(spawned, target.getBase(), data);
            data = data.replace(newData.getMatched(), "");
            newData = MobData.fromData(spawned, data);
        }
    }

    private static void defaultMobData(EntityType type, Entity spawned) {
        EntityEquipment invent;
        if (type == EntityType.SKELETON) {
            invent = ((LivingEntity)spawned).getEquipment();
            invent.setItemInHand(new ItemStack(Material.BOW, 1));
            invent.setItemInHandDropChance(0.1f);
            invent.setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
            invent.setBootsDropChance(0.0f);
        }
        if (type == EntityType.PIG_ZOMBIE) {
            invent = ((LivingEntity)spawned).getEquipment();
            invent.setItemInHand(new ItemStack(Material.GOLD_SWORD, 1));
            invent.setItemInHandDropChance(0.1f);
            invent.setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
            invent.setBootsDropChance(0.0f);
        }
        if (type == EntityType.ZOMBIE) {
            invent = ((LivingEntity)spawned).getEquipment();
            invent.setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
            invent.setBootsDropChance(0.0f);
        }
        if (type == EntityType.HORSE) {
            ((Horse)spawned).setJumpStrength(1.2);
        }
    }
}

