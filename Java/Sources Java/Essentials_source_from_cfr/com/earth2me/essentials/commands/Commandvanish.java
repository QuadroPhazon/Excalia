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
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandvanish
extends EssentialsToggleCommand {
    public Commandvanish() {
        super("vanish", "essentials.vanish.others");
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
    void togglePlayer(CommandSource sender, User user, Boolean enabled) throws NotEnoughArgumentsException {
        if (enabled == null) {
            enabled = !user.isVanished();
        }
        user.setVanished(enabled);
        Object[] arrobject = new Object[2];
        arrobject[0] = user.getDisplayName();
        arrobject[1] = enabled != false ? I18n._("enabled", new Object[0]) : I18n._("disabled", new Object[0]);
        user.sendMessage(I18n._("vanish", arrobject));
        if (enabled.booleanValue()) {
            user.sendMessage(I18n._("vanished", new Object[0]));
        }
        if (!sender.isPlayer() || !sender.getPlayer().equals((Object)user.getBase())) {
            Object[] arrobject2 = new Object[2];
            arrobject2[0] = user.getDisplayName();
            arrobject2[1] = enabled != false ? I18n._("enabled", new Object[0]) : I18n._("disabled", new Object[0]);
            sender.sendMessage(I18n._("vanish", arrobject2));
        }
    }
}

