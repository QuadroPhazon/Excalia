/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.TextInput;
import com.earth2me.essentials.textreader.TextPager;
import com.earth2me.essentials.utils.NumberUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandcustomtext
extends EssentialsCommand {
    public Commandcustomtext() {
        super("customtext");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String page;
        TextInput input = new TextInput(sender, "custom", true, this.ess);
        KeywordReplacer output = new KeywordReplacer(input, sender, this.ess);
        TextPager pager = new TextPager(output);
        String chapter = commandLabel;
        if (commandLabel.equalsIgnoreCase("customtext") && args.length > 0 && !NumberUtil.isInt(commandLabel)) {
            chapter = args[0];
            page = args.length > 1 ? args[1] : null;
        } else {
            page = args.length > 0 ? args[0] : null;
        }
        pager.showPage(chapter, page, null, sender);
    }
}

