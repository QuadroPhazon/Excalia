/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerExemptException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import java.util.List;
import java.util.Set;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public abstract class EssentialsLoopCommand
extends EssentialsCommand {
    public EssentialsLoopCommand(String command) {
        super(command);
    }

    protected void loopOfflinePlayers(Server server, CommandSource sender, boolean multipleStringMatches, boolean matchWildcards, String searchTerm, String[] commandArgs) throws PlayerNotFoundException, NotEnoughArgumentsException, PlayerExemptException, ChargeException, MaxMoneyException {
        if (searchTerm.isEmpty()) {
            throw new PlayerNotFoundException();
        }
        if (matchWildcards && searchTerm.contentEquals("**")) {
            for (String sUser : this.ess.getUserMap().getAllUniqueUsers()) {
                User matchedUser = this.ess.getUser(sUser);
                this.updatePlayer(server, sender, matchedUser, commandArgs);
            }
        } else if (matchWildcards && searchTerm.contentEquals("*")) {
            boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
            for (Player onlinePlayer : server.getOnlinePlayers()) {
                User onlineUser = this.ess.getUser(onlinePlayer);
                if (skipHidden && onlineUser.isHidden()) continue;
                this.updatePlayer(server, sender, onlineUser, commandArgs);
            }
        } else if (multipleStringMatches) {
            if (searchTerm.trim().length() < 3) {
                throw new PlayerNotFoundException();
            }
            List matchedPlayers = server.matchPlayer(searchTerm);
            if (matchedPlayers.isEmpty()) {
                User matchedUser = this.getPlayer(server, searchTerm, true, true);
                this.updatePlayer(server, sender, matchedUser, commandArgs);
            }
            for (Player matchPlayer : matchedPlayers) {
                User matchedUser = this.ess.getUser(matchPlayer);
                this.updatePlayer(server, sender, matchedUser, commandArgs);
            }
        } else {
            User user = this.getPlayer(server, searchTerm, true, true);
            this.updatePlayer(server, sender, user, commandArgs);
        }
    }

    protected void loopOnlinePlayers(Server server, CommandSource sender, boolean multipleStringMatches, boolean matchWildcards, String searchTerm, String[] commandArgs) throws PlayerNotFoundException, NotEnoughArgumentsException, PlayerExemptException, ChargeException, MaxMoneyException {
        boolean skipHidden;
        if (searchTerm.isEmpty()) {
            throw new PlayerNotFoundException();
        }
        boolean bl = skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        if (matchWildcards && (searchTerm.contentEquals("**") || searchTerm.contentEquals("*"))) {
            for (Player onlinePlayer : server.getOnlinePlayers()) {
                User onlineUser = this.ess.getUser(onlinePlayer);
                if (skipHidden && onlineUser.isHidden()) continue;
                this.updatePlayer(server, sender, onlineUser, commandArgs);
            }
        } else if (multipleStringMatches) {
            if (searchTerm.trim().length() < 2) {
                throw new PlayerNotFoundException();
            }
            boolean foundUser = false;
            List matchedPlayers = server.matchPlayer(searchTerm);
            for (Player matchPlayer : matchedPlayers) {
                User player = this.ess.getUser(matchPlayer);
                if (skipHidden && player.isHidden()) continue;
                foundUser = true;
                this.updatePlayer(server, sender, player, commandArgs);
            }
            if (!foundUser) {
                throw new PlayerNotFoundException();
            }
        } else {
            User player = this.getPlayer(server, searchTerm, !skipHidden, false);
            this.updatePlayer(server, sender, player, commandArgs);
        }
    }

    protected abstract void updatePlayer(Server var1, CommandSource var2, User var3, String[] var4) throws NotEnoughArgumentsException, PlayerExemptException, ChargeException, MaxMoneyException;
}

