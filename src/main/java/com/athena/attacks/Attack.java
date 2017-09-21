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

import com.athena.hashfamily.Hash;
import com.athena.utils.HashManager;
import com.athena.utils.Output;
import com.athena.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Attack {
    private HashManager hashman;
    private StringBuilder sb = new StringBuilder();
    private ArrayList<Integer> hashType;
    private Object digestFunction;
    private Method digest;
    private int mode;

    //public abstract ArrayList<byte[]> getNextCandidates();

    public abstract void attack();

    protected void checkAttempt(byte[] candidate) {
        byte[] candidateHash = getDigest(candidate);

        if (hashman.hashExists(candidateHash)) {
            hashman.setCracked(sb.append(StringUtils.byteArrayToHexString(candidateHash)).toString(), candidate);
            Output.noRecoveredUpdate();
            sb.setLength(0);
        }
    }

    private byte[] getDigest(byte[] candidate) {
        try {
            return (byte[]) digest.invoke(digestFunction, (Object) candidate);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Attack.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Attack.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[0];
    }

    void setHashman(HashManager hashman) {
        this.hashman = hashman;
    }

    public HashManager getHashman() {
        return this.hashman;
    }

    void initDigestInstance() {
        try {
            digestFunction = Hash.getHash(hashType.get(0)).getClassname().newInstance();
            digest = Hash.getHash(hashType.get(0)).getDigestInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(Attack.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Attack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setHashType(int hashType, ArrayList<byte[]> hashes) {
        if (hashType == 0 || !Hash.hashTypeExists(hashType)) {
            this.hashType = Hash.getHashType(StringUtils.byteArrayToHexString(hashes.get(0)));
        } else {
            this.hashType = new ArrayList<>(Collections.singletonList(hashType));
        }
        Output.printStatus("active", "input.txt", this.hashType.get(0), mode);
    }

    public boolean isAllCracked() {
        return hashman.isAllCracked();
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}