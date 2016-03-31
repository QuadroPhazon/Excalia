/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.iCo6.iConomy
 *  com.iCo6.system.Account
 *  com.iCo6.system.Accounts
 *  com.iCo6.system.Holdings
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.Method;
import com.iCo6.iConomy;
import com.iCo6.system.Account;
import com.iCo6.system.Accounts;
import com.iCo6.system.Holdings;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class iCo6
implements Method {
    private iConomy iConomy;

    public iConomy getPlugin() {
        return this.iConomy;
    }

    @Override
    public String getName() {
        return "iConomy";
    }

    @Override
    public String getLongName() {
        return this.getName();
    }

    @Override
    public String getVersion() {
        return "6";
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return iConomy.format((double)amount);
    }

    @Override
    public boolean hasBanks() {
        return false;
    }

    @Override
    public boolean hasBank(String bank) {
        return false;
    }

    @Override
    public boolean hasAccount(String name) {
        return new Accounts().exists(name);
    }

    @Override
    public boolean hasBankAccount(String bank, String name) {
        return false;
    }

    @Override
    public boolean createAccount(String name) {
        if (this.hasAccount(name)) {
            return false;
        }
        return new Accounts().create(name);
    }

    @Override
    public boolean createAccount(String name, Double balance) {
        if (this.hasAccount(name)) {
            return false;
        }
        return new Accounts().create(name, balance);
    }

    @Override
    public Method.MethodAccount getAccount(String name) {
        return new iCoAccount(new Accounts().get(name));
    }

    @Override
    public Method.MethodBankAccount getBankAccount(String bank, String name) {
        return null;
    }

    @Override
    public boolean isCompatible(Plugin plugin) {
        return plugin.getDescription().getName().equalsIgnoreCase("iconomy") && plugin.getClass().getName().equals("com.iCo6.iConomy") && plugin instanceof iConomy;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.iConomy = (iConomy)plugin;
    }

    public class iCoAccount
    implements Method.MethodAccount {
        private final Account account;
        private final Holdings holdings;

        public iCoAccount(Account account) {
            this.account = account;
            this.holdings = account.getHoldings();
        }

        public Account getiCoAccount() {
            return this.account;
        }

        @Override
        public double balance() {
            return this.holdings.getBalance();
        }

        @Override
        public boolean set(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.setBalance(amount);
            return true;
        }

        @Override
        public boolean add(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.add(amount);
            return true;
        }

        @Override
        public boolean subtract(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.subtract(amount);
            return true;
        }

        @Override
        public boolean multiply(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.multiply(amount);
            return true;
        }

        @Override
        public boolean divide(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.divide(amount);
            return true;
        }

        @Override
        public boolean hasEnough(double amount) {
            return this.holdings.hasEnough(amount);
        }

        @Override
        public boolean hasOver(double amount) {
            return this.holdings.hasOver(amount);
        }

        @Override
        public boolean hasUnder(double amount) {
            return this.holdings.hasUnder(amount);
        }

        @Override
        public boolean isNegative() {
            return this.holdings.isNegative();
        }

        @Override
        public boolean remove() {
            if (this.account == null) {
                return false;
            }
            this.account.remove();
            return true;
        }
    }

}

