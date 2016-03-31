/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.util;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.mail.internet.MimePart;

public class MimeUtil {
    private static final Method cleanContentType;

    private MimeUtil() {
    }

    public static String cleanContentType(MimePart mp, String contentType) {
        if (cleanContentType != null) {
            try {
                return (String)cleanContentType.invoke(null, mp, contentType);
            }
            catch (Exception ex) {
                return contentType;
            }
        }
        return contentType;
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                }
                catch (SecurityException ex) {
                    // empty catch block
                }
                return cl;
            }
        });
    }

    static {
        block11 : {
            Method meth = null;
            try {
                block10 : {
                    try {
                        String cth = System.getProperty("mail.mime.contenttypehandler");
                        if (cth == null) break block10;
                        ClassLoader cl = MimeUtil.getContextClassLoader();
                        Class clsHandler = null;
                        if (cl != null) {
                            try {
                                clsHandler = Class.forName(cth, false, cl);
                            }
                            catch (ClassNotFoundException cex) {
                                // empty catch block
                            }
                        }
                        if (clsHandler == null) {
                            clsHandler = Class.forName(cth);
                        }
                        meth = clsHandler.getMethod("cleanContentType", MimePart.class, String.class);
                    }
                    catch (ClassNotFoundException ex) {
                        Object var6_9 = null;
                        cleanContentType = meth;
                        break block11;
                    }
                    catch (NoSuchMethodException ex) {
                        Object var6_10 = null;
                        cleanContentType = meth;
                        break block11;
                    }
                    catch (RuntimeException ex) {
                        Object var6_11 = null;
                        cleanContentType = meth;
                    }
                }
                Object var6_8 = null;
                cleanContentType = meth;
            }
            catch (Throwable var5_13) {
                Object var6_12 = null;
                cleanContentType = meth;
                throw var5_13;
            }
        }
    }

}

