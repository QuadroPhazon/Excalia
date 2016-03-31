/*
 * Decompiled with CFR 0_110.
 */
package net.ess3.api.events;

import net.ess3.api.IUser;
import net.ess3.api.events.StatusChangeEvent;

public class MuteStatusChangeEvent
extends StatusChangeEvent {
    public MuteStatusChangeEvent(IUser affected, IUser controller, boolean value) {
        super(affected, controller, value);
    }
}

