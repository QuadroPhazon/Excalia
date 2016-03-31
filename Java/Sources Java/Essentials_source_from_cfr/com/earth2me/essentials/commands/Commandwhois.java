/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Commandwhois
extends EssentialsCommand {
    public Commandwhois() {
        super("whois");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User user = this.getPlayer(server, sender, args, 0);
        sender.sendMessage(I18n._("whoisTop", user.getName()));
        user.setDisplayNick();
        sender.sendMessage(I18n._("whoisNick", user.getDisplayName()));
        sender.sendMessage(I18n._("whoisHealth", user.getHealth()));
        sender.sendMessage(I18n._("whoisHunger", user.getFoodLevel(), Float.valueOf(user.getSaturation())));
        sender.sendMessage(I18n._("whoisExp", SetExpFix.getTotalExperience(user.getBase()), user.getLevel()));
        sender.sendMessage(I18n._("whoisLocation", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ()));
        if (!this.ess.getSettings().isEcoDisabled()) {
            sender.sendMessage(I18n._("whoisMoney", NumberUtil.displayCurrency(user.getMoney(), this.ess)));
        }
        sender.sendMessage(I18n._("whoisIPAddress", user.getAddress().getAddress().toString()));
        String location = user.getGeoLocation();
        if (location != null && (!sender.isPlayer() || this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show"))) {
            sender.sendMessage(I18n._("whoisGeoLocation", location));
        }
        sender.sendMessage(I18n._("whoisGamemode", I18n._(user.getGameMode().toString().toLowerCase(Locale.ENGLISH), new Object[0])));
        Object[] arrobject = new Object[1];
        arrobject[0] = user.isGodModeEnabled() ? I18n._("true", new Object[0]) : I18n._("false", new Object[0]);
        sender.sendMessage(I18n._("whoisGod", arrobject));
        Object[] arrobject2 = new Object[1];
        arrobject2[0] = user.isOp() ? I18n._("true", new Object[0]) : I18n._("false", new Object[0]);
        sender.sendMessage(I18n._("whoisOp", arrobject2));
        Object[] arrobject3 = new Object[2];
        arrobject3[0] = user.getAllowFlight() ? I18n._("true", new Object[0]) : I18n._("false", new Object[0]);
        arrobject3[1] = user.isFlying() ? I18n._("flying", new Object[0]) : I18n._("notFlying", new Object[0]);
        sender.sendMessage(I18n._("whoisFly", arrobject3));
        Object[] arrobject4 = new Object[1];
        arrobject4[0] = user.isAfk() ? I18n._("true", new Object[0]) : I18n._("false", new Object[0]);
        sender.sendMessage(I18n._("whoisAFK", arrobject4));
        Object[] arrobject5 = new Object[1];
        arrobject5[0] = user.isJailed() ? (user.getJailTimeout() > 0 ? DateUtil.formatDateDiff(user.getJailTimeout()) : I18n._("true", new Object[0])) : I18n._("false", new Object[0]);
        sender.sendMessage(I18n._("whoisJail", arrobject5));
        Object[] arrobject6 = new Object[1];
        arrobject6[0] = user.isMuted() ? (user.getMuteTimeout() > 0 ? DateUtil.formatDateDiff(user.getMuteTimeout()) : I18n._("true", new Object[0])) : I18n._("false", new Object[0]);
        sender.sendMessage(I18n._("whoisMuted", arrobject6));
    }
}

