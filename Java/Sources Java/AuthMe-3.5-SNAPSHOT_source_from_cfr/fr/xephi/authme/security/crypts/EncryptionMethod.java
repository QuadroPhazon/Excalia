/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import java.security.NoSuchAlgorithmException;

public interface EncryptionMethod {
    public String getHash(String var1, String var2, String var3) throws NoSuchAlgorithmException;

    public boolean comparePassword(String var1, String var2, String var3) throws NoSuchAlgorithmException;
}

