/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.yaml.snakeyaml.TypeDescription
 *  org.yaml.snakeyaml.Yaml
 *  org.yaml.snakeyaml.constructor.BaseConstructor
 *  org.yaml.snakeyaml.constructor.Constructor
 */
package com.earth2me.essentials.storage;

import com.earth2me.essentials.storage.BukkitConstructor;
import com.earth2me.essentials.storage.IStorageReader;
import com.earth2me.essentials.storage.ListType;
import com.earth2me.essentials.storage.MapKeyType;
import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.ObjectLoadException;
import com.earth2me.essentials.storage.StorageObject;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;

public class YamlStorageReader
implements IStorageReader {
    private static final transient Map<Class, Yaml> PREPARED_YAMLS = Collections.synchronizedMap(new HashMap());
    private static final transient Map<Class, ReentrantLock> LOCKS = new HashMap<Class, ReentrantLock>();
    private final transient Reader reader;
    private final transient Plugin plugin;

    public YamlStorageReader(Reader reader, Plugin plugin) {
        this.reader = reader;
        this.plugin = plugin;
    }

    @Override
    public <T extends StorageObject> T load(Class<? extends T> clazz) throws ObjectLoadException {
        ReentrantLock lock;
        Yaml yaml = PREPARED_YAMLS.get(clazz);
        if (yaml == null) {
            yaml = new Yaml((BaseConstructor)this.prepareConstructor(clazz));
            PREPARED_YAMLS.put(clazz, yaml);
        }
        Map<Class, ReentrantLock> map = LOCKS;
        synchronized (map) {
            lock = LOCKS.get(clazz);
            if (lock == null) {
                lock = new ReentrantLock();
            }
        }
        lock.lock();
        try {
            StorageObject object = (StorageObject)yaml.load(this.reader);
            if (object == null) {
                object = (StorageObject)clazz.newInstance();
            }
            StorageObject storageObject = object;
            return (T)storageObject;
        }
        catch (IllegalAccessException ex) {
            throw new ObjectLoadException(ex);
        }
        catch (InstantiationException ex) {
            throw new ObjectLoadException(ex);
        }
        finally {
            lock.unlock();
        }
    }

    private Constructor prepareConstructor(Class<?> clazz) {
        BukkitConstructor constructor = new BukkitConstructor(clazz, this.plugin);
        HashSet<Class> classes = new HashSet<Class>();
        this.prepareConstructor((Constructor)constructor, classes, clazz);
        return constructor;
    }

    private void prepareConstructor(Constructor constructor, Set<Class> classes, Class clazz) {
        classes.add(clazz);
        TypeDescription description = new TypeDescription(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            this.prepareList(field, description, classes, constructor);
            this.prepareMap(field, description, classes, constructor);
            if (!StorageObject.class.isAssignableFrom(field.getType()) || classes.contains(field.getType())) continue;
            this.prepareConstructor(constructor, classes, field.getType());
        }
        constructor.addTypeDescription(description);
    }

    private void prepareList(Field field, TypeDescription description, Set<Class> classes, Constructor constructor) {
        ListType listType = (ListType)field.getAnnotation(ListType.class);
        if (listType != null) {
            description.putListPropertyType(field.getName(), listType.value());
            if (StorageObject.class.isAssignableFrom(listType.value()) && !classes.contains(listType.value())) {
                this.prepareConstructor(constructor, classes, listType.value());
            }
        }
    }

    private void prepareMap(Field field, TypeDescription description, Set<Class> classes, Constructor constructor) {
        MapValueType mapType = (MapValueType)field.getAnnotation(MapValueType.class);
        if (mapType != null) {
            MapKeyType mapKeyType;
            description.putMapPropertyType(field.getName(), (mapKeyType = (MapKeyType)field.getAnnotation(MapKeyType.class)) == null ? String.class : mapKeyType.value(), mapType.value());
            if (StorageObject.class.isAssignableFrom(mapType.value()) && !classes.contains(mapType.value())) {
                this.prepareConstructor(constructor, classes, mapType.value());
            }
        }
    }
}

