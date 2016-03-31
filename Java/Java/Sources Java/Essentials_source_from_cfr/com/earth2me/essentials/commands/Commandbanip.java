/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandbanip
extends EssentialsCommand {
    public Commandbanip() {
        super("banip");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String senderName;
        String ipAddress;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        String string = senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : "Console";
        if (FormatUtil.validIP(args[0])) {
            ipAddress = args[0];
        } else {
            try {
                User player = this.getPlayer(server, args, 0, true, true);
                ipAddress = player.getLastLoginAddress();
            }
            catch (PlayerNotFoundException ex) {
                ipAddress = args[0];
            }
        }
        if (ipAddress.isEmpty()) {
            throw new PlayerNotFoundException();
        }
        this.ess.getServer().banIP(ipAddress);
        server.getLogger().log(Level.INFO, I18n._("playerBanIpAddress", senderName, ipAddress));
        this.ess.broadcastMessage("essentials.ban.notify", I18n._("playerBanIpAddress", senderName, ipAddress));
    }
}

