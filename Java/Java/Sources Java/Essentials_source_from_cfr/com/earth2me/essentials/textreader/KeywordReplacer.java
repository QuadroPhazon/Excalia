/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.EssentialsTimer;
import com.earth2me.essentials.ExecuteTimer;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.PlayerList;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordCachable;
import com.earth2me.essentials.textreader.KeywordType;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.DescParseTickFormat;
import com.earth2me.essentials.utils.NumberUtil;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class KeywordReplacer
implements IText {
    private static final Pattern KEYWORD = Pattern.compile("\\{([^\\{\\}]+)\\}");
    private static final Pattern KEYWORDSPLIT = Pattern.compile("\\:");
    private final transient IText input;
    private final transient List<String> replaced;
    private final transient IEssentials ess;
    private final transient boolean includePrivate;
    private transient ExecuteTimer execTimer;
    private final EnumMap<KeywordType, Object> keywordCache = new EnumMap(KeywordType.class);

    public KeywordReplacer(IText input, CommandSource sender, IEssentials ess) {
        this.input = input;
        this.replaced = new ArrayList<String>(this.input.getLines().size());
        this.ess = ess;
        this.includePrivate = true;
        this.replaceKeywords(sender);
    }

    public KeywordReplacer(IText input, CommandSource sender, IEssentials ess, boolean showPrivate) {
        this.input = input;
        this.replaced = new ArrayList<String>(this.input.getLines().size());
        this.ess = ess;
        this.includePrivate = showPrivate;
        this.replaceKeywords(sender);
    }

    private void replaceKeywords(CommandSource sender) {
        this.execTimer = new ExecuteTimer();
        this.execTimer.start();
        User user = null;
        if (sender.isPlayer()) {
            user = this.ess.getUser(sender.getPlayer());
            user.setDisplayNick();
        }
        this.execTimer.mark("User Grab");
        for (int i = 0; i < this.input.getLines().size(); ++i) {
            String line = this.input.getLines().get(i);
            Matcher matcher = KEYWORD.matcher(line);
            while (matcher.find()) {
                String fullMatch = matcher.group(0);
                String keywordMatch = matcher.group(1);
                String[] matchTokens = KEYWORDSPLIT.split(keywordMatch);
                line = this.replaceLine(line, fullMatch, matchTokens, user);
            }
            this.replaced.add(line);
        }
        this.execTimer.mark("Text Replace");
        String timeroutput = this.execTimer.end();
        if (this.ess.getSettings().isDebug()) {
            this.ess.getLogger().log(Level.INFO, "Keyword Replacer " + timeroutput);
        }
    }

    private String replaceLine(String line, String fullMatch, String[] matchTokens, User user) {
        String keyword = matchTokens[0];
        try {
            String replacer = null;
            KeywordType validKeyword = KeywordType.valueOf(keyword);
            if (validKeyword.getType().equals((Object)KeywordCachable.CACHEABLE) && this.keywordCache.containsKey((Object)validKeyword)) {
                replacer = this.keywordCache.get((Object)validKeyword).toString();
            } else if (validKeyword.getType().equals((Object)KeywordCachable.SUBVALUE)) {
                Map values;
                String subKeyword = "";
                if (matchTokens.length > 1) {
                    subKeyword = matchTokens[1].toLowerCase(Locale.ENGLISH);
                }
                if (this.keywordCache.containsKey((Object)validKeyword) && (values = (Map)this.keywordCache.get((Object)validKeyword)).containsKey(subKeyword)) {
                    replacer = (String)values.get(subKeyword);
                }
            }
            if (validKeyword.isPrivate() && !this.includePrivate) {
                replacer = "";
            }
            if (replacer == null) {
                replacer = "";
                switch (validKeyword) {
                    case PLAYER: 
                    case DISPLAYNAME: {
                        if (user == null) break;
                        replacer = user.getDisplayName();
                        break;
                    }
                    case USERNAME: {
                        if (user == null) break;
                        replacer = user.getName();
                        break;
                    }
                    case BALANCE: {
                        if (user == null) break;
                        replacer = NumberUtil.displayCurrency(user.getMoney(), this.ess);
                        break;
                    }
                    case MAILS: {
                        if (user == null) break;
                        replacer = Integer.toString(user.getMails().size());
                        break;
                    }
                    case WORLD: 
                    case WORLDNAME: {
                        if (user == null) break;
                        Location location = user.getLocation();
                        replacer = location == null || location.getWorld() == null ? "" : location.getWorld().getName();
                        break;
                    }
                    case ONLINE: {
                        int playerHidden = 0;
                        for (Player p : this.ess.getServer().getOnlinePlayers()) {
                            if (!this.ess.getUser(p).isHidden()) continue;
                            ++playerHidden;
                        }
                        replacer = Integer.toString(this.ess.getServer().getOnlinePlayers().length - playerHidden);
                        break;
                    }
                    case UNIQUE: {
                        replacer = Integer.toString(this.ess.getUserMap().getUniqueUsers());
                        break;
                    }
                    case WORLDS: {
                        StringBuilder worldsBuilder = new StringBuilder();
                        for (World w : this.ess.getServer().getWorlds()) {
                            if (worldsBuilder.length() > 0) {
                                worldsBuilder.append(", ");
                            }
                            worldsBuilder.append(w.getName());
                        }
                        replacer = worldsBuilder.toString();
                        break;
                    }
                    case PLAYERLIST: {
                        HashMap<String, String> outputList;
                        if (this.keywordCache.containsKey((Object)validKeyword)) {
                            outputList = (HashMap<String, String>)this.keywordCache.get((Object)validKeyword);
                        } else {
                            boolean showHidden = user == null ? true : user.isAuthorized("essentials.list.hidden") || user.isAuthorized("essentials.vanish.interact");
                            Map<String, List<User>> playerList = PlayerList.getPlayerLists(this.ess, showHidden);
                            outputList = new HashMap<String, String>();
                            for (String groupName : playerList.keySet()) {
                                List<User> groupUsers = playerList.get(groupName);
                                if (groupUsers == null || groupUsers.isEmpty()) continue;
                                outputList.put(groupName, PlayerList.listUsers(this.ess, groupUsers, " "));
                            }
                            StringBuilder playerlistBuilder = new StringBuilder();
                            for (Player p : this.ess.getServer().getOnlinePlayers()) {
                                if (this.ess.getUser(p).isHidden()) continue;
                                if (playerlistBuilder.length() > 0) {
                                    playerlistBuilder.append(", ");
                                }
                                playerlistBuilder.append(p.getDisplayName());
                            }
                            outputList.put("", playerlistBuilder.toString());
                            this.keywordCache.put(validKeyword, outputList);
                        }
                        if (matchTokens.length == 1) {
                            replacer = (String)outputList.get("");
                        } else if (outputList.containsKey(matchTokens[1].toLowerCase(Locale.ENGLISH))) {
                            replacer = (String)outputList.get(matchTokens[1].toLowerCase(Locale.ENGLISH));
                        } else if (matchTokens.length > 2) {
                            replacer = matchTokens[2];
                        }
                        this.keywordCache.put(validKeyword, outputList);
                        break;
                    }
                    case TIME: {
                        replacer = DateFormat.getTimeInstance(2, this.ess.getI18n().getCurrentLocale()).format(new Date());
                        break;
                    }
                    case DATE: {
                        replacer = DateFormat.getDateInstance(2, this.ess.getI18n().getCurrentLocale()).format(new Date());
                        break;
                    }
                    case WORLDTIME12: {
                        if (user == null) break;
                        replacer = DescParseTickFormat.format12(user.getWorld() == null ? 0 : user.getWorld().getTime());
                        break;
                    }
                    case WORLDTIME24: {
                        if (user == null) break;
                        replacer = DescParseTickFormat.format24(user.getWorld() == null ? 0 : user.getWorld().getTime());
                        break;
                    }
                    case WORLDDATE: {
                        if (user == null) break;
                        replacer = DateFormat.getDateInstance(2, this.ess.getI18n().getCurrentLocale()).format(DescParseTickFormat.ticksToDate(user.getWorld() == null ? 0 : user.getWorld().getFullTime()));
                        break;
                    }
                    case COORDS: {
                        if (user == null) break;
                        Location location = user.getLocation();
                        replacer = I18n._("coordsKeyword", location.getBlockX(), location.getBlockY(), location.getBlockZ());
                        break;
                    }
                    case TPS: {
                        replacer = NumberUtil.formatDouble(this.ess.getTimer().getAverageTPS());
                        break;
                    }
                    case UPTIME: {
                        replacer = DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime());
                        break;
                    }
                    case IP: {
                        if (user == null) break;
                        replacer = user.getAddress() == null || user.getAddress().getAddress() == null ? "" : user.getAddress().getAddress().toString();
                        break;
                    }
                    case ADDRESS: {
                        if (user == null) break;
                        replacer = user.getAddress() == null ? "" : user.getAddress().toString();
                        break;
                    }
                    case PLUGINS: {
                        StringBuilder pluginlistBuilder = new StringBuilder();
                        for (Plugin p : this.ess.getServer().getPluginManager().getPlugins()) {
                            if (pluginlistBuilder.length() > 0) {
                                pluginlistBuilder.append(", ");
                            }
                            pluginlistBuilder.append(p.getDescription().getName());
                        }
                        replacer = pluginlistBuilder.toString();
                        break;
                    }
                    case VERSION: {
                        replacer = this.ess.getServer().getVersion();
                        break;
                    }
                    default: {
                        replacer = "N/A";
                    }
                }
                if (validKeyword.getType().equals((Object)KeywordCachable.CACHEABLE)) {
                    this.keywordCache.put(validKeyword, (String)replacer);
                }
            }
            line = line.replace(fullMatch, replacer);
        }
        catch (IllegalArgumentException ex) {
            // empty catch block
        }
        return line;
    }

    @Override
    public List<String> getLines() {
        return this.replaced;
    }

    @Override
    public List<String> getChapters() {
        return this.input.getChapters();
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return this.input.getBookmarks();
    }

}

