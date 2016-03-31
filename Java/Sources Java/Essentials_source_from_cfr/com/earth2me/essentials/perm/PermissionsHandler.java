/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.BPermissions2Handler;
import com.earth2me.essentials.perm.ConfigPermissionsHandler;
import com.earth2me.essentials.perm.GroupManagerHandler;
import com.earth2me.essentials.perm.IPermissionsHandler;
import com.earth2me.essentials.perm.NullPermissionsHandler;
import com.earth2me.essentials.perm.PermissionsBukkitHandler;
import com.earth2me.essentials.perm.PermissionsExHandler;
import com.earth2me.essentials.perm.PrivilegesHandler;
import com.earth2me.essentials.perm.SimplyPermsHandler;
import com.earth2me.essentials.perm.SuperpermsHandler;
import com.earth2me.essentials.perm.ZPermissionsHandler;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PermissionsHandler
implements IPermissionsHandler {
    private transient IPermissionsHandler handler = new NullPermissionsHandler();
    private transient String defaultGroup = "default";
    private final transient Plugin plugin;
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private transient boolean useSuperperms = false;

    public PermissionsHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public PermissionsHandler(Plugin plugin, boolean useSuperperms) {
        this.plugin = plugin;
        this.useSuperperms = useSuperperms;
    }

    public PermissionsHandler(Plugin plugin, String defaultGroup) {
        this.plugin = plugin;
        this.defaultGroup = defaultGroup;
    }

    @Override
    public String getGroup(Player base) {
        String group = this.handler.getGroup(base);
        if (group == null) {
            group = this.defaultGroup;
        }
        return group;
    }

    @Override
    public List<String> getGroups(Player base) {
        List<String> groups = this.handler.getGroups(base);
        if (groups == null || groups.isEmpty()) {
            groups = Collections.singletonList(this.defaultGroup);
        }
        return Collections.unmodifiableList(groups);
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return this.handler.canBuild(base, group);
    }

    @Override
    public boolean inGroup(Player base, String group) {
        return this.handler.inGroup(base, group);
    }

    @Override
    public boolean hasPermission(Player base, String node) {
        return this.handler.hasPermission(base, node);
    }

    @Override
    public String getPrefix(Player base) {
        String prefix = this.handler.getPrefix(base);
        if (prefix == null) {
            prefix = "";
        }
        return prefix;
    }

    @Override
    public String getSuffix(Player base) {
        String suffix = this.handler.getSuffix(base);
        if (suffix == null) {
            suffix = "";
        }
        return suffix;
    }

    public void checkPermissions() {
        PluginManager pluginManager = this.plugin.getServer().getPluginManager();
        Plugin permExPlugin = pluginManager.getPlugin("PermissionsEx");
        if (permExPlugin != null && permExPlugin.isEnabled()) {
            if (!(this.handler instanceof PermissionsExHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using PermissionsEx based permissions.");
                this.handler = new PermissionsExHandler();
            }
            return;
        }
        Plugin GMplugin = pluginManager.getPlugin("GroupManager");
        if (GMplugin != null && GMplugin.isEnabled()) {
            if (!(this.handler instanceof GroupManagerHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using GroupManager based permissions.");
                this.handler = new GroupManagerHandler(GMplugin);
            }
            return;
        }
        Plugin permBukkitPlugin = pluginManager.getPlugin("PermissionsBukkit");
        if (permBukkitPlugin != null && permBukkitPlugin.isEnabled()) {
            if (!(this.handler instanceof PermissionsBukkitHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using PermissionsBukkit based permissions.");
                this.handler = new PermissionsBukkitHandler(permBukkitPlugin);
            }
            return;
        }
        Plugin simplyPermsPlugin = pluginManager.getPlugin("SimplyPerms");
        if (simplyPermsPlugin != null && simplyPermsPlugin.isEnabled()) {
            if (!(this.handler instanceof SimplyPermsHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using SimplyPerms based permissions.");
                this.handler = new SimplyPermsHandler(simplyPermsPlugin);
            }
            return;
        }
        Plugin privPlugin = pluginManager.getPlugin("Privileges");
        if (privPlugin != null && privPlugin.isEnabled()) {
            if (!(this.handler instanceof PrivilegesHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using Privileges based permissions.");
                this.handler = new PrivilegesHandler(privPlugin);
            }
            return;
        }
        Plugin bPermPlugin = pluginManager.getPlugin("bPermissions");
        if (bPermPlugin != null && bPermPlugin.isEnabled()) {
            if (!(this.handler instanceof BPermissions2Handler)) {
                LOGGER.log(Level.INFO, "Essentials: Using bPermissions2 based permissions.");
                this.handler = new BPermissions2Handler();
            }
            return;
        }
        Plugin zPermsPlugin = pluginManager.getPlugin("zPermissions");
        if (zPermsPlugin != null && zPermsPlugin.isEnabled()) {
            if (!(this.handler instanceof ZPermissionsHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using zPermissions based permissions.");
                this.handler = new ZPermissionsHandler(this.plugin);
            }
            return;
        }
        if (this.useSuperperms) {
            if (!(this.handler instanceof SuperpermsHandler)) {
                LOGGER.log(Level.INFO, "Essentials: Using superperms based permissions.");
                this.handler = new SuperpermsHandler();
            }
        } else if (!(this.handler instanceof ConfigPermissionsHandler)) {
            LOGGER.log(Level.INFO, "Essentials: Using config file enhanced permissions.");
            LOGGER.log(Level.INFO, "Permissions listed in as player-commands will be given to all users.");
            this.handler = new ConfigPermissionsHandler(this.plugin);
        }
    }

    public void setUseSuperperms(boolean useSuperperms) {
        this.useSuperperms = useSuperperms;
    }

    public String getName() {
        return this.handler.getClass().getSimpleName().replace("Handler", "");
    }
}

