/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Ageable
 *  org.bukkit.entity.AnimalTamer
 *  org.bukkit.entity.Creeper
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.Horse$Color
 *  org.bukkit.entity.Horse$Style
 *  org.bukkit.entity.Horse$Variant
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Ocelot
 *  org.bukkit.entity.Ocelot$Type
 *  org.bukkit.entity.Pig
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.Skeleton$SkeletonType
 *  org.bukkit.entity.Slime
 *  org.bukkit.entity.Tameable
 *  org.bukkit.entity.Villager
 *  org.bukkit.entity.Villager$Profession
 *  org.bukkit.entity.Wolf
 *  org.bukkit.entity.Zombie
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.HorseInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.Colorable
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

public enum MobData {
    BABY_AGEABLE("baby", Ageable.class, (Object)((Object)Data.BABY), true),
    BABY_PIG("piglet", (Object)EntityType.PIG, (Object)((Object)Data.BABY), false),
    BABY_WOLF("puppy", (Object)EntityType.WOLF, (Object)((Object)Data.BABY), false),
    BABY_CHICKEN("chick", (Object)EntityType.CHICKEN, (Object)((Object)Data.BABY), false),
    BABY_HORSE("colt", (Object)EntityType.HORSE, (Object)((Object)Data.BABY), false),
    BABY_OCELOT("kitten", (Object)EntityType.OCELOT, (Object)((Object)Data.BABY), false),
    BABY_SHEEP("lamb", (Object)EntityType.SHEEP, (Object)((Object)Data.BABY), false),
    BABY_COW("calf", EntityType.COW.getEntityClass(), (Object)((Object)Data.BABY), false),
    BABY_VILLAGER("child", (Object)EntityType.VILLAGER, (Object)((Object)Data.BABY), false),
    TAMED_TAMEABLE("tamed", Tameable.class, (Object)((Object)Data.TAMED), true),
    TAME_TAMEABLE("tame", Tameable.class, (Object)((Object)Data.TAMED), false),
    RANDOM_SHEEP("random", (Object)EntityType.SHEEP, (Object)((Object)Data.COLORABLE), true),
    COLORABLE_SHEEP("", StringUtil.joinList((Object[])DyeColor.values()).toLowerCase(Locale.ENGLISH), (Object)EntityType.SHEEP, (Object)((Object)Data.COLORABLE), true),
    DONKEY_HORSE("donkey", (Object)EntityType.HORSE, (Object)Horse.Variant.DONKEY, true),
    MULE_HORSE("mule", (Object)EntityType.HORSE, (Object)Horse.Variant.MULE, true),
    SKELETON_HORSE("skeleton", (Object)EntityType.HORSE, (Object)Horse.Variant.SKELETON_HORSE, true),
    UNDEAD_HORSE("undead", (Object)EntityType.HORSE, (Object)Horse.Variant.UNDEAD_HORSE, true),
    ZOMBIE_HORSE("zombie", (Object)EntityType.HORSE, (Object)Horse.Variant.UNDEAD_HORSE, false),
    POLKA_HORSE("polka", (Object)EntityType.HORSE, (Object)Horse.Style.BLACK_DOTS, true),
    SOOTY_HORSE("sooty", (Object)EntityType.HORSE, (Object)Horse.Style.BLACK_DOTS, false),
    BLAZE_HORSE("blaze", (Object)EntityType.HORSE, (Object)Horse.Style.WHITE, true),
    SOCKS_HORSE("socks", (Object)EntityType.HORSE, (Object)Horse.Style.WHITE, false),
    LEOPARD_HORSE("leopard", (Object)EntityType.HORSE, (Object)Horse.Style.WHITE_DOTS, true),
    APPALOOSA_HORSE("appaloosa", (Object)EntityType.HORSE, (Object)Horse.Style.WHITE_DOTS, false),
    PAINT_HORSE("paint", (Object)EntityType.HORSE, (Object)Horse.Style.WHITEFIELD, true),
    MILKY_HORSE("milky", (Object)EntityType.HORSE, (Object)Horse.Style.WHITEFIELD, false),
    SPLOTCHY_HORSE("splotchy", (Object)EntityType.HORSE, (Object)Horse.Style.WHITEFIELD, false),
    BLACK_HORSE("black", (Object)EntityType.HORSE, (Object)Horse.Color.BLACK, true),
    CHESTNUT_HORSE("chestnut", (Object)EntityType.HORSE, (Object)Horse.Color.CHESTNUT, true),
    LIVER_HORSE("liver", (Object)EntityType.HORSE, (Object)Horse.Color.CHESTNUT, false),
    CREAMY_HORSE("creamy", (Object)EntityType.HORSE, (Object)Horse.Color.CREAMY, true),
    FLAXEN_HORSE("flaxen", (Object)EntityType.HORSE, (Object)Horse.Color.CREAMY, false),
    GRAY_HORSE("gray", (Object)EntityType.HORSE, (Object)Horse.Color.GRAY, true),
    DAPPLE_HORSE("dapple", (Object)EntityType.HORSE, (Object)Horse.Color.GRAY, false),
    BUCKSKIN_HORSE("buckskin", (Object)EntityType.HORSE, (Object)Horse.Color.DARK_BROWN, true),
    DARKBROWN_HORSE("darkbrown", (Object)EntityType.HORSE, (Object)Horse.Color.DARK_BROWN, false),
    DARK_HORSE("dark", (Object)EntityType.HORSE, (Object)Horse.Color.DARK_BROWN, false),
    DBROWN_HORSE("dbrown", (Object)EntityType.HORSE, (Object)Horse.Color.DARK_BROWN, false),
    BAY_HORSE("bay", (Object)EntityType.HORSE, (Object)Horse.Color.BROWN, true),
    BROWN_HORSE("brown", (Object)EntityType.HORSE, (Object)Horse.Color.BROWN, false),
    CHEST_HORSE("chest", (Object)EntityType.HORSE, (Object)((Object)Data.CHEST), true),
    SADDLE_HORSE("saddle", (Object)EntityType.HORSE, (Object)((Object)Data.HORSESADDLE), true),
    GOLD_ARMOR_HORSE("goldarmor", (Object)EntityType.HORSE, (Object)Material.GOLD_BARDING, true),
    DIAMOND_ARMOR_HORSE("diamondarmor", (Object)EntityType.HORSE, (Object)Material.DIAMOND_BARDING, true),
    ARMOR_HORSE("armor", (Object)EntityType.HORSE, (Object)Material.IRON_BARDING, true),
    SIAMESE_CAT("siamese", (Object)EntityType.OCELOT, (Object)Ocelot.Type.SIAMESE_CAT, true),
    WHITE_CAT("white", (Object)EntityType.OCELOT, (Object)Ocelot.Type.SIAMESE_CAT, false),
    RED_CAT("red", (Object)EntityType.OCELOT, (Object)Ocelot.Type.RED_CAT, true),
    ORANGE_CAT("orange", (Object)EntityType.OCELOT, (Object)Ocelot.Type.RED_CAT, false),
    TABBY_CAT("tabby", (Object)EntityType.OCELOT, (Object)Ocelot.Type.RED_CAT, false),
    BLACK_CAT("black", (Object)EntityType.OCELOT, (Object)Ocelot.Type.BLACK_CAT, true),
    TUXEDO_CAT("tuxedo", (Object)EntityType.OCELOT, (Object)Ocelot.Type.BLACK_CAT, false),
    VILLAGER_ZOMBIE("villager", EntityType.ZOMBIE.getEntityClass(), (Object)((Object)Data.VILLAGER), true),
    BABY_ZOMBIE("baby", EntityType.ZOMBIE.getEntityClass(), (Object)((Object)Data.BABYZOMBIE), true),
    DIAMOND_SWORD_ZOMBIE("diamondsword", EntityType.ZOMBIE.getEntityClass(), (Object)Material.DIAMOND_SWORD, true),
    GOLD_SWORD_ZOMBIE("goldsword", EntityType.ZOMBIE.getEntityClass(), (Object)Material.GOLD_SWORD, true),
    IRON_SWORD_ZOMBIE("ironsword", EntityType.ZOMBIE.getEntityClass(), (Object)Material.IRON_SWORD, true),
    STONE_SWORD_ZOMBIE("stonesword", EntityType.ZOMBIE.getEntityClass(), (Object)Material.STONE_SWORD, false),
    SWORD_ZOMBIE("sword", EntityType.ZOMBIE.getEntityClass(), (Object)Material.STONE_SWORD, true),
    DIAMOND_SWORD_SKELETON("diamondsword", (Object)EntityType.SKELETON, (Object)Material.DIAMOND_SWORD, true),
    GOLD_SWORD_SKELETON("goldsword", (Object)EntityType.SKELETON, (Object)Material.GOLD_SWORD, true),
    IRON_SWORD_SKELETON("ironsword", (Object)EntityType.SKELETON, (Object)Material.IRON_SWORD, true),
    STONE_SWORD_SKELETON("stonesword", (Object)EntityType.SKELETON, (Object)Material.STONE_SWORD, false),
    SWORD_SKELETON("sword", (Object)EntityType.SKELETON, (Object)Material.STONE_SWORD, true),
    BOW_SKELETON("bow", (Object)EntityType.SKELETON, (Object)Material.BOW, true),
    WHITHER_SKELETON("wither", (Object)EntityType.SKELETON, (Object)((Object)Data.WITHER), true),
    POWERED_CREEPER("powered", (Object)EntityType.CREEPER, (Object)((Object)Data.ELECTRIFIED), true),
    ELECTRIC_CREEPER("electric", (Object)EntityType.CREEPER, (Object)((Object)Data.ELECTRIFIED), false),
    CHARGED_CREEPER("charged", (Object)EntityType.CREEPER, (Object)((Object)Data.ELECTRIFIED), false),
    SADDLE_PIG("saddle", (Object)EntityType.PIG, (Object)((Object)Data.PIGSADDLE), true),
    ANGRY_WOLF("angry", (Object)EntityType.WOLF, (Object)((Object)Data.ANGRY), true),
    RABID_WOLF("rabid", (Object)EntityType.WOLF, (Object)((Object)Data.ANGRY), false),
    FARMER_VILLAGER("farmer", (Object)EntityType.VILLAGER, (Object)Villager.Profession.FARMER, true),
    LIBRARIAN_VILLAGER("librarian", (Object)EntityType.VILLAGER, (Object)Villager.Profession.LIBRARIAN, true),
    PRIEST_VILLAGER("priest", (Object)EntityType.VILLAGER, (Object)Villager.Profession.PRIEST, true),
    FATHER_VILLAGER("father", (Object)EntityType.VILLAGER, (Object)Villager.Profession.PRIEST, false),
    SMITH_VILLAGER("smith", (Object)EntityType.VILLAGER, (Object)Villager.Profession.BLACKSMITH, true),
    BUTCHER_VILLAGER("butcher", (Object)EntityType.VILLAGER, (Object)Villager.Profession.BUTCHER, true),
    SIZE_SLIME("", "<1-100>", EntityType.SLIME.getEntityClass(), (Object)((Object)Data.SIZE), true),
    NUM_EXPERIENCE_ORB("", "<1-2000000000>", (Object)EntityType.EXPERIENCE_ORB, (Object)((Object)Data.EXP), true);
    
