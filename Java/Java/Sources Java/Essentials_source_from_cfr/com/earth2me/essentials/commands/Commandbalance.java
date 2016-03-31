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
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import net.ess3.api.IEssentials;
import org.bukkit.Server;

public class Commandbalance
extends EssentialsCommand {
    public Commandbalance() {
        super("balance");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        User target = this.getPlayer(server, args, 0, true, true);
        Object[] arrobject = new Object[2];
        arrobject[0] = target.isHidden() ? target.getName() : target.getDisplayName();
        arrobject[1] = NumberUtil.displayCurrency(target.getMoney(), this.ess);
        sender.sendMessage(I18n._("balanceOther", arrobject));
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length == 1 && user.isAuthorized("essentials.balance.others")) {
            User target = this.getPlayer(server, args, 0, true, true);
            BigDecimal bal = target.getMoney();
            Object[] arrobject = new Object[2];
            arrobject[0] = target.isHidden() ? target.getName() : target.getDisplayName();
            arrobject[1] = NumberUtil.displayCurrency(bal, this.ess);
            user.sendMessage(I18n._("balanceOther", arrobject));
        } else if (args.length < 2) {
            BigDecimal bal = user.getMoney();
            user.sendMessage(I18n._("balance", NumberUtil.displayCurrency(bal, this.ess)));
        } else {
            throw new NotEnoughArgumentsException();
        }
    }
}

