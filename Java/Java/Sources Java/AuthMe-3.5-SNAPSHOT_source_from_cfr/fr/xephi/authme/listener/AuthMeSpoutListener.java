/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent
 *  org.getspout.spoutapi.gui.InGameHUD
 *  org.getspout.spoutapi.gui.PopupScreen
 *  org.getspout.spoutapi.player.SpoutPlayer
 */
package fr.xephi.authme.listener;

import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.gui.screens.LoginScreen;
import fr.xephi.authme.settings.SpoutCfg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.InGameHUD;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AuthMeSpoutListener
implements Listener {
    private DataSource data;

    public AuthMeSpoutListener(DataSource data) {
        this.data = data;
    }

    @EventHandler
    public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
        if (SpoutCfg.getInstance().getBoolean("LoginScreen.enabled") && this.data.isAuthAvailable(event.getPlayer().getName()) && !PlayerCache.getInstance().isAuthenticated(event.getPlayer().getName())) {
            event.getPlayer().getMainScreen().attachPopupScreen((PopupScreen)new LoginScreen(event.getPlayer()));
        }
    }
}

