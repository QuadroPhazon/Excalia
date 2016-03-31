/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.Color;
import javax.swing.JLabel;

public class TransparentLabel
extends JLabel {
    private static final long serialVersionUID = 1;

    public TransparentLabel(String paramString, int paramInt) {
        super(paramString, paramInt);
        this.setForeground(Color.BLACK);
    }

    public TransparentLabel(String paramString) {
        super(paramString);
        this.setForeground(Color.BLACK);
    }

    public boolean isOpaque() {
        return false;
    }
}

