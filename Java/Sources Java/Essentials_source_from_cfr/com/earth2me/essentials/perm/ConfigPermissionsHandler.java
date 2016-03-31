/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.perm.SuperpermsHandler;
import net.ess3.api.IEssentials;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ConfigPermissionsHandler
extends SuperpermsHandler {
    private final transient IEssentials ess;

    public ConfigPermissionsHandler(Plugin ess) {
        this.ess = (IEssentials)ess;
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return true;
    }

    @Override
    public boolean hasPermission(Player base, String node) {
        String[] cmds = node.split("\\.", 2);
        return this.ess.getSettings().isPlayerCommand(cmds[cmds.length - 1]) || super.hasPermission(base, node);
    }
}

