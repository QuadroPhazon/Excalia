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
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandext
extends EssentialsLoopCommand {
    public Commandext() {
        super("ext");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        this.loopOnlinePlayers(server, sender, true, true, args[0], null);
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            this.extPlayer(user.getBase());
            user.sendMessage(I18n._("extinguish", new Object[0]));
            return;
        }
        this.loopOnlinePlayers(server, user.getSource(), true, true, args[0], null);
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User player, String[] args) {
        this.extPlayer(player.getBase());
        sender.sendMessage(I18n._("extinguishOthers", player.getDisplayName()));
    }

    private void extPlayer(Player player) {
        player.setFireTicks(0);
    }
}

