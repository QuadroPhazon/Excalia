/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerExemptException;
import com.earth2me.essentials.commands.QuietAbortException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.PluginManager;

public class Commandfeed
extends EssentialsLoopCommand {
    public Commandfeed() {
        super("feed");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (!user.isAuthorized("essentials.feed.cooldown.bypass")) {
            user.healCooldown();
        }
        if (args.length > 0 && user.isAuthorized("essentials.feed.others")) {
            this.loopOnlinePlayers(server, user.getSource(), true, true, args[0], null);
            return;
        }
        this.feedPlayer(user.getBase());
        user.sendMessage(I18n._("feed", new Object[0]));
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.loopOnlinePlayers(server, sender, true, true, args[0], null);
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User player, String[] args) throws PlayerExemptException {
        try {
            this.feedPlayer(player.getBase());
            sender.sendMessage(I18n._("feedOther", player.getDisplayName()));
        }
        catch (QuietAbortException e) {
            // empty catch block
        }
    }

    private void feedPlayer(Player player) throws QuietAbortException {
        int amount = 30;
        FoodLevelChangeEvent flce = new FoodLevelChangeEvent((HumanEntity)player, 30);
        this.ess.getServer().getPluginManager().callEvent((Event)flce);
        if (flce.isCancelled()) {
            throw new QuietAbortException();
        }
        player.setFoodLevel(flce.getFoodLevel() > 20 ? 20 : flce.getFoodLevel());
        player.setSaturation(10.0f);
        player.setExhaustion(0.0f);
    }
}

