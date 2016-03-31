/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package fr.xephi.authme.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.converter.Converter;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.bukkit.command.CommandSender;

public class CrazyLoginConverter
implements Converter {
    public AuthMe instance;
    public DataSource database;
    public CommandSender sender;
    private static String fileName;
    private static File source;

    public CrazyLoginConverter(AuthMe instance, DataSource database, CommandSender sender) {
        this.instance = instance;
        this.database = database;
        this.sender = sender;
    }

    public CrazyLoginConverter getInstance() {
        return this;
    }

    public void run() {
        fileName = Settings.crazyloginFileName;
        try {
            String line;
            source = new File(AuthMe.getInstance().getDataFolder() + File.separator + fileName);
            if (!source.exists()) {
                this.sender.sendMessage("Error while trying to import datas, please put " + fileName + " in AuthMe folder!");
                return;
            }
            source.createNewFile();
            BufferedReader users = null;
            users = new BufferedReader(new FileReader(source));
            while ((line = users.readLine()) != null) {
                String[] args;
                if (!line.contains("|") || (args = line.split("\\|")).length < 2 || args[0].equalsIgnoreCase("name")) continue;
                String player = args[0];
                String psw = args[1];
                try {
                    if (player == null || psw == null) continue;
                    PlayerAuth auth = new PlayerAuth(player, psw, "127.0.0.1", System.currentTimeMillis());
                    this.database.saveAuth(auth);
                }
                catch (Exception e) {}
            }
            users.close();
            ConsoleLogger.info("CrazyLogin database has been imported correctly");
        }
        catch (FileNotFoundException ex) {
            ConsoleLogger.showError(ex.getMessage());
        }
        catch (IOException ex) {
            ConsoleLogger.showError(ex.getMessage());
        }
    }
}

