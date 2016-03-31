/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignBalance;
import com.earth2me.essentials.signs.SignBuy;
import com.earth2me.essentials.signs.SignDisposal;
import com.earth2me.essentials.signs.SignEnchant;
import com.earth2me.essentials.signs.SignFree;
import com.earth2me.essentials.signs.SignGameMode;
import com.earth2me.essentials.signs.SignHeal;
import com.earth2me.essentials.signs.SignInfo;
import com.earth2me.essentials.signs.SignKit;
import com.earth2me.essentials.signs.SignMail;
import com.earth2me.essentials.signs.SignProtection;
import com.earth2me.essentials.signs.SignRepair;
import com.earth2me.essentials.signs.SignSell;
import com.earth2me.essentials.signs.SignSpawnmob;
import com.earth2me.essentials.signs.SignTime;
import com.earth2me.essentials.signs.SignTrade;
import com.earth2me.essentials.signs.SignWarp;
import com.earth2me.essentials.signs.SignWeather;

public enum Signs {
    BALANCE(new SignBalance()),
    BUY(new SignBuy()),
    DISPOSAL(new SignDisposal()),
    ENCHANT(new SignEnchant()),
    FREE(new SignFree()),
    GAMEMODE(new SignGameMode()),
    HEAL(new SignHeal()),
    INFO(new SignInfo()),
    KIT(new SignKit()),
    MAIL(new SignMail()),
    PROTECTION(new SignProtection()),
    REPAIR(new SignRepair()),
    SELL(new SignSell()),
    SPAWNMOB(new SignSpawnmob()),
    TIME(new SignTime()),
    TRADE(new SignTrade()),
    WARP(new SignWarp()),
    WEATHER(new SignWeather());
    
    private final EssentialsSign sign;

    private Signs(EssentialsSign sign) {
        this.sign = sign;
    }

    public EssentialsSign getSign() {
        return this.sign;
    }
}

