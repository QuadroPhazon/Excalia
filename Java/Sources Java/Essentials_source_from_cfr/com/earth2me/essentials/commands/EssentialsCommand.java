/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IEssentialsModule;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public abstract class EssentialsCommand
implements IEssentialsCommand {
    private final transient String name;
    protected transient IEssentials ess;
    protected transient IEssentialsModule module;
    protected static final Logger logger = Logger.getLogger("Essentials");

    protected EssentialsCommand(String name) {
        this.name = name;
    }

    @Override
    public void setEssentials(IEssentials ess) {
        this.ess = ess;
    }

    @Override
    public void setEssentialsModule(IEssentialsModule module) {
        this.module = module;
    }

    @Override
    public String getName() {
        return this.name;
    }

    protected User getPlayer(Server server, CommandSource sender, String[] args, int pos) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            return this.getPlayer(server, user, args, pos);
        }
        return this.getPlayer(server, args, pos, true, false);
    }

    protected User getPlayer(Server server, User user, String[] args, int pos) throws PlayerNotFoundException, NotEnoughArgumentsException {
        return this.getPlayer(server, user, args, pos, user.isAuthorized("essentials.vanish.interact"), false);
    }

    protected User getPlayer(Server server, String[] args, int pos, boolean getHidden, boolean getOffline) throws PlayerNotFoundException, NotEnoughArgumentsException {
        return this.getPlayer(server, null, args, pos, getHidden, getOffline);
    }

    private User getPlayer(Server server, User sourceUser, String[] args, int pos, boolean getHidden, boolean getOffline) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (args.length <= pos) {
            throw new NotEnoughArgumentsException();
        }
        if (args[pos].isEmpty()) {
            throw new PlayerNotFoundException();
        }
        return this.getPlayer(server, sourceUser, args[pos], getHidden, getOffline);
    }

    protected User getPlayer(Server server, String searchTerm, boolean getHidden, boolean getOffline) throws PlayerNotFoundException {
        return this.getPlayer(server, null, searchTerm, getHidden, getOffline);
    }

    private User getPlayer(Server server, User sourceUser, String searchTerm, boolean getHidden, boolean getOffline) throws PlayerNotFoundException {
        User user = this.ess.getUser(searchTerm);
        if (user != null) {
            if (!getOffline && !user.isOnline()) {
                throw new PlayerNotFoundException();
            }
            if (!getHidden && user.isHidden() && !user.equals(sourceUser)) {
                throw new PlayerNotFoundException();
            }
            return user;
        }
        List matches = server.matchPlayer(searchTerm);
        if (matches.isEmpty()) {
            String matchText = searchTerm.toLowerCase(Locale.ENGLISH);
            for (Player onlinePlayer : server.getOnlinePlayers()) {
                String displayName;
                User userMatch = this.ess.getUser(onlinePlayer);
                if (!getHidden && userMatch.isHidden() && !userMatch.equals(sourceUser) || !(displayName = FormatUtil.stripFormat(userMatch.getDisplayName()).toLowerCase(Locale.ENGLISH)).contains(matchText)) continue;
                return userMatch;
            }
        } else {
            for (Player player : matches) {
                User userMatch = this.ess.getUser(player);
                if (!userMatch.getDisplayName().startsWith(searchTerm) || !getHidden && userMatch.isHidden() && !userMatch.equals(sourceUser)) continue;
                return userMatch;
            }
            User userMatch = this.ess.getUser((Player)matches.get(0));
            if (getHidden || !userMatch.isHidden() || userMatch.equals(sourceUser)) {
                return userMatch;
            }
        }
        throw new PlayerNotFoundException();
    }

    @Override
    public final void run(Server server, User user, String commandLabel, Command cmd, String[] args) throws Exception {
        Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        this.run(server, user, commandLabel, args);
        charge.charge(user);
    }

    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        this.run(server, user.getSource(), commandLabel, args);
    }

    @Override
    public final void run(Server server, CommandSource sender, String commandLabel, Command cmd, String[] args) throws Exception {
        this.run(server, sender, commandLabel, args);
    }

    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        throw new Exception(I18n._("onlyPlayers", commandLabel));
    }

    public static String getFinalArg(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
}

