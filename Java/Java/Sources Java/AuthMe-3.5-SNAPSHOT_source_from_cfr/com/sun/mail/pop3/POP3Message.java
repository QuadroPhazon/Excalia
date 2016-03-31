/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.pop3;

import com.sun.mail.pop3.AppendStream;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;
import com.sun.mail.pop3.Protocol;
import com.sun.mail.pop3.TempFile;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.ReadableMime;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.Enumeration;
import java.util.logging.Level;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.SharedInputStream;

public class POP3Message
extends MimeMessage
implements ReadableMime {
    static final String UNKNOWN = "UNKNOWN";
    private POP3Folder folder;
    private int hdrSize = -1;
    private int msgSize = -1;
    String uid = "UNKNOWN";
    private SoftReference rawData = new SoftReference<Object>(null);

    public POP3Message(Folder folder, int msgno) throws MessagingException {
        super(folder, msgno);
        this.folder = (POP3Folder)folder;
    }

    public synchronized void setFlags(Flags newFlags, boolean set) throws MessagingException {
        Flags oldFlags = (Flags)this.flags.clone();
        super.setFlags(newFlags, set);
        if (!this.flags.equals(oldFlags)) {
            this.folder.notifyMessageChangedListeners(1, this);
        }
    }

    public int getSize() throws MessagingException {
        try {
            POP3Message pOP3Message = this;
            synchronized (pOP3Message) {
                if (this.msgSize > 0) {
                    return this.msgSize;
                }
            }
            if (this.headers == null) {
                this.loadHeaders();
            }
            pOP3Message = this;
            synchronized (pOP3Message) {
                if (this.msgSize < 0) {
                    this.msgSize = this.folder.getProtocol().list(this.msgnum) - this.hdrSize;
                }
                return this.msgSize;
            }
        }
        catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        }
        catch (IOException ex) {
            throw new MessagingException("error getting size", ex);
        }
    }

    private InputStream getRawStream(boolean skipHeader) throws MessagingException {
        InputStream rawcontent;
        rawcontent = null;
        try {
            POP3Message pOP3Message = this;
            synchronized (pOP3Message) {
                rawcontent = (InputStream)this.rawData.get();
                if (rawcontent == null) {
                    TempFile cache = this.folder.getFileCache();
                    if (cache != null) {
                        Session s = ((POP3Store)this.folder.getStore()).getSession();
                        if (this.folder.logger.isLoggable(Level.FINE)) {
                            this.folder.logger.fine("caching message #" + this.msgnum + " in temp file");
                        }
                        AppendStream os = cache.getAppendStream();
                        BufferedOutputStream bos = new BufferedOutputStream(os);
                        try {
                            this.folder.getProtocol().retr(this.msgnum, bos);
                        }
                        finally {
                            bos.close();
                        }
                        rawcontent = os.getInputStream();
                    } else {
                        rawcontent = this.folder.getProtocol().retr(this.msgnum, this.msgSize > 0 ? this.msgSize + this.hdrSize : 0);
                    }
                    if (rawcontent == null) {
                        this.expunged = true;
                        throw new MessageRemovedException("can't retrieve message #" + this.msgnum + " in POP3Message.getContentStream");
                    }
                    if (this.headers == null || ((POP3Store)this.folder.getStore()).forgetTopHeaders) {
                        this.headers = new InternetHeaders(rawcontent);
                        this.hdrSize = (int)((SharedInputStream)((Object)rawcontent)).getPosition();
                    } else {
                        int len;
                        boolean offset = false;
                        block9 : do {
                            int c1;
                            len = 0;
                            while ((c1 = rawcontent.read()) >= 0 && c1 != 10) {
                                if (c1 == 13) {
                                    if (rawcontent.available() <= 0) continue block9;
                                    rawcontent.mark(1);
                                    if (rawcontent.read() == 10) continue block9;
                                    rawcontent.reset();
                                    continue block9;
                                }
                                ++len;
                            }
                        } while (rawcontent.available() != 0 && len != 0);
                        this.hdrSize = (int)((SharedInputStream)((Object)rawcontent)).getPosition();
                    }
                    this.msgSize = rawcontent.available();
                    this.rawData = new SoftReference<InputStream>(rawcontent);
                }
            }
        }
        catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        }
        catch (IOException ex) {
            throw new MessagingException("error fetching POP3 content", ex);
        }
        rawcontent = ((SharedInputStream)((Object)rawcontent)).newStream(skipHeader ? (long)this.hdrSize : 0, -1);
        return rawcontent;
    }

    protected synchronized InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream)((Object)this.contentStream)).newStream(0, -1);
        }
        InputStream cstream = this.getRawStream(true);
        TempFile cache = this.folder.getFileCache();
        if (cache != null || ((POP3Store)this.folder.getStore()).keepMessageContent) {
            this.contentStream = ((SharedInputStream)((Object)cstream)).newStream(0, -1);
        }
        return cstream;
    }

    public InputStream getMimeStream() throws MessagingException {
        return this.getRawStream(false);
    }

    public synchronized void invalidate(boolean invalidateHeaders) {
        this.content = null;
        InputStream rstream = (InputStream)this.rawData.get();
        if (rstream != null) {
            try {
                rstream.close();
            }
            catch (IOException ex) {
                // empty catch block
            }
            this.rawData = new SoftReference<Object>(null);
        }
        if (this.contentStream != null) {
            try {
                this.contentStream.close();
            }
            catch (IOException ex) {
                // empty catch block
            }
            this.contentStream = null;
        }
        this.msgSize = -1;
        if (invalidateHeaders) {
            this.headers = null;
            this.hdrSize = -1;
        }
    }

    public InputStream top(int n) throws MessagingException {
        try {
            POP3Message pOP3Message = this;
            synchronized (pOP3Message) {
                return this.folder.getProtocol().top(this.msgnum, n);
            }
        }
        catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        }
        catch (IOException ex) {
            throw new MessagingException("error getting size", ex);
        }
    }

    public String[] getHeader(String name) throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public void addHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public void removeHeader(String name) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public Enumeration getAllHeaders() throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        if (this.headers == null) {
            this.loadHeaders();
        }
        return this.headers.getNonMatchingHeaderLines(names);
    }

    public void saveChanges() throws MessagingException {
        throw new IllegalWriteException("POP3 messages are read-only");
    }

    public synchronized void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        InputStream rawcontent = (InputStream)this.rawData.get();
        if (rawcontent == null && ignoreList == null && !((POP3Store)this.folder.getStore()).cacheWriteTo) {
            Session s = ((POP3Store)this.folder.getStore()).getSession();
            if (this.folder.logger.isLoggable(Level.FINE)) {
                this.folder.logger.fine("streaming msg " + this.msgnum);
            }
            if (!this.folder.getProtocol().retr(this.msgnum, os)) {
                this.expunged = true;
                throw new MessageRemovedException("can't retrieve message #" + this.msgnum + " in POP3Message.writeTo");
            }
        } else {
            if (rawcontent != null && ignoreList == null) {
                InputStream in = ((SharedInputStream)((Object)rawcontent)).newStream(0, -1);
                try {
                    int len;
                    byte[] buf = new byte[16384];
                    while ((len = in.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }
                }
                finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    }
                    catch (IOException ex) {}
                }
            }
            super.writeTo(os, ignoreList);
        }
    }

    private void loadHeaders() throws MessagingException {
        assert (!Thread.holdsLock(this));
        try {
            boolean fetchContent = false;
            POP3Message pOP3Message = this;
            synchronized (pOP3Message) {
                if (this.headers != null) {
                    return;
                }
                InputStream hdrs = null;
                if (((POP3Store)this.folder.getStore()).disableTop || (hdrs = this.folder.getProtocol().top(this.msgnum, 0)) == null) {
                    fetchContent = true;
                } else {
                    try {
                        this.hdrSize = hdrs.available();
                        this.headers = new InternetHeaders(hdrs);
                    }
                    finally {
                        hdrs.close();
                    }
                }
            }
            if (fetchContent) {
                InputStream cs = null;
                try {
                    cs = this.getContentStream();
                }
                finally {
                    if (cs != null) {
                        cs.close();
                    }
                }
            }
        }
        catch (EOFException eex) {
            this.folder.close(false);
            throw new FolderClosedException(this.folder, eex.toString());
        }
        catch (IOException ex) {
            throw new MessagingException("error loading POP3 headers", ex);
        }
    }
}

