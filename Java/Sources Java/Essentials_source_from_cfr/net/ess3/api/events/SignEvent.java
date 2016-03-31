/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.ess3.api.events;

import com.earth2me.essentials.signs.EssentialsSign;
import net.ess3.api.IUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SignEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    EssentialsSign.ISign sign;
    EssentialsSign essSign;
    IUser user;

    public SignEvent(EssentialsSign.ISign sign, EssentialsSign essSign, IUser user) {
        this.sign = sign;
        this.essSign = essSign;
        this.user = user;
    }

    public EssentialsSign.ISign getSign() {
        return this.sign;
    }

    public EssentialsSign getEssentialsSign() {
        return this.essSign;
    }

    public IUser getUser() {
        return this.user;
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

