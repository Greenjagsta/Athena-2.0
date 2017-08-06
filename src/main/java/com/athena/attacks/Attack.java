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

package com.athena.attacks;

import com.athena.hashfamily.md.MD5;
import com.athena.hashfamily.sha.SHA1;
import com.athena.utils.HashManager;
import com.athena.utils.Output;
import com.athena.utils.StringUtils;

import java.util.ArrayList;

import static com.athena.utils.StringUtils.byteArrayToHexString;
import static com.athena.utils.StringUtils.byteArrayToString;

public abstract class Attack {
    private HashManager hashman;
    private StringBuilder sb = new StringBuilder();
    private ArrayList<byte[]> candidates;
    private ArrayList<Integer> hashType;

    public abstract ArrayList<byte[]> getNextCandidates();

    public void attack() {
        for (byte[] fileBuffer : getNextCandidates())
            for (byte[] candidate : StringUtils.formatFileBytes(fileBuffer)) {
                if (!hashman.isAllCracked()) {
                    checkAttempt(candidate);
                } else {
                    return;
                }
            }
    }

    private void checkAttempt(byte[] candidate) {
        byte[] candidateHash = getDigest(candidate);

        /*System.out.println();
        hashman.printHashes();
        System.out.println("Hash: " + com.athena.utils.StringUtils.byteArrayToHexString(candidateHash) + " Bytes: " + java.util.Arrays.toString(candidateHash));
        System.out.println(hashman.hashExists(candidateHash));*/

        if (hashman.hashExists(candidateHash)) {
            hashman.setCracked(sb.append(byteArrayToHexString(candidateHash)).toString());
            Output.printCracked(sb.toString(), byteArrayToString(candidate));
            sb.setLength(0);
        }
    }

    private byte[] getDigest(byte[] candidate) {
        switch (hashType.get(0)) {
            case 100:
                return MD5.digest(candidate);

            case 200:
                return SHA1.digest(candidate);

            default:
                break;
        }
        return new byte[0];
    }

    public void setHashman(HashManager hashman) {
        this.hashman = hashman;
    }

    public void setHashType(ArrayList<Integer> hashType) {
        this.hashType = hashType;
    }
}