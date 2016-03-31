/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 */
package net.ess3.api.events;

import net.ess3.api.IUser;
import net.ess3.api.events.StateChangeEvent;
import org.bukkit.event.Cancellable;

public class StatusChangeEvent
extends StateChangeEvent
implements Cancellable {
    private boolean newValue;

    public StatusChangeEvent(IUser affected, IUser controller, boolean value) {
        super(affected, controller);
        this.newValue = value;
    }

    public StatusChangeEvent(boolean isAsync, IUser affected, IUser controller, boolean value) {
        super(isAsync, affected, controller);
        this.newValue = value;
    }

    public boolean getValue() {
        return this.newValue;
    }
}

