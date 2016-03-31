/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 */
package fr.xephi.authme.listener;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AuthMeBlockListener
implements Listener {
    private DataSource data;
    public AuthMe instance;

    public AuthMeBlockListener(DataSource data, AuthMe instance) {
        this.data = data;
        this.instance = instance;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled() || event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(player.getName())) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }
}

