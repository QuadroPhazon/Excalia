/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package fr.xephi.authme.datasource;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.settings.PlayersLogs;
import fr.xephi.authme.settings.Settings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class FlatFileThread
extends Thread
implements DataSource {
    private File source = new File(Settings.AUTH_FILE);

    @Override
    public void run() {
        try {
            this.source.createNewFile();
        }
        catch (IOException e) {
            ConsoleLogger.showError(e.getMessage());
            if (Settings.isStopEnabled.booleanValue()) {
                ConsoleLogger.showError("Can't use FLAT FILE... SHUTDOWN...");
                AuthMe.getInstance().getServer().shutdown();
            }
            if (!Settings.isStopEnabled.booleanValue()) {
                AuthMe.getInstance().getServer().getPluginManager().disablePlugin((Plugin)AuthMe.getInstance());
            }
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public synchronized boolean isAuthAvailable(String user) {
        br = null;
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args = line.split(":");
                if (args.length <= 1 || !args[0].equalsIgnoreCase(user)) continue;
                var5_9 = true;
                var7_10 = null;
                if (br == null) return var5_9;
                try {
                    br.close();
                    return var5_9;
                }
                catch (IOException ex) {
                    // empty catch block
                }
                return var5_9;
            }
            var7_11 = null;
            if (br == null) return false;
            try {}
            catch (IOException ex) {
                return false;
            }
            br.close();
            return false;
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_12 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [9 : 122->129)] { 
lbl32: // 1 sources:
                br.close();
                return args;
lbl34: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_13 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [9 : 122->129)] { 
lbl44: // 1 sources:
                br.close();
                return args;
lbl46: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
        }
        catch (Throwable var6_20) {
            var7_14 = null;
            if (br == null) throw var6_20;
            ** try [egrp 2[TRYBLOCK] [9 : 122->129)] { 
lbl54: // 1 sources:
            br.close();
            throw var6_20;
lbl56: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var6_20;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public synchronized boolean saveAuth(PlayerAuth auth) {
        if (this.isAuthAvailable(auth.getNickname())) {
            return false;
        }
        bw = null;
        try {
            try {
                bw = new BufferedWriter(new FileWriter(this.source, true));
                bw.write(auth.getNickname() + ":" + auth.getHash() + ":" + auth.getIp() + ":" + auth.getLastLogin() + ":" + auth.getQuitLocX() + ":" + auth.getQuitLocY() + ":" + auth.getQuitLocZ() + ":" + auth.getWorld() + ":" + auth.getEmail() + "\n");
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                bl = false;
                var6_4 = null;
                if (bw == null) return bl;
                ** try [egrp 2[TRYBLOCK] [4 : 194->201)] { 
lbl15: // 1 sources:
                bw.close();
                return bl;
lbl17: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return bl;
            }
            var6_3 = null;
            if (bw == null) return true;
            bw.close();
            return true;
            catch (IOException ex) {
                return true;
            }
        }
        catch (Throwable throwable) {
            var6_5 = null;
            if (bw == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [4 : 194->201)] { 
lbl33: // 1 sources:
            bw.close();
            throw throwable;
lbl35: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public synchronized boolean updatePassword(PlayerAuth auth) {
        if (!this.isAuthAvailable(auth.getNickname())) {
            return false;
        }
        newAuth = null;
        br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(this.source));
                line = "";
                block16 : while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (!args[0].equals(auth.getNickname())) continue;
                    switch (args.length) {
                        case 4: {
                            newAuth = new PlayerAuth(args[0], auth.getHash(), args[2], Long.parseLong(args[3]), 0.0, 0.0, 0.0, "world", "your@email.com");
                            break block16;
                        }
                        case 7: {
                            newAuth = new PlayerAuth(args[0], auth.getHash(), args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), "world", "your@email.com");
                            break block16;
                        }
                        case 8: {
                            newAuth = new PlayerAuth(args[0], auth.getHash(), args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], "your@email.com");
                            break block16;
                        }
                        case 9: {
                            newAuth = new PlayerAuth(args[0], auth.getHash(), args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], args[8]);
                            break block16;
                        }
                        default: {
                            newAuth = new PlayerAuth(args[0], auth.getHash(), args[2], 0, 0.0, 0.0, 0.0, "world", "your@email.com");
                            break block16;
                        }
                    }
                }
                var7_10 = null;
                ** if (br == null) goto lbl-1000
            }
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_11 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 422->429)] { 
lbl42: // 1 sources:
                br.close();
                return args;
lbl44: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_12 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 422->429)] { 
lbl54: // 1 sources:
                br.close();
                return args;
lbl56: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
lbl-1000: // 1 sources:
            {
                try {
                    br.close();
                }
                catch (IOException ex) {}
            }
lbl-1000: // 2 sources:
            {
            }
        }
        catch (Throwable var6_18) {
            var7_13 = null;
            if (br == null) throw var6_18;
            ** try [egrp 2[TRYBLOCK] [6 : 422->429)] { 
lbl64: // 1 sources:
            br.close();
            throw var6_18;
lbl66: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var6_18;
        }
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public boolean updateSession(PlayerAuth auth) {
        if (!this.isAuthAvailable(auth.getNickname())) {
            return false;
        }
        newAuth = null;
        br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(this.source));
                line = "";
                block16 : while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (!args[0].equalsIgnoreCase(auth.getNickname())) continue;
                    switch (args.length) {
                        case 4: {
                            newAuth = new PlayerAuth(args[0], args[1], auth.getIp(), auth.getLastLogin(), 0.0, 0.0, 0.0, "world", "your@email.com");
                            break block16;
                        }
                        case 7: {
                            newAuth = new PlayerAuth(args[0], args[1], auth.getIp(), auth.getLastLogin(), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), "world", "your@email.com");
                            break block16;
                        }
                        case 8: {
                            newAuth = new PlayerAuth(args[0], args[1], auth.getIp(), auth.getLastLogin(), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], "your@email.com");
                            break block16;
                        }
                        case 9: {
                            newAuth = new PlayerAuth(args[0], args[1], auth.getIp(), auth.getLastLogin(), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], args[8]);
                            break block16;
                        }
                        default: {
                            newAuth = new PlayerAuth(args[0], args[1], auth.getIp(), auth.getLastLogin(), 0.0, 0.0, 0.0, "world", "your@email.com");
                            break block16;
                        }
                    }
                }
                var7_10 = null;
                ** if (br == null) goto lbl-1000
            }
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_11 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 413->420)] { 
lbl42: // 1 sources:
                br.close();
                return args;
lbl44: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_12 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 413->420)] { 
lbl54: // 1 sources:
                br.close();
                return args;
lbl56: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
lbl-1000: // 1 sources:
            {
                try {
                    br.close();
                }
                catch (IOException ex) {}
            }
lbl-1000: // 2 sources:
            {
            }
        }
        catch (Throwable var6_18) {
            var7_13 = null;
            if (br == null) throw var6_18;
            ** try [egrp 2[TRYBLOCK] [6 : 413->420)] { 
lbl64: // 1 sources:
            br.close();
            throw var6_18;
lbl66: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var6_18;
        }
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public boolean updateQuitLoc(PlayerAuth auth) {
        if (!this.isAuthAvailable(auth.getNickname())) {
            return false;
        }
        newAuth = null;
        br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(this.source));
                line = "";
                while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (!args[0].equalsIgnoreCase(auth.getNickname())) continue;
                    newAuth = new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), auth.getQuitLocX(), auth.getQuitLocY(), auth.getQuitLocZ(), auth.getWorld(), auth.getEmail());
                    break;
                }
                var7_10 = null;
                ** if (br == null) goto lbl-1000
            }
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_11 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 184->191)] { 
lbl28: // 1 sources:
                br.close();
                return args;
lbl30: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_12 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 184->191)] { 
lbl40: // 1 sources:
                br.close();
                return args;
lbl42: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
lbl-1000: // 1 sources:
            {
                try {
                    br.close();
                }
                catch (IOException ex) {}
            }
lbl-1000: // 2 sources:
            {
            }
        }
        catch (Throwable var6_18) {
            var7_13 = null;
            if (br == null) throw var6_18;
            ** try [egrp 2[TRYBLOCK] [6 : 184->191)] { 
lbl50: // 1 sources:
            br.close();
            throw var6_18;
lbl52: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var6_18;
        }
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public int getIps(String ip) {
        br = null;
        countIp = 0;
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args22 = line.split(":");
                if (args22.length <= 3 || !args22[2].equals(ip)) continue;
                ++countIp;
            }
            args22 = countIp;
            var7_11 = null;
            if (br == null) return args22;
            try {
                br.close();
                return args22;
            }
            catch (IOException ex) {
                // empty catch block
            }
            return args22;
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args22 = 0;
                var7_12 = null;
                if (br == null) return args22;
                ** try [egrp 2[TRYBLOCK] [6 : 127->134)] { 
lbl28: // 1 sources:
                br.close();
                return args22;
lbl30: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args22;
            }
            catch (IOException ex2) {
                ConsoleLogger.showError(ex2.getMessage());
                args22 = 0;
                var7_13 = null;
                if (br == null) return args22;
                ** try [egrp 2[TRYBLOCK] [6 : 127->134)] { 
lbl40: // 1 sources:
                br.close();
                return args22;
lbl42: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args22;
            }
        }
        catch (Throwable throwable) {
            var7_14 = null;
            if (br == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [6 : 127->134)] { 
lbl50: // 1 sources:
            br.close();
            throw throwable;
lbl52: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public int purgeDatabase(long until) {
        br = null;
        bw = null;
        lines = new ArrayList<String>();
        cleared = 0;
        try {
            block21 : {
                br = new BufferedReader(new FileReader(this.source));
                while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (args.length >= 4 && Long.parseLong(args[3]) >= until) {
                        lines.add(line);
                        continue;
                    }
                    ++cleared;
                }
                bw = new BufferedWriter(new FileWriter(this.source));
                for (String l : lines) {
                    bw.write(l + "\n");
                }
                var11_13 = null;
                if (br == null) break block21;
                try {
                    br.close();
                }
                catch (IOException ex) {
                    // empty catch block
                }
            }
            if (bw == null) return cleared;
            try {
                bw.close();
                return cleared;
            }
            catch (IOException ex) {
                return cleared;
            }
            catch (FileNotFoundException ex2) {
                block22 : {
                    ConsoleLogger.showError(ex2.getMessage());
                    i$ = cleared;
                    var11_14 = null;
                    if (br != null) {
                        ** try [egrp 2[TRYBLOCK] [6 : 228->235)] { 
lbl40: // 1 sources:
                        br.close();
                        break block22;
lbl42: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return i$;
                ** try [egrp 3[TRYBLOCK] [7 : 242->250)] { 
lbl47: // 1 sources:
                bw.close();
                return i$;
lbl49: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return i$;
            }
            catch (IOException ex3) {
                block23 : {
                    ConsoleLogger.showError(ex3.getMessage());
                    i$ = cleared;
                    var11_15 = null;
                    if (br != null) {
                        ** try [egrp 2[TRYBLOCK] [6 : 228->235)] { 
lbl59: // 1 sources:
                        br.close();
                        break block23;
lbl61: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return i$;
                ** try [egrp 3[TRYBLOCK] [7 : 242->250)] { 
lbl66: // 1 sources:
                bw.close();
                return i$;
lbl68: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return i$;
            }
        }
        catch (Throwable var10_21) {
            block24 : {
                var11_16 = null;
                if (br != null) {
                    ** try [egrp 2[TRYBLOCK] [6 : 228->235)] { 
lbl76: // 1 sources:
                    br.close();
                    break block24;
lbl78: // 1 sources:
                    catch (IOException ex) {
                        // empty catch block
                    }
                }
            }
            if (bw == null) throw var10_21;
            ** try [egrp 3[TRYBLOCK] [7 : 242->250)] { 
lbl83: // 1 sources:
            bw.close();
            throw var10_21;
lbl85: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var10_21;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public List<String> autoPurgeDatabase(long until) {
        br = null;
        bw = null;
        lines = new ArrayList<String>();
        cleared = new ArrayList<String>();
        try {
            block21 : {
                br = new BufferedReader(new FileReader(this.source));
                while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (args.length >= 4 && Long.parseLong(args[3]) >= until) {
                        lines.add(line);
                        continue;
                    }
                    cleared.add(args[0]);
                }
                bw = new BufferedWriter(new FileWriter(this.source));
                for (String l : lines) {
                    bw.write(l + "\n");
                }
                var11_13 = null;
                if (br == null) break block21;
                try {
                    br.close();
                }
                catch (IOException ex) {
                    // empty catch block
                }
            }
            if (bw == null) return cleared;
            try {
                bw.close();
                return cleared;
            }
            catch (IOException ex) {
                return cleared;
            }
            catch (FileNotFoundException ex2) {
                block22 : {
                    ConsoleLogger.showError(ex2.getMessage());
                    i$ = cleared;
                    var11_14 = null;
                    if (br != null) {
                        ** try [egrp 2[TRYBLOCK] [6 : 243->250)] { 
lbl40: // 1 sources:
                        br.close();
                        break block22;
lbl42: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return i$;
                ** try [egrp 3[TRYBLOCK] [7 : 257->265)] { 
lbl47: // 1 sources:
                bw.close();
                return i$;
lbl49: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return i$;
            }
            catch (IOException ex3) {
                block23 : {
                    ConsoleLogger.showError(ex3.getMessage());
                    i$ = cleared;
                    var11_15 = null;
                    if (br != null) {
                        ** try [egrp 2[TRYBLOCK] [6 : 243->250)] { 
lbl59: // 1 sources:
                        br.close();
                        break block23;
lbl61: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return i$;
                ** try [egrp 3[TRYBLOCK] [7 : 257->265)] { 
lbl66: // 1 sources:
                bw.close();
                return i$;
lbl68: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return i$;
            }
        }
        catch (Throwable var10_21) {
            block24 : {
                var11_16 = null;
                if (br != null) {
                    ** try [egrp 2[TRYBLOCK] [6 : 243->250)] { 
lbl76: // 1 sources:
                    br.close();
                    break block24;
lbl78: // 1 sources:
                    catch (IOException ex) {
                        // empty catch block
                    }
                }
            }
            if (bw == null) throw var10_21;
            ** try [egrp 3[TRYBLOCK] [7 : 257->265)] { 
lbl83: // 1 sources:
            bw.close();
            throw var10_21;
lbl85: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var10_21;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public synchronized boolean removeAuth(String user) {
        if (!this.isAuthAvailable(user)) {
            return false;
        }
        br = null;
        bw = null;
        lines = new ArrayList<String>();
        try {
            block21 : {
                br = new BufferedReader(new FileReader(this.source));
                while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (args.length <= 1 || args[0].equals(user)) continue;
                    lines.add(line);
                }
                bw = new BufferedWriter(new FileWriter(this.source));
                for (String l : lines) {
                    bw.write(l + "\n");
                }
                var9_12 = null;
                if (br == null) break block21;
                try {
                    br.close();
                }
                catch (IOException ex) {
                    // empty catch block
                }
            }
            if (bw == null) return true;
            try {
                bw.close();
                return true;
            }
            catch (IOException ex) {
                return true;
            }
            catch (FileNotFoundException ex2) {
                block22 : {
                    ConsoleLogger.showError(ex2.getMessage());
                    i$ = false;
                    var9_13 = null;
                    if (br != null) {
                        ** try [egrp 2[TRYBLOCK] [6 : 223->230)] { 
lbl39: // 1 sources:
                        br.close();
                        break block22;
lbl41: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return i$;
                ** try [egrp 3[TRYBLOCK] [7 : 236->243)] { 
lbl46: // 1 sources:
                bw.close();
                return i$;
lbl48: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return i$;
            }
            catch (IOException ex3) {
                block23 : {
                    ConsoleLogger.showError(ex3.getMessage());
                    i$ = false;
                    var9_14 = null;
                    if (br != null) {
                        ** try [egrp 2[TRYBLOCK] [6 : 223->230)] { 
lbl58: // 1 sources:
                        br.close();
                        break block23;
lbl60: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return i$;
                ** try [egrp 3[TRYBLOCK] [7 : 236->243)] { 
lbl65: // 1 sources:
                bw.close();
                return i$;
lbl67: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return i$;
            }
        }
        catch (Throwable var8_20) {
            block24 : {
                var9_15 = null;
                if (br != null) {
                    ** try [egrp 2[TRYBLOCK] [6 : 223->230)] { 
lbl75: // 1 sources:
                    br.close();
                    break block24;
lbl77: // 1 sources:
                    catch (IOException ex) {
                        // empty catch block
                    }
                }
            }
            if (bw == null) throw var8_20;
            ** try [egrp 3[TRYBLOCK] [7 : 236->243)] { 
lbl82: // 1 sources:
            bw.close();
            throw var8_20;
lbl84: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var8_20;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public synchronized PlayerAuth getAuth(String user) {
        br = null;
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args = line.split(":");
                if (!args[0].equalsIgnoreCase(user)) continue;
                switch (args.length) {
                    case 2: {
                        var5_9 = new PlayerAuth(args[0], args[1], "198.18.0.1", 0, "your@email.com");
                        var7_15 = null;
                        if (br == null) return var5_9;
                        try {
                            br.close();
                            return var5_9;
                        }
                        catch (IOException ex) {
                            // empty catch block
                        }
                        return var5_9;
                    }
                    case 3: {
                        var5_10 = new PlayerAuth(args[0], args[1], args[2], 0, "your@email.com");
                        var7_16 = null;
                        if (br == null) return var5_10;
                        ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl26: // 1 sources:
                        br.close();
                        return var5_10;
lbl28: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                        return var5_10;
                    }
                    case 4: {
                        var5_11 = new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), "your@email.com");
                        var7_17 = null;
                        if (br == null) return var5_11;
                        ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl37: // 1 sources:
                        br.close();
                        return var5_11;
lbl39: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                        return var5_11;
                    }
                    case 7: {
                        var5_12 = new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), "unavailableworld", "your@email.com");
                        var7_18 = null;
                        if (br == null) return var5_12;
                        ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl48: // 1 sources:
                        br.close();
                        return var5_12;
lbl50: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                        return var5_12;
                    }
                    case 8: {
                        var5_13 = new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], "your@email.com");
                        var7_19 = null;
                        if (br == null) return var5_13;
                        ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl59: // 1 sources:
                        br.close();
                        return var5_13;
lbl61: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                        return var5_13;
                    }
                    case 9: {
                        var5_14 = new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], args[8]);
                        var7_20 = null;
                        if (br == null) return var5_14;
                        ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl70: // 1 sources:
                        br.close();
                        return var5_14;
lbl72: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                        return var5_14;
                    }
                }
            }
            var7_21 = null;
            if (br == null) return null;
            try {}
            catch (IOException ex) {
                return null;
            }
            br.close();
            return null;
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = null;
                var7_22 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl90: // 1 sources:
                br.close();
                return args;
lbl92: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = null;
                var7_23 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl102: // 1 sources:
                br.close();
                return args;
lbl104: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
        }
        catch (Throwable var6_35) {
            var7_24 = null;
            if (br == null) throw var6_35;
            ** try [egrp 2[TRYBLOCK] [24 : 440->447)] { 
lbl112: // 1 sources:
            br.close();
            throw var6_35;
lbl114: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var6_35;
        }
    }

    @Override
    public synchronized void close() {
    }

    @Override
    public void reload() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public boolean updateEmail(PlayerAuth auth) {
        if (!this.isAuthAvailable(auth.getNickname())) {
            return false;
        }
        newAuth = null;
        br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(this.source));
                line = "";
                while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    if (!args[0].equals(auth.getNickname())) continue;
                    newAuth = new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], auth.getEmail());
                    break;
                }
                var7_10 = null;
                ** if (br == null) goto lbl-1000
            }
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_11 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 195->202)] { 
lbl28: // 1 sources:
                br.close();
                return args;
lbl30: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = false;
                var7_12 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 195->202)] { 
lbl40: // 1 sources:
                br.close();
                return args;
lbl42: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
lbl-1000: // 1 sources:
            {
                try {
                    br.close();
                }
                catch (IOException ex) {}
            }
lbl-1000: // 2 sources:
            {
            }
        }
        catch (Throwable var6_18) {
            var7_13 = null;
            if (br == null) throw var6_18;
            ** try [egrp 2[TRYBLOCK] [6 : 195->202)] { 
lbl50: // 1 sources:
            br.close();
            throw var6_18;
lbl52: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var6_18;
        }
        this.removeAuth(auth.getNickname());
        this.saveAuth(newAuth);
        return true;
    }

    @Override
    public boolean updateSalt(PlayerAuth auth) {
        return false;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public List<String> getAllAuthsByName(PlayerAuth auth) {
        br = null;
        countIp = new String[]();
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args222 = line.split(":");
                if (args222.length <= 3 || !args222[2].equals(auth.getIp())) continue;
                countIp.add(args222[0]);
            }
            args222 = countIp;
            var7_10 = null;
            if (br == null) return args222;
            try {
                br.close();
                return args222;
            }
            catch (IOException ex) {
                // empty catch block
            }
            return args222;
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args222 = new ArrayList<String>();
                var7_11 = null;
                if (br == null) return args222;
                ** try [egrp 2[TRYBLOCK] [6 : 156->163)] { 
lbl28: // 1 sources:
                br.close();
                return args222;
lbl30: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args222;
            }
            catch (IOException ex2) {
                ConsoleLogger.showError(ex2.getMessage());
                args222 = new ArrayList<String>();
                var7_12 = null;
                if (br == null) return args222;
                ** try [egrp 2[TRYBLOCK] [6 : 156->163)] { 
lbl40: // 1 sources:
                br.close();
                return args222;
lbl42: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args222;
            }
        }
        catch (Throwable throwable) {
            var7_13 = null;
            if (br == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [6 : 156->163)] { 
lbl50: // 1 sources:
            br.close();
            throw throwable;
lbl52: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public List<String> getAllAuthsByIp(String ip) {
        br = null;
        countIp = new String[]();
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args222 = line.split(":");
                if (args222.length <= 3 || !args222[2].equals(ip)) continue;
                countIp.add(args222[0]);
            }
            args222 = countIp;
            var7_10 = null;
            if (br == null) return args222;
            try {
                br.close();
                return args222;
            }
            catch (IOException ex) {
                // empty catch block
            }
            return args222;
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args222 = new ArrayList<String>();
                var7_11 = null;
                if (br == null) return args222;
                ** try [egrp 2[TRYBLOCK] [6 : 153->160)] { 
lbl28: // 1 sources:
                br.close();
                return args222;
lbl30: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args222;
            }
            catch (IOException ex2) {
                ConsoleLogger.showError(ex2.getMessage());
                args222 = new ArrayList<String>();
                var7_12 = null;
                if (br == null) return args222;
                ** try [egrp 2[TRYBLOCK] [6 : 153->160)] { 
lbl40: // 1 sources:
                br.close();
                return args222;
lbl42: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args222;
            }
        }
        catch (Throwable throwable) {
            var7_13 = null;
            if (br == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [6 : 153->160)] { 
lbl50: // 1 sources:
            br.close();
            throw throwable;
lbl52: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public List<String> getAllAuthsByEmail(String email) {
        br = null;
        countEmail = new String[]();
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args222 = line.split(":");
                if (args222.length <= 8 || !args222[8].equals(email)) continue;
                countEmail.add(args222[0]);
            }
            args222 = countEmail;
            var7_10 = null;
            if (br == null) return args222;
            try {
                br.close();
                return args222;
            }
            catch (IOException ex) {
                // empty catch block
            }
            return args222;
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args222 = new ArrayList<String>();
                var7_11 = null;
                if (br == null) return args222;
                ** try [egrp 2[TRYBLOCK] [6 : 155->162)] { 
lbl28: // 1 sources:
                br.close();
                return args222;
lbl30: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args222;
            }
            catch (IOException ex2) {
                ConsoleLogger.showError(ex2.getMessage());
                args222 = new ArrayList<String>();
                var7_12 = null;
                if (br == null) return args222;
                ** try [egrp 2[TRYBLOCK] [6 : 155->162)] { 
lbl40: // 1 sources:
                br.close();
                return args222;
lbl42: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args222;
            }
        }
        catch (Throwable throwable) {
            var7_13 = null;
            if (br == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [6 : 155->162)] { 
lbl50: // 1 sources:
            br.close();
            throw throwable;
lbl52: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void purgeBanned(List<String> banned) {
        br = null;
        bw = null;
        lines = new ArrayList<String>();
        try {
            block23 : {
                br = new BufferedReader(new FileReader(this.source));
                while ((line = br.readLine()) != null) {
                    args = line.split(":");
                    try {
                        if (!banned.contains(args[0])) continue;
                        lines.add(line);
                    }
                    catch (NullPointerException npe) {
                    }
                    catch (ArrayIndexOutOfBoundsException aioobe) {}
                }
                bw = new BufferedWriter(new FileWriter(this.source));
                for (String l : lines) {
                    bw.write(l + "\n");
                }
                var9_12 = null;
                if (br == null) break block23;
                try {
                    br.close();
                }
                catch (IOException ex) {
                    // empty catch block
                }
            }
            if (bw == null) return;
            try {
                bw.close();
                return;
            }
            catch (IOException ex) {
                return;
            }
            catch (FileNotFoundException ex2) {
                block24 : {
                    ConsoleLogger.showError(ex2.getMessage());
                    var9_13 = null;
                    if (br != null) {
                        ** try [egrp 3[TRYBLOCK] [8 : 208->215)] { 
lbl41: // 1 sources:
                        br.close();
                        break block24;
lbl43: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return;
                ** try [egrp 4[TRYBLOCK] [9 : 221->228)] { 
lbl48: // 1 sources:
                bw.close();
                return;
lbl50: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return;
            }
            catch (IOException ex3) {
                block25 : {
                    ConsoleLogger.showError(ex3.getMessage());
                    var9_14 = null;
                    if (br != null) {
                        ** try [egrp 3[TRYBLOCK] [8 : 208->215)] { 
lbl59: // 1 sources:
                        br.close();
                        break block25;
lbl61: // 1 sources:
                        catch (IOException ex) {
                            // empty catch block
                        }
                    }
                }
                if (bw == null) return;
                ** try [egrp 4[TRYBLOCK] [9 : 221->228)] { 
lbl66: // 1 sources:
                bw.close();
                return;
lbl68: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return;
            }
        }
        catch (Throwable throwable) {
            block26 : {
                var9_15 = null;
                if (br != null) {
                    ** try [egrp 3[TRYBLOCK] [8 : 208->215)] { 
lbl76: // 1 sources:
                    br.close();
                    break block26;
lbl78: // 1 sources:
                    catch (IOException ex) {
                        // empty catch block
                    }
                }
            }
            if (bw == null) throw throwable;
            ** try [egrp 4[TRYBLOCK] [9 : 221->228)] { 
lbl83: // 1 sources:
            bw.close();
            throw throwable;
lbl85: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    @Override
    public DataSource.DataSourceType getType() {
        return DataSource.DataSourceType.FILE;
    }

    @Override
    public boolean isLogged(String user) {
        return PlayersLogs.getInstance().players.contains(user);
    }

    @Override
    public void setLogged(String user) {
        PlayersLogs.getInstance().addPlayer(user);
    }

    @Override
    public void setUnlogged(String user) {
        PlayersLogs.getInstance().removePlayer(user);
    }

    @Override
    public void purgeLogged() {
        PlayersLogs.getInstance().clear();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public int getAccountsRegistered() {
        br = null;
        result = 0;
        try {
            try {
                br = new BufferedReader(new FileReader(this.source));
                while (br.readLine() != null) {
                    ++result;
                }
                var6_3 = null;
                if (br == null) return result;
            }
            catch (Exception ex) {
                ConsoleLogger.showError(ex.getMessage());
                n = result;
                var6_4 = null;
                if (br == null) return n;
                ** try [egrp 2[TRYBLOCK] [4 : 73->80)] { 
lbl18: // 1 sources:
                br.close();
                return n;
lbl20: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return n;
            }
            try {
                br.close();
                return result;
            }
            catch (IOException ex) {
                return result;
            }
        }
        catch (Throwable throwable) {
            var6_5 = null;
            if (br == null) throw throwable;
            ** try [egrp 2[TRYBLOCK] [4 : 73->80)] { 
lbl33: // 1 sources:
            br.close();
            throw throwable;
lbl35: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw throwable;
        }
    }

    @Override
    public void updateName(String oldone, String newone) {
        PlayerAuth auth = this.getAuth(oldone);
        auth.setName(newone);
        this.saveAuth(auth);
        this.removeAuth(oldone);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public List<PlayerAuth> getAllAuths() {
        br = null;
        auths = new ArrayList<PlayerAuth>();
        try {
            br = new BufferedReader(new FileReader(this.source));
            while ((line = br.readLine()) != null) {
                args = line.split(":");
                switch (args.length) {
                    case 2: {
                        auths.add(new PlayerAuth(args[0], args[1], "198.18.0.1", 0, "your@email.com"));
                    }
                    case 3: {
                        auths.add(new PlayerAuth(args[0], args[1], args[2], 0, "your@email.com"));
                    }
                    case 4: {
                        auths.add(new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), "your@email.com"));
                    }
                    case 7: {
                        auths.add(new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), "unavailableworld", "your@email.com"));
                    }
                    case 8: {
                        auths.add(new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], "your@email.com"));
                    }
                    case 9: {
                        auths.add(new PlayerAuth(args[0], args[1], args[2], Long.parseLong(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), args[7], args[8]));
                    }
                }
            }
            var6_9 = null;
            if (br == null) return auths;
            try {
                br.close();
                return auths;
            }
            catch (IOException ex) {
                return auths;
            }
            catch (FileNotFoundException ex) {
                ConsoleLogger.showError(ex.getMessage());
                args = auths;
                var6_10 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 430->437)] { 
lbl36: // 1 sources:
                br.close();
                return args;
lbl38: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
            catch (IOException ex2) {
                ConsoleLogger.showError(ex2.getMessage());
                args = auths;
                var6_11 = null;
                if (br == null) return args;
                ** try [egrp 2[TRYBLOCK] [6 : 430->437)] { 
lbl48: // 1 sources:
                br.close();
                return args;
lbl50: // 1 sources:
                catch (IOException ex) {
                    // empty catch block
                }
                return args;
            }
        }
        catch (Throwable var5_17) {
            var6_12 = null;
            if (br == null) throw var5_17;
            ** try [egrp 2[TRYBLOCK] [6 : 430->437)] { 
lbl58: // 1 sources:
            br.close();
            throw var5_17;
lbl60: // 1 sources:
            catch (IOException ex) {
                // empty catch block
            }
            throw var5_17;
        }
    }
}

