/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class HelpInput
implements IText {
    private static final String DESCRIPTION = "description";
    private static final String PERMISSION = "permission";
    private static final String PERMISSIONS = "permissions";
    private static final Logger logger = Logger.getLogger("Essentials");
    private final transient List<String> lines = new ArrayList<String>();
    private final transient List<String> chapters = new ArrayList<String>();
    private final transient Map<String, Integer> bookmarks = new HashMap<String, Integer>();

    public HelpInput(User user, String match, IEssentials ess) throws IOException {
        boolean reported = false;
        ArrayList newLines = new ArrayList();
        String pluginName = "";
        String pluginNameLow = "";
        if (!match.equalsIgnoreCase("")) {
            this.lines.add(I18n._("helpMatching", match));
        }
        for (Plugin p : ess.getServer().getPluginManager().getPlugins()) {
            try {
                ArrayList<String> pluginLines = new ArrayList<String>();
                PluginDescriptionFile desc = p.getDescription();
                Map cmds = desc.getCommands();
                pluginName = p.getDescription().getName();
                pluginNameLow = pluginName.toLowerCase(Locale.ENGLISH);
                if (pluginNameLow.equals(match)) {
                    this.lines.clear();
                    newLines.clear();
                    this.lines.add(I18n._("helpFrom", p.getDescription().getName()));
                }
                boolean isOnWhitelist = user.isAuthorized("essentials.help." + pluginNameLow);
                for (Map.Entry k : cmds.entrySet()) {
                    try {
                        if (!match.equalsIgnoreCase("") && !pluginNameLow.contains(match) && !((String)k.getKey()).toLowerCase(Locale.ENGLISH).contains(match) && (!(((Map)k.getValue()).get("description") instanceof String) || !((String)((Map)k.getValue()).get("description")).toLowerCase(Locale.ENGLISH).contains(match))) continue;
                        if (pluginNameLow.contains("essentials")) {
                            String node = "essentials." + (String)k.getKey();
                            if (ess.getSettings().isCommandDisabled((String)k.getKey()) || !user.isAuthorized(node)) continue;
                            pluginLines.add(I18n._("helpLine", k.getKey(), ((Map)k.getValue()).get("description")));
                            continue;
                        }
                        if (!ess.getSettings().showNonEssCommandsInHelp()) continue;
                        Map value = (Map)k.getValue();
                        Object permissions = null;
                        if (value.containsKey("permission")) {
                            permissions = value.get("permission");
                        } else if (value.containsKey("permissions")) {
                            permissions = value.get("permissions");
                        }
                        if (isOnWhitelist || user.isAuthorized("essentials.help." + pluginNameLow + "." + (String)k.getKey())) {
                            pluginLines.add(I18n._("helpLine", k.getKey(), value.get("description")));
                            continue;
                        }
                        if (permissions instanceof List && !((List)permissions).isEmpty()) {
                            boolean enabled = false;
                            for (Object o : (List)permissions) {
                                if (!(o instanceof String) || !user.isAuthorized(o.toString())) continue;
                                enabled = true;
                                break;
                            }
                            if (!enabled) continue;
                            pluginLines.add(I18n._("helpLine", k.getKey(), value.get("description")));
                            continue;
                        }
                        if (permissions instanceof String && !"".equals(permissions)) {
                            if (!user.isAuthorized(permissions.toString())) continue;
                            pluginLines.add(I18n._("helpLine", k.getKey(), value.get("description")));
                            continue;
                        }
                        if (ess.getSettings().hidePermissionlessHelp()) continue;
                        pluginLines.add(I18n._("helpLine", k.getKey(), value.get("description")));
                    }
                    catch (NullPointerException ex) {}
                }
                if (pluginLines.isEmpty()) continue;
                newLines.addAll(pluginLines);
                if (pluginNameLow.equals(match)) break;
                if (!match.equalsIgnoreCase("")) continue;
                this.lines.add(I18n._("helpPlugin", pluginName, pluginNameLow));
                continue;
            }
            catch (NullPointerException ex) {
                continue;
            }
            catch (Exception ex) {
                if (!reported) {
                    logger.log(Level.WARNING, I18n._("commandHelpFailedForPlugin", pluginNameLow), ex);
                }
                reported = true;
            }
        }
        this.lines.addAll(newLines);
    }

    @Override
    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public List<String> getChapters() {
        return this.chapters;
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return this.bookmarks;
    }
}

