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

package org.panda_lang.framework.language.resource.expression.subparsers;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.language.resource.expression.PandaExpressionUtils;
import org.panda_lang.framework.language.resource.expression.ExpressionContextUtils;
import org.panda_lang.panda.util.BenchmarkUtils;

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
public class OperationExpressionBenchmark {

    private Expression expression;
    private Number integer;
    private Number integer2;

    @Setup
    public void setup() {
        ExpressionParser parser = new PandaExpressionParser(PandaExpressionUtils.collectSubparsers());
        this.expression = parser.parse(ExpressionContextUtils.createFakeContext((context -> new HashMap<>())), PandaLexerUtils.convert("1 + 2")).getExpression();
        this.integer = 1;
        this.integer2 = 2;
    }

    @Benchmark
    public void testPandaOperation(Blackhole blackhole) {
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
