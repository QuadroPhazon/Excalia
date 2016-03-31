/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        return SHA1.getSHA1(password);
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        return hash.equals(this.getHash(password, "", ""));
    }

    private static String getSHA1(String message) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        sha1.update(message.getBytes());
        byte[] digest = sha1.digest();
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }
}

