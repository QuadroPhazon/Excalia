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

public class SpawnTeleportEvent
extends CustomEvent {
    private Player player;
    private Location to;
    private Location from;
    private boolean isAuthenticated;

    public SpawnTeleportEvent(Player player, Location from, Location to, boolean isAuthenticated) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.isAuthenticated = isAuthenticated;
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

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }
}

