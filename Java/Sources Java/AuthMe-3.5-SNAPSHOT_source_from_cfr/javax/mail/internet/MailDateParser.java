/*
 * Decompiled with CFR 0_110.
 */
package javax.mail.internet;

import java.text.ParseException;

class MailDateParser {
    int index = 0;
    char[] orig = null;

    public MailDateParser(char[] orig, int index) {
        this.orig = orig;
        this.index = index;
    }

    public void skipUntilNumber() throws ParseException {
        try {
            do {
                switch (this.orig[this.index]) {
                    case '0': 
                    case '1': 
                    case '2': 
                    case '3': 
                    case '4': 
                    case '5': 
                    case '6': 
                    case '7': 
                    case '8': 
                    case '9': {
                        return;
                    }
                }
                ++this.index;
            } while (true);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new ParseException("No Number Found", this.index);
        }
    }

    public void skipWhiteSpace() {
        int len = this.orig.length;
        block3 : while (this.index < len) {
            switch (this.orig[this.index]) {
                case '\t': 
                case '\n': 
                case '\r': 
                case ' ': {
                    ++this.index;
                    continue block3;
                }
            }
            return;
        }
    }

    public int peekChar() throws ParseException {
        if (this.index < this.orig.length) {
            return this.orig[this.index];
        }
        throw new ParseException("No more characters", this.index);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void skipChar(char c) throws ParseException {
        if (this.index >= this.orig.length) throw new ParseException("No more characters", this.index);
        if (this.orig[this.index] != c) throw new ParseException("Wrong char", this.index);
        ++this.index;
    }

    public boolean skipIfChar(char c) throws ParseException {
        if (this.index < this.orig.length) {
            if (this.orig[this.index] == c) {
                ++this.index;
                return true;
            }
            return false;
        }
        throw new ParseException("No more characters", this.index);
    }

    public int parseNumber() throws ParseException {
        int length = this.orig.length;
        boolean gotNum = false;
        int result = 0;
        while (this.index < length) {
            switch (this.orig[this.index]) {
                case '0': {
                    result *= 10;
                    gotNum = true;
                    break;
                }
                case '1': {
                    result = result * 10 + 1;
                    gotNum = true;
                    break;
                }
                case '2': {
                    result = result * 10 + 2;
                    gotNum = true;
                    break;
                }
                case '3': {
                    result = result * 10 + 3;
                    gotNum = true;
                    break;
                }
                case '4': {
                    result = result * 10 + 4;
                    gotNum = true;
                    break;
                }
                case '5': {
                    result = result * 10 + 5;
                    gotNum = true;
                    break;
                }
                case '6': {
                    result = result * 10 + 6;
                    gotNum = true;
                    break;
                }
                case '7': {
                    result = result * 10 + 7;
                    gotNum = true;
                    break;
                }
                case '8': {
                    result = result * 10 + 8;
                    gotNum = true;
                    break;
                }
                case '9': {
                    result = result * 10 + 9;
                    gotNum = true;
                    break;
                }
                default: {
                    if (gotNum) {
                        return result;
                    }
                    throw new ParseException("No Number found", this.index);
                }
            }
            ++this.index;
        }
        if (gotNum) {
            return result;
        }
        throw new ParseException("No Number found", this.index);
    }

    public int parseMonth() throws ParseException {
        try {
            switch (this.orig[this.index++]) {
                case 'J': 
                case 'j': {
                    switch (this.orig[this.index++]) {
                        case 'A': 
                        case 'a': {
                            char curr = this.orig[this.index++];
                            if (curr != 'N' && curr != 'n') break;
                            return 0;
                        }
                        case 'U': 
                        case 'u': {
                            char curr = this.orig[this.index++];
                            if (curr == 'N' || curr == 'n') {
                                return 5;
                            }
                            if (curr != 'L' && curr != 'l') break;
                            return 6;
                        }
                    }
                    break;
                }
                case 'F': 
                case 'f': {
                    char curr = this.orig[this.index++];
                    if (curr != 'E' && curr != 'e' || (curr = this.orig[this.index++]) != 'B' && curr != 'b') break;
                    return 1;
                }
                case 'M': 
                case 'm': {
                    char curr = this.orig[this.index++];
                    if (curr != 'A' && curr != 'a') break;
                    if ((curr = this.orig[this.index++]) == 'R' || curr == 'r') {
                        return 2;
                    }
                    if (curr != 'Y' && curr != 'y') break;
                    return 4;
                }
                case 'A': 
                case 'a': {
                    char curr = this.orig[this.index++];
                    if (curr == 'P' || curr == 'p') {
                        if ((curr = this.orig[this.index++]) != 'R' && curr != 'r') break;
                        return 3;
                    }
                    if (curr != 'U' && curr != 'u' || (curr = this.orig[this.index++]) != 'G' && curr != 'g') break;
                    return 7;
                }
                case 'S': 
                case 's': {
                    char curr = this.orig[this.index++];
                    if (curr != 'E' && curr != 'e' || (curr = this.orig[this.index++]) != 'P' && curr != 'p') break;
                    return 8;
                }
                case 'O': 
                case 'o': {
                    char curr = this.orig[this.index++];
                    if (curr != 'C' && curr != 'c' || (curr = this.orig[this.index++]) != 'T' && curr != 't') break;
                    return 9;
                }
                case 'N': 
                case 'n': {
                    char curr = this.orig[this.index++];
                    if (curr != 'O' && curr != 'o' || (curr = this.orig[this.index++]) != 'V' && curr != 'v') break;
                    return 10;
                }
                case 'D': 
                case 'd': {
                    char curr = this.orig[this.index++];
                    if (curr != 'E' && curr != 'e' || (curr = this.orig[this.index++]) != 'C' && curr != 'c') break;
                    return 11;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // empty catch block
        }
        throw new ParseException("Bad Month", this.index);
    }

    public int parseTimeZone() throws ParseException {
        if (this.index >= this.orig.length) {
            throw new ParseException("No more characters", this.index);
        }
        char test = this.orig[this.index];
        if (test == '+' || test == '-') {
            return this.parseNumericTimeZone();
        }
        return this.parseAlphaTimeZone();
    }

    public int parseNumericTimeZone() throws ParseException {
        char first;
        boolean switchSign = false;
        if ((first = this.orig[this.index++]) == '+') {
            switchSign = true;
        } else if (first != '-') {
            throw new ParseException("Bad Numeric TimeZone", this.index);
        }
        int oindex = this.index;
        int tz = this.parseNumber();
        if (tz >= 2400) {
            throw new ParseException("Numeric TimeZone out of range", oindex);
        }
        int offset = tz / 100 * 60 + tz % 100;
        if (switchSign) {
            return - offset;
        }
        return offset;
    }

    public int parseAlphaTimeZone() throws ParseException {
        char curr;
        boolean foundCommon;
        int result;
        block19 : {
            result = 0;
            foundCommon = false;
            try {
                switch (this.orig[this.index++]) {
                    case 'U': 
                    case 'u': {
                        curr = this.orig[this.index++];
                        if (curr == 'T' || curr == 't') {
                            result = 0;
                            break block19;
                        }
                        throw new ParseException("Bad Alpha TimeZone", this.index);
                    }
                    case 'G': 
                    case 'g': {
                        curr = this.orig[this.index++];
                        if (!(curr != 'M' && curr != 'm' || (curr = this.orig[this.index++]) != 'T' && curr != 't')) {
                            result = 0;
                            break block19;
                        }
                        throw new ParseException("Bad Alpha TimeZone", this.index);
                    }
                    case 'E': 
                    case 'e': {
                        result = 300;
                        foundCommon = true;
                        break block19;
                    }
                    case 'C': 
                    case 'c': {
                        result = 360;
                        foundCommon = true;
                        break block19;
                    }
                    case 'M': 
                    case 'm': {
                        result = 420;
                        foundCommon = true;
                        break block19;
                    }
                    case 'P': 
                    case 'p': {
                        result = 480;
                        foundCommon = true;
                        break block19;
                    }
                }
                throw new ParseException("Bad Alpha TimeZone", this.index);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseException("Bad Alpha TimeZone", this.index);
            }
        }
        if (foundCommon) {
            if ((curr = this.orig[this.index++]) == 'S' || curr == 's') {
                if ((curr = this.orig[this.index++]) != 'T' && curr != 't') {
                    throw new ParseException("Bad Alpha TimeZone", this.index);
                }
            } else if (curr == 'D' || curr == 'd') {
                if ((curr = this.orig[this.index++]) == 'T' || curr != 't') {
                    result -= 60;
                } else {
                    throw new ParseException("Bad Alpha TimeZone", this.index);
                }
            }
        }
        return result;
    }

    int getIndex() {
        return this.index;
    }
}

