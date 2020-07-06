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

package org.panda_lang.utilities.inject;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.panda_lang.utilities.BenchmarkRunner;
import org.panda_lang.utilities.commons.ReflectionUtils;
import org.panda_lang.utilities.inject.annotations.Inject;

import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DependencyInjectionBenchmark {

    public static class Entity {
        private int points;
        @Inject
        public Integer bump() { return ++points; }
    }

    @Benchmark
    public Integer raw(DIState state) {
        return state.entity.bump();
    }

    @Benchmark
    public final Integer di(DIState state) throws Throwable {
        return state.bump.invoke(state.entity);
    }

    @State(Scope.Thread)
    public static class DIState {

        private Entity entity;
        private GeneratedMethodInjector bump;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            this.entity = new Entity();
            this.bump = DependencyInjection.createInjector().forGeneratedMethod(ReflectionUtils.getMethod(Entity.class, "bump").get());
        }

    }

    public static void main(String[] args) throws Exception {
        BenchmarkRunner.run(DependencyInjectionBenchmark.class);
    }

}
