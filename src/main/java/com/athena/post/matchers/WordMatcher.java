package com.athena.post.matchers;

public class WordMatcher extends BaseMatcher {
    private String pattern;
    private boolean hasMatch = false;

    public WordMatcher() {

    }

    @Override
    public void match(byte[] plain) {

    }

    @Override
    public boolean hasMatch() {
        return hasMatch;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}