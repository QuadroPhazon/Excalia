/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.api;

import com.earth2me.essentials.I18n;

public class InvalidWorldException
extends Exception {
    private final String world;

    public InvalidWorldException(String world) {
        super(I18n._("invalidWorld", new Object[0]));
        this.world = world;
    }

    public String getWorld() {
        return this.world;
    }
}

