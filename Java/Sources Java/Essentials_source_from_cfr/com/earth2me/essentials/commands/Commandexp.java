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
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandexp
extends EssentialsCommand {
    public Commandexp() {
        super("exp");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length == 0) {
            this.showExp(user.getSource(), user);
        } else if (args.length > 1 && args[0].equalsIgnoreCase("set") && user.isAuthorized("essentials.exp.set")) {
            if (args.length == 3 && user.isAuthorized("essentials.exp.set.others")) {
                this.expMatch(server, user.getSource(), args[1], args[2], false);
            } else {
                this.setExp(user.getSource(), user, args[1], false);
            }
        } else if (args.length > 1 && args[0].equalsIgnoreCase("give") && user.isAuthorized("essentials.exp.give")) {
            if (args.length == 3 && user.isAuthorized("essentials.exp.give.others")) {
                this.expMatch(server, user.getSource(), args[1], args[2], true);
            } else {
                this.setExp(user.getSource(), user, args[1], true);
            }
        } else if (args[0].equalsIgnoreCase("show")) {
            if (args.length >= 2 && user.isAuthorized("essentials.exp.others")) {
                String match = args[1].trim();
                this.showMatch(server, user.getSource(), match);
            } else {
                this.showExp(user.getSource(), user);
            }
        } else if (args.length >= 1 && NumberUtil.isInt(args[0].toLowerCase(Locale.ENGLISH).replace("l", "")) && user.isAuthorized("essentials.exp.give")) {
            if (args.length >= 2 && user.isAuthorized("essentials.exp.give.others")) {
                this.expMatch(server, user.getSource(), args[1], args[0], true);
            } else {
                this.setExp(user.getSource(), user, args[0], true);
            }
        } else if (args.length >= 1 && user.isAuthorized("essentials.exp.others")) {
            String match = args[0].trim();
            this.showMatch(server, user.getSource(), match);
        } else {
            this.showExp(user.getSource(), user);
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (args.length > 2 && args[0].equalsIgnoreCase("set")) {
            this.expMatch(server, sender, args[1], args[2], false);
        } else if (args.length > 2 && args[0].equalsIgnoreCase("give")) {
            this.expMatch(server, sender, args[1], args[2], true);
        } else {
            String match = args[0].trim();
            if (args.length >= 2 && NumberUtil.isInt(args[0].toLowerCase(Locale.ENGLISH).replace("l", ""))) {
                match = args[1].trim();
                this.expMatch(server, sender, match, args[0], true);
            } else if (args.length == 1) {
                match = args[0].trim();
            }
            this.showMatch(server, sender, match);
        }
    }

    private void showMatch(Server server, CommandSource sender, String match) throws PlayerNotFoundException {
        boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        boolean foundUser = false;
        List matchedPlayers = server.matchPlayer(match);
        for (Player matchPlayer : matchedPlayers) {
            User player = this.ess.getUser(matchPlayer);
            if (skipHidden && player.isHidden()) continue;
            foundUser = true;
            this.showExp(sender, player);
        }
        if (!foundUser) {
            throw new PlayerNotFoundException();
        }
    }

    private void expMatch(Server server, CommandSource sender, String match, String amount, boolean give) throws NotEnoughArgumentsException, PlayerNotFoundException {
        boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        boolean foundUser = false;
        List matchedPlayers = server.matchPlayer(match);
        for (Player matchPlayer : matchedPlayers) {
            User player = this.ess.getUser(matchPlayer);
            if (skipHidden && player.isHidden()) continue;
            foundUser = true;
            this.setExp(sender, player, amount, give);
        }
        if (!foundUser) {
            throw new PlayerNotFoundException();
        }
    }

    private void showExp(CommandSource sender, User target) {
        sender.sendMessage(I18n._("exp", target.getDisplayName(), SetExpFix.getTotalExperience(target.getBase()), target.getLevel(), SetExpFix.getExpUntilNextLevel(target.getBase())));
    }

    private void setExp(CommandSource sender, User target, String strAmount, boolean give) throws NotEnoughArgumentsException {
        long amount;
        if ((strAmount = strAmount.toLowerCase(Locale.ENGLISH)).contains("l")) {
            strAmount = strAmount.replaceAll("l", "");
            int neededLevel = Integer.parseInt(strAmount);
            if (give) {
                neededLevel += target.getLevel();
            }
            amount = SetExpFix.getExpToLevel(neededLevel);
            SetExpFix.setTotalExperience(target.getBase(), 0);
        } else {
            amount = Long.parseLong(strAmount);
            if (amount > Integer.MAX_VALUE || amount < Integer.MIN_VALUE) {
                throw new NotEnoughArgumentsException();
            }
        }
        if (give) {
            amount += (long)SetExpFix.getTotalExperience(target.getBase());
        }
        if (amount > Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE;
        }
        if (amount < 0) {
            amount = 0;
        }
        SetExpFix.setTotalExperience(target.getBase(), (int)amount);
        sender.sendMessage(I18n._("expSet", target.getDisplayName(), amount));
    }
}

