/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.api;

import com.earth2me.essentials.EssentialsConf;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.UserMap;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.earth2me.essentials.utils.NumberUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;

public class Economy {
    private static final Logger logger = Logger.getLogger("Essentials");
    private static IEssentials ess;
    private static final String noCallBeforeLoad = "Essentials API is called before Essentials is loaded.";
    public static final MathContext MATH_CONTEXT;

    public static void setEss(IEssentials aEss) {
        ess = aEss;
    }

    private static void createNPCFile(String name) {
        File folder = new File(ess.getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        EssentialsConf npcConfig = new EssentialsConf(new File(folder, StringUtil.sanitizeFileName(name) + ".yml"));
        npcConfig.load();
        npcConfig.setProperty("npc", true);
        npcConfig.setProperty("money", ess.getSettings().getStartingBalance());
        npcConfig.forceSave();
    }

    private static void deleteNPC(String name) {
        File folder = new File(ess.getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File config = new File(folder, StringUtil.sanitizeFileName(name) + ".yml");
        EssentialsConf npcConfig = new EssentialsConf(config);
        npcConfig.load();
        if (npcConfig.hasProperty("npc") && npcConfig.getBoolean("npc", false)) {
            if (!config.delete()) {
                logger.log(Level.WARNING, I18n._("deleteFileError", config));
            }
            ess.getUserMap().removeUser(name);
        }
    }

    private static User getUserByName(String name) {
        if (ess == null) {
            throw new RuntimeException("Essentials API is called before Essentials is loaded.");
        }
        return ess.getUser(name);
    }

    @Deprecated
    public static double getMoney(String name) throws UserDoesNotExistException {
        return Economy.getMoneyExact(name).doubleValue();
    }

    public static BigDecimal getMoneyExact(String name) throws UserDoesNotExistException {
        User user = Economy.getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        return user.getMoney();
    }

    @Deprecated
    public static void setMoney(String name, double balance) throws UserDoesNotExistException, NoLoanPermittedException {
        try {
            Economy.setMoney(name, BigDecimal.valueOf(balance));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to set balance of " + name + " to " + balance + ": " + e.getMessage(), e);
        }
    }

    public static void setMoney(String name, BigDecimal balance) throws UserDoesNotExistException, NoLoanPermittedException {
        User user = Economy.getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        if (balance.compareTo(ess.getSettings().getMinMoney()) < 0) {
            throw new NoLoanPermittedException();
        }
        if (balance.signum() < 0 && !user.isAuthorized("essentials.eco.loan")) {
            throw new NoLoanPermittedException();
        }
        try {
            user.setMoney(balance);
        }
        catch (MaxMoneyException ex) {
            // empty catch block
        }
    }

    @Deprecated
    public static void add(String name, double amount) throws UserDoesNotExistException, NoLoanPermittedException {
        try {
            Economy.add(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to add " + amount + " to balance of " + name + ": " + e.getMessage(), e);
        }
    }

    public static void add(String name, BigDecimal amount) throws UserDoesNotExistException, NoLoanPermittedException, ArithmeticException {
        BigDecimal result = Economy.getMoneyExact(name).add(amount, MATH_CONTEXT);
        Economy.setMoney(name, result);
    }

    @Deprecated
    public static void subtract(String name, double amount) throws UserDoesNotExistException, NoLoanPermittedException {
        try {
            Economy.substract(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to substract " + amount + " of balance of " + name + ": " + e.getMessage(), e);
        }
    }

    public static void substract(String name, BigDecimal amount) throws UserDoesNotExistException, NoLoanPermittedException, ArithmeticException {
        BigDecimal result = Economy.getMoneyExact(name).subtract(amount, MATH_CONTEXT);
        Economy.setMoney(name, result);
    }

    @Deprecated
    public static void divide(String name, double amount) throws UserDoesNotExistException, NoLoanPermittedException {
        try {
            Economy.divide(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to divide balance of " + name + " by " + amount + ": " + e.getMessage(), e);
        }
    }

    public static void divide(String name, BigDecimal amount) throws UserDoesNotExistException, NoLoanPermittedException, ArithmeticException {
        BigDecimal result = Economy.getMoneyExact(name).divide(amount, MATH_CONTEXT);
        Economy.setMoney(name, result);
    }

    @Deprecated
    public static void multiply(String name, double amount) throws UserDoesNotExistException, NoLoanPermittedException {
        try {
            Economy.multiply(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to multiply balance of " + name + " by " + amount + ": " + e.getMessage(), e);
        }
    }

    public static void multiply(String name, BigDecimal amount) throws UserDoesNotExistException, NoLoanPermittedException, ArithmeticException {
        BigDecimal result = Economy.getMoneyExact(name).multiply(amount, MATH_CONTEXT);
        Economy.setMoney(name, result);
    }

    public static void resetBalance(String name) throws UserDoesNotExistException, NoLoanPermittedException {
        if (ess == null) {
            throw new RuntimeException("Essentials API is called before Essentials is loaded.");
        }
        Economy.setMoney(name, ess.getSettings().getStartingBalance());
    }

    @Deprecated
    public static boolean hasEnough(String name, double amount) throws UserDoesNotExistException {
        try {
            return Economy.hasEnough(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to compare balance of " + name + " with " + amount + ": " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean hasEnough(String name, BigDecimal amount) throws UserDoesNotExistException, ArithmeticException {
        return amount.compareTo(Economy.getMoneyExact(name)) <= 0;
    }

    @Deprecated
    public static boolean hasMore(String name, double amount) throws UserDoesNotExistException {
        try {
            return Economy.hasMore(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to compare balance of " + name + " with " + amount + ": " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean hasMore(String name, BigDecimal amount) throws UserDoesNotExistException, ArithmeticException {
        return amount.compareTo(Economy.getMoneyExact(name)) < 0;
    }

    @Deprecated
    public static boolean hasLess(String name, double amount) throws UserDoesNotExistException {
        try {
            return Economy.hasLess(name, BigDecimal.valueOf(amount));
        }
        catch (ArithmeticException e) {
            logger.log(Level.WARNING, "Failed to compare balance of " + name + " with " + amount + ": " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean hasLess(String name, BigDecimal amount) throws UserDoesNotExistException, ArithmeticException {
        return amount.compareTo(Economy.getMoneyExact(name)) > 0;
    }

    public static boolean isNegative(String name) throws UserDoesNotExistException {
        return Economy.getMoneyExact(name).signum() < 0;
    }

    @Deprecated
    public static String format(double amount) {
        try {
            return Economy.format(BigDecimal.valueOf(amount));
        }
        catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Failed to display " + amount + ": " + e.getMessage(), e);
            return "NaN";
        }
    }

    public static String format(BigDecimal amount) {
        if (ess == null) {
            throw new RuntimeException("Essentials API is called before Essentials is loaded.");
        }
        return NumberUtil.displayCurrency(amount, ess);
    }

    public static boolean playerExists(String name) {
        return Economy.getUserByName(name) != null;
    }

    public static boolean isNPC(String name) throws UserDoesNotExistException {
        User user = Economy.getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        return user.isNPC();
    }

    public static boolean createNPC(String name) {
        User user = Economy.getUserByName(name);
        if (user == null) {
            Economy.createNPCFile(name);
            return true;
        }
        return false;
    }

    public static void removeNPC(String name) throws UserDoesNotExistException {
        User user = Economy.getUserByName(name);
        if (user == null) {
            throw new UserDoesNotExistException(name);
        }
        Economy.deleteNPC(name);
    }

    static {
        MATH_CONTEXT = MathContext.DECIMAL128;
    }
}

