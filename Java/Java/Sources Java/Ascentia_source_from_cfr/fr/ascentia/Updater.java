/*
 * Decompiled with CFR 0_110.
 */
package fr.ascentia;

import fr.ascentia.AsyncDownloader;
import fr.ascentia.Util;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Updater {
    public static void main(String[] paramArrayOfString) throws Exception {
        block23 : {
            boolean shouldUpdate = false;
            File jar = new File(Util.getWorkingDirectory(), "AscentiaLauncher.jar");
            String version = "";
            if (!jar.exists()) {
                shouldUpdate = true;
            }
            StringBuffer sb = new StringBuffer();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                FileInputStream fis = new FileInputStream(jar);
                byte[] dataBytes = new byte[1024];
                int nread = 0;
                while ((nread = fis.read(dataBytes)) != -1) {
                    md.update(dataBytes, 0, nread);
                }
                byte[] mdbytes = md.digest();
                for (int i = 0; i < mdbytes.length; ++i) {
                    sb.append(Integer.toString((mdbytes[i] & 255) + 256, 16).substring(1));
                }
            }
            catch (Exception e) {
                shouldUpdate = true;
            }
            String currentVersion = sb.toString();
            for (int tryingLeft = 2; tryingLeft > 0; --tryingLeft) {
                try {
                    String inputLine;
                    URL versionUrl = new URL("http://ascentia.fr/launcher/updater.asversion");
                    BufferedReader inUrl = new BufferedReader(new InputStreamReader(versionUrl.openStream()));
                    while ((inputLine = inUrl.readLine()) != null) {
                        version = version + inputLine;
                    }
                    inUrl.close();
                    if (!(version == null || version.equals("") || currentVersion.equals("") || version.equals(currentVersion))) {
                        shouldUpdate = true;
                    }
                    tryingLeft = 0;
                    continue;
                }
                catch (Exception e) {
                    if (tryingLeft > 1) {
                        System.out.println("Check Update : First try failed. Wait 100ms before second try.");
                        Thread.sleep(100);
                        continue;
                    }
                    System.out.println("Check Update : failed.");
                }
            }
            JFrame frame = new JFrame("Ascentia Updater");
            frame.setDefaultCloseOperation(3);
            JPanel localJPanel = new JPanel();
            localJPanel.setLayout(null);
            localJPanel.setPreferredSize(new Dimension(640, 80));
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setBounds(5, 5, 630, 48);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            localJPanel.add(progressBar);
            JLabel progressText = new JLabel("Initialisation de la mise \u00e0 jour du launcher.");
            progressText.setBounds(10, 54, 620, 22);
            localJPanel.add(progressText);
            frame.getContentPane().add((Component)localJPanel, "Center");
            if (shouldUpdate) {
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                if (!version.equals("")) {
                    AsyncDownloader adl = new AsyncDownloader("http://ascentia.fr/launcher/jars/AscentiaLauncher_" + version + ".jar", jar);
                    while (adl.running) {
                        if (adl.error != null) {
                            progressText.setText(adl.error);
                            continue;
                        }
                        progressBar.setValue(adl.status);
                    }
                } else {
                    progressText.setText("Impossible de se connecter au serveur de mise \u00e0 jour!");
                    do {
                        // Infinite loop
                    } while (true);
                }
            }
            try {
                ArrayList<String> localArrayList = new ArrayList<String>();
                if (Util.getPlatform().equals((Object)Util.OS.windows)) {
                    localArrayList.add("javaw");
                } else {
                    localArrayList.add("java");
                }
                localArrayList.add("-Xmx512m");
                localArrayList.add("-Dsun.java2d.noddraw=true");
                localArrayList.add("-Dsun.java2d.d3d=false");
                localArrayList.add("-Dsun.java2d.opengl=false");
                localArrayList.add("-Dsun.java2d.pmoffscreen=false");
                localArrayList.add("-jar");
                localArrayList.add(jar.getPath());
                ProcessBuilder localProcessBuilder = new ProcessBuilder(localArrayList);
                Process localProcess = localProcessBuilder.start();
                if (localProcess == null) {
                    throw new Exception("!");
                }
                System.exit(0);
                break block23;
            }
            catch (Exception e) {
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                progressText.setText("Fatal Error: " + e.getMessage());
            }
            do {
                // Infinite loop
            } while (true);
        }
    }
}

