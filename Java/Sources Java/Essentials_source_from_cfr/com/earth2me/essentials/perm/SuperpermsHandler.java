/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.IPermissionsHandler;
import java.util.List;
import org.bukkit.entity.Player;

public class SuperpermsHandler
implements IPermissionsHandler {
    @Override
    public String getGroup(Player base) {
        return null;
    }

    @Override
    public List<String> getGroups(Player base) {
        return null;
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return false;
    }

    @Override
    public boolean inGroup(Player base, String group) {
        return this.hasPermission(base, "group." + group);
    }

    @Override
    public boolean hasPermission(Player base, String node) {
        String permCheck = node;
        while (!base.isPermissionSet(permCheck)) {
            int index = node.lastIndexOf(46);
            if (index < 1) {
                return base.hasPermission("*");
            }
            node = node.substring(0, index);
            permCheck = node + ".*";
        }
        return base.hasPermission(permCheck);
    }

    @Override
    public String getPrefix(Player base) {
        return null;
    }

    @Override
    public String getSuffix(Player base) {
        return null;
    }
}

