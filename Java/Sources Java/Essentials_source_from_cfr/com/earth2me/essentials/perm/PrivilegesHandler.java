/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.krinsoft.privileges.Privileges
 *  net.krinsoft.privileges.groups.Group
 *  net.krinsoft.privileges.groups.GroupManager
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.SuperpermsHandler;
import java.util.ArrayList;
import java.util.List;
import net.krinsoft.privileges.Privileges;
import net.krinsoft.privileges.groups.Group;
import net.krinsoft.privileges.groups.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PrivilegesHandler
extends SuperpermsHandler {
    private final transient Privileges plugin;
    private final GroupManager manager;

    public PrivilegesHandler(Plugin plugin) {
        this.plugin = (Privileges)plugin;
        this.manager = this.plugin.getGroupManager();
    }

    @Override
    public String getGroup(Player base) {
        Group group = this.manager.getGroup(base);
        if (group == null) {
            return null;
        }
        return group.getName();
    }

    @Override
    public List<String> getGroups(Player base) {
        Group group = this.manager.getGroup(base);
        if (group == null) {
            return new ArrayList<String>();
        }
        return group.getGroupTree();
    }

    @Override
    public boolean inGroup(Player base, String group) {
        Group pGroup = this.manager.getGroup(base);
        if (pGroup == null) {
            return false;
        }
        return pGroup.isMemberOf(group);
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return this.hasPermission(base, "privileges.build");
    }
}

