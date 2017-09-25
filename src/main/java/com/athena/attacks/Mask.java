package com.athena.attacks;

import com.athena.rules.RulesProcessor;
import com.athena.utils.*;
import com.athena.utils.enums.CharSet;
import com.athena.utils.enums.Mode;

import java.io.File;
import java.util.*;

public class Mask extends Attack {
    private CounterList<byte[]> candidateElements;
    private byte[] mask;
    private boolean increment;
    private boolean complexityUpdateRequired = true;
    private boolean validMask = false;

    public Mask(String mask, boolean increment, ArrayList<byte[]> hashes, int hashType, String[] rules) {
        super.setHashType(hashType, hashes);
        super.setHashman(new HashManager(hashes));
        super.setRulesProcessor(new RulesProcessor(rules));
        super.initDigestInstance();

        Output.printDetails("Active");

        //TODO check increment here and pass mask to other method parsing it to check validity and to reduce it
        if (increment) {
            validMask = validateMask();
        }

        this.mask = mask.getBytes();
        this.increment = increment;
        this.candidateElements = new CounterList<>();
        parseMask(mask);
    }

    @Override
    public void attack() {
        if (complexityUpdateRequired) {
            Output.updateComplexity(candidateElements.size());
            complexityUpdateRequired = false;
        }

        for (int i = 0; i < candidateElements.size(); i++) {
            if (!super.isAllCracked()) {
                super.checkAttempt(candidateElements.get(i));
            } else {
                return;
            }
        }
    }

    //TODO Add support for masks containing static chars
    private boolean validateMask() {
        byte comp = mask[1];

        for (int i = 1; i < mask.length; i += 2) {
            if (mask[i] != comp) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<byte[]> getNextCandidates() {
        return null;
    }

    private void parseMask(String mask) {
        boolean modifier = false;

        for (byte b : mask.getBytes()) {
            switch (b) {
                case 63:
                    modifier = true;
                default:
                    if (modifier) {
                        switch (b) {
                            case 108:
                                candidateElements.add(CharSet.LOWER_ALPHABETIC.getCharsList());
                                modifier = false;
                                break;
                            case 100:
                                candidateElements.add(CharSet.NUMERIC.getCharsList());
                                modifier = false;
                                break;
                            case 115:
                                candidateElements.add(CharSet.SPECIAL.getCharsList());
                                modifier = false;
                                break;
                            case 117:
                                candidateElements.add(CharSet.UPPER_ALPHABETIC.getCharsList());
                                modifier = false;
                                break;
                            default:
                                break;
                        }
                    } else {
                        candidateElements.add(Collections.singletonList(new byte[]{b}));
                    }
            }
        }
    }
}