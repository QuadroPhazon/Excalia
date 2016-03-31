/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap;

public class AppendUID {
    public long uidvalidity = -1;
    public long uid = -1;

    public AppendUID(long uidvalidity, long uid) {
        this.uidvalidity = uidvalidity;
        this.uid = uid;
    }
}

