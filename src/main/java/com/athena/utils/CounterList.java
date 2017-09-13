package com.athena.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CounterList<T> {
    private final List<List<T>> elements;

    public CounterList() {
        elements = new ArrayList<>();
    }

    public CounterList(List<List<T>> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }

    public void add(List<T> element) {
        elements.add(element);
    }

    public List<T> get(int index) {
        List<T> result = new ArrayList<>();
        for (int i = elements.size() - 1; i >= 0; i--) {
            List<T> counter = elements.get(i);
            int counterSize = counter.size();
            result.add(counter.get(index % counterSize));
            index /= counterSize;
        }
        Collections.reverse(result);
        return result;
    }

    public int size() {
        int result = 1;
        for (List<T> next : elements) {
            result *= next.size();
        }
        return result;
    }

    public void clear() {
        elements.clear();
    }
}