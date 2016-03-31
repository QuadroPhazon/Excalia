/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.datasource;

import fr.xephi.authme.cache.auth.PlayerAuth;
import java.util.List;

public interface DataSource {
    public boolean isAuthAvailable(String var1);

    public PlayerAuth getAuth(String var1);

    public boolean saveAuth(PlayerAuth var1);

    public boolean updateSession(PlayerAuth var1);

    public boolean updatePassword(PlayerAuth var1);

    public int purgeDatabase(long var1);

    public List<String> autoPurgeDatabase(long var1);

    public boolean removeAuth(String var1);

    public boolean updateQuitLoc(PlayerAuth var1);

    public int getIps(String var1);

    public List<String> getAllAuthsByName(PlayerAuth var1);

    public List<String> getAllAuthsByIp(String var1);

    public List<String> getAllAuthsByEmail(String var1);

    public boolean updateEmail(PlayerAuth var1);

    public boolean updateSalt(PlayerAuth var1);

    public void close();

    public void reload();

    public void purgeBanned(List<String> var1);

    public DataSourceType getType();

    public boolean isLogged(String var1);

    public void setLogged(String var1);

    public void setUnlogged(String var1);

    public void purgeLogged();

    public int getAccountsRegistered();

    public void updateName(String var1, String var2);

    public List<PlayerAuth> getAllAuths();

    public static enum DataSourceType {
        MYSQL,
        FILE,
        SQLITE;
        

        private DataSourceType() {
        }
    }

}

