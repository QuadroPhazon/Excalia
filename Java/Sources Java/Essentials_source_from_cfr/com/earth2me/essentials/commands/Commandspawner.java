/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.CreatureSpawner
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.Mob;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.LocationUtil;
import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.util.Locale;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Commandspawner
extends EssentialsCommand {
    public Commandspawner() {
        super("spawner");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 1 || args[0].length() < 2) {
            throw new NotEnoughArgumentsException(I18n._("mobsAvailable", StringUtil.joinList(Mob.getMobList())));
        }
        Location target = LocationUtil.getTarget((LivingEntity)user.getBase());
        if (target == null || target.getBlock().getType() != Material.MOB_SPAWNER) {
            throw new Exception(I18n._("mobSpawnTarget", new Object[0]));
        }
        String name = args[0];
        int delay = 0;
        Mob mob = null;
        mob = Mob.fromName(name);
        if (mob == null) {
            throw new Exception(I18n._("invalidMob", new Object[0]));
        }
        if (this.ess.getSettings().getProtectPreventSpawn(mob.getType().toString().toLowerCase(Locale.ENGLISH))) {
            throw new Exception(I18n._("disabledToSpawnMob", new Object[0]));
        }
        if (!user.isAuthorized("essentials.spawner." + mob.name.toLowerCase(Locale.ENGLISH))) {
            throw new Exception(I18n._("noPermToSpawnMob", new Object[0]));
        }
        if (args.length > 1 && NumberUtil.isInt(args[1])) {
            delay = Integer.parseInt(args[1]);
        }
        Trade charge = new Trade("spawner-" + mob.name.toLowerCase(Locale.ENGLISH), this.ess);
        charge.isAffordableFor(user);
        try {
            CreatureSpawner spawner = (CreatureSpawner)target.getBlock().getState();
            spawner.setSpawnedType(mob.getType());
            spawner.setDelay(delay);
            spawner.update();
        }
        catch (Throwable ex) {
            throw new Exception(I18n._("mobSpawnError", new Object[0]), ex);
        }
        charge.charge(user);
        user.sendMessage(I18n._("setSpawner", mob.name));
    }
}

