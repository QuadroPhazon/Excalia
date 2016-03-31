/*
 * Decompiled with CFR 0_110.
 */
package net.ess3.api;

import com.earth2me.essentials.I18n;

public class MaxMoneyException
extends Exception {
    public MaxMoneyException() {
        super(I18n._("maxMoney", new Object[0]));
    }
}

