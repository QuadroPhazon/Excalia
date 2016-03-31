/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.textreader;

import com.earth2me.essentials.textreader.IText;
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
import java.util.logging.Logger;
import net.ess3.api.IEssentials;

public class BookInput
implements IText {
    private static final HashMap<String, SoftReference<BookInput>> cache = new HashMap();
    private final transient List<String> lines;
    private final transient List<String> chapters;
    private final transient Map<String, Integer> bookmarks;
    private final transient long lastChange;

    public BookInput(String filename, boolean createFile, IEssentials ess) throws IOException {
        boolean readFromfile;
        Object output;
        File file = null;
        if (file == null || !file.exists()) {
            file = new File(ess.getDataFolder(), filename + ".txt");
        }
        if (!file.exists() && createFile) {
            InputStream input = ess.getResource(filename + ".txt");
            output = new FileOutputStream(file);
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
            ess.getLogger().info("File " + filename + ".txt does not exist. Creating one for you.");
        }
        if (!file.exists()) {
            this.lastChange = 0;
            this.lines = Collections.emptyList();
            this.chapters = Collections.emptyList();
            this.bookmarks = Collections.emptyMap();
            throw new FileNotFoundException("Could not create " + filename + ".txt");
        }
        this.lastChange = file.lastModified();
        output = cache;
        synchronized (output) {
            BookInput input;
            SoftReference<BookInput> inputRef = cache.get(file.getName());
            if (inputRef == null || (input = inputRef.get()) == null || input.lastChange < this.lastChange) {
                this.lines = new ArrayList<String>();
                this.chapters = new ArrayList<String>();
                this.bookmarks = new HashMap<String, Integer>();
                cache.put(file.getName(), new SoftReference<BookInput>(this));
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
                    if (line.length() > 0 && line.charAt(0) == '#') {
                        this.bookmarks.put(line.substring(1).toLowerCase(Locale.ENGLISH).replaceAll("&[0-9a-fk]", ""), lineNumber);
                        this.chapters.add(line.substring(1).replace('&', '\u00a7').replace("\u00a7\u00a7", "&"));
                    }
                    this.lines.add(line.replace('&', '\u00a7').replace("\u00a7\u00a7", "&"));
                    ++lineNumber;
                }
            }
            finally {
                reader.close();
                bufferedReader.close();
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

