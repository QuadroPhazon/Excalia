/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.plugin.Plugin
 *  org.getspout.spoutapi.event.screen.ButtonClickEvent
 *  org.getspout.spoutapi.gui.Button
 *  org.getspout.spoutapi.gui.Color
 *  org.getspout.spoutapi.gui.GenericLabel
 *  org.getspout.spoutapi.gui.GenericPopup
 *  org.getspout.spoutapi.gui.GenericTextField
 *  org.getspout.spoutapi.gui.Label
 *  org.getspout.spoutapi.gui.RenderPriority
 *  org.getspout.spoutapi.gui.Screen
 *  org.getspout.spoutapi.gui.TextField
 *  org.getspout.spoutapi.gui.Widget
 *  org.getspout.spoutapi.gui.WidgetAnchor
 *  org.getspout.spoutapi.player.SpoutPlayer
 */
package fr.xephi.authme.gui.screens;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.gui.Clickable;
import fr.xephi.authme.gui.CustomButton;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.settings.SpoutCfg;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.TextField;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class LoginScreen
extends GenericPopup
implements Clickable {
    public AuthMe plugin = AuthMe.getInstance();
    private SpoutCfg spoutCfg = SpoutCfg.getInstance();
    private CustomButton exitBtn;
    private CustomButton loginBtn;
    private GenericTextField passBox;
    private GenericLabel titleLbl;
    private GenericLabel textLbl;
    private GenericLabel errorLbl;
    String exitTxt = this.spoutCfg.getString("LoginScreen.exit button");
    String loginTxt = this.spoutCfg.getString("LoginScreen.login button");
    String exitMsg = this.spoutCfg.getString("LoginScreen.exit message");
    String title = this.spoutCfg.getString("LoginScreen.title");
    List<String> textlines = this.spoutCfg.getList("LoginScreen.text");
    public SpoutPlayer splayer;

    public LoginScreen(SpoutPlayer player) {
        this.splayer = player;
        this.createScreen();
    }

    private void createScreen() {
        int objects = this.textlines.size() + 4;
        int part = this.textlines.size() > 5 ? 195 / objects : 20;
        int h = 3 * part / 4;
        int w = 8 * part;
        this.titleLbl = new GenericLabel();
        this.titleLbl.setText(this.title).setTextColor(new Color(1.0f, 0.0f, 0.0f, 1.0f)).setAlign(WidgetAnchor.TOP_CENTER).setHeight(h).setWidth(w).setX(this.maxWidth / 2).setY(25);
        this.attachWidget((Plugin)this.plugin, (Widget)this.titleLbl);
        int ystart = 25 + h + part / 2;
        for (int x = 0; x < this.textlines.size(); ++x) {
            this.textLbl = new GenericLabel();
            this.textLbl.setText(this.textlines.get(x)).setAlign(WidgetAnchor.TOP_CENTER).setHeight(h).setWidth(w).setX(this.maxWidth / 2).setY(ystart + x * part);
            this.attachWidget((Plugin)this.plugin, (Widget)this.textLbl);
        }
        this.passBox = new GenericTextField();
        this.passBox.setMaximumCharacters(18).setMaximumLines(1).setHeight(h - 2).setWidth(w - 2).setY(220 - h - 2 * part);
        this.passBox.setPasswordField(true);
        this.setXToMid((Widget)this.passBox);
        this.attachWidget((Plugin)this.plugin, (Widget)this.passBox);
        this.errorLbl = new GenericLabel();
        this.errorLbl.setText("").setTextColor(new Color(1.0f, 0.0f, 0.0f, 1.0f)).setHeight(h).setWidth(w).setX(this.passBox.getX() + this.passBox.getWidth() + 2).setY(this.passBox.getY());
        this.attachWidget((Plugin)this.plugin, (Widget)this.errorLbl);
        this.loginBtn = new CustomButton(this);
        this.loginBtn.setText(this.loginTxt).setHeight(h).setWidth(w).setY(220 - h - part);
        this.setXToMid((Widget)this.loginBtn);
        this.attachWidget((Plugin)this.plugin, (Widget)this.loginBtn);
        this.exitBtn = new CustomButton(this);
        this.exitBtn.setText(this.exitTxt).setHeight(h).setWidth(w).setY(220 - h);
        this.setXToMid((Widget)this.exitBtn);
        this.attachWidget((Plugin)this.plugin, (Widget)this.exitBtn);
        this.setPriority(RenderPriority.Highest);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void handleClick(ButtonClickEvent event) {
        Button b = event.getButton();
        SpoutPlayer player = event.getPlayer();
        if (event.isCancelled() || event == null || event.getPlayer() == null) {
            return;
        }
        if (b.equals((Object)this.loginBtn)) {
            this.plugin.management.performLogin((Player)player, this.passBox.getText(), false);
        } else if (b.equals((Object)this.exitBtn)) {
            event.getPlayer().kickPlayer(this.exitMsg);
        }
    }

    private void setXToMid(Widget w) {
        w.setX((this.maxWidth - w.getWidth()) / 2);
    }
}

