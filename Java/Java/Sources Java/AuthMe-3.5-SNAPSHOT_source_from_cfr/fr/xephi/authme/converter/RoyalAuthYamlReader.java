/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.converter;

import fr.xephi.authme.settings.CustomConfiguration;
import java.io.File;

public class RoyalAuthYamlReader
extends CustomConfiguration {
    public RoyalAuthYamlReader(File file) {
        super(file);
        this.load();
        this.save();
    }

    public long getLastLogin() {
        return this.getLong("timestamps.quit");
    }

    public String getHash() {
        return this.getString("login.password");
    }
}

