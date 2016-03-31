/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commandrepair
extends EssentialsCommand {
    public Commandrepair() {
        super("repair");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1 || args[0].equalsIgnoreCase("hand") || !user.isAuthorized("essentials.repair.all")) {
            this.repairHand(user);
        } else if (args[0].equalsIgnoreCase("all")) {
            Trade charge = new Trade("repair-all", this.ess);
            charge.isAffordableFor(user);
            this.repairAll(user);
            charge.charge(user);
        } else {
            throw new NotEnoughArgumentsException();
        }
    }

    public void repairHand(User user) throws Exception {
        ItemStack item = user.getItemInHand();
        if (item == null || item.getType().isBlock() || item.getDurability() == 0) {
            throw new Exception(I18n._("repairInvalidType", new Object[0]));
        }
        if (!(item.getEnchantments().isEmpty() || this.ess.getSettings().getRepairEnchanted() || user.isAuthorized("essentials.repair.enchanted"))) {
            throw new Exception(I18n._("repairEnchanted", new Object[0]));
        }
        String itemName = item.getType().toString().toLowerCase(Locale.ENGLISH);
        Trade charge = new Trade("repair-" + itemName.replace('_', '-'), new Trade("repair-" + item.getTypeId(), new Trade("repair-item", this.ess), this.ess), this.ess);
        charge.isAffordableFor(user);
        this.repairItem(item);
        charge.charge(user);
        user.updateInventory();
        user.sendMessage(I18n._("repair", itemName.replace('_', ' ')));
    }

    public void repairAll(User user) throws Exception {
        ArrayList<String> repaired = new ArrayList<String>();
        this.repairItems(user.getInventory().getContents(), user, repaired);
        if (user.isAuthorized("essentials.repair.armor")) {
            this.repairItems(user.getInventory().getArmorContents(), user, repaired);
        }
        user.updateInventory();
        if (repaired.isEmpty()) {
            throw new Exception(I18n._("repairNone", new Object[0]));
        }
        user.sendMessage(I18n._("repair", StringUtil.joinList(repaired)));
    }

    private void repairItem(ItemStack item) throws Exception {
        Material material = Material.getMaterial((int)item.getTypeId());
        if (material.isBlock() || material.getMaxDurability() < 1) {
            throw new Exception(I18n._("repairInvalidType", new Object[0]));
        }
        if (item.getDurability() == 0) {
            throw new Exception(I18n._("repairAlreadyFixed", new Object[0]));
        }
        item.setDurability(0);
    }

    private void repairItems(ItemStack[] items, IUser user, List<String> repaired) {
        for (ItemStack item : items) {
            if (item == null || item.getType().isBlock() || item.getDurability() == 0) continue;
            String itemName = item.getType().toString().toLowerCase(Locale.ENGLISH);
            Trade charge = new Trade("repair-" + itemName.replace('_', '-'), new Trade("repair-" + item.getTypeId(), new Trade("repair-item", this.ess), this.ess), this.ess);
            try {
                charge.isAffordableFor(user);
            }
            catch (ChargeException ex) {
                user.sendMessage(ex.getMessage());
                continue;
            }
            if (!item.getEnchantments().isEmpty() && !this.ess.getSettings().getRepairEnchanted() && !user.isAuthorized("essentials.repair.enchanted")) continue;
            try {
                this.repairItem(item);
            }
            catch (Exception e) {
                continue;
            }
            try {
                charge.charge(user);
            }
            catch (ChargeException ex) {
                user.sendMessage(ex.getMessage());
            }
            repaired.add(itemName.replace('_', ' '));
        }
    }
}

