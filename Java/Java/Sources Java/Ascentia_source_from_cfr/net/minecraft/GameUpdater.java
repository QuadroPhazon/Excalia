/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.SocketPermission;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipEntry;
import net.minecraft.Launcher;
import net.minecraft.Util;

public class GameUpdater
implements Runnable {
    public static final int STATE_INIT = 1;
    public static final int STATE_DETERMINING_PACKAGES = 2;
    public static final int STATE_CHECKING_CACHE = 3;
    public static final int STATE_DOWNLOADING = 4;
    public static final int STATE_EXTRACTING_PACKAGES = 5;
    public static final int STATE_UPDATING_CLASSPATH = 6;
    public static final int STATE_SWITCHING_APPLET = 7;
    public static final int STATE_INITIALIZE_REAL_APPLET = 8;
    public static final int STATE_START_REAL_APPLET = 9;
    public static final int STATE_DONE = 10;
    public int percentage;
    public int currentSizeDownload;
    public int totalSizeDownload;
    public int currentSizeExtract;
    public int totalSizeExtract;
    protected URL[] urlList;
    private static ClassLoader classLoader;
    protected Thread loaderThread;
    protected Thread animationThread;
    public boolean fatalError;
    public String fatalErrorDescription;
    protected String subtaskMessage = "";
    protected int state = 1;
    protected boolean lzmaSupported = false;
    protected boolean pack200Supported = false;
    protected String[] genericErrorMessage = new String[]{"An error occured while loading the applet.", "Please contact support to resolve this issue.", "<placeholder for error message>"};
    protected boolean certificateRefused;
    protected String[] certificateRefusedMessage = new String[]{"Permissions for Applet Refused.", "Please accept the permissions dialog to allow", "the applet to continue the loading process."};
    protected static boolean natives_loaded;
    public static boolean forceUpdate;
    private String latestVersion;
    private String mainGameUrl;
    public boolean pauseAskUpdate;
    public boolean shouldUpdate;
    public boolean preventErrorTime = false;
    public String preventError = "";
    public static Launcher launcherInstance;

    public GameUpdater(Launcher launcherInstance, String latestVersion, String mainGameUrl) {
        GameUpdater.launcherInstance = launcherInstance;
        this.latestVersion = latestVersion;
        this.mainGameUrl = mainGameUrl;
    }

    public void init() {
        this.state = 1;
        try {
            Class.forName("LZMA.LzmaInputStream");
            this.lzmaSupported = true;
        }
        catch (Throwable localThrowable) {
            // empty catch block
        }
        try {
            Pack200.class.getSimpleName();
            this.pack200Supported = true;
        }
        catch (Throwable localThrowable1) {
            // empty catch block
        }
    }

    private String generateStacktrace(Exception exception) {
        StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        return result.toString();
    }

    protected String getDescriptionForState() {
        switch (this.state) {
            case 1: {
                return "Initialisation du chargeur";
            }
            case 2: {
                return "Determine les paquets a charger";
            }
            case 3: {
                return "Verification du cache pour les fichiers existants";
            }
            case 4: {
                return "Telechargement des packages";
            }
            case 5: {
                return "Extracton des paquets telecharges";
            }
            case 6: {
                return "Mise \u00e0 jour du classpath";
            }
            case 7: {
                return "Commutation de l'applet";
            }
            case 8: {
                return "Initialisation de l'applet reel";
            }
            case 9: {
                return "Lancement de de l'applet reel";
            }
            case 10: {
                return "Ok. Chargement.";
            }
        }
        return "unknown state";
    }

    protected String trimExtensionByCapabilities(String file) {
        if (!this.pack200Supported) {
            file = file.replaceAll(".pack", "");
        }
        if (!this.lzmaSupported) {
            file = file.replaceAll(".lzma", "");
        }
        return file;
    }

    protected void loadJarURLs() throws Exception {
        this.state = 2;
        this.urlList = new URL[5];
        this.urlList[0] = new URL(this.mainGameUrl);
        this.urlList[1] = new URL("http://s3.amazonaws.com/MinecraftDownload/lwjgl.jar");
        this.urlList[2] = new URL("http://s3.amazonaws.com/MinecraftDownload/jinput.jar");
        this.urlList[3] = new URL("http://s3.amazonaws.com/MinecraftDownload/lwjgl_util.jar");
        String osName = System.getProperty("os.name");
        String nativeJar = null;
        if (osName.startsWith("Win")) {
            nativeJar = "windows_natives.jar.lzma";
        } else if (osName.startsWith("Linux")) {
            nativeJar = "linux_natives.jar.lzma";
        } else if (osName.startsWith("Mac")) {
            nativeJar = "macosx_natives.jar.lzma";
        } else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            nativeJar = "solaris_natives.jar.lzma";
        } else {
            this.fatalErrorOccured("OS (" + osName + ") not supported", null);
        }
        if (nativeJar == null) {
            this.fatalErrorOccured("no lwjgl natives files found", null);
        } else {
            nativeJar = this.trimExtensionByCapabilities(nativeJar);
            this.urlList[4] = new URL("http://s3.amazonaws.com/MinecraftDownload/" + nativeJar);
        }
    }

    public void run() {
        this.init();
        this.state = 3;
        this.percentage = 5;
        try {
            this.loadJarURLs();
            String path = (String)AccessController.doPrivileged(new PrivilegedExceptionAction(){

                public Object run() throws Exception {
                    return Util.getWorkingDirectory() + File.separator + "bin" + File.separator;
                }
            });
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            boolean cacheAvailable = false;
            this.shouldUpdate = false;
            if (forceUpdate) {
                this.shouldUpdate = true;
            } else {
                File jar = new File(Util.getWorkingDirectory(), "bin/minecraft.jar");
                if (jar.exists()) {
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
                        this.shouldUpdate = true;
                    }
                    String version = "";
                    for (int tryingLeft = 2; tryingLeft > 0; --tryingLeft) {
                        try {
                            String inputLine;
                            URL versionUrl = new URL("http://www.pactify.fr/launcher/version.md5.php");
                            BufferedReader inUrl = new BufferedReader(new InputStreamReader(versionUrl.openStream()));
                            while ((inputLine = inUrl.readLine()) != null) {
                                version = version + inputLine;
                            }
                            inUrl.close();
                            if (!(version == null || version.equals("") || sb.toString().equals("") || version.equals(sb.toString()))) {
                                this.shouldUpdate = true;
                            }
                            tryingLeft = 0;
                            continue;
                        }
                        catch (Exception e) {
                            if (tryingLeft > 1) {
                                System.out.println("Check Update : First try failed. Wait 500ms before second try.");
                                Thread.sleep(500);
                                continue;
                            }
                            System.out.println("Check Update : Second try failed. Ignore.");
                            this.preventErrorTime = true;
                            if (!this.shouldUpdate) {
                                this.preventError = "Impossible de v\u00e9rifier si une mise \u00e0 jour est disponible.";
                                this.subtaskMessage = "Patiente 5 secondes avant le lancement du jeu.";
                            } else {
                                this.preventError = "Le serveur de Mise \u00e0 jour est indisponible !";
                                this.subtaskMessage = "Patiente 5 secondes avant une nouvelle tentative...";
                            }
                            Thread.sleep(6000);
                            this.preventErrorTime = false;
                            this.subtaskMessage = "";
                        }
                    }
                } else {
                    this.shouldUpdate = true;
                }
            }
            if (this.shouldUpdate) {
                this.downloadJars(path);
                this.extractJars(path);
                this.extractNatives(path);
                if (this.latestVersion != null) {
                    this.percentage = 90;
                }
            } else {
                cacheAvailable = true;
                this.percentage = 90;
            }
            this.updateClassPath(dir);
            this.state = 10;
        }
        catch (AccessControlException ace) {
            this.fatalErrorOccured(ace.getMessage(), ace);
            this.certificateRefused = true;
        }
        catch (Exception e) {
            this.fatalErrorOccured(e.getMessage(), e);
        }
        finally {
            this.loaderThread = null;
        }
    }

    private void checkShouldUpdate() {
        this.pauseAskUpdate = true;
        while (this.pauseAskUpdate) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateClassPath(File dir) throws Exception {
        String path;
        this.state = 6;
        this.percentage = 95;
        URL[] urls = new URL[this.urlList.length];
        for (int i = 0; i < this.urlList.length; ++i) {
            urls[i] = new File(dir, this.getJarName(this.urlList[i])).toURI().toURL();
        }
        if (classLoader == null) {
            classLoader = new URLClassLoader(urls){

                protected PermissionCollection getPermissions(CodeSource codesource) {
                    PermissionCollection perms = null;
                    try {
                        Method method = SecureClassLoader.class.getDeclaredMethod("getPermissions", CodeSource.class);
                        method.setAccessible(true);
                        perms = (PermissionCollection)method.invoke(this.getClass().getClassLoader(), codesource);
                        String host = "www.minecraft.net";
                        if (host != null && host.length() > 0) {
                            perms.add(new SocketPermission(host, "connect,accept"));
                        } else {
                            codesource.getLocation().getProtocol().equals("file");
                        }
                        perms.add(new FilePermission("<<ALL FILES>>", "read"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return perms;
                }
            };
        }
        if (!(path = dir.getAbsolutePath()).endsWith(File.separator)) {
            path = path + File.separator;
        }
        this.unloadNatives(path);
        System.setProperty("org.lwjgl.librarypath", path + "natives");
        System.setProperty("net.java.games.input.librarypath", path + "natives");
        natives_loaded = true;
    }

    private void unloadNatives(String nativePath) {
        if (!natives_loaded) {
            return;
        }
        try {
            Field field = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            field.setAccessible(true);
            Vector libs = (Vector)field.get(this.getClass().getClassLoader());
            String path = new File(nativePath).getCanonicalPath();
            for (int i = 0; i < libs.size(); ++i) {
                String s = (String)libs.get(i);
                if (!s.startsWith(path)) continue;
                libs.remove(i);
                --i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Applet createApplet() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class appletClass = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
        return (Applet)appletClass.newInstance();
    }

    protected void downloadJars(String path) throws Exception {
        File versionFile = new File(path, "md5s");
        Properties md5s = new Properties();
        if (versionFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(versionFile);
                md5s.load(fis);
                fis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.state = 4;
        int[] fileSizes = new int[this.urlList.length];
        boolean[] skip = new boolean[this.urlList.length];
        for (int i = 0; i < this.urlList.length; ++i) {
            URLConnection urlconnection = this.urlList[i].openConnection();
            urlconnection.setDefaultUseCaches(false);
            skip[i] = false;
            if (urlconnection instanceof HttpURLConnection) {
                int code;
                ((HttpURLConnection)urlconnection).setRequestMethod("HEAD");
                String etagOnDisk = "\"" + md5s.getProperty(this.getFileName(this.urlList[i])) + "\"";
                if (!forceUpdate && etagOnDisk != null) {
                    urlconnection.setRequestProperty("If-None-Match", etagOnDisk);
                }
                if ((code = ((HttpURLConnection)urlconnection).getResponseCode()) / 100 == 3) {
                    skip[i] = true;
                }
            }
            fileSizes[i] = urlconnection.getContentLength();
            this.totalSizeDownload += fileSizes[i];
        }
        this.percentage = 10;
        int initialPercentage = 10;
        byte[] buffer = new byte[65536];
        for (int i2 = 0; i2 < this.urlList.length; ++i2) {
            if (skip[i2]) {
                this.percentage = initialPercentage + fileSizes[i2] * 45 / this.totalSizeDownload;
                continue;
            }
            try {
                md5s.remove(this.getFileName(this.urlList[i2]));
                md5s.store(new FileOutputStream(versionFile), "md5 hashes for downloaded files");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            boolean unsuccessfulAttempts = false;
            int maxUnsuccessfulAttempts = 3;
            boolean downloadFile = true;
            while (downloadFile) {
                int bufferSize;
                downloadFile = false;
                URLConnection urlconnection = this.urlList[i2].openConnection();
                String etag = "";
                if (urlconnection instanceof HttpURLConnection) {
                    urlconnection.setRequestProperty("Cache-Control", "no-cache");
                    urlconnection.connect();
                    etag = urlconnection.getHeaderField("ETag");
                    etag = etag.substring(1, etag.length() - 1);
                }
                String currentFile = this.getFileName(this.urlList[i2]);
                InputStream inputstream = this.getJarInputStream(currentFile, urlconnection);
                FileOutputStream fos = new FileOutputStream(path + currentFile);
                long downloadStartTime = System.currentTimeMillis();
                int downloadedAmount = 0;
                int fileSize = 0;
                String downloadSpeedMessage = "";
                MessageDigest m = MessageDigest.getInstance("MD5");
                while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1) {
                    fos.write(buffer, 0, bufferSize);
                    m.update(buffer, 0, bufferSize);
                    this.currentSizeDownload += bufferSize;
                    fileSize += bufferSize;
                    this.percentage = initialPercentage + this.currentSizeDownload * 45 / this.totalSizeDownload;
                    this.subtaskMessage = "Retrieving: " + currentFile + " " + this.currentSizeDownload * 100 / this.totalSizeDownload + "%";
                    downloadedAmount += bufferSize;
                    long timeLapse = System.currentTimeMillis() - downloadStartTime;
                    if (timeLapse >= 1000) {
                        float downloadSpeed = (float)downloadedAmount / (float)timeLapse;
                        downloadSpeed = (float)((int)(downloadSpeed * 100.0f)) / 100.0f;
                        downloadSpeedMessage = " @ " + downloadSpeed + " KB/sec";
                        downloadedAmount = 0;
                        downloadStartTime += 1000;
                    }
                    this.subtaskMessage = this.subtaskMessage + downloadSpeedMessage;
                }
                inputstream.close();
                fos.close();
                String md5 = new BigInteger(1, m.digest()).toString(16);
                while (md5.length() < 32) {
                    md5 = "0" + md5;
                }
                boolean md5Matches = true;
                if (etag == null) continue;
                md5Matches = md5.equals(etag);
            }
        }
        this.subtaskMessage = "";
    }

    protected InputStream getJarInputStream(String currentFile, final URLConnection urlconnection) throws Exception {
        final InputStream[] is = new InputStream[1];
        for (int j = 0; j < 3 && is[0] == null; ++j) {
            Thread t = new Thread(){

                public void run() {
                    try {
                        is[0] = urlconnection.getInputStream();
                    }
                    catch (IOException localIOException) {
                        // empty catch block
                    }
                }
            };
            t.setName("JarInputStreamThread");
            t.start();
            int iterationCount = 0;
            while (is[0] == null && iterationCount++ < 5) {
                try {
                    t.join(1000);
                }
                catch (InterruptedException localInterruptedException) {}
            }
            if (is[0] != null) continue;
            try {
                t.interrupt();
                t.join();
                continue;
            }
            catch (InterruptedException localInterruptedException1) {
                // empty catch block
            }
        }
        if (is[0] == null) {
            if (currentFile.equals("minecraft.jar")) {
                throw new Exception("Unable to download " + currentFile);
            }
            throw new Exception("Unable to download " + currentFile);
        }
        return is[0];
    }

    protected void extractLZMA(String in, String out) throws Exception {
        File f = new File(in);
        if (!f.exists()) {
            return;
        }
        FileInputStream fileInputHandle = new FileInputStream(f);
        Class clazz = Class.forName("LZMA.LzmaInputStream");
        Constructor constructor = clazz.getDeclaredConstructor(InputStream.class);
        InputStream inputHandle = (InputStream)constructor.newInstance(fileInputHandle);
        FileOutputStream outputHandle = new FileOutputStream(out);
        byte[] buffer = new byte[16384];
        int ret = inputHandle.read(buffer);
        while (ret >= 1) {
            outputHandle.write(buffer, 0, ret);
            ret = inputHandle.read(buffer);
        }
        inputHandle.close();
        outputHandle.close();
        outputHandle = null;
        inputHandle = null;
        f.delete();
    }

    protected void extractPack(String in, String out) throws Exception {
        File f = new File(in);
        if (!f.exists()) {
            return;
        }
        FileOutputStream fostream = new FileOutputStream(out);
        JarOutputStream jostream = new JarOutputStream(fostream);
        Pack200.Unpacker unpacker = Pack200.newUnpacker();
        unpacker.unpack(f, jostream);
        jostream.close();
        f.delete();
    }

    protected void extractJars(String path) throws Exception {
        this.state = 5;
        float increment = 10.0f / (float)this.urlList.length;
        for (int i = 0; i < this.urlList.length; ++i) {
            this.percentage = 55 + (int)(increment * (float)(i + 1));
            String filename = this.getFileName(this.urlList[i]);
            if (filename.endsWith(".pack.lzma")) {
                this.subtaskMessage = "Extracting: " + filename + " to " + filename.replaceAll(".lzma", "");
                this.extractLZMA(path + filename, path + filename.replaceAll(".lzma", ""));
                this.subtaskMessage = "Extracting: " + filename.replaceAll(".lzma", "") + " to " + filename.replaceAll(".pack.lzma", "");
                this.extractPack(path + filename.replaceAll(".lzma", ""), path + filename.replaceAll(".pack.lzma", ""));
                continue;
            }
            if (filename.endsWith(".pack")) {
                this.subtaskMessage = "Extracting: " + filename + " to " + filename.replace(".pack", "");
                this.extractPack(path + filename, path + filename.replace(".pack", ""));
                continue;
            }
            if (!filename.endsWith(".lzma")) continue;
            this.subtaskMessage = "Extracting: " + filename + " to " + filename.replace(".lzma", "");
            this.extractLZMA(path + filename, path + filename.replace(".lzma", ""));
        }
    }

    protected void extractNatives(String path) throws Exception {
        File nativeFolder;
        JarEntry entry;
        File file;
        this.state = 5;
        int initialPercentage = this.percentage;
        String nativeJar = this.getJarName(this.urlList[this.urlList.length - 1]);
        Certificate[] certificate = Launcher.class.getProtectionDomain().getCodeSource().getCertificates();
        if (certificate == null) {
            URL location = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
            JarURLConnection jurl = (JarURLConnection)new URL("jar:" + location.toString() + "!/net/minecraft/Launcher.class").openConnection();
            jurl.setDefaultUseCaches(true);
            try {
                certificate = jurl.getCertificates();
            }
            catch (Exception localException) {
                // empty catch block
            }
        }
        if (!(nativeFolder = new File(path + "natives")).exists()) {
            nativeFolder.mkdir();
        }
        if (!(file = new File(path + nativeJar)).exists()) {
            return;
        }
        JarFile jarFile = new JarFile(file, true);
        Enumeration<JarEntry> entities = jarFile.entries();
        this.totalSizeExtract = 0;
        while (entities.hasMoreElements()) {
            entry = entities.nextElement();
            if (entry.isDirectory() || entry.getName().indexOf(47) != -1) continue;
            this.totalSizeExtract = (int)((long)this.totalSizeExtract + entry.getSize());
        }
        this.currentSizeExtract = 0;
        entities = jarFile.entries();
        while (entities.hasMoreElements()) {
            File f;
            int bufferSize;
            entry = entities.nextElement();
            if (entry.isDirectory() || entry.getName().indexOf(47) != -1 || (f = new File(path + "natives" + File.separator + entry.getName())).exists() && !f.delete()) continue;
            InputStream in = jarFile.getInputStream(jarFile.getEntry(entry.getName()));
            FileOutputStream out = new FileOutputStream(path + "natives" + File.separator + entry.getName());
            byte[] buffer = new byte[65536];
            while ((bufferSize = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, bufferSize);
                this.currentSizeExtract += bufferSize;
                this.percentage = initialPercentage + this.currentSizeExtract * 20 / this.totalSizeExtract;
                this.subtaskMessage = "Extracting: " + entry.getName() + " " + this.currentSizeExtract * 100 / this.totalSizeExtract + "%";
            }
            GameUpdater.validateCertificateChain(certificate, entry.getCertificates());
            in.close();
            out.close();
        }
        this.subtaskMessage = "";
        jarFile.close();
        File f = new File(path + nativeJar);
        f.delete();
    }

    protected static void validateCertificateChain(Certificate[] ownCerts, Certificate[] native_certs) throws Exception {
        if (ownCerts == null) {
            return;
        }
        if (native_certs == null) {
            throw new Exception("Unable to validate certificate chain. Native entry did not have a certificate chain at all");
        }
        if (ownCerts.length != native_certs.length) {
            throw new Exception("Unable to validate certificate chain. Chain differs in length [" + ownCerts.length + " vs " + native_certs.length + "]");
        }
        for (int i = 0; i < ownCerts.length; ++i) {
            if (ownCerts[i].equals(native_certs[i])) continue;
            throw new Exception("Certificate mismatch: " + ownCerts[i] + " != " + native_certs[i]);
        }
    }

    protected String getJarName(URL url) {
        String fileName = url.getFile();
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        if (fileName.endsWith(".pack.lzma")) {
            fileName = fileName.replaceAll(".pack.lzma", "");
        } else if (fileName.endsWith(".pack")) {
            fileName = fileName.replaceAll(".pack", "");
        } else if (fileName.endsWith(".lzma")) {
            fileName = fileName.replaceAll(".lzma", "");
        }
        return fileName.substring(fileName.lastIndexOf(47) + 1);
    }

    protected String getFileName(URL url) {
        String fileName = url.getFile();
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName.substring(fileName.lastIndexOf(47) + 1);
    }

    protected void fatalErrorOccured(String error, Exception e) {
        e.printStackTrace();
        this.fatalError = true;
        this.fatalErrorDescription = "Fatal error occured (" + this.state + "): " + error;
        System.out.println(this.fatalErrorDescription);
        System.out.println(this.generateStacktrace(e));
    }

    public boolean canPlayOffline() {
        return true;
    }

    static {
        natives_loaded = false;
        forceUpdate = false;
    }

}

