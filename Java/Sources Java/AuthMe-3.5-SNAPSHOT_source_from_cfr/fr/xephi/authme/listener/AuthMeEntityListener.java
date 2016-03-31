/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityInteractEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 */
package fr.xephi.authme.listener;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.plugin.manager.CitizensCommunicator;
import fr.xephi.authme.plugin.manager.CombatTagComunicator;
import fr.xephi.authme.settings.Settings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class AuthMeEntityListener
implements Listener {
    private DataSource data;
    public AuthMe instance;

    public AuthMeEntityListener(DataSource data, AuthMe instance) {
        this.data = data;
        this.instance = instance;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        if (Utils.getInstance().isUnrestricted((Player)entity)) {
            return;
        }
        if (this.instance.citizens.isNPC(entity, this.instance)) {
            return;
        }
        Player player = (Player)entity;
        String name = player.getName();
        if (CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        player.setFireTicks(0);
        event.setDamage(0.0);
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getTarget() == null) {
            return;
        }
        Entity entity = event.getTarget();
        if (!(entity instanceof Player)) {
            return;
        }
        if (this.instance.citizens.isNPC(entity, this.instance)) {
            return;
        }
        Player player = (Player)entity;
        String name = player.getName();
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setTarget(null);
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        HumanEntity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        if (this.instance.citizens.isNPC((Entity)entity, this.instance)) {
            return;
        }
        Player player = (Player)entity;
        String name = player.getName();
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void EntityRegainHealthEvent(EntityRegainHealthEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        if (this.instance.citizens.isNPC(entity, this.instance)) {
            return;
        }
        Player player = (Player)entity;
        String name = player.getName();
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            return;
        }
        if (!this.data.isAuthAvailable(name) && !Settings.isForcedRegistrationEnabled.booleanValue()) {
            return;
        }
        event.setAmount(0.0);
        event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.isCancelled() || event == null) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (this.instance.citizens.isNPC((Entity)player, this.instance)) {
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

    @EventHandler(priority=EventPriority.LOWEST)
    public void onLowestEntityInteract(EntityInteractEvent event) {
        if (event.isCancelled() || event == null) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        String name = player.getName();
        if (Utils.getInstance().isUnrestricted(player) || CombatTagComunicator.isNPC((Entity)player)) {
            return;
        }
        if (this.instance.citizens.isNPC((Entity)player, this.instance)) {
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

