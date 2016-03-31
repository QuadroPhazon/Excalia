/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.crystalyx.bukkit.simplyperms.SimplyAPI
 *  net.crystalyx.bukkit.simplyperms.SimplyPlugin
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.SuperpermsHandler;
import java.util.List;
import net.crystalyx.bukkit.simplyperms.SimplyAPI;
import net.crystalyx.bukkit.simplyperms.SimplyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SimplyPermsHandler
extends SuperpermsHandler {
    private final transient SimplyAPI api;

    public SimplyPermsHandler(Plugin plugin) {
        this.api = ((SimplyPlugin)plugin).getAPI();
    }

    @Override
    public String getGroup(Player base) {
        List groups = this.api.getPlayerGroups(base.getName());
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        return (String)groups.get(0);
    }

    @Override
    public List<String> getGroups(Player base) {
        return this.api.getPlayerGroups(base.getName());
    }

    @Override
    public boolean inGroup(Player base, String group) {
        List groups = this.api.getPlayerGroups(base.getName());
        for (String group1 : groups) {
            if (!group1.equalsIgnoreCase(group)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return this.hasPermission(base, "permissions.allow.build");
    }
}

