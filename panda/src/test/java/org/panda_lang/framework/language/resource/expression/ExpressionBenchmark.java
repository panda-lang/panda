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

package org.panda_lang.framework.language.resource.expression;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressions;
import org.panda_lang.panda.util.BenchmarkUtils;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 2)
@SuppressWarnings("CanBeFinal")
public class ExpressionBenchmark extends ExpressionParserTestBootstrap {

    private static final Snippet SOURCE = PandaLexerUtils.convert(ExpressionBenchmark.class.getSimpleName(), "variable.toString().toString().toString().toString()");

    @Benchmark
    public void testParser(Configuration configuration, Blackhole blackhole) {
        blackhole.consume(configuration.expressionParser.parse(configuration.context, SOURCE));
    }

    @State(Scope.Thread)
    @SuppressWarnings("CanBeFinal")
    public static class Configuration {

        protected Context context;
        protected ExpressionParser expressionParser;

        @Setup(Level.Trial)
        public void setup() {
            this.expressionParser = new PandaExpressionParser(PandaExpressions.getExpressionSubparsers());
            this.context = prepareData();
        }

        public static void main(String[] args) throws Exception {
            BenchmarkUtils.run(ExpressionBenchmark.class);
        }

    }

}
