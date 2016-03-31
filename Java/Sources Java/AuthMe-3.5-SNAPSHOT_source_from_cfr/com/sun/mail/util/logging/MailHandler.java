/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.util.logging;

import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.logging.LogManagerProperties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class MailHandler
extends Handler {
    private static final Filter[] EMPTY_FILTERS = new Filter[0];
    private static final Formatter[] EMPTY_FORMATTERS = new Formatter[0];
    private static final int MIN_HEADER_SIZE = 1024;
    private static final int offValue = Level.OFF.intValue();
    private static final GetAndSetContext GET_AND_SET_CCL = new GetAndSetContext(MailHandler.class);
    private static final ThreadLocal<Level> MUTEX = new ThreadLocal();
    private static final Level MUTEX_PUBLISH = Level.ALL;
    private static final Level MUTEX_REPORT = Level.OFF;
    private volatile boolean sealed;
    private boolean isWriting;
    private Properties mailProps;
    private Authenticator auth;
    private Session session;
    private LogRecord[] data;
    private int size;
    private int capacity;
    private Comparator<? super LogRecord> comparator;
    private Formatter subjectFormatter;
    private Level pushLevel;
    private Filter pushFilter;
    private Filter[] attachmentFilters;
    private Formatter[] attachmentFormatters;
    private Formatter[] attachmentNames;
    private FileTypeMap contentTypes;

    public MailHandler() {
        this.init(null);
        this.sealed = true;
    }

    public MailHandler(int capacity) {
        this.init(null);
        this.sealed = true;
        this.setCapacity0(capacity);
    }

    public MailHandler(Properties props) {
        if (props == null) {
            throw new NullPointerException();
        }
        this.init(props);
        this.sealed = true;
        this.setMailProperties0(props);
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        int levelValue = this.getLevel().intValue();
        if (record.getLevel().intValue() < levelValue || levelValue == offValue) {
            return false;
        }
        Filter body = this.getFilter();
        if (body == null || body.isLoggable(record)) {
            return true;
        }
        return this.isAttachmentLoggable(record);
    }

    @Override
    public void publish(LogRecord record) {
        if (this.tryMutex()) {
            try {
                if (this.isLoggable(record)) {
                    record.getSourceMethodName();
                    this.publish0(record);
                }
                Object var3_2 = null;
                this.releaseMutex();
            }
            catch (Throwable var2_4) {
                Object var3_3 = null;
                this.releaseMutex();
                throw var2_4;
            }
        } else {
            this.reportUnPublishedError(record);
        }
    }

    private void publish0(LogRecord record) {
        Message msg;
        boolean priority;
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            if (this.size == this.data.length && this.size < this.capacity) {
                this.grow();
            }
            if (this.size < this.data.length) {
                this.data[this.size] = record;
                ++this.size;
                priority = this.isPushable(record);
                msg = priority || this.size >= this.capacity ? this.writeLogRecords(1) : null;
            } else {
                priority = false;
                msg = null;
            }
        }
        if (msg != null) {
            this.send(msg, priority, 1);
        }
    }

    private void reportUnPublishedError(LogRecord record) {
        if (MUTEX_PUBLISH.equals(MUTEX.get())) {
            MUTEX.set(MUTEX_REPORT);
            try {
                String msg;
                if (record != null) {
                    SimpleFormatter f = new SimpleFormatter();
                    msg = "Log record " + record.getSequenceNumber() + " was not published. " + this.head(f) + this.format(f, record) + this.tail(f, "");
                } else {
                    msg = null;
                }
                IllegalStateException e = new IllegalStateException("Recursive publish detected by thread " + Thread.currentThread());
                this.reportError(msg, (Exception)e, 1);
                Object var5_4 = null;
                MUTEX.set(MUTEX_PUBLISH);
            }
            catch (Throwable var4_6) {
                Object var5_5 = null;
                MUTEX.set(MUTEX_PUBLISH);
                throw var4_6;
            }
        }
    }

    private boolean tryMutex() {
        if (MUTEX.get() == null) {
            MUTEX.set(MUTEX_PUBLISH);
            return true;
        }
        return false;
    }

    private void releaseMutex() {
        MUTEX.remove();
    }

    public void push() {
        this.push(true, 2);
    }

    @Override
    public void flush() {
        this.push(false, 2);
    }

    @Override
    public void close() {
        this.checkAccess();
        Object ccl = this.getAndSetContextClassLoader();
        try {
            Message msg = null;
            MailHandler mailHandler = this;
            synchronized (mailHandler) {
                try {
                    msg = this.writeLogRecords(3);
                    Object var5_4 = null;
                }
                catch (Throwable var4_6) {
                    Object var5_5 = null;
                    super.setLevel(Level.OFF);
                    if (this.capacity > 0) {
                        this.capacity = - this.capacity;
                    }
                    if (this.size == 0 && this.data.length != 1) {
                        this.data = new LogRecord[1];
                    }
                    throw var4_6;
                }
                super.setLevel(Level.OFF);
                if (this.capacity > 0) {
                    this.capacity = - this.capacity;
                }
                if (this.size == 0 && this.data.length != 1) {
                    this.data = new LogRecord[1];
                }
            }
            if (msg != null) {
                this.send(msg, false, 3);
            }
            Object var8_8 = null;
            this.setContextClassLoader(ccl);
        }
        catch (Throwable var7_10) {
            Object var8_9 = null;
            this.setContextClassLoader(ccl);
            throw var7_10;
        }
    }

    @Override
    public synchronized void setLevel(Level newLevel) {
        if (this.capacity > 0) {
            super.setLevel(newLevel);
        } else {
            if (newLevel == null) {
                throw new NullPointerException();
            }
            this.checkAccess();
        }
    }

    public final synchronized Level getPushLevel() {
        return this.pushLevel;
    }

    public final synchronized void setPushLevel(Level level) {
        this.checkAccess();
        if (level == null) {
            throw new NullPointerException();
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushLevel = level;
    }

    public final synchronized Filter getPushFilter() {
        return this.pushFilter;
    }

    public final synchronized void setPushFilter(Filter filter) {
        this.checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.pushFilter = filter;
    }

    public final synchronized Comparator<? super LogRecord> getComparator() {
        return this.comparator;
    }

    public final synchronized void setComparator(Comparator<? super LogRecord> c) {
        this.checkAccess();
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.comparator = c;
    }

    public final synchronized int getCapacity() {
        assert (this.capacity != Integer.MIN_VALUE && this.capacity != 0);
        return Math.abs(this.capacity);
    }

    public final synchronized Authenticator getAuthenticator() {
        this.checkAccess();
        return this.auth;
    }

    public final void setAuthenticator(Authenticator auth) {
        this.setAuthenticator0(auth);
    }

    public final /* varargs */ void setAuthenticator(char ... password) {
        if (password == null) {
            this.setAuthenticator0(null);
        } else {
            this.setAuthenticator0(new DefaultAuthenticator(new String(password)));
        }
    }

    private void setAuthenticator0(Authenticator auth) {
        Session settings;
        this.checkAccess();
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.auth = auth;
            settings = this.fixUpSession();
        }
        this.verifySettings(settings);
    }

    public final void setMailProperties(Properties props) {
        this.setMailProperties0(props);
    }

    private void setMailProperties0(Properties props) {
        Session settings;
        this.checkAccess();
        props = (Properties)props.clone();
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.mailProps = props;
            settings = this.fixUpSession();
        }
        this.verifySettings(settings);
    }

    public final Properties getMailProperties() {
        Properties props;
        this.checkAccess();
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            props = this.mailProps;
        }
        return (Properties)props.clone();
    }

    public final Filter[] getAttachmentFilters() {
        return (Filter[])this.readOnlyAttachmentFilters().clone();
    }

    public final /* varargs */ void setAttachmentFilters(Filter ... filters) {
        this.checkAccess();
        filters = (Filter[])MailHandler.copyOf(filters, filters.length, Filter[].class);
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            if (this.attachmentFormatters.length != filters.length) {
                throw MailHandler.attachmentMismatch(this.attachmentFormatters.length, filters.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentFilters = filters;
        }
    }

    public final Formatter[] getAttachmentFormatters() {
        Formatter[] formatters;
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            formatters = this.attachmentFormatters;
        }
        return (Formatter[])formatters.clone();
    }

    public final /* varargs */ void setAttachmentFormatters(Formatter ... formatters) {
        this.checkAccess();
        if (formatters.length == 0) {
            formatters = MailHandler.emptyFormatterArray();
        } else {
            formatters = (Formatter[])MailHandler.copyOf(formatters, formatters.length, Formatter[].class);
            for (int i = 0; i < formatters.length; ++i) {
                if (formatters[i] != null) continue;
                throw new NullPointerException(MailHandler.atIndexMsg(i));
            }
        }
        MailHandler i = this;
        synchronized (i) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentFormatters = formatters;
            this.fixUpAttachmentFilters();
            this.fixUpAttachmentNames();
        }
    }

    public final Formatter[] getAttachmentNames() {
        Formatter[] formatters;
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            formatters = this.attachmentNames;
        }
        return (Formatter[])formatters.clone();
    }

    public final /* varargs */ void setAttachmentNames(String ... names) {
        this.checkAccess();
        Formatter[] formatters = names.length == 0 ? MailHandler.emptyFormatterArray() : new Formatter[names.length];
        for (int i2 = 0; i2 < names.length; ++i2) {
            String name = names[i2];
            if (name != null) {
                if (name.length() <= 0) {
                    throw new IllegalArgumentException(MailHandler.atIndexMsg(i2));
                }
            } else {
                throw new NullPointerException(MailHandler.atIndexMsg(i2));
            }
            formatters[i2] = new TailNameFormatter(name);
        }
        MailHandler i2 = this;
        synchronized (i2) {
            if (this.attachmentFormatters.length != names.length) {
                throw MailHandler.attachmentMismatch(this.attachmentFormatters.length, names.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentNames = formatters;
        }
    }

    public final /* varargs */ void setAttachmentNames(Formatter ... formatters) {
        this.checkAccess();
        formatters = (Formatter[])MailHandler.copyOf(formatters, formatters.length, Formatter[].class);
        for (int i2 = 0; i2 < formatters.length; ++i2) {
            if (formatters[i2] != null) continue;
            throw new NullPointerException(MailHandler.atIndexMsg(i2));
        }
        MailHandler i2 = this;
        synchronized (i2) {
            if (this.attachmentFormatters.length != formatters.length) {
                throw MailHandler.attachmentMismatch(this.attachmentFormatters.length, formatters.length);
            }
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.attachmentNames = formatters;
        }
    }

    public final synchronized Formatter getSubject() {
        return this.subjectFormatter;
    }

    public final void setSubject(String subject) {
        if (subject == null) {
            this.checkAccess();
            throw new NullPointerException();
        }
        this.setSubject(new TailNameFormatter(subject));
    }

    public final void setSubject(Formatter format) {
        this.checkAccess();
        if (format == null) {
            throw new NullPointerException();
        }
        MailHandler mailHandler = this;
        synchronized (mailHandler) {
            if (this.isWriting) {
                throw new IllegalStateException();
            }
            this.subjectFormatter = format;
        }
    }

    @Override
    protected void reportError(String msg, Exception ex, int code) {
        if (msg != null) {
            super.reportError(Level.SEVERE.getName() + ": " + msg, ex, code);
        } else {
            super.reportError(null, ex, code);
        }
    }

    final void checkAccess() {
        if (this.sealed) {
            LogManagerProperties.getLogManager().checkAccess();
        }
    }

    final String contentTypeOf(String head) {
        if (!MailHandler.isEmpty(head)) {
            int MAX_CHARS = 25;
            if (head.length() > 25) {
                head = head.substring(0, 25);
            }
            try {
                String encoding = this.getEncodingName();
                ByteArrayInputStream in = new ByteArrayInputStream(head.getBytes(encoding));
                assert (in.markSupported());
                return URLConnection.guessContentTypeFromStream(in);
            }
            catch (IOException IOE) {
                this.reportError(IOE.getMessage(), (Exception)IOE, 5);
            }
        }
        return null;
    }

    final boolean isMissingContent(Message msg, Throwable t) {
        block4 : {
            for (Throwable cause = t.getCause(); cause != null; cause = cause.getCause()) {
                t = cause;
            }
            try {
                msg.writeTo(new ByteArrayOutputStream(1024));
            }
            catch (RuntimeException RE) {
                throw RE;
            }
            catch (Exception noContent) {
                String txt = noContent.getMessage();
                if (MailHandler.isEmpty(txt) || noContent.getClass() != t.getClass()) break block4;
                return txt.equals(t.getMessage());
            }
        }
        return false;
    }

    private void reportError(Message msg, Exception ex, int code) {
        try {
            super.reportError(this.toRawString(msg), ex, code);
        }
        catch (MessagingException rawMe) {
            this.reportError(this.toMsgString(rawMe), ex, code);
        }
        catch (IOException rawIo) {
            this.reportError(this.toMsgString(rawIo), ex, code);
        }
    }

    private String getContentType(String name) {
        assert (Thread.holdsLock(this));
        String type = this.contentTypes.getContentType(name);
        if ("application/octet-stream".equalsIgnoreCase(type)) {
            return null;
        }
        return type;
    }

    private String getEncodingName() {
        String encoding = this.getEncoding();
        if (encoding == null) {
            encoding = MimeUtility.getDefaultJavaCharset();
        }
        return encoding;
    }

    private void setContent(MimeBodyPart part, CharSequence buf, String type) throws MessagingException {
        String encoding = this.getEncodingName();
        if (type != null && !"text/plain".equalsIgnoreCase(type)) {
            type = this.contentWithEncoding(type, encoding);
            try {
                ByteArrayDataSource source = new ByteArrayDataSource(buf.toString(), type);
                part.setDataHandler(new DataHandler(source));
            }
            catch (IOException IOE) {
                this.reportError(IOE.getMessage(), (Exception)IOE, 5);
                part.setText(buf.toString(), encoding);
            }
        } else {
            part.setText(buf.toString(), MimeUtility.mimeCharset(encoding));
        }
    }

    private String contentWithEncoding(String type, String encoding) {
        assert (encoding != null);
        try {
            ContentType ct = new ContentType(type);
            ct.setParameter("charset", MimeUtility.mimeCharset(encoding));
            encoding = ct.toString();
            if (!MailHandler.isEmpty(encoding)) {
                type = encoding;
            }
        }
        catch (MessagingException ME) {
            this.reportError(type, (Exception)ME, 5);
        }
        return type;
    }

    private synchronized void setCapacity0(int newCapacity) {
        if (newCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (this.isWriting) {
            throw new IllegalStateException();
        }
        this.capacity = this.capacity < 0 ? - newCapacity : newCapacity;
    }

    private synchronized Filter[] readOnlyAttachmentFilters() {
        return this.attachmentFilters;
    }

    private static Formatter[] emptyFormatterArray() {
        return EMPTY_FORMATTERS;
    }

    private static Filter[] emptyFilterArray() {
        return EMPTY_FILTERS;
    }

    private boolean fixUpAttachmentNames() {
        assert (Thread.holdsLock(this));
        boolean fixed = false;
        int current = this.attachmentNames.length;
        int expect = this.attachmentFormatters.length;
        if (current != expect) {
            this.attachmentNames = MailHandler.copyOf(this.attachmentNames, expect);
            boolean bl = fixed = current != 0;
        }
        if (expect == 0) {
            this.attachmentNames = MailHandler.emptyFormatterArray();
            assert (this.attachmentNames.length == 0);
        } else {
            for (int i = 0; i < expect; ++i) {
                if (this.attachmentNames[i] != null) continue;
                this.attachmentNames[i] = new TailNameFormatter(this.toString(this.attachmentFormatters[i]));
            }
        }
        return fixed;
    }

    private boolean fixUpAttachmentFilters() {
        assert (Thread.holdsLock(this));
        boolean fixed = false;
        int current = this.attachmentFilters.length;
        int expect = this.attachmentFormatters.length;
        if (current != expect) {
            this.attachmentFilters = MailHandler.copyOf(this.attachmentFilters, expect);
            fixed = current != 0;
            Filter body = super.getFilter();
            if (body != null) {
                for (int i = current; i < expect; ++i) {
                    this.attachmentFilters[i] = body;
                }
            }
        }
        if (expect == 0) {
            this.attachmentFilters = MailHandler.emptyFilterArray();
            assert (this.attachmentFilters.length == 0);
        }
        return fixed;
    }

    private static <T> T[] copyOf(T[] a, int len) {
        return MailHandler.copyOf(a, len, a.getClass());
    }

    private static <T, U> T[] copyOf(U[] a, int len, Class<? extends T[]> type) {
        Object[] copy = (Object[])Array.newInstance(type.getComponentType(), len);
        System.arraycopy(a, 0, copy, 0, Math.min(len, a.length));
        return copy;
    }

    private void reset() {
        assert (Thread.holdsLock(this));
        if (this.size < this.data.length) {
            Arrays.fill(this.data, 0, this.size, null);
        } else {
            Arrays.fill(this.data, null);
        }
        this.size = 0;
    }

    private void grow() {
        assert (Thread.holdsLock(this));
        int len = this.data.length;
        int newCapacity = len + (len >> 1) + 1;
        if (newCapacity > this.capacity || newCapacity < len) {
            newCapacity = this.capacity;
        }
        assert (len != this.capacity);
        this.data = MailHandler.copyOf(this.data, newCapacity);
    }

    private synchronized void init(Properties props) {
        LogManager manager = LogManagerProperties.getLogManager();
        String p = this.getClass().getName();
        this.mailProps = new Properties();
        this.contentTypes = FileTypeMap.getDefaultFileTypeMap();
        this.initErrorManager(manager, p);
        this.initLevel(manager, p);
        this.initFilter(manager, p);
        this.initCapacity(manager, p);
        this.initAuthenticator(manager, p);
        this.initEncoding(manager, p);
        this.initFormatter(manager, p);
        this.initComparator(manager, p);
        this.initPushLevel(manager, p);
        this.initPushFilter(manager, p);
        this.initSubject(manager, p);
        this.initAttachmentFormaters(manager, p);
        this.initAttachmentFilters(manager, p);
        this.initAttachmentNames(manager, p);
        if (props == null && manager.getProperty(p.concat(".verify")) != null) {
            this.verifySettings(this.initSession());
        }
        this.intern();
    }

    private void intern() {
        assert (Thread.holdsLock(this));
        try {
            Object canidate;
            Object result;
            HashMap<Object, Object> seen = new HashMap<Object, Object>();
            try {
                this.intern(seen, super.getErrorManager());
            }
            catch (SecurityException se) {
                this.reportError(se.getMessage(), (Exception)se, 4);
            }
            try {
                canidate = super.getFilter();
                result = this.intern(seen, canidate);
                if (result != canidate) {
                    super.setFilter((Filter)Filter.class.cast(result));
                }
                if ((result = this.intern(seen, canidate = super.getFormatter())) != canidate) {
                    super.setFormatter((Formatter)Formatter.class.cast(result));
                }
            }
            catch (SecurityException se) {
                this.reportError(se.getMessage(), (Exception)se, 4);
            }
            canidate = this.subjectFormatter;
            result = this.intern(seen, canidate);
            if (result != canidate) {
                this.subjectFormatter = (Formatter)Formatter.class.cast(result);
            }
            if ((result = this.intern(seen, canidate = this.pushFilter)) != canidate) {
                this.pushFilter = (Filter)Filter.class.cast(result);
            }
            for (int i = 0; i < this.attachmentFormatters.length; ++i) {
                canidate = this.attachmentFormatters[i];
                result = this.intern(seen, canidate);
                if (result != canidate) {
                    this.attachmentFormatters[i] = (Formatter)Formatter.class.cast(result);
                }
                if ((result = this.intern(seen, canidate = this.attachmentFilters[i])) != canidate) {
                    this.attachmentFilters[i] = (Filter)Filter.class.cast(result);
                }
                if ((result = this.intern(seen, canidate = this.attachmentNames[i])) == canidate) continue;
                this.attachmentNames[i] = (Formatter)Formatter.class.cast(result);
            }
        }
        catch (Exception skip) {
            this.reportError(skip.getMessage(), skip, 4);
        }
    }

    private Object intern(Map<Object, Object> m, Object o) throws Exception {
        Object use;
        if (o == null) {
            return null;
        }
        Object key = o.getClass().getName().equals(TailNameFormatter.class.getName()) ? o : o.getClass().getConstructor(new Class[0]).newInstance(new Object[0]);
        if (key.getClass() == o.getClass()) {
            Object found = m.get(key);
            if (found == null) {
                boolean right = key.equals(o);
                boolean left = o.equals(key);
                if (right && left) {
                    found = m.put(o, o);
                    if (found != null) {
                        this.reportNonDiscriminating(key, found);
                        found = m.remove(key);
                        if (found != o) {
                            this.reportNonDiscriminating(key, found);
                            m.clear();
                        }
                    }
                } else if (right != left) {
                    this.reportNonSymmetric(o, key);
                }
                use = o;
            } else if (o.getClass() == found.getClass()) {
                use = found;
            } else {
                this.reportNonDiscriminating(o, found);
                use = o;
            }
        } else {
            use = o;
        }
        return use;
    }

    private static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    private static boolean hasValue(String name) {
        return !MailHandler.isEmpty(name) && !"null".equalsIgnoreCase(name);
    }

    private void initAttachmentFilters(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        assert (this.attachmentFormatters != null);
        String list = manager.getProperty(p.concat(".attachment.filters"));
        if (!MailHandler.isEmpty(list)) {
            String[] names = list.split(",");
            Filter[] a = new Filter[names.length];
            for (int i = 0; i < a.length; ++i) {
                names[i] = names[i].trim();
                if ("null".equalsIgnoreCase(names[i])) continue;
                try {
                    a[i] = LogManagerProperties.newFilter(names[i]);
                    continue;
                }
                catch (SecurityException SE) {
                    throw SE;
                }
                catch (Exception E) {
                    this.reportError(E.getMessage(), E, 4);
                }
            }
            this.attachmentFilters = a;
            if (this.fixUpAttachmentFilters()) {
                this.reportError("Attachment filters.", (Exception)MailHandler.attachmentMismatch("Length mismatch."), 4);
            }
        } else {
            this.attachmentFilters = MailHandler.emptyFilterArray();
            this.fixUpAttachmentFilters();
        }
    }

    private void initAttachmentFormaters(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String list = manager.getProperty(p.concat(".attachment.formatters"));
        if (!MailHandler.isEmpty(list)) {
            String[] names = list.split(",");
            Formatter[] a = names.length == 0 ? MailHandler.emptyFormatterArray() : new Formatter[names.length];
            for (int i = 0; i < a.length; ++i) {
                names[i] = names[i].trim();
                if (!"null".equalsIgnoreCase(names[i])) {
                    try {
                        a[i] = LogManagerProperties.newFormatter(names[i]);
                        if (!(a[i] instanceof TailNameFormatter)) continue;
                        ClassNotFoundException CNFE = new ClassNotFoundException(a[i].toString());
                        this.reportError("Attachment formatter.", (Exception)CNFE, 4);
                        a[i] = new SimpleFormatter();
                        continue;
                    }
                    catch (SecurityException SE) {
                        throw SE;
                    }
                    catch (Exception E) {
                        this.reportError(E.getMessage(), E, 4);
                        a[i] = new SimpleFormatter();
                        continue;
                    }
                }
                NullPointerException NPE = new NullPointerException(MailHandler.atIndexMsg(i));
                this.reportError("Attachment formatter.", (Exception)NPE, 4);
                a[i] = new SimpleFormatter();
            }
            this.attachmentFormatters = a;
        } else {
            this.attachmentFormatters = MailHandler.emptyFormatterArray();
        }
    }

    private void initAttachmentNames(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        assert (this.attachmentFormatters != null);
        String list = manager.getProperty(p.concat(".attachment.names"));
        if (!MailHandler.isEmpty(list)) {
            String[] names = list.split(",");
            Formatter[] a = new Formatter[names.length];
            for (int i = 0; i < a.length; ++i) {
                names[i] = names[i].trim();
                if (!"null".equalsIgnoreCase(names[i])) {
                    try {
                        try {
                            a[i] = LogManagerProperties.newFormatter(names[i]);
                        }
                        catch (ClassNotFoundException literal) {
                            a[i] = new TailNameFormatter(names[i]);
                        }
                        catch (ClassCastException literal) {
                            a[i] = new TailNameFormatter(names[i]);
                        }
                        continue;
                    }
                    catch (SecurityException SE) {
                        throw SE;
                    }
                    catch (Exception E) {
                        this.reportError(E.getMessage(), E, 4);
                        continue;
                    }
                }
                NullPointerException NPE = new NullPointerException(MailHandler.atIndexMsg(i));
                this.reportError("Attachment names.", (Exception)NPE, 4);
            }
            this.attachmentNames = a;
            if (this.fixUpAttachmentNames()) {
                this.reportError("Attachment names.", (Exception)MailHandler.attachmentMismatch("Length mismatch."), 4);
            }
        } else {
            this.attachmentNames = MailHandler.emptyFormatterArray();
            this.fixUpAttachmentNames();
        }
    }

    private void initAuthenticator(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String name = manager.getProperty(p.concat(".authenticator"));
        if (MailHandler.hasValue(name)) {
            try {
                this.auth = LogManagerProperties.newAuthenticator(name);
            }
            catch (SecurityException SE) {
                throw SE;
            }
            catch (ClassNotFoundException literalAuth) {
                this.auth = new DefaultAuthenticator(name);
            }
            catch (ClassCastException literalAuth) {
                this.auth = new DefaultAuthenticator(name);
            }
            catch (Exception E) {
                this.reportError(E.getMessage(), E, 4);
            }
        }
    }

    private void initLevel(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        try {
            String val = manager.getProperty(p.concat(".level"));
            if (val != null) {
                super.setLevel(Level.parse(val));
            } else {
                super.setLevel(Level.WARNING);
            }
        }
        catch (SecurityException SE) {
            throw SE;
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 4);
            try {
                super.setLevel(Level.WARNING);
            }
            catch (RuntimeException fail) {
                this.reportError(fail.getMessage(), (Exception)fail, 4);
            }
        }
    }

    private void initFilter(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        try {
            String name = manager.getProperty(p.concat(".filter"));
            if (MailHandler.hasValue(name)) {
                super.setFilter(LogManagerProperties.newFilter(name));
            }
        }
        catch (SecurityException SE) {
            throw SE;
        }
        catch (Exception E) {
            this.reportError(E.getMessage(), E, 4);
        }
    }

    private void initCapacity(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        int DEFAULT_CAPACITY = 1000;
        try {
            String value = manager.getProperty(p.concat(".capacity"));
            if (value != null) {
                this.setCapacity0(Integer.parseInt(value));
            } else {
                this.setCapacity0(1000);
            }
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 4);
        }
        if (this.capacity <= 0) {
            this.capacity = 1000;
        }
        this.data = new LogRecord[1];
    }

    private void initEncoding(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        try {
            super.setEncoding(manager.getProperty(p.concat(".encoding")));
        }
        catch (SecurityException SE) {
            throw SE;
        }
        catch (UnsupportedEncodingException UEE) {
            this.reportError(UEE.getMessage(), (Exception)UEE, 4);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 4);
        }
    }

    private void initErrorManager(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String name = manager.getProperty(p.concat(".errorManager"));
        if (name != null) {
            try {
                ErrorManager em = LogManagerProperties.newErrorManager(name);
                super.setErrorManager(em);
            }
            catch (SecurityException SE) {
                throw SE;
            }
            catch (Exception E) {
                this.reportError(E.getMessage(), E, 4);
            }
        }
    }

    private void initFormatter(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String name = manager.getProperty(p.concat(".formatter"));
        if (MailHandler.hasValue(name)) {
            try {
                Formatter formatter = LogManagerProperties.newFormatter(name);
                assert (formatter != null);
                if (!(formatter instanceof TailNameFormatter)) {
                    super.setFormatter(formatter);
                }
                super.setFormatter(new SimpleFormatter());
            }
            catch (SecurityException SE) {
                throw SE;
            }
            catch (Exception E) {
                this.reportError(E.getMessage(), E, 4);
                try {
                    super.setFormatter(new SimpleFormatter());
                }
                catch (RuntimeException fail) {
                    this.reportError(fail.getMessage(), (Exception)fail, 4);
                }
            }
        } else {
            super.setFormatter(new SimpleFormatter());
        }
    }

    private void initComparator(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String name = manager.getProperty(p.concat(".comparator"));
        String reverse = manager.getProperty(p.concat(".comparator.reverse"));
        try {
            if (MailHandler.hasValue(name)) {
                this.comparator = LogManagerProperties.newComparator(name);
                if (Boolean.parseBoolean(reverse)) {
                    assert (this.comparator != null);
                    this.comparator = LogManagerProperties.reverseOrder(this.comparator);
                }
            } else if (!MailHandler.isEmpty(reverse)) {
                throw new IllegalArgumentException("No comparator to reverse.");
            }
        }
        catch (SecurityException SE) {
            throw SE;
        }
        catch (Exception E) {
            this.reportError(E.getMessage(), E, 4);
        }
    }

    private void initPushLevel(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        try {
            String val = manager.getProperty(p.concat(".pushLevel"));
            if (val != null) {
                this.pushLevel = Level.parse(val);
            }
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 4);
        }
        if (this.pushLevel == null) {
            this.pushLevel = Level.OFF;
        }
    }

    private void initPushFilter(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String name = manager.getProperty(p.concat(".pushFilter"));
        if (MailHandler.hasValue(name)) {
            try {
                this.pushFilter = LogManagerProperties.newFilter(name);
            }
            catch (SecurityException SE) {
                throw SE;
            }
            catch (Exception E) {
                this.reportError(E.getMessage(), E, 4);
            }
        }
    }

    private void initSubject(LogManager manager, String p) {
        assert (Thread.holdsLock(this));
        String name = manager.getProperty(p.concat(".subject"));
        if (MailHandler.hasValue(name)) {
            try {
                this.subjectFormatter = LogManagerProperties.newFormatter(name);
            }
            catch (SecurityException SE) {
                throw SE;
            }
            catch (ClassNotFoundException literalSubject) {
                this.subjectFormatter = new TailNameFormatter(name);
            }
            catch (ClassCastException literalSubject) {
                this.subjectFormatter = new TailNameFormatter(name);
            }
            catch (Exception E) {
                this.subjectFormatter = new TailNameFormatter(name);
                this.reportError(E.getMessage(), E, 4);
            }
        } else if (name != null) {
            this.subjectFormatter = new TailNameFormatter(name);
        }
        if (this.subjectFormatter == null) {
            this.subjectFormatter = new TailNameFormatter("");
        }
    }

    private boolean isAttachmentLoggable(LogRecord record) {
        Filter[] filters = this.readOnlyAttachmentFilters();
        for (int i = 0; i < filters.length; ++i) {
            Filter f = filters[i];
            if (f != null && !f.isLoggable(record)) continue;
            return true;
        }
        return false;
    }

    private boolean isPushable(LogRecord record) {
        assert (Thread.holdsLock(this));
        int value = this.getPushLevel().intValue();
        if (value == offValue || record.getLevel().intValue() < value) {
            return false;
        }
        Filter filter = this.getPushFilter();
        return filter == null || filter.isLoggable(record);
    }

    private void push(boolean priority, int code) {
        if (this.tryMutex()) {
            try {
                Message msg = this.writeLogRecords(code);
                if (msg != null) {
                    this.send(msg, priority, code);
                }
                Object var5_4 = null;
                this.releaseMutex();
            }
            catch (Throwable var4_6) {
                Object var5_5 = null;
                this.releaseMutex();
                throw var4_6;
            }
        } else {
            this.reportUnPublishedError(null);
        }
    }

    private void send(Message msg, boolean priority, int code) {
        try {
            this.envelopeFor(msg, priority);
            Transport.send(msg);
        }
        catch (Exception E) {
            this.reportError(msg, E, code);
        }
    }

    private void sort() {
        assert (Thread.holdsLock(this));
        if (this.comparator != null) {
            try {
                if (this.size != 1) {
                    Arrays.sort(this.data, 0, this.size, this.comparator);
                } else {
                    this.comparator.compare(this.data[0], this.data[0]);
                }
            }
            catch (RuntimeException RE) {
                this.reportError(RE.getMessage(), (Exception)RE, 5);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private synchronized Message writeLogRecords(int code) {
        if (this.size == 0) return null;
        if (this.isWriting) {
            return null;
        }
        this.isWriting = true;
        try {
            MimeMessage i4;
            try {
                this.sort();
                if (this.session == null) {
                    this.initSession();
                }
                MimeMessage msg = new MimeMessage(this.session);
                msg.setDescription(this.descriptionFrom(this.comparator, this.pushLevel, this.pushFilter));
                MimeBodyPart[] parts = new MimeBodyPart[this.attachmentFormatters.length];
                StringBuilder[] buffers = new StringBuilder[parts.length];
                String contentType = null;
                StringBuilder buf = null;
                this.appendSubject(msg, this.head(this.subjectFormatter));
                MimeBodyPart body = this.createBodyPart();
                Formatter bodyFormat = this.getFormatter();
                Filter bodyFilter = this.getFilter();
                Locale lastLocale = null;
                for (int ix = 0; ix < this.size; ++ix) {
                    boolean formatted = false;
                    LogRecord r = this.data[ix];
                    this.data[ix] = null;
                    Locale locale = this.localeFor(r);
                    this.appendSubject(msg, this.format(this.subjectFormatter, r));
                    if (bodyFilter == null || bodyFilter.isLoggable(r)) {
                        if (buf == null) {
                            buf = new StringBuilder();
                            String head = this.head(bodyFormat);
                            buf.append(head);
                            contentType = this.contentTypeOf(head);
                        }
                        formatted = true;
                        buf.append(this.format(bodyFormat, r));
                        if (locale != null && !locale.equals(lastLocale)) {
                            this.appendContentLang(body, locale);
                        }
                    }
                    for (int i2 = 0; i2 < parts.length; ++i2) {
                        Filter af = this.attachmentFilters[i2];
                        if (af != null && !af.isLoggable(r)) continue;
                        if (parts[i2] == null) {
                            parts[i2] = this.createBodyPart(i2);
                            buffers[i2] = new StringBuilder();
                            buffers[i2].append(this.head(this.attachmentFormatters[i2]));
                            this.appendFileName(parts[i2], this.head(this.attachmentNames[i2]));
                        }
                        formatted = true;
                        this.appendFileName(parts[i2], this.format(this.attachmentNames[i2], r));
                        buffers[i2].append(this.format(this.attachmentFormatters[i2], r));
                        if (locale == null || locale.equals(lastLocale)) continue;
                        this.appendContentLang(parts[i2], locale);
                    }
                    if (formatted) {
                        if (locale != null && !locale.equals(lastLocale)) {
                            this.appendContentLang(msg, locale);
                        }
                    } else {
                        this.reportFilterError(r);
                    }
                    lastLocale = locale;
                }
                this.size = 0;
                for (int i3 = parts.length - 1; i3 >= 0; --i3) {
                    if (parts[i3] == null) continue;
                    this.appendFileName(parts[i3], this.tail(this.attachmentNames[i3], "err"));
                    buffers[i3].append(this.tail(this.attachmentFormatters[i3], ""));
                    if (buffers[i3].length() > 0) {
                        String name = parts[i3].getFileName();
                        if (MailHandler.isEmpty(name)) {
                            name = this.toString(this.attachmentFormatters[i3]);
                            parts[i3].setFileName(name);
                        }
                        this.setContent(parts[i3], buffers[i3], this.getContentType(name));
                    } else {
                        this.setIncompleteCopy(msg);
                        parts[i3] = null;
                    }
                    buffers[i3] = null;
                }
                if (buf != null) {
                    buf.append(this.tail(bodyFormat, ""));
                } else {
                    buf = new StringBuilder(0);
                }
                this.appendSubject(msg, this.tail(this.subjectFormatter, ""));
                MimeMultipart multipart = new MimeMultipart();
                String altType = this.getContentType(bodyFormat.getClass().getName());
                this.setContent(body, buf, altType == null ? contentType : altType);
                multipart.addBodyPart(body);
                for (int i4 = 0; i4 < parts.length; ++i4) {
                    if (parts[i4] == null) continue;
                    multipart.addBodyPart(parts[i4]);
                }
                msg.setContent(multipart);
                i4 = msg;
                Object var18_26 = null;
            }
            catch (RuntimeException re) {
                this.reportError(re.getMessage(), (Exception)re, code);
                Object var18_27 = null;
                this.isWriting = false;
                if (this.size <= 0) return null;
                this.reset();
                return null;
            }
            catch (Exception e) {
                this.reportError(e.getMessage(), e, code);
                Object var18_28 = null;
                this.isWriting = false;
                if (this.size <= 0) return null;
                this.reset();
                return null;
            }
            this.isWriting = false;
            if (this.size <= 0) return i4;
            this.reset();
            return i4;
        }
        catch (Throwable var17_30) {
            Object var18_29 = null;
            this.isWriting = false;
            if (this.size <= 0) throw var17_30;
            this.reset();
            throw var17_30;
        }
    }

    private void verifySettings(Session session) {
        if (session != null) {
            Properties props = session.getProperties();
            String check = props.put("verify", "");
            if (check instanceof String) {
                String value = check;
                if (MailHandler.hasValue(value)) {
                    this.verifySettings0(session, value);
                }
            } else if (check != null) {
                this.verifySettings0(session, check.getClass().toString());
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void verifySettings0(Session session, String verify) {
        block53 : {
            if (!MailHandler.$assertionsDisabled && verify == null) {
                throw new AssertionError((Object)null);
            }
            if (!("local".equals(verify) || "remote".equals(verify) || "limited".equals(verify) || "resolve".equals(verify))) {
                this.reportError("Verify must be 'limited', local', 'resolve' or 'remote'.", (Exception)new IllegalArgumentException(verify), 4);
                return;
            }
            abort = new MimeMessage(session);
            if (!"limited".equals(verify)) {
                msg = "Local address is " + InternetAddress.getLocalAddress(session) + '.';
                try {
                    Charset.forName(this.getEncodingName());
                }
                catch (RuntimeException RE) {
                    UEE = new UnsupportedEncodingException(RE.toString());
                    UEE.initCause(RE);
                    this.reportError(msg, (Exception)UEE, 5);
                }
            } else {
                msg = "Skipping local address check.";
            }
            RE = this;
            // MONITORENTER : RE
            this.appendSubject(abort, this.head(this.subjectFormatter));
            this.appendSubject(abort, this.tail(this.subjectFormatter, ""));
            // MONITOREXIT : RE
            this.setIncompleteCopy(abort);
            this.envelopeFor(abort, true);
            try {
                abort.saveChanges();
            }
            catch (MessagingException ME) {
                this.reportError(msg, (Exception)ME, 5);
            }
            try {
                block51 : {
                    all = abort.getAllRecipients();
                    if (all == null) {
                        all = new InternetAddress[]{};
                    }
                    try {
                        v0 = any = all.length != 0 ? all : abort.getFrom();
                        if (any != null && any.length != 0) {
                            t = session.getTransport(any[0]);
                            session.getProperty("mail.transport.protocol");
                            break block51;
                        }
                        me = new MessagingException("No recipient or from address.");
                        this.reportError(msg, (Exception)me, 4);
                        throw me;
                    }
                    catch (MessagingException protocol) {
                        try {
                            t = session.getTransport();
                        }
                        catch (MessagingException fail) {
                            throw MailHandler.attach(protocol, fail);
                        }
                    }
                }
                local = null;
                if ("remote".equals(verify)) {
                    block52 : {
                        closed = null;
                        t.connect();
                        try {
                            try {
                                if (t instanceof SMTPTransport) {
                                    local = ((SMTPTransport)t).getLocalHost();
                                }
                                t.sendMessage(abort, all);
                                var10_19 = null;
                                try {
                                    t.close();
                                }
                                catch (MessagingException ME) {
                                    closed = ME;
                                }
                            }
                            catch (Throwable var9_24) {
                                var10_20 = null;
                                ** try [egrp 8[TRYBLOCK] [8 : 433->441)] { 
lbl70: // 1 sources:
                                t.close();
                                throw var9_24;
lbl72: // 1 sources:
                                catch (MessagingException ME) {
                                    closed = ME;
                                }
                                throw var9_24;
                            }
                            this.reportUnexpectedSend(abort, verify, null);
                        }
                        catch (SendFailedException sfe) {
                            recip = sfe.getInvalidAddresses();
                            if (recip != null && recip.length != 0) {
                                this.fixUpContent(abort, verify, sfe);
                                this.reportError(abort, (Exception)sfe, 4);
                            }
                            if ((recip = sfe.getValidSentAddresses()) != null && recip.length != 0) {
                                this.reportUnexpectedSend(abort, verify, sfe);
                            }
                        }
                        catch (MessagingException ME) {
                            if (this.isMissingContent(abort, ME)) break block52;
                            this.fixUpContent(abort, verify, ME);
                            this.reportError(abort, (Exception)ME, 4);
                        }
                    }
                    if (closed != null) {
                        this.fixUpContent(abort, verify, closed);
                        this.reportError(abort, (Exception)closed, 3);
                    }
                } else {
                    protocol = t.getURLName().getProtocol();
                    session.getProperty("mail.host");
                    session.getProperty("mail.user");
                    session.getProperty("mail." + protocol + ".host");
                    session.getProperty("mail." + protocol + ".port");
                    session.getProperty("mail." + protocol + ".user");
                    local = session.getProperty("mail." + protocol + ".localhost");
                    if (MailHandler.isEmpty(local)) {
                        local = session.getProperty("mail." + protocol + ".localaddress");
                    }
                    if ("resolve".equals(verify)) {
                        try {
                            MailHandler.verifyHost(t.getURLName().getHost());
                        }
                        catch (IOException IOE) {
                            ME = new MessagingException(msg, IOE);
                            this.fixUpContent(abort, verify, ME);
                            this.reportError(abort, (Exception)ME, 4);
                        }
                        catch (RuntimeException RE) {
                            ME = new MessagingException(msg, RE);
                            this.fixUpContent(abort, verify, RE);
                            this.reportError(abort, (Exception)ME, 4);
                        }
                    }
                }
                if (!"limited".equals(verify)) {
                    try {
                        if (!"remote".equals(verify) && t instanceof SMTPTransport) {
                            local = ((SMTPTransport)t).getLocalHost();
                        }
                        MailHandler.verifyHost(local);
                    }
                    catch (IOException IOE) {
                        ME = new MessagingException(msg, IOE);
                        this.fixUpContent(abort, verify, ME);
                        this.reportError(abort, (Exception)ME, 4);
                    }
                    catch (RuntimeException RE) {
                        ME = new MessagingException(msg, RE);
                        this.fixUpContent(abort, verify, ME);
                        this.reportError(abort, (Exception)ME, 4);
                    }
                    try {
                        multipart = new MimeMultipart();
                        body = new MimeBodyPart();
                        body.setDisposition("inline");
                        body.setDescription(verify);
                        this.setAcceptLang(body);
                        this.setContent(body, "", "text/plain");
                        multipart.addBodyPart(body);
                        abort.setContent(multipart);
                        abort.saveChanges();
                        abort.writeTo(new ByteArrayOutputStream(1024));
                    }
                    catch (IOException IOE) {
                        ME = new MessagingException(msg, IOE);
                        this.fixUpContent(abort, verify, ME);
                        this.reportError(abort, (Exception)ME, 5);
                    }
                }
                if (all.length == 0) throw new MessagingException("No recipient addresses.");
                MailHandler.verifyAddresses(all);
                from = abort.getFrom();
                sender = abort.getSender();
                if (sender instanceof InternetAddress) {
                    ((InternetAddress)sender).validate();
                }
                if (abort.getHeader("From", ",") != null && from.length != 0) {
                    MailHandler.verifyAddresses(from);
                    i = 0;
                    break block53;
                }
                if (sender == null) {
                    ME = new MessagingException("No from or sender address.");
                    throw new MessagingException(msg, ME);
                }
                do {
                    MailHandler.verifyAddresses(abort.getReplyTo());
                    return;
                    break;
                } while (true);
            }
            catch (MessagingException ME) {
                this.fixUpContent(abort, verify, ME);
                this.reportError(abort, (Exception)ME, 4);
                return;
            }
            catch (RuntimeException RE) {
                this.fixUpContent(abort, verify, RE);
                this.reportError(abort, (Exception)RE, 4);
            }
            return;
        }
        do {
            if (i >= from.length) ** continue;
            if (from[i].equals(sender)) {
                ME = new MessagingException("Sender address '" + sender + "' equals from address.");
                throw new MessagingException(msg, ME);
            }
            ++i;
        } while (true);
    }

    private static InetAddress verifyHost(String host) throws IOException {
        InetAddress a = MailHandler.isEmpty(host) ? InetAddress.getLocalHost() : InetAddress.getByName(host);
        if (a.getCanonicalHostName().length() == 0) {
            throw new UnknownHostException();
        }
        return a;
    }

    private static void verifyAddresses(Address[] all) throws AddressException {
        if (all != null) {
            for (int i = 0; i < all.length; ++i) {
                Address a = all[i];
                if (!(a instanceof InternetAddress)) continue;
                ((InternetAddress)a).validate();
            }
        }
    }

    private void reportUnexpectedSend(MimeMessage msg, String verify, Exception cause) {
        MessagingException write = new MessagingException("An empty message was sent.", cause);
        this.fixUpContent(msg, verify, write);
        this.reportError(msg, (Exception)write, 4);
    }

    private void fixUpContent(MimeMessage msg, String verify, Throwable t) {
        try {
            String subjectType;
            String msgDesc;
            MimeBodyPart body;
            MailHandler mailHandler = this;
            synchronized (mailHandler) {
                body = this.createBodyPart();
                msgDesc = this.descriptionFrom(this.comparator, this.pushLevel, this.pushFilter);
                subjectType = this.getClassId(this.subjectFormatter);
            }
            body.setDescription("Formatted using " + (t == null ? Throwable.class.getName() : t.getClass().getName()) + ", filtered with " + verify + ", and named by " + subjectType + '.');
            this.setContent(body, this.toMsgString(t), "text/plain");
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);
            msg.setContent(multipart);
            msg.setDescription(msgDesc);
            this.setAcceptLang(msg);
            msg.saveChanges();
        }
        catch (MessagingException ME) {
            this.reportError("Unable to create body.", (Exception)ME, 4);
        }
        catch (RuntimeException RE) {
            this.reportError("Unable to create body.", (Exception)RE, 4);
        }
    }

    private Session fixUpSession() {
        Session settings;
        assert (Thread.holdsLock(this));
        if (this.mailProps.getProperty("verify") != null) {
            settings = this.initSession();
            assert (settings == this.session);
        } else {
            this.session = null;
            settings = null;
        }
        return settings;
    }

    private Session initSession() {
        assert (Thread.holdsLock(this));
        String p = this.getClass().getName();
        LogManagerProperties proxy = new LogManagerProperties(this.mailProps, p);
        this.session = Session.getInstance(proxy, this.auth);
        return this.session;
    }

    private void envelopeFor(Message msg, boolean priority) {
        this.setAcceptLang(msg);
        this.setFrom(msg);
        if (!this.setRecipient(msg, "mail.to", Message.RecipientType.TO)) {
            this.setDefaultRecipient(msg, Message.RecipientType.TO);
        }
        this.setRecipient(msg, "mail.cc", Message.RecipientType.CC);
        this.setRecipient(msg, "mail.bcc", Message.RecipientType.BCC);
        this.setReplyTo(msg);
        this.setSender(msg);
        this.setMailer(msg);
        this.setAutoSubmitted(msg);
        if (priority) {
            this.setPriority(msg);
        }
        try {
            msg.setSentDate(new Date());
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private MimeBodyPart createBodyPart() throws MessagingException {
        assert (Thread.holdsLock(this));
        MimeBodyPart part = new MimeBodyPart();
        part.setDisposition("inline");
        part.setDescription(this.descriptionFrom(this.getFormatter(), this.getFilter(), this.subjectFormatter));
        this.setAcceptLang(part);
        return part;
    }

    private MimeBodyPart createBodyPart(int index) throws MessagingException {
        assert (Thread.holdsLock(this));
        MimeBodyPart part = new MimeBodyPart();
        part.setDisposition("attachment");
        part.setDescription(this.descriptionFrom(this.attachmentFormatters[index], this.attachmentFilters[index], this.attachmentNames[index]));
        this.setAcceptLang(part);
        return part;
    }

    private String descriptionFrom(Comparator c, Level l, Filter f) {
        return "Sorted using " + (c == null ? "no comparator" : c.getClass().getName()) + ", pushed when " + l.getName() + ", and " + (f == null ? "no push filter" : f.getClass().getName()) + '.';
    }

    private String descriptionFrom(Formatter f, Filter filter, Formatter name) {
        return "Formatted using " + this.getClassId(f) + ", filtered with " + (filter == null ? "no filter" : filter.getClass().getName()) + ", and named by " + this.getClassId(name) + '.';
    }

    private String getClassId(Formatter f) {
        if (f instanceof TailNameFormatter) {
            return String.class.getName();
        }
        return f.getClass().getName();
    }

    private String toString(Formatter f) {
        String name = f.toString();
        if (!MailHandler.isEmpty(name)) {
            return name;
        }
        return this.getClassId(f);
    }

    private void appendFileName(Part part, String chunk) {
        if (chunk != null) {
            if (chunk.length() > 0) {
                this.appendFileName0(part, chunk);
            }
        } else {
            this.reportNullError(5);
        }
    }

    private void appendFileName0(Part part, String chunk) {
        try {
            String old = part.getFileName();
            part.setFileName(old != null ? old.concat(chunk) : chunk);
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void appendSubject(Message msg, String chunk) {
        if (chunk != null) {
            if (chunk.length() > 0) {
                this.appendSubject0(msg, chunk);
            }
        } else {
            this.reportNullError(5);
        }
    }

    private void appendSubject0(Message msg, String chunk) {
        try {
            String encoding = this.getEncodingName();
            String old = msg.getSubject();
            assert (msg instanceof MimeMessage);
            ((MimeMessage)msg).setSubject(old != null ? old.concat(chunk) : chunk, MimeUtility.mimeCharset(encoding));
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private Locale localeFor(LogRecord r) {
        Locale l;
        ResourceBundle rb = r.getResourceBundle();
        if (rb != null) {
            l = rb.getLocale();
            if (l == null || MailHandler.isEmpty(l.getLanguage())) {
                l = Locale.getDefault();
            }
        } else {
            l = null;
        }
        return l;
    }

    private void appendContentLang(MimePart p, Locale l) {
        try {
            String lang = LogManagerProperties.toLanguageTag(l);
            if (lang.length() != 0) {
                String header = p.getHeader("Content-Language", null);
                if (MailHandler.isEmpty(header)) {
                    p.setHeader("Content-Language", lang);
                } else if (!header.equalsIgnoreCase(lang)) {
                    lang = ",".concat(lang);
                    int idx = 0;
                    while ((idx = header.indexOf(lang, idx)) > -1 && (idx += lang.length()) != header.length() && header.charAt(idx) != ',') {
                    }
                    if (idx < 0) {
                        int len = header.lastIndexOf("\r\n\t");
                        len = len < 0 ? 20 + header.length() : header.length() - len + 8;
                        header = len + lang.length() > 76 ? header.concat("\r\n\t".concat(lang)) : header.concat(lang);
                        p.setHeader("Content-Language", header);
                    }
                }
            }
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void setAcceptLang(Part p) {
        try {
            String lang = LogManagerProperties.toLanguageTag(Locale.getDefault());
            if (lang.length() != 0) {
                p.setHeader("Accept-Language", lang);
            }
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void reportFilterError(LogRecord record) {
        assert (Thread.holdsLock(this));
        SimpleFormatter f = new SimpleFormatter();
        String msg = "Log record " + record.getSequenceNumber() + " was filtered from all message parts.  " + this.head(f) + this.format(f, record) + this.tail(f, "");
        String txt = this.getFilter() + ", " + Arrays.asList(this.readOnlyAttachmentFilters());
        this.reportError(msg, (Exception)new IllegalArgumentException(txt), 5);
    }

    private void reportNonSymmetric(Object o, Object found) {
        this.reportError("Non symmetric equals implementation.", (Exception)new IllegalArgumentException(o.getClass().getName() + " is not equal to " + found.getClass().getName()), 4);
    }

    private void reportNonDiscriminating(Object o, Object found) {
        this.reportError("Non discriminating equals implementation.", (Exception)new IllegalArgumentException(o.getClass().getName() + " should not be equal to " + found.getClass().getName()), 4);
    }

    private void reportNullError(int code) {
        this.reportError("null", (Exception)new NullPointerException(), code);
    }

    private String head(Formatter f) {
        try {
            return f.getHead(this);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 5);
            return "";
        }
    }

    private String format(Formatter f, LogRecord r) {
        try {
            return f.format(r);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 5);
            return "";
        }
    }

    private String tail(Formatter f, String def) {
        try {
            return f.getTail(this);
        }
        catch (RuntimeException RE) {
            this.reportError(RE.getMessage(), (Exception)RE, 5);
            return def;
        }
    }

    private void setMailer(Message msg) {
        try {
            String value;
            reference mail = MailHandler.class;
            Class k = this.getClass();
            if (k == mail) {
                value = mail.getName();
            } else {
                try {
                    value = MimeUtility.encodeText(k.getName());
                }
                catch (UnsupportedEncodingException E) {
                    this.reportError(E.getMessage(), (Exception)E, 5);
                    value = k.getName().replaceAll("[^\\x00-\\x7F]", "\u001a");
                }
                value = MimeUtility.fold(10, mail.getName() + " using the " + value + " extension.");
            }
            msg.setHeader("X-Mailer", value);
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void setPriority(Message msg) {
        try {
            msg.setHeader("Importance", "High");
            msg.setHeader("Priority", "urgent");
            msg.setHeader("X-Priority", "2");
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void setIncompleteCopy(Message msg) {
        try {
            msg.setHeader("Incomplete-Copy", "");
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void setAutoSubmitted(Message msg) {
        try {
            msg.setHeader("auto-submitted", "auto-generated");
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void setFrom(Message msg) {
        block5 : {
            String from = msg.getSession().getProperty("mail.from");
            if (from != null) {
                try {
                    Address[] address = InternetAddress.parse(from, false);
                    if (address.length <= 0) break block5;
                    if (address.length == 1) {
                        msg.setFrom(address[0]);
                        break block5;
                    }
                    msg.addFrom(address);
                }
                catch (MessagingException ME) {
                    this.reportError(ME.getMessage(), (Exception)ME, 5);
                    this.setDefaultFrom(msg);
                }
            } else {
                this.setDefaultFrom(msg);
            }
        }
    }

    private void setDefaultFrom(Message msg) {
        try {
            msg.setFrom();
        }
        catch (MessagingException ME) {
            this.reportError(ME.getMessage(), (Exception)ME, 5);
        }
    }

    private void setDefaultRecipient(Message msg, Message.RecipientType type) {
        block5 : {
            try {
                InternetAddress a = InternetAddress.getLocalAddress(msg.getSession());
                if (a != null) {
                    msg.setRecipient(type, a);
                    break block5;
                }
                MimeMessage m = new MimeMessage(msg.getSession());
                m.setFrom();
                Address[] from = m.getFrom();
                if (from.length > 0) {
                    msg.setRecipients(type, from);
                    break block5;
                }
                throw new MessagingException("No local address.");
            }
            catch (MessagingException ME) {
                this.reportError("Unable to compute a default recipient.", (Exception)ME, 5);
            }
            catch (RuntimeException RE) {
                this.reportError("Unable to compute a default recipient.", (Exception)RE, 5);
            }
        }
    }

    private void setReplyTo(Message msg) {
        String reply = msg.getSession().getProperty("mail.reply.to");
        if (!MailHandler.isEmpty(reply)) {
            try {
                Address[] address = InternetAddress.parse(reply, false);
                if (address.length > 0) {
                    msg.setReplyTo(address);
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), (Exception)ME, 5);
            }
        }
    }

    private void setSender(Message msg) {
        assert (msg instanceof MimeMessage);
        String sender = msg.getSession().getProperty("mail.sender");
        if (!MailHandler.isEmpty(sender)) {
            try {
                Address[] address = InternetAddress.parse(sender, false);
                if (address.length > 0) {
                    ((MimeMessage)msg).setSender(address[0]);
                    if (address.length > 1) {
                        this.reportError("Ignoring other senders.", (Exception)this.tooManyAddresses(address, 1), 5);
                    }
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), (Exception)ME, 5);
            }
        }
    }

    private AddressException tooManyAddresses(Address[] address, int offset) {
        List<Address> l = Arrays.asList(address).subList(offset, address.length);
        return new AddressException(l.toString());
    }

    private boolean setRecipient(Message msg, String key, Message.RecipientType type) {
        boolean containsKey;
        String value = msg.getSession().getProperty(key);
        boolean bl = containsKey = value != null;
        if (!MailHandler.isEmpty(value)) {
            try {
                Address[] address = InternetAddress.parse(value, false);
                if (address.length > 0) {
                    msg.setRecipients(type, address);
                }
            }
            catch (MessagingException ME) {
                this.reportError(ME.getMessage(), (Exception)ME, 5);
            }
        }
        return containsKey;
    }

    private String toRawString(Message msg) throws MessagingException, IOException {
        if (msg != null) {
            int nbytes = Math.max(msg.getSize() + 1024, 1024);
            ByteArrayOutputStream out = new ByteArrayOutputStream(nbytes);
            msg.writeTo(out);
            return out.toString("US-ASCII");
        }
        return null;
    }

    private String toMsgString(Throwable t) {
        if (t == null) {
            return "null";
        }
        String encoding = this.getEncodingName();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter((OutputStream)out, encoding));
            pw.println(t.getMessage());
            t.printStackTrace(pw);
            pw.flush();
            pw.close();
            return out.toString(encoding);
        }
        catch (IOException badMimeCharset) {
            return t.toString() + ' ' + badMimeCharset.toString();
        }
    }

    private Object getAndSetContextClassLoader() {
        try {
            return AccessController.doPrivileged(GET_AND_SET_CCL);
        }
        catch (SecurityException ignore) {
            return GET_AND_SET_CCL;
        }
    }

    private void setContextClassLoader(Object ccl) {
        if (ccl == null || ccl instanceof ClassLoader) {
            AccessController.doPrivileged(new GetAndSetContext(ccl));
        }
    }

    private static RuntimeException attachmentMismatch(String msg) {
        return new IndexOutOfBoundsException(msg);
    }

    private static RuntimeException attachmentMismatch(int expected, int found) {
        return MailHandler.attachmentMismatch("Attachments mismatched, expected " + expected + " but given " + found + '.');
    }

    private static MessagingException attach(MessagingException required, Exception optional) {
        MessagingException head;
        if (optional != null && !required.setNextException(optional) && optional instanceof MessagingException && (head = (MessagingException)optional).setNextException(required)) {
            return head;
        }
        return required;
    }

    private static String atIndexMsg(int i) {
        return "At index: " + i + '.';
    }

    private static final class TailNameFormatter
    extends Formatter {
        private final String name;

        TailNameFormatter(String name) {
            assert (name != null);
            this.name = name;
        }

        public final String format(LogRecord record) {
            return "";
        }

        public final String getTail(Handler h) {
            return this.name;
        }

        public final boolean equals(Object o) {
            if (o instanceof TailNameFormatter) {
                return this.name.equals(((TailNameFormatter)o).name);
            }
            return false;
        }

        public final int hashCode() {
            return this.getClass().hashCode() + this.name.hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class GetAndSetContext
    implements PrivilegedAction<Object> {
        private final Object source;

        GetAndSetContext(Object source) {
            this.source = source;
        }

        @Override
        public final Object run() {
            Thread current = Thread.currentThread();
            ClassLoader ccl = current.getContextClassLoader();
            ClassLoader loader = this.source == null ? null : (this.source instanceof ClassLoader ? (ClassLoader)this.source : (this.source instanceof Class ? ((Class)this.source).getClassLoader() : this.source.getClass().getClassLoader()));
            if (ccl != loader) {
                current.setContextClassLoader(loader);
                return ccl;
            }
            return this;
        }
    }

    private static final class DefaultAuthenticator
    extends Authenticator {
        private final String pass;

        DefaultAuthenticator(String pass) {
            assert (pass != null);
            this.pass = pass;
        }

        protected final PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.getDefaultUserName(), this.pass);
        }
    }

}

