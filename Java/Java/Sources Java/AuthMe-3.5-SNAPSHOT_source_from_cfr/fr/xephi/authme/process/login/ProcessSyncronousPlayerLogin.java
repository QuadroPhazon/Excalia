/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffectType
 */
package fr.xephi.authme.process.login;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.Utils;
import fr.xephi.authme.api.API;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.backup.FileCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.AuthMeTeleportEvent;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RestoreInventoryEvent;
import fr.xephi.authme.events.SpawnTeleportEvent;
import fr.xephi.authme.listener.AuthMePlayerListener;
import fr.xephi.authme.settings.Settings;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffectType;

public class ProcessSyncronousPlayerLogin
implements Runnable {
    private LimboPlayer limbo;
    private Player player;
    private String name;
    private PlayerAuth auth;
    private AuthMe plugin;
    private DataSource database;
    private PluginManager pm;
    private FileCache playerCache;

    public ProcessSyncronousPlayerLogin(Player player, AuthMe plugin, DataSource data) {
        this.plugin = plugin;
        this.database = data;
        this.pm = plugin.getServer().getPluginManager();
        this.player = player;
        this.name = player.getName();
        this.limbo = LimboCache.getInstance().getLimboPlayer(this.name);
        this.auth = this.database.getAuth(this.name);
        this.playerCache = new FileCache(plugin);
    }

    public LimboPlayer getLimbo() {
        return this.limbo;
    }

    protected void restoreOpState() {
        this.player.setOp(this.limbo.getOperator());
        if (this.player.getGameMode() != GameMode.CREATIVE && !Settings.isMovementAllowed.booleanValue()) {
            this.player.setAllowFlight(this.limbo.isFlying());
            this.player.setFlying(this.limbo.isFlying());
        }
    }

    protected void packQuitLocation() {
        Utils.getInstance().packCoords(this.auth.getQuitLocX(), this.auth.getQuitLocY(), this.auth.getQuitLocZ(), this.auth.getWorld(), this.player);
    }

    protected void teleportBackFromSpawn() {
        AuthMeTeleportEvent tpEvent = new AuthMeTeleportEvent(this.player, this.limbo.getLoc());
        this.pm.callEvent((Event)tpEvent);
        if (!tpEvent.isCancelled()) {
            Location fLoc = tpEvent.getTo();
            if (!fLoc.getChunk().isLoaded()) {
                fLoc.getChunk().load();
            }
            this.player.teleport(fLoc);
        }
    }

    protected void teleportToSpawn() {
        Location spawnL = this.plugin.getSpawnLocation(this.player);
        SpawnTeleportEvent tpEvent = new SpawnTeleportEvent(this.player, this.player.getLocation(), spawnL, true);
        this.pm.callEvent((Event)tpEvent);
        if (!tpEvent.isCancelled()) {
            Location fLoc = tpEvent.getTo();
            if (!fLoc.getChunk().isLoaded()) {
                fLoc.getChunk().load();
            }
            this.player.teleport(fLoc);
        }
    }

    protected void restoreInventory() {
        RestoreInventoryEvent event = new RestoreInventoryEvent(this.player, this.limbo.getInventory(), this.limbo.getArmour());
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (!event.isCancelled()) {
            API.setPlayerInventory(this.player, event.getInventory(), event.getArmor());
        }
    }

    protected void forceCommands() {
        for (String command2 : Settings.forceCommands) {
            try {
                this.player.performCommand(command2.replace("%p", this.player.getName()));
            }
            catch (Exception e) {}
        }
        for (String command2 : Settings.forceCommandsAsConsole) {
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), command2.replace("%p", this.player.getName()));
        }
    }

    public void run() {
        if (this.limbo != null) {
            this.restoreOpState();
            if (!Settings.forceOnlyAfterLogin.booleanValue()) {
                this.player.setGameMode(this.limbo.getGameMode());
                if (Settings.protectInventoryBeforeLogInEnabled.booleanValue() && this.player.hasPlayedBefore()) {
                    this.restoreInventory();
                }
            } else {
                if (Settings.protectInventoryBeforeLogInEnabled.booleanValue() && this.player.hasPlayedBefore()) {
                    this.restoreInventory();
                }
                this.player.setGameMode(GameMode.SURVIVAL);
            }
            if (!Settings.noTeleport.booleanValue()) {
                if (Settings.isTeleportToSpawnEnabled.booleanValue() && !Settings.isForceSpawnLocOnJoinEnabled.booleanValue() && Settings.getForcedWorlds.contains(this.player.getWorld().getName())) {
                    if (Settings.isSaveQuitLocationEnabled.booleanValue() && this.auth.getQuitLocY() != 0.0) {
                        this.packQuitLocation();
                    } else {
                        this.teleportBackFromSpawn();
                    }
                } else if (Settings.isForceSpawnLocOnJoinEnabled.booleanValue() && Settings.getForcedWorlds.contains(this.player.getWorld().getName())) {
                    this.teleportToSpawn();
                } else if (Settings.isSaveQuitLocationEnabled.booleanValue() && this.auth.getQuitLocY() != 0.0) {
                    this.packQuitLocation();
                } else {
                    this.teleportBackFromSpawn();
                }
            }
            if (Settings.isForceSurvivalModeEnabled.booleanValue()) {
                Utils.forceGM(this.player);
            }
            Utils.getInstance().setGroup(this.player, Utils.groupType.LOGGEDIN);
            LimboCache.getInstance().deleteLimboPlayer(this.name);
            if (this.playerCache.doesCacheExist(this.player)) {
                this.playerCache.removeCache(this.player);
            }
        }
        if (AuthMePlayerListener.joinMessage.containsKey(this.name) && AuthMePlayerListener.joinMessage.get(this.name) != null && !AuthMePlayerListener.joinMessage.get(this.name).isEmpty()) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (!p.isOnline()) continue;
                p.sendMessage(AuthMePlayerListener.joinMessage.get(this.name));
            }
            AuthMePlayerListener.joinMessage.remove(this.name);
        }
        if (Settings.applyBlindEffect.booleanValue()) {
            this.player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
        Bukkit.getServer().getPluginManager().callEvent((Event)new LoginEvent(this.player, true));
        this.player.saveData();
        if (Settings.useWelcomeMessage.booleanValue()) {
            if (Settings.broadcastWelcomeMessage.booleanValue()) {
                for (String s : Settings.welcomeMsg) {
                    Bukkit.getServer().broadcastMessage(this.plugin.replaceAllInfos(s, this.player));
                }
            } else {
                for (String s : Settings.welcomeMsg) {
                    this.player.sendMessage(this.plugin.replaceAllInfos(s, this.player));
                }
            }
        }
        this.forceCommands();
    }
}

