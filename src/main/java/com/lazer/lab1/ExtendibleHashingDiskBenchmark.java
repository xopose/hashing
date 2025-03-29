package com.lazer.lab1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class ExtendibleHashingDiskBenchmark {

    private static final int SIZE = 10000;
    private static final int OPERATIONS = 100;
    private static final Random rand = new Random();
    private String[] testKeys;
    private ExtendibleHashing hashTable;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        clearBuckets();

        // Создание таблицы
        hashTable = new ExtendibleHashing(1000);
        testKeys = new String[SIZE];

        for (int i = 0; i < SIZE; i++) {
            testKeys[i] = "Key" + i;
        }
    }

    private void clearBuckets() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("bucket_") && name.endsWith(".dat"));
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Benchmark
    public void benchmarkBulkInsert() throws IOException, ClassNotFoundException {
        for (String key : testKeys) {
            hashTable.insert(key);
        }
    }

    @Benchmark
    public void benchmarkBulkSearch() throws IOException, ClassNotFoundException {
        for (int i = 0; i < OPERATIONS; i++) {
            hashTable.search(testKeys[rand.nextInt(SIZE)]);
        }
    }

    @Benchmark
    public void benchmarkBulkDelete() throws IOException, ClassNotFoundException {
        for (int i = 0; i < OPERATIONS; i++) {
            hashTable.delete(testKeys[rand.nextInt(SIZE)]);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ExtendibleHashingDiskBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(0)
                .measurementIterations(30)
                .build();

        new Runner(opt).run();
    }
}
