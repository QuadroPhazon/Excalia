/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Commandtpaall
extends EssentialsCommand {
    public Commandtpaall() {
        super("tpaall");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            if (sender.isPlayer()) {
                this.teleportAAllPlayers(server, sender, this.ess.getUser(sender.getPlayer()));
                return;
            }
            throw new NotEnoughArgumentsException();
        }
        User target = this.getPlayer(server, sender, args, 0);
        this.teleportAAllPlayers(server, sender, target);
    }

    private void teleportAAllPlayers(Server server, CommandSource sender, User target) {
        sender.sendMessage(I18n._("teleportAAll", new Object[0]));
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            User player = this.ess.getUser(onlinePlayer);
            if (target == player || !player.isTeleportEnabled() || sender.equals((Object)target.getBase()) && target.getWorld() != player.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !target.isAuthorized("essentials.worlds." + target.getWorld().getName())) continue;
            try {
                player.requestTeleport(target, true);
                player.sendMessage(I18n._("teleportHereRequest", target.getDisplayName()));
                player.sendMessage(I18n._("typeTpaccept", new Object[0]));
                if (this.ess.getSettings().getTpaAcceptCancellation() == 0) continue;
                player.sendMessage(I18n._("teleportRequestTimeoutInfo", this.ess.getSettings().getTpaAcceptCancellation()));
                continue;
            }
            catch (Exception ex) {
                this.ess.showError(sender, ex, this.getName());
            }
        }
    }
}

