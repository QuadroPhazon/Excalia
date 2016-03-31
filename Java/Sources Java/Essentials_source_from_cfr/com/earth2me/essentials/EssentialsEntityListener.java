/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Ageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityCombustEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent$RegainReason
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class EssentialsEntityListener
implements Listener {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private static final transient Pattern powertoolPlayer = Pattern.compile("\\{player\\}");
    private final IEssentials ess;

    public EssentialsEntityListener(IEssentials ess) {
        this.ess = ess;
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        ProjectileSource shooter;
        Projectile projectile;
        Entity eAttack = event.getDamager();
        Entity eDefend = event.getEntity();
        if (eAttack instanceof Player) {
            ItemStack hand;
            User attacker = this.ess.getUser((Player)eAttack);
            if (eDefend instanceof Player) {
                this.onPlayerVsPlayerDamage(event, (Player)eDefend, attacker);
            } else if (eDefend instanceof Ageable && (hand = attacker.getItemInHand()) != null && hand.getType() == Material.MILK_BUCKET) {
                ((Ageable)eDefend).setBaby();
                hand.setType(Material.BUCKET);
                attacker.setItemInHand(hand);
                attacker.updateInventory();
                event.setCancelled(true);
            }
            attacker.updateActivity(true);
        } else if (eAttack instanceof Projectile && eDefend instanceof Player && (shooter = (projectile = (Projectile)event.getDamager()).getShooter()) instanceof Player) {
            User attacker = this.ess.getUser((Player)shooter);
            this.onPlayerVsPlayerDamage(event, (Player)eDefend, attacker);
            attacker.updateActivity(true);
        }
    }

    private void onPlayerVsPlayerDamage(EntityDamageByEntityEvent event, Player defender, User attacker) {
        if (this.ess.getSettings().getLoginAttackDelay() > 0 && System.currentTimeMillis() < attacker.getLastLogin() + this.ess.getSettings().getLoginAttackDelay() && !attacker.isAuthorized("essentials.pvpdelay.exempt")) {
            event.setCancelled(true);
        }
        if (!defender.equals((Object)attacker.getBase()) && (attacker.hasInvulnerabilityAfterTeleport() || this.ess.getUser(defender).hasInvulnerabilityAfterTeleport())) {
            event.setCancelled(true);
        }
        if (attacker.isGodModeEnabled() && !attacker.isAuthorized("essentials.god.pvp")) {
            event.setCancelled(true);
        }
        if (attacker.isHidden() && !attacker.isAuthorized("essentials.vanish.pvp")) {
            event.setCancelled(true);
        }
        this.onPlayerVsPlayerPowertool(event, defender, attacker);
    }

    private void onPlayerVsPlayerPowertool(EntityDamageByEntityEvent event, Player defender, final User attacker) {
        List<String> commandList = attacker.getPowertool(attacker.getItemInHand());
        if (commandList != null && !commandList.isEmpty()) {
            for (String tempCommand : commandList) {
                final String command = powertoolPlayer.matcher(tempCommand).replaceAll(defender.getName());
                if (command == null || command.isEmpty() || command.equals(tempCommand)) continue;
                this.ess.scheduleSyncDelayedTask(new Runnable(){

                    @Override
                    public void run() {
                        attacker.getServer().dispatchCommand((CommandSender)attacker.getBase(), command);
                        LOGGER.log(Level.INFO, String.format("[PT] %s issued server command: /%s", attacker.getName(), command));
                    }
                });
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && this.ess.getUser((Player)event.getEntity()).isGodModeEnabled()) {
            Player player = (Player)event.getEntity();
            player.setFireTicks(0);
            player.setRemainingAir(player.getMaximumAir());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onEntityCombust(EntityCombustEvent event) {
        if (event.getEntity() instanceof Player && this.ess.getUser((Player)event.getEntity()).isGodModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        User user = this.ess.getUser(event.getEntity());
        if (user.isAuthorized("essentials.back.ondeath") && !this.ess.getSettings().isCommandDisabled("back")) {
            user.setLastLocation();
            user.sendMessage(I18n._("backAfterDeath", new Object[0]));
        }
        if (!this.ess.getSettings().areDeathMessagesEnabled()) {
            event.setDeathMessage("");
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerDeathExpEvent(PlayerDeathEvent event) {
        User user = this.ess.getUser(event.getEntity());
        if (user.isAuthorized("essentials.keepxp")) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        User user;
        if (event.getEntity() instanceof Player && (user = this.ess.getUser((Player)event.getEntity())).isGodModeEnabled()) {
            if (user.isGodModeEnabledRaw()) {
                user.setFoodLevel(20);
                user.setSaturation(10.0f);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED && event.getEntity() instanceof Player && this.ess.getUser((Player)event.getEntity()).isAfk() && this.ess.getSettings().getFreezeAfkPlayers()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onPotionSplashEvent(PotionSplashEvent event) {
        for (LivingEntity entity : event.getAffectedEntities()) {
            if (!(entity instanceof Player) || !this.ess.getUser((Player)entity).isGodModeEnabled()) continue;
            event.setIntensity(entity, 0.0);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onEntityShootBow(EntityShootBowEvent event) {
        User user;
        if (event.getEntity() instanceof Player && (user = this.ess.getUser((Player)event.getEntity())).isAfk()) {
            user.updateActivity(true);
        }
    }

}

