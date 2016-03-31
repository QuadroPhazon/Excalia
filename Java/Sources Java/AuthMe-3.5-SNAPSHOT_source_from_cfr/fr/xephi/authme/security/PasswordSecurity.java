/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Event
 */
package fr.xephi.authme.security;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.PasswordEncryptionEvent;
import fr.xephi.authme.security.HashAlgorithm;
import fr.xephi.authme.security.crypts.BCRYPT;
import fr.xephi.authme.security.crypts.EncryptionMethod;
import fr.xephi.authme.settings.Settings;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class PasswordSecurity {
    private static SecureRandom rnd = new SecureRandom();
    public static HashMap<String, String> userSalt = new HashMap();

    public static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] msg = new byte[40];
        rnd.nextBytes(msg);
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        byte[] digest = sha1.digest(msg);
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).substring(0, length);
    }

    public static String getHash(HashAlgorithm alg, String password, String playerName) throws NoSuchAlgorithmException {
        EncryptionMethod method;
        try {
            method = alg != HashAlgorithm.CUSTOM ? (EncryptionMethod)alg.getclass().newInstance() : null;
        }
        catch (InstantiationException e) {
            throw new NoSuchAlgorithmException("Problem with this hash algorithm");
        }
        catch (IllegalAccessException e) {
            throw new NoSuchAlgorithmException("Problem with this hash algorithm");
        }
        String salt = "";
        switch (alg) {
            case SHA256: {
                salt = PasswordSecurity.createSalt(16);
                break;
            }
            case MD5VB: {
                salt = PasswordSecurity.createSalt(16);
                break;
            }
            case XAUTH: {
                salt = PasswordSecurity.createSalt(12);
                break;
            }
            case MYBB: {
                salt = PasswordSecurity.createSalt(8);
                userSalt.put(playerName, salt);
                break;
            }
            case IPB3: {
                salt = PasswordSecurity.createSalt(5);
                userSalt.put(playerName, salt);
                break;
            }
            case PHPFUSION: {
                salt = PasswordSecurity.createSalt(12);
                userSalt.put(playerName, salt);
                break;
            }
            case SALTED2MD5: {
                salt = PasswordSecurity.createSalt(Settings.saltLength);
                userSalt.put(playerName, salt);
                break;
            }
            case JOOMLA: {
                salt = PasswordSecurity.createSalt(32);
                userSalt.put(playerName, salt);
                break;
            }
            case BCRYPT: {
                salt = BCRYPT.gensalt(Settings.bCryptLog2Rounds);
                userSalt.put(playerName, salt);
                break;
            }
            case WBB3: {
                salt = PasswordSecurity.createSalt(40);
                userSalt.put(playerName, salt);
                break;
            }
            case WBB4: {
                salt = BCRYPT.gensalt(8);
                userSalt.put(playerName, salt);
                break;
            }
            case PBKDF2: {
                salt = PasswordSecurity.createSalt(12);
                userSalt.put(playerName, salt);
                break;
            }
            case SMF: {
                return method.getHash(password, null, playerName);
            }
            case PHPBB: {
                salt = PasswordSecurity.createSalt(16);
                userSalt.put(playerName, salt);
                break;
            }
            case MD5: 
            case SHA1: 
            case WHIRLPOOL: 
            case PLAINTEXT: 
            case XENFORO: 
            case SHA512: 
            case ROYALAUTH: 
            case CRAZYCRYPT1: 
            case DOUBLEMD5: 
            case WORDPRESS: 
            case CUSTOM: {
                break;
            }
            default: {
                throw new NoSuchAlgorithmException("Unknown hash algorithm");
            }
        }
        PasswordEncryptionEvent event = new PasswordEncryptionEvent(method, playerName);
        Bukkit.getPluginManager().callEvent((Event)event);
        method = event.getMethod();
        if (method == null) {
            throw new NoSuchAlgorithmException("Unknown hash algorithm");
        }
        return method.getHash(password, salt, playerName);
    }

    public static boolean comparePasswordWithHash(String password, String hash, String playerName) throws NoSuchAlgorithmException {
        EncryptionMethod method;
        HashAlgorithm algo = Settings.getPasswordHash;
        try {
            method = algo != HashAlgorithm.CUSTOM ? (EncryptionMethod)algo.getclass().newInstance() : null;
        }
        catch (InstantiationException e) {
            throw new NoSuchAlgorithmException("Problem with this hash algorithm");
        }
        catch (IllegalAccessException e) {
            throw new NoSuchAlgorithmException("Problem with this hash algorithm");
        }
        PasswordEncryptionEvent event = new PasswordEncryptionEvent(method, playerName);
        Bukkit.getPluginManager().callEvent((Event)event);
        method = event.getMethod();
        if (method == null) {
            throw new NoSuchAlgorithmException("Unknown hash algorithm");
        }
        try {
            if (method.comparePassword(hash, password, playerName)) {
                return true;
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        if (Settings.supportOldPassword.booleanValue()) {
            try {
                if (PasswordSecurity.compareWithAllEncryptionMethod(password, hash, playerName)) {
                    return true;
                }
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        return false;
    }

    private static boolean compareWithAllEncryptionMethod(String password, String hash, String playerName) throws NoSuchAlgorithmException {
        for (HashAlgorithm algo : HashAlgorithm.values()) {
            if (algo == HashAlgorithm.CUSTOM) continue;
            try {
                EncryptionMethod method = (EncryptionMethod)algo.getclass().newInstance();
                if (!method.comparePassword(hash, password, playerName)) continue;
                PlayerAuth nAuth = AuthMe.getInstance().database.getAuth(playerName);
                if (nAuth != null) {
                    nAuth.setHash(PasswordSecurity.getHash(Settings.getPasswordHash, password, playerName));
                    nAuth.setSalt(userSalt.get(playerName));
                    AuthMe.getInstance().database.updatePassword(nAuth);
                    AuthMe.getInstance().database.updateSalt(nAuth);
                }
                return true;
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        return false;
    }

}

