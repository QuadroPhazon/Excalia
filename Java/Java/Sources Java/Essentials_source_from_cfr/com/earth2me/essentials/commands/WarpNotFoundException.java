/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;

public class WarpNotFoundException
extends Exception {
    public WarpNotFoundException() {
        super(I18n._("warpNotExist", new Object[0]));
    }

    public WarpNotFoundException(String message) {
        super(message);
    }
}

