/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.pbkdf2;

import fr.xephi.authme.security.pbkdf2.MacBasedPRF;
import fr.xephi.authme.security.pbkdf2.PBKDF2;
import fr.xephi.authme.security.pbkdf2.PBKDF2HexFormatter;
import fr.xephi.authme.security.pbkdf2.PBKDF2Parameters;
import fr.xephi.authme.security.pbkdf2.PRF;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PBKDF2Engine
implements PBKDF2 {
    protected PBKDF2Parameters parameters;
    protected PRF prf;

    public PBKDF2Engine() {
        this.parameters = null;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters parameters) {
        this.parameters = parameters;
        this.prf = null;
    }

    public PBKDF2Engine(PBKDF2Parameters parameters, PRF prf) {
        this.parameters = parameters;
        this.prf = prf;
    }

    public byte[] deriveKey(String inputPassword) {
        return this.deriveKey(inputPassword, 0);
    }

    public byte[] deriveKey(String inputPassword, int dkLen) {
        Object r = null;
        Object P = null;
        String charset = this.parameters.getHashCharset();
        if (inputPassword == null) {
            inputPassword = "";
        }
        try {
            P = charset == null ? (Object)inputPassword.getBytes() : (Object)inputPassword.getBytes(charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.assertPRF((byte[])P);
        if (dkLen == 0) {
            dkLen = this.prf.getHLen();
        }
        r = this.PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), dkLen);
        return r;
    }

    public boolean verifyKey(String inputPassword) {
        byte[] referenceKey = this.getParameters().getDerivedKey();
        if (referenceKey == null || referenceKey.length == 0) {
            return false;
        }
        byte[] inputKey = this.deriveKey(inputPassword, referenceKey.length);
        if (inputKey == null || inputKey.length != referenceKey.length) {
            return false;
        }
        for (int i = 0; i < inputKey.length; ++i) {
            if (inputKey[i] == referenceKey[i]) continue;
            return false;
        }
        return true;
    }

    protected void assertPRF(byte[] P) {
        if (this.prf == null) {
            this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
        }
        this.prf.init(P);
    }

    public PRF getPseudoRandomFunction() {
        return this.prf;
    }

    protected byte[] PBKDF2(PRF prf, byte[] S, int c, int dkLen) {
        if (S == null) {
            S = new byte[]{};
        }
        int hLen = prf.getHLen();
        int l = this.ceil(dkLen, hLen);
        int r = dkLen - (l - 1) * hLen;
        byte[] T = new byte[l * hLen];
        int ti_offset = 0;
        for (int i = 1; i <= l; ++i) {
            this._F(T, ti_offset, prf, S, c, i);
            ti_offset += hLen;
        }
        if (r < hLen) {
            byte[] DK = new byte[dkLen];
            System.arraycopy(T, 0, DK, 0, dkLen);
            return DK;
        }
        return T;
    }

    protected int ceil(int a, int b) {
        int m = 0;
        if (a % b > 0) {
            m = 1;
        }
        return a / b + m;
    }

    protected void _F(byte[] dest, int offset, PRF prf, byte[] S, int c, int blockIndex) {
        int hLen = prf.getHLen();
        byte[] U_r = new byte[hLen];
        byte[] U_i = new byte[S.length + 4];
        System.arraycopy(S, 0, U_i, 0, S.length);
        this.INT(U_i, S.length, blockIndex);
        for (int i = 0; i < c; ++i) {
            U_i = prf.doFinal(U_i);
            this.xor(U_r, U_i);
        }
        System.arraycopy(U_r, 0, dest, offset, hLen);
    }

    protected void xor(byte[] dest, byte[] src) {
        for (int i = 0; i < dest.length; ++i) {
            byte[] arrby = dest;
            int n = i;
            arrby[n] = (byte)(arrby[n] ^ src[i]);
        }
    }

    protected void INT(byte[] dest, int offset, int i) {
        dest[offset + 0] = (byte)(i / 16777216);
        dest[offset + 1] = (byte)(i / 65536);
        dest[offset + 2] = (byte)(i / 256);
        dest[offset + 3] = (byte)i;
    }

    public PBKDF2Parameters getParameters() {
        return this.parameters;
    }

    public void setParameters(PBKDF2Parameters parameters) {
        this.parameters = parameters;
    }

    public void setPseudoRandomFunction(PRF prf) {
        this.prf = prf;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String password = "password";
        String candidate = null;
        PBKDF2HexFormatter formatter = new PBKDF2HexFormatter();
        if (args.length >= 1) {
            password = args[0];
        }
        if (args.length >= 2) {
            candidate = args[1];
        }
        if (candidate == null) {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[8];
            sr.nextBytes(salt);
            int iterations = 1000;
            PBKDF2Parameters p = new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", salt, iterations);
            PBKDF2Engine e = new PBKDF2Engine(p);
            p.setDerivedKey(e.deriveKey(password));
            candidate = formatter.toString(p);
            System.out.println(candidate);
        } else {
            PBKDF2Parameters p = new PBKDF2Parameters();
            p.setHashAlgorithm("HmacSHA1");
            p.setHashCharset("ISO-8859-1");
            if (formatter.fromString(p, candidate)) {
                throw new IllegalArgumentException("Candidate data does not have correct format (\"" + candidate + "\")");
            }
            PBKDF2Engine e = new PBKDF2Engine(p);
            boolean verifyOK = e.verifyKey(password);
            System.out.println(verifyOK ? "OK" : "FAIL");
            System.exit(verifyOK ? 0 : 1);
        }
    }
}

