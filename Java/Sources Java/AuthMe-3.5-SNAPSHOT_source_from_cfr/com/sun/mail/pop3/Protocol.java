/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.pop3;

import com.sun.mail.pop3.Response;
import com.sun.mail.pop3.Status;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.SharedByteArrayOutputStream;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;

class Protocol {
    private Socket socket;
    private String host;
    private Properties props;
    private String prefix;
    private DataInputStream input;
    private PrintWriter output;
    private TraceInputStream traceInput;
    private TraceOutputStream traceOutput;
    private MailLogger logger;
    private MailLogger traceLogger;
    private String apopChallenge = null;
    private Map capabilities = null;
    private boolean pipelining;
    private boolean noauthdebug = true;
    private boolean traceSuspended;
    private static final int POP3_PORT = 110;
    private static final String CRLF = "\r\n";
    private static final int SLOP = 128;
    private static char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    Protocol(String host, int port, MailLogger logger, Properties props, String prefix, boolean isSSL) throws IOException {
        this.host = host;
        this.props = props;
        this.prefix = prefix;
        this.logger = logger;
        this.traceLogger = logger.getSubLogger("protocol", null);
        this.noauthdebug = !PropUtil.getBooleanProperty(props, "mail.debug.auth", false);
        boolean enableAPOP = this.getBoolProp(props, prefix + ".apop.enable");
        boolean disableCapa = this.getBoolProp(props, prefix + ".disablecapa");
        try {
            if (port == -1) {
                port = 110;
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("connecting to host \"" + host + "\", port " + port + ", isSSL " + isSSL);
            }
            this.socket = SocketFetcher.getSocket(host, port, props, prefix, isSSL);
            this.initStreams();
            Response r = this.simpleCommand(null);
        }
        catch (IOException ioe) {
            try {
                this.socket.close();
            }
            finally {
                throw ioe;
            }
        }
        if (!r.ok) {
            try {
                this.socket.close();
            }
            finally {
                throw new IOException("Connect failed");
            }
        }
        if (enableAPOP) {
            int challStart = r.data.indexOf(60);
            int challEnd = r.data.indexOf(62, challStart);
            if (challStart != -1 && challEnd != -1) {
                this.apopChallenge = r.data.substring(challStart, challEnd + 1);
            }
            logger.log(Level.FINE, "APOP challenge: {0}", this.apopChallenge);
        }
        if (!disableCapa) {
            this.setCapabilities(this.capa());
        }
        boolean bl = this.pipelining = this.hasCapability("PIPELINING") || PropUtil.getBooleanProperty(props, prefix + ".pipelining", false);
        if (this.pipelining) {
            logger.config("PIPELINING enabled");
        }
    }

    private final synchronized boolean getBoolProp(Properties props, String prop) {
        boolean val = PropUtil.getBooleanProperty(props, prop, false);
        if (this.logger.isLoggable(Level.CONFIG)) {
            this.logger.config(prop + ": " + val);
        }
        return val;
    }

