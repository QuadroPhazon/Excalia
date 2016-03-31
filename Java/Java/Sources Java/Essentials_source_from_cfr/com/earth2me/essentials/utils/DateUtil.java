/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.utils;

import com.earth2me.essentials.I18n;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
    public static long parseDateDiff(String time, boolean future) throws Exception {
        Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) continue;
            for (int i = 0; i < m.groupCount(); ++i) {
                if (m.group(i) == null || m.group(i).isEmpty()) continue;
                found = true;
                break;
            }
            if (!found) continue;
            if (m.group(1) != null && !m.group(1).isEmpty()) {
                years = Integer.parseInt(m.group(1));
            }
            if (m.group(2) != null && !m.group(2).isEmpty()) {
                months = Integer.parseInt(m.group(2));
            }
            if (m.group(3) != null && !m.group(3).isEmpty()) {
                weeks = Integer.parseInt(m.group(3));
            }
            if (m.group(4) != null && !m.group(4).isEmpty()) {
                days = Integer.parseInt(m.group(4));
            }
            if (m.group(5) != null && !m.group(5).isEmpty()) {
                hours = Integer.parseInt(m.group(5));
            }
            if (m.group(6) != null && !m.group(6).isEmpty()) {
                minutes = Integer.parseInt(m.group(6));
            }
            if (m.group(7) == null || m.group(7).isEmpty()) break;
            seconds = Integer.parseInt(m.group(7));
            break;
        }
        if (!found) {
            throw new Exception(I18n._("illegalDate", new Object[0]));
        }
        GregorianCalendar c = new GregorianCalendar();
        if (years > 0) {
            c.add(1, years * (future ? 1 : -1));
        }
        if (months > 0) {
            c.add(2, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            c.add(3, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            c.add(5, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            c.add(11, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            c.add(12, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            c.add(13, seconds * (future ? 1 : -1));
        }
        GregorianCalendar max = new GregorianCalendar();
        max.add(1, 10);
        if (c.after(max)) {
            return max.getTimeInMillis();
        }
        return c.getTimeInMillis();
    }

    static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while (future && !fromDate.after(toDate) || !future && !fromDate.before(toDate)) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            ++diff;
        }
        fromDate.setTimeInMillis(savedDate);
        return --diff;
    }

    public static String formatDateDiff(long date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        GregorianCalendar now = new GregorianCalendar();
        return DateUtil.formatDateDiff(now, c);
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return I18n._("now", new Object[0]);
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        StringBuilder sb = new StringBuilder();
        int[] types = new int[]{1, 2, 5, 11, 12, 13};
        String[] names = new String[]{I18n._("year", new Object[0]), I18n._("years", new Object[0]), I18n._("month", new Object[0]), I18n._("months", new Object[0]), I18n._("day", new Object[0]), I18n._("days", new Object[0]), I18n._("hour", new Object[0]), I18n._("hours", new Object[0]), I18n._("minute", new Object[0]), I18n._("minutes", new Object[0]), I18n._("second", new Object[0]), I18n._("seconds", new Object[0])};
        int accuracy = 0;
        for (int i = 0; i < types.length && accuracy <= 2; ++i) {
            int diff = DateUtil.dateDiff(types[i], fromDate, toDate, future);
            if (diff <= 0) continue;
            ++accuracy;
            sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
        }
        if (sb.length() == 0) {
            return "now";
        }
        return sb.toString().trim();
    }
}

