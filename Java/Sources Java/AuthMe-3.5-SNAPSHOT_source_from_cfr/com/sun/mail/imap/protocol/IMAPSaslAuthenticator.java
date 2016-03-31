/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.imap.protocol;

import com.sun.mail.iap.Argument;
import com.sun.mail.iap.ByteArray;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.SaslAuthenticator;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.MailLogger;
import com.sun.mail.util.PropUtil;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

public class IMAPSaslAuthenticator
implements SaslAuthenticator {
    private IMAPProtocol pr;
    private String name;
    private Properties props;
    private MailLogger logger;
    private String host;

    public IMAPSaslAuthenticator(IMAPProtocol pr, String name, Properties props, MailLogger logger, String host) {
        this.pr = pr;
        this.name = name;
        this.props = props;
        this.logger = logger;
        this.host = host;
    }

    public boolean authenticate(String[] mechs, final String realm, String authzid, final String u, final String p) throws ProtocolException {
        IMAPProtocol iMAPProtocol = this.pr;
        synchronized (iMAPProtocol) {
            String qop;
            boolean isXGWTRUSTEDAPP;
            SaslClient sc;
            Vector<Response> v = new Vector<Response>();
            String tag = null;
            Response r = null;
            boolean done = false;
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("SASL Mechanisms:");
                for (int i = 0; i < mechs.length; ++i) {
                    this.logger.fine(" " + mechs[i]);
                }
                this.logger.fine("");
            }
            CallbackHandler cbh = new CallbackHandler(){

                public void handle(Callback[] callbacks) {
                    if (IMAPSaslAuthenticator.this.logger.isLoggable(Level.FINE)) {
                        IMAPSaslAuthenticator.this.logger.fine("SASL callback length: " + callbacks.length);
                    }
                    block0 : for (int i = 0; i < callbacks.length; ++i) {
                        if (IMAPSaslAuthenticator.this.logger.isLoggable(Level.FINE)) {
                            IMAPSaslAuthenticator.this.logger.fine("SASL callback " + i + ": " + callbacks[i]);
                        }
                        if (callbacks[i] instanceof NameCallback) {
                            NameCallback ncb = (NameCallback)callbacks[i];
                            ncb.setName(u);
                            continue;
                        }
                        if (callbacks[i] instanceof PasswordCallback) {
                            PasswordCallback pcb = (PasswordCallback)callbacks[i];
                            pcb.setPassword(p.toCharArray());
                            continue;
                        }
                        if (callbacks[i] instanceof RealmCallback) {
                            RealmCallback rcb = (RealmCallback)callbacks[i];
                            rcb.setText(realm != null ? realm : rcb.getDefaultText());
                            continue;
                        }
                        if (!(callbacks[i] instanceof RealmChoiceCallback)) continue;
                        RealmChoiceCallback rcb = (RealmChoiceCallback)callbacks[i];
                        if (realm == null) {
                            rcb.setSelectedIndex(rcb.getDefaultChoice());
                            continue;
                        }
                        String[] choices = rcb.getChoices();
                        for (int k = 0; k < choices.length; ++k) {
                            if (!choices[k].equals(realm)) continue;
                            rcb.setSelectedIndex(k);
                            continue block0;
                        }
                    }
                }
            };
            try {
                sc = Sasl.createSaslClient(mechs, authzid, this.name, this.host, this.props, cbh);
            }
            catch (SaslException sex) {
                this.logger.log(Level.FINE, "Failed to create SASL client", sex);
                return false;
            }
            if (sc == null) {
                this.logger.fine("No SASL support");
                return false;
            }
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.fine("SASL client " + sc.getMechanismName());
            }
            try {
                tag = this.pr.writeCommand("AUTHENTICATE " + sc.getMechanismName(), null);
            }
            catch (Exception ex) {
                this.logger.log(Level.FINE, "SASL AUTHENTICATE Exception", ex);
                return false;
            }
            OutputStream os = this.pr.getIMAPOutputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] CRLF = new byte[]{13, 10};
            boolean bl = isXGWTRUSTEDAPP = sc.getMechanismName().equals("XGWTRUSTEDAPP") && PropUtil.getBooleanProperty(this.props, "mail." + this.name + ".sasl.xgwtrustedapphack.enable", true);
            while (!done) {
                try {
                    r = this.pr.readResponse();
                    if (r.isContinuation()) {
                        Object ba = null;
                        if (!sc.isComplete()) {
                            ba = r.readByteArray().getNewBytes();
                            if (ba.length > 0) {
                                ba = BASE64DecoderStream.decode((byte[])ba);
                            }
                            if (this.logger.isLoggable(Level.FINE)) {
                                this.logger.fine("SASL challenge: " + ASCIIUtility.toString((byte[])ba, 0, ba.length) + " :");
                            }
                            ba = sc.evaluateChallenge((byte[])ba);
                        }
                        if (ba == null) {
                            this.logger.fine("SASL no response");
                            os.write(CRLF);
                            os.flush();
                            bos.reset();
                            continue;
                        }
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("SASL response: " + ASCIIUtility.toString((byte[])ba, 0, ba.length) + " :");
                        }
                        ba = BASE64EncoderStream.encode((byte[])ba);
                        if (isXGWTRUSTEDAPP) {
                            bos.write(ASCIIUtility.getBytes("XGWTRUSTEDAPP "));
                        }
                        bos.write((byte[])ba);
                        bos.write(CRLF);
                        os.write(bos.toByteArray());
                        os.flush();
                        bos.reset();
                        continue;
                    }
                    if (r.isTagged() && r.getTag().equals(tag)) {
                        done = true;
                        continue;
                    }
                    if (r.isBYE()) {
                        done = true;
                        continue;
                    }
                    v.addElement(r);
                }
                catch (Exception ioex) {
                    this.logger.log(Level.FINE, "SASL Exception", ioex);
                    r = Response.byeResponse(ioex);
                    done = true;
                }
            }
            if (sc.isComplete() && (qop = (String)sc.getNegotiatedProperty("javax.security.sasl.qop")) != null && (qop.equalsIgnoreCase("auth-int") || qop.equalsIgnoreCase("auth-conf"))) {
                this.logger.fine("SASL Mechanism requires integrity or confidentiality");
                return false;
            }
            Object[] responses = new Response[v.size()];
            v.copyInto(responses);
            this.pr.notifyResponseHandlers((Response[])responses);
            this.pr.handleResult(r);
            this.pr.setCapabilities(r);
            if (isXGWTRUSTEDAPP) {
                Argument args = new Argument();
                args.writeString(authzid != null ? authzid : u);
                responses = this.pr.command("LOGIN", args);
                this.pr.notifyResponseHandlers((Response[])responses);
                this.pr.handleResult((Response)responses[responses.length - 1]);
                this.pr.setCapabilities((Response)responses[responses.length - 1]);
            }
            return true;
        }
    }

}

