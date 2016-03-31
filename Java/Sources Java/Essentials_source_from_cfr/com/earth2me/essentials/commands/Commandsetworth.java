/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Worth;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandsetworth
extends EssentialsCommand {
    public Commandsetworth() {
        super("setworth");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        String price;
        ItemStack stack;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (args.length == 1) {
            stack = user.getInventory().getItemInHand();
            price = args[0];
        } else {
            stack = this.ess.getItemDb().get(args[0]);
            price = args[1];
        }
        this.ess.getWorth().setPrice(stack, Double.parseDouble(price));
        user.sendMessage(I18n._("worthSet", new Object[0]));
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        ItemStack stack = this.ess.getItemDb().get(args[0]);
        this.ess.getWorth().setPrice(stack, Double.parseDouble(args[1]));
        sender.sendMessage(I18n._("worthSet", new Object[0]));
    }
}

