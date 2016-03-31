/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.Potions;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import net.ess3.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Commandpotion
extends EssentialsCommand {
    public Commandpotion() {
        super("potion");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        ItemStack stack = user.getItemInHand();
        if (args.length == 0) {
            TreeSet<String> potionslist = new TreeSet<String>();
            for (Map.Entry<String, PotionEffectType> entry : Potions.entrySet()) {
                String potionName = entry.getValue().getName().toLowerCase(Locale.ENGLISH);
                if (!potionslist.contains(potionName) && !user.isAuthorized("essentials.potion." + potionName)) continue;
                potionslist.add(entry.getKey());
            }
            throw new NotEnoughArgumentsException(I18n._("potions", StringUtil.joinList(potionslist.toArray())));
        }
        if (stack.getType() != Material.POTION) throw new Exception(I18n._("holdPotion", new Object[0]));
        PotionMeta pmeta = (PotionMeta)stack.getItemMeta();
        if (args.length <= 0) return;
        if (args[0].equalsIgnoreCase("clear")) {
            pmeta.clearCustomEffects();
            stack.setItemMeta((ItemMeta)pmeta);
            return;
        } else if (args[0].equalsIgnoreCase("apply") && user.isAuthorized("essentials.potion.apply")) {
            for (PotionEffect effect : pmeta.getCustomEffects()) {
                effect.apply((LivingEntity)user.getBase());
            }
            return;
        } else {
            if (args.length < 3) {
                throw new NotEnoughArgumentsException();
            }
            MetaItemStack mStack = new MetaItemStack(stack);
            for (String arg : args) {
                mStack.addPotionMeta(user.getSource(), true, arg, this.ess);
            }
            if (mStack.completePotion()) {
                pmeta = (PotionMeta)mStack.getItemStack().getItemMeta();
                stack.setItemMeta((ItemMeta)pmeta);
                return;
            } else {
                user.sendMessage(I18n._("invalidPotion", new Object[0]));
                throw new NotEnoughArgumentsException();
            }
        }
    }
}

