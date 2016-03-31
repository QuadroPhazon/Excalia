/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.AlternativeCommandsHandler;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.metrics.Metrics;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Commandessentials
extends EssentialsCommand {
    private transient int taskid;
    private final transient Map<Player, Block> noteBlocks = new HashMap<Player, Block>();
    private final String[] consoleMoo = new String[]{"         (__)", "         (oo)", "   /------\\/", "  / |    ||", " *  /\\---/\\", "    ~~   ~~", "....\"Have you mooed today?\"..."};
    private final String[] playerMoo = new String[]{"            (__)", "            (oo)", "   /------\\/", "  /  |      | |", " *  /\\---/\\", "    ~~    ~~", "....\"Have you mooed today?\"..."};

    public Commandessentials() {
        super("essentials");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length == 0) {
            this.run_disabled(server, sender, commandLabel, args);
        } else if (args[0].equalsIgnoreCase("debug")) {
            this.run_debug(server, sender, commandLabel, args);
        } else if (args[0].equalsIgnoreCase("nya")) {
            this.run_nya(server, sender, commandLabel, args);
        } else if (args[0].equalsIgnoreCase("moo")) {
            this.run_moo(server, sender, commandLabel, args);
        } else if (args[0].equalsIgnoreCase("reset")) {
            this.run_reset(server, sender, commandLabel, args);
        } else if (args[0].equalsIgnoreCase("opt-out")) {
            this.run_optout(server, sender, commandLabel, args);
        } else if (args[0].equalsIgnoreCase("cleanup")) {
            this.run_cleanup(server, sender, commandLabel, args);
        } else {
            this.run_reload(server, sender, commandLabel, args);
        }
    }

    private void run_disabled(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        sender.sendMessage("/<command> <reload/debug>");
        StringBuilder disabledCommands = new StringBuilder();
        for (Map.Entry<String, String> entry : this.ess.getAlternativeCommandsHandler().disabledCommands().entrySet()) {
            if (disabledCommands.length() > 0) {
                disabledCommands.append(", ");
            }
            disabledCommands.append(entry.getKey()).append(" => ").append(entry.getValue());
        }
        if (disabledCommands.length() > 0) {
            sender.sendMessage(I18n._("blockList", new Object[0]));
            sender.sendMessage(disabledCommands.toString());
        }
    }

    private void run_reset(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("/<command> reset <player>");
        }
        User user = this.getPlayer(server, args, 1, true, true);
        user.reset();
        sender.sendMessage("Reset Essentials userdata for player: " + user.getDisplayName());
    }

    private void run_debug(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.ess.getSettings().setDebug(!this.ess.getSettings().isDebug());
        sender.sendMessage("Essentials " + this.ess.getDescription().getVersion() + " debug mode " + (this.ess.getSettings().isDebug() ? "enabled" : "disabled"));
    }

    private void run_reload(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        this.ess.reload();
        sender.sendMessage(I18n._("essentialsReload", this.ess.getDescription().getVersion()));
    }

    private void run_nya(final Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        final HashMap<String, Float> noteMap = new HashMap<String, Float>();
        noteMap.put("1F#", Float.valueOf(0.5f));
        noteMap.put("1G", Float.valueOf(0.53f));
        noteMap.put("1G#", Float.valueOf(0.56f));
        noteMap.put("1A", Float.valueOf(0.6f));
        noteMap.put("1A#", Float.valueOf(0.63f));
        noteMap.put("1B", Float.valueOf(0.67f));
        noteMap.put("1C", Float.valueOf(0.7f));
        noteMap.put("1C#", Float.valueOf(0.76f));
        noteMap.put("1D", Float.valueOf(0.8f));
        noteMap.put("1D#", Float.valueOf(0.84f));
        noteMap.put("1E", Float.valueOf(0.9f));
        noteMap.put("1F", Float.valueOf(0.94f));
        noteMap.put("2F#", Float.valueOf(1.0f));
        noteMap.put("2G", Float.valueOf(1.06f));
        noteMap.put("2G#", Float.valueOf(1.12f));
        noteMap.put("2A", Float.valueOf(1.18f));
        noteMap.put("2A#", Float.valueOf(1.26f));
        noteMap.put("2B", Float.valueOf(1.34f));
        noteMap.put("2C", Float.valueOf(1.42f));
        noteMap.put("2C#", Float.valueOf(1.5f));
        noteMap.put("2D", Float.valueOf(1.6f));
        noteMap.put("2D#", Float.valueOf(1.68f));
        noteMap.put("2E", Float.valueOf(1.78f));
        noteMap.put("2F", Float.valueOf(1.88f));
        String tuneStr = "1D#,1E,2F#,,2A#,1E,1D#,1E,2F#,2B,2D#,2E,2D#,2A#,2B,,2F#,,1D#,1E,2F#,2B,2C#,2A#,2B,2C#,2E,2D#,2E,2C#,,2F#,,2G#,,1D,1D#,,1C#,1D,1C#,1B,,1B,,1C#,,1D,,1D,1C#,1B,1C#,1D#,2F#,2G#,1D#,2F#,1C#,1D#,1B,1C#,1B,1D#,,2F#,,2G#,1D#,2F#,1C#,1D#,1B,1D,1D#,1D,1C#,1B,1C#,1D,,1B,1C#,1D#,2F#,1C#,1D,1C#,1B,1C#,,1B,,1C#,,2F#,,2G#,,1D,1D#,,1C#,1D,1C#,1B,,1B,,1C#,,1D,,1D,1C#,1B,1C#,1D#,2F#,2G#,1D#,2F#,1C#,1D#,1B,1C#,1B,1D#,,2F#,,2G#,1D#,2F#,1C#,1D#,1B,1D,1D#,1D,1C#,1B,1C#,1D,,1B,1C#,1D#,2F#,1C#,1D,1C#,1B,1C#,,1B,,1B,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1B,,";
        final String[] tune = "1D#,1E,2F#,,2A#,1E,1D#,1E,2F#,2B,2D#,2E,2D#,2A#,2B,,2F#,,1D#,1E,2F#,2B,2C#,2A#,2B,2C#,2E,2D#,2E,2C#,,2F#,,2G#,,1D,1D#,,1C#,1D,1C#,1B,,1B,,1C#,,1D,,1D,1C#,1B,1C#,1D#,2F#,2G#,1D#,2F#,1C#,1D#,1B,1C#,1B,1D#,,2F#,,2G#,1D#,2F#,1C#,1D#,1B,1D,1D#,1D,1C#,1B,1C#,1D,,1B,1C#,1D#,2F#,1C#,1D,1C#,1B,1C#,,1B,,1C#,,2F#,,2G#,,1D,1D#,,1C#,1D,1C#,1B,,1B,,1C#,,1D,,1D,1C#,1B,1C#,1D#,2F#,2G#,1D#,2F#,1C#,1D#,1B,1C#,1B,1D#,,2F#,,2G#,1D#,2F#,1C#,1D#,1B,1D,1D#,1D,1C#,1B,1C#,1D,,1B,1C#,1D#,2F#,1C#,1D,1C#,1B,1C#,,1B,,1B,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1A#,,1B,,1F#,1G#,1B,,1F#,1G#,1B,1C#,1D#,1B,1E,1D#,1E,2F#,1B,,1B,,1F#,1G#,1B,1F#,1E,1D#,1C#,1B,,,,1F#,1B,,1F#,1G#,1B,,1F#,1G#,1B,1B,1C#,1D#,1B,1F#,1G#,1F#,1B,,1B,1A#,1B,1F#,1G#,1B,1E,1D#,1E,2F#,1B,,1B,,".split(",");
        this.taskid = this.ess.scheduleSyncRepeatingTask(new Runnable(){
            int i;

            @Override
            public void run() {
                String note = tune[this.i];
                ++this.i;
                if (this.i >= tune.length) {
                    Commandessentials.this.stopTune();
                }
                if (note == null || note.isEmpty()) {
                    return;
                }
                for (Player onlinePlayer : server.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.NOTE_PIANO, 1.0f, ((Float)noteMap.get(note)).floatValue());
                }
            }
        }, 20, 2);
    }

    private void stopTune() {
        this.ess.getScheduler().cancelTask(this.taskid);
        for (Block block : this.noteBlocks.values()) {
            if (block.getType() != Material.NOTE_BLOCK) continue;
            block.setType(Material.AIR);
        }
        this.noteBlocks.clear();
    }

    private void run_moo(Server server, CommandSource sender, String command, String[] args) {
        if (args.length == 2 && args[1].equals("moo")) {
            for (String s : this.consoleMoo) {
                logger.info(s);
            }
            for (String player : this.ess.getServer().getOnlinePlayers()) {
                player.sendMessage(this.playerMoo);
                player.playSound(player.getLocation(), Sound.COW_IDLE, 1.0f, 1.0f);
            }
        } else if (sender.isPlayer()) {
            sender.getSender().sendMessage(this.playerMoo);
            Player player = sender.getPlayer();
            player.playSound(player.getLocation(), Sound.COW_IDLE, 1.0f, 1.0f);
        } else {
            sender.getSender().sendMessage(this.consoleMoo);
        }
    }

    private void run_optout(Server server, CommandSource sender, String command, String[] args) {
        Metrics metrics = this.ess.getMetrics();
        try {
            sender.sendMessage("Essentials collects simple metrics to highlight which features to concentrate work on in the future.");
            if (metrics.isOptOut()) {
                metrics.enable();
            } else {
                metrics.disable();
            }
            sender.sendMessage("Anonymous Metrics are now " + (metrics.isOptOut() ? "disabled" : "enabled") + " for all plugins.");
        }
        catch (IOException ex) {
            sender.sendMessage("Unable to modify 'plugins/PluginMetrics/config.yml': " + ex.getMessage());
        }
    }

    private void run_cleanup(Server server, final CommandSource sender, String command, String[] args) throws Exception {
        if (args.length < 2 || !NumberUtil.isInt(args[1])) {
            sender.sendMessage("This sub-command will delete users who havent logged in in the last <days> days.");
            sender.sendMessage("Optional parameters define the minium amount required to prevent deletion.");
            sender.sendMessage("Unless you define larger default values, this command wil ignore people who have more than 0 money/homes/bans.");
            throw new Exception("/<command> cleanup <days> [money] [homes] [ban count]");
        }
        sender.sendMessage(I18n._("cleaning", new Object[0]));
        final long daysArg = Long.parseLong(args[1]);
        final double moneyArg = args.length >= 3 ? Double.parseDouble(args[2].replaceAll("[^0-9\\.]", "")) : 0.0;
        final int homesArg = args.length >= 4 && NumberUtil.isInt(args[3]) ? Integer.parseInt(args[3]) : 0;
        final int bansArg = args.length >= 5 && NumberUtil.isInt(args[4]) ? Integer.parseInt(args[4]) : 0;
        final UserMap userMap = this.ess.getUserMap();
        this.ess.runTaskAsynchronously(new Runnable(){

            @Override
            public void run() {
                Long currTime = System.currentTimeMillis();
                for (String u : userMap.getAllUniqueUsers()) {
                    User user = Commandessentials.this.ess.getUserMap().getUser(u);
                    if (user == null) continue;
                    int ban = user.getBanReason().isEmpty() ? 0 : 1;
                    long lastLog = user.getLastLogout();
                    if (lastLog == 0) {
                        lastLog = user.getLastLogin();
                    }
                    if (lastLog == 0) {
                        user.setLastLogin(currTime);
                    }
                    long timeDiff = currTime - lastLog;
                    long milliDays = daysArg * 24 * 60 * 60 * 1000;
                    int homeCount = user.getHomes().size();
                    double moneyCount = user.getMoney().doubleValue();
                    if (lastLog == 0 || ban > bansArg || timeDiff < milliDays || homeCount > homesArg || moneyCount > moneyArg) continue;
                    if (Commandessentials.this.ess.getSettings().isDebug()) {
                        Commandessentials.this.ess.getLogger().info("Deleting user: " + user.getName() + " Money: " + moneyCount + " Homes: " + homeCount + " Last seen: " + DateUtil.formatDateDiff(lastLog));
                    }
                    user.reset();
                }
                sender.sendMessage(I18n._("cleaned", new Object[0]));
            }
        });
    }

}

