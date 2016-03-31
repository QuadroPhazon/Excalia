/*
 * Decompiled with CFR 0_110.
 */
package net.ess3.api.events;

import com.earth2me.essentials.signs.EssentialsSign;
import net.ess3.api.IUser;
import net.ess3.api.events.SignEvent;

public class SignBreakEvent
extends SignEvent {
    public SignBreakEvent(EssentialsSign.ISign sign, EssentialsSign essSign, IUser user) {
        super(sign, essSign, user);
    }
}

