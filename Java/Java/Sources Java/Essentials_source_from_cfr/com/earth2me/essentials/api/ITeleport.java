/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.api;

import com.earth2me.essentials.Trade;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public interface ITeleport {
    public void now(Location var1, boolean var2, PlayerTeleportEvent.TeleportCause var3) throws Exception;

    public void now(Player var1, boolean var2, PlayerTeleportEvent.TeleportCause var3) throws Exception;

    @Deprecated
    public void teleport(Location var1, Trade var2) throws Exception;

    public void teleport(Location var1, Trade var2, PlayerTeleportEvent.TeleportCause var3) throws Exception;

    public void teleport(Player var1, Trade var2, PlayerTeleportEvent.TeleportCause var3) throws Exception;

    public void teleportPlayer(IUser var1, Location var2, Trade var3, PlayerTeleportEvent.TeleportCause var4) throws Exception;

    public void teleportPlayer(IUser var1, Player var2, Trade var3, PlayerTeleportEvent.TeleportCause var4) throws Exception;

    public void respawn(Trade var1, PlayerTeleportEvent.TeleportCause var2) throws Exception;

    public void warp(IUser var1, String var2, Trade var3, PlayerTeleportEvent.TeleportCause var4) throws Exception;

    public void back(Trade var1) throws Exception;

    public void back() throws Exception;
}

