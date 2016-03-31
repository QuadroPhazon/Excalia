/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.ObjectUtils
 *  org.apache.commons.lang.ObjectUtils$Null
 */
package fr.xephi.authme.security;

import fr.xephi.authme.security.crypts.BCRYPT;
import fr.xephi.authme.security.crypts.CRAZYCRYPT1;
import fr.xephi.authme.security.crypts.CryptPBKDF2;
import fr.xephi.authme.security.crypts.DOUBLEMD5;
import fr.xephi.authme.security.crypts.IPB3;
import fr.xephi.authme.security.crypts.JOOMLA;
import fr.xephi.authme.security.crypts.MD5;
import fr.xephi.authme.security.crypts.MD5VB;
import fr.xephi.authme.security.crypts.MYBB;
import fr.xephi.authme.security.crypts.PHPBB;
import fr.xephi.authme.security.crypts.PHPFUSION;
import fr.xephi.authme.security.crypts.PLAINTEXT;
import fr.xephi.authme.security.crypts.ROYALAUTH;
import fr.xephi.authme.security.crypts.SALTED2MD5;
import fr.xephi.authme.security.crypts.SHA1;
import fr.xephi.authme.security.crypts.SHA256;
import fr.xephi.authme.security.crypts.SHA512;
import fr.xephi.authme.security.crypts.SMF;
import fr.xephi.authme.security.crypts.WBB3;
import fr.xephi.authme.security.crypts.WBB4;
import fr.xephi.authme.security.crypts.WHIRLPOOL;
import fr.xephi.authme.security.crypts.WORDPRESS;
import fr.xephi.authme.security.crypts.XAUTH;
import fr.xephi.authme.security.crypts.XF;
import org.apache.commons.lang.ObjectUtils;

public enum HashAlgorithm {
    MD5(MD5.class),
    SHA1(SHA1.class),
    SHA256(SHA256.class),
    WHIRLPOOL(WHIRLPOOL.class),
    XAUTH(XAUTH.class),
    MD5VB(MD5VB.class),
    PHPBB(PHPBB.class),
    PLAINTEXT(PLAINTEXT.class),
    MYBB(MYBB.class),
    IPB3(IPB3.class),
    PHPFUSION(PHPFUSION.class),
    SMF(SMF.class),
    XENFORO(XF.class),
    SALTED2MD5(SALTED2MD5.class),
    JOOMLA(JOOMLA.class),
    BCRYPT(BCRYPT.class),
    WBB3(WBB3.class),
    WBB4(WBB4.class),
    SHA512(SHA512.class),
    DOUBLEMD5(DOUBLEMD5.class),
    PBKDF2(CryptPBKDF2.class),
    WORDPRESS(WORDPRESS.class),
    ROYALAUTH(ROYALAUTH.class),
    CRAZYCRYPT1(CRAZYCRYPT1.class),
    CUSTOM(ObjectUtils.Null.class);
    
    Class<?> classe;

    private HashAlgorithm(Class<?> classe) {
        this.classe = classe;
    }

    public Class<?> getclass() {
        return this.classe;
    }
}

