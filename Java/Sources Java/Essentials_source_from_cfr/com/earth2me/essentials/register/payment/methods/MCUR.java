/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  me.ashtheking.currency.Currency
 *  me.ashtheking.currency.CurrencyList
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.Method;
import me.ashtheking.currency.Currency;
import me.ashtheking.currency.CurrencyList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class MCUR
implements Method {
    private Currency currencyList;

    @Override
    public Object getPlugin() {
        return this.currencyList;
    }

    @Override
    public String getName() {
        return "MultiCurrency";
    }

    @Override
    public String getLongName() {
        return this.getName();
    }

    @Override
    public String getVersion() {
        return "0.09";
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return "" + amount + " Currency";
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
        return true;
    }

    @Override
    public boolean hasBankAccount(String bank, String name) {
        return false;
    }

    @Override
    public boolean createAccount(String name) {
        CurrencyList.setValue((String)((String)CurrencyList.maxCurrency((String)name)[0]), (String)name, (double)0.0);
        return true;
    }

    @Override
    public boolean createAccount(String name, Double balance) {
        CurrencyList.setValue((String)((String)CurrencyList.maxCurrency((String)name)[0]), (String)name, (double)balance);
        return true;
    }

    @Override
    public Method.MethodAccount getAccount(String name) {
        return new MCurrencyAccount(name);
    }

    @Override
    public Method.MethodBankAccount getBankAccount(String bank, String name) {
        return null;
    }

    @Override
    public boolean isCompatible(Plugin plugin) {
        return (plugin.getDescription().getName().equalsIgnoreCase("Currency") || plugin.getDescription().getName().equalsIgnoreCase("MultiCurrency")) && plugin instanceof Currency;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.currencyList = (Currency)plugin;
    }

    public class MCurrencyAccount
    implements Method.MethodAccount {
        private final String name;

        public MCurrencyAccount(String name) {
            this.name = name;
        }

        @Override
        public double balance() {
            return CurrencyList.getValue((String)((String)CurrencyList.maxCurrency((String)this.name)[0]), (String)this.name);
        }

        @Override
        public boolean set(double amount) {
            CurrencyList.setValue((String)((String)CurrencyList.maxCurrency((String)this.name)[0]), (String)this.name, (double)amount);
            return true;
        }

        @Override
        public boolean add(double amount) {
            return CurrencyList.add((String)this.name, (double)amount);
        }

        @Override
        public boolean subtract(double amount) {
            return CurrencyList.subtract((String)this.name, (double)amount);
        }

        @Override
        public boolean multiply(double amount) {
            return CurrencyList.multiply((String)this.name, (double)amount);
        }

        @Override
        public boolean divide(double amount) {
            return CurrencyList.divide((String)this.name, (double)amount);
        }

        @Override
        public boolean hasEnough(double amount) {
            return CurrencyList.hasEnough((String)this.name, (double)amount);
        }

        @Override
        public boolean hasOver(double amount) {
            return CurrencyList.hasOver((String)this.name, (double)amount);
        }

        @Override
        public boolean hasUnder(double amount) {
            return CurrencyList.hasUnder((String)this.name, (double)amount);
        }

        @Override
        public boolean isNegative() {
            return CurrencyList.isNegative((String)this.name);
        }

        @Override
        public boolean remove() {
            return CurrencyList.remove((String)this.name);
        }
    }

}

