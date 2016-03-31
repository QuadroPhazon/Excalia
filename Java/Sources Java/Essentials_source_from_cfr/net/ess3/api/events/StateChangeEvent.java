/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.ess3.api.events;

import net.ess3.api.IUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StateChangeEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    IUser affected;
    IUser controller;

    public StateChangeEvent(IUser affected, IUser controller) {
        this.affected = affected;
        this.controller = controller;
    }

    public StateChangeEvent(boolean isAsync, IUser affected, IUser controller) {
        super(isAsync);
        this.affected = affected;
        this.controller = controller;
    }

    public IUser getAffected() {
        return this.affected;
    }

    public IUser getController() {
        return this.controller;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

