/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap;

import com.sun.mail.iap.BadCommandException;
import com.sun.mail.iap.CommandFailedException;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.Literal;
import com.sun.mail.iap.Protocol;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.iap.ResponseHandler;
import com.sun.mail.imap.ACL;
import com.sun.mail.imap.AppendUID;
import com.sun.mail.imap.DefaultFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.MessageCache;
import com.sun.mail.imap.MessageLiteral;
import com.sun.mail.imap.Rights;
import com.sun.mail.imap.SortTerm;
import com.sun.mail.imap.Utility;
import com.sun.mail.imap.protocol.FetchItem;
import com.sun.mail.imap.protocol.FetchResponse;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.IMAPResponse;
import com.sun.mail.imap.protocol.Item;
import com.sun.mail.imap.protocol.ListInfo;
import com.sun.mail.imap.protocol.MailboxInfo;
import com.sun.mail.imap.protocol.MessageSet;
import com.sun.mail.imap.protocol.Status;
import com.sun.mail.imap.protocol.UID;
import com.sun.mail.imap.protocol.UIDSet;
import com.sun.mail.util.MailLogger;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.logging.Level;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Quota;
import javax.mail.ReadOnlyFolderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.UIDFolder;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchException;
import javax.mail.search.SearchTerm;

public class IMAPFolder
extends Folder
implements UIDFolder,
ResponseHandler {
    protected String fullName;
    protected String name;
    protected int type;
    protected char separator;
    protected Flags availableFlags;
    protected Flags permanentFlags;
    protected volatile boolean exists;
    protected boolean isNamespace = false;
    protected volatile String[] attributes;
    protected volatile IMAPProtocol protocol;
    protected MessageCache messageCache;
    protected final Object messageCacheLock = new Object();
    protected Hashtable uidTable;
    protected static final char UNKNOWN_SEPARATOR = '\uffff';
    private volatile boolean opened = false;
    private boolean reallyClosed = true;
    private static final int RUNNING = 0;
    private static final int IDLE = 1;
    private static final int ABORTING = 2;
    private int idleState = 0;
    private volatile int total = -1;
    private volatile int recent = -1;
    private int realTotal = -1;
    private long uidvalidity = -1;
    private long uidnext = -1;
    private boolean doExpungeNotification = true;
    private Status cachedStatus = null;
    private long cachedStatusTime = 0;
    private boolean hasMessageCountListener = false;
    protected MailLogger logger;
    private MailLogger connectionPoolLogger;

    protected IMAPFolder(String fullName, char separator, IMAPStore store, Boolean isNamespace) {
        int i;
        super(store);
        if (fullName == null) {
            throw new NullPointerException("Folder name is null");
        }
        this.fullName = fullName;
        this.separator = separator;
        this.logger = new MailLogger(this.getClass(), "DEBUG IMAP", store.getSession());
        this.connectionPoolLogger = store.getConnectionPoolLogger();
        this.isNamespace = false;
        if (separator != '\uffff' && separator != '\u0000' && (i = this.fullName.indexOf(separator)) > 0 && i == this.fullName.length() - 1) {
            this.fullName = this.fullName.substring(0, i);
            this.isNamespace = true;
        }
        if (isNamespace != null) {
            this.isNamespace = isNamespace;
        }
    }

    protected IMAPFolder(ListInfo li, IMAPStore store) {
        this(li.name, li.separator, store, null);
        if (li.hasInferiors) {
            this.type |= 2;
        }
        if (li.canOpen) {
            this.type |= 1;
        }
        this.exists = true;
        this.attributes = li.attrs;
    }

    protected void checkExists() throws MessagingException {
        if (!this.exists && !this.exists()) {
            throw new FolderNotFoundException(this, this.fullName + " not found");
        }
    }

    protected void checkClosed() {
        if (this.opened) {
            throw new IllegalStateException("This operation is not allowed on an open folder");
        }
    }

    protected void checkOpened() throws FolderClosedException {
        assert (Thread.holdsLock(this));
        if (!this.opened) {
            if (this.reallyClosed) {
                throw new IllegalStateException("This operation is not allowed on a closed folder");
            }
            throw new FolderClosedException(this, "Lost folder connection to server");
        }
    }

    protected void checkRange(int msgno) throws MessagingException {
        if (msgno < 1) {
            throw new IndexOutOfBoundsException("message number < 1");
        }
        if (msgno <= this.total) {
            return;
        }
        Object object = this.messageCacheLock;
        synchronized (object) {
            try {
                this.keepConnectionAlive(false);
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        if (msgno > this.total) {
            throw new IndexOutOfBoundsException("" + msgno + " > " + this.total);
        }
    }

    private void checkFlags(Flags flags) throws MessagingException {
        assert (Thread.holdsLock(this));
        if (this.mode != 2) {
            throw new IllegalStateException("Cannot change flags on READ_ONLY folder: " + this.fullName);
        }
    }

    public synchronized String getName() {
        if (this.name == null) {
            try {
                this.name = this.fullName.substring(this.fullName.lastIndexOf(this.getSeparator()) + 1);
            }
            catch (MessagingException mex) {
                // empty catch block
            }
        }
        return this.name;
    }

    public synchronized String getFullName() {
        return this.fullName;
    }

    public synchronized Folder getParent() throws MessagingException {
        char c = this.getSeparator();
        int index = this.fullName.lastIndexOf(c);
        if (index != -1) {
            return ((IMAPStore)this.store).newIMAPFolder(this.fullName.substring(0, index), c);
        }
        return new DefaultFolder((IMAPStore)this.store);
    }

    public synchronized boolean exists() throws MessagingException {
        ListInfo[] li = null;
        final String lname = this.isNamespace && this.separator != '\u0000' ? this.fullName + this.separator : this.fullName;
        li = (ListInfo[])this.doCommand(new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                return p.list("", lname);
            }
        });
        if (li != null) {
            int i = this.findName(li, lname);
            this.fullName = li[i].name;
            this.separator = li[i].separator;
            int len = this.fullName.length();
            if (this.separator != '\u0000' && len > 0 && this.fullName.charAt(len - 1) == this.separator) {
                this.fullName = this.fullName.substring(0, len - 1);
            }
            this.type = 0;
            if (li[i].hasInferiors) {
                this.type |= 2;
            }
            if (li[i].canOpen) {
                this.type |= 1;
            }
            this.exists = true;
            this.attributes = li[i].attrs;
        } else {
            this.exists = this.opened;
            this.attributes = null;
        }
        return this.exists;
    }

    private int findName(ListInfo[] li, String lname) {
        int i;
        for (i = 0; i < li.length && !li[i].name.equals(lname); ++i) {
        }
        if (i >= li.length) {
            i = 0;
        }
        return i;
    }

    public Folder[] list(String pattern) throws MessagingException {
        return this.doList(pattern, false);
    }

    public Folder[] listSubscribed(String pattern) throws MessagingException {
        return this.doList(pattern, true);
    }

    private synchronized Folder[] doList(final String pattern, final boolean subscribed) throws MessagingException {
        this.checkExists();
        if (this.attributes != null && !this.isDirectory()) {
            return new Folder[0];
        }
        final char c = this.getSeparator();
        ListInfo[] li = (ListInfo[])this.doCommandIgnoreFailure(new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                if (subscribed) {
                    return p.lsub("", IMAPFolder.this.fullName + c + pattern);
                }
                return p.list("", IMAPFolder.this.fullName + c + pattern);
            }
        });
        if (li == null) {
            return new Folder[0];
        }
        int start = 0;
        if (li.length > 0 && li[0].name.equals(this.fullName + c)) {
            start = 1;
        }
        Folder[] folders = new IMAPFolder[li.length - start];
        IMAPStore st = (IMAPStore)this.store;
        for (int i = start; i < li.length; ++i) {
            folders[i - start] = st.newIMAPFolder(li[i]);
        }
        return folders;
    }

    public synchronized char getSeparator() throws MessagingException {
        if (this.separator == '\uffff') {
            ListInfo[] li = null;
            li = (ListInfo[])this.doCommand(new ProtocolCommand(){

                public Object doCommand(IMAPProtocol p) throws ProtocolException {
                    if (p.isREV1()) {
                        return p.list(IMAPFolder.this.fullName, "");
                    }
                    return p.list("", IMAPFolder.this.fullName);
                }
            });
            this.separator = li != null ? li[0].separator : 47;
        }
        return this.separator;
    }

    public synchronized int getType() throws MessagingException {
        if (this.opened) {
            if (this.attributes == null) {
                this.exists();
            }
        } else {
            this.checkExists();
        }
        return this.type;
    }

    public synchronized boolean isSubscribed() {
        ListInfo[] li = null;
        final String lname = this.isNamespace && this.separator != '\u0000' ? this.fullName + this.separator : this.fullName;
        try {
            li = (ListInfo[])this.doProtocolCommand(new ProtocolCommand(){

                public Object doCommand(IMAPProtocol p) throws ProtocolException {
                    return p.lsub("", lname);
                }
            });
        }
        catch (ProtocolException pex) {
            // empty catch block
        }
        if (li != null) {
            int i = this.findName(li, lname);
            return li[i].canOpen;
        }
        return false;
    }

    public synchronized void setSubscribed(final boolean subscribe) throws MessagingException {
        this.doCommandIgnoreFailure(new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                if (subscribe) {
                    p.subscribe(IMAPFolder.this.fullName);
                } else {
                    p.unsubscribe(IMAPFolder.this.fullName);
                }
                return null;
            }
        });
    }

    public synchronized boolean create(final int type) throws MessagingException {
        char sep;
        Object ret;
        char c = '\u0000';
        if ((type & 1) == 0) {
            c = this.getSeparator();
        }
        if ((ret = this.doCommandIgnoreFailure(new ProtocolCommand(sep = c){
            final /* synthetic */ char val$sep;

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                if ((type & 1) == 0) {
                    p.create(IMAPFolder.this.fullName + this.val$sep);
                } else {
                    ListInfo[] li;
                    p.create(IMAPFolder.this.fullName);
                    if ((type & 2) != 0 && (li = p.list("", IMAPFolder.this.fullName)) != null && !li[0].hasInferiors) {
                        p.delete(IMAPFolder.this.fullName);
                        throw new ProtocolException("Unsupported type");
                    }
                }
                return Boolean.TRUE;
            }
        })) == null) {
            return false;
        }
        boolean retb = this.exists();
        if (retb) {
            this.notifyFolderListeners(1);
        }
        return retb;
    }

    public synchronized boolean hasNewMessages() throws MessagingException {
        if (this.opened) {
            Object object = this.messageCacheLock;
            synchronized (object) {
                try {
                    this.keepConnectionAlive(true);
                }
                catch (ConnectionException cex) {
                    throw new FolderClosedException(this, cex.getMessage());
                }
                catch (ProtocolException pex) {
                    throw new MessagingException(pex.getMessage(), pex);
                }
                return this.recent > 0;
            }
        }
        ListInfo[] li = null;
        final String lname = this.isNamespace && this.separator != '\u0000' ? this.fullName + this.separator : this.fullName;
        li = (ListInfo[])this.doCommandIgnoreFailure(new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                return p.list("", lname);
            }
        });
        if (li == null) {
            throw new FolderNotFoundException(this, this.fullName + " not found");
        }
        int i = this.findName(li, lname);
        if (li[i].changeState == 1) {
            return true;
        }
        if (li[i].changeState == 2) {
            return false;
        }
        try {
            Status status = this.getStatus();
            if (status.recent > 0) {
                return true;
            }
            return false;
        }
        catch (BadCommandException bex) {
            return false;
        }
        catch (ConnectionException cex) {
            throw new StoreClosedException(this.store, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    public synchronized Folder getFolder(String name) throws MessagingException {
        if (this.attributes != null && !this.isDirectory()) {
            throw new MessagingException("Cannot contain subfolders");
        }
        char c = this.getSeparator();
        return ((IMAPStore)this.store).newIMAPFolder(this.fullName + c + name, c);
    }

    public synchronized boolean delete(boolean recurse) throws MessagingException {
        Object ret;
        this.checkClosed();
        if (recurse) {
            Folder[] f = this.list();
            for (int i = 0; i < f.length; ++i) {
                f[i].delete(recurse);
            }
        }
        if ((ret = this.doCommandIgnoreFailure(new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                p.delete(IMAPFolder.this.fullName);
                return Boolean.TRUE;
            }
        })) == null) {
            return false;
        }
        this.exists = false;
        this.attributes = null;
        this.notifyFolderListeners(2);
        return true;
    }

    public synchronized boolean renameTo(final Folder f) throws MessagingException {
        this.checkClosed();
        this.checkExists();
        if (f.getStore() != this.store) {
            throw new MessagingException("Can't rename across Stores");
        }
        Object ret = this.doCommandIgnoreFailure(new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                p.rename(IMAPFolder.this.fullName, f.getFullName());
                return Boolean.TRUE;
            }
        });
        if (ret == null) {
            return false;
        }
        this.exists = false;
        this.attributes = null;
        this.notifyFolderRenamedListeners(f);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public synchronized void open(int mode) throws MessagingException {
        this.checkClosed();
        MailboxInfo mi = null;
        this.protocol = ((IMAPStore)this.store).getProtocol(this);
        Object object = this.messageCacheLock;
        synchronized (object) {
            this.protocol.addResponseHandler(this);
            try {
                mi = mode == 1 ? this.protocol.examine(this.fullName) : this.protocol.select(this.fullName);
            }
            catch (CommandFailedException cex) {
                try {
                    this.checkExists();
                    if ((this.type & 1) == 0) {
                        throw new MessagingException("folder cannot contain messages");
                    }
                    throw new MessagingException(cex.getMessage(), cex);
                }
                catch (Throwable var5_7) {
                    Object var6_10 = null;
                    this.exists = false;
                    this.attributes = null;
                    this.type = 0;
                    this.releaseProtocol(true);
                    throw var5_7;
                }
            }
            catch (ProtocolException pex) {
                try {
                    try {
                        this.protocol.logout();
                    }
                    catch (ProtocolException pex2) {
                        Object var8_12 = null;
                        this.releaseProtocol(false);
                        throw new MessagingException(pex.getMessage(), pex);
                    }
                    Object var8_11 = null;
                    this.releaseProtocol(false);
                    throw new MessagingException(pex.getMessage(), pex);
                }
                catch (Throwable var7_14) {
                    Object var8_13 = null;
                    this.releaseProtocol(false);
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
            if (!(mi.mode == mode || mode == 2 && mi.mode == 1 && ((IMAPStore)this.store).allowReadOnlySelect())) {
                try {
                    try {
                        this.protocol.close();
                        this.releaseProtocol(true);
                    }
                    catch (ProtocolException pex) {
                        try {
                            try {
                                this.protocol.logout();
                            }
                            catch (ProtocolException pex2) {
                                Object var10_19 = null;
                                this.releaseProtocol(false);
                            }
                            Object var10_18 = null;
                            this.releaseProtocol(false);
                        }
                        catch (Throwable var9_21) {
                            Object var10_20 = null;
                            this.releaseProtocol(false);
                            throw var9_21;
                        }
                        Object var12_16 = null;
                        throw new ReadOnlyFolderException(this, "Cannot open in desired mode");
                    }
                    Object var12_15 = null;
                    throw new ReadOnlyFolderException(this, "Cannot open in desired mode");
                }
                catch (Throwable var11_22) {
                    Object var12_17 = null;
                    throw new ReadOnlyFolderException(this, "Cannot open in desired mode");
                }
            }
            this.opened = true;
            this.reallyClosed = false;
            this.mode = mi.mode;
            this.availableFlags = mi.availableFlags;
            this.permanentFlags = mi.permanentFlags;
            this.total = this.realTotal = mi.total;
            this.recent = mi.recent;
            this.uidvalidity = mi.uidvalidity;
            this.uidnext = mi.uidnext;
            this.messageCache = new MessageCache(this, (IMAPStore)this.store, this.total);
        }
        this.exists = true;
        this.attributes = null;
        this.type = 1;
        this.notifyConnectionListeners(1);
    }

    public synchronized void fetch(Message[] msgs, FetchProfile fp) throws MessagingException {
        this.checkOpened();
        StringBuffer command = new StringBuffer();
        boolean first = true;
        boolean allHeaders = false;
        if (fp.contains(FetchProfile.Item.ENVELOPE)) {
            command.append(this.getEnvelopeCommand());
            first = false;
        }
        if (fp.contains(FetchProfile.Item.FLAGS)) {
            command.append(first ? "FLAGS" : " FLAGS");
            first = false;
        }
        if (fp.contains(FetchProfile.Item.CONTENT_INFO)) {
            command.append(first ? "BODYSTRUCTURE" : " BODYSTRUCTURE");
            first = false;
        }
        if (fp.contains(UIDFolder.FetchProfileItem.UID)) {
            command.append(first ? "UID" : " UID");
            first = false;
        }
        if (fp.contains(FetchProfileItem.HEADERS)) {
            allHeaders = true;
            if (this.protocol.isREV1()) {
                command.append(first ? "BODY.PEEK[HEADER]" : " BODY.PEEK[HEADER]");
            } else {
                command.append(first ? "RFC822.HEADER" : " RFC822.HEADER");
            }
            first = false;
        }
        if (fp.contains(FetchProfile.Item.SIZE) || fp.contains(FetchProfileItem.SIZE)) {
            command.append(first ? "RFC822.SIZE" : " RFC822.SIZE");
            first = false;
        }
        String[] hdrs = null;
        if (!allHeaders && (hdrs = fp.getHeaderNames()).length > 0) {
            if (!first) {
                command.append(" ");
            }
            command.append(this.createHeaderCommand(hdrs));
        }
        FetchItem[] fitems = this.protocol.getFetchItems();
        for (int i = 0; i < fitems.length; ++i) {
            if (!fp.contains(fitems[i].getFetchProfileItem())) continue;
            if (command.length() != 0) {
                command.append(" ");
            }
            command.append(fitems[i].getName());
        }
        IMAPMessage.FetchProfileCondition condition = new IMAPMessage.FetchProfileCondition(fp, fitems);
        Object object = this.messageCacheLock;
        synchronized (object) {
            MessageSet[] msgsets = Utility.toMessageSet(msgs, condition);
            if (msgsets == null) {
                return;
            }
            Response[] r = null;
            Vector<Response> v = new Vector<Response>();
            try {
                r = this.getProtocol().fetch(msgsets, command.toString());
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this, cex.getMessage());
            }
            catch (CommandFailedException cfx) {
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
            if (r == null) {
                return;
            }
            for (int i2 = 0; i2 < r.length; ++i2) {
                if (r[i2] == null) continue;
                if (!(r[i2] instanceof FetchResponse)) {
                    v.addElement(r[i2]);
                    continue;
                }
                FetchResponse f = (FetchResponse)r[i2];
                IMAPMessage msg = this.getMessageBySeqNumber(f.getNumber());
                int count = f.getItemCount();
                boolean unsolicitedFlags = false;
                for (int j = 0; j < count; ++j) {
                    Item item = f.getItem(j);
                    if (item instanceof Flags && (!fp.contains(FetchProfile.Item.FLAGS) || msg == null)) {
                        unsolicitedFlags = true;
                        continue;
                    }
                    if (msg == null) continue;
                    msg.handleFetchItem(item, hdrs, allHeaders);
                }
                if (msg != null) {
                    msg.handleExtensionFetchItems(f.getExtensionItems());
                }
                if (!unsolicitedFlags) continue;
                v.addElement(f);
            }
            int size = v.size();
            if (size != 0) {
                Object[] responses = new Response[size];
                v.copyInto(responses);
                this.handleResponses((Response[])responses);
            }
        }
    }

    protected String getEnvelopeCommand() {
        return "ENVELOPE INTERNALDATE RFC822.SIZE";
    }

    protected IMAPMessage newIMAPMessage(int msgnum) {
        return new IMAPMessage(this, msgnum);
    }

    private String createHeaderCommand(String[] hdrs) {
        StringBuffer sb = this.protocol.isREV1() ? new StringBuffer("BODY.PEEK[HEADER.FIELDS (") : new StringBuffer("RFC822.HEADER.LINES (");
        for (int i = 0; i < hdrs.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(hdrs[i]);
        }
        if (this.protocol.isREV1()) {
            sb.append(")]");
        } else {
            sb.append(")");
        }
        return sb.toString();
    }

    public synchronized void setFlags(Message[] msgs, Flags flag, boolean value) throws MessagingException {
        this.checkOpened();
        this.checkFlags(flag);
        if (msgs.length == 0) {
            return;
        }
        Object object = this.messageCacheLock;
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                MessageSet[] ms = Utility.toMessageSet(msgs, null);
                if (ms == null) {
                    throw new MessageRemovedException("Messages have been removed");
                }
                p.storeFlags(ms, flag, value);
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
    }

    public synchronized void setFlags(int start, int end, Flags flag, boolean value) throws MessagingException {
        this.checkOpened();
        Message[] msgs = new Message[end - start + 1];
        int i = 0;
        for (int n = start; n <= end; ++n) {
            msgs[i++] = this.getMessage(n);
        }
        this.setFlags(msgs, flag, value);
    }

    public synchronized void setFlags(int[] msgnums, Flags flag, boolean value) throws MessagingException {
        this.checkOpened();
        Message[] msgs = new Message[msgnums.length];
        for (int i = 0; i < msgnums.length; ++i) {
            msgs[i] = this.getMessage(msgnums[i]);
        }
        this.setFlags(msgs, flag, value);
    }

    public synchronized void close(boolean expunge) throws MessagingException {
        this.close(expunge, false);
    }

    public synchronized void forceClose() throws MessagingException {
        this.close(false, true);
    }

    private void close(boolean expunge, boolean force) throws MessagingException {
        assert (Thread.holdsLock(this));
        Object object = this.messageCacheLock;
        synchronized (object) {
            if (!this.opened && this.reallyClosed) {
                throw new IllegalStateException("This operation is not allowed on a closed folder");
            }
            this.reallyClosed = true;
            if (!this.opened) {
                return;
            }
            try {
                block25 : {
                    try {
                        this.waitIfIdle();
                        if (force) {
                            this.logger.log(Level.FINE, "forcing folder {0} to close", this.fullName);
                            if (this.protocol != null) {
                                this.protocol.disconnect();
                            }
                            break block25;
                        }
                        if (((IMAPStore)this.store).isConnectionPoolFull()) {
                            this.logger.fine("pool is full, not adding an Authenticated connection");
                            if (expunge && this.protocol != null) {
                                this.protocol.close();
                            }
                            if (this.protocol != null) {
                                this.protocol.logout();
                            }
                            break block25;
                        }
                        if (!expunge && this.mode == 2) {
                            try {
                                if (this.protocol != null && this.protocol.hasCapability("UNSELECT")) {
                                    this.protocol.unselect();
                                } else if (this.protocol != null) {
                                    MailboxInfo mi = this.protocol.examine(this.fullName);
                                    if (this.protocol != null) {
                                        this.protocol.close();
                                    }
                                }
                                break block25;
                            }
                            catch (ProtocolException pex2) {
                                if (this.protocol != null) {
                                    this.protocol.disconnect();
                                }
                                break block25;
                            }
                        }
                        if (this.protocol == null) break block25;
                        this.protocol.close();
                    }
                    catch (ProtocolException pex) {
                        throw new MessagingException(pex.getMessage(), pex);
                    }
                }
                Object var6_7 = null;
                if (this.opened) {
                    this.cleanup(true);
                }
            }
            catch (Throwable var5_9) {
                Object var6_8 = null;
                if (this.opened) {
                    this.cleanup(true);
                }
                throw var5_9;
            }
        }
    }

    private void cleanup(boolean returnToPool) {
        assert (Thread.holdsLock(this.messageCacheLock));
        this.releaseProtocol(returnToPool);
        this.messageCache = null;
        this.uidTable = null;
        this.exists = false;
        this.attributes = null;
        this.opened = false;
        this.idleState = 0;
        this.notifyConnectionListeners(3);
    }

    public synchronized boolean isOpen() {
        Object object = this.messageCacheLock;
        synchronized (object) {
            if (this.opened) {
                try {
                    this.keepConnectionAlive(false);
                }
                catch (ProtocolException pex) {
                    // empty catch block
                }
            }
        }
        return this.opened;
    }

    public synchronized Flags getPermanentFlags() {
        if (this.permanentFlags == null) {
            return null;
        }
        return (Flags)this.permanentFlags.clone();
    }

    public synchronized int getMessageCount() throws MessagingException {
        if (!this.opened) {
            this.checkExists();
            try {
                Status status = this.getStatus();
                return status.total;
            }
            catch (BadCommandException bex) {
                IMAPProtocol p = null;
                try {
                    p = this.getStoreProtocol();
                    MailboxInfo minfo = p.examine(this.fullName);
                    p.close();
                    int n = minfo.total;
                    Object var6_12 = null;
                    this.releaseStoreProtocol(p);
                    return n;
                }
                catch (ProtocolException pex) {
                    try {
                        throw new MessagingException(pex.getMessage(), pex);
                    }
                    catch (Throwable var5_14) {
                        Object var6_13 = null;
                        this.releaseStoreProtocol(p);
                        throw var5_14;
                    }
                }
            }
            catch (ConnectionException cex) {
                throw new StoreClosedException(this.store, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        Object pex = this.messageCacheLock;
        synchronized (pex) {
            try {
                this.keepConnectionAlive(true);
                return this.total;
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
    }

    public synchronized int getNewMessageCount() throws MessagingException {
        if (!this.opened) {
            this.checkExists();
            try {
                Status status = this.getStatus();
                return status.recent;
            }
            catch (BadCommandException bex) {
                IMAPProtocol p = null;
                try {
                    p = this.getStoreProtocol();
                    MailboxInfo minfo = p.examine(this.fullName);
                    p.close();
                    int n = minfo.recent;
                    Object var6_12 = null;
                    this.releaseStoreProtocol(p);
                    return n;
                }
                catch (ProtocolException pex) {
                    try {
                        throw new MessagingException(pex.getMessage(), pex);
                    }
                    catch (Throwable var5_14) {
                        Object var6_13 = null;
                        this.releaseStoreProtocol(p);
                        throw var5_14;
                    }
                }
            }
            catch (ConnectionException cex) {
                throw new StoreClosedException(this.store, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        Object pex = this.messageCacheLock;
        synchronized (pex) {
            try {
                this.keepConnectionAlive(true);
                return this.recent;
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
    }

    public synchronized int getUnreadMessageCount() throws MessagingException {
        if (!this.opened) {
            this.checkExists();
            try {
                Status status = this.getStatus();
                return status.unseen;
            }
            catch (BadCommandException bex) {
                return -1;
            }
            catch (ConnectionException cex) {
                throw new StoreClosedException(this.store, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        Flags f = new Flags();
        f.add(Flags.Flag.SEEN);
        try {
            Object object = this.messageCacheLock;
            synchronized (object) {
                int[] matches = this.getProtocol().search(new FlagTerm(f, false));
                return matches.length;
            }
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    public synchronized int getDeletedMessageCount() throws MessagingException {
        if (!this.opened) {
            this.checkExists();
            return -1;
        }
        Flags f = new Flags();
        f.add(Flags.Flag.DELETED);
        try {
            Object object = this.messageCacheLock;
            synchronized (object) {
                int[] matches = this.getProtocol().search(new FlagTerm(f, true));
                return matches.length;
            }
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    private Status getStatus() throws ProtocolException {
        int statusCacheTimeout = ((IMAPStore)this.store).getStatusCacheTimeout();
        if (statusCacheTimeout > 0 && this.cachedStatus != null && System.currentTimeMillis() - this.cachedStatusTime < (long)statusCacheTimeout) {
            return this.cachedStatus;
        }
        IMAPProtocol p = null;
        try {
            p = this.getStoreProtocol();
            Status s = p.status(this.fullName, null);
            if (statusCacheTimeout > 0) {
                this.cachedStatus = s;
                this.cachedStatusTime = System.currentTimeMillis();
            }
            Status status = s;
            Object var6_5 = null;
            this.releaseStoreProtocol(p);
            return status;
        }
        catch (Throwable var5_7) {
            Object var6_6 = null;
            this.releaseStoreProtocol(p);
            throw var5_7;
        }
    }

    public synchronized Message getMessage(int msgnum) throws MessagingException {
        this.checkOpened();
        this.checkRange(msgnum);
        return this.messageCache.getMessage(msgnum);
    }

    public synchronized void appendMessages(Message[] msgs) throws MessagingException {
        this.checkExists();
        int maxsize = ((IMAPStore)this.store).getAppendBufferSize();
        for (int i = 0; i < msgs.length; ++i) {
            MessageLiteral mos;
            Message m = msgs[i];
            Date d = m.getReceivedDate();
            if (d == null) {
                d = m.getSentDate();
            }
            final Date dd = d;
            final Flags f = m.getFlags();
            try {
                mos = new MessageLiteral(m, m.getSize() > maxsize ? 0 : maxsize);
            }
            catch (IOException ex) {
                throw new MessagingException("IOException while appending messages", ex);
            }
            catch (MessageRemovedException mrex) {
                continue;
            }
            this.doCommand(new ProtocolCommand(){

                public Object doCommand(IMAPProtocol p) throws ProtocolException {
                    p.append(IMAPFolder.this.fullName, f, dd, mos);
                    return null;
                }
            });
        }
    }

    public synchronized AppendUID[] appendUIDMessages(Message[] msgs) throws MessagingException {
        this.checkExists();
        int maxsize = ((IMAPStore)this.store).getAppendBufferSize();
        AppendUID[] uids = new AppendUID[msgs.length];
        for (int i = 0; i < msgs.length; ++i) {
            AppendUID auid;
            MessageLiteral mos;
            Message m = msgs[i];
            try {
                mos = new MessageLiteral(m, m.getSize() > maxsize ? 0 : maxsize);
            }
            catch (IOException ex) {
                throw new MessagingException("IOException while appending messages", ex);
            }
            catch (MessageRemovedException mrex) {
                continue;
            }
            Date d = m.getReceivedDate();
            if (d == null) {
                d = m.getSentDate();
            }
            final Date dd = d;
            final Flags f = m.getFlags();
            uids[i] = auid = (AppendUID)this.doCommand(new ProtocolCommand(){

                public Object doCommand(IMAPProtocol p) throws ProtocolException {
                    return p.appenduid(IMAPFolder.this.fullName, f, dd, mos);
                }
            });
        }
        return uids;
    }

    public synchronized Message[] addMessages(Message[] msgs) throws MessagingException {
        this.checkOpened();
        Message[] rmsgs = new MimeMessage[msgs.length];
        AppendUID[] uids = this.appendUIDMessages(msgs);
        for (int i = 0; i < uids.length; ++i) {
            AppendUID auid = uids[i];
            if (auid == null || auid.uidvalidity != this.uidvalidity) continue;
            try {
                rmsgs[i] = this.getMessageByUID(auid.uid);
                continue;
            }
            catch (MessagingException mex) {
                // empty catch block
            }
        }
        return rmsgs;
    }

    public synchronized void copyMessages(Message[] msgs, Folder folder) throws MessagingException {
        this.checkOpened();
        if (msgs.length == 0) {
            return;
        }
        if (folder.getStore() == this.store) {
            Object object = this.messageCacheLock;
            synchronized (object) {
                try {
                    IMAPProtocol p = this.getProtocol();
                    MessageSet[] ms = Utility.toMessageSet(msgs, null);
                    if (ms == null) {
                        throw new MessageRemovedException("Messages have been removed");
                    }
                    p.copy(ms, folder.getFullName());
                }
                catch (CommandFailedException cfx) {
                    if (cfx.getMessage().indexOf("TRYCREATE") != -1) {
                        throw new FolderNotFoundException(folder, folder.getFullName() + " does not exist");
                    }
                    throw new MessagingException(cfx.getMessage(), cfx);
                }
                catch (ConnectionException cex) {
                    throw new FolderClosedException(this, cex.getMessage());
                }
                catch (ProtocolException pex) {
                    throw new MessagingException(pex.getMessage(), pex);
                }
            }
        }
        super.copyMessages(msgs, folder);
    }

    public synchronized Message[] expunge() throws MessagingException {
        return this.expunge(null);
    }

    public synchronized Message[] expunge(Message[] msgs) throws MessagingException {
        Message[] rmsgs;
        this.checkOpened();
        if (msgs != null) {
            FetchProfile fp = new FetchProfile();
            fp.add(UIDFolder.FetchProfileItem.UID);
            this.fetch(msgs, fp);
        }
        Object object = this.messageCacheLock;
        synchronized (object) {
            this.doExpungeNotification = false;
            try {
                block15 : {
                    try {
                        IMAPProtocol p = this.getProtocol();
                        if (msgs != null) {
                            p.uidexpunge(Utility.toUIDSet(msgs));
                            break block15;
                        }
                        p.expunge();
                    }
                    catch (CommandFailedException cfx) {
                        if (this.mode != 2) {
                            throw new IllegalStateException("Cannot expunge READ_ONLY folder: " + this.fullName);
                        }
                        throw new MessagingException(cfx.getMessage(), cfx);
                    }
                    catch (ConnectionException cex) {
                        throw new FolderClosedException(this, cex.getMessage());
                    }
                    catch (ProtocolException pex) {
                        throw new MessagingException(pex.getMessage(), pex);
                    }
                }
                Object var6_9 = null;
                this.doExpungeNotification = true;
            }
            catch (Throwable var5_12) {
                Object var6_10 = null;
                this.doExpungeNotification = true;
                throw var5_12;
            }
            rmsgs = msgs != null ? this.messageCache.removeExpungedMessages(msgs) : this.messageCache.removeExpungedMessages();
            if (this.uidTable != null) {
                for (int i = 0; i < rmsgs.length; ++i) {
                    IMAPMessage m = rmsgs[i];
                    long uid = m.getUID();
                    if (uid == -1) continue;
                    this.uidTable.remove(new Long(uid));
                }
            }
            this.total = this.messageCache.size();
        }
        if (rmsgs.length > 0) {
            this.notifyMessageRemovedListeners(true, rmsgs);
        }
        return rmsgs;
    }

    public synchronized Message[] search(SearchTerm term) throws MessagingException {
        this.checkOpened();
        try {
            Message[] matchMsgs = null;
            Object object = this.messageCacheLock;
            synchronized (object) {
                int[] matches = this.getProtocol().search(term);
                if (matches != null) {
                    matchMsgs = new IMAPMessage[matches.length];
                    for (int i = 0; i < matches.length; ++i) {
                        matchMsgs[i] = this.getMessageBySeqNumber(matches[i]);
                    }
                }
            }
            return matchMsgs;
        }
        catch (CommandFailedException cfx) {
            return super.search(term);
        }
        catch (SearchException sex) {
            return super.search(term);
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    public synchronized Message[] search(SearchTerm term, Message[] msgs) throws MessagingException {
        this.checkOpened();
        if (msgs.length == 0) {
            return msgs;
        }
        try {
            Message[] matchMsgs = null;
            Object object = this.messageCacheLock;
            synchronized (object) {
                IMAPProtocol p = this.getProtocol();
                MessageSet[] ms = Utility.toMessageSet(msgs, null);
                if (ms == null) {
                    throw new MessageRemovedException("Messages have been removed");
                }
                int[] matches = p.search(ms, term);
                if (matches != null) {
                    matchMsgs = new IMAPMessage[matches.length];
                    for (int i = 0; i < matches.length; ++i) {
                        matchMsgs[i] = this.getMessageBySeqNumber(matches[i]);
                    }
                }
            }
            return matchMsgs;
        }
        catch (CommandFailedException cfx) {
            return super.search(term, msgs);
        }
        catch (SearchException sex) {
            return super.search(term, msgs);
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    public synchronized Message[] getSortedMessages(SortTerm[] term) throws MessagingException {
        return this.getSortedMessages(term, null);
    }

    public synchronized Message[] getSortedMessages(SortTerm[] term, SearchTerm sterm) throws MessagingException {
        this.checkOpened();
        try {
            Message[] matchMsgs = null;
            Object object = this.messageCacheLock;
            synchronized (object) {
                int[] matches = this.getProtocol().sort(term, sterm);
                if (matches != null) {
                    matchMsgs = new IMAPMessage[matches.length];
                    for (int i = 0; i < matches.length; ++i) {
                        matchMsgs[i] = this.getMessageBySeqNumber(matches[i]);
                    }
                }
            }
            return matchMsgs;
        }
        catch (CommandFailedException cfx) {
            throw new MessagingException(cfx.getMessage(), cfx);
        }
        catch (SearchException sex) {
            throw new MessagingException(sex.getMessage(), sex);
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    public synchronized void addMessageCountListener(MessageCountListener l) {
        super.addMessageCountListener(l);
        this.hasMessageCountListener = true;
    }

    public synchronized long getUIDValidity() throws MessagingException {
        block7 : {
            if (this.opened) {
                return this.uidvalidity;
            }
            IMAPProtocol p = null;
            Status status = null;
            try {
                try {
                    p = this.getStoreProtocol();
                    String[] item = new String[]{"UIDVALIDITY"};
                    status = p.status(this.fullName, item);
                }
                catch (BadCommandException bex) {
                    throw new MessagingException("Cannot obtain UIDValidity", bex);
                }
                catch (ConnectionException cex) {
                    this.throwClosedException(cex);
                    Object var5_8 = null;
                    this.releaseStoreProtocol(p);
                    break block7;
                }
                catch (ProtocolException pex) {
                    throw new MessagingException(pex.getMessage(), pex);
                }
                Object var5_7 = null;
                this.releaseStoreProtocol(p);
            }
            catch (Throwable var4_10) {
                Object var5_9 = null;
                this.releaseStoreProtocol(p);
                throw var4_10;
            }
        }
        return status.uidvalidity;
    }

    public synchronized long getUIDNext() throws MessagingException {
        block7 : {
            if (this.opened) {
                return this.uidnext;
            }
            IMAPProtocol p = null;
            Status status = null;
            try {
                try {
                    p = this.getStoreProtocol();
                    String[] item = new String[]{"UIDNEXT"};
                    status = p.status(this.fullName, item);
                }
                catch (BadCommandException bex) {
                    throw new MessagingException("Cannot obtain UIDNext", bex);
                }
                catch (ConnectionException cex) {
                    this.throwClosedException(cex);
                    Object var5_8 = null;
                    this.releaseStoreProtocol(p);
                    break block7;
                }
                catch (ProtocolException pex) {
                    throw new MessagingException(pex.getMessage(), pex);
                }
                Object var5_7 = null;
                this.releaseStoreProtocol(p);
            }
            catch (Throwable var4_10) {
                Object var5_9 = null;
                this.releaseStoreProtocol(p);
                throw var4_10;
            }
        }
        return status.uidnext;
    }

    public synchronized Message getMessageByUID(long uid) throws MessagingException {
        IMAPMessage m;
        this.checkOpened();
        m = null;
        try {
            Object object = this.messageCacheLock;
            synchronized (object) {
                UID u;
                Long l = new Long(uid);
                if (this.uidTable != null) {
                    m = (IMAPMessage)this.uidTable.get(l);
                    if (m != null) {
                        return m;
                    }
                } else {
                    this.uidTable = new Hashtable();
                }
                if ((u = this.getProtocol().fetchSequenceNumber(uid)) != null && u.seqnum <= this.total) {
                    m = this.getMessageBySeqNumber(u.seqnum);
                    m.setUID(u.uid);
                    this.uidTable.put(l, m);
                }
            }
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
        return m;
    }

    public synchronized Message[] getMessagesByUID(long start, long end) throws MessagingException {
        Message[] msgs;
        this.checkOpened();
        try {
            Object object = this.messageCacheLock;
            synchronized (object) {
                if (this.uidTable == null) {
                    this.uidTable = new Hashtable();
                }
                UID[] ua = this.getProtocol().fetchSequenceNumbers(start, end);
                msgs = new Message[ua.length];
                for (int i = 0; i < ua.length; ++i) {
                    IMAPMessage m = this.getMessageBySeqNumber(ua[i].seqnum);
                    m.setUID(ua[i].uid);
                    msgs[i] = m;
                    this.uidTable.put(new Long(ua[i].uid), m);
                }
            }
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
        return msgs;
    }

    public synchronized Message[] getMessagesByUID(long[] uids) throws MessagingException {
        this.checkOpened();
        try {
            Object object = this.messageCacheLock;
            synchronized (object) {
                int i;
                long[] unavailUids = uids;
                if (this.uidTable != null) {
                    Vector<Long> v = new Vector<Long>();
                    for (i = 0; i < uids.length; ++i) {
                        Long l = new Long(uids[i]);
                        if (this.uidTable.containsKey(l)) continue;
                        v.addElement(l);
                    }
                    int vsize = v.size();
                    unavailUids = new long[vsize];
                    for (int i2 = 0; i2 < vsize; ++i2) {
                        unavailUids[i2] = (Long)v.elementAt(i2);
                    }
                } else {
                    this.uidTable = new Hashtable();
                }
                if (unavailUids.length > 0) {
                    UID[] ua = this.getProtocol().fetchSequenceNumbers(unavailUids);
                    for (i = 0; i < ua.length; ++i) {
                        IMAPMessage m = this.getMessageBySeqNumber(ua[i].seqnum);
                        m.setUID(ua[i].uid);
                        this.uidTable.put(new Long(ua[i].uid), m);
                    }
                }
                Message[] msgs = new Message[uids.length];
                for (int i3 = 0; i3 < uids.length; ++i3) {
                    msgs[i3] = (Message)this.uidTable.get(new Long(uids[i3]));
                }
                return msgs;
            }
        }
        catch (ConnectionException cex) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
    }

    public synchronized long getUID(Message message) throws MessagingException {
        if (message.getFolder() != this) {
            throw new NoSuchElementException("Message does not belong to this folder");
        }
        this.checkOpened();
        IMAPMessage m = (IMAPMessage)message;
        long uid = m.getUID();
        if (uid != -1) {
            return uid;
        }
        Object object = this.messageCacheLock;
        synchronized (object) {
            try {
                IMAPProtocol p = this.getProtocol();
                m.checkExpunged();
                UID u = p.fetchUID(m.getSequenceNumber());
                if (u != null) {
                    uid = u.uid;
                    m.setUID(uid);
                    if (this.uidTable == null) {
                        this.uidTable = new Hashtable();
                    }
                    this.uidTable.put(new Long(uid), m);
                }
            }
            catch (ConnectionException cex) {
                throw new FolderClosedException(this, cex.getMessage());
            }
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        return uid;
    }

    public Quota[] getQuota() throws MessagingException {
        return (Quota[])this.doOptionalCommand("QUOTA not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                return p.getQuotaRoot(IMAPFolder.this.fullName);
            }
        });
    }

    public void setQuota(final Quota quota) throws MessagingException {
        this.doOptionalCommand("QUOTA not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                p.setQuota(quota);
                return null;
            }
        });
    }

    public ACL[] getACL() throws MessagingException {
        return (ACL[])this.doOptionalCommand("ACL not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                return p.getACL(IMAPFolder.this.fullName);
            }
        });
    }

    public void addACL(ACL acl) throws MessagingException {
        this.setACL(acl, '\u0000');
    }

    public void removeACL(final String name) throws MessagingException {
        this.doOptionalCommand("ACL not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                p.deleteACL(IMAPFolder.this.fullName, name);
                return null;
            }
        });
    }

    public void addRights(ACL acl) throws MessagingException {
        this.setACL(acl, '+');
    }

    public void removeRights(ACL acl) throws MessagingException {
        this.setACL(acl, '-');
    }

    public Rights[] listRights(final String name) throws MessagingException {
        return (Rights[])this.doOptionalCommand("ACL not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                return p.listRights(IMAPFolder.this.fullName, name);
            }
        });
    }

    public Rights myRights() throws MessagingException {
        return (Rights)this.doOptionalCommand("ACL not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                return p.myRights(IMAPFolder.this.fullName);
            }
        });
    }

    private void setACL(final ACL acl, final char mod) throws MessagingException {
        this.doOptionalCommand("ACL not supported", new ProtocolCommand(){

            public Object doCommand(IMAPProtocol p) throws ProtocolException {
                p.setACL(IMAPFolder.this.fullName, mod, acl);
                return null;
            }
        });
    }

    public synchronized String[] getAttributes() throws MessagingException {
        this.checkExists();
        if (this.attributes == null) {
            this.exists();
        }
        return this.attributes == null ? new String[]{} : (String[])this.attributes.clone();
    }

    public void idle() throws MessagingException {
        this.idle(false);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void idle(boolean once) throws MessagingException {
        block16 : {
            assert (!Thread.holdsLock(this));
            IMAPFolder iMAPFolder = this;
            // MONITORENTER : iMAPFolder
            this.checkOpened();
            Object started = (Boolean)this.doOptionalCommand("IDLE not supported", new ProtocolCommand(){

                public Object doCommand(IMAPProtocol p) throws ProtocolException {
                    if (IMAPFolder.this.idleState == 0) {
                        p.idleStart();
                        IMAPFolder.this.idleState = 1;
                        return Boolean.TRUE;
                    }
                    try {
                        IMAPFolder.this.messageCacheLock.wait();
                    }
                    catch (InterruptedException ex) {
                        // empty catch block
                    }
                    return Boolean.FALSE;
                }
            });
            if (!started.booleanValue()) {
                // MONITOREXIT : iMAPFolder
                return;
            }
            // MONITOREXIT : iMAPFolder
            do {
                Response r = this.protocol.readIdleResponse();
                try {
                    block15 : {
                        started = this.messageCacheLock;
                        // MONITORENTER : started
                        try {
                            if (r != null && this.protocol != null && this.protocol.processIdleResponse(r)) break block15;
                            this.idleState = 0;
                            this.messageCacheLock.notifyAll();
                            // MONITOREXIT : started
                            break block16;
                        }
                        catch (ProtocolException pex) {
                            this.idleState = 0;
                            this.messageCacheLock.notifyAll();
                            throw pex;
                        }
                    }
                    if (once && this.idleState == 1) {
                        this.protocol.idleAbort();
                        this.idleState = 2;
                    }
                    // MONITOREXIT : started
                    continue;
                }
                catch (ConnectionException cex) {
                    this.throwClosedException(cex);
                    continue;
                }
                break;
            } while (true);
            catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            }
        }
        int minidle = ((IMAPStore)this.store).getMinIdleTime();
        if (minidle <= 0) return;
        try {
            Thread.sleep(minidle);
            return;
        }
        catch (InterruptedException ex) {
            // empty catch block
        }
    }

    void waitIfIdle() throws ProtocolException {
        assert (Thread.holdsLock(this.messageCacheLock));
        while (this.idleState != 0) {
            if (this.idleState == 1) {
                this.protocol.idleAbort();
                this.idleState = 2;
            }
            try {
                this.messageCacheLock.wait();
            }
            catch (InterruptedException ex) {}
        }
    }

    public void handleResponse(Response r) {
        assert (Thread.holdsLock(this.messageCacheLock));
        if (r.isOK() || r.isNO() || r.isBAD() || r.isBYE()) {
            ((IMAPStore)this.store).handleResponseCode(r);
        }
        if (r.isBYE()) {
            if (this.opened) {
                this.cleanup(false);
            }
            return;
        }
        if (r.isOK()) {
            return;
        }
        if (!r.isUnTagged()) {
            return;
        }
        if (!(r instanceof IMAPResponse)) {
            this.logger.fine("UNEXPECTED RESPONSE : " + r.toString());
            return;
        }
        IMAPResponse ir = (IMAPResponse)r;
        if (ir.keyEquals("EXISTS")) {
            int exists = ir.getNumber();
            if (exists <= this.realTotal) {
                return;
            }
            int count = exists - this.realTotal;
            Message[] msgs = new Message[count];
            this.messageCache.addMessages(count, this.realTotal + 1);
            int oldtotal = this.total;
            this.realTotal += count;
            this.total += count;
            if (this.hasMessageCountListener) {
                for (int i = 0; i < count; ++i) {
                    msgs[i] = this.messageCache.getMessage(++oldtotal);
                }
                this.notifyMessageAddedListeners(msgs);
            }
        } else if (ir.keyEquals("EXPUNGE")) {
            int seqnum = ir.getNumber();
            Message[] msgs = null;
            if (this.doExpungeNotification && this.hasMessageCountListener) {
                msgs = new Message[]{this.getMessageBySeqNumber(seqnum)};
            }
            this.messageCache.expungeMessage(seqnum);
            --this.realTotal;
            if (msgs != null) {
                this.notifyMessageRemovedListeners(false, msgs);
            }
        } else if (ir.keyEquals("FETCH")) {
            IMAPMessage msg;
            assert (ir instanceof FetchResponse);
            FetchResponse f = (FetchResponse)ir;
            Flags flags = (Flags)((Object)f.getItem(Flags.class));
            if (flags != null && (msg = this.getMessageBySeqNumber(f.getNumber())) != null) {
                msg._setFlags(flags);
                this.notifyMessageChangedListeners(1, msg);
            }
        } else if (ir.keyEquals("RECENT")) {
            this.recent = ir.getNumber();
        }
    }

    void handleResponses(Response[] r) {
        for (int i = 0; i < r.length; ++i) {
            if (r[i] == null) continue;
            this.handleResponse(r[i]);
        }
    }

    protected synchronized IMAPProtocol getStoreProtocol() throws ProtocolException {
        this.connectionPoolLogger.fine("getStoreProtocol() borrowing a connection");
        return ((IMAPStore)this.store).getFolderStoreProtocol();
    }

    protected synchronized void throwClosedException(ConnectionException cex) throws FolderClosedException, StoreClosedException {
        if (this.protocol != null && cex.getProtocol() == this.protocol || this.protocol == null && !this.reallyClosed) {
            throw new FolderClosedException(this, cex.getMessage());
        }
        throw new StoreClosedException(this.store, cex.getMessage());
    }

    protected IMAPProtocol getProtocol() throws ProtocolException {
        assert (Thread.holdsLock(this.messageCacheLock));
        this.waitIfIdle();
        return this.protocol;
    }

    public Object doCommand(ProtocolCommand cmd) throws MessagingException {
        try {
            return this.doProtocolCommand(cmd);
        }
        catch (ConnectionException cex) {
            this.throwClosedException(cex);
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
        return null;
    }

    public Object doOptionalCommand(String err, ProtocolCommand cmd) throws MessagingException {
        try {
            return this.doProtocolCommand(cmd);
        }
        catch (BadCommandException bex) {
            throw new MessagingException(err, bex);
        }
        catch (ConnectionException cex) {
            this.throwClosedException(cex);
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
        return null;
    }

    public Object doCommandIgnoreFailure(ProtocolCommand cmd) throws MessagingException {
        try {
            return this.doProtocolCommand(cmd);
        }
        catch (CommandFailedException cfx) {
            return null;
        }
        catch (ConnectionException cex) {
            this.throwClosedException(cex);
        }
        catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        }
        return null;
    }

    protected Object doProtocolCommand(ProtocolCommand cmd) throws ProtocolException {
        IMAPFolder iMAPFolder = this;
        synchronized (iMAPFolder) {
            if (this.protocol != null) {
                Object object = this.messageCacheLock;
                synchronized (object) {
                    return cmd.doCommand(this.getProtocol());
                }
            }
        }
        IMAPProtocol p = null;
        try {
            p = this.getStoreProtocol();
            Object object = cmd.doCommand(p);
            Object var7_7 = null;
            this.releaseStoreProtocol(p);
            return object;
        }
        catch (Throwable var6_9) {
            Object var7_8 = null;
            this.releaseStoreProtocol(p);
            throw var6_9;
        }
    }

    protected synchronized void releaseStoreProtocol(IMAPProtocol p) {
        if (p != this.protocol) {
            ((IMAPStore)this.store).releaseFolderStoreProtocol(p);
        } else {
            this.logger.fine("releasing our protocol as store protocol?");
        }
    }

    protected void releaseProtocol(boolean returnToPool) {
        if (this.protocol != null) {
            this.protocol.removeResponseHandler(this);
            if (returnToPool) {
                ((IMAPStore)this.store).releaseProtocol(this, this.protocol);
            } else {
                this.protocol.disconnect();
                ((IMAPStore)this.store).releaseProtocol(this, null);
            }
            this.protocol = null;
        }
    }

    protected void keepConnectionAlive(boolean keepStoreAlive) throws ProtocolException {
        if (System.currentTimeMillis() - this.protocol.getTimestamp() > 1000) {
            this.waitIfIdle();
            if (this.protocol != null) {
                this.protocol.noop();
            }
        }
        if (keepStoreAlive && ((IMAPStore)this.store).hasSeparateStoreConnection()) {
            IMAPProtocol p = null;
            try {
                p = ((IMAPStore)this.store).getFolderStoreProtocol();
                if (System.currentTimeMillis() - p.getTimestamp() > 1000) {
                    p.noop();
                }
                Object var4_3 = null;
                ((IMAPStore)this.store).releaseFolderStoreProtocol(p);
            }
            catch (Throwable var3_5) {
                Object var4_4 = null;
                ((IMAPStore)this.store).releaseFolderStoreProtocol(p);
                throw var3_5;
            }
        }
    }

    protected IMAPMessage getMessageBySeqNumber(int seqnum) {
        return this.messageCache.getMessageBySeqnum(seqnum);
    }

    private boolean isDirectory() {
        return (this.type & 2) != 0;
    }

    public static interface ProtocolCommand {
        public Object doCommand(IMAPProtocol var1) throws ProtocolException;
    }

    public static class FetchProfileItem
    extends FetchProfile.Item {
        public static final FetchProfileItem HEADERS = new FetchProfileItem("HEADERS");
        public static final FetchProfileItem SIZE = new FetchProfileItem("SIZE");

        protected FetchProfileItem(String name) {
            super(name);
        }
    }

}

