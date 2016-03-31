/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.earth2me.essentials.api;

import com.earth2me.essentials.api.IReload;
import java.util.Collection;
import net.ess3.api.IUser;
import org.bukkit.Location;

public interface IJails
extends IReload {
    public Location getJail(String var1) throws Exception;

    public Collection<String> getList() throws Exception;

    public int getCount();

    public void removeJail(String var1) throws Exception;

    public void sendToJail(IUser var1, String var2) throws Exception;

    public void setJail(String var1, Location var2) throws Exception;
}

