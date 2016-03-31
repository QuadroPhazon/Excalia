/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.util;

import com.sun.mail.util.DecodingException;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.PropUtil;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UUDecoderStream
extends FilterInputStream {
    private String name;
    private int mode;
    private byte[] buffer = new byte[45];
    private int bufsize = 0;
    private int index = 0;
    private boolean gotPrefix = false;
    private boolean gotEnd = false;
    private LineInputStream lin;
    private boolean ignoreErrors;
    private boolean ignoreMissingBeginEnd;
    private String readAhead;

    public UUDecoderStream(InputStream in) {
        super(in);
        this.lin = new LineInputStream(in);
        this.ignoreErrors = PropUtil.getBooleanSystemProperty("mail.mime.uudecode.ignoreerrors", false);
        this.ignoreMissingBeginEnd = PropUtil.getBooleanSystemProperty("mail.mime.uudecode.ignoremissingbeginend", false);
    }

    public UUDecoderStream(InputStream in, boolean ignoreErrors, boolean ignoreMissingBeginEnd) {
        super(in);
        this.lin = new LineInputStream(in);
        this.ignoreErrors = ignoreErrors;
        this.ignoreMissingBeginEnd = ignoreMissingBeginEnd;
    }

    public int read() throws IOException {
        if (this.index >= this.bufsize) {
            this.readPrefix();
            if (!this.decode()) {
                return -1;
            }
            this.index = 0;
        }
        return this.buffer[this.index++] & 255;
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        int i;
        for (i = 0; i < len; ++i) {
            int c = this.read();
            if (c == -1) {
                if (i != 0) break;
                i = -1;
                break;
            }
            buf[off + i] = (byte)c;
        }
        return i;
    }

    public boolean markSupported() {
        return false;
    }

    public int available() throws IOException {
        return this.in.available() * 3 / 4 + (this.bufsize - this.index);
    }

    public String getName() throws IOException {
        this.readPrefix();
        return this.name;
    }

    public int getMode() throws IOException {
        this.readPrefix();
        return this.mode;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void readPrefix() throws IOException {
        if (this.gotPrefix) {
            return;
        }
        this.mode = 438;
        this.name = "encoder.buf";
        do lbl-1000: // 3 sources:
        {
            if ((line = this.lin.readLine()) == null) {
                if (!this.ignoreMissingBeginEnd) {
                    throw new DecodingException("UUDecoder: Missing begin");
                }
                this.gotPrefix = true;
                this.gotEnd = true;
                return;
            }
            if (line.regionMatches(false, 0, "begin", 0, 5)) {
                block10 : {
                    try {
                        this.mode = Integer.parseInt(line.substring(6, 9));
                    }
                    catch (NumberFormatException ex) {
                        if (this.ignoreErrors) break block10;
                        throw new DecodingException("UUDecoder: Error in mode: " + ex.toString());
                    }
                }
                if (line.length() > 10) {
                    this.name = line.substring(10);
                } else if (!this.ignoreErrors) {
                    throw new DecodingException("UUDecoder: Missing name: " + line);
                }
                this.gotPrefix = true;
                return;
            }
            if (!this.ignoreMissingBeginEnd || line.length() == 0) ** GOTO lbl-1000
            count = line.charAt(0);
        } while ((need = ((count = count - 32 & 63) * 8 + 5) / 6) != 0 && line.length() < need + 1);
        this.readAhead = line;
        this.gotPrefix = true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean decode() throws IOException {
        block11 : {
            if (this.gotEnd) {
                return false;
            }
            this.bufsize = 0;
            count = 0;
            do lbl-1000: // 4 sources:
            {
                if (this.readAhead != null) {
                    line = this.readAhead;
                    this.readAhead = null;
                } else {
                    line = this.lin.readLine();
                }
                if (line == null) {
                    if (!this.ignoreMissingBeginEnd) {
                        throw new DecodingException("UUDecoder: Missing end at EOF");
                    }
                    this.gotEnd = true;
                    return false;
                }
                if (line.equals("end")) {
                    this.gotEnd = true;
                    return false;
                }
                if (line.length() == 0) ** GOTO lbl-1000
                count = line.charAt(0);
                if (count >= 32) ** GOTO lbl24
                if (this.ignoreErrors) ** GOTO lbl-1000
                throw new DecodingException("UUDecoder: Buffer format error");
lbl24: // 1 sources:
                if ((count = count - 32 & 63) == 0) {
                    line = this.lin.readLine();
                    if (!(line != null && line.equals("end") || this.ignoreMissingBeginEnd)) {
                        throw new DecodingException("UUDecoder: Missing End after count 0 line");
                    }
                    this.gotEnd = true;
                    return false;
                }
                need = (count * 8 + 5) / 6;
                if (line.length() >= need + 1) break block11;
            } while (this.ignoreErrors);
            throw new DecodingException("UUDecoder: Short buffer error");
        }
        i = 1;
        while (this.bufsize < count) {
            a = (byte)(line.charAt(i++) - 32 & 63);
            b = (byte)(line.charAt(i++) - 32 & 63);
            this.buffer[this.bufsize++] = (byte)(a << 2 & 252 | b >>> 4 & 3);
            if (this.bufsize < count) {
                a = b;
                b = (byte)(line.charAt(i++) - 32 & 63);
                this.buffer[this.bufsize++] = (byte)(a << 4 & 240 | b >>> 2 & 15);
            }
            if (this.bufsize >= count) continue;
            a = b;
            b = (byte)(line.charAt(i++) - 32 & 63);
            this.buffer[this.bufsize++] = (byte)(a << 6 & 192 | b & 63);
        }
        return true;
    }
}

