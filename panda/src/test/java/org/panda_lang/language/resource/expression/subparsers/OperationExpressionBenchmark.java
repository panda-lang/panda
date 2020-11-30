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

package org.panda_lang.language.resource.expression.subparsers;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.language.interpreter.parser.PandaContextUtils;
import org.panda_lang.panda.language.resource.syntax.expressions.PandaExpressions;
import org.panda_lang.panda.util.BenchmarkUtils;
import org.panda_lang.panda.util.PandaUtils;

import java.util.HashMap;

/*
 * Benchmark (Java 8)                                Mode  Cnt          Score            Error  Units
 *  OperationExpressionBenchmark.testJavaOperation  thrpt    3  267526698,711 ± 1607535170,193  ops/s
 *  OperationExpressionBenchmark.testPandaOperation thrpt    3  111900453,779 ±    1374460,680  ops/s
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@SuppressWarnings("CanBeFinal")
public class OperationExpressionBenchmark {

    private Expression expression;

    @Setup
    public void setup() {
        ExpressionParser parser = new PandaExpressionParser(PandaExpressions.createExpressionSubparsers());
        Snippet source = PandaLexerUtils.convert(OperationExpressionBenchmark.class.getSimpleName(), "1 + 2");
        this.expression = parser.parse(PandaContextUtils.createStubContext(PandaUtils.defaultInstance(), (context -> new HashMap<>())), source).getExpression();
    }

    @Benchmark
    public void testPandaOperation(Blackhole blackhole) throws Exception {
        blackhole.consume(expression.evaluate(null, null));
    }

    @Benchmark
    public void testJavaOperation(Blackhole blackhole) {
        blackhole.consume(1 + 2);
    }

    public static void main(String[] args) throws Exception {
        BenchmarkUtils.run(OperationExpressionBenchmark.class);
    }

}
