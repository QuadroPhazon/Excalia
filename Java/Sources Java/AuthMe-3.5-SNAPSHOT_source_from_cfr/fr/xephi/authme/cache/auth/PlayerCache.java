/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.cache.auth;

import fr.xephi.authme.cache.auth.PlayerAuth;
import java.util.HashMap;

public class PlayerCache {
    private static PlayerCache singleton = null;
    private HashMap<String, PlayerAuth> cache = new HashMap();

    private PlayerCache() {
    }

    public void addPlayer(PlayerAuth auth) {
        this.cache.put(auth.getNickname(), auth);
    }

    public void updatePlayer(PlayerAuth auth) {
        this.cache.remove(auth.getNickname());
        this.cache.put(auth.getNickname(), auth);
    }

    public void removePlayer(String user) {
        this.cache.remove(user);
    }

    public boolean isAuthenticated(String user) {
        return this.cache.containsKey(user);
    }

    public PlayerAuth getAuth(String user) {
        return this.cache.get(user);
    }

    public static PlayerCache getInstance() {
        if (singleton == null) {
            singleton = new PlayerCache();
        }
        return singleton;
    }

    public int getLogged() {
        return this.cache.size();
    }
}

