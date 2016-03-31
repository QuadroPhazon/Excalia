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
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockBurnEvent
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockPistonExtendEvent
 *  org.bukkit.event.block.BlockPistonRetractEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.block.SignChangeEvent
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.Signs;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignBlockListener
implements Listener {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private static final Material WALL_SIGN = Material.WALL_SIGN;
    private static final Material SIGN_POST = Material.SIGN_POST;
    private final transient IEssentials ess;

    public SignBlockListener(IEssentials ess) {
        this.ess = ess;
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=1)
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        try {
            if (this.protectSignsAndBlocks(event.getBlock(), event.getPlayer())) {
                event.setCancelled(true);
            }
        }
        catch (MaxMoneyException ex) {
            event.setCancelled(true);
        }
    }

    public boolean protectSignsAndBlocks(Block block, Player player) throws MaxMoneyException {
        if (EssentialsSign.checkIfBlockBreaksSigns(block)) {
            LOGGER.log(Level.INFO, "Prevented that a block was broken next to a sign.");
            return true;
        }
        Material mat = block.getType();
        if (mat == SIGN_POST || mat == WALL_SIGN) {
            Sign csign = (Sign)block.getState();
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                if (!csign.getLine(0).equalsIgnoreCase(sign.getSuccessName()) || sign.onSignBreak(block, player, this.ess)) continue;
                return true;
            }
        }
        for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
            if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockBreak(block, player, this.ess)) continue;
            LOGGER.log(Level.INFO, "A block was protected by a sign.");
            return true;
        }
        return false;
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=1)
    public void onSignChange2(SignChangeEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        User user = this.ess.getUser(event.getPlayer());
        for (int i = 0; i < 4; ++i) {
            event.setLine(i, FormatUtil.formatString(user, "essentials.signs", event.getLine(i)));
        }
        String topLine = event.getLine(0);
        for (Signs signs : Signs.values()) {
            EssentialsSign sign = signs.getSign();
            if (!topLine.equalsIgnoreCase(sign.getSuccessName())) continue;
            event.setLine(0, FormatUtil.stripFormat(topLine));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=1)
    public void onSignChange(SignChangeEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
            if (event.getLine(0).equalsIgnoreCase(sign.getSuccessName())) {
                event.setCancelled(true);
                return;
            }
            if (!event.getLine(0).equalsIgnoreCase(sign.getTemplateName()) || sign.onSignCreate(event, this.ess)) continue;
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        Block against = event.getBlockAgainst();
        if ((against.getType() == WALL_SIGN || against.getType() == SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(against))) {
            event.setCancelled(true);
            return;
        }
        Block block = event.getBlock();
        if (block.getType() == WALL_SIGN || block.getType() == SIGN_POST) {
            return;
        }
        for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
            if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockPlace(block, event.getPlayer(), this.ess)) continue;
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onBlockBurn(BlockBurnEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        Block block = event.getBlock();
        if ((block.getType() == WALL_SIGN || block.getType() == SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
            event.setCancelled(true);
            return;
        }
        for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
            if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockBurn(block, this.ess)) continue;
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        Block block = event.getBlock();
        if ((block.getType() == WALL_SIGN || block.getType() == SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
            event.setCancelled(true);
            return;
        }
        for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
            if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockIgnite(block, this.ess)) continue;
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        for (Block block : event.getBlocks()) {
            if ((block.getType() == WALL_SIGN || block.getType() == SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
                event.setCancelled(true);
                return;
            }
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockPush(block, this.ess)) continue;
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (this.ess.getSettings().areSignsDisabled()) {
            event.getHandlers().unregister((Listener)this);
            return;
        }
        if (event.isSticky()) {
            Block block = event.getBlock();
            if ((block.getType() == WALL_SIGN || block.getType() == SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)) || EssentialsSign.checkIfBlockBreaksSigns(block)) {
                event.setCancelled(true);
                return;
            }
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                if (!sign.areHeavyEventRequired() || !sign.getBlocks().contains((Object)block.getType()) || sign.onBlockPush(block, this.ess)) continue;
                event.setCancelled(true);
                return;
            }
        }
    }
}

