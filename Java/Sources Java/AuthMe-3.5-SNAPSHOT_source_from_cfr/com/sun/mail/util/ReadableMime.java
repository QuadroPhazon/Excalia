/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.util;

import java.io.InputStream;
import javax.mail.MessagingException;

public interface ReadableMime {
    public InputStream getMimeStream() throws MessagingException;
}

