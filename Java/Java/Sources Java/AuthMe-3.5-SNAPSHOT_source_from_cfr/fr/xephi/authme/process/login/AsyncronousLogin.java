/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.muizers.Notifications.Notification
 *  me.muizers.Notifications.Notifications
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.process.login;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.listener.AuthMePlayerListener;
import fr.xephi.authme.process.login.ProcessSyncronousPlayerLogin;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.RandomString;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.OtherAccounts;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.task.MessageTask;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.muizers.Notifications.Notification;
import me.muizers.Notifications.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class AsyncronousLogin {
    protected Player player;
    protected String name;
    protected String password;
    protected boolean forceLogin;
    private AuthMe plugin;
    private DataSource database;
    private static RandomString rdm = new RandomString(Settings.captchaLength);
    private Messages m = Messages.getInstance();

    public AsyncronousLogin(Player player, String password, boolean forceLogin, AuthMe plugin, DataSource data) {
        this.player = player;
        this.password = password;
        this.name = player.getName();
        this.forceLogin = forceLogin;
        this.plugin = plugin;
        this.database = data;
    }

    protected String getIP() {
        return this.plugin.getIP(this.player);
    }

    protected boolean needsCaptcha() {
        if (Settings.useCaptcha.booleanValue()) {
            if (!this.plugin.captcha.containsKey(this.name)) {
                this.plugin.captcha.put(this.name, 1);
            } else {
                int i = this.plugin.captcha.get(this.name) + 1;
                this.plugin.captcha.remove(this.name);
                this.plugin.captcha.put(this.name, i);
            }
            if (this.plugin.captcha.containsKey(this.name) && this.plugin.captcha.get(this.name) >= Settings.maxLoginTry) {
                this.plugin.cap.put(this.name, rdm.nextString());
                for (String s : this.m._("usage_captcha")) {
                    this.player.sendMessage(s.replace("THE_CAPTCHA", this.plugin.cap.get(this.name)).replace("<theCaptcha>", this.plugin.cap.get(this.name)));
                }
                return true;
            }
            if (this.plugin.captcha.containsKey(this.name) && this.plugin.captcha.get(this.name) >= Settings.maxLoginTry) {
                try {
                    this.plugin.captcha.remove(this.name);
                    this.plugin.cap.remove(this.name);
                }
                catch (NullPointerException npe) {
                    // empty catch block
                }
            }
        }
        return false;
    }

    protected PlayerAuth preAuth() {
        if (PlayerCache.getInstance().isAuthenticated(this.name)) {
            this.m._((CommandSender)this.player, "logged_in");
            return null;
        }
        if (!this.database.isAuthAvailable(this.name)) {
            this.m._((CommandSender)this.player, "user_unknown");
            if (LimboCache.getInstance().hasLimboPlayer(this.name)) {
                Bukkit.getScheduler().cancelTask(LimboCache.getInstance().getLimboPlayer(this.name).getMessageTaskId());
                String[] msg = Settings.emailRegistration != false ? this.m._("reg_email_msg") : this.m._("reg_msg");
                int msgT = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new MessageTask(this.plugin, this.name, msg, Settings.getWarnMessageInterval));
                LimboCache.getInstance().getLimboPlayer(this.name).setMessageTaskId(msgT);
            }
            return null;
        }
        if (Settings.getMaxLoginPerIp > 0 && !this.plugin.authmePermissible(this.player, "authme.allow2accounts") && !this.getIP().equalsIgnoreCase("127.0.0.1") && !this.getIP().equalsIgnoreCase("localhost") && this.plugin.isLoggedIp(this.name, this.getIP())) {
            this.m._((CommandSender)this.player, "logged_in");
            return null;
        }
        PlayerAuth pAuth = this.database.getAuth(this.name);
        if (pAuth == null) {
            this.m._((CommandSender)this.player, "user_unknown");
            return null;
        }
        if (!Settings.getMySQLColumnGroup.isEmpty() && pAuth.getGroupId() == Settings.getNonActivatedGroup) {
            this.m._((CommandSender)this.player, "vb_nonActiv");
            return null;
        }
        return pAuth;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void process() {
        pAuth = this.preAuth();
        if (pAuth == null) return;
        if (this.needsCaptcha()) {
            return;
        }
        hash = pAuth.getHash();
        email = pAuth.getEmail();
        passwordVerified = true;
        if (this.forceLogin) ** GOTO lbl-1000
        try {
            passwordVerified = PasswordSecurity.comparePasswordWithHash(this.password, hash, this.name);
        }
        catch (Exception ex) {
            ConsoleLogger.showError(ex.getMessage());
            this.m._((CommandSender)this.player, "error");
            return;
        }
        if (passwordVerified) lbl-1000: // 2 sources:
        {
            if (this.player.isOnline()) {
                auth = new PlayerAuth(this.name, hash, this.getIP(), new Date().getTime(), email);
                this.database.updateSession(auth);
                if (Settings.useCaptcha.booleanValue()) {
                    if (this.plugin.captcha.containsKey(this.name)) {
                        this.plugin.captcha.remove(this.name);
                    }
                    if (this.plugin.cap.containsKey(this.name)) {
                        this.plugin.cap.remove(this.name);
                    }
                }
                this.player.setNoDamageTicks(0);
                this.m._((CommandSender)this.player, "login");
                this.displayOtherAccounts(auth, this.player);
                if (!Settings.noConsoleSpam.booleanValue()) {
                    ConsoleLogger.info(this.player.getName() + " logged in!");
                }
                if (this.plugin.notifications != null) {
                    this.plugin.notifications.showNotification(new Notification("[AuthMe] " + this.player.getName() + " logged in!"));
                }
                PlayerCache.getInstance().addPlayer(auth);
                this.database.setLogged(this.name);
                this.plugin.otherAccounts.addPlayer(this.player.getUniqueId());
                syncronousPlayerLogin = new ProcessSyncronousPlayerLogin(this.player, this.plugin, this.database);
                if (syncronousPlayerLogin.getLimbo() != null) {
                    this.player.getServer().getScheduler().cancelTask(syncronousPlayerLogin.getLimbo().getTimeoutTaskId());
                    this.player.getServer().getScheduler().cancelTask(syncronousPlayerLogin.getLimbo().getMessageTaskId());
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)syncronousPlayerLogin);
                return;
            }
        }
        if (!this.player.isOnline()) {
            ConsoleLogger.showError("Player " + this.name + " wasn't online during login process, aborted... ");
            return;
        }
        if (!Settings.noConsoleSpam.booleanValue()) {
            ConsoleLogger.info(this.player.getName() + " used the wrong password");
        }
        if (Settings.isKickOnWrongPasswordEnabled.booleanValue()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

                public void run() {
                    if (AuthMePlayerListener.gameMode != null && AuthMePlayerListener.gameMode.containsKey(AsyncronousLogin.this.name)) {
                        AsyncronousLogin.this.player.setGameMode(AuthMePlayerListener.gameMode.get(AsyncronousLogin.this.name));
                    }
                    AsyncronousLogin.this.player.kickPlayer(AsyncronousLogin.this.m._("wrong_pwd")[0]);
                }
            });
            return;
        }
        this.m._((CommandSender)this.player, "wrong_pwd");
    }

    public void displayOtherAccounts(PlayerAuth auth, Player p) {
        if (!Settings.displayOtherAccounts.booleanValue()) {
            return;
        }
        if (auth == null) {
            return;
        }
        List<String> auths = this.database.getAllAuthsByName(auth);
        if (auths.isEmpty() || auths == null) {
            return;
        }
        if (auths.size() == 1) {
            return;
        }
        String message = "[AuthMe] ";
        int i = 0;
        for (String account : auths) {
            message = message + account;
            if (++i != auths.size()) {
                message = message + ", ";
                continue;
            }
            message = message + ".";
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (!this.plugin.authmePermissible(player, "authme.seeOtherAccounts")) continue;
            player.sendMessage("[AuthMe] The player " + auth.getNickname() + " has " + auths.size() + " accounts");
            player.sendMessage(message);
        }
    }

}

