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
import fr.xephi.authme.Utils;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.settings.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasspartuCommand
implements CommandExecutor {
    private Utils utils = Utils.getInstance();
    public AuthMe plugin;
    private Messages m = Messages.getInstance();

    public PasspartuCommand(AuthMe plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!this.plugin.authmePermissible(sender, "authme." + label.toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        if (PlayerCache.getInstance().isAuthenticated(sender.getName())) {
            return true;
        }
        if (sender instanceof Player && args.length == 1) {
            if (this.utils.readToken(args[0])) {
                this.plugin.management.performLogin((Player)sender, "dontneed", true);
                return true;
            }
            sender.sendMessage("Time is expired or Token is Wrong!");
            return true;
        }
        sender.sendMessage("usage: /passpartu token");
        return true;
    }
}

