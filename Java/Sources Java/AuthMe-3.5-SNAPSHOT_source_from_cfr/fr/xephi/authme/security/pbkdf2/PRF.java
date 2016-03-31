/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.pbkdf2;

public interface PRF {
    public void init(byte[] var1);

    public byte[] doFinal(byte[] var1);

    public int getHLen();
}

