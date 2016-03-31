/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.WeatherType
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
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

public class Commandpweather
extends EssentialsCommand {
    public static final Set<String> getAliases = new HashSet<String>();
    public static final Map<String, WeatherType> weatherAliases = new HashMap<String, WeatherType>();

    public Commandpweather() {
        super("pweather");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        User user;
        String userSelector = null;
        if (args.length == 2) {
            userSelector = args[1];
        }
        Set<User> users = this.getUsers(server, sender, userSelector);
        if (args.length == 0) {
            this.getUsersWeather(sender, users);
            return;
        }
        if (getAliases.contains(args[0])) {
            this.getUsersWeather(sender, users);
            return;
        }
        if (!(!sender.isPlayer() || (user = this.ess.getUser(sender.getPlayer())) == null || users.contains(user) && users.size() <= 1 || user.isAuthorized("essentials.pweather.others"))) {
            user.sendMessage(I18n._("pWeatherOthersPermission", new Object[0]));
            return;
        }
        this.setUsersWeather(sender, users, args[0].toLowerCase());
    }

    private void getUsersWeather(CommandSource sender, Collection<User> users) {
        if (users.size() > 1) {
            sender.sendMessage(I18n._("pWeatherPlayers", new Object[0]));
        }
        for (User user : users) {
            if (user.getPlayerWeather() == null) {
                sender.sendMessage(I18n._("pWeatherNormal", user.getName()));
                continue;
            }
            sender.sendMessage(I18n._("pWeatherCurrent", user.getName(), user.getPlayerWeather().toString().toLowerCase(Locale.ENGLISH)));
        }
    }

    private void setUsersWeather(CommandSource sender, Collection<User> users, String weatherType) throws Exception {
        StringBuilder msg = new StringBuilder();
        for (User user2 : users) {
            if (msg.length() > 0) {
                msg.append(", ");
            }
            msg.append(user2.getName());
        }
        if (weatherType.equalsIgnoreCase("reset")) {
            for (User user2 : users) {
                user2.resetPlayerWeather();
            }
            sender.sendMessage(I18n._("pWeatherReset", msg));
        } else {
            if (!weatherAliases.containsKey(weatherType)) {
                throw new NotEnoughArgumentsException(I18n._("pWeatherInvalidAlias", new Object[0]));
            }
            for (User user2 : users) {
                user2.setPlayerWeather(weatherAliases.get(weatherType));
            }
            sender.sendMessage(I18n._("pWeatherSet", weatherType, msg.toString()));
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
        weatherAliases.put("sun", WeatherType.CLEAR);
        weatherAliases.put("clear", WeatherType.CLEAR);
        weatherAliases.put("storm", WeatherType.DOWNFALL);
        weatherAliases.put("thunder", WeatherType.DOWNFALL);
    }
}

