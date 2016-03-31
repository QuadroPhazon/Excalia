/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.anjocaido.groupmanager.GroupManager
 *  org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder
 *  org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.IPermissionsHandler;
import java.util.Arrays;
import java.util.List;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GroupManagerHandler
implements IPermissionsHandler {
    private final transient GroupManager groupManager;

    public GroupManagerHandler(Plugin permissionsPlugin) {
        this.groupManager = (GroupManager)permissionsPlugin;
    }

    @Override
    public String getGroup(Player base) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return null;
        }
        return handler.getGroup(base.getName());
    }

    @Override
    public List<String> getGroups(Player base) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return null;
        }
        return Arrays.asList(handler.getGroups(base.getName()));
    }

    @Override
    public boolean canBuild(Player base, String group) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return false;
        }
        return handler.canUserBuild(base.getName());
    }

    @Override
    public boolean inGroup(Player base, String group) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return false;
        }
        return handler.inGroup(base.getName(), group);
    }

    @Override
    public boolean hasPermission(Player base, String node) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return false;
        }
        return handler.has(base, node);
    }

    @Override
    public String getPrefix(Player base) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return null;
        }
        return handler.getUserPrefix(base.getName());
    }

    @Override
    public String getSuffix(Player base) {
        AnjoPermissionsHandler handler = this.getHandler(base);
        if (handler == null) {
            return null;
        }
        return handler.getUserSuffix(base.getName());
    }

    private AnjoPermissionsHandler getHandler(Player base) {
        WorldsHolder holder = this.groupManager.getWorldsHolder();
        if (holder == null) {
            return null;
        }
        try {
            return holder.getWorldPermissions(base);
        }
        catch (NullPointerException ex) {
            return null;
        }
    }
}

