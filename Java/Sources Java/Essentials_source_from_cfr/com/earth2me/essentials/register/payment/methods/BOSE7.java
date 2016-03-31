/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  cosine.boseconomy.BOSEconomy
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.Method;
import cosine.boseconomy.BOSEconomy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class BOSE7
implements Method {
    private BOSEconomy BOSEconomy;

    public BOSEconomy getPlugin() {
        return this.BOSEconomy;
    }

    @Override
    public String getName() {
        return "BOSEconomy";
    }

    @Override
    public String getLongName() {
        return this.getName();
    }

    @Override
    public String getVersion() {
        return "0.7.0";
    }

    @Override
    public int fractionalDigits() {
        return this.BOSEconomy.getFractionalDigits();
    }

    @Override
    public String format(double amount) {
        String currency = this.BOSEconomy.getMoneyNamePlural();
        if (amount == 1.0) {
            currency = this.BOSEconomy.getMoneyName();
        }
        return "" + amount + " " + currency;
    }

    @Override
    public boolean hasBanks() {
        return true;
    }

    @Override
    public boolean hasBank(String bank) {
        return this.BOSEconomy.bankExists(bank);
    }

    @Override
    public boolean hasAccount(String name) {
        return this.BOSEconomy.playerRegistered(name, false);
    }

    @Override
    public boolean hasBankAccount(String bank, String name) {
        return this.BOSEconomy.isBankOwner(bank, name) || this.BOSEconomy.isBankMember(bank, name);
    }

    @Override
    public boolean createAccount(String name) {
        if (this.hasAccount(name)) {
            return false;
        }
        this.BOSEconomy.registerPlayer(name);
        return true;
    }

    @Override
    public boolean createAccount(String name, Double balance) {
        if (this.hasAccount(name)) {
            return false;
        }
        this.BOSEconomy.registerPlayer(name);
        this.BOSEconomy.setPlayerMoney(name, balance.doubleValue(), false);
        return true;
    }

    @Override
    public Method.MethodAccount getAccount(String name) {
        if (!this.hasAccount(name)) {
            return null;
        }
        return new BOSEAccount(name, this.BOSEconomy);
    }

    @Override
    public Method.MethodBankAccount getBankAccount(String bank, String name) {
        if (!this.hasBankAccount(bank, name)) {
            return null;
        }
        return new BOSEBankAccount(bank, this.BOSEconomy);
    }

    @Override
    public boolean isCompatible(Plugin plugin) {
        return plugin.getDescription().getName().equalsIgnoreCase("boseconomy") && plugin instanceof BOSEconomy && !plugin.getDescription().getVersion().equals("0.6.2");
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.BOSEconomy = (BOSEconomy)plugin;
    }

    public class BOSEBankAccount
    implements Method.MethodBankAccount {
        private final String bank;
        private final BOSEconomy BOSEconomy;

        public BOSEBankAccount(String bank, BOSEconomy bOSEconomy) {
            this.bank = bank;
            this.BOSEconomy = bOSEconomy;
        }

        @Override
        public String getBankName() {
            return this.bank;
        }

        @Override
        public int getBankId() {
            return -1;
        }

        @Override
        public double balance() {
            return this.BOSEconomy.getBankMoneyDouble(this.bank);
        }

        @Override
        public boolean set(double amount) {
            return this.BOSEconomy.setBankMoney(this.bank, amount, true);
        }

        @Override
        public boolean add(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance + amount, false);
        }

        @Override
        public boolean subtract(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance - amount, false);
        }

        @Override
        public boolean multiply(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance * amount, false);
        }

        @Override
        public boolean divide(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setBankMoney(this.bank, balance / amount, false);
        }

        @Override
        public boolean hasEnough(double amount) {
            return this.balance() >= amount;
        }

        @Override
        public boolean hasOver(double amount) {
            return this.balance() > amount;
        }

        @Override
        public boolean hasUnder(double amount) {
            return this.balance() < amount;
        }

        @Override
        public boolean isNegative() {
            return this.balance() < 0.0;
        }

        @Override
        public boolean remove() {
            return this.BOSEconomy.removeBank(this.bank);
        }
    }

    public class BOSEAccount
    implements Method.MethodAccount {
        private final String name;
        private final BOSEconomy BOSEconomy;

        public BOSEAccount(String name, BOSEconomy bOSEconomy) {
            this.name = name;
            this.BOSEconomy = bOSEconomy;
        }

        @Override
        public double balance() {
            return this.BOSEconomy.getPlayerMoneyDouble(this.name);
        }

        @Override
        public boolean set(double amount) {
            return this.BOSEconomy.setPlayerMoney(this.name, amount, false);
        }

        @Override
        public boolean add(double amount) {
            return this.BOSEconomy.addPlayerMoney(this.name, amount, false);
        }

        @Override
        public boolean subtract(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setPlayerMoney(this.name, balance - amount, false);
        }

        @Override
        public boolean multiply(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setPlayerMoney(this.name, balance * amount, false);
        }

        @Override
        public boolean divide(double amount) {
            double balance = this.balance();
            return this.BOSEconomy.setPlayerMoney(this.name, balance / amount, false);
        }

        @Override
        public boolean hasEnough(double amount) {
            return this.balance() >= amount;
        }

        @Override
        public boolean hasOver(double amount) {
            return this.balance() > amount;
        }

        @Override
        public boolean hasUnder(double amount) {
            return this.balance() < amount;
        }

        @Override
        public boolean isNegative() {
            return this.balance() < 0.0;
        }

        @Override
        public boolean remove() {
            return false;
        }
    }

}

