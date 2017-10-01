package com.athena.attacks.extensions.rules;

import com.athena.utils.ArrayUtils;
import com.athena.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RulesProcessor {
    private ArrayList<byte[]> processedCandidates;
    private ArrayList<byte[]> rules;
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
            for (byte[] rule : rules) {
                processedCandidates.addAll(Rule.getRule(rule[0]).apply(candidate, rule[1]));
            }
        }
        return processedCandidates;
    }

    public boolean isRules() {
        return !rules.isEmpty();
    }

    private void initRules(String[] rulesArray) {
        try {
            for (String ruleString : rulesArray) {
                if (new File(ruleString).exists()) {
                    for (byte[] fileBuffer : FileUtils.getFileChunk(new File(ruleString))) {
                        for (byte[] rule : ArrayUtils.formatFileBytes(fileBuffer)) {
                            if (rule[0] != 35) {
                                rules.add(rule);
                            }
                        }
                    }
                } else {
                    rules.add(ruleString.getBytes());
                }
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}