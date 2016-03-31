/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import java.util.List;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Commandspeed
extends EssentialsCommand {
    public Commandspeed() {
        super("speed");
    }

    @Override
    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        boolean isFly = this.isFlyMode(args[0]);
        float speed = this.getMoveSpeed(args[1]);
        this.speedOtherPlayers(server, sender, isFly, true, speed, args[2]);
    }

    @Override
    protected void run(Server server, User user, String commandLabel, String[] args) throws Exception {
        float speed;
        boolean isFly;
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }
        boolean isBypass = user.isAuthorized("essentials.speed.bypass");
        if (args.length == 1) {
            isFly = this.flyPermCheck(user, user.isFlying());
            speed = this.getMoveSpeed(args[0]);
        } else {
            isFly = this.flyPermCheck(user, this.isFlyMode(args[0]));
            speed = this.getMoveSpeed(args[1]);
            if (args.length > 2 && user.isAuthorized("essentials.speed.others")) {
                if (args[2].trim().length() < 2) {
                    throw new PlayerNotFoundException();
                }
                this.speedOtherPlayers(server, user.getSource(), isFly, isBypass, speed, args[2]);
                return;
            }
        }
        if (isFly) {
            user.setFlySpeed(this.getRealMoveSpeed(speed, isFly, isBypass));
            user.sendMessage(I18n._("moveSpeed", I18n._("flying", new Object[0]), Float.valueOf(speed), user.getDisplayName()));
        } else {
            user.setWalkSpeed(this.getRealMoveSpeed(speed, isFly, isBypass));
            user.sendMessage(I18n._("moveSpeed", I18n._("walking", new Object[0]), Float.valueOf(speed), user.getDisplayName()));
        }
    }

    private void speedOtherPlayers(Server server, CommandSource sender, boolean isFly, boolean isBypass, float speed, String name) throws PlayerNotFoundException {
        boolean skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).isAuthorized("essentials.vanish.interact");
        boolean foundUser = false;
        List matchedPlayers = server.matchPlayer(name);
        for (Player matchPlayer : matchedPlayers) {
            User player = this.ess.getUser(matchPlayer);
            if (skipHidden && player.isHidden()) continue;
            foundUser = true;
            if (isFly) {
                matchPlayer.setFlySpeed(this.getRealMoveSpeed(speed, isFly, isBypass));
                sender.sendMessage(I18n._("moveSpeed", I18n._("flying", new Object[0]), Float.valueOf(speed), matchPlayer.getDisplayName()));
                continue;
            }
            matchPlayer.setWalkSpeed(this.getRealMoveSpeed(speed, isFly, isBypass));
            sender.sendMessage(I18n._("moveSpeed", I18n._("walking", new Object[0]), Float.valueOf(speed), matchPlayer.getDisplayName()));
        }
        if (!foundUser) {
            throw new PlayerNotFoundException();
        }
    }

    private Boolean flyPermCheck(User user, boolean input) throws Exception {
        boolean canFly = user.isAuthorized("essentials.speed.fly");
        boolean canWalk = user.isAuthorized("essentials.speed.walk");
        if (input && canFly || !input && canWalk || !canFly && !canWalk) {
            return input;
        }
        if (canWalk) {
            return false;
        }
        return true;
    }

    private boolean isFlyMode(String modeString) throws NotEnoughArgumentsException {
        boolean isFlyMode;
        if (modeString.contains("fly") || modeString.equalsIgnoreCase("f")) {
            isFlyMode = true;
        } else if (modeString.contains("walk") || modeString.contains("run") || modeString.equalsIgnoreCase("w") || modeString.equalsIgnoreCase("r")) {
            isFlyMode = false;
        } else {
            throw new NotEnoughArgumentsException();
        }
        return isFlyMode;
    }

    private float getMoveSpeed(String moveSpeed) throws NotEnoughArgumentsException {
        float userSpeed;
        try {
            userSpeed = Float.parseFloat(moveSpeed);
            if (userSpeed > 10.0f) {
                userSpeed = 10.0f;
            } else if (userSpeed < 1.0E-4f) {
                userSpeed = 1.0E-4f;
            }
        }
        catch (NumberFormatException e) {
            throw new NotEnoughArgumentsException();
        }
        return userSpeed;
    }

    private float getRealMoveSpeed(float userSpeed, boolean isFly, boolean isBypass) {
        float defaultSpeed = isFly ? 0.1f : 0.2f;
        float maxSpeed = 1.0f;
        if (!isBypass) {
            maxSpeed = (float)(isFly ? this.ess.getSettings().getMaxFlySpeed() : this.ess.getSettings().getMaxWalkSpeed());
        }
        if (userSpeed < 1.0f) {
            return defaultSpeed * userSpeed;
        }
        float ratio = (userSpeed - 1.0f) / 9.0f * (maxSpeed - defaultSpeed);
        return ratio + defaultSpeed;
    }
}

