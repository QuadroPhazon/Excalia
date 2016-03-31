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
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.security.RandomString;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand
implements CommandExecutor {
    private Messages m = Messages.getInstance();
    public PlayerAuth auth;
    public AuthMe plugin;

    public RegisterCommand(AuthMe plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player Only! Use 'authme register <playername> <password>' instead");
            return true;
        }
        if (args.length == 0) {
            this.m._(sender, "usage_reg");
        }
        if (!this.plugin.authmePermissible(sender, "authme." + label.toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        Player player = (Player)sender;
        if (Settings.emailRegistration.booleanValue() && !Settings.getmailAccount.isEmpty()) {
            String email;
            if (Settings.doubleEmailCheck.booleanValue()) {
                if (args.length < 2) {
                    this.m._((CommandSender)player, "usage_reg");
                    return true;
                }
                if (!args[0].equals(args[1])) {
                    this.m._((CommandSender)player, "usage_reg");
                    return true;
                }
            }
            if (!Settings.isEmailCorrect(email = args[0])) {
                this.m._((CommandSender)player, "email_invalid");
                return true;
            }
            RandomString rand = new RandomString(Settings.getRecoveryPassLength);
            String thePass = rand.nextString();
            this.plugin.management.performRegister(player, thePass, email);
            return true;
        }
        if (args.length == 0 || Settings.getEnablePasswordVerifier.booleanValue() && args.length < 2) {
            this.m._((CommandSender)player, "usage_reg");
            return true;
        }
        if (args.length > 1 && Settings.getEnablePasswordVerifier.booleanValue() && !args[0].equals(args[1])) {
            this.m._((CommandSender)player, "password_error");
            return true;
        }
        this.plugin.management.performRegister(player, args[0], "");
        return true;
    }
}

