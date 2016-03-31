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

public class Jails
implements StorageObject {
    @MapValueType(value=Location.class)
    private Map<String, Location> jails = new HashMap<String, Location>();

    public Map<String, Location> getJails() {
        return this.jails;
    }

    public void setJails(Map<String, Location> jails) {
        this.jails = jails;
    }

    public String toString() {
        return "Jails(jails=" + this.getJails() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Jails)) {
            return false;
        }
        Jails other = (Jails)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Map<String, Location> this$jails = this.getJails();
        Map<String, Location> other$jails = other.getJails();
        if (this$jails == null ? other$jails != null : !this$jails.equals(other$jails)) {
            return false;
        }
        return true;
    }

    public boolean canEqual(Object other) {
        return other instanceof Jails;
    }

    public int hashCode() {
        int PRIME = 31;
        int result = 1;
        Map<String, Location> $jails = this.getJails();
        result = result * 31 + ($jails == null ? 0 : $jails.hashCode());
        return result;
    }
}

