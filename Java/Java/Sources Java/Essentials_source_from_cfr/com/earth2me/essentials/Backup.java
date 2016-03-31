/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Backup
implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private final transient Server server;
    private final transient IEssentials ess;
    private transient boolean running = false;
    private transient int taskId = -1;
    private transient boolean active = false;

    public Backup(IEssentials ess) {
        this.ess = ess;
        this.server = ess.getServer();
        if (this.server.getOnlinePlayers().length > 0) {
            ess.runTaskAsynchronously(new Runnable(){

                @Override
                public void run() {
                    Backup.this.startTask();
                }
            });
        }
    }

    public void onPlayerJoin() {
        this.startTask();
    }

    public synchronized void stopTask() {
        this.running = false;
        if (this.taskId != -1) {
            this.server.getScheduler().cancelTask(this.taskId);
        }
        this.taskId = -1;
    }

    private synchronized void startTask() {
        if (!this.running) {
            long interval = this.ess.getSettings().getBackupInterval() * 1200;
            if (interval < 1200) {
                return;
            }
            this.taskId = this.ess.scheduleSyncRepeatingTask(this, interval, interval);
            this.running = true;
        }
    }

    @Override
    public void run() {
        if (this.active) {
            return;
        }
        this.active = true;
        final String command = this.ess.getSettings().getBackupCommand();
        if (command == null || "".equals(command)) {
            return;
        }
        if ("save-all".equalsIgnoreCase(command)) {
            ConsoleCommandSender cs = this.server.getConsoleSender();
            this.server.dispatchCommand((CommandSender)cs, "save-all");
            this.active = false;
            return;
        }
        LOGGER.log(Level.INFO, I18n._("backupStarted", new Object[0]));
        ConsoleCommandSender cs = this.server.getConsoleSender();
        this.server.dispatchCommand((CommandSender)cs, "save-all");
        this.server.dispatchCommand((CommandSender)cs, "save-off");
        this.ess.runTaskAsynchronously(new Runnable((CommandSender)cs){
            final /* synthetic */ CommandSender val$cs;

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                try {
                    ProcessBuilder childBuilder = new ProcessBuilder(command);
                    childBuilder.redirectErrorStream(true);
                    childBuilder.directory(Backup.this.ess.getDataFolder().getParentFile().getParentFile());
                    final Process child = childBuilder.start();
                    Backup.this.ess.runTaskAsynchronously(new Runnable(){

                        @Override
                        public void run() {
                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(child.getInputStream()));
                                try {
                                    String line;
                                    do {
                                        if ((line = reader.readLine()) == null) continue;
                                        LOGGER.log(Level.INFO, line);
                                    } while (line != null);
                                }
                                finally {
                                    reader.close();
                                }
                            }
                            catch (IOException ex) {
                                LOGGER.log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    child.waitFor();
                }
                catch (InterruptedException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    {
                        catch (Throwable throwable) {
                            Backup.this.ess.scheduleSyncDelayedTask(new Runnable(){

                                @Override
                                public void run() {
                                    Backup.this.server.dispatchCommand(2.this.val$cs, "save-on");
                                    if (Backup.this.server.getOnlinePlayers().length == 0) {
                                        Backup.this.stopTask();
                                    }
                                    Backup.this.active = false;
                                    LOGGER.log(Level.INFO, I18n._("backupFinished", new Object[0]));
                                }
                            });
                            throw throwable;
                        }
                    }
                    Backup.this.ess.scheduleSyncDelayedTask(new );
                    return;
                    catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                        Backup.this.ess.scheduleSyncDelayedTask(new );
                        return;
                    }
                }
                Backup.this.ess.scheduleSyncDelayedTask(new );
                return;
            }

        });
    }

}

