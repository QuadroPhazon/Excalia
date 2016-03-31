/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockDamageEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.IJails;
import net.ess3.api.ITeleport;
import net.ess3.api.IUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Jails
extends AsyncStorageObjectHolder<com.earth2me.essentials.settings.Jails>
implements IJails {
    private static final transient Logger LOGGER = Bukkit.getLogger();
    private static transient boolean enabled = false;

    public Jails(IEssentials ess) {
        super(ess, com.earth2me.essentials.settings.Jails.class);
        this.reloadConfig();
    }

    private void registerListeners() {
        enabled = true;
        PluginManager pluginManager = this.ess.getServer().getPluginManager();
        JailListener blockListener = new JailListener();
        pluginManager.registerEvents((Listener)blockListener, (Plugin)this.ess);
        if (this.ess.getSettings().isDebug()) {
            LOGGER.log(Level.INFO, "Registering Jail listener");
        }
    }

    @Override
    public File getStorageFile() {
        return new File(this.ess.getDataFolder(), "jail.yml");
    }

    @Override
    public void finishRead() {
        this.checkRegister();
    }

    @Override
    public void finishWrite() {
        this.checkRegister();
    }

    public void resetListener() {
        enabled = false;
        this.checkRegister();
    }

    private void checkRegister() {
        if (!enabled && this.getCount() > 0) {
            this.registerListeners();
        }
    }

    @Override
    public Location getJail(String jailName) throws Exception {
        this.acquireReadLock();
        try {
            if (((com.earth2me.essentials.settings.Jails)this.getData()).getJails() == null || jailName == null || !((com.earth2me.essentials.settings.Jails)this.getData()).getJails().containsKey(jailName.toLowerCase(Locale.ENGLISH))) {
                throw new Exception(I18n._("jailNotExist", new Object[0]));
            }
            Location loc = ((com.earth2me.essentials.settings.Jails)this.getData()).getJails().get(jailName.toLowerCase(Locale.ENGLISH));
            if (loc == null || loc.getWorld() == null) {
                throw new Exception(I18n._("jailNotExist", new Object[0]));
            }
            Location location = loc;
            return location;
        }
        finally {
            this.unlock();
        }
    }

    @Override
    public Collection<String> getList() throws Exception {
        this.acquireReadLock();
        try {
            if (((com.earth2me.essentials.settings.Jails)this.getData()).getJails() == null) {
                List<String> list = Collections.emptyList();
                return list;
            }
            ArrayList<String> arrayList = new ArrayList<String>(((com.earth2me.essentials.settings.Jails)this.getData()).getJails().keySet());
            return arrayList;
        }
        finally {
            this.unlock();
        }
    }

    @Override
    public void removeJail(String jail) throws Exception {
        this.acquireWriteLock();
        try {
            if (((com.earth2me.essentials.settings.Jails)this.getData()).getJails() == null) {
                return;
            }
            ((com.earth2me.essentials.settings.Jails)this.getData()).getJails().remove(jail.toLowerCase(Locale.ENGLISH));
        }
        finally {
            this.unlock();
        }
    }

    @Override
    public void sendToJail(IUser user, String jail) throws Exception {
        this.acquireReadLock();
        try {
            if (user.getBase().isOnline()) {
                Location loc = this.getJail(jail);
                user.getTeleport().now(loc, false, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            user.setJail(jail);
        }
        finally {
            this.unlock();
        }
    }

    @Override
    public void setJail(String jailName, Location loc) throws Exception {
        this.acquireWriteLock();
        try {
            if (((com.earth2me.essentials.settings.Jails)this.getData()).getJails() == null) {
                ((com.earth2me.essentials.settings.Jails)this.getData()).setJails(new HashMap<String, Location>());
            }
            ((com.earth2me.essentials.settings.Jails)this.getData()).getJails().put(jailName.toLowerCase(Locale.ENGLISH), loc);
        }
        finally {
            this.unlock();
        }
    }

    @Override
    public int getCount() {
        try {
            return this.getList().size();
        }
        catch (Exception ex) {
            return 0;
        }
    }

    private class JailListener
    implements Listener {
        private JailListener() {
        }

        @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
        public void onBlockBreak(BlockBreakEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            if (user.isJailed()) {
                event.setCancelled(true);
            }
        }

        @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
        public void onBlockPlace(BlockPlaceEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            if (user.isJailed()) {
                event.setCancelled(true);
            }
        }

        @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
        public void onBlockDamage(BlockDamageEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            if (user.isJailed()) {
                event.setCancelled(true);
            }
        }

        @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
        public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
            User user;
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getEntity().getType() != EntityType.PLAYER) {
                return;
            }
            Entity damager = event.getDamager();
            if (damager.getType() == EntityType.PLAYER && (user = Jails.this.ess.getUser((Player)damager)) != null && user.isJailed()) {
                event.setCancelled(true);
            }
        }

        @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
        public void onPlayerInteract(PlayerInteractEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            if (user.isJailed()) {
                event.setCancelled(true);
            }
        }

        @EventHandler(priority=EventPriority.HIGHEST)
        public void onPlayerRespawn(PlayerRespawnEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty()) {
                return;
            }
            try {
                event.setRespawnLocation(Jails.this.getJail(user.getJail()));
            }
            catch (Exception ex) {
                if (Jails.this.ess.getSettings().isDebug()) {
                    LOGGER.log(Level.INFO, I18n._("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
                }
                LOGGER.log(Level.INFO, I18n._("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
            }
        }

        @EventHandler(priority=EventPriority.HIGH)
        public void onPlayerTeleport(PlayerTeleportEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty()) {
                return;
            }
            try {
                event.setTo(Jails.this.getJail(user.getJail()));
            }
            catch (Exception ex) {
                if (Jails.this.ess.getSettings().isDebug()) {
                    LOGGER.log(Level.INFO, I18n._("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
                }
                LOGGER.log(Level.INFO, I18n._("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
            }
            user.sendMessage(I18n._("jailMessage", new Object[0]));
        }

        @EventHandler(priority=EventPriority.HIGHEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            User user = Jails.this.ess.getUser(event.getPlayer());
            long currentTime = System.currentTimeMillis();
            user.checkJailTimeout(currentTime);
            if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty()) {
                return;
            }
            try {
                Jails.this.sendToJail(user, user.getJail());
            }
            catch (Exception ex) {
                if (Jails.this.ess.getSettings().isDebug()) {
                    LOGGER.log(Level.INFO, I18n._("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
                }
                LOGGER.log(Level.INFO, I18n._("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
            }
            user.sendMessage(I18n._("jailMessage", new Object[0]));
        }
    }

}

