/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package fr.xephi.authme.commands;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.SendMailSSL;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.RandomString;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class EmailCommand
implements CommandExecutor {
    public AuthMe plugin;
    private DataSource data;
    private Messages m = Messages.getInstance();

    public EmailCommand(AuthMe plugin, DataSource data) {
        this.plugin = plugin;
        this.data = data;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        PlayerAuth auth;
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!this.plugin.authmePermissible(sender, "authme." + label.toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        Player player = (Player)sender;
        String name = player.getName();
        if (args.length == 0) {
            this.m._((CommandSender)player, "usage_email_add");
            this.m._((CommandSender)player, "usage_email_change");
            this.m._((CommandSender)player, "usage_email_recovery");
            return true;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 3) {
                this.m._((CommandSender)player, "usage_email_add");
                return true;
            }
            if (Settings.getmaxRegPerEmail > 0 && !this.plugin.authmePermissible(sender, "authme.allow2accounts") && this.data.getAllAuthsByEmail(args[1]).size() >= Settings.getmaxRegPerEmail) {
                this.m._((CommandSender)player, "max_reg");
                return true;
            }
            if (args[1].equals(args[2]) && PlayerCache.getInstance().isAuthenticated(name)) {
                auth = PlayerCache.getInstance().getAuth(name);
                if (auth.getEmail() == null || !auth.getEmail().equals("your@email.com") && !auth.getEmail().isEmpty()) {
                    this.m._((CommandSender)player, "usage_email_change");
                    return true;
                }
                if (!Settings.isEmailCorrect(args[1])) {
                    this.m._((CommandSender)player, "email_invalid");
                    return true;
                }
                auth.setEmail(args[1]);
                if (!this.data.updateEmail(auth)) {
                    this.m._((CommandSender)player, "error");
                    return true;
                }
                PlayerCache.getInstance().updatePlayer(auth);
                this.m._((CommandSender)player, "email_added");
                player.sendMessage(auth.getEmail());
            } else if (PlayerCache.getInstance().isAuthenticated(name)) {
                this.m._((CommandSender)player, "email_confirm");
            } else if (!this.data.isAuthAvailable(name)) {
                this.m._((CommandSender)player, "login_msg");
            } else {
                this.m._((CommandSender)player, "reg_email_msg");
            }
        } else if (args[0].equalsIgnoreCase("change")) {
            if (args.length != 3) {
                this.m._((CommandSender)player, "usage_email_change");
                return true;
            }
            if (Settings.getmaxRegPerEmail > 0 && !this.plugin.authmePermissible(sender, "authme.allow2accounts") && this.data.getAllAuthsByEmail(args[2]).size() >= Settings.getmaxRegPerEmail) {
                this.m._((CommandSender)player, "max_reg");
                return true;
            }
            if (PlayerCache.getInstance().isAuthenticated(name)) {
                auth = PlayerCache.getInstance().getAuth(name);
                if (auth.getEmail() == null || auth.getEmail().equals("your@email.com") || auth.getEmail().isEmpty()) {
                    this.m._((CommandSender)player, "usage_email_add");
                    return true;
                }
                if (!args[1].equals(auth.getEmail())) {
                    this.m._((CommandSender)player, "old_email_invalid");
                    return true;
                }
                if (!Settings.isEmailCorrect(args[2])) {
                    this.m._((CommandSender)player, "new_email_invalid");
                    return true;
                }
                auth.setEmail(args[2]);
                if (!this.data.updateEmail(auth)) {
                    this.m._((CommandSender)player, "error");
                    return true;
                }
                PlayerCache.getInstance().updatePlayer(auth);
                this.m._((CommandSender)player, "email_changed");
                player.sendMessage(this.m._("email_defined") + auth.getEmail());
            } else if (PlayerCache.getInstance().isAuthenticated(name)) {
                this.m._((CommandSender)player, "email_confirm");
            } else if (!this.data.isAuthAvailable(name)) {
                this.m._((CommandSender)player, "login_msg");
            } else {
                this.m._((CommandSender)player, "reg_email_msg");
            }
        }
        if (args[0].equalsIgnoreCase("recovery")) {
            if (args.length != 2) {
                this.m._((CommandSender)player, "usage_email_recovery");
                return true;
            }
            if (this.plugin.mail == null) {
                this.m._((CommandSender)player, "error");
                return true;
            }
            if (this.data.isAuthAvailable(name)) {
                if (PlayerCache.getInstance().isAuthenticated(name)) {
                    this.m._((CommandSender)player, "logged_in");
                    return true;
                }
                try {
                    RandomString rand = new RandomString(Settings.getRecoveryPassLength);
                    String thePass = rand.nextString();
                    String hashnew = PasswordSecurity.getHash(Settings.getPasswordHash, thePass, name);
                    PlayerAuth auth2 = null;
                    if (PlayerCache.getInstance().isAuthenticated(name)) {
                        auth2 = PlayerCache.getInstance().getAuth(name);
                    } else if (this.data.isAuthAvailable(name)) {
                        auth2 = this.data.getAuth(name);
                    } else {
                        this.m._((CommandSender)player, "unknown_user");
                        return true;
                    }
                    if (Settings.getmailAccount.equals("") || Settings.getmailAccount.isEmpty()) {
                        this.m._((CommandSender)player, "error");
                        return true;
                    }
                    if (!args[1].equalsIgnoreCase(auth2.getEmail()) || args[1].equalsIgnoreCase("your@email.com") || auth2.getEmail().equalsIgnoreCase("your@email.com")) {
                        this.m._((CommandSender)player, "email_invalid");
                        return true;
                    }
                    final String finalhashnew = hashnew;
                    final PlayerAuth finalauth = auth2;
                    if (this.data instanceof Thread) {
                        finalauth.setHash(hashnew);
                        this.data.updatePassword(auth2);
                    } else {
                        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, new Runnable(){

                            public void run() {
                                finalauth.setHash(finalhashnew);
                                EmailCommand.this.data.updatePassword(finalauth);
                            }
                        });
                    }
                    this.plugin.mail.main(auth2, thePass);
                    this.m._((CommandSender)player, "email_send");
                }
                catch (NoSuchAlgorithmException ex) {
                    ConsoleLogger.showError(ex.getMessage());
                    this.m._(sender, "error");
                }
                catch (NoClassDefFoundError ncdfe) {
                    ConsoleLogger.showError(ncdfe.getMessage());
                    this.m._(sender, "error");
                }
            } else {
                this.m._((CommandSender)player, "reg_email_msg");
            }
        }
        return true;
    }

}

