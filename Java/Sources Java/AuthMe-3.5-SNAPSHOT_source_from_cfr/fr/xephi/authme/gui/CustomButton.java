/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.getspout.spoutapi.event.screen.ButtonClickEvent
 *  org.getspout.spoutapi.gui.GenericButton
 *  org.getspout.spoutapi.gui.Widget
 */
package fr.xephi.authme.gui;

import fr.xephi.authme.gui.Clickable;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.Widget;

public class CustomButton
extends GenericButton {
    public Clickable handleRef = null;

    public CustomButton(Clickable c) {
        this.handleRef = c;
    }

    public void onButtonClick(ButtonClickEvent event) {
        this.handleRef.handleClick(event);
    }

    public CustomButton setMidPos(int x, int y) {
        this.setX(x).setY(y).shiftXPos(- this.width / 2).shiftYPos(- this.height / 2);
        return this;
    }
}

