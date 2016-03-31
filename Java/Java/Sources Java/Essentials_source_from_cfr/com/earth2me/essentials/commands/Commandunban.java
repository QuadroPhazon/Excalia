/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandunban
extends EssentialsCommand {
    public Commandunban() {
        super("unban");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String name;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        try {
            User user = this.getPlayer(server, args, 0, true, true);
            name = user.getName();
            user.setBanned(false);
            user.setBanTimeout(0);
        }
        catch (NoSuchFieldException e) {
            OfflinePlayer player = server.getOfflinePlayer(args[0]);
            name = player.getName();
            if (!player.isBanned()) {
                throw new Exception(I18n._("playerNotFound", new Object[0]), e);
            }
            player.setBanned(false);
        }
        String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
        server.getLogger().log(Level.INFO, I18n._("playerUnbanned", senderName, name));
        this.ess.broadcastMessage("essentials.ban.notify", I18n._("playerUnbanned", senderName, name));
    }
}

