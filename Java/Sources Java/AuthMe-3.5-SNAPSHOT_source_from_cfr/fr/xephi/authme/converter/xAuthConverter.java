/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package fr.xephi.authme.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.converter.Converter;
import fr.xephi.authme.converter.newxAuthToFlat;
import fr.xephi.authme.converter.oldxAuthToFlat;
import fr.xephi.authme.datasource.DataSource;
import org.bukkit.command.CommandSender;

public class xAuthConverter
implements Converter {
    public AuthMe plugin;
    public DataSource database;
    public CommandSender sender;

    public xAuthConverter(AuthMe plugin, DataSource database, CommandSender sender) {
        this.plugin = plugin;
        this.database = database;
        this.sender = sender;
    }

    public void run() {
        try {
            Class.forName("com.cypherx.xauth.xAuth");
            oldxAuthToFlat converter = new oldxAuthToFlat(this.plugin, this.database, this.sender);
            converter.convert();
        }
        catch (ClassNotFoundException e) {
            try {
                Class.forName("de.luricos.bukkit.xAuth.xAuth");
                newxAuthToFlat converter = new newxAuthToFlat(this.plugin, this.database, this.sender);
                converter.convert();
            }
            catch (ClassNotFoundException ce) {
                this.sender.sendMessage("xAuth has not been found, please put xAuth.jar in your plugin folder and restart!");
            }
        }
    }
}

