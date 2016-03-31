/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URL;
import javax.swing.JButton;
import net.minecraft.Util;

public class TransparentButton
extends JButton {
    private static final long serialVersionUID = 1;

    public TransparentButton(String paramString) {
        super(paramString);
    }

    public void addHttpLink(final String link) {
        this.addMouseListener(new MouseAdapter(){

            public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
                try {
                    Util.openLink(new URL(link).toURI());
                }
                catch (Exception localException) {
                    localException.printStackTrace();
                }
            }
        });
    }

    public boolean isOpaque() {
        return false;
    }

}

