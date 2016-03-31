/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandtpall
extends EssentialsCommand {
    public Commandtpall() {
        super("tpall");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            if (sender.isPlayer()) {
                this.teleportAllPlayers(server, sender, this.ess.getUser(sender.getPlayer()));
                return;
            }
            throw new NotEnoughArgumentsException();
        }
        User target = this.getPlayer(server, sender, args, 0);
        this.teleportAllPlayers(server, sender, target);
    }

    private void teleportAllPlayers(Server server, CommandSource sender, User target) {
        sender.sendMessage(I18n._("teleportAll", new Object[0]));
        Location loc = target.getLocation();
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            User player = this.ess.getUser(onlinePlayer);
            if (target == player || sender.equals((Object)target.getBase()) && target.getWorld() != player.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !target.isAuthorized("essentials.worlds." + target.getWorld().getName())) continue;
            try {
                player.getTeleport().now(loc, false, PlayerTeleportEvent.TeleportCause.COMMAND);
                continue;
            }
            catch (Exception ex) {
                this.ess.showError(sender, ex, this.getName());
            }
        }
    }
}

