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
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandrules
extends EssentialsCommand {
    public Commandrules() {
        super("rules");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        TextInput input = new TextInput(sender, "rules", true, this.ess);
        KeywordReplacer output = new KeywordReplacer(input, sender, this.ess);
        TextPager pager = new TextPager(output);
        pager.showPage(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null, "rules", sender);
    }
}

