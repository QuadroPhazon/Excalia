/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import net.ess3.api.IEssentials;

public class SignBalance
extends EssentialsSign {
    public SignBalance() {
        super("Balance");
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        player.sendMessage(I18n._("balance", NumberUtil.displayCurrency(player.getMoney(), ess)));
        return true;
    }
}

