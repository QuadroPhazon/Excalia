/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Sign
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class SignProtection
extends EssentialsSign {
    private final transient Set<Material> protectedBlocks = EnumSet.noneOf(Material.class);

    public SignProtection() {
        super("Protection");
        this.protectedBlocks.add(Material.CHEST);
        this.protectedBlocks.add(Material.BURNING_FURNACE);
        this.protectedBlocks.add(Material.FURNACE);
        this.protectedBlocks.add(Material.DISPENSER);
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        SignProtectionState state;
        sign.setLine(3, "\u00a74" + username);
        if (this.hasAdjacentBlock(sign.getBlock(), new Block[0]) && ((state = this.isBlockProtected(sign.getBlock(), player, username, true)) == SignProtectionState.NOSIGN || state == SignProtectionState.OWNER || player.isAuthorized("essentials.signs.protection.override"))) {
            sign.setLine(3, "\u00a71" + username);
            return true;
        }
        player.sendMessage(I18n._("signProtectInvalidLocation", new Object[0]));
        return false;
    }

    @Override
    protected boolean onSignBreak(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        SignProtectionState state = this.checkProtectionSign(sign, player, username);
        return state == SignProtectionState.OWNER;
    }

    public /* varargs */ boolean hasAdjacentBlock(Block block, Block ... ignoredBlocks) {
        Block[] faces;
        for (Block b : faces = this.getAdjacentBlocks(block)) {
            for (Block ignoredBlock : ignoredBlocks) {
                if (!b.getLocation().equals((Object)ignoredBlock.getLocation())) continue;
            }
            if (!this.protectedBlocks.contains((Object)b.getType())) continue;
            return true;
        }
        return false;
    }

    private void checkIfSignsAreBroken(Block block, User player, String username, IEssentials ess) throws MaxMoneyException {
        Map<Location, SignProtectionState> signs = this.getConnectedSigns(block, player, username, false);
        for (Map.Entry<Location, SignProtectionState> entry : signs.entrySet()) {
            Block sign;
            if (entry.getValue() == SignProtectionState.NOSIGN || this.hasAdjacentBlock(sign = entry.getKey().getBlock(), block)) continue;
            block.setType(Material.AIR);
            Trade trade = new Trade(new ItemStack(Material.SIGN, 1), ess);
            trade.pay(player, Trade.OverflowType.DROP);
        }
    }

    private Map<Location, SignProtectionState> getConnectedSigns(Block block, User user, String username, boolean secure) {
        HashMap<Location, SignProtectionState> signs = new HashMap<Location, SignProtectionState>();
        this.getConnectedSigns(block, signs, user, username, secure ? 4 : 2);
        return signs;
    }

    private void getConnectedSigns(Block block, Map<Location, SignProtectionState> signs, User user, String username, int depth) {
        Block[] faces;
        for (Block b : faces = this.getAdjacentBlocks(block)) {
            Location loc = b.getLocation();
            if (signs.containsKey((Object)loc)) continue;
            SignProtectionState check = this.checkProtectionSign(b, user, username);
            signs.put(loc, check);
            if (!this.protectedBlocks.contains((Object)b.getType()) || depth <= 0) continue;
            this.getConnectedSigns(b, signs, user, username, depth - 1);
        }
    }

    private SignProtectionState checkProtectionSign(Block block, User user, String username) {
        EssentialsSign.BlockSign sign;
        if ((block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) && (sign = new EssentialsSign.BlockSign(block)).getLine(0).equals(this.getSuccessName())) {
            return this.checkProtectionSign(sign, user, username);
        }
        return SignProtectionState.NOSIGN;
    }

    private SignProtectionState checkProtectionSign(EssentialsSign.ISign sign, User user, String username) {
        if (user == null || username == null) {
            return SignProtectionState.NOT_ALLOWED;
        }
        if (user.isAuthorized("essentials.signs.protection.override")) {
            return SignProtectionState.OWNER;
        }
        if (FormatUtil.stripFormat(sign.getLine(3)).equalsIgnoreCase(username)) {
            return SignProtectionState.OWNER;
        }
        for (int i = 1; i <= 2; ++i) {
            String line = sign.getLine(i);
            if (line.startsWith("(") && line.endsWith(")") && user.inGroup(line.substring(1, line.length() - 1))) {
                return SignProtectionState.ALLOWED;
            }
            if (!line.equalsIgnoreCase(username)) continue;
            return SignProtectionState.ALLOWED;
        }
        return SignProtectionState.NOT_ALLOWED;
    }

    private Block[] getAdjacentBlocks(Block block) {
        return new Block[]{block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.EAST), block.getRelative(BlockFace.WEST), block.getRelative(BlockFace.DOWN), block.getRelative(BlockFace.UP)};
    }

    public SignProtectionState isBlockProtected(Block block, User user, String username, boolean secure) {
        Map<Location, SignProtectionState> signs = this.getConnectedSigns(block, user, username, secure);
        SignProtectionState retstate = SignProtectionState.NOSIGN;
        for (SignProtectionState state2 : signs.values()) {
            if (state2 == SignProtectionState.ALLOWED) {
                retstate = state2;
                continue;
            }
            if (state2 != SignProtectionState.NOT_ALLOWED || retstate == SignProtectionState.ALLOWED) continue;
            retstate = state2;
        }
        if (!secure || retstate == SignProtectionState.NOSIGN) {
            for (SignProtectionState state2 : signs.values()) {
                if (state2 != SignProtectionState.OWNER) continue;
                return state2;
            }
        }
        return retstate;
    }

    public boolean isBlockProtected(Block block) {
        Block[] faces;
        for (Block b : faces = this.getAdjacentBlocks(block)) {
            Block[] faceChest;
            Sign sign;
            if ((b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) && (sign = (Sign)b.getState()).getLine(0).equalsIgnoreCase("\u00a71[Protection]")) {
                return true;
            }
            if (!this.protectedBlocks.contains((Object)b.getType())) continue;
            for (Block a : faceChest = this.getAdjacentBlocks(b)) {
                Sign sign2;
                if (a.getType() != Material.SIGN_POST && a.getType() != Material.WALL_SIGN || !(sign2 = (Sign)a.getState()).getLine(0).equalsIgnoreCase("\u00a71[Protection]")) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Material> getBlocks() {
        return this.protectedBlocks;
    }

    @Override
    public boolean areHeavyEventRequired() {
        return true;
    }

    @Override
    protected boolean onBlockPlace(Block block, User player, String username, IEssentials ess) throws SignException {
        for (Block adjBlock : this.getAdjacentBlocks(block)) {
            SignProtectionState state = this.isBlockProtected(adjBlock, player, username, true);
            if (state != SignProtectionState.ALLOWED && state != SignProtectionState.NOT_ALLOWED || player.isAuthorized("essentials.signs.protection.override")) continue;
            player.sendMessage(I18n._("noPlacePermission", block.getType().toString().toLowerCase(Locale.ENGLISH)));
            return false;
        }
        return true;
    }

    @Override
    protected boolean onBlockInteract(Block block, User player, String username, IEssentials ess) throws SignException {
        SignProtectionState state = this.isBlockProtected(block, player, username, false);
        if (state == SignProtectionState.OWNER || state == SignProtectionState.NOSIGN || state == SignProtectionState.ALLOWED) {
            return true;
        }
        if (state == SignProtectionState.NOT_ALLOWED && player.isAuthorized("essentials.signs.protection.override")) {
            return true;
        }
        player.sendMessage(I18n._("noAccessPermission", block.getType().toString().toLowerCase(Locale.ENGLISH)));
        return false;
    }

    @Override
    protected boolean onBlockBreak(Block block, User player, String username, IEssentials ess) throws SignException, MaxMoneyException {
        SignProtectionState state = this.isBlockProtected(block, player, username, false);
        if (state == SignProtectionState.OWNER || state == SignProtectionState.NOSIGN) {
            this.checkIfSignsAreBroken(block, player, username, ess);
            return true;
        }
        if ((state == SignProtectionState.ALLOWED || state == SignProtectionState.NOT_ALLOWED) && player.isAuthorized("essentials.signs.protection.override")) {
            this.checkIfSignsAreBroken(block, player, username, ess);
            return true;
        }
        player.sendMessage(I18n._("noDestroyPermission", block.getType().toString().toLowerCase(Locale.ENGLISH)));
        return false;
    }

    @Override
    public boolean onBlockBreak(Block block, IEssentials ess) {
        SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }

    @Override
    public boolean onBlockExplode(Block block, IEssentials ess) {
        SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }

    @Override
    public boolean onBlockBurn(Block block, IEssentials ess) {
        SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }

    @Override
    public boolean onBlockIgnite(Block block, IEssentials ess) {
        SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }

    @Override
    public boolean onBlockPush(Block block, IEssentials ess) {
        SignProtectionState state = this.isBlockProtected(block, null, null, false);
        return state == SignProtectionState.NOSIGN;
    }

    public static enum SignProtectionState {
        NOT_ALLOWED,
        ALLOWED,
        NOSIGN,
        OWNER;
        

        private SignProtectionState() {
        }
    }

}

