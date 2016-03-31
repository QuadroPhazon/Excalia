/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.smtp;

import javax.mail.MessagingException;

public interface SaslAuthenticator {
    public boolean authenticate(String[] var1, String var2, String var3, String var4, String var5) throws MessagingException;
}

