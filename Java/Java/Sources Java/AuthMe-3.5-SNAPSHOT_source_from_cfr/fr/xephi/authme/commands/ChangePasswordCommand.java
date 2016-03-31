/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.muizers.Notifications.Notification
 *  me.muizers.Notifications.Notifications
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package fr.xephi.authme.commands;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import me.muizers.Notifications.Notification;
import me.muizers.Notifications.Notifications;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangePasswordCommand
implements CommandExecutor {
    private Messages m = Messages.getInstance();
    private DataSource database;
    public AuthMe plugin;

    public ChangePasswordCommand(DataSource database, AuthMe plugin) {
        this.database = database;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!this.plugin.authmePermissible(sender, "authme." + label.toLowerCase())) {
            this.m._(sender, "no_perm");
            return true;
        }
        Player player = (Player)sender;
        String name = player.getName();
        if (!PlayerCache.getInstance().isAuthenticated(name)) {
            this.m._((CommandSender)player, "not_logged_in");
            return true;
        }
        if (args.length != 2) {
            this.m._((CommandSender)player, "usage_changepassword");
            return true;
        }
        try {
            String hashnew = PasswordSecurity.getHash(Settings.getPasswordHash, args[1], name);
            if (PasswordSecurity.comparePasswordWithHash(args[0], PlayerCache.getInstance().getAuth(name).getHash(), player.getName())) {
                PlayerAuth auth = PlayerCache.getInstance().getAuth(name);
                auth.setHash(hashnew);
                if (PasswordSecurity.userSalt.containsKey(name) && PasswordSecurity.userSalt.get(name) != null) {
                    auth.setSalt(PasswordSecurity.userSalt.get(name));
                } else {
                    auth.setSalt("");
                }
                if (!this.database.updatePassword(auth)) {
                    this.m._((CommandSender)player, "error");
                    return true;
                }
                this.database.updateSalt(auth);
                PlayerCache.getInstance().updatePlayer(auth);
                this.m._((CommandSender)player, "pwd_changed");
                ConsoleLogger.info(player.getName() + " changed his password");
                if (this.plugin.notifications != null) {
                    this.plugin.notifications.showNotification(new Notification("[AuthMe] " + player.getName() + " change his password!"));
                }
            } else {
                this.m._((CommandSender)player, "wrong_pwd");
            }
        }
        catch (NoSuchAlgorithmException ex) {
            ConsoleLogger.showError(ex.getMessage());
            this.m._(sender, "error");
        }
        return true;
    }
}

