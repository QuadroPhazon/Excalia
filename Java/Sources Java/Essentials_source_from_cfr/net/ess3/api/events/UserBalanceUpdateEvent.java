/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.ess3.api.events;

import java.math.BigDecimal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserBalanceUpdateEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final BigDecimal originalBalance;
    private final BigDecimal balance;

    public UserBalanceUpdateEvent(Player player, BigDecimal originalBalance, BigDecimal balance) {
        this.player = player;
        this.originalBalance = originalBalance;
        this.balance = balance;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BigDecimal getNewBalance() {
        return this.balance;
    }

    public BigDecimal getOldBalance() {
        return this.originalBalance;
    }
}

