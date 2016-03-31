/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandback
extends EssentialsCommand {
    public Commandback() {
        super("back");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (user.getLastLocation() == null) {
            throw new Exception(I18n._("noLocationFound", new Object[0]));
        }
        if (user.getWorld() != user.getLastLocation().getWorld() && this.ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + user.getLastLocation().getWorld().getName())) {
            throw new Exception(I18n._("noPerm", "essentials.worlds." + user.getLastLocation().getWorld().getName()));
        }
        Trade charge = new Trade(this.getName(), this.ess);
        charge.isAffordableFor(user);
        user.getTeleport().back(charge);
        throw new NoChargeException();
    }
}

