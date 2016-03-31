/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerformBackup {
    private String dbName = Settings.getMySQLDatabase;
    private String dbUserName = Settings.getMySQLUsername;
    private String dbPassword = Settings.getMySQLPassword;
    private String tblname = Settings.getMySQLTablename;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
    String dateString = this.format.format(new Date());
    private String path = AuthMe.getInstance().getDataFolder() + File.separator + "backups" + File.separator + "backup" + this.dateString;
    private AuthMe instance;

    public PerformBackup(AuthMe instance) {
        this.setInstance(instance);
    }

    public boolean DoBackup() {
        switch (Settings.getDataSource) {
            case FILE: {
                return this.FileBackup("auths.db");
            }
            case MYSQL: {
                return this.MySqlBackup();
            }
            case SQLITE: {
                return this.FileBackup(Settings.getMySQLDatabase + ".db");
            }
        }
        return false;
    }

    private boolean MySqlBackup() {
        File dirBackup = new File(AuthMe.getInstance().getDataFolder() + "/backups");
        if (!dirBackup.exists()) {
            dirBackup.mkdir();
        }
        if (this.checkWindows(Settings.backupWindowsPath)) {
            String executeCmd = Settings.backupWindowsPath + "\\bin\\mysqldump.exe -u " + this.dbUserName + " -p" + this.dbPassword + " " + this.dbName + " --tables " + this.tblname + " -r " + this.path + ".sql";
            try {
                Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                int processComplete = runtimeProcess.waitFor();
                if (processComplete == 0) {
                    ConsoleLogger.info("Backup created successfully");
                    return true;
                }
                ConsoleLogger.info("Could not create the backup");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            String executeCmd = "mysqldump -u " + this.dbUserName + " -p" + this.dbPassword + " " + this.dbName + " --tables " + this.tblname + " -r " + this.path + ".sql";
            try {
                Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                int processComplete = runtimeProcess.waitFor();
                if (processComplete == 0) {
                    ConsoleLogger.info("Backup created successfully");
                    return true;
                }
                ConsoleLogger.info("Could not create the backup");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private boolean FileBackup(String backend) {
        File dirBackup = new File(AuthMe.getInstance().getDataFolder() + "/backups");
        if (!dirBackup.exists()) {
            dirBackup.mkdir();
        }
        try {
            this.copy(new File("plugins" + File.separator + "AuthMe" + File.separator + backend), new File(this.path + ".db"));
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean checkWindows(String windowsPath) {
        String isWin = System.getProperty("os.name").toLowerCase();
        if (isWin.indexOf("win") >= 0) {
            if (new File(windowsPath + "\\bin\\mysqldump.exe").exists()) {
                return true;
            }
            ConsoleLogger.showError("Mysql Windows Path is incorrect please check it");
            return true;
        }
        return false;
    }

    void copy(File src, File dst) throws IOException {
        int len;
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public void setInstance(AuthMe instance) {
        this.instance = instance;
    }

    public AuthMe getInstance() {
        return this.instance;
    }

}

