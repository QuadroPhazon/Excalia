/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.textreader.IText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SimpleTextInput
implements IText {
    private final transient List<String> lines = new ArrayList<String>();

    public SimpleTextInput(String input) {
        this.lines.addAll(Arrays.asList(input.split("\\n")));
    }

    public SimpleTextInput(List<String> input) {
        this.lines.addAll(input);
    }

    public SimpleTextInput() {
    }

    @Override
    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public List<String> getChapters() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return Collections.emptyMap();
    }
}

