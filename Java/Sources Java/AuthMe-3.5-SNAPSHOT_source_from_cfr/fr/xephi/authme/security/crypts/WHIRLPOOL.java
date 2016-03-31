/*
 * Decompiled with CFR 0_110.
 */
package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.crypts.EncryptionMethod;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WHIRLPOOL
implements EncryptionMethod {
    public static final int DIGESTBITS = 512;
    public static final int DIGESTBYTES = 64;
    protected static final int R = 10;
    private static final String sbox = "\u1823\uc6e8\u87b8\u014f\u36a6\ud2f5\u796f\u9152\u60bc\u9b8e\ua30c\u7b35\u1de0\ud7c2\u2e4b\ufe57\u1577\u37e5\u9ff0\u4ada\u58c9\u290a\ub1a0\u6b85\ubd5d\u10f4\ucb3e\u0567\ue427\u418b\ua77d\u95d8\ufbee\u7c66\udd17\u479e\uca2d\ubf07\uad5a\u8333\u6302\uaa71\uc819\u49d9\uf2e3\u5b88\u9a26\u32b0\ue90f\ud580\ubecd\u3448\uff7a\u905f\u2068\u1aae\ub454\u9322\u64f1\u7312\u4008\uc3ec\udba1\u8d3d\u9700\ucf2b\u7682\ud61b\ub5af\u6a50\u45f3\u30ef\u3f55\ua2ea\u65ba\u2fc0\ude1c\ufd4d\u9275\u068a\ub2e6\u0e1f\u62d4\ua896\uf9c5\u2559\u8472\u394c\u5e78\u388c\ud1a5\ue261\ub321\u9c1e\u43c7\ufc04\u5199\u6d0d\ufadf\u7e24\u3bab\uce11\u8f4e\ub7eb\u3c81\u94f7\ub913\u2cd3\ue76e\uc403\u5644\u7fa9\u2abb\uc153\udc0b\u9d6c\u3174\uf646\uac89\u14e1\u163a\u6909\u70b6\ud0ed\ucc42\u98a4\u285c\uf886";
    private static long[][] C = new long[8][256];
    private static long[] rc = new long[11];
    protected byte[] bitLength = new byte[32];
    protected byte[] buffer = new byte[64];
    protected int bufferBits = 0;
    protected int bufferPos = 0;
    protected long[] hash = new long[8];
    protected long[] K = new long[8];
    protected long[] L = new long[8];
    protected long[] block = new long[8];
    protected long[] state = new long[8];

    protected void processBuffer() {
        int i = 0;
        int j = 0;
        while (i < 8) {
            this.block[i] = (long)this.buffer[j] << 56 ^ ((long)this.buffer[j + 1] & 255) << 48 ^ ((long)this.buffer[j + 2] & 255) << 40 ^ ((long)this.buffer[j + 3] & 255) << 32 ^ ((long)this.buffer[j + 4] & 255) << 24 ^ ((long)this.buffer[j + 5] & 255) << 16 ^ ((long)this.buffer[j + 6] & 255) << 8 ^ (long)this.buffer[j + 7] & 255;
            ++i;
            j += 8;
        }
        for (i = 0; i < 8; ++i) {
            this.K[i] = this.hash[i];
            this.state[i] = this.block[i] ^ this.K[i];
        }
        for (int r = 1; r <= 10; ++r) {
            int s;
            int i2;
            int t;
            for (i2 = 0; i2 < 8; ++i2) {
                this.L[i2] = 0;
                t = 0;
                s = 56;
                while (t < 8) {
                    long[] arrl = this.L;
                    int n = i2;
                    arrl[n] = arrl[n] ^ C[t][(int)(this.K[i2 - t & 7] >>> s) & 255];
                    ++t;
                    s -= 8;
                }
            }
            for (i2 = 0; i2 < 8; ++i2) {
                this.K[i2] = this.L[i2];
            }
            long[] arrl = this.K;
            arrl[0] = arrl[0] ^ rc[r];
            for (i2 = 0; i2 < 8; ++i2) {
                this.L[i2] = this.K[i2];
                t = 0;
                s = 56;
                while (t < 8) {
                    long[] arrl2 = this.L;
                    int n = i2;
                    arrl2[n] = arrl2[n] ^ C[t][(int)(this.state[i2 - t & 7] >>> s) & 255];
                    ++t;
                    s -= 8;
                }
            }
            for (i2 = 0; i2 < 8; ++i2) {
                this.state[i2] = this.L[i2];
            }
        }
        for (i = 0; i < 8; ++i) {
            long[] arrl = this.hash;
            int n = i;
            arrl[n] = arrl[n] ^ (this.state[i] ^ this.block[i]);
        }
    }

    public void NESSIEinit() {
        Arrays.fill(this.bitLength, 0);
        this.bufferPos = 0;
        this.bufferBits = 0;
        this.buffer[0] = 0;
        Arrays.fill(this.hash, 0);
    }

    public void NESSIEadd(byte[] source, long sourceBits) {
        int b;
        int sourcePos = 0;
        int sourceGap = 8 - ((int)sourceBits & 7) & 7;
        int bufferRem = this.bufferBits & 7;
        long value = sourceBits;
        int carry = 0;
        for (int i = 31; i >= 0; --i) {
            this.bitLength[i] = (byte)(carry += (this.bitLength[i] & 255) + ((int)value & 255));
            carry >>>= 8;
            value >>>= 8;
        }
        while (sourceBits > 8) {
            b = source[sourcePos] << sourceGap & 255 | (source[sourcePos + 1] & 255) >>> 8 - sourceGap;
            if (b < 0 || b >= 256) {
                throw new RuntimeException("LOGIC ERROR");
            }
            byte[] arrby = this.buffer;
            int n = this.bufferPos++;
            arrby[n] = (byte)(arrby[n] | b >>> bufferRem);
            this.bufferBits += 8 - bufferRem;
            if (this.bufferBits == 512) {
                this.processBuffer();
                this.bufferPos = 0;
                this.bufferBits = 0;
            }
            this.buffer[this.bufferPos] = (byte)(b << 8 - bufferRem & 255);
            this.bufferBits += bufferRem;
            sourceBits -= 8;
            ++sourcePos;
        }
        if (sourceBits > 0) {
            b = source[sourcePos] << sourceGap & 255;
            byte[] arrby = this.buffer;
            int n = this.bufferPos;
            arrby[n] = (byte)(arrby[n] | b >>> bufferRem);
        } else {
            b = 0;
        }
        if ((long)bufferRem + sourceBits < 8) {
            this.bufferBits = (int)((long)this.bufferBits + sourceBits);
        } else {
            ++this.bufferPos;
            this.bufferBits += 8 - bufferRem;
            sourceBits -= (long)(8 - bufferRem);
            if (this.bufferBits == 512) {
                this.processBuffer();
                this.bufferPos = 0;
                this.bufferBits = 0;
            }
            this.buffer[this.bufferPos] = (byte)(b << 8 - bufferRem & 255);
            this.bufferBits += (int)sourceBits;
        }
    }

    public void NESSIEfinalize(byte[] digest) {
        byte[] arrby = this.buffer;
        int n = this.bufferPos++;
        arrby[n] = (byte)(arrby[n] | 128 >>> (this.bufferBits & 7));
        if (this.bufferPos > 32) {
            while (this.bufferPos < 64) {
                this.buffer[this.bufferPos++] = 0;
            }
            this.processBuffer();
            this.bufferPos = 0;
        }
        while (this.bufferPos < 32) {
            this.buffer[this.bufferPos++] = 0;
        }
        System.arraycopy(this.bitLength, 0, this.buffer, 32, 32);
        this.processBuffer();
        int i = 0;
        int j = 0;
        while (i < 8) {
            long h = this.hash[i];
            digest[j] = (byte)(h >>> 56);
            digest[j + 1] = (byte)(h >>> 48);
            digest[j + 2] = (byte)(h >>> 40);
            digest[j + 3] = (byte)(h >>> 32);
            digest[j + 4] = (byte)(h >>> 24);
            digest[j + 5] = (byte)(h >>> 16);
            digest[j + 6] = (byte)(h >>> 8);
            digest[j + 7] = (byte)h;
            ++i;
            j += 8;
        }
    }

    public void NESSIEadd(String source) {
        if (source.length() > 0) {
            byte[] data = new byte[source.length()];
            for (int i = 0; i < source.length(); ++i) {
                data[i] = (byte)source.charAt(i);
            }
            this.NESSIEadd(data, 8 * data.length);
        }
    }

    protected static String display(byte[] array) {
        char[] val = new char[2 * array.length];
        String hex = "0123456789ABCDEF";
        for (int i = 0; i < array.length; ++i) {
            int b = array[i] & 255;
            val[2 * i] = hex.charAt(b >>> 4);
            val[2 * i + 1] = hex.charAt(b & 15);
        }
        return String.valueOf(val);
    }

    public String getHash(String password, String salt, String name) throws NoSuchAlgorithmException {
        byte[] digest = new byte[64];
        this.NESSIEinit();
        this.NESSIEadd(password);
        this.NESSIEfinalize(digest);
        return WHIRLPOOL.display(digest);
    }

    public boolean comparePassword(String hash, String password, String playerName) throws NoSuchAlgorithmException {
        return hash.equals(this.getHash(password, "", ""));
    }

    static {
        for (int x = 0; x < 256; ++x) {
            long v4;
            char c = "\u1823\uc6e8\u87b8\u014f\u36a6\ud2f5\u796f\u9152\u60bc\u9b8e\ua30c\u7b35\u1de0\ud7c2\u2e4b\ufe57\u1577\u37e5\u9ff0\u4ada\u58c9\u290a\ub1a0\u6b85\ubd5d\u10f4\ucb3e\u0567\ue427\u418b\ua77d\u95d8\ufbee\u7c66\udd17\u479e\uca2d\ubf07\uad5a\u8333\u6302\uaa71\uc819\u49d9\uf2e3\u5b88\u9a26\u32b0\ue90f\ud580\ubecd\u3448\uff7a\u905f\u2068\u1aae\ub454\u9322\u64f1\u7312\u4008\uc3ec\udba1\u8d3d\u9700\ucf2b\u7682\ud61b\ub5af\u6a50\u45f3\u30ef\u3f55\ua2ea\u65ba\u2fc0\ude1c\ufd4d\u9275\u068a\ub2e6\u0e1f\u62d4\ua896\uf9c5\u2559\u8472\u394c\u5e78\u388c\ud1a5\ue261\ub321\u9c1e\u43c7\ufc04\u5199\u6d0d\ufadf\u7e24\u3bab\uce11\u8f4e\ub7eb\u3c81\u94f7\ub913\u2cd3\ue76e\uc403\u5644\u7fa9\u2abb\uc153\udc0b\u9d6c\u3174\uf646\uac89\u14e1\u163a\u6909\u70b6\ud0ed\ucc42\u98a4\u285c\uf886".charAt(x / 2);
            long v1 = (x & 1) == 0 ? (long)(c >>> 8) : (long)(c & 255);
            long v2 = v1 << 1;
            if (v2 >= 256) {
                v2 ^= 285;
            }
            if ((v4 = v2 << 1) >= 256) {
                v4 ^= 285;
            }
            long v5 = v4 ^ v1;
            long v8 = v4 << 1;
            if (v8 >= 256) {
                v8 ^= 285;
            }
            long v9 = v8 ^ v1;
            WHIRLPOOL.C[0][x] = v1 << 56 | v1 << 48 | v4 << 40 | v1 << 32 | v8 << 24 | v5 << 16 | v2 << 8 | v9;
            for (int t = 1; t < 8; ++t) {
                WHIRLPOOL.C[t][x] = C[t - 1][x] >>> 8 | C[t - 1][x] << 56;
            }
        }
        WHIRLPOOL.rc[0] = 0;
        for (int r = 1; r <= 10; ++r) {
            int i = 8 * (r - 1);
            WHIRLPOOL.rc[r] = C[0][i] & -72057594037927936L ^ C[1][i + 1] & 0xFF000000000000L ^ C[2][i + 2] & 0xFF0000000000L ^ C[3][i + 3] & 0xFF00000000L ^ C[4][i + 4] & 0xFF000000L ^ C[5][i + 5] & 0xFF0000 ^ C[6][i + 6] & 65280 ^ C[7][i + 7] & 255;
        }
    }
}

