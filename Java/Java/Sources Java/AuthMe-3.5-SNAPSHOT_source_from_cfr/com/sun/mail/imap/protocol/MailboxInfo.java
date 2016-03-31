/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.protocol.FLAGS;
import com.sun.mail.imap.protocol.IMAPResponse;
import javax.mail.Flags;

public class MailboxInfo {
    public Flags availableFlags = null;
    public Flags permanentFlags = null;
    public int total = -1;
    public int recent = -1;
    public int first = -1;
    public long uidvalidity = -1;
    public long uidnext = -1;
    public int mode;

    public MailboxInfo(Response[] r) throws ParsingException {
        for (int i = 0; i < r.length; ++i) {
            if (r[i] == null || !(r[i] instanceof IMAPResponse)) continue;
            IMAPResponse ir = (IMAPResponse)r[i];
            if (ir.keyEquals("EXISTS")) {
                this.total = ir.getNumber();
                r[i] = null;
                continue;
            }
            if (ir.keyEquals("RECENT")) {
                this.recent = ir.getNumber();
                r[i] = null;
                continue;
            }
            if (ir.keyEquals("FLAGS")) {
                this.availableFlags = new FLAGS(ir);
                r[i] = null;
                continue;
            }
            if (!ir.isUnTagged() || !ir.isOK()) continue;
            ir.skipSpaces();
            if (ir.readByte() != 91) {
                ir.reset();
                continue;
            }
            boolean handled = true;
            String s = ir.readAtom();
            if (s.equalsIgnoreCase("UNSEEN")) {
                this.first = ir.readNumber();
            } else if (s.equalsIgnoreCase("UIDVALIDITY")) {
                this.uidvalidity = ir.readLong();
            } else if (s.equalsIgnoreCase("PERMANENTFLAGS")) {
                this.permanentFlags = new FLAGS(ir);
            } else if (s.equalsIgnoreCase("UIDNEXT")) {
                this.uidnext = ir.readLong();
            } else {
                handled = false;
            }
            if (handled) {
                r[i] = null;
                continue;
            }
            ir.reset();
        }
        if (this.permanentFlags == null) {
            this.permanentFlags = this.availableFlags != null ? new Flags(this.availableFlags) : new Flags();
        }
    }
}

