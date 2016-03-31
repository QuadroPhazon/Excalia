/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.earth2me.essentials;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IReplyTo;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.UserData;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.perm.PermissionsHandler;
import com.earth2me.essentials.register.payment.Method;
import com.earth2me.essentials.register.payment.Methods;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.FormatUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.ITeleport;
import net.ess3.api.MaxMoneyException;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class User
extends UserData
implements Comparable<User>,
IReplyTo,
net.ess3.api.IUser {
    private static final Logger logger = Logger.getLogger("Essentials");
    private CommandSource replyTo = null;
    private transient String teleportRequester;
    private transient boolean teleportRequestHere;
    private transient Location teleportLocation;
    private transient boolean vanished;
    private final transient Teleport teleport;
    private transient long teleportRequestTime;
    private transient long lastOnlineActivity;
    private transient long lastThrottledAction;
    private transient long lastActivity = System.currentTimeMillis();
    private boolean hidden = false;
    private boolean rightClickJump = false;
    private transient Location afkPosition = null;
    private boolean invSee = false;
    private boolean recipeSee = false;
    private boolean enderSee = false;
    private transient long teleportInvulnerabilityTimestamp = 0;

    User(Player base, IEssentials ess) {
        super(base, ess);
        this.teleport = new Teleport(this, ess);
        if (this.isAfk()) {
            this.afkPosition = this.getBase().getLocation();
        }
        if (this.getBase().isOnline()) {
            this.lastOnlineActivity = System.currentTimeMillis();
        }
    }

    User update(Player base) {
        this.setBase(base);
        return this;
    }

    @Override
    public boolean isAuthorized(IEssentialsCommand cmd) {
        return this.isAuthorized(cmd, "essentials.");
    }

    @Override
    public boolean isAuthorized(IEssentialsCommand cmd, String permissionPrefix) {
        return this.isAuthorized(permissionPrefix + (cmd.getName().equals("r") ? "msg" : cmd.getName()));
    }

    @Override
    public boolean isAuthorized(String node) {
        boolean result = this.isAuthorizedCheck(node);
        if (this.ess.getSettings().isDebug()) {
            this.ess.getLogger().log(Level.INFO, "checking if " + this.base.getName() + " has " + node + " - " + result);
        }
        return result;
    }

    private boolean isAuthorizedCheck(String node) {
        if (this.base instanceof OfflinePlayer) {
            return false;
        }
        try {
            return this.ess.getPermissionsHandler().hasPermission(this.base, node);
        }
        catch (Exception ex) {
            if (this.ess.getSettings().isDebug()) {
                this.ess.getLogger().log(Level.SEVERE, "Permission System Error: " + this.ess.getPermissionsHandler().getName() + " returned: " + ex.getMessage(), ex);
            } else {
                this.ess.getLogger().log(Level.SEVERE, "Permission System Error: " + this.ess.getPermissionsHandler().getName() + " returned: " + ex.getMessage());
            }
            return false;
        }
    }

    @Override
    public void healCooldown() throws Exception {
        GregorianCalendar now = new GregorianCalendar();
        if (this.getLastHealTimestamp() > 0) {
            double cooldown = this.ess.getSettings().getHealCooldown();
            GregorianCalendar cooldownTime = new GregorianCalendar();
            cooldownTime.setTimeInMillis(this.getLastHealTimestamp());
            cooldownTime.add(13, (int)cooldown);
            cooldownTime.add(14, (int)(cooldown * 1000.0 % 1000.0));
            if (cooldownTime.after(now) && !this.isAuthorized("essentials.heal.cooldown.bypass")) {
                throw new Exception(I18n._("timeBeforeHeal", DateUtil.formatDateDiff(cooldownTime.getTimeInMillis())));
            }
        }
        this.setLastHealTimestamp(now.getTimeInMillis());
    }

    @Override
    public void giveMoney(BigDecimal value) throws MaxMoneyException {
        this.giveMoney(value, null);
    }

    @Override
    public void giveMoney(BigDecimal value, CommandSource initiator) throws MaxMoneyException {
        if (value.signum() == 0) {
            return;
        }
        this.setMoney(this.getMoney().add(value));
        this.sendMessage(I18n._("addedToAccount", NumberUtil.displayCurrency(value, this.ess)));
        if (initiator != null) {
            initiator.sendMessage(I18n._("addedToOthersAccount", NumberUtil.displayCurrency(value, this.ess), this.getDisplayName(), NumberUtil.displayCurrency(this.getMoney(), this.ess)));
        }
    }

    @Override
    public void payUser(User reciever, BigDecimal value) throws ChargeException, MaxMoneyException {
        if (value.signum() == 0) {
            return;
        }
        if (!this.canAfford(value)) {
            throw new ChargeException(I18n._("notEnoughMoney", new Object[0]));
        }
        this.setMoney(this.getMoney().subtract(value));
        reciever.setMoney(reciever.getMoney().add(value));
        this.sendMessage(I18n._("moneySentTo", NumberUtil.displayCurrency(value, this.ess), reciever.getDisplayName()));
        reciever.sendMessage(I18n._("moneyRecievedFrom", NumberUtil.displayCurrency(value, this.ess), this.getDisplayName()));
    }

    @Override
    public void takeMoney(BigDecimal value) {
        this.takeMoney(value, null);
    }

    @Override
    public void takeMoney(BigDecimal value, CommandSource initiator) {
        if (value.signum() == 0) {
            return;
        }
        try {
            this.setMoney(this.getMoney().subtract(value));
        }
        catch (MaxMoneyException ex) {
            // empty catch block
        }
        this.sendMessage(I18n._("takenFromAccount", NumberUtil.displayCurrency(value, this.ess)));
        if (initiator != null) {
            initiator.sendMessage(I18n._("takenFromOthersAccount", NumberUtil.displayCurrency(value, this.ess), this.getDisplayName(), NumberUtil.displayCurrency(this.getMoney(), this.ess)));
        }
    }

    @Override
    public boolean canAfford(BigDecimal cost) {
        return this.canAfford(cost, true);
    }

    public boolean canAfford(BigDecimal cost, boolean permcheck) {
        if (cost.signum() <= 0) {
            return true;
        }
        BigDecimal remainingBalance = this.getMoney().subtract(cost);
        if (!permcheck || this.isAuthorized("essentials.eco.loan")) {
            return remainingBalance.compareTo(this.ess.getSettings().getMinMoney()) >= 0;
        }
        return remainingBalance.signum() >= 0;
    }

    public void dispose() {
        this.base = new OfflinePlayer(this.getName(), this.ess);
    }

    @Override
    public Boolean canSpawnItem(int itemId) {
        return !this.ess.getSettings().itemSpawnBlacklist().contains(itemId);
    }

    @Override
    public void setLastLocation() {
        this.setLastLocation(this.getBase().getLocation());
    }

    @Override
    public void setLogoutLocation() {
        this.setLogoutLocation(this.getBase().getLocation());
    }

    @Override
    public void requestTeleport(User player, boolean here) {
        this.teleportRequestTime = System.currentTimeMillis();
        this.teleportRequester = player == null ? null : player.getName();
        this.teleportRequestHere = here;
        this.teleportLocation = player == null ? null : (here ? player.getBase().getLocation() : this.getBase().getLocation());
    }

    public String getTeleportRequest() {
        return this.teleportRequester;
    }

    public boolean isTpRequestHere() {
        return this.teleportRequestHere;
    }

    public Location getTpRequestLocation() {
        return this.teleportLocation;
    }

    public String getNick(boolean longnick) {
        String nickname;
        StringBuilder prefix = new StringBuilder();
        String suffix = "";
        String nick = this.getNickname();
        if (this.ess.getSettings().isCommandDisabled("nick") || nick == null || nick.isEmpty() || nick.equalsIgnoreCase(this.getName())) {
            nickname = this.getName();
        } else {
            nickname = this.ess.getSettings().getNicknamePrefix() + nick;
            suffix = "\u00a7r";
        }
        if (this.getBase().isOp()) {
            try {
                ChatColor opPrefix = this.ess.getSettings().getOperatorColor();
                if (opPrefix != null && opPrefix.toString().length() > 0) {
                    prefix.insert(0, opPrefix.toString());
                    suffix = "\u00a7r";
                }
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        if (this.ess.getSettings().addPrefixSuffix()) {
            if (!this.ess.getSettings().disablePrefix()) {
                String ptext = this.ess.getPermissionsHandler().getPrefix(this.base).replace('&', '\u00a7');
                prefix.insert(0, ptext);
                suffix = "\u00a7r";
            }
            if (!this.ess.getSettings().disableSuffix()) {
                String stext = this.ess.getPermissionsHandler().getSuffix(this.base).replace('&', '\u00a7');
                suffix = stext + "\u00a7r";
                suffix = suffix.replace("\u00a7f\u00a7f", "\u00a7f").replace("\u00a7f\u00a7r", "\u00a7r").replace("\u00a7r\u00a7r", "\u00a7r");
            }
        }
        String strPrefix = prefix.toString();
        String output = strPrefix + nickname + suffix;
        if (!longnick && output.length() > 16) {
            output = strPrefix + nickname;
        }
        if (!longnick && output.length() > 16) {
            output = FormatUtil.lastCode(strPrefix) + nickname;
        }
        if (!longnick && output.length() > 16) {
            output = FormatUtil.lastCode(strPrefix) + nickname.substring(0, 14);
        }
        if (output.charAt(output.length() - 1) == '\u00a7') {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    public void setDisplayNick() {
        block4 : {
            if (this.base.isOnline() && this.ess.getSettings().changeDisplayName()) {
                this.getBase().setDisplayName(this.getNick(true));
                if (this.ess.getSettings().changePlayerListName()) {
                    String name = this.getNick(false);
                    try {
                        this.getBase().setPlayerListName(name);
                    }
                    catch (IllegalArgumentException e) {
                        if (!this.ess.getSettings().isDebug()) break block4;
                        logger.log(Level.INFO, "Playerlist for " + name + " was not updated. Name clashed with another online player.");
                    }
                }
            }
        }
    }

    @Override
    public String getDisplayName() {
        return super.getBase().getDisplayName() == null ? super.getBase().getName() : super.getBase().getDisplayName();
    }

    @Override
    public Teleport getTeleport() {
        return this.teleport;
    }

    public long getLastOnlineActivity() {
        return this.lastOnlineActivity;
    }

    public void setLastOnlineActivity(long timestamp) {
        this.lastOnlineActivity = timestamp;
    }

    @Override
    public BigDecimal getMoney() {
        long start = System.nanoTime();
        BigDecimal value = this._getMoney();
        long elapsed = System.nanoTime() - start;
        if (elapsed > this.ess.getSettings().getEconomyLagWarning()) {
            this.ess.getLogger().log(Level.INFO, "Lag Notice - Slow Economy Response - Request took over {0}ms!", (double)elapsed / 1000000.0);
        }
        return value;
    }

    private BigDecimal _getMoney() {
        if (this.ess.getSettings().isEcoDisabled()) {
            if (this.ess.getSettings().isDebug()) {
                this.ess.getLogger().info("Internal economy functions disabled, aborting balance check.");
            }
            return BigDecimal.ZERO;
        }
        if (Methods.hasMethod()) {
            try {
                Method method = Methods.getMethod();
                if (!method.hasAccount(this.getName())) {
                    throw new Exception();
                }
                Method.MethodAccount account = Methods.getMethod().getAccount(this.getName());
                return BigDecimal.valueOf(account.balance());
            }
            catch (Exception ex) {
                // empty catch block
            }
        }
        return super.getMoney();
    }

    @Override
    public void setMoney(BigDecimal value) throws MaxMoneyException {
        if (this.ess.getSettings().isEcoDisabled()) {
            if (this.ess.getSettings().isDebug()) {
                this.ess.getLogger().info("Internal economy functions disabled, aborting balance change.");
            }
            return;
        }
        BigDecimal oldBalance = this._getMoney();
        if (Methods.hasMethod()) {
            try {
                Method method = Methods.getMethod();
                if (!method.hasAccount(this.getName())) {
                    throw new Exception();
                }
                Method.MethodAccount account = Methods.getMethod().getAccount(this.getName());
                account.set(value.doubleValue());
            }
            catch (Exception ex) {
                // empty catch block
            }
        }
        super.setMoney(value, true);
        this.ess.getServer().getPluginManager().callEvent((Event)new UserBalanceUpdateEvent(this.getBase(), oldBalance, value));
        Trade.log("Update", "Set", "API", this.getName(), new Trade(value, this.ess), null, null, null, this.ess);
    }

    public void updateMoneyCache(BigDecimal value) {
        if (this.ess.getSettings().isEcoDisabled()) {
            return;
        }
        if (Methods.hasMethod() && super.getMoney() != value) {
            try {
                super.setMoney(value, false);
            }
            catch (MaxMoneyException ex) {
                // empty catch block
            }
        }
    }

    @Override
    public void setAfk(boolean set) {
        AfkStatusChangeEvent afkEvent = new AfkStatusChangeEvent(this, set);
        this.ess.getServer().getPluginManager().callEvent((Event)afkEvent);
        if (afkEvent.isCancelled()) {
            return;
        }
        this.getBase().setSleepingIgnored(this.isAuthorized("essentials.sleepingignored") ? true : set);
        if (set && !this.isAfk()) {
            this.afkPosition = this.getBase().getLocation();
        } else if (!set && this.isAfk()) {
            this.afkPosition = null;
        }
        this._setAfk(set);
    }

    public boolean toggleAfk() {
        this.setAfk(!this.isAfk());
        return this.isAfk();
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        if (hidden) {
            this.setLastLogout(this.getLastOnlineActivity());
        }
    }

    public boolean checkJailTimeout(long currentTime) {
        if (this.getJailTimeout() > 0 && this.getJailTimeout() < currentTime && this.isJailed()) {
            this.setJailTimeout(0);
            this.setJailed(false);
            this.sendMessage(I18n._("haveBeenReleased", new Object[0]));
            this.setJail(null);
            try {
                this.getTeleport().back();
            }
            catch (Exception ex) {
                try {
                    this.getTeleport().respawn(null, PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
                catch (Exception ex1) {
                    // empty catch block
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkMuteTimeout(long currentTime) {
        if (this.getMuteTimeout() > 0 && this.getMuteTimeout() < currentTime && this.isMuted()) {
            this.setMuteTimeout(0);
            this.sendMessage(I18n._("canTalkAgain", new Object[0]));
            this.setMuted(false);
            return true;
        }
        return false;
    }

    public boolean checkBanTimeout(long currentTime) {
        if (this.getBanTimeout() > 0 && this.getBanTimeout() < currentTime && this.getBase().isBanned()) {
            this.setBanTimeout(0);
            this.getBase().setBanned(false);
            return true;
        }
        return false;
    }

    public void updateActivity(boolean broadcast) {
        if (this.isAfk() && this.ess.getSettings().cancelAfkOnInteract()) {
            this.setAfk(false);
            if (broadcast && !this.isHidden()) {
                this.setDisplayNick();
                String msg = I18n._("userIsNotAway", this.getDisplayName());
                if (!msg.isEmpty()) {
                    this.ess.broadcastMessage(this, msg);
                }
            }
        }
        this.lastActivity = System.currentTimeMillis();
    }

    public void checkActivity() {
        long autoafkkick = this.ess.getSettings().getAutoAfkKick();
        if (!(autoafkkick <= 0 || this.lastActivity <= 0 || this.lastActivity + autoafkkick * 1000 >= System.currentTimeMillis() || this.isHidden() || this.isAuthorized("essentials.kick.exempt") || this.isAuthorized("essentials.afk.kickexempt"))) {
            String kickReason = I18n._("autoAfkKickReason", (double)autoafkkick / 60.0);
            this.lastActivity = 0;
            this.getBase().kickPlayer(kickReason);
            for (Player player : this.ess.getServer().getOnlinePlayers()) {
                User user = this.ess.getUser(player);
                if (!user.isAuthorized("essentials.kick.notify")) continue;
                user.sendMessage(I18n._("playerKicked", "Console", this.getName(), kickReason));
            }
        }
        long autoafk = this.ess.getSettings().getAutoAfk();
        if (!this.isAfk() && autoafk > 0 && this.lastActivity + autoafk * 1000 < System.currentTimeMillis() && this.isAuthorized("essentials.afk.auto")) {
            this.setAfk(true);
            if (!this.isHidden()) {
                this.setDisplayNick();
                String msg = I18n._("userIsAway", this.getDisplayName());
                if (!msg.isEmpty()) {
                    this.ess.broadcastMessage(this, msg);
                }
            }
        }
    }

    public Location getAfkPosition() {
        return this.afkPosition;
    }

    @Override
    public boolean isGodModeEnabled() {
        return super.isGodModeEnabled() && !this.ess.getSettings().getNoGodWorlds().contains(this.getBase().getLocation().getWorld().getName()) || this.isAfk() && this.ess.getSettings().getFreezeAfkPlayers();
    }

    public boolean isGodModeEnabledRaw() {
        return super.isGodModeEnabled();
    }

    @Override
    public String getGroup() {
        String result = this.ess.getPermissionsHandler().getGroup(this.base);
        if (this.ess.getSettings().isDebug()) {
            this.ess.getLogger().log(Level.INFO, "looking up groupname of " + this.base.getName() + " - " + result);
        }
        return result;
    }

    @Override
    public boolean inGroup(String group) {
        boolean result = this.ess.getPermissionsHandler().inGroup(this.base, group);
        if (this.ess.getSettings().isDebug()) {
            this.ess.getLogger().log(Level.INFO, "checking if " + this.base.getName() + " is in group " + group + " - " + result);
        }
        return result;
    }

    @Override
    public boolean canBuild() {
        if (this.getBase().isOp()) {
            return true;
        }
        return this.ess.getPermissionsHandler().canBuild(this.base, this.getGroup());
    }

    @Override
    public long getTeleportRequestTime() {
        return this.teleportRequestTime;
    }

    public boolean isInvSee() {
        return this.invSee;
    }

    public void setInvSee(boolean set) {
        this.invSee = set;
    }

    public boolean isEnderSee() {
        return this.enderSee;
    }

    public void setEnderSee(boolean set) {
        this.enderSee = set;
    }

    @Override
    public void enableInvulnerabilityAfterTeleport() {
        long time = this.ess.getSettings().getTeleportInvulnerability();
        if (time > 0) {
            this.teleportInvulnerabilityTimestamp = System.currentTimeMillis() + time;
        }
    }

    @Override
    public void resetInvulnerabilityAfterTeleport() {
        if (this.teleportInvulnerabilityTimestamp != 0 && this.teleportInvulnerabilityTimestamp < System.currentTimeMillis()) {
            this.teleportInvulnerabilityTimestamp = 0;
        }
    }

    @Override
    public boolean hasInvulnerabilityAfterTeleport() {
        return this.teleportInvulnerabilityTimestamp != 0 && this.teleportInvulnerabilityTimestamp >= System.currentTimeMillis();
    }

    @Override
    public boolean isVanished() {
        return this.vanished;
    }

    @Override
    public void setVanished(boolean set) {
        this.vanished = set;
        if (set) {
            for (Player p : this.ess.getServer().getOnlinePlayers()) {
                if (this.ess.getUser(p).isAuthorized("essentials.vanish.see")) continue;
                p.hidePlayer(this.getBase());
            }
            this.setHidden(true);
            this.ess.getVanishedPlayers().add(this.getName());
            if (this.isAuthorized("essentials.vanish.effect")) {
                this.getBase().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false));
            }
        } else {
            for (Player p : this.ess.getServer().getOnlinePlayers()) {
                p.showPlayer(this.getBase());
            }
            this.setHidden(false);
            this.ess.getVanishedPlayers().remove(this.getName());
            if (this.isAuthorized("essentials.vanish.effect")) {
                this.getBase().removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }

    public boolean checkSignThrottle() {
        if (this.isSignThrottled()) {
            return true;
        }
        this.updateThrottle();
        return false;
    }

    public boolean isSignThrottled() {
        long minTime = this.lastThrottledAction + (long)(1000 / this.ess.getSettings().getSignUsePerSecond());
        return System.currentTimeMillis() < minTime;
    }

    public void updateThrottle() {
        this.lastThrottledAction = System.currentTimeMillis();
    }

    public boolean isFlyClickJump() {
        return this.rightClickJump;
    }

    public void setRightClickJump(boolean rightClickJump) {
        this.rightClickJump = rightClickJump;
    }

    @Override
    public boolean isIgnoreExempt() {
        return this.isAuthorized("essentials.chat.ignoreexempt");
    }

    public boolean isRecipeSee() {
        return this.recipeSee;
    }

    public void setRecipeSee(boolean recipeSee) {
        this.recipeSee = recipeSee;
    }

    @Override
    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            this.base.sendMessage(message);
        }
    }

    @Override
    public void setReplyTo(CommandSource user) {
        this.replyTo = user;
    }

    @Override
    public CommandSource getReplyTo() {
        return this.replyTo;
    }

    @Override
    public int compareTo(User other) {
        return FormatUtil.stripFormat(this.getDisplayName()).compareToIgnoreCase(FormatUtil.stripFormat(other.getDisplayName()));
    }

    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        return this.getName().equalsIgnoreCase(((User)object).getName());
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public CommandSource getSource() {
        return new CommandSource((CommandSender)this.getBase());
    }

    @Override
    public String getName() {
        return this.getBase().getName();
    }
}

