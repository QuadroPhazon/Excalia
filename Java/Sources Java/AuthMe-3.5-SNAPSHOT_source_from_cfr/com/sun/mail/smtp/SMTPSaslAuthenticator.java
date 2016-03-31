/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.smtp;

import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.smtp.SaslAuthenticator;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.MailLogger;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

public class SMTPSaslAuthenticator
implements SaslAuthenticator {
    private SMTPTransport pr;
    private String name;
    private Properties props;
    private MailLogger logger;
    private String host;

    public SMTPSaslAuthenticator(SMTPTransport pr, String name, Properties props, MailLogger logger, String host) {
        this.pr = pr;
        this.name = name;
        this.props = props;
        this.logger = logger;
        this.host = host;
    }

    public boolean authenticate(String[] mechs, final String realm, String authzid, final String u, final String p) throws MessagingException {
        int resp;
        SaslClient sc;
        String qop;
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
                if (SMTPSaslAuthenticator.this.logger.isLoggable(Level.FINE)) {
                    SMTPSaslAuthenticator.this.logger.fine("SASL callback length: " + callbacks.length);
                }
                block0 : for (int i = 0; i < callbacks.length; ++i) {
                    if (SMTPSaslAuthenticator.this.logger.isLoggable(Level.FINE)) {
                        SMTPSaslAuthenticator.this.logger.fine("SASL callback " + i + ": " + callbacks[i]);
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
            this.logger.log(Level.FINE, "Failed to create SASL client: ", sex);
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
            String mech = sc.getMechanismName();
            String ir = null;
            if (sc.hasInitialResponse()) {
                byte[] ba = sc.evaluateChallenge(new byte[0]);
                ba = BASE64EncoderStream.encode(ba);
                ir = ASCIIUtility.toString(ba, 0, ba.length);
            }
            resp = ir != null ? this.pr.simpleCommand("AUTH " + mech + " " + ir) : this.pr.simpleCommand("AUTH " + mech);
            if (resp == 530) {
                this.pr.startTLS();
                resp = ir != null ? this.pr.simpleCommand("AUTH " + mech + " " + ir) : this.pr.simpleCommand("AUTH " + mech);
            }
            if (resp == 235) {
                return true;
            }
            if (resp != 334) {
                return false;
            }
        }
        catch (Exception ex) {
            this.logger.log(Level.FINE, "SASL AUTHENTICATE Exception", ex);
            return false;
        }
        while (!done) {
            try {
                if (resp == 334) {
                    Object ba = null;
                    if (!sc.isComplete()) {
                        ba = ASCIIUtility.getBytes(SMTPSaslAuthenticator.responseText(this.pr));
                        if (ba.length > 0) {
                            ba = BASE64DecoderStream.decode((byte[])ba);
                        }
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.fine("SASL challenge: " + ASCIIUtility.toString((byte[])ba, 0, ba.length) + " :");
                        }
                        ba = sc.evaluateChallenge((byte[])ba);
                    }
                    if (ba == null) {
                        this.logger.fine("SASL: no response");
                        resp = this.pr.simpleCommand("*");
                        continue;
                    }
                    if (this.logger.isLoggable(Level.FINE)) {
                        this.logger.fine("SASL response: " + ASCIIUtility.toString((byte[])ba, 0, ba.length) + " :");
                    }
                    ba = BASE64EncoderStream.encode((byte[])ba);
                    resp = this.pr.simpleCommand((byte[])ba);
                    continue;
                }
                done = true;
            }
            catch (Exception ioex) {
                this.logger.log(Level.FINE, "SASL Exception", ioex);
                done = true;
            }
        }
        if (sc.isComplete() && (qop = (String)sc.getNegotiatedProperty("javax.security.sasl.qop")) != null && (qop.equalsIgnoreCase("auth-int") || qop.equalsIgnoreCase("auth-conf"))) {
            this.logger.fine("SASL Mechanism requires integrity or confidentiality");
            return false;
        }
        return true;
    }

    private static final String responseText(SMTPTransport pr) {
        String resp = pr.getLastServerResponse().trim();
        if (resp.length() > 4) {
            return resp.substring(4);
        }
        return "";
    }

}

