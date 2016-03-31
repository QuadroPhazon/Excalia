/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package fr.xephi.authme.plugin.manager;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class EssSpawn
extends CustomConfiguration {
    private static EssSpawn spawn;

    public EssSpawn() {
        super(new File("." + File.separator + "plugins" + File.separator + "Essentials" + File.separator + "spawn.yml"));
        spawn = this;
        this.load();
    }

    public static EssSpawn getInstance() {
        if (spawn == null) {
            spawn = new EssSpawn();
        }
        return spawn;
    }

    public Location getLocation() {
        try {
            if (!this.contains("spawns.default.world")) {
                return null;
            }
            if (this.getString("spawns.default.world").isEmpty() || this.getString("spawns.default.world") == "") {
                return null;
            }
            Location location = new Location(Bukkit.getWorld((String)this.getString("spawns.default.world")), this.getDouble("spawns.default.x"), this.getDouble("spawns.default.y"), this.getDouble("spawns.default.z"), Float.parseFloat(this.getString("spawns.default.yaw")), Float.parseFloat(this.getString("spawns.default.pitch")));
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

