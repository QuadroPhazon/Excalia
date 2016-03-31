/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandsethome
extends EssentialsCommand {
    public Commandsethome() {
        super("sethome");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        User usersHome = user;
        String name = "home";
        Location location = user.getLocation();
        if (args.length > 0) {
            String[] nameParts = args[0].split(":");
            if (nameParts[0].length() != args[0].length()) {
                args = nameParts;
            }
            if (args.length < 2) {
                name = args[0].toLowerCase(Locale.ENGLISH);
            } else {
                name = args[1].toLowerCase(Locale.ENGLISH);
                if (user.isAuthorized("essentials.sethome.others") && (usersHome = this.ess.getUser(args[0])) == null) {
                    throw new PlayerNotFoundException();
                }
            }
        }
        if (this.checkHomeLimit(user, usersHome, name)) {
            name = "home";
        }
        if ("bed".equals(name) || NumberUtil.isInt(name)) {
            throw new NoSuchFieldException(I18n._("invalidHomeName", new Object[0]));
        }
        usersHome.setHome(name, location);
        user.sendMessage(I18n._("homeSet", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ(), name));
    }

    private boolean checkHomeLimit(User user, User usersHome, String name) throws Exception {
        if (!user.isAuthorized("essentials.sethome.multiple.unlimited")) {
            int limit = this.ess.getSettings().getHomeLimit(user);
            if (usersHome.getHomes().size() == limit && usersHome.getHomes().contains(name)) {
                return false;
            }
            if (usersHome.getHomes().size() >= limit) {
                throw new Exception(I18n._("maxHomes", this.ess.getSettings().getHomeLimit(user)));
            }
            if (limit == 1) {
                return true;
            }
        }
        return false;
    }
}

