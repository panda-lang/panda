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

package org.panda_lang.panda.framework.design.interpreter.pattern;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.linear.LinearPattern;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
public class LinearPatternVsDescriptiveBenchmark {

    @Benchmark
    public void testLinear(Blackhole blackhole) {
    }

    @Benchmark
    public void testDescriptive(Blackhole blackhole) {
    }

    public static void main(String[] args) throws Exception {
    }

}
