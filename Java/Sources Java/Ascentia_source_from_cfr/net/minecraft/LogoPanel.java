/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import net.minecraft.LoginForm;

public class LogoPanel
extends JPanel {
    private static final long serialVersionUID = 1;
    private Image bgImage;

    public LogoPanel() {
        this.setOpaque(true);
        try {
            BufferedImage localBufferedImage = ImageIO.read(LoginForm.class.getResource("logo.png"));
            int i = localBufferedImage.getWidth();
            int j = localBufferedImage.getHeight();
            this.bgImage = localBufferedImage.getScaledInstance(i, j, 16);
            this.setPreferredSize(new Dimension(i + 32, j + 16 + 16));
        }
        catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public void update(Graphics paramGraphics) {
        this.paint(paramGraphics);
    }

    public void paintComponent(Graphics paramGraphics) {
        int i;
        paramGraphics.setColor(new Color(255, 255, 255));
        paramGraphics.fillRect(0, 0, 420, 74);
        paramGraphics.setColor(new Color(240, 240, 240));
        paramGraphics.fillRect(0, 74, 420, 6);
        for (i = 0; i < 2; ++i) {
            paramGraphics.setColor(new Color(153, 153, 153, 160 - (i + 1) * 53));
            paramGraphics.fillRect(0, 73 - i, 420, 1);
        }
        for (i = 0; i < 3; ++i) {
            paramGraphics.setColor(new Color(0, 0, 0, 160 - (i + 1) * 40));
            paramGraphics.fillRect(0, 80 + i, 420, 1);
        }
        paramGraphics.drawImage(this.bgImage, 24, 12, null);
    }
}

