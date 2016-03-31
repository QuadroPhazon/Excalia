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

public class NickChangeEvent
extends StateChangeEvent
implements Cancellable {
    private String newValue;

    public NickChangeEvent(IUser affected, IUser controller, String value) {
        super(affected, controller);
        this.newValue = value;
    }

    public String getValue() {
        return this.newValue;
    }
}

