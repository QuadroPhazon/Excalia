/*
 * Decompiled with CFR 0_110.
 */
package com.maxmind.geoip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseInfo {
    public static final int COUNTRY_EDITION = 1;
    public static final int REGION_EDITION_REV0 = 7;
    public static final int REGION_EDITION_REV1 = 3;
    public static final int CITY_EDITION_REV0 = 6;
    public static final int CITY_EDITION_REV1 = 2;
    public static final int ORG_EDITION = 5;
    public static final int ISP_EDITION = 4;
    public static final int PROXY_EDITION = 8;
    public static final int ASNUM_EDITION = 9;
    public static final int NETSPEED_EDITION = 10;
    public static final int COUNTRY_EDITION_V6 = 12;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    private String info;

    public DatabaseInfo(String info) {
        this.info = info;
    }

    public int getType() {
        if (this.info == null || this.info.equals("")) {
            return 1;
        }
        return Integer.parseInt(this.info.substring(4, 7)) - 105;
    }

    public boolean isPremium() {
        return this.info.indexOf("FREE") < 0;
    }

    public Date getDate() {
        for (int i = 0; i < this.info.length() - 9; ++i) {
            if (!Character.isWhitespace(this.info.charAt(i))) continue;
            String dateString = this.info.substring(i + 1, i + 9);
            try {
                SimpleDateFormat simpleDateFormat = formatter;
                synchronized (simpleDateFormat) {
                    return formatter.parse(dateString);
                }
            }
            catch (ParseException pe) {
                break;
            }
        }
        return null;
    }

    public String toString() {
        return this.info;
    }
}

