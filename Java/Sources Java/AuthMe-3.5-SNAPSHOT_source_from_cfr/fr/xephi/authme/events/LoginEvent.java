/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoginEvent
extends Event {
    private Player player;
    private boolean isLogin;
    private static final HandlerList handlers = new HandlerList();

    public LoginEvent(Player player, boolean isLogin) {
        this.player = player;
        this.isLogin = isLogin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Deprecated
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

