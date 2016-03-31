/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Chunk
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.BlockState
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.EssentialsTimer;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.lang.management.ManagementFactory;
import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockState;

public class Commandgc
extends EssentialsCommand {
    public Commandgc() {
        super("gc");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        double tps = this.ess.getTimer().getAverageTPS();
        ChatColor color = tps >= 18.0 ? ChatColor.GREEN : (tps >= 15.0 ? ChatColor.YELLOW : ChatColor.RED);
        sender.sendMessage(I18n._("uptime", DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
        sender.sendMessage(I18n._("tps", "" + (Object)color + NumberUtil.formatDouble(tps)));
        sender.sendMessage(I18n._("gcmax", Runtime.getRuntime().maxMemory() / 1024 / 1024));
        sender.sendMessage(I18n._("gctotal", Runtime.getRuntime().totalMemory() / 1024 / 1024));
        sender.sendMessage(I18n._("gcfree", Runtime.getRuntime().freeMemory() / 1024 / 1024));
        List worlds = server.getWorlds();
        for (World w : worlds) {
            String worldType = "World";
            switch (w.getEnvironment()) {
                case NETHER: {
                    worldType = "Nether";
                    break;
                }
                case THE_END: {
                    worldType = "The End";
                }
            }
            int tileEntities = 0;
            for (Chunk chunk : w.getLoadedChunks()) {
                tileEntities += chunk.getTileEntities().length;
            }
            sender.sendMessage(I18n._("gcWorld", worldType, w.getName(), w.getLoadedChunks().length, w.getEntities().size(), tileEntities));
        }
    }

}

