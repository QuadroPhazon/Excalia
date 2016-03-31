/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.register.payment;

import com.earth2me.essentials.register.payment.Method;
import com.earth2me.essentials.register.payment.methods.BOSE7;
import com.earth2me.essentials.register.payment.methods.MCUR;
import com.earth2me.essentials.register.payment.methods.VaultEco;
import com.earth2me.essentials.register.payment.methods.iCo5;
import com.earth2me.essentials.register.payment.methods.iCo6;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Methods {
    private static String version = null;
    private static boolean self = false;
    private static Method Method = null;
    private static String preferred = "";
    private static final Set<Method> Methods = new HashSet<Method>();
    private static final Set<String> Dependencies = new HashSet<String>();
    private static final Set<Method> Attachables = new HashSet<Method>();

    private static void _init() {
        Methods.addMethod("iConomy", new iCo6());
        Methods.addMethod("iConomy", new iCo5());
        Methods.addMethod("BOSEconomy", new BOSE7());
        Methods.addMethod("Currency", new MCUR());
        Dependencies.add("MultiCurrency");
        Methods.addMethod("Vault", new VaultEco());
    }

    public static void setVersion(String v) {
        version = v;
    }

    public static void reset() {
        version = null;
        self = false;
        Method = null;
        preferred = "";
        Attachables.clear();
    }

    public static String getVersion() {
        return version;
    }

    public static Set<String> getDependencies() {
        return Dependencies;
    }

    public static Method createMethod(Plugin plugin) {
        for (Method method : Methods) {
            if (!method.isCompatible(plugin)) continue;
            method.setPlugin(plugin);
            return method;
        }
        return null;
    }

    private static void addMethod(String name, Method method) {
        Dependencies.add(name);
        Methods.add(method);
    }

    public static boolean hasMethod() {
        return Method != null;
    }

    public static boolean setMethod(PluginManager manager) {
        if (Methods.hasMethod()) {
            return true;
        }
        if (self) {
            self = false;
            return false;
        }
        int count = 0;
        boolean match = false;
        Plugin plugin = null;
        for (String name : Methods.getDependencies()) {
            Method current;
            if (Methods.hasMethod()) break;
            plugin = manager.getPlugin(name);
            if (plugin == null || !plugin.isEnabled() || (current = Methods.createMethod(plugin)) == null) continue;
            if (preferred.isEmpty()) {
                Method = current;
                continue;
            }
            Attachables.add(current);
        }
        if (!preferred.isEmpty()) {
            do {
                if (Methods.hasMethod()) {
                    match = true;
                    continue;
                }
                for (Method attached : Attachables) {
                    if (attached == null) continue;
                    if (Methods.hasMethod()) {
                        match = true;
                        break;
                    }
                    if (preferred.isEmpty()) {
                        Method = attached;
                    }
                    if (count != 0) continue;
                    if (preferred.equalsIgnoreCase(attached.getName())) {
                        Method = attached;
                        continue;
                    }
                    Method = attached;
                }
                ++count;
            } while (!match);
        }
        return Methods.hasMethod();
    }

    public static boolean setPreferred(String check) {
        if (Methods.getDependencies().contains(check)) {
            preferred = check;
            return true;
        }
        return false;
    }

    public static Method getMethod() {
        return Method;
    }

    public static boolean checkDisabled(Plugin method) {
        if (!Methods.hasMethod()) {
            return true;
        }
        if (Method.isCompatible(method)) {
            Method = null;
        }
        return Method == null;
    }

    static {
        Methods._init();
    }
}

