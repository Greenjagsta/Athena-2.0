/*
 * Copyright (C) 2017 Jack Green
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.athena.hashfamily.sha;

public class SHA1 {
    public static byte[] digest(byte[] x) {
        int[] blks = new int[(((x.length + 8) >> 6) + 1) * 16];
        int i;
        for (i = 0; i < x.length; i++) {
            blks[i >> 2] |= x[i] << (24 - (i % 4) * 8);
        }
        
        blks[i >> 2] |= 0x80 << (24 - (i % 4) * 8);
        blks[blks.length - 1] = x.length * 8;
        int[] w = new int[80];
        
        int a = 1732584193;
        int b = -271733879;
        int c = -1732584194;
        int d = 271733878;
        int e = -1009589776;
        
        for (i = 0; i < blks.length; i += 16) {
            int olda = a;
            int oldb = b;
            int oldc = c;
            int oldd = d;
            int olde = e;
            
            for (int j = 0; j < 80; j++) {
                w[j] = (j < 16) ? blks[i + j] : (rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1));
                int t = rol(a, 5) + e + w[j] + ((j < 20) ? 1518500249 + ((b & c) | ((~b) & d)) : (j < 40) ? 1859775393 + (b ^ c ^ d) : (j < 60) ? -1894007588 + ((b & c) | (b & d) | (c & d)) : -899497514 + (b ^ c ^ d));
                
                e = d;
                d = c;
                c = rol(b, 30);
                b = a;
                a = t;
            }
            
            a += olda;
            b += oldb;
            c += oldc;
            d += oldd;
            e += olde;
        }
        
        byte[] digest = new byte[20];
        digest[0] = (byte) ((a >> 24) & 0xff);
        digest[1] = (byte) ((a >> 16) & 0xff);
        digest[2] = (byte) ((a >> 8) & 0xff);
        digest[3] = (byte) (a & 0xff);
        
        digest[4] = (byte) ((b >> 24) & 0xff);
        digest[5] = (byte) ((b >> 16) & 0xff);
        digest[6] = (byte) ((b >> 8) & 0xff);
        digest[7] = (byte) (b & 0xff);
        
        digest[8] = (byte) ((c >> 24) & 0xff);
        digest[9] = (byte) ((c >> 16) & 0xff);
        digest[10] = (byte) ((c >> 8) & 0xff);
        digest[11] = (byte) (c & 0xff);
        
        digest[12] = (byte) ((d >> 24) & 0xff);
        digest[13] = (byte) ((d >> 16) & 0xff);
        digest[14] = (byte) ((d >> 8) & 0xff);
        digest[15] = (byte) (d & 0xff);
        
        digest[16] = (byte) ((e >> 24) & 0xff);
        digest[17] = (byte) ((e >> 16) & 0xff);
        digest[18] = (byte) ((e >> 8) & 0xff);
        digest[19] = (byte) (e & 0xff);
        
        return digest;
    }
    
    private static int rol(int num, int cnt) {
        return (num << cnt) | (num >>> (32 - cnt));
    }
}
