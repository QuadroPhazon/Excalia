/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.IConf;
import com.earth2me.essentials.storage.AbstractDelayedYamlFileReader;
import com.earth2me.essentials.storage.AbstractDelayedYamlFileWriter;
import com.earth2me.essentials.storage.IStorageObjectHolder;
import com.earth2me.essentials.storage.StorageObject;
import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import net.ess3.api.IReload;
import org.bukkit.Bukkit;

public abstract class AsyncStorageObjectHolder<T extends StorageObject>
implements IConf,
IStorageObjectHolder<T>,
IReload {
    private transient T data;
    private final transient ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final transient Class<T> clazz;
    protected final transient IEssentials ess;

    public AsyncStorageObjectHolder(IEssentials ess, Class<T> clazz) {
        this.ess = ess;
        this.clazz = clazz;
        try {
            this.data = (StorageObject)clazz.newInstance();
        }
        catch (IllegalAccessException ex) {
            Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (InstantiationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public void acquireReadLock() {
        this.rwl.readLock().lock();
    }

    @Override
    public void acquireWriteLock() {
        while (this.rwl.getReadHoldCount() > 0) {
            this.rwl.readLock().unlock();
        }
        this.rwl.writeLock().lock();
        this.rwl.readLock().lock();
    }

    @Override
    public void close() {
        this.unlock();
    }

    @Override
    public void unlock() {
        if (this.rwl.isWriteLockedByCurrentThread()) {
            this.rwl.writeLock().unlock();
            new StorageObjectDataWriter();
        }
        while (this.rwl.getReadHoldCount() > 0) {
            this.rwl.readLock().unlock();
        }
    }

    @Override
    public void reloadConfig() {
        new StorageObjectDataReader();
    }

    @Override
    public void onReload() {
        new StorageObjectDataReader();
    }

    public abstract void finishRead();

    public abstract void finishWrite();

    public abstract File getStorageFile();

    private class StorageObjectDataReader
    extends AbstractDelayedYamlFileReader<T> {
        StorageObjectDataReader() {
            super(AsyncStorageObjectHolder.this.ess, AsyncStorageObjectHolder.this.getStorageFile(), AsyncStorageObjectHolder.this.clazz);
        }

        @Override
        public void onStart() {
            AsyncStorageObjectHolder.this.rwl.writeLock().lock();
        }

        @Override
        public void onSuccess(T object) {
            if (object != null) {
                AsyncStorageObjectHolder.this.data = object;
            }
            AsyncStorageObjectHolder.this.rwl.writeLock().unlock();
            AsyncStorageObjectHolder.this.finishRead();
        }

        @Override
        public void onException() {
            if (AsyncStorageObjectHolder.this.data == null) {
                try {
                    AsyncStorageObjectHolder.this.data = (StorageObject)AsyncStorageObjectHolder.this.clazz.newInstance();
                }
                catch (IllegalAccessException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                }
                catch (InstantiationException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            AsyncStorageObjectHolder.this.rwl.writeLock().unlock();
        }
    }

    private class StorageObjectDataWriter
    extends AbstractDelayedYamlFileWriter {
        StorageObjectDataWriter() {
            super(AsyncStorageObjectHolder.this.ess, AsyncStorageObjectHolder.this.getStorageFile());
        }

        @Override
        public StorageObject getObject() {
            AsyncStorageObjectHolder.this.acquireReadLock();
            return AsyncStorageObjectHolder.this.getData();
        }

        @Override
        public void onFinish() {
            AsyncStorageObjectHolder.this.unlock();
            AsyncStorageObjectHolder.this.finishWrite();
        }
    }

}

