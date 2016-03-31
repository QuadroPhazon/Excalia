/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import net.minecraft.LoginForm;

public class TexturedPanel
extends JPanel {
    private static final long serialVersionUID = 1;
    private Image img;
    private Image bgImage;

    public TexturedPanel() {
        this.setOpaque(true);
        try {
            this.bgImage = ImageIO.read(LoginForm.class.getResource("background.jpg")).getScaledInstance(1920, 1080, 16);
        }
        catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public void update(Graphics paramGraphics) {
        this.paint(paramGraphics);
    }

    public void paintComponent(Graphics paramGraphics) {
        paramGraphics.drawImage(this.bgImage, this.getWidth() / 2 - 960, this.getHeight() / 2 - 540, 1920, 1080, null);
    }
}

