/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.TextInput;
import com.earth2me.essentials.textreader.TextPager;
import java.io.IOException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class SignInfo
extends EssentialsSign {
    public SignInfo() {
        super("Info");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        this.validateTrade(sign, 3, ess);
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        Trade charge = this.getTrade(sign, 3, ess);
        charge.isAffordableFor(player);
        String chapter = sign.getLine(1);
        String page = sign.getLine(2);
        try {
            TextInput input = new TextInput(player.getSource(), "info", true, ess);
            KeywordReplacer output = new KeywordReplacer(input, player.getSource(), ess);
            TextPager pager = new TextPager(output);
            pager.showPage(chapter, page, null, player.getSource());
        }
        catch (IOException ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        charge.charge(player);
        Trade.log("Sign", "Info", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
        return true;
    }
}

