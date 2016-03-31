/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Server;

public class Commandkit
extends EssentialsCommand {
    public Commandkit() {
        super("kit");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            String kitList = Kit.listKits(this.ess, user);
            user.sendMessage(kitList.length() > 0 ? I18n._("kits", kitList) : I18n._("noKits", new Object[0]));
            throw new NoChargeException();
        }
        if (args.length > 1 && user.isAuthorized("essentials.kit.others")) {
            User userTo = this.getPlayer(server, user, args, 1);
            String kitName = StringUtil.sanitizeString(args[0].toLowerCase(Locale.ENGLISH)).trim();
            this.giveKit(userTo, user, kitName);
        } else {
            String kitName = StringUtil.sanitizeString(args[0].toLowerCase(Locale.ENGLISH)).trim();
            this.giveKit(user, user, kitName);
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            String kitList = Kit.listKits(this.ess, null);
            sender.sendMessage(kitList.length() > 0 ? I18n._("kits", kitList) : I18n._("noKits", new Object[0]));
            throw new NoChargeException();
        }
        User userTo = this.getPlayer(server, args, 1, true, false);
        String kitName = args[0].toLowerCase(Locale.ENGLISH);
        Map<String, Object> kit = this.ess.getSettings().getKit(kitName);
        List<String> items = Kit.getItems(this.ess, userTo, kitName, kit);
        Kit.expandItems(this.ess, userTo, items);
        sender.sendMessage(I18n._("kitGiveTo", kitName, userTo.getDisplayName()));
        userTo.sendMessage(I18n._("kitReceive", kitName));
    }

    private void giveKit(User userTo, User userFrom, String kitName) throws Exception {
        if (kitName.isEmpty()) {
            throw new Exception(I18n._("kitError2", new Object[0]));
        }
        Map<String, Object> kit = this.ess.getSettings().getKit(kitName);
        if (!userFrom.isAuthorized("essentials.kits." + kitName)) {
            throw new Exception(I18n._("noKitPermission", "essentials.kits." + kitName));
        }
        List<String> items = Kit.getItems(this.ess, userTo, kitName, kit);
        Trade charge = new Trade("kit-" + kitName, this.ess);
        charge.isAffordableFor(userFrom);
        Kit.checkTime(userFrom, kitName, kit);
        Kit.expandItems(this.ess, userTo, items);
        charge.charge(userFrom);
        userFrom.sendMessage(I18n._("kitGiveTo", kitName, userTo.getDisplayName()));
        userTo.sendMessage(I18n._("kitReceive", kitName));
    }
}

