/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 */
package fr.xephi.authme.converter;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.converter.Converter;
import fr.xephi.authme.converter.RoyalAuthYamlReader;
import fr.xephi.authme.datasource.DataSource;
import java.io.File;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class RoyalAuthConverter
implements Converter {
    public AuthMe plugin;
    private DataSource data;

    public RoyalAuthConverter(AuthMe plugin) {
        this.plugin = plugin;
        this.data = plugin.database;
    }

    public void run() {
        for (OfflinePlayer o : this.plugin.getServer().getOfflinePlayers()) {
            try {
                String name = o.getName().toLowerCase();
                String separator = File.separator;
                File file = new File("." + separator + "plugins" + separator + "RoyalAuth" + separator + "userdata" + separator + name + ".yml");
                if (this.data.isAuthAvailable(name) || !file.exists()) continue;
                RoyalAuthYamlReader ra = new RoyalAuthYamlReader(file);
                PlayerAuth auth = new PlayerAuth(o.getName(), ra.getHash(), "127.0.0.1", ra.getLastLogin(), "your@email.com");
                this.data.saveAuth(auth);
                continue;
            }
            catch (Exception e) {
                ConsoleLogger.showError("Error while trying to import " + o.getName() + " RoyalAuth datas");
            }
        }
    }
}

