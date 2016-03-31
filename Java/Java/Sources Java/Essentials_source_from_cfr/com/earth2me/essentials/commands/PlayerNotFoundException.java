/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;

public class PlayerNotFoundException
extends NoSuchFieldException {
    public PlayerNotFoundException() {
        super(I18n._("playerNotFound", new Object[0]));
    }
}

