/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import java.util.Vector;

public class UIDSet {
    public long start;
    public long end;

    public UIDSet() {
    }

    public UIDSet(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long size() {
        return this.end - this.start + 1;
    }

    public static UIDSet[] createUIDSets(long[] msgs) {
        Vector<UIDSet> v = new Vector<UIDSet>();
        for (int i = 0; i < msgs.length; ++i) {
            int j;
            UIDSet ms = new UIDSet();
            ms.start = msgs[i];
            for (j = i + 1; j < msgs.length && msgs[j] == msgs[j - 1] + 1; ++j) {
            }
            ms.end = msgs[j - 1];
            v.addElement(ms);
            i = j - 1;
        }
        Object[] msgsets = new UIDSet[v.size()];
        v.copyInto(msgsets);
        return msgsets;
    }

    public static String toString(UIDSet[] msgsets) {
        if (msgsets == null || msgsets.length == 0) {
            return null;
        }
        int i = 0;
        StringBuffer s = new StringBuffer();
        int size = msgsets.length;
        do {
            long end;
            long start;
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

    public static long size(UIDSet[] msgsets) {
        long count = 0;
        if (msgsets == null) {
            return 0;
        }
        for (int i = 0; i < msgsets.length; ++i) {
            count += msgsets[i].size();
        }
        return count;
    }
}

