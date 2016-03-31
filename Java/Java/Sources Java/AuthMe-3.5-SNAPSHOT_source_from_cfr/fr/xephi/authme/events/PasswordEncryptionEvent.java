/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package fr.xephi.authme.events;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PasswordEncryptionEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private EncryptionMethod method = null;
    private String playerName = "";

    public PasswordEncryptionEvent(EncryptionMethod method, String playerName) {
        this.method = method;
        this.playerName = playerName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public void setMethod(EncryptionMethod method) {
        this.method = method;
    }

    public EncryptionMethod getMethod() {
        return this.method;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

