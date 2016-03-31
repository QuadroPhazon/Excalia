/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Commandhome
extends EssentialsCommand {
    public Commandhome() {
        super("home");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        Trade charge = new Trade(this.getName(), this.ess);
        User player = user;
        String homeName = "";
        if (args.length > 0) {
            String[] nameParts = args[0].split(":");
            if (nameParts[0].length() == args[0].length() || !user.isAuthorized("essentials.home.others")) {
                homeName = nameParts[0];
            } else {
                player = this.getPlayer(server, nameParts, 0, true, true);
                if (nameParts.length > 1) {
                    homeName = nameParts[1];
                }
            }
        }
        try {
            if ("bed".equalsIgnoreCase(homeName) && user.isAuthorized("essentials.home.bed")) {
                Location bed = player.getBedSpawnLocation();
                if (bed != null) {
                    user.getTeleport().teleport(bed, charge, PlayerTeleportEvent.TeleportCause.COMMAND);
                    throw new NoChargeException();
                }
                throw new Exception(I18n._("bedMissing", new Object[0]));
            }
            this.goHome(user, player, homeName.toLowerCase(Locale.ENGLISH), charge);
        }
        catch (NotEnoughArgumentsException e) {
            Location bed = player.getBedSpawnLocation();
            List<String> homes = player.getHomes();
            if (homes.isEmpty() && player.equals(user)) {
                user.getTeleport().respawn(charge, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            if (homes.isEmpty()) {
                throw new Exception(I18n._("noHomeSetPlayer", new Object[0]));
            }
            if (homes.size() == 1 && player.equals(user)) {
                this.goHome(user, player, homes.get(0), charge);
            }
            int count = homes.size();
            if (user.isAuthorized("essentials.home.bed")) {
                if (bed != null) {
                    homes.add(I18n._("bed", new Object[0]));
                } else {
                    homes.add(I18n._("bedNull", new Object[0]));
                }
            }
            user.sendMessage(I18n._("homes", StringUtil.joinList(homes), count, this.getHomeLimit(player)));
        }
        throw new NoChargeException();
    }

    private String getHomeLimit(User player) {
        if (!player.isOnline()) {
            return "?";
        }
        if (player.isAuthorized("essentials.sethome.multiple.unlimited")) {
            return "*";
        }
        return Integer.toString(this.ess.getSettings().getHomeLimit(player));
    }

    private void goHome(User user, User player, String home, Trade charge) throws Exception {
        if (home.length() < 1) {
            throw new NotEnoughArgumentsException();
        }
        Location loc = player.getHome(home);
        if (loc == null) {
            throw new NotEnoughArgumentsException();
        }
        if (user.getWorld() != loc.getWorld() && this.ess.getSettings().isWorldHomePermissions() && !user.isAuthorized("essentials.worlds." + loc.getWorld().getName())) {
            throw new Exception(I18n._("noPerm", "essentials.worlds." + loc.getWorld().getName()));
        }
        user.getTeleport().teleport(loc, charge, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}

