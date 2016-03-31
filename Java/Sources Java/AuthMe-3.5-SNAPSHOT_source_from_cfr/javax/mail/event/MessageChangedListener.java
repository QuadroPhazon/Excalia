/*
 * Decompiled with CFR 0_110.
 */
package javax.mail.event;

import java.util.EventListener;
import javax.mail.event.MessageChangedEvent;

public interface MessageChangedListener
extends EventListener {
    public void messageChanged(MessageChangedEvent var1);
}

