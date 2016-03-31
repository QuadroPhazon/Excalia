/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandgamemode
extends EssentialsCommand {
    public Commandgamemode() {
        super("gamemode");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length == 0) {
            throw new NotEnoughArgumentsException();
        }
        if (args.length == 1) {
            GameMode gameMode = this.matchGameMode(commandLabel);
            this.gamemodeOtherPlayers(server, sender, gameMode, args[0]);
        } else if (args.length == 2) {
            GameMode gameMode = this.matchGameMode(args[0].toLowerCase(Locale.ENGLISH));
            this.gamemodeOtherPlayers(server, sender, gameMode, args[1]);
        }
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        GameMode gameMode;
        if (args.length == 0) {
            gameMode = this.matchGameMode(commandLabel);
        } else {
            if (args.length > 1 && args[1].trim().length() > 2 && user.isAuthorized("essentials.gamemode.others")) {
                GameMode gameMode2 = this.matchGameMode(args[0].toLowerCase(Locale.ENGLISH));
                this.gamemodeOtherPlayers(server, user.getSource(), gameMode2, args[1]);
                return;
            }
            try {
                gameMode = this.matchGameMode(args[0].toLowerCase(Locale.ENGLISH));
            }
            catch (NotEnoughArgumentsException e) {
                if (user.isAuthorized("essentials.gamemode.others")) {
                    GameMode gameMode3 = this.matchGameMode(commandLabel);
                    this.gamemodeOtherPlayers(server, user.getSource(), gameMode3, args[0]);
                    return;
                }
                throw new NotEnoughArgumentsException();
            }
        }
        if (gameMode == null) {
            gameMode = user.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : (user.getGameMode() == GameMode.CREATIVE ? GameMode.ADVENTURE : GameMode.SURVIVAL);
        }
        user.setGameMode(gameMode);
        user.sendMessage(I18n._("gameMode", I18n._(user.getGameMode().toString().toLowerCase(Locale.ENGLISH), new Object[0]), user.getDisplayName()));
    }

    private void gamemodeOtherPlayers(Server server, CommandSource sender, GameMode gameMode, String name) throws NotEnoughArgumentsException, PlayerNotFoundException {
        if (name.trim().length() < 2 || gameMode == null) {
            throw new NotEnoughArgumentsException(I18n._("gameModeInvalid", new Object[0]));
        }
        boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        boolean foundUser = false;
        List matchedPlayers = server.matchPlayer(name);
        for (Player matchPlayer : matchedPlayers) {
            User player = this.ess.getUser(matchPlayer);
            if (skipHidden && player.isHidden()) continue;
            foundUser = true;
            player.setGameMode(gameMode);
            sender.sendMessage(I18n._("gameMode", I18n._(player.getGameMode().toString().toLowerCase(Locale.ENGLISH), new Object[0]), player.getDisplayName()));
        }
        if (!foundUser) {
            throw new PlayerNotFoundException();
        }
    }

    private GameMode matchGameMode(String modeString) throws NotEnoughArgumentsException {
        GameMode mode = null;
        if (modeString.equalsIgnoreCase("gmc") || modeString.equalsIgnoreCase("egmc") || modeString.contains("creat") || modeString.equalsIgnoreCase("1") || modeString.equalsIgnoreCase("c")) {
            mode = GameMode.CREATIVE;
        } else if (modeString.equalsIgnoreCase("gms") || modeString.equalsIgnoreCase("egms") || modeString.contains("survi") || modeString.equalsIgnoreCase("0") || modeString.equalsIgnoreCase("s")) {
            mode = GameMode.SURVIVAL;
        } else if (modeString.equalsIgnoreCase("gma") || modeString.equalsIgnoreCase("egma") || modeString.contains("advent") || modeString.equalsIgnoreCase("2") || modeString.equalsIgnoreCase("a")) {
            mode = GameMode.ADVENTURE;
        } else if (modeString.equalsIgnoreCase("gmt") || modeString.equalsIgnoreCase("egmt") || modeString.contains("toggle") || modeString.contains("cycle") || modeString.equalsIgnoreCase("t")) {
            mode = null;
        } else {
            throw new NotEnoughArgumentsException();
        }
        return mode;
    }
}

