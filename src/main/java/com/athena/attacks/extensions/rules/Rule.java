package com.athena.attacks.extensions.rules;

import java.util.*;

public enum Rule {
    PASSTHROUGH((byte) 58) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            return candidates;
        }
    },
    APPEND((byte) 36) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();
            System.out.println("candidate list size: " + candidates.size());

            for (byte[] candidate : candidates) {
                System.out.println("candidate: " + new String(candidate));
                byte[] result = new byte[candidate.length + 1];
                System.arraycopy(candidate, 0, result, 0, candidate.length);
                result[result.length - 1] = operator;
                System.out.println("candidate result: " + new String(result));
                resultList.add(result);
            }
            return resultList;
        }
    },
    PREPEND((byte) 94) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();

            for (byte[] candidate : candidates) {
                byte[] result = new byte[candidate.length + 1];
                System.arraycopy(candidate, 0, result, 1, candidate.length);
                result[0] = operator;
                resultList.add(result);
            }
            return resultList;
        }
    },
    LOWERCASE((byte) 108) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();

            for (byte[] candidate : candidates) {
                for (int i = 0; i < candidate.length; i++) {
                    if (candidate[i] >= 65 && candidate[i] <= 90) {
                        candidate[i] = (byte) (candidate[i] + 32);
                    }
                }
                resultList.add(candidate);
            }
            return resultList;
        }
    },
    UPPERCASE((byte) 117) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();

            for (byte[] candidate : candidates) {
                for (int i = 0; i < candidate.length; i++) {
                    if (candidate[i] >= 97 && candidate[i] <= 122) {
                        candidate[i] = (byte) (candidate[i] - 32);
                    }
                }
                resultList.add(candidate);
            }
            return resultList;
        }
    },
    CAPITALISE((byte) 99) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();

            for (byte[] candidate : candidates) {
                if (candidate[0] >= 97 && candidate[0] <= 122) {
                    candidate[0] = (byte) (candidate[0] - 32);
                }
                for (int i = 1; i < candidate.length; i++) {
                    if (candidate[i] >= 65 && candidate[i] <= 90) {
                        candidate[i] = (byte) (candidate[i] + 32);
                    }
                }
                resultList.add(candidate);
            }
            return resultList;
        }
    },
    INVERSE_CAPITALISE((byte) 67) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();

            for (byte[] candidate : candidates) {
                if (candidate[0] >= 65 && candidate[0] <= 90) {
                    candidate[0] = (byte) (candidate[0] + 32);
                }
                for (int i = 1; i < candidate.length; i++) {
                    if (candidate[i] >= 97 && candidate[i] <= 122) {
                        candidate[i] = (byte) (candidate[i] - 32);
                    }
                }
                resultList.add(candidate);
            }
            return resultList;
        }
    },
    REVERSE((byte) 114) {
        @Override
        public List<byte[]> apply(List<byte[]> candidates, byte operator) {
            resultList.clear();

            for (byte[] candidate : candidates) {
                int length = candidate.length;
                byte[] result = new byte[length];

                for (int i = length; i <= 0; i--) {
                    result[length - i] = candidate[i];
                }
                resultList.add(result);
            }
            return resultList;
        }
    };

    private final byte identifier;

    private static List<byte[]> resultList = new ArrayList<>();
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

    public abstract List<byte[]> apply(List<byte[]> candidates, byte operator);
}