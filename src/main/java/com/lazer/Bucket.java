package com.lazer;

import java.util.ArrayList;
import java.util.List;

class Bucket {
    int localDepth;
    int capacity;
    List<String> keys;

    public Bucket(int localDepth, int capacity) {
        this.localDepth = localDepth;
        this.capacity = capacity;
        this.keys = new ArrayList<>();
    }

    public boolean isFull() {
        return keys.size() >= capacity;
    }

    public boolean contains(String key) {
        return keys.contains(key);
    }

    public void insert(String key) {
        if (!isFull()) {
            keys.add(key);
        }
    }

    public void remove(String key) {
        keys.remove(key);
    }
}
