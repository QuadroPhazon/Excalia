/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package fr.xephi.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.settings.Settings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ConsoleLogger {
    private static final Logger log = Logger.getLogger("AuthMe");

    public static void info(String message) {
        if (AuthMe.getInstance().isEnabled()) {
            log.info("[AuthMe] " + message);
            if (Settings.useLogging.booleanValue()) {
                Calendar date = Calendar.getInstance();
                final String actually = "[" + DateFormat.getDateInstance().format(date.getTime()) + ", " + date.get(11) + ":" + date.get(12) + ":" + date.get(13) + "] " + message;
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)AuthMe.getInstance(), new Runnable(){

                    public void run() {
                        ConsoleLogger.writeLog(actually);
                    }
                });
            }
        }
    }

    public static void showError(String message) {
        if (AuthMe.getInstance().isEnabled()) {
            log.warning("[AuthMe] ERROR: " + message);
            if (Settings.useLogging.booleanValue()) {
                Calendar date = Calendar.getInstance();
                final String actually = "[" + DateFormat.getDateInstance().format(date.getTime()) + ", " + date.get(11) + ":" + date.get(12) + ":" + date.get(13) + "] ERROR : " + message;
                Bukkit.getScheduler().runTaskAsynchronously((Plugin)AuthMe.getInstance(), new Runnable(){

                    public void run() {
                        ConsoleLogger.writeLog(actually);
                    }
                });
            }
        }
    }

    public static void writeLog(String string) {
        try {
            FileWriter fw = new FileWriter(AuthMe.getInstance().getDataFolder() + File.separator + "authme.log", true);
            BufferedWriter w = new BufferedWriter(fw);
            w.write(string);
            w.newLine();
            w.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

