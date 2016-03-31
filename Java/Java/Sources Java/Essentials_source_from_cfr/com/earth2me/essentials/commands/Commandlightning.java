/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LightningStrike
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import java.util.HashSet;
import net.ess3.api.IEssentials;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;

public class Commandlightning
extends EssentialsLoopCommand {
    int power = 5;

    public Commandlightning() {
        super("lightning");
    }

    @Override
    public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (sender.isPlayer()) {
            User user = this.ess.getUser(sender.getPlayer());
            if (args.length < 1 || user != null && !user.isAuthorized("essentials.lightning.others")) {
                user.getWorld().strikeLightning(user.getTargetBlock(null, 600).getLocation());
                return;
            }
        }
        if (args.length > 1) {
            try {
                this.power = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ex) {
                // empty catch block
            }
        }
        this.loopOnlinePlayers(server, sender, true, true, args[0], null);
    }

    @Override
    protected void updatePlayer(Server server, CommandSource sender, User matchUser, String[] args) {
        sender.sendMessage(I18n._("lightningUse", matchUser.getDisplayName()));
        LightningStrike strike = matchUser.getBase().getWorld().strikeLightningEffect(matchUser.getBase().getLocation());
        if (!matchUser.isGodModeEnabled()) {
            matchUser.getBase().damage((double)this.power, (Entity)strike);
        }
        if (this.ess.getSettings().warnOnSmite()) {
            matchUser.sendMessage(I18n._("lightningSmited", new Object[0]));
        }
    }
}

