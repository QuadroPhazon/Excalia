/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.PluginDisableEvent
 *  org.bukkit.event.server.PluginEnableEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials;

import com.earth2me.essentials.AlternativeCommandsHandler;
import com.earth2me.essentials.IConf;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.perm.PermissionsHandler;
import com.earth2me.essentials.register.payment.Methods;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EssentialsPluginListener
implements Listener,
IConf {
    private final transient IEssentials ess;

    public EssentialsPluginListener(IEssentials ess) {
        this.ess = ess;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("EssentialsChat")) {
            this.ess.getSettings().setEssentialsChatActive(true);
        }
        this.ess.getPermissionsHandler().checkPermissions();
        this.ess.getAlternativeCommandsHandler().addPlugin(event.getPlugin());
        if (!Methods.hasMethod() && Methods.setMethod(this.ess.getServer().getPluginManager())) {
            this.ess.getPaymentMethod();
            this.ess.getLogger().log(Level.INFO, "Payment method found (" + Methods.getMethod().getLongName() + " version: " + Methods.getMethod().getVersion() + ")");
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        PermissionsHandler permHandler;
        if (event.getPlugin().getName().equals("EssentialsChat")) {
            this.ess.getSettings().setEssentialsChatActive(false);
        }
        if ((permHandler = this.ess.getPermissionsHandler()) != null) {
            permHandler.checkPermissions();
        }
        this.ess.getAlternativeCommandsHandler().removePlugin(event.getPlugin());
        if (this.ess.getPaymentMethod() != null && Methods.hasMethod() && Methods.checkDisabled(event.getPlugin())) {
            this.ess.getPaymentMethod();
            Methods.reset();
            this.ess.getLogger().log(Level.INFO, "Payment method was disabled. No longer accepting payments.");
        }
    }

    @Override
    public void reloadConfig() {
        this.ess.getPermissionsHandler().setUseSuperperms(this.ess.getSettings().useBukkitPermissions());
        this.ess.getPermissionsHandler().checkPermissions();
    }
}

