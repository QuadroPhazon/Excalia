/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PHPBB
implements EncryptionMethod {
    private static final int PHP_VERSION = 4;
    private String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String phpbb_hash(String password, String salt) {
        String hash;
        String random_state = salt;
        String random = "";
        int count = 6;
        if (random.length() < count) {
            random = "";
            for (int i = 0; i < count; i += 16) {
                random_state = PHPBB.md5(salt + random_state);
                random = random + PHPBB.pack(PHPBB.md5(random_state));
            }
            random = random.substring(0, count);
        }
        if ((hash = this._hash_crypt_private(password, this._hash_gensalt_private(random, this.itoa64))).length() == 34) {
            return hash;
        }
        return PHPBB.md5(password);
    }

    private String _hash_gensalt_private(String input, String itoa64) {
        return this._hash_gensalt_private(input, itoa64, 6);
    }

    private String _hash_gensalt_private(String input, String itoa64, int iteration_count_log2) {
        if (iteration_count_log2 < 4 || iteration_count_log2 > 31) {
            iteration_count_log2 = 8;
        }
        String output = "$H$";
        output = output + itoa64.charAt(Math.min(iteration_count_log2 + 3, 30));
        output = output + this._hash_encode64(input, 6);
        return output;
    }

    private String _hash_encode64(String input, int count) {
        String output = "";
        int i = 0;
        do {
            int value = input.charAt(i++);
            output = output + this.itoa64.charAt(value & 63);
            if (i < count) {
                value |= input.charAt(i) << 8;
            }
            output = output + this.itoa64.charAt(value >> 6 & 63);
            if (i++ >= count) break;
            if (i < count) {
                value |= input.charAt(i) << 16;
            }
            output = output + this.itoa64.charAt(value >> 12 & 63);
            if (i++ >= count) break;
            output = output + this.itoa64.charAt(value >> 18 & 63);
        } while (i < count);
        return output;
    }

    String _hash_crypt_private(String password, String setting) {
        String output = "*";
        if (!setting.substring(0, 3).equals("$H$")) {
            return output;
        }
        int count_log2 = this.itoa64.indexOf(setting.charAt(3));
        if (count_log2 < 7 || count_log2 > 30) {
            return output;
        }
        int count = 1 << count_log2;
        String salt = setting.substring(4, 12);
        if (salt.length() != 8) {
            return output;
        }
        String m1 = PHPBB.md5(salt + password);
        String hash = PHPBB.pack(m1);
        do {
            hash = PHPBB.pack(PHPBB.md5(hash + password));
        } while (--count > 0);
        output = setting.substring(0, 12);
        output = output + this._hash_encode64(hash, 16);
        return output;
    }

    public boolean phpbb_check_hash(String password, String hash) {
        if (hash.length() == 34) {
            return this._hash_crypt_private(password, hash).equals(hash);
        }
        return PHPBB.md5(password).equals(hash);
    }

    public static String md5(String data) {
        try {
            byte[] bytes = data.getBytes("ISO-8859-1");
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            byte[] hash = md5er.digest(bytes);
            return PHPBB.bytes2hex(hash);
        }
        catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static int hexToInt(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        }
        if ((ch = Character.toUpperCase(ch)) >= 'A' && ch <= 'F') {
            return ch - 65 + 10;
        }
        throw new IllegalArgumentException("Not a hex character: " + ch);
    }

    private static String bytes2hex(byte[] bytes) {
        StringBuffer r = new StringBuffer(32);
        for (int i = 0; i < bytes.length; ++i) {
            String x = Integer.toHexString(bytes[i] & 255);
            if (x.length() < 2) {
                r.append("0");
            }
            r.append(x);
        }
        return r.toString();
    }

    static String pack(String hex) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < hex.length(); i += 2) {
            char c1 = hex.charAt(i);
            char c2 = hex.charAt(i + 1);
            char packed = (char)(PHPBB.hexToInt(c1) * 16 + PHPBB.hexToInt(c2));
            buf.append(packed);
        }
        return buf.toString();
    }

    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        return this.phpbb_hash(password, salt);
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        return this.phpbb_check_hash(password, hash);
    }
}

