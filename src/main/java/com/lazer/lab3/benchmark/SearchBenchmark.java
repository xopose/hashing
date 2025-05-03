package com.lazer.lab3.benchmark;
import com.lazer.lab3.component.Search;

import com.lazer.lab3.request.SearchRequest;
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
public class SearchBenchmark {


        private static final String BENCHMARK_INDEX_PATH = "./src/main/java/com/lazer/lab3/runtime_files";

        private String bench;
        private Search search;

        @Setup(Level.Trial)
        public void setUp(BenchmarkParams params) throws IOException {
            bench = params.getBenchmark();
            FileUtils.deleteDirectory(new File(BENCHMARK_INDEX_PATH));
            if (bench.contains("TenThousand")) {
                search = new Search(BENCHMARK_INDEX_PATH, 10_000);
            } else if (bench.contains("FiveThousand")) {
                search = new Search(BENCHMARK_INDEX_PATH, 5_000);
            } else if (bench.contains("Thousand")) {
                search = new Search(BENCHMARK_INDEX_PATH, 1_000);
            } else if (bench.contains("FiveHundred")) {
                search = new Search(BENCHMARK_INDEX_PATH, 500);
            } else if (bench.contains("Hundred")) {
                search = new Search(BENCHMARK_INDEX_PATH, 100);
            } else {
                search = new Search(BENCHMARK_INDEX_PATH, 1);
            }
        }

        @Benchmark
        @BenchmarkMode(Mode.Throughput)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 3, time = 1)
        @Measurement(iterations = 10, time = 1)
        @Fork(value = 2, warmups = 1)
        public void luceneSearchWithOneRow(Blackhole blackhole) {
            var result = search.search(new SearchRequest("winner:\"white\""));
            blackhole.consume(result);
        }

        @Benchmark
        @BenchmarkMode(Mode.AverageTime)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 3, time = 1)
        @Measurement(iterations = 10, time = 1)
        @Fork(value = 2, warmups = 1)
        public void luceneSearchWithOneHundredRows(Blackhole blackhole) {
            var result = search.search(new SearchRequest("winner:\"white\""));
            blackhole.consume(result);
        }

        @Benchmark
        @BenchmarkMode(Mode.Throughput)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 3, time = 1)
        @Measurement(iterations = 10, time = 1)
        @Fork(value = 2, warmups = 1)
        public void luceneSearchWithFiveHundredRows(Blackhole blackhole) {
            var result = search.search(new SearchRequest("winner:\"white\""));
            blackhole.consume(result);
        }

        @Benchmark
        @BenchmarkMode(Mode.Throughput)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 3, time = 1)
        @Measurement(iterations = 10, time = 1)
        @Fork(value = 2, warmups = 1)
        public void luceneSearchWithThousandRows(Blackhole blackhole) {
            var result = search.search(new SearchRequest("winner:\"white\""));
            blackhole.consume(result);
        }

        @Benchmark
        @BenchmarkMode(Mode.Throughput)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 3, time = 1)
        @Measurement(iterations = 10, time = 1)
        @Fork(value = 2, warmups = 1)
        public void luceneSearchWithFiveThousandRows(Blackhole blackhole) {
            var result = search.search(new SearchRequest("winner:\"white\""));
            blackhole.consume(result);
        }

        @Benchmark
        @BenchmarkMode(Mode.Throughput)
        @OutputTimeUnit(TimeUnit.MILLISECONDS)
        @Warmup(iterations = 3, time = 1)
        @Measurement(iterations = 10, time = 1)
        @Fork(value = 2, warmups = 1)
        public void luceneSearchWithTenThousandRows(Blackhole blackhole) {
            var result = search.search(new SearchRequest("winner:\"white\""));
            blackhole.consume(result);
        }

        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(SearchBenchmark.class.getSimpleName())
                    .build();
            new Runner(opt).run();
        }
}
