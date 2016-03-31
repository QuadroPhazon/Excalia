/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.textreader;

import java.util.List;
import java.util.Map;

public interface IText {
    public List<String> getLines();

    public List<String> getChapters();

    public Map<String, Integer> getBookmarks();
}

