/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public enum Mob {
    CHICKEN("Chicken", Enemies.FRIENDLY, EntityType.CHICKEN),
    COW("Cow", Enemies.FRIENDLY, EntityType.COW),
    CREEPER("Creeper", Enemies.ENEMY, EntityType.CREEPER),
    GHAST("Ghast", Enemies.ENEMY, EntityType.GHAST),
    GIANT("Giant", Enemies.ENEMY, EntityType.GIANT),
    HORSE("Horse", Enemies.FRIENDLY, EntityType.HORSE),
    PIG("Pig", Enemies.FRIENDLY, EntityType.PIG),
    PIGZOMB("PigZombie", Enemies.NEUTRAL, EntityType.PIG_ZOMBIE),
    SHEEP("Sheep", Enemies.FRIENDLY, "", EntityType.SHEEP),
    SKELETON("Skeleton", Enemies.ENEMY, EntityType.SKELETON),
    SLIME("Slime", Enemies.ENEMY, EntityType.SLIME),
    SPIDER("Spider", Enemies.ENEMY, EntityType.SPIDER),
    SQUID("Squid", Enemies.FRIENDLY, EntityType.SQUID),
    ZOMBIE("Zombie", Enemies.ENEMY, EntityType.ZOMBIE),
    WOLF("Wolf", Enemies.NEUTRAL, "", EntityType.WOLF),
    CAVESPIDER("CaveSpider", Enemies.ENEMY, EntityType.CAVE_SPIDER),
    ENDERMAN("Enderman", Enemies.ENEMY, "", EntityType.ENDERMAN),
    SILVERFISH("Silverfish", Enemies.ENEMY, "", EntityType.SILVERFISH),
    ENDERDRAGON("EnderDragon", Enemies.ENEMY, EntityType.ENDER_DRAGON),
    VILLAGER("Villager", Enemies.FRIENDLY, EntityType.VILLAGER),
    BLAZE("Blaze", Enemies.ENEMY, EntityType.BLAZE),
    MUSHROOMCOW("MushroomCow", Enemies.FRIENDLY, EntityType.MUSHROOM_COW),
    MAGMACUBE("MagmaCube", Enemies.ENEMY, EntityType.MAGMA_CUBE),
    SNOWMAN("Snowman", Enemies.FRIENDLY, "", EntityType.SNOWMAN),
    OCELOT("Ocelot", Enemies.NEUTRAL, EntityType.OCELOT),
    IRONGOLEM("IronGolem", Enemies.NEUTRAL, EntityType.IRON_GOLEM),
    WITHER("Wither", Enemies.ENEMY, EntityType.WITHER),
    BAT("Bat", Enemies.FRIENDLY, EntityType.BAT),
    WITCH("Witch", Enemies.ENEMY, EntityType.WITCH),
    BOAT("Boat", Enemies.NEUTRAL, EntityType.BOAT),
    MINECART("Minecart", Enemies.NEUTRAL, EntityType.MINECART),
    MINECART_CHEST("ChestMinecart", Enemies.NEUTRAL, EntityType.MINECART_CHEST),
    MINECART_FURNACE("FurnaceMinecart", Enemies.NEUTRAL, EntityType.MINECART_FURNACE),
    MINECART_TNT("TNTMinecart", Enemies.NEUTRAL, EntityType.MINECART_TNT),
    MINECART_HOPPER("HopperMinecart", Enemies.NEUTRAL, EntityType.MINECART_HOPPER),
    MINECART_MOB_SPAWNER("SpawnerMinecart", Enemies.NEUTRAL, EntityType.MINECART_MOB_SPAWNER),
    ENDERCRYSTAL("EnderCrystal", Enemies.NEUTRAL, EntityType.ENDER_CRYSTAL),
    EXPERIENCEORB("ExperienceOrb", Enemies.NEUTRAL, EntityType.EXPERIENCE_ORB);
    
    public static final Logger logger;
    public String suffix = "s";
    public final String name;
    public final Enemies type;
    private final EntityType bukkitType;
    private static final Map<String, Mob> hashMap;
    private static final Map<EntityType, Mob> bukkitMap;

    private Mob(String n2, Enemies en, String s, EntityType type) {
        this.suffix = s;
        this.name = n2;
        this.type = en;
        this.bukkitType = type;
    }

    private Mob(String n2, Enemies en, EntityType type) {
        this.name = n2;
        this.type = en;
        this.bukkitType = type;
    }

    public static Set<String> getMobList() {
        return Collections.unmodifiableSet(hashMap.keySet());
    }

    public Entity spawn(World world, Server server, Location loc) throws MobException {
        Entity entity = world.spawn(loc, this.bukkitType.getEntityClass());
        if (entity == null) {
            logger.log(Level.WARNING, I18n._("unableToSpawnMob", new Object[0]));
            throw new MobException();
        }
        return entity;
    }

    public EntityType getType() {
        return this.bukkitType;
    }

    public static Mob fromName(String name) {
        return hashMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    public static Mob fromBukkitType(EntityType type) {
        return bukkitMap.get((Object)type);
    }

    static {
        logger = Logger.getLogger("Essentials");
        hashMap = new HashMap<String, Mob>();
        bukkitMap = new HashMap<EntityType, Mob>();
        for (Mob mob : Mob.values()) {
            hashMap.put(mob.name.toLowerCase(Locale.ENGLISH), mob);
            bukkitMap.put(mob.bukkitType, mob);
        }
    }

    public static class MobException
    extends Exception {
        private static final long serialVersionUID = 1;
    }

    public static enum Enemies {
        FRIENDLY("friendly"),
        NEUTRAL("neutral"),
        ENEMY("enemy");
        
        protected final String type;

        private Enemies(String type) {
            this.type = type;
        }
    }

}

