/*
 * Decompiled with CFR 0_110.
 */
package net.ess3.api;

import com.earth2me.essentials.I18n;

public class NoLoanPermittedException
extends Exception {
    public NoLoanPermittedException() {
        super(I18n._("negativeBalanceError", new Object[0]));
    }
}

