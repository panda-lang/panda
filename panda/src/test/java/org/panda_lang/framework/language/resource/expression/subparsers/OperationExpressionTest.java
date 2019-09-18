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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.language.resource.expression.PandaExpressionUtils;
import org.panda_lang.framework.language.resource.expression.ExpressionContextUtils;
import org.panda_lang.utilities.commons.TimeUtils;

import java.util.HashMap;

class OperationExpressionTest {

    private static final ExpressionParser PARSER = new PandaExpressionParser(PandaExpressionUtils.collectSubparsers());

    @Test
    void testMathOperation() {
        Expression expression = PARSER.parse(ExpressionContextUtils.createFakeContext((context -> new HashMap<>())), PandaLexerUtils.convert("1 + 2")).getExpression();
        Assertions.assertEquals((Object) 3, expression.evaluate(null, null));
    }

    @Test
    void test100M() {
        Expression expression = PARSER.parse(ExpressionContextUtils.createFakeContext((context -> new HashMap<>())), PandaLexerUtils.convert("1 + 1")).getExpression();
        long time = System.nanoTime();

        for (int times = 0; times < 100_000_000; times++) {
            expression.evaluate(null, null);
        }

        time = System.nanoTime() - time;
        System.out.println("Time: " + TimeUtils.toMilliseconds(time));
    }

}
