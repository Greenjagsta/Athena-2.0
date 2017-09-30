package com.athena.attacks;

import com.athena.attacks.extensions.HashManager;

import java.util.ArrayList;

public class BruteForce extends Attack {
    public BruteForce(ArrayList<byte[]> hashes, int hashType, int mode) {
        super.setHashType(hashType, hashes);
        super.setHashman(new HashManager(hashes));
        super.initDigestInstance();
//        super.setMode();
    }

    @Override
    public void attack() {

    }

    public ArrayList<byte[]> getNextCandidates() {
        return null;
    }
}