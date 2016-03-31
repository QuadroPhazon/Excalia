/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CRAZYCRYPT1
implements EncryptionMethod {
    protected final Charset charset = Charset.forName("UTF-8");
    private static final char[] CRYPTCHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        String text = "\u00dc\u00c4aeut//&/=I " + password + "7421\u20ac547" + name + "__+I\u00c4IH\u00a7%NK " + password;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(text.getBytes(this.charset), 0, text.length());
            return CRAZYCRYPT1.byteArrayToHexString(md.digest());
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        return hash.equals(this.getHash(password, null, playerName));
    }

    public static /* varargs */ String byteArrayToHexString(byte ... args) {
        char[] chars = new char[args.length * 2];
        for (int i = 0; i < args.length; ++i) {
            chars[i * 2] = CRYPTCHARS[args[i] >> 4 & 15];
            chars[i * 2 + 1] = CRYPTCHARS[args[i] & 15];
        }
        return new String(chars);
    }
}

