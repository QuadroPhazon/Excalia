/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.minecraft.GameUpdater;
import net.minecraft.TransparentLabel;
import net.minecraft.Util;

public class OptionsPanel
extends JDialog {
    private static final long serialVersionUID = 1;

    public OptionsPanel(Frame paramFrame) {
        super(paramFrame);
        this.setModal(true);
        JPanel localJPanel1 = new JPanel(new BorderLayout());
        JLabel localJLabel = new JLabel("Launcher options", 0);
        localJLabel.setBorder(new EmptyBorder(0, 0, 16, 0));
        localJLabel.setFont(new Font("Default", 1, 16));
        localJPanel1.add((Component)localJLabel, "North");
        JPanel localJPanel2 = new JPanel(new BorderLayout());
        JPanel localJPanel3 = new JPanel(new GridLayout(0, 1));
        JPanel localJPanel4 = new JPanel(new GridLayout(0, 1));
        localJPanel2.add((Component)localJPanel3, "West");
        localJPanel2.add((Component)localJPanel4, "Center");
        final JButton localJButton1 = new JButton("Force update!");
        localJButton1.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                GameUpdater.forceUpdate = true;
                localJButton1.setText("Will force!");
                localJButton1.setEnabled(false);
            }
        });
        localJPanel3.add(new JLabel("Force game update: ", 4));
        localJPanel4.add(localJButton1);
        localJPanel3.add(new JLabel("Game location on disk: ", 4));
        TransparentLabel local2 = new TransparentLabel(Util.getWorkingDirectory().toString()){
            private static final long serialVersionUID = 0;

            public void paint(Graphics paramAnonymousGraphics) {
                super.paint(paramAnonymousGraphics);
                int i = 0;
                int j = 0;
                FontMetrics localFontMetrics = paramAnonymousGraphics.getFontMetrics();
                int k = localFontMetrics.stringWidth(this.getText());
                int m = localFontMetrics.getHeight();
                if (this.getAlignmentX() == 2.0f) {
                    i = 0;
                } else if (this.getAlignmentX() == 0.0f) {
                    i = this.getBounds().width / 2 - k / 2;
                } else if (this.getAlignmentX() == 4.0f) {
                    i = this.getBounds().width - k;
                }
                j = this.getBounds().height / 2 + m / 2 - 1;
                paramAnonymousGraphics.drawLine(i + 2, j, i + k - 2, j);
            }

            public void update(Graphics paramAnonymousGraphics) {
                this.paint(paramAnonymousGraphics);
            }
        };
        local2.setCursor(Cursor.getPredefinedCursor(12));
        local2.addMouseListener(new MouseAdapter(){

            public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
                try {
                    Util.openLink(Util.getWorkingDirectory().toURI());
                }
                catch (Exception localException) {
                    localException.printStackTrace();
                }
            }
        });
        local2.setForeground(new Color(2105599));
        localJPanel4.add(local2);
        localJPanel1.add((Component)localJPanel2, "Center");
        JPanel localJPanel5 = new JPanel(new BorderLayout());
        localJPanel5.add((Component)new JPanel(), "Center");
        JButton localJButton2 = new JButton("Done");
        localJButton2.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent paramAnonymousActionEvent) {
                OptionsPanel.this.setVisible(false);
            }
        });
        localJPanel5.add((Component)localJButton2, "East");
        localJPanel5.setBorder(new EmptyBorder(16, 0, 0, 0));
        localJPanel1.add((Component)localJPanel5, "South");
        this.add(localJPanel1);
        localJPanel1.setBorder(new EmptyBorder(16, 24, 24, 24));
        this.pack();
        this.setLocationRelativeTo(paramFrame);
    }

}

