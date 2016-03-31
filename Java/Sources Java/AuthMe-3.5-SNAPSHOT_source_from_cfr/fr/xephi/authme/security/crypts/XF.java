/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XF
implements EncryptionMethod {
    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        return this.getSHA256(this.getSHA256(password) + this.regmatch("\"salt\";.:..:\"(.*)\";.:.:\"hashFunc\"", salt));
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        String salt = AuthMe.getInstance().database.getAuth(playerName).getSalt();
        return hash.equals(this.regmatch("\"hash\";.:..:\"(.*)\";.:.:\"salt\"", salt));
    }

    public String getSHA256(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte element : byteData) {
            sb.append(Integer.toString((element & 255) + 256, 16).substring(1));
        }
        StringBuffer hexString = new StringBuffer();
        for (byte element2 : byteData) {
            String hex = Integer.toHexString(255 & element2);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String regmatch(String pattern, String line) {
        ArrayList<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(pattern).matcher(line);
        while (m.find()) {
            allMatches.add(m.group(1));
        }
        return (String)allMatches.get(0);
    }
}

