/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public class TransparentPanel
extends JPanel {
    private static final long serialVersionUID = 1;
    private Insets insets;

    public TransparentPanel() {
    }

    public TransparentPanel(LayoutManager paramLayoutManager) {
        this.setLayout(paramLayoutManager);
    }

    public boolean isOpaque() {
        return false;
    }

    public void setInsets(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this.insets = new Insets(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    public Insets getInsets() {
        if (this.insets == null) {
            return super.getInsets();
        }
        return this.insets;
    }
}

