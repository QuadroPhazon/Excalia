/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.api.API;
import fr.xephi.authme.cache.limbo.LimboCache;
import fr.xephi.authme.cache.limbo.LimboPlayer;
import fr.xephi.authme.events.AuthMeTeleportEvent;
import fr.xephi.authme.settings.Settings;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Utils {
    private String currentGroup;
    private static Utils singleton;
    int id;
    public AuthMe plugin;

    public Utils(AuthMe plugin) {
        this.plugin = plugin;
    }

    public void setGroup(Player player, groupType group) {
        this.setGroup(player.getName(), group);
    }

    public void setGroup(String player, groupType group) {
        World world;
        if (!Settings.isPermissionCheckEnabled.booleanValue()) {
            return;
        }
        if (this.plugin.permission == null) {
            return;
        }
        String name = player;
        try {
            world = null;
            this.currentGroup = this.plugin.permission.getPrimaryGroup(world, name);
        }
        catch (UnsupportedOperationException e) {
            ConsoleLogger.showError("Your permission system (" + this.plugin.permission.getName() + ") do not support Group system with that config... unhook!");
            this.plugin.permission = null;
            return;
        }
        world = null;
        switch (group) {
            case UNREGISTERED: {
                this.plugin.permission.playerRemoveGroup(world, name, this.currentGroup);
                this.plugin.permission.playerAddGroup(world, name, Settings.unRegisteredGroup);
                break;
            }
            case REGISTERED: {
                this.plugin.permission.playerRemoveGroup(world, name, this.currentGroup);
                this.plugin.permission.playerAddGroup(world, name, Settings.getRegisteredGroup);
                break;
            }
            case NOTLOGGEDIN: {
                if (!this.useGroupSystem()) break;
                this.plugin.permission.playerRemoveGroup(world, name, this.currentGroup);
                this.plugin.permission.playerAddGroup(world, name, Settings.getUnloggedinGroup);
                break;
            }
            case LOGGEDIN: {
                LimboPlayer limbo;
                if (!this.useGroupSystem() || (limbo = LimboCache.getInstance().getLimboPlayer(name)) == null) break;
                String realGroup = limbo.getGroup();
                this.plugin.permission.playerRemoveGroup(world, name, this.currentGroup);
                this.plugin.permission.playerAddGroup(world, name, realGroup);
                break;
            }
        }
    }

    public boolean addNormal(Player player, String group) {
        if (!this.useGroupSystem()) {
            return false;
        }
        if (this.plugin.permission == null) {
            return false;
        }
        World world = null;
        try {
            if (this.plugin.permission.playerRemoveGroup(world, player.getName().toString(), Settings.getUnloggedinGroup) && this.plugin.permission.playerAddGroup(world, player.getName().toString(), group)) {
                return true;
            }
        }
        catch (UnsupportedOperationException e) {
            ConsoleLogger.showError("Your permission system (" + this.plugin.permission.getName() + ") do not support Group system with that config... unhook!");
            this.plugin.permission = null;
            return false;
        }
        return false;
    }

    public void hasPermOnJoin(Player player) {
        if (this.plugin.permission == null) {
            return;
        }
        for (String permission : Settings.getJoinPermissions) {
            if (!this.plugin.permission.playerHas(player, permission)) continue;
            this.plugin.permission.playerAddTransient(player, permission);
        }
    }

    public boolean isUnrestricted(Player player) {
        if (!Settings.isAllowRestrictedIp.booleanValue()) {
            return false;
        }
        if (Settings.getUnrestrictedName.isEmpty() || Settings.getUnrestrictedName == null) {
            return false;
        }
        if (Settings.getUnrestrictedName.contains(player.getName())) {
            return true;
        }
        return false;
    }

    public static Utils getInstance() {
        singleton = new Utils(AuthMe.getInstance());
        return singleton;
    }

    private boolean useGroupSystem() {
        if (Settings.isPermissionCheckEnabled.booleanValue() && !Settings.getUnloggedinGroup.isEmpty()) {
            return true;
        }
        return false;
    }

    public void packCoords(double x, double y, double z, String w, final Player pl) {
        World theWorld = w.equals("unavailableworld") ? pl.getWorld() : Bukkit.getWorld((String)w);
        if (theWorld == null) {
            theWorld = pl.getWorld();
        }
        World world = theWorld;
        final Location locat = new Location(world, x, y, z);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

            public void run() {
                AuthMeTeleportEvent tpEvent = new AuthMeTeleportEvent(pl, locat);
                Utils.this.plugin.getServer().getPluginManager().callEvent((Event)tpEvent);
                if (!tpEvent.isCancelled()) {
                    if (!tpEvent.getTo().getChunk().isLoaded()) {
                        tpEvent.getTo().getChunk().load();
                    }
                    pl.teleport(tpEvent.getTo());
                }
            }
        });
    }

    public boolean obtainToken() {
        File file = new File("plugins" + File.separator + "AuthMe" + File.separator + "passpartu.token");
        if (file.exists()) {
            file.delete();
        }
        FileWriter writer = null;
        try {
            file.createNewFile();
            writer = new FileWriter(file);
            String token = this.generateToken();
            writer.write(token + ":" + System.currentTimeMillis() / 1000 + API.newline);
            writer.flush();
            ConsoleLogger.info("[AuthMe] Security passpartu token: " + token);
            writer.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean readToken(String inputToken) {
        File file = new File("plugins" + File.separator + "AuthMe" + File.separator + "passpartu.token");
        if (!file.exists()) {
            return false;
        }
        if (inputToken.isEmpty()) {
            return false;
        }
        Scanner reader = null;
        try {
            reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] tokenInfo;
                String line = reader.nextLine();
                if (!line.contains(":") || !(tokenInfo = line.split(":"))[0].equals(inputToken) || System.currentTimeMillis() / 1000 - 30 > (long)Integer.parseInt(tokenInfo[1])) continue;
                file.delete();
                reader.close();
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        reader.close();
        return false;
    }

    private String generateToken() {
        Random rnd = new Random();
        char[] arr = new char[5];
        for (int i = 0; i < 5; ++i) {
            int n = rnd.nextInt(36);
            arr[i] = (char)(n < 10 ? 48 + n : 97 + n - 10);
        }
        return new String(arr);
    }

    public static void forceGM(Player player) {
        if (!AuthMe.getInstance().authmePermissible(player, "authme.bypassforcesurvival")) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public static enum groupType {
        UNREGISTERED,
        REGISTERED,
        NOTLOGGEDIN,
        LOGGEDIN;
        

        private groupType() {
        }
    }

}

