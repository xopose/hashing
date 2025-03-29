package com.lazer.lab2;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class BTreeBenchmark {
    private BTree bTree;

    @Setup(Level.Trial)
    public void setup() {
        bTree = new BTree(3);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void benchmarkInsert() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/lazer/lab2/fashion-mnist_test.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                int key = Integer.parseInt(parts[0]);
                bTree.insert(key);
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void benchmarkSearch() {
        int key = (int) (Math.random() * 10000);
        bTree.search(bTree.root, key);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void benchmarkDelete() {
        int key = (int) (Math.random() * 10000);
        bTree.delete(key);
    }

    public static void main(String[] args) throws Exception {
        String[] jmhArgs = new String[]{"-i", "3", "-wi", "0", "-wf", "1", "-t", "1", BTreeBenchmark.class.getName()};
        Main.main(jmhArgs);
    }
}
