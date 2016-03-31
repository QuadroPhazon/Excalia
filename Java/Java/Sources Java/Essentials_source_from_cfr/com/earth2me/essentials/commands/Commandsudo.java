/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class Commandsudo
extends EssentialsCommand {
    private static final Logger LOGGER = Logger.getLogger("Essentials");

    public Commandsudo() {
        super("sudo");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        final User user = this.getPlayer(server, sender, args, 0);
        if (args[1].toLowerCase(Locale.ENGLISH).startsWith("c:")) {
            if (user.isAuthorized("essentials.sudo.exempt") && sender.isPlayer()) {
                throw new Exception(I18n._("sudoExempt", new Object[0]));
            }
            user.chat(Commandsudo.getFinalArg(args, 1).substring(2));
            return;
        }
        final String command = args[1];
        final String[] arguments = new String[args.length - 2];
        if (arguments.length > 0) {
            System.arraycopy(args, 2, arguments, 0, args.length - 2);
        }
        if (user.isAuthorized("essentials.sudo.exempt") && sender.isPlayer()) {
            throw new Exception(I18n._("sudoExempt", new Object[0]));
        }
        sender.sendMessage(I18n._("sudoRun", user.getDisplayName(), command, Commandsudo.getFinalArg(arguments, 0)));
        final PluginCommand execCommand = this.ess.getServer().getPluginCommand(command);
        if (execCommand != null) {
            this.ess.scheduleSyncDelayedTask(new Runnable(){

                @Override
                public void run() {
                    LOGGER.log(Level.INFO, String.format("[Sudo] %s issued server command: /%s %s", user.getName(), command, EssentialsCommand.getFinalArg(arguments, 0)));
                    execCommand.execute((CommandSender)user.getBase(), command, arguments);
                }
            });
        } else {
            sender.sendMessage(I18n._("errorCallingCommand", command));
        }
    }

}

