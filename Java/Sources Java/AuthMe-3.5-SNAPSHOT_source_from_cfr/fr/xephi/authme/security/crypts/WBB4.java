/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.BCRYPT;
import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.security.NoSuchAlgorithmException;

public class WBB4
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        return BCRYPT.getDoubleHash(password, salt);
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        return BCRYPT.checkpw(password, hash, 2);
    }
}

