package com.athena.post.matchers;

public class Matcher {
    private StringBuilder pattern;
    private WordMatcher wordMatcher;

    private int lower;
    private int upper;

    public Matcher() {
        pattern = new StringBuilder();
        wordMatcher = new WordMatcher();
    }

    public String match(byte[] plain) {
        lower = 0;
        upper = plain.length;
        pattern.setLength(0);

        while (lower != upper) {
            byte[] segment = new byte[upper - lower];
            System.arraycopy(plain, lower, segment, 0, upper - lower);

            wordMatcher.match(segment);
            if (wordMatcher.hasMatch()) {
                pattern.append(wordMatcher.getPattern());
            }
        }
        return pattern.toString();
    }
}