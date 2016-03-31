/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ROYALAUTH
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        for (int i = 0; i < 25; ++i) {
            password = this.hash(password, salt);
        }
        return password;
    }

    public String hash(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        return hash.equalsIgnoreCase(this.getHash(password, "", ""));
    }
}

