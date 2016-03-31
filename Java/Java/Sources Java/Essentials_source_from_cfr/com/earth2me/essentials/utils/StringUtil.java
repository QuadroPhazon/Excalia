/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.utils;

import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private static final Pattern INVALIDFILECHARS = Pattern.compile("[^a-z0-9]");
    private static final Pattern INVALIDCHARS = Pattern.compile("[^\t\n\r -~\u0085\u00a0-\ud7ff\ue000-\ufffc]");

    public static String sanitizeFileName(String name) {
        return StringUtil.safeString(name);
    }

    public static String safeString(String string) {
        return INVALIDFILECHARS.matcher(string.toLowerCase(Locale.ENGLISH)).replaceAll("_");
    }

    public static String sanitizeString(String string) {
        return INVALIDCHARS.matcher(string).replaceAll("");
    }

    public static /* varargs */ String joinList(Object ... list) {
        return StringUtil.joinList(", ", list);
    }

    public static /* varargs */ String joinList(String seperator, Object ... list) {
        StringBuilder buf = new StringBuilder();
        for (Object each : list) {
            if (buf.length() > 0) {
                buf.append(seperator);
            }
            if (each instanceof Collection) {
                buf.append(StringUtil.joinList(seperator, ((Collection)each).toArray()));
                continue;
            }
            try {
                buf.append(each.toString());
                continue;
            }
            catch (Exception e) {
                buf.append(each.toString());
            }
        }
        return buf.toString();
    }

    private StringUtil() {
    }
}

