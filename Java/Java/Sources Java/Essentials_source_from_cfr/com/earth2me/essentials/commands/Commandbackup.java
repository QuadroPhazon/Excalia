/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.Backup;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.commands.EssentialsCommand;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandbackup
extends EssentialsCommand {
    public Commandbackup() {
        super("backup");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        Backup backup = this.ess.getBackup();
        if (backup == null) {
            throw new Exception(I18n._("backupDisabled", new Object[0]));
        }
        String command = this.ess.getSettings().getBackupCommand();
        if (command == null || "".equals(command) || "save-all".equalsIgnoreCase(command)) {
            throw new Exception(I18n._("backupDisabled", new Object[0]));
        }
        backup.run();
        sender.sendMessage(I18n._("backupStarted", new Object[0]));
    }
}

