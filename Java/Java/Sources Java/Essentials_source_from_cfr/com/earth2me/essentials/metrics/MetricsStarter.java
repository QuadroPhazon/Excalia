/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.metrics;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.metrics.Metrics;
import com.earth2me.essentials.perm.PermissionsHandler;
import com.earth2me.essentials.register.payment.Method;
import com.earth2me.essentials.register.payment.Methods;
import com.earth2me.essentials.register.payment.methods.VaultEco;
import com.earth2me.essentials.signs.EssentialsSign;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class MetricsStarter
implements Runnable {
    private final IEssentials ess;
    private transient Boolean start;

    public MetricsStarter(IEssentials plugin) {
        this.ess = plugin;
        try {
            Metrics metrics = new Metrics(this.ess);
            this.ess.setMetrics(metrics);
            if (!metrics.isOptOut()) {
                if (this.ess.getSettings().isMetricsEnabled()) {
                    this.start = true;
                } else {
                    this.ess.getLogger().info("This plugin collects minimal statistic data and sends it to http://metrics.essentials3.net.");
                    this.ess.getLogger().info("You can opt out, disabling metrics for all plugins, by running /essentials opt-out");
                    this.ess.getLogger().info("This will start 5 minutes after the first admin/op joins.");
                    this.start = false;
                }
                return;
            }
        }
        catch (Exception ex) {
            this.metricsError(ex);
        }
    }

    @Override
    public void run() {
        try {
            Metrics metrics = this.ess.getMetrics();
            Metrics.Graph moduleGraph = metrics.createGraph("Modules Used");
            for (Modules module : Modules.values()) {
                String moduleName = module.toString();
                if (!this.ess.getServer().getPluginManager().isPluginEnabled(moduleName)) continue;
                moduleGraph.addPlotter(new SimplePlotter(moduleName));
            }
            Metrics.Graph localeGraph = metrics.createGraph("Locale");
            localeGraph.addPlotter(new SimplePlotter(this.ess.getI18n().getCurrentLocale().getDisplayLanguage(Locale.ENGLISH)));
            Metrics.Graph featureGraph = metrics.createGraph("Features");
            featureGraph.addPlotter(new Metrics.Plotter("Unique Accounts"){

                @Override
                public int getValue() {
                    return MetricsStarter.this.ess.getUserMap().getUniqueUsers();
                }
            });
            featureGraph.addPlotter(new Metrics.Plotter("Jails"){

                @Override
                public int getValue() {
                    return MetricsStarter.this.ess.getJails().getCount();
                }
            });
            featureGraph.addPlotter(new Metrics.Plotter("Kits"){

                @Override
                public int getValue() {
                    ConfigurationSection kits = MetricsStarter.this.ess.getSettings().getKits();
                    if (kits == null) {
                        return 0;
                    }
                    return kits.getKeys(false).size();
                }
            });
            featureGraph.addPlotter(new Metrics.Plotter("Warps"){

                @Override
                public int getValue() {
                    return MetricsStarter.this.ess.getWarps().getCount();
                }
            });
            Metrics.Graph enabledGraph = metrics.createGraph("EnabledFeatures");
            enabledGraph.addPlotter(new SimplePlotter("Total"));
            String BKcommand = this.ess.getSettings().getBackupCommand();
            if (BKcommand != null && !"".equals(BKcommand)) {
                enabledGraph.addPlotter(new SimplePlotter("Backup"));
            }
            if (this.ess.getJails().getCount() > 0) {
                enabledGraph.addPlotter(new SimplePlotter("Jails"));
            }
            if (this.ess.getSettings().getKits() != null && this.ess.getSettings().getKits().getKeys(false).size() > 0) {
                enabledGraph.addPlotter(new SimplePlotter("Kits"));
            }
            if (this.ess.getWarps().getCount() > 0) {
                enabledGraph.addPlotter(new SimplePlotter("Warps"));
            }
            if (this.ess.getSettings().getTeleportCooldown() > 0.0) {
                enabledGraph.addPlotter(new SimplePlotter("TeleportCooldown"));
            }
            if (this.ess.getSettings().getTeleportDelay() > 0.0) {
                enabledGraph.addPlotter(new SimplePlotter("TeleportDelay"));
            }
            if (!this.ess.getSettings().areSignsDisabled()) {
                enabledGraph.addPlotter(new SimplePlotter("Signs"));
            }
            if (this.ess.getSettings().getAutoAfk() > 0) {
                enabledGraph.addPlotter(new SimplePlotter("AutoAFK"));
            }
            if (this.ess.getSettings().changePlayerListName()) {
                enabledGraph.addPlotter(new SimplePlotter("PlayerListName"));
            }
            if (this.ess.getSettings().getOperatorColor() != null) {
                enabledGraph.addPlotter(new SimplePlotter("OpColour"));
            }
            if (this.ess.getSettings().changeDisplayName()) {
                enabledGraph.addPlotter(new SimplePlotter("DisplayName"));
            }
            if (this.ess.getSettings().getChatRadius() >= 1) {
                enabledGraph.addPlotter(new SimplePlotter("LocalChat"));
            }
            Metrics.Graph depGraph = metrics.createGraph("Dependencies");
            this.ess.getPaymentMethod();
            Method method = Methods.getMethod();
            if (method != null) {
                String version;
                if (method instanceof VaultEco) {
                    version = ((VaultEco)method).getEconomy();
                } else {
                    version = method.getVersion();
                    int dashPosition = version.indexOf(45);
                    if (dashPosition > 0) {
                        version = version.substring(0, dashPosition);
                    }
                }
                depGraph.addPlotter(new SimplePlotter(method.getName() + " " + version));
            }
            depGraph.addPlotter(new SimplePlotter(this.ess.getPermissionsHandler().getName()));
            Metrics.Graph signGraph = metrics.createGraph("Signs");
            for (EssentialsSign sign : this.ess.getSettings().enabledSigns()) {
                signGraph.addPlotter(new SimplePlotter(sign.getName()));
            }
            metrics.start();
        }
        catch (Exception ex) {
            this.metricsError(ex);
        }
    }

    public void metricsError(Exception ex) {
        if (this.ess.getSettings().isDebug()) {
            this.ess.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage(), ex);
        } else {
            this.ess.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
        }
    }

    public Boolean getStart() {
        return this.start;
    }

    private class SimplePlotter
    extends Metrics.Plotter {
        public SimplePlotter(String name) {
            super(name);
        }

        @Override
        public int getValue() {
            return 1;
        }
    }

    private static enum Modules {
        Essentials,
        EssentialsAntiBuild,
        EssentialsAntiCheat,
        EssentialsChat,
        EssentialsSpawn,
        EssentialsProtect,
        EssentialsGeoIP,
        EssentialsXMPP;
        

        private Modules() {
        }
    }

}

