/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security;

import java.util.Random;

public class RandomString {
    private static final char[] chars;
    private final Random random = new Random();
    private final char[] buf;

    public RandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }
        this.buf = new char[length];
    }

    public String nextString() {
        for (int idx = 0; idx < this.buf.length; ++idx) {
            this.buf[idx] = chars[this.random.nextInt(chars.length)];
        }
        return new String(this.buf);
    }

    static {
        int idx;
        chars = new char[36];
        for (idx = 0; idx < 10; ++idx) {
            RandomString.chars[idx] = (char)(48 + idx);
        }
        for (idx = 10; idx < 36; ++idx) {
            RandomString.chars[idx] = (char)(97 + idx - 10);
        }
    }
}

