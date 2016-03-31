/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.Vault
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.bukkit.Server
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicesManager
 */
package com.earth2me.essentials.register.payment.methods;

import com.earth2me.essentials.register.payment.Method;
import java.util.List;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

public class VaultEco
implements Method {
    private Vault vault;
    private Economy economy;

    public Vault getPlugin() {
        return this.vault;
    }

    @Override
    public boolean createAccount(String name, Double amount) {
        if (this.hasAccount(name)) {
            return false;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.vault.getDescription().getName();
    }

    public String getEconomy() {
        return this.economy == null ? "NoEco" : this.economy.getName();
    }

    @Override
    public String getLongName() {
        return this.getName().concat(" - Economy: ").concat(this.getEconomy());
    }

    @Override
    public String getVersion() {
        return this.vault.getDescription().getVersion();
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return this.economy.format(amount);
    }

    @Override
    public boolean hasBanks() {
        return this.economy.hasBankSupport();
    }

    @Override
    public boolean hasBank(String bank) {
        return this.economy.getBanks().contains(bank);
    }

    @Override
    public boolean hasAccount(String name) {
        return this.economy.hasAccount(name);
    }

    @Override
    public boolean hasBankAccount(String bank, String name) {
        return this.economy.isBankOwner(bank, name).transactionSuccess() || this.economy.isBankMember(bank, name).transactionSuccess();
    }

    @Override
    public boolean createAccount(String name) {
        return this.economy.createBank(name, "").transactionSuccess();
    }

    public boolean createAccount(String name, double balance) {
        if (!this.economy.createBank(name, "").transactionSuccess()) {
            return false;
        }
        return this.economy.bankDeposit(name, balance).transactionSuccess();
    }

    @Override
    public Method.MethodAccount getAccount(String name) {
        if (!this.hasAccount(name)) {
            return null;
        }
        return new VaultAccount(name, this.economy);
    }

    @Override
    public Method.MethodBankAccount getBankAccount(String bank, String name) {
        if (!this.hasBankAccount(bank, name)) {
            return null;
        }
        return new VaultBankAccount(bank, this.economy);
    }

    @Override
    public boolean isCompatible(Plugin plugin) {
        try {
            RegisteredServiceProvider ecoPlugin = plugin.getServer().getServicesManager().getRegistration((Class)Economy.class);
            return plugin instanceof Vault && ecoPlugin != null && !((Economy)ecoPlugin.getProvider()).getName().equals("Essentials Economy");
        }
        catch (LinkageError e) {
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setPlugin(Plugin plugin) {
        this.vault = (Vault)plugin;
        RegisteredServiceProvider economyProvider = this.vault.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economyProvider != null) {
            this.economy = (Economy)economyProvider.getProvider();
        }
    }

    public class VaultBankAccount
    implements Method.MethodBankAccount {
        private final String bank;
        private final Economy economy;

        public VaultBankAccount(String bank, Economy economy) {
            this.bank = bank;
            this.economy = economy;
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
            return this.economy.bankBalance((String)this.bank).balance;
        }

        @Override
        public boolean set(double amount) {
            if (!this.economy.bankWithdraw(this.bank, this.balance()).transactionSuccess()) {
                return false;
            }
            if (amount == 0.0) {
                return true;
            }
            return this.economy.bankDeposit(this.bank, amount).transactionSuccess();
        }

        @Override
        public boolean add(double amount) {
            return this.economy.bankDeposit(this.bank, amount).transactionSuccess();
        }

        @Override
        public boolean subtract(double amount) {
            return this.economy.bankWithdraw(this.bank, amount).transactionSuccess();
        }

        @Override
        public boolean multiply(double amount) {
            double balance = this.balance();
            return this.set(balance * amount);
        }

        @Override
        public boolean divide(double amount) {
            double balance = this.balance();
            return this.set(balance / amount);
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
            return this.set(0.0);
        }
    }

    public class VaultAccount
    implements Method.MethodAccount {
        private final String name;
        private final Economy economy;

        public VaultAccount(String name, Economy economy) {
            this.name = name;
            this.economy = economy;
        }

        @Override
        public double balance() {
            return this.economy.getBalance(this.name);
        }

        @Override
        public boolean set(double amount) {
            if (!this.economy.withdrawPlayer(this.name, this.balance()).transactionSuccess()) {
                return false;
            }
            if (amount == 0.0) {
                return true;
            }
            return this.economy.depositPlayer(this.name, amount).transactionSuccess();
        }

        @Override
        public boolean add(double amount) {
            return this.economy.depositPlayer(this.name, amount).transactionSuccess();
        }

        @Override
        public boolean subtract(double amount) {
            return this.economy.withdrawPlayer(this.name, amount).transactionSuccess();
        }

        @Override
        public boolean multiply(double amount) {
            double balance = this.balance();
            return this.set(balance * amount);
        }

        @Override
        public boolean divide(double amount) {
            double balance = this.balance();
            return this.set(balance / amount);
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
            return this.set(0.0);
        }
    }

}

