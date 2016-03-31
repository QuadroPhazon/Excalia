/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class SignSell
extends EssentialsSign {
    public SignSell() {
        super("Sell");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        this.validateTrade(sign, 1, 2, player, ess);
        this.validateTrade(sign, 3, ess);
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException, MaxMoneyException {
        Trade charge = this.getTrade(sign, 1, 2, player, ess);
        Trade money = this.getTrade(sign, 3, ess);
        charge.isAffordableFor(player);
        money.pay(player, Trade.OverflowType.DROP);
        charge.charge(player);
        Trade.log("Sign", "Sell", "Interact", username, charge, username, money, sign.getBlock().getLocation(), ess);
        return true;
    }
}

