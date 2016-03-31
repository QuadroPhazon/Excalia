/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.I18n;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import net.ess3.api.IUser;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LocationUtil {
    public static final Set<Integer> HOLLOW_MATERIALS = new HashSet<Integer>();
    private static final HashSet<Byte> TRANSPARENT_MATERIALS = new HashSet();
    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;

    public static ItemStack convertBlockToItem(Block block) {
        ItemStack is = new ItemStack(block.getType(), 1, 0, Byte.valueOf(block.getData()));
        switch (is.getType()) {
            case WOODEN_DOOR: {
                is.setType(Material.WOOD_DOOR);
                is.setDurability(0);
                break;
            }
            case IRON_DOOR_BLOCK: {
                is.setType(Material.IRON_DOOR);
                is.setDurability(0);
                break;
            }
            case SIGN_POST: 
            case WALL_SIGN: {
                is.setType(Material.SIGN);
                is.setDurability(0);
                break;
            }
            case CROPS: {
                is.setType(Material.SEEDS);
                is.setDurability(0);
                break;
            }
            case CAKE_BLOCK: {
                is.setType(Material.CAKE);
                is.setDurability(0);
                break;
            }
            case BED_BLOCK: {
                is.setType(Material.BED);
                is.setDurability(0);
                break;
            }
            case REDSTONE_WIRE: {
                is.setType(Material.REDSTONE);
                is.setDurability(0);
                break;
            }
            case REDSTONE_TORCH_OFF: 
            case REDSTONE_TORCH_ON: {
                is.setType(Material.REDSTONE_TORCH_ON);
                is.setDurability(0);
                break;
            }
            case DIODE_BLOCK_OFF: 
            case DIODE_BLOCK_ON: {
                is.setType(Material.DIODE);
                is.setDurability(0);
                break;
            }
            case DOUBLE_STEP: {
                is.setType(Material.STEP);
                break;
            }
            case TORCH: 
            case RAILS: 
            case LADDER: 
            case WOOD_STAIRS: 
            case COBBLESTONE_STAIRS: 
            case LEVER: 
            case STONE_BUTTON: 
            case FURNACE: 
            case DISPENSER: 
            case PUMPKIN: 
            case JACK_O_LANTERN: 
            case WOOD_PLATE: 
            case STONE_PLATE: 
            case PISTON_STICKY_BASE: 
            case PISTON_BASE: 
            case IRON_FENCE: 
            case THIN_GLASS: 
            case TRAP_DOOR: 
            case FENCE: 
            case FENCE_GATE: 
            case NETHER_FENCE: {
                is.setDurability(0);
                break;
            }
            case FIRE: {
                return null;
            }
            case PUMPKIN_STEM: {
                is.setType(Material.PUMPKIN_SEEDS);
                break;
            }
            case MELON_STEM: {
                is.setType(Material.MELON_SEEDS);
            }
        }
        return is;
    }

    public static Location getTarget(LivingEntity entity) throws Exception {
        Block block = entity.getTargetBlock(TRANSPARENT_MATERIALS, 300);
        if (block == null) {
            throw new Exception("Not targeting a block");
        }
        return block.getLocation();
    }

    static boolean isBlockAboveAir(World world, int x, int y, int z) {
        if (y > world.getMaxHeight()) {
            return true;
        }
        return HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType().getId());
    }

    public static boolean isBlockUnsafe(World world, int x, int y, int z) {
        if (LocationUtil.isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return LocationUtil.isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockDamaging(World world, int x, int y, int z) {
        Block below = world.getBlockAt(x, y - 1, z);
        if (below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA) {
            return true;
        }
        if (below.getType() == Material.FIRE) {
            return true;
        }
        if (below.getType() == Material.BED_BLOCK) {
            return true;
        }
        if (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType().getId()) || !HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType().getId())) {
            return true;
        }
        return false;
    }

    public static Location getRoundedDestination(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int)Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, (double)x + 0.5, (double)y, (double)z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static Location getSafeDestination(IUser user, Location loc) throws Exception {
        if (loc.getWorld().equals((Object)user.getBase().getWorld()) && (user.getBase().getGameMode() == GameMode.CREATIVE || user.isGodModeEnabled() && user.getBase().getAllowFlight())) {
            if (LocationUtil.shouldFly(loc)) {
                user.getBase().setFlying(true);
            }
            return LocationUtil.getRoundedDestination(loc);
        }
        return LocationUtil.getSafeDestination(loc);
    }

    public static Location getSafeDestination(Location loc) throws Exception {
        if (loc == null || loc.getWorld() == null) {
            throw new Exception(I18n._("destinationNotSet", new Object[0]));
        }
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int)Math.round(loc.getY());
        int z = loc.getBlockZ();
        int origX = x;
        int origY = y;
        int origZ = z;
        while (LocationUtil.isBlockAboveAir(world, x, y, z)) {
            if (--y >= 0) continue;
            y = origY;
            break;
        }
        if (LocationUtil.isBlockUnsafe(world, x, y, z)) {
            x = Math.round(loc.getX()) == (long)origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == (long)origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (LocationUtil.isBlockUnsafe(world, x, y, z)) {
            if (++i >= VOLUME.length) {
                x = origX;
                y = origY + 3;
                z = origZ;
                break;
            }
            x = origX + LocationUtil.VOLUME[i].x;
            y = origY + LocationUtil.VOLUME[i].y;
            z = origZ + LocationUtil.VOLUME[i].z;
        }
        while (LocationUtil.isBlockUnsafe(world, x, y, z)) {
            if (++y < world.getMaxHeight()) continue;
            ++x;
            break;
        }
        while (LocationUtil.isBlockUnsafe(world, x, y, z)) {
            if (--y > 1) continue;
            y = world.getHighestBlockYAt(++x, z);
            if (x - 48 <= loc.getBlockX()) continue;
            throw new Exception(I18n._("holeInFloor", new Object[0]));
        }
        return new Location(world, (double)x + 0.5, (double)y, (double)z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static boolean shouldFly(Location loc) {
        int y;
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int count = 0;
        for (y = (int)Math.round((double)loc.getY()); LocationUtil.isBlockUnsafe(world, x, y, z) && y > -1; --y) {
            if (++count <= 2) continue;
            return true;
        }
        return y < 0;
    }

    static {
        HOLLOW_MATERIALS.add(Material.AIR.getId());
        HOLLOW_MATERIALS.add(Material.SAPLING.getId());
        HOLLOW_MATERIALS.add(Material.POWERED_RAIL.getId());
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL.getId());
        HOLLOW_MATERIALS.add(Material.LONG_GRASS.getId());
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH.getId());
        HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER.getId());
        HOLLOW_MATERIALS.add(Material.RED_ROSE.getId());
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM.getId());
        HOLLOW_MATERIALS.add(Material.TORCH.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE.getId());
        HOLLOW_MATERIALS.add(Material.SEEDS.getId());
        HOLLOW_MATERIALS.add(Material.SIGN_POST.getId());
        HOLLOW_MATERIALS.add(Material.WOODEN_DOOR.getId());
        HOLLOW_MATERIALS.add(Material.LADDER.getId());
        HOLLOW_MATERIALS.add(Material.RAILS.getId());
        HOLLOW_MATERIALS.add(Material.WALL_SIGN.getId());
        HOLLOW_MATERIALS.add(Material.LEVER.getId());
        HOLLOW_MATERIALS.add(Material.STONE_PLATE.getId());
        HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
        HOLLOW_MATERIALS.add(Material.WOOD_PLATE.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
        HOLLOW_MATERIALS.add(Material.STONE_BUTTON.getId());
        HOLLOW_MATERIALS.add(Material.SNOW.getId());
        HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM.getId());
        HOLLOW_MATERIALS.add(Material.MELON_STEM.getId());
        HOLLOW_MATERIALS.add(Material.VINE.getId());
        HOLLOW_MATERIALS.add(Material.FENCE_GATE.getId());
        HOLLOW_MATERIALS.add(Material.WATER_LILY.getId());
        HOLLOW_MATERIALS.add(Material.NETHER_WARTS.getId());
        try {
            HOLLOW_MATERIALS.add(Material.CARPET.getId());
        }
        catch (NoSuchFieldError e) {
            Essentials.wrongVersion();
        }
        for (Integer integer : HOLLOW_MATERIALS) {
            TRANSPARENT_MATERIALS.add(Byte.valueOf(integer.byteValue()));
        }
        TRANSPARENT_MATERIALS.add(Byte.valueOf((byte)Material.WATER.getId()));
        TRANSPARENT_MATERIALS.add(Byte.valueOf((byte)Material.STATIONARY_WATER.getId()));
        ArrayList<Vector3D> pos = new ArrayList<Vector3D>();
        for (int x = -3; x <= 3; ++x) {
            for (int y = -3; y <= 3; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        Collections.sort(pos, new Comparator<Vector3D>(){

            @Override
            public int compare(Vector3D a, Vector3D b) {
                return a.x * a.x + a.y * a.y + a.z * a.z - (b.x * b.x + b.y * b.y + b.z * b.z);
            }
        });
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    public static class Vector3D {
        public int x;
        public int y;
        public int z;

        public Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

}

