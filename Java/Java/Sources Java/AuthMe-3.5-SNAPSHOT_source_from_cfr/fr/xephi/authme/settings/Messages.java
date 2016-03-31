/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.configuration.file.YamlConfigurationOptions
 */
package fr.xephi.authme.settings;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.settings.CustomConfiguration;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.io.InputStream;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class Messages
extends CustomConfiguration {
    private static Messages singleton = null;

    public Messages(File file) {
        super(file);
        this.loadDefaults(file);
        this.loadFile();
        this.saveDefaults(file);
        singleton = this;
    }

    public final void loadDefaults(File file) {
        InputStream stream = AuthMe.getInstance().getResource(file.getName());
        if (stream == null) {
            return;
        }
        this.setDefaults((Configuration)YamlConfiguration.loadConfiguration((InputStream)stream));
    }

    public final boolean saved(File file) {
        try {
            this.save(file);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public final boolean saveDefaults(File file) {
        this.options().copyDefaults(true);
        this.options().copyHeader(true);
        boolean success = this.saved(file);
        this.options().copyDefaults(false);
        this.options().copyHeader(false);
        return success;
    }

    private void loadFile() {
        this.load();
        this.save();
    }

    public void _(CommandSender sender, String msg) {
        String loc = (String)this.get(msg);
        if (loc == null) {
            loc = "Error with Translation files; Please contact the admin for verify or update translation";
            ConsoleLogger.showError("Error with the " + msg + " translation, verify in your " + Settings.MESSAGE_FILE + "_" + Settings.messagesLanguage + ".yml !");
        }
        for (String l : loc.split("&n")) {
            sender.sendMessage(l.replace("&", "\u00a7"));
        }
    }

    public String[] _(String msg) {
        int i = ((String)this.get(msg)).split("&n").length;
        String[] loc = new String[i];
        for (int a = 0; a < i; ++a) {
            loc[a] = ((String)this.get(msg)).split("&n")[a].replace("&", "\u00a7");
        }
        if (loc == null || loc.length == 0) {
            loc[0] = "Error with " + msg + " translation; Please contact the admin for verify or update translation files";
            ConsoleLogger.showError("Error with the " + msg + " translation, verify in your " + Settings.MESSAGE_FILE + "_" + Settings.messagesLanguage + ".yml !");
        }
        return loc;
    }

    public static Messages getInstance() {
        if (singleton == null) {
            singleton = new Messages(new File(Settings.MESSAGE_FILE + "_" + Settings.messagesLanguage + ".yml"));
        }
        return singleton;
    }
}

