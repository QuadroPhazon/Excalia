/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.trc202.CombatTag.CombatTag
 *  com.trc202.CombatTagApi.CombatTagApi
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.plugin.manager;

import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public abstract class CombatTagComunicator {
    public static CombatTagApi combatApi;

    public static boolean isNPC(Entity player) {
        block6 : {
            try {
                if (Bukkit.getServer().getPluginManager().getPlugin("CombatTag") == null) break block6;
                combatApi = new CombatTagApi((CombatTag)Bukkit.getServer().getPluginManager().getPlugin("CombatTag"));
                try {
                    combatApi.getClass().getMethod("isNPC", new Class[0]);
                }
                catch (Exception e) {
                    return false;
                }
                return combatApi.isNPC(player);
            }
            catch (ClassCastException ex) {
                return false;
            }
            catch (NullPointerException npe) {
                return false;
            }
            catch (NoClassDefFoundError ncdfe) {
                return false;
            }
        }
        return false;
    }
}

