package com.athena.rules;

import com.athena.utils.ArrayUtils;
import com.athena.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class RulesProcessor {
    private ArrayList<byte[]> rules;

    public RulesProcessor(String[] rulesArray) {
        rules = new ArrayList<>();

        if (rulesArray == null) {
            return;
        }

        for (String ruleString : rulesArray) {
            if (new File(ruleString).exists()) {
                for (byte[] fileBuffer : FileUtils.getFileChunk(new File(ruleString))) {
                    rules.addAll(ArrayUtils.formatFileBytes(fileBuffer));
                }
            } else {
                rules.add(ruleString.getBytes());
            }
        }
    }
}