/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package panda.examples.summary_example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;
import panda.examples.PandaTestSpecification;
import panda.interpreter.utils.BenchmarkUtils;

import java.util.concurrent.TimeUnit;

/**
 * Just a preview of approximate performance to check some basic behaviours
 *
 * Bootstrap :: Reflection based injector
 * SummaryExampleBenchmark.currentTest  thrpt    2  0,019          ops/ms
 *
 * Bootstrap :: Generated method injector
 * SummaryExampleBenchmark.currentTest  thrpt    2  0,025          ops/ms
 *
 * Cached Panda instance
 * SummaryExampleBenchmark.currentTest  thrpt    2  0,027          ops/ms
 *
 * Java 14
 * SummaryExampleBenchmark.currentTest  thrpt    2  0,033          ops/ms
 *
 * 0.2.0-alpha and Java 15
 * SummaryExampleBenchmark.currentTest  thrpt    2  0,035          ops/ms
 *
 * 0.2.1-alpha and Java 15
 * SummaryExampleBenchmark.currentTest  thrpt    2  0,040          ops/ms
 */
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SummaryExampleBenchmark extends PandaTestSpecification {

    @Benchmark
    public Object summaryExample() {
        return launch("/summary-example/", "panda.cdn");
    }

    public static void main(String[] args) throws RunnerException {
        BenchmarkUtils.run(SummaryExampleBenchmark.class);
    }

}
