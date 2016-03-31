/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.PlayerList;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.perm.PermissionsHandler;
import com.earth2me.essentials.utils.FormatUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandlist
extends EssentialsCommand {
    public Commandlist() {
        super("list");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        boolean showHidden = true;
        if (sender.isPlayer()) {
            showHidden = this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.list.hidden") || this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        }
        sender.sendMessage(PlayerList.listSummary(this.ess, showHidden));
        Map<String, List<User>> playerList = PlayerList.getPlayerLists(this.ess, showHidden);
        if (args.length > 0) {
            sender.sendMessage(PlayerList.listGroupUsers(this.ess, playerList, args[0].toLowerCase()));
        } else {
            this.sendGroupedList(sender, commandLabel, playerList);
        }
    }

    private void sendGroupedList(CommandSource sender, String commandLabel, Map<String, List<User>> playerList) {
        Set<String> configGroups = this.ess.getSettings().getListGroupConfig().keySet();
        ArrayList<String> asterisk = new ArrayList<String>();
        for (String oConfigGroup : configGroups) {
            String groupValue = this.ess.getSettings().getListGroupConfig().get(oConfigGroup).toString().trim();
            String configGroup = oConfigGroup.toLowerCase();
            if (groupValue.equals("*")) {
                asterisk.add(oConfigGroup);
                continue;
            }
            if (groupValue.equalsIgnoreCase("hidden")) {
                playerList.remove(configGroup);
                continue;
            }
            List outputUserList = new ArrayList();
            List<User> matchedList = playerList.get(configGroup);
            if (NumberUtil.isInt(groupValue) && matchedList != null && !matchedList.isEmpty()) {
                playerList.remove(configGroup);
                outputUserList.addAll(matchedList);
                int limit = Integer.parseInt(groupValue);
                if (matchedList.size() > limit) {
                    sender.sendMessage(PlayerList.outputFormat(oConfigGroup, I18n._("groupNumber", matchedList.size(), commandLabel, FormatUtil.stripFormat(configGroup))));
                    continue;
                }
                sender.sendMessage(PlayerList.outputFormat(oConfigGroup, PlayerList.listUsers(this.ess, outputUserList, ", ")));
                continue;
            }
            outputUserList = PlayerList.getMergedList(this.ess, playerList, configGroup);
            if (outputUserList == null || outputUserList.isEmpty()) continue;
            sender.sendMessage(PlayerList.outputFormat(oConfigGroup, PlayerList.listUsers(this.ess, outputUserList, ", ")));
        }
        String[] onlineGroups = playerList.keySet().toArray(new String[0]);
        Arrays.sort(onlineGroups, String.CASE_INSENSITIVE_ORDER);
        if (!asterisk.isEmpty()) {
            ArrayList asteriskUsers = new ArrayList();
            for (String onlineGroup : onlineGroups) {
                asteriskUsers.addAll(playerList.get(onlineGroup));
            }
            for (String key : asterisk) {
                playerList.put(key, asteriskUsers);
            }
            onlineGroups = asterisk.toArray(new String[0]);
        }
        for (String onlineGroup : onlineGroups) {
            String groupName;
            List<User> users = playerList.get(onlineGroup);
            String string = groupName = asterisk.isEmpty() ? users.get(0).getGroup() : onlineGroup;
            if (this.ess.getPermissionsHandler().getName().equals("ConfigPermissions")) {
                groupName = I18n._("connectedPlayers", new Object[0]);
            }
            if (users == null || users.isEmpty()) continue;
            sender.sendMessage(PlayerList.outputFormat(groupName, PlayerList.listUsers(this.ess, users, ", ")));
        }
    }
}

