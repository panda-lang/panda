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

package org.panda_lang.panda.interpreter.parser.implementation.general.expression.fixed;

import org.junit.jupiter.api.Assertions;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparsersLoader;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsers;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsersLoaderOld;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;

import java.util.ArrayList;

@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class ExpressionParserBenchmark {

    private static final Tokens LITERAL = PandaLexerUtils.convert("true");

    @Benchmark
    public void testLiteral(Configuration configuration, Blackhole blackhole) {
        blackhole.consume(configuration.expressionParser.parse(null, LITERAL));
    }

    @Benchmark
    public void testOldLiteral(Configuration configuration, Blackhole blackhole) {
        blackhole.consume(configuration.oldExpressionParser.parse(null, LITERAL));
    }

    @State(Scope.Thread)
    public static class Configuration {

        protected ExpressionParser expressionParser;
        protected ExpressionParserOld oldExpressionParser;

        @Setup(Level.Trial)
        public void setup() throws Exception {
            this.expressionParser = new ExpressionParser(new ExpressionSubparsersLoader().load());

            ParserData data = new PandaParserData();
            ExpressionSubparsers subparsers = new ExpressionSubparsers(new ArrayList<>());
            data.setComponent(PandaComponents.EXPRESSION, new ExpressionParserOld(null, subparsers));

            ExpressionSubparsersLoaderOld loader = new ExpressionSubparsersLoaderOld();
            ExpressionSubparsers loadedSubparsers = Assertions.assertDoesNotThrow(() -> loader.load(data));
            subparsers.merge(loadedSubparsers);

            this.oldExpressionParser = data.getComponent(PandaComponents.EXPRESSION);
        }

        public static void main(String[] args) throws Exception {
            Main.main(args);
        }

    }

}
