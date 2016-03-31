/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.perm;

import java.util.List;
import org.bukkit.entity.Player;

public interface IPermissionsHandler {
    public String getGroup(Player var1);

    public List<String> getGroups(Player var1);

    public boolean canBuild(Player var1, String var2);

    public boolean inGroup(Player var1, String var2);

    public boolean hasPermission(Player var1, String var2);

    public String getPrefix(Player var1);

    public String getSuffix(Player var1);
}

