/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package fr.xephi.authme.process.register;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.SendMailSSL;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.process.register.ProcessSyncronousEmailRegister;
import fr.xephi.authme.process.register.ProcessSyncronousPasswordRegister;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.settings.Messages;
import fr.xephi.authme.settings.OtherAccounts;
import fr.xephi.authme.settings.PlayersLogs;
import fr.xephi.authme.settings.Settings;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class AsyncronousRegister {
    protected Player player;
    protected String name;
    protected String password;
    protected String email = "";
    protected boolean allowRegister;
    private AuthMe plugin;
    private DataSource database;
    private Messages m = Messages.getInstance();

    public AsyncronousRegister(Player player, String password, String email, AuthMe plugin, DataSource data) {
        this.player = player;
        this.password = password;
        this.name = player.getName();
        this.email = email;
        this.plugin = plugin;
        this.database = data;
        this.allowRegister = true;
    }

    protected String getIp() {
        return this.plugin.getIP(this.player);
    }

    protected void preRegister() {
        String lowpass;
        if (PlayerCache.getInstance().isAuthenticated(this.name)) {
            this.m._((CommandSender)this.player, "logged_in");
            this.allowRegister = false;
        }
        if (!Settings.isRegistrationEnabled.booleanValue()) {
            this.m._((CommandSender)this.player, "reg_disabled");
            this.allowRegister = false;
        }
        if ((lowpass = this.password.toLowerCase()).contains("delete") || lowpass.contains("where") || lowpass.contains("insert") || lowpass.contains("modify") || lowpass.contains("from") || lowpass.contains("select") || lowpass.contains(";") || lowpass.contains("null") || !lowpass.matches(Settings.getPassRegex)) {
            this.m._((CommandSender)this.player, "password_error");
            this.allowRegister = false;
        }
        if (this.database.isAuthAvailable(this.player.getName())) {
            this.m._((CommandSender)this.player, "user_regged");
            if (this.plugin.pllog.getStringList("players").contains(this.player.getName())) {
                this.plugin.pllog.getStringList("players").remove(this.player.getName());
            }
            this.allowRegister = false;
        }
        if (!(Settings.getmaxRegPerIp <= 0 || this.plugin.authmePermissible(this.player, "authme.allow2accounts") || this.database.getAllAuthsByIp(this.getIp()).size() < Settings.getmaxRegPerIp || this.getIp().equalsIgnoreCase("127.0.0.1") || this.getIp().equalsIgnoreCase("localhost"))) {
            this.m._((CommandSender)this.player, "max_reg");
            this.allowRegister = false;
        }
    }

    public void process() {
        this.preRegister();
        if (!this.allowRegister) {
            return;
        }
        if (!this.email.isEmpty() && this.email != "") {
            if (Settings.getmaxRegPerEmail > 0 && !this.plugin.authmePermissible(this.player, "authme.allow2accounts") && this.database.getAllAuthsByEmail(this.email).size() >= Settings.getmaxRegPerEmail) {
                this.m._((CommandSender)this.player, "max_reg");
                return;
            }
            this.emailRegister();
            return;
        }
        this.passwordRegister();
    }

    protected void emailRegister() {
        if (Settings.getmaxRegPerEmail > 0 && !this.plugin.authmePermissible(this.player, "authme.allow2accounts") && this.database.getAllAuthsByEmail(this.email).size() >= Settings.getmaxRegPerEmail) {
            this.m._((CommandSender)this.player, "max_reg");
            return;
        }
        PlayerAuth auth = null;
        try {
            String hashnew = PasswordSecurity.getHash(Settings.getPasswordHash, this.password, this.name);
            auth = new PlayerAuth(this.name, hashnew, this.getIp(), 0, (int)this.player.getLocation().getX(), (int)this.player.getLocation().getY(), (int)this.player.getLocation().getZ(), this.player.getLocation().getWorld().getName(), this.email);
        }
        catch (NoSuchAlgorithmException e) {
            ConsoleLogger.showError(e.getMessage());
            this.m._((CommandSender)this.player, "error");
            return;
        }
        if (PasswordSecurity.userSalt.containsKey(this.name)) {
            auth.setSalt(PasswordSecurity.userSalt.get(this.name));
        }
        this.database.saveAuth(auth);
        this.database.updateEmail(auth);
        this.database.updateSession(auth);
        this.plugin.mail.main(auth, this.password);
        ProcessSyncronousEmailRegister syncronous = new ProcessSyncronousEmailRegister(this.player, this.plugin);
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)syncronous);
    }

    protected void passwordRegister() {
        if (this.password.length() < Settings.getPasswordMinLen || this.password.length() > Settings.passwordMaxLength) {
            this.m._((CommandSender)this.player, "pass_len");
            return;
        }
        if (!Settings.unsafePasswords.isEmpty() && Settings.unsafePasswords.contains(this.password.toLowerCase())) {
            this.m._((CommandSender)this.player, "password_error");
            return;
        }
        PlayerAuth auth = null;
        String hash = "";
        try {
            hash = PasswordSecurity.getHash(Settings.getPasswordHash, this.password, this.name);
        }
        catch (NoSuchAlgorithmException e) {
            ConsoleLogger.showError(e.getMessage());
            this.m._((CommandSender)this.player, "error");
            return;
        }
        auth = Settings.getMySQLColumnSalt.isEmpty() && !PasswordSecurity.userSalt.containsKey(this.name) ? new PlayerAuth(this.name, hash, this.getIp(), new Date().getTime(), "your@email.com") : new PlayerAuth(this.name, hash, PasswordSecurity.userSalt.get(this.name), this.getIp(), new Date().getTime());
        if (!this.database.saveAuth(auth)) {
            this.m._((CommandSender)this.player, "error");
            return;
        }
        if (!Settings.forceRegLogin.booleanValue()) {
            PlayerCache.getInstance().addPlayer(auth);
            this.database.setLogged(this.name);
        }
        this.plugin.otherAccounts.addPlayer(this.player.getUniqueId());
        ProcessSyncronousPasswordRegister syncronous = new ProcessSyncronousPasswordRegister(this.player, this.plugin);
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)syncronous);
    }
}

