/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.pop3;

import com.sun.mail.pop3.POP3Store;
import javax.mail.Session;
import javax.mail.URLName;

public class POP3SSLStore
extends POP3Store {
    public POP3SSLStore(Session session, URLName url) {
        super(session, url, "pop3s", true);
    }
}