    private void initStreams() throws IOException {
        boolean quote = PropUtil.getBooleanProperty(this.props, "mail.debug.quote", false);
        this.traceInput = new TraceInputStream(this.socket.getInputStream(), this.traceLogger);
        this.traceInput.setQuote(quote);
        this.traceOutput = new TraceOutputStream(this.socket.getOutputStream(), this.traceLogger);
        this.traceOutput.setQuote(quote);
        this.input = new DataInputStream(new BufferedInputStream(this.traceInput));
        this.output = new PrintWriter(new BufferedWriter(new OutputStreamWriter((OutputStream)this.traceOutput, "iso-8859-1")));
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (this.socket != null) {
            this.quit();
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    synchronized void setCapabilities(InputStream in) {
        BufferedReader r;
        String s;
        block15 : {
            if (in == null) {
                this.capabilities = null;
                return;
            }
            this.capabilities = new HashMap(10);
            r = null;
            try {
                r = new BufferedReader(new InputStreamReader(in, "us-ascii"));
            }
            catch (UnsupportedEncodingException ex) {
                if ($assertionsDisabled) break block15;
                throw new AssertionError();
            }
        }
        while ((s = r.readLine()) != null) {
            String cap = s;
            int i = cap.indexOf(32);
            if (i > 0) {
                cap = cap.substring(0, i);
            }
            this.capabilities.put(cap.toUpperCase(Locale.ENGLISH), s);
        }
        try {
            in.close();
            return;
        }
        catch (IOException ex) {
            return;
        }
        catch (IOException ex) {
            try {
                in.close();
                return;
            }
            catch (IOException ex) {
                return;
            }
            catch (Throwable throwable) {
                try {
                    in.close();
                    throw throwable;
                }
                catch (IOException ex) {
                    // empty catch block
                }
                throw throwable;
            }
        }
    }

    synchronized boolean hasCapability(String c) {
        return this.capabilities != null && this.capabilities.containsKey(c.toUpperCase(Locale.ENGLISH));
    }

    synchronized Map getCapabilities() {
        return this.capabilities;
    }

    synchronized String login(String user, String password) throws IOException {
        boolean batch = this.pipelining && this.socket instanceof SSLSocket;
        try {
            String cmd2;
            Response r;
            if (this.noauthdebug && this.isTracing()) {
                this.logger.fine("authentication command trace suppressed");
                this.suspendTracing();
            }
            String dpw = null;
            if (this.apopChallenge != null) {
                dpw = this.getDigest(password);
            }
            if (this.apopChallenge != null && dpw != null) {
                r = this.simpleCommand("APOP " + user + " " + dpw);
            } else if (batch) {
                cmd2 = "USER " + user;
                this.batchCommandStart(cmd2);
                this.issueCommand(cmd2);
                cmd2 = "PASS " + password;
                this.batchCommandContinue(cmd2);
                this.issueCommand(cmd2);
                r = this.readResponse();
                if (!r.ok) {
                    String err = r.data != null ? r.data : "USER command failed";
                    r = this.readResponse();
                    this.batchCommandEnd();
                    String string = err;
                    return string;
                }
                r = this.readResponse();
                this.batchCommandEnd();
            } else {
                r = this.simpleCommand("USER " + user);
                if (!r.ok) {
                    String cmd2 = r.data != null ? r.data : "USER command failed";
                    return cmd2;
                }
                r = this.simpleCommand("PASS " + password);
            }
            if (this.noauthdebug && this.isTracing()) {
                this.logger.log(Level.FINE, "authentication command {0}", r.ok ? "succeeded" : "failed");
            }
            if (!r.ok) {
                cmd2 = r.data != null ? r.data : "login failed";
                return cmd2;
            }
            cmd2 = null;
            return cmd2;
        }
        finally {
            this.resumeTracing();
        }
    }

    private String getDigest(String password) {
        byte[] digest;
        String key = this.apopChallenge + password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(key.getBytes("iso-8859-1"));
        }
        catch (NoSuchAlgorithmException nsae) {
            return null;
        }
        catch (UnsupportedEncodingException uee) {
            return null;
        }
        return Protocol.toHex(digest);
    }

    private static String toHex(byte[] bytes) {
        char[] result = new char[bytes.length * 2];
        int i = 0;
        for (int index = 0; index < bytes.length; ++index) {
            int temp = bytes[index] & 255;
            result[i++] = digits[temp >> 4];
            result[i++] = digits[temp & 15];
        }
        return new String(result);
    }

    synchronized boolean quit() throws IOException {
        boolean ok = false;
        try {
            Response r = this.simpleCommand("QUIT");
            ok = r.ok;
        }
        finally {
            try {
                this.socket.close();
            }
            finally {
                this.socket = null;
                this.input = null;
                this.output = null;
            }
        }
        return ok;
    }

    synchronized Status stat() throws IOException {
        Response r = this.simpleCommand("STAT");
        Status s = new Status();
        if (!r.ok) {
            throw new IOException("STAT command failed: " + r.data);
        }
        if (r.data != null) {
            try {
                StringTokenizer st = new StringTokenizer(r.data);
                s.total = Integer.parseInt(st.nextToken());
                s.size = Integer.parseInt(st.nextToken());
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        return s;
    }

    synchronized int list(int msg) throws IOException {
        Response r = this.simpleCommand("LIST " + msg);
        int size = -1;
        if (r.ok && r.data != null) {
            try {
                StringTokenizer st = new StringTokenizer(r.data);
                st.nextToken();
                size = Integer.parseInt(st.nextToken());
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        return size;
    }

    synchronized InputStream list() throws IOException {
        Response r = this.multilineCommand("LIST", 128);
        return r.bytes;
    }

    synchronized InputStream retr(int msg, int size) throws IOException {
        boolean batch;
        Response r;
        boolean bl = batch = size == 0 && this.pipelining;
        if (batch) {
            String cmd = "LIST " + msg;
            this.batchCommandStart(cmd);
            this.issueCommand(cmd);
            cmd = "RETR " + msg;
            this.batchCommandContinue(cmd);
            this.issueCommand(cmd);
            r = this.readResponse();
            if (r.ok && r.data != null) {
                try {
                    StringTokenizer st = new StringTokenizer(r.data);
                    st.nextToken();
                    size = Integer.parseInt(st.nextToken());
                    if (size > 1073741824 || size < 0) {
                        size = 0;
                    } else {
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("pipeline message size " + size);
                        }
                        size += 128;
                    }
                }
                catch (Exception e) {
                    // empty catch block
                }
            }
            r = this.readResponse();
            if (r.ok) {
                r.bytes = this.readMultilineResponse(size + 128);
            }
            this.batchCommandEnd();
        } else {
            String cmd = "RETR " + msg;
            this.multilineCommandStart(cmd);
            this.issueCommand(cmd);
            r = this.readResponse();
            if (!r.ok) {
                this.multilineCommandEnd();
                return null;
            }
            if (size <= 0 && r.data != null) {
                try {
                    StringTokenizer st = new StringTokenizer(r.data);
                    String s = st.nextToken();
                    String octets = st.nextToken();
                    if (octets.equals("octets")) {
                        size = Integer.parseInt(s);
                        if (size > 1073741824 || size < 0) {
                            size = 0;
                        } else {
                            if (this.logger.isLoggable(Level.FINE)) {
                                this.logger.fine("guessing message size: " + size);
                            }
                            size += 128;
                        }
                    }
                }
                catch (Exception e) {
                    // empty catch block
                }
            }
            r.bytes = this.readMultilineResponse(size);
            this.multilineCommandEnd();
        }
        if (r.ok && size > 0 && this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("got message size " + r.bytes.available());
        }
        return r.bytes;
    }

    synchronized boolean retr(int msg, OutputStream os) throws IOException {
        int b;
        Exception terr2;
        String cmd = "RETR " + msg;
        this.multilineCommandStart(cmd);
        this.issueCommand(cmd);
        Response r = this.readResponse();
        if (!r.ok) {
            this.multilineCommandEnd();
            return false;
        }
        Exception terr2 = null;
        int lastb = 10;
        try {
            while ((b = this.input.read()) >= 0) {
                if (lastb == 10 && b == 46 && (b = this.input.read()) == 13) {
                    b = this.input.read();
                    break;
                }
                if (terr2 == null) {
                    try {
                        os.write(b);
                    }
                    catch (IOException ex) {
                        this.logger.log(Level.FINE, "exception while streaming", ex);
                        terr2 = ex;
                    }
                    catch (RuntimeException ex) {
                        this.logger.log(Level.FINE, "exception while streaming", ex);
                        terr2 = ex;
                    }
                }
                lastb = b;
            }
        }
        catch (InterruptedIOException iioex) {
            try {
                this.socket.close();
            }
            catch (IOException cex) {
                // empty catch block
            }
            throw iioex;
        }
        if (b < 0) {
            throw new EOFException("EOF on socket");
        }
        if (terr2 != null) {
            if (terr2 instanceof IOException) {
                throw (IOException)terr2;
            }
            if (terr2 instanceof RuntimeException) {
                throw (RuntimeException)terr2;
            }
            assert (false);
        }
        this.multilineCommandEnd();
        return true;
    }

    synchronized InputStream top(int msg, int n) throws IOException {
        Response r = this.multilineCommand("TOP " + msg + " " + n, 0);
        return r.bytes;
    }

    synchronized boolean dele(int msg) throws IOException {
        Response r = this.simpleCommand("DELE " + msg);
        return r.ok;
    }

    synchronized String uidl(int msg) throws IOException {
        Response r = this.simpleCommand("UIDL " + msg);
        if (!r.ok) {
            return null;
        }
        int i = r.data.indexOf(32);
        if (i > 0) {
            return r.data.substring(i + 1);
        }
        return null;
    }

    synchronized boolean uidl(String[] uids) throws IOException {
        Response r = this.multilineCommand("UIDL", 15 * uids.length);
        if (!r.ok) {
            return false;
        }
        LineInputStream lis = new LineInputStream(r.bytes);
        String line = null;
        while ((line = lis.readLine()) != null) {
            int n;
            int i = line.indexOf(32);
            if (i < 1 || i >= line.length() || (n = Integer.parseInt(line.substring(0, i))) <= 0 || n > uids.length) continue;
            uids[n - 1] = line.substring(i + 1);
        }
        try {
            r.bytes.close();
        }
        catch (IOException ex) {
            // empty catch block
        }
        return true;
    }

    synchronized boolean noop() throws IOException {
        Response r = this.simpleCommand("NOOP");
        return r.ok;
    }

    synchronized boolean rset() throws IOException {
        Response r = this.simpleCommand("RSET");
        return r.ok;
    }

    synchronized boolean stls() throws IOException {
        if (this.socket instanceof SSLSocket) {
            return true;
        }
        Response r = this.simpleCommand("STLS");
        if (r.ok) {
            try {
                this.socket = SocketFetcher.startTLS(this.socket, this.host, this.props, this.prefix);
                this.initStreams();
            }
            catch (IOException ioex) {
                try {
                    this.socket.close();
                }
                finally {
                    this.socket = null;
                    this.input = null;
                    this.output = null;
                }
                IOException sioex = new IOException("Could not convert socket to TLS");
                sioex.initCause(ioex);
                throw sioex;
            }
        }
        return r.ok;
    }

    synchronized boolean isSSL() {
        return this.socket instanceof SSLSocket;
    }

    synchronized InputStream capa() throws IOException {
        Response r = this.multilineCommand("CAPA", 128);
        if (!r.ok) {
            return null;
        }
        return r.bytes;
    }

    private Response simpleCommand(String cmd) throws IOException {
        this.simpleCommandStart(cmd);
        this.issueCommand(cmd);
        Response r = this.readResponse();
        this.simpleCommandEnd();
        return r;
    }

    private void issueCommand(String cmd) throws IOException {
        if (this.socket == null) {
            throw new IOException("Folder is closed");
        }
        if (cmd != null) {
            cmd = cmd + "\r\n";
            this.output.print(cmd);
            this.output.flush();
        }
    }

    private Response readResponse() throws IOException {
        String line = null;
        try {
            line = this.input.readLine();
        }
        catch (InterruptedIOException iioex) {
            try {
                this.socket.close();
            }
            catch (IOException cex) {
                // empty catch block
            }
            throw new EOFException(iioex.getMessage());
        }
        catch (SocketException ex) {
            try {
                this.socket.close();
            }
            catch (IOException cex) {
                // empty catch block
            }
            throw new EOFException(ex.getMessage());
        }
        if (line == null) {
            this.traceLogger.finest("<EOF>");
            throw new EOFException("EOF on socket");
        }
        Response r = new Response();
        if (line.startsWith("+OK")) {
            r.ok = true;
        } else if (line.startsWith("-ERR")) {
            r.ok = false;
        } else {
            throw new IOException("Unexpected response: " + line);
        }
        int i = line.indexOf(32);
        if (i >= 0) {
            r.data = line.substring(i + 1);
        }
        return r;
    }

    private Response multilineCommand(String cmd, int size) throws IOException {
        this.multilineCommandStart(cmd);
        this.issueCommand(cmd);
        Response r = this.readResponse();
        if (!r.ok) {
            this.multilineCommandEnd();
            return r;
        }
        r.bytes = this.readMultilineResponse(size);
        this.multilineCommandEnd();
        return r;
    }

    private InputStream readMultilineResponse(int size) throws IOException {
        int b;
        SharedByteArrayOutputStream buf;
        buf = new SharedByteArrayOutputStream(size);
        int lastb = 10;
        try {
            while ((b = this.input.read()) >= 0) {
                if (lastb == 10 && b == 46 && (b = this.input.read()) == 13) {
                    b = this.input.read();
                    break;
                }
                buf.write(b);
                lastb = b;
            }
        }
        catch (InterruptedIOException iioex) {
            try {
                this.socket.close();
            }
            catch (IOException cex) {
                // empty catch block
            }
            throw iioex;
        }
        if (b < 0) {
            throw new EOFException("EOF on socket");
        }
        return buf.toStream();
    }

    protected boolean isTracing() {
        return this.traceLogger.isLoggable(Level.FINEST);
    }

    private void suspendTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(false);
            this.traceOutput.setTrace(false);
        }
    }

    private void resumeTracing() {
        if (this.traceLogger.isLoggable(Level.FINEST)) {
            this.traceInput.setTrace(true);
            this.traceOutput.setTrace(true);
        }
    }

    private void simpleCommandStart(String command) {
    }

    private void simpleCommandEnd() {
    }

    private void multilineCommandStart(String command) {
    }

    private void multilineCommandEnd() {
    }

    private void batchCommandStart(String command) {
    }

    private void batchCommandContinue(String command) {
    }

    private void batchCommandEnd() {
    }
}

