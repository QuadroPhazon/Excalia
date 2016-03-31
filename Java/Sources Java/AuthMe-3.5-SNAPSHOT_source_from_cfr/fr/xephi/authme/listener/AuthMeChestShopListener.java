/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.Acrobot.ChestShop.Events.PreTransactionEvent
 *  com.Acrobot.ChestShop.Events.PreTransactionEvent$TransactionOutcome
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package fr.xephi.authme.listener;

import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AuthMeChestShopListener
implements Listener {
    public DataSource database;
    public AuthMe plugin;

    public AuthMeChestShopListener(DataSource database, AuthMe plugin) {
        this.database = database;
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPreTransaction(PreTransactionEvent event) {
        if (event.isCancelled() || event.getClient() == null || event == null) {
            return;
        }
        Player player = event.getClient();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.database.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(PreTransactionEvent.TransactionOutcome.OTHER);
    }
}

