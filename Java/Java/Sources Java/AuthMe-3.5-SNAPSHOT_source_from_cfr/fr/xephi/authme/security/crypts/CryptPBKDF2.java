/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.security.pbkdf2.PBKDF2Engine;
import fr.xephi.authme.security.pbkdf2.PBKDF2Parameters;
import java.security.NoSuchAlgorithmException;

public class CryptPBKDF2
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        String result = "pbkdf2_sha256$10000$" + salt + "$";
        PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "ASCII", salt.getBytes(), 10000);
        PBKDF2Engine engine = new PBKDF2Engine(params);
        return result + engine.deriveKey(password, 64).toString();
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        String[] line = hash.split("\\$");
        String salt = line[2];
        String derivedKey = line[3];
        PBKDF2Parameters params = new PBKDF2Parameters("HmacSHA256", "ASCII", salt.getBytes(), 10000, derivedKey.getBytes());
        PBKDF2Engine engine = new PBKDF2Engine(params);
        return engine.verifyKey(password);
    }
}

