/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.security.crypts.WHIRLPOOL;
import java.security.NoSuchAlgorithmException;

public class XAUTH
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        String hash = XAUTH.getWhirlpool(salt + password).toLowerCase();
        int saltPos = password.length() >= hash.length() ? hash.length() - 1 : password.length();
        return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        int saltPos = password.length() >= hash.length() ? hash.length() - 1 : password.length();
        String salt = hash.substring(saltPos, saltPos + 12);
        return hash.equals(this.getHash(password, salt, ""));
    }

    public static String getWhirlpool(String message) {
        WHIRLPOOL w = new WHIRLPOOL();
        byte[] digest = new byte[64];
        w.NESSIEinit();
        w.NESSIEadd(message);
        w.NESSIEfinalize(digest);
        return WHIRLPOOL.display(digest);
    }
}

