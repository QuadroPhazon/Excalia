/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ByteArray;
import com.sun.mail.iap.ParsingException;
import com.sun.mail.imap.protocol.FetchResponse;
import com.sun.mail.imap.protocol.Item;
import java.io.ByteArrayInputStream;

public class BODY
implements Item {
    static final char[] name = new char[]{'B', 'O', 'D', 'Y'};
    public int msgno;
    public ByteArray data;
    public String section;
    public int origin = 0;

    public BODY(FetchResponse r) throws ParsingException {
        byte b;
        this.msgno = r.getNumber();
        r.skipSpaces();
        while ((b = r.readByte()) != 93) {
            if (b != 0) continue;
            throw new ParsingException("BODY parse error: missing ``]'' at section end");
        }
        if (r.readByte() == 60) {
            this.origin = r.readNumber();
            r.skip(1);
        }
        this.data = r.readByteArray();
    }

    public ByteArray getByteArray() {
        return this.data;
    }

    public ByteArrayInputStream getByteArrayInputStream() {
        if (this.data != null) {
            return this.data.toByteArrayInputStream();
        }
        return null;
    }
}

