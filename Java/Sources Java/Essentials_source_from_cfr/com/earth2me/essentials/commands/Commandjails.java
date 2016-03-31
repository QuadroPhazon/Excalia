/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.utils.StringUtil;
import java.util.Collection;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandjails
extends EssentialsCommand {
    public Commandjails() {
        super("jails");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        sender.sendMessage("\u00a77" + StringUtil.joinList(" ", this.ess.getJails().getList()));
    }
}

