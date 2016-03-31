/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.utils.FormatUtil;
import com.earth2me.essentials.utils.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IEssentials;
import org.bukkit.entity.Player;

public class TextInput
implements IText {
    private static final HashMap<String, SoftReference<TextInput>> cache = new HashMap();
    private final transient List<String> lines;
    private final transient List<String> chapters;
    private final transient Map<String, Integer> bookmarks;
    private final transient long lastChange;

    public TextInput(CommandSource sender, String filename, boolean createFile, IEssentials ess) throws IOException {
        File file = null;
        if (sender.isPlayer()) {
            User user = ess.getUser(sender.getPlayer());
            file = new File(ess.getDataFolder(), filename + "_" + StringUtil.sanitizeFileName(user.getName()) + ".txt");
            if (!file.exists()) {
                file = new File(ess.getDataFolder(), filename + "_" + StringUtil.sanitizeFileName(user.getGroup()) + ".txt");
            }
        }
        if (file == null || !file.exists()) {
            file = new File(ess.getDataFolder(), filename + ".txt");
        }
        if (file.exists()) {
            boolean readFromfile;
            this.lastChange = file.lastModified();
            HashMap<String, SoftReference<TextInput>> hashMap = cache;
            synchronized (hashMap) {
                TextInput input;
                SoftReference<TextInput> inputRef = cache.get(file.getName());
                if (inputRef == null || (input = inputRef.get()) == null || input.lastChange < this.lastChange) {
                    this.lines = new ArrayList<String>();
                    this.chapters = new ArrayList<String>();
                    this.bookmarks = new HashMap<String, Integer>();
                    cache.put(file.getName(), new SoftReference<TextInput>(this));
                    readFromfile = true;
                } else {
                    this.lines = Collections.unmodifiableList(input.getLines());
                    this.chapters = Collections.unmodifiableList(input.getChapters());
                    this.bookmarks = Collections.unmodifiableMap(input.getBookmarks());
                    readFromfile = false;
                }
            }
            if (readFromfile) {
                InputStreamReader reader = new InputStreamReader((InputStream)new FileInputStream(file), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                try {
                    String line;
                    int lineNumber = 0;
                    while (bufferedReader.ready() && (line = bufferedReader.readLine()) != null) {
                        if (line.length() > 1 && line.charAt(0) == '#') {
                            String[] titles = line.substring(1).trim().replace(" ", "_").split(",");
                            this.chapters.add(FormatUtil.replaceFormat(titles[0]));
                            for (String title : titles) {
                                this.bookmarks.put(FormatUtil.stripEssentialsFormat(title.toLowerCase(Locale.ENGLISH)), lineNumber);
                            }
                        }
                        this.lines.add(FormatUtil.replaceFormat(line));
                        ++lineNumber;
                    }
                }
                finally {
                    reader.close();
                    bufferedReader.close();
                }
            }
        } else {
            this.lastChange = 0;
            this.lines = Collections.emptyList();
            this.chapters = Collections.emptyList();
            this.bookmarks = Collections.emptyMap();
            if (createFile) {
                InputStream input = ess.getResource(filename + ".txt");
                FileOutputStream output = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[1024];
                    int length = input.read(buffer);
                    while (length > 0) {
                        output.write(buffer, 0, length);
                        length = input.read(buffer);
                    }
                }
                finally {
                    output.close();
                    input.close();
                }
                throw new FileNotFoundException("File " + filename + ".txt does not exist. Creating one for you.");
            }
        }
    }

    @Override
    public List<String> getLines() {
        return this.lines;
    }

    @Override
    public List<String> getChapters() {
        return this.chapters;
    }

    @Override
    public Map<String, Integer> getBookmarks() {
        return this.bookmarks;
    }
}

