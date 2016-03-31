/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent$RegainReason
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerExemptException;
import com.earth2me.essentials.commands.QuietAbortException;
import java.util.Collection;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Commandheal
extends EssentialsLoopCommand {
    public Commandheal() {
        super("heal");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (!user.isAuthorized("essentials.heal.cooldown.bypass")) {
            user.healCooldown();
        }
        if (args.length > 0 && user.isAuthorized("essentials.heal.others")) {
            this.loopOnlinePlayers(server, user.getSource(), true, true, args[0], null);
            return;
        }
        this.healPlayer(user);
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.loopOnlinePlayers(server, sender, true, true, args[0], null);
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User player, String[] args) throws PlayerExemptException {
        try {
            this.healPlayer(player);
            sender.sendMessage(I18n._("healOther", player.getDisplayName()));
        }
        catch (QuietAbortException e) {
            // empty catch block
        }
    }

    private void healPlayer(User user) throws PlayerExemptException, QuietAbortException {
        Player player = user.getBase();
        if (player.getHealth() == 0.0) {
            throw new PlayerExemptException(I18n._("healDead", new Object[0]));
        }
        double amount = player.getMaxHealth() - player.getHealth();
        EntityRegainHealthEvent erhe = new EntityRegainHealthEvent((Entity)player, amount, EntityRegainHealthEvent.RegainReason.CUSTOM);
        this.ess.getServer().getPluginManager().callEvent((Event)erhe);
        if (erhe.isCancelled()) {
            throw new QuietAbortException();
        }
        double newAmount = player.getHealth() + erhe.getAmount();
        if (newAmount > player.getMaxHealth()) {
            newAmount = player.getMaxHealth();
        }
        player.setHealth(newAmount);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        user.sendMessage(I18n._("heal", new Object[0]));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
}

