/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExecuteTimer {
    private final transient List<ExecuteRecord> times = new ArrayList<ExecuteRecord>();
    private final transient DecimalFormat decimalFormat = new DecimalFormat("#0.000", DecimalFormatSymbols.getInstance(Locale.US));

    public void start() {
        this.times.clear();
        this.mark("start");
    }

    public void mark(String label) {
        if (!this.times.isEmpty() || "start".equals(label)) {
            this.times.add(new ExecuteRecord(label, System.nanoTime()));
        }
    }

    public String end() {
        double duration;
        StringBuilder output = new StringBuilder();
        output.append("execution time: ");
        long time0 = 0;
        long time1 = 0;
        long time2 = 0;
        for (ExecuteRecord pair : this.times) {
            String mark = pair.getMark();
            time2 = pair.getTime();
            if (time1 > 0) {
                duration = (double)(time2 - time1) / 1000000.0;
                output.append(mark).append(": ").append(this.decimalFormat.format(duration)).append("ms - ");
            } else {
                time0 = time2;
            }
            time1 = time2;
        }
        duration = (double)(time1 - time0) / 1000000.0;
        output.append("Total: ").append(this.decimalFormat.format(duration)).append("ms");
        this.times.clear();
        return output.toString();
    }

    private static class ExecuteRecord {
        private final String mark;
        private final long time;

        public ExecuteRecord(String mark, long time) {
            this.mark = mark;
            this.time = time;
        }

        public String getMark() {
            return this.mark;
        }

        public long getTime() {
            return this.time;
        }
    }

}

