/*
 * Decompiled with CFR 0_110.
 */
package net.ess3.api.events;

import net.ess3.api.IUser;
import net.ess3.api.events.StatusChangeEvent;

public class AfkStatusChangeEvent
extends StatusChangeEvent {
    public AfkStatusChangeEvent(IUser affected, boolean value) {
        super(affected, affected, value);
    }
}

