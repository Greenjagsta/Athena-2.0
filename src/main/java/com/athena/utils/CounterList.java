package com.athena.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CounterList<T> {
    private final List<List<byte[]>> elements;
    private int size = 1;

    public CounterList() {
        this.elements = new ArrayList<>();
    }

    public CounterList(List<List<byte[]>> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }

    public void add(List<byte[]> element) {
        elements.add(element);
        size *= element.size();
    }

    public byte[] get(int index) {
        List<byte[]> result = new ArrayList<>();
        for (int i = elements.size() - 1; i >= 0; i--) {
            List<byte[]> counter = elements.get(i);
            int counterSize = counter.size();
            result.add(counter.get(index % counterSize));
            index /= counterSize;
        }
        Collections.reverse(result);
        return ArrayUtils.stripList(result);
    }

    public int size() {
        return size;
    }

    public void clear() {
        elements.clear();
        size = 1;
    }
}