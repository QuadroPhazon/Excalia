/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.process;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.process.login.AsyncronousLogin;
import fr.xephi.authme.process.register.AsyncronousRegister;
import fr.xephi.authme.security.RandomString;
import fr.xephi.authme.settings.Settings;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class Management
extends Thread {
    public DataSource database;
    public AuthMe plugin;
    public static RandomString rdm = new RandomString(Settings.captchaLength);
    public PluginManager pm;

    public Management(DataSource database, AuthMe plugin) {
        this.database = database;
        this.plugin = plugin;
        this.pm = plugin.getServer().getPluginManager();
    }

    public void run() {
    }

    public void performLogin(Player player, String password, boolean forceLogin) {
        new AsyncronousLogin(player, password, forceLogin, this.plugin, this.database).process();
    }

    public void performRegister(Player player, String password, String email) {
        new AsyncronousRegister(player, password, email, this.plugin, this.database).process();
    }
}

