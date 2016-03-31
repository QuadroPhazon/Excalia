/*
 * Decompiled with CFR 0_110.
 */
package com.maxmind.geoip;

public class regionName {
    public static String regionNameByCode(String country_code, String region_code) {
        void var3_3;
        String string;
        String string2;
        String name = null;
        int region_code2 = -1;
        if (region_code == null) {
            return null;
        }
        if (region_code.equals("")) {
            return null;
        }
        if (region_code.charAt(0) >= '0' && region_code.charAt(0) < ':' && region_code.charAt(1) >= '0' && region_code.charAt(1) < ':') {
            region_code2 = (region_code.charAt(0) - 48) * 10 + region_code.charAt(1) - 48;
        } else if ((region_code.charAt(0) >= 'A' && region_code.charAt(0) < '[' || region_code.charAt(0) >= '0' && region_code.charAt(0) < ':') && (region_code.charAt(1) >= 'A' && region_code.charAt(1) < '[' || region_code.charAt(1) >= '0' && region_code.charAt(1) < ':')) {
            region_code2 = (region_code.charAt(0) - 48) * 43 + region_code.charAt(1) - 48 + 100;
        }
        if (region_code2 == -1) {
            return null;
        }
        if (country_code.equals("CA")) {
            switch (region_code2) {
                case 849: {
                    name = "Alberta";
                    break;
                }
                case 893: {
                    name = "British Columbia";
                    break;
                }
                case 1365: {
                    name = "Manitoba";
                    break;
                }
                case 1408: {
                    name = "New Brunswick";
                    break;
                }
                case 1418: {
                    name = "Newfoundland";
                    break;
                }
                case 1425: {
                    name = "Nova Scotia";
                    break;
                }
                case 1427: {
                    name = "Nunavut";
                    break;
                }
                case 1463: {
                    name = "Ontario";
                    break;
                }
                case 1497: {
                    name = "Prince Edward Island";
                    break;
                }
                case 1538: {
                    name = "Quebec";
                    break;
                }
                case 1632: {
                    name = "Saskatchewan";
                    break;
                }
                case 1426: {
                    name = "Northwest Territories";
                    break;
                }
                case 1899: {
                    name = "Yukon Territory";
                }
            }
        }
        if (country_code.equals("US")) {
            switch (region_code2) {
                case 848: {
                    name = "Armed Forces Americas";
                    break;
                }
                case 852: {
                    name = "Armed Forces Europe";
                    break;
                }
                case 858: {
                    name = "Alaska";
                    break;
                }
                case 859: {
                    name = "Alabama";
                    break;
                }
                case 863: {
                    name = "Armed Forces Pacific";
                    break;
                }
                case 865: {
                    name = "Arkansas";
                    break;
                }
                case 866: {
                    name = "American Samoa";
                    break;
                }
                case 873: {
                    name = "Arizona";
                    break;
                }
                case 934: {
                    name = "California";
                    break;
                }
                case 948: {
                    name = "Colorado";
                    break;
                }
                case 953: {
                    name = "Connecticut";
                    break;
                }
                case 979: {
                    name = "District of Columbia";
                    break;
                }
                case 981: {
                    name = "Delaware";
                    break;
                }
                case 1074: {
                    name = "Florida";
                    break;
                }
                case 1075: {
                    name = "Federated States of Micronesia";
                    break;
                }
                case 1106: {
                    name = "Georgia";
                    break;
                }
                case 1126: {
                    name = "Guam";
                    break;
                }
                case 1157: {
                    name = "Hawaii";
                    break;
                }
                case 1192: {
                    name = "Iowa";
                    break;
                }
                case 1195: {
                    name = "Idaho";
                    break;
                }
                case 1203: {
                    name = "Illinois";
                    break;
                }
                case 1205: {
                    name = "Indiana";
                    break;
                }
                case 1296: {
                    name = "Kansas";
                    break;
                }
                case 1302: {
                    name = "Kentucky";
                    break;
                }
                case 1321: {
                    name = "Louisiana";
                    break;
                }
                case 1364: {
                    name = "Massachusetts";
                    break;
                }
                case 1367: {
                    name = "Maryland";
                    break;
                }
                case 1368: {
                    name = "Maine";
                    break;
                }
                case 1371: {
                    name = "Marshall Islands";
                    break;
                }
                case 1372: {
                    name = "Michigan";
                    break;
                }
                case 1377: {
                    name = "Minnesota";
                    break;
                }
                case 1378: {
                    name = "Missouri";
                    break;
                }
                case 1379: {
                    name = "Northern Mariana Islands";
                    break;
                }
                case 1382: {
                    name = "Mississippi";
                    break;
                }
                case 1383: {
                    name = "Montana";
                    break;
                }
                case 1409: {
                    name = "North Carolina";
                    break;
                }
                case 1410: {
                    name = "North Dakota";
                    break;
                }
                case 1411: {
                    name = "Nebraska";
                    break;
                }
                case 1414: {
                    name = "New Hampshire";
                    break;
                }
                case 1416: {
                    name = "New Jersey";
                    break;
                }
                case 1419: {
                    name = "New Mexico";
                    break;
                }
                case 1428: {
                    name = "Nevada";
                    break;
                }
                case 1431: {
                    name = "New York";
                    break;
                }
                case 1457: {
                    name = "Ohio";
                    break;
                }
                case 1460: {
                    name = "Oklahoma";
                    break;
                }
                case 1467: {
                    name = "Oregon";
                    break;
                }
                case 1493: {
                    name = "Pennsylvania";
                    break;
                }
                case 1510: {
                    name = "Puerto Rico";
                    break;
                }
                case 1515: {
                    name = "Palau";
                    break;
                }
                case 1587: {
                    name = "Rhode Island";
                    break;
                }
                case 1624: {
                    name = "South Carolina";
                    break;
                }
                case 1625: {
                    name = "South Dakota";
                    break;
                }
                case 1678: {
                    name = "Tennessee";
                    break;
                }
                case 1688: {
                    name = "Texas";
                    break;
                }
                case 1727: {
                    name = "Utah";
                    break;
                }
                case 1751: {
                    name = "Virginia";
                    break;
                }
                case 1759: {
                    name = "Virgin Islands";
                    break;
                }
                case 1770: {
                    name = "Vermont";
                    break;
                }
                case 1794: {
                    name = "Washington";
                    break;
                }
                case 1815: {
                    name = "West Virginia";
                    break;
                }
                case 1802: {
                    name = "Wisconsin";
                    break;
                }
                case 1818: {
                    name = "Wyoming";
                }
            }
        }
        if (country_code.equals("AD")) {
            switch (region_code2) {
                case 2: {
                    name = "Canillo";
                    break;
                }
                case 3: {
                    name = "Encamp";
                    break;
                }
                case 4: {
                    name = "La Massana";
                    break;
                }
                case 5: {
                    name = "Ordino";
                    break;
                }
                case 6: {
                    name = "Sant Julia de Loria";
                    break;
                }
                case 7: {
                    name = "Andorra la Vella";
                    break;
                }
                case 8: {
                    name = "Escaldes-Engordany";
                }
            }
        }
        if (country_code.equals("AE")) {
            switch (region_code2) {
                case 1: {
                    name = "Abu Dhabi";
                    break;
                }
                case 2: {
                    name = "Ajman";
                    break;
                }
                case 3: {
                    name = "Dubai";
                    break;
                }
                case 4: {
                    name = "Fujairah";
                    break;
                }
                case 5: {
                    name = "Ras Al Khaimah";
                    break;
                }
                case 6: {
                    name = "Sharjah";
                    break;
                }
                case 7: {
                    name = "Umm Al Quwain";
                }
            }
        }
        if (country_code.equals("AF")) {
            switch (region_code2) {
                case 1: {
                    name = "Badakhshan";
                    break;
                }
                case 2: {
                    name = "Badghis";
                    break;
                }
                case 3: {
                    name = "Baghlan";
                    break;
                }
                case 5: {
                    name = "Bamian";
                    break;
                }
                case 6: {
                    name = "Farah";
                    break;
                }
                case 7: {
                    name = "Faryab";
                    break;
                }
                case 8: {
                    name = "Ghazni";
                    break;
                }
                case 9: {
                    name = "Ghowr";
                    break;
                }
                case 10: {
                    name = "Helmand";
                    break;
                }
                case 11: {
                    name = "Herat";
                    break;
                }
                case 13: {
                    name = "Kabol";
                    break;
                }
                case 14: {
                    name = "Kapisa";
                    break;
                }
                case 15: {
                    name = "Konar";
                    break;
                }
                case 16: {
                    name = "Laghman";
                    break;
                }
                case 17: {
                    name = "Lowgar";
                    break;
                }
                case 18: {
                    name = "Nangarhar";
                    break;
                }
                case 19: {
                    name = "Nimruz";
                    break;
                }
                case 21: {
                    name = "Paktia";
                    break;
                }
                case 22: {
                    name = "Parvan";
                    break;
                }
                case 23: {
                    name = "Kandahar";
                    break;
                }
                case 24: {
                    name = "Kondoz";
                    break;
                }
                case 26: {
                    name = "Takhar";
                    break;
                }
                case 27: {
                    name = "Vardak";
                    break;
                }
                case 28: {
                    name = "Zabol";
                    break;
                }
                case 29: {
                    name = "Paktika";
                    break;
                }
                case 30: {
                    name = "Balkh";
                    break;
                }
                case 31: {
                    name = "Jowzjan";
                    break;
                }
                case 32: {
                    name = "Samangan";
                    break;
                }
                case 33: {
                    name = "Sar-e Pol";
                    break;
                }
                case 34: {
                    name = "Konar";
                    break;
                }
                case 35: {
                    name = "Laghman";
                    break;
                }
                case 36: {
                    name = "Paktia";
                    break;
                }
                case 37: {
                    name = "Khowst";
                    break;
                }
                case 38: {
                    name = "Nurestan";
                    break;
                }
                case 39: {
                    name = "Oruzgan";
                    break;
                }
                case 40: {
                    name = "Parvan";
                    break;
                }
                case 41: {
                    name = "Daykondi";
                    break;
                }
                case 42: {
                    name = "Panjshir";
                }
            }
        }
        if (country_code.equals("AG")) {
            switch (region_code2) {
                case 1: {
                    name = "Barbuda";
                    break;
                }
                case 3: {
                    name = "Saint George";
                    break;
                }
                case 4: {
                    name = "Saint John";
                    break;
                }
                case 5: {
                    name = "Saint Mary";
                    break;
                }
                case 6: {
                    name = "Saint Paul";
                    break;
                }
                case 7: {
                    name = "Saint Peter";
                    break;
                }
                case 8: {
                    name = "Saint Philip";
                }
            }
        }
        if (country_code.equals("AL")) {
            switch (region_code2) {
                case 40: {
                    name = "Berat";
                    break;
                }
                case 41: {
                    name = "Diber";
                    break;
                }
                case 42: {
                    name = "Durres";
                    break;
                }
                case 43: {
                    name = "Elbasan";
                    break;
                }
                case 44: {
                    name = "Fier";
                    break;
                }
                case 45: {
                    name = "Gjirokaster";
                    break;
                }
                case 46: {
                    name = "Korce";
                    break;
                }
                case 47: {
                    name = "Kukes";
                    break;
                }
                case 48: {
                    name = "Lezhe";
                    break;
                }
                case 49: {
                    name = "Shkoder";
                    break;
                }
                case 50: {
                    name = "Tirane";
                    break;
                }
                case 51: {
                    name = "Vlore";
                }
            }
        }
        if (country_code.equals("AM")) {
            switch (region_code2) {
                case 1: {
                    name = "Aragatsotn";
                    break;
                }
                case 2: {
                    name = "Ararat";
                    break;
                }
                case 3: {
                    name = "Armavir";
                    break;
                }
                case 4: {
                    name = "Geghark'unik'";
                    break;
                }
                case 5: {
                    name = "Kotayk'";
                    break;
                }
                case 6: {
                    name = "Lorri";
                    break;
                }
                case 7: {
                    name = "Shirak";
                    break;
                }
                case 8: {
                    name = "Syunik'";
                    break;
                }
                case 9: {
                    name = "Tavush";
                    break;
                }
                case 10: {
                    name = "Vayots' Dzor";
                    break;
                }
                case 11: {
                    name = "Yerevan";
                }
            }
        }
        if (country_code.equals("AO")) {
            switch (region_code2) {
                case 1: {
                    name = "Benguela";
                    break;
                }
                case 2: {
                    name = "Bie";
                    break;
                }
                case 3: {
                    name = "Cabinda";
                    break;
                }
                case 4: {
                    name = "Cuando Cubango";
                    break;
                }
                case 5: {
                    name = "Cuanza Norte";
                    break;
                }
                case 6: {
                    name = "Cuanza Sul";
                    break;
                }
                case 7: {
                    name = "Cunene";
                    break;
                }
                case 8: {
                    name = "Huambo";
                    break;
                }
                case 9: {
                    name = "Huila";
                    break;
                }
                case 10: {
                    name = "Luanda";
                    break;
                }
                case 12: {
                    name = "Malanje";
                    break;
                }
                case 13: {
                    name = "Namibe";
                    break;
                }
                case 14: {
                    name = "Moxico";
                    break;
                }
                case 15: {
                    name = "Uige";
                    break;
                }
                case 16: {
                    name = "Zaire";
                    break;
                }
                case 17: {
                    name = "Lunda Norte";
                    break;
                }
                case 18: {
                    name = "Lunda Sul";
                    break;
                }
                case 19: {
                    name = "Bengo";
                    break;
                }
                case 20: {
                    name = "Luanda";
                }
            }
        }
        if (country_code.equals("AR")) {
            switch (region_code2) {
                case 1: {
                    name = "Buenos Aires";
                    break;
                }
                case 2: {
                    name = "Catamarca";
                    break;
                }
                case 3: {
                    name = "Chaco";
                    break;
                }
                case 4: {
                    name = "Chubut";
                    break;
                }
                case 5: {
                    name = "Cordoba";
                    break;
                }
                case 6: {
                    name = "Corrientes";
                    break;
                }
                case 7: {
                    name = "Distrito Federal";
                    break;
                }
                case 8: {
                    name = "Entre Rios";
                    break;
                }
                case 9: {
                    name = "Formosa";
                    break;
                }
                case 10: {
                    name = "Jujuy";
                    break;
                }
                case 11: {
                    name = "La Pampa";
                    break;
                }
                case 12: {
                    name = "La Rioja";
                    break;
                }
                case 13: {
                    name = "Mendoza";
                    break;
                }
                case 14: {
                    name = "Misiones";
                    break;
                }
                case 15: {
                    name = "Neuquen";
                    break;
                }
                case 16: {
                    name = "Rio Negro";
                    break;
                }
                case 17: {
                    name = "Salta";
                    break;
                }
                case 18: {
                    name = "San Juan";
                    break;
                }
                case 19: {
                    name = "San Luis";
                    break;
                }
                case 20: {
                    name = "Santa Cruz";
                    break;
                }
                case 21: {
                    name = "Santa Fe";
                    break;
                }
                case 22: {
                    name = "Santiago del Estero";
                    break;
                }
                case 23: {
                    name = "Tierra del Fuego";
                    break;
                }
                case 24: {
                    name = "Tucuman";
                }
            }
        }
        if (country_code.equals("AT")) {
            switch (region_code2) {
                case 1: {
                    name = "Burgenland";
                    break;
                }
                case 2: {
                    name = "Karnten";
                    break;
                }
                case 3: {
                    name = "Niederosterreich";
                    break;
                }
                case 4: {
                    name = "Oberosterreich";
                    break;
                }
                case 5: {
                    name = "Salzburg";
                    break;
                }
                case 6: {
                    name = "Steiermark";
                    break;
                }
                case 7: {
                    name = "Tirol";
                    break;
                }
                case 8: {
                    name = "Vorarlberg";
                    break;
                }
                case 9: {
                    name = "Wien";
                }
            }
        }
        if (country_code.equals("AU")) {
            switch (region_code2) {
                case 1: {
                    name = "Australian Capital Territory";
                    break;
                }
                case 2: {
                    name = "New South Wales";
                    break;
                }
                case 3: {
                    name = "Northern Territory";
                    break;
                }
                case 4: {
                    name = "Queensland";
                    break;
                }
                case 5: {
                    name = "South Australia";
                    break;
                }
                case 6: {
                    name = "Tasmania";
                    break;
                }
                case 7: {
                    name = "Victoria";
                    break;
                }
                case 8: {
                    name = "Western Australia";
                }
            }
        }
        if (country_code.equals("AZ")) {
            switch (region_code2) {
                case 1: {
                    name = "Abseron";
                    break;
                }
                case 2: {
                    name = "Agcabadi";
                    break;
                }
                case 3: {
                    name = "Agdam";
                    break;
                }
                case 4: {
                    name = "Agdas";
                    break;
                }
                case 5: {
                    name = "Agstafa";
                    break;
                }
                case 6: {
                    name = "Agsu";
                    break;
                }
                case 7: {
                    name = "Ali Bayramli";
                    break;
                }
                case 8: {
                    name = "Astara";
                    break;
                }
                case 9: {
                    name = "Baki";
                    break;
                }
                case 10: {
                    name = "Balakan";
                    break;
                }
                case 11: {
                    name = "Barda";
                    break;
                }
                case 12: {
                    name = "Beylaqan";
                    break;
                }
                case 13: {
                    name = "Bilasuvar";
                    break;
                }
                case 14: {
                    name = "Cabrayil";
                    break;
                }
                case 15: {
                    name = "Calilabad";
                    break;
                }
                case 16: {
                    name = "Daskasan";
                    break;
                }
                case 17: {
                    name = "Davaci";
                    break;
                }
                case 18: {
                    name = "Fuzuli";
                    break;
                }
                case 19: {
                    name = "Gadabay";
                    break;
                }
                case 20: {
                    name = "Ganca";
                    break;
                }
                case 21: {
                    name = "Goranboy";
                    break;
                }
                case 22: {
                    name = "Goycay";
                    break;
                }
                case 23: {
                    name = "Haciqabul";
                    break;
                }
                case 24: {
                    name = "Imisli";
                    break;
                }
                case 25: {
                    name = "Ismayilli";
                    break;
                }
                case 26: {
                    name = "Kalbacar";
                    break;
                }
                case 27: {
                    name = "Kurdamir";
                    break;
                }
                case 28: {
                    name = "Lacin";
                    break;
                }
                case 29: {
                    name = "Lankaran";
                    break;
                }
                case 30: {
                    name = "Lankaran";
                    break;
                }
                case 31: {
                    name = "Lerik";
                    break;
                }
                case 32: {
                    name = "Masalli";
                    break;
                }
                case 33: {
                    name = "Mingacevir";
                    break;
                }
                case 34: {
                    name = "Naftalan";
                    break;
                }
                case 35: {
                    name = "Naxcivan";
                    break;
                }
                case 36: {
                    name = "Neftcala";
                    break;
                }
                case 37: {
                    name = "Oguz";
                    break;
                }
                case 38: {
                    name = "Qabala";
                    break;
                }
                case 39: {
                    name = "Qax";
                    break;
                }
                case 40: {
                    name = "Qazax";
                    break;
                }
                case 41: {
                    name = "Qobustan";
                    break;
                }
                case 42: {
                    name = "Quba";
                    break;
                }
                case 43: {
                    name = "Qubadli";
                    break;
                }
                case 44: {
                    name = "Qusar";
                    break;
                }
                case 45: {
                    name = "Saatli";
                    break;
                }
                case 46: {
                    name = "Sabirabad";
                    break;
                }
                case 47: {
                    name = "Saki";
                    break;
                }
                case 48: {
                    name = "Saki";
                    break;
                }
                case 49: {
                    name = "Salyan";
                    break;
                }
                case 50: {
                    name = "Samaxi";
                    break;
                }
                case 51: {
                    name = "Samkir";
                    break;
                }
                case 52: {
                    name = "Samux";
                    break;
                }
                case 53: {
                    name = "Siyazan";
                    break;
                }
                case 54: {
                    name = "Sumqayit";
                    break;
                }
                case 55: {
                    name = "Susa";
                    break;
                }
                case 56: {
                    name = "Susa";
                    break;
                }
                case 57: {
                    name = "Tartar";
                    break;
                }
                case 58: {
                    name = "Tovuz";
                    break;
                }
                case 59: {
                    name = "Ucar";
                    break;
                }
                case 60: {
                    name = "Xacmaz";
                    break;
                }
                case 61: {
                    name = "Xankandi";
                    break;
                }
                case 62: {
                    name = "Xanlar";
                    break;
                }
                case 63: {
                    name = "Xizi";
                    break;
                }
                case 64: {
                    name = "Xocali";
                    break;
                }
                case 65: {
                    name = "Xocavand";
                    break;
                }
                case 66: {
                    name = "Yardimli";
                    break;
                }
                case 67: {
                    name = "Yevlax";
                    break;
                }
                case 68: {
                    name = "Yevlax";
                    break;
                }
                case 69: {
                    name = "Zangilan";
                    break;
                }
                case 70: {
                    name = "Zaqatala";
                    break;
                }
                case 71: {
                    name = "Zardab";
                }
            }
        }
        if (country_code.equals("BA")) {
            switch (region_code2) {
                case 1: {
                    name = "Federation of Bosnia and Herzegovina";
                    break;
                }
                case 2: {
                    name = "Republika Srpska";
                }
            }
        }
        if (country_code.equals("BB")) {
            switch (region_code2) {
                case 1: {
                    name = "Christ Church";
                    break;
                }
                case 2: {
                    name = "Saint Andrew";
                    break;
                }
                case 3: {
                    name = "Saint George";
                    break;
                }
                case 4: {
                    name = "Saint James";
                    break;
                }
                case 5: {
                    name = "Saint John";
                    break;
                }
                case 6: {
                    name = "Saint Joseph";
                    break;
                }
                case 7: {
                    name = "Saint Lucy";
                    break;
                }
                case 8: {
                    name = "Saint Michael";
                    break;
                }
                case 9: {
                    name = "Saint Peter";
                    break;
                }
                case 10: {
                    name = "Saint Philip";
                    break;
                }
                case 11: {
                    name = "Saint Thomas";
                }
            }
        }
        if (country_code.equals("BD")) {
            switch (region_code2) {
                case 1: {
                    name = "Barisal";
                    break;
                }
                case 4: {
                    name = "Bandarban";
                    break;
                }
                case 5: {
                    name = "Comilla";
                    break;
                }
                case 12: {
                    name = "Mymensingh";
                    break;
                }
                case 13: {
                    name = "Noakhali";
                    break;
                }
                case 15: {
                    name = "Patuakhali";
                    break;
                }
                case 22: {
                    name = "Bagerhat";
                    break;
                }
                case 23: {
                    name = "Bhola";
                    break;
                }
                case 24: {
                    name = "Bogra";
                    break;
                }
                case 25: {
                    name = "Barguna";
                    break;
                }
                case 26: {
                    name = "Brahmanbaria";
                    break;
                }
                case 27: {
                    name = "Chandpur";
                    break;
                }
                case 28: {
                    name = "Chapai Nawabganj";
                    break;
                }
                case 29: {
                    name = "Chattagram";
                    break;
                }
                case 30: {
                    name = "Chuadanga";
                    break;
                }
                case 31: {
                    name = "Cox's Bazar";
                    break;
                }
                case 32: {
                    name = "Dhaka";
                    break;
                }
                case 33: {
                    name = "Dinajpur";
                    break;
                }
                case 34: {
                    name = "Faridpur";
                    break;
                }
                case 35: {
                    name = "Feni";
                    break;
                }
                case 36: {
                    name = "Gaibandha";
                    break;
                }
                case 37: {
                    name = "Gazipur";
                    break;
                }
                case 38: {
                    name = "Gopalganj";
                    break;
                }
                case 39: {
                    name = "Habiganj";
                    break;
                }
                case 40: {
                    name = "Jaipurhat";
                    break;
                }
                case 41: {
                    name = "Jamalpur";
                    break;
                }
                case 42: {
                    name = "Jessore";
                    break;
                }
                case 43: {
                    name = "Jhalakati";
                    break;
                }
                case 44: {
                    name = "Jhenaidah";
                    break;
                }
                case 45: {
                    name = "Khagrachari";
                    break;
                }
                case 46: {
                    name = "Khulna";
                    break;
                }
                case 47: {
                    name = "Kishorganj";
                    break;
                }
                case 48: {
                    name = "Kurigram";
                    break;
                }
                case 49: {
                    name = "Kushtia";
                    break;
                }
                case 50: {
                    name = "Laksmipur";
                    break;
                }
                case 51: {
                    name = "Lalmonirhat";
                    break;
                }
                case 52: {
                    name = "Madaripur";
                    break;
                }
                case 53: {
                    name = "Magura";
                    break;
                }
                case 54: {
                    name = "Manikganj";
                    break;
                }
                case 55: {
                    name = "Meherpur";
                    break;
                }
                case 56: {
                    name = "Moulavibazar";
                    break;
                }
                case 57: {
                    name = "Munshiganj";
                    break;
                }
                case 58: {
                    name = "Naogaon";
                    break;
                }
                case 59: {
                    name = "Narail";
                    break;
                }
                case 60: {
                    name = "Narayanganj";
                    break;
                }
                case 61: {
                    name = "Narsingdi";
                    break;
                }
                case 62: {
                    name = "Nator";
                    break;
                }
                case 63: {
                    name = "Netrakona";
                    break;
                }
                case 64: {
                    name = "Nilphamari";
                    break;
                }
                case 65: {
                    name = "Pabna";
                    break;
                }
                case 66: {
                    name = "Panchagar";
                    break;
                }
                case 67: {
                    name = "Parbattya Chattagram";
                    break;
                }
                case 68: {
                    name = "Pirojpur";
                    break;
                }
                case 69: {
                    name = "Rajbari";
                    break;
                }
                case 70: {
                    name = "Rajshahi";
                    break;
                }
                case 71: {
                    name = "Rangpur";
                    break;
                }
                case 72: {
                    name = "Satkhira";
                    break;
                }
                case 73: {
                    name = "Shariyatpur";
                    break;
                }
                case 74: {
                    name = "Sherpur";
                    break;
                }
                case 75: {
                    name = "Sirajganj";
                    break;
                }
                case 76: {
                    name = "Sunamganj";
                    break;
                }
                case 77: {
                    name = "Sylhet";
                    break;
                }
                case 78: {
                    name = "Tangail";
                    break;
                }
                case 79: {
                    name = "Thakurgaon";
                    break;
                }
                case 81: {
                    name = "Dhaka";
                    break;
                }
                case 82: {
                    name = "Khulna";
                    break;
                }
                case 83: {
                    name = "Rajshahi";
                    break;
                }
                case 84: {
                    name = "Chittagong";
                    break;
                }
                case 85: {
                    name = "Barisal";
                    break;
                }
                case 86: {
                    name = "Sylhet";
                }
            }
        }
        if (country_code.equals("BE")) {
            switch (region_code2) {
                case 1: {
                    name = "Antwerpen";
                    break;
                }
                case 2: {
                    name = "Brabant";
                    break;
                }
                case 3: {
                    name = "Hainaut";
                    break;
                }
                case 4: {
                    name = "Liege";
                    break;
                }
                case 5: {
                    name = "Limburg";
                    break;
                }
                case 6: {
                    name = "Luxembourg";
                    break;
                }
                case 7: {
                    name = "Namur";
                    break;
                }
                case 8: {
                    name = "Oost-Vlaanderen";
                    break;
                }
                case 9: {
                    name = "West-Vlaanderen";
                    break;
                }
                case 10: {
                    name = "Brabant Wallon";
                    break;
                }
                case 11: {
                    name = "Brussels Hoofdstedelijk Gewest";
                    break;
                }
                case 12: {
                    name = "Vlaams-Brabant";
                }
            }
        }
        if (country_code.equals("BF")) {
            switch (region_code2) {
                case 15: {
                    name = "Bam";
                    break;
                }
                case 19: {
                    name = "Boulkiemde";
                    break;
                }
                case 20: {
                    name = "Ganzourgou";
                    break;
                }
                case 21: {
                    name = "Gnagna";
                    break;
                }
                case 28: {
                    name = "Kouritenga";
                    break;
                }
                case 33: {
                    name = "Oudalan";
                    break;
                }
                case 34: {
                    name = "Passore";
                    break;
                }
                case 36: {
                    name = "Sanguie";
                    break;
                }
                case 40: {
                    name = "Soum";
                    break;
                }
                case 42: {
                    name = "Tapoa";
                    break;
                }
                case 44: {
                    name = "Zoundweogo";
                    break;
                }
                case 45: {
                    name = "Bale";
                    break;
                }
                case 46: {
                    name = "Banwa";
                    break;
                }
                case 47: {
                    name = "Bazega";
                    break;
                }
                case 48: {
                    name = "Bougouriba";
                    break;
                }
                case 49: {
                    name = "Boulgou";
                    break;
                }
                case 50: {
                    name = "Gourma";
                    break;
                }
                case 51: {
                    name = "Houet";
                    break;
                }
                case 52: {
                    name = "Ioba";
                    break;
                }
                case 53: {
                    name = "Kadiogo";
                    break;
                }
                case 54: {
                    name = "Kenedougou";
                    break;
                }
                case 55: {
                    name = "Komoe";
                    break;
                }
                case 56: {
                    name = "Komondjari";
                    break;
                }
                case 57: {
                    name = "Kompienga";
                    break;
                }
                case 58: {
                    name = "Kossi";
                    break;
                }
                case 59: {
                    name = "Koulpelogo";
                    break;
                }
                case 60: {
                    name = "Kourweogo";
                    break;
                }
                case 61: {
                    name = "Leraba";
                    break;
                }
                case 62: {
                    name = "Loroum";
                    break;
                }
                case 63: {
                    name = "Mouhoun";
                    break;
                }
                case 64: {
                    name = "Namentenga";
                    break;
                }
                case 65: {
                    name = "Naouri";
                    break;
                }
                case 66: {
                    name = "Nayala";
                    break;
                }
                case 67: {
                    name = "Noumbiel";
                    break;
                }
                case 68: {
                    name = "Oubritenga";
                    break;
                }
                case 69: {
                    name = "Poni";
                    break;
                }
                case 70: {
                    name = "Sanmatenga";
                    break;
                }
                case 71: {
                    name = "Seno";
                    break;
                }
                case 72: {
                    name = "Sissili";
                    break;
                }
                case 73: {
                    name = "Sourou";
                    break;
                }
                case 74: {
                    name = "Tuy";
                    break;
                }
                case 75: {
                    name = "Yagha";
                    break;
                }
                case 76: {
                    name = "Yatenga";
                    break;
                }
                case 77: {
                    name = "Ziro";
                    break;
                }
                case 78: {
                    name = "Zondoma";
                }
            }
        }
        if (country_code.equals("BG")) {
            switch (region_code2) {
                case 33: {
                    name = "Mikhaylovgrad";
                    break;
                }
                case 38: {
                    name = "Blagoevgrad";
                    break;
                }
                case 39: {
                    name = "Burgas";
                    break;
                }
                case 40: {
                    name = "Dobrich";
                    break;
                }
                case 41: {
                    name = "Gabrovo";
                    break;
                }
                case 42: {
                    name = "Grad Sofiya";
                    break;
                }
                case 43: {
                    name = "Khaskovo";
                    break;
                }
                case 44: {
                    name = "Kurdzhali";
                    break;
                }
                case 45: {
                    name = "Kyustendil";
                    break;
                }
                case 46: {
                    name = "Lovech";
                    break;
                }
                case 47: {
                    name = "Montana";
                    break;
                }
                case 48: {
                    name = "Pazardzhik";
                    break;
                }
                case 49: {
                    name = "Pernik";
                    break;
                }
                case 50: {
                    name = "Pleven";
                    break;
                }
                case 51: {
                    name = "Plovdiv";
                    break;
                }
                case 52: {
                    name = "Razgrad";
                    break;
                }
                case 53: {
                    name = "Ruse";
                    break;
                }
                case 54: {
                    name = "Shumen";
                    break;
                }
                case 55: {
                    name = "Silistra";
                    break;
                }
                case 56: {
                    name = "Sliven";
                    break;
                }
                case 57: {
                    name = "Smolyan";
                    break;
                }
                case 58: {
                    name = "Sofiya";
                    break;
                }
                case 59: {
                    name = "Stara Zagora";
                    break;
                }
                case 60: {
                    name = "Turgovishte";
                    break;
                }
                case 61: {
                    name = "Varna";
                    break;
                }
                case 62: {
                    name = "Veliko Turnovo";
                    break;
                }
                case 63: {
                    name = "Vidin";
                    break;
                }
                case 64: {
                    name = "Vratsa";
                    break;
                }
                case 65: {
                    name = "Yambol";
                }
            }
        }
        if (country_code.equals("BH")) {
            switch (region_code2) {
                case 1: {
                    name = "Al Hadd";
                    break;
                }
                case 2: {
                    name = "Al Manamah";
                    break;
                }
                case 3: {
                    name = "Al Muharraq";
                    break;
                }
                case 5: {
                    name = "Jidd Hafs";
                    break;
                }
                case 6: {
                    name = "Sitrah";
                    break;
                }
                case 7: {
                    name = "Ar Rifa' wa al Mintaqah al Janubiyah";
                    break;
                }
                case 8: {
                    name = "Al Mintaqah al Gharbiyah";
                    break;
                }
                case 9: {
                    name = "Mintaqat Juzur Hawar";
                    break;
                }
                case 10: {
                    name = "Al Mintaqah ash Shamaliyah";
                    break;
                }
                case 11: {
                    name = "Al Mintaqah al Wusta";
                    break;
                }
                case 12: {
                    name = "Madinat";
                    break;
                }
                case 13: {
                    name = "Ar Rifa";
                    break;
                }
                case 14: {
                    name = "Madinat Hamad";
                    break;
                }
                case 15: {
                    name = "Al Muharraq";
                    break;
                }
                case 16: {
                    name = "Al Asimah";
                    break;
                }
                case 17: {
                    name = "Al Janubiyah";
                    break;
                }
                case 18: {
                    name = "Ash Shamaliyah";
                    break;
                }
                case 19: {
                    name = "Al Wusta";
                }
            }
        }
        if (country_code.equals("BI")) {
            switch (region_code2) {
                case 2: {
                    name = "Bujumbura";
                    break;
                }
                case 9: {
                    name = "Bubanza";
                    break;
                }
                case 10: {
                    name = "Bururi";
                    break;
                }
                case 11: {
                    name = "Cankuzo";
                    break;
                }
                case 12: {
                    name = "Cibitoke";
                    break;
                }
                case 13: {
                    name = "Gitega";
                    break;
                }
                case 14: {
                    name = "Karuzi";
                    break;
                }
                case 15: {
                    name = "Kayanza";
                    break;
                }
                case 16: {
                    name = "Kirundo";
                    break;
                }
                case 17: {
                    name = "Makamba";
                    break;
                }
                case 18: {
                    name = "Muyinga";
                    break;
                }
                case 19: {
                    name = "Ngozi";
                    break;
                }
                case 20: {
                    name = "Rutana";
                    break;
                }
                case 21: {
                    name = "Ruyigi";
                    break;
                }
                case 22: {
                    name = "Muramvya";
                    break;
                }
                case 23: {
                    name = "Mwaro";
                }
            }
        }
        if (country_code.equals("BJ")) {
            switch (region_code2) {
                case 1: {
                    name = "Atakora";
                    break;
                }
                case 2: {
                    name = "Atlantique";
                    break;
                }
                case 3: {
                    name = "Borgou";
                    break;
                }
                case 4: {
                    name = "Mono";
                    break;
                }
                case 5: {
                    name = "Oueme";
                    break;
                }
                case 6: {
                    name = "Zou";
                    break;
                }
                case 7: {
                    name = "Alibori";
                    break;
                }
                case 8: {
                    name = "Atakora";
                    break;
                }
                case 9: {
                    name = "Atlanyique";
                    break;
                }
                case 10: {
                    name = "Borgou";
                    break;
                }
                case 11: {
                    name = "Collines";
                    break;
                }
                case 12: {
                    name = "Kouffo";
                    break;
                }
                case 13: {
                    name = "Donga";
                    break;
                }
                case 14: {
                    name = "Littoral";
                    break;
                }
                case 15: {
                    name = "Mono";
                    break;
                }
                case 16: {
                    name = "Oueme";
                    break;
                }
                case 17: {
                    name = "Plateau";
                    break;
                }
                case 18: {
                    name = "Zou";
                }
            }
        }
        if (country_code.equals("BM")) {
            switch (region_code2) {
                case 1: {
                    name = "Devonshire";
                    break;
                }
                case 2: {
                    name = "Hamilton";
                    break;
                }
                case 3: {
                    name = "Hamilton";
                    break;
                }
                case 4: {
                    name = "Paget";
                    break;
                }
                case 5: {
                    name = "Pembroke";
                    break;
                }
                case 6: {
                    name = "Saint George";
                    break;
                }
                case 7: {
                    name = "Saint George's";
                    break;
                }
                case 8: {
                    name = "Sandys";
                    break;
                }
                case 9: {
                    name = "Smiths";
                    break;
                }
                case 10: {
                    name = "Southampton";
                    break;
                }
                case 11: {
                    name = "Warwick";
                }
            }
        }
        if (country_code.equals("BN")) {
            switch (region_code2) {
                case 7: {
                    name = "Alibori";
                    break;
                }
                case 8: {
                    name = "Belait";
                    break;
                }
                case 9: {
                    name = "Brunei and Muara";
                    break;
                }
                case 10: {
                    name = "Temburong";
                    break;
                }
                case 11: {
                    name = "Collines";
                    break;
                }
                case 12: {
                    name = "Kouffo";
                    break;
                }
                case 13: {
                    name = "Donga";
                    break;
                }
                case 14: {
                    name = "Littoral";
                    break;
                }
                case 15: {
                    name = "Tutong";
                    break;
                }
                case 16: {
                    name = "Oueme";
                    break;
                }
                case 17: {
                    name = "Plateau";
                    break;
                }
                case 18: {
                    name = "Zou";
                }
            }
        }
        if (country_code.equals("BO")) {
            switch (region_code2) {
                case 1: {
                    name = "Chuquisaca";
                    break;
                }
                case 2: {
                    name = "Cochabamba";
                    break;
                }
                case 3: {
                    name = "El Beni";
                    break;
                }
                case 4: {
                    name = "La Paz";
                    break;
                }
                case 5: {
                    name = "Oruro";
                    break;
                }
                case 6: {
                    name = "Pando";
                    break;
                }
                case 7: {
                    name = "Potosi";
                    break;
                }
                case 8: {
                    name = "Santa Cruz";
                    break;
                }
                case 9: {
                    name = "Tarija";
                }
            }
        }
        if (country_code.equals("BR")) {
            switch (region_code2) {
                case 1: {
                    name = "Acre";
                    break;
                }
                case 2: {
                    name = "Alagoas";
                    break;
                }
                case 3: {
                    name = "Amapa";
                    break;
                }
                case 4: {
                    name = "Amazonas";
                    break;
                }
                case 5: {
                    name = "Bahia";
                    break;
                }
                case 6: {
                    name = "Ceara";
                    break;
                }
                case 7: {
                    name = "Distrito Federal";
                    break;
                }
                case 8: {
                    name = "Espirito Santo";
                    break;
                }
                case 11: {
                    name = "Mato Grosso do Sul";
                    break;
                }
                case 13: {
                    name = "Maranhao";
                    break;
                }
                case 14: {
                    name = "Mato Grosso";
                    break;
                }
                case 15: {
                    name = "Minas Gerais";
                    break;
                }
                case 16: {
                    name = "Para";
                    break;
                }
                case 17: {
                    name = "Paraiba";
                    break;
                }
                case 18: {
                    name = "Parana";
                    break;
                }
                case 20: {
                    name = "Piaui";
                    break;
                }
                case 21: {
                    name = "Rio de Janeiro";
                    break;
                }
                case 22: {
                    name = "Rio Grande do Norte";
                    break;
                }
                case 23: {
                    name = "Rio Grande do Sul";
                    break;
                }
                case 24: {
                    name = "Rondonia";
                    break;
                }
                case 25: {
                    name = "Roraima";
                    break;
                }
                case 26: {
                    name = "Santa Catarina";
                    break;
                }
                case 27: {
                    name = "Sao Paulo";
                    break;
                }
                case 28: {
                    name = "Sergipe";
                    break;
                }
                case 29: {
                    name = "Goias";
                    break;
                }
                case 30: {
                    name = "Pernambuco";
                    break;
                }
                case 31: {
                    name = "Tocantins";
                }
            }
        }
        if (country_code.equals("BS")) {
            switch (region_code2) {
                case 5: {
                    name = "Bimini";
                    break;
                }
                case 6: {
                    name = "Cat Island";
                    break;
                }
                case 10: {
                    name = "Exuma";
                    break;
                }
                case 13: {
                    name = "Inagua";
                    break;
                }
                case 15: {
                    name = "Long Island";
                    break;
                }
                case 16: {
                    name = "Mayaguana";
                    break;
                }
                case 18: {
                    name = "Ragged Island";
                    break;
                }
                case 22: {
                    name = "Harbour Island";
                    break;
                }
                case 23: {
                    name = "New Providence";
                    break;
                }
                case 24: {
                    name = "Acklins and Crooked Islands";
                    break;
                }
                case 25: {
                    name = "Freeport";
                    break;
                }
                case 26: {
                    name = "Fresh Creek";
                    break;
                }
                case 27: {
                    name = "Governor's Harbour";
                    break;
                }
                case 28: {
                    name = "Green Turtle Cay";
                    break;
                }
                case 29: {
                    name = "High Rock";
                    break;
                }
                case 30: {
                    name = "Kemps Bay";
                    break;
                }
                case 31: {
                    name = "Marsh Harbour";
                    break;
                }
                case 32: {
                    name = "Nichollstown and Berry Islands";
                    break;
                }
                case 33: {
                    name = "Rock Sound";
                    break;
                }
                case 34: {
                    name = "Sandy Point";
                    break;
                }
                case 35: {
                    name = "San Salvador and Rum Cay";
                }
            }
        }
        if (country_code.equals("BT")) {
            switch (region_code2) {
                case 5: {
                    name = "Bumthang";
                    break;
                }
                case 6: {
                    name = "Chhukha";
                    break;
                }
                case 7: {
                    name = "Chirang";
                    break;
                }
                case 8: {
                    name = "Daga";
                    break;
                }
                case 9: {
                    name = "Geylegphug";
                    break;
                }
                case 10: {
                    name = "Ha";
                    break;
                }
                case 11: {
                    name = "Lhuntshi";
                    break;
                }
                case 12: {
                    name = "Mongar";
                    break;
                }
                case 13: {
                    name = "Paro";
                    break;
                }
                case 14: {
                    name = "Pemagatsel";
                    break;
                }
                case 15: {
                    name = "Punakha";
                    break;
                }
                case 16: {
                    name = "Samchi";
                    break;
                }
                case 17: {
                    name = "Samdrup";
                    break;
                }
                case 18: {
                    name = "Shemgang";
                    break;
                }
                case 19: {
                    name = "Tashigang";
                    break;
                }
                case 20: {
                    name = "Thimphu";
                    break;
                }
                case 21: {
                    name = "Tongsa";
                    break;
                }
                case 22: {
                    name = "Wangdi Phodrang";
                }
            }
        }
        if (country_code.equals("BW")) {
            switch (region_code2) {
                case 1: {
                    name = "Central";
                    break;
                }
                case 3: {
                    name = "Ghanzi";
                    break;
                }
                case 4: {
                    name = "Kgalagadi";
                    break;
                }
                case 5: {
                    name = "Kgatleng";
                    break;
                }
                case 6: {
                    name = "Kweneng";
                    break;
                }
                case 8: {
                    name = "North-East";
                    break;
                }
                case 9: {
                    name = "South-East";
                    break;
                }
                case 10: {
                    name = "Southern";
                    break;
                }
                case 11: {
                    name = "North-West";
                }
            }
        }
        if (country_code.equals("BY")) {
            switch (region_code2) {
                case 1: {
                    name = "Brestskaya Voblasts'";
                    break;
                }
                case 2: {
                    name = "Homyel'skaya Voblasts'";
                    break;
                }
                case 3: {
                    name = "Hrodzyenskaya Voblasts'";
                    break;
                }
                case 4: {
                    name = "Minsk";
                    break;
                }
                case 5: {
                    name = "Minskaya Voblasts'";
                    break;
                }
                case 6: {
                    name = "Mahilyowskaya Voblasts'";
                    break;
                }
                case 7: {
                    name = "Vitsyebskaya Voblasts'";
                }
            }
        }
        if (country_code.equals("BZ")) {
            switch (region_code2) {
                case 1: {
                    name = "Belize";
                    break;
                }
                case 2: {
                    name = "Cayo";
                    break;
                }
                case 3: {
                    name = "Corozal";
                    break;
                }
                case 4: {
                    name = "Orange Walk";
                    break;
                }
                case 5: {
                    name = "Stann Creek";
                    break;
                }
                case 6: {
                    name = "Toledo";
                }
            }
        }
        if (country_code.equals("CD")) {
            switch (region_code2) {
                case 1: {
                    name = "Bandundu";
                    break;
                }
                case 2: {
                    name = "Equateur";
                    break;
                }
                case 4: {
                    name = "Kasai-Oriental";
                    break;
                }
                case 5: {
                    name = "Katanga";
                    break;
                }
                case 6: {
                    name = "Kinshasa";
                    break;
                }
                case 7: {
                    name = "Kivu";
                    break;
                }
                case 8: {
                    name = "Bas-Congo";
                    break;
                }
                case 9: {
                    name = "Orientale";
                    break;
                }
                case 10: {
                    name = "Maniema";
                    break;
                }
                case 11: {
                    name = "Nord-Kivu";
                    break;
                }
                case 12: {
                    name = "Sud-Kivu";
                    break;
                }
                case 13: {
                    name = "Cuvette";
                }
            }
        }
        if (country_code.equals("CF")) {
            switch (region_code2) {
                case 1: {
                    name = "Bamingui-Bangoran";
                    break;
                }
                case 2: {
                    name = "Basse-Kotto";
                    break;
                }
                case 3: {
                    name = "Haute-Kotto";
                    break;
                }
                case 4: {
                    name = "Mambere-Kadei";
                    break;
                }
                case 5: {
                    name = "Haut-Mbomou";
                    break;
                }
                case 6: {
                    name = "Kemo";
                    break;
                }
                case 7: {
                    name = "Lobaye";
                    break;
                }
                case 8: {
                    name = "Mbomou";
                    break;
                }
                case 9: {
                    name = "Nana-Mambere";
                    break;
                }
                case 11: {
                    name = "Ouaka";
                    break;
                }
                case 12: {
                    name = "Ouham";
                    break;
                }
                case 13: {
                    name = "Ouham-Pende";
                    break;
                }
                case 14: {
                    name = "Cuvette-Ouest";
                    break;
                }
                case 15: {
                    name = "Nana-Grebizi";
                    break;
                }
                case 16: {
                    name = "Sangha-Mbaere";
                    break;
                }
                case 17: {
                    name = "Ombella-Mpoko";
                    break;
                }
                case 18: {
                    name = "Bangui";
                }
            }
        }
        if (country_code.equals("CG")) {
            switch (region_code2) {
                case 1: {
                    name = "Bouenza";
                    break;
                }
                case 3: {
                    name = "Cuvette";
                    break;
                }
                case 4: {
                    name = "Kouilou";
                    break;
                }
                case 5: {
                    name = "Lekoumou";
                    break;
                }
                case 6: {
                    name = "Likouala";
                    break;
                }
                case 7: {
                    name = "Niari";
                    break;
                }
                case 8: {
                    name = "Plateaux";
                    break;
                }
                case 10: {
                    name = "Sangha";
                    break;
                }
                case 11: {
                    name = "Pool";
                    break;
                }
                case 12: {
                    name = "Brazzaville";
                }
            }
        }
        if (country_code.equals("CH")) {
            switch (region_code2) {
                case 1: {
                    name = "Aargau";
                    break;
                }
                case 2: {
                    name = "Ausser-Rhoden";
                    break;
                }
                case 3: {
                    name = "Basel-Landschaft";
                    break;
                }
                case 4: {
                    name = "Basel-Stadt";
                    break;
                }
                case 5: {
                    name = "Bern";
                    break;
                }
                case 6: {
                    name = "Fribourg";
                    break;
                }
                case 7: {
                    name = "Geneve";
                    break;
                }
                case 8: {
                    name = "Glarus";
                    break;
                }
                case 9: {
                    name = "Graubunden";
                    break;
                }
                case 10: {
                    name = "Inner-Rhoden";
                    break;
                }
                case 11: {
                    name = "Luzern";
                    break;
                }
                case 12: {
                    name = "Neuchatel";
                    break;
                }
                case 13: {
                    name = "Nidwalden";
                    break;
                }
                case 14: {
                    name = "Obwalden";
                    break;
                }
                case 15: {
                    name = "Sankt Gallen";
                    break;
                }
                case 16: {
                    name = "Schaffhausen";
                    break;
                }
                case 17: {
                    name = "Schwyz";
                    break;
                }
                case 18: {
                    name = "Solothurn";
                    break;
                }
                case 19: {
                    name = "Thurgau";
                    break;
                }
                case 20: {
                    name = "Ticino";
                    break;
                }
                case 21: {
                    name = "Uri";
                    break;
                }
                case 22: {
                    name = "Valais";
                    break;
                }
                case 23: {
                    name = "Vaud";
                    break;
                }
                case 24: {
                    name = "Zug";
                    break;
                }
                case 25: {
                    name = "Zurich";
                    break;
                }
                case 26: {
                    name = "Jura";
                }
            }
        }
        if (country_code.equals("CI")) {
            switch (region_code2) {
                case 5: {
                    name = "Atacama";
                    break;
                }
                case 6: {
                    name = "Biobio";
                    break;
                }
                case 51: {
                    name = "Sassandra";
                    break;
                }
                case 61: {
                    name = "Abidjan";
                    break;
                }
                case 74: {
                    name = "Agneby";
                    break;
                }
                case 75: {
                    name = "Bafing";
                    break;
                }
                case 76: {
                    name = "Bas-Sassandra";
                    break;
                }
                case 77: {
                    name = "Denguele";
                    break;
                }
                case 78: {
                    name = "Dix-Huit Montagnes";
                    break;
                }
                case 79: {
                    name = "Fromager";
                    break;
                }
                case 80: {
                    name = "Haut-Sassandra";
                    break;
                }
                case 81: {
                    name = "Lacs";
                    break;
                }
                case 82: {
                    name = "Lagunes";
                    break;
                }
                case 83: {
                    name = "Marahoue";
                    break;
                }
                case 84: {
                    name = "Moyen-Cavally";
                    break;
                }
                case 85: {
                    name = "Moyen-Comoe";
                    break;
                }
                case 86: {
                    name = "N'zi-Comoe";
                    break;
                }
                case 87: {
                    name = "Savanes";
                    break;
                }
                case 88: {
                    name = "Sud-Bandama";
                    break;
                }
                case 89: {
                    name = "Sud-Comoe";
                    break;
                }
                case 90: {
                    name = "Vallee du Bandama";
                    break;
                }
                case 91: {
                    name = "Worodougou";
                    break;
                }
                case 92: {
                    name = "Zanzan";
                }
            }
        }
        if (country_code.equals("CL")) {
            switch (region_code2) {
                case 1: {
                    name = "Valparaiso";
                    break;
                }
                case 2: {
                    name = "Aisen del General Carlos Ibanez del Campo";
                    break;
                }
                case 3: {
                    name = "Antofagasta";
                    break;
                }
                case 4: {
                    name = "Araucania";
                    break;
                }
                case 5: {
                    name = "Atacama";
                    break;
                }
                case 6: {
                    name = "Bio-Bio";
                    break;
                }
                case 7: {
                    name = "Coquimbo";
                    break;
                }
                case 8: {
                    name = "Libertador General Bernardo O'Higgins";
                    break;
                }
                case 9: {
                    name = "Los Lagos";
                    break;
                }
                case 10: {
                    name = "Magallanes y de la Antartica Chilena";
                    break;
                }
                case 11: {
                    name = "Maule";
                    break;
                }
                case 12: {
                    name = "Region Metropolitana";
                    break;
                }
                case 13: {
                    name = "Tarapaca";
                    break;
                }
                case 14: {
                    name = "Los Lagos";
                    break;
                }
                case 15: {
                    name = "Tarapaca";
                    break;
                }
                case 16: {
                    name = "Arica y Parinacota";
                    break;
                }
                case 17: {
                    name = "Los Rios";
                }
            }
        }
        if (country_code.equals("CM")) {
            switch (region_code2) {
                case 4: {
                    name = "Est";
                    break;
                }
                case 5: {
                    name = "Littoral";
                    break;
                }
                case 7: {
                    name = "Nord-Ouest";
                    break;
                }
                case 8: {
                    name = "Ouest";
                    break;
                }
                case 9: {
                    name = "Sud-Ouest";
                    break;
                }
                case 10: {
                    name = "Adamaoua";
                    break;
                }
                case 11: {
                    name = "Centre";
                    break;
                }
                case 12: {
                    name = "Extreme-Nord";
                    break;
                }
                case 13: {
                    name = "Nord";
                    break;
                }
                case 14: {
                    name = "Sud";
                }
            }
        }
        if (country_code.equals("CN")) {
            switch (region_code2) {
                case 1: {
                    name = "Anhui";
                    break;
                }
                case 2: {
                    name = "Zhejiang";
                    break;
                }
                case 3: {
                    name = "Jiangxi";
                    break;
                }
                case 4: {
                    name = "Jiangsu";
                    break;
                }
                case 5: {
                    name = "Jilin";
                    break;
                }
                case 6: {
                    name = "Qinghai";
                    break;
                }
                case 7: {
                    name = "Fujian";
                    break;
                }
                case 8: {
                    name = "Heilongjiang";
                    break;
                }
                case 9: {
                    name = "Henan";
                    break;
                }
                case 10: {
                    name = "Hebei";
                    break;
                }
                case 11: {
                    name = "Hunan";
                    break;
                }
                case 12: {
                    name = "Hubei";
                    break;
                }
                case 13: {
                    name = "Xinjiang";
                    break;
                }
                case 14: {
                    name = "Xizang";
                    break;
                }
                case 15: {
                    name = "Gansu";
                    break;
                }
                case 16: {
                    name = "Guangxi";
                    break;
                }
                case 18: {
                    name = "Guizhou";
                    break;
                }
                case 19: {
                    name = "Liaoning";
                    break;
                }
                case 20: {
                    name = "Nei Mongol";
                    break;
                }
                case 21: {
                    name = "Ningxia";
                    break;
                }
                case 22: {
                    name = "Beijing";
                    break;
                }
                case 23: {
                    name = "Shanghai";
                    break;
                }
                case 24: {
                    name = "Shanxi";
                    break;
                }
                case 25: {
                    name = "Shandong";
                    break;
                }
                case 26: {
                    name = "Shaanxi";
                    break;
                }
                case 28: {
                    name = "Tianjin";
                    break;
                }
                case 29: {
                    name = "Yunnan";
                    break;
                }
                case 30: {
                    name = "Guangdong";
                    break;
                }
                case 31: {
                    name = "Hainan";
                    break;
                }
                case 32: {
                    name = "Sichuan";
                    break;
                }
                case 33: {
                    name = "Chongqing";
                }
            }
        }
        if (country_code.equals("CO")) {
            switch (region_code2) {
                case 1: {
                    name = "Amazonas";
                    break;
                }
                case 2: {
                    name = "Antioquia";
                    break;
                }
                case 3: {
                    name = "Arauca";
                    break;
                }
                case 4: {
                    name = "Atlantico";
                    break;
                }
                case 5: {
                    name = "Bolivar Department";
                    break;
                }
                case 6: {
                    name = "Boyaca Department";
                    break;
                }
                case 7: {
                    name = "Caldas Department";
                    break;
                }
                case 8: {
                    name = "Caqueta";
                    break;
                }
                case 9: {
                    name = "Cauca";
                    break;
                }
                case 10: {
                    name = "Cesar";
                    break;
                }
                case 11: {
                    name = "Choco";
                    break;
                }
                case 12: {
                    name = "Cordoba";
                    break;
                }
                case 14: {
                    name = "Guaviare";
                    break;
                }
                case 15: {
                    name = "Guainia";
                    break;
                }
                case 16: {
                    name = "Huila";
                    break;
                }
                case 17: {
                    name = "La Guajira";
                    break;
                }
                case 18: {
                    name = "Magdalena Department";
                    break;
                }
                case 19: {
                    name = "Meta";
                    break;
                }
                case 20: {
                    name = "Narino";
                    break;
                }
                case 21: {
                    name = "Norte de Santander";
                    break;
                }
                case 22: {
                    name = "Putumayo";
                    break;
                }
                case 23: {
                    name = "Quindio";
                    break;
                }
                case 24: {
                    name = "Risaralda";
                    break;
                }
                case 25: {
                    name = "San Andres y Providencia";
                    break;
                }
                case 26: {
                    name = "Santander";
                    break;
                }
                case 27: {
                    name = "Sucre";
                    break;
                }
                case 28: {
                    name = "Tolima";
                    break;
                }
                case 29: {
                    name = "Valle del Cauca";
                    break;
                }
                case 30: {
                    name = "Vaupes";
                    break;
                }
                case 31: {
                    name = "Vichada";
                    break;
                }
                case 32: {
                    name = "Casanare";
                    break;
                }
                case 33: {
                    name = "Cundinamarca";
                    break;
                }
                case 34: {
                    name = "Distrito Especial";
                    break;
                }
                case 35: {
                    name = "Bolivar";
                    break;
                }
                case 36: {
                    name = "Boyaca";
                    break;
                }
                case 37: {
                    name = "Caldas";
                    break;
                }
                case 38: {
                    name = "Magdalena";
                }
            }
        }
        if (country_code.equals("CR")) {
            switch (region_code2) {
                case 1: {
                    name = "Alajuela";
                    break;
                }
                case 2: {
                    name = "Cartago";
                    break;
                }
                case 3: {
                    name = "Guanacaste";
                    break;
                }
                case 4: {
                    name = "Heredia";
                    break;
                }
                case 6: {
                    name = "Limon";
                    break;
                }
                case 7: {
                    name = "Puntarenas";
                    break;
                }
                case 8: {
                    name = "San Jose";
                }
            }
        }
        if (country_code.equals("CU")) {
            switch (region_code2) {
                case 1: {
                    name = "Pinar del Rio";
                    break;
                }
                case 2: {
                    name = "Ciudad de la Habana";
                    break;
                }
                case 3: {
                    name = "Matanzas";
                    break;
                }
                case 4: {
                    name = "Isla de la Juventud";
                    break;
                }
                case 5: {
                    name = "Camaguey";
                    break;
                }
                case 7: {
                    name = "Ciego de Avila";
                    break;
                }
                case 8: {
                    name = "Cienfuegos";
                    break;
                }
                case 9: {
                    name = "Granma";
                    break;
                }
                case 10: {
                    name = "Guantanamo";
                    break;
                }
                case 11: {
                    name = "La Habana";
                    break;
                }
                case 12: {
                    name = "Holguin";
                    break;
                }
                case 13: {
                    name = "Las Tunas";
                    break;
                }
                case 14: {
                    name = "Sancti Spiritus";
                    break;
                }
                case 15: {
                    name = "Santiago de Cuba";
                    break;
                }
                case 16: {
                    name = "Villa Clara";
                }
            }
        }
        if (country_code.equals("CV")) {
            switch (region_code2) {
                case 1: {
                    name = "Boa Vista";
                    break;
                }
                case 2: {
                    name = "Brava";
                    break;
                }
                case 4: {
                    name = "Maio";
                    break;
                }
                case 5: {
                    name = "Paul";
                    break;
                }
                case 7: {
                    name = "Ribeira Grande";
                    break;
                }
                case 8: {
                    name = "Sal";
                    break;
                }
                case 10: {
                    name = "Sao Nicolau";
                    break;
                }
                case 11: {
                    name = "Sao Vicente";
                    break;
                }
                case 13: {
                    name = "Mosteiros";
                    break;
                }
                case 14: {
                    name = "Praia";
                    break;
                }
                case 15: {
                    name = "Santa Catarina";
                    break;
                }
                case 16: {
                    name = "Santa Cruz";
                    break;
                }
                case 17: {
                    name = "Sao Domingos";
                    break;
                }
                case 18: {
                    name = "Sao Filipe";
                    break;
                }
                case 19: {
                    name = "Sao Miguel";
                    break;
                }
                case 20: {
                    name = "Tarrafal";
                }
            }
        }
        if (country_code.equals("CY")) {
            switch (region_code2) {
                case 1: {
                    name = "Famagusta";
                    break;
                }
                case 2: {
                    name = "Kyrenia";
                    break;
                }
                case 3: {
                    name = "Larnaca";
                    break;
                }
                case 4: {
                    name = "Nicosia";
                    break;
                }
                case 5: {
                    name = "Limassol";
                    break;
                }
                case 6: {
                    name = "Paphos";
                }
            }
        }
        if (country_code.equals("CZ")) {
            switch (region_code2) {
                case 3: {
                    name = "Blansko";
                    break;
                }
                case 4: {
                    name = "Breclav";
                    break;
                }
                case 20: {
                    name = "Hradec Kralove";
                    break;
                }
                case 21: {
                    name = "Jablonec nad Nisou";
                    break;
                }
                case 23: {
                    name = "Jicin";
                    break;
                }
                case 24: {
                    name = "Jihlava";
                    break;
                }
                case 30: {
                    name = "Kolin";
                    break;
                }
                case 33: {
                    name = "Liberec";
                    break;
                }
                case 36: {
                    name = "Melnik";
                    break;
                }
                case 37: {
                    name = "Mlada Boleslav";
                    break;
                }
                case 39: {
                    name = "Nachod";
                    break;
                }
                case 41: {
                    name = "Nymburk";
                    break;
                }
                case 45: {
                    name = "Pardubice";
                    break;
                }
                case 52: {
                    name = "Hlavni mesto Praha";
                    break;
                }
                case 61: {
                    name = "Semily";
                    break;
                }
                case 70: {
                    name = "Trutnov";
                    break;
                }
                case 78: {
                    name = "Jihomoravsky kraj";
                    break;
                }
                case 79: {
                    name = "Jihocesky kraj";
                    break;
                }
                case 80: {
                    name = "Vysocina";
                    break;
                }
                case 81: {
                    name = "Karlovarsky kraj";
                    break;
                }
                case 82: {
                    name = "Kralovehradecky kraj";
                    break;
                }
                case 83: {
                    name = "Liberecky kraj";
                    break;
                }
                case 84: {
                    name = "Olomoucky kraj";
                    break;
                }
                case 85: {
                    name = "Moravskoslezsky kraj";
                    break;
                }
                case 86: {
                    name = "Pardubicky kraj";
                    break;
                }
                case 87: {
                    name = "Plzensky kraj";
                    break;
                }
                case 88: {
                    name = "Stredocesky kraj";
                    break;
                }
                case 89: {
                    name = "Ustecky kraj";
                    break;
                }
                case 90: {
                    name = "Zlinsky kraj";
                }
            }
        }
        if (country_code.equals("DE")) {
            switch (region_code2) {
                case 1: {
                    name = "Baden-Wurttemberg";
                    break;
                }
                case 2: {
                    name = "Bayern";
                    break;
                }
                case 3: {
                    name = "Bremen";
                    break;
                }
                case 4: {
                    name = "Hamburg";
                    break;
                }
                case 5: {
                    name = "Hessen";
                    break;
                }
                case 6: {
                    name = "Niedersachsen";
                    break;
                }
                case 7: {
                    name = "Nordrhein-Westfalen";
                    break;
                }
                case 8: {
                    name = "Rheinland-Pfalz";
                    break;
                }
                case 9: {
                    name = "Saarland";
                    break;
                }
                case 10: {
                    name = "Schleswig-Holstein";
                    break;
                }
                case 11: {
                    name = "Brandenburg";
                    break;
                }
                case 12: {
                    name = "Mecklenburg-Vorpommern";
                    break;
                }
                case 13: {
                    name = "Sachsen";
                    break;
                }
                case 14: {
                    name = "Sachsen-Anhalt";
                    break;
                }
                case 15: {
                    name = "Thuringen";
                    break;
                }
                case 16: {
                    name = "Berlin";
                }
            }
        }
        if (country_code.equals("DJ")) {
            switch (region_code2) {
                case 1: {
                    name = "Ali Sabieh";
                    break;
                }
                case 4: {
                    name = "Obock";
                    break;
                }
                case 5: {
                    name = "Tadjoura";
                    break;
                }
                case 6: {
                    name = "Dikhil";
                    break;
                }
                case 7: {
                    name = "Djibouti";
                    break;
                }
                case 8: {
                    name = "Arta";
                }
            }
        }
        if (country_code.equals("DK")) {
            switch (region_code2) {
                case 17: {
                    name = "Hovedstaden";
                    break;
                }
                case 18: {
                    name = "Midtjylland";
                    break;
                }
                case 19: {
                    name = "Nordjylland";
                    break;
                }
                case 20: {
                    name = "Sjelland";
                    break;
                }
                case 21: {
                    name = "Syddanmark";
                }
            }
        }
        if (country_code.equals("DM")) {
            switch (region_code2) {
                case 2: {
                    name = "Saint Andrew";
                    break;
                }
                case 3: {
                    name = "Saint David";
                    break;
                }
                case 4: {
                    name = "Saint George";
                    break;
                }
                case 5: {
                    name = "Saint John";
                    break;
                }
                case 6: {
                    name = "Saint Joseph";
                    break;
                }
                case 7: {
                    name = "Saint Luke";
                    break;
                }
                case 8: {
                    name = "Saint Mark";
                    break;
                }
                case 9: {
                    name = "Saint Patrick";
                    break;
                }
                case 10: {
                    name = "Saint Paul";
                    break;
                }
                case 11: {
                    name = "Saint Peter";
                }
            }
        }
        if (country_code.equals("DO")) {
            switch (region_code2) {
                case 1: {
                    name = "Azua";
                    break;
                }
                case 2: {
                    name = "Baoruco";
                    break;
                }
                case 3: {
                    name = "Barahona";
                    break;
                }
                case 4: {
                    name = "Dajabon";
                    break;
                }
                case 5: {
                    name = "Distrito Nacional";
                    break;
                }
                case 6: {
                    name = "Duarte";
                    break;
                }
                case 8: {
                    name = "Espaillat";
                    break;
                }
                case 9: {
                    name = "Independencia";
                    break;
                }
                case 10: {
                    name = "La Altagracia";
                    break;
                }
                case 11: {
                    name = "Elias Pina";
                    break;
                }
                case 12: {
                    name = "La Romana";
                    break;
                }
                case 14: {
                    name = "Maria Trinidad Sanchez";
                    break;
                }
                case 15: {
                    name = "Monte Cristi";
                    break;
                }
                case 16: {
                    name = "Pedernales";
                    break;
                }
                case 17: {
                    name = "Peravia";
                    break;
                }
                case 18: {
                    name = "Puerto Plata";
                    break;
                }
                case 19: {
                    name = "Salcedo";
                    break;
                }
                case 20: {
                    name = "Samana";
                    break;
                }
                case 21: {
                    name = "Sanchez Ramirez";
                    break;
                }
                case 23: {
                    name = "San Juan";
                    break;
                }
                case 24: {
                    name = "San Pedro De Macoris";
                    break;
                }
                case 25: {
                    name = "Santiago";
                    break;
                }
                case 26: {
                    name = "Santiago Rodriguez";
                    break;
                }
                case 27: {
                    name = "Valverde";
                    break;
                }
                case 28: {
                    name = "El Seibo";
                    break;
                }
                case 29: {
                    name = "Hato Mayor";
                    break;
                }
                case 30: {
                    name = "La Vega";
                    break;
                }
                case 31: {
                    name = "Monsenor Nouel";
                    break;
                }
                case 32: {
                    name = "Monte Plata";
                    break;
                }
                case 33: {
                    name = "San Cristobal";
                    break;
                }
                case 34: {
                    name = "Distrito Nacional";
                    break;
                }
                case 35: {
                    name = "Peravia";
                    break;
                }
                case 36: {
                    name = "San Jose de Ocoa";
                    break;
                }
                case 37: {
                    name = "Santo Domingo";
                }
            }
        }
        if (country_code.equals("DZ")) {
            switch (region_code2) {
                case 1: {
                    name = "Alger";
                    break;
                }
                case 3: {
                    name = "Batna";
                    break;
                }
                case 4: {
                    name = "Constantine";
                    break;
                }
                case 6: {
                    name = "Medea";
                    break;
                }
                case 7: {
                    name = "Mostaganem";
                    break;
                }
                case 9: {
                    name = "Oran";
                    break;
                }
                case 10: {
                    name = "Saida";
                    break;
                }
                case 12: {
                    name = "Setif";
                    break;
                }
                case 13: {
                    name = "Tiaret";
                    break;
                }
                case 14: {
                    name = "Tizi Ouzou";
                    break;
                }
                case 15: {
                    name = "Tlemcen";
                    break;
                }
                case 18: {
                    name = "Bejaia";
                    break;
                }
                case 19: {
                    name = "Biskra";
                    break;
                }
                case 20: {
                    name = "Blida";
                    break;
                }
                case 21: {
                    name = "Bouira";
                    break;
                }
                case 22: {
                    name = "Djelfa";
                    break;
                }
                case 23: {
                    name = "Guelma";
                    break;
                }
                case 24: {
                    name = "Jijel";
                    break;
                }
                case 25: {
                    name = "Laghouat";
                    break;
                }
                case 26: {
                    name = "Mascara";
                    break;
                }
                case 27: {
                    name = "M'sila";
                    break;
                }
                case 29: {
                    name = "Oum el Bouaghi";
                    break;
                }
                case 30: {
                    name = "Sidi Bel Abbes";
                    break;
                }
                case 31: {
                    name = "Skikda";
                    break;
                }
                case 33: {
                    name = "Tebessa";
                    break;
                }
                case 34: {
                    name = "Adrar";
                    break;
                }
                case 35: {
                    name = "Ain Defla";
                    break;
                }
                case 36: {
                    name = "Ain Temouchent";
                    break;
                }
                case 37: {
                    name = "Annaba";
                    break;
                }
                case 38: {
                    name = "Bechar";
                    break;
                }
                case 39: {
                    name = "Bordj Bou Arreridj";
                    break;
                }
                case 40: {
                    name = "Boumerdes";
                    break;
                }
                case 41: {
                    name = "Chlef";
                    break;
                }
                case 42: {
                    name = "El Bayadh";
                    break;
                }
                case 43: {
                    name = "El Oued";
                    break;
                }
                case 44: {
                    name = "El Tarf";
                    break;
                }
                case 45: {
                    name = "Ghardaia";
                    break;
                }
                case 46: {
                    name = "Illizi";
                    break;
                }
                case 47: {
                    name = "Khenchela";
                    break;
                }
                case 48: {
                    name = "Mila";
                    break;
                }
                case 49: {
                    name = "Naama";
                    break;
                }
                case 50: {
                    name = "Ouargla";
                    break;
                }
                case 51: {
                    name = "Relizane";
                    break;
                }
                case 52: {
                    name = "Souk Ahras";
                    break;
                }
                case 53: {
                    name = "Tamanghasset";
                    break;
                }
                case 54: {
                    name = "Tindouf";
                    break;
                }
                case 55: {
                    name = "Tipaza";
                    break;
                }
                case 56: {
                    name = "Tissemsilt";
                }
            }
        }
        if (country_code.equals("EC")) {
            switch (region_code2) {
                case 1: {
                    name = "Galapagos";
                    break;
                }
                case 2: {
                    name = "Azuay";
                    break;
                }
                case 3: {
                    name = "Bolivar";
                    break;
                }
                case 4: {
                    name = "Canar";
                    break;
                }
                case 5: {
                    name = "Carchi";
                    break;
                }
                case 6: {
                    name = "Chimborazo";
                    break;
                }
                case 7: {
                    name = "Cotopaxi";
                    break;
                }
                case 8: {
                    name = "El Oro";
                    break;
                }
                case 9: {
                    name = "Esmeraldas";
                    break;
                }
                case 10: {
                    name = "Guayas";
                    break;
                }
                case 11: {
                    name = "Imbabura";
                    break;
                }
                case 12: {
                    name = "Loja";
                    break;
                }
                case 13: {
                    name = "Los Rios";
                    break;
                }
                case 14: {
                    name = "Manabi";
                    break;
                }
                case 15: {
                    name = "Morona-Santiago";
                    break;
                }
                case 17: {
                    name = "Pastaza";
                    break;
                }
                case 18: {
                    name = "Pichincha";
                    break;
                }
                case 19: {
                    name = "Tungurahua";
                    break;
                }
                case 20: {
                    name = "Zamora-Chinchipe";
                    break;
                }
                case 22: {
                    name = "Sucumbios";
                    break;
                }
                case 23: {
                    name = "Napo";
                    break;
                }
                case 24: {
                    name = "Orellana";
                }
            }
        }
        if (country_code.equals("EE")) {
            switch (region_code2) {
                case 1: {
                    name = "Harjumaa";
                    break;
                }
                case 2: {
                    name = "Hiiumaa";
                    break;
                }
                case 3: {
                    name = "Ida-Virumaa";
                    break;
                }
                case 4: {
                    name = "Jarvamaa";
                    break;
                }
                case 5: {
                    name = "Jogevamaa";
                    break;
                }
                case 6: {
                    name = "Kohtla-Jarve";
                    break;
                }
                case 7: {
                    name = "Laanemaa";
                    break;
                }
                case 8: {
                    name = "Laane-Virumaa";
                    break;
                }
                case 9: {
                    name = "Narva";
                    break;
                }
                case 10: {
                    name = "Parnu";
                    break;
                }
                case 11: {
                    name = "Parnumaa";
                    break;
                }
                case 12: {
                    name = "Polvamaa";
                    break;
                }
                case 13: {
                    name = "Raplamaa";
                    break;
                }
                case 14: {
                    name = "Saaremaa";
                    break;
                }
                case 15: {
                    name = "Sillamae";
                    break;
                }
                case 16: {
                    name = "Tallinn";
                    break;
                }
                case 17: {
                    name = "Tartu";
                    break;
                }
                case 18: {
                    name = "Tartumaa";
                    break;
                }
                case 19: {
                    name = "Valgamaa";
                    break;
                }
                case 20: {
                    name = "Viljandimaa";
                    break;
                }
                case 21: {
                    name = "Vorumaa";
                }
            }
        }
        if (country_code.equals("EG")) {
            switch (region_code2) {
                case 1: {
                    name = "Ad Daqahliyah";
                    break;
                }
                case 2: {
                    name = "Al Bahr al Ahmar";
                    break;
                }
                case 3: {
                    name = "Al Buhayrah";
                    break;
                }
                case 4: {
                    name = "Al Fayyum";
                    break;
                }
                case 5: {
                    name = "Al Gharbiyah";
                    break;
                }
                case 6: {
                    name = "Al Iskandariyah";
                    break;
                }
                case 7: {
                    name = "Al Isma'iliyah";
                    break;
                }
                case 8: {
                    name = "Al Jizah";
                    break;
                }
                case 9: {
                    name = "Al Minufiyah";
                    break;
                }
                case 10: {
                    name = "Al Minya";
                    break;
                }
                case 11: {
                    name = "Al Qahirah";
                    break;
                }
                case 12: {
                    name = "Al Qalyubiyah";
                    break;
                }
                case 13: {
                    name = "Al Wadi al Jadid";
                    break;
                }
                case 14: {
                    name = "Ash Sharqiyah";
                    break;
                }
                case 15: {
                    name = "As Suways";
                    break;
                }
                case 16: {
                    name = "Aswan";
                    break;
                }
                case 17: {
                    name = "Asyut";
                    break;
                }
                case 18: {
                    name = "Bani Suwayf";
                    break;
                }
                case 19: {
                    name = "Bur Sa'id";
                    break;
                }
                case 20: {
                    name = "Dumyat";
                    break;
                }
                case 21: {
                    name = "Kafr ash Shaykh";
                    break;
                }
                case 22: {
                    name = "Matruh";
                    break;
                }
                case 23: {
                    name = "Qina";
                    break;
                }
                case 24: {
                    name = "Suhaj";
                    break;
                }
                case 26: {
                    name = "Janub Sina'";
                    break;
                }
                case 27: {
                    name = "Shamal Sina'";
                }
            }
        }
        if (country_code.equals("ER")) {
            switch (region_code2) {
                case 1: {
                    name = "Anseba";
                    break;
                }
                case 2: {
                    name = "Debub";
                    break;
                }
                case 3: {
                    name = "Debubawi K'eyih Bahri";
                    break;
                }
                case 4: {
                    name = "Gash Barka";
                    break;
                }
                case 5: {
                    name = "Ma'akel";
                    break;
                }
                case 6: {
                    name = "Semenawi K'eyih Bahri";
                }
            }
        }
        if (country_code.equals("ES")) {
            switch (region_code2) {
                case 7: {
                    name = "Islas Baleares";
                    break;
                }
                case 27: {
                    name = "La Rioja";
                    break;
                }
                case 29: {
                    name = "Madrid";
                    break;
                }
                case 31: {
                    name = "Murcia";
                    break;
                }
                case 32: {
                    name = "Navarra";
                    break;
                }
                case 34: {
                    name = "Asturias";
                    break;
                }
                case 39: {
                    name = "Cantabria";
                    break;
                }
                case 51: {
                    name = "Andalucia";
                    break;
                }
                case 52: {
                    name = "Aragon";
                    break;
                }
                case 53: {
                    name = "Canarias";
                    break;
                }
                case 54: {
                    name = "Castilla-La Mancha";
                    break;
                }
                case 55: {
                    name = "Castilla y Leon";
                    break;
                }
                case 56: {
                    name = "Catalonia";
                    break;
                }
                case 57: {
                    name = "Extremadura";
                    break;
                }
                case 58: {
                    name = "Galicia";
                    break;
                }
                case 59: {
                    name = "Pais Vasco";
                    break;
                }
                case 60: {
                    name = "Comunidad Valenciana";
                }
            }
        }
        if (country_code.equals("ET")) {
            switch (region_code2) {
                case 2: {
                    name = "Amhara";
                    break;
                }
                case 7: {
                    name = "Somali";
                    break;
                }
                case 8: {
                    name = "Gambella";
                    break;
                }
                case 10: {
                    name = "Addis Abeba";
                    break;
                }
                case 11: {
                    name = "Southern";
                    break;
                }
                case 12: {
                    name = "Tigray";
                    break;
                }
                case 13: {
                    name = "Benishangul";
                    break;
                }
                case 14: {
                    name = "Afar";
                    break;
                }
                case 44: {
                    name = "Adis Abeba";
                    break;
                }
                case 45: {
                    name = "Afar";
                    break;
                }
                case 46: {
                    name = "Amara";
                    break;
                }
                case 47: {
                    name = "Binshangul Gumuz";
                    break;
                }
                case 48: {
                    name = "Dire Dawa";
                    break;
                }
                case 49: {
                    name = "Gambela Hizboch";
                    break;
                }
                case 50: {
                    name = "Hareri Hizb";
                    break;
                }
                case 51: {
                    name = "Oromiya";
                    break;
                }
                case 52: {
                    name = "Sumale";
                    break;
                }
                case 53: {
                    name = "Tigray";
                    break;
                }
                case 54: {
                    name = "YeDebub Biheroch Bihereseboch na Hizboch";
                }
            }
        }
        if (country_code.equals("FI")) {
            switch (region_code2) {
                case 1: {
                    name = "Aland";
                    break;
                }
                case 6: {
                    name = "Lapland";
                    break;
                }
                case 8: {
                    name = "Oulu";
                    break;
                }
                case 13: {
                    name = "Southern Finland";
                    break;
                }
                case 14: {
                    name = "Eastern Finland";
                    break;
                }
                case 15: {
                    name = "Western Finland";
                }
            }
        }
        if (country_code.equals("FJ")) {
            switch (region_code2) {
                case 1: {
                    name = "Central";
                    break;
                }
                case 2: {
                    name = "Eastern";
                    break;
                }
                case 3: {
                    name = "Northern";
                    break;
                }
                case 4: {
                    name = "Rotuma";
                    break;
                }
                case 5: {
                    name = "Western";
                }
            }
        }
        if (country_code.equals("FM")) {
            switch (region_code2) {
                case 1: {
                    name = "Kosrae";
                    break;
                }
                case 2: {
                    name = "Pohnpei";
                    break;
                }
                case 3: {
                    name = "Chuuk";
                    break;
                }
                case 4: {
                    name = "Yap";
                }
            }
        }
        if (country_code.equals("FR")) {
            switch (region_code2) {
                case 97: {
                    name = "Aquitaine";
                    break;
                }
                case 98: {
                    name = "Auvergne";
                    break;
                }
                case 99: {
                    name = "Basse-Normandie";
                    break;
                }
                case 832: {
                    name = "Bourgogne";
                    break;
                }
                case 833: {
                    name = "Bretagne";
                    break;
                }
                case 834: {
                    name = "Centre";
                    break;
                }
                case 835: {
                    name = "Champagne-Ardenne";
                    break;
                }
                case 836: {
                    name = "Corse";
                    break;
                }
                case 837: {
                    name = "Franche-Comte";
                    break;
                }
                case 838: {
                    name = "Haute-Normandie";
                    break;
                }
                case 839: {
                    name = "Ile-de-France";
                    break;
                }
                case 840: {
                    name = "Languedoc-Roussillon";
                    break;
                }
                case 875: {
                    name = "Limousin";
                    break;
                }
                case 876: {
                    name = "Lorraine";
                    break;
                }
                case 877: {
                    name = "Midi-Pyrenees";
                    break;
                }
                case 878: {
                    name = "Nord-Pas-de-Calais";
                    break;
                }
                case 879: {
                    name = "Pays de la Loire";
                    break;
                }
                case 880: {
                    name = "Picardie";
                    break;
                }
                case 881: {
                    name = "Poitou-Charentes";
                    break;
                }
                case 882: {
                    name = "Provence-Alpes-Cote d'Azur";
                    break;
                }
                case 883: {
                    name = "Rhone-Alpes";
                    break;
                }
                case 918: {
                    name = "Alsace";
                }
            }
        }
        if (country_code.equals("GA")) {
            switch (region_code2) {
                case 1: {
                    name = "Estuaire";
                    break;
                }
                case 2: {
                    name = "Haut-Ogooue";
                    break;
                }
                case 3: {
                    name = "Moyen-Ogooue";
                    break;
                }
                case 4: {
                    name = "Ngounie";
                    break;
                }
                case 5: {
                    name = "Nyanga";
                    break;
                }
                case 6: {
                    name = "Ogooue-Ivindo";
                    break;
                }
                case 7: {
                    name = "Ogooue-Lolo";
                    break;
                }
                case 8: {
                    name = "Ogooue-Maritime";
                    break;
                }
                case 9: {
                    name = "Woleu-Ntem";
                }
            }
        }
        if (country_code.equals("GB")) {
            switch (region_code2) {
                case 1: {
                    name = "Avon";
                    break;
                }
                case 3: {
                    name = "Berkshire";
                    break;
                }
                case 7: {
                    name = "Cleveland";
                    break;
                }
                case 17: {
                    name = "Greater London";
                    break;
                }
                case 18: {
                    name = "Greater Manchester";
                    break;
                }
                case 20: {
                    name = "Hereford and Worcester";
                    break;
                }
                case 22: {
                    name = "Humberside";
                    break;
                }
                case 28: {
                    name = "Merseyside";
                    break;
                }
                case 37: {
                    name = "South Yorkshire";
                    break;
                }
                case 41: {
                    name = "Tyne and Wear";
                    break;
                }
                case 43: {
                    name = "West Midlands";
                    break;
                }
                case 45: {
                    name = "West Yorkshire";
                    break;
                }
                case 79: {
                    name = "Central";
                    break;
                }
                case 82: {
                    name = "Grampian";
                    break;
                }
                case 84: {
                    name = "Lothian";
                    break;
                }
                case 87: {
                    name = "Strathclyde";
                    break;
                }
                case 88: {
                    name = "Tayside";
                    break;
                }
                case 90: {
                    name = "Clwyd";
                    break;
                }
                case 91: {
                    name = "Dyfed";
                    break;
                }
                case 92: {
                    name = "Gwent";
                    break;
                }
                case 94: {
                    name = "Mid Glamorgan";
                    break;
                }
                case 96: {
                    name = "South Glamorgan";
                    break;
                }
                case 97: {
                    name = "West Glamorgan";
                    break;
                }
                case 832: {
                    name = "Barking and Dagenham";
                    break;
                }
                case 833: {
                    name = "Barnet";
                    break;
                }
                case 834: {
                    name = "Barnsley";
                    break;
                }
                case 835: {
                    name = "Bath and North East Somerset";
                    break;
                }
                case 836: {
                    name = "Bedfordshire";
                    break;
                }
                case 837: {
                    name = "Bexley";
                    break;
                }
                case 838: {
                    name = "Birmingham";
                    break;
                }
                case 839: {
                    name = "Blackburn with Darwen";
                    break;
                }
                case 840: {
                    name = "Blackpool";
                    break;
                }
                case 875: {
                    name = "Bolton";
                    break;
                }
                case 876: {
                    name = "Bournemouth";
                    break;
                }
                case 877: {
                    name = "Bracknell Forest";
                    break;
                }
                case 878: {
                    name = "Bradford";
                    break;
                }
                case 879: {
                    name = "Brent";
                    break;
                }
                case 880: {
                    name = "Brighton and Hove";
                    break;
                }
                case 881: {
                    name = "Bristol";
                    break;
                }
                case 882: {
                    name = "Bromley";
                    break;
                }
                case 883: {
                    name = "Buckinghamshire";
                    break;
                }
                case 918: {
                    name = "Bury";
                    break;
                }
                case 919: {
                    name = "Calderdale";
                    break;
                }
                case 920: {
                    name = "Cambridgeshire";
                    break;
                }
                case 921: {
                    name = "Camden";
                    break;
                }
                case 922: {
                    name = "Cheshire";
                    break;
                }
                case 923: {
                    name = "Cornwall";
                    break;
                }
                case 924: {
                    name = "Coventry";
                    break;
                }
                case 925: {
                    name = "Croydon";
                    break;
                }
                case 926: {
                    name = "Cumbria";
                    break;
                }
                case 961: {
                    name = "Darlington";
                    break;
                }
                case 962: {
                    name = "Derby";
                    break;
                }
                case 963: {
                    name = "Derbyshire";
                    break;
                }
                case 964: {
                    name = "Devon";
                    break;
                }
                case 965: {
                    name = "Doncaster";
                    break;
                }
                case 966: {
                    name = "Dorset";
                    break;
                }
                case 967: {
                    name = "Dudley";
                    break;
                }
                case 968: {
                    name = "Durham";
                    break;
                }
                case 969: {
                    name = "Ealing";
                    break;
                }
                case 1004: {
                    name = "East Riding of Yorkshire";
                    break;
                }
                case 1005: {
                    name = "East Sussex";
                    break;
                }
                case 1006: {
                    name = "Enfield";
                    break;
                }
                case 1007: {
                    name = "Essex";
                    break;
                }
                case 1008: {
                    name = "Gateshead";
                    break;
                }
                case 1009: {
                    name = "Gloucestershire";
                    break;
                }
                case 1010: {
                    name = "Greenwich";
                    break;
                }
                case 1011: {
                    name = "Hackney";
                    break;
                }
                case 1012: {
                    name = "Halton";
                    break;
                }
                case 1047: {
                    name = "Hammersmith and Fulham";
                    break;
                }
                case 1048: {
                    name = "Hampshire";
                    break;
                }
                case 1049: {
                    name = "Haringey";
                    break;
                }
                case 1050: {
                    name = "Harrow";
                    break;
                }
                case 1051: {
                    name = "Hartlepool";
                    break;
                }
                case 1052: {
                    name = "Havering";
                    break;
                }
                case 1053: {
                    name = "Herefordshire";
                    break;
                }
                case 1054: {
                    name = "Hertford";
                    break;
                }
                case 1055: {
                    name = "Hillingdon";
                    break;
                }
                case 1090: {
                    name = "Hounslow";
                    break;
                }
                case 1091: {
                    name = "Isle of Wight";
                    break;
                }
                case 1092: {
                    name = "Islington";
                    break;
                }
                case 1093: {
                    name = "Kensington and Chelsea";
                    break;
                }
                case 1094: {
                    name = "Kent";
                    break;
                }
                case 1095: {
                    name = "Kingston upon Hull";
                    break;
                }
                case 1096: {
                    name = "Kingston upon Thames";
                    break;
                }
                case 1097: {
                    name = "Kirklees";
                    break;
                }
                case 1098: {
                    name = "Knowsley";
                    break;
                }
                case 1133: {
                    name = "Lambeth";
                    break;
                }
                case 1134: {
                    name = "Lancashire";
                    break;
                }
                case 1135: {
                    name = "Leeds";
                    break;
                }
                case 1136: {
                    name = "Leicester";
                    break;
                }
                case 1137: {
                    name = "Leicestershire";
                    break;
                }
                case 1138: {
                    name = "Lewisham";
                    break;
                }
                case 1139: {
                    name = "Lincolnshire";
                    break;
                }
                case 1140: {
                    name = "Liverpool";
                    break;
                }
                case 1141: {
                    name = "London";
                    break;
                }
                case 1176: {
                    name = "Luton";
                    break;
                }
                case 1177: {
                    name = "Manchester";
                    break;
                }
                case 1178: {
                    name = "Medway";
                    break;
                }
                case 1179: {
                    name = "Merton";
                    break;
                }
                case 1180: {
                    name = "Middlesbrough";
                    break;
                }
                case 1181: {
                    name = "Milton Keynes";
                    break;
                }
                case 1182: {
                    name = "Newcastle upon Tyne";
                    break;
                }
                case 1183: {
                    name = "Newham";
                    break;
                }
                case 1184: {
                    name = "Norfolk";
                    break;
                }
                case 1219: {
                    name = "Northamptonshire";
                    break;
                }
                case 1220: {
                    name = "North East Lincolnshire";
                    break;
                }
                case 1221: {
                    name = "North Lincolnshire";
                    break;
                }
                case 1222: {
                    name = "North Somerset";
                    break;
                }
                case 1223: {
                    name = "North Tyneside";
                    break;
                }
                case 1224: {
                    name = "Northumberland";
                    break;
                }
                case 1225: {
                    name = "North Yorkshire";
                    break;
                }
                case 1226: {
                    name = "Nottingham";
                    break;
                }
                case 1227: {
                    name = "Nottinghamshire";
                    break;
                }
                case 1262: {
                    name = "Oldham";
                    break;
                }
                case 1263: {
                    name = "Oxfordshire";
                    break;
                }
                case 1264: {
                    name = "Peterborough";
                    break;
                }
                case 1265: {
                    name = "Plymouth";
                    break;
                }
                case 1266: {
                    name = "Poole";
                    break;
                }
                case 1267: {
                    name = "Portsmouth";
                    break;
                }
                case 1268: {
                    name = "Reading";
                    break;
                }
                case 1269: {
                    name = "Redbridge";
                    break;
                }
                case 1270: {
                    name = "Redcar and Cleveland";
                    break;
                }
                case 1305: {
                    name = "Richmond upon Thames";
                    break;
                }
                case 1306: {
                    name = "Rochdale";
                    break;
                }
                case 1307: {
                    name = "Rotherham";
                    break;
                }
                case 1308: {
                    name = "Rutland";
                    break;
                }
                case 1309: {
                    name = "Salford";
                    break;
                }
                case 1310: {
                    name = "Shropshire";
                    break;
                }
                case 1311: {
                    name = "Sandwell";
                    break;
                }
                case 1312: {
                    name = "Sefton";
                    break;
                }
                case 1313: {
                    name = "Sheffield";
                    break;
                }
                case 1348: {
                    name = "Slough";
                    break;
                }
                case 1349: {
                    name = "Solihull";
                    break;
                }
                case 1350: {
                    name = "Somerset";
                    break;
                }
                case 1351: {
                    name = "Southampton";
                    break;
                }
                case 1352: {
                    name = "Southend-on-Sea";
                    break;
                }
                case 1353: {
                    name = "South Gloucestershire";
                    break;
                }
                case 1354: {
                    name = "South Tyneside";
                    break;
                }
                case 1355: {
                    name = "Southwark";
                    break;
                }
                case 1356: {
                    name = "Staffordshire";
                    break;
                }
                case 1391: {
                    name = "St. Helens";
                    break;
                }
                case 1392: {
                    name = "Stockport";
                    break;
                }
                case 1393: {
                    name = "Stockton-on-Tees";
                    break;
                }
                case 1394: {
                    name = "Stoke-on-Trent";
                    break;
                }
                case 1395: {
                    name = "Suffolk";
                    break;
                }
                case 1396: {
                    name = "Sunderland";
                    break;
                }
                case 1397: {
                    name = "Surrey";
                    break;
                }
                case 1398: {
                    name = "Sutton";
                    break;
                }
                case 1399: {
                    name = "Swindon";
                    break;
                }
                case 1434: {
                    name = "Tameside";
                    break;
                }
                case 1435: {
                    name = "Telford and Wrekin";
                    break;
                }
                case 1436: {
                    name = "Thurrock";
                    break;
                }
                case 1437: {
                    name = "Torbay";
                    break;
                }
                case 1438: {
                    name = "Tower Hamlets";
                    break;
                }
                case 1439: {
                    name = "Trafford";
                    break;
                }
                case 1440: {
                    name = "Wakefield";
                    break;
                }
                case 1441: {
                    name = "Walsall";
                    break;
                }
                case 1442: {
                    name = "Waltham Forest";
                    break;
                }
                case 1477: {
                    name = "Wandsworth";
                    break;
                }
                case 1478: {
                    name = "Warrington";
                    break;
                }
                case 1479: {
                    name = "Warwickshire";
                    break;
                }
                case 1480: {
                    name = "West Berkshire";
                    break;
                }
                case 1481: {
                    name = "Westminster";
                    break;
                }
                case 1482: {
                    name = "West Sussex";
                    break;
                }
                case 1483: {
                    name = "Wigan";
                    break;
                }
                case 1484: {
                    name = "Wiltshire";
                    break;
                }
                case 1485: {
                    name = "Windsor and Maidenhead";
                    break;
                }
                case 1520: {
                    name = "Wirral";
                    break;
                }
                case 1521: {
                    name = "Wokingham";
                    break;
                }
                case 1522: {
                    name = "Wolverhampton";
                    break;
                }
                case 1523: {
                    name = "Worcestershire";
                    break;
                }
                case 1524: {
                    name = "York";
                    break;
                }
                case 1525: {
                    name = "Antrim";
                    break;
                }
                case 1526: {
                    name = "Ards";
                    break;
                }
                case 1527: {
                    name = "Armagh";
                    break;
                }
                case 1528: {
                    name = "Ballymena";
                    break;
                }
                case 1563: {
                    name = "Ballymoney";
                    break;
                }
                case 1564: {
                    name = "Banbridge";
                    break;
                }
                case 1565: {
                    name = "Belfast";
                    break;
                }
                case 1566: {
                    name = "Carrickfergus";
                    break;
                }
                case 1567: {
                    name = "Castlereagh";
                    break;
                }
                case 1568: {
                    name = "Coleraine";
                    break;
                }
                case 1569: {
                    name = "Cookstown";
                    break;
                }
                case 1570: {
                    name = "Craigavon";
                    break;
                }
                case 1571: {
                    name = "Down";
                    break;
                }
                case 1606: {
                    name = "Dungannon";
                    break;
                }
                case 1607: {
                    name = "Fermanagh";
                    break;
                }
                case 1608: {
                    name = "Larne";
                    break;
                }
                case 1609: {
                    name = "Limavady";
                    break;
                }
                case 1610: {
                    name = "Lisburn";
                    break;
                }
                case 1611: {
                    name = "Derry";
                    break;
                }
                case 1612: {
                    name = "Magherafelt";
                    break;
                }
                case 1613: {
                    name = "Moyle";
                    break;
                }
                case 1614: {
                    name = "Newry and Mourne";
                    break;
                }
                case 1649: {
                    name = "Newtownabbey";
                    break;
                }
                case 1650: {
                    name = "North Down";
                    break;
                }
                case 1651: {
                    name = "Omagh";
                    break;
                }
                case 1652: {
                    name = "Strabane";
                    break;
                }
                case 1653: {
                    name = "Aberdeen City";
                    break;
                }
                case 1654: {
                    name = "Aberdeenshire";
                    break;
                }
                case 1655: {
                    name = "Angus";
                    break;
                }
                case 1656: {
                    name = "Argyll and Bute";
                    break;
                }
                case 1657: {
                    name = "Scottish Borders";
                    break;
                }
                case 1692: {
                    name = "Clackmannanshire";
                    break;
                }
                case 1693: {
                    name = "Dumfries and Galloway";
                    break;
                }
                case 1694: {
                    name = "Dundee City";
                    break;
                }
                case 1695: {
                    name = "East Ayrshire";
                    break;
                }
                case 1696: {
                    name = "East Dunbartonshire";
                    break;
                }
                case 1697: {
                    name = "East Lothian";
                    break;
                }
                case 1698: {
                    name = "East Renfrewshire";
                    break;
                }
                case 1699: {
                    name = "Edinburgh";
                    break;
                }
                case 1700: {
                    name = "Falkirk";
                    break;
                }
                case 1735: {
                    name = "Fife";
                    break;
                }
                case 1736: {
                    name = "Glasgow City";
                    break;
                }
                case 1737: {
                    name = "Highland";
                    break;
                }
                case 1738: {
                    name = "Inverclyde";
                    break;
                }
                case 1739: {
                    name = "Midlothian";
                    break;
                }
                case 1740: {
                    name = "Moray";
                    break;
                }
                case 1741: {
                    name = "North Ayrshire";
                    break;
                }
                case 1742: {
                    name = "North Lanarkshire";
                    break;
                }
                case 1743: {
                    name = "Orkney";
                    break;
                }
                case 1778: {
                    name = "Perth and Kinross";
                    break;
                }
                case 1779: {
                    name = "Renfrewshire";
                    break;
                }
                case 1780: {
                    name = "Shetland Islands";
                    break;
                }
                case 1781: {
                    name = "South Ayrshire";
                    break;
                }
                case 1782: {
                    name = "South Lanarkshire";
                    break;
                }
                case 1783: {
                    name = "Stirling";
                    break;
                }
                case 1784: {
                    name = "West Dunbartonshire";
                    break;
                }
                case 1785: {
                    name = "Eilean Siar";
                    break;
                }
                case 1786: {
                    name = "West Lothian";
                    break;
                }
                case 1821: {
                    name = "Isle of Anglesey";
                    break;
                }
                case 1822: {
                    name = "Blaenau Gwent";
                    break;
                }
                case 1823: {
                    name = "Bridgend";
                    break;
                }
                case 1824: {
                    name = "Caerphilly";
                    break;
                }
                case 1825: {
                    name = "Cardiff";
                    break;
                }
                case 1826: {
                    name = "Ceredigion";
                    break;
                }
                case 1827: {
                    name = "Carmarthenshire";
                    break;
                }
                case 1828: {
                    name = "Conwy";
                    break;
                }
                case 1829: {
                    name = "Denbighshire";
                    break;
                }
                case 1864: {
                    name = "Flintshire";
                    break;
                }
                case 1865: {
                    name = "Gwynedd";
                    break;
                }
                case 1866: {
                    name = "Merthyr Tydfil";
                    break;
                }
                case 1867: {
                    name = "Monmouthshire";
                    break;
                }
                case 1868: {
                    name = "Neath Port Talbot";
                    break;
                }
                case 1869: {
                    name = "Newport";
                    break;
                }
                case 1870: {
                    name = "Pembrokeshire";
                    break;
                }
                case 1871: {
                    name = "Powys";
                    break;
                }
                case 1872: {
                    name = "Rhondda Cynon Taff";
                    break;
                }
                case 1907: {
                    name = "Swansea";
                    break;
                }
                case 1908: {
                    name = "Torfaen";
                    break;
                }
                case 1909: {
                    name = "Vale of Glamorgan";
                    break;
                }
                case 1910: {
                    name = "Wrexham";
                }
            }
        }
        if (country_code.equals("GD")) {
            switch (region_code2) {
                case 1: {
                    name = "Saint Andrew";
                    break;
                }
                case 2: {
                    name = "Saint David";
                    break;
                }
                case 3: {
                    name = "Saint George";
                    break;
                }
                case 4: {
                    name = "Saint John";
                    break;
                }
                case 5: {
                    name = "Saint Mark";
                    break;
                }
                case 6: {
                    name = "Saint Patrick";
                }
            }
        }
        if (country_code.equals("GE")) {
            switch (region_code2) {
                case 1: {
                    name = "Abashis Raioni";
                    break;
                }
                case 2: {
                    name = "Abkhazia";
                    break;
                }
                case 3: {
                    name = "Adigenis Raioni";
                    break;
                }
                case 4: {
                    name = "Ajaria";
                    break;
                }
                case 5: {
                    name = "Akhalgoris Raioni";
                    break;
                }
                case 6: {
                    name = "Akhalk'alak'is Raioni";
                    break;
                }
                case 7: {
                    name = "Akhalts'ikhis Raioni";
                    break;
                }
                case 8: {
                    name = "Akhmetis Raioni";
                    break;
                }
                case 9: {
                    name = "Ambrolauris Raioni";
                    break;
                }
                case 10: {
                    name = "Aspindzis Raioni";
                    break;
                }
                case 11: {
                    name = "Baghdat'is Raioni";
                    break;
                }
                case 12: {
                    name = "Bolnisis Raioni";
                    break;
                }
                case 13: {
                    name = "Borjomis Raioni";
                    break;
                }
                case 14: {
                    name = "Chiat'ura";
                    break;
                }
                case 15: {
                    name = "Ch'khorotsqus Raioni";
                    break;
                }
                case 16: {
                    name = "Ch'okhatauris Raioni";
                    break;
                }
                case 17: {
                    name = "Dedop'listsqaros Raioni";
                    break;
                }
                case 18: {
                    name = "Dmanisis Raioni";
                    break;
                }
                case 19: {
                    name = "Dushet'is Raioni";
                    break;
                }
                case 20: {
                    name = "Gardabanis Raioni";
                    break;
                }
                case 21: {
                    name = "Gori";
                    break;
                }
                case 22: {
                    name = "Goris Raioni";
                    break;
                }
                case 23: {
                    name = "Gurjaanis Raioni";
                    break;
                }
                case 24: {
                    name = "Javis Raioni";
                    break;
                }
                case 25: {
                    name = "K'arelis Raioni";
                    break;
                }
                case 26: {
                    name = "Kaspis Raioni";
                    break;
                }
                case 27: {
                    name = "Kharagaulis Raioni";
                    break;
                }
                case 28: {
                    name = "Khashuris Raioni";
                    break;
                }
                case 29: {
                    name = "Khobis Raioni";
                    break;
                }
                case 30: {
                    name = "Khonis Raioni";
                    break;
                }
                case 31: {
                    name = "K'ut'aisi";
                    break;
                }
                case 32: {
                    name = "Lagodekhis Raioni";
                    break;
                }
                case 33: {
                    name = "Lanch'khut'is Raioni";
                    break;
                }
                case 34: {
                    name = "Lentekhis Raioni";
                    break;
                }
                case 35: {
                    name = "Marneulis Raioni";
                    break;
                }
                case 36: {
                    name = "Martvilis Raioni";
                    break;
                }
                case 37: {
                    name = "Mestiis Raioni";
                    break;
                }
                case 38: {
                    name = "Mts'khet'is Raioni";
                    break;
                }
                case 39: {
                    name = "Ninotsmindis Raioni";
                    break;
                }
                case 40: {
                    name = "Onis Raioni";
                    break;
                }
                case 41: {
                    name = "Ozurget'is Raioni";
                    break;
                }
                case 42: {
                    name = "P'ot'i";
                    break;
                }
                case 43: {
                    name = "Qazbegis Raioni";
                    break;
                }
                case 44: {
                    name = "Qvarlis Raioni";
                    break;
                }
                case 45: {
                    name = "Rust'avi";
                    break;
                }
                case 46: {
                    name = "Sach'kheris Raioni";
                    break;
                }
                case 47: {
                    name = "Sagarejos Raioni";
                    break;
                }
                case 48: {
                    name = "Samtrediis Raioni";
                    break;
                }
                case 49: {
                    name = "Senakis Raioni";
                    break;
                }
                case 50: {
                    name = "Sighnaghis Raioni";
                    break;
                }
                case 51: {
                    name = "T'bilisi";
                    break;
                }
                case 52: {
                    name = "T'elavis Raioni";
                    break;
                }
                case 53: {
                    name = "T'erjolis Raioni";
                    break;
                }
                case 54: {
                    name = "T'et'ritsqaros Raioni";
                    break;
                }
                case 55: {
                    name = "T'ianet'is Raioni";
                    break;
                }
                case 56: {
                    name = "Tqibuli";
                    break;
                }
                case 57: {
                    name = "Ts'ageris Raioni";
                    break;
                }
                case 58: {
                    name = "Tsalenjikhis Raioni";
                    break;
                }
                case 59: {
                    name = "Tsalkis Raioni";
                    break;
                }
                case 60: {
                    name = "Tsqaltubo";
                    break;
                }
                case 61: {
                    name = "Vanis Raioni";
                    break;
                }
                case 62: {
                    name = "Zestap'onis Raioni";
                    break;
                }
                case 63: {
                    name = "Zugdidi";
                    break;
                }
                case 64: {
                    name = "Zugdidis Raioni";
                }
            }
        }
        if (country_code.equals("GH")) {
            switch (region_code2) {
                case 1: {
                    name = "Greater Accra";
                    break;
                }
                case 2: {
                    name = "Ashanti";
                    break;
                }
                case 3: {
                    name = "Brong-Ahafo";
                    break;
                }
                case 4: {
                    name = "Central";
                    break;
                }
                case 5: {
                    name = "Eastern";
                    break;
                }
                case 6: {
                    name = "Northern";
                    break;
                }
                case 8: {
                    name = "Volta";
                    break;
                }
                case 9: {
                    name = "Western";
                    break;
                }
                case 10: {
                    name = "Upper East";
                    break;
                }
                case 11: {
                    name = "Upper West";
                }
            }
        }
        if (country_code.equals("GL")) {
            switch (region_code2) {
                case 1: {
                    name = "Nordgronland";
                    break;
                }
                case 2: {
                    name = "Ostgronland";
                    break;
                }
                case 3: {
                    name = "Vestgronland";
                }
            }
        }
        if (country_code.equals("GM")) {
            switch (region_code2) {
                case 1: {
                    name = "Banjul";
                    break;
                }
                case 2: {
                    name = "Lower River";
                    break;
                }
                case 3: {
                    name = "Central River";
                    break;
                }
                case 4: {
                    name = "Upper River";
                    break;
                }
                case 5: {
                    name = "Western";
                    break;
                }
                case 7: {
                    name = "North Bank";
                }
            }
        }
        if (country_code.equals("GN")) {
            switch (region_code2) {
                case 1: {
                    name = "Beyla";
                    break;
                }
                case 2: {
                    name = "Boffa";
                    break;
                }
                case 3: {
                    name = "Boke";
                    break;
                }
                case 4: {
                    name = "Conakry";
                    break;
                }
                case 5: {
                    name = "Dabola";
                    break;
                }
                case 6: {
                    name = "Dalaba";
                    break;
                }
                case 7: {
                    name = "Dinguiraye";
                    break;
                }
                case 9: {
                    name = "Faranah";
                    break;
                }
                case 10: {
                    name = "Forecariah";
                    break;
                }
                case 11: {
                    name = "Fria";
                    break;
                }
                case 12: {
                    name = "Gaoual";
                    break;
                }
                case 13: {
                    name = "Gueckedou";
                    break;
                }
                case 15: {
                    name = "Kerouane";
                    break;
                }
                case 16: {
                    name = "Kindia";
                    break;
                }
                case 17: {
                    name = "Kissidougou";
                    break;
                }
                case 18: {
                    name = "Koundara";
                    break;
                }
                case 19: {
                    name = "Kouroussa";
                    break;
                }
                case 21: {
                    name = "Macenta";
                    break;
                }
                case 22: {
                    name = "Mali";
                    break;
                }
                case 23: {
                    name = "Mamou";
                    break;
                }
                case 25: {
                    name = "Pita";
                    break;
                }
                case 27: {
                    name = "Telimele";
                    break;
                }
                case 28: {
                    name = "Tougue";
                    break;
                }
                case 29: {
                    name = "Yomou";
                    break;
                }
                case 30: {
                    name = "Coyah";
                    break;
                }
                case 31: {
                    name = "Dubreka";
                    break;
                }
                case 32: {
                    name = "Kankan";
                    break;
                }
                case 33: {
                    name = "Koubia";
                    break;
                }
                case 34: {
                    name = "Labe";
                    break;
                }
                case 35: {
                    name = "Lelouma";
                    break;
                }
                case 36: {
                    name = "Lola";
                    break;
                }
                case 37: {
                    name = "Mandiana";
                    break;
                }
                case 38: {
                    name = "Nzerekore";
                    break;
                }
                case 39: {
                    name = "Siguiri";
                }
            }
        }
        if (country_code.equals("GQ")) {
            switch (region_code2) {
                case 3: {
                    name = "Annobon";
                    break;
                }
                case 4: {
                    name = "Bioko Norte";
                    break;
                }
                case 5: {
                    name = "Bioko Sur";
                    break;
                }
                case 6: {
                    name = "Centro Sur";
                    break;
                }
                case 7: {
                    name = "Kie-Ntem";
                    break;
                }
                case 8: {
                    name = "Litoral";
                    break;
                }
                case 9: {
                    name = "Wele-Nzas";
                }
            }
        }
        if (country_code.equals("GR")) {
            switch (region_code2) {
                case 1: {
                    name = "Evros";
                    break;
                }
                case 2: {
                    name = "Rodhopi";
                    break;
                }
                case 3: {
                    name = "Xanthi";
                    break;
                }
                case 4: {
                    name = "Drama";
                    break;
                }
                case 5: {
                    name = "Serrai";
                    break;
                }
                case 6: {
                    name = "Kilkis";
                    break;
                }
                case 7: {
                    name = "Pella";
                    break;
                }
                case 8: {
                    name = "Florina";
                    break;
                }
                case 9: {
                    name = "Kastoria";
                    break;
                }
                case 10: {
                    name = "Grevena";
                    break;
                }
                case 11: {
                    name = "Kozani";
                    break;
                }
                case 12: {
                    name = "Imathia";
                    break;
                }
                case 13: {
                    name = "Thessaloniki";
                    break;
                }
                case 14: {
                    name = "Kavala";
                    break;
                }
                case 15: {
                    name = "Khalkidhiki";
                    break;
                }
                case 16: {
                    name = "Pieria";
                    break;
                }
                case 17: {
                    name = "Ioannina";
                    break;
                }
                case 18: {
                    name = "Thesprotia";
                    break;
                }
                case 19: {
                    name = "Preveza";
                    break;
                }
                case 20: {
                    name = "Arta";
                    break;
                }
                case 21: {
                    name = "Larisa";
                    break;
                }
                case 22: {
                    name = "Trikala";
                    break;
                }
                case 23: {
                    name = "Kardhitsa";
                    break;
                }
                case 24: {
                    name = "Magnisia";
                    break;
                }
                case 25: {
                    name = "Kerkira";
                    break;
                }
                case 26: {
                    name = "Levkas";
                    break;
                }
                case 27: {
                    name = "Kefallinia";
                    break;
                }
                case 28: {
                    name = "Zakinthos";
                    break;
                }
                case 29: {
                    name = "Fthiotis";
                    break;
                }
                case 30: {
                    name = "Evritania";
                    break;
                }
                case 31: {
                    name = "Aitolia kai Akarnania";
                    break;
                }
                case 32: {
                    name = "Fokis";
                    break;
                }
                case 33: {
                    name = "Voiotia";
                    break;
                }
                case 34: {
                    name = "Evvoia";
                    break;
                }
                case 35: {
                    name = "Attiki";
                    break;
                }
                case 36: {
                    name = "Argolis";
                    break;
                }
                case 37: {
                    name = "Korinthia";
                    break;
                }
                case 38: {
                    name = "Akhaia";
                    break;
                }
                case 39: {
                    name = "Ilia";
                    break;
                }
                case 40: {
                    name = "Messinia";
                    break;
                }
                case 41: {
                    name = "Arkadhia";
                    break;
                }
                case 42: {
                    name = "Lakonia";
                    break;
                }
                case 43: {
                    name = "Khania";
                    break;
                }
                case 44: {
                    name = "Rethimni";
                    break;
                }
                case 45: {
                    name = "Iraklion";
                    break;
                }
                case 46: {
                    name = "Lasithi";
                    break;
                }
                case 47: {
                    name = "Dhodhekanisos";
                    break;
                }
                case 48: {
                    name = "Samos";
                    break;
                }
                case 49: {
                    name = "Kikladhes";
                    break;
                }
                case 50: {
                    name = "Khios";
                    break;
                }
                case 51: {
                    name = "Lesvos";
                }
            }
        }
        if (country_code.equals("GT")) {
            switch (region_code2) {
                case 1: {
                    name = "Alta Verapaz";
                    break;
                }
                case 2: {
                    name = "Baja Verapaz";
                    break;
                }
                case 3: {
                    name = "Chimaltenango";
                    break;
                }
                case 4: {
                    name = "Chiquimula";
                    break;
                }
                case 5: {
                    name = "El Progreso";
                    break;
                }
                case 6: {
                    name = "Escuintla";
                    break;
                }
                case 7: {
                    name = "Guatemala";
                    break;
                }
                case 8: {
                    name = "Huehuetenango";
                    break;
                }
                case 9: {
                    name = "Izabal";
                    break;
                }
                case 10: {
                    name = "Jalapa";
                    break;
                }
                case 11: {
                    name = "Jutiapa";
                    break;
                }
                case 12: {
                    name = "Peten";
                    break;
                }
                case 13: {
                    name = "Quetzaltenango";
                    break;
                }
                case 14: {
                    name = "Quiche";
                    break;
                }
                case 15: {
                    name = "Retalhuleu";
                    break;
                }
                case 16: {
                    name = "Sacatepequez";
                    break;
                }
                case 17: {
                    name = "San Marcos";
                    break;
                }
                case 18: {
                    name = "Santa Rosa";
                    break;
                }
                case 19: {
                    name = "Solola";
                    break;
                }
                case 20: {
                    name = "Suchitepequez";
                    break;
                }
                case 21: {
                    name = "Totonicapan";
                    break;
                }
                case 22: {
                    name = "Zacapa";
                }
            }
        }
        if (country_code.equals("GW")) {
            switch (region_code2) {
                case 1: {
                    name = "Bafata";
                    break;
                }
                case 2: {
                    name = "Quinara";
                    break;
                }
                case 4: {
                    name = "Oio";
                    break;
                }
                case 5: {
                    name = "Bolama";
                    break;
                }
                case 6: {
                    name = "Cacheu";
                    break;
                }
                case 7: {
                    name = "Tombali";
                    break;
                }
                case 10: {
                    name = "Gabu";
                    break;
                }
                case 11: {
                    name = "Bissau";
                    break;
                }
                case 12: {
                    name = "Biombo";
                }
            }
        }
        if (country_code.equals("GY")) {
            switch (region_code2) {
                case 10: {
                    name = "Barima-Waini";
                    break;
                }
                case 11: {
                    name = "Cuyuni-Mazaruni";
                    break;
                }
                case 12: {
                    name = "Demerara-Mahaica";
                    break;
                }
                case 13: {
                    name = "East Berbice-Corentyne";
                    break;
                }
                case 14: {
                    name = "Essequibo Islands-West Demerara";
                    break;
                }
                case 15: {
                    name = "Mahaica-Berbice";
                    break;
                }
                case 16: {
                    name = "Pomeroon-Supenaam";
                    break;
                }
                case 17: {
                    name = "Potaro-Siparuni";
                    break;
                }
                case 18: {
                    name = "Upper Demerara-Berbice";
                    break;
                }
                case 19: {
                    name = "Upper Takutu-Upper Essequibo";
                }
            }
        }
        if (country_code.equals("HN")) {
            switch (region_code2) {
                case 1: {
                    name = "Atlantida";
                    break;
                }
                case 2: {
                    name = "Choluteca";
                    break;
                }
                case 3: {
                    name = "Colon";
                    break;
                }
                case 4: {
                    name = "Comayagua";
                    break;
                }
                case 5: {
                    name = "Copan";
                    break;
                }
                case 6: {
                    name = "Cortes";
                    break;
                }
                case 7: {
                    name = "El Paraiso";
                    break;
                }
                case 8: {
                    name = "Francisco Morazan";
                    break;
                }
                case 9: {
                    name = "Gracias a Dios";
                    break;
                }
                case 10: {
                    name = "Intibuca";
                    break;
                }
                case 11: {
                    name = "Islas de la Bahia";
                    break;
                }
                case 12: {
                    name = "La Paz";
                    break;
                }
                case 13: {
                    name = "Lempira";
                    break;
                }
                case 14: {
                    name = "Ocotepeque";
                    break;
                }
                case 15: {
                    name = "Olancho";
                    break;
                }
                case 16: {
                    name = "Santa Barbara";
                    break;
                }
                case 17: {
                    name = "Valle";
                    break;
                }
                case 18: {
                    name = "Yoro";
                }
            }
        }
        if (country_code.equals("HR")) {
            switch (region_code2) {
                case 1: {
                    name = "Bjelovarsko-Bilogorska";
                    break;
                }
                case 2: {
                    name = "Brodsko-Posavska";
                    break;
                }
                case 3: {
                    name = "Dubrovacko-Neretvanska";
                    break;
                }
                case 4: {
                    name = "Istarska";
                    break;
                }
                case 5: {
                    name = "Karlovacka";
                    break;
                }
                case 6: {
                    name = "Koprivnicko-Krizevacka";
                    break;
                }
                case 7: {
                    name = "Krapinsko-Zagorska";
                    break;
                }
                case 8: {
                    name = "Licko-Senjska";
                    break;
                }
                case 9: {
                    name = "Medimurska";
                    break;
                }
                case 10: {
                    name = "Osjecko-Baranjska";
                    break;
                }
                case 11: {
                    name = "Pozesko-Slavonska";
                    break;
                }
                case 12: {
                    name = "Primorsko-Goranska";
                    break;
                }
                case 13: {
                    name = "Sibensko-Kninska";
                    break;
                }
                case 14: {
                    name = "Sisacko-Moslavacka";
                    break;
                }
                case 15: {
                    name = "Splitsko-Dalmatinska";
                    break;
                }
                case 16: {
                    name = "Varazdinska";
                    break;
                }
                case 17: {
                    name = "Viroviticko-Podravska";
                    break;
                }
                case 18: {
                    name = "Vukovarsko-Srijemska";
                    break;
                }
                case 19: {
                    name = "Zadarska";
                    break;
                }
                case 20: {
                    name = "Zagrebacka";
                    break;
                }
                case 21: {
                    name = "Grad Zagreb";
                }
            }
        }
        if (country_code.equals("HT")) {
            switch (region_code2) {
                case 3: {
                    name = "Nord-Ouest";
                    break;
                }
                case 6: {
                    name = "Artibonite";
                    break;
                }
                case 7: {
                    name = "Centre";
                    break;
                }
                case 9: {
                    name = "Nord";
                    break;
                }
                case 10: {
                    name = "Nord-Est";
                    break;
                }
                case 11: {
                    name = "Ouest";
                    break;
                }
                case 12: {
                    name = "Sud";
                    break;
                }
                case 13: {
                    name = "Sud-Est";
                    break;
                }
                case 14: {
                    name = "Grand' Anse";
                    break;
                }
                case 15: {
                    name = "Nippes";
                }
            }
        }
        if (country_code.equals("HU")) {
            switch (region_code2) {
                case 1: {
                    name = "Bacs-Kiskun";
                    break;
                }
                case 2: {
                    name = "Baranya";
                    break;
                }
                case 3: {
                    name = "Bekes";
                    break;
                }
                case 4: {
                    name = "Borsod-Abauj-Zemplen";
                    break;
                }
                case 5: {
                    name = "Budapest";
                    break;
                }
                case 6: {
                    name = "Csongrad";
                    break;
                }
                case 7: {
                    name = "Debrecen";
                    break;
                }
                case 8: {
                    name = "Fejer";
                    break;
                }
                case 9: {
                    name = "Gyor-Moson-Sopron";
                    break;
                }
                case 10: {
                    name = "Hajdu-Bihar";
                    break;
                }
                case 11: {
                    name = "Heves";
                    break;
                }
                case 12: {
                    name = "Komarom-Esztergom";
                    break;
                }
                case 13: {
                    name = "Miskolc";
                    break;
                }
                case 14: {
                    name = "Nograd";
                    break;
                }
                case 15: {
                    name = "Pecs";
                    break;
                }
                case 16: {
                    name = "Pest";
                    break;
                }
                case 17: {
                    name = "Somogy";
                    break;
                }
                case 18: {
                    name = "Szabolcs-Szatmar-Bereg";
                    break;
                }
                case 19: {
                    name = "Szeged";
                    break;
                }
                case 20: {
                    name = "Jasz-Nagykun-Szolnok";
                    break;
                }
                case 21: {
                    name = "Tolna";
                    break;
                }
                case 22: {
                    name = "Vas";
                    break;
                }
                case 23: {
                    name = "Veszprem";
                    break;
                }
                case 24: {
                    name = "Zala";
                    break;
                }
                case 25: {
                    name = "Gyor";
                    break;
                }
                case 26: {
                    name = "Bekescsaba";
                    break;
                }
                case 27: {
                    name = "Dunaujvaros";
                    break;
                }
                case 28: {
                    name = "Eger";
                    break;
                }
                case 29: {
                    name = "Hodmezovasarhely";
                    break;
                }
                case 30: {
                    name = "Kaposvar";
                    break;
                }
                case 31: {
                    name = "Kecskemet";
                    break;
                }
                case 32: {
                    name = "Nagykanizsa";
                    break;
                }
                case 33: {
                    name = "Nyiregyhaza";
                    break;
                }
                case 34: {
                    name = "Sopron";
                    break;
                }
                case 35: {
                    name = "Szekesfehervar";
                    break;
                }
                case 36: {
                    name = "Szolnok";
                    break;
                }
                case 37: {
                    name = "Szombathely";
                    break;
                }
                case 38: {
                    name = "Tatabanya";
                    break;
                }
                case 39: {
                    name = "Veszprem";
                    break;
                }
                case 40: {
                    name = "Zalaegerszeg";
                    break;
                }
                case 41: {
                    name = "Salgotarjan";
                    break;
                }
                case 42: {
                    name = "Szekszard";
                }
            }
        }
        if (country_code.equals("ID")) {
            switch (region_code2) {
                case 1: {
                    name = "Aceh";
                    break;
                }
                case 2: {
                    name = "Bali";
                    break;
                }
                case 3: {
                    name = "Bengkulu";
                    break;
                }
                case 4: {
                    name = "Jakarta Raya";
                    break;
                }
                case 5: {
                    name = "Jambi";
                    break;
                }
                case 6: {
                    name = "Jawa Barat";
                    break;
                }
                case 7: {
                    name = "Jawa Tengah";
                    break;
                }
                case 8: {
                    name = "Jawa Timur";
                    break;
                }
                case 9: {
                    name = "Papua";
                    break;
                }
                case 10: {
                    name = "Yogyakarta";
                    break;
                }
                case 11: {
                    name = "Kalimantan Barat";
                    break;
                }
                case 12: {
                    name = "Kalimantan Selatan";
                    break;
                }
                case 13: {
                    name = "Kalimantan Tengah";
                    break;
                }
                case 14: {
                    name = "Kalimantan Timur";
                    break;
                }
                case 15: {
                    name = "Lampung";
                    break;
                }
                case 16: {
                    name = "Maluku";
                    break;
                }
                case 17: {
                    name = "Nusa Tenggara Barat";
                    break;
                }
                case 18: {
                    name = "Nusa Tenggara Timur";
                    break;
                }
                case 19: {
                    name = "Riau";
                    break;
                }
                case 20: {
                    name = "Sulawesi Selatan";
                    break;
                }
                case 21: {
                    name = "Sulawesi Tengah";
                    break;
                }
                case 22: {
                    name = "Sulawesi Tenggara";
                    break;
                }
                case 23: {
                    name = "Sulawesi Utara";
                    break;
                }
                case 24: {
                    name = "Sumatera Barat";
                    break;
                }
                case 25: {
                    name = "Sumatera Selatan";
                    break;
                }
                case 26: {
                    name = "Sumatera Utara";
                    break;
                }
                case 28: {
                    name = "Maluku";
                    break;
                }
                case 29: {
                    name = "Maluku Utara";
                    break;
                }
                case 30: {
                    name = "Jawa Barat";
                    break;
                }
                case 31: {
                    name = "Sulawesi Utara";
                    break;
                }
                case 32: {
                    name = "Sumatera Selatan";
                    break;
                }
                case 33: {
                    name = "Banten";
                    break;
                }
                case 34: {
                    name = "Gorontalo";
                    break;
                }
                case 35: {
                    name = "Kepulauan Bangka Belitung";
                    break;
                }
                case 36: {
                    name = "Papua";
                    break;
                }
                case 37: {
                    name = "Riau";
                    break;
                }
                case 38: {
                    name = "Sulawesi Selatan";
                    break;
                }
                case 39: {
                    name = "Irian Jaya Barat";
                    break;
                }
                case 40: {
                    name = "Kepulauan Riau";
                    break;
                }
                case 41: {
                    name = "Sulawesi Barat";
                }
            }
        }
        if (country_code.equals("IE")) {
            switch (region_code2) {
                case 1: {
                    name = "Carlow";
                    break;
                }
                case 2: {
                    name = "Cavan";
                    break;
                }
                case 3: {
                    name = "Clare";
                    break;
                }
                case 4: {
                    name = "Cork";
                    break;
                }
                case 6: {
                    name = "Donegal";
                    break;
                }
                case 7: {
                    name = "Dublin";
                    break;
                }
                case 10: {
                    name = "Galway";
                    break;
                }
                case 11: {
                    name = "Kerry";
                    break;
                }
                case 12: {
                    name = "Kildare";
                    break;
                }
                case 13: {
                    name = "Kilkenny";
                    break;
                }
                case 14: {
                    name = "Leitrim";
                    break;
                }
                case 15: {
                    name = "Laois";
                    break;
                }
                case 16: {
                    name = "Limerick";
                    break;
                }
                case 18: {
                    name = "Longford";
                    break;
                }
                case 19: {
                    name = "Louth";
                    break;
                }
                case 20: {
                    name = "Mayo";
                    break;
                }
                case 21: {
                    name = "Meath";
                    break;
                }
                case 22: {
                    name = "Monaghan";
                    break;
                }
                case 23: {
                    name = "Offaly";
                    break;
                }
                case 24: {
                    name = "Roscommon";
                    break;
                }
                case 25: {
                    name = "Sligo";
                    break;
                }
                case 26: {
                    name = "Tipperary";
                    break;
                }
                case 27: {
                    name = "Waterford";
                    break;
                }
                case 29: {
                    name = "Westmeath";
                    break;
                }
                case 30: {
                    name = "Wexford";
                    break;
                }
                case 31: {
                    name = "Wicklow";
                }
            }
        }
        if (country_code.equals("IL")) {
            switch (region_code2) {
                case 1: {
                    name = "HaDarom";
                    break;
                }
                case 2: {
                    name = "HaMerkaz";
                    break;
                }
                case 3: {
                    name = "HaZafon";
                    break;
                }
                case 4: {
                    name = "Hefa";
                    break;
                }
                case 5: {
                    name = "Tel Aviv";
                    break;
                }
                case 6: {
                    name = "Yerushalayim";
                }
            }
        }
        if (country_code.equals("IN")) {
            switch (region_code2) {
                case 1: {
                    name = "Andaman and Nicobar Islands";
                    break;
                }
                case 2: {
                    name = "Andhra Pradesh";
                    break;
                }
                case 3: {
                    name = "Assam";
                    break;
                }
                case 5: {
                    name = "Chandigarh";
                    break;
                }
                case 6: {
                    name = "Dadra and Nagar Haveli";
                    break;
                }
                case 7: {
                    name = "Delhi";
                    break;
                }
                case 9: {
                    name = "Gujarat";
                    break;
                }
                case 10: {
                    name = "Haryana";
                    break;
                }
                case 11: {
                    name = "Himachal Pradesh";
                    break;
                }
                case 12: {
                    name = "Jammu and Kashmir";
                    break;
                }
                case 13: {
                    name = "Kerala";
                    break;
                }
                case 14: {
                    name = "Lakshadweep";
                    break;
                }
                case 16: {
                    name = "Maharashtra";
                    break;
                }
                case 17: {
                    name = "Manipur";
                    break;
                }
                case 18: {
                    name = "Meghalaya";
                    break;
                }
                case 19: {
                    name = "Karnataka";
                    break;
                }
                case 20: {
                    name = "Nagaland";
                    break;
                }
                case 21: {
                    name = "Orissa";
                    break;
                }
                case 22: {
                    name = "Puducherry";
                    break;
                }
                case 23: {
                    name = "Punjab";
                    break;
                }
                case 24: {
                    name = "Rajasthan";
                    break;
                }
                case 25: {
                    name = "Tamil Nadu";
                    break;
                }
                case 26: {
                    name = "Tripura";
                    break;
                }
                case 28: {
                    name = "West Bengal";
                    break;
                }
                case 29: {
                    name = "Sikkim";
                    break;
                }
                case 30: {
                    name = "Arunachal Pradesh";
                    break;
                }
                case 31: {
                    name = "Mizoram";
                    break;
                }
                case 32: {
                    name = "Daman and Diu";
                    break;
                }
                case 33: {
                    name = "Goa";
                    break;
                }
                case 34: {
                    name = "Bihar";
                    break;
                }
                case 35: {
                    name = "Madhya Pradesh";
                    break;
                }
                case 36: {
                    name = "Uttar Pradesh";
                    break;
                }
                case 37: {
                    name = "Chhattisgarh";
                    break;
                }
                case 38: {
                    name = "Jharkhand";
                    break;
                }
                case 39: {
                    name = "Uttarakhand";
                }
            }
        }
        if (country_code.equals("IQ")) {
            switch (region_code2) {
                case 1: {
                    name = "Al Anbar";
                    break;
                }
                case 2: {
                    name = "Al Basrah";
                    break;
                }
                case 3: {
                    name = "Al Muthanna";
                    break;
                }
                case 4: {
                    name = "Al Qadisiyah";
                    break;
                }
                case 5: {
                    name = "As Sulaymaniyah";
                    break;
                }
                case 6: {
                    name = "Babil";
                    break;
                }
                case 7: {
                    name = "Baghdad";
                    break;
                }
                case 8: {
                    name = "Dahuk";
                    break;
                }
                case 9: {
                    name = "Dhi Qar";
                    break;
                }
                case 10: {
                    name = "Diyala";
                    break;
                }
                case 11: {
                    name = "Arbil";
                    break;
                }
                case 12: {
                    name = "Karbala'";
                    break;
                }
                case 13: {
                    name = "At Ta'mim";
                    break;
                }
                case 14: {
                    name = "Maysan";
                    break;
                }
                case 15: {
                    name = "Ninawa";
                    break;
                }
                case 16: {
                    name = "Wasit";
                    break;
                }
                case 17: {
                    name = "An Najaf";
                    break;
                }
                case 18: {
                    name = "Salah ad Din";
                }
            }
        }
        if (country_code.equals("IR")) {
            switch (region_code2) {
                case 1: {
                    name = "Azarbayjan-e Bakhtari";
                    break;
                }
                case 2: {
                    name = "Azarbayjan-e Khavari";
                    break;
                }
                case 3: {
                    name = "Chahar Mahall va Bakhtiari";
                    break;
                }
                case 4: {
                    name = "Sistan va Baluchestan";
                    break;
                }
                case 5: {
                    name = "Kohkiluyeh va Buyer Ahmadi";
                    break;
                }
                case 7: {
                    name = "Fars";
                    break;
                }
                case 8: {
                    name = "Gilan";
                    break;
                }
                case 9: {
                    name = "Hamadan";
                    break;
                }
                case 10: {
                    name = "Ilam";
                    break;
                }
                case 11: {
                    name = "Hormozgan";
                    break;
                }
                case 12: {
                    name = "Kerman";
                    break;
                }
                case 13: {
                    name = "Bakhtaran";
                    break;
                }
                case 15: {
                    name = "Khuzestan";
                    break;
                }
                case 16: {
                    name = "Kordestan";
                    break;
                }
                case 17: {
                    name = "Mazandaran";
                    break;
                }
                case 18: {
                    name = "Semnan Province";
                    break;
                }
                case 19: {
                    name = "Markazi";
                    break;
                }
                case 21: {
                    name = "Zanjan";
                    break;
                }
                case 22: {
                    name = "Bushehr";
                    break;
                }
                case 23: {
                    name = "Lorestan";
                    break;
                }
                case 24: {
                    name = "Markazi";
                    break;
                }
                case 25: {
                    name = "Semnan";
                    break;
                }
                case 26: {
                    name = "Tehran";
                    break;
                }
                case 27: {
                    name = "Zanjan";
                    break;
                }
                case 28: {
                    name = "Esfahan";
                    break;
                }
                case 29: {
                    name = "Kerman";
                    break;
                }
                case 30: {
                    name = "Khorasan";
                    break;
                }
                case 31: {
                    name = "Yazd";
                    break;
                }
                case 32: {
                    name = "Ardabil";
                    break;
                }
                case 33: {
                    name = "East Azarbaijan";
                    break;
                }
                case 34: {
                    name = "Markazi";
                    break;
                }
                case 35: {
                    name = "Mazandaran";
                    break;
                }
                case 36: {
                    name = "Zanjan";
                    break;
                }
                case 37: {
                    name = "Golestan";
                    break;
                }
                case 38: {
                    name = "Qazvin";
                    break;
                }
                case 39: {
                    name = "Qom";
                    break;
                }
                case 40: {
                    name = "Yazd";
                    break;
                }
                case 41: {
                    name = "Khorasan-e Janubi";
                    break;
                }
                case 42: {
                    name = "Khorasan-e Razavi";
                    break;
                }
                case 43: {
                    name = "Khorasan-e Shemali";
                }
            }
        }
        if (country_code.equals("IS")) {
            switch (region_code2) {
                case 3: {
                    name = "Arnessysla";
                    break;
                }
                case 5: {
                    name = "Austur-Hunavatnssysla";
                    break;
                }
                case 6: {
                    name = "Austur-Skaftafellssysla";
                    break;
                }
                case 7: {
                    name = "Borgarfjardarsysla";
                    break;
                }
                case 9: {
                    name = "Eyjafjardarsysla";
                    break;
                }
                case 10: {
                    name = "Gullbringusysla";
                    break;
                }
                case 15: {
                    name = "Kjosarsysla";
                    break;
                }
                case 17: {
                    name = "Myrasysla";
                    break;
                }
                case 20: {
                    name = "Nordur-Mulasysla";
                    break;
                }
                case 21: {
                    name = "Nordur-Tingeyjarsysla";
                    break;
                }
                case 23: {
                    name = "Rangarvallasysla";
                    break;
                }
                case 28: {
                    name = "Skagafjardarsysla";
                    break;
                }
                case 29: {
                    name = "Snafellsnes- og Hnappadalssysla";
                    break;
                }
                case 30: {
                    name = "Strandasysla";
                    break;
                }
                case 31: {
                    name = "Sudur-Mulasysla";
                    break;
                }
                case 32: {
                    name = "Sudur-Tingeyjarsysla";
                    break;
                }
                case 34: {
                    name = "Vestur-Bardastrandarsysla";
                    break;
                }
                case 35: {
                    name = "Vestur-Hunavatnssysla";
                    break;
                }
                case 36: {
                    name = "Vestur-Isafjardarsysla";
                    break;
                }
                case 37: {
                    name = "Vestur-Skaftafellssysla";
                    break;
                }
                case 40: {
                    name = "Norourland Eystra";
                    break;
                }
                case 41: {
                    name = "Norourland Vestra";
                    break;
                }
                case 42: {
                    name = "Suourland";
                    break;
                }
                case 43: {
                    name = "Suournes";
                    break;
                }
                case 44: {
                    name = "Vestfiroir";
                    break;
                }
                case 45: {
                    name = "Vesturland";
                }
            }
        }
        if (country_code.equals("IT")) {
            switch (region_code2) {
                case 1: {
                    name = "Abruzzi";
                    break;
                }
                case 2: {
                    name = "Basilicata";
                    break;
                }
                case 3: {
                    name = "Calabria";
                    break;
                }
                case 4: {
                    name = "Campania";
                    break;
                }
                case 5: {
                    name = "Emilia-Romagna";
                    break;
                }
                case 6: {
                    name = "Friuli-Venezia Giulia";
                    break;
                }
                case 7: {
                    name = "Lazio";
                    break;
                }
                case 8: {
                    name = "Liguria";
                    break;
                }
                case 9: {
                    name = "Lombardia";
                    break;
                }
                case 10: {
                    name = "Marche";
                    break;
                }
                case 11: {
                    name = "Molise";
                    break;
                }
                case 12: {
                    name = "Piemonte";
                    break;
                }
                case 13: {
                    name = "Puglia";
                    break;
                }
                case 14: {
                    name = "Sardegna";
                    break;
                }
                case 15: {
                    name = "Sicilia";
                    break;
                }
                case 16: {
                    name = "Toscana";
                    break;
                }
                case 17: {
                    name = "Trentino-Alto Adige";
                    break;
                }
                case 18: {
                    name = "Umbria";
                    break;
                }
                case 19: {
                    name = "Valle d'Aosta";
                    break;
                }
                case 20: {
                    name = "Veneto";
                }
            }
        }
        if (country_code.equals("JM")) {
            switch (region_code2) {
                case 1: {
                    name = "Clarendon";
                    break;
                }
                case 2: {
                    name = "Hanover";
                    break;
                }
                case 4: {
                    name = "Manchester";
                    break;
                }
                case 7: {
                    name = "Portland";
                    break;
                }
                case 8: {
                    name = "Saint Andrew";
                    break;
                }
                case 9: {
                    name = "Saint Ann";
                    break;
                }
                case 10: {
                    name = "Saint Catherine";
                    break;
                }
                case 11: {
                    name = "Saint Elizabeth";
                    break;
                }
                case 12: {
                    name = "Saint James";
                    break;
                }
                case 13: {
                    name = "Saint Mary";
                    break;
                }
                case 14: {
                    name = "Saint Thomas";
                    break;
                }
                case 15: {
                    name = "Trelawny";
                    break;
                }
                case 16: {
                    name = "Westmoreland";
                    break;
                }
                case 17: {
                    name = "Kingston";
                }
            }
        }
        if (country_code.equals("JO")) {
            switch (region_code2) {
                case 2: {
                    name = "Al Balqa'";
                    break;
                }
                case 7: {
                    name = "Ma";
                    break;
                }
                case 9: {
                    name = "Al Karak";
                    break;
                }
                case 10: {
                    name = "Al Mafraq";
                    break;
                }
                case 11: {
                    name = "Amman Governorate";
                    break;
                }
                case 12: {
                    name = "At Tafilah";
                    break;
                }
                case 13: {
                    name = "Az Zarqa";
                    break;
                }
                case 14: {
                    name = "Irbid";
                    break;
                }
                case 16: {
                    name = "Amman";
                }
            }
        }
        if (country_code.equals("JP")) {
            switch (region_code2) {
                case 1: {
                    name = "Aichi";
                    break;
                }
                case 2: {
                    name = "Akita";
                    break;
                }
                case 3: {
                    name = "Aomori";
                    break;
                }
                case 4: {
                    name = "Chiba";
                    break;
                }
                case 5: {
                    name = "Ehime";
                    break;
                }
                case 6: {
                    name = "Fukui";
                    break;
                }
                case 7: {
                    name = "Fukuoka";
                    break;
                }
                case 8: {
                    name = "Fukushima";
                    break;
                }
                case 9: {
                    name = "Gifu";
                    break;
                }
                case 10: {
                    name = "Gumma";
                    break;
                }
                case 11: {
                    name = "Hiroshima";
                    break;
                }
                case 12: {
                    name = "Hokkaido";
                    break;
                }
                case 13: {
                    name = "Hyogo";
                    break;
                }
                case 14: {
                    name = "Ibaraki";
                    break;
                }
                case 15: {
                    name = "Ishikawa";
                    break;
                }
                case 16: {
                    name = "Iwate";
                    break;
                }
                case 17: {
                    name = "Kagawa";
                    break;
                }
                case 18: {
                    name = "Kagoshima";
                    break;
                }
                case 19: {
                    name = "Kanagawa";
                    break;
                }
                case 20: {
                    name = "Kochi";
                    break;
                }
                case 21: {
                    name = "Kumamoto";
                    break;
                }
                case 22: {
                    name = "Kyoto";
                    break;
                }
                case 23: {
                    name = "Mie";
                    break;
                }
                case 24: {
                    name = "Miyagi";
                    break;
                }
                case 25: {
                    name = "Miyazaki";
                    break;
                }
                case 26: {
                    name = "Nagano";
                    break;
                }
                case 27: {
                    name = "Nagasaki";
                    break;
                }
                case 28: {
                    name = "Nara";
                    break;
                }
                case 29: {
                    name = "Niigata";
                    break;
                }
                case 30: {
                    name = "Oita";
                    break;
                }
                case 31: {
                    name = "Okayama";
                    break;
                }
                case 32: {
                    name = "Osaka";
                    break;
                }
                case 33: {
                    name = "Saga";
                    break;
                }
                case 34: {
                    name = "Saitama";
                    break;
                }
                case 35: {
                    name = "Shiga";
                    break;
                }
                case 36: {
                    name = "Shimane";
                    break;
                }
                case 37: {
                    name = "Shizuoka";
                    break;
                }
                case 38: {
                    name = "Tochigi";
                    break;
                }
                case 39: {
                    name = "Tokushima";
                    break;
                }
                case 40: {
                    name = "Tokyo";
                    break;
                }
                case 41: {
                    name = "Tottori";
                    break;
                }
                case 42: {
                    name = "Toyama";
                    break;
                }
                case 43: {
                    name = "Wakayama";
                    break;
                }
                case 44: {
                    name = "Yamagata";
                    break;
                }
                case 45: {
                    name = "Yamaguchi";
                    break;
                }
                case 46: {
                    name = "Yamanashi";
                    break;
                }
                case 47: {
                    name = "Okinawa";
                }
            }
        }
        if (country_code.equals("KE")) {
            switch (region_code2) {
                case 1: {
                    name = "Central";
                    break;
                }
                case 2: {
                    name = "Coast";
                    break;
                }
                case 3: {
                    name = "Eastern";
                    break;
                }
                case 5: {
                    name = "Nairobi Area";
                    break;
                }
                case 6: {
                    name = "North-Eastern";
                    break;
                }
                case 7: {
                    name = "Nyanza";
                    break;
                }
                case 8: {
                    name = "Rift Valley";
                    break;
                }
                case 9: {
                    name = "Western";
                }
            }
        }
        if (country_code.equals("KG")) {
            switch (region_code2) {
                case 1: {
                    name = "Bishkek";
                    break;
                }
                case 2: {
                    name = "Chuy";
                    break;
                }
                case 3: {
                    name = "Jalal-Abad";
                    break;
                }
                case 4: {
                    name = "Naryn";
                    break;
                }
                case 5: {
                    name = "Osh";
                    break;
                }
                case 6: {
                    name = "Talas";
                    break;
                }
                case 7: {
                    name = "Ysyk-Kol";
                    break;
                }
                case 8: {
                    name = "Osh";
                    break;
                }
                case 9: {
                    name = "Batken";
                }
            }
        }
        if (country_code.equals("KH")) {
            switch (region_code2) {
                case 1: {
                    name = "Batdambang";
                    break;
                }
                case 2: {
                    name = "Kampong Cham";
                    break;
                }
                case 3: {
                    name = "Kampong Chhnang";
                    break;
                }
                case 4: {
                    name = "Kampong Speu";
                    break;
                }
                case 5: {
                    name = "Kampong Thum";
                    break;
                }
                case 6: {
                    name = "Kampot";
                    break;
                }
                case 7: {
                    name = "Kandal";
                    break;
                }
                case 8: {
                    name = "Koh Kong";
                    break;
                }
                case 9: {
                    name = "Kracheh";
                    break;
                }
                case 10: {
                    name = "Mondulkiri";
                    break;
                }
                case 11: {
                    name = "Phnum Penh";
                    break;
                }
                case 12: {
                    name = "Pursat";
                    break;
                }
                case 13: {
                    name = "Preah Vihear";
                    break;
                }
                case 14: {
                    name = "Prey Veng";
                    break;
                }
                case 15: {
                    name = "Ratanakiri Kiri";
                    break;
                }
                case 16: {
                    name = "Siem Reap";
                    break;
                }
                case 17: {
                    name = "Stung Treng";
                    break;
                }
                case 18: {
                    name = "Svay Rieng";
                    break;
                }
                case 19: {
                    name = "Takeo";
                    break;
                }
                case 25: {
                    name = "Banteay Meanchey";
                    break;
                }
                case 29: {
                    name = "Batdambang";
                    break;
                }
                case 30: {
                    name = "Pailin";
                }
            }
        }
        if (country_code.equals("KI")) {
            switch (region_code2) {
                case 1: {
                    name = "Gilbert Islands";
                    break;
                }
                case 2: {
                    name = "Line Islands";
                    break;
                }
                case 3: {
                    name = "Phoenix Islands";
                }
            }
        }
        if (country_code.equals("KM")) {
            switch (region_code2) {
                case 1: {
                    name = "Anjouan";
                    break;
                }
                case 2: {
                    name = "Grande Comore";
                    break;
                }
                case 3: {
                    name = "Moheli";
                }
            }
        }
        if (country_code.equals("KN")) {
            switch (region_code2) {
                case 1: {
                    name = "Christ Church Nichola Town";
                    break;
                }
                case 2: {
                    name = "Saint Anne Sandy Point";
                    break;
                }
                case 3: {
                    name = "Saint George Basseterre";
                    break;
                }
                case 4: {
                    name = "Saint George Gingerland";
                    break;
                }
                case 5: {
                    name = "Saint James Windward";
                    break;
                }
                case 6: {
                    name = "Saint John Capisterre";
                    break;
                }
                case 7: {
                    name = "Saint John Figtree";
                    break;
                }
                case 8: {
                    name = "Saint Mary Cayon";
                    break;
                }
                case 9: {
                    name = "Saint Paul Capisterre";
                    break;
                }
                case 10: {
                    name = "Saint Paul Charlestown";
                    break;
                }
                case 11: {
                    name = "Saint Peter Basseterre";
                    break;
                }
                case 12: {
                    name = "Saint Thomas Lowland";
                    break;
                }
                case 13: {
                    name = "Saint Thomas Middle Island";
                    break;
                }
                case 15: {
                    name = "Trinity Palmetto Point";
                }
            }
        }
        if (country_code.equals("KP")) {
            switch (region_code2) {
                case 1: {
                    name = "Chagang-do";
                    break;
                }
                case 3: {
                    name = "Hamgyong-namdo";
                    break;
                }
                case 6: {
                    name = "Hwanghae-namdo";
                    break;
                }
                case 7: {
                    name = "Hwanghae-bukto";
                    break;
                }
                case 8: {
                    name = "Kaesong-si";
                    break;
                }
                case 9: {
                    name = "Kangwon-do";
                    break;
                }
                case 11: {
                    name = "P'yongan-bukto";
                    break;
                }
                case 12: {
                    name = "P'yongyang-si";
                    break;
                }
                case 13: {
                    name = "Yanggang-do";
                    break;
                }
                case 14: {
                    name = "Namp'o-si";
                    break;
                }
                case 15: {
                    name = "P'yongan-namdo";
                    break;
                }
                case 17: {
                    name = "Hamgyong-bukto";
                    break;
                }
                case 18: {
                    name = "Najin Sonbong-si";
                }
            }
        }
        if (country_code.equals("KR")) {
            switch (region_code2) {
                case 1: {
                    name = "Cheju-do";
                    break;
                }
                case 3: {
                    name = "Cholla-bukto";
                    break;
                }
                case 5: {
                    name = "Ch'ungch'ong-bukto";
                    break;
                }
                case 6: {
                    name = "Kangwon-do";
                    break;
                }
                case 10: {
                    name = "Pusan-jikhalsi";
                    break;
                }
                case 11: {
                    name = "Seoul-t'ukpyolsi";
                    break;
                }
                case 12: {
                    name = "Inch'on-jikhalsi";
                    break;
                }
                case 13: {
                    name = "Kyonggi-do";
                    break;
                }
                case 14: {
                    name = "Kyongsang-bukto";
                    break;
                }
                case 15: {
                    name = "Taegu-jikhalsi";
                    break;
                }
                case 16: {
                    name = "Cholla-namdo";
                    break;
                }
                case 17: {
                    name = "Ch'ungch'ong-namdo";
                    break;
                }
                case 18: {
                    name = "Kwangju-jikhalsi";
                    break;
                }
                case 19: {
                    name = "Taejon-jikhalsi";
                    break;
                }
                case 20: {
                    name = "Kyongsang-namdo";
                    break;
                }
                case 21: {
                    name = "Ulsan-gwangyoksi";
                }
            }
        }
        if (country_code.equals("KW")) {
            switch (region_code2) {
                case 1: {
                    name = "Al Ahmadi";
                    break;
                }
                case 2: {
                    name = "Al Kuwayt";
                    break;
                }
                case 5: {
                    name = "Al Jahra";
                    break;
                }
                case 7: {
                    name = "Al Farwaniyah";
                    break;
                }
                case 8: {
                    name = "Hawalli";
                    break;
                }
                case 9: {
                    name = "Mubarak al Kabir";
                }
            }
        }
        if (country_code.equals("KY")) {
            switch (region_code2) {
                case 1: {
                    name = "Creek";
                    break;
                }
                case 2: {
                    name = "Eastern";
                    break;
                }
                case 3: {
                    name = "Midland";
                    break;
                }
                case 4: {
                    name = "South Town";
                    break;
                }
                case 5: {
                    name = "Spot Bay";
                    break;
                }
                case 6: {
                    name = "Stake Bay";
                    break;
                }
                case 7: {
                    name = "West End";
                    break;
                }
                case 8: {
                    name = "Western";
                }
            }
        }
        if (country_code.equals("KZ")) {
            switch (region_code2) {
                case 1: {
                    name = "Almaty";
                    break;
                }
                case 2: {
                    name = "Almaty City";
                    break;
                }
                case 3: {
                    name = "Aqmola";
                    break;
                }
                case 4: {
                    name = "Aqtobe";
                    break;
                }
                case 5: {
                    name = "Astana";
                    break;
                }
                case 6: {
                    name = "Atyrau";
                    break;
                }
                case 7: {
                    name = "West Kazakhstan";
                    break;
                }
                case 8: {
                    name = "Bayqonyr";
                    break;
                }
                case 9: {
                    name = "Mangghystau";
                    break;
                }
                case 10: {
                    name = "South Kazakhstan";
                    break;
                }
                case 11: {
                    name = "Pavlodar";
                    break;
                }
                case 12: {
                    name = "Qaraghandy";
                    break;
                }
                case 13: {
                    name = "Qostanay";
                    break;
                }
                case 14: {
                    name = "Qyzylorda";
                    break;
                }
                case 15: {
                    name = "East Kazakhstan";
                    break;
                }
                case 16: {
                    name = "North Kazakhstan";
                    break;
                }
                case 17: {
                    name = "Zhambyl";
                }
            }
        }
        if (country_code.equals("LA")) {
            switch (region_code2) {
                case 1: {
                    name = "Attapu";
                    break;
                }
                case 2: {
                    name = "Champasak";
                    break;
                }
                case 3: {
                    name = "Houaphan";
                    break;
                }
                case 4: {
                    name = "Khammouan";
                    break;
                }
                case 5: {
                    name = "Louang Namtha";
                    break;
                }
                case 7: {
                    name = "Oudomxai";
                    break;
                }
                case 8: {
                    name = "Phongsali";
                    break;
                }
                case 9: {
                    name = "Saravan";
                    break;
                }
                case 10: {
                    name = "Savannakhet";
                    break;
                }
                case 11: {
                    name = "Vientiane";
                    break;
                }
                case 13: {
                    name = "Xaignabouri";
                    break;
                }
                case 14: {
                    name = "Xiangkhoang";
                    break;
                }
                case 17: {
                    name = "Louangphrabang";
                }
            }
        }
        if (country_code.equals("LB")) {
            switch (region_code2) {
                case 1: {
                    name = "Beqaa";
                    break;
                }
                case 2: {
                    name = "Al Janub";
                    break;
                }
                case 3: {
                    name = "Liban-Nord";
                    break;
                }
                case 4: {
                    name = "Beyrouth";
                    break;
                }
                case 5: {
                    name = "Mont-Liban";
                    break;
                }
                case 6: {
                    name = "Liban-Sud";
                    break;
                }
                case 7: {
                    name = "Nabatiye";
                    break;
                }
                case 8: {
                    name = "Beqaa";
                    break;
                }
                case 9: {
                    name = "Liban-Nord";
                    break;
                }
                case 10: {
                    name = "Aakk";
                    break;
                }
                case 11: {
                    name = "Baalbek-Hermel";
                }
            }
        }
        if (country_code.equals("LC")) {
            switch (region_code2) {
                case 1: {
                    name = "Anse-la-Raye";
                    break;
                }
                case 2: {
                    name = "Dauphin";
                    break;
                }
                case 3: {
                    name = "Castries";
                    break;
                }
                case 4: {
                    name = "Choiseul";
                    break;
                }
                case 5: {
                    name = "Dennery";
                    break;
                }
                case 6: {
                    name = "Gros-Islet";
                    break;
                }
                case 7: {
                    name = "Laborie";
                    break;
                }
                case 8: {
                    name = "Micoud";
                    break;
                }
                case 9: {
                    name = "Soufriere";
                    break;
                }
                case 10: {
                    name = "Vieux-Fort";
                    break;
                }
                case 11: {
                    name = "Praslin";
                }
            }
        }
        if (country_code.equals("LI")) {
            switch (region_code2) {
                case 1: {
                    name = "Balzers";
                    break;
                }
                case 2: {
                    name = "Eschen";
                    break;
                }
                case 3: {
                    name = "Gamprin";
                    break;
                }
                case 4: {
                    name = "Mauren";
                    break;
                }
                case 5: {
                    name = "Planken";
                    break;
                }
                case 6: {
                    name = "Ruggell";
                    break;
                }
                case 7: {
                    name = "Schaan";
                    break;
                }
                case 8: {
                    name = "Schellenberg";
                    break;
                }
                case 9: {
                    name = "Triesen";
                    break;
                }
                case 10: {
                    name = "Triesenberg";
                    break;
                }
                case 11: {
                    name = "Vaduz";
                    break;
                }
                case 21: {
                    name = "Gbarpolu";
                    break;
                }
                case 22: {
                    name = "River Gee";
                }
            }
        }
        if (country_code.equals("LK")) {
            switch (region_code2) {
                case 1: {
                    name = "Amparai";
                    break;
                }
                case 2: {
                    name = "Anuradhapura";
                    break;
                }
                case 3: {
                    name = "Badulla";
                    break;
                }
                case 4: {
                    name = "Batticaloa";
                    break;
                }
                case 6: {
                    name = "Galle";
                    break;
                }
                case 7: {
                    name = "Hambantota";
                    break;
                }
                case 9: {
                    name = "Kalutara";
                    break;
                }
                case 10: {
                    name = "Kandy";
                    break;
                }
                case 11: {
                    name = "Kegalla";
                    break;
                }
                case 12: {
                    name = "Kurunegala";
                    break;
                }
                case 14: {
                    name = "Matale";
                    break;
                }
                case 15: {
                    name = "Matara";
                    break;
                }
                case 16: {
                    name = "Moneragala";
                    break;
                }
                case 17: {
                    name = "Nuwara Eliya";
                    break;
                }
                case 18: {
                    name = "Polonnaruwa";
                    break;
                }
                case 19: {
                    name = "Puttalam";
                    break;
                }
                case 20: {
                    name = "Ratnapura";
                    break;
                }
                case 21: {
                    name = "Trincomalee";
                    break;
                }
                case 23: {
                    name = "Colombo";
                    break;
                }
                case 24: {
                    name = "Gampaha";
                    break;
                }
                case 25: {
                    name = "Jaffna";
                    break;
                }
                case 26: {
                    name = "Mannar";
                    break;
                }
                case 27: {
                    name = "Mullaittivu";
                    break;
                }
                case 28: {
                    name = "Vavuniya";
                    break;
                }
                case 29: {
                    name = "Central";
                    break;
                }
                case 30: {
                    name = "North Central";
                    break;
                }
                case 31: {
                    name = "Northern";
                    break;
                }
                case 32: {
                    name = "North Western";
                    break;
                }
                case 33: {
                    name = "Sabaragamuwa";
                    break;
                }
                case 34: {
                    name = "Southern";
                    break;
                }
                case 35: {
                    name = "Uva";
                    break;
                }
                case 36: {
                    name = "Western";
                }
            }
        }
        if (country_code.equals("LR")) {
            switch (region_code2) {
                case 1: {
                    name = "Bong";
                    break;
                }
                case 4: {
                    name = "Grand Cape Mount";
                    break;
                }
                case 5: {
                    name = "Lofa";
                    break;
                }
                case 6: {
                    name = "Maryland";
                    break;
                }
                case 7: {
                    name = "Monrovia";
                    break;
                }
                case 9: {
                    name = "Nimba";
                    break;
                }
                case 10: {
                    name = "Sino";
                    break;
                }
                case 11: {
                    name = "Grand Bassa";
                    break;
                }
                case 12: {
                    name = "Grand Cape Mount";
                    break;
                }
                case 13: {
                    name = "Maryland";
                    break;
                }
                case 14: {
                    name = "Montserrado";
                    break;
                }
                case 17: {
                    name = "Margibi";
                    break;
                }
                case 18: {
                    name = "River Cess";
                    break;
                }
                case 19: {
                    name = "Grand Gedeh";
                    break;
                }
                case 20: {
                    name = "Lofa";
                    break;
                }
                case 21: {
                    name = "Gbarpolu";
                    break;
                }
                case 22: {
                    name = "River Gee";
                }
            }
        }
        if (country_code.equals("LS")) {
            switch (region_code2) {
                case 10: {
                    name = "Berea";
                    break;
                }
                case 11: {
                    name = "Butha-Buthe";
                    break;
                }
                case 12: {
                    name = "Leribe";
                    break;
                }
                case 13: {
                    name = "Mafeteng";
                    break;
                }
                case 14: {
                    name = "Maseru";
                    break;
                }
                case 15: {
                    name = "Mohales Hoek";
                    break;
                }
                case 16: {
                    name = "Mokhotlong";
                    break;
                }
                case 17: {
                    name = "Qachas Nek";
                    break;
                }
                case 18: {
                    name = "Quthing";
                    break;
                }
                case 19: {
                    name = "Thaba-Tseka";
                }
            }
        }
        if (country_code.equals("LT")) {
            switch (region_code2) {
                case 56: {
                    name = "Alytaus Apskritis";
                    break;
                }
                case 57: {
                    name = "Kauno Apskritis";
                    break;
                }
                case 58: {
                    name = "Klaipedos Apskritis";
                    break;
                }
                case 59: {
                    name = "Marijampoles Apskritis";
                    break;
                }
                case 60: {
                    name = "Panevezio Apskritis";
                    break;
                }
                case 61: {
                    name = "Siauliu Apskritis";
                    break;
                }
                case 62: {
                    name = "Taurages Apskritis";
                    break;
                }
                case 63: {
                    name = "Telsiu Apskritis";
                    break;
                }
                case 64: {
                    name = "Utenos Apskritis";
                    break;
                }
                case 65: {
                    name = "Vilniaus Apskritis";
                }
            }
        }
        if (country_code.equals("LU")) {
            switch (region_code2) {
                case 1: {
                    name = "Diekirch";
                    break;
                }
                case 2: {
                    name = "Grevenmacher";
                    break;
                }
                case 3: {
                    name = "Luxembourg";
                }
            }
        }
        if (country_code.equals("LV")) {
            switch (region_code2) {
                case 1: {
                    name = "Aizkraukles";
                    break;
                }
                case 2: {
                    name = "Aluksnes";
                    break;
                }
                case 3: {
                    name = "Balvu";
                    break;
                }
                case 4: {
                    name = "Bauskas";
                    break;
                }
                case 5: {
                    name = "Cesu";
                    break;
                }
                case 6: {
                    name = "Daugavpils";
                    break;
                }
                case 7: {
                    name = "Daugavpils";
                    break;
                }
                case 8: {
                    name = "Dobeles";
                    break;
                }
                case 9: {
                    name = "Gulbenes";
                    break;
                }
                case 10: {
                    name = "Jekabpils";
                    break;
                }
                case 11: {
                    name = "Jelgava";
                    break;
                }
                case 12: {
                    name = "Jelgavas";
                    break;
                }
                case 13: {
                    name = "Jurmala";
                    break;
                }
                case 14: {
                    name = "Kraslavas";
                    break;
                }
                case 15: {
                    name = "Kuldigas";
                    break;
                }
                case 16: {
                    name = "Liepaja";
                    break;
                }
                case 17: {
                    name = "Liepajas";
                    break;
                }
                case 18: {
                    name = "Limbazu";
                    break;
                }
                case 19: {
                    name = "Ludzas";
                    break;
                }
                case 20: {
                    name = "Madonas";
                    break;
                }
                case 21: {
                    name = "Ogres";
                    break;
                }
                case 22: {
                    name = "Preilu";
                    break;
                }
                case 23: {
                    name = "Rezekne";
                    break;
                }
                case 24: {
                    name = "Rezeknes";
                    break;
                }
                case 25: {
                    name = "Riga";
                    break;
                }
                case 26: {
                    name = "Rigas";
                    break;
                }
                case 27: {
                    name = "Saldus";
                    break;
                }
                case 28: {
                    name = "Talsu";
                    break;
                }
                case 29: {
                    name = "Tukuma";
                    break;
                }
                case 30: {
                    name = "Valkas";
                    break;
                }
                case 31: {
                    name = "Valmieras";
                    break;
                }
                case 32: {
                    name = "Ventspils";
                    break;
                }
                case 33: {
                    name = "Ventspils";
                }
            }
        }
        if (country_code.equals("LY")) {
            switch (region_code2) {
                case 3: {
                    name = "Al Aziziyah";
                    break;
                }
                case 5: {
                    name = "Al Jufrah";
                    break;
                }
                case 8: {
                    name = "Al Kufrah";
                    break;
                }
                case 13: {
                    name = "Ash Shati'";
                    break;
                }
                case 30: {
                    name = "Murzuq";
                    break;
                }
                case 34: {
                    name = "Sabha";
                    break;
                }
                case 41: {
                    name = "Tarhunah";
                    break;
                }
                case 42: {
                    name = "Tubruq";
                    break;
                }
                case 45: {
                    name = "Zlitan";
                    break;
                }
                case 47: {
                    name = "Ajdabiya";
                    break;
                }
                case 48: {
                    name = "Al Fatih";
                    break;
                }
                case 49: {
                    name = "Al Jabal al Akhdar";
                    break;
                }
                case 50: {
                    name = "Al Khums";
                    break;
                }
                case 51: {
                    name = "An Nuqat al Khams";
                    break;
                }
                case 52: {
                    name = "Awbari";
                    break;
                }
                case 53: {
                    name = "Az Zawiyah";
                    break;
                }
                case 54: {
                    name = "Banghazi";
                    break;
                }
                case 55: {
                    name = "Darnah";
                    break;
                }
                case 56: {
                    name = "Ghadamis";
                    break;
                }
                case 57: {
                    name = "Gharyan";
                    break;
                }
                case 58: {
                    name = "Misratah";
                    break;
                }
                case 59: {
                    name = "Sawfajjin";
                    break;
                }
                case 60: {
                    name = "Surt";
                    break;
                }
                case 61: {
                    name = "Tarabulus";
                    break;
                }
                case 62: {
                    name = "Yafran";
                }
            }
        }
        if (country_code.equals("MA")) {
            switch (region_code2) {
                case 1: {
                    name = "Agadir";
                    break;
                }
                case 2: {
                    name = "Al Hoceima";
                    break;
                }
                case 3: {
                    name = "Azilal";
                    break;
                }
                case 4: {
                    name = "Ben Slimane";
                    break;
                }
                case 5: {
                    name = "Beni Mellal";
                    break;
                }
                case 6: {
                    name = "Boulemane";
                    break;
                }
                case 7: {
                    name = "Casablanca";
                    break;
                }
                case 8: {
                    name = "Chaouen";
                    break;
                }
                case 9: {
                    name = "El Jadida";
                    break;
                }
                case 10: {
                    name = "El Kelaa des Srarhna";
                    break;
                }
                case 11: {
                    name = "Er Rachidia";
                    break;
                }
                case 12: {
                    name = "Essaouira";
                    break;
                }
                case 13: {
                    name = "Fes";
                    break;
                }
                case 14: {
                    name = "Figuig";
                    break;
                }
                case 15: {
                    name = "Kenitra";
                    break;
                }
                case 16: {
                    name = "Khemisset";
                    break;
                }
                case 17: {
                    name = "Khenifra";
                    break;
                }
                case 18: {
                    name = "Khouribga";
                    break;
                }
                case 19: {
                    name = "Marrakech";
                    break;
                }
                case 20: {
                    name = "Meknes";
                    break;
                }
                case 21: {
                    name = "Nador";
                    break;
                }
                case 22: {
                    name = "Ouarzazate";
                    break;
                }
                case 23: {
                    name = "Oujda";
                    break;
                }
                case 24: {
                    name = "Rabat-Sale";
                    break;
                }
                case 25: {
                    name = "Safi";
                    break;
                }
                case 26: {
                    name = "Settat";
                    break;
                }
                case 27: {
                    name = "Tanger";
                    break;
                }
                case 29: {
                    name = "Tata";
                    break;
                }
                case 30: {
                    name = "Taza";
                    break;
                }
                case 32: {
                    name = "Tiznit";
                    break;
                }
                case 33: {
                    name = "Guelmim";
                    break;
                }
                case 34: {
                    name = "Ifrane";
                    break;
                }
                case 35: {
                    name = "Laayoune";
                    break;
                }
                case 36: {
                    name = "Tan-Tan";
                    break;
                }
                case 37: {
                    name = "Taounate";
                    break;
                }
                case 38: {
                    name = "Sidi Kacem";
                    break;
                }
                case 39: {
                    name = "Taroudannt";
                    break;
                }
                case 40: {
                    name = "Tetouan";
                    break;
                }
                case 41: {
                    name = "Larache";
                    break;
                }
                case 45: {
                    name = "Grand Casablanca";
                    break;
                }
                case 46: {
                    name = "Fes-Boulemane";
                    break;
                }
                case 47: {
                    name = "Marrakech-Tensift-Al Haouz";
                    break;
                }
                case 48: {
                    name = "Meknes-Tafilalet";
                    break;
                }
                case 49: {
                    name = "Rabat-Sale-Zemmour-Zaer";
                    break;
                }
                case 50: {
                    name = "Chaouia-Ouardigha";
                    break;
                }
                case 51: {
                    name = "Doukkala-Abda";
                    break;
                }
                case 52: {
                    name = "Gharb-Chrarda-Beni Hssen";
                    break;
                }
                case 53: {
                    name = "Guelmim-Es Smara";
                    break;
                }
                case 54: {
                    name = "Oriental";
                    break;
                }
                case 55: {
                    name = "Souss-Massa-Dr";
                    break;
                }
                case 56: {
                    name = "Tadla-Azilal";
                    break;
                }
                case 57: {
                    name = "Tanger-Tetouan";
                    break;
                }
                case 58: {
                    name = "Taza-Al Hoceima-Taounate";
                    break;
                }
                case 59: {
                    name = "La";
                }
            }
        }
        if (country_code.equals("MC")) {
            switch (region_code2) {
                case 1: {
                    name = "La Condamine";
                    break;
                }
                case 2: {
                    name = "Monaco";
                    break;
                }
                case 3: {
                    name = "Monte-Carlo";
                }
            }
        }
        if (country_code.equals("MD")) {
            switch (region_code2) {
                case 46: {
                    name = "Balti";
                    break;
                }
                case 47: {
                    name = "Cahul";
                    break;
                }
                case 48: {
                    name = "Chisinau";
                    break;
                }
                case 49: {
                    name = "Stinga Nistrului";
                    break;
                }
                case 50: {
                    name = "Edinet";
                    break;
                }
                case 51: {
                    name = "Gagauzia";
                    break;
                }
                case 52: {
                    name = "Lapusna";
                    break;
                }
                case 53: {
                    name = "Orhei";
                    break;
                }
                case 54: {
                    name = "Soroca";
                    break;
                }
                case 55: {
                    name = "Tighina";
                    break;
                }
                case 56: {
                    name = "Ungheni";
                    break;
                }
                case 58: {
                    name = "Stinga Nistrului";
                    break;
                }
                case 59: {
                    name = "Anenii Noi";
                    break;
                }
                case 60: {
                    name = "Balti";
                    break;
                }
                case 61: {
                    name = "Basarabeasca";
                    break;
                }
                case 62: {
                    name = "Bender";
                    break;
                }
                case 63: {
                    name = "Briceni";
                    break;
                }
                case 64: {
                    name = "Cahul";
                    break;
                }
                case 65: {
                    name = "Cantemir";
                    break;
                }
                case 66: {
                    name = "Calarasi";
                    break;
                }
                case 67: {
                    name = "Causeni";
                    break;
                }
                case 68: {
                    name = "Cimislia";
                    break;
                }
                case 69: {
                    name = "Criuleni";
                    break;
                }
                case 70: {
                    name = "Donduseni";
                    break;
                }
                case 71: {
                    name = "Drochia";
                    break;
                }
                case 72: {
                    name = "Dubasari";
                    break;
                }
                case 73: {
                    name = "Edinet";
                    break;
                }
                case 74: {
                    name = "Falesti";
                    break;
                }
                case 75: {
                    name = "Floresti";
                    break;
                }
                case 76: {
                    name = "Glodeni";
                    break;
                }
                case 77: {
                    name = "Hincesti";
                    break;
                }
                case 78: {
                    name = "Ialoveni";
                    break;
                }
                case 79: {
                    name = "Leova";
                    break;
                }
                case 80: {
                    name = "Nisporeni";
                    break;
                }
                case 81: {
                    name = "Ocnita";
                    break;
                }
                case 83: {
                    name = "Rezina";
                    break;
                }
                case 84: {
                    name = "Riscani";
                    break;
                }
                case 85: {
                    name = "Singerei";
                    break;
                }
                case 86: {
                    name = "Soldanesti";
                    break;
                }
                case 87: {
                    name = "Soroca";
                    break;
                }
                case 88: {
                    name = "Stefan-Voda";
                    break;
                }
                case 89: {
                    name = "Straseni";
                    break;
                }
                case 90: {
                    name = "Taraclia";
                    break;
                }
                case 91: {
                    name = "Telenesti";
                    break;
                }
                case 92: {
                    name = "Ungheni";
                }
            }
        }
        if (country_code.equals("MG")) {
            switch (region_code2) {
                case 1: {
                    name = "Antsiranana";
                    break;
                }
                case 2: {
                    name = "Fianarantsoa";
                    break;
                }
                case 3: {
                    name = "Mahajanga";
                    break;
                }
                case 4: {
                    name = "Toamasina";
                    break;
                }
                case 5: {
                    name = "Antananarivo";
                    break;
                }
                case 6: {
                    name = "Toliara";
                }
            }
        }
        if (country_code.equals("MK")) {
            switch (region_code2) {
                case 1: {
                    string = "Aracinovo";
                    break;
                }
                case 2: {
                    string = "Bac";
                    break;
                }
                case 3: {
                    string = "Belcista";
                    break;
                }
                case 4: {
                    string = "Berovo";
                    break;
                }
                case 5: {
                    string = "Bistrica";
                    break;
                }
                case 6: {
                    string = "Bitola";
                    break;
                }
                case 7: {
                    string = "Blatec";
                    break;
                }
                case 8: {
                    string = "Bogdanci";
                    break;
                }
                case 9: {
                    string = "Bogomila";
                    break;
                }
                case 10: {
                    string = "Bogovinje";
                    break;
                }
                case 11: {
                    string = "Bosilovo";
                    break;
                }
                case 12: {
                    string = "Brvenica";
                    break;
                }
                case 13: {
                    string = "Cair";
                    break;
                }
                case 14: {
                    string = "Capari";
                    break;
                }
                case 15: {
                    string = "Caska";
                    break;
                }
                case 16: {
                    string = "Cegrane";
                    break;
                }
                case 17: {
                    string = "Centar";
                    break;
                }
                case 18: {
                    string = "Centar Zupa";
                    break;
                }
                case 19: {
                    string = "Cesinovo";
                    break;
                }
                case 20: {
                    string = "Cucer-Sandevo";
                    break;
                }
                case 21: {
                    string = "Debar";
                    break;
                }
                case 22: {
                    string = "Delcevo";
                    break;
                }
                case 23: {
                    string = "Delogozdi";
                    break;
                }
                case 24: {
                    string = "Demir Hisar";
                    break;
                }
                case 25: {
                    string = "Demir Kapija";
                    break;
                }
                case 26: {
                    string = "Dobrusevo";
                    break;
                }
                case 27: {
                    string = "Dolna Banjica";
                    break;
                }
                case 28: {
                    string = "Dolneni";
                    break;
                }
                case 29: {
                    string = "Dorce Petrov";
                    break;
                }
                case 30: {
                    string = "Drugovo";
                    break;
                }
                case 31: {
                    string = "Dzepciste";
                    break;
                }
                case 32: {
                    string = "Gazi Baba";
                    break;
                }
                case 33: {
                    string = "Gevgelija";
                    break;
                }
                case 34: {
                    string = "Gostivar";
                    break;
                }
                case 35: {
                    string = "Gradsko";
                    break;
                }
                case 36: {
                    string = "Ilinden";
                    break;
                }
                case 37: {
                    string = "Izvor";
                    break;
                }
                case 38: {
                    string = "Jegunovce";
                    break;
                }
                case 39: {
                    string = "Kamenjane";
                    break;
                }
                case 40: {
                    string = "Karbinci";
                    break;
                }
                case 41: {
                    string = "Karpos";
                    break;
                }
                case 42: {
                    string = "Kavadarci";
                    break;
                }
                case 43: {
                    string = "Kicevo";
                    break;
                }
                case 44: {
                    string = "Kisela Voda";
                    break;
                }
                case 45: {
                    string = "Klecevce";
                    break;
                }
                case 46: {
                    string = "Kocani";
                    break;
                }
                case 47: {
                    string = "Konce";
                    break;
                }
                case 48: {
                    string = "Kondovo";
                    break;
                }
                case 49: {
                    string = "Konopiste";
                    break;
                }
                case 50: {
                    string = "Kosel";
                    break;
                }
                case 51: {
                    string = "Kratovo";
                    break;
                }
                case 52: {
                    string = "Kriva Palanka";
                    break;
                }
                case 53: {
                    string = "Krivogastani";
                    break;
                }
                case 54: {
                    string = "Krusevo";
                    break;
                }
                case 55: {
                    string = "Kuklis";
                    break;
                }
                case 56: {
                    string = "Kukurecani";
                    break;
                }
                case 57: {
                    string = "Kumanovo";
                    break;
                }
                case 58: {
                    string = "Labunista";
                    break;
                }
                case 59: {
                    string = "Lipkovo";
                    break;
                }
                case 60: {
                    string = "Lozovo";
                    break;
                }
                case 61: {
                    string = "Lukovo";
                    break;
                }
                case 62: {
                    string = "Makedonska Kamenica";
                    break;
                }
                case 63: {
                    string = "Makedonski Brod";
                    break;
                }
                case 64: {
                    string = "Mavrovi Anovi";
                    break;
                }
                case 65: {
                    string = "Meseista";
                    break;
                }
                case 66: {
                    string = "Miravci";
                    break;
                }
                case 67: {
                    string = "Mogila";
                    break;
                }
                case 68: {
                    string = "Murtino";
                    break;
                }
                case 69: {
                    string = "Negotino";
                    break;
                }
                case 70: {
                    string = "Negotino-Polosko";
                    break;
                }
                case 71: {
                    string = "Novaci";
                    break;
                }
                case 72: {
                    string = "Novo Selo";
                    break;
                }
                case 73: {
                    string = "Oblesevo";
                    break;
                }
                case 74: {
                    string = "Ohrid";
                    break;
                }
                case 75: {
                    string = "Orasac";
                    break;
                }
                case 76: {
                    string = "Orizari";
                    break;
                }
                case 77: {
                    string = "Oslomej";
                    break;
                }
                case 78: {
                    string = "Pehcevo";
                    break;
                }
                case 79: {
                    string = "Petrovec";
                    break;
                }
                case 80: {
                    string = "Plasnica";
                    break;
                }
                case 81: {
                    string = "Podares";
                    break;
                }
                case 82: {
                    string = "Prilep";
                    break;
                }
                case 83: {
                    string = "Probistip";
                    break;
                }
                case 84: {
                    string = "Radovis";
                    break;
                }
                case 85: {
                    string = "Rankovce";
                    break;
                }
                case 86: {
                    string = "Resen";
                    break;
                }
                case 87: {
                    string = "Rosoman";
                    break;
                }
                case 88: {
                    string = "Rostusa";
                    break;
                }
                case 89: {
                    string = "Samokov";
                    break;
                }
                case 90: {
                    string = "Saraj";
                    break;
                }
                case 91: {
                    string = "Sipkovica";
                    break;
                }
                case 92: {
                    string = "Sopiste";
                    break;
                }
                case 93: {
                    string = "Sopotnica";
                    break;
                }
                case 94: {
                    string = "Srbinovo";
                    break;
                }
                case 95: {
                    string = "Staravina";
                    break;
                }
                case 96: {
                    string = "Star Dojran";
                    break;
                }
                case 97: {
                    string = "Staro Nagoricane";
                    break;
                }
                case 98: {
                    string = "Stip";
                    break;
                }
                case 99: {
                    string = "Struga";
                    break;
                }
                case 832: {
                    string = "Strumica";
                    break;
                }
                case 833: {
                    string = "Studenicani";
                    break;
                }
                case 834: {
                    string = "Suto Orizari";
                    break;
                }
                case 835: {
                    string = "Sveti Nikole";
                    break;
                }
                case 836: {
                    string = "Tearce";
                    break;
                }
                case 837: {
                    string = "Tetovo";
                    break;
                }
                case 838: {
                    string = "Topolcani";
                    break;
                }
                case 839: {
                    string = "Valandovo";
                    break;
                }
                case 840: {
                    string = "Vasilevo";
                    break;
                }
                case 875: {
                    string = "Veles";
                    break;
                }
                case 876: {
                    string = "Velesta";
                    break;
                }
                case 877: {
                    string = "Vevcani";
                    break;
                }
                case 878: {
                    string = "Vinica";
                    break;
                }
                case 879: {
                    string = "Vitoliste";
                    break;
                }
                case 880: {
                    string = "Vranestica";
                    break;
                }
                case 881: {
                    string = "Vrapciste";
                    break;
                }
                case 882: {
                    string = "Vratnica";
                    break;
                }
                case 883: {
                    string = "Vrutok";
                    break;
                }
                case 918: {
                    string = "Zajas";
                    break;
                }
                case 919: {
                    string = "Zelenikovo";
                    break;
                }
                case 920: {
                    string = "Zelino";
                    break;
                }
                case 921: {
                    string = "Zitose";
                    break;
                }
                case 922: {
                    string = "Zletovo";
                    break;
                }
                case 923: {
                    string = "Zrnovci";
                }
            }
        }
        if (string2.equals("ML")) {
            switch (var3_3) {
                case 1: {
                    string = "Bamako";
                    break;
                }
                case 3: {
                    string = "Kayes";
                    break;
                }
                case 4: {
                    string = "Mopti";
                    break;
                }
                case 5: {
                    string = "Segou";
                    break;
                }
                case 6: {
                    string = "Sikasso";
                    break;
                }
                case 7: {
                    string = "Koulikoro";
                    break;
                }
                case 8: {
                    string = "Tombouctou";
                    break;
                }
                case 9: {
                    string = "Gao";
                    break;
                }
                case 10: {
                    string = "Kidal";
                }
            }
        }
        if (string2.equals("MM")) {
            switch (var3_3) {
                case 1: {
                    string = "Rakhine State";
                    break;
                }
                case 2: {
                    string = "Chin State";
                    break;
                }
                case 3: {
                    string = "Irrawaddy";
                    break;
                }
                case 4: {
                    string = "Kachin State";
                    break;
                }
                case 5: {
                    string = "Karan State";
                    break;
                }
                case 6: {
                    string = "Kayah State";
                    break;
                }
                case 7: {
                    string = "Magwe";
                    break;
                }
                case 8: {
                    string = "Mandalay";
                    break;
                }
                case 9: {
                    string = "Pegu";
                    break;
                }
                case 10: {
                    string = "Sagaing";
                    break;
                }
                case 11: {
                    string = "Shan State";
                    break;
                }
                case 12: {
                    string = "Tenasserim";
                    break;
                }
                case 13: {
                    string = "Mon State";
                    break;
                }
                case 14: {
                    string = "Rangoon";
                    break;
                }
                case 17: {
                    string = "Yangon";
                }
            }
        }
        if (string2.equals("MN")) {
            switch (var3_3) {
                case 1: {
                    string = "Arhangay";
                    break;
                }
                case 2: {
                    string = "Bayanhongor";
                    break;
                }
                case 3: {
                    string = "Bayan-Olgiy";
                    break;
                }
                case 5: {
                    string = "Darhan";
                    break;
                }
                case 6: {
                    string = "Dornod";
                    break;
                }
                case 7: {
                    string = "Dornogovi";
                    break;
                }
                case 8: {
                    string = "Dundgovi";
                    break;
                }
                case 9: {
                    string = "Dzavhan";
                    break;
                }
                case 10: {
                    string = "Govi-Altay";
                    break;
                }
                case 11: {
                    string = "Hentiy";
                    break;
                }
                case 12: {
                    string = "Hovd";
                    break;
                }
                case 13: {
                    string = "Hovsgol";
                    break;
                }
                case 14: {
                    string = "Omnogovi";
                    break;
                }
                case 15: {
                    string = "Ovorhangay";
                    break;
                }
                case 16: {
                    string = "Selenge";
                    break;
                }
                case 17: {
                    string = "Suhbaatar";
                    break;
                }
                case 18: {
                    string = "Tov";
                    break;
                }
                case 19: {
                    string = "Uvs";
                    break;
                }
                case 20: {
                    string = "Ulaanbaatar";
                    break;
                }
                case 21: {
                    string = "Bulgan";
                    break;
                }
                case 22: {
                    string = "Erdenet";
                    break;
                }
                case 23: {
                    string = "Darhan-Uul";
                    break;
                }
                case 24: {
                    string = "Govisumber";
                    break;
                }
                case 25: {
                    string = "Orhon";
                }
            }
        }
        if (string2.equals("MO")) {
            switch (var3_3) {
                case 1: {
                    string = "Ilhas";
                    break;
                }
                case 2: {
                    string = "Macau";
                }
            }
        }
        if (string2.equals("MR")) {
            switch (var3_3) {
                case 1: {
                    string = "Hodh Ech Chargui";
                    break;
                }
                case 2: {
                    string = "Hodh El Gharbi";
                    break;
                }
                case 3: {
                    string = "Assaba";
                    break;
                }
                case 4: {
                    string = "Gorgol";
                    break;
                }
                case 5: {
                    string = "Brakna";
                    break;
                }
                case 6: {
                    string = "Trarza";
                    break;
                }
                case 7: {
                    string = "Adrar";
                    break;
                }
                case 8: {
                    string = "Dakhlet Nouadhibou";
                    break;
                }
                case 9: {
                    string = "Tagant";
                    break;
                }
                case 10: {
                    string = "Guidimaka";
                    break;
                }
                case 11: {
                    string = "Tiris Zemmour";
                    break;
                }
                case 12: {
                    string = "Inchiri";
                }
            }
        }
        if (string2.equals("MS")) {
            switch (var3_3) {
                case 1: {
                    string = "Saint Anthony";
                    break;
                }
                case 2: {
                    string = "Saint Georges";
                    break;
                }
                case 3: {
                    string = "Saint Peter";
                }
            }
        }
        if (string2.equals("MU")) {
            switch (var3_3) {
                case 12: {
                    string = "Black River";
                    break;
                }
                case 13: {
                    string = "Flacq";
                    break;
                }
                case 14: {
                    string = "Grand Port";
                    break;
                }
                case 15: {
                    string = "Moka";
                    break;
                }
                case 16: {
                    string = "Pamplemousses";
                    break;
                }
                case 17: {
                    string = "Plaines Wilhems";
                    break;
                }
                case 18: {
                    string = "Port Louis";
                    break;
                }
                case 19: {
                    string = "Riviere du Rempart";
                    break;
                }
                case 20: {
                    string = "Savanne";
                    break;
                }
                case 21: {
                    string = "Agalega Islands";
                    break;
                }
                case 22: {
                    string = "Cargados Carajos";
                    break;
                }
                case 23: {
                    string = "Rodrigues";
                }
            }
        }
        if (string2.equals("MV")) {
            switch (var3_3) {
                case 1: {
                    string = "Seenu";
                    break;
                }
                case 2: {
                    string = "Aliff";
                    break;
                }
                case 3: {
                    string = "Laviyani";
                    break;
                }
                case 4: {
                    string = "Waavu";
                    break;
                }
                case 5: {
                    string = "Laamu";
                    break;
                }
                case 7: {
                    string = "Haa Aliff";
                    break;
                }
                case 8: {
                    string = "Thaa";
                    break;
                }
                case 12: {
                    string = "Meemu";
                    break;
                }
                case 13: {
                    string = "Raa";
                    break;
                }
                case 14: {
                    string = "Faafu";
                    break;
                }
                case 17: {
                    string = "Daalu";
                    break;
                }
                case 20: {
                    string = "Baa";
                    break;
                }
                case 23: {
                    string = "Haa Daalu";
                    break;
                }
                case 24: {
                    string = "Shaviyani";
                    break;
                }
                case 25: {
                    string = "Noonu";
                    break;
                }
                case 26: {
                    string = "Kaafu";
                    break;
                }
                case 27: {
                    string = "Gaafu Aliff";
                    break;
                }
                case 28: {
                    string = "Gaafu Daalu";
                    break;
                }
                case 29: {
                    string = "Naviyani";
                    break;
                }
                case 40: {
                    string = "Male";
                }
            }
        }
        if (string2.equals("MW")) {
            switch (var3_3) {
                case 2: {
                    string = "Chikwawa";
                    break;
                }
                case 3: {
                    string = "Chiradzulu";
                    break;
                }
                case 4: {
                    string = "Chitipa";
                    break;
                }
                case 5: {
                    string = "Thyolo";
                    break;
                }
                case 6: {
                    string = "Dedza";
                    break;
                }
                case 7: {
                    string = "Dowa";
                    break;
                }
                case 8: {
                    string = "Karonga";
                    break;
                }
                case 9: {
                    string = "Kasungu";
                    break;
                }
                case 11: {
                    string = "Lilongwe";
                    break;
                }
                case 12: {
                    string = "Mangochi";
                    break;
                }
                case 13: {
                    string = "Mchinji";
                    break;
                }
                case 15: {
                    string = "Mzimba";
                    break;
                }
                case 16: {
                    string = "Ntcheu";
                    break;
                }
                case 17: {
                    string = "Nkhata Bay";
                    break;
                }
                case 18: {
                    string = "Nkhotakota";
                    break;
                }
                case 19: {
                    string = "Nsanje";
                    break;
                }
                case 20: {
                    string = "Ntchisi";
                    break;
                }
                case 21: {
                    string = "Rumphi";
                    break;
                }
                case 22: {
                    string = "Salima";
                    break;
                }
                case 23: {
                    string = "Zomba";
                    break;
                }
                case 24: {
                    string = "Blantyre";
                    break;
                }
                case 25: {
                    string = "Mwanza";
                    break;
                }
                case 26: {
                    string = "Balaka";
                    break;
                }
                case 27: {
                    string = "Likoma";
                    break;
                }
                case 28: {
                    string = "Machinga";
                    break;
                }
                case 29: {
                    string = "Mulanje";
                    break;
                }
                case 30: {
                    string = "Phalombe";
                }
            }
        }
        if (string2.equals("MX")) {
            switch (var3_3) {
                case 1: {
                    string = "Aguascalientes";
                    break;
                }
                case 2: {
                    string = "Baja California";
                    break;
                }
                case 3: {
                    string = "Baja California Sur";
                    break;
                }
                case 4: {
                    string = "Campeche";
                    break;
                }
                case 5: {
                    string = "Chiapas";
                    break;
                }
                case 6: {
                    string = "Chihuahua";
                    break;
                }
                case 7: {
                    string = "Coahuila de Zaragoza";
                    break;
                }
                case 8: {
                    string = "Colima";
                    break;
                }
                case 9: {
                    string = "Distrito Federal";
                    break;
                }
                case 10: {
                    string = "Durango";
                    break;
                }
                case 11: {
                    string = "Guanajuato";
                    break;
                }
                case 12: {
                    string = "Guerrero";
                    break;
                }
                case 13: {
                    string = "Hidalgo";
                    break;
                }
                case 14: {
                    string = "Jalisco";
                    break;
                }
                case 15: {
                    string = "Mexico";
                    break;
                }
                case 16: {
                    string = "Michoacan de Ocampo";
                    break;
                }
                case 17: {
                    string = "Morelos";
                    break;
                }
                case 18: {
                    string = "Nayarit";
                    break;
                }
                case 19: {
                    string = "Nuevo Leon";
                    break;
                }
                case 20: {
                    string = "Oaxaca";
                    break;
                }
                case 21: {
                    string = "Puebla";
                    break;
                }
                case 22: {
                    string = "Queretaro de Arteaga";
                    break;
                }
                case 23: {
                    string = "Quintana Roo";
                    break;
                }
                case 24: {
                    string = "San Luis Potosi";
                    break;
                }
                case 25: {
                    string = "Sinaloa";
                    break;
                }
                case 26: {
                    string = "Sonora";
                    break;
                }
                case 27: {
                    string = "Tabasco";
                    break;
                }
                case 28: {
                    string = "Tamaulipas";
                    break;
                }
                case 29: {
                    string = "Tlaxcala";
                    break;
                }
                case 30: {
                    string = "Veracruz-Llave";
                    break;
                }
                case 31: {
                    string = "Yucatan";
                    break;
                }
                case 32: {
                    string = "Zacatecas";
                }
            }
        }
        if (string2.equals("MY")) {
            switch (var3_3) {
                case 1: {
                    string = "Johor";
                    break;
                }
                case 2: {
                    string = "Kedah";
                    break;
                }
                case 3: {
                    string = "Kelantan";
                    break;
                }
                case 4: {
                    string = "Melaka";
                    break;
                }
                case 5: {
                    string = "Negeri Sembilan";
                    break;
                }
                case 6: {
                    string = "Pahang";
                    break;
                }
                case 7: {
                    string = "Perak";
                    break;
                }
                case 8: {
                    string = "Perlis";
                    break;
                }
                case 9: {
                    string = "Pulau Pinang";
                    break;
                }
                case 11: {
                    string = "Sarawak";
                    break;
                }
                case 12: {
                    string = "Selangor";
                    break;
                }
                case 13: {
                    string = "Terengganu";
                    break;
                }
                case 14: {
                    string = "Kuala Lumpur";
                    break;
                }
                case 15: {
                    string = "Labuan";
                    break;
                }
                case 16: {
                    string = "Sabah";
                    break;
                }
                case 17: {
                    string = "Putrajaya";
                }
            }
        }
        if (string2.equals("MZ")) {
            switch (var3_3) {
                case 1: {
                    string = "Cabo Delgado";
                    break;
                }
                case 2: {
                    string = "Gaza";
                    break;
                }
                case 3: {
                    string = "Inhambane";
                    break;
                }
                case 4: {
                    string = "Maputo";
                    break;
                }
                case 5: {
                    string = "Sofala";
                    break;
                }
                case 6: {
                    string = "Nampula";
                    break;
                }
                case 7: {
                    string = "Niassa";
                    break;
                }
                case 8: {
                    string = "Tete";
                    break;
                }
                case 9: {
                    string = "Zambezia";
                    break;
                }
                case 10: {
                    string = "Manica";
                    break;
                }
                case 11: {
                    string = "Maputo";
                }
            }
        }
        if (string2.equals("NA")) {
            switch (var3_3) {
                case 1: {
                    string = "Bethanien";
                    break;
                }
                case 2: {
                    string = "Caprivi Oos";
                    break;
                }
                case 3: {
                    string = "Boesmanland";
                    break;
                }
                case 4: {
                    string = "Gobabis";
                    break;
                }
                case 5: {
                    string = "Grootfontein";
                    break;
                }
                case 6: {
                    string = "Kaokoland";
                    break;
                }
                case 7: {
                    string = "Karibib";
                    break;
                }
                case 8: {
                    string = "Keetmanshoop";
                    break;
                }
                case 9: {
                    string = "Luderitz";
                    break;
                }
                case 10: {
                    string = "Maltahohe";
                    break;
                }
                case 11: {
                    string = "Okahandja";
                    break;
                }
                case 12: {
                    string = "Omaruru";
                    break;
                }
                case 13: {
                    string = "Otjiwarongo";
                    break;
                }
                case 14: {
                    string = "Outjo";
                    break;
                }
                case 15: {
                    string = "Owambo";
                    break;
                }
                case 16: {
                    string = "Rehoboth";
                    break;
                }
                case 17: {
                    string = "Swakopmund";
                    break;
                }
                case 18: {
                    string = "Tsumeb";
                    break;
                }
                case 20: {
                    string = "Karasburg";
                    break;
                }
                case 21: {
                    string = "Windhoek";
                    break;
                }
                case 22: {
                    string = "Damaraland";
                    break;
                }
                case 23: {
                    string = "Hereroland Oos";
                    break;
                }
                case 24: {
                    string = "Hereroland Wes";
                    break;
                }
                case 25: {
                    string = "Kavango";
                    break;
                }
                case 26: {
                    string = "Mariental";
                    break;
                }
                case 27: {
                    string = "Namaland";
                    break;
                }
                case 28: {
                    string = "Caprivi";
                    break;
                }
                case 29: {
                    string = "Erongo";
                    break;
                }
                case 30: {
                    string = "Hardap";
                    break;
                }
                case 31: {
                    string = "Karas";
                    break;
                }
                case 32: {
                    string = "Kunene";
                    break;
                }
                case 33: {
                    string = "Ohangwena";
                    break;
                }
                case 34: {
                    string = "Okavango";
                    break;
                }
                case 35: {
                    string = "Omaheke";
                    break;
                }
                case 36: {
                    string = "Omusati";
                    break;
                }
                case 37: {
                    string = "Oshana";
                    break;
                }
                case 38: {
                    string = "Oshikoto";
                    break;
                }
                case 39: {
                    string = "Otjozondjupa";
                }
            }
        }
        if (string2.equals("NE")) {
            switch (var3_3) {
                case 1: {
                    string = "Agadez";
                    break;
                }
                case 2: {
                    string = "Diffa";
                    break;
                }
                case 3: {
                    string = "Dosso";
                    break;
                }
                case 4: {
                    string = "Maradi";
                    break;
                }
                case 5: {
                    string = "Niamey";
                    break;
                }
                case 6: {
                    string = "Tahoua";
                    break;
                }
                case 7: {
                    string = "Zinder";
                    break;
                }
                case 8: {
                    string = "Niamey";
                }
            }
        }
        if (string2.equals("NG")) {
            switch (var3_3) {
                case 5: {
                    string = "Lagos";
                    break;
                }
                case 10: {
                    string = "Rivers";
                    break;
                }
                case 11: {
                    string = "Federal Capital Territory";
                    break;
                }
                case 12: {
                    string = "Gongola";
                    break;
                }
                case 16: {
                    string = "Ogun";
                    break;
                }
                case 17: {
                    string = "Ondo";
                    break;
                }
                case 18: {
                    string = "Oyo";
                    break;
                }
                case 21: {
                    string = "Akwa Ibom";
                    break;
                }
                case 22: {
                    string = "Cross River";
                    break;
                }
                case 23: {
                    string = "Kaduna";
                    break;
                }
                case 24: {
                    string = "Katsina";
                    break;
                }
                case 25: {
                    string = "Anambra";
                    break;
                }
                case 26: {
                    string = "Benue";
                    break;
                }
                case 27: {
                    string = "Borno";
                    break;
                }
                case 28: {
                    string = "Imo";
                    break;
                }
                case 29: {
                    string = "Kano";
                    break;
                }
                case 30: {
                    string = "Kwara";
                    break;
                }
                case 31: {
                    string = "Niger";
                    break;
                }
                case 32: {
                    string = "Oyo";
                    break;
                }
                case 35: {
                    string = "Adamawa";
                    break;
                }
                case 36: {
                    string = "Delta";
                    break;
                }
                case 37: {
                    string = "Edo";
                    break;
                }
                case 39: {
                    string = "Jigawa";
                    break;
                }
                case 40: {
                    string = "Kebbi";
                    break;
                }
                case 41: {
                    string = "Kogi";
                    break;
                }
                case 42: {
                    string = "Osun";
                    break;
                }
                case 43: {
                    string = "Taraba";
                    break;
                }
                case 44: {
                    string = "Yobe";
                    break;
                }
                case 45: {
                    string = "Abia";
                    break;
                }
                case 46: {
                    string = "Bauchi";
                    break;
                }
                case 47: {
                    string = "Enugu";
                    break;
                }
                case 48: {
                    string = "Ondo";
                    break;
                }
                case 49: {
                    string = "Plateau";
                    break;
                }
                case 50: {
                    string = "Rivers";
                    break;
                }
                case 51: {
                    string = "Sokoto";
                    break;
                }
                case 52: {
                    string = "Bayelsa";
                    break;
                }
                case 53: {
                    string = "Ebonyi";
                    break;
                }
                case 54: {
                    string = "Ekiti";
                    break;
                }
                case 55: {
                    string = "Gombe";
                    break;
                }
                case 56: {
                    string = "Nassarawa";
                    break;
                }
                case 57: {
                    string = "Zamfara";
                }
            }
        }
        if (string2.equals("NI")) {
            switch (var3_3) {
                case 1: {
                    string = "Boaco";
                    break;
                }
                case 2: {
                    string = "Carazo";
                    break;
                }
                case 3: {
                    string = "Chinandega";
                    break;
                }
                case 4: {
                    string = "Chontales";
                    break;
                }
                case 5: {
                    string = "Esteli";
                    break;
                }
                case 6: {
                    string = "Granada";
                    break;
                }
                case 7: {
                    string = "Jinotega";
                    break;
                }
                case 8: {
                    string = "Leon";
                    break;
                }
                case 9: {
                    string = "Madriz";
                    break;
                }
                case 10: {
                    string = "Managua";
                    break;
                }
                case 11: {
                    string = "Masaya";
                    break;
                }
                case 12: {
                    string = "Matagalpa";
                    break;
                }
                case 13: {
                    string = "Nueva Segovia";
                    break;
                }
                case 14: {
                    string = "Rio San Juan";
                    break;
                }
                case 15: {
                    string = "Rivas";
                    break;
                }
                case 16: {
                    string = "Zelaya";
                    break;
                }
                case 17: {
                    string = "Autonoma Atlantico Norte";
                    break;
                }
                case 18: {
                    string = "Region Autonoma Atlantico Sur";
                }
            }
        }
        if (string2.equals("NL")) {
            switch (var3_3) {
                case 1: {
                    string = "Drenthe";
                    break;
                }
                case 2: {
                    string = "Friesland";
                    break;
                }
                case 3: {
                    string = "Gelderland";
                    break;
                }
                case 4: {
                    string = "Groningen";
                    break;
                }
                case 5: {
                    string = "Limburg";
                    break;
                }
                case 6: {
                    string = "Noord-Brabant";
                    break;
                }
                case 7: {
                    string = "Noord-Holland";
                    break;
                }
                case 8: {
                    string = "Overijssel";
                    break;
                }
                case 9: {
                    string = "Utrecht";
                    break;
                }
                case 10: {
                    string = "Zeeland";
                    break;
                }
                case 11: {
                    string = "Zuid-Holland";
                    break;
                }
                case 12: {
                    string = "Dronten";
                    break;
                }
                case 13: {
                    string = "Zuidelijke IJsselmeerpolders";
                    break;
                }
                case 14: {
                    string = "Lelystad";
                    break;
                }
                case 15: {
                    string = "Overijssel";
                    break;
                }
                case 16: {
                    string = "Flevoland";
                }
            }
        }
        if (string2.equals("NO")) {
            switch (var3_3) {
                case 1: {
                    string = "Akershus";
                    break;
                }
                case 2: {
                    string = "Aust-Agder";
                    break;
                }
                case 4: {
                    string = "Buskerud";
                    break;
                }
                case 5: {
                    string = "Finnmark";
                    break;
                }
                case 6: {
                    string = "Hedmark";
                    break;
                }
                case 7: {
                    string = "Hordaland";
                    break;
                }
                case 8: {
                    string = "More og Romsdal";
                    break;
                }
                case 9: {
                    string = "Nordland";
                    break;
                }
                case 10: {
                    string = "Nord-Trondelag";
                    break;
                }
                case 11: {
                    string = "Oppland";
                    break;
                }
                case 12: {
                    string = "Oslo";
                    break;
                }
                case 13: {
                    string = "Ostfold";
                    break;
                }
                case 14: {
                    string = "Rogaland";
                    break;
                }
                case 15: {
                    string = "Sogn og Fjordane";
                    break;
                }
                case 16: {
                    string = "Sor-Trondelag";
                    break;
                }
                case 17: {
                    string = "Telemark";
                    break;
                }
                case 18: {
                    string = "Troms";
                    break;
                }
                case 19: {
                    string = "Vest-Agder";
                    break;
                }
                case 20: {
                    string = "Vestfold";
                }
            }
        }
        if (string2.equals("NP")) {
            switch (var3_3) {
                case 1: {
                    string = "Bagmati";
                    break;
                }
                case 2: {
                    string = "Bheri";
                    break;
                }
                case 3: {
                    string = "Dhawalagiri";
                    break;
                }
                case 4: {
                    string = "Gandaki";
                    break;
                }
                case 5: {
                    string = "Janakpur";
                    break;
                }
                case 6: {
                    string = "Karnali";
                    break;
                }
                case 7: {
                    string = "Kosi";
                    break;
                }
                case 8: {
                    string = "Lumbini";
                    break;
                }
                case 9: {
                    string = "Mahakali";
                    break;
                }
                case 10: {
                    string = "Mechi";
                    break;
                }
                case 11: {
                    string = "Narayani";
                    break;
                }
                case 12: {
                    string = "Rapti";
                    break;
                }
                case 13: {
                    string = "Sagarmatha";
                    break;
                }
                case 14: {
                    string = "Seti";
                }
            }
        }
        if (string2.equals("NR")) {
            switch (var3_3) {
                case 1: {
                    string = "Aiwo";
                    break;
                }
                case 2: {
                    string = "Anabar";
                    break;
                }
                case 3: {
                    string = "Anetan";
                    break;
                }
                case 4: {
                    string = "Anibare";
                    break;
                }
                case 5: {
                    string = "Baiti";
                    break;
                }
                case 6: {
                    string = "Boe";
                    break;
                }
                case 7: {
                    string = "Buada";
                    break;
                }
                case 8: {
                    string = "Denigomodu";
                    break;
                }
                case 9: {
                    string = "Ewa";
                    break;
                }
                case 10: {
                    string = "Ijuw";
                    break;
                }
                case 11: {
                    string = "Meneng";
                    break;
                }
                case 12: {
                    string = "Nibok";
                    break;
                }
                case 13: {
                    string = "Uaboe";
                    break;
                }
                case 14: {
                    string = "Yaren";
                }
            }
        }
        if (string2.equals("NZ")) {
            switch (var3_3) {
                case 10: {
                    string = "Chatham Islands";
                    break;
                }
                case 1010: {
                    string = "Auckland";
                    break;
                }
                case 1011: {
                    string = "Bay of Plenty";
                    break;
                }
                case 1012: {
                    string = "Canterbury";
                    break;
                }
                case 1047: {
                    string = "Gisborne";
                    break;
                }
                case 1048: {
                    string = "Hawke's Bay";
                    break;
                }
                case 1049: {
                    string = "Manawatu-Wanganui";
                    break;
                }
                case 1050: {
                    string = "Marlborough";
                    break;
                }
                case 1051: {
                    string = "Nelson";
                    break;
                }
                case 1052: {
                    string = "Northland";
                    break;
                }
                case 1053: {
                    string = "Otago";
                    break;
                }
                case 1054: {
                    string = "Southland";
                    break;
                }
                case 1055: {
                    string = "Taranaki";
                    break;
                }
                case 1090: {
                    string = "Waikato";
                    break;
                }
                case 1091: {
                    string = "Wellington";
                    break;
                }
                case 1092: {
                    string = "West Coast";
                    break;
                }
                case 85: {
                    string = "Waikato";
                }
            }
        }
        if (string2.equals("OM")) {
            switch (var3_3) {
                case 1: {
                    string = "Ad Dakhiliyah";
                    break;
                }
                case 2: {
                    string = "Al Batinah";
                    break;
                }
                case 3: {
                    string = "Al Wusta";
                    break;
                }
                case 4: {
                    string = "Ash Sharqiyah";
                    break;
                }
                case 5: {
                    string = "Az Zahirah";
                    break;
                }
                case 6: {
                    string = "Masqat";
                    break;
                }
                case 7: {
                    string = "Musandam";
                    break;
                }
                case 8: {
                    string = "Zufar";
                }
            }
        }
        if (string2.equals("PA")) {
            switch (var3_3) {
                case 1: {
                    string = "Bocas del Toro";
                    break;
                }
                case 2: {
                    string = "Chiriqui";
                    break;
                }
                case 3: {
                    string = "Cocle";
                    break;
                }
                case 4: {
                    string = "Colon";
                    break;
                }
                case 5: {
                    string = "Darien";
                    break;
                }
                case 6: {
                    string = "Herrera";
                    break;
                }
                case 7: {
                    string = "Los Santos";
                    break;
                }
                case 8: {
                    string = "Panama";
                    break;
                }
                case 9: {
                    string = "San Blas";
                    break;
                }
                case 10: {
                    string = "Veraguas";
                }
            }
        }
        if (string2.equals("PE")) {
            switch (var3_3) {
                case 1: {
                    string = "Amazonas";
                    break;
                }
                case 2: {
                    string = "Ancash";
                    break;
                }
                case 3: {
                    string = "Apurimac";
                    break;
                }
                case 4: {
                    string = "Arequipa";
                    break;
                }
                case 5: {
                    string = "Ayacucho";
                    break;
                }
                case 6: {
                    string = "Cajamarca";
                    break;
                }
                case 7: {
                    string = "Callao";
                    break;
                }
                case 8: {
                    string = "Cusco";
                    break;
                }
                case 9: {
                    string = "Huancavelica";
                    break;
                }
                case 10: {
                    string = "Huanuco";
                    break;
                }
                case 11: {
                    string = "Ica";
                    break;
                }
                case 12: {
                    string = "Junin";
                    break;
                }
                case 13: {
                    string = "La Libertad";
                    break;
                }
                case 14: {
                    string = "Lambayeque";
                    break;
                }
                case 15: {
                    string = "Lima";
                    break;
                }
                case 16: {
                    string = "Loreto";
                    break;
                }
                case 17: {
                    string = "Madre de Dios";
                    break;
                }
                case 18: {
                    string = "Moquegua";
                    break;
                }
                case 19: {
                    string = "Pasco";
                    break;
                }
                case 20: {
                    string = "Piura";
                    break;
                }
                case 21: {
                    string = "Puno";
                    break;
                }
                case 22: {
                    string = "San Martin";
                    break;
                }
                case 23: {
                    string = "Tacna";
                    break;
                }
                case 24: {
                    string = "Tumbes";
                    break;
                }
                case 25: {
                    string = "Ucayali";
                }
            }
        }
        if (string2.equals("PG")) {
            switch (var3_3) {
                case 1: {
                    string = "Central";
                    break;
                }
                case 2: {
                    string = "Gulf";
                    break;
                }
                case 3: {
                    string = "Milne Bay";
                    break;
                }
                case 4: {
                    string = "Northern";
                    break;
                }
                case 5: {
                    string = "Southern Highlands";
                    break;
                }
                case 6: {
                    string = "Western";
                    break;
                }
                case 7: {
                    string = "North Solomons";
                    break;
                }
                case 8: {
                    string = "Chimbu";
                    break;
                }
                case 9: {
                    string = "Eastern Highlands";
                    break;
                }
                case 10: {
                    string = "East New Britain";
                    break;
                }
                case 11: {
                    string = "East Sepik";
                    break;
                }
                case 12: {
                    string = "Madang";
                    break;
                }
                case 13: {
                    string = "Manus";
                    break;
                }
                case 14: {
                    string = "Morobe";
                    break;
                }
                case 15: {
                    string = "New Ireland";
                    break;
                }
                case 16: {
                    string = "Western Highlands";
                    break;
                }
                case 17: {
                    string = "West New Britain";
                    break;
                }
                case 18: {
                    string = "Sandaun";
                    break;
                }
                case 19: {
                    string = "Enga";
                    break;
                }
                case 20: {
                    string = "National Capital";
                }
            }
        }
        if (string2.equals("PH")) {
            switch (var3_3) {
                case 1: {
                    string = "Abra";
                    break;
                }
                case 2: {
                    string = "Agusan del Norte";
                    break;
                }
                case 3: {
                    string = "Agusan del Sur";
                    break;
                }
                case 4: {
                    string = "Aklan";
                    break;
                }
                case 5: {
                    string = "Albay";
                    break;
                }
                case 6: {
                    string = "Antique";
                    break;
                }
                case 7: {
                    string = "Bataan";
                    break;
                }
                case 8: {
                    string = "Batanes";
                    break;
                }
                case 9: {
                    string = "Batangas";
                    break;
                }
                case 10: {
                    string = "Benguet";
                    break;
                }
                case 11: {
                    string = "Bohol";
                    break;
                }
                case 12: {
                    string = "Bukidnon";
                    break;
                }
                case 13: {
                    string = "Bulacan";
                    break;
                }
                case 14: {
                    string = "Cagayan";
                    break;
                }
                case 15: {
                    string = "Camarines Norte";
                    break;
                }
                case 16: {
                    string = "Camarines Sur";
                    break;
                }
                case 17: {
                    string = "Camiguin";
                    break;
                }
                case 18: {
                    string = "Capiz";
                    break;
                }
                case 19: {
                    string = "Catanduanes";
                    break;
                }
                case 20: {
                    string = "Cavite";
                    break;
                }
                case 21: {
                    string = "Cebu";
                    break;
                }
                case 22: {
                    string = "Basilan";
                    break;
                }
                case 23: {
                    string = "Eastern Samar";
                    break;
                }
                case 24: {
                    string = "Davao";
                    break;
                }
                case 25: {
                    string = "Davao del Sur";
                    break;
                }
                case 26: {
                    string = "Davao Oriental";
                    break;
                }
                case 27: {
                    string = "Ifugao";
                    break;
                }
                case 28: {
                    string = "Ilocos Norte";
                    break;
                }
                case 29: {
                    string = "Ilocos Sur";
                    break;
                }
                case 30: {
                    string = "Iloilo";
                    break;
                }
                case 31: {
                    string = "Isabela";
                    break;
                }
                case 32: {
                    string = "Kalinga-Apayao";
                    break;
                }
                case 33: {
                    string = "Laguna";
                    break;
                }
                case 34: {
                    string = "Lanao del Norte";
                    break;
                }
                case 35: {
                    string = "Lanao del Sur";
                    break;
                }
                case 36: {
                    string = "La Union";
                    break;
                }
                case 37: {
                    string = "Leyte";
                    break;
                }
                case 38: {
                    string = "Marinduque";
                    break;
                }
                case 39: {
                    string = "Masbate";
                    break;
                }
                case 40: {
                    string = "Mindoro Occidental";
                    break;
                }
                case 41: {
                    string = "Mindoro Oriental";
                    break;
                }
                case 42: {
                    string = "Misamis Occidental";
                    break;
                }
                case 43: {
                    string = "Misamis Oriental";
                    break;
                }
                case 44: {
                    string = "Mountain";
                    break;
                }
                case 45: {
                    string = "Negros Occidental";
                    break;
                }
                case 46: {
                    string = "Negros Oriental";
                    break;
                }
                case 47: {
                    string = "Nueva Ecija";
                    break;
                }
                case 48: {
                    string = "Nueva Vizcaya";
                    break;
                }
                case 49: {
                    string = "Palawan";
                    break;
                }
                case 50: {
                    string = "Pampanga";
                    break;
                }
                case 51: {
                    string = "Pangasinan";
                    break;
                }
                case 53: {
                    string = "Rizal";
                    break;
                }
                case 54: {
                    string = "Romblon";
                    break;
                }
                case 55: {
                    string = "Samar";
                    break;
                }
                case 56: {
                    string = "Maguindanao";
                    break;
                }
                case 57: {
                    string = "North Cotabato";
                    break;
                }
                case 58: {
                    string = "Sorsogon";
                    break;
                }
                case 59: {
                    string = "Southern Leyte";
                    break;
                }
                case 60: {
                    string = "Sulu";
                    break;
                }
                case 61: {
                    string = "Surigao del Norte";
                    break;
                }
                case 62: {
                    string = "Surigao del Sur";
                    break;
                }
                case 63: {
                    string = "Tarlac";
                    break;
                }
                case 64: {
                    string = "Zambales";
                    break;
                }
                case 65: {
                    string = "Zamboanga del Norte";
                    break;
                }
                case 66: {
                    string = "Zamboanga del Sur";
                    break;
                }
                case 67: {
                    string = "Northern Samar";
                    break;
                }
                case 68: {
                    string = "Quirino";
                    break;
                }
                case 69: {
                    string = "Siquijor";
                    break;
                }
                case 70: {
                    string = "South Cotabato";
                    break;
                }
                case 71: {
                    string = "Sultan Kudarat";
                    break;
                }
                case 72: {
                    string = "Tawitawi";
                    break;
                }
                case 832: {
                    string = "Angeles";
                    break;
                }
                case 833: {
                    string = "Bacolod";
                    break;
                }
                case 834: {
                    string = "Bago";
                    break;
                }
                case 835: {
                    string = "Baguio";
                    break;
                }
                case 836: {
                    string = "Bais";
                    break;
                }
                case 837: {
                    string = "Basilan City";
                    break;
                }
                case 838: {
                    string = "Batangas City";
                    break;
                }
                case 839: {
                    string = "Butuan";
                    break;
                }
                case 840: {
                    string = "Cabanatuan";
                    break;
                }
                case 875: {
                    string = "Cadiz";
                    break;
                }
                case 876: {
                    string = "Cagayan de Oro";
                    break;
                }
                case 877: {
                    string = "Calbayog";
                    break;
                }
                case 878: {
                    string = "Caloocan";
                    break;
                }
                case 879: {
                    string = "Canlaon";
                    break;
                }
                case 880: {
                    string = "Cavite City";
                    break;
                }
                case 881: {
                    string = "Cebu City";
                    break;
                }
                case 882: {
                    string = "Cotabato";
                    break;
                }
                case 883: {
                    string = "Dagupan";
                    break;
                }
                case 918: {
                    string = "Danao";
                    break;
                }
                case 919: {
                    string = "Dapitan";
                    break;
                }
                case 920: {
                    string = "Davao City";
                    break;
                }
                case 921: {
                    string = "Dipolog";
                    break;
                }
                case 922: {
                    string = "Dumaguete";
                    break;
                }
                case 923: {
                    string = "General Santos";
                    break;
                }
                case 924: {
                    string = "Gingoog";
                    break;
                }
                case 925: {
                    string = "Iligan";
                    break;
                }
                case 926: {
                    string = "Iloilo City";
                    break;
                }
                case 961: {
                    string = "Iriga";
                    break;
                }
                case 962: {
                    string = "La Carlota";
                    break;
                }
                case 963: {
                    string = "Laoag";
                    break;
                }
                case 964: {
                    string = "Lapu-Lapu";
                    break;
                }
                case 965: {
                    string = "Legaspi";
                    break;
                }
                case 966: {
                    string = "Lipa";
                    break;
                }
                case 967: {
                    string = "Lucena";
                    break;
                }
                case 968: {
                    string = "Mandaue";
                    break;
                }
                case 969: {
                    string = "Manila";
                    break;
                }
                case 1004: {
                    string = "Marawi";
                    break;
                }
                case 1005: {
                    string = "Naga";
                    break;
                }
                case 1006: {
                    string = "Olongapo";
                    break;
                }
                case 1007: {
                    string = "Ormoc";
                    break;
                }
                case 1008: {
                    string = "Oroquieta";
                    break;
                }
                case 1009: {
                    string = "Ozamis";
                    break;
                }
                case 1010: {
                    string = "Pagadian";
                    break;
                }
                case 1011: {
                    string = "Palayan";
                    break;
                }
                case 1012: {
                    string = "Pasay";
                    break;
                }
                case 1047: {
                    string = "Puerto Princesa";
                    break;
                }
                case 1048: {
                    string = "Quezon City";
                    break;
                }
                case 1049: {
                    string = "Roxas";
                    break;
                }
                case 1050: {
                    string = "San Carlos";
                    break;
                }
                case 1051: {
                    string = "San Carlos";
                    break;
                }
                case 1052: {
                    string = "San Jose";
                    break;
                }
                case 1053: {
                    string = "San Pablo";
                    break;
                }
                case 1054: {
                    string = "Silay";
                    break;
                }
                case 1055: {
                    string = "Surigao";
                    break;
                }
                case 1090: {
                    string = "Tacloban";
                    break;
                }
                case 1091: {
                    string = "Tagaytay";
                    break;
                }
                case 1092: {
                    string = "Tagbilaran";
                    break;
                }
                case 1093: {
                    string = "Tangub";
                    break;
                }
                case 1094: {
                    string = "Toledo";
                    break;
                }
                case 1095: {
                    string = "Trece Martires";
                    break;
                }
                case 1096: {
                    string = "Zamboanga";
                    break;
                }
                case 1097: {
                    string = "Aurora";
                    break;
                }
                case 1134: {
                    string = "Quezon";
                    break;
                }
                case 1135: {
                    string = "Negros Occidental";
                }
            }
        }
        if (string2.equals("PK")) {
            switch (var3_3) {
                case 1: {
                    string = "Federally Administered Tribal Areas";
                    break;
                }
                case 2: {
                    string = "Balochistan";
                    break;
                }
                case 3: {
                    string = "North-West Frontier";
                    break;
                }
                case 4: {
                    string = "Punjab";
                    break;
                }
                case 5: {
                    string = "Sindh";
                    break;
                }
                case 6: {
                    string = "Azad Kashmir";
                    break;
                }
                case 7: {
                    string = "Northern Areas";
                    break;
                }
                case 8: {
                    string = "Islamabad";
                }
            }
        }
        if (string2.equals("PL")) {
            switch (var3_3) {
                case 72: {
                    string = "Dolnoslaskie";
                    break;
                }
                case 73: {
                    string = "Kujawsko-Pomorskie";
                    break;
                }
                case 74: {
                    string = "Lodzkie";
                    break;
                }
                case 75: {
                    string = "Lubelskie";
                    break;
                }
                case 76: {
                    string = "Lubuskie";
                    break;
                }
                case 77: {
                    string = "Malopolskie";
                    break;
                }
                case 78: {
                    string = "Mazowieckie";
                    break;
                }
                case 79: {
                    string = "Opolskie";
                    break;
                }
                case 80: {
                    string = "Podkarpackie";
                    break;
                }
                case 81: {
                    string = "Podlaskie";
                    break;
                }
                case 82: {
                    string = "Pomorskie";
                    break;
                }
                case 83: {
                    string = "Slaskie";
                    break;
                }
                case 84: {
                    string = "Swietokrzyskie";
                    break;
                }
                case 85: {
                    string = "Warminsko-Mazurskie";
                    break;
                }
                case 86: {
                    string = "Wielkopolskie";
                    break;
                }
                case 87: {
                    string = "Zachodniopomorskie";
                }
            }
        }
        if (string2.equals("PS")) {
            switch (var3_3) {
                case 1131: {
                    string = "Gaza";
                    break;
                }
                case 1798: {
                    string = "West Bank";
                }
            }
        }
        if (string2.equals("PT")) {
            switch (var3_3) {
                case 2: {
                    string = "Aveiro";
                    break;
                }
                case 3: {
                    string = "Beja";
                    break;
                }
                case 4: {
                    string = "Braga";
                    break;
                }
                case 5: {
                    string = "Braganca";
                    break;
                }
                case 6: {
                    string = "Castelo Branco";
                    break;
                }
                case 7: {
                    string = "Coimbra";
                    break;
                }
                case 8: {
                    string = "Evora";
                    break;
                }
                case 9: {
                    string = "Faro";
                    break;
                }
                case 10: {
                    string = "Madeira";
                    break;
                }
                case 11: {
                    string = "Guarda";
                    break;
                }
                case 13: {
                    string = "Leiria";
                    break;
                }
                case 14: {
                    string = "Lisboa";
                    break;
                }
                case 16: {
                    string = "Portalegre";
                    break;
                }
                case 17: {
                    string = "Porto";
                    break;
                }
                case 18: {
                    string = "Santarem";
                    break;
                }
                case 19: {
                    string = "Setubal";
                    break;
                }
                case 20: {
                    string = "Viana do Castelo";
                    break;
                }
                case 21: {
                    string = "Vila Real";
                    break;
                }
                case 22: {
                    string = "Viseu";
                    break;
                }
                case 23: {
                    string = "Azores";
                }
            }
        }
        if (string2.equals("PY")) {
            switch (var3_3) {
                case 1: {
                    string = "Alto Parana";
                    break;
                }
                case 2: {
                    string = "Amambay";
                    break;
                }
                case 3: {
                    string = "Boqueron";
                    break;
                }
                case 4: {
                    string = "Caaguazu";
                    break;
                }
                case 5: {
                    string = "Caazapa";
                    break;
                }
                case 6: {
                    string = "Central";
                    break;
                }
                case 7: {
                    string = "Concepcion";
                    break;
                }
                case 8: {
                    string = "Cordillera";
                    break;
                }
                case 10: {
                    string = "Guaira";
                    break;
                }
                case 11: {
                    string = "Itapua";
                    break;
                }
                case 12: {
                    string = "Misiones";
                    break;
                }
                case 13: {
                    string = "Neembucu";
                    break;
                }
                case 15: {
                    string = "Paraguari";
                    break;
                }
                case 16: {
                    string = "Presidente Hayes";
                    break;
                }
                case 17: {
                    string = "San Pedro";
                    break;
                }
                case 19: {
                    string = "Canindeyu";
                    break;
                }
                case 20: {
                    string = "Chaco";
                    break;
                }
                case 21: {
                    string = "Nueva Asuncion";
                    break;
                }
                case 23: {
                    string = "Alto Paraguay";
                }
            }
        }
        if (string2.equals("QA")) {
            switch (var3_3) {
                case 1: {
                    string = "Ad Dawhah";
                    break;
                }
                case 2: {
                    string = "Al Ghuwariyah";
                    break;
                }
                case 3: {
                    string = "Al Jumaliyah";
                    break;
                }
                case 4: {
                    string = "Al Khawr";
                    break;
                }
                case 5: {
                    string = "Al Wakrah Municipality";
                    break;
                }
                case 6: {
                    string = "Ar Rayyan";
                    break;
                }
                case 8: {
                    string = "Madinat ach Shamal";
                    break;
                }
                case 9: {
                    string = "Umm Salal";
                    break;
                }
                case 10: {
                    string = "Al Wakrah";
                    break;
                }
                case 11: {
                    string = "Jariyan al Batnah";
                    break;
                }
                case 12: {
                    string = "Umm Sa'id";
                }
            }
        }
        if (string2.equals("RO")) {
            switch (var3_3) {
                case 1: {
                    string = "Alba";
                    break;
                }
                case 2: {
                    string = "Arad";
                    break;
                }
                case 3: {
                    string = "Arges";
                    break;
                }
                case 4: {
                    string = "Bacau";
                    break;
                }
                case 5: {
                    string = "Bihor";
                    break;
                }
                case 6: {
                    string = "Bistrita-Nasaud";
                    break;
                }
                case 7: {
                    string = "Botosani";
                    break;
                }
                case 8: {
                    string = "Braila";
                    break;
                }
                case 9: {
                    string = "Brasov";
                    break;
                }
                case 10: {
                    string = "Bucuresti";
                    break;
                }
                case 11: {
                    string = "Buzau";
                    break;
                }
                case 12: {
                    string = "Caras-Severin";
                    break;
                }
                case 13: {
                    string = "Cluj";
                    break;
                }
                case 14: {
                    string = "Constanta";
                    break;
                }
                case 15: {
                    string = "Covasna";
                    break;
                }
                case 16: {
                    string = "Dambovita";
                    break;
                }
                case 17: {
                    string = "Dolj";
                    break;
                }
                case 18: {
                    string = "Galati";
                    break;
                }
                case 19: {
                    string = "Gorj";
                    break;
                }
                case 20: {
                    string = "Harghita";
                    break;
                }
                case 21: {
                    string = "Hunedoara";
                    break;
                }
                case 22: {
                    string = "Ialomita";
                    break;
                }
                case 23: {
                    string = "Iasi";
                    break;
                }
                case 25: {
                    string = "Maramures";
                    break;
                }
                case 26: {
                    string = "Mehedinti";
                    break;
                }
                case 27: {
                    string = "Mures";
                    break;
                }
                case 28: {
                    string = "Neamt";
                    break;
                }
                case 29: {
                    string = "Olt";
                    break;
                }
                case 30: {
                    string = "Prahova";
                    break;
                }
                case 31: {
                    string = "Salaj";
                    break;
                }
                case 32: {
                    string = "Satu Mare";
                    break;
                }
                case 33: {
                    string = "Sibiu";
                    break;
                }
                case 34: {
                    string = "Suceava";
                    break;
                }
                case 35: {
                    string = "Teleorman";
                    break;
                }
                case 36: {
                    string = "Timis";
                    break;
                }
                case 37: {
                    string = "Tulcea";
                    break;
                }
                case 38: {
                    string = "Vaslui";
                    break;
                }
                case 39: {
                    string = "Valcea";
                    break;
                }
                case 40: {
                    string = "Vrancea";
                    break;
                }
                case 41: {
                    string = "Calarasi";
                    break;
                }
                case 42: {
                    string = "Giurgiu";
                    break;
                }
                case 43: {
                    string = "Ilfov";
                }
            }
        }
        if (string2.equals("RS")) {
            switch (var3_3) {
                case 1: {
                    string = "Kosovo";
                    break;
                }
                case 2: {
                    string = "Vojvodina";
                }
            }
        }
        if (string2.equals("RU")) {
            switch (var3_3) {
                case 1: {
                    string = "Adygeya";
                    break;
                }
                case 2: {
                    string = "Aginsky Buryatsky AO";
                    break;
                }
                case 3: {
                    string = "Gorno-Altay";
                    break;
                }
                case 4: {
                    string = "Altaisky krai";
                    break;
                }
                case 5: {
                    string = "Amur";
                    break;
                }
                case 6: {
                    string = "Arkhangel'sk";
                    break;
                }
                case 7: {
                    string = "Astrakhan'";
                    break;
                }
                case 8: {
                    string = "Bashkortostan";
                    break;
                }
                case 9: {
                    string = "Belgorod";
                    break;
                }
                case 10: {
                    string = "Bryansk";
                    break;
                }
                case 11: {
                    string = "Buryat";
                    break;
                }
                case 12: {
                    string = "Chechnya";
                    break;
                }
                case 13: {
                    string = "Chelyabinsk";
                    break;
                }
                case 14: {
                    string = "Chita";
                    break;
                }
                case 15: {
                    string = "Chukot";
                    break;
                }
                case 16: {
                    string = "Chuvashia";
                    break;
                }
                case 17: {
                    string = "Dagestan";
                    break;
                }
                case 18: {
                    string = "Evenk";
                    break;
                }
                case 19: {
                    string = "Ingush";
                    break;
                }
                case 20: {
                    string = "Irkutsk";
                    break;
                }
                case 21: {
                    string = "Ivanovo";
                    break;
                }
                case 22: {
                    string = "Kabardin-Balkar";
                    break;
                }
                case 23: {
                    string = "Kaliningrad";
                    break;
                }
                case 24: {
                    string = "Kalmyk";
                    break;
                }
                case 25: {
                    string = "Kaluga";
                    break;
                }
                case 26: {
                    string = "Kamchatka";
                    break;
                }
                case 27: {
                    string = "Karachay-Cherkess";
                    break;
                }
                case 28: {
                    string = "Karelia";
                    break;
                }
                case 29: {
                    string = "Kemerovo";
                    break;
                }
                case 30: {
                    string = "Khabarovsk";
                    break;
                }
                case 31: {
                    string = "Khakass";
                    break;
                }
                case 32: {
                    string = "Khanty-Mansiy";
                    break;
                }
                case 33: {
                    string = "Kirov";
                    break;
                }
                case 34: {
                    string = "Komi";
                    break;
                }
                case 35: {
                    string = "Komi-Permyak";
                    break;
                }
                case 36: {
                    string = "Koryak";
                    break;
                }
                case 37: {
                    string = "Kostroma";
                    break;
                }
                case 38: {
                    string = "Krasnodar";
                    break;
                }
                case 39: {
                    string = "Krasnoyarsk";
                    break;
                }
                case 40: {
                    string = "Kurgan";
                    break;
                }
                case 41: {
                    string = "Kursk";
                    break;
                }
                case 42: {
                    string = "Leningrad";
                    break;
                }
                case 43: {
                    string = "Lipetsk";
                    break;
                }
                case 44: {
                    string = "Magadan";
                    break;
                }
                case 45: {
                    string = "Mariy-El";
                    break;
                }
                case 46: {
                    string = "Mordovia";
                    break;
                }
                case 47: {
                    string = "Moskva";
                    break;
                }
                case 48: {
                    string = "Moscow City";
                    break;
                }
                case 49: {
                    string = "Murmansk";
                    break;
                }
                case 50: {
                    string = "Nenets";
                    break;
                }
                case 51: {
                    string = "Nizhegorod";
                    break;
                }
                case 52: {
                    string = "Novgorod";
                    break;
                }
                case 53: {
                    string = "Novosibirsk";
                    break;
                }
                case 54: {
                    string = "Omsk";
                    break;
                }
                case 55: {
                    string = "Orenburg";
                    break;
                }
                case 56: {
                    string = "Orel";
                    break;
                }
                case 57: {
                    string = "Penza";
                    break;
                }
                case 58: {
                    string = "Perm'";
                    break;
                }
                case 59: {
                    string = "Primor'ye";
                    break;
                }
                case 60: {
                    string = "Pskov";
                    break;
                }
                case 61: {
                    string = "Rostov";
                    break;
                }
                case 62: {
                    string = "Ryazan'";
                    break;
                }
                case 63: {
                    string = "Sakha";
                    break;
                }
                case 64: {
                    string = "Sakhalin";
                    break;
                }
                case 65: {
                    string = "Samara";
                    break;
                }
                case 66: {
                    string = "Saint Petersburg City";
                    break;
                }
                case 67: {
                    string = "Saratov";
                    break;
                }
                case 68: {
                    string = "North Ossetia";
                    break;
                }
                case 69: {
                    string = "Smolensk";
                    break;
                }
                case 70: {
                    string = "Stavropol'";
                    break;
                }
                case 71: {
                    string = "Sverdlovsk";
                    break;
                }
                case 72: {
                    string = "Tambovskaya oblast";
                    break;
                }
                case 73: {
                    string = "Tatarstan";
                    break;
                }
                case 74: {
                    string = "Taymyr";
                    break;
                }
                case 75: {
                    string = "Tomsk";
                    break;
                }
                case 76: {
                    string = "Tula";
                    break;
                }
                case 77: {
                    string = "Tver'";
                    break;
                }
                case 78: {
                    string = "Tyumen'";
                    break;
                }
                case 79: {
                    string = "Tuva";
                    break;
                }
                case 80: {
                    string = "Udmurt";
                    break;
                }
                case 81: {
                    string = "Ul'yanovsk";
                    break;
                }
                case 82: {
                    string = "Ust-Orda Buryat";
                    break;
                }
                case 83: {
                    string = "Vladimir";
                    break;
                }
                case 84: {
                    string = "Volgograd";
                    break;
                }
                case 85: {
                    string = "Vologda";
                    break;
                }
                case 86: {
                    string = "Voronezh";
                    break;
                }
                case 87: {
                    string = "Yamal-Nenets";
                    break;
                }
                case 88: {
                    string = "Yaroslavl'";
                    break;
                }
                case 89: {
                    string = "Yevrey";
                    break;
                }
                case 90: {
                    string = "Permskiy Kray";
                    break;
                }
                case 91: {
                    string = "Krasnoyarskiy Kray";
                    break;
                }
                case 942: {
                    string = "Chechnya Republic";
                }
            }
        }
        if (string2.equals("RW")) {
            switch (var3_3) {
                case 1: {
                    string = "Butare";
                    break;
                }
                case 6: {
                    string = "Gitarama";
                    break;
                }
                case 7: {
                    string = "Kibungo";
                    break;
                }
                case 9: {
                    string = "Kigali";
                    break;
                }
                case 11: {
                    string = "Est";
                    break;
                }
                case 12: {
                    string = "Kigali";
                    break;
                }
                case 13: {
                    string = "Nord";
                    break;
                }
                case 14: {
                    string = "Ouest";
                    break;
                }
                case 15: {
                    string = "Sud";
                }
            }
        }
        if (string2.equals("SA")) {
            switch (var3_3) {
                case 2: {
                    string = "Al Bahah";
                    break;
                }
                case 3: {
                    string = "Al Jawf";
                    break;
                }
                case 5: {
                    string = "Al Madinah";
                    break;
                }
                case 6: {
                    string = "Ash Sharqiyah";
                    break;
                }
                case 8: {
                    string = "Al Qasim";
                    break;
                }
                case 9: {
                    string = "Al Qurayyat";
                    break;
                }
                case 10: {
                    string = "Ar Riyad";
                    break;
                }
                case 13: {
                    string = "Ha'il";
                    break;
                }
                case 14: {
                    string = "Makkah";
                    break;
                }
                case 15: {
                    string = "Al Hudud ash Shamaliyah";
                    break;
                }
                case 16: {
                    string = "Najran";
                    break;
                }
                case 17: {
                    string = "Jizan";
                    break;
                }
                case 19: {
                    string = "Tabuk";
                    break;
                }
                case 20: {
                    string = "Al Jawf";
                }
            }
        }
        if (string2.equals("SB")) {
            switch (var3_3) {
                case 3: {
                    string = "Malaita";
                    break;
                }
                case 6: {
                    string = "Guadalcanal";
                    break;
                }
                case 7: {
                    string = "Isabel";
                    break;
                }
                case 8: {
                    string = "Makira";
                    break;
                }
                case 9: {
                    string = "Temotu";
                    break;
                }
                case 10: {
                    string = "Central";
                    break;
                }
                case 11: {
                    string = "Western";
                    break;
                }
                case 12: {
                    string = "Choiseul";
                    break;
                }
                case 13: {
                    string = "Rennell and Bellona";
                }
            }
        }
        if (string2.equals("SC")) {
            switch (var3_3) {
                case 1: {
                    string = "Anse aux Pins";
                    break;
                }
                case 2: {
                    string = "Anse Boileau";
                    break;
                }
                case 3: {
                    string = "Anse Etoile";
                    break;
                }
                case 4: {
                    string = "Anse Louis";
                    break;
                }
                case 5: {
                    string = "Anse Royale";
                    break;
                }
                case 6: {
                    string = "Baie Lazare";
                    break;
                }
                case 7: {
                    string = "Baie Sainte Anne";
                    break;
                }
                case 8: {
                    string = "Beau Vallon";
                    break;
                }
                case 9: {
                    string = "Bel Air";
                    break;
                }
                case 10: {
                    string = "Bel Ombre";
                    break;
                }
                case 11: {
                    string = "Cascade";
                    break;
                }
                case 12: {
                    string = "Glacis";
                    break;
                }
                case 13: {
                    string = "Grand' Anse";
                    break;
                }
                case 14: {
                    string = "Grand' Anse";
                    break;
                }
                case 15: {
                    string = "La Digue";
                    break;
                }
                case 16: {
                    string = "La Riviere Anglaise";
                    break;
                }
                case 17: {
                    string = "Mont Buxton";
                    break;
                }
                case 18: {
                    string = "Mont Fleuri";
                    break;
                }
                case 19: {
                    string = "Plaisance";
                    break;
                }
                case 20: {
                    string = "Pointe La Rue";
                    break;
                }
                case 21: {
                    string = "Port Glaud";
                    break;
                }
                case 22: {
                    string = "Saint Louis";
                    break;
                }
                case 23: {
                    string = "Takamaka";
                }
            }
        }
        if (string2.equals("SD")) {
            switch (var3_3) {
                case 27: {
                    string = "Al Wusta";
                    break;
                }
                case 28: {
                    string = "Al Istiwa'iyah";
                    break;
                }
                case 29: {
                    string = "Al Khartum";
                    break;
                }
                case 30: {
                    string = "Ash Shamaliyah";
                    break;
                }
                case 31: {
                    string = "Ash Sharqiyah";
                    break;
                }
                case 32: {
                    string = "Bahr al Ghazal";
                    break;
                }
                case 33: {
                    string = "Darfur";
                    break;
                }
                case 34: {
                    string = "Kurdufan";
                    break;
                }
                case 35: {
                    string = "Upper Nile";
                    break;
                }
                case 40: {
                    string = "Al Wahadah State";
                    break;
                }
                case 44: {
                    string = "Central Equatoria State";
                }
            }
        }
        if (string2.equals("SE")) {
            switch (var3_3) {
                case 1: {
                    string = "Alvsborgs Lan";
                    break;
                }
                case 2: {
                    string = "Blekinge Lan";
                    break;
                }
                case 3: {
                    string = "Gavleborgs Lan";
                    break;
                }
                case 4: {
                    string = "Goteborgs och Bohus Lan";
                    break;
                }
                case 5: {
                    string = "Gotlands Lan";
                    break;
                }
                case 6: {
                    string = "Hallands Lan";
                    break;
                }
                case 7: {
                    string = "Jamtlands Lan";
                    break;
                }
                case 8: {
                    string = "Jonkopings Lan";
                    break;
                }
                case 9: {
                    string = "Kalmar Lan";
                    break;
                }
                case 10: {
                    string = "Dalarnas Lan";
                    break;
                }
                case 11: {
                    string = "Kristianstads Lan";
                    break;
                }
                case 12: {
                    string = "Kronobergs Lan";
                    break;
                }
                case 13: {
                    string = "Malmohus Lan";
                    break;
                }
                case 14: {
                    string = "Norrbottens Lan";
                    break;
                }
                case 15: {
                    string = "Orebro Lan";
                    break;
                }
                case 16: {
                    string = "Ostergotlands Lan";
                    break;
                }
                case 17: {
                    string = "Skaraborgs Lan";
                    break;
                }
                case 18: {
                    string = "Sodermanlands Lan";
                    break;
                }
                case 21: {
                    string = "Uppsala Lan";
                    break;
                }
                case 22: {
                    string = "Varmlands Lan";
                    break;
                }
                case 23: {
                    string = "Vasterbottens Lan";
                    break;
                }
                case 24: {
                    string = "Vasternorrlands Lan";
                    break;
                }
                case 25: {
                    string = "Vastmanlands Lan";
                    break;
                }
                case 26: {
                    string = "Stockholms Lan";
                    break;
                }
                case 27: {
                    string = "Skane Lan";
                    break;
                }
                case 28: {
                    string = "Vastra Gotaland";
                }
            }
        }
        if (string2.equals("SH")) {
            switch (var3_3) {
                case 1: {
                    string = "Ascension";
                    break;
                }
                case 2: {
                    string = "Saint Helena";
                    break;
                }
                case 3: {
                    string = "Tristan da Cunha";
                }
            }
        }
        if (string2.equals("SI")) {
            switch (var3_3) {
                case 1: {
                    string = "Ajdovscina";
                    break;
                }
                case 2: {
                    string = "Beltinci";
                    break;
                }
                case 3: {
                    string = "Bled";
                    break;
                }
                case 4: {
                    string = "Bohinj";
                    break;
                }
                case 5: {
                    string = "Borovnica";
                    break;
                }
                case 6: {
                    string = "Bovec";
                    break;
                }
                case 7: {
                    string = "Brda";
                    break;
                }
                case 8: {
                    string = "Brezice";
                    break;
                }
                case 9: {
                    string = "Brezovica";
                    break;
                }
                case 11: {
                    string = "Celje";
                    break;
                }
                case 12: {
                    string = "Cerklje na Gorenjskem";
                    break;
                }
                case 13: {
                    string = "Cerknica";
                    break;
                }
                case 14: {
                    string = "Cerkno";
                    break;
                }
                case 15: {
                    string = "Crensovci";
                    break;
                }
                case 16: {
                    string = "Crna na Koroskem";
                    break;
                }
                case 17: {
                    string = "Crnomelj";
                    break;
                }
                case 19: {
                    string = "Divaca";
                    break;
                }
                case 20: {
                    string = "Dobrepolje";
                    break;
                }
                case 22: {
                    string = "Dol pri Ljubljani";
                    break;
                }
                case 24: {
                    string = "Dornava";
                    break;
                }
                case 25: {
                    string = "Dravograd";
                    break;
                }
                case 26: {
                    string = "Duplek";
                    break;
                }
                case 27: {
                    string = "Gorenja Vas-Poljane";
                    break;
                }
                case 28: {
                    string = "Gorisnica";
                    break;
                }
                case 29: {
                    string = "Gornja Radgona";
                    break;
                }
                case 30: {
                    string = "Gornji Grad";
                    break;
                }
                case 31: {
                    string = "Gornji Petrovci";
                    break;
                }
                case 32: {
                    string = "Grosuplje";
                    break;
                }
                case 34: {
                    string = "Hrastnik";
                    break;
                }
                case 35: {
                    string = "Hrpelje-Kozina";
                    break;
                }
                case 36: {
                    string = "Idrija";
                    break;
                }
                case 37: {
                    string = "Ig";
                    break;
                }
                case 38: {
                    string = "Ilirska Bistrica";
                    break;
                }
                case 39: {
                    string = "Ivancna Gorica";
                    break;
                }
                case 40: {
                    string = "Izola-Isola";
                    break;
                }
                case 42: {
                    string = "Jursinci";
                    break;
                }
                case 44: {
                    string = "Kanal";
                    break;
                }
                case 45: {
                    string = "Kidricevo";
                    break;
                }
                case 46: {
                    string = "Kobarid";
                    break;
                }
                case 47: {
                    string = "Kobilje";
                    break;
                }
                case 49: {
                    string = "Komen";
                    break;
                }
                case 50: {
                    string = "Koper-Capodistria";
                    break;
                }
                case 51: {
                    string = "Kozje";
                    break;
                }
                case 52: {
                    string = "Kranj";
                    break;
                }
                case 53: {
                    string = "Kranjska Gora";
                    break;
                }
                case 54: {
                    string = "Krsko";
                    break;
                }
                case 55: {
                    string = "Kungota";
                    break;
                }
                case 57: {
                    string = "Lasko";
                    break;
                }
                case 61: {
                    string = "Ljubljana";
                    break;
                }
                case 62: {
                    string = "Ljubno";
                    break;
                }
                case 64: {
                    string = "Logatec";
                    break;
                }
                case 66: {
                    string = "Loski Potok";
                    break;
                }
                case 68: {
                    string = "Lukovica";
                    break;
                }
                case 71: {
                    string = "Medvode";
                    break;
                }
                case 72: {
                    string = "Menges";
                    break;
                }
                case 73: {
                    string = "Metlika";
                    break;
                }
                case 74: {
                    string = "Mezica";
                    break;
                }
                case 76: {
                    string = "Mislinja";
                    break;
                }
                case 77: {
                    string = "Moravce";
                    break;
                }
                case 78: {
                    string = "Moravske Toplice";
                    break;
                }
                case 79: {
                    string = "Mozirje";
                    break;
                }
                case 80: {
                    string = "Murska Sobota";
                    break;
                }
                case 81: {
                    string = "Muta";
                    break;
                }
                case 82: {
                    string = "Naklo";
                    break;
                }
                case 83: {
                    string = "Nazarje";
                    break;
                }
                case 84: {
                    string = "Nova Gorica";
                    break;
                }
                case 86: {
                    string = "Odranci";
                    break;
                }
                case 87: {
                    string = "Ormoz";
                    break;
                }
                case 88: {
                    string = "Osilnica";
                    break;
                }
                case 89: {
                    string = "Pesnica";
                    break;
                }
                case 91: {
                    string = "Pivka";
                    break;
                }
                case 92: {
                    string = "Podcetrtek";
                    break;
                }
                case 94: {
                    string = "Postojna";
                    break;
                }
                case 97: {
                    string = "Puconci";
                    break;
                }
                case 98: {
                    string = "Racam";
                    break;
                }
                case 99: {
                    string = "Radece";
                    break;
                }
                case 832: {
                    string = "Radenci";
                    break;
                }
                case 833: {
                    string = "Radlje ob Dravi";
                    break;
                }
                case 834: {
                    string = "Radovljica";
                    break;
                }
                case 837: {
                    string = "Rogasovci";
                    break;
                }
                case 838: {
                    string = "Rogaska Slatina";
                    break;
                }
                case 839: {
                    string = "Rogatec";
                    break;
                }
                case 875: {
                    string = "Semic";
                    break;
                }
                case 876: {
                    string = "Sencur";
                    break;
                }
                case 877: {
                    string = "Sentilj";
                    break;
                }
                case 878: {
                    string = "Sentjernej";
                    break;
                }
                case 880: {
                    string = "Sevnica";
                    break;
                }
                case 881: {
                    string = "Sezana";
                    break;
                }
                case 882: {
                    string = "Skocjan";
                    break;
                }
                case 883: {
                    string = "Skofja Loka";
                    break;
                }
                case 918: {
                    string = "Skofljica";
                    break;
                }
                case 919: {
                    string = "Slovenj Gradec";
                    break;
                }
                case 921: {
                    string = "Slovenske Konjice";
                    break;
                }
                case 922: {
                    string = "Smarje pri Jelsah";
                    break;
                }
                case 923: {
                    string = "Smartno ob Paki";
                    break;
                }
                case 924: {
                    string = "Sostanj";
                    break;
                }
                case 925: {
                    string = "Starse";
                    break;
                }
                case 926: {
                    string = "Store";
                    break;
                }
                case 961: {
                    string = "Sveti Jurij";
                    break;
                }
                case 962: {
                    string = "Tolmin";
                    break;
                }
                case 963: {
                    string = "Trbovlje";
                    break;
                }
                case 964: {
                    string = "Trebnje";
                    break;
                }
                case 965: {
                    string = "Trzic";
                    break;
                }
                case 966: {
                    string = "Turnisce";
                    break;
                }
                case 967: {
                    string = "Velenje";
                    break;
                }
                case 968: {
                    string = "Velike Lasce";
                    break;
                }
                case 1004: {
                    string = "Vipava";
                    break;
                }
                case 1005: {
                    string = "Vitanje";
                    break;
                }
                case 1006: {
                    string = "Vodice";
                    break;
                }
                case 1008: {
                    string = "Vrhnika";
                    break;
                }
                case 1009: {
                    string = "Vuzenica";
                    break;
                }
                case 1010: {
                    string = "Zagorje ob Savi";
                    break;
                }
                case 1012: {
                    string = "Zavrc";
                    break;
                }
                case 1047: {
                    string = "Zelezniki";
                    break;
                }
                case 1048: {
                    string = "Ziri";
                    break;
                }
                case 1049: {
                    string = "Zrece";
                    break;
                }
                case 1093: {
                    string = "Dobrova-Horjul-Polhov Gradec";
                    break;
                }
                case 1096: {
                    string = "Domzale";
                    break;
                }
                case 1136: {
                    string = "Jesenice";
                    break;
                }
                case 1138: {
                    string = "Kamnik";
                    break;
                }
                case 1139: {
                    string = "Kocevje";
                    break;
                }
                case 1177: {
                    string = "Kuzma";
                    break;
                }
                case 1178: {
                    string = "Lenart";
                    break;
                }
                case 1180: {
                    string = "Litija";
                    break;
                }
                case 1181: {
                    string = "Ljutomer";
                    break;
                }
                case 1182: {
                    string = "Loska Dolina";
                    break;
                }
                case 1184: {
                    string = "Luce";
                    break;
                }
                case 1219: {
                    string = "Majsperk";
                    break;
                }
                case 1220: {
                    string = "Maribor";
                    break;
                }
                case 1223: {
                    string = "Miren-Kostanjevica";
                    break;
                }
                case 1225: {
                    string = "Novo Mesto";
                    break;
                }
                case 1227: {
                    string = "Piran";
                    break;
                }
                case 1266: {
                    string = "Preddvor";
                    break;
                }
                case 1268: {
                    string = "Ptuj";
                    break;
                }
                case 1305: {
                    string = "Ribnica";
                    break;
                }
                case 1307: {
                    string = "Ruse";
                    break;
                }
                case 1311: {
                    string = "Sentjur pri Celju";
                    break;
                }
                case 1312: {
                    string = "Slovenska Bistrica";
                    break;
                }
                case 1392: {
                    string = "Videm";
                    break;
                }
                case 1393: {
                    string = "Vojnik";
                    break;
                }
                case 1395: {
                    string = "Zalec";
                }
            }
        }
        if (string2.equals("SK")) {
            switch (var3_3) {
                case 1: {
                    string = "Banska Bystrica";
                    break;
                }
                case 2: {
                    string = "Bratislava";
                    break;
                }
                case 3: {
                    string = "Kosice";
                    break;
                }
                case 4: {
                    string = "Nitra";
                    break;
                }
                case 5: {
                    string = "Presov";
                    break;
                }
                case 6: {
                    string = "Trencin";
                    break;
                }
                case 7: {
                    string = "Trnava";
                    break;
                }
                case 8: {
                    string = "Zilina";
                }
            }
        }
        if (string2.equals("SL")) {
            switch (var3_3) {
                case 1: {
                    string = "Eastern";
                    break;
                }
                case 2: {
                    string = "Northern";
                    break;
                }
                case 3: {
                    string = "Southern";
                    break;
                }
                case 4: {
                    string = "Western Area";
                }
            }
        }
        if (string2.equals("SM")) {
            switch (var3_3) {
                case 1: {
                    string = "Acquaviva";
                    break;
                }
                case 2: {
                    string = "Chiesanuova";
                    break;
                }
                case 3: {
                    string = "Domagnano";
                    break;
                }
                case 4: {
                    string = "Faetano";
                    break;
                }
                case 5: {
                    string = "Fiorentino";
                    break;
                }
                case 6: {
                    string = "Borgo Maggiore";
                    break;
                }
                case 7: {
                    string = "San Marino";
                    break;
                }
                case 8: {
                    string = "Monte Giardino";
                    break;
                }
                case 9: {
                    string = "Serravalle";
                }
            }
        }
        if (string2.equals("SN")) {
            switch (var3_3) {
                case 1: {
                    string = "Dakar";
                    break;
                }
                case 3: {
                    string = "Diourbel";
                    break;
                }
                case 4: {
                    string = "Saint-Louis";
                    break;
                }
                case 5: {
                    string = "Tambacounda";
                    break;
                }
                case 7: {
                    string = "Thies";
                    break;
                }
                case 8: {
                    string = "Louga";
                    break;
                }
                case 9: {
                    string = "Fatick";
                    break;
                }
                case 10: {
                    string = "Kaolack";
                    break;
                }
                case 11: {
                    string = "Kolda";
                    break;
                }
                case 12: {
                    string = "Ziguinchor";
                    break;
                }
                case 13: {
                    string = "Louga";
                    break;
                }
                case 14: {
                    string = "Saint-Louis";
                    break;
                }
                case 15: {
                    string = "Matam";
                }
            }
        }
        if (string2.equals("SO")) {
            switch (var3_3) {
                case 1: {
                    string = "Bakool";
                    break;
                }
                case 2: {
                    string = "Banaadir";
                    break;
                }
                case 3: {
                    string = "Bari";
                    break;
                }
                case 4: {
                    string = "Bay";
                    break;
                }
                case 5: {
                    string = "Galguduud";
                    break;
                }
                case 6: {
                    string = "Gedo";
                    break;
                }
                case 7: {
                    string = "Hiiraan";
                    break;
                }
                case 8: {
                    string = "Jubbada Dhexe";
                    break;
                }
                case 9: {
                    string = "Jubbada Hoose";
                    break;
                }
                case 10: {
                    string = "Mudug";
                    break;
                }
                case 11: {
                    string = "Nugaal";
                    break;
                }
                case 12: {
                    string = "Sanaag";
                    break;
                }
                case 13: {
                    string = "Shabeellaha Dhexe";
                    break;
                }
                case 14: {
                    string = "Shabeellaha Hoose";
                    break;
                }
                case 16: {
                    string = "Woqooyi Galbeed";
                    break;
                }
                case 18: {
                    string = "Nugaal";
                    break;
                }
                case 19: {
                    string = "Togdheer";
                    break;
                }
                case 20: {
                    string = "Woqooyi Galbeed";
                    break;
                }
                case 21: {
                    string = "Awdal";
                    break;
                }
                case 22: {
                    string = "Sool";
                }
            }
        }
        if (string2.equals("SR")) {
            switch (var3_3) {
                case 10: {
                    string = "Brokopondo";
                    break;
                }
                case 11: {
                    string = "Commewijne";
                    break;
                }
                case 12: {
                    string = "Coronie";
                    break;
                }
                case 13: {
                    string = "Marowijne";
                    break;
                }
                case 14: {
                    string = "Nickerie";
                    break;
                }
                case 15: {
                    string = "Para";
                    break;
                }
                case 16: {
                    string = "Paramaribo";
                    break;
                }
                case 17: {
                    string = "Saramacca";
                    break;
                }
                case 18: {
                    string = "Sipaliwini";
                    break;
                }
                case 19: {
                    string = "Wanica";
                }
            }
        }
        if (string2.equals("ST")) {
            switch (var3_3) {
                case 1: {
                    string = "Principe";
                    break;
                }
                case 2: {
                    string = "Sao Tome";
                }
            }
        }
        if (string2.equals("SV")) {
            switch (var3_3) {
                case 1: {
                    string = "Ahuachapan";
                    break;
                }
                case 2: {
                    string = "Cabanas";
                    break;
                }
                case 3: {
                    string = "Chalatenango";
                    break;
                }
                case 4: {
                    string = "Cuscatlan";
                    break;
                }
                case 5: {
                    string = "La Libertad";
                    break;
                }
                case 6: {
                    string = "La Paz";
                    break;
                }
                case 7: {
                    string = "La Union";
                    break;
                }
                case 8: {
                    string = "Morazan";
                    break;
                }
                case 9: {
                    string = "San Miguel";
                    break;
                }
                case 10: {
                    string = "San Salvador";
                    break;
                }
                case 11: {
                    string = "Santa Ana";
                    break;
                }
                case 12: {
                    string = "San Vicente";
                    break;
                }
                case 13: {
                    string = "Sonsonate";
                    break;
                }
                case 14: {
                    string = "Usulutan";
                }
            }
        }
        if (string2.equals("SY")) {
            switch (var3_3) {
                case 1: {
                    string = "Al Hasakah";
                    break;
                }
                case 2: {
                    string = "Al Ladhiqiyah";
                    break;
                }
                case 3: {
                    string = "Al Qunaytirah";
                    break;
                }
                case 4: {
                    string = "Ar Raqqah";
                    break;
                }
                case 5: {
                    string = "As Suwayda'";
                    break;
                }
                case 6: {
                    string = "Dar";
                    break;
                }
                case 7: {
                    string = "Dayr az Zawr";
                    break;
                }
                case 8: {
                    string = "Rif Dimashq";
                    break;
                }
                case 9: {
                    string = "Halab";
                    break;
                }
                case 10: {
                    string = "Hamah";
                    break;
                }
                case 11: {
                    string = "Hims";
                    break;
                }
                case 12: {
                    string = "Idlib";
                    break;
                }
                case 13: {
                    string = "Dimashq";
                    break;
                }
                case 14: {
                    string = "Tartus";
                }
            }
        }
        if (string2.equals("SZ")) {
            switch (var3_3) {
                case 1: {
                    string = "Hhohho";
                    break;
                }
                case 2: {
                    string = "Lubombo";
                    break;
                }
                case 3: {
                    string = "Manzini";
                    break;
                }
                case 4: {
                    string = "Shiselweni";
                    break;
                }
                case 5: {
                    string = "Praslin";
                }
            }
        }
        if (string2.equals("TD")) {
            switch (var3_3) {
                case 1: {
                    string = "Batha";
                    break;
                }
                case 2: {
                    string = "Biltine";
                    break;
                }
                case 3: {
                    string = "Borkou-Ennedi-Tibesti";
                    break;
                }
                case 4: {
                    string = "Chari-Baguirmi";
                    break;
                }
                case 5: {
                    string = "Guera";
                    break;
                }
                case 6: {
                    string = "Kanem";
                    break;
                }
                case 7: {
                    string = "Lac";
                    break;
                }
                case 8: {
                    string = "Logone Occidental";
                    break;
                }
                case 9: {
                    string = "Logone Oriental";
                    break;
                }
                case 10: {
                    string = "Mayo-Kebbi";
                    break;
                }
                case 11: {
                    string = "Moyen-Chari";
                    break;
                }
                case 12: {
                    string = "Ouaddai";
                    break;
                }
                case 13: {
                    string = "Salamat";
                    break;
                }
                case 14: {
                    string = "Tandjile";
                }
            }
        }
        if (string2.equals("TG")) {
            switch (var3_3) {
                case 9: {
                    string = "Lama-Kara";
                    break;
                }
                case 18: {
                    string = "Tsevie";
                    break;
                }
                case 22: {
                    string = "Centrale";
                    break;
                }
                case 23: {
                    string = "Kara";
                    break;
                }
                case 24: {
                    string = "Maritime";
                    break;
                }
                case 25: {
                    string = "Plateaux";
                    break;
                }
                case 26: {
                    string = "Savanes";
                }
            }
        }
        if (string2.equals("TH")) {
            switch (var3_3) {
                case 1: {
                    string = "Mae Hong Son";
                    break;
                }
                case 2: {
                    string = "Chiang Mai";
                    break;
                }
                case 3: {
                    string = "Chiang Rai";
                    break;
                }
                case 4: {
                    string = "Nan";
                    break;
                }
                case 5: {
                    string = "Lamphun";
                    break;
                }
                case 6: {
                    string = "Lampang";
                    break;
                }
                case 7: {
                    string = "Phrae";
                    break;
                }
                case 8: {
                    string = "Tak";
                    break;
                }
                case 9: {
                    string = "Sukhothai";
                    break;
                }
                case 10: {
                    string = "Uttaradit";
                    break;
                }
                case 11: {
                    string = "Kamphaeng Phet";
                    break;
                }
                case 12: {
                    string = "Phitsanulok";
                    break;
                }
                case 13: {
                    string = "Phichit";
                    break;
                }
                case 14: {
                    string = "Phetchabun";
                    break;
                }
                case 15: {
                    string = "Uthai Thani";
                    break;
                }
                case 16: {
                    string = "Nakhon Sawan";
                    break;
                }
                case 17: {
                    string = "Nong Khai";
                    break;
                }
                case 18: {
                    string = "Loei";
                    break;
                }
                case 20: {
                    string = "Sakon Nakhon";
                    break;
                }
                case 21: {
                    string = "Nakhon Phanom";
                    break;
                }
                case 22: {
                    string = "Khon Kaen";
                    break;
                }
                case 23: {
                    string = "Kalasin";
                    break;
                }
                case 24: {
                    string = "Maha Sarakham";
                    break;
                }
                case 25: {
                    string = "Roi Et";
                    break;
                }
                case 26: {
                    string = "Chaiyaphum";
                    break;
                }
                case 27: {
                    string = "Nakhon Ratchasima";
                    break;
                }
                case 28: {
                    string = "Buriram";
                    break;
                }
                case 29: {
                    string = "Surin";
                    break;
                }
                case 30: {
                    string = "Sisaket";
                    break;
                }
                case 31: {
                    string = "Narathiwat";
                    break;
                }
                case 32: {
                    string = "Chai Nat";
                    break;
                }
                case 33: {
                    string = "Sing Buri";
                    break;
                }
                case 34: {
                    string = "Lop Buri";
                    break;
                }
                case 35: {
                    string = "Ang Thong";
                    break;
                }
                case 36: {
                    string = "Phra Nakhon Si Ayutthaya";
                    break;
                }
                case 37: {
                    string = "Saraburi";
                    break;
                }
                case 38: {
                    string = "Nonthaburi";
                    break;
                }
                case 39: {
                    string = "Pathum Thani";
                    break;
                }
                case 40: {
                    string = "Krung Thep";
                    break;
                }
                case 41: {
                    string = "Phayao";
                    break;
                }
                case 42: {
                    string = "Samut Prakan";
                    break;
                }
                case 43: {
                    string = "Nakhon Nayok";
                    break;
                }
                case 44: {
                    string = "Chachoengsao";
                    break;
                }
                case 45: {
                    string = "Prachin Buri";
                    break;
                }
                case 46: {
                    string = "Chon Buri";
                    break;
                }
                case 47: {
                    string = "Rayong";
                    break;
                }
                case 48: {
                    string = "Chanthaburi";
                    break;
                }
                case 49: {
                    string = "Trat";
                    break;
                }
                case 50: {
                    string = "Kanchanaburi";
                    break;
                }
                case 51: {
                    string = "Suphan Buri";
                    break;
                }
                case 52: {
                    string = "Ratchaburi";
                    break;
                }
                case 53: {
                    string = "Nakhon Pathom";
                    break;
                }
                case 54: {
                    string = "Samut Songkhram";
                    break;
                }
                case 55: {
                    string = "Samut Sakhon";
                    break;
                }
                case 56: {
                    string = "Phetchaburi";
                    break;
                }
                case 57: {
                    string = "Prachuap Khiri Khan";
                    break;
                }
                case 58: {
                    string = "Chumphon";
                    break;
                }
                case 59: {
                    string = "Ranong";
                    break;
                }
                case 60: {
                    string = "Surat Thani";
                    break;
                }
                case 61: {
                    string = "Phangnga";
                    break;
                }
                case 62: {
                    string = "Phuket";
                    break;
                }
                case 63: {
                    string = "Krabi";
                    break;
                }
                case 64: {
                    string = "Nakhon Si Thammarat";
                    break;
                }
                case 65: {
                    string = "Trang";
                    break;
                }
                case 66: {
                    string = "Phatthalung";
                    break;
                }
                case 67: {
                    string = "Satun";
                    break;
                }
                case 68: {
                    string = "Songkhla";
                    break;
                }
                case 69: {
                    string = "Pattani";
                    break;
                }
                case 70: {
                    string = "Yala";
                    break;
                }
                case 71: {
                    string = "Ubon Ratchathani";
                    break;
                }
                case 72: {
                    string = "Yasothon";
                    break;
                }
                case 73: {
                    string = "Nakhon Phanom";
                    break;
                }
                case 75: {
                    string = "Ubon Ratchathani";
                    break;
                }
                case 76: {
                    string = "Udon Thani";
                    break;
                }
                case 77: {
                    string = "Amnat Charoen";
                    break;
                }
                case 78: {
                    string = "Mukdahan";
                    break;
                }
                case 79: {
                    string = "Nong Bua Lamphu";
                    break;
                }
                case 80: {
                    string = "Sa Kaeo";
                }
            }
        }
        if (string2.equals("TJ")) {
            switch (var3_3) {
                case 1: {
                    string = "Kuhistoni Badakhshon";
                    break;
                }
                case 2: {
                    string = "Khatlon";
                    break;
                }
                case 3: {
                    string = "Sughd";
                }
            }
        }
        if (string2.equals("TM")) {
            switch (var3_3) {
                case 1: {
                    string = "Ahal";
                    break;
                }
                case 2: {
                    string = "Balkan";
                    break;
                }
                case 3: {
                    string = "Dashoguz";
                    break;
                }
                case 4: {
                    string = "Lebap";
                    break;
                }
                case 5: {
                    string = "Mary";
                }
            }
        }
        if (string2.equals("TN")) {
            switch (var3_3) {
                case 2: {
                    string = "Kasserine";
                    break;
                }
                case 3: {
                    string = "Kairouan";
                    break;
                }
                case 6: {
                    string = "Jendouba";
                    break;
                }
                case 14: {
                    string = "El Kef";
                    break;
                }
                case 15: {
                    string = "Al Mahdia";
                    break;
                }
                case 16: {
                    string = "Al Munastir";
                    break;
                }
                case 17: {
                    string = "Bajah";
                    break;
                }
                case 18: {
                    string = "Bizerte";
                    break;
                }
                case 19: {
                    string = "Nabeul";
                    break;
                }
                case 22: {
                    string = "Siliana";
                    break;
                }
                case 23: {
                    string = "Sousse";
                    break;
                }
                case 26: {
                    string = "Ariana";
                    break;
                }
                case 27: {
                    string = "Ben Arous";
                    break;
                }
                case 28: {
                    string = "Madanin";
                    break;
                }
                case 29: {
                    string = "Gabes";
                    break;
                }
                case 30: {
                    string = "Gafsa";
                    break;
                }
                case 31: {
                    string = "Kebili";
                    break;
                }
                case 32: {
                    string = "Sfax";
                    break;
                }
                case 33: {
                    string = "Sidi Bou Zid";
                    break;
                }
                case 34: {
                    string = "Tataouine";
                    break;
                }
                case 35: {
                    string = "Tozeur";
                    break;
                }
                case 36: {
                    string = "Tunis";
                    break;
                }
                case 37: {
                    string = "Zaghouan";
                    break;
                }
                case 38: {
                    string = "Aiana";
                    break;
                }
                case 39: {
                    string = "Manouba";
                }
            }
        }
        if (string2.equals("TO")) {
            switch (var3_3) {
                case 1: {
                    string = "Ha";
                    break;
                }
                case 2: {
                    string = "Tongatapu";
                    break;
                }
                case 3: {
                    string = "Vava";
                }
            }
        }
        if (string2.equals("TR")) {
            switch (var3_3) {
                case 2: {
                    string = "Adiyaman";
                    break;
                }
                case 3: {
                    string = "Afyonkarahisar";
                    break;
                }
                case 4: {
                    string = "Agri";
                    break;
                }
                case 5: {
                    string = "Amasya";
                    break;
                }
                case 7: {
                    string = "Antalya";
                    break;
                }
                case 8: {
                    string = "Artvin";
                    break;
                }
                case 9: {
                    string = "Aydin";
                    break;
                }
                case 10: {
                    string = "Balikesir";
                    break;
                }
                case 11: {
                    string = "Bilecik";
                    break;
                }
                case 12: {
                    string = "Bingol";
                    break;
                }
                case 13: {
                    string = "Bitlis";
                    break;
                }
                case 14: {
                    string = "Bolu";
                    break;
                }
                case 15: {
                    string = "Burdur";
                    break;
                }
                case 16: {
                    string = "Bursa";
                    break;
                }
                case 17: {
                    string = "Canakkale";
                    break;
                }
                case 19: {
                    string = "Corum";
                    break;
                }
                case 20: {
                    string = "Denizli";
                    break;
                }
                case 21: {
                    string = "Diyarbakir";
                    break;
                }
                case 22: {
                    string = "Edirne";
                    break;
                }
                case 23: {
                    string = "Elazig";
                    break;
                }
                case 24: {
                    string = "Erzincan";
                    break;
                }
                case 25: {
                    string = "Erzurum";
                    break;
                }
                case 26: {
                    string = "Eskisehir";
                    break;
                }
                case 28: {
                    string = "Giresun";
                    break;
                }
                case 31: {
                    string = "Hatay";
                    break;
                }
                case 32: {
                    string = "Mersin";
                    break;
                }
                case 33: {
                    string = "Isparta";
                    break;
                }
                case 34: {
                    string = "Istanbul";
                    break;
                }
                case 35: {
                    string = "Izmir";
                    break;
                }
                case 37: {
                    string = "Kastamonu";
                    break;
                }
                case 38: {
                    string = "Kayseri";
                    break;
                }
                case 39: {
                    string = "Kirklareli";
                    break;
                }
                case 40: {
                    string = "Kirsehir";
                    break;
                }
                case 41: {
                    string = "Kocaeli";
                    break;
                }
                case 43: {
                    string = "Kutahya";
                    break;
                }
                case 44: {
                    string = "Malatya";
                    break;
                }
                case 45: {
                    string = "Manisa";
                    break;
                }
                case 46: {
                    string = "Kahramanmaras";
                    break;
                }
                case 48: {
                    string = "Mugla";
                    break;
                }
                case 49: {
                    string = "Mus";
                    break;
                }
                case 50: {
                    string = "Nevsehir";
                    break;
                }
                case 52: {
                    string = "Ordu";
                    break;
                }
                case 53: {
                    string = "Rize";
                    break;
                }
                case 54: {
                    string = "Sakarya";
                    break;
                }
                case 55: {
                    string = "Samsun";
                    break;
                }
                case 57: {
                    string = "Sinop";
                    break;
                }
                case 58: {
                    string = "Sivas";
                    break;
                }
                case 59: {
                    string = "Tekirdag";
                    break;
                }
                case 60: {
                    string = "Tokat";
                    break;
                }
                case 61: {
                    string = "Trabzon";
                    break;
                }
                case 62: {
                    string = "Tunceli";
                    break;
                }
                case 63: {
                    string = "Sanliurfa";
                    break;
                }
                case 64: {
                    string = "Usak";
                    break;
                }
                case 65: {
                    string = "Van";
                    break;
                }
                case 66: {
                    string = "Yozgat";
                    break;
                }
                case 68: {
                    string = "Ankara";
                    break;
                }
                case 69: {
                    string = "Gumushane";
                    break;
                }
                case 70: {
                    string = "Hakkari";
                    break;
                }
                case 71: {
                    string = "Konya";
                    break;
                }
                case 72: {
                    string = "Mardin";
                    break;
                }
                case 73: {
                    string = "Nigde";
                    break;
                }
                case 74: {
                    string = "Siirt";
                    break;
                }
                case 75: {
                    string = "Aksaray";
                    break;
                }
                case 76: {
                    string = "Batman";
                    break;
                }
                case 77: {
                    string = "Bayburt";
                    break;
                }
                case 78: {
                    string = "Karaman";
                    break;
                }
                case 79: {
                    string = "Kirikkale";
                    break;
                }
                case 80: {
                    string = "Sirnak";
                    break;
                }
                case 81: {
                    string = "Adana";
                    break;
                }
                case 82: {
                    string = "Cankiri";
                    break;
                }
                case 83: {
                    string = "Gaziantep";
                    break;
                }
                case 84: {
                    string = "Kars";
                    break;
                }
                case 85: {
                    string = "Zonguldak";
                    break;
                }
                case 86: {
                    string = "Ardahan";
                    break;
                }
                case 87: {
                    string = "Bartin";
                    break;
                }
                case 88: {
                    string = "Igdir";
                    break;
                }
                case 89: {
                    string = "Karabuk";
                    break;
                }
                case 90: {
                    string = "Kilis";
                    break;
                }
                case 91: {
                    string = "Osmaniye";
                    break;
                }
                case 92: {
                    string = "Yalova";
                    break;
                }
                case 93: {
                    string = "Duzce";
                }
            }
        }
        if (string2.equals("TT")) {
            switch (var3_3) {
                case 1: {
                    string = "Arima";
                    break;
                }
                case 2: {
                    string = "Caroni";
                    break;
                }
                case 3: {
                    string = "Mayaro";
                    break;
                }
                case 4: {
                    string = "Nariva";
                    break;
                }
                case 5: {
                    string = "Port-of-Spain";
                    break;
                }
                case 6: {
                    string = "Saint Andrew";
                    break;
                }
                case 7: {
                    string = "Saint David";
                    break;
                }
                case 8: {
                    string = "Saint George";
                    break;
                }
                case 9: {
                    string = "Saint Patrick";
                    break;
                }
                case 10: {
                    string = "San Fernando";
                    break;
                }
                case 11: {
                    string = "Tobago";
                    break;
                }
                case 12: {
                    string = "Victoria";
                }
            }
        }
        if (string2.equals("TW")) {
            switch (var3_3) {
                case 1: {
                    string = "Fu-chien";
                    break;
                }
                case 2: {
                    string = "Kao-hsiung";
                    break;
                }
                case 3: {
                    string = "T'ai-pei";
                    break;
                }
                case 4: {
                    string = "T'ai-wan";
                }
            }
        }
        if (string2.equals("TZ")) {
            switch (var3_3) {
                case 2: {
                    string = "Pwani";
                    break;
                }
                case 3: {
                    string = "Dodoma";
                    break;
                }
                case 4: {
                    string = "Iringa";
                    break;
                }
                case 5: {
                    string = "Kigoma";
                    break;
                }
                case 6: {
                    string = "Kilimanjaro";
                    break;
                }
                case 7: {
                    string = "Lindi";
                    break;
                }
                case 8: {
                    string = "Mara";
                    break;
                }
                case 9: {
                    string = "Mbeya";
                    break;
                }
                case 10: {
                    string = "Morogoro";
                    break;
                }
                case 11: {
                    string = "Mtwara";
                    break;
                }
                case 12: {
                    string = "Mwanza";
                    break;
                }
                case 13: {
                    string = "Pemba North";
                    break;
                }
                case 14: {
                    string = "Ruvuma";
                    break;
                }
                case 15: {
                    string = "Shinyanga";
                    break;
                }
                case 16: {
                    string = "Singida";
                    break;
                }
                case 17: {
                    string = "Tabora";
                    break;
                }
                case 18: {
                    string = "Tanga";
                    break;
                }
                case 19: {
                    string = "Kagera";
                    break;
                }
                case 20: {
                    string = "Pemba South";
                    break;
                }
                case 21: {
                    string = "Zanzibar Central";
                    break;
                }
                case 22: {
                    string = "Zanzibar North";
                    break;
                }
                case 23: {
                    string = "Dar es Salaam";
                    break;
                }
                case 24: {
                    string = "Rukwa";
                    break;
                }
                case 25: {
                    string = "Zanzibar Urban";
                    break;
                }
                case 26: {
                    string = "Arusha";
                    break;
                }
                case 27: {
                    string = "Manyara";
                }
            }
        }
        if (string2.equals("UA")) {
            switch (var3_3) {
                case 1: {
                    string = "Cherkas'ka Oblast'";
                    break;
                }
                case 2: {
                    string = "Chernihivs'ka Oblast'";
                    break;
                }
                case 3: {
                    string = "Chernivets'ka Oblast'";
                    break;
                }
                case 4: {
                    string = "Dnipropetrovs'ka Oblast'";
                    break;
                }
                case 5: {
                    string = "Donets'ka Oblast'";
                    break;
                }
                case 6: {
                    string = "Ivano-Frankivs'ka Oblast'";
                    break;
                }
                case 7: {
                    string = "Kharkivs'ka Oblast'";
                    break;
                }
                case 8: {
                    string = "Khersons'ka Oblast'";
                    break;
                }
                case 9: {
                    string = "Khmel'nyts'ka Oblast'";
                    break;
                }
                case 10: {
                    string = "Kirovohrads'ka Oblast'";
                    break;
                }
                case 11: {
                    string = "Krym";
                    break;
                }
                case 12: {
                    string = "Kyyiv";
                    break;
                }
                case 13: {
                    string = "Kyyivs'ka Oblast'";
                    break;
                }
                case 14: {
                    string = "Luhans'ka Oblast'";
                    break;
                }
                case 15: {
                    string = "L'vivs'ka Oblast'";
                    break;
                }
                case 16: {
                    string = "Mykolayivs'ka Oblast'";
                    break;
                }
                case 17: {
                    string = "Odes'ka Oblast'";
                    break;
                }
                case 18: {
                    string = "Poltavs'ka Oblast'";
                    break;
                }
                case 19: {
                    string = "Rivnens'ka Oblast'";
                    break;
                }
                case 20: {
                    string = "Sevastopol'";
                    break;
                }
                case 21: {
                    string = "Sums'ka Oblast'";
                    break;
                }
                case 22: {
                    string = "Ternopil's'ka Oblast'";
                    break;
                }
                case 23: {
                    string = "Vinnyts'ka Oblast'";
                    break;
                }
                case 24: {
                    string = "Volyns'ka Oblast'";
                    break;
                }
                case 25: {
                    string = "Zakarpats'ka Oblast'";
                    break;
                }
                case 26: {
                    string = "Zaporiz'ka Oblast'";
                    break;
                }
                case 27: {
                    string = "Zhytomyrs'ka Oblast'";
                }
            }
        }
        if (string2.equals("UG")) {
            switch (var3_3) {
                case 5: {
                    string = "Busoga";
                    break;
                }
                case 8: {
                    string = "Karamoja";
                    break;
                }
                case 12: {
                    string = "South Buganda";
                    break;
                }
                case 18: {
                    string = "Central";
                    break;
                }
                case 20: {
                    string = "Eastern";
                    break;
                }
                case 21: {
                    string = "Nile";
                    break;
                }
                case 22: {
                    string = "North Buganda";
                    break;
                }
                case 23: {
                    string = "Northern";
                    break;
                }
                case 24: {
                    string = "Southern";
                    break;
                }
                case 25: {
                    string = "Western";
                    break;
                }
                case 33: {
                    string = "Jinja";
                    break;
                }
                case 36: {
                    string = "Kalangala";
                    break;
                }
                case 37: {
                    string = "Kampala";
                    break;
                }
                case 42: {
                    string = "Kiboga";
                    break;
                }
                case 52: {
                    string = "Mbarara";
                    break;
                }
                case 56: {
                    string = "Mubende";
                    break;
                }
                case 65: {
                    string = "Adjumani";
                    break;
                }
                case 66: {
                    string = "Bugiri";
                    break;
                }
                case 67: {
                    string = "Busia";
                    break;
                }
                case 69: {
                    string = "Katakwi";
                    break;
                }
                case 71: {
                    string = "Masaka";
                    break;
                }
                case 73: {
                    string = "Nakasongola";
                    break;
                }
                case 74: {
                    string = "Sembabule";
                    break;
                }
                case 77: {
                    string = "Arua";
                    break;
                }
                case 78: {
                    string = "Iganga";
                    break;
                }
                case 79: {
                    string = "Kabarole";
                    break;
                }
                case 80: {
                    string = "Kaberamaido";
                    break;
                }
                case 81: {
                    string = "Kamwenge";
                    break;
                }
                case 82: {
                    string = "Kanungu";
                    break;
                }
                case 83: {
                    string = "Kayunga";
                    break;
                }
                case 84: {
                    string = "Kitgum";
                    break;
                }
                case 85: {
                    string = "Kyenjojo";
                    break;
                }
                case 86: {
                    string = "Mayuge";
                    break;
                }
                case 87: {
                    string = "Mbale";
                    break;
                }
                case 88: {
                    string = "Moroto";
                    break;
                }
                case 89: {
                    string = "Mpigi";
                    break;
                }
                case 90: {
                    string = "Mukono";
                    break;
                }
                case 91: {
                    string = "Nakapiripirit";
                    break;
                }
                case 92: {
                    string = "Pader";
                    break;
                }
                case 93: {
                    string = "Rukungiri";
                    break;
                }
                case 94: {
                    string = "Sironko";
                    break;
                }
                case 95: {
                    string = "Soroti";
                    break;
                }
                case 96: {
                    string = "Wakiso";
                    break;
                }
                case 97: {
                    string = "Yumbe";
                }
            }
        }
        if (string2.equals("UY")) {
            switch (var3_3) {
                case 1: {
                    string = "Artigas";
                    break;
                }
                case 2: {
                    string = "Canelones";
                    break;
                }
                case 3: {
                    string = "Cerro Largo";
                    break;
                }
                case 4: {
                    string = "Colonia";
                    break;
                }
                case 5: {
                    string = "Durazno";
                    break;
                }
                case 6: {
                    string = "Flores";
                    break;
                }
                case 7: {
                    string = "Florida";
                    break;
                }
                case 8: {
                    string = "Lavalleja";
                    break;
                }
                case 9: {
                    string = "Maldonado";
                    break;
                }
                case 10: {
                    string = "Montevideo";
                    break;
                }
                case 11: {
                    string = "Paysandu";
                    break;
                }
                case 12: {
                    string = "Rio Negro";
                    break;
                }
                case 13: {
                    string = "Rivera";
                    break;
                }
                case 14: {
                    string = "Rocha";
                    break;
                }
                case 15: {
                    string = "Salto";
                    break;
                }
                case 16: {
                    string = "San Jose";
                    break;
                }
                case 17: {
                    string = "Soriano";
                    break;
                }
                case 18: {
                    string = "Tacuarembo";
                    break;
                }
                case 19: {
                    string = "Treinta y Tres";
                }
            }
        }
        if (string2.equals("UZ")) {
            switch (var3_3) {
                case 1: {
                    string = "Andijon";
                    break;
                }
                case 2: {
                    string = "Bukhoro";
                    break;
                }
                case 3: {
                    string = "Farghona";
                    break;
                }
                case 4: {
                    string = "Jizzakh";
                    break;
                }
                case 5: {
                    string = "Khorazm";
                    break;
                }
                case 6: {
                    string = "Namangan";
                    break;
                }
                case 7: {
                    string = "Nawoiy";
                    break;
                }
                case 8: {
                    string = "Qashqadaryo";
                    break;
                }
                case 9: {
                    string = "Qoraqalpoghiston";
                    break;
                }
                case 10: {
                    string = "Samarqand";
                    break;
                }
                case 11: {
                    string = "Sirdaryo";
                    break;
                }
                case 12: {
                    string = "Surkhondaryo";
                    break;
                }
                case 13: {
                    string = "Toshkent";
                    break;
                }
                case 14: {
                    string = "Toshkent";
                }
            }
        }
        if (string2.equals("VC")) {
            switch (var3_3) {
                case 1: {
                    string = "Charlotte";
                    break;
                }
                case 2: {
                    string = "Saint Andrew";
                    break;
                }
                case 3: {
                    string = "Saint David";
                    break;
                }
                case 4: {
                    string = "Saint George";
                    break;
                }
                case 5: {
                    string = "Saint Patrick";
                    break;
                }
                case 6: {
                    string = "Grenadines";
                }
            }
        }
        if (string2.equals("VE")) {
            switch (var3_3) {
                case 1: {
                    string = "Amazonas";
                    break;
                }
                case 2: {
                    string = "Anzoategui";
                    break;
                }
                case 3: {
                    string = "Apure";
                    break;
                }
                case 4: {
                    string = "Aragua";
                    break;
                }
                case 5: {
                    string = "Barinas";
                    break;
                }
                case 6: {
                    string = "Bolivar";
                    break;
                }
                case 7: {
                    string = "Carabobo";
                    break;
                }
                case 8: {
                    string = "Cojedes";
                    break;
                }
                case 9: {
                    string = "Delta Amacuro";
                    break;
                }
                case 11: {
                    string = "Falcon";
                    break;
                }
                case 12: {
                    string = "Guarico";
                    break;
                }
                case 13: {
                    string = "Lara";
                    break;
                }
                case 14: {
                    string = "Merida";
                    break;
                }
                case 15: {
                    string = "Miranda";
                    break;
                }
                case 16: {
                    string = "Monagas";
                    break;
                }
                case 17: {
                    string = "Nueva Esparta";
                    break;
                }
                case 18: {
                    string = "Portuguesa";
                    break;
                }
                case 19: {
                    string = "Sucre";
                    break;
                }
                case 20: {
                    string = "Tachira";
                    break;
                }
                case 21: {
                    string = "Trujillo";
                    break;
                }
                case 22: {
                    string = "Yaracuy";
                    break;
                }
                case 23: {
                    string = "Zulia";
                    break;
                }
                case 24: {
                    string = "Dependencias Federales";
                    break;
                }
                case 25: {
                    string = "Distrito Federal";
                    break;
                }
                case 26: {
                    string = "Vargas";
                }
            }
        }
        if (string2.equals("VN")) {
            switch (var3_3) {
                case 1: {
                    string = "An Giang";
                    break;
                }
                case 2: {
                    string = "Bac Thai";
                    break;
                }
                case 3: {
                    string = "Ben Tre";
                    break;
                }
                case 4: {
                    string = "Binh Tri Thien";
                    break;
                }
                case 5: {
                    string = "Cao Bang";
                    break;
                }
                case 6: {
                    string = "Cuu Long";
                    break;
                }
                case 7: {
                    string = "Dac Lac";
                    break;
                }
                case 9: {
                    string = "Dong Thap";
                    break;
                }
                case 11: {
                    string = "Ha Bac";
                    break;
                }
                case 12: {
                    string = "Hai Hung";
                    break;
                }
                case 13: {
                    string = "Hai Phong";
                    break;
                }
                case 14: {
                    string = "Ha Nam Ninh";
                    break;
                }
                case 15: {
                    string = "Ha Noi";
                    break;
                }
                case 16: {
                    string = "Ha Son Binh";
                    break;
                }
                case 17: {
                    string = "Ha Tuyen";
                    break;
                }
                case 19: {
                    string = "Hoang Lien Son";
                    break;
                }
                case 20: {
                    string = "Ho Chi Minh";
                    break;
                }
                case 21: {
                    string = "Kien Giang";
                    break;
                }
                case 22: {
                    string = "Lai Chau";
                    break;
                }
                case 23: {
                    string = "Lam Dong";
                    break;
                }
                case 24: {
                    string = "Long An";
                    break;
                }
                case 25: {
                    string = "Minh Hai";
                    break;
                }
                case 26: {
                    string = "Nghe Tinh";
                    break;
                }
                case 27: {
                    string = "Nghia Binh";
                    break;
                }
                case 28: {
                    string = "Phu Khanh";
                    break;
                }
                case 29: {
                    string = "Quang Nam-Da Nang";
                    break;
                }
                case 30: {
                    string = "Quang Ninh";
                    break;
                }
                case 31: {
                    string = "Song Be";
                    break;
                }
                case 32: {
                    string = "Son La";
                    break;
                }
                case 33: {
                    string = "Tay Ninh";
                    break;
                }
                case 34: {
                    string = "Thanh Hoa";
                    break;
                }
                case 35: {
                    string = "Thai Binh";
                    break;
                }
                case 36: {
                    string = "Thuan Hai";
                    break;
                }
                case 37: {
                    string = "Tien Giang";
                    break;
                }
                case 38: {
                    string = "Vinh Phu";
                    break;
                }
                case 39: {
                    string = "Lang Son";
                    break;
                }
                case 40: {
                    string = "Dong Nai";
                    break;
                }
                case 43: {
                    string = "An Giang";
                    break;
                }
                case 44: {
                    string = "Dac Lac";
                    break;
                }
                case 45: {
                    string = "Dong Nai";
                    break;
                }
                case 46: {
                    string = "Dong Thap";
                    break;
                }
                case 47: {
                    string = "Kien Giang";
                    break;
                }
                case 48: {
                    string = "Minh Hai";
                    break;
                }
                case 49: {
                    string = "Song Be";
                    break;
                }
                case 50: {
                    string = "Vinh Phu";
                    break;
                }
                case 51: {
                    string = "Ha Noi";
                    break;
                }
                case 52: {
                    string = "Ho Chi Minh";
                    break;
                }
                case 53: {
                    string = "Ba Ria-Vung Tau";
                    break;
                }
                case 54: {
                    string = "Binh Dinh";
                    break;
                }
                case 55: {
                    string = "Binh Thuan";
                    break;
                }
                case 56: {
                    string = "Can Tho";
                    break;
                }
                case 57: {
                    string = "Gia Lai";
                    break;
                }
                case 58: {
                    string = "Ha Giang";
                    break;
                }
                case 59: {
                    string = "Ha Tay";
                    break;
                }
                case 60: {
                    string = "Ha Tinh";
                    break;
                }
                case 61: {
                    string = "Hoa Binh";
                    break;
                }
                case 62: {
                    string = "Khanh Hoa";
                    break;
                }
                case 63: {
                    string = "Kon Tum";
                    break;
                }
                case 64: {
                    string = "Quang Tri";
                    break;
                }
                case 65: {
                    string = "Nam Ha";
                    break;
                }
                case 66: {
                    string = "Nghe An";
                    break;
                }
                case 67: {
                    string = "Ninh Binh";
                    break;
                }
                case 68: {
                    string = "Ninh Thuan";
                    break;
                }
                case 69: {
                    string = "Phu Yen";
                    break;
                }
                case 70: {
                    string = "Quang Binh";
                    break;
                }
                case 71: {
                    string = "Quang Ngai";
                    break;
                }
                case 72: {
                    string = "Quang Tri";
                    break;
                }
                case 73: {
                    string = "Soc Trang";
                    break;
                }
                case 74: {
                    string = "Thua Thien";
                    break;
                }
                case 75: {
                    string = "Tra Vinh";
                    break;
                }
                case 76: {
                    string = "Tuyen Quang";
                    break;
                }
                case 77: {
                    string = "Vinh Long";
                    break;
                }
                case 78: {
                    string = "Da Nang";
                    break;
                }
                case 79: {
                    string = "Hai Duong";
                    break;
                }
                case 80: {
                    string = "Ha Nam";
                    break;
                }
                case 81: {
                    string = "Hung Yen";
                    break;
                }
                case 82: {
                    string = "Nam Dinh";
                    break;
                }
                case 83: {
                    string = "Phu Tho";
                    break;
                }
                case 84: {
                    string = "Quang Nam";
                    break;
                }
                case 85: {
                    string = "Thai Nguyen";
                    break;
                }
                case 86: {
                    string = "Vinh Puc Province";
                    break;
                }
                case 87: {
                    string = "Can Tho";
                    break;
                }
                case 88: {
                    string = "Dak Lak";
                    break;
                }
                case 89: {
                    string = "Lai Chau";
                    break;
                }
                case 90: {
                    string = "Lao Cai";
                    break;
                }
                case 91: {
                    string = "Dak Nong";
                    break;
                }
                case 92: {
                    string = "Dien Bien";
                    break;
                }
                case 93: {
                    string = "Hau Giang";
                }
            }
        }
        if (string2.equals("VU")) {
            switch (var3_3) {
                case 5: {
                    string = "Ambrym";
                    break;
                }
                case 6: {
                    string = "Aoba";
                    break;
                }
                case 7: {
                    string = "Torba";
                    break;
                }
                case 8: {
                    string = "Efate";
                    break;
                }
                case 9: {
                    string = "Epi";
                    break;
                }
                case 10: {
                    string = "Malakula";
                    break;
                }
                case 11: {
                    string = "Paama";
                    break;
                }
                case 12: {
                    string = "Pentecote";
                    break;
                }
                case 13: {
                    string = "Sanma";
                    break;
                }
                case 14: {
                    string = "Shepherd";
                    break;
                }
                case 15: {
                    string = "Tafea";
                    break;
                }
                case 16: {
                    string = "Malampa";
                    break;
                }
                case 17: {
                    string = "Penama";
                    break;
                }
                case 18: {
                    string = "Shefa";
                }
            }
        }
        if (string2.equals("WS")) {
            switch (var3_3) {
                case 2: {
                    string = "Aiga-i-le-Tai";
                    break;
                }
                case 3: {
                    string = "Atua";
                    break;
                }
                case 4: {
                    string = "Fa";
                    break;
                }
                case 5: {
                    string = "Gaga";
                    break;
                }
                case 6: {
                    string = "Va";
                    break;
                }
                case 7: {
                    string = "Gagaifomauga";
                    break;
                }
                case 8: {
                    string = "Palauli";
                    break;
                }
                case 9: {
                    string = "Satupa";
                    break;
                }
                case 10: {
                    string = "Tuamasaga";
                    break;
                }
                case 11: {
                    string = "Vaisigano";
                }
            }
        }
        if (string2.equals("YE")) {
            switch (var3_3) {
                case 1: {
                    string = "Abyan";
                    break;
                }
                case 2: {
                    string = "Adan";
                    break;
                }
                case 3: {
                    string = "Al Mahrah";
                    break;
                }
                case 4: {
                    string = "Hadramawt";
                    break;
                }
                case 5: {
                    string = "Shabwah";
                    break;
                }
                case 6: {
                    string = "Al Ghaydah";
                    break;
                }
                case 8: {
                    string = "Al Hudaydah";
                    break;
                }
                case 10: {
                    string = "Al Mahwit";
                    break;
                }
                case 11: {
                    string = "Dhamar";
                    break;
                }
                case 14: {
                    string = "Ma'rib";
                    break;
                }
                case 15: {
                    string = "Sa";
                    break;
                }
                case 16: {
                    string = "San";
                    break;
                }
                case 20: {
                    string = "Al Bayda'";
                    break;
                }
                case 21: {
                    string = "Al Jawf";
                    break;
                }
                case 22: {
                    string = "Hajjah";
                    break;
                }
                case 23: {
                    string = "Ibb";
                    break;
                }
                case 24: {
                    string = "Lahij";
                    break;
                }
                case 25: {
                    string = "Ta";
                }
            }
        }
        if (string2.equals("ZA")) {
            switch (var3_3) {
                case 1: {
                    string = "North-Western Province";
                    break;
                }
                case 2: {
                    string = "KwaZulu-Natal";
                    break;
                }
                case 3: {
                    string = "Free State";
                    break;
                }
                case 5: {
                    string = "Eastern Cape";
                    break;
                }
                case 6: {
                    string = "Gauteng";
                    break;
                }
                case 7: {
                    string = "Mpumalanga";
                    break;
                }
                case 8: {
                    string = "Northern Cape";
                    break;
                }
                case 9: {
                    string = "Limpopo";
                    break;
                }
                case 10: {
                    string = "North-West";
                    break;
                }
                case 11: {
                    string = "Western Cape";
                }
            }
        }
        if (string2.equals("ZM")) {
            switch (var3_3) {
                case 1: {
                    string = "Western";
                    break;
                }
                case 2: {
                    string = "Central";
                    break;
                }
                case 3: {
                    string = "Eastern";
                    break;
                }
                case 4: {
                    string = "Luapula";
                    break;
                }
                case 5: {
                    string = "Northern";
                    break;
                }
                case 6: {
                    string = "North-Western";
                    break;
                }
                case 7: {
                    string = "Southern";
                    break;
                }
                case 8: {
                    string = "Copperbelt";
                    break;
                }
                case 9: {
                    string = "Lusaka";
                }
            }
        }
        if (string2.equals("ZW")) {
            switch (var3_3) {
                case 1: {
                    string = "Manicaland";
                    break;
                }
                case 2: {
                    string = "Midlands";
                    break;
                }
                case 3: {
                    string = "Mashonaland Central";
                    break;
                }
                case 4: {
                    string = "Mashonaland East";
                    break;
                }
                case 5: {
                    string = "Mashonaland West";
                    break;
                }
                case 6: {
                    string = "Matabeleland North";
                    break;
                }
                case 7: {
                    string = "Matabeleland South";
                    break;
                }
                case 8: {
                    string = "Masvingo";
                    break;
                }
                case 9: {
                    string = "Bulawayo";
                    break;
                }
                case 10: {
                    string = "Harare";
                }
            }
        }
        return string;
    }
}

