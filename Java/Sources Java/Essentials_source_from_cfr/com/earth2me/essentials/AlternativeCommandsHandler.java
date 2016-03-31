/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.PluginCommandYamlParser
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials;

import com.earth2me.essentials.ISettings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class AlternativeCommandsHandler {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private final transient Map<String, List<PluginCommand>> altcommands = new HashMap<String, List<PluginCommand>>();
    private final transient Map<String, String> disabledList = new HashMap<String, String>();
    private final transient IEssentials ess;

    public AlternativeCommandsHandler(IEssentials ess) {
        this.ess = ess;
        for (Plugin plugin : ess.getServer().getPluginManager().getPlugins()) {
            if (!plugin.isEnabled()) continue;
            this.addPlugin(plugin);
        }
    }

    public final void addPlugin(Plugin plugin) {
        if (plugin.getDescription().getMain().contains("com.earth2me.essentials")) {
            return;
        }
        List commands = PluginCommandYamlParser.parse((Plugin)plugin);
        String pluginName = plugin.getDescription().getName().toLowerCase(Locale.ENGLISH);
        for (Command command : commands) {
            PluginCommand pc = (PluginCommand)command;
            ArrayList<String> labels = new ArrayList<String>(pc.getAliases());
            labels.add(pc.getName());
            PluginCommand reg = this.ess.getServer().getPluginCommand(pluginName + ":" + pc.getName().toLowerCase(Locale.ENGLISH));
            if (reg == null) {
                reg = this.ess.getServer().getPluginCommand(pc.getName().toLowerCase(Locale.ENGLISH));
            }
            if (reg == null || !reg.getPlugin().equals((Object)plugin)) continue;
            for (String label : labels) {
                List<PluginCommand> plugincommands = this.altcommands.get(label.toLowerCase(Locale.ENGLISH));
                if (plugincommands == null) {
                    plugincommands = new ArrayList<PluginCommand>();
                    this.altcommands.put(label.toLowerCase(Locale.ENGLISH), plugincommands);
                }
                boolean found = false;
                for (PluginCommand pc2 : plugincommands) {
                    if (!pc2.getPlugin().equals((Object)plugin)) continue;
                    found = true;
                }
                if (found) continue;
                plugincommands.add(reg);
            }
        }
    }

    public void removePlugin(Plugin plugin) {
        Iterator<Map.Entry<String, List<PluginCommand>>> iterator = this.altcommands.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<PluginCommand>> entry = iterator.next();
            Iterator<PluginCommand> pcIterator = entry.getValue().iterator();
            while (pcIterator.hasNext()) {
                PluginCommand pc = pcIterator.next();
                if (pc.getPlugin() != null && !pc.getPlugin().equals((Object)plugin)) continue;
                pcIterator.remove();
            }
            if (!entry.getValue().isEmpty()) continue;
            iterator.remove();
        }
    }

    public PluginCommand getAlternative(String label) {
        List<PluginCommand> commands = this.altcommands.get(label);
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        if (commands.size() == 1) {
            return commands.get(0);
        }
        for (PluginCommand command : commands) {
            if (!command.getName().equalsIgnoreCase(label)) continue;
            return command;
        }
        return commands.get(0);
    }

    public void executed(String label, PluginCommand pc) {
        String altString = pc.getPlugin().getName() + ":" + pc.getLabel();
        if (this.ess.getSettings().isDebug()) {
            LOGGER.log(Level.INFO, "Essentials: Alternative command " + label + " found, using " + altString);
        }
        this.disabledList.put(label, altString);
    }

    public Map<String, String> disabledCommands() {
        return this.disabledList;
    }
}

