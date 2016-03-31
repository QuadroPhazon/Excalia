/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package fr.xephi.authme.settings;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Spawn
extends CustomConfiguration {
    private static Spawn spawn;

    public Spawn() {
        super(new File("." + File.separator + "plugins" + File.separator + "AuthMe" + File.separator + "spawn.yml"));
        spawn = this;
        this.load();
        this.save();
        this.saveDefault();
    }

    private void saveDefault() {
        if (!this.contains("spawn")) {
            this.set("spawn.world", (Object)"");
            this.set("spawn.x", (Object)"");
            this.set("spawn.y", (Object)"");
            this.set("spawn.z", (Object)"");
            this.set("spawn.yaw", (Object)"");
            this.set("spawn.pitch", (Object)"");
            this.save();
        }
        if (!this.contains("firstspawn")) {
            this.set("firstspawn.world", (Object)"");
            this.set("firstspawn.x", (Object)"");
            this.set("firstspawn.y", (Object)"");
            this.set("firstspawn.z", (Object)"");
            this.set("firstspawn.yaw", (Object)"");
            this.set("firstspawn.pitch", (Object)"");
            this.save();
        }
    }

    public static Spawn getInstance() {
        if (spawn == null) {
            spawn = new Spawn();
        }
        return spawn;
    }

    public boolean setSpawn(Location location) {
        try {
            this.set("spawn.world", (Object)location.getWorld().getName());
            this.set("spawn.x", (Object)location.getX());
            this.set("spawn.y", (Object)location.getY());
            this.set("spawn.z", (Object)location.getZ());
            this.set("spawn.yaw", (Object)Float.valueOf(location.getYaw()));
            this.set("spawn.pitch", (Object)Float.valueOf(location.getPitch()));
            this.save();
            return true;
        }
        catch (NullPointerException npe) {
            return false;
        }
    }

    public boolean setFirstSpawn(Location location) {
        try {
            this.set("firstspawn.world", (Object)location.getWorld().getName());
            this.set("firstspawn.x", (Object)location.getX());
            this.set("firstspawn.y", (Object)location.getY());
            this.set("firstspawn.z", (Object)location.getZ());
            this.set("firstspawn.yaw", (Object)Float.valueOf(location.getYaw()));
            this.set("firstspawn.pitch", (Object)Float.valueOf(location.getPitch()));
            this.save();
            return true;
        }
        catch (NullPointerException npe) {
            return false;
        }
    }

    @Deprecated
    public Location getLocation() {
        return this.getSpawn();
    }

    public Location getSpawn() {
        try {
            if (this.getString("spawn.world").isEmpty() || this.getString("spawn.world") == "") {
                return null;
            }
            Location location = new Location(Bukkit.getWorld((String)this.getString("spawn.world")), this.getDouble("spawn.x"), this.getDouble("spawn.y"), this.getDouble("spawn.z"), Float.parseFloat(this.getString("spawn.yaw")), Float.parseFloat(this.getString("spawn.pitch")));
            return location;
        }
        catch (NullPointerException npe) {
            return null;
        }
        catch (NumberFormatException nfe) {
            return null;
        }
    }

    public Location getFirstSpawn() {
        try {
            if (this.getString("firstspawn.world").isEmpty() || this.getString("firstspawn.world") == "") {
                return null;
            }
            Location location = new Location(Bukkit.getWorld((String)this.getString("firstspawn.world")), this.getDouble("firstspawn.x"), this.getDouble("firstspawn.y"), this.getDouble("firstspawn.z"), Float.parseFloat(this.getString("firstspawn.yaw")), Float.parseFloat(this.getString("firstspawn.pitch")));
            return location;
        }
        catch (NullPointerException npe) {
            return null;
        }
        catch (NumberFormatException nfe) {
            return null;
        }
    }
}

