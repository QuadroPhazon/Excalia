/*
 * Decompiled with CFR 0_110.
 */
package net.minecraft;

import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.LauncherFrame;
import net.minecraft.Util;

public class MinecraftLauncher {
    private static final int MIN_HEAP = 511;
    private static final int RECOMMENDED_HEAP = 1024;

    public static void main(String[] paramArrayOfString) throws Exception {
        float f = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        if (f > 511.0f) {
            LauncherFrame.main(paramArrayOfString);
        } else {
            try {
                String str = MinecraftLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                ArrayList<String> localArrayList = new ArrayList<String>();
                if (Util.getPlatform().equals((Object)Util.OS.windows)) {
                    localArrayList.add("javaw");
                } else {
                    localArrayList.add("java");
                }
                localArrayList.add("-Xmx1024m");
                localArrayList.add("-Dsun.java2d.noddraw=true");
                localArrayList.add("-Dsun.java2d.d3d=false");
                localArrayList.add("-Dsun.java2d.opengl=false");
                localArrayList.add("-Dsun.java2d.pmoffscreen=false");
                localArrayList.add("-classpath");
                localArrayList.add(str);
                localArrayList.add("net.minecraft.LauncherFrame");
                ProcessBuilder localProcessBuilder = new ProcessBuilder(localArrayList);
                Process localProcess = localProcessBuilder.start();
                if (localProcess == null) {
                    throw new Exception("!");
                }
                System.exit(0);
            }
            catch (Exception localException) {
                localException.printStackTrace();
                LauncherFrame.main(paramArrayOfString);
            }
        }
    }
}

