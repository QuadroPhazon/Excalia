/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.storage.ObjectLoadException;
import com.earth2me.essentials.storage.StorageObject;
import com.earth2me.essentials.storage.YamlStorageReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractDelayedYamlFileReader<T extends StorageObject>
implements Runnable {
    private final transient File file;
    private final transient Class<T> clazz;
    protected final transient IEssentials plugin;

    public AbstractDelayedYamlFileReader(IEssentials ess, File file, Class<T> clazz) {
        this.file = file;
        this.clazz = clazz;
        this.plugin = ess;
        ess.runTaskAsynchronously(this);
    }

    public abstract void onStart();

    @Override
    public void run() {
        this.onStart();
        try {
            FileReader reader = new FileReader(this.file);
            try {
                T object = new YamlStorageReader(reader, this.plugin).load(this.clazz);
                this.onSuccess(object);
            }
            finally {
                try {
                    reader.close();
                }
                catch (IOException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "File can't be closed: " + this.file.toString(), ex);
                }
            }
        }
        catch (FileNotFoundException ex) {
            this.onException();
            if (this.plugin.getSettings() == null || this.plugin.getSettings().isDebug()) {
                Bukkit.getLogger().log(Level.INFO, "File not found: " + this.file.toString());
            }
        }
        catch (ObjectLoadException ex) {
            this.onException();
            Bukkit.getLogger().log(Level.SEVERE, "File broken: " + this.file.toString(), ex.getCause());
        }
    }

    public abstract void onSuccess(T var1);

    public abstract void onException();
}

