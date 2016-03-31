/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import java.math.BigDecimal;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandpay
extends EssentialsLoopCommand {
    BigDecimal amount;

    public Commandpay() {
        super("pay");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        this.amount = new BigDecimal(args[1].replaceAll("[^0-9\\.]", ""));
        this.loopOnlinePlayers(server, user.getSource(), false, user.isAuthorized("essentials.pay.multiple"), args[0], args);
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User player, String[] args) throws ChargeException {
        User user = this.ess.getUser(sender.getPlayer());
        try {
            user.payUser(player, this.amount);
            Trade.log("Command", "Pay", "Player", user.getName(), new Trade(this.amount, this.ess), player.getName(), new Trade(this.amount, this.ess), user.getLocation(), this.ess);
        }
        catch (MaxMoneyException ex) {
            sender.sendMessage(I18n._("maxMoney", new Object[0]));
        }
    }
}

