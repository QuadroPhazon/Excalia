/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.iap;

import com.sun.mail.iap.AString;
import com.sun.mail.iap.Atom;
import com.sun.mail.iap.Literal;
import com.sun.mail.iap.LiteralException;
import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.util.ASCIIUtility;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class Argument {
    protected Vector items = new Vector(1);

    public void append(Argument arg) {
        this.items.ensureCapacity(this.items.size() + arg.items.size());
        for (int i = 0; i < arg.items.size(); ++i) {
            this.items.addElement(arg.items.elementAt(i));
        }
    }

    public void writeString(String s) {
        this.items.addElement(new AString(ASCIIUtility.getBytes(s)));
    }

    public void writeString(String s, String charset) throws UnsupportedEncodingException {
        if (charset == null) {
            this.writeString(s);
        } else {
            this.items.addElement(new AString(s.getBytes(charset)));
        }
    }

    public void writeBytes(byte[] b) {
        this.items.addElement(b);
    }

    public void writeBytes(ByteArrayOutputStream b) {
        this.items.addElement(b);
    }

    public void writeBytes(Literal b) {
        this.items.addElement(b);
    }

    public void writeAtom(String s) {
        this.items.addElement(new Atom(s));
    }

    public void writeNumber(int i) {
        this.items.addElement(new Integer(i));
    }

    public void writeNumber(long i) {
        this.items.addElement(new Long(i));
    }

    public void writeArgument(Argument c) {
        this.items.addElement(c);
    }

    public void write(Protocol protocol) throws IOException, ProtocolException {
        int size = this.items != null ? this.items.size() : 0;
        DataOutputStream os = (DataOutputStream)protocol.getOutputStream();
        for (int i = 0; i < size; ++i) {
            Object o;
            if (i > 0) {
                os.write(32);
            }
            if ((o = this.items.elementAt(i)) instanceof Atom) {
                os.writeBytes(((Atom)o).string);
                continue;
            }
            if (o instanceof Number) {
                os.writeBytes(((Number)o).toString());
                continue;
            }
            if (o instanceof AString) {
                this.astring(((AString)o).bytes, protocol);
                continue;
            }
            if (o instanceof byte[]) {
                this.literal((byte[])o, protocol);
                continue;
            }
            if (o instanceof ByteArrayOutputStream) {
                this.literal((ByteArrayOutputStream)o, protocol);
                continue;
            }
            if (o instanceof Literal) {
                this.literal((Literal)o, protocol);
                continue;
            }
            if (!(o instanceof Argument)) continue;
            os.write(40);
            ((Argument)o).write(protocol);
            os.write(41);
        }
    }

    private void astring(byte[] bytes, Protocol protocol) throws IOException, ProtocolException {
        int i;
        byte b;
        DataOutputStream os = (DataOutputStream)protocol.getOutputStream();
        int len = bytes.length;
        if (len > 1024) {
            this.literal(bytes, protocol);
            return;
        }
        boolean quote = len == 0;
        boolean escape = false;
        for (i = 0; i < len; ++i) {
            b = bytes[i];
            if (b == 0 || b == 13 || b == 10 || (b & 255) > 127) {
                this.literal(bytes, protocol);
                return;
            }
            if (b != 42 && b != 37 && b != 40 && b != 41 && b != 123 && b != 34 && b != 92 && (b & 255) > 32) continue;
            quote = true;
            if (b != 34 && b != 92) continue;
            escape = true;
        }
        if (quote) {
            os.write(34);
        }
        if (escape) {
            for (i = 0; i < len; ++i) {
                b = bytes[i];
                if (b == 34 || b == 92) {
                    os.write(92);
                }
                os.write(b);
            }
        } else {
            os.write(bytes);
        }
        if (quote) {
            os.write(34);
        }
    }

    private void literal(byte[] b, Protocol protocol) throws IOException, ProtocolException {
        this.startLiteral(protocol, b.length).write(b);
    }

    private void literal(ByteArrayOutputStream b, Protocol protocol) throws IOException, ProtocolException {
        b.writeTo(this.startLiteral(protocol, b.size()));
    }

    private void literal(Literal b, Protocol protocol) throws IOException, ProtocolException {
        b.writeTo(this.startLiteral(protocol, b.size()));
    }

    private OutputStream startLiteral(Protocol protocol, int size) throws IOException, ProtocolException {
        DataOutputStream os = (DataOutputStream)protocol.getOutputStream();
        boolean nonSync = protocol.supportsNonSyncLiterals();
        os.write(123);
        os.writeBytes(Integer.toString(size));
        if (nonSync) {
            os.writeBytes("+}\r\n");
        } else {
            os.writeBytes("}\r\n");
        }
        os.flush();
        if (!nonSync) {
            Response r;
            while (!(r = protocol.readResponse()).isContinuation()) {
                if (!r.isTagged()) continue;
                throw new LiteralException(r);
            }
        }
        return os;
    }
}

