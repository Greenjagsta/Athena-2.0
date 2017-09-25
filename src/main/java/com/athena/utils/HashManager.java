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

package com.athena.utils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.athena.utils.StringUtils.byteArrayToString;

public class HashManager {
    private ArrayList<byte[]> hashes;
    private ArrayList<byte[]> cracked;
    private ArrayList<byte[]> plains;

    public HashManager(ArrayList<byte[]> hashesIn) {
        //hashes = new HashMap<>();
        hashes = new ArrayList<>();
        cracked = new ArrayList<>();
        plains = new ArrayList<>();

        setHashes(hashesIn);
    }

    private void setHashes(ArrayList<byte[]> hashesIn) {
        try {
            for (byte[] hash : hashesIn) {
                //System.out.println("HashIN: " + StringUtils.byteArrayToHexString(hash));
                if (!(hash.length == 0) && !hashExists(hash)) {
                    hashes.add(hash);
                }
            }

            hashes.sort((b1, b2) -> {
                int len = Math.min(b1.length, b2.length);
                for (int i = 0; i < len; i++) {
                    int i1 = b1[i] < 0 ? 256 + b1[i] : b1[i];
                    int i2 = b2[i] < 0 ? 256 + b2[i] : b2[i];
                    int cmp = i1 - i2;
                    if (cmp != 0) {
                        return cmp;
                    }
                }
                return b1.length - b2.length;
            });
        } catch (NullPointerException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<byte[]> getHashes() {
        return hashes;
    }

    public ArrayList<byte[]> getCracked() {
        return cracked;
    }

    public boolean isAllCracked() {
        return (hashes.isEmpty());
    }

    public int getCrackedAmt() {
        return cracked.size();
    }

    public boolean hashExists(byte[] hashIn) {
        for (byte[] hash : hashes) {
            if (hash[0] > hashIn[0]) {
                return false;
            }
            if (Arrays.equals(hash, hashIn)) {
                return true;
            }
        }
        return false;
    }

    public void setCracked(byte[] hash, byte[] plaintext) {
        hashes.remove(getHashIndex(hash));
        cracked.add(hash);
        plains.add(plaintext);
    }

    public ArrayList<byte[]> getPlains() {
        return plains;
    }

    private int getHashIndex(byte[] hashIn) {
        for (byte[] hash : hashes) {
            if (Arrays.equals(hash, hashIn)) {
                return hashes.indexOf(hash);
            }
        }
        return -1;
    }
}