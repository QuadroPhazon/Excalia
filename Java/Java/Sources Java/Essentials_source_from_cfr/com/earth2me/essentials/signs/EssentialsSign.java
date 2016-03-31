/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Sign
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.SignChangeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.material.MaterialData
 *  org.bukkit.material.Sign
 *  org.bukkit.plugin.PluginManager
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IItemDb;
import com.earth2me.essentials.signs.SignException;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.MaxMoneyException;
import net.ess3.api.events.SignBreakEvent;
import net.ess3.api.events.SignCreateEvent;
import net.ess3.api.events.SignInteractEvent;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginManager;

public class EssentialsSign {
    private static final Set<Material> EMPTY_SET = new HashSet<Material>();
    protected static final BigDecimal MINTRANSACTION = new BigDecimal("0.01");
    protected final transient String signName;

    public EssentialsSign(String signName) {
        this.signName = signName;
    }

    protected final boolean onSignCreate(SignChangeEvent event, IEssentials ess) {
        EventSign sign = new EventSign(event);
        User user = ess.getUser(event.getPlayer());
        if (!user.isAuthorized("essentials.signs." + this.signName.toLowerCase(Locale.ENGLISH) + ".create") && !user.isAuthorized("essentials.signs.create." + this.signName.toLowerCase(Locale.ENGLISH))) {
            return true;
        }
        sign.setLine(0, I18n._("signFormatFail", this.signName));
        SignCreateEvent signEvent = new SignCreateEvent(sign, this, user);
        ess.getServer().getPluginManager().callEvent((Event)signEvent);
        if (signEvent.isCancelled()) {
            return false;
        }
        try {
            boolean ret = this.onSignCreate(sign, user, this.getUsername(user), ess);
            if (ret) {
                sign.setLine(0, this.getSuccessName());
            }
            return ret;
        }
        catch (ChargeException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
        }
        catch (SignException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
        }
        return true;
    }

    public String getSuccessName() {
        return I18n._("signFormatSuccess", this.signName);
    }

    public String getTemplateName() {
        return I18n._("signFormatTemplate", this.signName);
    }

    public String getName() {
        return this.signName;
    }

    public String getUsername(User user) {
        return user.getName().substring(0, user.getName().length() > 13 ? 13 : user.getName().length());
    }

    protected final boolean onSignInteract(Block block, Player player, IEssentials ess) {
        BlockSign sign = new BlockSign(block);
        User user = ess.getUser(player);
        if (user.checkSignThrottle()) {
            return false;
        }
        try {
            if (user.isDead() || !user.isAuthorized("essentials.signs." + this.signName.toLowerCase(Locale.ENGLISH) + ".use") && !user.isAuthorized("essentials.signs.use." + this.signName.toLowerCase(Locale.ENGLISH))) {
                return false;
            }
            SignInteractEvent signEvent = new SignInteractEvent(sign, this, user);
            ess.getServer().getPluginManager().callEvent((Event)signEvent);
            if (signEvent.isCancelled()) {
                return false;
            }
            return this.onSignInteract(sign, user, this.getUsername(user), ess);
        }
        catch (ChargeException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
            return false;
        }
        catch (Exception ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
            return false;
        }
    }

    protected final boolean onSignBreak(Block block, Player player, IEssentials ess) throws MaxMoneyException {
        BlockSign sign = new BlockSign(block);
        User user = ess.getUser(player);
        try {
            if (!user.isAuthorized("essentials.signs." + this.signName.toLowerCase(Locale.ENGLISH) + ".break") && !user.isAuthorized("essentials.signs.break." + this.signName.toLowerCase(Locale.ENGLISH))) {
                return false;
            }
            SignBreakEvent signEvent = new SignBreakEvent(sign, this, user);
            ess.getServer().getPluginManager().callEvent((Event)signEvent);
            if (signEvent.isCancelled()) {
                return false;
            }
            return this.onSignBreak(sign, user, this.getUsername(user), ess);
        }
        catch (SignException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
            return false;
        }
    }

