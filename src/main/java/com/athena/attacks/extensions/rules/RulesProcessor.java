package com.athena.attacks.extensions.rules;

import com.athena.utils.ArrayUtils;
import com.athena.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RulesProcessor {
    private List<byte[]> candidateToAdd;
    private List<byte[]> tempCandidateToAdd;
    private List<byte[]> processedCandidates;
    private List<List<byte[]>> rules;

    public RulesProcessor(String[] rulesArray) {
        this.processedCandidates = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.tempCandidateToAdd = new ArrayList<>();

        if (rulesArray == null) {
            return;
        }

        initRules(rulesArray);
    }

    public List<byte[]> apply(List<byte[]> candidates) {
        processedCandidates.clear();

        for (byte[] candidate : candidates) {
            for (List<byte[]> ruleLine : rules) {
                candidateToAdd = Collections.singletonList(candidate);
                for (byte[] rule : ruleLine) {
                    tempCandidateToAdd.clear();
                    tempCandidateToAdd.addAll(candidateToAdd);

                    if (rule.length > 1) {
                        candidateToAdd = Rule.getRule(rule[0]).apply(tempCandidateToAdd, rule[1]);
                    } else {
                        candidateToAdd = Rule.getRule(rule[0]).apply(tempCandidateToAdd, (byte) 0);
                    }
                }
                processedCandidates.addAll(candidateToAdd);
            }
        }
        return processedCandidates;
    }

    public boolean isRules() {
        return !rules.isEmpty();
    }

    private List<byte[]> parseRule(byte[] rule) {
        List<byte[]> result = new ArrayList<>();

        for (int i = 0; i < rule.length; i++) {
            switch (rule[i]) {
                case 58: case 108: case 117: case 99: case 67: case 114: case 100:
                    result.add(new byte[]{rule[i]});
                    break;

                case 36: case 94:
                    result.add(new byte[]{rule[i], rule[i + 1]});
                    i++;
                    break;

                default:
                    break;
            }
        }
        return result;
    }

    private void initRules(String[] rulesArray) {
        try {
            for (String ruleString : rulesArray) {
                if (new File(ruleString).exists()) {
                    for (byte[] fileBuffer : FileUtils.getFileChunk(new File(ruleString))) {
                        for (byte[] rule : ArrayUtils.formatFileBytes(fileBuffer)) {
                            if (rule[0] != 35) {
                                rules.add(parseRule(rule));
                            }
                        }
                    }
                } else {
                    rules.add(Collections.singletonList(ruleString.getBytes()));
                }
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}