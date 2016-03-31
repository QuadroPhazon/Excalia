/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandtpa
extends EssentialsCommand {
    public Commandtpa() {
        super("tpa");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User player = this.getPlayer(server, user, args, 0);
        if (user.getName().equalsIgnoreCase(player.getName())) {
            throw new NotEnoughArgumentsException();
        }
        if (!player.isTeleportEnabled()) {
            throw new Exception(I18n._("teleportDisabled", player.getDisplayName()));
        }
        if (user.getWorld() != player.getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + player.getWorld().getName())) {
            throw new Exception(I18n._("noPerm", "essentials.worlds." + player.getWorld().getName()));
        }
        if (!player.isIgnoredPlayer(user)) {
            player.requestTeleport(user, false);
            player.sendMessage(I18n._("teleportRequest", user.getDisplayName()));
            player.sendMessage(I18n._("typeTpaccept", new Object[0]));
            player.sendMessage(I18n._("typeTpdeny", new Object[0]));
            if (this.ess.getSettings().getTpaAcceptCancellation() != 0) {
                player.sendMessage(I18n._("teleportRequestTimeoutInfo", this.ess.getSettings().getTpaAcceptCancellation()));
            }
        }
        user.sendMessage(I18n._("requestSent", player.getDisplayName()));
    }
}

