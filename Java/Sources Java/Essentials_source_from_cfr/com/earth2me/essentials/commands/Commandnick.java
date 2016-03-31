/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.Locale;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.events.NickChangeEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

public class Commandnick
extends EssentialsLoopCommand {
    public Commandnick() {
        super("nick");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (!this.ess.getSettings().changeDisplayName()) {
            throw new Exception(I18n._("nickDisplayName", new Object[0]));
        }
        if (args.length > 1 && user.isAuthorized("essentials.nick.others")) {
            String[] nickname = this.formatNickname(user, args[1]).split(" ");
            this.loopOfflinePlayers(server, user.getSource(), false, true, args[0], nickname);
            user.sendMessage(I18n._("nickChanged", new Object[0]));
        } else {
            String[] nickname = this.formatNickname(user, args[0]).split(" ");
            this.updatePlayer(server, user.getSource(), user, nickname);
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        if (!this.ess.getSettings().changeDisplayName()) {
            throw new Exception(I18n._("nickDisplayName", new Object[0]));
        }
        String[] nickname = this.formatNickname(null, args[1]).split(" ");
        this.loopOfflinePlayers(server, sender, false, true, args[0], nickname);
        sender.sendMessage(I18n._("nickChanged", new Object[0]));
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User target, String[] args) throws NotEnoughArgumentsException {
        String nick = args[0];
        if (target.getName().equalsIgnoreCase(nick)) {
            this.setNickname(server, sender, target, nick);
            target.sendMessage(I18n._("nickNoMore", new Object[0]));
        } else if ("off".equalsIgnoreCase(nick)) {
            this.setNickname(server, sender, target, null);
            target.sendMessage(I18n._("nickNoMore", new Object[0]));
        } else {
            if (this.nickInUse(server, target, nick)) {
                throw new NotEnoughArgumentsException(I18n._("nickInUse", new Object[0]));
            }
            this.setNickname(server, sender, target, nick);
            target.sendMessage(I18n._("nickSet", target.getDisplayName()));
        }
    }

    private String formatNickname(User user, String nick) throws Exception {
        String newNick;
        String string = newNick = user == null ? FormatUtil.replaceFormat(nick) : FormatUtil.formatString(user, "essentials.nick", nick);
        if (!newNick.matches("^[a-zA-Z_0-9\u00a7]+$")) {
            throw new Exception(I18n._("nickNamesAlpha", new Object[0]));
        }
        if (newNick.length() > this.ess.getSettings().getMaxNickLength()) {
            throw new Exception(I18n._("nickTooLong", new Object[0]));
        }
        if (FormatUtil.stripFormat(newNick).length() < 1) {
            throw new Exception(I18n._("nickNamesAlpha", new Object[0]));
        }
        return newNick;
    }

    private boolean nickInUse(Server server, User target, String nick) {
        String lowerNick = FormatUtil.stripFormat(nick.toLowerCase(Locale.ENGLISH));
        for (Player onlinePlayer : server.getOnlinePlayers()) {
            String matchNick;
            if (target.getBase().getName().equals(onlinePlayer.getName()) || !lowerNick.equals((matchNick = FormatUtil.stripFormat(onlinePlayer.getDisplayName().replace(this.ess.getSettings().getNicknamePrefix(), ""))).toLowerCase(Locale.ENGLISH)) && !lowerNick.equals(onlinePlayer.getName().toLowerCase(Locale.ENGLISH))) continue;
            return true;
        }
        if (this.ess.getUser(lowerNick) != null && this.ess.getUser(lowerNick) != target) {
            return true;
        }
        return false;
    }

    private void setNickname(Server server, CommandSource sender, User target, String nickname) {
        User controller = sender.isPlayer() ? this.ess.getUser(sender.getPlayer()) : null;
        NickChangeEvent nickEvent = new NickChangeEvent(controller, target, nickname);
        server.getPluginManager().callEvent((Event)nickEvent);
        if (!nickEvent.isCancelled()) {
            target.setNickname(nickname);
            target.setDisplayNick();
        }
    }
}

