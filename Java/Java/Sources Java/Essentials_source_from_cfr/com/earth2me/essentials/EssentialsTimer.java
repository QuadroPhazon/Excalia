/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials;

import com.earth2me.essentials.User;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class EssentialsTimer
implements Runnable {
    private final transient IEssentials ess;
    private final transient Set<String> onlineUsers = new HashSet<String>();
    private transient long lastPoll = System.nanoTime();
    private final LinkedList<Double> history = new LinkedList();
    private int skip1 = 0;
    private int skip2 = 0;
    private final long maxTime = 10000000;
    private final long tickInterval = 50;

    EssentialsTimer(IEssentials ess) {
        this.ess = ess;
        this.history.add(20.0);
    }

    @Override
    public void run() {
        double tps;
        long startTime = System.nanoTime();
        long currentTime = System.currentTimeMillis();
        long timeSpent = (startTime - this.lastPoll) / 1000;
        if (timeSpent == 0) {
            timeSpent = 1;
        }
        if (this.history.size() > 10) {
            this.history.remove();
        }
        if ((tps = 5.0E7 / (double)timeSpent) <= 21.0) {
            this.history.add(tps);
        }
        this.lastPoll = startTime;
        int count = 0;
        for (Player player : this.ess.getServer().getOnlinePlayers()) {
            ++count;
            if (this.skip1 > 0) {
                --this.skip1;
                continue;
            }
            if (count % 10 == 0 && System.nanoTime() - startTime > 5000000) {
                this.skip1 = count - 1;
                break;
            }
            try {
                User user = this.ess.getUser(player);
                this.onlineUsers.add(user.getName());
                user.setLastOnlineActivity(currentTime);
                user.checkActivity();
                continue;
            }
            catch (Exception e) {
                this.ess.getLogger().log(Level.WARNING, "EssentialsTimer Error:", e);
            }
        }
        count = 0;
        Iterator<String> iterator = this.onlineUsers.iterator();
        while (iterator.hasNext()) {
            ++count;
            if (this.skip2 > 0) {
                --this.skip2;
                continue;
            }
            if (count % 10 == 0 && System.nanoTime() - startTime > 10000000) {
                this.skip2 = count - 1;
                break;
            }
            User user = this.ess.getUser(iterator.next());
            if (user.getLastOnlineActivity() < currentTime && user.getLastOnlineActivity() > user.getLastLogout()) {
                if (!user.isHidden()) {
                    user.setLastLogout(user.getLastOnlineActivity());
                }
                iterator.remove();
                continue;
            }
            user.checkMuteTimeout(currentTime);
            user.checkJailTimeout(currentTime);
            user.resetInvulnerabilityAfterTeleport();
        }
    }

    public double getAverageTPS() {
        double avg = 0.0;
        for (Double f : this.history) {
            if (f == null) continue;
            avg += f.doubleValue();
        }
        return avg / (double)this.history.size();
    }
}

