/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package com.earth2me.essentials.api;

import com.earth2me.essentials.IConf;
import com.earth2me.essentials.commands.WarpNotFoundException;
import java.io.File;
import java.util.Collection;
import net.ess3.api.InvalidNameException;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;

public interface IWarps
extends IConf {
    public Location getWarp(String var1) throws WarpNotFoundException, InvalidWorldException;

    public Collection<String> getList();

    public int getCount();

    public void removeWarp(String var1) throws Exception;

    public void setWarp(String var1, Location var2) throws Exception;

    public boolean isEmpty();

    public File getWarpFile(String var1) throws InvalidNameException;
}

