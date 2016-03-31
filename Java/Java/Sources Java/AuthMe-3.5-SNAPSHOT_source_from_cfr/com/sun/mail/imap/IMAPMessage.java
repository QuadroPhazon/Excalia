/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap;

import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPInputStream;
import com.sun.mail.imap.IMAPMultipartDataSource;
import com.sun.mail.imap.IMAPNestedMessage;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.MessageCache;
import com.sun.mail.imap.Utility;
import com.sun.mail.imap.protocol.BODY;
import com.sun.mail.imap.protocol.BODYSTRUCTURE;
import com.sun.mail.imap.protocol.ENVELOPE;
import com.sun.mail.imap.protocol.FetchItem;
import com.sun.mail.imap.protocol.FetchResponse;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.INTERNALDATE;
import com.sun.mail.imap.protocol.Item;
import com.sun.mail.imap.protocol.RFC822DATA;
import com.sun.mail.imap.protocol.RFC822SIZE;
import com.sun.mail.imap.protocol.UID;
import com.sun.mail.util.ReadableMime;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Header;
import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParameterList;

public class IMAPMessage
extends MimeMessage
implements ReadableMime {
    protected BODYSTRUCTURE bs;
    protected ENVELOPE envelope;
    protected Map items;
    private Date receivedDate;
    private int size = -1;
    private boolean peek;
    private long uid = -1;
    protected String sectionId;
    private String type;
    private String subject;
    private String description;
    private volatile boolean headersLoaded = false;
    private Hashtable loadedHeaders = new Hashtable(1);
    static final String EnvelopeCmd = "ENVELOPE INTERNALDATE RFC822.SIZE";

    protected IMAPMessage(IMAPFolder folder, int msgnum) {
        super(folder, msgnum);
        this.flags = null;
    }

    protected IMAPMessage(Session session) {
        super(session);
    }

    protected IMAPProtocol getProtocol() throws ProtocolException, FolderClosedException {
        ((IMAPFolder)this.folder).waitIfIdle();
        IMAPProtocol p = ((IMAPFolder)this.folder).protocol;
        if (p == null) {
            throw new FolderClosedException(this.folder);
        }
        return p;
    }

    protected boolean isREV1() throws FolderClosedException {
        IMAPProtocol p = ((IMAPFolder)this.folder).protocol;
        if (p == null) {
            throw new FolderClosedException(this.folder);
        }
        return p.isREV1();
    }

    protected Object getMessageCacheLock() {
        return ((IMAPFolder)this.folder).messageCacheLock;
    }

    protected int getSequenceNumber() {
        return ((IMAPFolder)this.folder).messageCache.seqnumOf(this.getMessageNumber());
    }

    protected void setMessageNumber(int msgnum) {
        super.setMessageNumber(msgnum);
    }

    protected long getUID() {
        return this.uid;
    }

    protected void setUID(long uid) {
        this.uid = uid;
    }

    protected void setExpunged(boolean set) {
        super.setExpunged(set);
    }

    protected void checkExpunged() throws MessageRemovedException {
        if (this.expunged) {
            throw new MessageRemovedException();
        }
    }

    protected void forceCheckExpunged() throws MessageRemovedException, FolderClosedException {
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                this.getProtocol().noop();
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                // empty catch block
            }
        }
        if (this.expunged) {
            throw new MessageRemovedException();
        }
    }

    protected int getFetchBlockSize() {
        return ((IMAPStore)this.folder.getStore()).getFetchBlockSize();
    }

    protected boolean ignoreBodyStructureSize() {
        return ((IMAPStore)this.folder.getStore()).ignoreBodyStructureSize();
    }

    public Address[] getFrom() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        InternetAddress[] a = this.envelope.from;
        if (a == null || a.length == 0) {
            a = this.envelope.sender;
        }
        return this.aaclone(a);
    }

    public void setFrom(Address address) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void addFrom(Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Address getSender() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        if (this.envelope.sender != null) {
            return this.envelope.sender[0];
        }
        return null;
    }

    public void setSender(Address address) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Address[] getRecipients(Message.RecipientType type) throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        if (type == Message.RecipientType.TO) {
            return this.aaclone(this.envelope.to);
        }
        if (type == Message.RecipientType.CC) {
            return this.aaclone(this.envelope.cc);
        }
        if (type == Message.RecipientType.BCC) {
            return this.aaclone(this.envelope.bcc);
        }
        return super.getRecipients(type);
    }

    public void setRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void addRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Address[] getReplyTo() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        if (this.envelope.replyTo == null || this.envelope.replyTo.length == 0) {
            return this.getFrom();
        }
        return this.aaclone(this.envelope.replyTo);
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getSubject() throws MessagingException {
        this.checkExpunged();
        if (this.subject != null) {
            return this.subject;
        }
        this.loadEnvelope();
        if (this.envelope.subject == null) {
            return null;
        }
        try {
            this.subject = MimeUtility.decodeText(MimeUtility.unfold(this.envelope.subject));
        }
        catch (UnsupportedEncodingException ex) {
            this.subject = this.envelope.subject;
        }
        return this.subject;
    }

    public void setSubject(String subject, String charset) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Date getSentDate() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        if (this.envelope.date == null) {
            return null;
        }
        return new Date(this.envelope.date.getTime());
    }

    public void setSentDate(Date d) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Date getReceivedDate() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        if (this.receivedDate == null) {
            return null;
        }
        return new Date(this.receivedDate.getTime());
    }

    public int getSize() throws MessagingException {
        this.checkExpunged();
        if (this.size == -1) {
            this.loadEnvelope();
        }
        return this.size;
    }

    public int getLineCount() throws MessagingException {
        this.checkExpunged();
        this.loadBODYSTRUCTURE();
        return this.bs.lines;
    }

    public String[] getContentLanguage() throws MessagingException {
        this.checkExpunged();
        this.loadBODYSTRUCTURE();
        if (this.bs.language != null) {
            return (String[])this.bs.language.clone();
        }
        return null;
    }

    public void setContentLanguage(String[] languages) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getInReplyTo() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        return this.envelope.inReplyTo;
    }

    public synchronized String getContentType() throws MessagingException {
        this.checkExpunged();
        if (this.type == null) {
            this.loadBODYSTRUCTURE();
            ContentType ct = new ContentType(this.bs.type, this.bs.subtype, this.bs.cParams);
            this.type = ct.toString();
        }
        return this.type;
    }

    public String getDisposition() throws MessagingException {
        this.checkExpunged();
        this.loadBODYSTRUCTURE();
        return this.bs.disposition;
    }

    public void setDisposition(String disposition) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getEncoding() throws MessagingException {
        this.checkExpunged();
        this.loadBODYSTRUCTURE();
        return this.bs.encoding;
    }

    public String getContentID() throws MessagingException {
        this.checkExpunged();
        this.loadBODYSTRUCTURE();
        return this.bs.id;
    }

    public void setContentID(String cid) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getContentMD5() throws MessagingException {
        this.checkExpunged();
        this.loadBODYSTRUCTURE();
        return this.bs.md5;
    }

    public void setContentMD5(String md5) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getDescription() throws MessagingException {
        this.checkExpunged();
        if (this.description != null) {
            return this.description;
        }
        this.loadBODYSTRUCTURE();
        if (this.bs.description == null) {
            return null;
        }
        try {
            this.description = MimeUtility.decodeText(this.bs.description);
        }
        catch (UnsupportedEncodingException ex) {
            this.description = this.bs.description;
        }
        return this.description;
    }

    public void setDescription(String description, String charset) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public String getMessageID() throws MessagingException {
        this.checkExpunged();
        this.loadEnvelope();
        return this.envelope.messageId;
    }

    public String getFileName() throws MessagingException {
        this.checkExpunged();
        String filename = null;
        this.loadBODYSTRUCTURE();
        if (this.bs.dParams != null) {
            filename = this.bs.dParams.get("filename");
        }
        if (filename == null && this.bs.cParams != null) {
            filename = this.bs.cParams.get("name");
        }
        return filename;
    }

    public void setFileName(String filename) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    protected InputStream getContentStream() throws MessagingException {
        ByteArrayInputStream is = null;
        boolean pk = this.getPeek();
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                if (p.isREV1() && this.getFetchBlockSize() != -1) {
                    return new IMAPInputStream(this, this.toSection("TEXT"), this.bs != null && !this.ignoreBodyStructureSize() ? this.bs.size : -1, pk);
                }
                if (p.isREV1()) {
                    BODY b = pk ? p.peekBody(this.getSequenceNumber(), this.toSection("TEXT")) : p.fetchBody(this.getSequenceNumber(), this.toSection("TEXT"));
                    if (b != null) {
                        is = b.getByteArrayInputStream();
                    }
                } else {
                    RFC822DATA rd = p.fetchRFC822(this.getSequenceNumber(), "TEXT");
                    if (rd != null) {
                        is = rd.getByteArrayInputStream();
                    }
                }
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) {
            throw new MessagingException("No content");
        }
        return is;
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        this.checkExpunged();
        if (this.dh == null) {
            this.loadBODYSTRUCTURE();
            if (this.type == null) {
                ContentType ct = new ContentType(this.bs.type, this.bs.subtype, this.bs.cParams);
                this.type = ct.toString();
            }
            if (this.bs.isMulti()) {
                this.dh = new DataHandler(new IMAPMultipartDataSource(this, this.bs.bodies, this.sectionId, this));
            } else if (this.bs.isNested() && this.isREV1() && this.bs.envelope != null) {
                this.dh = new DataHandler(new IMAPNestedMessage(this, this.bs.bodies[0], this.bs.envelope, this.sectionId == null ? "1" : this.sectionId + ".1"), this.type);
            }
        }
        return super.getDataHandler();
    }

    public void setDataHandler(DataHandler content) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public InputStream getMimeStream() throws MessagingException {
        ByteArrayInputStream is = null;
        boolean pk = this.getPeek();
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                if (p.isREV1() && this.getFetchBlockSize() != -1) {
                    return new IMAPInputStream(this, this.sectionId, -1, pk);
                }
                if (p.isREV1()) {
                    BODY b = pk ? p.peekBody(this.getSequenceNumber(), this.sectionId) : p.fetchBody(this.getSequenceNumber(), this.sectionId);
                    if (b != null) {
                        is = b.getByteArrayInputStream();
                    }
                } else {
                    RFC822DATA rd = p.fetchRFC822(this.getSequenceNumber(), null);
                    if (rd != null) {
                        is = rd.getByteArrayInputStream();
                    }
                }
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) {
            this.forceCheckExpunged();
            throw new MessagingException("No content");
        }
        return is;
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        InputStream is = this.getMimeStream();
        try {
            int count;
            byte[] bytes = new byte[16384];
            while ((count = is.read(bytes)) != -1) {
                os.write(bytes, 0, count);
            }
            Object var6_5 = null;
        }
        catch (Throwable var5_7) {
            Object var6_6 = null;
            is.close();
            throw var5_7;
        }
        is.close();
        {
        }
    }

    public String[] getHeader(String name) throws MessagingException {
        this.checkExpunged();
        if (this.isHeaderLoaded(name)) {
            return this.headers.getHeader(name);
        }
        ByteArrayInputStream is = null;
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                if (p.isREV1()) {
                    BODY b = p.peekBody(this.getSequenceNumber(), this.toSection("HEADER.FIELDS (" + name + ")"));
                    if (b != null) {
                        is = b.getByteArrayInputStream();
                    }
                } else {
                    RFC822DATA rd = p.fetchRFC822(this.getSequenceNumber(), "HEADER.LINES (" + name + ")");
                    if (rd != null) {
                        is = rd.getByteArrayInputStream();
                    }
                }
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) {
            return null;
        }
        if (this.headers == null) {
            this.headers = new InternetHeaders();
        }
        this.headers.load(is);
        this.setHeaderLoaded(name);
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        this.checkExpunged();
        if (this.getHeader(name) == null) {
            return null;
        }
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void addHeader(String name, String value) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public void removeHeader(String name) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Enumeration getAllHeaders() throws MessagingException {
        this.checkExpunged();
        this.loadHeaders();
        return super.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        this.checkExpunged();
        this.loadHeaders();
        return super.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        this.checkExpunged();
        this.loadHeaders();
        return super.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        throw new IllegalWriteException("IMAPMessage is read-only");
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        this.checkExpunged();
        this.loadHeaders();
        return super.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        this.checkExpunged();
        this.loadHeaders();
        return super.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        this.checkExpunged();
        this.loadHeaders();
        return super.getNonMatchingHeaderLines(names);
    }

    public synchronized Flags getFlags() throws MessagingException {
        this.checkExpunged();
        this.loadFlags();
        return super.getFlags();
    }

    public synchronized boolean isSet(Flags.Flag flag) throws MessagingException {
        this.checkExpunged();
        this.loadFlags();
        return super.isSet(flag);
    }

    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                p.storeFlags(this.getSequenceNumber(), flag, set);
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
    }

    public synchronized void setPeek(boolean peek) {
        this.peek = peek;
    }

    public synchronized boolean getPeek() {
        return this.peek;
    }

    public synchronized void invalidateHeaders() {
        this.headersLoaded = false;
        this.loadedHeaders.clear();
        this.headers = null;
        this.envelope = null;
        this.bs = null;
        this.receivedDate = null;
        this.size = -1;
        this.type = null;
        this.subject = null;
        this.description = null;
        this.flags = null;
    }

    protected boolean handleFetchItem(Item item, String[] hdrs, boolean allHeaders) throws MessagingException {
        if (item instanceof Flags) {
            this.flags = (Flags)((Object)item);
        } else if (item instanceof ENVELOPE) {
            this.envelope = (ENVELOPE)item;
        } else if (item instanceof INTERNALDATE) {
            this.receivedDate = ((INTERNALDATE)item).getDate();
        } else if (item instanceof RFC822SIZE) {
            this.size = ((RFC822SIZE)item).size;
        } else if (item instanceof BODYSTRUCTURE) {
            this.bs = (BODYSTRUCTURE)item;
        } else if (item instanceof UID) {
            UID u = (UID)item;
            this.uid = u.uid;
            if (((IMAPFolder)this.folder).uidTable == null) {
                ((IMAPFolder)this.folder).uidTable = new Hashtable();
            }
            ((IMAPFolder)this.folder).uidTable.put(new Long(u.uid), this);
        } else if (item instanceof RFC822DATA || item instanceof BODY) {
            ByteArrayInputStream headerStream = item instanceof RFC822DATA ? ((RFC822DATA)item).getByteArrayInputStream() : ((BODY)item).getByteArrayInputStream();
            InternetHeaders h = new InternetHeaders();
            if (headerStream != null) {
                h.load(headerStream);
            }
            if (this.headers == null || allHeaders) {
                this.headers = h;
            } else {
                Enumeration e = h.getAllHeaders();
                while (e.hasMoreElements()) {
                    Header he = (Header)e.nextElement();
                    if (this.isHeaderLoaded(he.getName())) continue;
                    this.headers.addHeader(he.getName(), he.getValue());
                }
            }
            if (allHeaders) {
                this.setHeadersLoaded(true);
            } else {
                for (int k = 0; k < hdrs.length; ++k) {
                    this.setHeaderLoaded(hdrs[k]);
                }
            }
        } else {
            return false;
        }
        return true;
    }

    protected void handleExtensionFetchItems(Map extensionItems) throws MessagingException {
        if (this.items == null) {
            this.items = extensionItems;
        } else {
            this.items.putAll(extensionItems);
        }
    }

    protected Object fetchItem(FetchItem fitem) throws MessagingException {
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            Object robj = null;
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                int seqnum = this.getSequenceNumber();
                Response[] r = p.fetch(seqnum, fitem.getName());
                for (int i = 0; i < r.length; ++i) {
                    Object o;
                    FetchResponse f;
                    if (r[i] == null || !(r[i] instanceof FetchResponse) || ((FetchResponse)r[i]).getNumber() != seqnum || (o = (f = (FetchResponse)r[i]).getExtensionItems().get(fitem.getName())) == null) continue;
                    robj = o;
                }
                p.notifyResponseHandlers(r);
                p.handleResult(r[r.length - 1]);
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
            return robj;
        }
    }

    public synchronized Object getItem(FetchItem fitem) throws MessagingException {
        Object item;
        Object object = item = this.items == null ? null : (Object)this.items.get(fitem.getName());
        if (item == null) {
            item = this.fetchItem(fitem);
        }
        return item;
    }

    private synchronized void loadEnvelope() throws MessagingException {
        if (this.envelope != null) {
            return;
        }
        Response[] r = null;
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                int seqnum = this.getSequenceNumber();
                r = p.fetch(seqnum, "ENVELOPE INTERNALDATE RFC822.SIZE");
                for (int i = 0; i < r.length; ++i) {
                    if (r[i] == null || !(r[i] instanceof FetchResponse) || ((FetchResponse)r[i]).getNumber() != seqnum) continue;
                    FetchResponse f = (FetchResponse)r[i];
                    int count = f.getItemCount();
                    for (int j = 0; j < count; ++j) {
                        Item item = f.getItem(j);
                        if (item instanceof ENVELOPE) {
                            this.envelope = (ENVELOPE)item;
                            continue;
                        }
                        if (item instanceof INTERNALDATE) {
                            this.receivedDate = ((INTERNALDATE)item).getDate();
                            continue;
                        }
                        if (!(item instanceof RFC822SIZE)) continue;
                        this.size = ((RFC822SIZE)item).size;
                    }
                }
                p.notifyResponseHandlers(r);
                p.handleResult(r[r.length - 1]);
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (this.envelope == null) {
            throw new MessagingException("Failed to load IMAP envelope");
        }
    }

    private synchronized void loadBODYSTRUCTURE() throws MessagingException {
        if (this.bs != null) {
            return;
        }
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                this.bs = p.fetchBodyStructure(this.getSequenceNumber());
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
            if (this.bs == null) {
                this.forceCheckExpunged();
                throw new MessagingException("Unable to load BODYSTRUCTURE");
            }
        }
    }

    private synchronized void loadHeaders() throws MessagingException {
        if (this.headersLoaded) {
            return;
        }
        ByteArrayInputStream is = null;
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                if (p.isREV1()) {
                    BODY b = p.peekBody(this.getSequenceNumber(), this.toSection("HEADER"));
                    if (b != null) {
                        is = b.getByteArrayInputStream();
                    }
                } else {
                    RFC822DATA rd = p.fetchRFC822(this.getSequenceNumber(), "HEADER");
                    if (rd != null) {
                        is = rd.getByteArrayInputStream();
                    }
                }
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (is == null) {
            throw new MessagingException("Cannot load header");
        }
        this.headers = new InternetHeaders(is);
        this.headersLoaded = true;
    }

    private synchronized void loadFlags() throws MessagingException {
        if (this.flags != null) {
            return;
        }
        Object object = this.getMessageCacheLock();
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                this.checkExpunged();
                this.flags = p.fetchFlags(this.getSequenceNumber());
                if (this.flags == null) {
                    this.flags = new Flags();
                }
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this.folder, cex.getMessage());
            }
            catch (ProtocolException pex) {
                this.forceCheckExpunged();
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
    }

    private boolean areHeadersLoaded() {
        return this.headersLoaded;
    }

    private void setHeadersLoaded(boolean loaded) {
        this.headersLoaded = loaded;
    }

    private boolean isHeaderLoaded(String name) {
        if (this.headersLoaded) {
            return true;
        }
        return this.loadedHeaders.containsKey(name.toUpperCase(Locale.ENGLISH));
    }

    private void setHeaderLoaded(String name) {
        this.loadedHeaders.put(name.toUpperCase(Locale.ENGLISH), name);
    }

    private String toSection(String what) {
        if (this.sectionId == null) {
            return what;
        }
        return this.sectionId + "." + what;
    }

    private InternetAddress[] aaclone(InternetAddress[] aa) {
        if (aa == null) {
            return null;
        }
        return (InternetAddress[])aa.clone();
    }

    private Flags _getFlags() {
        return this.flags;
    }

    private ENVELOPE _getEnvelope() {
        return this.envelope;
    }

    private BODYSTRUCTURE _getBodyStructure() {
        return this.bs;
    }

    void _setFlags(Flags flags) {
        this.flags = flags;
    }

    Session _getSession() {
        return this.session;
    }

    public static class FetchProfileCondition
    implements Utility.Condition {
        private boolean needEnvelope = false;
        private boolean needFlags = false;
        private boolean needBodyStructure = false;
        private boolean needUID = false;
        private boolean needHeaders = false;
        private boolean needSize = false;
        private String[] hdrs = null;
        private Set need = new HashSet();

        public FetchProfileCondition(FetchProfile fp, FetchItem[] fitems) {
            if (fp.contains(FetchProfile.Item.ENVELOPE)) {
                this.needEnvelope = true;
            }
            if (fp.contains(FetchProfile.Item.FLAGS)) {
                this.needFlags = true;
            }
            if (fp.contains(FetchProfile.Item.CONTENT_INFO)) {
                this.needBodyStructure = true;
            }
            if (fp.contains(FetchProfile.Item.SIZE)) {
                this.needSize = true;
            }
            if (fp.contains(UIDFolder.FetchProfileItem.UID)) {
                this.needUID = true;
            }
            if (fp.contains(IMAPFolder.FetchProfileItem.HEADERS)) {
                this.needHeaders = true;
            }
            if (fp.contains(IMAPFolder.FetchProfileItem.SIZE)) {
                this.needSize = true;
            }
            this.hdrs = fp.getHeaderNames();
            for (int i = 0; i < fitems.length; ++i) {
                if (!fp.contains(fitems[i].getFetchProfileItem())) continue;
                this.need.add(fitems[i]);
            }
        }

        public boolean test(IMAPMessage m) {
            if (this.needEnvelope && m._getEnvelope() == null) {
                return true;
            }
            if (this.needFlags && m._getFlags() == null) {
                return true;
            }
            if (this.needBodyStructure && m._getBodyStructure() == null) {
                return true;
            }
            if (this.needUID && m.getUID() == -1) {
                return true;
            }
            if (this.needHeaders && !m.areHeadersLoaded()) {
                return true;
            }
            if (this.needSize && m.size == -1) {
                return true;
            }
            for (int i = 0; i < this.hdrs.length; ++i) {
                if (m.isHeaderLoaded(this.hdrs[i])) continue;
                return true;
            }
            for (FetchItem fitem : this.need) {
                if (m.items != null && m.items.get(fitem.getName()) != null) continue;
                return true;
            }
            return false;
        }
    }

}

