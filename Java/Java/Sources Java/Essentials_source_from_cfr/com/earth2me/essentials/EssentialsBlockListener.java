/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.CreatureSpawner
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.material.MaterialData
 */
package com.earth2me.essentials;

import com.earth2me.essentials.Mob;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.LocationUtil;
import java.util.HashMap;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

public class EssentialsBlockListener
implements Listener {
    private final transient IEssentials ess;

    public EssentialsBlockListener(IEssentials ess) {
        this.ess = ess;
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onBlockPlace(BlockPlaceEvent event) {
        BlockState blockState;
        User user;
        final ItemStack is = LocationUtil.convertBlockToItem(event.getBlockPlaced());
        if (is == null) {
            return;
        }
        if (is.getType() == Material.MOB_SPAWNER && event.getItemInHand() != null && event.getPlayer() != null && event.getItemInHand().getType() == Material.MOB_SPAWNER && (blockState = event.getBlockPlaced().getState()) instanceof CreatureSpawner) {
            CreatureSpawner spawner = (CreatureSpawner)blockState;
            EntityType type = EntityType.fromId((int)event.getItemInHand().getData().getData());
            if (type != null && Mob.fromBukkitType(type) != null && this.ess.getUser(event.getPlayer()).isAuthorized("essentials.spawnerconvert." + Mob.fromBukkitType(type).name().toLowerCase(Locale.ENGLISH))) {
                spawner.setSpawnedType(type);
            }
        }
        if ((user = this.ess.getUser(event.getPlayer())).hasUnlimited(is) && user.getGameMode() == GameMode.SURVIVAL) {
            this.ess.scheduleSyncDelayedTask(new Runnable(){

                @Override
                public void run() {
                    user.getBase().getInventory().addItem(new ItemStack[]{is});
                    user.getBase().updateInventory();
                }
            });
        }
    }

}

