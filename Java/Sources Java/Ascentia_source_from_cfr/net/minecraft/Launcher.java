/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.GameUpdater;
import net.minecraft.LoginForm;

public class Launcher
extends Applet
implements Runnable,
AppletStub,
MouseListener {
    private static final long serialVersionUID = 1;
    public Map<String, String> customParameters = new HashMap<String, String>();
    private GameUpdater gameUpdater;
    private boolean gameUpdaterStarted = false;
    private Applet applet;
    private Image bgImage;
    private boolean active = false;
    private int context = 0;
    private boolean hasMouseListener = false;
    private VolatileImage img;

    public boolean isActive() {
        if (this.context == 0) {
            this.context = -1;
            try {
                if (this.getAppletContext() != null) {
                    this.context = 1;
                }
            }
            catch (Exception localException) {
                // empty catch block
            }
        }
        if (this.context == -1) {
            return this.active;
        }
        return super.isActive();
    }

    public void init(String paramString1, String paramString2, String paramString3, String paramString4) {
        try {
            this.bgImage = ImageIO.read(LoginForm.class.getResource("dirt.png")).getScaledInstance(32, 32, 16);
        }
        catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        this.customParameters.put("username", paramString1);
        this.customParameters.put("sessionid", paramString4);
        this.gameUpdater = new GameUpdater(this, paramString2, "http://www.pactify.fr/launcher/minecraft.jar");
    }

    public boolean canPlayOffline() {
        return this.gameUpdater.canPlayOffline();
    }

    public void init() {
        if (this.applet != null) {
            this.applet.init();
            return;
        }
        this.init(this.getParameter("userName"), this.getParameter("latestVersion"), this.getParameter("downloadTicket"), this.getParameter("sessionId"));
    }

    public void start() {
        if (this.applet != null) {
            this.applet.start();
            return;
        }
        if (this.gameUpdaterStarted) {
            return;
        }
        Thread localObject = new Thread(){

            public void run() {
                Launcher.this.gameUpdater.run();
                try {
                    if (!Launcher.access$000((Launcher)Launcher.this).fatalError) {
                        Launcher.this.replace(Launcher.this.gameUpdater.createApplet());
                    }
                }
                catch (ClassNotFoundException localClassNotFoundException) {
                    localClassNotFoundException.printStackTrace();
                }
                catch (InstantiationException localInstantiationException) {
                    localInstantiationException.printStackTrace();
                }
                catch (IllegalAccessException localIllegalAccessException) {
                    localIllegalAccessException.printStackTrace();
                }
            }
        };
        ((Thread)localObject).setDaemon(true);
        ((Thread)localObject).start();
        localObject = new Thread(){

            public void run() {
                while (Launcher.this.applet == null) {
                    Launcher.this.repaint();
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException localInterruptedException) {
                        localInterruptedException.printStackTrace();
                    }
                }
            }
        };
        ((Thread)localObject).setDaemon(true);
        ((Thread)localObject).start();
        this.gameUpdaterStarted = true;
    }

    public void stop() {
        if (this.applet != null) {
            this.active = false;
            this.applet.stop();
        }
    }

    public void destroy() {
        if (this.applet != null) {
            this.applet.destroy();
        }
    }

    public void replace(Applet paramApplet) {
        this.applet = paramApplet;
        paramApplet.setStub(this);
        paramApplet.setSize(this.getWidth(), this.getHeight());
        this.setLayout(new BorderLayout());
        this.add((Component)paramApplet, "Center");
        paramApplet.init();
        this.active = true;
        paramApplet.start();
        this.validate();
    }

    public void update(Graphics paramGraphics) {
        this.paint(paramGraphics);
    }

    public void paint(Graphics paramGraphics) {
        if (this.applet != null) {
            return;
        }
        int i = this.getWidth() / 2;
        int j = this.getHeight() / 2;
        if (this.img == null || this.img.getWidth() != i || this.img.getHeight() != j) {
            this.img = this.createVolatileImage(i, j);
        }
        Graphics localGraphics = this.img.getGraphics();
        for (int k = 0; k <= i / 32; ++k) {
            for (int m = 0; m <= j / 32; ++m) {
                localGraphics.drawImage(this.bgImage, k * 32, m * 32, null);
            }
        }
        if (this.gameUpdater.pauseAskUpdate) {
            if (!this.hasMouseListener) {
                this.hasMouseListener = true;
                this.addMouseListener(this);
            }
            localGraphics.setColor(Color.LIGHT_GRAY);
            String str = "New update available";
            localGraphics.setFont(new Font(null, 1, 20));
            FontMetrics localFontMetrics = localGraphics.getFontMetrics();
            localGraphics.drawString(str, i / 2 - localFontMetrics.stringWidth(str) / 2, j / 2 - localFontMetrics.getHeight() * 2);
            localGraphics.setFont(new Font(null, 0, 12));
            localFontMetrics = localGraphics.getFontMetrics();
            localGraphics.fill3DRect(i / 2 - 56 - 8, j / 2, 56, 20, true);
            localGraphics.fill3DRect(i / 2 + 8, j / 2, 56, 20, true);
            str = "Would you like to update?";
            localGraphics.drawString(str, i / 2 - localFontMetrics.stringWidth(str) / 2, j / 2 - 8);
            localGraphics.setColor(Color.BLACK);
            str = "Yes";
            localGraphics.drawString(str, i / 2 - 56 - 8 - localFontMetrics.stringWidth(str) / 2 + 28, j / 2 + 14);
            str = "Not now";
            localGraphics.drawString(str, i / 2 + 8 - localFontMetrics.stringWidth(str) / 2 + 28, j / 2 + 14);
        } else {
            localGraphics.setColor(Color.LIGHT_GRAY);
            String str = "Updating Minecraft";
            if (this.gameUpdater.fatalError) {
                str = "Failed to launch";
            }
            localGraphics.setFont(new Font(null, 1, 20));
            FontMetrics localFontMetrics = localGraphics.getFontMetrics();
            localGraphics.drawString(str, i / 2 - localFontMetrics.stringWidth(str) / 2, j / 2 - localFontMetrics.getHeight() * 2);
            localGraphics.setFont(new Font(null, 0, 12));
            localFontMetrics = localGraphics.getFontMetrics();
            str = this.gameUpdater.getDescriptionForState();
            if (this.gameUpdater.fatalError) {
                str = this.gameUpdater.fatalErrorDescription;
            } else if (this.gameUpdater.preventErrorTime) {
                str = this.gameUpdater.preventError;
            }
            localGraphics.drawString(str, i / 2 - localFontMetrics.stringWidth(str) / 2, j / 2 + localFontMetrics.getHeight() * 1);
            str = this.gameUpdater.subtaskMessage;
            localGraphics.drawString(str, i / 2 - localFontMetrics.stringWidth(str) / 2, j / 2 + localFontMetrics.getHeight() * 2);
            if (!this.gameUpdater.fatalError) {
                localGraphics.setColor(Color.black);
                localGraphics.fillRect(64, j - 64, i - 128 + 1, 5);
                localGraphics.setColor(new Color(32768));
                localGraphics.fillRect(64, j - 64, this.gameUpdater.percentage * (i - 128) / 100, 4);
                localGraphics.setColor(new Color(2138144));
                localGraphics.fillRect(65, j - 64 + 1, this.gameUpdater.percentage * (i - 128) / 100 - 2, 1);
            }
        }
        localGraphics.dispose();
        paramGraphics.drawImage(this.img, 0, 0, i * 2, j * 2, null);
    }

    public void run() {
    }

    public String getParameter(String paramString) {
        String str = this.customParameters.get(paramString);
        if (str != null) {
            return str;
        }
        try {
            return super.getParameter(paramString);
        }
        catch (Exception localException) {
            this.customParameters.put(paramString, null);
            return null;
        }
    }

    public void appletResize(int paramInt1, int paramInt2) {
    }

    public URL getDocumentBase() {
        try {
            return new URL("http://www.minecraft.net/game/");
        }
        catch (MalformedURLException localMalformedURLException) {
            localMalformedURLException.printStackTrace();
            return null;
        }
    }

    public void mouseClicked(MouseEvent paramMouseEvent) {
    }

    public void mouseEntered(MouseEvent paramMouseEvent) {
    }

    public void mouseExited(MouseEvent paramMouseEvent) {
    }

    public void mousePressed(MouseEvent paramMouseEvent) {
        int j;
        int m;
        int k;
        int i = paramMouseEvent.getX() / 2;
        if (this.contains(i, j = paramMouseEvent.getY() / 2, (k = this.getWidth() / 2) / 2 - 56 - 8, (m = this.getHeight() / 2) / 2, 56, 20)) {
            this.removeMouseListener(this);
            this.gameUpdater.shouldUpdate = true;
            this.gameUpdater.pauseAskUpdate = false;
            this.hasMouseListener = false;
        }
        if (this.contains(i, j, k / 2 + 8, m / 2, 56, 20)) {
            this.removeMouseListener(this);
            this.gameUpdater.shouldUpdate = false;
            this.gameUpdater.pauseAskUpdate = false;
            this.hasMouseListener = false;
        }
    }

    private boolean contains(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        return paramInt1 >= paramInt3 && paramInt2 >= paramInt4 && paramInt1 < paramInt3 + paramInt5 && paramInt2 < paramInt4 + paramInt6;
    }

    public void mouseReleased(MouseEvent paramMouseEvent) {
    }

}

