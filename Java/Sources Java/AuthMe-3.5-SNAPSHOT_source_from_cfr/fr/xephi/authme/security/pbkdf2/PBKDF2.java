/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.pbkdf2;

import fr.xephi.authme.security.pbkdf2.PBKDF2Parameters;
import fr.xephi.authme.security.pbkdf2.PRF;

public interface PBKDF2 {
    public byte[] deriveKey(String var1);

    public byte[] deriveKey(String var1, int var2);

    public boolean verifyKey(String var1);

    public PBKDF2Parameters getParameters();

    public void setParameters(PBKDF2Parameters var1);

    public PRF getPseudoRandomFunction();

    public void setPseudoRandomFunction(PRF var1);
}

