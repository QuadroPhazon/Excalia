/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.utils.LocationUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandtop
extends EssentialsCommand {
    public Commandtop() {
        super("top");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        int topX = user.getLocation().getBlockX();
        int topZ = user.getLocation().getBlockZ();
        float pitch = user.getLocation().getPitch();
        float yaw = user.getLocation().getYaw();
        Location loc = LocationUtil.getSafeDestination(new Location(user.getWorld(), (double)topX, (double)user.getWorld().getMaxHeight(), (double)topZ, yaw, pitch));
        user.getTeleport().teleport(loc, new Trade(this.getName(), this.ess), PlayerTeleportEvent.TeleportCause.COMMAND);
        user.sendMessage(I18n._("teleportTop", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }
}

