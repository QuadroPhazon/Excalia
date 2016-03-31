/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.onarandombox.MultiverseCore.MultiverseCore
 *  me.muizers.Notifications.Notifications
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.PluginDisableEvent
 *  org.bukkit.event.server.PluginEnableEvent
 *  org.bukkit.event.server.ServerListPingEvent
 *  org.bukkit.plugin.Plugin
 */
package fr.xephi.authme.listener;

import com.earth2me.essentials.Essentials;
import com.onarandombox.MultiverseCore.MultiverseCore;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import java.net.InetAddress;
import java.util.List;
import me.muizers.Notifications.Notifications;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

public class AuthMeServerListener
implements Listener {
    public AuthMe plugin;
    private Messages m = Messages.getInstance();

    public AuthMeServerListener(AuthMe plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onServerPing(ServerListPingEvent event) {
        if (!Settings.enableProtection.booleanValue()) {
            return;
        }
        if (Settings.countries.isEmpty()) {
            return;
        }
        if (!Settings.countriesBlacklist.isEmpty() && Settings.countriesBlacklist.contains(this.plugin.getCountryCode(event.getAddress().getHostAddress()))) {
            event.setMotd(this.m._("country_banned")[0]);
        }
        if (Settings.countries.contains(this.plugin.getCountryCode(event.getAddress().getHostAddress()))) {
            event.setMotd(this.plugin.getServer().getMotd());
        } else {
            event.setMotd(this.m._("country_banned")[0]);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPluginDisable(PluginDisableEvent event) {
        String pluginName = event.getPlugin().getName();
        if (pluginName.equalsIgnoreCase("Essentials")) {
            this.plugin.ess = null;
            ConsoleLogger.info("Essentials has been disabled, unhook!");
            return;
        }
        if (pluginName.equalsIgnoreCase("EssentialsSpawn")) {
            this.plugin.essentialsSpawn = null;
            ConsoleLogger.info("EssentialsSpawn has been disabled, unhook!");
            return;
        }
        if (pluginName.equalsIgnoreCase("Multiverse-Core")) {
            this.plugin.multiverse = null;
            ConsoleLogger.info("Multiverse-Core has been disabled, unhook!");
            return;
        }
        if (pluginName.equalsIgnoreCase("Notifications")) {
            this.plugin.notifications = null;
            ConsoleLogger.info("Notifications has been disabled, unhook!");
        }
        if (pluginName.equalsIgnoreCase("ChestShop")) {
            this.plugin.ChestShop = 0.0;
            ConsoleLogger.info("ChestShop has been disabled, unhook!");
        }
        if (pluginName.equalsIgnoreCase("CombatTag")) {
            this.plugin.CombatTag = 0;
            ConsoleLogger.info("CombatTag has been disabled, unhook!");
        }
        if (pluginName.equalsIgnoreCase("Citizens")) {
            this.plugin.CitizensVersion = 0;
            ConsoleLogger.info("Citizens has been disabled, unhook!");
        }
        if (pluginName.equalsIgnoreCase("Vault")) {
            this.plugin.permission = null;
            ConsoleLogger.showError("Vault has been disabled, unhook permissions!");
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPluginEnable(PluginEnableEvent event) {
        String pluginName = event.getPlugin().getName();
        if (pluginName.equalsIgnoreCase("Essentials") || pluginName.equalsIgnoreCase("EssentialsSpawn")) {
            this.plugin.checkEssentials();
        }
        if (pluginName.equalsIgnoreCase("Multiverse-Core")) {
            this.plugin.checkMultiverse();
        }
        if (pluginName.equalsIgnoreCase("Notifications")) {
            this.plugin.checkNotifications();
        }
        if (pluginName.equalsIgnoreCase("ChestShop")) {
            this.plugin.checkChestShop();
        }
        if (pluginName.equalsIgnoreCase("CombatTag")) {
            this.plugin.combatTag();
        }
        if (pluginName.equalsIgnoreCase("Citizens")) {
            this.plugin.citizensVersion();
        }
        if (pluginName.equalsIgnoreCase("Vault")) {
            this.plugin.checkVault();
        }
    }
}

