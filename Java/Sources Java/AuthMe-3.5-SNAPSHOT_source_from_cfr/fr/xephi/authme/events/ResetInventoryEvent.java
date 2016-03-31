/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package fr.xephi.authme.events;

import fr.xephi.authme.events.CustomEvent;
import org.bukkit.entity.Player;

public class ResetInventoryEvent
extends CustomEvent {
    private Player player;

    public ResetInventoryEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

