package com.lazer.lab1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class DynamicPerfectHashTableBenchmark {

    private DynamicPerfectHashTable hashTable;
    private String[] testKeys;
    private static final int SIZE = 1_000_000;

    @Setup(Level.Trial)
    public void setup() {
        hashTable = new DynamicPerfectHashTable(SIZE);
        testKeys = new String[SIZE];
    }

    @Benchmark
    public void benchmarkBulkInsert() {
        for (int i = 0; i < SIZE; i++) {
            testKeys[i] = "Key" + i;
            hashTable.insert(testKeys[i]);
        }
    }

    @Benchmark
    public void benchmarkBulkSearch() {
        for (int i = 0; i < 100_000; i++) {
            if (testKeys[i] != null) {
                hashTable.contains(testKeys[i]);
                testKeys[i] = null;
            }
        }
    }

    @Benchmark
    public void benchmarkBulkRemove() {
        for (int i = 101000; i < 201000; i++) {
            if (testKeys[i] != null) {
                hashTable.remove(testKeys[i]);
                testKeys[i] = null;
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DynamicPerfectHashTableBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(0)
                .measurementIterations(30)
                .build();

        new Runner(opt).run();
    }
}
