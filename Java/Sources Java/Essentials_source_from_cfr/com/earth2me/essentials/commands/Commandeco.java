/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.Locale;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Server;

public class Commandeco
extends EssentialsLoopCommand {
    EcoCommands cmd;
    BigDecimal amount;

    public Commandeco() {
        super("eco");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        BigDecimal startingBalance = this.ess.getSettings().getStartingBalance();
        try {
            this.cmd = EcoCommands.valueOf(args[0].toUpperCase(Locale.ENGLISH));
            this.amount = this.cmd == EcoCommands.RESET ? startingBalance : new BigDecimal(args[2].replaceAll("[^0-9\\.]", ""));
        }
        catch (Exception ex) {
            throw new NotEnoughArgumentsException(ex);
        }
        this.loopOfflinePlayers(server, sender, false, true, args[1], args);
        if (this.cmd == EcoCommands.RESET || this.cmd == EcoCommands.SET) {
            if (args[1].contentEquals("**")) {
                server.broadcastMessage(I18n._("resetBalAll", NumberUtil.displayCurrency(this.amount, this.ess)));
            } else if (args[1].contentEquals("*")) {
                server.broadcastMessage(I18n._("resetBal", NumberUtil.displayCurrency(this.amount, this.ess)));
            }
        }
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User player, String[] args) throws NotEnoughArgumentsException, ChargeException, MaxMoneyException {
        switch (this.cmd) {
            case GIVE: {
                player.giveMoney(this.amount, sender);
                break;
            }
            case TAKE: {
                this.take(this.amount, player, sender);
                break;
            }
            case RESET: 
            case SET: {
                this.set(this.amount, player, sender);
            }
        }
    }

    private void take(BigDecimal amount, User player, CommandSource sender) throws ChargeException {
        BigDecimal money = player.getMoney();
        BigDecimal minBalance = this.ess.getSettings().getMinMoney();
        if (money.subtract(amount).compareTo(minBalance) > 0) {
            player.takeMoney(amount, sender);
        } else if (sender == null) {
            try {
                player.setMoney(minBalance);
            }
            catch (MaxMoneyException ex) {
                // empty catch block
            }
            player.sendMessage(I18n._("takenFromAccount", NumberUtil.displayCurrency(player.getMoney(), this.ess)));
        } else {
            throw new ChargeException(I18n._("insufficientFunds", new Object[0]));
        }
    }

    private void set(BigDecimal amount, User player, CommandSource sender) throws MaxMoneyException {
        BigDecimal minBalance = this.ess.getSettings().getMinMoney();
        boolean underMinimum = amount.compareTo(minBalance) < 0;
        player.setMoney(underMinimum ? minBalance : amount);
        player.sendMessage(I18n._("setBal", NumberUtil.displayCurrency(player.getMoney(), this.ess)));
        if (sender != null) {
            sender.sendMessage(I18n._("setBalOthers", player.getDisplayName(), NumberUtil.displayCurrency(player.getMoney(), this.ess)));
        }
    }

    private static enum EcoCommands {
        GIVE,
        TAKE,
        SET,
        RESET;
        

        private EcoCommands() {
        }
    }

}

