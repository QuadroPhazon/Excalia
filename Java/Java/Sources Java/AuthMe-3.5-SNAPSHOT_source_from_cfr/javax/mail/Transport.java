/*
 * Decompiled with CFR 0_110.
 */
package javax.mail;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.event.MailEvent;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

public abstract class Transport
extends Service {
    private volatile Vector transportListeners = null;

    public Transport(Session session, URLName urlname) {
        super(session, urlname);
    }

    public static void send(Message msg) throws MessagingException {
        msg.saveChanges();
        Transport.send0(msg, msg.getAllRecipients(), null, null);
    }

    public static void send(Message msg, Address[] addresses) throws MessagingException {
        msg.saveChanges();
        Transport.send0(msg, addresses, null, null);
    }

    public static void send(Message msg, String user, String password) throws MessagingException {
        msg.saveChanges();
        Transport.send0(msg, msg.getAllRecipients(), user, password);
    }

    public static void send(Message msg, Address[] addresses, String user, String password) throws MessagingException {
        msg.saveChanges();
        Transport.send0(msg, addresses, user, password);
    }

    private static void send0(Message msg, Address[] addresses, String user, String password) throws MessagingException {
        Session s;
        if (addresses == null || addresses.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Hashtable protocols = new Hashtable();
        Vector<Object> invalid = new Vector<Object>();
        Vector<Address> validSent = new Vector<Address>();
        Vector<Address> validUnsent = new Vector<Address>();
        for (int i = 0; i < addresses.length; ++i) {
            if (protocols.containsKey(addresses[i].getType())) {
                Vector v = (Vector)protocols.get(addresses[i].getType());
                v.addElement(addresses[i]);
                continue;
            }
            Vector<Address> w = new Vector<Address>();
            w.addElement(addresses[i]);
            protocols.put(addresses[i].getType(), w);
        }
        int dsize = protocols.size();
        if (dsize == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        Session session = s = msg.session != null ? msg.session : Session.getDefaultInstance(System.getProperties(), null);
        if (dsize == 1) {
            Transport transport = s.getTransport(addresses[0]);
            try {
                if (user != null) {
                    transport.connect(user, password);
                } else {
                    transport.connect();
                }
                transport.sendMessage(msg, addresses);
            }
            finally {
                transport.close();
            }
            return;
        }
        MessagingException chainedEx = null;
        boolean sendFailed = false;
        Enumeration e = protocols.elements();
        while (e.hasMoreElements()) {
            Vector v = (Vector)e.nextElement();
            Object[] protaddresses = new Address[v.size()];
            v.copyInto(protaddresses);
            Transport transport = s.getTransport((Address)protaddresses[0]);
            if (transport == null) {
                for (int j = 0; j < protaddresses.length; ++j) {
                    invalid.addElement(protaddresses[j]);
                }
                continue;
            }
            try {
                transport.connect();
                transport.sendMessage(msg, (Address[])protaddresses);
                continue;
            }
            catch (SendFailedException sex) {
                Address[] c;
                sendFailed = true;
                if (chainedEx == null) {
                    chainedEx = sex;
                } else {
                    chainedEx.setNextException(sex);
                }
                Address[] a = sex.getInvalidAddresses();
                if (a != null) {
                    for (int j = 0; j < a.length; ++j) {
                        invalid.addElement(a[j]);
                    }
                }
                if ((a = sex.getValidSentAddresses()) != null) {
                    for (int k = 0; k < a.length; ++k) {
                        validSent.addElement(a[k]);
                    }
                }
                if ((c = sex.getValidUnsentAddresses()) == null) continue;
                for (int l = 0; l < c.length; ++l) {
                    validUnsent.addElement(c[l]);
                }
                continue;
            }
            catch (MessagingException mex) {
                sendFailed = true;
                if (chainedEx == null) {
                    chainedEx = mex;
                    continue;
                }
                chainedEx.setNextException(mex);
                continue;
            }
            finally {
                transport.close();
                continue;
            }
        }
        if (sendFailed || invalid.size() != 0 || validUnsent.size() != 0) {
            Object[] a = null;
            Object[] b = null;
            Object[] c = null;
            if (validSent.size() > 0) {
                a = new Address[validSent.size()];
                validSent.copyInto(a);
            }
            if (validUnsent.size() > 0) {
                b = new Address[validUnsent.size()];
                validUnsent.copyInto(b);
            }
            if (invalid.size() > 0) {
                c = new Address[invalid.size()];
                invalid.copyInto(c);
            }
            throw new SendFailedException("Sending failed", chainedEx, (Address[])a, (Address[])b, (Address[])c);
        }
    }

    public abstract void sendMessage(Message var1, Address[] var2) throws MessagingException;

    public synchronized void addTransportListener(TransportListener l) {
        if (this.transportListeners == null) {
            this.transportListeners = new Vector<E>();
        }
        this.transportListeners.addElement(l);
    }

    public synchronized void removeTransportListener(TransportListener l) {
        if (this.transportListeners != null) {
            this.transportListeners.removeElement(l);
        }
    }

    protected void notifyTransportListeners(int type, Address[] validSent, Address[] validUnsent, Address[] invalid, Message msg) {
        if (this.transportListeners == null) {
            return;
        }
        TransportEvent e = new TransportEvent(this, type, validSent, validUnsent, invalid, msg);
        this.queueEvent(e, this.transportListeners);
    }
}

