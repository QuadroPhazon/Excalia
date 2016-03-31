/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commanditemdb
extends EssentialsCommand {
    public Commanditemdb() {
        super("itemdb");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        String itemNameList;
        ItemStack itemStack = null;
        boolean itemHeld = false;
        if (args.length < 1) {
            if (sender.isPlayer()) {
                itemHeld = true;
                itemStack = sender.getPlayer().getItemInHand();
            }
            if (itemStack == null) {
                throw new NotEnoughArgumentsException();
            }
        } else {
            itemStack = this.ess.getItemDb().get(args[0]);
        }
        sender.sendMessage(I18n._("itemType", itemStack.getType().toString(), "" + itemStack.getTypeId() + ":" + Integer.toString(itemStack.getDurability())));
        if (itemHeld && itemStack.getType() != Material.AIR) {
            short maxuses = itemStack.getType().getMaxDurability();
            int durability = maxuses + 1 - itemStack.getDurability();
            if (maxuses != 0) {
                sender.sendMessage(I18n._("durability", Integer.toString(durability)));
            }
        }
        if ((itemNameList = this.ess.getItemDb().names(itemStack)) != null) {
            sender.sendMessage(I18n._("itemNames", this.ess.getItemDb().names(itemStack)));
        }
    }
}

