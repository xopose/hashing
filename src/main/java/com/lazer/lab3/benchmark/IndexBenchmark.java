package com.lazer.lab3.benchmark;
import com.lazer.lab3.component.Search;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class IndexBenchmark {
    private static final String BENCHMARK_INDEX_PATH = "./src/main/java/com/lazer/lab3/runtime_files";

    @Setup(Level.Invocation)
    public void setUp(BenchmarkParams params) throws IOException {
        FileUtils.deleteDirectory(new File(BENCHMARK_INDEX_PATH));
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void indexOneRow(Blackhole blackhole) {
        var searcher = new Search(BENCHMARK_INDEX_PATH, 1);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void indexOneHundredRows(Blackhole blackhole) {
        var searcher = new Search(BENCHMARK_INDEX_PATH, 100);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void indexFiveHundredRows(Blackhole blackhole) {
        var searcher = new Search(BENCHMARK_INDEX_PATH, 500);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void indexThousandRows(Blackhole blackhole) {
        var searcher = new Search(BENCHMARK_INDEX_PATH, 1_000);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void indexFiveThousandRows(Blackhole blackhole) {
        var searcher = new Search(BENCHMARK_INDEX_PATH, 5_000);
        blackhole.consume(searcher);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 10, time = 1)
    @Fork(value = 2, warmups = 1)
    public void indexTenThousandRows(Blackhole blackhole) {
        var searcher = new Search(BENCHMARK_INDEX_PATH, 10_000);
        blackhole.consume(searcher);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(IndexBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
