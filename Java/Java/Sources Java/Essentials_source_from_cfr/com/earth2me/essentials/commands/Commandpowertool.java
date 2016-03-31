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
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commandpowertool
extends EssentialsCommand {
    public Commandpowertool() {
        super("powertool");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        String command = Commandpowertool.getFinalArg(args, 0);
        ItemStack itemStack = user.getItemInHand();
        this.powertool(server, user.getSource(), user, commandLabel, itemStack, command);
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 3) {
            throw new Exception("When running from console, usage is: /" + commandLabel + " <player> <itemid> <command>");
        }
        User user = this.getPlayer(server, args, 0, true, true);
        ItemStack itemStack = this.ess.getItemDb().get(args[1]);
        String command = Commandpowertool.getFinalArg(args, 2);
        this.powertool(server, sender, user, commandLabel, itemStack, command);
    }

    protected void powertool(Server server, CommandSource sender, User user, String commandLabel, ItemStack itemStack, String command) throws Exception {
        if (command != null && command.equalsIgnoreCase("d:")) {
            user.clearAllPowertools();
            sender.sendMessage(I18n._("powerToolClearAll", new Object[0]));
            return;
        }
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            throw new Exception(I18n._("powerToolAir", new Object[0]));
        }
        String itemName = itemStack.getType().toString().toLowerCase(Locale.ENGLISH).replaceAll("_", " ");
        List<String> powertools = user.getPowertool(itemStack);
        if (command != null && !command.isEmpty()) {
            if (command.equalsIgnoreCase("l:")) {
                if (powertools == null || powertools.isEmpty()) {
                    throw new Exception(I18n._("powerToolListEmpty", itemName));
                }
                sender.sendMessage(I18n._("powerToolList", StringUtil.joinList(powertools), itemName));
                throw new NoChargeException();
            }
            if (command.startsWith("r:")) {
                if (!powertools.contains(command = command.substring(2))) {
                    throw new Exception(I18n._("powerToolNoSuchCommandAssigned", command, itemName));
                }
                powertools.remove(command);
                sender.sendMessage(I18n._("powerToolRemove", command, itemName));
            } else {
                if (command.startsWith("a:")) {
                    if (sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.powertool.append")) {
                        throw new Exception(I18n._("noPerm", "essentials.powertool.append"));
                    }
                    if (powertools.contains(command = command.substring(2))) {
                        throw new Exception(I18n._("powerToolAlreadySet", command, itemName));
                    }
                } else if (powertools != null && !powertools.isEmpty()) {
                    powertools.clear();
                } else {
                    powertools = new ArrayList<String>();
                }
                powertools.add(command);
                sender.sendMessage(I18n._("powerToolAttach", StringUtil.joinList(powertools), itemName));
            }
        } else {
            if (powertools != null) {
                powertools.clear();
            }
            sender.sendMessage(I18n._("powerToolRemoveAll", itemName));
        }
        if (!user.arePowerToolsEnabled()) {
            user.setPowerToolsEnabled(true);
            user.sendMessage(I18n._("powerToolsEnabled", new Object[0]));
        }
        user.setPowertool(itemStack, powertools);
    }
}

