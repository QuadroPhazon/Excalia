/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.IEssentialsModule;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.Command;

public interface IEssentialsCommand {
    public String getName();

    public void run(Server var1, User var2, String var3, Command var4, String[] var5) throws Exception;

    public void run(Server var1, CommandSource var2, String var3, Command var4, String[] var5) throws Exception;

    public void setEssentials(IEssentials var1);

    public void setEssentialsModule(IEssentialsModule var1);
}

