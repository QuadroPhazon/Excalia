/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ess3.api.IUser;

public class FormatUtil {
    static final transient Pattern VANILLA_PATTERN = Pattern.compile("\u00a7+[0-9A-FK-ORa-fk-or]?");
    static final transient Pattern VANILLA_COLOR_PATTERN = Pattern.compile("\u00a7+[0-9A-Fa-f]");
    static final transient Pattern VANILLA_MAGIC_PATTERN = Pattern.compile("\u00a7+[Kk]");
    static final transient Pattern VANILLA_FORMAT_PATTERN = Pattern.compile("\u00a7+[L-ORl-or]");
    static final transient Pattern REPLACE_ALL_PATTERN = Pattern.compile("(?<!&)&([0-9a-fk-orA-FK-OR])");
    static final transient Pattern REPLACE_COLOR_PATTERN = Pattern.compile("(?<!&)&([0-9a-fA-F])");
    static final transient Pattern REPLACE_MAGIC_PATTERN = Pattern.compile("(?<!&)&([Kk])");
    static final transient Pattern REPLACE_FORMAT_PATTERN = Pattern.compile("(?<!&)&([l-orL-OR])");
    static final transient Pattern REPLACE_PATTERN = Pattern.compile("&&(?=[0-9a-fk-orA-FK-OR])");
    static final transient Pattern LOGCOLOR_PATTERN = Pattern.compile("\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]");
    static final transient Pattern URL_PATTERN = Pattern.compile("((?:(?:https?)://)?[\\w-_\\.]{2,})\\.([a-zA-Z]{2,3}(?:/\\S+)?)");
    public static final Pattern IPPATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static String stripFormat(String input) {
        if (input == null) {
            return null;
        }
        return FormatUtil.stripColor(input, VANILLA_PATTERN);
    }

    public static String stripEssentialsFormat(String input) {
        if (input == null) {
            return null;
        }
        return FormatUtil.stripColor(input, REPLACE_ALL_PATTERN);
    }

    public static String formatMessage(IUser user, String permBase, String input) {
        if (input == null) {
            return null;
        }
        String message = FormatUtil.formatString(user, permBase, input);
        if (!user.isAuthorized(permBase + ".url")) {
            message = FormatUtil.blockURL(message);
        }
        return message;
    }

    public static String replaceFormat(String input) {
        if (input == null) {
            return null;
        }
        return FormatUtil.replaceColor(input, REPLACE_ALL_PATTERN);
    }

    static String replaceColor(String input, Pattern pattern) {
        return REPLACE_PATTERN.matcher(pattern.matcher(input).replaceAll("\u00a7$1")).replaceAll("&");
    }

    public static String formatString(IUser user, String permBase, String input) {
        if (input == null) {
            return null;
        }
        String message = user.isAuthorized(permBase + ".color") || user.isAuthorized(permBase + ".colour") ? FormatUtil.replaceColor(input, REPLACE_COLOR_PATTERN) : FormatUtil.stripColor(input, VANILLA_COLOR_PATTERN);
        message = user.isAuthorized(permBase + ".magic") ? FormatUtil.replaceColor(message, REPLACE_MAGIC_PATTERN) : FormatUtil.stripColor(message, VANILLA_MAGIC_PATTERN);
        message = user.isAuthorized(permBase + ".format") ? FormatUtil.replaceColor(message, REPLACE_FORMAT_PATTERN) : FormatUtil.stripColor(message, VANILLA_FORMAT_PATTERN);
        return message;
    }

    public static String stripLogColorFormat(String input) {
        if (input == null) {
            return null;
        }
        return FormatUtil.stripColor(input, LOGCOLOR_PATTERN);
    }

    static String stripColor(String input, Pattern pattern) {
        return pattern.matcher(input).replaceAll("");
    }

    public static String lastCode(String input) {
        int pos = input.lastIndexOf(167);
        if (pos == -1 || pos + 1 == input.length()) {
            return "";
        }
        return input.substring(pos, pos + 2);
    }

    static String blockURL(String input) {
        if (input == null) {
            return null;
        }
        String text = URL_PATTERN.matcher(input).replaceAll("$1 $2");
        while (URL_PATTERN.matcher(text).find()) {
            text = URL_PATTERN.matcher(text).replaceAll("$1 $2");
        }
        return text;
    }

    public static boolean validIP(String ipAddress) {
        return IPPATTERN.matcher(ipAddress).matches();
    }
}

