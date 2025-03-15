package com.lazer;

import java.io.*;
import java.util.*;

public class ExtendibleHashing {

    private int globalDepth;
    private int bucketCapacity;
    private List<String> directory;
    private static int bucketCounter = 0;

    public ExtendibleHashing(int bucketCapacity) throws IOException {
        this.globalDepth = 1;
        this.bucketCapacity = bucketCapacity;
        this.directory = new ArrayList<>();

        String bucketFile1 = createBucketFile(1);
        String bucketFile2 = createBucketFile(1);

        directory.add(bucketFile1);
        directory.add(bucketFile2);
    }

    private String createBucketFile(int localDepth) throws IOException {
        String filename = "bucket_" + (bucketCounter++) + ".dat";
        Bucket bucket = new Bucket(localDepth, bucketCapacity);
        bucket.saveToFile(filename);
        return filename;
    }

    private int hash(String key) {
        return key.hashCode() & ((1 << globalDepth) - 1);
    }

    public void insert(String key) throws IOException, ClassNotFoundException {
        int index = hash(key);
        String bucketFile = directory.get(index);
        Bucket bucket = Bucket.loadFromFile(bucketFile);

        if (!bucket.isFull()) {
            bucket.insert(key);
            bucket.saveToFile(bucketFile);
            return;
        }

        if (bucket.localDepth == globalDepth) {
            expandDirectory();
        }

        splitBucket(index, bucket);
        insert(key);
    }

    private void expandDirectory() {
        globalDepth++;
        List<String> newDirectory = new ArrayList<>(directory);
        newDirectory.addAll(directory);
        directory = newDirectory;
    }

    private void splitBucket(int index, Bucket oldBucket) throws IOException {
        int localDepth = oldBucket.localDepth;
        int newLocalDepth = localDepth + 1;

        String bucketFile0 = createBucketFile(newLocalDepth);
        String bucketFile1 = createBucketFile(newLocalDepth);

        Bucket bucket0 = new Bucket(newLocalDepth, bucketCapacity);
        Bucket bucket1 = new Bucket(newLocalDepth, bucketCapacity);

        for (String key : oldBucket.keys) {
            int newIndex = hash(key);
            if ((newIndex & (1 << localDepth)) == 0) {
                bucket0.insert(key);
            } else {
                bucket1.insert(key);
            }
        }

        bucket0.saveToFile(bucketFile0);
        bucket1.saveToFile(bucketFile1);

        for (int i = 0; i < directory.size(); i++) {
            if (directory.get(i).equals(directory.get(index))) {
                if ((i & (1 << localDepth)) == 0) {
                    directory.set(i, bucketFile0);
                } else {
                    directory.set(i, bucketFile1);
                }
            }
        }
    }

    public boolean search(String key) throws IOException, ClassNotFoundException {
        int index = hash(key);
        Bucket bucket = Bucket.loadFromFile(directory.get(index));
        return bucket.contains(key);
    }

    public void delete(String key) throws IOException, ClassNotFoundException {
        int index = hash(key);
        String bucketFile = directory.get(index);
        Bucket bucket = Bucket.loadFromFile(bucketFile);
        bucket.remove(key);
        bucket.saveToFile(bucketFile);
    }

    public void print() throws IOException, ClassNotFoundException {
        System.out.println("Global Depth: " + globalDepth);
        Set<String> seen = new HashSet<>();

        for (int i = 0; i < directory.size(); i++) {
            String fileName = directory.get(i);
            if (!seen.contains(fileName)) {
                Bucket bucket = Bucket.loadFromFile(fileName);
                System.out.println("Bucket file: " + fileName + " (Local Depth: " + bucket.localDepth + ") -> " + bucket.keys);
                seen.add(fileName);
            }
        }
    }
}