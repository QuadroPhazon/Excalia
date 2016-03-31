/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.settings;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayersLogs
extends CustomConfiguration {
    private static PlayersLogs pllog = null;
    public List<String> players;

    public PlayersLogs() {
        super(new File("." + File.separator + "plugins" + File.separator + "AuthMe" + File.separator + "players.yml"));
        pllog = this;
        this.load();
        this.save();
        this.players = this.getStringList("players");
    }

    public void clear() {
        this.set("players", new ArrayList());
        this.save();
    }

    public static PlayersLogs getInstance() {
        if (pllog == null) {
            pllog = new PlayersLogs();
        }
        return pllog;
    }

    public void addPlayer(String user) {
        this.players = this.getStringList("players");
        if (!this.players.contains(user)) {
            this.players.add(user);
            this.set("players", this.players);
            this.save();
        }
    }

    public void removePlayer(String user) {
        this.players = this.getStringList("players");
        if (this.players.contains(user)) {
            this.players.remove(user);
            this.set("players", this.players);
            this.save();
        }
    }
}