    public static final Logger logger;
    private final String nickname;
    private final String helpMessage;
    private final Object type;
    private final Object value;
    private final boolean isPublic;
    private String matched;

    private MobData(String n2, Object type, Object value, boolean isPublic) {
        this.nickname = n2;
        this.matched = n2;
        this.helpMessage = n2;
        this.type = type;
        this.value = value;
        this.isPublic = isPublic;
    }

    private MobData(String n2, String h, Object type, Object value, boolean isPublic) {
        this.nickname = n2;
        this.matched = n2;
        this.helpMessage = h;
        this.type = type;
        this.value = value;
        this.isPublic = isPublic;
    }

    public static LinkedHashMap<String, MobData> getPossibleData(Entity spawned, boolean publicOnly) {
        LinkedHashMap<String, MobData> mobList = new LinkedHashMap<String, MobData>();
        for (MobData data : MobData.values()) {
            if (data.type instanceof EntityType && spawned.getType().equals(data.type) && (publicOnly && data.isPublic || !publicOnly)) {
                mobList.put(data.nickname.toLowerCase(Locale.ENGLISH), data);
                continue;
            }
            if (!(data.type instanceof Class) || !((Class)data.type).isAssignableFrom(spawned.getClass()) || (!publicOnly || !data.isPublic) && publicOnly) continue;
            mobList.put(data.nickname.toLowerCase(Locale.ENGLISH), data);
        }
        return mobList;
    }

