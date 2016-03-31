/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandclearinventory
extends EssentialsCommand {
    private static final int BASE_AMOUNT = 100000;
    private static final int EXTENDED_CAP = 8;

    public Commandclearinventory() {
        super("clearinventory");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        this.parseCommand(server, user.getSource(), args, user.isAuthorized("essentials.clearinventory.others"), user.isAuthorized("essentials.clearinventory.all"));
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.parseCommand(server, sender, args, true, true);
    }

    private void parseCommand(Server server, CommandSource sender, String[] args, boolean allowOthers, boolean allowAll) throws Exception {
        List players = new ArrayList<Player>();
        int offset = 0;
        if (sender.isPlayer()) {
            players.add((Player)sender.getPlayer());
        }
        if (allowAll && args.length > 0 && args[0].contentEquals("*")) {
            sender.sendMessage(I18n._("inventoryClearingFromAll", new Object[0]));
            offset = 1;
            players = Arrays.asList(server.getOnlinePlayers());
        } else if (allowOthers && args.length > 0 && args[0].trim().length() > 2) {
            offset = 1;
            players = server.matchPlayer(args[0].trim());
        }
        if (players.size() < 1) {
            throw new PlayerNotFoundException();
        }
        for (Player player : players) {
            this.clearHandler(sender, player, args, offset, players.size() < 8);
        }
    }

    protected void clearHandler(CommandSource sender, Player player, String[] args, int offset, boolean showExtended) throws Exception {
        ItemStack stack;
        short data = -1;
        int type = -1;
        int amount = -1;
        if (args.length > offset + 1 && NumberUtil.isInt(args[offset + 1])) {
            amount = Integer.parseInt(args[offset + 1]);
        }
        if (args.length > offset) {
            if (args[offset].equalsIgnoreCase("**")) {
                type = -2;
            } else if (!args[offset].equalsIgnoreCase("*")) {
                String[] split = args[offset].split(":");
                ItemStack item = this.ess.getItemDb().get(split[0]);
                type = item.getTypeId();
                data = split.length > 1 && NumberUtil.isInt(split[1]) ? Short.parseShort(split[1]) : item.getDurability();
            }
        }
        if (type == -1) {
            if (showExtended) {
                sender.sendMessage(I18n._("inventoryClearingAllItems", player.getDisplayName()));
            }
            player.getInventory().clear();
        } else if (type == -2) {
            if (showExtended) {
                sender.sendMessage(I18n._("inventoryClearingAllArmor", player.getDisplayName()));
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        } else if (data == -1) {
            stack = new ItemStack(type);
            if (showExtended) {
                sender.sendMessage(I18n._("inventoryClearingAllStack", stack.getType().toString().toLowerCase(Locale.ENGLISH), player.getDisplayName()));
            }
            player.getInventory().clear(type, (int)data);
        } else if (amount == -1) {
            stack = new ItemStack(type, 100000, data);
            ItemStack removedStack = (ItemStack)player.getInventory().removeItem(new ItemStack[]{stack}).get(0);
            int removedAmount = 100000 - removedStack.getAmount();
            if (removedAmount > 0 || showExtended) {
                sender.sendMessage(I18n._("inventoryClearingStack", removedAmount, stack.getType().toString().toLowerCase(Locale.ENGLISH), player.getDisplayName()));
            }
        } else {
            if (amount < 0) {
                amount = 1;
            }
            stack = new ItemStack(type, amount, data);
            if (player.getInventory().containsAtLeast(stack, amount)) {
                sender.sendMessage(I18n._("inventoryClearingStack", amount, stack.getType().toString().toLowerCase(Locale.ENGLISH), player.getDisplayName()));
                player.getInventory().removeItem(new ItemStack[]{stack});
            } else if (showExtended) {
                sender.sendMessage(I18n._("inventoryClearFail", player.getDisplayName(), amount, stack.getType().toString().toLowerCase(Locale.ENGLISH)));
            }
        }
    }
}

