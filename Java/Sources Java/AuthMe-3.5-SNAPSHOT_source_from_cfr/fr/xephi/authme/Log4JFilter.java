/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.Marker
 *  org.apache.logging.log4j.core.Filter
 *  org.apache.logging.log4j.core.Filter$Result
 *  org.apache.logging.log4j.core.LogEvent
 *  org.apache.logging.log4j.core.Logger
 *  org.apache.logging.log4j.message.Message
 */
package fr.xephi.authme;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class Log4JFilter
implements Filter {
    public Filter.Result filter(LogEvent record) {
        try {
            if (record == null || record.getMessage() == null) {
                return Filter.Result.NEUTRAL;
            }
            String logM = record.getMessage().getFormattedMessage().toLowerCase();
            if (!logM.contains("issued server command:")) {
                return Filter.Result.NEUTRAL;
            }
            if (!(logM.contains("/login ") || logM.contains("/l ") || logM.contains("/reg ") || logM.contains("/changepassword ") || logM.contains("/unregister ") || logM.contains("/authme register ") || logM.contains("/authme changepassword ") || logM.contains("/authme reg ") || logM.contains("/authme cp ") || logM.contains("/register "))) {
                return Filter.Result.NEUTRAL;
            }
            return Filter.Result.DENY;
        }
        catch (NullPointerException npe) {
            return Filter.Result.NEUTRAL;
        }
    }

    public /* varargs */ Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object ... arg4) {
        try {
            if (message == null) {
                return Filter.Result.NEUTRAL;
            }
            String logM = message.toLowerCase();
            if (!logM.contains("issued server command:")) {
                return Filter.Result.NEUTRAL;
            }
            if (!(logM.contains("/login ") || logM.contains("/l ") || logM.contains("/reg ") || logM.contains("/changepassword ") || logM.contains("/unregister ") || logM.contains("/authme register ") || logM.contains("/authme changepassword ") || logM.contains("/authme reg ") || logM.contains("/authme cp ") || logM.contains("/register "))) {
                return Filter.Result.NEUTRAL;
            }
            return Filter.Result.DENY;
        }
        catch (NullPointerException npe) {
            return Filter.Result.NEUTRAL;
        }
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object message, Throwable arg4) {
        try {
            if (message == null) {
                return Filter.Result.NEUTRAL;
            }
            String logM = message.toString().toLowerCase();
            if (!logM.contains("issued server command:")) {
                return Filter.Result.NEUTRAL;
            }
            if (!(logM.contains("/login ") || logM.contains("/l ") || logM.contains("/reg ") || logM.contains("/changepassword ") || logM.contains("/unregister ") || logM.contains("/authme register ") || logM.contains("/authme changepassword ") || logM.contains("/authme reg ") || logM.contains("/authme cp ") || logM.contains("/register "))) {
                return Filter.Result.NEUTRAL;
            }
            return Filter.Result.DENY;
        }
        catch (NullPointerException npe) {
            return Filter.Result.NEUTRAL;
        }
    }

    public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message message, Throwable arg4) {
        try {
            if (message == null) {
                return Filter.Result.NEUTRAL;
            }
            String logM = message.getFormattedMessage().toLowerCase();
            if (!logM.contains("issued server command:")) {
                return Filter.Result.NEUTRAL;
            }
            if (!(logM.contains("/login ") || logM.contains("/l ") || logM.contains("/reg ") || logM.contains("/changepassword ") || logM.contains("/unregister ") || logM.contains("/authme register ") || logM.contains("/authme changepassword ") || logM.contains("/authme reg ") || logM.contains("/authme cp ") || logM.contains("/register "))) {
                return Filter.Result.NEUTRAL;
            }
            return Filter.Result.DENY;
        }
        catch (NullPointerException npe) {
            return Filter.Result.NEUTRAL;
        }
    }

    public Filter.Result getOnMatch() {
        return Filter.Result.NEUTRAL;
    }

    public Filter.Result getOnMismatch() {
        return Filter.Result.NEUTRAL;
    }
}

