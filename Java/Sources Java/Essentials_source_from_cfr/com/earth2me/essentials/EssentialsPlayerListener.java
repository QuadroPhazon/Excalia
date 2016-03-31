/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerEggThrowEvent
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials;

import com.earth2me.essentials.Backup;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.TextInput;
import com.earth2me.essentials.textreader.TextPager;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.LocationUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

public class EssentialsPlayerListener
implements Listener {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private final transient IEssentials ess;

    public EssentialsPlayerListener(IEssentials parent) {
        this.ess = parent;
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        User user = this.ess.getUser(event.getPlayer());
        this.updateCompass(user);
        user.setDisplayNick();
        if (this.ess.getSettings().isTeleportInvulnerability()) {
            user.enableInvulnerabilityAfterTeleport();
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        User user = this.ess.getUser(event.getPlayer());
        if (user.isMuted()) {
            event.setCancelled(true);
            user.sendMessage(I18n._("voiceSilenced", new Object[0]));
            LOGGER.info(I18n._("mutedUserSpeaks", user.getName()));
        }
        try {
            Iterator it = event.getRecipients().iterator();
            while (it.hasNext()) {
                User u = this.ess.getUser((Player)it.next());
                if (!u.isIgnoredPlayer(user)) continue;
                it.remove();
            }
        }
        catch (UnsupportedOperationException ex) {
            if (this.ess.getSettings().isDebug()) {
                this.ess.getLogger().log(Level.INFO, "Ignore could not block chat due to custom chat plugin event.", ex);
            }
            this.ess.getLogger().info("Ignore could not block chat due to custom chat plugin event.");
        }
        user.updateActivity(true);
        user.setDisplayNick();
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=1)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY()) {
            return;
        }
        if (!this.ess.getSettings().cancelAfkOnMove() && !this.ess.getSettings().getFreezeAfkPlayers()) {
            event.getHandlers().unregister((Listener)this);
            if (this.ess.getSettings().isDebug()) {
                LOGGER.log(Level.INFO, "Unregistering move listener");
            }
            return;
        }
        User user = this.ess.getUser(event.getPlayer());
        if (user.isAfk() && this.ess.getSettings().getFreezeAfkPlayers()) {
            Location from = event.getFrom();
            Location origTo = event.getTo();
            Location to = origTo.clone();
            if (this.ess.getSettings().cancelAfkOnMove() && origTo.getY() >= (double)(from.getBlockY() + 1)) {
                user.updateActivity(true);
                return;
            }
            to.setX(from.getX());
            to.setY(from.getY());
            to.setZ(from.getZ());
            try {
                event.setTo(LocationUtil.getSafeDestination(to));
            }
            catch (Exception ex) {
                event.setTo(to);
            }
            return;
        }
        Location afk = user.getAfkPosition();
        if (afk == null || !event.getTo().getWorld().equals((Object)afk.getWorld()) || afk.distanceSquared(event.getTo()) > 9.0) {
            user.updateActivity(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        User user = this.ess.getUser(event.getPlayer());
        if (this.ess.getSettings().allowSilentJoinQuit() && user.isAuthorized("essentials.silentquit")) {
            event.setQuitMessage(null);
        } else if (this.ess.getSettings().isCustomQuitMessage() && event.getQuitMessage() != null) {
            Player player = event.getPlayer();
            event.setQuitMessage(this.ess.getSettings().getCustomQuitMessage().replace("{PLAYER}", player.getDisplayName()).replace("{USERNAME}", player.getName()));
        }
        if (this.ess.getSettings().removeGodOnDisconnect() && user.isGodModeEnabled()) {
            user.setGodModeEnabled(false);
        }
        if (user.isVanished()) {
            user.setVanished(false);
        }
        user.setLogoutLocation();
        if (user.isRecipeSee()) {
            user.getBase().getOpenInventory().getTopInventory().clear();
        }
        user.updateActivity(false);
        user.dispose();
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final String joinMessage = event.getJoinMessage();
        this.ess.runTaskAsynchronously(new Runnable(){

            @Override
            public void run() {
                EssentialsPlayerListener.this.delayedJoin(event.getPlayer(), joinMessage);
            }
        });
        if (this.ess.getSettings().allowSilentJoinQuit() || this.ess.getSettings().isCustomJoinMessage()) {
            event.setJoinMessage(null);
        }
    }

    public void delayedJoin(final Player player, final String message) {
        if (!player.isOnline()) {
            return;
        }
        this.ess.getBackup().onPlayerJoin();
        final User user = this.ess.getUser(player);
        if (user.isNPC()) {
            user.setNPC(false);
        }
        final long currentTime = System.currentTimeMillis();
        user.checkMuteTimeout(currentTime);
        user.updateActivity(false);
        this.ess.scheduleSyncDelayedTask(new Runnable(){

            @Override
            public void run() {
                if (!user.isOnline()) {
                    return;
                }
                user.trackUUID();
                user.setLastLogin(currentTime);
                user.setDisplayNick();
                EssentialsPlayerListener.this.updateCompass(user);
                if (!EssentialsPlayerListener.this.ess.getVanishedPlayers().isEmpty() && !user.isAuthorized("essentials.vanish.see")) {
                    for (String p : EssentialsPlayerListener.this.ess.getVanishedPlayers()) {
                        Player toVanish = EssentialsPlayerListener.this.ess.getServer().getPlayerExact(p);
                        if (toVanish == null || !toVanish.isOnline()) continue;
                        user.hidePlayer(toVanish);
                    }
                }
                if (user.isAuthorized("essentials.sleepingignored")) {
                    user.setSleepingIgnored(true);
                }
                if (!(EssentialsPlayerListener.this.ess.getSettings().allowSilentJoinQuit() && user.isAuthorized("essentials.silentjoin") || message == null)) {
                    if (EssentialsPlayerListener.this.ess.getSettings().isCustomJoinMessage()) {
                        EssentialsPlayerListener.this.ess.getServer().broadcastMessage(EssentialsPlayerListener.this.ess.getSettings().getCustomJoinMessage().replace("{PLAYER}", player.getDisplayName()).replace("{USERNAME}", player.getName()));
                    } else if (EssentialsPlayerListener.this.ess.getSettings().allowSilentJoinQuit()) {
                        EssentialsPlayerListener.this.ess.getServer().broadcastMessage(message);
                    }
                }
                if (!EssentialsPlayerListener.this.ess.getSettings().isCommandDisabled("motd") && user.isAuthorized("essentials.motd")) {
                    try {
                        TextInput input = new TextInput(user.getSource(), "motd", true, EssentialsPlayerListener.this.ess);
                        KeywordReplacer output = new KeywordReplacer(input, user.getSource(), EssentialsPlayerListener.this.ess);
                        TextPager pager = new TextPager(output, true);
                        pager.showPage("1", null, "motd", user.getSource());
                    }
                    catch (IOException ex) {
                        if (EssentialsPlayerListener.this.ess.getSettings().isDebug()) {
                            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
                        }
                        LOGGER.log(Level.WARNING, ex.getMessage());
                    }
                }
                if (!EssentialsPlayerListener.this.ess.getSettings().isCommandDisabled("mail") && user.isAuthorized("essentials.mail")) {
                    List<String> mail = user.getMails();
                    if (mail.isEmpty()) {
                        user.sendMessage(I18n._("noNewMail", new Object[0]));
                    } else {
                        user.sendMessage(I18n._("youHaveNewMail", mail.size()));
                    }
                }
                if (user.isAuthorized("essentials.fly.safelogin")) {
                    user.setFallDistance(0.0f);
                    if (LocationUtil.shouldFly(user.getLocation())) {
                        user.setAllowFlight(true);
                        user.setFlying(true);
                        user.sendMessage(I18n._("flyMode", I18n._("enabled", new Object[0]), user.getDisplayName()));
                    }
                }
                user.setFlySpeed(0.1f);
                user.setWalkSpeed(0.2f);
            }
        });
    }

    private void updateCompass(User user) {
        Location loc = user.getHome(user.getLocation());
        if (loc == null) {
            loc = user.getBedSpawnLocation();
        }
        if (loc != null) {
            Location updateLoc = loc;
            user.setCompassTarget(updateLoc);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerLogin2(PlayerLoginEvent event) {
        switch (event.getResult()) {
            case KICK_BANNED: {
                break;
            }
            default: {
                return;
            }
        }
        String banReason = I18n._("banFormat", I18n._("defaultBanReason", new Object[0]), "Console");
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, banReason);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        boolean banExpired;
        switch (event.getResult()) {
            case KICK_BANNED: 
            case KICK_FULL: {
                break;
            }
            default: {
                return;
            }
        }
        User user = this.ess.getUser(event.getPlayer());
        if ((event.getResult() == PlayerLoginEvent.Result.KICK_BANNED || user.isBanned()) && !(banExpired = user.checkBanTimeout(System.currentTimeMillis()))) {
            String banReason = user.getBanReason();
            if (banReason == null || banReason.isEmpty() || banReason.equalsIgnoreCase("ban")) {
                banReason = event.getKickMessage();
            }
            if (user.getBanTimeout() > 0) {
                banReason = banReason + "\n\nExpires in " + DateUtil.formatDateDiff(user.getBanTimeout());
            }
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, banReason);
            return;
        }
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL && !user.isAuthorized("essentials.joinfullserver")) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, I18n._("serverFull", new Object[0]));
            return;
        }
        event.allow();
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=1)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        boolean backListener = this.ess.getSettings().registerBackInListener();
        boolean teleportInvulnerability = this.ess.getSettings().isTeleportInvulnerability();
        if (backListener || teleportInvulnerability) {
            User user = this.ess.getUser(event.getPlayer());
            if (backListener && (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND)) {
                user.setLastLocation();
            }
            if (teleportInvulnerability && (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND)) {
                user.enableInvulnerabilityAfterTeleport();
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=1)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        ItemStack stack;
        User user = this.ess.getUser(event.getPlayer());
        if (user.hasUnlimited(stack = new ItemStack(Material.EGG, 1))) {
            user.getInventory().addItem(new ItemStack[]{stack});
            user.updateInventory();
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=1)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        final User user = this.ess.getUser(event.getPlayer());
        if (user.hasUnlimited(new ItemStack(event.getBucket()))) {
            event.getItemStack().setType(event.getBucket());
            this.ess.scheduleSyncDelayedTask(new Runnable(){

                @Override
                public void run() {
                    user.updateInventory();
                }
            });
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=1)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String cmd = event.getMessage().toLowerCase(Locale.ENGLISH).split(" ")[0].replace("/", "").toLowerCase(Locale.ENGLISH);
        if (this.ess.getSettings().getSocialSpyCommands().contains(cmd)) {
            for (Player onlinePlayer : this.ess.getServer().getOnlinePlayers()) {
                User spyer = this.ess.getUser(onlinePlayer);
                if (!spyer.isSocialSpyEnabled() || player.equals((Object)onlinePlayer)) continue;
                spyer.sendMessage(player.getDisplayName() + " : " + event.getMessage());
            }
        } else if (!cmd.equalsIgnoreCase("afk")) {
            User user = this.ess.getUser(player);
            user.updateActivity(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerChangedWorldFlyReset(PlayerChangedWorldEvent event) {
        User user = this.ess.getUser(event.getPlayer());
        if (user.getGameMode() != GameMode.CREATIVE && !user.isAuthorized("essentials.fly")) {
            user.setFallDistance(0.0f);
            user.setAllowFlight(false);
        }
        if (!user.isAuthorized("essentials.speed")) {
            user.setFlySpeed(0.1f);
            user.setWalkSpeed(0.2f);
        } else {
            if ((double)user.getFlySpeed() > this.ess.getSettings().getMaxFlySpeed() && !user.isAuthorized("essentials.speed.bypass")) {
                user.setFlySpeed((float)this.ess.getSettings().getMaxFlySpeed());
            } else {
                user.setFlySpeed(user.getFlySpeed() * 0.99999f);
            }
            if ((double)user.getWalkSpeed() > this.ess.getSettings().getMaxWalkSpeed() && !user.isAuthorized("essentials.speed.bypass")) {
                user.setWalkSpeed((float)this.ess.getSettings().getMaxWalkSpeed());
            } else {
                user.setWalkSpeed(user.getWalkSpeed() * 0.99999f);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        User user = this.ess.getUser(event.getPlayer());
        String newWorld = event.getPlayer().getLocation().getWorld().getName();
        user.setDisplayNick();
        this.updateCompass(user);
        if (this.ess.getSettings().getNoGodWorlds().contains(newWorld) && user.isGodModeEnabledRaw()) {
            user.sendMessage(I18n._("noGodWorldWarning", new Object[0]));
        }
        if (!user.getWorld().getName().equals(newWorld)) {
            user.sendMessage(I18n._("currentWorld", newWorld));
        }
        if (user.isVanished()) {
            user.setVanished(user.isAuthorized("essentials.vanish"));
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        switch (event.getAction()) {
            User user;
            case RIGHT_CLICK_BLOCK: {
                User player;
                if (event.isCancelled() || event.getClickedBlock().getType() != Material.BED_BLOCK || !this.ess.getSettings().getUpdateBedAtDaytime() || !(player = this.ess.getUser(event.getPlayer())).isAuthorized("essentials.sethome.bed")) break;
                player.setBedSpawnLocation(event.getClickedBlock().getLocation());
                player.sendMessage(I18n._("bedSet", player.getLocation().getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
                break;
            }
            case LEFT_CLICK_AIR: {
                if (event.getPlayer().isFlying() && (user = this.ess.getUser(event.getPlayer())).isFlyClickJump()) {
                    this.useFlyClickJump(user);
                    return;
                }
            }
            case LEFT_CLICK_BLOCK: {
                if (event.getItem() == null || event.getItem().getType() == Material.AIR) break;
                user = this.ess.getUser(event.getPlayer());
                user.updateActivity(true);
                if (user.hasPowerTools() && user.arePowerToolsEnabled() && this.usePowertools(user, event.getItem().getTypeId())) {
                    event.setCancelled(true);
                    break;
                } else {
                    break;
                }
            }
        }
    }

    private void useFlyClickJump(final User user) {
        block2 : {
            try {
                final Location otarget = LocationUtil.getTarget((LivingEntity)user.getBase());
                this.ess.scheduleSyncDelayedTask(new Runnable(){

                    @Override
                    public void run() {
                        Location loc = user.getLocation();
                        loc.setX(otarget.getX());
                        loc.setZ(otarget.getZ());
                        while (LocationUtil.isBlockDamaging(loc.getWorld(), loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ())) {
                            loc.setY(loc.getY() + 1.0);
                        }
                        user.getBase().teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                });
            }
            catch (Exception ex) {
                if (!this.ess.getSettings().isDebug()) break block2;
                LOGGER.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }

    private boolean usePowertools(final User user, int id) {
        List<String> commandList = user.getPowertool(id);
        if (commandList == null || commandList.isEmpty()) {
            return false;
        }
        boolean used = false;
        for (final String command : commandList) {
            if (command.contains("{player}")) continue;
            if (command.startsWith("c:")) {
                used = true;
                user.chat(command.substring(2));
                continue;
            }
            used = true;
            this.ess.scheduleSyncDelayedTask(new Runnable(){

                @Override
                public void run() {
                    user.getServer().dispatchCommand((CommandSender)user.getBase(), command);
                    LOGGER.log(Level.INFO, String.format("[PT] %s issued server command: /%s", user.getName(), command));
                }
            });
        }
        return used;
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (this.ess.getSettings().getDisableItemPickupWhileAfk() && this.ess.getUser(event.getPlayer()).isAfk()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=1)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        User user;
        Player refreshPlayer = null;
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        if (type == InventoryType.PLAYER) {
            user = this.ess.getUser((Player)event.getWhoClicked());
            InventoryHolder invHolder = top.getHolder();
            if (invHolder != null && invHolder instanceof HumanEntity) {
                User invOwner = this.ess.getUser((Player)invHolder);
                if (user.isInvSee() && (!user.isAuthorized("essentials.invsee.modify") || invOwner.isAuthorized("essentials.invsee.preventmodify") || !invOwner.isOnline())) {
                    event.setCancelled(true);
                    refreshPlayer = user.getBase();
                }
            }
        } else if (type == InventoryType.ENDER_CHEST) {
            user = this.ess.getUser((Player)event.getWhoClicked());
            if (user.isEnderSee() && !user.isAuthorized("essentials.enderchest.modify")) {
                event.setCancelled(true);
                refreshPlayer = user.getBase();
            }
        } else if (type == InventoryType.WORKBENCH) {
            user = this.ess.getUser((Player)event.getWhoClicked());
            if (user.isRecipeSee()) {
                event.setCancelled(true);
                refreshPlayer = user.getBase();
            }
        } else if (type == InventoryType.CHEST && top.getSize() == 9) {
            user = this.ess.getUser((Player)event.getWhoClicked());
            InventoryHolder invHolder = top.getHolder();
            if (invHolder != null && invHolder instanceof HumanEntity && user.isInvSee()) {
                event.setCancelled(true);
                refreshPlayer = user.getBase();
            }
        }
        if (refreshPlayer != null) {
            final Player player = refreshPlayer;
            this.ess.scheduleSyncDelayedTask(new Runnable(){

                @Override
                public void run() {
                    player.updateInventory();
                }
            }, 1);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        User user;
        InventoryHolder invHolder;
        Player refreshPlayer = null;
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        if (type == InventoryType.PLAYER) {
            user = this.ess.getUser((Player)event.getPlayer());
            user.setInvSee(false);
            refreshPlayer = user.getBase();
        } else if (type == InventoryType.ENDER_CHEST) {
            user = this.ess.getUser((Player)event.getPlayer());
            user.setEnderSee(false);
            refreshPlayer = user.getBase();
        } else if (type == InventoryType.WORKBENCH) {
            user = this.ess.getUser((Player)event.getPlayer());
            if (user.isRecipeSee()) {
                user.setRecipeSee(false);
                event.getView().getTopInventory().clear();
                refreshPlayer = user.getBase();
            }
        } else if (type == InventoryType.CHEST && top.getSize() == 9 && (invHolder = top.getHolder()) != null && invHolder instanceof HumanEntity) {
            User user2 = this.ess.getUser((Player)event.getPlayer());
            user2.setInvSee(false);
            refreshPlayer = user2.getBase();
        }
        if (refreshPlayer != null) {
            final Player player = refreshPlayer;
            this.ess.scheduleSyncDelayedTask(new Runnable(){

                @Override
                public void run() {
                    player.updateInventory();
                }
            }, 1);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=1)
    public void onPlayerFishEvent(PlayerFishEvent event) {
        User user = this.ess.getUser(event.getPlayer());
        user.updateActivity(true);
    }

}

