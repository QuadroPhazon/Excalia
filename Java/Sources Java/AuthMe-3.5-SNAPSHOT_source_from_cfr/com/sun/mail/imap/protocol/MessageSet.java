/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import java.util.Vector;

public class MessageSet {
    public int start;
    public int end;

    public MessageSet() {
    }

    public MessageSet(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int size() {
        return this.end - this.start + 1;
    }

    public static MessageSet[] createMessageSets(int[] msgs) {
        Vector<MessageSet> v = new Vector<MessageSet>();
        for (int i = 0; i < msgs.length; ++i) {
            int j;
            MessageSet ms = new MessageSet();
            ms.start = msgs[i];
            for (j = i + 1; j < msgs.length && msgs[j] == msgs[j - 1] + 1; ++j) {
            }
            ms.end = msgs[j - 1];
            v.addElement(ms);
            i = j - 1;
        }
        Object[] msgsets = new MessageSet[v.size()];
        v.copyInto(msgsets);
        return msgsets;
    }

    public static String toString(MessageSet[] msgsets) {
        if (msgsets == null || msgsets.length == 0) {
            return null;
        }
        int i = 0;
        StringBuffer s = new StringBuffer();
        int size = msgsets.length;
        do {
            int start;
            int end;
            if ((end = msgsets[i].end) > (start = msgsets[i].start)) {
                s.append(start).append(':').append(end);
            } else {
                s.append(start);
            }
            if (++i >= size) break;
            s.append(',');
        } while (true);
        return s.toString();
    }

    public static int size(MessageSet[] msgsets) {
        int count = 0;
        if (msgsets == null) {
            return 0;
        }
        for (int i = 0; i < msgsets.length; ++i) {
            count += msgsets[i].size();
        }
        return count;
    }
}

