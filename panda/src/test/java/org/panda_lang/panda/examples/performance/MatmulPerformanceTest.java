/*
 * Copyright (c) 2015-2019 Dzikoysk
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
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.panda_lang.panda.examples.Launcher;
import org.panda_lang.panda.framework.design.architecture.Application;

@State(Scope.Benchmark)
@Warmup(time = 1, iterations = 1)
@Fork(1)
public class MatmulPerformanceTest {

    private Application matmulApplication;

    @Setup
    public void setup() {
        this.matmulApplication = Launcher.interpret("performance", "matmul.panda");
    }

    @Benchmark
    public void benchmarkMatmulPanda() {
        matmulApplication.launch();
    }

    @Benchmark
    public void benchmarkMatmulJava() {
        MatmulJava.main();
    }

    public static void main(String[] args) throws Exception {
        new Runner(new OptionsBuilder().include(MatmulPerformanceTest.class.getName()).build()).run();
    }

}
