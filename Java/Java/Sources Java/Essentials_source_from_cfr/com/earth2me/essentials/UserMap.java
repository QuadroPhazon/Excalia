/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.util.concurrent.UncheckedExecutionException
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials;

import com.earth2me.essentials.IConf;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.StringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class UserMap
extends CacheLoader<String, User>
implements IConf {
    private final transient IEssentials ess;
    private final transient Cache<String, User> users;
    private final transient ConcurrentSkipListSet<String> keys = new ConcurrentSkipListSet();

    public UserMap(IEssentials ess) {
        this.ess = ess;
        this.users = CacheBuilder.newBuilder().maximumSize(ess.getSettings().getMaxUserCacheCount()).softValues().build((CacheLoader)this);
        this.loadAllUsersAsync(ess);
    }

    private void loadAllUsersAsync(final IEssentials ess) {
        ess.runTaskAsynchronously(new Runnable(){

            @Override
            public void run() {
                File userdir = new File(ess.getDataFolder(), "userdata");
                if (!userdir.exists()) {
                    return;
                }
                UserMap.this.keys.clear();
                UserMap.this.users.invalidateAll();
                for (String string : userdir.list()) {
                    if (!string.endsWith(".yml")) continue;
                    String name = string.substring(0, string.length() - 4);
                    UserMap.this.keys.add(StringUtil.sanitizeFileName(name));
                }
            }
        });
    }

    public boolean userExists(String name) {
        return this.keys.contains(StringUtil.sanitizeFileName(name));
    }

    public User getUser(String name) {
        try {
            String sanitizedName = StringUtil.sanitizeFileName(name);
            return (User)this.users.get((Object)sanitizedName);
        }
        catch (ExecutionException ex) {
            return null;
        }
        catch (UncheckedExecutionException ex) {
            return null;
        }
    }

    public User load(String sanitizedName) throws Exception {
        for (Player player : this.ess.getServer().getOnlinePlayers()) {
            String sanitizedPlayer = StringUtil.sanitizeFileName(player.getName());
            if (!sanitizedPlayer.equalsIgnoreCase(sanitizedName)) continue;
            this.keys.add(sanitizedName);
            return new User(player, this.ess);
        }
        File userFile = this.getUserFile2(sanitizedName);
        if (userFile.exists()) {
            this.keys.add(sanitizedName);
            return new User(new OfflinePlayer(sanitizedName, this.ess), this.ess);
        }
        throw new Exception("User not found!");
    }

    @Override
    public void reloadConfig() {
        this.loadAllUsersAsync(this.ess);
    }

    public void removeUser(String name) {
        this.keys.remove(StringUtil.sanitizeFileName(name));
        this.users.invalidate((Object)StringUtil.sanitizeFileName(name));
        this.users.invalidate((Object)name);
    }

    public Set<String> getAllUniqueUsers() {
        return Collections.unmodifiableSet(this.keys);
    }

    public int getUniqueUsers() {
        return this.keys.size();
    }

    public File getUserFile(String name) {
        return this.getUserFile2(StringUtil.sanitizeFileName(name));
    }

    private File getUserFile2(String name) {
        File userFolder = new File(this.ess.getDataFolder(), "userdata");
        return new File(userFolder, name + ".yml");
    }

}

