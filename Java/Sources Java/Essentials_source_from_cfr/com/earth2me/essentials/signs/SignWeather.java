/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
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
import org.bukkit.World;
import org.bukkit.block.Block;

public class SignWeather
extends EssentialsSign {
    public SignWeather() {
        super("Weather");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        this.validateTrade(sign, 2, ess);
        String timeString = sign.getLine(1);
        if ("Sun".equalsIgnoreCase(timeString)) {
            sign.setLine(1, "\u00a72Sun");
            return true;
        }
        if ("Storm".equalsIgnoreCase(timeString)) {
            sign.setLine(1, "\u00a72Storm");
            return true;
        }
        sign.setLine(1, "\u00a7c<sun|storm>");
        throw new SignException(I18n._("onlySunStorm", new Object[0]));
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        Trade charge = this.getTrade(sign, 2, ess);
        charge.isAffordableFor(player);
        String weatherString = sign.getLine(1);
        if ("\u00a72Sun".equalsIgnoreCase(weatherString)) {
            player.getWorld().setStorm(false);
            charge.charge(player);
            Trade.log("Sign", "WeatherSun", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
            return true;
        }
        if ("\u00a72Storm".equalsIgnoreCase(weatherString)) {
            player.getWorld().setStorm(true);
            charge.charge(player);
            Trade.log("Sign", "WeatherStorm", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
            return true;
        }
        throw new SignException(I18n._("onlySunStorm", new Object[0]));
    }
}

