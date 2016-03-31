/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Commandnear
extends EssentialsCommand {
    public Commandnear() {
        super("near");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        long maxRadius = this.ess.getSettings().getChatRadius();
        if (maxRadius == 0) {
            maxRadius = 200;
        }
        long radius = maxRadius;
        User otherUser = null;
        if (args.length > 0) {
            try {
                radius = Long.parseLong(args[0]);
            }
            catch (NumberFormatException e) {
                try {
                    otherUser = this.getPlayer(server, user, args, 0);
                }
                catch (Exception ex) {
                    // empty catch block
                }
            }
            if (args.length > 1 && otherUser != null) {
                try {
                    radius = Long.parseLong(args[1]);
                }
                catch (NumberFormatException e) {
                    // empty catch block
                }
            }
        }
        if ((radius = Math.abs(radius)) > maxRadius && !user.isAuthorized("essentials.near.maxexempt")) {
            user.sendMessage(I18n._("radiusTooBig", maxRadius));
            radius = maxRadius;
        }
        if (otherUser == null || !user.isAuthorized("essentials.near.others")) {
            otherUser = user;
        }
        user.sendMessage(I18n._("nearbyPlayers", this.getLocal(server, otherUser, radius)));
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length == 0) {
            throw new NotEnoughArgumentsException();
        }
        User otherUser = this.getPlayer(server, args, 0, true, false);
        long radius = 200;
        if (args.length > 1) {
            try {
                radius = Long.parseLong(args[1]);
            }
            catch (NumberFormatException e) {
                // empty catch block
            }
        }
        sender.sendMessage(I18n._("nearbyPlayers", this.getLocal(server, otherUser, radius)));
    }

    private String getLocal(Server server, User user, long radius) {
        Location loc = user.getLocation();
        World world = loc.getWorld();
        StringBuilder output = new StringBuilder();
        long radiusSquared = radius * radius;
        boolean showHidden = user.isAuthorized("essentials.vanish.interact");
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            long delta;
            Location playerLoc;
            User player = this.ess.getUser(onlinePlayer);
            if (player.equals(user) || player.isHidden() && !showHidden || (playerLoc = player.getLocation()).getWorld() != world || (delta = (long)playerLoc.distanceSquared(loc)) >= radiusSquared) continue;
            if (output.length() > 0) {
                output.append(", ");
            }
            output.append(player.getDisplayName()).append("\u00a7f(\u00a74").append((long)Math.sqrt(delta)).append("m\u00a7f)");
        }
        return output.length() > 1 ? output.toString() : I18n._("none", new Object[0]);
    }
}

