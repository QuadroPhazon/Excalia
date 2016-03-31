/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

public class ManagedFile {
    private static final int BUFFERSIZE = 8192;
    private final transient File file;

    public ManagedFile(String filename, IEssentials ess) {
        this.file = new File(ess.getDataFolder(), filename);
        if (this.file.exists()) {
            try {
                if (ManagedFile.checkForVersion(this.file, ess.getDescription().getVersion()) && !this.file.delete()) {
                    throw new IOException("Could not delete file " + this.file.toString());
                }
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        if (!this.file.exists()) {
            try {
                ManagedFile.copyResourceAscii("/" + filename, this.file);
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, I18n._("itemsCsvNotLoaded", new Object[0]), ex);
            }
        }
    }

    public static void copyResourceAscii(String resourceName, File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(ManagedFile.class.getResourceAsStream(resourceName));
        try {
            MessageDigest digest = ManagedFile.getDigest();
            DigestOutputStream digestStream = new DigestOutputStream(new FileOutputStream(file), digest);
            try {
                OutputStreamWriter writer = new OutputStreamWriter(digestStream);
                try {
                    int length;
                    char[] buffer = new char[8192];
                    while ((length = reader.read(buffer)) >= 0) {
                        writer.write(buffer, 0, length);
                    }
                    writer.write("\n");
                    writer.flush();
                    BigInteger hashInt = new BigInteger(1, digest.digest());
                    digestStream.on(false);
                    digestStream.write(35);
                    digestStream.write(hashInt.toString(16).getBytes());
                }
                finally {
                    writer.close();
                }
            }
            finally {
                digestStream.close();
            }
        }
        finally {
            reader.close();
        }
    }

    public static boolean checkForVersion(File file, String version) throws IOException {
        if (file.length() < 33) {
            return false;
        }
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        try {
            int length;
            byte[] buffer = new byte[(int)file.length()];
            int position = 0;
            while ((length = bis.read(buffer, position, Math.min((int)file.length() - position, 8192))) >= 0 && (long)(position += length) < file.length()) {
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            if (bais.skip(file.length() - 33) != file.length() - 33) {
                boolean bl = false;
                return bl;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
            try {
                String hash = reader.readLine();
                if (hash != null && hash.matches("#[a-f0-9]{32}")) {
                    String versioncheck;
                    hash = hash.substring(1);
                    bais.reset();
                    String versionline = reader.readLine();
                    if (versionline != null && versionline.matches("#version: .+") && !(versioncheck = versionline.substring(10)).equalsIgnoreCase(version)) {
                        bais.reset();
                        MessageDigest digest = ManagedFile.getDigest();
                        DigestInputStream digestStream = new DigestInputStream(bais, digest);
                        try {
                            byte[] bytes = new byte[(int)file.length() - 33];
                            digestStream.read(bytes);
                            BigInteger correct = new BigInteger(hash, 16);
                            BigInteger test = new BigInteger(1, digest.digest());
                            if (correct.equals(test)) {
                                boolean bl = true;
                                return bl;
                            }
                            Bukkit.getLogger().warning("File " + file.toString() + " has been modified by user and file version differs, please update the file manually.");
                        }
                        finally {
                            digestStream.close();
                        }
                    }
                }
            }
            finally {
                reader.close();
            }
        }
        finally {
            bis.close();
        }
        return false;
    }

    public static MessageDigest getDigest() throws IOException {
        try {
            return MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }
    }

    public List<String> getLines() {
        Object line;
        BufferedReader reader = new BufferedReader(new FileReader(this.file));
        try {
            ArrayList<String> lines = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                lines.add((String)line);
            }
            line = lines;
        }
        catch (Throwable var4_5) {
            try {
                reader.close();
                throw var4_5;
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                return Collections.emptyList();
            }
        }
        reader.close();
        return line;
    }
}

