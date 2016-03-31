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
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.settings.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.CommandSender;

public class RakamakConverter
implements Converter {
    public AuthMe instance;
    public DataSource database;
    public CommandSender sender;
    private static Boolean useIP;
    private static String fileName;
    private static String ipFileName;
    private static File source;
    private static File ipfiles;

    public RakamakConverter(AuthMe instance, DataSource database, CommandSender sender) {
        this.instance = instance;
        this.database = database;
        this.sender = sender;
    }

    public RakamakConverter getInstance() {
        return this;
    }

    public void run() {
        HashAlgorithm hash = Settings.getPasswordHash;
        useIP = Settings.rakamakUseIp;
        fileName = Settings.rakamakUsers;
        ipFileName = Settings.rakamakUsersIp;
        HashMap<String, String> playerIP = new HashMap<String, String>();
        HashMap<String, String> playerPSW = new HashMap<String, String>();
        try {
            String line;
            source = new File(AuthMe.getInstance().getDataFolder() + File.separator + fileName);
            ipfiles = new File(AuthMe.getInstance().getDataFolder() + File.separator + ipFileName);
            source.createNewFile();
            ipfiles.createNewFile();
            BufferedReader users = null;
            BufferedReader ipFile = null;
            ipFile = new BufferedReader(new FileReader(ipfiles));
            if (useIP.booleanValue()) {
                String tempLine;
                while ((tempLine = ipFile.readLine()) != null) {
                    if (!tempLine.contains("=")) continue;
                    String[] args = tempLine.split("=");
                    playerIP.put(args[0], args[1]);
                }
            }
            ipFile.close();
            users = new BufferedReader(new FileReader(source));
            while ((line = users.readLine()) != null) {
                if (!line.contains("=")) continue;
                String[] arguments = line.split("=");
                try {
                    playerPSW.put(arguments[0], PasswordSecurity.getHash(hash, arguments[1], arguments[0]));
                }
                catch (NoSuchAlgorithmException e) {
                    ConsoleLogger.showError(e.getMessage());
                }
            }
            users.close();
            for (Map.Entry m : playerPSW.entrySet()) {
                String player = (String)m.getKey();
                String psw = (String)playerPSW.get(player);
                String ip = useIP != false ? (String)playerIP.get(player) : "127.0.0.1";
                PlayerAuth auth = new PlayerAuth(player, psw, ip, System.currentTimeMillis());
                if (PasswordSecurity.userSalt.containsKey(player)) {
                    auth.setSalt(PasswordSecurity.userSalt.get(player));
                }
                this.database.saveAuth(auth);
            }
            ConsoleLogger.info("Rakamak database has been imported correctly");
            this.sender.sendMessage("Rakamak database has been imported correctly");
        }
        catch (FileNotFoundException ex) {
            ConsoleLogger.showError(ex.getMessage());
            this.sender.sendMessage("Error file not found");
        }
        catch (IOException ex) {
            ConsoleLogger.showError(ex.getMessage());
            this.sender.sendMessage("Error IOException");
        }
    }
}

