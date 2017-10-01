package com.athena.attacks.extensions.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Rule {
    APPEND((byte) 36) {
        @Override
        public List<byte[]> apply(byte[] candidate, byte operator) {
            byte[] result = new byte[candidate.length + 1];
            System.arraycopy(candidate, 0, result, 0, candidate.length);
            result[result.length - 1] = operator;
            return Collections.singletonList(result);
        }
    },
    PREPEND((byte) 94) {
        @Override
        public List<byte[]> apply(byte[] candidate, byte operator) {
            byte[] result = new byte[candidate.length + 1];
            System.arraycopy(candidate, 0, result, 1, candidate.length);
            result[0] = operator;
            return Collections.singletonList(result);
        }
    };

    private final byte identifier;

    private static Map<Byte, Rule> idToRuleMapping;

    Rule(byte identifier) {
        this.identifier = identifier;
    }

    public static Rule getRule(byte b) {
        if (idToRuleMapping == null) {
            initMapping();
        }
        return idToRuleMapping.get(b);
    }

    private static void initMapping() {
        idToRuleMapping = new HashMap<>();
        for (Rule s : values()) {
            idToRuleMapping.put(s.identifier, s);
        }
    }

    public abstract List<byte[]> apply(byte[] candidate, byte operator);
}