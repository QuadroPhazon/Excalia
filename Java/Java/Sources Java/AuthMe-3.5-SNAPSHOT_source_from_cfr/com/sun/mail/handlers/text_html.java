/*
 * Decompiled with CFR 0_110.
 */
package com.sun.mail.handlers;

import com.sun.mail.handlers.text_plain;
import javax.activation.ActivationDataFlavor;

public class text_html
extends text_plain {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(String.class, "text/html", "HTML String");

    protected ActivationDataFlavor getDF() {
        return myDF;
    }
}

