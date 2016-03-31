/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.settings;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;
import java.util.ArrayList;

public class SpoutCfg
extends CustomConfiguration {
    private static SpoutCfg instance = null;

    public SpoutCfg(File file) {
        super(file);
        this.loadDefaults();
        this.load();
        this.save();
    }

    private void loadDefaults() {
        this.set("Spout GUI enabled", (Object)true);
        this.set("LoginScreen.enabled", (Object)true);
        this.set("LoginScreen.exit button", (Object)"Quit");
        this.set("LoginScreen.exit message", (Object)"Good Bye");
        this.set("LoginScreen.login button", (Object)"Login");
        this.set("LoginScreen.title", (Object)"LOGIN");
        this.set("LoginScreen.text", (Object)new ArrayList<String>(){});
    }

    public static SpoutCfg getInstance() {
        if (instance == null) {
            instance = new SpoutCfg(new File("plugins" + File.separator + "AuthMe", "spout.yml"));
        }
        return instance;
    }

}

