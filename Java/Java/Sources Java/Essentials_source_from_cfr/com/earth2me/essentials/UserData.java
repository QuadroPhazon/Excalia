/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials;

import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IConf;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.PlayerExtension;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.ess3.api.IEssentials;
import net.ess3.api.InvalidWorldException;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UserData
extends PlayerExtension
implements IConf {
    protected final transient IEssentials ess;
    private final EssentialsConf config;
    private final File folder;
    private BigDecimal money;
    private Map<String, Object> homes;
    private String nickname;
    private List<Integer> unlimited;
    private Map<String, Object> powertools;
    private Location lastLocation;
    private Location logoutLocation;
    private long lastTeleportTimestamp;
    private long lastHealTimestamp;
    private String jail;
    private List<String> mails;
    private boolean teleportEnabled;
    private List<String> ignoredPlayers;
    private boolean godmode;
    private boolean muted;
    private long muteTimeout;
    private boolean jailed;
    private long jailTimeout;
    private long lastLogin;
    private long lastLogout;
    private String lastLoginAddress;
    private boolean afk;
    private boolean newplayer;
    private String geolocation;
    private boolean isSocialSpyEnabled;
    private boolean isNPC;
    private boolean arePowerToolsEnabled;
    private Map<String, Long> kitTimestamps;

    protected UserData(Player base, IEssentials ess) {
        super(base);
        this.ess = ess;
        this.folder = new File(ess.getDataFolder(), "userdata");
        if (!this.folder.exists()) {
            this.folder.mkdirs();
        }
        this.config = new EssentialsConf(new File(this.folder, StringUtil.sanitizeFileName(base.getName()) + ".yml"));
        this.reloadConfig();
    }

    public final void reset() {
        this.config.getFile().delete();
        this.ess.getUserMap().removeUser(this.getBase().getName());
    }

    @Override
    public final void reloadConfig() {
        this.config.load();
        this.money = this._getMoney();
        this.unlimited = this._getUnlimited();
        this.powertools = this._getPowertools();
        this.homes = this._getHomes();
        this.lastLocation = this._getLastLocation();
        this.lastTeleportTimestamp = this._getLastTeleportTimestamp();
        this.lastHealTimestamp = this._getLastHealTimestamp();
        this.jail = this._getJail();
        this.mails = this._getMails();
        this.teleportEnabled = this._getTeleportEnabled();
        this.godmode = this._getGodModeEnabled();
        this.muted = this._getMuted();
        this.muteTimeout = this._getMuteTimeout();
        this.jailed = this._getJailed();
        this.jailTimeout = this._getJailTimeout();
        this.lastLogin = this._getLastLogin();
        this.lastLogout = this._getLastLogout();
        this.lastLoginAddress = this._getLastLoginAddress();
        this.afk = this._getAfk();
        this.geolocation = this._getGeoLocation();
        this.isSocialSpyEnabled = this._isSocialSpyEnabled();
        this.isNPC = this._isNPC();
        this.arePowerToolsEnabled = this._arePowerToolsEnabled();
        this.kitTimestamps = this._getKitTimestamps();
        this.nickname = this._getNickname();
        this.ignoredPlayers = this._getIgnoredPlayers();
        this.logoutLocation = this._getLogoutLocation();
    }

    private BigDecimal _getMoney() {
        BigDecimal result = this.ess.getSettings().getStartingBalance();
        BigDecimal maxMoney = this.ess.getSettings().getMaxMoney();
        BigDecimal minMoney = this.ess.getSettings().getMinMoney();
        if (this.config.hasProperty("money")) {
            result = this.config.getBigDecimal("money", result);
        }
        if (result.compareTo(maxMoney) > 0) {
            result = maxMoney;
        }
        if (result.compareTo(minMoney) < 0) {
            result = minMoney;
        }
        return result;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    public void setMoney(BigDecimal value, boolean throwError) throws MaxMoneyException {
        this.money = value;
        BigDecimal maxMoney = this.ess.getSettings().getMaxMoney();
        BigDecimal minMoney = this.ess.getSettings().getMinMoney();
        if (this.money.compareTo(maxMoney) > 0) {
            this.money = maxMoney;
            if (throwError) {
                throw new MaxMoneyException();
            }
        }
        if (this.money.compareTo(minMoney) < 0) {
            this.money = minMoney;
        }
        this.config.setProperty("money", this.money);
        this.config.save();
    }

    private Map<String, Object> _getHomes() {
        if (this.config.isConfigurationSection("homes")) {
            return this.config.getConfigurationSection("homes").getValues(false);
        }
        return new HashMap<String, Object>();
    }

    private String getHomeName(String search) {
        if (NumberUtil.isInt(search)) {
            try {
                search = this.getHomes().get(Integer.parseInt(search) - 1);
            }
            catch (NumberFormatException e) {
            }
            catch (IndexOutOfBoundsException e) {
                // empty catch block
            }
        }
        return search;
    }

    public Location getHome(String name) throws Exception {
        String search = this.getHomeName(name);
        return this.config.getLocation("homes." + search, this.getBase().getServer());
    }

    public Location getHome(Location world) {
        try {
            Location loc;
            if (this.getHomes().isEmpty()) {
                return null;
            }
            for (String home : this.getHomes()) {
                loc = this.config.getLocation("homes." + home, this.getBase().getServer());
                if (world.getWorld() != loc.getWorld()) continue;
                return loc;
            }
            loc = this.config.getLocation("homes." + this.getHomes().get(0), this.getBase().getServer());
            return loc;
        }
        catch (InvalidWorldException ex) {
            return null;
        }
    }

    public List<String> getHomes() {
        return new ArrayList<String>(this.homes.keySet());
    }

    public void setHome(String name, Location loc) {
        name = StringUtil.safeString(name);
        this.homes.put(name, (Object)loc);
        this.config.setProperty("homes." + name, loc);
        this.config.save();
    }

    public void delHome(String name) throws Exception {
        String search = this.getHomeName(name);
        if (!this.homes.containsKey(search)) {
            search = StringUtil.safeString(search);
        }
        if (!this.homes.containsKey(search)) {
            throw new Exception(I18n._("invalidHome", search));
        }
        this.homes.remove(search);
        this.config.removeProperty("homes." + search);
        this.config.save();
    }

    public boolean hasHome() {
        if (this.config.hasProperty("home")) {
            return true;
        }
        return false;
    }

    public String _getNickname() {
        return this.config.getString("nickname");
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
        this.config.setProperty("nickname", nick);
        this.config.save();
    }

    private List<Integer> _getUnlimited() {
        return this.config.getIntegerList("unlimited");
    }

    public List<Integer> getUnlimited() {
        return this.unlimited;
    }

    public boolean hasUnlimited(ItemStack stack) {
        return this.unlimited.contains(stack.getTypeId());
    }

    public void setUnlimited(ItemStack stack, boolean state) {
        if (this.unlimited.contains(stack.getTypeId())) {
            this.unlimited.remove((Object)stack.getTypeId());
        }
        if (state) {
            this.unlimited.add(stack.getTypeId());
        }
        this.config.setProperty("unlimited", this.unlimited);
        this.config.save();
    }

    private Map<String, Object> _getPowertools() {
        if (this.config.isConfigurationSection("powertools")) {
            return this.config.getConfigurationSection("powertools").getValues(false);
        }
        return new HashMap<String, Object>();
    }

    public void clearAllPowertools() {
        this.powertools.clear();
        this.config.setProperty("powertools", this.powertools);
        this.config.save();
    }

    public List<String> getPowertool(ItemStack stack) {
        return (List)this.powertools.get("" + stack.getTypeId());
    }

    public List<String> getPowertool(int id) {
        return (List)this.powertools.get("" + id);
    }

    public void setPowertool(ItemStack stack, List<String> commandList) {
        if (commandList == null || commandList.isEmpty()) {
            this.powertools.remove("" + stack.getTypeId());
        } else {
            this.powertools.put("" + stack.getTypeId(), commandList);
        }
        this.config.setProperty("powertools", this.powertools);
        this.config.save();
    }

    public boolean hasPowerTools() {
        return !this.powertools.isEmpty();
    }

    private Location _getLastLocation() {
        try {
            return this.config.getLocation("lastlocation", this.getBase().getServer());
        }
        catch (InvalidWorldException e) {
            return null;
        }
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    public void setLastLocation(Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return;
        }
        this.lastLocation = loc;
        this.config.setProperty("lastlocation", loc);
        this.config.save();
    }

    private Location _getLogoutLocation() {
        try {
            return this.config.getLocation("logoutlocation", this.getBase().getServer());
        }
        catch (InvalidWorldException e) {
            return null;
        }
    }

    public Location getLogoutLocation() {
        return this.logoutLocation;
    }

    public void setLogoutLocation(Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return;
        }
        this.logoutLocation = loc;
        this.config.setProperty("logoutlocation", loc);
        this.config.save();
    }

    private long _getLastTeleportTimestamp() {
        return this.config.getLong("timestamps.lastteleport", 0);
    }

    public long getLastTeleportTimestamp() {
        return this.lastTeleportTimestamp;
    }

    public void setLastTeleportTimestamp(long time) {
        this.lastTeleportTimestamp = time;
        this.config.setProperty("timestamps.lastteleport", time);
        this.config.save();
    }

    private long _getLastHealTimestamp() {
        return this.config.getLong("timestamps.lastheal", 0);
    }

    public long getLastHealTimestamp() {
        return this.lastHealTimestamp;
    }

    public void setLastHealTimestamp(long time) {
        this.lastHealTimestamp = time;
        this.config.setProperty("timestamps.lastheal", time);
        this.config.save();
    }

    private String _getJail() {
        return this.config.getString("jail");
    }

    public String getJail() {
        return this.jail;
    }

    public void setJail(String jail) {
        if (jail == null || jail.isEmpty()) {
            this.jail = null;
            this.config.removeProperty("jail");
        } else {
            this.jail = jail;
            this.config.setProperty("jail", jail);
        }
        this.config.save();
    }

    private List<String> _getMails() {
        return this.config.getStringList("mail");
    }

    public List<String> getMails() {
        return this.mails;
    }

    public void setMails(List<String> mails) {
        if (mails == null) {
            this.config.removeProperty("mail");
            mails = this._getMails();
        } else {
            this.config.setProperty("mail", mails);
        }
        this.mails = mails;
        this.config.save();
    }

    public void addMail(String mail) {
        this.mails.add(mail);
        this.setMails(this.mails);
    }

    private boolean _getTeleportEnabled() {
        return this.config.getBoolean("teleportenabled", true);
    }

    public boolean isTeleportEnabled() {
        return this.teleportEnabled;
    }

    public void setTeleportEnabled(boolean set) {
        this.teleportEnabled = set;
        this.config.setProperty("teleportenabled", set);
        this.config.save();
    }

    public List<String> _getIgnoredPlayers() {
        return Collections.synchronizedList(this.config.getStringList("ignore"));
    }

    public void setIgnoredPlayers(List<String> players) {
        if (players == null || players.isEmpty()) {
            this.ignoredPlayers = Collections.synchronizedList(new ArrayList());
            this.config.removeProperty("ignore");
        } else {
            this.ignoredPlayers = players;
            this.config.setProperty("ignore", players);
        }
        this.config.save();
    }

    @Deprecated
    public boolean isIgnoredPlayer(String userName) {
        User user = this.ess.getUser(userName);
        if (user == null || !user.getBase().isOnline()) {
            return false;
        }
        return this.isIgnoredPlayer(user);
    }

    public boolean isIgnoredPlayer(IUser user) {
        return this.ignoredPlayers.contains(user.getName().toLowerCase(Locale.ENGLISH)) && !user.isIgnoreExempt();
    }

    public void setIgnoredPlayer(IUser user, boolean set) {
        if (set) {
            this.ignoredPlayers.add(user.getName().toLowerCase(Locale.ENGLISH));
        } else {
            this.ignoredPlayers.remove(user.getName().toLowerCase(Locale.ENGLISH));
        }
        this.setIgnoredPlayers(this.ignoredPlayers);
    }

    private boolean _getGodModeEnabled() {
        return this.config.getBoolean("godmode", false);
    }

    public boolean isGodModeEnabled() {
        return this.godmode;
    }

    public void setGodModeEnabled(boolean set) {
        this.godmode = set;
        this.config.setProperty("godmode", set);
        this.config.save();
    }

    public boolean _getMuted() {
        return this.config.getBoolean("muted", false);
    }

    public boolean getMuted() {
        return this.muted;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public void setMuted(boolean set) {
        this.muted = set;
        this.config.setProperty("muted", set);
        this.config.save();
    }

    private long _getMuteTimeout() {
        return this.config.getLong("timestamps.mute", 0);
    }

    public long getMuteTimeout() {
        return this.muteTimeout;
    }

    public void setMuteTimeout(long time) {
        this.muteTimeout = time;
        this.config.setProperty("timestamps.mute", time);
        this.config.save();
    }

    private boolean _getJailed() {
        return this.config.getBoolean("jailed", false);
    }

    public boolean isJailed() {
        return this.jailed;
    }

    public void setJailed(boolean set) {
        this.jailed = set;
        this.config.setProperty("jailed", set);
        this.config.save();
    }

    public boolean toggleJailed() {
        boolean ret = !this.isJailed();
        this.setJailed(ret);
        return ret;
    }

    private long _getJailTimeout() {
        return this.config.getLong("timestamps.jail", 0);
    }

    public long getJailTimeout() {
        return this.jailTimeout;
    }

    public void setJailTimeout(long time) {
        this.jailTimeout = time;
        this.config.setProperty("timestamps.jail", time);
        this.config.save();
    }

    public String getBanReason() {
        return this.config.getString("ban.reason", "");
    }

    public void setBanReason(String reason) {
        this.config.setProperty("ban.reason", StringUtil.sanitizeString(reason));
        this.config.save();
    }

    public long getBanTimeout() {
        return this.config.getLong("ban.timeout", 0);
    }

    public void setBanTimeout(long time) {
        this.config.setProperty("ban.timeout", time);
        this.config.save();
    }

    private long _getLastLogin() {
        return this.config.getLong("timestamps.login", 0);
    }

    public long getLastLogin() {
        return this.lastLogin;
    }

    private void _setLastLogin(long time) {
        this.lastLogin = time;
        this.config.setProperty("timestamps.login", time);
    }

    public void setLastLogin(long time) {
        this._setLastLogin(time);
        if (this.base.getAddress() != null && this.base.getAddress().getAddress() != null) {
            this._setLastLoginAddress(this.base.getAddress().getAddress().getHostAddress());
        }
        this.config.save();
    }

    private long _getLastLogout() {
        return this.config.getLong("timestamps.logout", 0);
    }

    public long getLastLogout() {
        return this.lastLogout;
    }

    public void setLastLogout(long time) {
        this.lastLogout = time;
        this.config.setProperty("timestamps.logout", time);
        this.config.save();
    }

    private String _getLastLoginAddress() {
        return this.config.getString("ipAddress", "");
    }

    public String getLastLoginAddress() {
        return this.lastLoginAddress;
    }

    private void _setLastLoginAddress(String address) {
        this.lastLoginAddress = address;
        this.config.setProperty("ipAddress", address);
    }

    private boolean _getAfk() {
        return this.config.getBoolean("afk", false);
    }

    public boolean isAfk() {
        return this.afk;
    }

    public void _setAfk(boolean set) {
        this.afk = set;
        this.config.setProperty("afk", set);
        this.config.save();
    }

    private String _getGeoLocation() {
        return this.config.getString("geolocation");
    }

    public String getGeoLocation() {
        return this.geolocation;
    }

    public void setGeoLocation(String geolocation) {
        if (geolocation == null || geolocation.isEmpty()) {
            this.geolocation = null;
            this.config.removeProperty("geolocation");
        } else {
            this.geolocation = geolocation;
            this.config.setProperty("geolocation", geolocation);
        }
        this.config.save();
    }

    private boolean _isSocialSpyEnabled() {
        return this.config.getBoolean("socialspy", false);
    }

    public boolean isSocialSpyEnabled() {
        return this.isSocialSpyEnabled;
    }

    public void setSocialSpyEnabled(boolean status) {
        this.isSocialSpyEnabled = status;
        this.config.setProperty("socialspy", status);
        this.config.save();
    }

    private boolean _isNPC() {
        return this.config.getBoolean("npc", false);
    }

    public boolean isNPC() {
        return this.isNPC;
    }

    public void setNPC(boolean set) {
        this.isNPC = set;
        this.config.setProperty("npc", set);
        this.config.save();
    }

    public boolean arePowerToolsEnabled() {
        return this.arePowerToolsEnabled;
    }

    public void setPowerToolsEnabled(boolean set) {
        this.arePowerToolsEnabled = set;
        this.config.setProperty("powertoolsenabled", set);
        this.config.save();
    }

    public boolean togglePowerToolsEnabled() {
        boolean ret = !this.arePowerToolsEnabled();
        this.setPowerToolsEnabled(ret);
        return ret;
    }

    private boolean _arePowerToolsEnabled() {
        return this.config.getBoolean("powertoolsenabled", true);
    }

    private Map<String, Long> _getKitTimestamps() {
        if (this.config.isConfigurationSection("timestamps.kits")) {
            ConfigurationSection section = this.config.getConfigurationSection("timestamps.kits");
            HashMap<String, Long> timestamps = new HashMap<String, Long>();
            for (String command : section.getKeys(false)) {
                if (section.isLong(command)) {
                    timestamps.put(command.toLowerCase(Locale.ENGLISH), section.getLong(command));
                    continue;
                }
                if (!section.isInt(command)) continue;
                timestamps.put(command.toLowerCase(Locale.ENGLISH), Long.valueOf(section.getInt(command)));
            }
            return timestamps;
        }
        return new HashMap<String, Long>();
    }

    public long getKitTimestamp(String name) {
        name = name.replace('.', '_').replace('/', '_');
        if (this.kitTimestamps != null && this.kitTimestamps.containsKey(name)) {
            return this.kitTimestamps.get(name);
        }
        return 0;
    }

    public void setKitTimestamp(String name, long time) {
        this.kitTimestamps.put(name.toLowerCase(Locale.ENGLISH), time);
        this.config.setProperty("timestamps.kits", this.kitTimestamps);
        this.config.save();
    }

    public void trackUUID() {
        this.config.setProperty("uuid", this.base.getUniqueId().toString());
        this.config.save();
    }

    public void setConfigProperty(String node, Object object) {
        String prefix = "info.";
        node = "info." + node;
        if (object instanceof Map) {
            this.config.setProperty(node, (Map)object);
        } else if (object instanceof List) {
            this.config.setProperty(node, (List)object);
        } else if (object instanceof Location) {
            this.config.setProperty(node, (Location)object);
        } else if (object instanceof ItemStack) {
            this.config.setProperty(node, (ItemStack)object);
        } else {
            this.config.setProperty(node, object);
        }
        this.config.save();
    }

    public Set<String> getConfigKeys() {
        if (this.config.isConfigurationSection("info")) {
            return this.config.getConfigurationSection("info").getKeys(true);
        }
        return new HashSet<String>();
    }

    public Map<String, Object> getConfigMap() {
        if (this.config.isConfigurationSection("info")) {
            return this.config.getConfigurationSection("info").getValues(true);
        }
        return new HashMap<String, Object>();
    }

    public Map<String, Object> getConfigMap(String node) {
        if (this.config.isConfigurationSection("info." + node)) {
            return this.config.getConfigurationSection("info." + node).getValues(true);
        }
        return new HashMap<String, Object>();
    }

    public void save() {
        this.config.save();
    }
}

