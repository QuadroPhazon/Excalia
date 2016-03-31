/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.textreader.HelpInput;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.TextInput;
import com.earth2me.essentials.textreader.TextPager;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandhelp
extends EssentialsCommand {
    public Commandhelp() {
        super("help");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        IText output2;
        IText output2;
        String pageStr = args.length > 0 ? args[0] : null;
        String chapterPageStr = args.length > 1 ? args[1] : null;
        String command = commandLabel;
        TextInput input = new TextInput(user.getSource(), "help", false, this.ess);
        if (input.getLines().isEmpty()) {
            if (NumberUtil.isInt(pageStr) || pageStr == null) {
                output2 = new HelpInput(user, "", this.ess);
            } else {
                if (pageStr.length() > 26) {
                    pageStr = pageStr.substring(0, 25);
                }
                output2 = new HelpInput(user, pageStr.toLowerCase(Locale.ENGLISH), this.ess);
                command = command.concat(" ").concat(pageStr);
                pageStr = chapterPageStr;
            }
            chapterPageStr = null;
        } else {
            output2 = new KeywordReplacer(input, user.getSource(), this.ess);
        }
        TextPager pager = new TextPager(output2);
        pager.showPage(pageStr, chapterPageStr, command, user.getSource());
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        sender.sendMessage(I18n._("helpConsole", new Object[0]));
    }
}

