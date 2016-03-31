/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.protocol.MessageSet;
import com.sun.mail.imap.protocol.UIDSet;
import java.util.Vector;
import javax.mail.Message;

public final class Utility {
    private Utility() {
    }

    public static MessageSet[] toMessageSet(Message[] msgs, Condition cond) {
        Vector<MessageSet> v = new Vector<MessageSet>(1);
        for (int i = 0; i < msgs.length; ++i) {
            IMAPMessage msg = (IMAPMessage)msgs[i];
            if (msg.isExpunged()) continue;
            int current = msg.getSequenceNumber();
            if (cond != null && !cond.test(msg)) continue;
            MessageSet set = new MessageSet();
            set.start = current;
            ++i;
            while (i < msgs.length) {
                msg = (IMAPMessage)msgs[i];
                if (!msg.isExpunged()) {
                    int next = msg.getSequenceNumber();
                    if (cond == null || cond.test(msg)) {
                        if (next == current + 1) {
                            current = next;
                        } else {
                            --i;
                            break;
                        }
                    }
                }
                ++i;
            }
            set.end = current;
            v.addElement(set);
        }
        if (v.isEmpty()) {
            return null;
        }
        Object[] sets = new MessageSet[v.size()];
        v.copyInto(sets);
        return sets;
    }

    public static UIDSet[] toUIDSet(Message[] msgs) {
        Vector<UIDSet> v = new Vector<UIDSet>(1);
        for (int i = 0; i < msgs.length; ++i) {
            IMAPMessage msg = (IMAPMessage)msgs[i];
            if (msg.isExpunged()) continue;
            long current = msg.getUID();
            UIDSet set = new UIDSet();
            set.start = current;
            ++i;
            while (i < msgs.length) {
                msg = (IMAPMessage)msgs[i];
                if (!msg.isExpunged()) {
                    long next = msg.getUID();
                    if (next == current + 1) {
                        current = next;
                    } else {
                        --i;
                        break;
                    }
                }
                ++i;
            }
            set.end = current;
            v.addElement(set);
        }
        if (v.isEmpty()) {
            return null;
        }
        Object[] sets = new UIDSet[v.size()];
        v.copyInto(sets);
        return sets;
    }

    public static interface Condition {
        public boolean test(IMAPMessage var1);
    }

}

