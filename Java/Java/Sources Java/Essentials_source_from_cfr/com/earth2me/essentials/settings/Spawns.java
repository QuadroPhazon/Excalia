/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class Spawns
implements StorageObject {
    @MapValueType(value=Location.class)
    private Map<String, Location> spawns = new HashMap<String, Location>();

    public Map<String, Location> getSpawns() {
        return this.spawns;
    }

    public void setSpawns(Map<String, Location> spawns) {
        this.spawns = spawns;
    }

    public String toString() {
        return "Spawns(spawns=" + this.getSpawns() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Spawns)) {
            return false;
        }
        Spawns other = (Spawns)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Map<String, Location> this$spawns = this.getSpawns();
        Map<String, Location> other$spawns = other.getSpawns();
        if (this$spawns == null ? other$spawns != null : !this$spawns.equals(other$spawns)) {
            return false;
        }
        return true;
    }

    public boolean canEqual(Object other) {
        return other instanceof Spawns;
    }

    public int hashCode() {
        int PRIME = 31;
        int result = 1;
        Map<String, Location> $spawns = this.getSpawns();
        result = result * 31 + ($spawns == null ? 0 : $spawns.hashCode());
        return result;
    }
}

