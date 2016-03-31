/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.FormatUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Set;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Commandseen
extends EssentialsCommand {
    public Commandseen() {
        super("seen");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.seen(server, sender, args, true, true, true);
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        this.seen(server, user.getSource(), args, user.isAuthorized("essentials.seen.banreason"), user.isAuthorized("essentials.seen.extra"), user.isAuthorized("essentials.seen.ipsearch"));
    }

    protected void seen(Server server, CommandSource sender, String[] args, boolean showBan, boolean extra, boolean ipLookup) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        try {
            User user = this.getPlayer(server, sender, args, 0);
            this.seenOnline(server, sender, user, showBan, extra);
        }
        catch (NoSuchFieldException e) {
            User player = this.ess.getOfflineUser(args[0]);
            if (player == null) {
                if (ipLookup && FormatUtil.validIP(args[0])) {
                    this.seenIP(server, sender, args[0]);
                    return;
                }
                if (FormatUtil.validIP(args[0]) && server.getIPBans().contains(args[0])) {
                    sender.sendMessage(I18n._("isIpBanned", args[0]));
                    return;
                }
                throw new PlayerNotFoundException();
            }
            this.seenOffline(server, sender, player, showBan, extra);
        }
    }

    private void seenOnline(Server server, CommandSource sender, User user, boolean showBan, boolean extra) throws Exception {
        String location;
        user.setDisplayNick();
        sender.sendMessage(I18n._("seenOnline", user.getDisplayName(), DateUtil.formatDateDiff(user.getLastLogin())));
        if (user.isAfk()) {
            sender.sendMessage(I18n._("whoisAFK", I18n._("true", new Object[0])));
        }
        if (user.isJailed()) {
            Object[] arrobject = new Object[1];
            arrobject[0] = user.getJailTimeout() > 0 ? DateUtil.formatDateDiff(user.getJailTimeout()) : I18n._("true", new Object[0]);
            sender.sendMessage(I18n._("whoisJail", arrobject));
        }
        if (user.isMuted()) {
            Object[] arrobject = new Object[1];
            arrobject[0] = user.getMuteTimeout() > 0 ? DateUtil.formatDateDiff(user.getMuteTimeout()) : I18n._("true", new Object[0]);
            sender.sendMessage(I18n._("whoisMuted", arrobject));
        }
        if ((location = user.getGeoLocation()) != null && (!sender.isPlayer() || this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show"))) {
            sender.sendMessage(I18n._("whoisGeoLocation", location));
        }
        if (extra) {
            sender.sendMessage(I18n._("whoisIPAddress", user.getAddress().getAddress().toString()));
        }
    }

    private void seenOffline(Server server, CommandSource sender, User user, boolean showBan, boolean extra) throws Exception {
        String location;
        user.setDisplayNick();
        if (user.getLastLogout() > 0) {
            sender.sendMessage(I18n._("seenOffline", user.getName(), DateUtil.formatDateDiff(user.getLastLogout())));
        } else {
            sender.sendMessage(I18n._("userUnknown", user.getName()));
        }
        if (user.isBanned()) {
            Object[] arrobject = new Object[1];
            arrobject[0] = showBan ? user.getBanReason() : I18n._("true", new Object[0]);
            sender.sendMessage(I18n._("whoisBanned", arrobject));
        }
        if ((location = user.getGeoLocation()) != null && (!sender.isPlayer() || this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show"))) {
            sender.sendMessage(I18n._("whoisGeoLocation", location));
        }
        if (extra) {
            Location loc;
            if (!user.getLastLoginAddress().isEmpty()) {
                sender.sendMessage(I18n._("whoisIPAddress", user.getLastLoginAddress()));
            }
            if ((loc = user.getLogoutLocation()) != null) {
                sender.sendMessage(I18n._("whoisLocation", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
            }
        }
    }

    private void seenIP(Server server, final CommandSource sender, final String ipAddress) throws Exception {
        final UserMap userMap = this.ess.getUserMap();
        if (server.getIPBans().contains(ipAddress)) {
            sender.sendMessage(I18n._("isIpBanned", ipAddress));
        }
        sender.sendMessage(I18n._("runningPlayerMatch", ipAddress));
        this.ess.runTaskAsynchronously(new Runnable(){

            @Override
            public void run() {
                ArrayList<String> matches = new ArrayList<String>();
                for (String u : userMap.getAllUniqueUsers()) {
                    String uIPAddress;
                    User user = Commandseen.this.ess.getUserMap().getUser(u);
                    if (user == null || (uIPAddress = user.getLastLoginAddress()).isEmpty() || !uIPAddress.equalsIgnoreCase(ipAddress)) continue;
                    matches.add(user.getName());
                }
                if (matches.size() > 0) {
                    sender.sendMessage(I18n._("matchingIPAddress", new Object[0]));
                    sender.sendMessage(StringUtil.joinList(matches));
                } else {
                    sender.sendMessage(I18n._("noMatchingPlayers", new Object[0]));
                }
            }
        });
    }

}

