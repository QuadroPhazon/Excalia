/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.Color;
import javax.swing.JCheckBox;

public class TransparentCheckbox
extends JCheckBox {
    private static final long serialVersionUID = 1;

    public TransparentCheckbox(String paramString) {
        super(paramString);
        this.setForeground(Color.BLACK);
    }

    public boolean isOpaque() {
        return false;
    }
}

