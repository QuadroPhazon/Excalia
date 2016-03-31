/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 */
package com.earth2me.essentials;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.IReplyTo;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public final class Console
implements IReplyTo {
    private static final Console instance = new Console();
    private CommandSource replyTo;
    public static final String NAME = "Console";

    private Console() {
    }

    public static CommandSender getCommandSender(Server server) throws Exception {
        return server.getConsoleSender();
    }

    @Override
    public void setReplyTo(CommandSource user) {
        this.replyTo = user;
    }

    @Override
    public CommandSource getReplyTo() {
        return this.replyTo;
    }

    public static Console getConsoleReplyTo() {
        return instance;
    }
}

