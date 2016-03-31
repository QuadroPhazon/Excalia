/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.textreader.IText;
import java.util.List;

public class SimpleTextPager {
    private final transient IText text;

    public SimpleTextPager(IText text) {
        this.text = text;
    }

    public void showPage(CommandSource sender) {
        for (String line : this.text.getLines()) {
            sender.sendMessage(line);
        }
    }

    public List<String> getLines() {
        return this.text.getLines();
    }

    public String getLine(int line) {
        if (this.text.getLines().size() < line) {
            return null;
        }
        return this.text.getLines().get(line);
    }
}

