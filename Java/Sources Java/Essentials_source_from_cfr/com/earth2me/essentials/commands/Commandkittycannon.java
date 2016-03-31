/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Ocelot
 *  org.bukkit.entity.Ocelot$Type
 *  org.bukkit.util.Vector
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.Mob;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import java.util.Random;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.util.Vector;

public class Commandkittycannon
extends EssentialsCommand {
    private static final Random random = new Random();

    public Commandkittycannon() {
        super("kittycannon");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        Mob cat = Mob.OCELOT;
        final Ocelot ocelot = (Ocelot)cat.spawn(user.getWorld(), server, user.getEyeLocation());
        if (ocelot == null) {
            return;
        }
        int i = random.nextInt(Ocelot.Type.values().length);
        ocelot.setCatType(Ocelot.Type.values()[i]);
        ocelot.setTamed(true);
        ocelot.setBaby();
        ocelot.setVelocity(user.getEyeLocation().getDirection().multiply(2));
        this.ess.scheduleSyncDelayedTask(new Runnable(){

            @Override
            public void run() {
                Location loc = ocelot.getLocation();
                ocelot.remove();
                loc.getWorld().createExplosion(loc, 0.0f);
            }
        }, 20);
    }

}

