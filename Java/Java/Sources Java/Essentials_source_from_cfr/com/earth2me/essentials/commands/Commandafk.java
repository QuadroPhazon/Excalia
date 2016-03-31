/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandafk
extends EssentialsCommand {
    public Commandafk() {
        super("afk");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length > 0 && user.isAuthorized("essentials.afk.others")) {
            User afkUser = this.getPlayer(server, user, args, 0);
            this.toggleAfk(afkUser);
        } else {
            this.toggleAfk(user);
        }
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length <= 0) {
            throw new NotEnoughArgumentsException();
        }
        User afkUser = this.getPlayer(server, args, 0, true, false);
        this.toggleAfk(afkUser);
    }

    private void toggleAfk(User user) {
        user.setDisplayNick();
        String msg = "";
        if (!user.toggleAfk()) {
            if (!user.isHidden()) {
                msg = I18n._("userIsNotAway", user.getDisplayName());
            }
            user.updateActivity(false);
        } else if (!user.isHidden()) {
            msg = I18n._("userIsAway", user.getDisplayName());
        }
        if (!msg.isEmpty()) {
            this.ess.broadcastMessage(user, msg);
        }
    }
}

