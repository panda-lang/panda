/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.resource.expression.subparsers

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.panda_lang.framework.interpreter.lexer.PandaLexer
import org.panda_lang.framework.interpreter.source.PandaSource
import org.panda_lang.framework.interpreter.source.Source
import org.panda_lang.framework.interpreter.token.Snippet
import org.panda_lang.framework.interpreter.token.Token
import org.panda_lang.framework.resource.syntax.PandaSyntax
import org.panda_lang.framework.resource.syntax.operator.Operators
import org.panda_lang.framework.resource.syntax.separator.Separators
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.pattern.OperationPattern
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.pattern.OperationPatternResult

@CompileStatic
final class OperationPatternTest {

    private static final Source SOURCE = new PandaSource(OperationPatternTest.class, "(new Integer(5).intValue() + 3) + 2");

    private static final OperationPattern EXTRACTOR = new OperationPattern(Separators.getOpeningSeparators(), new Token[] {
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION
    });

    @Test
    void testVagueExtractor() {
        Snippet snippet = PandaLexer.of(new PandaSyntax())
                .build()
                .convert(SOURCE);

        OperationPatternResult result = EXTRACTOR.extract(snippet);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isMatched());
        Assertions.assertEquals(3, result.size());

        Assertions.assertAll(
                () -> Assertions.assertEquals("( new Integer ( 5 ) . intValue ( ) + 3 )", result.get(0)),
                () -> Assertions.assertEquals("+", result.get(1)),
                () -> Assertions.assertEquals("2", result.get(2))
        );
    }

}
