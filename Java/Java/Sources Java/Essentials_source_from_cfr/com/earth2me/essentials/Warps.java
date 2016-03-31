/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 */
package com.earth2me.essentials;

import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.IConf;
import com.earth2me.essentials.commands.WarpNotFoundException;
import com.earth2me.essentials.utils.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IWarps;
import net.ess3.api.InvalidNameException;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.Server;

public class Warps
implements IConf,
IWarps {
    private static final Logger logger = Logger.getLogger("Essentials");
    private final Map<StringIgnoreCase, EssentialsConf> warpPoints = new HashMap<StringIgnoreCase, EssentialsConf>();
    private final File warpsFolder;
    private final Server server;

    public Warps(Server server, File dataFolder) {
        this.server = server;
        this.warpsFolder = new File(dataFolder, "warps");
        if (!this.warpsFolder.exists()) {
            this.warpsFolder.mkdirs();
        }
        this.reloadConfig();
    }

    @Override
    public boolean isEmpty() {
        return this.warpPoints.isEmpty();
    }

    @Override
    public Collection<String> getList() {
        ArrayList<String> keys = new ArrayList<String>();
        for (StringIgnoreCase stringIgnoreCase : this.warpPoints.keySet()) {
            keys.add(stringIgnoreCase.getString());
        }
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        return keys;
    }

    @Override
    public Location getWarp(String warp) throws WarpNotFoundException, InvalidWorldException {
        EssentialsConf conf = this.warpPoints.get(new StringIgnoreCase(warp));
        if (conf == null) {
            throw new WarpNotFoundException();
        }
        return conf.getLocation(null, this.server);
    }

    @Override
    public void setWarp(String name, Location loc) throws Exception {
        String filename = StringUtil.sanitizeFileName(name);
        EssentialsConf conf = this.warpPoints.get(new StringIgnoreCase(name));
        if (conf == null) {
            File confFile = new File(this.warpsFolder, filename + ".yml");
            if (confFile.exists()) {
                throw new Exception(I18n._("similarWarpExist", new Object[0]));
            }
            conf = new EssentialsConf(confFile);
            this.warpPoints.put(new StringIgnoreCase(name), conf);
        }
        conf.setProperty(null, loc);
        conf.setProperty("name", name);
        try {
            conf.saveWithError();
        }
        catch (IOException ex) {
            throw new IOException(I18n._("invalidWarpName", new Object[0]));
        }
    }

    @Override
    public void removeWarp(String name) throws Exception {
        EssentialsConf conf = this.warpPoints.get(new StringIgnoreCase(name));
        if (conf == null) {
            throw new Exception(I18n._("warpNotExist", new Object[0]));
        }
        if (!conf.getFile().delete()) {
            throw new Exception(I18n._("warpDeleteError", new Object[0]));
        }
        this.warpPoints.remove(new StringIgnoreCase(name));
    }

    @Override
    public final void reloadConfig() {
        this.warpPoints.clear();
        File[] listOfFiles = this.warpsFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (int i = 0; i < listOfFiles.length; ++i) {
                String filename = listOfFiles[i].getName();
                if (!listOfFiles[i].isFile() || !filename.endsWith(".yml")) continue;
                try {
                    EssentialsConf conf = new EssentialsConf(listOfFiles[i]);
                    conf.load();
                    String name = conf.getString("name");
                    if (name == null) continue;
                    this.warpPoints.put(new StringIgnoreCase(name), conf);
                    continue;
                }
                catch (Exception ex) {
                    logger.log(Level.WARNING, I18n._("loadWarpError", filename), ex);
                }
            }
        }
    }

    @Override
    public File getWarpFile(String name) throws InvalidNameException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCount() {
        return this.getList().size();
    }

    private static class StringIgnoreCase {
        private final String string;

        public StringIgnoreCase(String string) {
            this.string = string;
        }

        public int hashCode() {
            return this.getString().toLowerCase(Locale.ENGLISH).hashCode();
        }

        public boolean equals(Object o) {
            if (o instanceof StringIgnoreCase) {
                return this.getString().equalsIgnoreCase(((StringIgnoreCase)o).getString());
            }
            return false;
        }

        public String getString() {
            return this.string;
        }
    }

}

