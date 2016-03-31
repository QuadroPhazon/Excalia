/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Ambient
 *  org.bukkit.entity.Animals
 *  org.bukkit.entity.Boat
 *  org.bukkit.entity.ComplexLivingEntity
 *  org.bukkit.entity.EnderCrystal
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.Flying
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.ItemFrame
 *  org.bukkit.entity.Minecart
 *  org.bukkit.entity.Monster
 *  org.bukkit.entity.NPC
 *  org.bukkit.entity.Painting
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Slime
 *  org.bukkit.entity.Snowman
 *  org.bukkit.entity.Tameable
 *  org.bukkit.entity.WaterMob
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Mob;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Flying;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.WaterMob;

public class Commandremove
extends EssentialsCommand {
    public Commandremove() {
        super("remove");
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        World world = user.getWorld();
        int radius = 0;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        if (args.length >= 2) {
            try {
                radius = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                world = this.ess.getWorld(args[1]);
            }
        }
        if (args.length >= 3) {
            radius = 0;
            world = this.ess.getWorld(args[2]);
        }
        this.parseCommand(server, user.getSource(), args, world, radius);
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        World world = this.ess.getWorld(args[1]);
        this.parseCommand(server, sender, args, world, 0);
    }

    private void parseCommand(Server server, CommandSource sender, String[] args, World world, int radius) throws Exception {
        ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> customTypes = new ArrayList<String>();
        if (world == null) {
            throw new Exception(I18n._("invalidWorld", new Object[0]));
        }
        if (args[0].contentEquals("*") || args[0].contentEquals("all")) {
            types.add(0, "ALL");
        } else {
            for (String entityType : args[0].split(",")) {
                ToRemove toRemove;
                try {
                    toRemove = ToRemove.valueOf(entityType.toUpperCase(Locale.ENGLISH));
                }
                catch (Exception e) {
                    try {
                        toRemove = ToRemove.valueOf(entityType.concat("S").toUpperCase(Locale.ENGLISH));
                    }
                    catch (Exception ee) {
                        toRemove = ToRemove.CUSTOM;
                        customTypes.add(entityType);
                    }
                }
                types.add(toRemove.toString());
            }
        }
        this.removeHandler(sender, types, customTypes, world, radius);
    }

    private void removeHandler(CommandSource sender, List<String> types, List<String> customTypes, World world, int radius) {
        int removed = 0;
        if (radius > 0) {
            radius *= radius;
        }
        ArrayList<ToRemove> removeTypes = new ArrayList<ToRemove>();
        ArrayList<Mob> customRemoveTypes = new ArrayList<Mob>();
        for (String s : types) {
            removeTypes.add(ToRemove.valueOf(s));
        }
        boolean warnUser = false;
        for (String s2 : customTypes) {
            Mob mobType = Mob.fromName(s2);
            if (mobType == null) {
                warnUser = true;
                continue;
            }
            customRemoveTypes.add(mobType);
        }
        if (warnUser) {
            sender.sendMessage(I18n._("invalidMob", new Object[0]));
        }
        for (Chunk chunk : world.getLoadedChunks()) {
            for (Entity e : chunk.getEntities()) {
                if (radius > 0 && sender.getPlayer().getLocation().distanceSquared(e.getLocation()) > (double)radius || e instanceof HumanEntity) continue;
                for (ToRemove toRemove : removeTypes) {
                    if (e instanceof Tameable && ((Tameable)e).isTamed()) {
                        if (toRemove != ToRemove.TAMED) continue;
                        e.remove();
                        ++removed;
                    }
                    switch (toRemove) {
                        case DROPS: {
                            if (!(e instanceof Item)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case ARROWS: {
                            if (!(e instanceof Projectile)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case BOATS: {
                            if (!(e instanceof Boat)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case MINECARTS: {
                            if (!(e instanceof Minecart)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case XP: {
                            if (!(e instanceof ExperienceOrb)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case PAINTINGS: {
                            if (!(e instanceof Painting)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case ITEMFRAMES: {
                            if (!(e instanceof ItemFrame)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case ENDERCRYSTALS: {
                            if (!(e instanceof EnderCrystal)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case AMBIENT: {
                            if (!(e instanceof Flying)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case HOSTILE: 
                        case MONSTERS: {
                            if (!(e instanceof Monster) && !(e instanceof ComplexLivingEntity) && !(e instanceof Flying) && !(e instanceof Slime)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case PASSIVE: 
                        case ANIMALS: {
                            if (!(e instanceof Animals) && !(e instanceof NPC) && !(e instanceof Snowman) && !(e instanceof WaterMob) && !(e instanceof Ambient)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case MOBS: {
                            if (!(e instanceof Animals) && !(e instanceof NPC) && !(e instanceof Snowman) && !(e instanceof WaterMob) && !(e instanceof Monster) && !(e instanceof ComplexLivingEntity) && !(e instanceof Flying) && !(e instanceof Slime) && !(e instanceof Ambient)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case ENTITIES: 
                        case ALL: {
                            if (!(e instanceof Entity)) break;
                            e.remove();
                            ++removed;
                            break;
                        }
                        case CUSTOM: {
                            for (Mob type : customRemoveTypes) {
                                if (e.getType() != type.getType()) continue;
                                e.remove();
                                ++removed;
                            }
                            break;
                        }
                    }
                }
            }
        }
        sender.sendMessage(I18n._("removed", removed));
    }

    private static enum ToRemove {
        DROPS,
        ARROWS,
        BOATS,
        MINECARTS,
        XP,
        PAINTINGS,
        ITEMFRAMES,
        ENDERCRYSTALS,
        HOSTILE,
        MONSTERS,
        PASSIVE,
        ANIMALS,
        AMBIENT,
        MOBS,
        ENTITIES,
        ALL,
        CUSTOM,
        TAMED;
        

        private ToRemove() {
        }
    }

}

