/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.pbkdf2;

public class BinTools {
    public static final String hex = "0123456789ABCDEF";

    public static String bin2hex(byte[] b) {
        if (b == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(2 * b.length);
        for (int i = 0; i < b.length; ++i) {
            int v = (256 + b[i]) % 256;
            sb.append("0123456789ABCDEF".charAt(v / 16 & 15));
            sb.append("0123456789ABCDEF".charAt(v % 16 & 15));
        }
        return sb.toString();
    }

    public static byte[] hex2bin(String s) {
        String m = s;
        if (s == null) {
            m = "";
        } else if (s.length() % 2 != 0) {
            m = "0" + s;
        }
        byte[] r = new byte[m.length() / 2];
        int i = 0;
        int n = 0;
        while (i < m.length()) {
            char h = m.charAt(i++);
            char l = m.charAt(i++);
            r[n] = (byte)(BinTools.hex2bin(h) * 16 + BinTools.hex2bin(l));
            ++n;
        }
        return r;
    }

    public static int hex2bin(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 65 + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 97 + 10;
        }
        throw new IllegalArgumentException("Input string may only contain hex digits, but found '" + c + "'");
    }

    public static void main(String[] args) {
        String t;
        byte[] c;
        byte[] b = new byte[256];
        int bb = 0;
        for (int i = 0; i < 256; ++i) {
            int n = bb;
            bb = (byte)(bb + 1);
            b[i] = n;
        }
        String s = BinTools.bin2hex(b);
        if (!s.equals(t = BinTools.bin2hex(c = BinTools.hex2bin(s)))) {
            throw new AssertionError((Object)"Mismatch");
        }
    }
}

