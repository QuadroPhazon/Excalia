/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.platymuus.bukkit.permissions.Group
 *  com.platymuus.bukkit.permissions.PermissionInfo
 *  com.platymuus.bukkit.permissions.PermissionsPlugin
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.SuperpermsHandler;
import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionInfo;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PermissionsBukkitHandler
extends SuperpermsHandler {
    private final transient PermissionsPlugin plugin;

    public PermissionsBukkitHandler(Plugin plugin) {
        this.plugin = (PermissionsPlugin)plugin;
    }

    @Override
    public String getGroup(Player base) {
        List<Group> groups = this.getPBGroups(base);
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        return groups.get(0).getName();
    }

    @Override
    public List<String> getGroups(Player base) {
        List<Group> groups = this.getPBGroups(base);
        if (groups.size() == 1) {
            return Collections.singletonList(groups.get(0).getName());
        }
        ArrayList<String> groupNames = new ArrayList<String>(groups.size());
        for (Group group : groups) {
            groupNames.add(group.getName());
        }
        return groupNames;
    }

    private List<Group> getPBGroups(Player base) {
        PermissionInfo info = this.plugin.getPlayerInfo(base.getName());
        if (info == null) {
            return Collections.emptyList();
        }
        List groups = info.getGroups();
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }
        return groups;
    }

    @Override
    public boolean inGroup(Player base, String group) {
        List<Group> groups = this.getPBGroups(base);
        for (Group group1 : groups) {
            if (!group1.getName().equalsIgnoreCase(group)) continue;
            return true;
        }
        return false;
    }
}

