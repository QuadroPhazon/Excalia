/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import java.util.Locale;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SignGameMode
extends EssentialsSign {
    public SignGameMode() {
        super("GameMode");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        String gamemode = sign.getLine(1);
        if (gamemode.isEmpty()) {
            sign.setLine(1, "Survival");
        }
        this.validateTrade(sign, 2, ess);
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        Trade charge = this.getTrade(sign, 2, ess);
        String mode = sign.getLine(1).trim();
        if (mode.isEmpty()) {
            throw new SignException(I18n._("invalidSignLine", 2));
        }
        charge.isAffordableFor(player);
        this.performSetMode(mode.toLowerCase(Locale.ENGLISH), player.getBase());
        player.sendMessage(I18n._("gameMode", I18n._(player.getGameMode().toString().toLowerCase(Locale.ENGLISH), new Object[0]), player.getDisplayName()));
        Trade.log("Sign", "gameMode", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
        charge.charge(player);
        return true;
    }

    private void performSetMode(String mode, Player player) throws SignException {
        if (mode.contains("survi") || mode.equalsIgnoreCase("0")) {
            player.setGameMode(GameMode.SURVIVAL);
        } else if (mode.contains("creat") || mode.equalsIgnoreCase("1")) {
            player.setGameMode(GameMode.CREATIVE);
        } else if (mode.contains("advent") || mode.equalsIgnoreCase("2")) {
            player.setGameMode(GameMode.ADVENTURE);
        } else {
            throw new SignException(I18n._("invalidSignLine", 2));
        }
    }
}

