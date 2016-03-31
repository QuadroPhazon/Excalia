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
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.security.RandomString;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.Settings;
import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CaptchaCommand
implements CommandExecutor {
    public AuthMe plugin;
    private Messages m = Messages.getInstance();
    public static RandomString rdm = new RandomString(Settings.captchaLength);

    public CaptchaCommand(AuthMe plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        String name = player.getName();
        if (args.length == 0) {
            this.m._((CommandSender)player, "usage_captcha");
            return true;
        }
        if (PlayerCache.getInstance().isAuthenticated(name)) {
            this.m._((CommandSender)player, "logged_in");
            return true;
        }
        if (!this.plugin.authmePermissible(player, "authme." + label.toLowerCase())) {
            this.m._((CommandSender)player, "no_perm");
            return true;
        }
        if (!Settings.useCaptcha.booleanValue()) {
            this.m._((CommandSender)player, "usage_log");
            return true;
        }
        if (!this.plugin.cap.containsKey(name)) {
            this.m._((CommandSender)player, "usage_log");
            return true;
        }
        if (Settings.useCaptcha.booleanValue() && !args[0].equals(this.plugin.cap.get(name))) {
            this.plugin.cap.remove(name);
            this.plugin.cap.put(name, rdm.nextString());
            for (String s : this.m._("wrong_captcha")) {
                player.sendMessage(s.replace("THE_CAPTCHA", this.plugin.cap.get(name)));
            }
            return true;
        }
        try {
            this.plugin.captcha.remove(name);
            this.plugin.cap.remove(name);
        }
        catch (NullPointerException npe) {
            // empty catch block
        }
        this.m._((CommandSender)player, "valid_captcha");
        this.m._((CommandSender)player, "login_msg");
        return true;
    }
}

