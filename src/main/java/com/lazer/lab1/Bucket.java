package com.lazer.lab1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Bucket implements Serializable {
    public int localDepth;
    public int capacity;
    public List<String> keys;

    public Bucket(int localDepth, int capacity) {
        this.localDepth = localDepth;
        this.capacity = capacity;
        this.keys = new ArrayList<>();
    }

    public boolean isFull() {
        return keys.size() >= capacity;
    }

    public void insert(String key) {
        if (!keys.contains(key)) keys.add(key);
    }

    public void remove(String key) {
        keys.remove(key);
    }

    public boolean contains(String key) {
        return keys.contains(key);
    }
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    public static Bucket loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Bucket) ois.readObject();
        }
    }
}
