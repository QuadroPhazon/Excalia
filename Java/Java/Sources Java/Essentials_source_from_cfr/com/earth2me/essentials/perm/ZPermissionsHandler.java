/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServiceRegisterEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.SuperpermsHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

public class ZPermissionsHandler
extends SuperpermsHandler
implements Listener {
    private ZPermissionsService service = null;
    private boolean hasGetPlayerPrimaryGroup = false;

    public ZPermissionsHandler(Plugin plugin) {
        this.acquireZPermissionsService();
        if (!this.isReady()) {
            Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
        }
    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (ZPermissionsService.class.equals(event.getProvider().getService())) {
            this.acquireZPermissionsService();
        }
    }

    @Override
    public String getGroup(Player base) {
        if (!this.isReady()) {
            return super.getGroup(base);
        }
        return this.getPrimaryGroup(base.getName());
    }

    @Override
    public List<String> getGroups(Player base) {
        if (!this.isReady()) {
            return super.getGroups(base);
        }
        return new ArrayList<String>(this.service.getPlayerGroups(base.getName()));
    }

    @Override
    public boolean inGroup(Player base, String group) {
        if (!this.isReady()) {
            return super.inGroup(base, group);
        }
        Set groups = this.service.getPlayerGroups(base.getName());
        for (String test : groups) {
            if (!test.equalsIgnoreCase(group)) continue;
            return true;
        }
        return false;
    }

    @Override
    public String getPrefix(Player base) {
        if (!this.isReady()) {
            return super.getPrefix(base);
        }
        return this.getPrefixSuffix(base, "prefix");
    }

    @Override
    public String getSuffix(Player base) {
        if (!this.isReady()) {
            return super.getSuffix(base);
        }
        return this.getPrefixSuffix(base, "suffix");
    }

    private String getPrefixSuffix(Player base, String metadataName) {
        String playerPrefixSuffix;
        try {
            playerPrefixSuffix = (String)this.service.getPlayerMetadata(base.getName(), metadataName, (Class)String.class);
        }
        catch (IllegalStateException e) {
            playerPrefixSuffix = null;
        }
        if (playerPrefixSuffix == null) {
            try {
                return (String)this.service.getGroupMetadata(this.getPrimaryGroup(base.getName()), metadataName, (Class)String.class);
            }
            catch (IllegalStateException e) {
                return null;
            }
        }
        return playerPrefixSuffix;
    }

    private void acquireZPermissionsService() {
        this.service = (ZPermissionsService)Bukkit.getServicesManager().load((Class)ZPermissionsService.class);
        if (this.isReady()) {
            try {
                this.service.getClass().getMethod("getPlayerPrimaryGroup", String.class);
                this.hasGetPlayerPrimaryGroup = true;
            }
            catch (NoSuchMethodException e) {
                this.hasGetPlayerPrimaryGroup = false;
            }
            catch (SecurityException e) {
                this.hasGetPlayerPrimaryGroup = false;
            }
        }
    }

    private boolean isReady() {
        return this.service != null;
    }

    private String getPrimaryGroup(String playerName) {
        if (this.hasGetPlayerPrimaryGroup) {
            return this.service.getPlayerPrimaryGroup(playerName);
        }
        List groups = this.service.getPlayerAssignedGroups(playerName);
        return (String)groups.get(0);
    }
}

