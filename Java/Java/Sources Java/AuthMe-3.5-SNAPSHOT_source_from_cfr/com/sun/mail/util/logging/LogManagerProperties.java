/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.util.logging;

import java.io.ObjectStreamException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import javax.mail.Authenticator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class LogManagerProperties
extends Properties {
    private static final long serialVersionUID = -2239983349056806252L;
    private static final LogManager LOG_MANAGER = LogManager.getLogManager();
    private final String prefix;

    static LogManager getLogManager() {
        return LOG_MANAGER;
    }

    static String toLanguageTag(Locale locale) {
        String l = locale.getLanguage();
        String c = locale.getCountry();
        String v = locale.getVariant();
        char[] b = new char[l.length() + c.length() + v.length() + 2];
        int count = l.length();
        l.getChars(0, count, b, 0);
        if (c.length() != 0 || l.length() != 0 && v.length() != 0) {
            b[count] = 45;
            c.getChars(0, c.length(), b, ++count);
            count += c.length();
        }
        if (v.length() != 0 && (l.length() != 0 || c.length() != 0)) {
            b[count] = 45;
            v.getChars(0, v.length(), b, ++count);
            count += v.length();
        }
        return String.valueOf(b, 0, count);
    }

    static Filter newFilter(String name) throws Exception {
        return (Filter)LogManagerProperties.newObjectFrom(name, Filter.class);
    }

    static Formatter newFormatter(String name) throws Exception {
        return (Formatter)LogManagerProperties.newObjectFrom(name, Formatter.class);
    }

    static Comparator<? super LogRecord> newComparator(String name) throws Exception {
        return (Comparator)LogManagerProperties.newObjectFrom(name, Comparator.class);
    }

    static <T> Comparator<T> reverseOrder(Comparator<T> c) {
        Comparator reverse;
        block7 : {
            reverse = null;
            try {
                Method m = c.getClass().getMethod("reverseOrder", new Class[0]);
                if (!Comparator.class.isAssignableFrom(m.getReturnType())) break block7;
                try {
                    reverse = (Comparator)m.invoke(c, new Object[0]);
                }
                catch (ExceptionInInitializerError eiie) {
                    throw LogManagerProperties.wrapOrThrow(eiie);
                }
            }
            catch (NoSuchMethodException ignore) {
            }
            catch (IllegalAccessException ignore) {
            }
            catch (InvocationTargetException ite) {
                LogManagerProperties.paramOrError(ite);
            }
        }
        if (reverse == null) {
            reverse = Collections.reverseOrder(c);
        }
        return reverse;
    }

    static ErrorManager newErrorManager(String name) throws Exception {
        return (ErrorManager)LogManagerProperties.newObjectFrom(name, ErrorManager.class);
    }

    static Authenticator newAuthenticator(String name) throws Exception {
        return (Authenticator)LogManagerProperties.newObjectFrom(name, Authenticator.class);
    }

    private static <T> T newObjectFrom(String name, Class<T> type) throws Exception {
        try {
            Class clazz = LogManagerProperties.findClass(name);
            if (type.isAssignableFrom(clazz)) {
                try {
                    return type.cast(clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
                }
                catch (InvocationTargetException ITE) {
                    throw LogManagerProperties.paramOrError(ITE);
                }
            }
            throw new ClassCastException(clazz.getName() + " cannot be cast to " + type.getName());
        }
        catch (NoClassDefFoundError NCDFE) {
            throw new ClassNotFoundException(NCDFE.toString(), NCDFE);
        }
        catch (ExceptionInInitializerError EIIE) {
            throw LogManagerProperties.wrapOrThrow(EIIE);
        }
    }

    private static Exception paramOrError(InvocationTargetException ite) {
        Throwable cause = ite.getCause();
        if (cause != null && (cause instanceof VirtualMachineError || cause instanceof ThreadDeath)) {
            throw (Error)cause;
        }
        return ite;
    }

    private static InvocationTargetException wrapOrThrow(ExceptionInInitializerError eiie) {
        if (eiie.getCause() instanceof Error) {
            throw eiie;
        }
        return new InvocationTargetException(eiie);
    }

    private static Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz;
        ClassLoader[] loaders = LogManagerProperties.getClassLoaders();
        assert (loaders.length == 2);
        if (loaders[0] != null) {
            try {
                clazz = Class.forName(name, false, loaders[0]);
            }
            catch (ClassNotFoundException tryContext) {
                clazz = LogManagerProperties.tryLoad(name, loaders[1]);
            }
        } else {
            clazz = LogManagerProperties.tryLoad(name, loaders[1]);
        }
        return clazz;
    }

    private static Class<?> tryLoad(String name, ClassLoader l) throws ClassNotFoundException {
        if (l != null) {
            return Class.forName(name, false, l);
        }
        return Class.forName(name);
    }

    private static ClassLoader[] getClassLoaders() {
        return (ClassLoader[])AccessController.doPrivileged(new PrivilegedAction<ClassLoader[]>(){

            @Override
            public ClassLoader[] run() {
                ClassLoader[] loaders = new ClassLoader[2];
                try {
                    loaders[0] = ClassLoader.getSystemClassLoader();
                }
                catch (SecurityException ignore) {
                    loaders[0] = null;
                }
                try {
                    loaders[1] = Thread.currentThread().getContextClassLoader();
                }
                catch (SecurityException ignore) {
                    loaders[1] = null;
                }
                return loaders;
            }
        });
    }

    LogManagerProperties(Properties parent, String prefix) {
        super(parent);
        parent.isEmpty();
        if (prefix == null) {
            throw new NullPointerException();
        }
        this.prefix = prefix;
        super.isEmpty();
    }

    @Override
    public synchronized Object clone() {
        return this.exportCopy(this.defaults);
    }

    @Override
    public synchronized String getProperty(String key) {
        String value = this.defaults.getProperty(key);
        if (value == null) {
            LogManager manager = LogManagerProperties.getLogManager();
            if (key.length() > 0) {
                value = manager.getProperty(this.prefix + '.' + key);
            }
            if (value == null) {
                value = manager.getProperty(key);
            }
            if (value != null) {
                super.put(key, value);
            } else {
                Object v = super.get(key);
                value = v instanceof String ? (String)v : null;
            }
        }
        return value;
    }

    @Override
    public String getProperty(String key, String def) {
        String value = this.getProperty(key);
        return value == null ? def : value;
    }

    @Override
    public Object get(Object key) {
        if (key instanceof String) {
            return this.getProperty((String)key);
        }
        return super.get(key);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        Object def = this.preWrite(key);
        Object man = super.put(key, value);
        return man == null ? def : man;
    }

    @Override
    public Object setProperty(String key, String value) {
        return this.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return this.getProperty((String)key) != null;
        }
        return super.containsKey(key);
    }

    @Override
    public synchronized Object remove(Object key) {
        Object def = this.preWrite(key);
        Object man = super.remove(key);
        return man == null ? def : man;
    }

    public Enumeration propertyNames() {
        assert (false);
        return super.propertyNames();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Properties)) {
            return false;
        }
        assert (false);
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        assert (false);
        return super.hashCode();
    }

    private Object preWrite(Object key) {
        assert (Thread.holdsLock(this));
        String value = key instanceof String && !super.containsKey(key) ? this.getProperty((String)key) : null;
        return value;
    }

    private Properties exportCopy(Properties parent) {
        Thread.holdsLock(this);
        Properties child = new Properties(parent);
        child.putAll(this);
        return child;
    }

    private synchronized Object writeReplace() throws ObjectStreamException {
        assert (false);
        return this.exportCopy((Properties)this.defaults.clone());
    }

}

