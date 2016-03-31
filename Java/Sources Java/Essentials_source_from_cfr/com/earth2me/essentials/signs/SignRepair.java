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
import com.earth2me.essentials.commands.Commandrepair;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class SignRepair
extends EssentialsSign {
    public SignRepair() {
        super("Repair");
    }

    @Override
    protected boolean onSignCreate(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        String repairTarget = sign.getLine(1);
        if (repairTarget.isEmpty()) {
            sign.setLine(1, "Hand");
        } else if (!repairTarget.equalsIgnoreCase("all") && !repairTarget.equalsIgnoreCase("hand")) {
            sign.setLine(1, "\u00a7c<hand|all>");
            throw new SignException(I18n._("invalidSignLine", 2));
        }
        this.validateTrade(sign, 2, ess);
        return true;
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        Trade charge;
        block4 : {
            charge = this.getTrade(sign, 2, ess);
            charge.isAffordableFor(player);
            Commandrepair command = new Commandrepair();
            command.setEssentials(ess);
            try {
                if (sign.getLine(1).equalsIgnoreCase("hand")) {
                    command.repairHand(player);
                    break block4;
                }
                if (sign.getLine(1).equalsIgnoreCase("all")) {
                    command.repairAll(player);
                    break block4;
                }
                throw new NotEnoughArgumentsException();
            }
            catch (Exception ex) {
                throw new SignException(ex.getMessage(), ex);
            }
        }
        charge.charge(player);
        Trade.log("Sign", "Repair", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
        return true;
    }
}

