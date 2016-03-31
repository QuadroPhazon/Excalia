/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package fr.xephi.authme.commands;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.settings.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand
implements CommandExecutor {
    private AuthMe plugin;
    private Messages m = Messages.getInstance();

    public LoginCommand(AuthMe plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 0) {
            this.m._((CommandSender)player, "usage_log");
            return true;
        }
        if (!this.plugin.authmePermissible(player, "authme." + label.toLowerCase())) {
            this.m._((CommandSender)player, "no_perm");
            return true;
        }
        this.plugin.management.performLogin(player, args[0], false);
        return true;
    }
}

