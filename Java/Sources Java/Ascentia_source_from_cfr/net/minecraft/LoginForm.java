/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.minecraft.LauncherFrame;
import net.minecraft.OptionsPanel;
import net.minecraft.TexturedPanel;
import net.minecraft.TransparentButton;
import net.minecraft.TransparentCheckbox;
import net.minecraft.TransparentLabel;
import net.minecraft.TransparentPanel;
import net.minecraft.Util;

public class LoginForm
extends TransparentPanel {
    public static final File historyUsernameFile = new File(Util.getWorkingDirectory(), "username.history.txt");
    public static final List<String> historyUsername = new ArrayList<String>();
    public static final JTextField userName = new JTextField(16);
    public static final TransparentLabel errorLabel = new TransparentLabel("", 0);
    public static final TransparentButton loginButton = new TransparentButton("Connexion");
    public static final TransparentButton settingsButton = new TransparentButton("Options");
    public static final TransparentCheckbox rememberCheckbox = new TransparentCheckbox("Ajouter ce pseudo \u00e0 l'historique.");
    public static LauncherFrame launcherFrame;
    public static LoginPanel loginPanel;
    public static JPanel historyPanel;
    public static Thread[] iconsThread;
    public static int iconsThreadPos;

    public LoginForm(LauncherFrame paramLauncherFrame) {
        launcherFrame = paramLauncherFrame;
        this.setLayout(new BorderLayout());
        TexturedPanel background = new TexturedPanel();
        background.setLayout(new BorderLayout());
        this.add(background);
        Box box = new Box(1);
        box.setAlignmentX(0.5f);
        box.add(Box.createVerticalGlue());
        loginPanel = new LoginPanel(paramLauncherFrame);
        box.add(loginPanel);
        historyPanel = new JPanel(){
            private static final long serialVersionUID = 1;
            public static final int width = 640;
            public static final int height = 64;

            public Dimension getMinimumSize() {
                return new Dimension(640, 64);
            }

            public Dimension getMaximumSize() {
                return new Dimension(640, 64);
            }

            public Dimension getPreferredSize() {
                return new Dimension(640, 64);
            }
        };
        historyPanel.setBounds(0, 200, 640, 64);
        historyPanel.setLayout(null);
        historyPanel.setOpaque(false);
        loginPanel.add(historyPanel);
        box.add(Box.createVerticalGlue());
        background.add(box);
        try {
            if (historyUsernameFile.exists()) {
                int currentLine = 0;
                BufferedReader reader = new BufferedReader(new FileReader(historyUsernameFile));
                String ln = null;
                while ((ln = reader.readLine()) != null) {
                    if (!ln.matches("[a-zA-Z0-9_-]{3,16}")) continue;
                    historyUsername.add(ln);
                    if (++currentLine <= 2) continue;
                }
                reader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        int xM = 0;
        if (historyUsername.size() == 0) {
            rememberCheckbox.setSelected(true);
        }
        if (historyUsername.size() == 2) {
            xM = 106;
        } else if (historyUsername.size() <= 1) {
            xM = 213;
        }
        int i = 0;
        for (String name : historyUsername) {
            TransparentButton btn = this.addHistoryPlayer(name, 2 + i * 212 + xM, 0);
            ++i;
        }
        if (historyUsername.size() == 0) {
            TransparentButton btn = this.addHistoryPlayer("", 2 + i * 212 + xM, 0);
        }
    }

    public void setError(String error) {
        loginButton.setEnabled(true);
        errorLabel.setText(error);
    }

    public void doLogin(final String username) {
        loginButton.setEnabled(false);
        if (username.length() < 3 || username.length() > 16) {
            this.setError("Le pseudo doit faire entre 3 et 16 carract\u00e8res!");
            return;
        }
        if (rememberCheckbox.isSelected()) {
            this.saveUsernameHistory(username);
        }
        new Thread(){

            public void run() {
                try {
                    LoginForm.launcherFrame.login(username, "");
                }
                catch (Exception localException) {
                    LoginForm.this.setError(localException.toString());
                }
            }
        }.start();
    }

    public void saveUsernameHistory(String username) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(historyUsernameFile));
            ArrayList<String> tmpHistoryUsername = new ArrayList<String>();
            if (!username.equals("")) {
                tmpHistoryUsername.add(username);
            }
            for (String uname : historyUsername) {
                if (!tmpHistoryUsername.contains(uname)) {
                    tmpHistoryUsername.add(uname);
                }
                if (tmpHistoryUsername.size() != 3) continue;
                break;
            }
            historyUsername.clear();
            historyUsername.addAll(tmpHistoryUsername);
            for (String history : historyUsername) {
                writer.write(history, 0, history.length());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            // empty catch block
        }
    }

    public TransparentButton addHistoryPlayer(final String name, int x, int y) {
        final TransparentButton loginSkin1 = new TransparentButton(name);
        loginSkin1.setBounds(x, y, 200, 56);
        if (name.equals("")) {
            loginSkin1.setText("Connexion rapide");
            loginSkin1.setEnabled(false);
        } else {
            loginSkin1.setHorizontalAlignment(2);
            loginSkin1.setIconTextGap(12);
            loginSkin1.setIcon(new BlankImageIcon());
            new Thread(new Runnable(){

                public void run() {
                    SkinImageIcon sii = new SkinImageIcon(name);
                    boolean notready = true;
                    while (notready) {
                        try {
                            Thread.sleep(100);
                        }
                        catch (Exception e2) {
                            // empty catch block
                        }
                        AtomicBoolean e2 = sii.notready;
                        synchronized (e2) {
                            notready = sii.notready.get();
                            continue;
                        }
                    }
                    if (loginSkin1.isEnabled()) {
                        loginSkin1.setIcon(sii);
                    }
                }
            }).start();
        }
        loginSkin1.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                LoginForm.rememberCheckbox.setSelected(true);
                LoginForm.this.doLogin(name);
            }
        });
        if (!name.equals("")) {
            loginSkin1.setToolTipText("Clic droit pour retirer ce pseudo de l'historique.");
            loginSkin1.addMouseListener(new MouseAdapter(){

                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 3) {
                        loginSkin1.setIcon(null);
                        loginSkin1.setEnabled(false);
                        LoginForm.historyUsername.remove(loginSkin1.getText());
                        loginSkin1.setText("Supprim\u00e9");
                        LoginForm.this.saveUsernameHistory("");
                        loginSkin1.setToolTipText("");
                    }
                }
            });
        }
        historyPanel.add(loginSkin1);
        return loginSkin1;
    }

    static {
        iconsThread = new Thread[10];
        iconsThreadPos = 0;
    }

    public class BlankImageIcon
    extends ImageIcon {
        public int getIconWidth() {
            return 48;
        }

        public int getIconHeight() {
            return 48;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }
    }

    public class SkinImageIcon
    extends ImageIcon {
        public AtomicBoolean notready;
        public BufferedImage head;
        public BufferedImage hat;

        public SkinImageIcon(String name) {
            this.notready = new AtomicBoolean(true);
            this.head = null;
            this.hat = null;
            new Thread(new Runnable(LoginForm.this, name){
                final /* synthetic */ LoginForm val$this$0;
                final /* synthetic */ String val$name;

                public void run() {
                    BufferedImage bi;
                    try {
                        Thread.sleep(10);
                    }
                    catch (Exception e) {
                        // empty catch block
                    }
                    SkinImageIcon.this.head = bi = SkinImageIcon.this.getSkinImage(this.val$name, 8, 8, 16, 16, 0, 0, 6);
                    SkinImageIcon.this.hat = bi = SkinImageIcon.this.getSkinImage(this.val$name, 40, 8, 48, 16, 0, 0, 6);
                    AtomicBoolean atomicBoolean = SkinImageIcon.this.notready;
                    synchronized (atomicBoolean) {
                        SkinImageIcon.this.notready.set(false);
                    }
                }
            }).start();
        }

        public int getIconWidth() {
            return 48;
        }

        public int getIconHeight() {
            return 48;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            BufferedImage bufferedImage = this.head;
            synchronized (bufferedImage) {
                if (this.head != null) {
                    g.drawImage(this.head, x, y, null);
                }
            }
            bufferedImage = this.hat;
            synchronized (bufferedImage) {
                if (this.hat != null) {
                    g.drawImage(this.hat, x, y, null);
                }
            }
        }

        public BufferedImage getSkinImage(String username, int sx1, int sy1, int sx2, int sy2, int x, int y, int scale) {
            try {
                BufferedImage img;
                try {
                    img = ImageIO.read(new URL("http://ascentia-skin.fr/skins/" + username + ".png"));
                }
                catch (Exception e) {
                    img = ImageIO.read(new URL("http://ascentia-skin.fr/skins/char.png"));
                }
                int type = 2;
                BufferedImage resizedImage = new BufferedImage((sx2 - sx1) * scale, (sy2 - sy1) * scale, type);
                Graphics2D g = resizedImage.createGraphics();
                int asx2 = sx2;
                int asx1 = sx1;
                g.drawImage(img, 0, 0, (sx2 - sx1) * scale, (sy2 - sy1) * scale, asx1, sy1, asx2, sy2, null);
                g.dispose();
                return resizedImage;
            }
            catch (Exception e) {
                return null;
            }
        }

    }

    class LoginPanel
    extends JPanel {
        private static final long serialVersionUID = 1;
        public static final int width = 640;
        public static final int height = 360;

        public LoginPanel(LauncherFrame paramLauncherFrame) {
            TransparentButton buttonLink;
            this.setLayout(null);
            this.setOpaque(false);
            LoginForm.errorLabel.setBounds(0, 168, 640, 20);
            LoginForm.errorLabel.setFont(new Font(null, 0, 14));
            LoginForm.errorLabel.setForeground(new Color(255, 0, 0));
            this.add(LoginForm.errorLabel);
            TransparentLabel label = new TransparentLabel("Pseudo:");
            label.setBounds(185, 114, 100, 20);
            label.setFont(new Font(null, 0, 16));
            this.add(label);
            LoginForm.userName.setBounds(250, 114, 140, 22);
            this.add(LoginForm.userName);
            LoginForm.loginButton.setFont(new Font(null, 0, 14));
            LoginForm.loginButton.setBounds(393, 111, 114, 28);
            this.add(LoginForm.loginButton);
            LoginForm.rememberCheckbox.setBounds(185, 141, 190, 24);
            this.add(LoginForm.rememberCheckbox);
            LoginForm.settingsButton.setBounds(395, 141, 110, 24);
            this.add(LoginForm.settingsButton);
            LoginForm.loginButton.addActionListener(new ActionListener(LoginForm.this){
                final /* synthetic */ LoginForm val$this$0;

                public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                    LoginForm.this.doLogin(LoginForm.userName.getText());
                }
            });
            LoginForm.settingsButton.addActionListener(new ActionListener(LoginForm.this, paramLauncherFrame){
                final /* synthetic */ LoginForm val$this$0;
                final /* synthetic */ LauncherFrame val$paramLauncherFrame;

                public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                    new OptionsPanel(this.val$paramLauncherFrame).setVisible(true);
                }
            });
            int bw = 200;
            try {
                buttonLink = new TransparentButton("Nous suivre sur Facebook");
                buttonLink.setIcon(new ImageIcon(ImageIO.read(LoginForm.class.getResource("iconfb.png"))));
                buttonLink.setBounds(320 - bw / 2, 290, bw, 28);
                buttonLink.addHttpLink("http://ascentia.fr/modules/fb/fb.php");
                this.add(buttonLink);
            }
            catch (Exception e) {
                // empty catch block
            }
            try {
                buttonLink = new TransparentButton("Acheter des points VIP");
                buttonLink.setIcon(new ImageIcon(ImageIO.read(LoginForm.class.getResource("iconbuy.png"))));
                buttonLink.setBounds(320 - bw / 2 - bw - 8, 290, bw, 28);
                buttonLink.addHttpLink("http://shop.ascentia.fr/");
                this.add(buttonLink);
            }
            catch (Exception e) {
                // empty catch block
            }
            try {
                buttonLink = new TransparentButton("Voter pour le serveur");
                buttonLink.setIcon(new ImageIcon(ImageIO.read(LoginForm.class.getResource("iconvote.png"))));
                buttonLink.setBounds(320 + bw / 2 + 8, 290, bw, 28);
                buttonLink.addHttpLink("http://www.pactify.fr/");
                this.add(buttonLink);
            }
            catch (Exception e) {
                // empty catch block
            }
        }

        public Dimension getMinimumSize() {
            return new Dimension(640, 360);
        }

        public Dimension getMaximumSize() {
            return new Dimension(640, 360);
        }

        public Dimension getPreferredSize() {
            return new Dimension(640, 360);
        }

    }

}

