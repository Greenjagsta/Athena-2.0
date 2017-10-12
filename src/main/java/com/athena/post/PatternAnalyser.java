package com.athena.post;

import com.athena.post.matchers.Matcher;

import java.util.ArrayList;
import java.util.List;

public class PatternAnalyser {
    private List<byte[]> plains;
    private List<String> patterns;

    public PatternAnalyser(List<byte[]> plains) {
        this.plains = plains;
        this.patterns = new ArrayList<>();
    }

    public void analyse() {
        Matcher matcher = new Matcher();

        for (byte[] plain : plains) {
            patterns.add(matcher.match(plain));
        }
    }
}
