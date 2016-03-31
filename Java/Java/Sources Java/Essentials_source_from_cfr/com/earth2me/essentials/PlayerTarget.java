/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials;

import com.earth2me.essentials.ITarget;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerTarget
implements ITarget {
    private final String name;

    public PlayerTarget(Player entity) {
        this.name = entity.getName();
    }

    @Override
    public Location getLocation() {
        return Bukkit.getServer().getPlayerExact(this.name).getLocation();
    }
}

