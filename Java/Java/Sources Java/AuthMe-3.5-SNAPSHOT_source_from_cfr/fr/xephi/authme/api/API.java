/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.api;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.plugin.manager.CitizensCommunicator;
import fr.xephi.authme.plugin.manager.CombatTagComunicator;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.settings.Settings;
import java.security.NoSuchAlgorithmException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class API {
    public static final String newline = System.getProperty("line.separator");
    public static AuthMe instance;
    public static DataSource database;

    public API(AuthMe instance, DataSource database) {
        API.instance = instance;
        API.database = database;
    }

    public static AuthMe hookAuthMe() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        if (plugin == null || !(plugin instanceof AuthMe)) {
            return null;
        }
        return (AuthMe)plugin;
    }

    public AuthMe getPlugin() {
        return instance;
    }

    public static boolean isAuthenticated(Player player) {
        return PlayerCache.getInstance().isAuthenticated(player.getName());
    }

    @Deprecated
    public boolean isaNPC(Player player) {
        if (instance.getCitizensCommunicator().isNPC((Entity)player, instance)) {
            return true;
        }
        return CombatTagComunicator.isNPC((Entity)player);
    }

    public boolean isNPC(Player player) {
        if (instance.getCitizensCommunicator().isNPC((Entity)player, instance)) {
            return true;
        }
        return CombatTagComunicator.isNPC((Entity)player);
    }

    public static boolean isUnrestricted(Player player) {
        return Utils.getInstance().isUnrestricted(player);
    }

    public static Location getLastLocation(Player player) {
        try {
            PlayerAuth auth = PlayerCache.getInstance().getAuth(player.getName());
            if (auth != null) {
                Location loc = new Location(Bukkit.getWorld((String)auth.getWorld()), auth.getQuitLocX(), auth.getQuitLocY(), auth.getQuitLocZ());
                return loc;
            }
            return null;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    public static void setPlayerInventory(Player player, ItemStack[] content, ItemStack[] armor) {
        try {
            player.getInventory().setContents(content);
            player.getInventory().setArmorContents(armor);
        }
        catch (NullPointerException npe) {
            // empty catch block
        }
    }

    public static boolean isRegistered(String playerName) {
        String player = playerName;
        return database.isAuthAvailable(player);
    }

    public static boolean checkPassword(String playerName, String passwordToCheck) {
        if (!API.isRegistered(playerName)) {
            return false;
        }
        String player = playerName;
        PlayerAuth auth = database.getAuth(player);
        try {
            return PasswordSecurity.comparePasswordWithHash(passwordToCheck, auth.getHash(), playerName);
        }
        catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public static boolean registerPlayer(String playerName, String password) {
        try {
            String name = playerName;
            String hash = PasswordSecurity.getHash(Settings.getPasswordHash, password, name);
            if (API.isRegistered(name)) {
                return false;
            }
            PlayerAuth auth = new PlayerAuth(name, hash, "198.18.0.1", 0, "your@email.com");
            if (!database.saveAuth(auth)) {
                return false;
            }
            return true;
        }
        catch (NoSuchAlgorithmException ex) {
            return false;
        }
    }

    public static void forceLogin(Player player) {
        API.instance.management.performLogin(player, "dontneed", true);
    }
}

