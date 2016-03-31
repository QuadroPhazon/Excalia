/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.cache.limbo;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.backup.DataFileCache;
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.events.CustomEvent;
import fr.xephi.authme.events.ResetInventoryEvent;
import fr.xephi.authme.events.StoreInventoryEvent;
import fr.xephi.authme.settings.Settings;
import java.util.HashMap;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

public class LimboCache {
    private static LimboCache singleton = null;
    public HashMap<String, LimboPlayer> cache;
    private FileCache playerData;
    public AuthMe plugin;

    private LimboCache(AuthMe plugin) {
        this.plugin = plugin;
        this.cache = new HashMap();
        this.playerData = new FileCache(plugin);
    }

    public void addLimboPlayer(Player player) {
        ItemStack[] inv;
        CustomEvent event;
        ItemStack[] arm;
        boolean flying;
        boolean operator;
        String name = player.getName();
        Location loc = player.getLocation();
        GameMode gameMode = player.getGameMode();
        String playerGroup = "";
        if (this.playerData.doesCacheExist(player)) {
            event = new StoreInventoryEvent(player, this.playerData);
            Bukkit.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled() && event.getInventory() != null && event.getArmor() != null) {
                inv = event.getInventory();
                arm = event.getArmor();
            } else {
                inv = null;
                arm = null;
            }
            playerGroup = this.playerData.readCache(player).getGroup();
            operator = this.playerData.readCache(player).getOperator();
            flying = this.playerData.readCache(player).isFlying();
        } else {
            event = new StoreInventoryEvent(player);
            Bukkit.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled() && event.getInventory() != null && event.getArmor() != null) {
                inv = event.getInventory();
                arm = event.getArmor();
            } else {
                inv = null;
                arm = null;
            }
            operator = player.isOp();
            flying = player.isFlying();
            if (this.plugin.permission != null) {
                try {
                    playerGroup = this.plugin.permission.getPrimaryGroup(player);
                }
                catch (UnsupportedOperationException e) {
                    ConsoleLogger.showError("Your permission system (" + this.plugin.permission.getName() + ") do not support Group system with that config... unhook!");
                    this.plugin.permission = null;
                }
            }
        }
        if (Settings.isForceSurvivalModeEnabled.booleanValue()) {
            if (Settings.isResetInventoryIfCreative.booleanValue() && player.getGameMode() == GameMode.CREATIVE) {
                event = new ResetInventoryEvent(player);
                Bukkit.getServer().getPluginManager().callEvent((Event)event);
                if (!event.isCancelled()) {
                    player.getInventory().clear();
                    player.sendMessage("Your inventory has been cleaned!");
                }
            }
            gameMode = GameMode.SURVIVAL;
        }
        if (player.isDead()) {
            loc = this.plugin.getSpawnLocation(player);
        }
        this.cache.put(player.getName(), new LimboPlayer(name, loc, inv, arm, gameMode, operator, playerGroup, flying));
    }

    public void addLimboPlayer(Player player, String group) {
        this.cache.put(player.getName(), new LimboPlayer(player.getName(), group));
    }

    public void deleteLimboPlayer(String name) {
        this.cache.remove(name);
    }

    public LimboPlayer getLimboPlayer(String name) {
        return this.cache.get(name);
    }

    public boolean hasLimboPlayer(String name) {
        return this.cache.containsKey(name);
    }

    public static LimboCache getInstance() {
        if (singleton == null) {
            singleton = new LimboCache(AuthMe.getInstance());
        }
        return singleton;
    }

    public void updateLimboPlayer(Player player) {
        if (this.hasLimboPlayer(player.getName())) {
            this.deleteLimboPlayer(player.getName());
        }
        this.addLimboPlayer(player);
    }
}

