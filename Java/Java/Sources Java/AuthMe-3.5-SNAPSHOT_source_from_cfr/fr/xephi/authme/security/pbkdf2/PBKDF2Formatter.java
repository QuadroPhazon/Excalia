/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.pbkdf2;

import fr.xephi.authme.security.pbkdf2.PBKDF2Parameters;

public interface PBKDF2Formatter {
    public String toString(PBKDF2Parameters var1);

    public boolean fromString(PBKDF2Parameters var1, String var2);
}

