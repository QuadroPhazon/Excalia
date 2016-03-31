/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 */
package fr.xephi.authme;

import com.earth2me.essentials.Essentials;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DataManager
extends Thread {
    public AuthMe plugin;
    public DataSource database;

    public DataManager(AuthMe plugin, DataSource database) {
        this.plugin = plugin;
        this.database = database;
    }

    @Override
    public void run() {
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        OfflinePlayer result;
        block4 : {
            result = null;
            try {
                if (Bukkit.class.getMethod("getOfflinePlayer", String.class).isAnnotationPresent(Deprecated.class)) {
                    for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                        if (!op.getName().equalsIgnoreCase(name)) continue;
                        result = op;
                        break block4;
                    }
                    break block4;
                }
                result = Bukkit.getOfflinePlayer((String)name);
            }
            catch (Exception e) {
                result = Bukkit.getOfflinePlayer((String)name);
            }
        }
        return result;
    }

    public void purgeAntiXray(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            try {
                OfflinePlayer player = this.getOfflinePlayer(name);
                if (player == null) continue;
                String playerName = player.getName();
                File playerFile = new File("." + File.separator + "plugins" + File.separator + "AntiXRayData" + File.separator + "PlayerData" + File.separator + playerName);
                if (!playerFile.exists()) continue;
                playerFile.delete();
                ++i;
            }
            catch (Exception e) {}
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " AntiXRayData Files");
    }

    public void purgeLimitedCreative(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            try {
                OfflinePlayer player = this.getOfflinePlayer(name);
                if (player == null) continue;
                String playerName = player.getName();
                File playerFile = new File("." + File.separator + "plugins" + File.separator + "LimitedCreative" + File.separator + "inventories" + File.separator + playerName + ".yml");
                if (playerFile.exists()) {
                    playerFile.delete();
                    ++i;
                }
                if ((playerFile = new File("." + File.separator + "plugins" + File.separator + "LimitedCreative" + File.separator + "inventories" + File.separator + playerName + "_creative.yml")).exists()) {
                    playerFile.delete();
                    ++i;
                }
                if (!(playerFile = new File("." + File.separator + "plugins" + File.separator + "LimitedCreative" + File.separator + "inventories" + File.separator + playerName + "_adventure.yml")).exists()) continue;
                playerFile.delete();
                ++i;
            }
            catch (Exception e) {}
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " LimitedCreative Survival, Creative and Adventure files");
    }

    public void purgeDat(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            try {
                OfflinePlayer player = this.getOfflinePlayer(name);
                if (player == null) continue;
                String playerName = player.getName();
                File playerFile = new File(this.plugin.getServer().getWorldContainer() + File.separator + Settings.defaultWorld + File.separator + "players" + File.separator + playerName + ".dat");
                if (!playerFile.exists()) continue;
                playerFile.delete();
                ++i;
            }
            catch (Exception e) {}
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " .dat Files");
    }

    public void purgeEssentials(List<String> cleared) {
        int i = 0;
        for (String name : cleared) {
            try {
                File playerFile = new File(this.plugin.ess.getDataFolder() + File.separator + "userdata" + File.separator + name + ".yml");
                if (!playerFile.exists()) continue;
                playerFile.delete();
                ++i;
            }
            catch (Exception e) {}
        }
        ConsoleLogger.info("AutoPurgeDatabase : Remove " + i + " EssentialsFiles");
    }
}

