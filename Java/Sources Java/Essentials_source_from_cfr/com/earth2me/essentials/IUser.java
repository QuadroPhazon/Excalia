/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.IEssentialsCommand;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.ess3.api.ITeleport;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IUser {
    public boolean isAuthorized(String var1);

    public boolean isAuthorized(IEssentialsCommand var1);

    public boolean isAuthorized(IEssentialsCommand var1, String var2);

    public void healCooldown() throws Exception;

    public void giveMoney(BigDecimal var1) throws MaxMoneyException;

    public void giveMoney(BigDecimal var1, CommandSource var2) throws MaxMoneyException;

    public void payUser(User var1, BigDecimal var2) throws Exception;

    public void takeMoney(BigDecimal var1);

    public void takeMoney(BigDecimal var1, CommandSource var2);

    public boolean canAfford(BigDecimal var1);

    public Boolean canSpawnItem(int var1);

    public void setLastLocation();

    public void setLogoutLocation();

    public void requestTeleport(User var1, boolean var2);

    public ITeleport getTeleport();

    public BigDecimal getMoney();

    public void setMoney(BigDecimal var1) throws MaxMoneyException;

    public void setAfk(boolean var1);

    public boolean isHidden();

    public void setHidden(boolean var1);

    public boolean isGodModeEnabled();

    public String getGroup();

    public boolean inGroup(String var1);

    public boolean canBuild();

    public long getTeleportRequestTime();

    public void enableInvulnerabilityAfterTeleport();

    public void resetInvulnerabilityAfterTeleport();

    public boolean hasInvulnerabilityAfterTeleport();

    public boolean isVanished();

    public void setVanished(boolean var1);

    public boolean isIgnoreExempt();

    public void sendMessage(String var1);

    public Location getHome(String var1) throws Exception;

    public Location getHome(Location var1) throws Exception;

    public List<String> getHomes();

    public void setHome(String var1, Location var2);

    public void delHome(String var1) throws Exception;

    public boolean hasHome();

    public Location getLastLocation();

    public Location getLogoutLocation();

    public long getLastTeleportTimestamp();

    public void setLastTeleportTimestamp(long var1);

    public String getJail();

    public void setJail(String var1);

    public List<String> getMails();

    public void addMail(String var1);

    public boolean isAfk();

    public void setConfigProperty(String var1, Object var2);

    public Set<String> getConfigKeys();

    public Map<String, Object> getConfigMap();

    public Map<String, Object> getConfigMap(String var1);

    public Player getBase();

    public CommandSource getSource();

    public String getName();
}

