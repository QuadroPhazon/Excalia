/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsToggleCommand;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.events.GodStatusChangeEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

public class Commandgod
extends EssentialsToggleCommand {
    public Commandgod() {
        super("god", "essentials.god.others");
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
            enabled = !user.isGodModeEnabled();
        }
        User controller = sender.isPlayer() ? this.ess.getUser(sender.getPlayer()) : null;
        GodStatusChangeEvent godEvent = new GodStatusChangeEvent(controller, user, enabled);
        this.ess.getServer().getPluginManager().callEvent((Event)godEvent);
        if (!godEvent.isCancelled()) {
            user.setGodModeEnabled(enabled);
            if (enabled.booleanValue() && user.getHealth() != 0.0) {
                user.setHealth(user.getMaxHealth());
                user.setFoodLevel(20);
            }
            Object[] arrobject = new Object[1];
            arrobject[0] = enabled != false ? I18n._("enabled", new Object[0]) : I18n._("disabled", new Object[0]);
            user.sendMessage(I18n._("godMode", arrobject));
            if (!sender.isPlayer() || !sender.getPlayer().equals((Object)user.getBase())) {
                Object[] arrobject2 = new Object[1];
                arrobject2[0] = I18n._(enabled != false ? "godEnabledFor" : "godDisabledFor", user.getDisplayName());
                sender.sendMessage(I18n._("godMode", arrobject2));
            }
        }
    }
}

