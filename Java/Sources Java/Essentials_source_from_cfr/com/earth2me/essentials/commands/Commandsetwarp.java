/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.WarpNotFoundException;
import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.StringUtil;
import net.ess3.api.IEssentials;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.Server;

public class Commandsetwarp
extends EssentialsCommand {
    public Commandsetwarp() {
        super("setwarp");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (NumberUtil.isInt(args[0]) || args[0].isEmpty()) {
            throw new NoSuchFieldException(I18n._("invalidWarpName", new Object[0]));
        }
        Location loc = user.getLocation();
        IWarps warps = this.ess.getWarps();
        Location warpLoc = null;
        try {
            warpLoc = warps.getWarp(args[0]);
        }
        catch (WarpNotFoundException ex) {
        }
        catch (InvalidWorldException ex) {
            // empty catch block
        }
        if (warpLoc != null && !user.isAuthorized("essentials.warp.overwrite." + StringUtil.safeString(args[0]))) {
            throw new Exception(I18n._("warpOverwrite", new Object[0]));
        }
        warps.setWarp(args[0], loc);
        user.sendMessage(I18n._("warpSet", args[0]));
    }
}

