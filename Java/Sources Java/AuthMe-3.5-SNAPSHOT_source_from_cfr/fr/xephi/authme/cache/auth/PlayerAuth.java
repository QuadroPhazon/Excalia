/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.cache.auth;

import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.settings.Settings;

public class PlayerAuth {
    private String nickname = "";
    private String hash = "";
    private String ip = "198.18.0.1";
    private long lastLogin = 0;
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private String world = "world";
    private String salt = "";
    private String vBhash = null;
    private int groupId = -1;
    private String email = "your@email.com";

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.email = email;
    }

    public PlayerAuth(String nickname, double x, double y, double z, String world) {
        this.nickname = nickname;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.lastLogin = System.currentTimeMillis();
    }

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin, double x, double y, double z, String world, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.email = email;
    }

    public PlayerAuth(String nickname, String hash, String salt, int groupId, String ip, long lastLogin, double x, double y, double z, String world, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.salt = salt;
        this.groupId = groupId;
        this.email = email;
    }

    public PlayerAuth(String nickname, String hash, String salt, int groupId, String ip, long lastLogin) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.salt = salt;
        this.groupId = groupId;
    }

    public PlayerAuth(String nickname, String hash, String salt, String ip, long lastLogin) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.salt = salt;
    }

    public PlayerAuth(String nickname, String hash, String salt, String ip, long lastLogin, double x, double y, double z, String world, String email) {
        this.nickname = nickname;
        this.hash = hash;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.salt = salt;
        this.email = email;
    }

    public PlayerAuth(String nickname, String ip, long lastLogin) {
        this.nickname = nickname;
        this.ip = ip;
        this.lastLogin = lastLogin;
    }

    public PlayerAuth(String nickname, String hash, String ip, long lastLogin) {
        this.nickname = nickname;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.hash = hash;
    }

    public String getIp() {
        if (this.ip == null || this.ip.isEmpty()) {
            this.ip = "127.0.0.1";
        }
        return this.ip;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getHash() {
        if (Settings.getPasswordHash == HashAlgorithm.MD5VB && this.salt != null && !this.salt.isEmpty() && Settings.getPasswordHash == HashAlgorithm.MD5VB) {
            this.vBhash = "$MD5vb$" + this.salt + "$" + this.hash;
            return this.vBhash;
        }
        return this.hash;
    }

    public String getSalt() {
        return this.salt;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public double getQuitLocX() {
        return this.x;
    }

    public double getQuitLocY() {
        return this.y;
    }

    public double getQuitLocZ() {
        return this.z;
    }

    public String getEmail() {
        return this.email;
    }

    public void setQuitLocX(double d) {
        this.x = d;
    }

    public void setQuitLocY(double d) {
        this.y = d;
    }

    public void setQuitLocZ(double d) {
        this.z = d;
    }

    public long getLastLogin() {
        try {
            if (Long.valueOf(this.lastLogin) == null) {
                this.lastLogin = 0;
            }
        }
        catch (NullPointerException e) {
            this.lastLogin = 0;
        }
        return this.lastLogin;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerAuth)) {
            return false;
        }
        PlayerAuth other = (PlayerAuth)obj;
        return other.getIp().equals(this.ip) && other.getNickname().equals(this.nickname);
    }

    public int hashCode() {
        int hashCode = 7;
        hashCode = 71 * hashCode + (this.nickname != null ? this.nickname.hashCode() : 0);
        hashCode = 71 * hashCode + (this.ip != null ? this.ip.hashCode() : 0);
        return hashCode;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getWorld() {
        return this.world;
    }

    public String toString() {
        String s = "Player : " + this.nickname + " ! IP : " + this.ip + " ! LastLogin : " + this.lastLogin + " ! LastPosition : " + this.x + "," + this.y + "," + this.z + "," + this.world + " ! Email : " + this.email + " ! Hash : " + this.hash + " ! Salt : " + this.salt;
        return s;
    }

    public void setName(String nickname) {
        this.nickname = nickname;
    }
}

