/*
 * Decompiled with CFR 0_110.
 */
package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import com.sun.mail.util.MimeUtil;
import com.sun.mail.util.PropUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MailDateFormat;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimePartDataSource;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.NewsAddress;
import javax.mail.internet.SharedInputStream;
import javax.mail.internet.UniqueValue;
import javax.mail.util.SharedByteArrayInputStream;

public class MimeMessage
extends Message
implements MimePart {
    protected DataHandler dh;
    protected byte[] content;
    protected InputStream contentStream;
    protected InternetHeaders headers;
    protected Flags flags;
    protected boolean modified = false;
    protected boolean saved = false;
    protected Object cachedContent;
    private static final MailDateFormat mailDateFormat = new MailDateFormat();
    private boolean strict = true;
    private static final Flags answeredFlag = new Flags(Flags.Flag.ANSWERED);

    public MimeMessage(Session session) {
        super(session);
        this.modified = true;
        this.headers = new InternetHeaders();
        this.flags = new Flags();
        this.initStrict();
    }

    public MimeMessage(Session session, InputStream is) throws MessagingException {
        super(session);
        this.flags = new Flags();
        this.initStrict();
        this.parse(is);
        this.saved = true;
    }

    public MimeMessage(MimeMessage source) throws MessagingException {
        int size;
        super(source.session);
        this.flags = source.getFlags();
        if (this.flags == null) {
            this.flags = new Flags();
        }
        ByteArrayOutputStream bos = (size = source.getSize()) > 0 ? new ByteArrayOutputStream(size) : new ByteArrayOutputStream();
        try {
            this.strict = source.strict;
            source.writeTo(bos);
            bos.close();
            SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
            this.parse(bis);
            bis.close();
            this.saved = true;
        }
        catch (IOException ex) {
            throw new MessagingException("IOException while copying message", ex);
        }
    }

    protected MimeMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
        this.flags = new Flags();
        this.saved = true;
        this.initStrict();
    }

    protected MimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
        this(folder, msgnum);
        this.initStrict();
        this.parse(is);
    }

    protected MimeMessage(Folder folder, InternetHeaders headers, byte[] content, int msgnum) throws MessagingException {
        this(folder, msgnum);
        this.headers = headers;
        this.content = content;
        this.initStrict();
    }

    private void initStrict() {
        if (this.session != null) {
            this.strict = PropUtil.getBooleanSessionProperty(this.session, "mail.mime.address.strict", true);
        }
    }

    protected void parse(InputStream is) throws MessagingException {
        if (!(is instanceof ByteArrayInputStream || is instanceof BufferedInputStream || is instanceof SharedInputStream)) {
            is = new BufferedInputStream(is);
        }
        this.headers = this.createInternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream)((Object)is);
            this.contentStream = sis.newStream(sis.getPosition(), -1);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(is);
            }
            catch (IOException ioex) {
                throw new MessagingException("IOException", ioex);
            }
        }
        this.modified = false;
    }

    public Address[] getFrom() throws MessagingException {
        Address[] a = this.getAddressHeader("From");
        if (a == null) {
            a = this.getAddressHeader("Sender");
        }
        return a;
    }

    public void setFrom(Address address) throws MessagingException {
        if (address == null) {
            this.removeHeader("From");
        } else {
            this.setHeader("From", address.toString());
        }
    }

    public void setFrom(String address) throws MessagingException {
        if (address == null) {
            this.removeHeader("From");
        } else {
            this.setAddressHeader("From", InternetAddress.parse(address));
        }
    }

    public void setFrom() throws MessagingException {
        InternetAddress me = null;
        try {
            me = InternetAddress._getLocalAddress(this.session);
        }
        catch (Exception ex) {
            throw new MessagingException("No From address", ex);
        }
        if (me == null) {
            throw new MessagingException("No From address");
        }
        this.setFrom(me);
    }

    public void addFrom(Address[] addresses) throws MessagingException {
        this.addAddressHeader("From", addresses);
    }

    public Address getSender() throws MessagingException {
        Address[] a = this.getAddressHeader("Sender");
        if (a == null || a.length == 0) {
            return null;
        }
        return a[0];
    }

    public void setSender(Address address) throws MessagingException {
        if (address == null) {
            this.removeHeader("Sender");
        } else {
            this.setHeader("Sender", address.toString());
        }
    }

    public Address[] getRecipients(Message.RecipientType type) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            String s = this.getHeader("Newsgroups", ",");
            return s == null ? null : NewsAddress.parse(s);
        }
        return this.getAddressHeader(this.getHeaderName(type));
    }

    public Address[] getAllRecipients() throws MessagingException {
        Address[] all = super.getAllRecipients();
        Address[] ng = this.getRecipients(RecipientType.NEWSGROUPS);
        if (ng == null) {
            return all;
        }
        if (all == null) {
            return ng;
        }
        Address[] addresses = new Address[all.length + ng.length];
        System.arraycopy(all, 0, addresses, 0, all.length);
        System.arraycopy(ng, 0, addresses, all.length, ng.length);
        return addresses;
    }

    public void setRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            if (addresses == null || addresses.length == 0) {
                this.removeHeader("Newsgroups");
            } else {
                this.setHeader("Newsgroups", NewsAddress.toString(addresses));
            }
        } else {
            this.setAddressHeader(this.getHeaderName(type), addresses);
        }
    }

    public void setRecipients(Message.RecipientType type, String addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            if (addresses == null || addresses.length() == 0) {
                this.removeHeader("Newsgroups");
            } else {
                this.setHeader("Newsgroups", addresses);
            }
        } else {
            this.setAddressHeader(this.getHeaderName(type), addresses == null ? null : InternetAddress.parse(addresses));
        }
    }

    public void addRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            String s = NewsAddress.toString(addresses);
            if (s != null) {
                this.addHeader("Newsgroups", s);
            }
        } else {
            this.addAddressHeader(this.getHeaderName(type), addresses);
        }
    }

    public void addRecipients(Message.RecipientType type, String addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            if (addresses != null && addresses.length() != 0) {
                this.addHeader("Newsgroups", addresses);
            }
        } else {
            this.addAddressHeader(this.getHeaderName(type), InternetAddress.parse(addresses));
        }
    }

    public Address[] getReplyTo() throws MessagingException {
        Address[] a = this.getAddressHeader("Reply-To");
        if (a == null || a.length == 0) {
            a = this.getFrom();
        }
        return a;
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        this.setAddressHeader("Reply-To", addresses);
    }

    private Address[] getAddressHeader(String name) throws MessagingException {
        String s = this.getHeader(name, ",");
        return s == null ? null : InternetAddress.parseHeader(s, this.strict);
    }

    private void setAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s == null) {
            this.removeHeader(name);
        } else {
            this.setHeader(name, s);
        }
    }

    private void addAddressHeader(String name, Address[] addresses) throws MessagingException {
        Address[] anew;
        if (addresses == null || addresses.length == 0) {
            return;
        }
        Address[] a = this.getAddressHeader(name);
        if (a == null || a.length == 0) {
            anew = addresses;
        } else {
            anew = new Address[a.length + addresses.length];
            System.arraycopy(a, 0, anew, 0, a.length);
            System.arraycopy(addresses, 0, anew, a.length, addresses.length);
        }
        String s = InternetAddress.toString(anew);
        if (s == null) {
            return;
        }
        this.setHeader(name, s);
    }

    public String getSubject() throws MessagingException {
        String rawvalue = this.getHeader("Subject", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        }
        catch (UnsupportedEncodingException ex) {
            return rawvalue;
        }
    }

    public void setSubject(String subject) throws MessagingException {
        this.setSubject(subject, null);
    }

    public void setSubject(String subject, String charset) throws MessagingException {
        if (subject == null) {
            this.removeHeader("Subject");
        } else {
            try {
                this.setHeader("Subject", MimeUtility.fold(9, MimeUtility.encodeText(subject, charset, null)));
            }
            catch (UnsupportedEncodingException uex) {
                throw new MessagingException("Encoding error", uex);
            }
        }
    }

    public Date getSentDate() throws MessagingException {
        String s = this.getHeader("Date", null);
        if (s != null) {
            try {
                MailDateFormat mailDateFormat = MimeMessage.mailDateFormat;
                synchronized (mailDateFormat) {
                    return MimeMessage.mailDateFormat.parse(s);
                }
            }
            catch (ParseException pex) {
                return null;
            }
        }
        return null;
    }

    public void setSentDate(Date d) throws MessagingException {
        if (d == null) {
            this.removeHeader("Date");
        } else {
            MailDateFormat mailDateFormat = MimeMessage.mailDateFormat;
            synchronized (mailDateFormat) {
                this.setHeader("Date", MimeMessage.mailDateFormat.format(d));
            }
        }
    }

    public Date getReceivedDate() throws MessagingException {
        return null;
    }

    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream != null) {
            try {
                int size = this.contentStream.available();
                if (size > 0) {
                    return size;
                }
            }
            catch (IOException ex) {
                // empty catch block
            }
        }
        return -1;
    }

    public int getLineCount() throws MessagingException {
        return -1;
    }

    public String getContentType() throws MessagingException {
        String s = this.getHeader("Content-Type", null);
        if ((s = MimeUtil.cleanContentType(this, s)) == null) {
            return "text/plain";
        }
        return s;
    }

    public boolean isMimeType(String mimeType) throws MessagingException {
        return MimeBodyPart.isMimeType(this, mimeType);
    }

    public String getDisposition() throws MessagingException {
        return MimeBodyPart.getDisposition(this);
    }

    public void setDisposition(String disposition) throws MessagingException {
        MimeBodyPart.setDisposition(this, disposition);
    }

    public String getEncoding() throws MessagingException {
        return MimeBodyPart.getEncoding(this);
    }

    public String getContentID() throws MessagingException {
        return this.getHeader("Content-Id", null);
    }

    public void setContentID(String cid) throws MessagingException {
        if (cid == null) {
            this.removeHeader("Content-ID");
        } else {
            this.setHeader("Content-ID", cid);
        }
    }

    public String getContentMD5() throws MessagingException {
        return this.getHeader("Content-MD5", null);
    }

    public void setContentMD5(String md5) throws MessagingException {
        this.setHeader("Content-MD5", md5);
    }

    public String getDescription() throws MessagingException {
        return MimeBodyPart.getDescription(this);
    }

    public void setDescription(String description) throws MessagingException {
        this.setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        MimeBodyPart.setDescription(this, description, charset);
    }

    public String[] getContentLanguage() throws MessagingException {
        return MimeBodyPart.getContentLanguage(this);
    }

    public void setContentLanguage(String[] languages) throws MessagingException {
        MimeBodyPart.setContentLanguage(this, languages);
    }

    public String getMessageID() throws MessagingException {
        return this.getHeader("Message-ID", null);
    }

    public String getFileName() throws MessagingException {
        return MimeBodyPart.getFileName(this);
    }

    public void setFileName(String filename) throws MessagingException {
        MimeBodyPart.setFileName(this, filename);
    }

    private String getHeaderName(Message.RecipientType type) throws MessagingException {
        String headerName;
        if (type == Message.RecipientType.TO) {
            headerName = "To";
        } else if (type == Message.RecipientType.CC) {
            headerName = "Cc";
        } else if (type == Message.RecipientType.BCC) {
            headerName = "Bcc";
        } else if (type == RecipientType.NEWSGROUPS) {
            headerName = "Newsgroups";
        } else {
            throw new MessagingException("Invalid Recipient Type");
        }
        return headerName;
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return this.getDataHandler().getInputStream();
    }

    protected InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream)((Object)this.contentStream)).newStream(0, -1);
        }
        if (this.content != null) {
            return new SharedByteArrayInputStream(this.content);
        }
        throw new MessagingException("No MimeMessage content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return this.getContentStream();
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new MimeBodyPart.MimePartDataHandler(new MimePartDataSource(this));
        }
        return this.dh;
    }

    public Object getContent() throws IOException, MessagingException {
        Object c;
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            c = this.getDataHandler().getContent();
        }
        catch (FolderClosedIOException fex) {
            throw new FolderClosedException(fex.getFolder(), fex.getMessage());
        }
        catch (MessageRemovedIOException mex) {
            throw new MessageRemovedException(mex.getMessage());
        }
        if (MimeBodyPart.cacheMultipart && (c instanceof Multipart || c instanceof Message) && (this.content != null || this.contentStream != null)) {
            this.cachedContent = c;
            if (c instanceof MimeMultipart) {
                ((MimeMultipart)c).parse();
            }
        }
        return c;
    }

    public synchronized void setDataHandler(DataHandler dh) throws MessagingException {
        this.dh = dh;
        this.cachedContent = null;
        MimeBodyPart.invalidateContentHeaders(this);
    }

    public void setContent(Object o, String type) throws MessagingException {
        if (o instanceof Multipart) {
            this.setContent((Multipart)o);
        } else {
            this.setDataHandler(new DataHandler(o, type));
        }
    }

    public void setText(String text) throws MessagingException {
        this.setText(text, null);
    }

    public void setText(String text, String charset) throws MessagingException {
        MimeBodyPart.setText(this, text, charset, "plain");
    }

    public void setText(String text, String charset, String subtype) throws MessagingException {
        MimeBodyPart.setText(this, text, charset, subtype);
    }

    public void setContent(Multipart mp) throws MessagingException {
        this.setDataHandler(new DataHandler(mp, mp.getContentType()));
        mp.setParent(this);
    }

    public Message reply(boolean replyToAll) throws MessagingException {
        return this.reply(replyToAll, true);
    }

    public Message reply(boolean replyToAll, boolean setAnswered) throws MessagingException {
        String msgId;
        String refs;
        MimeMessage reply = this.createMimeMessage(this.session);
        String subject = this.getHeader("Subject", null);
        if (subject != null) {
            if (!subject.regionMatches(true, 0, "Re: ", 0, 4)) {
                subject = "Re: " + subject;
            }
            reply.setHeader("Subject", subject);
        }
        Address[] a = this.getReplyTo();
        reply.setRecipients(Message.RecipientType.TO, a);
        if (replyToAll) {
            Vector<InternetAddress> v = new Vector<InternetAddress>();
            InternetAddress me = InternetAddress.getLocalAddress(this.session);
            if (me != null) {
                v.addElement(me);
            }
            String alternates = null;
            if (this.session != null) {
                alternates = this.session.getProperty("mail.alternates");
            }
            if (alternates != null) {
                this.eliminateDuplicates(v, InternetAddress.parse(alternates, false));
            }
            Object replyallccStr = null;
            boolean replyallcc = false;
            if (this.session != null) {
                replyallcc = PropUtil.getBooleanSessionProperty(this.session, "mail.replyallcc", false);
            }
            this.eliminateDuplicates(v, a);
            a = this.getRecipients(Message.RecipientType.TO);
            a = this.eliminateDuplicates(v, a);
            if (a != null && a.length > 0) {
                if (replyallcc) {
                    reply.addRecipients(Message.RecipientType.CC, a);
                } else {
                    reply.addRecipients(Message.RecipientType.TO, a);
                }
            }
            a = this.getRecipients(Message.RecipientType.CC);
            if ((a = this.eliminateDuplicates(v, a)) != null && a.length > 0) {
                reply.addRecipients(Message.RecipientType.CC, a);
            }
            if ((a = this.getRecipients(RecipientType.NEWSGROUPS)) != null && a.length > 0) {
                reply.setRecipients((Message.RecipientType)RecipientType.NEWSGROUPS, a);
            }
        }
        if ((msgId = this.getHeader("Message-Id", null)) != null) {
            reply.setHeader("In-Reply-To", msgId);
        }
        if ((refs = this.getHeader("References", " ")) == null) {
            refs = this.getHeader("In-Reply-To", " ");
        }
        if (msgId != null) {
            refs = refs != null ? MimeUtility.unfold(refs) + " " + msgId : msgId;
        }
        if (refs != null) {
            reply.setHeader("References", MimeUtility.fold(12, refs));
        }
        if (setAnswered) {
            try {
                this.setFlags(answeredFlag, true);
            }
            catch (MessagingException mex) {
                // empty catch block
            }
        }
        return reply;
    }

    private Address[] eliminateDuplicates(Vector v, Address[] addrs) {
        int j;
        if (addrs == null) {
            return null;
        }
        int gone = 0;
        for (int i = 0; i < addrs.length; ++i) {
            boolean found = false;
            for (j = 0; j < v.size(); ++j) {
                if (!((InternetAddress)v.elementAt(j)).equals(addrs[i])) continue;
                found = true;
                ++gone;
                addrs[i] = null;
                break;
            }
            if (found) continue;
            v.addElement(addrs[i]);
        }
        if (gone != 0) {
            InternetAddress[] a = addrs instanceof InternetAddress[] ? new InternetAddress[addrs.length - gone] : new Address[addrs.length - gone];
            j = 0;
            for (int i2 = 0; i2 < addrs.length; ++i2) {
                if (addrs[i2] == null) continue;
                a[j++] = addrs[i2];
            }
            addrs = a;
        }
        return addrs;
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        this.writeTo(os, null);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        block7 : {
            if (!this.saved) {
                this.saveChanges();
            }
            if (this.modified) {
                MimeBodyPart.writeTo(this, os, ignoreList);
                return;
            }
            hdrLines = this.getNonMatchingHeaderLines(ignoreList);
            los = new LineOutputStream(os);
            while (hdrLines.hasMoreElements()) {
                los.writeln((String)hdrLines.nextElement());
            }
            los.writeln();
            if (this.content != null) ** GOTO lbl30
            is = null;
            buf = new byte[8192];
            try {
                is = this.getContentStream();
                while ((len = is.read((byte[])buf)) > 0) {
                    os.write((byte[])buf, 0, len);
                }
                if (is == null) break block7;
            }
            catch (Throwable var8_8) {
                if (is != null) {
                    is.close();
                }
                buf = null;
                throw var8_8;
            }
            is.close();
        }
        buf = null;
        ** GOTO lbl31
lbl30: // 1 sources:
        os.write(this.content);
lbl31: // 2 sources:
        os.flush();
    }

    public String[] getHeader(String name) throws MessagingException {
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        this.headers.setHeader(name, value);
    }

    public void addHeader(String name, String value) throws MessagingException {
        this.headers.addHeader(name, value);
    }

    public void removeHeader(String name) throws MessagingException {
        this.headers.removeHeader(name);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        this.headers.addHeaderLine(line);
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(names);
    }

    public synchronized Flags getFlags() throws MessagingException {
        return (Flags)this.flags.clone();
    }

    public synchronized boolean isSet(Flags.Flag flag) throws MessagingException {
        return this.flags.contains(flag);
    }

    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        if (set) {
            this.flags.add(flag);
        } else {
            this.flags.remove(flag);
        }
    }

    public void saveChanges() throws MessagingException {
        this.modified = true;
        this.saved = true;
        this.updateHeaders();
    }

    protected void updateMessageID() throws MessagingException {
        this.setHeader("Message-ID", "<" + UniqueValue.getUniqueMessageIDValue(this.session) + ">");
    }

    protected synchronized void updateHeaders() throws MessagingException {
        MimeBodyPart.updateHeaders(this);
        this.setHeader("MIME-Version", "1.0");
        this.updateMessageID();
        if (this.cachedContent != null) {
            this.dh = new DataHandler(this.cachedContent, this.getContentType());
            this.cachedContent = null;
            this.content = null;
            if (this.contentStream != null) {
                try {
                    this.contentStream.close();
                }
                catch (IOException ioex) {
                    // empty catch block
                }
            }
            this.contentStream = null;
        }
    }

    protected InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    protected MimeMessage createMimeMessage(Session session) throws MessagingException {
        return new MimeMessage(session);
    }

    public static class RecipientType
    extends Message.RecipientType {
        private static final long serialVersionUID = -5468290701714395543L;
        public static final RecipientType NEWSGROUPS = new RecipientType("Newsgroups");

        protected RecipientType(String type) {
            super(type);
        }

        protected Object readResolve() throws ObjectStreamException {
            if (this.type.equals("Newsgroups")) {
                return NEWSGROUPS;
            }
            return super.readResolve();
        }
    }

}

