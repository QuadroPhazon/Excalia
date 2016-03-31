/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Util {
    private static File workDir = null;

    public static File getWorkingDirectory() {
        if (workDir == null) {
            workDir = Util.getWorkingDirectory("pactify");
        }
        System.out.println(workDir);
        return workDir;
    }

    public static File getWorkingDirectory(String applicationName) {
        File workingDirectory;
        String userHome = System.getProperty("user.home", ".");
        System.out.println(Util.getPlatform().ordinal());
        switch (Util.getPlatform().ordinal()) {
            case 0: 
            case 1: {
                workingDirectory = new File(userHome, "" + '.' + applicationName + '/');
                break;
            }
            case 2: {
                String applicationData = System.getenv("APPDATA");
                if (applicationData != null) {
                    workingDirectory = new File(applicationData, "." + applicationName + '/');
                    break;
                }
                workingDirectory = new File(userHome, "" + '.' + applicationName + '/');
                break;
            }
            case 3: {
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            }
            default: {
                workingDirectory = new File(userHome, applicationName + '/');
            }
        }
        if (!workingDirectory.exists() && !workingDirectory.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        }
        return workingDirectory;
    }

    public static OS getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OS.windows;
        }
        if (osName.contains("mac")) {
            return OS.macos;
        }
        if (osName.contains("solaris")) {
            return OS.solaris;
        }
        if (osName.contains("sunos")) {
            return OS.solaris;
        }
        if (osName.contains("linux")) {
            return OS.linux;
        }
        if (osName.contains("unix")) {
            return OS.linux;
        }
        return OS.unknown;
    }

    public static String buildQuery(Map<String, Object> paramMap) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> localEntry : paramMap.entrySet()) {
            if (localStringBuilder.length() > 0) {
                localStringBuilder.append('&');
            }
            try {
                localStringBuilder.append(URLEncoder.encode(localEntry.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
                localUnsupportedEncodingException1.printStackTrace();
            }
            if (localEntry.getValue() == null) continue;
            localStringBuilder.append('=');
            try {
                localStringBuilder.append(URLEncoder.encode(localEntry.getValue().toString(), "UTF-8"));
            }
            catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
                localUnsupportedEncodingException2.printStackTrace();
            }
        }
        return localStringBuilder.toString();
    }

    public static String executePost(String paramString, Map<String, Object> paramMap) {
        return Util.executePost(paramString, Util.buildQuery(paramMap));
    }

    public static String executePost(String paramString1, String paramString2) {
        HttpURLConnection localHttpsURLConnection = null;
        try {
            String str1;
            URL localURL = new URL(paramString1);
            localHttpsURLConnection = (HttpsURLConnection)localURL.openConnection();
            localHttpsURLConnection.setRequestMethod("POST");
            localHttpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            localHttpsURLConnection.setRequestProperty("Content-Length", "" + Integer.toString(paramString2.getBytes().length));
            localHttpsURLConnection.setRequestProperty("Content-Language", "en-US");
            localHttpsURLConnection.setUseCaches(false);
            localHttpsURLConnection.setDoInput(true);
            localHttpsURLConnection.setDoOutput(true);
            localHttpsURLConnection.connect();
            Certificate[] arrayOfCertificate = localHttpsURLConnection.getServerCertificates();
            byte[] arrayOfByte1 = new byte[294];
            DataInputStream localDataInputStream = new DataInputStream(Util.class.getResourceAsStream("minecraft.key"));
            localDataInputStream.readFully(arrayOfByte1);
            localDataInputStream.close();
            Certificate localCertificate = arrayOfCertificate[0];
            PublicKey localPublicKey = localCertificate.getPublicKey();
            byte[] arrayOfByte2 = localPublicKey.getEncoded();
            for (int i = 0; i < arrayOfByte2.length; ++i) {
                if (arrayOfByte2[i] == arrayOfByte1[i]) continue;
                throw new RuntimeException("Public key mismatch");
            }
            DataOutputStream localDataOutputStream = new DataOutputStream(localHttpsURLConnection.getOutputStream());
            localDataOutputStream.writeBytes(paramString2);
            localDataOutputStream.flush();
            localDataOutputStream.close();
            InputStream localInputStream = localHttpsURLConnection.getInputStream();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
            StringBuffer localStringBuffer = new StringBuffer();
            while ((str1 = localBufferedReader.readLine()) != null) {
                localStringBuffer.append(str1);
                localStringBuffer.append('\r');
            }
            localBufferedReader.close();
            String string = localStringBuffer.toString();
            return string;
        }
        catch (Exception localException) {
            localException.printStackTrace();
            String arrayOfByte1 = null;
            return arrayOfByte1;
        }
        finally {
            if (localHttpsURLConnection != null) {
                localHttpsURLConnection.disconnect();
            }
        }
    }

    public static boolean isEmpty(String paramString) {
        return paramString == null || paramString.length() == 0;
    }

    public static void openLink(URI paramURI) {
        try {
            Object localObject = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            localObject.getClass().getMethod("browse", URI.class).invoke(localObject, paramURI);
        }
        catch (Throwable localThrowable) {
            System.out.println("Failed to open link " + paramURI.toString());
        }
    }

    public static enum OS {
        linux,
        solaris,
        windows,
        macos,
        unknown;
        

        private OS() {
        }
    }

}

