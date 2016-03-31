/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package fr.xephi.authme.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.converter.Converter;
import fr.xephi.authme.converter.vAuthFileReader;
import fr.xephi.authme.datasource.DataSource;
import org.bukkit.command.CommandSender;

public class vAuthConverter
implements Converter {
    public AuthMe plugin;
    public DataSource database;
    public CommandSender sender;

    public vAuthConverter(AuthMe plugin, DataSource database, CommandSender sender) {
        this.plugin = plugin;
        this.database = database;
        this.sender = sender;
    }

    public void run() {
        try {
            new vAuthFileReader(this.plugin, this.database, this.sender).convert();
        }
        catch (Exception e) {
            this.sender.sendMessage(e.getMessage());
            ConsoleLogger.showError(e.getMessage());
        }
    }
}

