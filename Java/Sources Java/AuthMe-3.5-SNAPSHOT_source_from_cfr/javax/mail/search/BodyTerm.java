/*
 * Decompiled with CFR 0_110.
 */
package javax.mail.search;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.search.StringTerm;

public final class BodyTerm
extends StringTerm {
    private static final long serialVersionUID = -4888862527916911385L;

    public BodyTerm(String pattern) {
        super(pattern);
    }

    public boolean match(Message msg) {
        return this.matchPart(msg);
    }

    private boolean matchPart(Part p) {
        try {
            if (p.isMimeType("text/*")) {
                String s = (String)p.getContent();
                if (s == null) {
                    return false;
                }
                return super.match(s);
            }
            if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart)p.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; ++i) {
                    if (!this.matchPart(mp.getBodyPart(i))) continue;
                    return true;
                }
            } else if (p.isMimeType("message/rfc822")) {
                return this.matchPart((Part)p.getContent());
            }
        }
        catch (Exception ex) {
            // empty catch block
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BodyTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}

