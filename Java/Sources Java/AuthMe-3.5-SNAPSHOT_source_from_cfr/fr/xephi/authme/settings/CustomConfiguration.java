/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package fr.xephi.authme.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomConfiguration
extends YamlConfiguration {
    private File configFile;

    public CustomConfiguration(File file) {
        this.configFile = file;
        this.load();
    }

    public void load() {
        try {
            super.load(this.configFile);
        }
        catch (FileNotFoundException e) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not find " + this.configFile.getName() + ", creating new one...");
            this.reLoad();
        }
        catch (IOException e) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not load " + this.configFile.getName(), e);
        }
        catch (InvalidConfigurationException e) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, this.configFile.getName() + " is no valid configuration file", (Throwable)e);
        }
    }

    public boolean reLoad() {
        boolean out = true;
        if (!this.configFile.exists()) {
            out = this.loadRessource(this.configFile);
        }
        if (out) {
            this.load();
        }
        return out;
    }

    public void save() {
        try {
            super.save(this.configFile);
        }
        catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + this.configFile.getName(), ex);
        }
    }

    public boolean loadRessource(File file) {
        boolean out = true;
        if (!file.exists()) {
            try {
                String str;
                InputStream fis = this.getClass().getResourceAsStream("/" + file.getName());
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8").newDecoder()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(file), Charset.forName("UTF-8").newEncoder()));
                while ((str = reader.readLine()) != null) {
                    writer.append(str).append("\r\n");
                }
                writer.flush();
                writer.close();
                reader.close();
                fis.close();
            }
            catch (Exception e) {
                Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Failed to load config from JAR");
                out = false;
            }
        }
        return out;
    }
}

