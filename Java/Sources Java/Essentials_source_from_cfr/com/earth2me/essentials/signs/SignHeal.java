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
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class SignHeal
extends EssentialsSign {
    public SignHeal() {
        super("Heal");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        this.validateTrade(sign, 1, ess);
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        if (player.getHealth() == 0.0) {
            throw new SignException(I18n._("healDead", new Object[0]));
        }
        Trade charge = this.getTrade(sign, 1, ess);
        charge.isAffordableFor(player);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.sendMessage(I18n._("youAreHealed", new Object[0]));
        charge.charge(player);
        Trade.log("Sign", "Heal", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
        return true;
    }
}

