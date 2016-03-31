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

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerExemptException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;

public class Commandkill
extends EssentialsLoopCommand {
    public Commandkill() {
        super("kill");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.loopOnlinePlayers(server, sender, true, true, args[0], null);
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User user, String[] args) throws PlayerExemptException {
        Player matchPlayer = user.getBase();
        if (sender.isPlayer() && user.isAuthorized("essentials.kill.exempt") && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.kill.force")) {
            throw new PlayerExemptException(I18n._("killExempt", matchPlayer.getDisplayName()));
        }
        EntityDamageEvent ede = new EntityDamageEvent((Entity)matchPlayer, sender.isPlayer() && sender.getPlayer().getName().equals(matchPlayer.getName()) ? EntityDamageEvent.DamageCause.SUICIDE : EntityDamageEvent.DamageCause.CUSTOM, 32767);
        server.getPluginManager().callEvent((Event)ede);
        if (ede.isCancelled() && sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.kill.force")) {
            return;
        }
        matchPlayer.damage(32767.0);
        if (matchPlayer.getHealth() > 0.0) {
            matchPlayer.setHealth(0.0);
        }
        sender.sendMessage(I18n._("kill", matchPlayer.getDisplayName()));
    }
}

