/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import net.minecraft.Launcher;
import net.minecraft.LoginForm;

public class LauncherFrame
extends Frame {
    public static final int VERSION = 13;
    private static final long serialVersionUID = 1;
    public Map<String, String> customParameters = new HashMap<String, String>();
    public Launcher launcher;
    public LoginForm loginForm;

    public LauncherFrame() {
        super("Ascentia Launcher");
        this.setBackground(Color.BLACK);
        this.loginForm = new LoginForm(this);
        JPanel localJPanel = new JPanel();
        localJPanel.setLayout(new BorderLayout());
        localJPanel.add((Component)this.loginForm, "Center");
        localJPanel.setPreferredSize(new Dimension(854, 480));
        localJPanel.setMinimumSize(new Dimension(660, 380));
        this.setMinimumSize(new Dimension(660, 380));
        this.setLayout(new BorderLayout());
        this.add((Component)localJPanel, "Center");
        this.pack();
        this.setLocationRelativeTo(null);
        try {
            this.setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
        }
        catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
                new Thread(){

                    public void run() {
                        try {
                            Thread.sleep(30000);
                        }
                        catch (InterruptedException localInterruptedException) {
                            localInterruptedException.printStackTrace();
                        }
                        System.out.println("FORCING EXIT!");
                        System.exit(0);
                    }
                }.start();
                if (LauncherFrame.this.launcher != null) {
                    LauncherFrame.this.launcher.stop();
                    LauncherFrame.this.launcher.destroy();
                }
                System.exit(0);
            }

        });
    }

    public void playCached(String paramString, boolean paramBoolean) {
        try {
            if (paramString == null || paramString.length() <= 0) {
                paramString = "Player";
            }
            this.launcher = new Launcher();
            this.launcher.customParameters.putAll(this.customParameters);
            this.launcher.customParameters.put("userName", paramString);
            this.launcher.customParameters.put("demo", "" + paramBoolean);
            this.launcher.customParameters.put("sessionId", "1");
            this.launcher.init();
            this.removeAll();
            this.add((Component)this.launcher, "Center");
            this.validate();
            this.launcher.start();
            this.loginForm = null;
            this.setTitle("Minecraft");
        }
        catch (Exception localException) {
            localException.printStackTrace();
            this.showError(localException.toString());
        }
    }

    public void login(String paramString1, String paramString2) {
        try {
            this.launcher = new Launcher();
            this.launcher.customParameters.putAll(this.customParameters);
            this.launcher.customParameters.put("userName", paramString1);
            this.launcher.customParameters.put("latestVersion", "abcd");
            this.launcher.customParameters.put("downloadTicket", "abcd");
            this.launcher.customParameters.put("sessionId", "abcd");
            this.launcher.init();
            this.removeAll();
            this.add((Component)this.launcher, "Center");
            this.validate();
            this.launcher.start();
            this.loginForm = null;
            this.setTitle("Minecraft");
        }
        catch (Exception localException) {
            localException.printStackTrace();
            this.showError(localException.toString());
        }
    }

    private void showError(String paramString) {
        this.removeAll();
        this.add(this.loginForm);
        this.validate();
    }

    public boolean canPlayOffline(String paramString) {
        Launcher localLauncher = new Launcher();
        localLauncher.customParameters.putAll(this.customParameters);
        localLauncher.init(paramString, null, null, "1");
        return localLauncher.canPlayOffline();
    }

    public static void main(String[] paramArrayOfString) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception localException) {
            // empty catch block
        }
        System.out.println("asdf");
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
        LauncherFrame localLauncherFrame = new LauncherFrame();
        localLauncherFrame.setVisible(true);
        localLauncherFrame.customParameters.put("stand-alone", "true");
        String str1 = null;
        String str2 = null;
        for (String str4 : paramArrayOfString) {
            if (str4.startsWith("-u=") || str4.startsWith("--user=")) {
                str1 = LauncherFrame.getArgValue(str4);
                localLauncherFrame.customParameters.put("username", str1);
                LoginForm.userName.setText(str1);
                continue;
            }
            if (str4.startsWith("-p=") || str4.startsWith("--password=")) {
                str2 = LauncherFrame.getArgValue(str4);
                continue;
            }
            if (!str4.startsWith("--noupdate")) continue;
            localLauncherFrame.customParameters.put("noupdate", "true");
        }
        if (paramArrayOfString.length >= 3) {
            String ip = paramArrayOfString[2];
            String str3 = "25565";
            if (ip.contains(":")) {
                String[] arrayOfString = ip.split(":");
                ip = arrayOfString[0];
                str3 = arrayOfString[1];
            }
            localLauncherFrame.customParameters.put("server", ip);
            localLauncherFrame.customParameters.put("port", str3);
        }
    }

    private static String getArgValue(String paramString) {
        int i = paramString.indexOf(61);
        if (i < 0) {
            return "";
        }
        return paramString.substring(i + 1);
    }

}

