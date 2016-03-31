/*
 * Decompiled with CFR 0_110.
 */
package fr.ascentia;

import java.io.File;

public class Util {
    private static File workDir = null;

    public static File getWorkingDirectory() {
        if (workDir == null) {
            workDir = Util.getWorkingDirectory("ascentia");
        }
        return workDir;
    }

    public static File getWorkingDirectory(String applicationName) {
        File workingDirectory;
        String userHome = System.getProperty("user.home", ".");
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

