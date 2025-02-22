package com.lazer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExtendibleHashing {
    private int globalDepth;
    private int bucketCapacity;
    private List<Bucket> directory;

    public ExtendibleHashing(int bucketCapacity) {
        this.globalDepth = 1;
        this.bucketCapacity = bucketCapacity;
        this.directory = new ArrayList<>();

        directory.add(new Bucket(1, bucketCapacity));
        directory.add(new Bucket(1, bucketCapacity));
    }

    private int hash(String key) {
        return key.hashCode() & ((1 << globalDepth) - 1);
    }

    public void insert(String key) {
        int index = hash(key);
        Bucket bucket = directory.get(index);

        if (!bucket.isFull()) {
            bucket.insert(key);
            return;
        }

        if (bucket.localDepth == globalDepth) {
            expandDirectory();
        }

        splitBucket(index);
        insert(key);
    }

    private void expandDirectory() {
        globalDepth++;
        List<Bucket> newDirectory = new ArrayList<>(directory);

        for (int i = 0; i < directory.size(); i++) {
            newDirectory.add(directory.get(i));
        }

        directory = newDirectory;
    }

    private void splitBucket(int index) {
        Bucket oldBucket = directory.get(index);
        int localDepth = oldBucket.localDepth;
        int newLocalDepth = localDepth + 1;

        Bucket bucket0 = new Bucket(newLocalDepth, bucketCapacity);
        Bucket bucket1 = new Bucket(newLocalDepth, bucketCapacity);

        List<String> keys = new ArrayList<>(oldBucket.keys);
        oldBucket.keys.clear();

        for (String key : keys) {
            int newIndex = hash(key);
            if ((newIndex & (1 << localDepth)) == 0) {
                bucket0.insert(key);
            } else {
                bucket1.insert(key);
            }
        }

        for (int i = 0; i < directory.size(); i++) {
            if (directory.get(i) == oldBucket) {
                if ((i & (1 << localDepth)) == 0) {
                    directory.set(i, bucket0);
                } else {
                    directory.set(i, bucket1);
                }
            }
        }
    }

    public boolean search(String key) {
        int index = hash(key);
        return directory.get(index).contains(key);
    }

    public void delete(String key) {
        int index = hash(key);
        directory.get(index).remove(key);
    }

    public void print() {
        System.out.println("Global Depth: " + globalDepth);
        Set<Bucket> seen = new HashSet<>();

        for (int i = 0; i < directory.size(); i++) {
            if (!seen.contains(directory.get(i))) {
                System.out.println("Bucket " + i + " (Local Depth: " + directory.get(i).localDepth + ") -> " + directory.get(i).keys);
                seen.add(directory.get(i));
            }
        }
    }
}