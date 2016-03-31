/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials;

import com.earth2me.essentials.AlternativeCommandsHandler;
import com.earth2me.essentials.Backup;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.EssentialsTimer;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IConf;
import com.earth2me.essentials.IEssentialsModule;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.TNTExplodeListener;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.metrics.Metrics;
import com.earth2me.essentials.perm.PermissionsHandler;
import com.earth2me.essentials.register.payment.Methods;
import java.util.List;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public interface IEssentials
extends Plugin {
    public void addReloadListener(IConf var1);

    public void reload();

    public boolean onCommandEssentials(CommandSender var1, Command var2, String var3, String[] var4, ClassLoader var5, String var6, String var7, IEssentialsModule var8);

    @Deprecated
    public User getUser(Object var1);

    public User getUser(String var1);

    public User getUser(Player var1);

    public I18n getI18n();

    public User getOfflineUser(String var1);

    public World getWorld(String var1);

    public int broadcastMessage(String var1);

    public int broadcastMessage(IUser var1, String var2);

    public int broadcastMessage(String var1, String var2);

    public ISettings getSettings();

    public BukkitScheduler getScheduler();

    public IJails getJails();

    public IWarps getWarps();

    public Worth getWorth();

    public Backup getBackup();

    public Methods getPaymentMethod();

    public BukkitTask runTaskAsynchronously(Runnable var1);

    public BukkitTask runTaskLaterAsynchronously(Runnable var1, long var2);

    public int scheduleSyncDelayedTask(Runnable var1);

    public int scheduleSyncDelayedTask(Runnable var1, long var2);

    public int scheduleSyncRepeatingTask(Runnable var1, long var2, long var4);

    public TNTExplodeListener getTNTListener();

    public PermissionsHandler getPermissionsHandler();

    public AlternativeCommandsHandler getAlternativeCommandsHandler();

    public void showError(CommandSource var1, Throwable var2, String var3);

    public IItemDb getItemDb();

    public UserMap getUserMap();

    public Metrics getMetrics();

    public void setMetrics(Metrics var1);

    public EssentialsTimer getTimer();

    public List<String> getVanishedPlayers();
}

