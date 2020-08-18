/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package org.panda_lang.panda.examples.performance;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;
import org.panda_lang.panda.util.BenchmarkUtils;
import org.panda_lang.panda.util.PandaUtils;

import java.util.concurrent.TimeUnit;

/**
 * Just a preview of approximate performance to check some basic behaviours
 *
 * Bootstrap :: Reflection based injector
 * CurrentTestBenchmark.currentTest  thrpt    2  0,019          ops/ms
 *
 * Bootstrap :: Generated method injector
 * CurrentTestBenchmark.currentTest  thrpt    2  0,025          ops/ms
 *
 * Cached Panda instance
 * CurrentTestBenchmark.currentTest  thrpt    2  0,027          ops/ms
 *
 * Java 14
 * CurrentTestBenchmark.currentTest  thrpt    2  0,033          ops/ms
 *
 */
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CurrentTestBenchmark {

    @Benchmark
    public Object currentTest() {
        return PandaUtils.load("./examples/tests", "./examples/tests/current_test.panda").getValue().launch();
    }

    public static void main(String[] args) throws RunnerException {
        BenchmarkUtils.run(CurrentTestBenchmark.class);
    }

}
