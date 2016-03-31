/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package fr.xephi.authme.settings;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class OtherAccounts
extends CustomConfiguration {
    private static OtherAccounts others = null;

    public OtherAccounts() {
        super(new File("." + File.separator + "plugins" + File.separator + "AuthMe" + File.separator + "otheraccounts.yml"));
        others = this;
        this.load();
        this.save();
    }

    public void clear(UUID uuid) {
        this.set(uuid.toString(), new ArrayList());
        this.save();
    }

    public static OtherAccounts getInstance() {
        if (others == null) {
            others = new OtherAccounts();
        }
        return others;
    }

    public void addPlayer(UUID uuid) {
        try {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player == null) {
                return;
            }
            if (!this.getStringList(uuid.toString()).contains(player.getName())) {
                this.getStringList(uuid.toString()).add(player.getName());
                this.save();
            }
        }
        catch (NoSuchMethodError e) {
        }
        catch (Exception e) {
            // empty catch block
        }
    }

    public void removePlayer(UUID uuid) {
        try {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player == null) {
                return;
            }
            if (this.getStringList(uuid.toString()).contains(player.getName())) {
                this.getStringList(uuid.toString()).remove(player.getName());
                this.save();
            }
        }
        catch (NoSuchMethodError e) {
        }
        catch (Exception e) {
            // empty catch block
        }
    }

    public List<String> getAllPlayersByUUID(UUID uuid) {
        return this.getStringList(uuid.toString());
    }
}

