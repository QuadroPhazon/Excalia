/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.CitizensManager
 *  org.bukkit.entity.Entity
 */
package fr.xephi.authme.plugin.manager;

import fr.xephi.authme.AuthMe;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.CitizensManager;
import org.bukkit.entity.Entity;

public class CitizensCommunicator {
    public AuthMe instance;

    public CitizensCommunicator(AuthMe instance) {
        this.instance = instance;
    }

    public boolean isNPC(Entity player, AuthMe instance) {
        try {
            if (instance.CitizensVersion == 1) {
                return CitizensManager.isNPC((Entity)player);
            }
            if (instance.CitizensVersion == 2) {
                return CitizensAPI.getNPCRegistry().isNPC(player);
            }
            return false;
        }
        catch (NoClassDefFoundError ncdfe) {
            return false;
        }
        catch (Exception npe) {
            return false;
        }
    }
}

