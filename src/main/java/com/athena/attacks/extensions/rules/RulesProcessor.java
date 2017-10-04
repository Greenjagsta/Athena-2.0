package com.athena.attacks.extensions.rules;

import com.athena.utils.ArrayUtils;
import com.athena.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RulesProcessor {
    private List<byte[]> candidateToAdd;
    private List<byte[]> processedCandidates;
    private List<List<byte[]>> rules;
    private File ruleFile;

    public RulesProcessor(String[] rulesArray) {
        this.processedCandidates = new ArrayList<>();
        this.rules = new ArrayList<>();

        if (rulesArray == null) {
            return;
        }

        initRules(rulesArray);
    }

    public List<byte[]> apply(List<byte[]> candidates) {
        processedCandidates.clear();
        for (byte[] candidate : candidates) {
            for (List<byte[]> ruleLine : rules) {
                System.out.println("\nNew RuleLine Loop!");
                candidateToAdd = Collections.singletonList(candidate);
                for (byte[] rule : ruleLine) {
                    System.out.println("\nNew Loop!");
                    System.out.println("rule: " + new String(rule));
                    if (rule.length > 1) {
                        System.out.println("before apply: " + new String(candidateToAdd.get(0)));
                        candidateToAdd = Rule.getRule(rule[0]).apply(candidateToAdd, rule[1]);
                        System.out.println("after apply: " + new String(candidateToAdd.get(0)));
                    } else {
                        candidateToAdd = Rule.getRule(rule[0]).apply(candidateToAdd, (byte) 0);
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