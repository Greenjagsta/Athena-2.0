package com.athena.attacks.extensions;

import com.athena.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CounterList<T> {
    private final int CHUNK_SIZE = 100000;
    private final List<byte[]> chunk = new ArrayList<>();
    private final List<List<byte[]>> elements;
    private int size = 1;
    private int lower;
    private int upper;

    public CounterList() {
        this.elements = new ArrayList<>();
        this.lower = 0;
        this.upper = 0;
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

    public List<byte[]> getChunk() {
        chunk.clear();
        this.upper = Math.min(upper + CHUNK_SIZE, size);

        for (int i = lower; i < upper; i++) {
            int index = i;
            List<byte[]> result = new ArrayList<>();
            for (int j = elements.size() - 1; j >= 0; j--) {
                List<byte[]> counter = elements.get(j);
                int counterSize = counter.size();
                result.add(counter.get(index % counterSize));
                index /= counterSize;
            }
            Collections.reverse(result);
            chunk.add(ArrayUtils.stripList(result));
        }
        lower = upper;
        return chunk;
    }

    public boolean hasMoreChunks() {
        return this.lower != this.size;
    }

    public int size() {
        return size;
    }

    public void clear() {
        elements.clear();
        this.size = 1;
    }
}