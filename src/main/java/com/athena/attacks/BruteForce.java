package com.athena.attacks;

import com.athena.utils.HashManager;

import java.util.ArrayList;

public class BruteForce extends Attack {
    public BruteForce(String hashes_filename, int hashType) {
        super.setHashType(hashType, hashes_filename);
        super.setHashman(new HashManager(hashes_filename));
    }

    @Override
    public void attack() {

    }

    public ArrayList<byte[]> getNextCandidates() {
        return null;
    }
}