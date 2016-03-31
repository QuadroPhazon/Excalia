/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.storage.StorageObject;

public interface IStorageObjectHolder<T extends StorageObject> {
    public T getData();

    public void acquireReadLock();

    public void acquireWriteLock();

    public void close();

    public void unlock();
}

