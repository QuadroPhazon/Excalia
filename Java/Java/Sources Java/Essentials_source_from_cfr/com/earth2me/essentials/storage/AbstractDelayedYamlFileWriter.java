/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.storage.StorageObject;
import com.earth2me.essentials.storage.YamlStorageWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractDelayedYamlFileWriter
implements Runnable {
    private final transient File file;

    public AbstractDelayedYamlFileWriter(IEssentials ess, File file) {
        this.file = file;
        ess.runTaskAsynchronously(this);
    }

    public abstract StorageObject getObject();

    @Override
    public void run() {
        PrintWriter pw = null;
        try {
            StorageObject object = this.getObject();
            File folder = this.file.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            pw = new PrintWriter(this.file);
            new YamlStorageWriter(pw).save(object);
        }
        catch (FileNotFoundException ex) {
            Bukkit.getLogger().log(Level.SEVERE, this.file.toString(), ex);
        }
        finally {
            this.onFinish();
            if (pw != null) {
                pw.close();
            }
        }
    }

    public abstract void onFinish();
}

