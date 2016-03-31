/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.messaging.PluginMessageListener
 */
package fr.xephi.authme.plugin.manager;

import fr.xephi.authme.AuthMe;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeCordMessage
implements PluginMessageListener {
    public AuthMe plugin;

    public BungeeCordMessage(AuthMe plugin) {
        this.plugin = plugin;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subchannel = in.readUTF();
            if (subchannel.equals("IP")) {
                String ip = in.readUTF();
                this.plugin.realIp.put(player.getName(), ip);
            }
        }
        catch (IOException ex) {
            // empty catch block
        }
    }
}

