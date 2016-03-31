/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.signs.EssentialsSign;
import java.util.List;
import java.util.Set;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class SignEntityListener
implements Listener {
    private final transient IEssentials ess;

    public SignEntityListener(IEssentials ess) {
        this.ess = ess;
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        for (Block block : event.blockList()) {
            if ((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
                event.setCancelled(true);
                return;
            }
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType())) continue;
                event.setCancelled(!sign.onBlockExplode(block, this.ess));
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        Block block = event.getBlock();
        if ((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
            event.setCancelled(true);
            return;
        }
        for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
            if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockBreak(block, this.ess)) continue;
            event.setCancelled(true);
            return;
        }
    }
}

