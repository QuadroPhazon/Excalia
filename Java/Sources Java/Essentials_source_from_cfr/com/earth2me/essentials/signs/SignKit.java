/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class SignKit
extends EssentialsSign {
    public SignKit() {
        super("Kit");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        this.validateTrade(sign, 3, ess);
        String kitName = sign.getLine(1).toLowerCase(Locale.ENGLISH).trim();
        if (kitName.isEmpty()) {
            sign.setLine(1, "\u00a7dKit name!");
            return false;
        }
        try {
            ess.getSettings().getKit(kitName);
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
        String kitName = sign.getLine(1).toLowerCase(Locale.ENGLISH).trim();
        String group = sign.getLine(2).trim();
        if (!group.isEmpty() && ("\u00a72Everyone".equals(group) || player.inGroup(group)) || group.isEmpty() && player.isAuthorized("essentials.kits." + kitName)) {
            Trade charge = this.getTrade(sign, 3, ess);
            charge.isAffordableFor(player);
            try {
                Map<String, Object> kit = ess.getSettings().getKit(kitName);
                Kit.checkTime(player, kitName, kit);
                List<String> items = Kit.getItems(ess, player, kitName, kit);
                Kit.expandItems(ess, player, items);
                charge.charge(player);
                Trade.log("Sign", "Kit", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
            }
            catch (NoChargeException ex) {
                return false;
            }
            catch (Exception ex) {
                throw new SignException(ex.getMessage(), ex);
            }
            return true;
        }
        if (group.isEmpty()) {
            throw new SignException(I18n._("noKitPermission", "essentials.kits." + kitName));
        }
        throw new SignException(I18n._("noKitGroup", group));
    }
}

