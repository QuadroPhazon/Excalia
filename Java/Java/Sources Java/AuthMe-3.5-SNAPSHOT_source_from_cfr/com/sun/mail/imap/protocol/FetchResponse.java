/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.BODYSTRUCTURE;
import com.sun.mail.imap.protocol.ENVELOPE;
import com.sun.mail.imap.protocol.FLAGS;
import com.sun.mail.imap.protocol.FetchItem;
import com.sun.mail.imap.protocol.IMAPResponse;
import com.sun.mail.imap.protocol.INTERNALDATE;
import com.sun.mail.imap.protocol.Item;
import com.sun.mail.imap.protocol.RFC822DATA;
import com.sun.mail.imap.protocol.RFC822SIZE;
import com.sun.mail.imap.protocol.UID;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FetchResponse
extends IMAPResponse {
    private Item[] items;
    private Map extensionItems;
    private final FetchItem[] fitems;
    private static final char[] HEADER = new char[]{'.', 'H', 'E', 'A', 'D', 'E', 'R'};
    private static final char[] TEXT = new char[]{'.', 'T', 'E', 'X', 'T'};

    public FetchResponse(Protocol p) throws IOException, ProtocolException {
        super(p);
        this.fitems = null;
        this.parse();
    }

    public FetchResponse(IMAPResponse r) throws IOException, ProtocolException {
        this(r, null);
    }

    public FetchResponse(IMAPResponse r, FetchItem[] fitems) throws IOException, ProtocolException {
        super(r);
        this.fitems = fitems;
        this.parse();
    }

    public int getItemCount() {
        return this.items.length;
    }

    public Item getItem(int index) {
        return this.items[index];
    }

    public Item getItem(Class c) {
        for (int i = 0; i < this.items.length; ++i) {
            if (!c.isInstance(this.items[i])) continue;
            return this.items[i];
        }
        return null;
    }

    public static Item getItem(Response[] r, int msgno, Class c) {
        if (r == null) {
            return null;
        }
        for (int i = 0; i < r.length; ++i) {
            if (r[i] == null || !(r[i] instanceof FetchResponse) || ((FetchResponse)r[i]).getNumber() != msgno) continue;
            FetchResponse f = (FetchResponse)r[i];
            for (int j = 0; j < f.items.length; ++j) {
                if (!c.isInstance(f.items[j])) continue;
                return f.items[j];
            }
        }
        return null;
    }

    public Map getExtensionItems() {
        if (this.extensionItems == null) {
            this.extensionItems = new HashMap();
        }
        return this.extensionItems;
    }

    private void parse() throws ParsingException {
        this.skipSpaces();
        if (this.buffer[this.index] != 40) {
            throw new ParsingException("error in FETCH parsing, missing '(' at index " + this.index);
        }
        Vector<Item> v = new Vector<Item>();
        Item i = null;
        do {
            ++this.index;
            if (this.index >= this.size) {
                throw new ParsingException("error in FETCH parsing, ran off end of buffer, size " + this.size);
            }
            i = this.parseItem();
            if (i != null) {
                v.addElement(i);
                continue;
            }
            if (this.parseExtensionItem()) continue;
            throw new ParsingException("error in FETCH parsing, unrecognized item at index " + this.index);
        } while (this.buffer[this.index] != 41);
        ++this.index;
        this.items = new Item[v.size()];
        v.copyInto(this.items);
    }

    private Item parseItem() throws ParsingException {
        switch (this.buffer[this.index]) {
            case 69: 
            case 101: {
                if (!this.match(ENVELOPE.name)) break;
                return new ENVELOPE(this);
            }
            case 70: 
            case 102: {
                if (!this.match(FLAGS.name)) break;
                return new FLAGS(this);
            }
            case 73: 
            case 105: {
                if (!this.match(INTERNALDATE.name)) break;
                return new INTERNALDATE(this);
            }
            case 66: 
            case 98: {
                if (this.match(BODYSTRUCTURE.name)) {
                    return new BODYSTRUCTURE(this);
                }
                if (!this.match(BODY.name)) break;
                if (this.buffer[this.index] == 91) {
                    return new BODY(this);
                }
                return new BODYSTRUCTURE(this);
            }
            case 82: 
            case 114: {
                if (this.match(RFC822SIZE.name)) {
                    return new RFC822SIZE(this);
                }
                if (!this.match(RFC822DATA.name)) break;
                if (this.match(HEADER) || this.match(TEXT)) {
                    // empty if block
                }
                return new RFC822DATA(this);
            }
            case 85: 
            case 117: {
                if (!this.match(UID.name)) break;
                return new UID(this);
            }
        }
        return null;
    }

    private boolean parseExtensionItem() throws ParsingException {
        if (this.fitems == null) {
            return false;
        }
        for (int i = 0; i < this.fitems.length; ++i) {
            if (!this.match(this.fitems[i].getName())) continue;
            this.getExtensionItems().put(this.fitems[i].getName(), this.fitems[i].parseItem(this));
            return true;
        }
        return false;
    }

    private boolean match(char[] itemName) {
        int len = itemName.length;
        int i = 0;
        int j = this.index;
        while (i < len) {
            if (Character.toUpperCase(this.buffer[j++]) == itemName[i++]) continue;
            return false;
        }
        this.index += len;
        return true;
    }

    private boolean match(String itemName) {
        int len = itemName.length();
        int i = 0;
        int j = this.index;
        while (i < len) {
            if (Character.toUpperCase(this.buffer[j++]) == itemName.charAt(i++)) continue;
            return false;
        }
        this.index += len;
        return true;
    }
}

