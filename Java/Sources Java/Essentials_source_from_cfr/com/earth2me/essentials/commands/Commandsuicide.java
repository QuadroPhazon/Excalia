/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;

public class Commandsuicide
extends EssentialsCommand {
    public Commandsuicide() {
        super("suicide");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        EntityDamageEvent ede = new EntityDamageEvent((Entity)user.getBase(), EntityDamageEvent.DamageCause.SUICIDE, 32767);
        server.getPluginManager().callEvent((Event)ede);
        user.damage(32767.0);
        if (user.getHealth() > 0.0) {
            user.setHealth(0.0);
        }
        user.sendMessage(I18n._("suicideMessage", new Object[0]));
        user.setDisplayNick();
        this.ess.broadcastMessage(user, I18n._("suicideSuccess", user.getDisplayName()));
    }
}

