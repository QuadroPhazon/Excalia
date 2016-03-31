/*
 * Decompiled with CFR 0_110.
 */
package javax.mail.internet;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

class UniqueValue {
    private static int id = 0;

    UniqueValue() {
    }

    public static String getUniqueBoundaryValue() {
        StringBuffer s = new StringBuffer();
        s.append("----=_Part_").append(UniqueValue.getUniqueId()).append("_").append(s.hashCode()).append('.').append(System.currentTimeMillis());
        return s.toString();
    }

    public static String getUniqueMessageIDValue(Session ssn) {
        String suffix = null;
        InternetAddress addr = InternetAddress.getLocalAddress(ssn);
        suffix = addr != null ? addr.getAddress() : "javamailuser@localhost";
        StringBuffer s = new StringBuffer();
        s.append(s.hashCode()).append('.').append(UniqueValue.getUniqueId()).append('.').append(System.currentTimeMillis()).append('.').append("JavaMail.").append(suffix);
        return s.toString();
    }

    private static synchronized int getUniqueId() {
        return id++;
    }
}

