/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.textreader.IText;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookPager {
    private final transient IText text;

    public BookPager(IText text) {
        this.text = text;
    }

    public List<String> getPages(String pageStr) throws Exception {
        int chapterend;
        String line;
        List<String> lines = this.text.getLines();
        List<String> chapters = this.text.getChapters();
        ArrayList<String> pageLines = new ArrayList<String>();
        Map<String, Integer> bookmarks = this.text.getBookmarks();
        if (!bookmarks.containsKey(pageStr.toLowerCase(Locale.ENGLISH))) {
            throw new Exception(I18n._("infoUnknownChapter", new Object[0]));
        }
        for (chapterend = chapterstart = bookmarks.get((Object)pageStr.toLowerCase((Locale)Locale.ENGLISH)).intValue() + 1; chapterend < lines.size() && ((line = lines.get(chapterend)).length() <= 0 || line.charAt(0) != '#'); ++chapterend) {
        }
        for (int lineNo = chapterstart; lineNo < chapterend; ++lineNo) {
            String tempLine;
            String pageLine = "\u00a70" + lines.get(lineNo);
            double max = 18.0;
            int lineLength = pageLine.length();
            double length = 0.0;
            int pointer = 0;
            int start = 0;
            double weight = 1.0;
            while (pointer < lineLength) {
                Character letter = Character.valueOf(pageLine.charAt(pointer));
                if (pageLine.charAt(start) == ' ') {
                    ++start;
                    ++pointer;
                    continue;
                }
                if (length >= 18.0 || letter.charValue() == '\u00a7' && length + 1.0 >= 18.0) {
                    int pos;
                    for (pos = pointer; pos > start && pageLine.charAt(pos) != ' ' && pageLine.charAt(pos) != "\n".charAt(0); --pos) {
                    }
                    if (pos != start) {
                        pointer = pos;
                    }
                    tempLine = pageLine.substring(start, pointer);
                    pageLines.add(tempLine);
                    start = pointer;
                    length = 0.0;
                }
                if (letter.charValue() == '\u00a7' && pointer + 1 < lineLength) {
                    Character nextLetter = Character.valueOf(pageLine.charAt(pointer + 1));
                    weight = nextLetter.charValue() == 'l' || nextLetter.charValue() == 'L' ? 1.25 : 1.0;
                    ++pointer;
                } else {
                    length = letter.charValue() == 'i' || letter.charValue() == '.' || letter.charValue() == ',' ? (length += 0.4 * weight) : (letter.charValue() == 'l' ? (length += 0.6 * weight) : (letter.charValue() == ' ' || letter.charValue() == 't' ? (length += 0.7 * weight) : (length += weight)));
                }
                ++pointer;
            }
            if (length <= 0.0) continue;
            tempLine = pageLine.substring(start, lineLength);
            pageLines.add(tempLine);
        }
        ArrayList<String> pages = new ArrayList<String>();
        for (int count = 0; count < pageLines.size(); count += 12) {
            StringBuilder newPage = new StringBuilder();
            for (int i = count; i < count + 12 && i < pageLines.size(); ++i) {
                newPage.append((String)pageLines.get(i)).append("\n");
            }
            pages.add(newPage.toString());
        }
        return pages;
    }
}

