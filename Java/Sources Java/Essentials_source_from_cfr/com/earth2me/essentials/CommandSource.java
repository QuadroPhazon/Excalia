/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials;

import com.earth2me.essentials.IReplyTo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSource
implements IReplyTo {
    private CommandSource replyTo = null;
    protected CommandSender sender;

    public CommandSource(CommandSender base) {
        this.sender = base;
    }

    public final CommandSender getSender() {
        return this.sender;
    }

    public final Player getPlayer() {
        if (this.sender instanceof Player) {
            return (Player)this.sender;
        }
        return null;
    }

    public final boolean isPlayer() {
        return this.sender instanceof Player;
    }

    public final CommandSender setSender(CommandSender base) {
        this.sender = base;
        return this.sender;
    }

    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            this.sender.sendMessage(message);
        }
    }

    @Override
    public void setReplyTo(CommandSource user) {
        this.replyTo = user;
    }

    @Override
    public CommandSource getReplyTo() {
        return this.replyTo;
    }
}

