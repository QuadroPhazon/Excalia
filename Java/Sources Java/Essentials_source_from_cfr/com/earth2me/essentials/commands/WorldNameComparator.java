/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import java.util.Comparator;
import org.bukkit.World;

class WorldNameComparator
implements Comparator<World> {
    WorldNameComparator() {
    }

    @Override
    public int compare(World a, World b) {
        return a.getName().compareTo(b.getName());
    }
}

