/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.MemoryConfiguration
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.configuration.file.YamlConfigurationOptions
 */
package fr.xephi.authme.settings;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.security.HashAlgorithm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public final class Settings
extends YamlConfiguration {
    public static String PLUGIN_FOLDER = "." + File.separator + "plugins" + File.separator + "AuthMe";
    public static final String CACHE_FOLDER = PLUGIN_FOLDER + File.separator + "cache";
    public static final String AUTH_FILE = PLUGIN_FOLDER + File.separator + "auths.db";
    public static final String MESSAGE_FILE = PLUGIN_FOLDER + File.separator + "messages";
    public static final String SETTINGS_FILE = PLUGIN_FOLDER + File.separator + "config.yml";
    public static List<String> allowCommands = null;
    public static List<String> getJoinPermissions = null;
    public static List<String> getUnrestrictedName = null;
    private static List<String> getRestrictedIp;
    public static List<String> getMySQLOtherUsernameColumn;
    public static List<String> getForcedWorlds;
    public static List<String> countries;
    public static List<String> countriesBlacklist;
    public static List<String> forceCommands;
    public static List<String> forceCommandsAsConsole;
    public static List<String> forceRegisterCommands;
    public static List<String> forceRegisterCommandsAsConsole;
    private AuthMe plugin;
    private final File file;
    public static DataSource.DataSourceType getDataSource;
    public static HashAlgorithm getPasswordHash;
    public static Boolean useLogging;
    public static int purgeDelay;
    public static List<String> welcomeMsg;
    public static List<String> unsafePasswords;
    public static List<String> emailBlacklist;
    public static List<String> emailWhitelist;
    public static Boolean isPermissionCheckEnabled;
    public static Boolean isRegistrationEnabled;
    public static Boolean isForcedRegistrationEnabled;
    public static Boolean isTeleportToSpawnEnabled;
    public static Boolean isSessionsEnabled;
    public static Boolean isChatAllowed;
    public static Boolean isAllowRestrictedIp;
    public static Boolean isMovementAllowed;
    public static Boolean isKickNonRegisteredEnabled;
    public static Boolean isForceSingleSessionEnabled;
    public static Boolean isForceSpawnLocOnJoinEnabled;
    public static Boolean isSaveQuitLocationEnabled;
    public static Boolean isForceSurvivalModeEnabled;
    public static Boolean isResetInventoryIfCreative;
    public static Boolean isCachingEnabled;
    public static Boolean isKickOnWrongPasswordEnabled;
    public static Boolean getEnablePasswordVerifier;
    public static Boolean protectInventoryBeforeLogInEnabled;
    public static Boolean isBackupActivated;
    public static Boolean isBackupOnStart;
    public static Boolean isBackupOnStop;
    public static Boolean enablePasspartu;
    public static Boolean isStopEnabled;
    public static Boolean reloadSupport;
    public static Boolean rakamakUseIp;
    public static Boolean noConsoleSpam;
    public static Boolean removePassword;
    public static Boolean displayOtherAccounts;
    public static Boolean useCaptcha;
    public static Boolean emailRegistration;
    public static Boolean multiverse;
    public static Boolean notifications;
    public static Boolean chestshop;
    public static Boolean bungee;
    public static Boolean banUnsafeIp;
    public static Boolean doubleEmailCheck;
    public static Boolean sessionExpireOnIpChange;
    public static Boolean disableSocialSpy;
    public static Boolean forceOnlyAfterLogin;
    public static Boolean useEssentialsMotd;
    public static Boolean usePurge;
    public static Boolean purgePlayerDat;
    public static Boolean purgeEssentialsFile;
    public static Boolean supportOldPassword;
    public static Boolean purgeLimitedCreative;
    public static Boolean purgeAntiXray;
    public static Boolean purgePermissions;
    public static Boolean enableProtection;
    public static Boolean enableAntiBot;
    public static Boolean recallEmail;
    public static Boolean useWelcomeMessage;
    public static Boolean broadcastWelcomeMessage;
    public static Boolean forceRegKick;
    public static Boolean forceRegLogin;
    public static Boolean checkVeryGames;
    public static Boolean delayJoinMessage;
    public static Boolean noTeleport;
    public static Boolean applyBlindEffect;
    public static String getNickRegex;
    public static String getUnloggedinGroup;
    public static String getMySQLHost;
    public static String getMySQLPort;
    public static String getMySQLUsername;
    public static String getMySQLPassword;
    public static String getMySQLDatabase;
    public static String getMySQLTablename;
    public static String getMySQLColumnName;
    public static String getMySQLColumnPassword;
    public static String getMySQLColumnIp;
    public static String getMySQLColumnLastLogin;
    public static String getMySQLColumnSalt;
    public static String getMySQLColumnGroup;
    public static String getMySQLColumnEmail;
    public static String unRegisteredGroup;
    public static String backupWindowsPath;
    public static String getcUnrestrictedName;
    public static String getRegisteredGroup;
    public static String messagesLanguage;
    public static String getMySQLlastlocX;
    public static String getMySQLlastlocY;
    public static String getMySQLlastlocZ;
    public static String rakamakUsers;
    public static String rakamakUsersIp;
    public static String getmailAccount;
    public static String getmailPassword;
    public static String getmailSMTP;
    public static String getMySQLColumnId;
    public static String getmailSenderName;
    public static String getMailSubject;
    public static String getMailText;
    public static String getMySQLlastlocWorld;
    public static String defaultWorld;
    public static String getPhpbbPrefix;
    public static String getWordPressPrefix;
    public static String getMySQLColumnLogged;
    public static String spawnPriority;
    public static String crazyloginFileName;
    public static String getPassRegex;
    public static int getWarnMessageInterval;
    public static int getSessionTimeout;
    public static int getRegistrationTimeout;
    public static int getMaxNickLength;
    public static int getMinNickLength;
    public static int getPasswordMinLen;
    public static int getMovementRadius;
    public static int getmaxRegPerIp;
    public static int getNonActivatedGroup;
    public static int passwordMaxLength;
    public static int getRecoveryPassLength;
    public static int getMailPort;
    public static int maxLoginTry;
    public static int captchaLength;
    public static int saltLength;
    public static int getmaxRegPerEmail;
    public static int bCryptLog2Rounds;
    public static int getPhpbbGroup;
    public static int antiBotSensibility;
    public static int antiBotDuration;
    public static int delayRecall;
    public static int getMaxLoginPerIp;
    public static int getMaxJoinPerIp;
    protected static YamlConfiguration configFile;

    public Settings(AuthMe plugin) {
        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.plugin = plugin;
        if (this.exists()) {
            this.load();
        } else {
            this.loadDefaults(this.file.getName());
            this.load();
        }
        configFile = (YamlConfiguration)plugin.getConfig();
        PLUGIN_FOLDER = plugin.getDataFolder().toString();
    }

    public void loadConfigOptions() {
        this.plugin.getLogger().info("Loading Configuration File...");
        this.mergeConfig();
        messagesLanguage = Settings.checkLang(configFile.getString("settings.messagesLanguage", "en"));
        isPermissionCheckEnabled = configFile.getBoolean("permission.EnablePermissionCheck", false);
        isForcedRegistrationEnabled = configFile.getBoolean("settings.registration.force", true);
        isRegistrationEnabled = configFile.getBoolean("settings.registration.enabled", true);
        isTeleportToSpawnEnabled = configFile.getBoolean("settings.restrictions.teleportUnAuthedToSpawn", false);
        getWarnMessageInterval = configFile.getInt("settings.registration.messageInterval", 5);
        isSessionsEnabled = configFile.getBoolean("settings.sessions.enabled", false);
        getSessionTimeout = configFile.getInt("settings.sessions.timeout", 10);
        getRegistrationTimeout = configFile.getInt("settings.restrictions.timeout", 30);
        isChatAllowed = configFile.getBoolean("settings.restrictions.allowChat", false);
        getMaxNickLength = configFile.getInt("settings.restrictions.maxNicknameLength", 20);
        getMinNickLength = configFile.getInt("settings.restrictions.minNicknameLength", 3);
        getPasswordMinLen = configFile.getInt("settings.security.minPasswordLength", 4);
        getNickRegex = configFile.getString("settings.restrictions.allowedNicknameCharacters", "[a-zA-Z0-9_?]*");
        isAllowRestrictedIp = configFile.getBoolean("settings.restrictions.AllowRestrictedUser", false);
        getRestrictedIp = configFile.getStringList("settings.restrictions.AllowedRestrictedUser");
        isMovementAllowed = configFile.getBoolean("settings.restrictions.allowMovement", false);
        getMovementRadius = configFile.getInt("settings.restrictions.allowedMovementRadius", 100);
        getJoinPermissions = configFile.getStringList("GroupOptions.Permissions.PermissionsOnJoin");
        isKickOnWrongPasswordEnabled = configFile.getBoolean("settings.restrictions.kickOnWrongPassword", false);
        isKickNonRegisteredEnabled = configFile.getBoolean("settings.restrictions.kickNonRegistered", false);
        isForceSingleSessionEnabled = configFile.getBoolean("settings.restrictions.ForceSingleSession", true);
        isForceSpawnLocOnJoinEnabled = configFile.getBoolean("settings.restrictions.ForceSpawnLocOnJoinEnabled", false);
        isSaveQuitLocationEnabled = configFile.getBoolean("settings.restrictions.SaveQuitLocation", false);
        isForceSurvivalModeEnabled = configFile.getBoolean("settings.GameMode.ForceSurvivalMode", false);
        isResetInventoryIfCreative = configFile.getBoolean("settings.GameMode.ResetInventoryIfCreative", false);
        getmaxRegPerIp = configFile.getInt("settings.restrictions.maxRegPerIp", 1);
        getPasswordHash = Settings.getPasswordHash();
        getUnloggedinGroup = configFile.getString("settings.security.unLoggedinGroup", "unLoggedInGroup");
        getDataSource = Settings.getDataSource();
        isCachingEnabled = configFile.getBoolean("DataSource.caching", true);
        getMySQLHost = configFile.getString("DataSource.mySQLHost", "127.0.0.1");
        getMySQLPort = configFile.getString("DataSource.mySQLPort", "3306");
        getMySQLUsername = configFile.getString("DataSource.mySQLUsername", "authme");
        getMySQLPassword = configFile.getString("DataSource.mySQLPassword", "12345");
        getMySQLDatabase = configFile.getString("DataSource.mySQLDatabase", "authme");
        getMySQLTablename = configFile.getString("DataSource.mySQLTablename", "authme");
        getMySQLColumnEmail = configFile.getString("DataSource.mySQLColumnEmail", "email");
        getMySQLColumnName = configFile.getString("DataSource.mySQLColumnName", "username");
        getMySQLColumnPassword = configFile.getString("DataSource.mySQLColumnPassword", "password");
        getMySQLColumnIp = configFile.getString("DataSource.mySQLColumnIp", "ip");
        getMySQLColumnLastLogin = configFile.getString("DataSource.mySQLColumnLastLogin", "lastlogin");
        getMySQLColumnSalt = configFile.getString("ExternalBoardOptions.mySQLColumnSalt");
        getMySQLColumnGroup = configFile.getString("ExternalBoardOptions.mySQLColumnGroup", "");
        getMySQLlastlocX = configFile.getString("DataSource.mySQLlastlocX", "x");
        getMySQLlastlocY = configFile.getString("DataSource.mySQLlastlocY", "y");
        getMySQLlastlocZ = configFile.getString("DataSource.mySQLlastlocZ", "z");
        getMySQLlastlocWorld = configFile.getString("DataSource.mySQLlastlocWorld", "world");
        getNonActivatedGroup = configFile.getInt("ExternalBoardOptions.nonActivedUserGroup", -1);
        unRegisteredGroup = configFile.getString("GroupOptions.UnregisteredPlayerGroup", "");
        getUnrestrictedName = configFile.getStringList("settings.unrestrictions.UnrestrictedName");
        getRegisteredGroup = configFile.getString("GroupOptions.RegisteredPlayerGroup", "");
        getEnablePasswordVerifier = configFile.getBoolean("settings.restrictions.enablePasswordVerifier", true);
        protectInventoryBeforeLogInEnabled = configFile.getBoolean("settings.restrictions.ProtectInventoryBeforeLogIn", true);
        passwordMaxLength = configFile.getInt("settings.security.passwordMaxLength", 20);
        isBackupActivated = configFile.getBoolean("BackupSystem.ActivateBackup", false);
        isBackupOnStart = configFile.getBoolean("BackupSystem.OnServerStart", false);
        isBackupOnStop = configFile.getBoolean("BackupSystem.OnServeStop", false);
        backupWindowsPath = configFile.getString("BackupSystem.MysqlWindowsPath", "C:\\Program Files\\MySQL\\MySQL Server 5.1\\");
        enablePasspartu = configFile.getBoolean("Passpartu.enablePasspartu", false);
        isStopEnabled = configFile.getBoolean("Security.SQLProblem.stopServer", true);
        reloadSupport = configFile.getBoolean("Security.ReloadCommand.useReloadCommandSupport", true);
        allowCommands = configFile.getList("settings.restrictions.allowCommands");
        if (configFile.contains("allowCommands")) {
            if (!allowCommands.contains("/login")) {
                allowCommands.add("/login");
            }
            if (!allowCommands.contains("/register")) {
                allowCommands.add("/register");
            }
            if (!allowCommands.contains("/l")) {
                allowCommands.add("/l");
            }
            if (!allowCommands.contains("/reg")) {
                allowCommands.add("/reg");
            }
            if (!allowCommands.contains("/passpartu")) {
                allowCommands.add("/passpartu");
            }
            if (!allowCommands.contains("/email")) {
                allowCommands.add("/email");
            }
            if (!allowCommands.contains("/captcha")) {
                allowCommands.add("/captcha");
            }
        }
        rakamakUsers = configFile.getString("Converter.Rakamak.fileName", "users.rak");
        rakamakUsersIp = configFile.getString("Converter.Rakamak.ipFileName", "UsersIp.rak");
        rakamakUseIp = configFile.getBoolean("Converter.Rakamak.useIp", false);
        noConsoleSpam = configFile.getBoolean("Security.console.noConsoleSpam", false);
        removePassword = configFile.getBoolean("Security.console.removePassword", true);
        getmailAccount = configFile.getString("Email.mailAccount", "");
        getmailPassword = configFile.getString("Email.mailPassword", "");
        getmailSMTP = configFile.getString("Email.mailSMTP", "smtp.gmail.com");
        getMailPort = configFile.getInt("Email.mailPort", 465);
        getRecoveryPassLength = configFile.getInt("Email.RecoveryPasswordLength", 8);
        getMySQLOtherUsernameColumn = configFile.getList("ExternalBoardOptions.mySQLOtherUsernameColumns", new ArrayList());
        displayOtherAccounts = configFile.getBoolean("settings.restrictions.displayOtherAccounts", true);
        getMySQLColumnId = configFile.getString("DataSource.mySQLColumnId", "id");
        getmailSenderName = configFile.getString("Email.mailSenderName", "");
        useCaptcha = configFile.getBoolean("Security.captcha.useCaptcha", false);
        maxLoginTry = configFile.getInt("Security.captcha.maxLoginTry", 5);
        captchaLength = configFile.getInt("Security.captcha.captchaLength", 5);
        getMailSubject = configFile.getString("Email.mailSubject", "Your new AuthMe Password");
        getMailText = configFile.getString("Email.mailText", "Dear <playername>, <br /><br /> This is your new AuthMe password for the server <br /><br /> <servername> : <br /><br /> <generatedpass><br /><br />Do not forget to change password after login! <br /> /changepassword <generatedpass> newPassword");
        emailRegistration = configFile.getBoolean("settings.registration.enableEmailRegistrationSystem", false);
        saltLength = configFile.getInt("settings.security.doubleMD5SaltLength", 8);
        getmaxRegPerEmail = configFile.getInt("Email.maxRegPerEmail", 1);
        multiverse = configFile.getBoolean("Hooks.multiverse", true);
        chestshop = configFile.getBoolean("Hooks.chestshop", true);
        notifications = configFile.getBoolean("Hooks.notifications", true);
        bungee = configFile.getBoolean("Hooks.bungeecord", false);
        getForcedWorlds = configFile.getList("settings.restrictions.ForceSpawnOnTheseWorlds", new ArrayList());
        banUnsafeIp = configFile.getBoolean("settings.restrictions.banUnsafedIP", false);
        doubleEmailCheck = configFile.getBoolean("settings.registration.doubleEmailCheck", false);
        sessionExpireOnIpChange = configFile.getBoolean("settings.sessions.sessionExpireOnIpChange", false);
        useLogging = configFile.getBoolean("Security.console.logConsole", false);
        disableSocialSpy = configFile.getBoolean("Hooks.disableSocialSpy", true);
        bCryptLog2Rounds = configFile.getInt("ExternalBoardOptions.bCryptLog2Round", 10);
        forceOnlyAfterLogin = configFile.getBoolean("settings.GameMode.ForceOnlyAfterLogin", false);
        useEssentialsMotd = configFile.getBoolean("Hooks.useEssentialsMotd", false);
        usePurge = configFile.getBoolean("Purge.useAutoPurge", false);
        purgeDelay = configFile.getInt("Purge.daysBeforeRemovePlayer", 60);
        purgePlayerDat = configFile.getBoolean("Purge.removePlayerDat", false);
        purgeEssentialsFile = configFile.getBoolean("Purge.removeEssentialsFile", false);
        defaultWorld = configFile.getString("Purge.defaultWorld", "world");
        getPhpbbPrefix = configFile.getString("ExternalBoardOptions.phpbbTablePrefix", "phpbb_");
        getPhpbbGroup = configFile.getInt("ExternalBoardOptions.phpbbActivatedGroupId", 2);
        supportOldPassword = configFile.getBoolean("settings.security.supportOldPasswordHash", false);
        getWordPressPrefix = configFile.getString("ExternalBoardOptions.wordpressTablePrefix", "wp_");
        purgeLimitedCreative = configFile.getBoolean("Purge.removeLimitedCreativesInventories", false);
        purgeAntiXray = configFile.getBoolean("Purge.removeAntiXRayFile", false);
        enableProtection = configFile.getBoolean("Protection.enableProtection", false);
        countries = configFile.getList("Protection.countries", new ArrayList());
        enableAntiBot = configFile.getBoolean("Protection.enableAntiBot", false);
        antiBotSensibility = configFile.getInt("Protection.antiBotSensibility", 5);
        antiBotDuration = configFile.getInt("Protection.antiBotDuration", 10);
        forceCommands = configFile.getList("settings.forceCommands", new ArrayList());
        forceCommandsAsConsole = configFile.getList("settings.forceCommandsAsConsole", new ArrayList());
        recallEmail = configFile.getBoolean("Email.recallPlayers", false);
        delayRecall = configFile.getInt("Email.delayRecall", 5);
        useWelcomeMessage = configFile.getBoolean("settings.useWelcomeMessage", true);
        unsafePasswords = configFile.getList("settings.security.unsafePasswords", new ArrayList());
        countriesBlacklist = configFile.getList("Protection.countriesBlacklist", new ArrayList());
        broadcastWelcomeMessage = configFile.getBoolean("settings.broadcastWelcomeMessage", false);
        forceRegKick = configFile.getBoolean("settings.registration.forceKickAfterRegister", false);
        forceRegLogin = configFile.getBoolean("settings.registration.forceLoginAfterRegister", false);
        getMySQLColumnLogged = configFile.getString("DataSource.mySQLColumnLogged", "isLogged");
        spawnPriority = configFile.getString("settings.restrictions.spawnPriority", "authme,essentials,multiverse,default");
        getMaxLoginPerIp = configFile.getInt("settings.restrictions.maxLoginPerIp", 0);
        getMaxJoinPerIp = configFile.getInt("settings.restrictions.maxJoinPerIp", 0);
        checkVeryGames = configFile.getBoolean("VeryGames.enableIpCheck", false);
        delayJoinMessage = configFile.getBoolean("settings.delayJoinMessage", false);
        noTeleport = configFile.getBoolean("settings.restrictions.noTeleport", false);
        crazyloginFileName = configFile.getString("Converter.CrazyLogin.fileName", "accounts.db");
        getPassRegex = configFile.getString("settings.restrictions.allowedPasswordCharacters", "[a-zA-Z0-9_?!@+&-]*");
        applyBlindEffect = configFile.getBoolean("settings.applyBlindEffect", false);
        emailBlacklist = configFile.getStringList("Email.emailBlacklisted");
        emailWhitelist = configFile.getStringList("Email.emailWhitelisted");
        forceRegisterCommands = configFile.getList("settings.forceRegisterCommands", new ArrayList());
        forceRegisterCommandsAsConsole = configFile.getList("settings.forceRegisterCommandsAsConsole", new ArrayList());
        Settings.getWelcomeMessage(this.plugin);
        this.saveDefaults();
    }

    public static void reloadConfigOptions(YamlConfiguration newConfig) {
        configFile = newConfig;
        messagesLanguage = Settings.checkLang(configFile.getString("settings.messagesLanguage", "en"));
        isPermissionCheckEnabled = configFile.getBoolean("permission.EnablePermissionCheck", false);
        isForcedRegistrationEnabled = configFile.getBoolean("settings.registration.force", true);
        isRegistrationEnabled = configFile.getBoolean("settings.registration.enabled", true);
        isTeleportToSpawnEnabled = configFile.getBoolean("settings.restrictions.teleportUnAuthedToSpawn", false);
        getWarnMessageInterval = configFile.getInt("settings.registration.messageInterval", 5);
        isSessionsEnabled = configFile.getBoolean("settings.sessions.enabled", false);
        getSessionTimeout = configFile.getInt("settings.sessions.timeout", 10);
        getRegistrationTimeout = configFile.getInt("settings.restrictions.timeout", 30);
        isChatAllowed = configFile.getBoolean("settings.restrictions.allowChat", false);
        getMaxNickLength = configFile.getInt("settings.restrictions.maxNicknameLength", 20);
        getMinNickLength = configFile.getInt("settings.restrictions.minNicknameLength", 3);
        getPasswordMinLen = configFile.getInt("settings.security.minPasswordLength", 4);
        getNickRegex = configFile.getString("settings.restrictions.allowedNicknameCharacters", "[a-zA-Z0-9_?]*");
        isAllowRestrictedIp = configFile.getBoolean("settings.restrictions.AllowRestrictedUser", false);
        getRestrictedIp = configFile.getStringList("settings.restrictions.AllowedRestrictedUser");
        isMovementAllowed = configFile.getBoolean("settings.restrictions.allowMovement", false);
        getMovementRadius = configFile.getInt("settings.restrictions.allowedMovementRadius", 100);
        getJoinPermissions = configFile.getStringList("GroupOptions.Permissions.PermissionsOnJoin");
        isKickOnWrongPasswordEnabled = configFile.getBoolean("settings.restrictions.kickOnWrongPassword", false);
        isKickNonRegisteredEnabled = configFile.getBoolean("settings.restrictions.kickNonRegistered", false);
        isForceSingleSessionEnabled = configFile.getBoolean("settings.restrictions.ForceSingleSession", true);
        isForceSpawnLocOnJoinEnabled = configFile.getBoolean("settings.restrictions.ForceSpawnLocOnJoinEnabled", false);
        isSaveQuitLocationEnabled = configFile.getBoolean("settings.restrictions.SaveQuitLocation", false);
        isForceSurvivalModeEnabled = configFile.getBoolean("settings.GameMode.ForceSurvivalMode", false);
        isResetInventoryIfCreative = configFile.getBoolean("settings.GameMode.ResetInventoryIfCreative", false);
        getmaxRegPerIp = configFile.getInt("settings.restrictions.maxRegPerIp", 1);
        getPasswordHash = Settings.getPasswordHash();
        getUnloggedinGroup = configFile.getString("settings.security.unLoggedinGroup", "unLoggedInGroup");
        getDataSource = Settings.getDataSource();
        isCachingEnabled = configFile.getBoolean("DataSource.caching", true);
        getMySQLHost = configFile.getString("DataSource.mySQLHost", "127.0.0.1");
        getMySQLPort = configFile.getString("DataSource.mySQLPort", "3306");
        getMySQLUsername = configFile.getString("DataSource.mySQLUsername", "authme");
        getMySQLPassword = configFile.getString("DataSource.mySQLPassword", "12345");
        getMySQLDatabase = configFile.getString("DataSource.mySQLDatabase", "authme");
        getMySQLTablename = configFile.getString("DataSource.mySQLTablename", "authme");
        getMySQLColumnEmail = configFile.getString("DataSource.mySQLColumnEmail", "email");
        getMySQLColumnName = configFile.getString("DataSource.mySQLColumnName", "username");
        getMySQLColumnPassword = configFile.getString("DataSource.mySQLColumnPassword", "password");
        getMySQLColumnIp = configFile.getString("DataSource.mySQLColumnIp", "ip");
        getMySQLColumnLastLogin = configFile.getString("DataSource.mySQLColumnLastLogin", "lastlogin");
        getMySQLlastlocX = configFile.getString("DataSource.mySQLlastlocX", "x");
        getMySQLlastlocY = configFile.getString("DataSource.mySQLlastlocY", "y");
        getMySQLlastlocZ = configFile.getString("DataSource.mySQLlastlocZ", "z");
        getMySQLlastlocWorld = configFile.getString("DataSource.mySQLlastlocWorld", "world");
        getMySQLColumnSalt = configFile.getString("ExternalBoardOptions.mySQLColumnSalt", "");
        getMySQLColumnGroup = configFile.getString("ExternalBoardOptions.mySQLColumnGroup", "");
        getNonActivatedGroup = configFile.getInt("ExternalBoardOptions.nonActivedUserGroup", -1);
        unRegisteredGroup = configFile.getString("GroupOptions.UnregisteredPlayerGroup", "");
        getUnrestrictedName = configFile.getStringList("settings.unrestrictions.UnrestrictedName");
        getRegisteredGroup = configFile.getString("GroupOptions.RegisteredPlayerGroup", "");
        getEnablePasswordVerifier = configFile.getBoolean("settings.restrictions.enablePasswordVerifier", true);
        protectInventoryBeforeLogInEnabled = configFile.getBoolean("settings.restrictions.ProtectInventoryBeforeLogIn", true);
        passwordMaxLength = configFile.getInt("settings.security.passwordMaxLength", 20);
        isBackupActivated = configFile.getBoolean("BackupSystem.ActivateBackup", false);
        isBackupOnStart = configFile.getBoolean("BackupSystem.OnServerStart", false);
        isBackupOnStop = configFile.getBoolean("BackupSystem.OnServeStop", false);
        backupWindowsPath = configFile.getString("BackupSystem.MysqlWindowsPath", "C:\\Program Files\\MySQL\\MySQL Server 5.1\\");
        enablePasspartu = configFile.getBoolean("Passpartu.enablePasspartu", false);
        isStopEnabled = configFile.getBoolean("Security.SQLProblem.stopServer", true);
        reloadSupport = configFile.getBoolean("Security.ReloadCommand.useReloadCommandSupport", true);
        allowCommands = configFile.getList("settings.restrictions.allowCommands");
        if (configFile.contains("allowCommands")) {
            if (!allowCommands.contains("/login")) {
                allowCommands.add("/login");
            }
            if (!allowCommands.contains("/register")) {
                allowCommands.add("/register");
            }
            if (!allowCommands.contains("/l")) {
                allowCommands.add("/l");
            }
            if (!allowCommands.contains("/reg")) {
                allowCommands.add("/reg");
            }
            if (!allowCommands.contains("/passpartu")) {
                allowCommands.add("/passpartu");
            }
            if (!allowCommands.contains("/email")) {
                allowCommands.add("/email");
            }
            if (!allowCommands.contains("/captcha")) {
                allowCommands.add("/captcha");
            }
        }
        rakamakUsers = configFile.getString("Converter.Rakamak.fileName", "users.rak");
        rakamakUsersIp = configFile.getString("Converter.Rakamak.ipFileName", "UsersIp.rak");
        rakamakUseIp = configFile.getBoolean("Converter.Rakamak.useIp", false);
        noConsoleSpam = configFile.getBoolean("Security.console.noConsoleSpam", false);
        removePassword = configFile.getBoolean("Security.console.removePassword", true);
        getmailAccount = configFile.getString("Email.mailAccount", "");
        getmailPassword = configFile.getString("Email.mailPassword", "");
        getmailSMTP = configFile.getString("Email.mailSMTP", "smtp.gmail.com");
        getMailPort = configFile.getInt("Email.mailPort", 465);
        getRecoveryPassLength = configFile.getInt("Email.RecoveryPasswordLength", 8);
        getMySQLOtherUsernameColumn = configFile.getList("ExternalBoardOptions.mySQLOtherUsernameColumns", new ArrayList());
        displayOtherAccounts = configFile.getBoolean("settings.restrictions.displayOtherAccounts", true);
        getMySQLColumnId = configFile.getString("DataSource.mySQLColumnId", "id");
        getmailSenderName = configFile.getString("Email.mailSenderName", "");
        useCaptcha = configFile.getBoolean("Security.captcha.useCaptcha", false);
        maxLoginTry = configFile.getInt("Security.captcha.maxLoginTry", 5);
        captchaLength = configFile.getInt("Security.captcha.captchaLength", 5);
        getMailSubject = configFile.getString("Email.mailSubject", "Your new AuthMe Password");
        getMailText = configFile.getString("Email.mailText", "Dear <playername>, <br /><br /> This is your new AuthMe password for the server <br /><br /> <servername> : <br /><br /> <generatedpass><br /><br />Do not forget to change password after login! <br /> /changepassword <generatedpass> newPassword");
        emailRegistration = configFile.getBoolean("settings.registration.enableEmailRegistrationSystem", false);
        saltLength = configFile.getInt("settings.security.doubleMD5SaltLength", 8);
        getmaxRegPerEmail = configFile.getInt("Email.maxRegPerEmail", 1);
        multiverse = configFile.getBoolean("Hooks.multiverse", true);
        chestshop = configFile.getBoolean("Hooks.chestshop", true);
        notifications = configFile.getBoolean("Hooks.notifications", true);
        bungee = configFile.getBoolean("Hooks.bungeecord", false);
        getForcedWorlds = configFile.getList("settings.restrictions.ForceSpawnOnTheseWorlds");
        banUnsafeIp = configFile.getBoolean("settings.restrictions.banUnsafedIP", false);
        doubleEmailCheck = configFile.getBoolean("settings.registration.doubleEmailCheck", false);
        sessionExpireOnIpChange = configFile.getBoolean("settings.sessions.sessionExpireOnIpChange", false);
        useLogging = configFile.getBoolean("Security.console.logConsole", false);
        disableSocialSpy = configFile.getBoolean("Hooks.disableSocialSpy", true);
        bCryptLog2Rounds = configFile.getInt("ExternalBoardOptions.bCryptLog2Round", 10);
        forceOnlyAfterLogin = configFile.getBoolean("settings.GameMode.ForceOnlyAfterLogin", false);
        useEssentialsMotd = configFile.getBoolean("Hooks.useEssentialsMotd", false);
        usePurge = configFile.getBoolean("Purge.useAutoPurge", false);
        purgeDelay = configFile.getInt("Purge.daysBeforeRemovePlayer", 60);
        purgePlayerDat = configFile.getBoolean("Purge.removePlayerDat", false);
        purgeEssentialsFile = configFile.getBoolean("Purge.removeEssentialsFile", false);
        defaultWorld = configFile.getString("Purge.defaultWorld", "world");
        getPhpbbPrefix = configFile.getString("ExternalBoardOptions.phpbbTablePrefix", "phpbb_");
        getPhpbbGroup = configFile.getInt("ExternalBoardOptions.phpbbActivatedGroupId", 2);
        supportOldPassword = configFile.getBoolean("settings.security.supportOldPasswordHash", false);
        getWordPressPrefix = configFile.getString("ExternalBoardOptions.wordpressTablePrefix", "wp_");
        purgeLimitedCreative = configFile.getBoolean("Purge.removeLimitedCreativesInventories", false);
        purgeAntiXray = configFile.getBoolean("Purge.removeAntiXRayFile", false);
        enableProtection = configFile.getBoolean("Protection.enableProtection", false);
        countries = configFile.getList("Protection.countries");
        enableAntiBot = configFile.getBoolean("Protection.enableAntiBot", false);
        antiBotSensibility = configFile.getInt("Protection.antiBotSensibility", 5);
        antiBotDuration = configFile.getInt("Protection.antiBotDuration", 10);
        forceCommands = configFile.getList("settings.forceCommands", new ArrayList());
        forceCommandsAsConsole = configFile.getList("settings.forceCommandsAsConsole", new ArrayList());
        recallEmail = configFile.getBoolean("Email.recallPlayers", false);
        delayRecall = configFile.getInt("Email.delayRecall", 5);
        useWelcomeMessage = configFile.getBoolean("settings.useWelcomeMessage", true);
        unsafePasswords = configFile.getList("settings.security.unsafePasswords", new ArrayList());
        countriesBlacklist = configFile.getList("Protection.countriesBlacklist", new ArrayList());
        broadcastWelcomeMessage = configFile.getBoolean("settings.broadcastWelcomeMessage", false);
        forceRegKick = configFile.getBoolean("settings.registration.forceKickAfterRegister", false);
        forceRegLogin = configFile.getBoolean("settings.registration.forceLoginAfterRegister", false);
        getMySQLColumnLogged = configFile.getString("DataSource.mySQLColumnLogged", "isLogged");
        spawnPriority = configFile.getString("settings.restrictions.spawnPriority", "authme,essentials,multiverse,default");
        getMaxLoginPerIp = configFile.getInt("settings.restrictions.maxLoginPerIp", 0);
        getMaxJoinPerIp = configFile.getInt("settings.restrictions.maxJoinPerIp", 0);
        checkVeryGames = configFile.getBoolean("VeryGames.enableIpCheck", false);
        delayJoinMessage = configFile.getBoolean("settings.delayJoinMessage", false);
        noTeleport = configFile.getBoolean("settings.restrictions.noTeleport", false);
        crazyloginFileName = configFile.getString("Converter.CrazyLogin.fileName", "accounts.db");
        getPassRegex = configFile.getString("settings.restrictions.allowedPasswordCharacters", "[a-zA-Z0-9_?!@+&-]*");
        applyBlindEffect = configFile.getBoolean("settings.applyBlindEffect", false);
        emailBlacklist = configFile.getStringList("Email.emailBlacklisted");
        emailWhitelist = configFile.getStringList("Email.emailWhitelisted");
        forceRegisterCommands = configFile.getList("settings.forceRegisterCommands", new ArrayList());
        forceRegisterCommandsAsConsole = configFile.getList("settings.forceRegisterCommandsAsConsole", new ArrayList());
        Settings.getWelcomeMessage(AuthMe.getInstance());
    }

    public void mergeConfig() {
        boolean changes = false;
        if (this.contains("Xenoforo.predefinedSalt")) {
            this.set("Xenoforo.predefinedSalt", (Object)null);
        }
        if (configFile.getString("settings.security.passwordHash", "SHA256").toUpperCase().equals("XFSHA1") || configFile.getString("settings.security.passwordHash", "SHA256").toUpperCase().equals("XFSHA256")) {
            this.set("settings.security.passwordHash", (Object)"XENFORO");
        }
        if (!this.contains("Protection.enableProtection")) {
            this.set("Protection.enableProtection", (Object)false);
            changes = true;
        }
        if (!this.contains("Protection.countries")) {
            countries = new ArrayList<String>();
            countries.add("US");
            countries.add("GB");
            this.set("Protection.countries", countries);
            changes = true;
        }
        if (!this.contains("Protection.enableAntiBot")) {
            this.set("Protection.enableAntiBot", (Object)false);
            changes = true;
        }
        if (!this.contains("Protection.antiBotSensibility")) {
            this.set("Protection.antiBotSensibility", (Object)5);
            changes = true;
        }
        if (!this.contains("Protection.antiBotDuration")) {
            this.set("Protection.antiBotDuration", (Object)10);
            changes = true;
        }
        if (!this.contains("settings.forceCommands")) {
            this.set("settings.forceCommands", new ArrayList());
            changes = true;
        }
        if (!this.contains("settings.forceCommandsAsConsole")) {
            this.set("settings.forceCommandsAsConsole", new ArrayList());
            changes = true;
        }
        if (!this.contains("Email.recallPlayers")) {
            this.set("Email.recallPlayers", (Object)false);
            changes = true;
        }
        if (!this.contains("Email.delayRecall")) {
            this.set("Email.delayRecall", (Object)5);
            changes = true;
        }
        if (!this.contains("settings.useWelcomeMessage")) {
            this.set("settings.useWelcomeMessage", (Object)true);
            changes = true;
        }
        if (!this.contains("settings.security.unsafePasswords")) {
            ArrayList<String> str = new ArrayList<String>();
            str.add("123456");
            str.add("password");
            this.set("settings.security.unsafePasswords", str);
            changes = true;
        }
        if (!this.contains("Protection.countriesBlacklist")) {
            countriesBlacklist = new ArrayList<String>();
            countriesBlacklist.add("A1");
            this.set("Protection.countriesBlacklist", countriesBlacklist);
            changes = true;
        }
        if (!this.contains("settings.broadcastWelcomeMessage")) {
            this.set("settings.broadcastWelcomeMessage", (Object)false);
            changes = true;
        }
        if (!this.contains("settings.registration.forceKickAfterRegister")) {
            this.set("settings.registration.forceKickAfterRegister", (Object)false);
            changes = true;
        }
        if (!this.contains("settings.registration.forceLoginAfterRegister")) {
            this.set("settings.registration.forceLoginAfterRegister", (Object)false);
            changes = true;
        }
        if (!this.contains("DataSource.mySQLColumnLogged")) {
            this.set("DataSource.mySQLColumnLogged", (Object)"isLogged");
            changes = true;
        }
        if (!this.contains("settings.restrictions.spawnPriority")) {
            this.set("settings.restrictions.spawnPriority", (Object)"authme,essentials,multiverse,default");
            changes = true;
        }
        if (!this.contains("settings.restrictions.maxLoginPerIp")) {
            this.set("settings.restrictions.maxLoginPerIp", (Object)0);
            changes = true;
        }
        if (!this.contains("settings.restrictions.maxJoinPerIp")) {
            this.set("settings.restrictions.maxJoinPerIp", (Object)0);
            changes = true;
        }
        if (!this.contains("VeryGames.enableIpCheck")) {
            this.set("VeryGames.enableIpCheck", (Object)false);
            changes = true;
        }
        if (this.getString("settings.restrictions.allowedNicknameCharacters").equals("[a-zA-Z0-9_?]*")) {
            this.set("settings.restrictions.allowedNicknameCharacters", (Object)"[a-zA-Z0-9_]*");
        }
        if (!this.contains("settings.delayJoinMessage")) {
            this.set("settings.delayJoinMessage", (Object)false);
            changes = true;
        }
        if (!this.contains("settings.restrictions.noTeleport")) {
            this.set("settings.restrictions.noTeleport", (Object)false);
            changes = true;
        }
        if (this.contains("Converter.Rakamak.newPasswordHash")) {
            this.set("Converter.Rakamak.newPasswordHash", (Object)null);
        }
        if (!this.contains("Converter.CrazyLogin.fileName")) {
            this.set("Converter.CrazyLogin.fileName", (Object)"accounts.db");
            changes = true;
        }
        if (!this.contains("settings.restrictions.allowedPasswordCharacters")) {
            this.set("settings.restrictions.allowedPasswordCharacters", (Object)"[a-zA-Z0-9_?!@+&-]*");
            changes = true;
        }
        if (!this.contains("settings.applyBlindEffect")) {
            this.set("settings.applyBlindEffect", (Object)false);
            changes = true;
        }
        if (!this.contains("Email.emailBlacklisted")) {
            this.set("Email.emailBlacklisted", new ArrayList());
            changes = true;
        }
        if (this.contains("Performances.useMultiThreading")) {
            this.set("Performances.useMultiThreading", (Object)null);
        }
        if (!this.contains("Email.emailWhitelisted")) {
            this.set("Email.emailWhitelisted", new ArrayList());
            changes = true;
        }
        if (!this.contains("settings.forceRegisterCommands")) {
            this.set("settings.forceRegisterCommands", new ArrayList());
            changes = true;
        }
        if (!this.contains("settings.forceRegisterCommandsAsConsole")) {
            this.set("settings.forceRegisterCommandsAsConsole", new ArrayList());
            changes = true;
        }
        if (changes) {
            this.plugin.getLogger().warning("Merge new Config Options - I'm not an error, please don't report me");
            this.plugin.getLogger().warning("Please check your config.yml file for new configs!");
        }
        this.plugin.saveConfig();
    }

    private static HashAlgorithm getPasswordHash() {
        String key = "settings.security.passwordHash";
        try {
            return HashAlgorithm.valueOf(configFile.getString(key, "SHA256").toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            ConsoleLogger.showError("Unknown Hash Algorithm; defaulting to SHA256");
            return HashAlgorithm.SHA256;
        }
    }

    private static DataSource.DataSourceType getDataSource() {
        String key = "DataSource.backend";
        try {
            return DataSource.DataSourceType.valueOf(configFile.getString(key).toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            ConsoleLogger.showError("Unknown database backend; defaulting to file database");
            return DataSource.DataSourceType.FILE;
        }
    }

    public static Boolean getRestrictedIp(String name, String ip) {
        Iterator<String> iter = getRestrictedIp.iterator();
        Boolean trueonce = false;
        Boolean namefound = false;
        while (iter.hasNext()) {
            String[] args = iter.next().split(";");
            String testname = args[0];
            String testip = args[1];
            if (!testname.equalsIgnoreCase(name)) continue;
            namefound = true;
            if (!testip.equalsIgnoreCase(ip)) continue;
            trueonce = true;
        }
        if (!namefound.booleanValue()) {
            return true;
        }
        if (trueonce.booleanValue()) {
            return true;
        }
        return false;
    }

    public final boolean load() {
        try {
            this.load(this.file);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public final void reload() {
        this.load();
        this.loadDefaults(this.file.getName());
    }

    public final boolean save() {
        try {
            this.save(this.file);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public final boolean exists() {
        return this.file.exists();
    }

    public final void loadDefaults(String filename) {
        InputStream stream = this.plugin.getResource(filename);
        if (stream == null) {
            return;
        }
        this.setDefaults((Configuration)YamlConfiguration.loadConfiguration((InputStream)stream));
    }

    public final boolean saveDefaults() {
        this.options().copyDefaults(true);
        this.options().copyHeader(true);
        boolean success = this.save();
        this.options().copyDefaults(false);
        this.options().copyHeader(false);
        return success;
    }

    public final void clearDefaults() {
        this.setDefaults((Configuration)new MemoryConfiguration());
    }

    public boolean checkDefaults() {
        if (this.getDefaults() == null) {
            return true;
        }
        return this.getKeys(true).containsAll(this.getDefaults().getKeys(true));
    }

    public static String checkLang(String lang) {
        for (messagesLang language : messagesLang.values()) {
            if (!lang.toLowerCase().contains(language.toString())) continue;
            ConsoleLogger.info("Set Language: " + lang);
            return lang;
        }
        ConsoleLogger.info("Set Default Language: En ");
        return "en";
    }

    public static void switchAntiBotMod(boolean mode) {
        isKickNonRegisteredEnabled = mode ? Boolean.valueOf(true) : Boolean.valueOf(configFile.getBoolean("settings.restrictions.kickNonRegistered", false));
    }

    private static void getWelcomeMessage(AuthMe plugin) {
        welcomeMsg = new ArrayList<String>();
        if (!useWelcomeMessage.booleanValue()) {
            return;
        }
        if (!new File(plugin.getDataFolder() + File.separator + "welcome.txt").exists()) {
            try {
                FileWriter fw = new FileWriter(plugin.getDataFolder() + File.separator + "welcome.txt", true);
                BufferedWriter w = new BufferedWriter(fw);
                w.write("Welcome {PLAYER} on {SERVER} server");
                w.newLine();
                w.write("This server use AuthMe protection!");
                w.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileReader fr = new FileReader(plugin.getDataFolder() + File.separator + "welcome.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                welcomeMsg.add(line);
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmailCorrect(String email) {
        if (!email.contains("@")) {
            return false;
        }
        if (email.equalsIgnoreCase("your@email.com")) {
            return false;
        }
        String emailDomain = email.split("@")[1];
        boolean correct = true;
        if (emailWhitelist != null && !emailWhitelist.isEmpty()) {
            for (String domain : emailWhitelist) {
                if (!domain.equalsIgnoreCase(emailDomain)) {
                    correct = false;
                    continue;
                }
                correct = true;
                break;
            }
            return correct;
        }
        if (emailBlacklist != null && !emailBlacklist.isEmpty()) {
            for (String domain : emailBlacklist) {
                if (!domain.equalsIgnoreCase(emailDomain)) continue;
                correct = false;
                break;
            }
        }
        return correct;
    }

    static {
        getMySQLOtherUsernameColumn = null;
        getForcedWorlds = null;
        countries = null;
        countriesBlacklist = null;
        forceCommands = null;
        forceCommandsAsConsole = null;
        forceRegisterCommands = null;
        forceRegisterCommandsAsConsole = null;
        useLogging = false;
        purgeDelay = 60;
        welcomeMsg = null;
        emailBlacklist = null;
        emailWhitelist = null;
    }

    public static enum messagesLang {
        en,
        de,
        br,
        cz,
        pl,
        fr,
        uk,
        ru,
        hu,
        sk,
        es,
        fi,
        zhtw,
        zhhk,
        zhcn,
        lt,
        it,
        ko,
        pt,
        nl,
        gl;
        

        private messagesLang() {
        }
    }

}

