/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandwarp
extends EssentialsCommand {
    private static final int WARPS_PER_PAGE = 20;

    public Commandwarp() {
        super("warp");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length == 0 || args[0].matches("[0-9]+")) {
            if (!user.isAuthorized("essentials.warp.list")) {
                throw new Exception(I18n._("warpListPermission", new Object[0]));
            }
            this.warpList(user.getSource(), args, user);
            throw new NoChargeException();
        }
        if (args.length > 0) {
            User otherUser = null;
            if (args.length == 2 && (user.isAuthorized("essentials.warp.otherplayers") || user.isAuthorized("essentials.warp.others"))) {
                otherUser = this.getPlayer(server, user, args, 1);
                this.warpUser(user, otherUser, args[0]);
                throw new NoChargeException();
            }
            this.warpUser(user, user, args[0]);
            throw new NoChargeException();
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2 || NumberUtil.isInt(args[0])) {
            this.warpList(sender, args, null);
            throw new NoChargeException();
        }
        User otherUser = this.getPlayer(server, args, 1, true, false);
        otherUser.getTeleport().warp(otherUser, args[0], null, PlayerTeleportEvent.TeleportCause.COMMAND);
        throw new NoChargeException();
    }

    private void warpList(CommandSource sender, String[] args, IUser user) throws Exception {
        int maxPages;
        IWarps warps = this.ess.getWarps();
        ArrayList<String> warpNameList = new ArrayList<String>(warps.getList());
        if (user != null) {
            Iterator<String> iterator = warpNameList.iterator();
            while (iterator.hasNext()) {
                String warpName = iterator.next();
                if (!this.ess.getSettings().getPerWarpPermission() || user.isAuthorized("essentials.warps." + warpName)) continue;
                iterator.remove();
            }
        }
        if (warpNameList.isEmpty()) {
            throw new Exception(I18n._("noWarpsDefined", new Object[0]));
        }
        int page = 1;
        if (args.length > 0 && NumberUtil.isInt(args[0])) {
            page = Integer.parseInt(args[0]);
        }
        if (page > (maxPages = (int)Math.ceil((double)warpNameList.size() / 20.0))) {
            page = maxPages;
        }
        int warpPage = (page - 1) * 20;
        String warpList = StringUtil.joinList(warpNameList.subList(warpPage, warpPage + Math.min(warpNameList.size() - warpPage, 20)));
        if (warpNameList.size() > 20) {
            sender.sendMessage(I18n._("warpsCount", warpNameList.size(), page, maxPages));
            sender.sendMessage(I18n._("warpList", warpList));
        } else {
            sender.sendMessage(I18n._("warps", warpList));
        }
    }

    private void warpUser(User owner, User user, String name) throws Exception {
        Trade chargeWarp = new Trade("warp-" + name.toLowerCase(Locale.ENGLISH).replace('_', '-'), this.ess);
        Trade chargeCmd = new Trade(this.getName(), this.ess);
        BigDecimal fullCharge = chargeWarp.getCommandCost(user).add(chargeCmd.getCommandCost(user));
        Trade charge = new Trade(fullCharge, this.ess);
        charge.isAffordableFor(owner);
        if (this.ess.getSettings().getPerWarpPermission() && !owner.isAuthorized("essentials.warps." + name)) {
            throw new Exception(I18n._("warpUsePermission", new Object[0]));
        }
        owner.getTeleport().warp(user, name, charge, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}

