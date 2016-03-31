/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package fr.xephi.authme.events;

import fr.xephi.authme.events.CustomEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegisterTeleportEvent
extends CustomEvent {
    private Player player;
    private Location to;
    private Location from;

    public RegisterTeleportEvent(Player player, Location to) {
        this.player = player;
        this.from = player.getLocation();
        this.to = to;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Location getTo() {
        return this.to;
    }

    public Location getFrom() {
        return this.from;
    }
}

