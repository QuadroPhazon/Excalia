/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.MemoryConfiguration
 *  org.bukkit.event.EventPriority
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials;

import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.Signs;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.earth2me.essentials.utils.FormatUtil;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class Settings
implements ISettings {
    private final transient EssentialsConf config;
    private static final Logger logger = Logger.getLogger("Essentials");
    private final transient IEssentials ess;
    private boolean metricsEnabled = true;
    private int chatRadius = 0;
    private boolean teleportSafety;
    private Set<String> disabledCommands = new HashSet<String>();
    private ConfigurationSection commandCosts;
    private Set<String> socialSpyCommands = new HashSet<String>();
    private String nicknamePrefix = "~";
    private ConfigurationSection kits;
    private ChatColor operatorColor = null;
    private final Map<String, String> chatFormats = Collections.synchronizedMap(new HashMap());
    private List<Integer> itemSpawnBl = new ArrayList<Integer>();
    private List<EssentialsSign> enabledSigns = new ArrayList<EssentialsSign>();
    private boolean signsEnabled = false;
    private boolean warnOnBuildDisallow;
    private boolean debug = false;
    private boolean configDebug = false;
    private boolean economyDisabled = false;
    private static final BigDecimal MAXMONEY = new BigDecimal("10000000000000");
    private BigDecimal maxMoney = MAXMONEY;
    private static final BigDecimal MINMONEY = new BigDecimal("-10000000000000");
    private BigDecimal minMoney = MINMONEY;
    private boolean economyLog = false;
    private boolean economyLogUpdate = false;
    private boolean changeDisplayName = true;
    private boolean changePlayerListName = false;
    private boolean prefixsuffixconfigured = false;
    private boolean addprefixsuffix = false;
    private boolean essentialsChatActive = false;
    private boolean disablePrefix = false;
    private boolean disableSuffix = false;
    private boolean getFreezeAfkPlayers;
    private boolean cancelAfkOnMove;
    private boolean cancelAfkOnInteract;
    private Set<String> noGodWorlds = new HashSet<String>();
    private boolean registerBackInListener;
    private boolean disableItemPickupWhileAfk;
    private long teleportInvulnerabilityTime;
    private boolean teleportInvulnerability;
    private long loginAttackDelay;
    private int signUsePerSecond;
    private int mailsPerMinute;
    private long economyLagWarning;
    private boolean allowSilentJoin;
    private String customJoinMessage;
    private boolean isCustomJoinMessage;
    private String customQuitMessage;
    private boolean isCustomQuitMessage;

    public Settings(IEssentials ess) {
        this.ess = ess;
        this.config = new EssentialsConf(new File(ess.getDataFolder(), "config.yml"));
        this.config.setTemplateName("/config.yml");
        this.reloadConfig();
    }

    @Override
    public boolean getRespawnAtHome() {
        return this.config.getBoolean("respawn-at-home", false);
    }

    @Override
    public boolean getUpdateBedAtDaytime() {
        return this.config.getBoolean("update-bed-at-daytime", true);
    }

    @Override
    public Set<String> getMultipleHomes() {
        return this.config.getConfigurationSection("sethome-multiple").getKeys(false);
    }

    @Override
    public int getHomeLimit(User user) {
        Set<String> homeList;
        int limit = 1;
        if (user.isAuthorized("essentials.sethome.multiple")) {
            limit = this.getHomeLimit("default");
        }
        if ((homeList = this.getMultipleHomes()) != null) {
            for (String set : homeList) {
                if (!user.isAuthorized("essentials.sethome.multiple." + set) || limit >= this.getHomeLimit(set)) continue;
                limit = this.getHomeLimit(set);
            }
        }
        return limit;
    }

    @Override
    public int getHomeLimit(String set) {
        return this.config.getInt("sethome-multiple." + set, this.config.getInt("sethome-multiple.default", 3));
    }

    private int _getChatRadius() {
        return this.config.getInt("chat.radius", this.config.getInt("chat-radius", 0));
    }

    @Override
    public int getChatRadius() {
        return this.chatRadius;
    }

    public boolean _isTeleportSafetyEnabled() {
        return this.config.getBoolean("teleport-safety", true);
    }

    @Override
    public boolean isTeleportSafetyEnabled() {
        return this.teleportSafety;
    }

    @Override
    public double getTeleportDelay() {
        return this.config.getDouble("teleport-delay", 0.0);
    }

    @Override
    public int getOversizedStackSize() {
        return this.config.getInt("oversized-stacksize", 64);
    }

    @Override
    public int getDefaultStackSize() {
        return this.config.getInt("default-stack-size", -1);
    }

    @Override
    public BigDecimal getStartingBalance() {
        return this.config.getBigDecimal("starting-balance", BigDecimal.ZERO);
    }

    @Override
    public boolean isCommandDisabled(IEssentialsCommand cmd) {
        return this.isCommandDisabled(cmd.getName());
    }

    @Override
    public boolean isCommandDisabled(String label) {
        return this.disabledCommands.contains(label);
    }

    private Set<String> getDisabledCommands() {
        HashSet<String> disCommands = new HashSet<String>();
        for (String c2 : this.config.getStringList("disabled-commands")) {
            disCommands.add(c2.toLowerCase(Locale.ENGLISH));
        }
        for (String c2 : this.config.getKeys(false)) {
            if (!c2.startsWith("disable-")) continue;
            disCommands.add(c2.substring(8).toLowerCase(Locale.ENGLISH));
        }
        return disCommands;
    }

    @Override
    public boolean isPlayerCommand(String label) {
        for (String c : this.config.getStringList("player-commands")) {
            if (!c.equalsIgnoreCase(label)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isCommandOverridden(String name) {
        for (String c : this.config.getStringList("overridden-commands")) {
            if (!c.equalsIgnoreCase(name)) continue;
            return true;
        }
        return this.config.getBoolean("override-" + name.toLowerCase(Locale.ENGLISH), false);
    }

    @Override
    public BigDecimal getCommandCost(IEssentialsCommand cmd) {
        return this.getCommandCost(cmd.getName());
    }

    private ConfigurationSection _getCommandCosts() {
        if (this.config.isConfigurationSection("command-costs")) {
            ConfigurationSection section = this.config.getConfigurationSection("command-costs");
            MemoryConfiguration newSection = new MemoryConfiguration();
            for (String command : section.getKeys(false)) {
                PluginCommand cmd = this.ess.getServer().getPluginCommand(command);
                if (command.charAt(0) == '/') {
                    this.ess.getLogger().warning("Invalid command cost. '" + command + "' should not start with '/'.");
                }
                if (section.isDouble(command)) {
                    newSection.set(command.toLowerCase(Locale.ENGLISH), (Object)section.getDouble(command));
                    continue;
                }
                if (section.isInt(command)) {
                    newSection.set(command.toLowerCase(Locale.ENGLISH), (Object)Double.valueOf(section.getInt(command)));
                    continue;
                }
                if (section.isString(command)) {
                    String costString = section.getString(command);
                    try {
                        double cost = Double.parseDouble(costString.trim().replace(this.getCurrencySymbol(), "").replaceAll("\\W", ""));
                        newSection.set(command.toLowerCase(Locale.ENGLISH), (Object)cost);
                    }
                    catch (NumberFormatException ex) {
                        this.ess.getLogger().warning("Invalid command cost for: " + command + " (" + costString + ")");
                    }
                    continue;
                }
                this.ess.getLogger().warning("Invalid command cost for: " + command);
            }
            return newSection;
        }
        return null;
    }

    @Override
    public BigDecimal getCommandCost(String name) {
        name = name.replace('.', '_').replace('/', '_');
        if (this.commandCosts != null) {
            return EssentialsConf.toBigDecimal(this.commandCosts.getString(name), BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    private Set<String> _getSocialSpyCommands() {
        HashSet<String> socialspyCommands = new HashSet<String>();
        if (this.config.isList("socialspy-commands")) {
            for (String c : this.config.getStringList("socialspy-commands")) {
                socialspyCommands.add(c.toLowerCase(Locale.ENGLISH));
            }
        } else {
            socialspyCommands.addAll(Arrays.asList("msg", "r", "mail", "m", "whisper", "emsg", "t", "tell", "er", "reply", "ereply", "email", "action", "describe", "eme", "eaction", "edescribe", "etell", "ewhisper", "pm"));
        }
        return socialspyCommands;
    }

    @Override
    public Set<String> getSocialSpyCommands() {
        return this.socialSpyCommands;
    }

    private String _getNicknamePrefix() {
        return this.config.getString("nickname-prefix", "~");
    }

    @Override
    public String getNicknamePrefix() {
        return this.nicknamePrefix;
    }

    @Override
    public double getTeleportCooldown() {
        return this.config.getDouble("teleport-cooldown", 0.0);
    }

    @Override
    public double getHealCooldown() {
        return this.config.getDouble("heal-cooldown", 0.0);
    }

    private ConfigurationSection _getKits() {
        if (this.config.isConfigurationSection("kits")) {
            ConfigurationSection section = this.config.getConfigurationSection("kits");
            MemoryConfiguration newSection = new MemoryConfiguration();
            for (String kitItem : section.getKeys(false)) {
                if (!section.isConfigurationSection(kitItem)) continue;
                newSection.set(kitItem.toLowerCase(Locale.ENGLISH), (Object)section.getConfigurationSection(kitItem));
            }
            return newSection;
        }
        return null;
    }

    @Override
    public ConfigurationSection getKits() {
        return this.kits;
    }

    @Override
    public Map<String, Object> getKit(String name) {
        ConfigurationSection kits;
        name = name.replace('.', '_').replace('/', '_');
        if (this.getKits() != null && (kits = this.getKits()).isConfigurationSection(name)) {
            return kits.getConfigurationSection(name).getValues(true);
        }
        return null;
    }

    @Override
    public ChatColor getOperatorColor() {
        return this.operatorColor;
    }

    private ChatColor _getOperatorColor() {
        String colorName = this.config.getString("ops-name-color", null);
        if (colorName == null) {
            return ChatColor.DARK_RED;
        }
        if ("none".equalsIgnoreCase(colorName) || colorName.isEmpty()) {
            return null;
        }
        try {
            return ChatColor.valueOf((String)colorName.toUpperCase(Locale.ENGLISH));
        }
        catch (IllegalArgumentException ex) {
            return ChatColor.getByChar((String)colorName);
        }
    }

    @Override
    public int getSpawnMobLimit() {
        return this.config.getInt("spawnmob-limit", 10);
    }

    @Override
    public boolean showNonEssCommandsInHelp() {
        return this.config.getBoolean("non-ess-in-help", true);
    }

    @Override
    public boolean hidePermissionlessHelp() {
        return this.config.getBoolean("hide-permissionless-help", true);
    }

    @Override
    public int getProtectCreeperMaxHeight() {
        return this.config.getInt("protect.creeper.max-height", -1);
    }

    @Override
    public boolean areSignsDisabled() {
        return !this.signsEnabled;
    }

    @Override
    public long getBackupInterval() {
        return this.config.getInt("backup.interval", 1440);
    }

    @Override
    public String getBackupCommand() {
        return this.config.getString("backup.command", null);
    }

    @Override
    public String getChatFormat(String group) {
        String mFormat = this.chatFormats.get(group);
        if (mFormat == null) {
            mFormat = this.config.getString("chat.group-formats." + (group == null ? "Default" : group), this.config.getString("chat.format", "&7[{GROUP}]&r {DISPLAYNAME}&7:&r {MESSAGE}"));
            mFormat = FormatUtil.replaceFormat(mFormat);
            mFormat = mFormat.replace("{DISPLAYNAME}", "%1$s");
            mFormat = mFormat.replace("{MESSAGE}", "%2$s");
            mFormat = mFormat.replace("{GROUP}", "{0}");
            mFormat = mFormat.replace("{WORLD}", "{1}");
            mFormat = mFormat.replace("{WORLDNAME}", "{1}");
            mFormat = mFormat.replace("{SHORTWORLDNAME}", "{2}");
            mFormat = mFormat.replace("{TEAMPREFIX}", "{3}");
            mFormat = mFormat.replace("{TEAMSUFFIX}", "{4}");
            mFormat = mFormat.replace("{TEAMNAME}", "{5}");
            mFormat = "\u00a7r".concat(mFormat);
            this.chatFormats.put(group, mFormat);
        }
        return mFormat;
    }

    @Override
    public boolean getAnnounceNewPlayers() {
        return !this.config.getString("newbies.announce-format", "-").isEmpty();
    }

    @Override
    public IText getAnnounceNewPlayerFormat() {
        return new SimpleTextInput(FormatUtil.replaceFormat(this.config.getString("newbies.announce-format", "&dWelcome {DISPLAYNAME} to the server!")));
    }

    @Override
    public String getNewPlayerKit() {
        return this.config.getString("newbies.kit", "");
    }

    @Override
    public String getNewbieSpawn() {
        return this.config.getString("newbies.spawnpoint", "default");
    }

    @Override
    public boolean getPerWarpPermission() {
        return this.config.getBoolean("per-warp-permission", false);
    }

    @Override
    public Map<String, Object> getListGroupConfig() {
        Map values;
        if (this.config.isConfigurationSection("list") && !(values = this.config.getConfigurationSection("list").getValues(false)).isEmpty()) {
            return values;
        }
        HashMap<String, Object> defaultMap = new HashMap<String, Object>();
        if (this.config.getBoolean("sort-list-by-groups", false)) {
            defaultMap.put("ListByGroup", "ListByGroup");
        } else {
            defaultMap.put("Players", "*");
        }
        return defaultMap;
    }

    @Override
    public void reloadConfig() {
        this.config.load();
        this.noGodWorlds = new HashSet<String>(this.config.getStringList("no-god-in-worlds"));
        this.enabledSigns = this._getEnabledSigns();
        this.teleportSafety = this._isTeleportSafetyEnabled();
        this.teleportInvulnerabilityTime = this._getTeleportInvulnerability();
        this.teleportInvulnerability = this._isTeleportInvulnerability();
        this.disableItemPickupWhileAfk = this._getDisableItemPickupWhileAfk();
        this.registerBackInListener = this._registerBackInListener();
        this.cancelAfkOnInteract = this._cancelAfkOnInteract();
        this.cancelAfkOnMove = this._cancelAfkOnMove() && this.cancelAfkOnInteract;
        this.getFreezeAfkPlayers = this._getFreezeAfkPlayers();
        this.itemSpawnBl = this._getItemSpawnBlacklist();
        this.loginAttackDelay = this._getLoginAttackDelay();
        this.signUsePerSecond = this._getSignUsePerSecond();
        this.kits = this._getKits();
        this.chatFormats.clear();
        this.changeDisplayName = this._changeDisplayName();
        this.disabledCommands = this.getDisabledCommands();
        this.nicknamePrefix = this._getNicknamePrefix();
        this.operatorColor = this._getOperatorColor();
        this.changePlayerListName = this._changePlayerListName();
        this.configDebug = this._isDebug();
        this.prefixsuffixconfigured = this._isPrefixSuffixConfigured();
        this.addprefixsuffix = this._addPrefixSuffix();
        this.disablePrefix = this._disablePrefix();
        this.disableSuffix = this._disableSuffix();
        this.chatRadius = this._getChatRadius();
        this.commandCosts = this._getCommandCosts();
        this.socialSpyCommands = this._getSocialSpyCommands();
        this.warnOnBuildDisallow = this._warnOnBuildDisallow();
        this.mailsPerMinute = this._getMailsPerMinute();
        this.maxMoney = this._getMaxMoney();
        this.minMoney = this._getMinMoney();
        this.economyLagWarning = this._getEconomyLagWarning();
        this.economyLog = this._isEcoLogEnabled();
        this.economyLogUpdate = this._isEcoLogUpdateEnabled();
        this.economyDisabled = this._isEcoDisabled();
        this.allowSilentJoin = this._isJoinQuitMessagesDisabled();
        this.customJoinMessage = this._getCustomJoinMessage();
        this.isCustomJoinMessage = !this.customJoinMessage.equals("none");
        this.customQuitMessage = this._getCustomQuitMessage();
        this.isCustomQuitMessage = !this.customQuitMessage.equals("none");
    }

    @Override
    public List<Integer> itemSpawnBlacklist() {
        return this.itemSpawnBl;
    }

    private List<Integer> _getItemSpawnBlacklist() {
        ArrayList<Integer> epItemSpwn = new ArrayList<Integer>();
        if (this.ess.getItemDb() == null) {
            logger.log(Level.FINE, "Aborting ItemSpawnBL read, itemDB not yet loaded.");
            return epItemSpwn;
        }
        for (String itemName : this.config.getString("item-spawn-blacklist", "").split(",")) {
            if ((itemName = itemName.trim()).isEmpty()) continue;
            try {
                ItemStack iStack = this.ess.getItemDb().get(itemName);
                epItemSpwn.add(iStack.getTypeId());
                continue;
            }
            catch (Exception ex) {
                logger.log(Level.SEVERE, I18n._("unknownItemInList", itemName, "item-spawn-blacklist"));
            }
        }
        return epItemSpwn;
    }

    @Override
    public List<EssentialsSign> enabledSigns() {
        return this.enabledSigns;
    }

    private List<EssentialsSign> _getEnabledSigns() {
        ArrayList<EssentialsSign> newSigns = new ArrayList<EssentialsSign>();
        for (String signName : this.config.getStringList("enabledSigns")) {
            if ((signName = signName.trim().toUpperCase(Locale.ENGLISH)).isEmpty()) continue;
            if (signName.equals("COLOR") || signName.equals("COLOUR")) {
                this.signsEnabled = true;
                continue;
            }
            try {
                newSigns.add(Signs.valueOf(signName).getSign());
            }
            catch (Exception ex) {
                logger.log(Level.SEVERE, I18n._("unknownItemInList", signName, "enabledSigns"));
                continue;
            }
            this.signsEnabled = true;
        }
        return newSigns;
    }

    private boolean _warnOnBuildDisallow() {
        return this.config.getBoolean("protect.disable.warn-on-build-disallow", false);
    }

    @Override
    public boolean warnOnBuildDisallow() {
        return this.warnOnBuildDisallow;
    }

    private boolean _isDebug() {
        return this.config.getBoolean("debug", false);
    }

    @Override
    public boolean isDebug() {
        return this.debug || this.configDebug;
    }

    @Override
    public boolean warnOnSmite() {
        return this.config.getBoolean("warn-on-smite", true);
    }

    @Override
    public boolean permissionBasedItemSpawn() {
        return this.config.getBoolean("permission-based-item-spawn", false);
    }

    @Override
    public String getLocale() {
        return this.config.getString("locale", "");
    }

    @Override
    public String getCurrencySymbol() {
        return this.config.getString("currency-symbol", "$").concat("$").substring(0, 1).replaceAll("[0-9]", "$");
    }

    @Override
    public boolean isTradeInStacks(int id) {
        return this.config.getBoolean("trade-in-stacks-" + id, false);
    }

    public boolean _isEcoDisabled() {
        return this.config.getBoolean("disable-eco", false);
    }

    @Override
    public boolean isEcoDisabled() {
        return this.economyDisabled;
    }

    @Override
    public boolean getProtectPreventSpawn(String creatureName) {
        return this.config.getBoolean("protect.prevent.spawn." + creatureName, false);
    }

    @Override
    public List<Integer> getProtectList(String configName) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (String itemName : this.config.getString(configName, "").split(",")) {
            if ((itemName = itemName.trim()).isEmpty()) continue;
            try {
                ItemStack itemStack = this.ess.getItemDb().get(itemName);
                list.add(itemStack.getTypeId());
                continue;
            }
            catch (Exception ex) {
                logger.log(Level.SEVERE, I18n._("unknownItemInList", itemName, configName));
            }
        }
        return list;
    }

    @Override
    public String getProtectString(String configName) {
        return this.config.getString(configName, null);
    }

    @Override
    public boolean getProtectBoolean(String configName, boolean def) {
        return this.config.getBoolean(configName, def);
    }

    private BigDecimal _getMaxMoney() {
        return this.config.getBigDecimal("max-money", MAXMONEY);
    }

    @Override
    public BigDecimal getMaxMoney() {
        return this.maxMoney;
    }

    private BigDecimal _getMinMoney() {
        BigDecimal min = this.config.getBigDecimal("min-money", MINMONEY);
        if (min.signum() > 0) {
            min = min.negate();
        }
        return min;
    }

    @Override
    public BigDecimal getMinMoney() {
        return this.minMoney;
    }

    @Override
    public boolean isEcoLogEnabled() {
        return this.economyLog;
    }

    public boolean _isEcoLogEnabled() {
        return this.config.getBoolean("economy-log-enabled", false);
    }

    @Override
    public boolean isEcoLogUpdateEnabled() {
        return this.economyLogUpdate;
    }

    public boolean _isEcoLogUpdateEnabled() {
        return this.config.getBoolean("economy-log-update-enabled", false);
    }

    @Override
    public boolean removeGodOnDisconnect() {
        return this.config.getBoolean("remove-god-on-disconnect", false);
    }

    private boolean _changeDisplayName() {
        return this.config.getBoolean("change-displayname", true);
    }

    @Override
    public boolean changeDisplayName() {
        return this.changeDisplayName;
    }

    private boolean _changePlayerListName() {
        return this.config.getBoolean("change-playerlist", false);
    }

    @Override
    public boolean changePlayerListName() {
        return this.changePlayerListName;
    }

    @Override
    public boolean useBukkitPermissions() {
        return this.config.getBoolean("use-bukkit-permissions", false);
    }

    private boolean _addPrefixSuffix() {
        return this.config.getBoolean("add-prefix-suffix", false);
    }

    private boolean _isPrefixSuffixConfigured() {
        return this.config.hasProperty("add-prefix-suffix");
    }

    @Override
    public void setEssentialsChatActive(boolean essentialsChatActive) {
        this.essentialsChatActive = essentialsChatActive;
    }

    @Override
    public boolean addPrefixSuffix() {
        return this.prefixsuffixconfigured ? this.addprefixsuffix : this.essentialsChatActive;
    }

    private boolean _disablePrefix() {
        return this.config.getBoolean("disablePrefix", false);
    }

    @Override
    public boolean disablePrefix() {
        return this.disablePrefix;
    }

    private boolean _disableSuffix() {
        return this.config.getBoolean("disableSuffix", false);
    }

    @Override
    public boolean disableSuffix() {
        return this.disableSuffix;
    }

    @Override
    public long getAutoAfk() {
        return this.config.getLong("auto-afk", 300);
    }

    @Override
    public long getAutoAfkKick() {
        return this.config.getLong("auto-afk-kick", -1);
    }

    @Override
    public boolean getFreezeAfkPlayers() {
        return this.getFreezeAfkPlayers;
    }

    private boolean _getFreezeAfkPlayers() {
        return this.config.getBoolean("freeze-afk-players", false);
    }

    @Override
    public boolean cancelAfkOnMove() {
        return this.cancelAfkOnMove;
    }

    private boolean _cancelAfkOnMove() {
        return this.config.getBoolean("cancel-afk-on-move", true);
    }

    @Override
    public boolean cancelAfkOnInteract() {
        return this.cancelAfkOnInteract;
    }

    private boolean _cancelAfkOnInteract() {
        return this.config.getBoolean("cancel-afk-on-interact", true);
    }

    @Override
    public boolean areDeathMessagesEnabled() {
        return this.config.getBoolean("death-messages", true);
    }

    @Override
    public Set<String> getNoGodWorlds() {
        return this.noGodWorlds;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public boolean getRepairEnchanted() {
        return this.config.getBoolean("repair-enchanted", true);
    }

    @Override
    public boolean allowUnsafeEnchantments() {
        return this.config.getBoolean("unsafe-enchantments", false);
    }

    @Override
    public boolean isWorldTeleportPermissions() {
        return this.config.getBoolean("world-teleport-permissions", false);
    }

    @Override
    public boolean isWorldHomePermissions() {
        return this.config.getBoolean("world-home-permissions", false);
    }

    @Override
    public boolean registerBackInListener() {
        return this.registerBackInListener;
    }

    private boolean _registerBackInListener() {
        return this.config.getBoolean("register-back-in-listener", false);
    }

    @Override
    public boolean getDisableItemPickupWhileAfk() {
        return this.disableItemPickupWhileAfk;
    }

    private boolean _getDisableItemPickupWhileAfk() {
        return this.config.getBoolean("disable-item-pickup-while-afk", false);
    }

    @Override
    public EventPriority getRespawnPriority() {
        String priority = this.config.getString("respawn-listener-priority", "normal").toLowerCase(Locale.ENGLISH);
        if ("lowest".equals(priority)) {
            return EventPriority.LOWEST;
        }
        if ("low".equals(priority)) {
            return EventPriority.LOW;
        }
        if ("normal".equals(priority)) {
            return EventPriority.NORMAL;
        }
        if ("high".equals(priority)) {
            return EventPriority.HIGH;
        }
        if ("highest".equals(priority)) {
            return EventPriority.HIGHEST;
        }
        return EventPriority.NORMAL;
    }

    @Override
    public long getTpaAcceptCancellation() {
        return this.config.getLong("tpa-accept-cancellation", 120);
    }

    @Override
    public boolean isMetricsEnabled() {
        return this.metricsEnabled;
    }

    @Override
    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }

    private long _getTeleportInvulnerability() {
        return this.config.getLong("teleport-invulnerability", 0) * 1000;
    }

    @Override
    public long getTeleportInvulnerability() {
        return this.teleportInvulnerabilityTime;
    }

    private boolean _isTeleportInvulnerability() {
        return this.config.getLong("teleport-invulnerability", 0) > 0;
    }

    @Override
    public boolean isTeleportInvulnerability() {
        return this.teleportInvulnerability;
    }

    private long _getLoginAttackDelay() {
        return this.config.getLong("login-attack-delay", 0) * 1000;
    }

    @Override
    public long getLoginAttackDelay() {
        return this.loginAttackDelay;
    }

    private int _getSignUsePerSecond() {
        int perSec = this.config.getInt("sign-use-per-second", 4);
        return perSec > 0 ? perSec : 1;
    }

    @Override
    public int getSignUsePerSecond() {
        return this.signUsePerSecond;
    }

    @Override
    public double getMaxFlySpeed() {
        double maxSpeed = this.config.getDouble("max-fly-speed", 0.8);
        return maxSpeed > 1.0 ? 1.0 : Math.abs(maxSpeed);
    }

    @Override
    public double getMaxWalkSpeed() {
        double maxSpeed = this.config.getDouble("max-walk-speed", 0.8);
        return maxSpeed > 1.0 ? 1.0 : Math.abs(maxSpeed);
    }

    private int _getMailsPerMinute() {
        return this.config.getInt("mails-per-minute", 1000);
    }

    @Override
    public int getMailsPerMinute() {
        return this.mailsPerMinute;
    }

    private long _getEconomyLagWarning() {
        long value = (long)(this.config.getDouble("economy-lag-warning", 20.0) * 1000000.0);
        return value;
    }

    @Override
    public long getEconomyLagWarning() {
        return this.economyLagWarning;
    }

    @Override
    public long getMaxTempban() {
        return this.config.getLong("max-tempban-time", -1);
    }

    @Override
    public int getMaxNickLength() {
        return this.config.getInt("max-nick-length", 30);
    }

    public boolean _isJoinQuitMessagesDisabled() {
        return this.config.getBoolean("allow-silent-join-quit");
    }

    @Override
    public boolean allowSilentJoinQuit() {
        return this.allowSilentJoin;
    }

    public String _getCustomJoinMessage() {
        return FormatUtil.replaceFormat(this.config.getString("custom-join-message", "none"));
    }

    @Override
    public String getCustomJoinMessage() {
        return this.customJoinMessage;
    }

    @Override
    public boolean isCustomJoinMessage() {
        return this.isCustomJoinMessage;
    }

    public String _getCustomQuitMessage() {
        return FormatUtil.replaceFormat(this.config.getString("custom-quit-message", "none"));
    }

    @Override
    public String getCustomQuitMessage() {
        return this.customQuitMessage;
    }

    @Override
    public boolean isCustomQuitMessage() {
        return this.isCustomQuitMessage;
    }

    @Override
    public int getMaxUserCacheCount() {
        long count = Runtime.getRuntime().maxMemory() / 1024 / 96;
        return this.config.getInt("max-user-cache-count", (int)count);
    }
}

