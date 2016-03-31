/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.IPermissionsHandler;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;

public class NullPermissionsHandler
implements IPermissionsHandler {
    @Override
    public String getGroup(Player base) {
        return null;
    }

    @Override
    public List<String> getGroups(Player base) {
        return Collections.emptyList();
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return false;
    }

    @Override
    public boolean inGroup(Player base, String group) {
        return false;
    }

    @Override
    public boolean hasPermission(Player base, String node) {
        return false;
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

