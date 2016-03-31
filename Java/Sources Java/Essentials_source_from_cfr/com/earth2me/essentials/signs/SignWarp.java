/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SignWarp
extends EssentialsSign {
    public SignWarp() {
        super("Warp");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        this.validateTrade(sign, 3, ess);
        String warpName = sign.getLine(1);
        if (warpName.isEmpty()) {
            sign.setLine(1, "\u00a7c<Warp name>");
            throw new SignException(I18n._("invalidSignLine", 1));
        }
        try {
            ess.getWarps().getWarp(warpName);
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        String group = sign.getLine(2);
        if ("Everyone".equalsIgnoreCase(group) || "Everybody".equalsIgnoreCase(group)) {
            sign.setLine(2, "\u00a72Everyone");
        }
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        String warpName = sign.getLine(1);
        String group = sign.getLine(2);
        if (!group.isEmpty() && ("\u00a72Everyone".equals(group) || player.inGroup(group)) || group.isEmpty() && (!ess.getSettings().getPerWarpPermission() || player.isAuthorized("essentials.warps." + warpName))) {
            Trade charge = this.getTrade(sign, 3, ess);
            try {
                player.getTeleport().warp(player, warpName, charge, PlayerTeleportEvent.TeleportCause.PLUGIN);
                Trade.log("Sign", "Warp", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
            }
            catch (Exception ex) {
                throw new SignException(ex.getMessage(), ex);
            }
            return true;
        }
        return false;
    }
}

