/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package fr.xephi.authme.datasource;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.cache.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.entity.Player;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class CacheDataSource
implements DataSource {
    private DataSource source;
    public AuthMe plugin;
    private HashMap<String, PlayerAuth> cache = new HashMap();

    public CacheDataSource(AuthMe plugin, DataSource source) {
        this.plugin = plugin;
        this.source = source;
    }

    @Override
    public synchronized boolean isAuthAvailable(String user) {
        if (this.cache.containsKey(user)) {
            return true;
        }
        return this.source.isAuthAvailable(user);
    }

    @Override
    public synchronized PlayerAuth getAuth(String user) {
        if (this.cache.containsKey(user)) {
            return this.cache.get(user);
        }
        PlayerAuth auth = this.source.getAuth(user);
        if (auth != null) {
            this.cache.put(user, auth);
        }
        return auth;
    }

    @Override
    public synchronized boolean saveAuth(PlayerAuth auth) {
        if (this.source.saveAuth(auth)) {
            this.cache.put(auth.getNickname(), auth);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean updatePassword(PlayerAuth auth) {
        if (this.source.updatePassword(auth)) {
            if (this.cache.containsKey(auth.getNickname())) {
                this.cache.get(auth.getNickname()).setHash(auth.getHash());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSession(PlayerAuth auth) {
        if (this.source.updateSession(auth)) {
            if (this.cache.containsKey(auth.getNickname())) {
                this.cache.get(auth.getNickname()).setIp(auth.getIp());
                this.cache.get(auth.getNickname()).setLastLogin(auth.getLastLogin());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateQuitLoc(PlayerAuth auth) {
        if (this.source.updateQuitLoc(auth)) {
            if (this.cache.containsKey(auth.getNickname())) {
                this.cache.get(auth.getNickname()).setQuitLocX(auth.getQuitLocX());
                this.cache.get(auth.getNickname()).setQuitLocY(auth.getQuitLocY());
                this.cache.get(auth.getNickname()).setQuitLocZ(auth.getQuitLocZ());
                this.cache.get(auth.getNickname()).setWorld(auth.getWorld());
            }
            return true;
        }
        return false;
    }

    @Override
    public int getIps(String ip) {
        return this.source.getIps(ip);
    }

    @Override
    public int purgeDatabase(long until) {
        int cleared = this.source.purgeDatabase(until);
        if (cleared > 0) {
            for (PlayerAuth auth : this.cache.values()) {
                if (auth.getLastLogin() >= until) continue;
                this.cache.remove(auth.getNickname());
            }
        }
        return cleared;
    }

    @Override
    public List<String> autoPurgeDatabase(long until) {
        List<String> cleared = this.source.autoPurgeDatabase(until);
        if (cleared.size() > 0) {
            for (PlayerAuth auth : this.cache.values()) {
                if (auth.getLastLogin() >= until) continue;
                this.cache.remove(auth.getNickname());
            }
        }
        return cleared;
    }

    @Override
    public synchronized boolean removeAuth(String user) {
        if (this.source.removeAuth(user)) {
            this.cache.remove(user);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void close() {
        this.source.close();
    }

    @Override
    public void reload() {
        this.cache.clear();
        this.source.reload();
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            String user = player.getName();
            if (!PlayerCache.getInstance().isAuthenticated(user)) continue;
            try {
                PlayerAuth auth = this.source.getAuth(user);
                this.cache.put(user, auth);
                continue;
            }
            catch (NullPointerException npe) {
                // empty catch block
            }
        }
    }

    @Override
    public synchronized boolean updateEmail(PlayerAuth auth) {
        if (this.source.updateEmail(auth)) {
            if (this.cache.containsKey(auth.getNickname())) {
                this.cache.get(auth.getNickname()).setEmail(auth.getEmail());
            }
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean updateSalt(PlayerAuth auth) {
        if (this.source.updateSalt(auth)) {
            if (this.cache.containsKey(auth.getNickname())) {
                this.cache.get(auth.getNickname()).setSalt(auth.getSalt());
            }
            return true;
        }
        return false;
    }

    @Override
    public synchronized List<String> getAllAuthsByName(PlayerAuth auth) {
        return this.source.getAllAuthsByName(auth);
    }

    @Override
    public synchronized List<String> getAllAuthsByIp(String ip) {
        return this.source.getAllAuthsByIp(ip);
    }

    @Override
    public synchronized List<String> getAllAuthsByEmail(String email) {
        return this.source.getAllAuthsByEmail(email);
    }

    @Override
    public synchronized void purgeBanned(List<String> banned) {
        this.source.purgeBanned(banned);
        for (PlayerAuth auth : this.cache.values()) {
            if (!banned.contains(auth.getNickname())) continue;
            this.cache.remove(auth.getNickname());
        }
    }

    @Override
    public DataSource.DataSourceType getType() {
        return this.source.getType();
    }

    @Override
    public boolean isLogged(String user) {
        return this.source.isLogged(user);
    }

    @Override
    public void setLogged(String user) {
        this.source.setLogged(user);
    }

    @Override
    public void setUnlogged(String user) {
        this.source.setUnlogged(user);
    }

    @Override
    public void purgeLogged() {
        this.source.purgeLogged();
    }

    @Override
    public int getAccountsRegistered() {
        return this.source.getAccountsRegistered();
    }

    @Override
    public void updateName(String oldone, String newone) {
        if (this.cache.containsKey(oldone)) {
            this.cache.put(newone, this.cache.get(oldone));
            this.cache.remove(oldone);
        }
        this.source.updateName(oldone, newone);
    }

    @Override
    public List<PlayerAuth> getAllAuths() {
        return this.source.getAllAuths();
    }
}

