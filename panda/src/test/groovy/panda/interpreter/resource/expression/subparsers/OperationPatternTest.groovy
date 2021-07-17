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
import panda.interpreter.lexer.PandaLexer
import panda.interpreter.resource.syntax.PandaSyntax
import panda.interpreter.resource.syntax.operator.Operators
import panda.interpreter.resource.syntax.separator.Separators
import panda.interpreter.source.PandaSource
import panda.interpreter.source.Source
import panda.interpreter.syntax.expressions.subparsers.operation.pattern.OperationPattern
import panda.interpreter.syntax.expressions.subparsers.operation.pattern.OperationPatternResult
import panda.interpreter.token.Snippet
import panda.interpreter.token.Token

import static org.junit.jupiter.api.Assertions.assertAll
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class OperationPatternTest {

    private static final Source SOURCE = new PandaSource(null, OperationPatternTest.class, "(new Integer(5).intValue() + 3) + 2")

    private static final OperationPattern EXTRACTOR = new OperationPattern(Separators.getOpeningSeparators(), new Token[] {
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION
    })

    @Test
    void testVagueExtractor() {
        Snippet snippet = PandaLexer.of(new PandaSyntax())
                .build()
                .convert(SOURCE)

        OperationPatternResult result = EXTRACTOR.extract(snippet)

        assertNotNull(result)
        assertTrue(result.isMatched())
        assertEquals(3, result.size())

        assertAll(
                () -> assertEquals("( new Integer ( 5 ) . intValue ( ) + 3 )", result.get(0)),
                () -> assertEquals("+", result.get(1)),
                () -> assertEquals("2", result.get(2))
        )
    }

}
