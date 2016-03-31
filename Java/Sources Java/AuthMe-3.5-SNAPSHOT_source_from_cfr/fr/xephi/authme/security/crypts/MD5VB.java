/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5VB
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        return "$MD5vb$" + salt + "$" + MD5VB.getMD5(new StringBuilder().append(MD5VB.getMD5(password)).append(salt).toString());
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        String[] line = hash.split("\\$");
        return hash.equals(this.getHash(password, line[2], ""));
    }

    private static String getMD5(String message) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(message.getBytes());
        byte[] digest = md5.digest();
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }
}

