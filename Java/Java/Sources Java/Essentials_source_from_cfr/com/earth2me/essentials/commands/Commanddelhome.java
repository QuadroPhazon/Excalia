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
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commanddelhome
extends EssentialsCommand {
    public Commanddelhome() {
        super("delhome");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String name;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User user = this.ess.getUser(sender.getPlayer());
        String[] nameParts = args[0].split(":");
        String[] expandedArg = nameParts[0].length() != args[0].length() ? nameParts : args;
        if (expandedArg.length > 1 && (user == null || user.isAuthorized("essentials.delhome.others"))) {
            user = this.getPlayer(server, expandedArg, 0, true, true);
            name = expandedArg[1];
        } else {
            if (user == null) {
                throw new NotEnoughArgumentsException();
            }
            name = expandedArg[0];
        }
        if (name.equalsIgnoreCase("bed")) {
            throw new Exception(I18n._("invalidHomeName", new Object[0]));
        }
        user.delHome(name.toLowerCase(Locale.ENGLISH));
        sender.sendMessage(I18n._("deleteHome", name));
    }
}

