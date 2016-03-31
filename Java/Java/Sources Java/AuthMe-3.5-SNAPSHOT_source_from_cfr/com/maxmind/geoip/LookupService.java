/*
 * Decompiled with CFR 0_110.
 */
package com.maxmind.geoip;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.DatabaseInfo;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.Region;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class LookupService {
    private RandomAccessFile file = null;
    private File databaseFile = null;
    private DatabaseInfo databaseInfo = null;
    byte databaseType = 1;
    int[] databaseSegments;
    int recordLength;
    String licenseKey;
    int dnsService = 0;
    int dboptions;
    byte[] dbbuffer;
    byte[] index_cache;
    long mtime;
    int last_netmask;
    private static final int US_OFFSET = 1;
    private static final int CANADA_OFFSET = 677;
    private static final int WORLD_OFFSET = 1353;
    private static final int FIPS_RANGE = 360;
    private static final int COUNTRY_BEGIN = 16776960;
    private static final int STATE_BEGIN_REV0 = 16700000;
    private static final int STATE_BEGIN_REV1 = 16000000;
    private static final int STRUCTURE_INFO_MAX_SIZE = 20;
    private static final int DATABASE_INFO_MAX_SIZE = 100;
    public static final int GEOIP_STANDARD = 0;
    public static final int GEOIP_MEMORY_CACHE = 1;
    public static final int GEOIP_CHECK_CACHE = 2;
    public static final int GEOIP_INDEX_CACHE = 4;
    public static final int GEOIP_UNKNOWN_SPEED = 0;
    public static final int GEOIP_DIALUP_SPEED = 1;
    public static final int GEOIP_CABLEDSL_SPEED = 2;
    public static final int GEOIP_CORPORATE_SPEED = 3;
    private static final int SEGMENT_RECORD_LENGTH = 3;
    private static final int STANDARD_RECORD_LENGTH = 3;
    private static final int ORG_RECORD_LENGTH = 4;
    private static final int MAX_RECORD_LENGTH = 4;
    private static final int MAX_ORG_RECORD_LENGTH = 300;
    private static final int FULL_RECORD_LENGTH = 60;
    private final Country UNKNOWN_COUNTRY = new Country("--", "N/A");
    private static final HashMap<String, Integer> hashmapcountryCodetoindex = new HashMap(512);
    private static final HashMap<String, Integer> hashmapcountryNametoindex = new HashMap(512);
    private static final String[] countryCode = new String[]{"--", "AP", "EU", "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AN", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BM", "BN", "BO", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "FX", "GA", "GB", "GD", "GE", "GF", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IN", "IO", "IQ", "IR", "IS", "IT", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SV", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TM", "TN", "TO", "TL", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "RS", "ZA", "ZM", "ME", "ZW", "A1", "A2", "O1", "AX", "GG", "IM", "JE", "BL", "MF"};
    private static final String[] countryName = new String[]{"N/A", "Asia/Pacific Region", "Europe", "Andorra", "United Arab Emirates", "Afghanistan", "Antigua and Barbuda", "Anguilla", "Albania", "Armenia", "Netherlands Antilles", "Angola", "Antarctica", "Argentina", "American Samoa", "Austria", "Australia", "Aruba", "Azerbaijan", "Bosnia and Herzegovina", "Barbados", "Bangladesh", "Belgium", "Burkina Faso", "Bulgaria", "Bahrain", "Burundi", "Benin", "Bermuda", "Brunei Darussalam", "Bolivia", "Brazil", "Bahamas", "Bhutan", "Bouvet Island", "Botswana", "Belarus", "Belize", "Canada", "Cocos (Keeling) Islands", "Congo, The Democratic Republic of the", "Central African Republic", "Congo", "Switzerland", "Cote D'Ivoire", "Cook Islands", "Chile", "Cameroon", "China", "Colombia", "Costa Rica", "Cuba", "Cape Verde", "Christmas Island", "Cyprus", "Czech Republic", "Germany", "Djibouti", "Denmark", "Dominica", "Dominican Republic", "Algeria", "Ecuador", "Estonia", "Egypt", "Western Sahara", "Eritrea", "Spain", "Ethiopia", "Finland", "Fiji", "Falkland Islands (Malvinas)", "Micronesia, Federated States of", "Faroe Islands", "France", "France, Metropolitan", "Gabon", "United Kingdom", "Grenada", "Georgia", "French Guiana", "Ghana", "Gibraltar", "Greenland", "Gambia", "Guinea", "Guadeloupe", "Equatorial Guinea", "Greece", "South Georgia and the South Sandwich Islands", "Guatemala", "Guam", "Guinea-Bissau", "Guyana", "Hong Kong", "Heard Island and McDonald Islands", "Honduras", "Croatia", "Haiti", "Hungary", "Indonesia", "Ireland", "Israel", "India", "British Indian Ocean Territory", "Iraq", "Iran, Islamic Republic of", "Iceland", "Italy", "Jamaica", "Jordan", "Japan", "Kenya", "Kyrgyzstan", "Cambodia", "Kiribati", "Comoros", "Saint Kitts and Nevis", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Cayman Islands", "Kazakhstan", "Lao People's Democratic Republic", "Lebanon", "Saint Lucia", "Liechtenstein", "Sri Lanka", "Liberia", "Lesotho", "Lithuania", "Luxembourg", "Latvia", "Libyan Arab Jamahiriya", "Morocco", "Monaco", "Moldova, Republic of", "Madagascar", "Marshall Islands", "Macedonia", "Mali", "Myanmar", "Mongolia", "Macau", "Northern Mariana Islands", "Martinique", "Mauritania", "Montserrat", "Malta", "Mauritius", "Maldives", "Malawi", "Mexico", "Malaysia", "Mozambique", "Namibia", "New Caledonia", "Niger", "Norfolk Island", "Nigeria", "Nicaragua", "Netherlands", "Norway", "Nepal", "Nauru", "Niue", "New Zealand", "Oman", "Panama", "Peru", "French Polynesia", "Papua New Guinea", "Philippines", "Pakistan", "Poland", "Saint Pierre and Miquelon", "Pitcairn Islands", "Puerto Rico", "Palestinian Territory", "Portugal", "Palau", "Paraguay", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saudi Arabia", "Solomon Islands", "Seychelles", "Sudan", "Sweden", "Singapore", "Saint Helena", "Slovenia", "Svalbard and Jan Mayen", "Slovakia", "Sierra Leone", "San Marino", "Senegal", "Somalia", "Suriname", "Sao Tome and Principe", "El Salvador", "Syrian Arab Republic", "Swaziland", "Turks and Caicos Islands", "Chad", "French Southern Territories", "Togo", "Thailand", "Tajikistan", "Tokelau", "Turkmenistan", "Tunisia", "Tonga", "Timor-Leste", "Turkey", "Trinidad and Tobago", "Tuvalu", "Taiwan", "Tanzania, United Republic of", "Ukraine", "Uganda", "United States Minor Outlying Islands", "United States", "Uruguay", "Uzbekistan", "Holy See (Vatican City State)", "Saint Vincent and the Grenadines", "Venezuela", "Virgin Islands, British", "Virgin Islands, U.S.", "Vietnam", "Vanuatu", "Wallis and Futuna", "Samoa", "Yemen", "Mayotte", "Serbia", "South Africa", "Zambia", "Montenegro", "Zimbabwe", "Anonymous Proxy", "Satellite Provider", "Other", "Aland Islands", "Guernsey", "Isle of Man", "Jersey", "Saint Barthelemy", "Saint Martin"};

    public LookupService(String databaseFile, String licenseKey) throws IOException {
        this(new File(databaseFile));
        this.licenseKey = licenseKey;
        this.dnsService = 1;
    }

    public LookupService(File databaseFile, String licenseKey) throws IOException {
        this(databaseFile);
        this.licenseKey = licenseKey;
        this.dnsService = 1;
    }

    public LookupService(int options, String licenseKey) throws IOException {
        this.licenseKey = licenseKey;
        this.dnsService = 1;
        this.init();
    }

    public LookupService(String databaseFile) throws IOException {
        this(new File(databaseFile));
    }

    public LookupService(File databaseFile) throws IOException {
        this.databaseFile = databaseFile;
        this.file = new RandomAccessFile(databaseFile, "r");
        this.init();
    }

    public LookupService(String databaseFile, int options) throws IOException {
        this(new File(databaseFile), options);
    }

    public LookupService(File databaseFile, int options) throws IOException {
        this.databaseFile = databaseFile;
        this.file = new RandomAccessFile(databaseFile, "r");
        this.dboptions = options;
        this.init();
    }

    private void init() throws IOException {
        int l;
        byte[] delim = new byte[3];
        byte[] buf = new byte[3];
        if (this.file == null) {
            return;
        }
        if ((this.dboptions & 2) != 0) {
            this.mtime = this.databaseFile.lastModified();
        }
        this.file.seek(this.file.length() - 3);
        for (int i = 0; i < 20; ++i) {
            this.file.readFully(delim);
            if (delim[0] == -1 && delim[1] == -1 && delim[2] == -1) {
                this.databaseType = this.file.readByte();
                if (this.databaseType >= 106) {
                    this.databaseType = (byte)(this.databaseType - 105);
                }
                if (this.databaseType == 7) {
                    this.databaseSegments = new int[1];
                    this.databaseSegments[0] = 16700000;
                    this.recordLength = 3;
                    break;
                }
                if (this.databaseType == 3) {
                    this.databaseSegments = new int[1];
                    this.databaseSegments[0] = 16000000;
                    this.recordLength = 3;
                    break;
                }
                if (this.databaseType != 6 && this.databaseType != 2 && this.databaseType != 5 && this.databaseType != 4 && this.databaseType != 9) break;
                this.databaseSegments = new int[1];
                this.databaseSegments[0] = 0;
                this.recordLength = this.databaseType == 6 || this.databaseType == 2 || this.databaseType == 9 ? 3 : 4;
                this.file.readFully(buf);
                for (int j = 0; j < 3; ++j) {
                    int[] arrn = this.databaseSegments;
                    arrn[0] = arrn[0] + (LookupService.unsignedByteToInt(buf[j]) << j * 8);
                }
                break;
            }
            this.file.seek(this.file.getFilePointer() - 4);
        }
        if (this.databaseType == 1 || this.databaseType == 12 || this.databaseType == 8 || this.databaseType == 10) {
            this.databaseSegments = new int[1];
            this.databaseSegments[0] = 16776960;
            this.recordLength = 3;
        }
        if ((this.dboptions & 1) == 1) {
            l = (int)this.file.length();
            this.dbbuffer = new byte[l];
            this.file.seek(0);
            this.file.readFully(this.dbbuffer, 0, l);
            this.databaseInfo = this.getDatabaseInfo();
            this.file.close();
        }
        if ((this.dboptions & 4) != 0) {
            l = this.databaseSegments[0] * this.recordLength * 2;
            this.index_cache = new byte[l];
            if (this.index_cache != null) {
                this.file.seek(0);
                this.file.readFully(this.index_cache, 0, l);
            }
        } else {
            this.index_cache = null;
        }
    }

    public void close() {
        try {
            if (this.file != null) {
                this.file.close();
            }
            this.file = null;
        }
        catch (Exception e) {
            // empty catch block
        }
    }

    public Country getCountryV6(String ipAddress) {
        InetAddress addr;
        try {
            addr = Inet6Address.getByName(ipAddress);
        }
        catch (UnknownHostException e) {
            return this.UNKNOWN_COUNTRY;
        }
        return this.getCountryV6(addr);
    }

    public Country getCountry(String ipAddress) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(ipAddress);
        }
        catch (UnknownHostException e) {
            return this.UNKNOWN_COUNTRY;
        }
        return this.getCountry(LookupService.bytesToLong(addr.getAddress()));
    }

    public synchronized Country getCountry(InetAddress ipAddress) {
        return this.getCountry(LookupService.bytesToLong(ipAddress.getAddress()));
    }

    public Country getCountryV6(InetAddress addr) {
        if (this.file == null && (this.dboptions & 1) == 0) {
            throw new IllegalStateException("Database has been closed.");
        }
        int ret = this.seekCountryV6(addr) - 16776960;
        if (ret == 0) {
            return this.UNKNOWN_COUNTRY;
        }
        return new Country(countryCode[ret], countryName[ret]);
    }

    public Country getCountry(long ipAddress) {
        if (this.file == null && (this.dboptions & 1) == 0) {
            throw new IllegalStateException("Database has been closed.");
        }
        int ret = this.seekCountry(ipAddress) - 16776960;
        if (ret == 0) {
            return this.UNKNOWN_COUNTRY;
        }
        return new Country(countryCode[ret], countryName[ret]);
    }

    public int getID(String ipAddress) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(ipAddress);
        }
        catch (UnknownHostException e) {
            return 0;
        }
        return this.getID(LookupService.bytesToLong(addr.getAddress()));
    }

    public int getID(InetAddress ipAddress) {
        return this.getID(LookupService.bytesToLong(ipAddress.getAddress()));
    }

    public synchronized int getID(long ipAddress) {
        if (this.file == null && (this.dboptions & 1) == 0) {
            throw new IllegalStateException("Database has been closed.");
        }
        int ret = this.seekCountry(ipAddress) - this.databaseSegments[0];
        return ret;
    }

    public int last_netmask() {
        return this.last_netmask;
    }

    public void netmask(int nm) {
        this.last_netmask = nm;
    }

    public synchronized DatabaseInfo getDatabaseInfo() {
        if (this.databaseInfo != null) {
            return this.databaseInfo;
        }
        try {
            int i;
            this._check_mtime();
            boolean hasStructureInfo = false;
            byte[] delim = new byte[3];
            this.file.seek(this.file.length() - 3);
            for (i = 0; i < 20; ++i) {
                int read = this.file.read(delim);
                if (read == 3 && (delim[0] & 255) == 255 && (delim[1] & 255) == 255 && (delim[2] & 255) == 255) {
                    hasStructureInfo = true;
                    break;
                }
                this.file.seek(this.file.getFilePointer() - 4);
            }
            if (hasStructureInfo) {
                this.file.seek(this.file.getFilePointer() - 6);
            } else {
                this.file.seek(this.file.length() - 3);
            }
            for (i = 0; i < 100; ++i) {
                this.file.readFully(delim);
                if (delim[0] == 0 && delim[1] == 0 && delim[2] == 0) {
                    byte[] dbInfo = new byte[i];
                    this.file.readFully(dbInfo);
                    this.databaseInfo = new DatabaseInfo(new String(dbInfo));
                    return this.databaseInfo;
                }
                this.file.seek(this.file.getFilePointer() - 4);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new DatabaseInfo("");
    }

    synchronized void _check_mtime() {
        try {
            long t;
            if ((this.dboptions & 2) != 0 && (t = this.databaseFile.lastModified()) != this.mtime) {
                this.file.close();
                this.file = new RandomAccessFile(this.databaseFile, "r");
                this.databaseInfo = null;
                this.init();
            }
        }
        catch (IOException e) {
            System.out.println("file not found");
        }
    }

    public Location getLocation(InetAddress addr) {
        return this.getLocation(LookupService.bytesToLong(addr.getAddress()));
    }

    public Location getLocation(String str) {
        if (this.dnsService == 0) {
            InetAddress addr;
            try {
                addr = InetAddress.getByName(str);
            }
            catch (UnknownHostException e) {
                return null;
            }
            return this.getLocation(addr);
        }
        String str2 = this.getDnsAttributes(str);
        return this.getLocationwithdnsservice(str2);
    }

    String getDnsAttributes(String ip) {
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            env.put("java.naming.provider.url", "dns://ws1.maxmind.com/");
            InitialDirContext ictx = new InitialDirContext(env);
            Attributes attrs = ictx.getAttributes(this.licenseKey + "." + ip + ".s.maxmind.com", new String[]{"txt"});
            String str = attrs.get("txt").get().toString();
            return str;
        }
        catch (NamingException e) {
            System.out.println("DNS error");
            return null;
        }
    }

    public Location getLocationwithdnsservice(String str) {
        Location record = new Location();
        StringTokenizer st = new StringTokenizer(str, ";=\"");
        while (st.hasMoreTokens()) {
            String key = st.nextToken();
            String value = st.hasMoreTokens() ? st.nextToken() : "";
            if (key.equals("co")) {
                Integer i = hashmapcountryCodetoindex.get(value);
                record.countryCode = value;
                record.countryName = countryName[i];
            }
            if (key.equals("ci")) {
                record.city = value;
            }
            if (key.equals("re")) {
                record.region = value;
            }
            if (key.equals("zi")) {
                record.postalCode = value;
            }
            if (key.equals("la")) {
                try {
                    record.latitude = Float.parseFloat(value);
                }
                catch (NumberFormatException e) {
                    record.latitude = 0.0f;
                }
            }
            if (key.equals("lo")) {
                try {
                    record.longitude = Float.parseFloat(value);
                }
                catch (NumberFormatException e) {
                    record.latitude = 0.0f;
                }
            }
            if (key.equals("dm") || key.equals("me")) {
                try {
                    record.metro_code = record.dma_code = Integer.parseInt(value);
                }
                catch (NumberFormatException e) {
                    record.dma_code = 0;
                    record.metro_code = 0;
                }
            }
            if (!key.equals("ac")) continue;
            try {
                record.area_code = Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                record.area_code = 0;
            }
        }
        return record;
    }

    public synchronized Region getRegion(String str) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(str);
        }
        catch (UnknownHostException e) {
            return null;
        }
        return this.getRegion(LookupService.bytesToLong(addr.getAddress()));
    }

    public synchronized Region getRegion(long ipnum) {
        Region record = new Region();
        int seek_region = 0;
        if (this.databaseType == 7) {
            seek_region = this.seekCountry(ipnum) - 16700000;
            char[] ch = new char[2];
            if (seek_region >= 1000) {
                record.countryCode = "US";
                record.countryName = "United States";
                ch[0] = (char)((seek_region - 1000) / 26 + 65);
                ch[1] = (char)((seek_region - 1000) % 26 + 65);
                record.region = new String(ch);
            } else {
                record.countryCode = countryCode[seek_region];
                record.countryName = countryName[seek_region];
                record.region = "";
            }
        } else if (this.databaseType == 3) {
            seek_region = this.seekCountry(ipnum) - 16000000;
            char[] ch = new char[2];
            if (seek_region < 1) {
                record.countryCode = "";
                record.countryName = "";
                record.region = "";
            } else if (seek_region < 677) {
                record.countryCode = "US";
                record.countryName = "United States";
                ch[0] = (char)((seek_region - 1) / 26 + 65);
                ch[1] = (char)((seek_region - 1) % 26 + 65);
                record.region = new String(ch);
            } else if (seek_region < 1353) {
                record.countryCode = "CA";
                record.countryName = "Canada";
                ch[0] = (char)((seek_region - 677) / 26 + 65);
                ch[1] = (char)((seek_region - 677) % 26 + 65);
                record.region = new String(ch);
            } else {
                record.countryCode = countryCode[(seek_region - 1353) / 360];
                record.countryName = countryName[(seek_region - 1353) / 360];
                record.region = "";
            }
        }
        return record;
    }

    public synchronized Location getLocation(long ipnum) {
        byte[] record_buf = new byte[60];
        int record_buf_offset = 0;
        Location record = new Location();
        int str_length = 0;
        double latitude = 0.0;
        double longitude = 0.0;
        try {
            int j;
            int seek_country = this.seekCountry(ipnum);
            if (seek_country == this.databaseSegments[0]) {
                return null;
            }
            int record_pointer = seek_country + (2 * this.recordLength - 1) * this.databaseSegments[0];
            if ((this.dboptions & 1) == 1) {
                System.arraycopy(this.dbbuffer, record_pointer, record_buf, 0, Math.min(this.dbbuffer.length - record_pointer, 60));
            } else {
                this.file.seek(record_pointer);
                this.file.readFully(record_buf);
            }
            record.countryCode = countryCode[LookupService.unsignedByteToInt(record_buf[0])];
            record.countryName = countryName[LookupService.unsignedByteToInt(record_buf[0])];
            while (record_buf[++record_buf_offset + str_length] != 0) {
                ++str_length;
            }
            if (str_length > 0) {
                record.region = new String(record_buf, record_buf_offset, str_length);
            }
            str_length = 0;
            while (record_buf[(record_buf_offset += str_length + 1) + str_length] != 0) {
                ++str_length;
            }
            if (str_length > 0) {
                record.city = new String(record_buf, record_buf_offset, str_length, "ISO-8859-1");
            }
            str_length = 0;
            while (record_buf[(record_buf_offset += str_length + 1) + str_length] != 0) {
                ++str_length;
            }
            if (str_length > 0) {
                record.postalCode = new String(record_buf, record_buf_offset, str_length);
            }
            record_buf_offset += str_length + 1;
            for (j = 0; j < 3; ++j) {
                latitude += (double)(LookupService.unsignedByteToInt(record_buf[record_buf_offset + j]) << j * 8);
            }
            record.latitude = (float)latitude / 10000.0f - 180.0f;
            record_buf_offset += 3;
            for (j = 0; j < 3; ++j) {
                longitude += (double)(LookupService.unsignedByteToInt(record_buf[record_buf_offset + j]) << j * 8);
            }
            record.longitude = (float)longitude / 10000.0f - 180.0f;
            record.metro_code = 0;
            record.dma_code = 0;
            record.area_code = 0;
            if (this.databaseType == 2) {
                int metroarea_combo = 0;
                if (record.countryCode == "US") {
                    record_buf_offset += 3;
                    for (j = 0; j < 3; ++j) {
                        metroarea_combo += LookupService.unsignedByteToInt(record_buf[record_buf_offset + j]) << j * 8;
                    }
                    record.metro_code = record.dma_code = metroarea_combo / 1000;
                    record.area_code = metroarea_combo % 1000;
                }
            }
        }
        catch (IOException e) {
            System.err.println("IO Exception while setting up segments");
        }
        return record;
    }

    public String getOrg(InetAddress addr) {
        return this.getOrg(LookupService.bytesToLong(addr.getAddress()));
    }

    public String getOrg(String str) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(str);
        }
        catch (UnknownHostException e) {
            return null;
        }
        return this.getOrg(addr);
    }

    public synchronized String getOrg(long ipnum) {
        int str_length = 0;
        byte[] buf = new byte[300];
        try {
            int seek_org = this.seekCountry(ipnum);
            if (seek_org == this.databaseSegments[0]) {
                return null;
            }
            int record_pointer = seek_org + (2 * this.recordLength - 1) * this.databaseSegments[0];
            if ((this.dboptions & 1) == 1) {
                System.arraycopy(this.dbbuffer, record_pointer, buf, 0, Math.min(this.dbbuffer.length - record_pointer, 300));
            } else {
                this.file.seek(record_pointer);
                this.file.readFully(buf);
            }
            while (buf[str_length] != 0) {
                ++str_length;
            }
            String org_buf = new String(buf, 0, str_length, "ISO-8859-1");
            return org_buf;
        }
        catch (IOException e) {
            System.out.println("IO Exception");
            return null;
        }
    }

    private synchronized int seekCountryV6(InetAddress addr) {
        byte[] v6vec = addr.getAddress();
        byte[] buf = new byte[8];
        int[] x = new int[2];
        int offset = 0;
        this._check_mtime();
        for (int depth = 127; depth >= 0; --depth) {
            int i;
            if ((this.dboptions & 1) == 1) {
                for (i = 0; i < 8; ++i) {
                    buf[i] = this.dbbuffer[2 * this.recordLength * offset + i];
                }
            } else if ((this.dboptions & 4) != 0) {
                for (i = 0; i < 8; ++i) {
                    buf[i] = this.index_cache[2 * this.recordLength * offset + i];
                }
            } else {
                try {
                    this.file.seek(2 * this.recordLength * offset);
                    this.file.readFully(buf);
                }
                catch (IOException e) {
                    System.out.println("IO Exception");
                }
            }
            for (i = 0; i < 2; ++i) {
                x[i] = 0;
                for (int j = 0; j < this.recordLength; ++j) {
                    int y = buf[i * this.recordLength + j];
                    if (y < 0) {
                        y += 256;
                    }
                    int[] arrn = x;
                    int n = i;
                    arrn[n] = arrn[n] + (y << j * 8);
                }
            }
            int bnum = 127 - depth;
            int idx = bnum >> 3;
            int b_mask = 1 << (bnum & 7 ^ 7);
            if ((v6vec[idx] & b_mask) > 0) {
                if (x[1] >= this.databaseSegments[0]) {
                    this.last_netmask = 128 - depth;
                    return x[1];
                }
                offset = x[1];
                continue;
            }
            if (x[0] >= this.databaseSegments[0]) {
                this.last_netmask = 128 - depth;
                return x[0];
            }
            offset = x[0];
        }
        System.err.println("Error seeking country while seeking " + addr.getHostAddress());
        return 0;
    }

    private synchronized int seekCountry(long ipAddress) {
        byte[] buf = new byte[8];
        int[] x = new int[2];
        int offset = 0;
        this._check_mtime();
        for (int depth = 31; depth >= 0; --depth) {
            int i;
            if ((this.dboptions & 1) == 1) {
                for (i = 0; i < 8; ++i) {
                    buf[i] = this.dbbuffer[2 * this.recordLength * offset + i];
                }
            } else if ((this.dboptions & 4) != 0) {
                for (i = 0; i < 8; ++i) {
                    buf[i] = this.index_cache[2 * this.recordLength * offset + i];
                }
            } else {
                try {
                    this.file.seek(2 * this.recordLength * offset);
                    this.file.readFully(buf);
                }
                catch (IOException e) {
                    System.out.println("IO Exception");
                }
            }
            for (i = 0; i < 2; ++i) {
                x[i] = 0;
                for (int j = 0; j < this.recordLength; ++j) {
                    int y = buf[i * this.recordLength + j];
                    if (y < 0) {
                        y += 256;
                    }
                    int[] arrn = x;
                    int n = i;
                    arrn[n] = arrn[n] + (y << j * 8);
                }
            }
            if ((ipAddress & (long)(1 << depth)) > 0) {
                if (x[1] >= this.databaseSegments[0]) {
                    this.last_netmask = 32 - depth;
                    return x[1];
                }
                offset = x[1];
                continue;
            }
            if (x[0] >= this.databaseSegments[0]) {
                this.last_netmask = 32 - depth;
                return x[0];
            }
            offset = x[0];
        }
        System.err.println("Error seeking country while seeking " + ipAddress);
        return 0;
    }

    private static long bytesToLong(byte[] address) {
        long ipnum = 0;
        for (int i = 0; i < 4; ++i) {
            long y = address[i];
            if (y < 0) {
                y += 256;
            }
            ipnum += y << (3 - i) * 8;
        }
        return ipnum;
    }

    private static int unsignedByteToInt(byte b) {
        return b & 255;
    }

    static {
        if (countryCode.length != countryName.length) {
            throw new AssertionError((Object)"countryCode.length!=countryName.length");
        }
        for (int i = 0; i < countryCode.length; ++i) {
            hashmapcountryCodetoindex.put(countryCode[i], i);
            hashmapcountryNametoindex.put(countryName[i], i);
        }
    }
}

