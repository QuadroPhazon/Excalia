/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.textreader.IText;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TextPager {
    private final transient IText text;
    private final transient boolean onePage;

    public TextPager(IText text) {
        this(text, false);
    }

    public TextPager(IText text, boolean onePage) {
        this.text = text;
        this.onePage = onePage;
    }

    public void showPage(String pageStr, String chapterPageStr, String commandName, CommandSource sender) {
        int chapterstart;
        int chapterend;
        String line;
        List<String> lines = this.text.getLines();
        List<String> chapters = this.text.getChapters();
        Map<String, Integer> bookmarks = this.text.getBookmarks();
        if (pageStr == null || pageStr.isEmpty() || pageStr.matches("[0-9]+")) {
            int end;
            String line2;
            if (!lines.isEmpty() && lines.get(0).startsWith("#")) {
                if (this.onePage) {
                    return;
                }
                sender.sendMessage(I18n._("infoChapter", new Object[0]));
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                for (String string : chapters) {
                    if (!first) {
                        sb.append(", ");
                    }
                    first = false;
                    sb.append(string);
                }
                sender.sendMessage(sb.toString());
                return;
            }
            int page = 1;
            try {
                page = Integer.parseInt(pageStr);
            }
            catch (NumberFormatException ex) {
                page = 1;
            }
            if (page < 1) {
                page = 1;
            }
            int start = this.onePage ? 0 : (page - 1) * 9;
            for (end = 0; end < lines.size() && !(line2 = lines.get(end)).startsWith("#"); ++end) {
            }
            int pages = end / 9 + (end % 9 > 0 ? 1 : 0);
            if (!this.onePage && commandName != null) {
                StringBuilder content = new StringBuilder();
                String[] title = commandName.split(" ", 2);
                if (title.length > 1) {
                    content.append(I18n.capitalCase(title[0])).append(": ");
                    content.append(title[1]);
                } else {
                    content.append(I18n.capitalCase(commandName));
                }
                sender.sendMessage(I18n._("infoPages", page, pages, content));
            }
            for (int i = start; i < end && i < start + (this.onePage ? 20 : 9); ++i) {
                sender.sendMessage("\u00a7r" + lines.get(i));
            }
            if (!this.onePage && page < pages && commandName != null) {
                sender.sendMessage(I18n._("readNextPage", commandName, page + 1));
            }
            return;
        }
        int chapterpage = 0;
        if (chapterPageStr != null) {
            try {
                chapterpage = Integer.parseInt(chapterPageStr) - 1;
            }
            catch (NumberFormatException ex) {
                chapterpage = 0;
            }
            if (chapterpage < 0) {
                chapterpage = 0;
            }
        }
        if (!bookmarks.containsKey(pageStr.toLowerCase(Locale.ENGLISH))) {
            sender.sendMessage(I18n._("infoUnknownChapter", new Object[0]));
            return;
        }
        for (chapterend = chapterstart = bookmarks.get((Object)pageStr.toLowerCase((Locale)Locale.ENGLISH)).intValue() + 1; chapterend < lines.size() && ((line = lines.get(chapterend)).length() <= 0 || line.charAt(0) != '#'); ++chapterend) {
        }
        int start = chapterstart + (this.onePage ? 0 : chapterpage * 9);
        int page = chapterpage + 1;
        int pages = (chapterend - chapterstart) / 9 + ((chapterend - chapterstart) % 9 > 0 ? 1 : 0);
        if (!this.onePage && commandName != null) {
            StringBuilder content = new StringBuilder();
            content.append(I18n.capitalCase(commandName)).append(": ");
            content.append(pageStr);
            sender.sendMessage(I18n._("infoChapterPages", content, page, pages));
        }
        for (int i = start; i < chapterend && i < start + (this.onePage ? 20 : 9); ++i) {
            sender.sendMessage("\u00a7r" + lines.get(i));
        }
        if (!this.onePage && page < pages && commandName != null) {
            sender.sendMessage(I18n._("readNextPage", commandName, pageStr + " " + (page + 1)));
        }
    }
}

