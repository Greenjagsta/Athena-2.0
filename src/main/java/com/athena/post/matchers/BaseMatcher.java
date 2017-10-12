package com.athena.post.matchers;

public abstract class BaseMatcher {

    public abstract void match(byte[] plain);

    public abstract boolean hasMatch();

    public abstract String getPattern();
}