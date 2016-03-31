/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitTask
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.earth2me.essentials.textreader.TextPager;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Commandbalancetop
extends EssentialsCommand {
    private static final int CACHETIME = 120000;
    public static final int MINUSERS = 50;
    private static final SimpleTextInput cache = new SimpleTextInput();
    private static long cacheage = 0;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Commandbalancetop() {
        super("balancetop");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        boolean force;
        int page;
        block12 : {
            page = 0;
            force = false;
            if (args.length > 0) {
                try {
                    page = Integer.parseInt(args[0]);
                }
                catch (NumberFormatException ex) {
                    if (!args[0].equalsIgnoreCase("force") || sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.balancetop.force")) break block12;
                    force = true;
                }
            }
        }
        if (!force && lock.readLock().tryLock()) {
            try {
                if (cacheage > System.currentTimeMillis() - 120000) {
                    Commandbalancetop.outputCache(sender, page);
                    return;
                }
                if (this.ess.getUserMap().getUniqueUsers() > 50) {
                    sender.sendMessage(I18n._("orderBalances", this.ess.getUserMap().getUniqueUsers()));
                }
            }
            finally {
                lock.readLock().unlock();
            }
            this.ess.runTaskAsynchronously(new Viewer(sender, page, force));
        } else {
            if (this.ess.getUserMap().getUniqueUsers() > 50) {
                sender.sendMessage(I18n._("orderBalances", this.ess.getUserMap().getUniqueUsers()));
            }
            this.ess.runTaskAsynchronously(new Viewer(sender, page, force));
        }
    }

    private static void outputCache(CommandSource sender, int page) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cacheage);
        DateFormat format = DateFormat.getDateTimeInstance(3, 3);
        sender.sendMessage(I18n._("balanceTop", format.format(cal.getTime())));
        new TextPager(cache).showPage(Integer.toString(page), null, "balancetop", sender);
    }

    private class Viewer
    implements Runnable {
        private final transient CommandSource sender;
        private final transient int page;
        private final transient boolean force;

        public Viewer(CommandSource sender, int page, boolean force) {
            this.sender = sender;
            this.page = page;
            this.force = force;
        }

        @Override
        public void run() {
            lock.readLock().lock();
            try {
                if (!this.force && cacheage > System.currentTimeMillis() - 120000) {
                    Commandbalancetop.outputCache(this.sender, this.page);
                    return;
                }
            }
            finally {
                lock.readLock().unlock();
            }
            Commandbalancetop.this.ess.runTaskAsynchronously(new Calculator(new Viewer(this.sender, this.page, false), this.force));
        }
    }

    private class Calculator
    implements Runnable {
        private final transient Viewer viewer;
        private final boolean force;

        public Calculator(Viewer viewer, boolean force) {
            this.viewer = viewer;
            this.force = force;
        }

        @Override
        public void run() {
            lock.writeLock().lock();
            try {
                if (this.force || cacheage <= System.currentTimeMillis() - 120000) {
                    cache.getLines().clear();
                    HashMap<String, BigDecimal> balances = new HashMap<String, BigDecimal>();
                    BigDecimal totalMoney = BigDecimal.ZERO;
                    if (Commandbalancetop.this.ess.getSettings().isEcoDisabled()) {
                        if (Commandbalancetop.this.ess.getSettings().isDebug()) {
                            Commandbalancetop.this.ess.getLogger().info("Internal economy functions disabled, aborting baltop.");
                        }
                    } else {
                        for (String u : Commandbalancetop.this.ess.getUserMap().getAllUniqueUsers()) {
                            User user = Commandbalancetop.this.ess.getUserMap().getUser(u);
                            if (user == null) continue;
                            BigDecimal userMoney = user.getMoney();
                            user.updateMoneyCache(userMoney);
                            totalMoney = totalMoney.add(userMoney);
                            String name = user.isHidden() ? user.getName() : user.getDisplayName();
                            balances.put(name, userMoney);
                        }
                    }
                    ArrayList sortedEntries = new ArrayList(balances.entrySet());
                    Collections.sort(sortedEntries, new Comparator<Map.Entry<String, BigDecimal>>(){

                        @Override
                        public int compare(Map.Entry<String, BigDecimal> entry1, Map.Entry<String, BigDecimal> entry2) {
                            return entry2.getValue().compareTo(entry1.getValue());
                        }
                    });
                    cache.getLines().add(I18n._("serverTotal", NumberUtil.displayCurrency(totalMoney, Commandbalancetop.this.ess)));
                    int pos = 1;
                    for (Map.Entry entry : sortedEntries) {
                        cache.getLines().add("" + pos + ". " + (String)entry.getKey() + ", " + NumberUtil.displayCurrency((BigDecimal)entry.getValue(), Commandbalancetop.this.ess));
                        ++pos;
                    }
                    cacheage = System.currentTimeMillis();
                }
            }
            finally {
                lock.writeLock().unlock();
            }
            Commandbalancetop.this.ess.runTaskAsynchronously(this.viewer);
        }

    }

}

