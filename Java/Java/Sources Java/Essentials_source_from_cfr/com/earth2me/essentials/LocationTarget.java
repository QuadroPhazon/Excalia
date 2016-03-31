/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.earth2me.essentials;

import com.earth2me.essentials.ITarget;
import org.bukkit.Location;

public class LocationTarget
implements ITarget {
    private final Location location;

    LocationTarget(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}