    public static List<String> getValidHelp(Entity spawned) {
        ArrayList<String> output = new ArrayList<String>();
        LinkedHashMap<String, MobData> posData = MobData.getPossibleData(spawned, true);
        for (MobData data : posData.values()) {
            output.add(data.helpMessage);
        }
        return output;
    }

    public static MobData fromData(Entity spawned, String name) {
        if (name.isEmpty()) {
            return null;
        }
        LinkedHashMap<String, MobData> posData = MobData.getPossibleData(spawned, false);
        for (String data : posData.keySet()) {
            if (!name.contains(data)) continue;
            return posData.get(data);
        }
        return null;
    }

    public String getMatched() {
        return this.matched;
    }

    public void setData(Entity spawned, Player target, String rawData) throws Exception {
        if (this.value.equals((Object)Data.ANGRY)) {
            ((Wolf)spawned).setAngry(true);
        } else if (this.value.equals((Object)Data.BABY)) {
            ((Ageable)spawned).setBaby();
        } else if (this.value.equals((Object)Data.BABYZOMBIE)) {
            ((Zombie)spawned).setBaby(true);
        } else if (this.value.equals((Object)Data.CHEST)) {
            ((Horse)spawned).setTamed(true);
            ((Horse)spawned).setCarryingChest(true);
        } else if (this.value.equals((Object)Data.ELECTRIFIED)) {
            ((Creeper)spawned).setPowered(true);
        } else if (this.value.equals((Object)Data.HORSESADDLE)) {
            Horse horse = (Horse)spawned;
            horse.setTamed(true);
            horse.setOwner((AnimalTamer)target);
            horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
        } else if (this.value.equals((Object)Data.PIGSADDLE)) {
            ((Pig)spawned).setSaddle(true);
        } else if (this.value.equals((Object)Data.TAMED)) {
            Tameable tameable = (Tameable)spawned;
            tameable.setTamed(true);
            tameable.setOwner((AnimalTamer)target);
        } else if (this.value.equals((Object)Data.VILLAGER)) {
            ((Zombie)spawned).setVillager(this.value.equals((Object)Data.VILLAGER));
        } else if (this.value.equals((Object)Data.WITHER)) {
            ((Skeleton)spawned).setSkeletonType(Skeleton.SkeletonType.WITHER);
        } else if (this.value.equals((Object)Data.COLORABLE)) {
            String color = rawData.toUpperCase(Locale.ENGLISH);
            try {
                if (color.equals("RANDOM")) {
                    Random rand = new Random();
                    ((Colorable)spawned).setColor(DyeColor.values()[rand.nextInt(DyeColor.values().length)]);
                } else if (!color.isEmpty()) {
                    ((Colorable)spawned).setColor(DyeColor.valueOf((String)color));
                }
                this.matched = rawData;
            }
            catch (Exception e) {
                throw new Exception(I18n._("sheepMalformedColor", new Object[0]), e);
            }
        } else if (this.value.equals((Object)Data.EXP)) {
            try {
                ((ExperienceOrb)spawned).setExperience(Integer.parseInt(rawData));
                this.matched = rawData;
            }
            catch (NumberFormatException e) {
                throw new Exception(I18n._("invalidNumber", new Object[0]), e);
            }
        } else if (this.value.equals((Object)Data.SIZE)) {
            try {
                ((Slime)spawned).setSize(Integer.parseInt(rawData));
                this.matched = rawData;
            }
            catch (NumberFormatException e) {
                throw new Exception(I18n._("slimeMalformedSize", new Object[0]), e);
            }
        } else if (this.value instanceof Horse.Color) {
            ((Horse)spawned).setColor((Horse.Color)this.value);
        } else if (this.value instanceof Horse.Style) {
            ((Horse)spawned).setStyle((Horse.Style)this.value);
        } else if (this.value instanceof Horse.Variant) {
            ((Horse)spawned).setVariant((Horse.Variant)this.value);
        } else if (this.value instanceof Ocelot.Type) {
            ((Ocelot)spawned).setCatType((Ocelot.Type)this.value);
        } else if (this.value instanceof Villager.Profession) {
            ((Villager)spawned).setProfession((Villager.Profession)this.value);
        } else if (this.value instanceof Material) {
            if (this.type.equals((Object)EntityType.HORSE)) {
                ((Horse)spawned).setTamed(true);
                ((Horse)spawned).getInventory().setArmor(new ItemStack((Material)this.value, 1));
            } else if (this.type.equals(EntityType.ZOMBIE.getEntityClass()) || this.type.equals((Object)EntityType.SKELETON)) {
                EntityEquipment invent = ((LivingEntity)spawned).getEquipment();
                invent.setItemInHand(new ItemStack((Material)this.value, 1));
                invent.setItemInHandDropChance(0.1f);
            }
        }
    }

    static {
        logger = Logger.getLogger("Essentials");
    }

    public static enum Data {
        BABY,
        CHEST,
        BABYZOMBIE,
        VILLAGER,
        HORSESADDLE,
        PIGSADDLE,
        ELECTRIFIED,
        WITHER,
        ANGRY,
        TAMED,
        COLORABLE,
        EXP,
        SIZE;
        

        private Data() {
        }
    }

}

