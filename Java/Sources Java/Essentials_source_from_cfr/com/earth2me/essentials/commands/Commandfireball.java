/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Egg
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.LargeFireball
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.SmallFireball
 *  org.bukkit.entity.Snowball
 *  org.bukkit.entity.ThrownExpBottle
 *  org.bukkit.entity.WitherSkull
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.util.Vector
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.WitherSkull;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class Commandfireball
extends EssentialsCommand {
    public Commandfireball() {
        super("fireball");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        reference type = Fireball.class;
        int speed = 2;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("small")) {
                type = SmallFireball.class;
            } else if (args[0].equalsIgnoreCase("arrow")) {
                type = Arrow.class;
            } else if (args[0].equalsIgnoreCase("skull")) {
                type = WitherSkull.class;
            } else if (args[0].equalsIgnoreCase("egg")) {
                type = Egg.class;
            } else if (args[0].equalsIgnoreCase("snowball")) {
                type = Snowball.class;
            } else if (args[0].equalsIgnoreCase("expbottle")) {
                type = ThrownExpBottle.class;
            } else if (args[0].equalsIgnoreCase("large")) {
                type = LargeFireball.class;
            }
        }
        Vector direction = user.getEyeLocation().getDirection().multiply(speed);
        Projectile projectile = (Projectile)user.getWorld().spawn(user.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), (Class)type);
        projectile.setShooter((ProjectileSource)user.getBase());
        projectile.setVelocity(direction);
    }
}

