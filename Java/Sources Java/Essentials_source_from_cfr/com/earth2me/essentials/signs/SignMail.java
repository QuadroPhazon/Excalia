/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.signs;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.signs.EssentialsSign;
import com.earth2me.essentials.signs.SignException;
import java.util.List;
import net.ess3.api.IEssentials;

public class SignMail
extends EssentialsSign {
    public SignMail() {
        super("Mail");
    }

    @Override
    protected boolean onSignInteract(EssentialsSign.ISign sign, User player, String username, IEssentials ess) throws SignException {
        List<String> mail = player.getMails();
        if (mail.isEmpty()) {
            player.sendMessage(I18n._("noNewMail", new Object[0]));
            return false;
        }
        for (String s : mail) {
            player.sendMessage(s);
        }
        player.sendMessage(I18n._("markMailAsRead", new Object[0]));
        return true;
    }
}

