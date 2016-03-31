/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.commands.UserNameComparator;
import com.earth2me.essentials.utils.DescParseTickFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Commandptime
extends EssentialsCommand {
    private static final Set<String> getAliases = new HashSet<String>();

    public Commandptime() {
        super("ptime");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        Long ticks;
        User user;
        String userSelector = null;
        if (args.length == 2) {
            userSelector = args[1];
        }
        Set<User> users = this.getUsers(server, sender, userSelector);
        if (args.length == 0) {
            this.getUsersTime(sender, users);
            return;
        }
        if (!(!sender.isPlayer() || (user = this.ess.getUser(sender.getPlayer())) == null || users.contains(user) && users.size() <= 1 || user.isAuthorized("essentials.ptime.others"))) {
            user.sendMessage(I18n._("pTimeOthersPermission", new Object[0]));
            return;
        }
        String timeParam = args[0];
        boolean relative = true;
        if (timeParam.startsWith("@")) {
            relative = false;
            timeParam = timeParam.substring(1);
        }
        if (getAliases.contains(timeParam)) {
            this.getUsersTime(sender, users);
            return;
        }
        if (DescParseTickFormat.meansReset(timeParam)) {
            ticks = null;
        } else {
            try {
                ticks = DescParseTickFormat.parse(timeParam);
            }
            catch (NumberFormatException e) {
                throw new NotEnoughArgumentsException(e);
            }
        }
        this.setUsersTime(sender, users, ticks, relative);
    }

    private void getUsersTime(CommandSource sender, Collection<User> users) {
        if (users.size() > 1) {
            sender.sendMessage(I18n._("pTimePlayers", new Object[0]));
        }
        for (User user : users) {
            if (user.getPlayerTimeOffset() == 0) {
                sender.sendMessage(I18n._("pTimeNormal", user.getName()));
                continue;
            }
            String time = DescParseTickFormat.format(user.getPlayerTime());
            if (!user.isPlayerTimeRelative()) {
                sender.sendMessage(I18n._("pTimeCurrentFixed", user.getName(), time));
                continue;
            }
            sender.sendMessage(I18n._("pTimeCurrent", user.getName(), time));
        }
    }

    private void setUsersTime(CommandSource sender, Collection<User> users, Long ticks, Boolean relative) {
        if (ticks == null) {
            for (User user : users) {
                user.resetPlayerTime();
            }
        } else {
            for (User user : users) {
                World world = user.getWorld();
                long time = user.getPlayerTime();
                time -= time % 24000;
                time += 24000 + ticks;
                if (relative.booleanValue()) {
                    time -= world.getTime();
                }
                user.setPlayerTime(time, relative);
            }
        }
        StringBuilder msg = new StringBuilder();
        for (User user : users) {
            if (msg.length() > 0) {
                msg.append(", ");
            }
            msg.append(user.getName());
        }
        if (ticks == null) {
            sender.sendMessage(I18n._("pTimeReset", msg.toString()));
        } else {
            String time = DescParseTickFormat.format(ticks);
            if (!relative.booleanValue()) {
                sender.sendMessage(I18n._("pTimeSetFixed", time, msg.toString()));
            } else {
                sender.sendMessage(I18n._("pTimeSet", time, msg.toString()));
            }
        }
    }

    private Set<User> getUsers(Server server, CommandSource sender, String selector) throws Exception {
        TreeSet<User> users = new TreeSet<User>(new UserNameComparator());
        if (selector == null) {
            if (sender.isPlayer()) {
                User user = this.ess.getUser(sender.getPlayer());
                users.add(user);
            } else {
                for (Player player : server.getOnlinePlayers()) {
                    users.add(this.ess.getUser(player));
                }
            }
            return users;
        }
        User user = null;
        List matchedPlayers = server.matchPlayer(selector);
        if (!matchedPlayers.isEmpty()) {
            user = this.ess.getUser((Player)matchedPlayers.get(0));
        }
        if (user != null) {
            users.add(user);
        } else if (selector.equalsIgnoreCase("*") || selector.equalsIgnoreCase("all")) {
            for (Player player : server.getOnlinePlayers()) {
                users.add(this.ess.getUser(player));
            }
        } else {
            throw new PlayerNotFoundException();
        }
        return users;
    }

    static {
        getAliases.add("get");
        getAliases.add("list");
        getAliases.add("show");
        getAliases.add("display");
    }
}

