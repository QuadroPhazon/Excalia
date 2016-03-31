/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.inventory.ItemStack
 */
package fr.xephi.authme.cache.limbo;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class LimboPlayer {
    private String name;
    private ItemStack[] inventory;
    private ItemStack[] armour;
    private Location loc = null;
    private int timeoutTaskId = -1;
    private int messageTaskId = -1;
    private GameMode gameMode = GameMode.SURVIVAL;
    private boolean operator = false;
    private String group = "";
    private boolean flying = false;

    public LimboPlayer(String name, Location loc, ItemStack[] inventory, ItemStack[] armour, GameMode gameMode, boolean operator, String group, boolean flying) {
        this.name = name;
        this.loc = loc;
        this.inventory = inventory;
        this.armour = armour;
        this.gameMode = gameMode;
        this.operator = operator;
        this.group = group;
        this.flying = flying;
    }

    public LimboPlayer(String name, Location loc, GameMode gameMode, boolean operator, String group, boolean flying) {
        this.name = name;
        this.loc = loc;
        this.gameMode = gameMode;
        this.operator = operator;
        this.group = group;
        this.flying = flying;
    }

    public LimboPlayer(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return this.name;
    }

    public Location getLoc() {
        return this.loc;
    }

    public ItemStack[] getArmour() {
        return this.armour;
    }

    public ItemStack[] getInventory() {
        return this.inventory;
    }

    public void setArmour(ItemStack[] armour) {
        this.armour = armour;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public boolean getOperator() {
        return this.operator;
    }

    public String getGroup() {
        return this.group;
    }

    public void setTimeoutTaskId(int i) {
        this.timeoutTaskId = i;
    }

    public int getTimeoutTaskId() {
        return this.timeoutTaskId;
    }

    public void setMessageTaskId(int messageTaskId) {
        this.messageTaskId = messageTaskId;
    }

    public int getMessageTaskId() {
        return this.messageTaskId;
    }

    public boolean isFlying() {
        return this.flying;
    }
}

