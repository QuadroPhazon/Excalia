/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import net.ess3.api.II18n;

public class I18n
implements II18n {
    private static I18n instance;
    private static final String MESSAGES = "messages";
    private final transient Locale defaultLocale;
    private transient Locale currentLocale;
    private transient ResourceBundle customBundle;
    private transient ResourceBundle localeBundle;
    private final transient ResourceBundle defaultBundle;
    private final transient Map<String, MessageFormat> messageFormatCache;
    private final transient IEssentials ess;
    private static final Pattern NODOUBLEMARK;

    public I18n(IEssentials ess) {
        this.currentLocale = this.defaultLocale = Locale.getDefault();
        this.messageFormatCache = new HashMap<String, MessageFormat>();
        this.ess = ess;
        this.customBundle = ResourceBundle.getBundle("messages", this.defaultLocale, new FileResClassLoader(I18n.class.getClassLoader(), ess));
        this.localeBundle = ResourceBundle.getBundle("messages", this.defaultLocale);
        this.defaultBundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    }

    public void onEnable() {
        instance = this;
    }

    public void onDisable() {
        instance = null;
    }

    @Override
    public Locale getCurrentLocale() {
        return this.currentLocale;
    }

    private String translate(String string) {
        try {
            return this.customBundle.getString(string);
        }
        catch (MissingResourceException ex) {
            try {
                return this.localeBundle.getString(string);
            }
            catch (MissingResourceException ex) {
                Logger.getLogger("Essentials").log(Level.WARNING, String.format("Missing translation key \"%s\" in translation file %s", ex.getKey(), this.localeBundle.getLocale().toString()), ex);
                return this.defaultBundle.getString(string);
            }
        }
    }

    public static /* varargs */ String _(String string, Object ... objects) {
        if (instance == null) {
            return "";
        }
        if (objects.length == 0) {
            return NODOUBLEMARK.matcher(instance.translate(string)).replaceAll("'");
        }
        return instance.format(string, objects);
    }

    public /* varargs */ String format(String string, Object ... objects) {
        String format = this.translate(string);
        MessageFormat messageFormat = this.messageFormatCache.get(format);
        if (messageFormat == null) {
            try {
                messageFormat = new MessageFormat(format);
            }
            catch (IllegalArgumentException e) {
                this.ess.getLogger().log(Level.SEVERE, "Invalid Translation key for '" + string + "': " + e.getMessage());
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }
            this.messageFormatCache.put(format, messageFormat);
        }
        return messageFormat.format(objects);
    }

    public void updateLocale(String loc) {
        if (loc == null || loc.isEmpty()) {
            return;
        }
        String[] parts = loc.split("[_\\.]");
        if (parts.length == 1) {
            this.currentLocale = new Locale(parts[0]);
        }
        if (parts.length == 2) {
            this.currentLocale = new Locale(parts[0], parts[1]);
        }
        if (parts.length == 3) {
            this.currentLocale = new Locale(parts[0], parts[1], parts[2]);
        }
        ResourceBundle.clearCache();
        Logger.getLogger("Essentials").log(Level.INFO, String.format("Using locale %s", this.currentLocale.toString()));
        this.customBundle = ResourceBundle.getBundle("messages", this.currentLocale, new FileResClassLoader(I18n.class.getClassLoader(), this.ess));
        this.localeBundle = ResourceBundle.getBundle("messages", this.currentLocale);
    }

    public static String capitalCase(String input) {
        return input == null || input.length() == 0 ? input : "" + input.toUpperCase(Locale.ENGLISH).charAt(0) + input.toLowerCase(Locale.ENGLISH).substring(1);
    }

    static {
        NODOUBLEMARK = Pattern.compile("''");
    }

    private static class FileResClassLoader
    extends ClassLoader {
        private final transient File dataFolder;

        FileResClassLoader(ClassLoader classLoader, IEssentials ess) {
            super(classLoader);
            this.dataFolder = ess.getDataFolder();
        }

        @Override
        public URL getResource(String string) {
            File file = new File(this.dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                }
                catch (MalformedURLException ex) {
                    // empty catch block
                }
            }
            return super.getResource(string);
        }

        @Override
        public InputStream getResourceAsStream(String string) {
            File file = new File(this.dataFolder, string);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                }
                catch (FileNotFoundException ex) {
                    // empty catch block
                }
            }
            return super.getResourceAsStream(string);
        }
    }

}

