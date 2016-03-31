/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.events;

import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.events.CustomEvent;

public class SessionEvent
extends CustomEvent {
    private PlayerAuth player;
    private boolean isLogin;

    public SessionEvent(PlayerAuth auth, boolean isLogin) {
        this.player = auth;
        this.isLogin = isLogin;
    }

    public PlayerAuth getPlayerAuth() {
        return this.player;
    }

    public void setPlayer(PlayerAuth player) {
        this.player = player;
    }

    public boolean isLogin() {
        return this.isLogin;
    }
}

