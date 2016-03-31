/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandtpo
extends EssentialsCommand {
    public Commandtpo() {
        super("tpo");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        switch (args.length) {
            case 0: {
                throw new NotEnoughArgumentsException();
            }
            case 1: {
                User player = this.getPlayer(server, user, args, 0);
                if (user.getWorld() != player.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + player.getWorld().getName())) {
                    throw new Exception(I18n._("noPerm", "essentials.worlds." + player.getWorld().getName()));
                }
                user.getTeleport().now(player.getBase(), false, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            }
            default: {
                if (!user.isAuthorized("essentials.tp.others")) {
                    throw new Exception(I18n._("noPerm", "essentials.tp.others"));
                }
                User target = this.getPlayer(server, user, args, 0);
                User toPlayer = this.getPlayer(server, user, args, 1);
                if (target.getWorld() != toPlayer.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + toPlayer.getWorld().getName())) {
                    throw new Exception(I18n._("noPerm", "essentials.worlds." + toPlayer.getWorld().getName()));
                }
                target.getTeleport().now(toPlayer.getBase(), false, PlayerTeleportEvent.TeleportCause.COMMAND);
                target.sendMessage(I18n._("teleportAtoB", user.getDisplayName(), toPlayer.getDisplayName()));
            }
        }
    }
}

