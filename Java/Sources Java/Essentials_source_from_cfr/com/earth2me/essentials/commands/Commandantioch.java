/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TNTPrimed
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.utils.LocationUtil;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class Commandantioch
extends EssentialsCommand {
    public Commandantioch() {
        super("antioch");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        if (args.length > 0) {
            this.ess.broadcastMessage(user, "...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe,");
            this.ess.broadcastMessage(user, "who being naughty in My sight, shall snuff it.");
        }
        Location loc = LocationUtil.getTarget((LivingEntity)user.getBase());
        loc.getWorld().spawn(loc, (Class)TNTPrimed.class);
    }
}

