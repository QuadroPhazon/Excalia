/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Mob;
import com.earth2me.essentials.SpawnMob;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandspawnmob
extends EssentialsCommand {
    public Commandspawnmob() {
        super("spawnmob");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            String mobList = SpawnMob.mobList(user);
            throw new NotEnoughArgumentsException(I18n._("mobsAvailable", mobList));
        }
        List<String> mobParts = SpawnMob.mobParts(args[0]);
        List<String> mobData = SpawnMob.mobData(args[0]);
        int mobCount = 1;
        if (args.length >= 2) {
            mobCount = Integer.parseInt(args[1]);
        }
        if (mobParts.size() > 1 && !user.isAuthorized("essentials.spawnmob.stack")) {
            throw new Exception(I18n._("cannotStackMob", new Object[0]));
        }
        if (args.length >= 3) {
            User target = this.getPlayer(this.ess.getServer(), user, args, 2);
            SpawnMob.spawnmob(this.ess, server, user.getSource(), target, mobParts, mobData, mobCount);
            return;
        }
        SpawnMob.spawnmob(this.ess, server, user, mobParts, mobData, mobCount);
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 3) {
            String mobList = StringUtil.joinList(Mob.getMobList());
            throw new NotEnoughArgumentsException(I18n._("mobsAvailable", mobList));
        }
        List<String> mobParts = SpawnMob.mobParts(args[0]);
        List<String> mobData = SpawnMob.mobData(args[0]);
        int mobCount = Integer.parseInt(args[1]);
        User target = this.getPlayer(this.ess.getServer(), args, 2, true, false);
        SpawnMob.spawnmob(this.ess, server, sender, target, mobParts, mobData, mobCount);
    }
}

