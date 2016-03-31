/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  ru.tehkode.permissions.PermissionManager
 *  ru.tehkode.permissions.PermissionUser
 *  ru.tehkode.permissions.bukkit.PermissionsEx
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.SuperpermsHandler;
import java.util.Arrays;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHandler
extends SuperpermsHandler {
    private final transient PermissionManager manager = PermissionsEx.getPermissionManager();

    @Override
    public String getGroup(Player base) {
        PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return user.getGroupsNames()[0];
    }

    @Override
    public List<String> getGroups(Player base) {
        PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return Arrays.asList(user.getGroupsNames());
    }

    @Override
    public boolean canBuild(Player base, String group) {
        PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return false;
        }
        return user.getOptionBoolean("build", base.getWorld().getName(), false);
    }

    @Override
    public boolean inGroup(Player base, String group) {
        PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return false;
        }
        return user.inGroup(group);
    }

    @Override
    public boolean hasPermission(Player base, String node) {
        return super.hasPermission(base, node);
    }

    @Override
    public String getPrefix(Player base) {
        PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return user.getPrefix(base.getWorld().getName());
    }

    @Override
    public String getSuffix(Player base) {
        PermissionUser user = this.manager.getUser(base.getName());
        if (user == null) {
            return null;
        }
        return user.getSuffix(base.getWorld().getName());
    }
}

