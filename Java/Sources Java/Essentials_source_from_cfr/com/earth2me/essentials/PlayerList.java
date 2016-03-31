/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PlayerList {
    public static String listUsers(IEssentials ess, List<User> users, String seperator) {
        StringBuilder groupString = new StringBuilder();
        Collections.sort(users);
        boolean needComma = false;
        for (User user : users) {
            if (needComma) {
                groupString.append(seperator);
            }
            needComma = true;
            if (user.isAfk()) {
                groupString.append(I18n._("listAfkTag", new Object[0]));
            }
            if (user.isHidden()) {
                groupString.append(I18n._("listHiddenTag", new Object[0]));
            }
            user.setDisplayNick();
            groupString.append(user.getDisplayName());
            groupString.append("\u00a7f");
        }
        return groupString.toString();
    }

    public static String listSummary(IEssentials ess, boolean showHidden) {
        Server server = ess.getServer();
        int playerHidden = 0;
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            if (!ess.getUser(onlinePlayer).isHidden()) continue;
            ++playerHidden;
        }
        String online = showHidden && playerHidden > 0 ? I18n._("listAmountHidden", server.getOnlinePlayers().length - playerHidden, playerHidden, server.getMaxPlayers()) : I18n._("listAmount", server.getOnlinePlayers().length - playerHidden, server.getMaxPlayers());
        return online;
    }

    public static Map<String, List<User>> getPlayerLists(IEssentials ess, boolean showHidden) {
        Server server = ess.getServer();
        HashMap<String, List<User>> playerList = new HashMap<String, List<User>>();
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            User onlineUser = ess.getUser(onlinePlayer);
            if (onlineUser.isHidden() && !showHidden) continue;
            String group = FormatUtil.stripFormat(FormatUtil.stripEssentialsFormat(onlineUser.getGroup().toLowerCase()));
            List<User> list = playerList.get(group);
            if (list == null) {
                list = new ArrayList<User>();
                playerList.put(group, list);
            }
            list.add(onlineUser);
        }
        return playerList;
    }

    public static List<User> getMergedList(IEssentials ess, Map<String, List<User>> playerList, String groupName) {
        Set<String> configGroups = ess.getSettings().getListGroupConfig().keySet();
        ArrayList<User> users = new ArrayList<User>();
        for (String configGroup : configGroups) {
            String[] groupValues;
            if (!configGroup.equalsIgnoreCase(groupName)) continue;
            for (String groupValue : groupValues = ess.getSettings().getListGroupConfig().get(configGroup).toString().trim().split(" ")) {
                List<User> u;
                if ((groupValue = groupValue.toLowerCase(Locale.ENGLISH)) == null || groupValue.isEmpty() || (u = playerList.get(groupValue.trim())) == null || u.isEmpty()) continue;
                playerList.remove(groupValue);
                users.addAll(u);
            }
        }
        return users;
    }

    public static String listGroupUsers(IEssentials ess, Map<String, List<User>> playerList, String groupName) throws Exception {
        List<User> users = PlayerList.getMergedList(ess, playerList, groupName);
        List<User> groupUsers = playerList.get(groupName);
        if (groupUsers != null && !groupUsers.isEmpty()) {
            users.addAll(groupUsers);
        }
        if (users == null || users.isEmpty()) {
            throw new Exception(I18n._("groupDoesNotExist", new Object[0]));
        }
        StringBuilder displayGroupName = new StringBuilder();
        displayGroupName.append(Character.toTitleCase(groupName.charAt(0)));
        displayGroupName.append(groupName.substring(1));
        return PlayerList.outputFormat(displayGroupName.toString(), PlayerList.listUsers(ess, users, ", "));
    }

    public static String outputFormat(String group, String message) {
        StringBuilder outputString = new StringBuilder();
        outputString.append(I18n._("listGroupTag", FormatUtil.replaceFormat(group)));
        outputString.append(message);
        return outputString.toString();
    }
}

