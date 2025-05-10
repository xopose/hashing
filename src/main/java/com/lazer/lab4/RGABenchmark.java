package com.lazer.lab4;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class RGABenchmark {

    @Param({"100", "500", "1000", "5000", "10000"})
    private int size;

    private RGA rga;
    private List<String> insertedElementIds;

    @Setup(Level.Invocation)
    public void setUp() {
        rga = new RGA("benchmark");
        insertedElementIds = new ArrayList<>();
        String parent = "ROOT";

        for (int i = 0; i < size; i++) {
            InsertOperation op = rga.localInsert(parent, randomChar());
            insertedElementIds.add(op.elementId);
            parent = op.elementId;
        }
    }

    private char randomChar() {
        return (char) ('a' + (int) (Math.random() * 26));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(1)
    public void benchmarkInsert(Blackhole blackhole) {
        String parentId = insertedElementIds.get((int) (Math.random() * insertedElementIds.size()));
        InsertOperation op = rga.localInsert(parentId, randomChar());
        blackhole.consume(op);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(1)
    public void benchmarkDelete(Blackhole blackhole) {
        String elementId = insertedElementIds.get((int) (Math.random() * insertedElementIds.size()));
        DeleteOperation op = rga.localDelete(elementId);
        blackhole.consume(op);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(1)
    public void benchmarkGetText(Blackhole blackhole) {
        String text = rga.getText();
        blackhole.consume(text);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RGABenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}