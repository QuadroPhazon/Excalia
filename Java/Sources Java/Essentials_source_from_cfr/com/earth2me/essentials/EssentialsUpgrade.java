/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials;

import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.ManagedFile;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.craftbukkit.FakeWorld;
import com.earth2me.essentials.settings.Jails;
import com.earth2me.essentials.settings.Spawns;
import com.earth2me.essentials.storage.StorageObject;
import com.earth2me.essentials.storage.YamlStorageWriter;
import com.earth2me.essentials.utils.StringUtil;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EssentialsUpgrade {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private final transient IEssentials ess;
    private final transient EssentialsConf doneFile;

    EssentialsUpgrade(IEssentials essentials) {
        this.ess = essentials;
        if (!this.ess.getDataFolder().exists()) {
            this.ess.getDataFolder().mkdirs();
        }
        this.doneFile = new EssentialsConf(new File(this.ess.getDataFolder(), "upgrades-done.yml"));
        this.doneFile.load();
    }

    private void moveWorthValuesToWorthYml() {
        if (this.doneFile.getBoolean("moveWorthValuesToWorthYml", false)) {
            return;
        }
        try {
            File configFile = new File(this.ess.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                return;
            }
            EssentialsConf conf = new EssentialsConf(configFile);
            conf.load();
            Worth worth = new Worth(this.ess.getDataFolder());
            boolean found = false;
            for (Material mat : Material.values()) {
                int id = mat.getId();
                double value = conf.getDouble("worth-" + id, Double.NaN);
                if (Double.isNaN(value)) continue;
                found = true;
                worth.setPrice(new ItemStack(mat, 1, 0, Byte.valueOf(0)), value);
            }
            if (found) {
                this.removeLinesFromConfig(configFile, "\\s*#?\\s*worth-[0-9]+.*", "# Worth values have been moved to worth.yml");
            }
            this.doneFile.setProperty("moveWorthValuesToWorthYml", true);
            this.doneFile.save();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, I18n._("upgradingFilesError", new Object[0]), e);
        }
    }

    private void moveMotdRulesToFile(String name) {
        if (this.doneFile.getBoolean("move" + name + "ToFile", false)) {
            return;
        }
        try {
            File file = new File(this.ess.getDataFolder(), name + ".txt");
            if (file.exists()) {
                return;
            }
            File configFile = new File(this.ess.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                return;
            }
            EssentialsConf conf = new EssentialsConf(configFile);
            conf.load();
            List<String> lines = conf.getStringList(name);
            if (lines != null && !lines.isEmpty()) {
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create file " + file);
                }
                PrintWriter writer = new PrintWriter(file);
                for (String line : lines) {
                    writer.println(line);
                }
                writer.close();
            }
            this.doneFile.setProperty("move" + name + "ToFile", true);
            this.doneFile.save();
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, I18n._("upgradingFilesError", new Object[0]), e);
        }
    }

    private void removeLinesFromConfig(File file, String regex, String info) throws Exception {
        String line;
        boolean needUpdate = false;
        BufferedReader bReader = new BufferedReader(new FileReader(file));
        File tempFile = File.createTempFile("essentialsupgrade", ".tmp.yml", this.ess.getDataFolder());
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(tempFile));
        while ((line = bReader.readLine()) != null) {
            if (line.matches(regex)) {
                if (!needUpdate && info != null) {
                    bWriter.write(info, 0, info.length());
                    bWriter.newLine();
                }
                needUpdate = true;
                continue;
            }
            if (line.endsWith("\r\n")) {
                bWriter.write(line, 0, line.length() - 2);
            } else if (line.endsWith("\r") || line.endsWith("\n")) {
                bWriter.write(line, 0, line.length() - 1);
            } else {
                bWriter.write(line, 0, line.length());
            }
            bWriter.newLine();
        }
        bReader.close();
        bWriter.close();
        if (needUpdate) {
            if (!file.renameTo(new File(file.getParentFile(), file.getName().concat("." + System.currentTimeMillis() + ".upgradebackup")))) {
                throw new Exception(I18n._("configFileMoveError", new Object[0]));
            }
            if (!tempFile.renameTo(file)) {
                throw new Exception(I18n._("configFileRenameError", new Object[0]));
            }
        } else {
            tempFile.delete();
        }
    }

    private void updateUsersToNewDefaultHome() {
        File[] userFiles;
        if (this.doneFile.getBoolean("updateUsersToNewDefaultHome", false)) {
            return;
        }
        File userdataFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            return;
        }
        for (File file : userFiles = userdataFolder.listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(".yml")) continue;
            EssentialsConf config = new EssentialsConf(file);
            try {
                List vals;
                config.load();
                if (!config.hasProperty("home") || config.hasProperty("home.default") || (vals = (List)config.getProperty("home")) == null) continue;
                World world = (World)this.ess.getServer().getWorlds().get(0);
                if (vals.size() > 5) {
                    world = this.ess.getServer().getWorld((String)vals.get(5));
                }
                if (world == null) continue;
                Location loc = new Location(world, ((Number)vals.get(0)).doubleValue(), ((Number)vals.get(1)).doubleValue(), ((Number)vals.get(2)).doubleValue(), ((Number)vals.get(3)).floatValue(), ((Number)vals.get(4)).floatValue());
                String worldName = world.getName().toLowerCase(Locale.ENGLISH);
                if (worldName == null || worldName.isEmpty()) continue;
                config.removeProperty("home");
                config.setProperty("home.default", worldName);
                config.setProperty("home.worlds." + worldName, loc);
                config.forceSave();
                continue;
            }
            catch (RuntimeException ex) {
                LOGGER.log(Level.INFO, "File: " + file.toString());
                throw ex;
            }
        }
        this.doneFile.setProperty("updateUsersToNewDefaultHome", true);
        this.doneFile.save();
    }

    private void updateUsersPowerToolsFormat() {
        File[] userFiles;
        if (this.doneFile.getBoolean("updateUsersPowerToolsFormat", false)) {
            return;
        }
        File userdataFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            return;
        }
        for (File file : userFiles = userdataFolder.listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(".yml")) continue;
            EssentialsConf config = new EssentialsConf(file);
            try {
                Map powertools;
                config.load();
                if (!config.hasProperty("powertools") || (powertools = config.getConfigurationSection("powertools").getValues(false)) == null) continue;
                for (Map.Entry entry : powertools.entrySet()) {
                    if (!(entry.getValue() instanceof String)) continue;
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add((String)entry.getValue());
                    powertools.put(entry.getKey(), temp);
                }
                config.forceSave();
                continue;
            }
            catch (RuntimeException ex) {
                LOGGER.log(Level.INFO, "File: " + file.toString());
                throw ex;
            }
        }
        this.doneFile.setProperty("updateUsersPowerToolsFormat", true);
        this.doneFile.save();
    }

    private void updateUsersHomesFormat() {
        File[] userFiles;
        if (this.doneFile.getBoolean("updateUsersHomesFormat", false)) {
            return;
        }
        File userdataFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!userdataFolder.exists() || !userdataFolder.isDirectory()) {
            return;
        }
        for (File file : userFiles = userdataFolder.listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(".yml")) continue;
            EssentialsConf config = new EssentialsConf(file);
            try {
                Set worlds;
                config.load();
                if (!config.hasProperty("home") || !config.hasProperty("home.default")) continue;
                String defworld = (String)config.getProperty("home.default");
                Location defloc = this.getFakeLocation(config, "home.worlds." + defworld);
                if (defloc != null) {
                    config.setProperty("homes.home", defloc);
                }
                if ((worlds = config.getConfigurationSection("home.worlds").getKeys(false)) == null) continue;
                for (String world : worlds) {
                    String worldName;
                    Location loc;
                    if (defworld.equalsIgnoreCase(world) || (loc = this.getFakeLocation(config, "home.worlds." + world)) == null || (worldName = loc.getWorld().getName().toLowerCase(Locale.ENGLISH)) == null || worldName.isEmpty()) continue;
                    config.setProperty("homes." + worldName, loc);
                }
                config.removeProperty("home");
                config.forceSave();
                continue;
            }
            catch (RuntimeException ex) {
                LOGGER.log(Level.INFO, "File: " + file.toString());
                throw ex;
            }
        }
        this.doneFile.setProperty("updateUsersHomesFormat", true);
        this.doneFile.save();
    }

    private void moveUsersDataToUserdataFolder() {
        File usersFile = new File(this.ess.getDataFolder(), "users.yml");
        if (!usersFile.exists()) {
            return;
        }
        EssentialsConf usersConfig = new EssentialsConf(usersFile);
        usersConfig.load();
        for (String username : usersConfig.getKeys(false)) {
            List vals;
            List<String> mails;
            User user = new User(new OfflinePlayer(username, this.ess), this.ess);
            String nickname = usersConfig.getString(username + ".nickname");
            if (nickname != null && !nickname.isEmpty() && !nickname.equals(username)) {
                user.setNickname(nickname);
            }
            if ((mails = usersConfig.getStringList(username + ".mail")) != null && !mails.isEmpty()) {
                user.setMails(mails);
            }
            if (user.hasHome() || (vals = (List)usersConfig.getProperty(username + ".home")) == null) continue;
            World world = (World)this.ess.getServer().getWorlds().get(0);
            if (vals.size() > 5) {
                world = this.getFakeWorld((String)vals.get(5));
            }
            if (world == null) continue;
            user.setHome("home", new Location(world, ((Number)vals.get(0)).doubleValue(), ((Number)vals.get(1)).doubleValue(), ((Number)vals.get(2)).doubleValue(), ((Number)vals.get(3)).floatValue(), ((Number)vals.get(4)).floatValue()));
        }
        usersFile.renameTo(new File(usersFile.getAbsolutePath() + ".old"));
    }

    private void convertWarps() {
        File[] listOfFiles;
        Location loc;
        World w;
        File warpFile;
        File warpsFolder = new File(this.ess.getDataFolder(), "warps");
        if (!warpsFolder.exists()) {
            warpsFolder.mkdirs();
        }
        if ((listOfFiles = warpsFolder.listFiles()).length >= 1) {
            for (int i = 0; i < listOfFiles.length; ++i) {
                String filename = listOfFiles[i].getName();
                if (!listOfFiles[i].isFile() || !filename.endsWith(".dat")) continue;
                try {
                    float pitch;
                    double x;
                    double y;
                    double z;
                    String worldName;
                    float yaw;
                    BufferedReader rx = new BufferedReader(new FileReader(listOfFiles[i]));
                    try {
                        if (!rx.ready()) continue;
                        x = Double.parseDouble(rx.readLine().trim());
                        if (!rx.ready()) continue;
                        y = Double.parseDouble(rx.readLine().trim());
                        if (!rx.ready()) continue;
                        z = Double.parseDouble(rx.readLine().trim());
                        if (!rx.ready()) continue;
                        yaw = Float.parseFloat(rx.readLine().trim());
                        if (!rx.ready()) continue;
                        pitch = Float.parseFloat(rx.readLine().trim());
                        worldName = rx.readLine();
                    }
                    finally {
                        rx.close();
                    }
                    w = null;
                    for (World world : this.ess.getServer().getWorlds()) {
                        if (world.getEnvironment() == World.Environment.NETHER) continue;
                        w = world;
                        break;
                    }
                    if (worldName != null) {
                        worldName = worldName.trim();
                        World w1 = null;
                        w1 = this.getFakeWorld(worldName);
                        if (w1 != null) {
                            w = w1;
                        }
                    }
                    loc = new Location(w, x, y, z, yaw, pitch);
                    this.ess.getWarps().setWarp(filename.substring(0, filename.length() - 4), loc);
                    if (listOfFiles[i].renameTo(new File(warpsFolder, filename + ".old"))) continue;
                    throw new Exception(I18n._("fileRenameError", filename));
                }
                catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
        if ((warpFile = new File(this.ess.getDataFolder(), "warps.txt")).exists()) {
            try {
                BufferedReader rx = new BufferedReader(new FileReader(warpFile));
                try {
                    String[] parts = new String[]{};
                    while (rx.ready()) {
                        if (parts.length >= 6) {
                            String name = parts[0];
                            double x = Double.parseDouble(parts[1].trim());
                            double y = Double.parseDouble(parts[2].trim());
                            double z = Double.parseDouble(parts[3].trim());
                            float yaw = Float.parseFloat(parts[4].trim());
                            float pitch = Float.parseFloat(parts[5].trim());
                            if (!name.isEmpty()) {
                                w = null;
                                for (World world : this.ess.getServer().getWorlds()) {
                                    if (world.getEnvironment() == World.Environment.NETHER) continue;
                                    w = world;
                                    break;
                                }
                                loc = new Location(w, x, y, z, yaw, pitch);
                                this.ess.getWarps().setWarp(name, loc);
                                if (!warpFile.renameTo(new File(this.ess.getDataFolder(), "warps.txt.old"))) {
                                    throw new Exception(I18n._("fileRenameError", "warps.txt"));
                                }
                            }
                        }
                        parts = rx.readLine().split(":");
                    }
                }
                finally {
                    rx.close();
                }
            }
            catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sanitizeAllUserFilenames() {
        File[] listOfFiles;
        if (this.doneFile.getBoolean("sanitizeAllUserFilenames", false)) {
            return;
        }
        File usersFolder = new File(this.ess.getDataFolder(), "userdata");
        if (!usersFolder.exists()) {
            return;
        }
        for (File listOfFile : listOfFiles = usersFolder.listFiles()) {
            String sanitizedFilename;
            String filename = listOfFile.getName();
            if (!listOfFile.isFile() || !filename.endsWith(".yml") || (sanitizedFilename = StringUtil.sanitizeFileName(filename.substring(0, filename.length() - 4)) + ".yml").equals(filename)) continue;
            File tmpFile = new File(listOfFile.getParentFile(), sanitizedFilename + ".tmp");
            File newFile = new File(listOfFile.getParentFile(), sanitizedFilename);
            if (!listOfFile.renameTo(tmpFile)) {
                LOGGER.log(Level.WARNING, I18n._("userdataMoveError", filename, sanitizedFilename));
                continue;
            }
            if (newFile.exists()) {
                LOGGER.log(Level.WARNING, I18n._("duplicatedUserdata", filename, sanitizedFilename));
                continue;
            }
            if (tmpFile.renameTo(newFile)) continue;
            LOGGER.log(Level.WARNING, I18n._("userdataMoveBackError", sanitizedFilename, sanitizedFilename));
        }
        this.doneFile.setProperty("sanitizeAllUserFilenames", true);
        this.doneFile.save();
    }

    private World getFakeWorld(String name) {
        File bukkitDirectory = this.ess.getDataFolder().getParentFile().getParentFile();
        File worldDirectory = new File(bukkitDirectory, name);
        if (worldDirectory.exists() && worldDirectory.isDirectory()) {
            return new FakeWorld(worldDirectory.getName(), World.Environment.NORMAL);
        }
        return null;
    }

    public Location getFakeLocation(EssentialsConf config, String path) {
        String worldName = config.getString((path != null ? new StringBuilder().append(path).append(".").toString() : "") + "world");
        if (worldName == null || worldName.isEmpty()) {
            return null;
        }
        World world = this.getFakeWorld(worldName);
        if (world == null) {
            return null;
        }
        return new Location(world, config.getDouble((path != null ? new StringBuilder().append(path).append(".").toString() : "") + "x", 0.0), config.getDouble((path != null ? new StringBuilder().append(path).append(".").toString() : "") + "y", 0.0), config.getDouble((path != null ? new StringBuilder().append(path).append(".").toString() : "") + "z", 0.0), (float)config.getDouble((path != null ? new StringBuilder().append(path).append(".").toString() : "") + "yaw", 0.0), (float)config.getDouble((path != null ? new StringBuilder().append(path).append(".").toString() : "") + "pitch", 0.0));
    }

    private void deleteOldItemsCsv() {
        if (this.doneFile.getBoolean("deleteOldItemsCsv", false)) {
            return;
        }
        File file = new File(this.ess.getDataFolder(), "items.csv");
        if (file.exists()) {
            try {
                MessageDigest digest;
                HashSet<BigInteger> oldconfigs;
                oldconfigs = new HashSet<BigInteger>();
                oldconfigs.add(new BigInteger("66ec40b09ac167079f558d1099e39f10", 16));
                oldconfigs.add(new BigInteger("34284de1ead43b0bee2aae85e75c041d", 16));
                oldconfigs.add(new BigInteger("c33bc9b8ee003861611bbc2f48eb6f4f", 16));
                oldconfigs.add(new BigInteger("6ff17925430735129fc2a02f830c1daa", 16));
                digest = ManagedFile.getDigest();
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                DigestInputStream dis = new DigestInputStream(bis, digest);
                byte[] buffer = new byte[1024];
                try {
                    while (dis.read(buffer) != -1) {
                    }
                }
                finally {
                    dis.close();
                }
                BigInteger hash = new BigInteger(1, digest.digest());
                if (oldconfigs.contains(hash) && !file.delete()) {
                    throw new IOException("Could not delete file " + file.toString());
                }
                this.doneFile.setProperty("deleteOldItemsCsv", true);
                this.doneFile.save();
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private void updateSpawnsToNewSpawnsConfig() {
        if (this.doneFile.getBoolean("updateSpawnsToNewSpawnsConfig", false)) {
            return;
        }
        File configFile = new File(this.ess.getDataFolder(), "spawn.yml");
        if (configFile.exists()) {
            EssentialsConf config = new EssentialsConf(configFile);
            try {
                config.load();
                if (!config.hasProperty("spawns")) {
                    Spawns spawns = new Spawns();
                    Set<String> keys = config.getKeys(false);
                    for (String group : keys) {
                        Location loc = this.getFakeLocation(config, group);
                        spawns.getSpawns().put(group.toLowerCase(Locale.ENGLISH), loc);
                    }
                    if (!configFile.renameTo(new File(this.ess.getDataFolder(), "spawn.yml.old"))) {
                        throw new Exception(I18n._("fileRenameError", "spawn.yml"));
                    }
                    PrintWriter writer = new PrintWriter(configFile);
                    try {
                        new YamlStorageWriter(writer).save(spawns);
                    }
                    finally {
                        writer.close();
                    }
                }
            }
            catch (Exception ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        this.doneFile.setProperty("updateSpawnsToNewSpawnsConfig", true);
        this.doneFile.save();
    }

    private void updateJailsToNewJailsConfig() {
        if (this.doneFile.getBoolean("updateJailsToNewJailsConfig", false)) {
            return;
        }
        File configFile = new File(this.ess.getDataFolder(), "jail.yml");
        if (configFile.exists()) {
            EssentialsConf config = new EssentialsConf(configFile);
            try {
                config.load();
                if (!config.hasProperty("jails")) {
                    Jails jails = new Jails();
                    Set<String> keys = config.getKeys(false);
                    for (String jailName : keys) {
                        Location loc = this.getFakeLocation(config, jailName);
                        jails.getJails().put(jailName.toLowerCase(Locale.ENGLISH), loc);
                    }
                    if (!configFile.renameTo(new File(this.ess.getDataFolder(), "jail.yml.old"))) {
                        throw new Exception(I18n._("fileRenameError", "jail.yml"));
                    }
                    PrintWriter writer = new PrintWriter(configFile);
                    try {
                        new YamlStorageWriter(writer).save(jails);
                    }
                    finally {
                        writer.close();
                    }
                }
            }
            catch (Exception ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        this.doneFile.setProperty("updateJailsToNewJailsConfig", true);
        this.doneFile.save();
    }

    private void warnMetrics() {
        if (this.doneFile.getBoolean("warnMetrics", false)) {
            return;
        }
        this.ess.getSettings().setMetricsEnabled(false);
        this.doneFile.setProperty("warnMetrics", true);
        this.doneFile.save();
    }

    public void beforeSettings() {
        if (!this.ess.getDataFolder().exists()) {
            this.ess.getDataFolder().mkdirs();
        }
        this.moveWorthValuesToWorthYml();
        this.moveMotdRulesToFile("motd");
        this.moveMotdRulesToFile("rules");
    }

    public void afterSettings() {
        this.sanitizeAllUserFilenames();
        this.updateUsersToNewDefaultHome();
        this.moveUsersDataToUserdataFolder();
        this.convertWarps();
        this.updateUsersPowerToolsFormat();
        this.updateUsersHomesFormat();
        this.deleteOldItemsCsv();
        this.updateSpawnsToNewSpawnsConfig();
        this.updateJailsToNewJailsConfig();
        this.warnMetrics();
    }
}

