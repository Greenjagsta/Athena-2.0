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
import com.athena.rules.RulesProcessor;
import com.athena.utils.HashManager;
import com.athena.utils.Output;
import com.athena.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Attack {
    private final int hashMax = 1000000;
    private HashManager hashman;
    private RulesProcessor rulesProcessor;
    private ArrayList<Integer> hashType;
    private Object digestFunction;
    private Method digest;
    private double counter = 0;

    public abstract void attack();

    //TODO - change this to work with ArrayList<byte[]> so that rules can be processed in chunks (needs a change in CounterList structure
    void checkAttempt(List<byte[]> candidates) {
        for (byte[] candidate : candidates) {
            counter++;
            if (counter == hashMax) {
                Output.printDetails("Active");
                counter = 0;
            }
            byte[] candidateHash = getDigest(candidate);
            if (hashman.hashExists(candidateHash)) {
                hashman.setCracked(candidateHash, candidate);
                Output.updateRecovered();
            }
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

    void setRulesProcessor(RulesProcessor rulesProcessor) {
        this.rulesProcessor = rulesProcessor;
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
        Output.updateHashType(this.hashType.get(0));
    }

    boolean isAllCracked() {
        return hashman.isAllCracked();
    }
}