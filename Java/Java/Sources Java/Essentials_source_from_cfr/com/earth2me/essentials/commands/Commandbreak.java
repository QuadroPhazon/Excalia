/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;

public class Commandbreak
extends EssentialsCommand {
    public Commandbreak() {
        super("break");
    }

    @Override
    public void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        Block block = user.getTargetBlock(null, 20);
        if (block == null) {
            throw new NoChargeException();
        }
        if (block.getType() == Material.AIR) {
            throw new NoChargeException();
        }
        if (block.getType() == Material.BEDROCK && !user.isAuthorized("essentials.break.bedrock")) {
            throw new Exception(I18n._("noBreakBedrock", new Object[0]));
        }
        BlockBreakEvent event = new BlockBreakEvent(block, user.getBase());
        server.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            throw new NoChargeException();
        }
        block.setType(Material.AIR);
    }
}

