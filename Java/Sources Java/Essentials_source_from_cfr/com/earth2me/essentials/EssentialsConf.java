/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package com.earth2me.essentials;

import com.earth2me.essentials.I18n;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EssentialsConf
extends YamlConfiguration {
    private static final Logger LOGGER = Logger.getLogger("Essentials");
    private final File configFile;
    private String templateName = null;
    private Class<?> resourceClass = EssentialsConf.class;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private final AtomicInteger pendingDiskWrites = new AtomicInteger(0);
    private final byte[] bytebuffer = new byte[1024];

    public EssentialsConf(File configFile) {
        this.configFile = configFile.getAbsoluteFile();
    }

    public synchronized void load() {
        if (this.pendingDiskWrites.get() != 0) {
            LOGGER.log(Level.INFO, "File " + this.configFile + " not read, because it's not yet written to disk.");
            return;
        }
        if (!this.configFile.getParentFile().exists() && !this.configFile.getParentFile().mkdirs()) {
            LOGGER.log(Level.SEVERE, I18n._("failedToCreateConfig", this.configFile.toString()));
        }
        if (this.configFile.exists() && this.configFile.length() != 0) {
            try {
                FileInputStream input = new FileInputStream(this.configFile);
                try {
                    if (input.read() == 0) {
                        input.close();
                        this.configFile.delete();
                    }
                }
                catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
                finally {
                    try {
                        input.close();
                    }
                    catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
            catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        if (!this.configFile.exists()) {
            if (this.templateName != null) {
                LOGGER.log(Level.INFO, I18n._("creatingConfigFromTemplate", this.configFile.toString()));
                this.createFromTemplate();
            } else {
                return;
            }
        }
        try {
            FileInputStream inputStream = new FileInputStream(this.configFile);
            try {
                int length;
                long startSize = this.configFile.length();
                if (startSize > Integer.MAX_VALUE) {
                    throw new InvalidConfigurationException("File too big");
                }
                ByteBuffer buffer = ByteBuffer.allocate((int)startSize);
                while ((length = inputStream.read(this.bytebuffer)) != -1) {
                    if (length > buffer.remaining()) {
                        ByteBuffer resize = ByteBuffer.allocate(buffer.capacity() + length - buffer.remaining());
                        int resizePosition = buffer.position();
                        buffer.rewind();
                        resize.put(buffer);
                        resize.position(resizePosition);
                        buffer = resize;
                    }
                    buffer.put(this.bytebuffer, 0, length);
                }
                buffer.rewind();
                CharBuffer data = CharBuffer.allocate(buffer.capacity());
                CharsetDecoder decoder = UTF8.newDecoder();
                CoderResult result = decoder.decode(buffer, data, true);
                if (result.isError()) {
                    buffer.rewind();
                    data.clear();
                    LOGGER.log(Level.INFO, "File " + this.configFile.getAbsolutePath().toString() + " is not utf-8 encoded, trying " + Charset.defaultCharset().displayName());
                    decoder = Charset.defaultCharset().newDecoder();
                    result = decoder.decode(buffer, data, true);
                    if (result.isError()) {
                        throw new InvalidConfigurationException("Invalid Characters in file " + this.configFile.getAbsolutePath().toString());
                    }
                    decoder.flush(data);
                } else {
                    decoder.flush(data);
                }
                int end = data.position();
                data.rewind();
                super.loadFromString(data.subSequence(0, end).toString());
            }
            finally {
                inputStream.close();
            }
        }
        catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (InvalidConfigurationException ex) {
            File broken = new File(this.configFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            this.configFile.renameTo(broken);
            LOGGER.log(Level.SEVERE, "The file " + this.configFile.toString() + " is broken, it has been renamed to " + broken.toString(), ex.getCause());
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void createFromTemplate() {
        OutputStream ostr;
        InputStream istr;
        block26 : {
            istr = null;
            ostr = null;
            istr = this.resourceClass.getResourceAsStream(this.templateName);
            if (istr != null) break block26;
            LOGGER.log(Level.SEVERE, I18n._("couldNotFindTemplate", this.templateName));
            try {
                if (istr != null) {
                    istr.close();
                }
            }
            catch (IOException ex) {
                Logger.getLogger(EssentialsConf.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ostr == null) return;
                ostr.close();
                return;
            }
            catch (IOException ex) {
                LOGGER.log(Level.SEVERE, I18n._("failedToCloseConfig", this.configFile.toString()), ex);
            }
            return;
        }
        ostr = new FileOutputStream(this.configFile);
        byte[] buffer = new byte[1024];
        int length = 0;
        length = istr.read(buffer);
        while (length > 0) {
            ostr.write(buffer, 0, length);
            length = istr.read(buffer);
        }
        try {
            if (istr != null) {
                istr.close();
            }
        }
        catch (IOException ex) {
            Logger.getLogger(EssentialsConf.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (ostr == null) return;
            ostr.close();
            return;
        }
        catch (IOException ex) {
            LOGGER.log(Level.SEVERE, I18n._("failedToCloseConfig", this.configFile.toString()), ex);
            return;
        }
        catch (IOException ex) {
            try {
                LOGGER.log(Level.SEVERE, I18n._("failedToWriteConfig", this.configFile.toString()), ex);
            }
            catch (Throwable var5_13) {
                try {
                    if (istr != null) {
                        istr.close();
                    }
                }
                catch (IOException ex) {
                    Logger.getLogger(EssentialsConf.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (ostr == null) throw var5_13;
                    ostr.close();
                    throw var5_13;
                }
                catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, I18n._("failedToCloseConfig", this.configFile.toString()), ex);
                }
                throw var5_13;
            }
            try {
                if (istr != null) {
                    istr.close();
                }
            }
            catch (IOException ex) {
                Logger.getLogger(EssentialsConf.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ostr == null) return;
                ostr.close();
                return;
            }
            catch (IOException ex) {
                LOGGER.log(Level.SEVERE, I18n._("failedToCloseConfig", this.configFile.toString()), ex);
                return;
            }
        }
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public File getFile() {
        return this.configFile;
    }

    public void setTemplateName(String templateName, Class<?> resClass) {
        this.templateName = templateName;
        this.resourceClass = resClass;
    }

    public void save() {
        try {
            this.save(this.configFile);
        }
        catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void saveWithError() throws IOException {
        this.save(this.configFile);
    }

    public synchronized void save(File file) throws IOException {
        this.delayedSave(file);
    }

    public synchronized void forceSave() {
        try {
            Future future = this.delayedSave(this.configFile);
            if (future != null) {
                future.get();
            }
        }
        catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (ExecutionException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private Future<?> delayedSave(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        String data = this.saveToString();
        if (data.length() == 0) {
            return null;
        }
        this.pendingDiskWrites.incrementAndGet();
        Future future = EXECUTOR_SERVICE.submit(new WriteRunner(this.configFile, data, this.pendingDiskWrites));
        return future;
    }

    public boolean hasProperty(String path) {
        return this.isSet(path);
    }

    public Location getLocation(String path, Server server) throws InvalidWorldException {
        String worldString = (path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "world";
        String worldName = this.getString(worldString);
        if (worldName == null || worldName.isEmpty()) {
            return null;
        }
        World world = server.getWorld(worldName);
        if (world == null) {
            throw new InvalidWorldException(worldName);
        }
        return new Location(world, this.getDouble((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "x", 0.0), this.getDouble((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "y", 0.0), this.getDouble((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "z", 0.0), (float)this.getDouble((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "yaw", 0.0), (float)this.getDouble((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "pitch", 0.0));
    }

    public void setProperty(String path, Location loc) {
        this.set((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "world", loc.getWorld().getName());
        this.set((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "x", loc.getX());
        this.set((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "y", loc.getY());
        this.set((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "z", loc.getZ());
        this.set((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "yaw", Float.valueOf(loc.getYaw()));
        this.set((path == null ? "" : new StringBuilder().append(path).append(".").toString()) + "pitch", Float.valueOf(loc.getPitch()));
    }

    public ItemStack getItemStack(String path) {
        ItemStack stack = new ItemStack(Material.valueOf((String)this.getString(path + ".type", "AIR")), this.getInt(path + ".amount", 1), (short)this.getInt(path + ".damage", 0));
        ConfigurationSection enchants = this.getConfigurationSection(path + ".enchant");
        if (enchants != null) {
            for (String enchant : enchants.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName((String)enchant.toUpperCase(Locale.ENGLISH));
                if (enchantment == null) continue;
                int level = this.getInt(path + ".enchant." + enchant, enchantment.getStartLevel());
                stack.addUnsafeEnchantment(enchantment, level);
            }
        }
        return stack;
    }

    public void setProperty(String path, ItemStack stack) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", stack.getType().toString());
        map.put("amount", stack.getAmount());
        map.put("damage", stack.getDurability());
        Map enchantments = stack.getEnchantments();
        if (!enchantments.isEmpty()) {
            HashMap enchant = new HashMap();
            for (Map.Entry entry : enchantments.entrySet()) {
                enchant.put(((Enchantment)entry.getKey()).getName().toLowerCase(Locale.ENGLISH), entry.getValue());
            }
            map.put("enchant", enchant);
        }
        this.set(path, map);
    }

    public void setProperty(String path, List object) {
        this.set(path, new ArrayList(object));
    }

    public void setProperty(String path, Map object) {
        this.set(path, new LinkedHashMap(object));
    }

    public Object getProperty(String path) {
        return this.get(path);
    }

    public void setProperty(String path, BigDecimal bigDecimal) {
        this.set(path, bigDecimal.toString());
    }

    public void setProperty(String path, Object object) {
        this.set(path, object);
    }

    public void removeProperty(String path) {
        this.set(path, null);
    }

    public synchronized Object get(String path) {
        return super.get(path);
    }

    public synchronized Object get(String path, Object def) {
        return super.get(path, def);
    }

    public synchronized BigDecimal getBigDecimal(String path, BigDecimal def) {
        String input = super.getString(path);
        return EssentialsConf.toBigDecimal(input, def);
    }

    public static BigDecimal toBigDecimal(String input, BigDecimal def) {
        if (input == null || input.isEmpty()) {
            return def;
        }
        try {
            return new BigDecimal(input, MathContext.DECIMAL128);
        }
        catch (NumberFormatException e) {
            return def;
        }
        catch (ArithmeticException e) {
            return def;
        }
    }

    public synchronized boolean getBoolean(String path) {
        return super.getBoolean(path);
    }

    public synchronized boolean getBoolean(String path, boolean def) {
        return super.getBoolean(path, def);
    }

    public synchronized List<Boolean> getBooleanList(String path) {
        return super.getBooleanList(path);
    }

    public synchronized List<Byte> getByteList(String path) {
        return super.getByteList(path);
    }

    public synchronized List<Character> getCharacterList(String path) {
        return super.getCharacterList(path);
    }

    public synchronized ConfigurationSection getConfigurationSection(String path) {
        return super.getConfigurationSection(path);
    }

    public synchronized double getDouble(String path) {
        return super.getDouble(path);
    }

    public synchronized double getDouble(String path, double def) {
        return super.getDouble(path, def);
    }

    public synchronized List<Double> getDoubleList(String path) {
        return super.getDoubleList(path);
    }

    public synchronized List<Float> getFloatList(String path) {
        return super.getFloatList(path);
    }

    public synchronized int getInt(String path) {
        return super.getInt(path);
    }

    public synchronized int getInt(String path, int def) {
        return super.getInt(path, def);
    }

    public synchronized List<Integer> getIntegerList(String path) {
        return super.getIntegerList(path);
    }

    public synchronized ItemStack getItemStack(String path, ItemStack def) {
        return super.getItemStack(path, def);
    }

    public synchronized Set<String> getKeys(boolean deep) {
        return super.getKeys(deep);
    }

    public synchronized List<?> getList(String path) {
        return super.getList(path);
    }

    public synchronized List<?> getList(String path, List<?> def) {
        return super.getList(path, def);
    }

    public synchronized long getLong(String path) {
        return super.getLong(path);
    }

    public synchronized long getLong(String path, long def) {
        return super.getLong(path, def);
    }

    public synchronized List<Long> getLongList(String path) {
        return super.getLongList(path);
    }

    public synchronized Map<String, Object> getMap() {
        return this.map;
    }

    public synchronized List<Map<?, ?>> getMapList(String path) {
        return super.getMapList(path);
    }

    public synchronized OfflinePlayer getOfflinePlayer(String path) {
        return super.getOfflinePlayer(path);
    }

    public synchronized OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return super.getOfflinePlayer(path, def);
    }

    public synchronized List<Short> getShortList(String path) {
        return super.getShortList(path);
    }

    public synchronized String getString(String path) {
        return super.getString(path);
    }

    public synchronized String getString(String path, String def) {
        return super.getString(path, def);
    }

    public synchronized List<String> getStringList(String path) {
        return super.getStringList(path);
    }

    public synchronized Map<String, Object> getValues(boolean deep) {
        return super.getValues(deep);
    }

    public synchronized Vector getVector(String path) {
        return super.getVector(path);
    }

    public synchronized Vector getVector(String path, Vector def) {
        return super.getVector(path, def);
    }

    public synchronized boolean isBoolean(String path) {
        return super.isBoolean(path);
    }

    public synchronized boolean isConfigurationSection(String path) {
        return super.isConfigurationSection(path);
    }

    public synchronized boolean isDouble(String path) {
        return super.isDouble(path);
    }

    public synchronized boolean isInt(String path) {
        return super.isInt(path);
    }

    public synchronized boolean isItemStack(String path) {
        return super.isItemStack(path);
    }

    public synchronized boolean isList(String path) {
        return super.isList(path);
    }

    public synchronized boolean isLong(String path) {
        return super.isLong(path);
    }

    public synchronized boolean isOfflinePlayer(String path) {
        return super.isOfflinePlayer(path);
    }

    public synchronized boolean isSet(String path) {
        return super.isSet(path);
    }

    public synchronized boolean isString(String path) {
        return super.isString(path);
    }

    public synchronized boolean isVector(String path) {
        return super.isVector(path);
    }

    public synchronized void set(String path, Object value) {
        super.set(path, value);
    }

    static /* synthetic */ Logger access$100() {
        return LOGGER;
    }

    static /* synthetic */ Charset access$200() {
        return UTF8;
    }

    private static class WriteRunner
    implements Runnable {
        private final File configFile;
        private final String data;
        private final AtomicInteger pendingDiskWrites;

        private WriteRunner(File configFile, String data, AtomicInteger pendingDiskWrites) {
            this.configFile = configFile;
            this.data = data;
            this.pendingDiskWrites = pendingDiskWrites;
        }

        /*
         * Exception decompiling
         */
        @Override
        public void run() {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [2[TRYBLOCK]], but top level block is 18[SIMPLE_IF_TAKEN]
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2859)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:805)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
            // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:664)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:747)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
            // org.benf.cfr.reader.Main.doJar(Main.java:128)
            // org.benf.cfr.reader.Main.main(Main.java:178)
            throw new IllegalStateException("Decompilation failed");
        }
    }

}

