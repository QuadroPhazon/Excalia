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
import fr.xephi.authme.datasource.FlatFileThread;
import fr.xephi.authme.settings.Messages;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SqlToFlat
implements Converter {
    public AuthMe plugin;
    public DataSource database;
    public CommandSender sender;

    public SqlToFlat(AuthMe plugin, DataSource database, CommandSender sender) {
        this.plugin = plugin;
        this.database = database;
        this.sender = sender;
    }

    public void run() {
        try {
            FlatFileThread flat = new FlatFileThread();
            flat.start();
            List<PlayerAuth> auths = this.database.getAllAuths();
            int i = 0;
            int size = auths.size();
            for (PlayerAuth auth : auths) {
                flat.saveAuth(auth);
                if (++i % 100 != 0) continue;
                this.sender.sendMessage("Conversion Status : " + i + " / " + size);
            }
            if (flat != null && flat.isAlive()) {
                flat.interrupt();
            }
            this.sender.sendMessage("Successfully convert from SQL table to file auths.db");
            return;
        }
        catch (Exception ex) {
            ConsoleLogger.showError(ex.getMessage());
            Messages.getInstance()._(this.sender, "error");
            return;
        }
    }
}

