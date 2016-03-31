/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Builder
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Firework
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.util.Vector
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.List;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Commandfirework
extends EssentialsCommand {
    private final transient Pattern splitPattern = Pattern.compile("[:+',;.]");

    public Commandfirework() {
        super("firework");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        ItemStack stack = user.getItemInHand();
        if (stack.getType() != Material.FIREWORK) throw new Exception(I18n._("holdFirework", new Object[0]));
        if (args.length <= 0) throw new NotEnoughArgumentsException();
        if (args[0].equalsIgnoreCase("clear")) {
            FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
            fmeta.clearEffects();
            stack.setItemMeta((ItemMeta)fmeta);
            user.sendMessage(I18n._("fireworkEffectsCleared", new Object[0]));
            return;
        }
        if (args.length > 1 && (args[0].equalsIgnoreCase("power") || args[0].equalsIgnoreCase("p"))) {
            FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
            try {
                int power = Integer.parseInt(args[1]);
                fmeta.setPower(power > 3 ? 4 : power);
            }
            catch (NumberFormatException e) {
                throw new Exception(I18n._("invalidFireworkFormat", args[1], args[0]));
            }
            stack.setItemMeta((ItemMeta)fmeta);
            return;
        }
        if ((args[0].equalsIgnoreCase("fire") || args[0].equalsIgnoreCase("f")) && user.isAuthorized("essentials.firework.fire")) {
            int amount = 1;
            boolean direction = false;
            if (args.length > 1) {
                if (NumberUtil.isInt(args[1])) {
                    int serverLimit = this.ess.getSettings().getSpawnMobLimit();
                    amount = Integer.parseInt(args[1]);
                    if (amount > serverLimit) {
                        amount = serverLimit;
                        user.sendMessage(I18n._("mobSpawnLimit", new Object[0]));
                    }
                } else {
                    direction = true;
                }
            }
            int i = 0;
            while (i < amount) {
                Firework firework = (Firework)user.getWorld().spawnEntity(user.getLocation(), EntityType.FIREWORK);
                FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
                if (direction) {
                    Vector vector = user.getEyeLocation().getDirection().multiply(0.07);
                    if (fmeta.getPower() > 1) {
                        fmeta.setPower(1);
                    }
                    firework.setVelocity(vector);
                }
                firework.setFireworkMeta(fmeta);
                ++i;
            }
            return;
        }
        MetaItemStack mStack = new MetaItemStack(stack);
        for (String arg : args) {
            try {
                mStack.addFireworkMeta(user.getSource(), true, arg, this.ess);
                continue;
            }
            catch (Exception e) {
                user.sendMessage(I18n._("fireworkSyntax", new Object[0]));
                throw e;
            }
        }
        if (mStack.isValidFirework()) {
            FireworkMeta fmeta = (FireworkMeta)mStack.getItemStack().getItemMeta();
            FireworkEffect effect = mStack.getFireworkBuilder().build();
            if (fmeta.getEffects().size() > 0 && !user.isAuthorized("essentials.firework.multiple")) {
                throw new Exception(I18n._("multipleCharges", new Object[0]));
            }
            fmeta.addEffect(effect);
            stack.setItemMeta((ItemMeta)fmeta);
            return;
        } else {
            user.sendMessage(I18n._("fireworkSyntax", new Object[0]));
            throw new Exception(I18n._("fireworkColor", new Object[0]));
        }
    }
}

