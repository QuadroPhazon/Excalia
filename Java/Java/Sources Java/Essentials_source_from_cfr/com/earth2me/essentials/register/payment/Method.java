/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package com.earth2me.essentials.register.payment;

import org.bukkit.plugin.Plugin;

public interface Method {
    public Object getPlugin();

    public String getName();

    public String getLongName();

    public String getVersion();

    public int fractionalDigits();

    public String format(double var1);

    public boolean hasBanks();

    public boolean hasBank(String var1);

    public boolean hasAccount(String var1);

    public boolean hasBankAccount(String var1, String var2);

    public boolean createAccount(String var1);

    public boolean createAccount(String var1, Double var2);

    public MethodAccount getAccount(String var1);

    public MethodBankAccount getBankAccount(String var1, String var2);

    public boolean isCompatible(Plugin var1);

    public void setPlugin(Plugin var1);

    public static interface MethodBankAccount {
        public double balance();

        public String getBankName();

        public int getBankId();

        public boolean set(double var1);

        public boolean add(double var1);

        public boolean subtract(double var1);

        public boolean multiply(double var1);

        public boolean divide(double var1);

        public boolean hasEnough(double var1);

        public boolean hasOver(double var1);

        public boolean hasUnder(double var1);

        public boolean isNegative();

        public boolean remove();

        public String toString();
    }

    public static interface MethodAccount {
        public double balance();

        public boolean set(double var1);

        public boolean add(double var1);

        public boolean subtract(double var1);

        public boolean multiply(double var1);

        public boolean divide(double var1);

        public boolean hasEnough(double var1);

        public boolean hasOver(double var1);

        public boolean hasUnder(double var1);

        public boolean isNegative();

        public boolean remove();

        public String toString();
    }

}