    protected boolean onSignCreate(ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException {
        return true;
    }

    protected boolean onSignInteract(ISign sign, User player, String username, IEssentials ess) throws SignException, ChargeException, MaxMoneyException {
        return true;
    }

    protected boolean onSignBreak(ISign sign, User player, String username, IEssentials ess) throws SignException, MaxMoneyException {
        return true;
    }

    protected final boolean onBlockPlace(Block block, Player player, IEssentials ess) {
        User user = ess.getUser(player);
        try {
            return this.onBlockPlace(block, user, this.getUsername(user), ess);
        }
        catch (ChargeException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
        }
        catch (SignException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
        }
        return false;
    }

    protected final boolean onBlockInteract(Block block, Player player, IEssentials ess) {
        User user = ess.getUser(player);
        try {
            return this.onBlockInteract(block, user, this.getUsername(user), ess);
        }
        catch (ChargeException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
        }
        catch (SignException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
        }
        return false;
    }

    protected final boolean onBlockBreak(Block block, Player player, IEssentials ess) throws MaxMoneyException {
        User user = ess.getUser(player);
        try {
            return this.onBlockBreak(block, user, this.getUsername(user), ess);
        }
        catch (SignException ex) {
            this.showError(ess, user.getSource(), ex, this.signName);
            return false;
        }
    }

    protected boolean onBlockBreak(Block block, IEssentials ess) {
        return true;
    }

    protected boolean onBlockExplode(Block block, IEssentials ess) {
        return true;
    }

    protected boolean onBlockBurn(Block block, IEssentials ess) {
        return true;
    }

    protected boolean onBlockIgnite(Block block, IEssentials ess) {
        return true;
    }

    protected boolean onBlockPush(Block block, IEssentials ess) {
        return true;
    }

    protected static boolean checkIfBlockBreaksSigns(Block block) {
        BlockFace[] directions;
        Block sign = block.getRelative(BlockFace.UP);
        if (sign.getType() == Material.SIGN_POST && EssentialsSign.isValidSign(new BlockSign(sign))) {
            return true;
        }
        for (BlockFace blockFace : directions = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
            Block signblock = block.getRelative(blockFace);
            if (signblock.getType() != Material.WALL_SIGN) continue;
            try {
                org.bukkit.material.Sign signMat = (org.bukkit.material.Sign)signblock.getState().getData();
                if (signMat == null || signMat.getFacing() != blockFace || !EssentialsSign.isValidSign(new BlockSign(signblock))) continue;
                return true;
            }
            catch (NullPointerException ex) {
                // empty catch block
            }
        }
        return false;
    }

    public static boolean isValidSign(ISign sign) {
        return sign.getLine(0).matches("\u00a71\\[.*\\]");
    }

    protected boolean onBlockPlace(Block block, User player, String username, IEssentials ess) throws SignException, ChargeException {
        return true;
    }

    protected boolean onBlockInteract(Block block, User player, String username, IEssentials ess) throws SignException, ChargeException {
        return true;
    }

    protected boolean onBlockBreak(Block block, User player, String username, IEssentials ess) throws SignException, MaxMoneyException {
        return true;
    }

    public Set<Material> getBlocks() {
        return EMPTY_SET;
    }

    public boolean areHeavyEventRequired() {
        return false;
    }

    private String getSignText(ISign sign, int lineNumber) {
        return sign.getLine(lineNumber).trim();
    }

    protected final void validateTrade(ISign sign, int index, IEssentials ess) throws SignException {
        String line = this.getSignText(sign, index);
        if (line.isEmpty()) {
            return;
        }
        Trade trade = this.getTrade(sign, index, 0, ess);
        BigDecimal money = trade.getMoney();
        if (money != null) {
            sign.setLine(index, NumberUtil.shortCurrency(money, ess));
        }
    }

    protected final void validateTrade(ISign sign, int amountIndex, int itemIndex, User player, IEssentials ess) throws SignException {
        String itemType = this.getSignText(sign, itemIndex);
        if (itemType.equalsIgnoreCase("exp") || itemType.equalsIgnoreCase("xp")) {
            int amount = this.getIntegerPositive(this.getSignText(sign, amountIndex));
            sign.setLine(amountIndex, Integer.toString(amount));
            sign.setLine(itemIndex, "exp");
            return;
        }
        Trade trade = this.getTrade(sign, amountIndex, itemIndex, player, ess);
        ItemStack item = trade.getItemStack();
        sign.setLine(amountIndex, Integer.toString(item.getAmount()));
        sign.setLine(itemIndex, itemType);
    }

    protected final Trade getTrade(ISign sign, int amountIndex, int itemIndex, User player, IEssentials ess) throws SignException {
        String itemType = this.getSignText(sign, itemIndex);
        if (itemType.equalsIgnoreCase("exp") || itemType.equalsIgnoreCase("xp")) {
            int amount = this.getIntegerPositive(this.getSignText(sign, amountIndex));
            return new Trade(amount, ess);
        }
        ItemStack item = this.getItemStack(itemType, 1, ess);
        int amount = Math.min(this.getIntegerPositive(this.getSignText(sign, amountIndex)), item.getType().getMaxStackSize() * player.getInventory().getSize());
        if (item.getType() == Material.AIR || amount < 1) {
            throw new SignException(I18n._("moreThanZero", new Object[0]));
        }
        item.setAmount(amount);
        return new Trade(item, ess);
    }

    protected final void validateInteger(ISign sign, int index) throws SignException {
        String line = this.getSignText(sign, index);
        if (line.isEmpty()) {
            throw new SignException("Empty line " + index);
        }
        int quantity = this.getIntegerPositive(line);
        sign.setLine(index, Integer.toString(quantity));
    }

    protected final int getIntegerPositive(String line) throws SignException {
        int quantity = this.getInteger(line);
        if (quantity < 1) {
            throw new SignException(I18n._("moreThanZero", new Object[0]));
        }
        return quantity;
    }

    protected final int getInteger(String line) throws SignException {
        try {
            int quantity = Integer.parseInt(line);
            return quantity;
        }
        catch (NumberFormatException ex) {
            throw new SignException("Invalid sign", ex);
        }
    }

    protected final ItemStack getItemStack(String itemName, int quantity, IEssentials ess) throws SignException {
        try {
            ItemStack item = ess.getItemDb().get(itemName);
            item.setAmount(quantity);
            return item;
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
    }

    protected final ItemStack getItemMeta(ItemStack item, String meta, IEssentials ess) throws SignException {
        ItemStack stack = item;
        try {
            if (!meta.isEmpty()) {
                MetaItemStack metaStack = new MetaItemStack(stack);
                boolean allowUnsafe = ess.getSettings().allowUnsafeEnchantments();
                metaStack.addStringMeta(null, allowUnsafe, meta, ess);
                stack = metaStack.getItemStack();
            }
        }
        catch (Exception ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        return stack;
    }

    protected final BigDecimal getMoney(String line) throws SignException {
        boolean isMoney = line.matches("^[^0-9-\\.][\\.0-9]+$");
        return isMoney ? this.getBigDecimalPositive(line.substring(1)) : null;
    }

    protected final BigDecimal getBigDecimalPositive(String line) throws SignException {
        BigDecimal quantity = this.getBigDecimal(line);
        if (quantity.compareTo(MINTRANSACTION) < 0) {
            throw new SignException(I18n._("moreThanZero", new Object[0]));
        }
        return quantity;
    }

    protected final BigDecimal getBigDecimal(String line) throws SignException {
        try {
            return new BigDecimal(line);
        }
        catch (ArithmeticException ex) {
            throw new SignException(ex.getMessage(), ex);
        }
        catch (NumberFormatException ex) {
            throw new SignException(ex.getMessage(), ex);
        }
    }

    protected final Trade getTrade(ISign sign, int index, IEssentials ess) throws SignException {
        return this.getTrade(sign, index, 1, ess);
    }

    protected final Trade getTrade(ISign sign, int index, int decrement, IEssentials ess) throws SignException {
        String line = this.getSignText(sign, index);
        if (line.isEmpty()) {
            return new Trade(this.signName.toLowerCase(Locale.ENGLISH) + "sign", ess);
        }
        BigDecimal money = this.getMoney(line);
        if (money == null) {
            String[] split = line.split("[ :]+", 2);
            if (split.length != 2) {
                throw new SignException(I18n._("invalidCharge", new Object[0]));
            }
            int quantity = this.getIntegerPositive(split[0]);
            String item = split[1].toLowerCase(Locale.ENGLISH);
            if (item.equalsIgnoreCase("times")) {
                sign.setLine(index, "" + (quantity - decrement) + " times");
                sign.updateSign();
                return new Trade(this.signName.toLowerCase(Locale.ENGLISH) + "sign", ess);
            }
            if (item.equalsIgnoreCase("exp") || item.equalsIgnoreCase("xp")) {
                sign.setLine(index, "" + quantity + " exp");
                return new Trade(quantity, ess);
            }
            ItemStack stack = this.getItemStack(item, quantity, ess);
            sign.setLine(index, "" + quantity + " " + item);
            return new Trade(stack, ess);
        }
        return new Trade(money, ess);
    }

    private void showError(IEssentials ess, CommandSource sender, Throwable exception, String signName) {
        ess.showError(sender, exception, "\\ sign: " + signName);
    }

    public static interface ISign {
        public String getLine(int var1);

        public void setLine(int var1, String var2);

        public Block getBlock();

        public void updateSign();
    }

    static class BlockSign
    implements ISign {
        private final transient Sign sign;
        private final transient Block block;

        BlockSign(Block block) {
            this.block = block;
            this.sign = (Sign)block.getState();
        }

        @Override
        public final String getLine(int index) {
            StringBuilder builder = new StringBuilder();
            for (char c : this.sign.getLine(index).toCharArray()) {
                if (c >= '\uf700' && c <= '\uf747') continue;
                builder.append(c);
            }
            return builder.toString();
        }

        @Override
        public final void setLine(int index, String text) {
            this.sign.setLine(index, text);
        }

        @Override
        public final Block getBlock() {
            return this.block;
        }

        @Override
        public final void updateSign() {
            this.sign.update();
        }
    }

    static class EventSign
    implements ISign {
        private final transient SignChangeEvent event;
        private final transient Block block;
        private final transient Sign sign;

        EventSign(SignChangeEvent event) {
            this.event = event;
            this.block = event.getBlock();
            this.sign = (Sign)this.block.getState();
        }

        @Override
        public final String getLine(int index) {
            StringBuilder builder = new StringBuilder();
            for (char c : this.event.getLine(index).toCharArray()) {
                if (c >= '\uf700' && c <= '\uf747') continue;
                builder.append(c);
            }
            return builder.toString();
        }

        @Override
        public final void setLine(int index, String text) {
            this.event.setLine(index, text);
            this.sign.setLine(index, text);
            this.updateSign();
        }

        @Override
        public Block getBlock() {
            return this.block;
        }

        @Override
        public void updateSign() {
            this.sign.update();
        }
    }

}

