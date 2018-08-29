/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.interpreter.lexer.extractor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.pattern.vague.VagueExtractor;
import org.panda_lang.panda.framework.language.interpreter.pattern.vague.VagueResult;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class VagueExtractorTest {

    private static final String SOURCE = "(new Integer(5).intValue() + 3)";

    private static final VagueExtractor EXTRACTOR = new VagueExtractor(new Separator[]{
            Separators.LEFT_PARENTHESIS_DELIMITER,
            Separators.RIGHT_PARENTHESIS_DELIMITER
    }, new Token[]{
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION
    });

    @Test
    public void testVagueExtractor() {
        Source source = new PandaSource(VagueExtractorTest.class, SOURCE);

        Lexer lexer = new PandaLexer(PandaSyntax.getInstance(), source);
        TokenizedSource tokenizedSource = lexer.convert();

        VagueResult result = EXTRACTOR.extract(tokenizedSource);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isSucceeded());
        Assertions.assertEquals(5, result.size());

        Assertions.assertAll(
                () -> Assertions.assertEquals("(", result.get(0)),
                () -> Assertions.assertEquals("+", result.get(2)),
                () -> Assertions.assertEquals("3", result.get(3)),
                () -> Assertions.assertEquals(")", result.get(4))
        );
    }

}
