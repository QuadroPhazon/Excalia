/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public abstract class EssentialsToggleCommand
extends EssentialsCommand {
    String othersPermission;

    public EssentialsToggleCommand(String command, String othersPermission) {
        super(command);
        this.othersPermission = othersPermission;
    }

    protected Boolean matchToggleArgument(String arg) {
        if (arg.equalsIgnoreCase("on") || arg.startsWith("ena") || arg.equalsIgnoreCase("1")) {
            return true;
        }
        if (arg.equalsIgnoreCase("off") || arg.startsWith("dis") || arg.equalsIgnoreCase("0")) {
            return false;
        }
        return null;
    }

    protected void toggleOtherPlayers(Server server, CommandSource sender, String[] args) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (args.length < 1 || args[0].trim().length() < 2) {
            throw new PlayerNotFoundException();
        }
        boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        boolean foundUser = false;
        List matchedPlayers = server.matchPlayer(args[0]);
        for (Player matchPlayer : matchedPlayers) {
            User player = this.ess.getUser(matchPlayer);
            if (skipHidden && player.isHidden()) continue;
            foundUser = true;
            if (args.length > 1) {
                Boolean toggle = this.matchToggleArgument(args[1]);
                if (toggle.booleanValue()) {
                    this.togglePlayer(sender, player, true);
                    continue;
                }
                this.togglePlayer(sender, player, false);
                continue;
            }
            this.togglePlayer(sender, player, null);
        }
        if (!foundUser) {
            throw new PlayerNotFoundException();
        }
    }

    abstract void togglePlayer(CommandSource var1, User var2, Boolean var3) throws NotEnoughArgumentsException;
}

