/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.ess3.api.events;

import com.earth2me.essentials.I18n;
import java.util.IllegalFormatException;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LocalChatSpyEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private String message;
    private String format;
    private Player player;
    private final Set<Player> recipients;

    public LocalChatSpyEvent(boolean async, Player who, String format, String message, Set<Player> players) {
        super(async);
        this.format = I18n._("chatTypeLocal", new Object[0]).concat(I18n._("chatTypeSpy", new Object[0])).concat(format);
        this.message = message;
        this.recipients = players;
        this.player = who;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) throws IllegalFormatException, NullPointerException {
        try {
            String.format(format, new Object[]{this.player, this.message});
        }
        catch (RuntimeException ex) {
            ex.fillInStackTrace();
            throw ex;
        }
        this.format = format;
    }

    public Set<Player> getRecipients() {
        return this.recipients;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

