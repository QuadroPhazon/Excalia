/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.plugin.manager;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EssKit
extends CustomConfiguration {
    private static EssKit config;

    public EssKit() {
        super(new File("." + File.separator + "plugins" + File.separator + "Essentials" + File.separator + "config.yml"));
        config = this;
        this.load();
    }

    public static EssKit getInstance() {
        if (config == null) {
            config = new EssKit();
        }
        return config;
    }

    public String getKitName() {
        if (config == null) {
            config = new EssKit();
        }
        return config.getString("newbies.kit");
    }

    public List<String> getKit(String name) {
        if (config == null) {
            config = new EssKit();
        }
        return config.getStringList("kits." + name + ".items");
    }
}

