/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.storage.ObjectLoadException;
import com.earth2me.essentials.storage.StorageObject;

public interface IStorageReader {
    public <T extends StorageObject> T load(Class<? extends T> var1) throws ObjectLoadException;
}

