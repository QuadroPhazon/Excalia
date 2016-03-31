/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.protocol.FetchResponse;
import com.sun.mail.imap.protocol.IMAPAddress;
import com.sun.mail.imap.protocol.Item;
import java.util.Date;
import java.util.Vector;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MailDateFormat;

public class ENVELOPE
implements Item {
    static final char[] name = new char[]{'E', 'N', 'V', 'E', 'L', 'O', 'P', 'E'};
    public int msgno;
    public Date date = null;
    public String subject;
    public InternetAddress[] from;
    public InternetAddress[] sender;
    public InternetAddress[] replyTo;
    public InternetAddress[] to;
    public InternetAddress[] cc;
    public InternetAddress[] bcc;
    public String inReplyTo;
    public String messageId;
    private static MailDateFormat mailDateFormat = new MailDateFormat();

    public ENVELOPE(FetchResponse r) throws ParsingException {
        this.msgno = r.getNumber();
        r.skipSpaces();
        if (r.readByte() != 40) {
            throw new ParsingException("ENVELOPE parse error");
        }
        String s = r.readString();
        if (s != null) {
            try {
                this.date = mailDateFormat.parse(s);
            }
            catch (Exception pex) {
                // empty catch block
            }
        }
        this.subject = r.readString();
        this.from = this.parseAddressList(r);
        this.sender = this.parseAddressList(r);
        this.replyTo = this.parseAddressList(r);
        this.to = this.parseAddressList(r);
        this.cc = this.parseAddressList(r);
        this.bcc = this.parseAddressList(r);
        this.inReplyTo = r.readString();
        this.messageId = r.readString();
        if (r.readByte() != 41) {
            throw new ParsingException("ENVELOPE parse error");
        }
    }

    private InternetAddress[] parseAddressList(Response r) throws ParsingException {
        r.skipSpaces();
        byte b = r.readByte();
        if (b == 40) {
            Object[] a;
            Vector<IMAPAddress> v = new Vector<IMAPAddress>();
            do {
                if ((a = new Object[](r)).isEndOfGroup()) continue;
                v.addElement((IMAPAddress)a);
            } while (r.peekByte() != 41);
            r.skip(1);
            a = new InternetAddress[v.size()];
            v.copyInto(a);
            return a;
        }
        if (b == 78 || b == 110) {
            r.skip(2);
            return null;
        }
        throw new ParsingException("ADDRESS parse error");
    }
}

