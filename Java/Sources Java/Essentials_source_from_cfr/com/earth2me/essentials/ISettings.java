/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.event.EventPriority
 */
package com.earth2me.essentials;

import com.earth2me.essentials.IConf;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.textreader.IText;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventPriority;

public interface ISettings
extends IConf {
    public boolean areSignsDisabled();

    public IText getAnnounceNewPlayerFormat();

    public boolean getAnnounceNewPlayers();

    public String getNewPlayerKit();

    public String getBackupCommand();

    public long getBackupInterval();

    public String getChatFormat(String var1);

    public int getChatRadius();

    public BigDecimal getCommandCost(IEssentialsCommand var1);

    public BigDecimal getCommandCost(String var1);

    public String getCurrencySymbol();

    public int getOversizedStackSize();

    public int getDefaultStackSize();

    public double getHealCooldown();

    public Set<String> getSocialSpyCommands();

    public Map<String, Object> getKit(String var1);

    public ConfigurationSection getKits();

    public String getLocale();

    public String getNewbieSpawn();

    public String getNicknamePrefix();

    public ChatColor getOperatorColor() throws Exception;

    public boolean getPerWarpPermission();

    public boolean getProtectBoolean(String var1, boolean var2);

    public int getProtectCreeperMaxHeight();

    public List<Integer> getProtectList(String var1);

    public boolean getProtectPreventSpawn(String var1);

    public String getProtectString(String var1);

    public boolean getRespawnAtHome();

    public Set getMultipleHomes();

    public int getHomeLimit(String var1);

    public int getHomeLimit(User var1);

    public int getSpawnMobLimit();

    public BigDecimal getStartingBalance();

    public boolean isTeleportSafetyEnabled();

    public double getTeleportCooldown();

    public double getTeleportDelay();

    public boolean hidePermissionlessHelp();

    public boolean isCommandDisabled(IEssentialsCommand var1);

    public boolean isCommandDisabled(String var1);

    public boolean isCommandOverridden(String var1);

    public boolean isDebug();

    public boolean isEcoDisabled();

    public boolean isTradeInStacks(int var1);

    public List<Integer> itemSpawnBlacklist();

    public List<EssentialsSign> enabledSigns();

    public boolean permissionBasedItemSpawn();

    public boolean showNonEssCommandsInHelp();

    public boolean warnOnBuildDisallow();

    public boolean warnOnSmite();

    public BigDecimal getMaxMoney();

    public BigDecimal getMinMoney();

    public boolean isEcoLogEnabled();

    public boolean isEcoLogUpdateEnabled();

    public boolean removeGodOnDisconnect();

    public boolean changeDisplayName();

    public boolean changePlayerListName();

    public boolean isPlayerCommand(String var1);

    public boolean useBukkitPermissions();

    public boolean addPrefixSuffix();

    public boolean disablePrefix();

    public boolean disableSuffix();

    public long getAutoAfk();

    public long getAutoAfkKick();

    public boolean getFreezeAfkPlayers();

    public boolean cancelAfkOnMove();

    public boolean cancelAfkOnInteract();

    public boolean areDeathMessagesEnabled();

    public void setDebug(boolean var1);

    public Set<String> getNoGodWorlds();

    public boolean getUpdateBedAtDaytime();

    public boolean allowUnsafeEnchantments();

    public boolean getRepairEnchanted();

    public boolean isWorldTeleportPermissions();

    public boolean isWorldHomePermissions();

    public boolean registerBackInListener();

    public boolean getDisableItemPickupWhileAfk();

    public EventPriority getRespawnPriority();

    public long getTpaAcceptCancellation();

    public boolean isMetricsEnabled();

    public void setMetricsEnabled(boolean var1);

    public long getTeleportInvulnerability();

    public boolean isTeleportInvulnerability();

    public long getLoginAttackDelay();

    public int getSignUsePerSecond();

    public double getMaxFlySpeed();

    public double getMaxWalkSpeed();

    public int getMailsPerMinute();

    public long getEconomyLagWarning();

    public void setEssentialsChatActive(boolean var1);

    public long getMaxTempban();

    public Map<String, Object> getListGroupConfig();

    public int getMaxNickLength();

    public int getMaxUserCacheCount();

    public boolean allowSilentJoinQuit();

    public boolean isCustomJoinMessage();

    public String getCustomJoinMessage();

    public boolean isCustomQuitMessage();

    public String getCustomQuitMessage();
}

