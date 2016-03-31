/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.FormatUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.util.List;
import java.util.Set;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitTask;

public class Commandmail
extends EssentialsCommand {
    private static int mailsPerMinute = 0;
    private static long timestamp = 0;

    public Commandmail() {
        super("mail");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length >= 1 && "read".equalsIgnoreCase(args[0])) {
            List<String> mail = user.getMails();
            if (mail.isEmpty()) {
                user.sendMessage(I18n._("noMail", new Object[0]));
                throw new NoChargeException();
            }
            for (String messages : mail) {
                user.sendMessage(messages);
            }
            user.sendMessage(I18n._("mailClear", new Object[0]));
            return;
        }
        if (args.length >= 3 && "send".equalsIgnoreCase(args[0])) {
            if (!user.isAuthorized("essentials.mail.send")) {
                throw new Exception(I18n._("noPerm", "essentials.mail.send"));
            }
            if (user.isMuted()) {
                throw new Exception(I18n._("voiceSilenced", new Object[0]));
            }
            User u = this.ess.getUser(args[1]);
            if (u == null) {
                throw new Exception(I18n._("playerNeverOnServer", args[1]));
            }
            if (!u.isIgnoredPlayer(user)) {
                String mail = user.getName() + ": " + StringUtil.sanitizeString(FormatUtil.stripFormat(Commandmail.getFinalArg(args, 2)));
                if (mail.length() > 1000) {
                    throw new Exception(I18n._("mailTooLong", new Object[0]));
                }
                if (Math.abs(System.currentTimeMillis() - timestamp) > 60000) {
                    timestamp = System.currentTimeMillis();
                    mailsPerMinute = 0;
                }
                if (++mailsPerMinute > this.ess.getSettings().getMailsPerMinute()) {
                    throw new Exception(I18n._("mailDelay", this.ess.getSettings().getMailsPerMinute()));
                }
                u.addMail(mail);
            }
            user.sendMessage(I18n._("mailSent", new Object[0]));
            return;
        }
        if (args.length > 1 && "sendall".equalsIgnoreCase(args[0])) {
            if (!user.isAuthorized("essentials.mail.sendall")) {
                throw new Exception(I18n._("noPerm", "essentials.mail.sendall"));
            }
            this.ess.runTaskAsynchronously(new SendAll(user.getName() + ": " + FormatUtil.stripFormat(Commandmail.getFinalArg(args, 1))));
            user.sendMessage(I18n._("mailSent", new Object[0]));
            return;
        }
        if (args.length >= 1 && "clear".equalsIgnoreCase(args[0])) {
            user.setMails(null);
            user.sendMessage(I18n._("mailCleared", new Object[0]));
            return;
        }
        throw new NotEnoughArgumentsException();
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length >= 1 && "read".equalsIgnoreCase(args[0])) {
            throw new Exception(I18n._("onlyPlayers", commandLabel + " read"));
        }
        if (args.length >= 1 && "clear".equalsIgnoreCase(args[0])) {
            throw new Exception(I18n._("onlyPlayers", commandLabel + " clear"));
        }
        if (args.length >= 3 && "send".equalsIgnoreCase(args[0])) {
            User u = this.ess.getUser(args[1]);
            if (u == null) {
                throw new Exception(I18n._("playerNeverOnServer", args[1]));
            }
            u.addMail("Server: " + Commandmail.getFinalArg(args, 2));
            sender.sendMessage(I18n._("mailSent", new Object[0]));
            return;
        }
        if (args.length >= 2 && "sendall".equalsIgnoreCase(args[0])) {
            this.ess.runTaskAsynchronously(new SendAll("Server: " + Commandmail.getFinalArg(args, 1)));
            sender.sendMessage(I18n._("mailSent", new Object[0]));
            return;
        }
        if (args.length >= 2) {
            User u = this.ess.getUser(args[0]);
            if (u == null) {
                throw new Exception(I18n._("playerNeverOnServer", args[0]));
            }
            u.addMail("Server: " + Commandmail.getFinalArg(args, 1));
            sender.sendMessage(I18n._("mailSent", new Object[0]));
            return;
        }
        throw new NotEnoughArgumentsException();
    }

    private class SendAll
    implements Runnable {
        String message;

        public SendAll(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            for (String username : Commandmail.this.ess.getUserMap().getAllUniqueUsers()) {
                User user = Commandmail.this.ess.getUserMap().getUser(username);
                if (user == null) continue;
                user.addMail(this.message);
            }
        }
    }

}

