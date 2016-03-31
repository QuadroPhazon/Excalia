/*
 * Decompiled with CFR 0_110.
 */
package fr.ascentia;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

class AsyncDownloader
extends Thread {
    public boolean running = true;
    public int status = 0;
    public String error = null;
    public String remoteFile;
    public File localFile;

    public AsyncDownloader(String r, File l) {
        this.remoteFile = r;
        this.localFile = l;
        this.start();
    }

    public void run() {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            int numRead;
            this.status = 1;
            URL url = new URL(this.remoteFile);
            URLConnection urlconnection = url.openConnection();
            urlconnection.setDefaultUseCaches(false);
            this.status = 2;
            int fileSize = urlconnection.getContentLength();
            this.status = 3;
            out = new BufferedOutputStream(new FileOutputStream(this.localFile));
            conn = url.openConnection();
            this.status = 6;
            try {
                in = conn.getInputStream();
            }
            catch (Exception e) {
                this.status = 7;
                in = conn.getInputStream();
            }
            this.status = 9;
            byte[] buffer = new byte[1024];
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                float percent = (float)numWritten / (float)fileSize * 90.0f;
                this.status = 10 + (int)percent;
                System.out.println(this.status);
                out.write(buffer, 0, numRead);
                numWritten += (long)numRead;
            }
            this.running = false;
        }
        catch (Exception exception) {
            this.error = "Fatal Error: " + exception.getMessage();
            exception.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException ioe) {}
        }
    }
}

