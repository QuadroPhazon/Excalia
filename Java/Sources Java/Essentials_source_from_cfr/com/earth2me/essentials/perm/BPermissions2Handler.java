/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  de.bananaco.bpermissions.api.ApiLayer
 *  de.bananaco.bpermissions.api.util.CalculableType
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.perm;

import com.earth2me.essentials.perm.SuperpermsHandler;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import java.util.Arrays;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BPermissions2Handler
extends SuperpermsHandler {
    @Override
    public String getGroup(Player base) {
        List<String> groups = this.getGroups(base);
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        return groups.get(0);
    }

    @Override
    public List<String> getGroups(Player base) {
        String[] groups = ApiLayer.getGroups((String)base.getWorld().getName(), (CalculableType)CalculableType.USER, (String)base.getName());
        return Arrays.asList(groups);
    }

    @Override
    public boolean inGroup(Player base, String group) {
        return ApiLayer.hasGroupRecursive((String)base.getWorld().getName(), (CalculableType)CalculableType.USER, (String)base.getName(), (String)group);
    }

    @Override
    public boolean canBuild(Player base, String group) {
        return this.hasPermission(base, "bPermissions.build");
    }

    @Override
    public String getPrefix(Player base) {
        return ApiLayer.getValue((String)base.getWorld().getName(), (CalculableType)CalculableType.USER, (String)base.getName(), (String)"prefix");
    }

    @Override
    public String getSuffix(Player base) {
        return ApiLayer.getValue((String)base.getWorld().getName(), (CalculableType)CalculableType.USER, (String)base.getName(), (String)"suffix");
    }
}

