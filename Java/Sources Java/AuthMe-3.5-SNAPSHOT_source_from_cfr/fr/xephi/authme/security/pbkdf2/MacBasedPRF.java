/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.pbkdf2;

import fr.xephi.authme.security.pbkdf2.PRF;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MacBasedPRF
implements PRF {
    protected Mac mac;
    protected int hLen;
    protected String macAlgorithm;

    public MacBasedPRF(String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
        try {
            this.mac = Mac.getInstance(macAlgorithm);
            this.hLen = this.mac.getMacLength();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public MacBasedPRF(String macAlgorithm, String provider) {
        this.macAlgorithm = macAlgorithm;
        try {
            this.mac = Mac.getInstance(macAlgorithm, provider);
            this.hLen = this.mac.getMacLength();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] doFinal(byte[] M) {
        byte[] r = this.mac.doFinal(M);
        return r;
    }

    public int getHLen() {
        return this.hLen;
    }

    public void init(byte[] P) {
        try {
            this.mac.init(new SecretKeySpec(P, this.macAlgorithm));
        }
        catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}

