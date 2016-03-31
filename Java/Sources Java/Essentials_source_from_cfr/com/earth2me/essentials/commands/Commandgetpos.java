/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandgetpos
extends EssentialsCommand {
    public Commandgetpos() {
        super("getpos");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.getpos.others")) {
            User otherUser = this.getPlayer(server, user, args, 0);
            this.outputPosition(user.getSource(), otherUser.getLocation(), user.getLocation());
            return;
        }
        this.outputPosition(user.getSource(), user.getLocation(), null);
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User user = this.getPlayer(server, args, 0, true, false);
        this.outputPosition(sender, user.getLocation(), null);
    }

    private void outputPosition(CommandSource sender, Location coords, Location distance) {
        sender.sendMessage(I18n._("currentWorld", coords.getWorld().getName()));
        sender.sendMessage(I18n._("posX", coords.getBlockX()));
        sender.sendMessage(I18n._("posY", coords.getBlockY()));
        sender.sendMessage(I18n._("posZ", coords.getBlockZ()));
        sender.sendMessage(I18n._("posYaw", Float.valueOf((coords.getYaw() + 180.0f + 360.0f) % 360.0f)));
        sender.sendMessage(I18n._("posPitch", Float.valueOf(coords.getPitch())));
        if (distance != null && coords.getWorld().equals((Object)distance.getWorld())) {
            sender.sendMessage(I18n._("distance", coords.distance(distance)));
        }
    }
}

