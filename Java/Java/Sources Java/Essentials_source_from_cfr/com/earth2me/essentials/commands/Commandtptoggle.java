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
import com.earth2me.essentials.commands.EssentialsToggleCommand;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandtptoggle
extends EssentialsToggleCommand {
    public Commandtptoggle() {
        super("tptoggle", "essentials.tptoggle.others");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.toggleOtherPlayers(server, sender, args);
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length == 1) {
            Boolean toggle = this.matchToggleArgument(args[0]);
            if (toggle == null && user.isAuthorized(this.othersPermission)) {
                this.toggleOtherPlayers(server, user.getSource(), args);
            } else {
                this.togglePlayer(user.getSource(), user, toggle);
            }
        } else if (args.length == 2 && user.isAuthorized(this.othersPermission)) {
            this.toggleOtherPlayers(server, user.getSource(), args);
        } else {
            this.togglePlayer(user.getSource(), user, null);
        }
    }

    @Override
    void togglePlayer(CommandSource sender, User user, Boolean enabled) {
        if (enabled == null) {
            enabled = !user.isTeleportEnabled();
        }
        user.setTeleportEnabled(enabled);
        user.sendMessage(enabled != false ? I18n._("teleportationEnabled", new Object[0]) : I18n._("teleportationDisabled", new Object[0]));
        if (!sender.isPlayer() || !sender.getPlayer().equals((Object)user.getBase())) {
            sender.sendMessage(enabled != false ? I18n._("teleportationEnabledFor", user.getDisplayName()) : I18n._("teleportationDisabledFor", user.getDisplayName()));
        }
    }
}

