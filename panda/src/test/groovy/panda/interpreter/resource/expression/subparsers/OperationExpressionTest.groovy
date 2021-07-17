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

package panda.interpreter.resource.expression.subparsers

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import panda.interpreter.architecture.expression.Expression
import panda.interpreter.lexer.PandaLexerUtils
import panda.interpreter.parser.expression.ExpressionParser
import panda.interpreter.parser.expression.PandaExpressionParser
import panda.interpreter.syntax.expressions.PandaExpressions
import panda.interpreter.token.Snippet
import panda.interpreter.utils.PandaContextUtils
import panda.interpreter.utils.PandaUtils
import panda.utilities.TimeUtils

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class OperationExpressionTest {

    private static final ExpressionParser PARSER = new PandaExpressionParser(PandaExpressions.createExpressionSubparsers())
    private static final Snippet SOURCE = PandaLexerUtils.convert(OperationExpressionTest.class.getSimpleName(), "1 + 2")

    @Test
    void testMathOperation() throws Exception {
        Expression expression = PARSER.parse(PandaContextUtils.createStubContext(PandaUtils.defaultInstance(), (context -> new HashMap<>())), SOURCE)
        assertEquals((Object) 3, expression.evaluate(null, null))
    }

    @Test
    void test100M() throws Exception {
        Expression expression = PARSER.parse(PandaContextUtils.createStubContext(PandaUtils.defaultInstance(), (context -> new HashMap<>())), SOURCE)
        long time = System.nanoTime()

        for (int times = 0; times < 100_000_000; times++) {
            expression.evaluate(null, null)
        }

        time = System.nanoTime() - time;
        System.out.println("Time: " + TimeUtils.toMilliseconds(time))
    }

}
