/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  com.iConomy.iConomy
 *  com.iConomy.system.Account
 *  com.iConomy.system.Accounts
 *  com.iConomy.system.BankAccount
 *  com.iConomy.system.Banks
 *  com.iConomy.system.Holdings
 *  com.iConomy.util.Constants
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.Method;
import com.iConomy.iConomy;
import com.iConomy.system.Account;
import com.iConomy.system.Accounts;
import com.iConomy.system.BankAccount;
import com.iConomy.system.Banks;
import com.iConomy.system.Holdings;
import com.iConomy.util.Constants;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class iCo5
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
        return "5";
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
        return Constants.Banking;
    }

    @Override
    public boolean hasBank(String bank) {
        return this.hasBanks() && iConomy.Banks.exists(bank);
    }

    @Override
    public boolean hasAccount(String name) {
        return iConomy.hasAccount((String)name);
    }

    @Override
    public boolean hasBankAccount(String bank, String name) {
        return this.hasBank(bank) && iConomy.getBank((String)bank).hasAccount(name);
    }

    @Override
    public boolean createAccount(String name) {
        if (this.hasAccount(name)) {
            return false;
        }
        return iConomy.Accounts.create(name);
    }

    @Override
    public boolean createAccount(String name, Double balance) {
        if (this.hasAccount(name)) {
            return false;
        }
        if (!iConomy.Accounts.create(name)) {
            return false;
        }
        this.getAccount(name).set(balance);
        return true;
    }

    @Override
    public Method.MethodAccount getAccount(String name) {
        return new iCoAccount(iConomy.getAccount((String)name));
    }

    @Override
    public Method.MethodBankAccount getBankAccount(String bank, String name) {
        return new iCoBankAccount(iConomy.getBank((String)bank).getAccount(name));
    }

    @Override
    public boolean isCompatible(Plugin plugin) {
        return plugin.getDescription().getName().equalsIgnoreCase("iconomy") && plugin.getClass().getName().equals("com.iConomy.iConomy") && plugin instanceof iConomy;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.iConomy = (iConomy)plugin;
    }

    public class iCoBankAccount
    implements Method.MethodBankAccount {
        private final BankAccount account;
        private final Holdings holdings;

        public iCoBankAccount(BankAccount account) {
            this.account = account;
            this.holdings = account.getHoldings();
        }

        public BankAccount getiCoBankAccount() {
            return this.account;
        }

        @Override
        public String getBankName() {
            return this.account.getBankName();
        }

        @Override
        public int getBankId() {
            return this.account.getBankId();
        }

        @Override
        public double balance() {
            return this.holdings.balance();
        }

        @Override
        public boolean set(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.set(amount);
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
            return this.holdings.balance();
        }

        @Override
        public boolean set(double amount) {
            if (this.holdings == null) {
                return false;
            }
            this.holdings.set(amount);
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

