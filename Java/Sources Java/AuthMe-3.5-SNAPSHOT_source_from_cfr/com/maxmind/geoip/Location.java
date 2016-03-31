/*
 * Decompiled with CFR 0_110.
 */
package com.maxmind.geoip;

public class Location {
    public String countryCode;
    public String countryName;
    public String region;
    public String city;
    public String postalCode;
    public float latitude;
    public float longitude;
    public int dma_code;
    public int area_code;
    public int metro_code;
    private static final double EARTH_DIAMETER = 12756.4;
    private static final double PI = 3.14159265;
    private static final double RAD_CONVERT = 0.017453292500000002;

    public double distance(Location loc) {
        float lat1 = this.latitude;
        float lon1 = this.longitude;
        float lat2 = loc.latitude;
        float lon2 = loc.longitude;
        lat1 = (float)((double)lat1 * 0.017453292500000002);
        lat2 = (float)((double)lat2 * 0.017453292500000002);
        double delta_lat = lat2 - lat1;
        double delta_lon = (double)(lon2 - lon1) * 0.017453292500000002;
        double temp = Math.pow(Math.sin(delta_lat / 2.0), 2.0) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(delta_lon / 2.0), 2.0);
        return 12756.4 * Math.atan2(Math.sqrt(temp), Math.sqrt(1.0 - temp));
    }
}

