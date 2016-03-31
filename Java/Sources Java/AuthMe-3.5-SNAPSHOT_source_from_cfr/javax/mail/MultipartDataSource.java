/*
 * Decompiled with CFR 0_110.
 */
package javax.mail;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;

public interface MultipartDataSource
extends DataSource {
    public int getCount();

    public BodyPart getBodyPart(int var1) throws MessagingException;
}

