/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Sign
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.signs.EssentialsSign;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignPlayerListener
implements Listener {
    private final transient IEssentials ess;

    public SignPlayerListener(IEssentials ess) {
        this.ess = ess;
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block;
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        if (event.isCancelled() && event.getAction() == Action.RIGHT_CLICK_AIR) {
            Block targetBlock;
            block11 : {
                targetBlock = null;
                try {
                    targetBlock = event.getPlayer().getTargetBlock(null, 5);
                }
                catch (IllegalStateException ex) {
                    if (!this.ess.getSettings().isDebug()) break block11;
                    this.ess.getLogger().log(Level.WARNING, ex.getMessage(), ex);
                }
            }
            block = targetBlock;
        } else {
            block = event.getClickedBlock();
        }
        if (block == null) {
            return;
        }
        Material mat = block.getType();
        if (mat == Material.SIGN_POST || mat == Material.WALL_SIGN) {
            String csign = ((Sign)block.getState()).getLine(0);
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                if (!csign.equalsIgnoreCase(sign.getSuccessName())) continue;
                sign.onSignInteract(block, event.getPlayer(), this.ess);
                event.setCancelled(true);
                return;
            }
        } else {
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockInteract(block, event.getPlayer(), this.ess)) continue;
                event.setCancelled(true);
                return;
            }
        }
    }
}

