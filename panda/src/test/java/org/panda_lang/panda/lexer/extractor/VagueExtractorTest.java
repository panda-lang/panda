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

package org.panda_lang.panda.lexer.extractor;

import org.junit.Assert;
import org.junit.Test;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.vague.VagueExtractor;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.vague.VagueResult;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.defaults.Separator;
import org.panda_lang.panda.language.interpreter.PandaSyntax;
import org.panda_lang.panda.language.interpreter.tokens.Operators;
import org.panda_lang.panda.language.interpreter.tokens.Separators;

public class VagueExtractorTest {

    private static final VagueExtractor EXTRACTOR = new VagueExtractor(new Separator[] {
            Separators.LEFT_PARENTHESIS_DELIMITER,
            Separators.RIGHT_PARENTHESIS_DELIMITER
    }, new Token[] {
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION });

    @Test
    public void testVagueExtractor() {
        Lexer lexer = new PandaLexer(PandaSyntax.getInstance(), "(new Integer(5).intValue() + 3)");

        TokenizedSource source = lexer.convert();
        VagueResult result = EXTRACTOR.extract(source);

        Assert.assertEquals(true, result.isSucceeded());
        Assert.assertEquals(5, result.size());
        Assert.assertEquals("(", result.get(0));
        Assert.assertEquals("+", result.get(2));
        Assert.assertEquals("3", result.get(3));
        Assert.assertEquals(")", result.get(4));
    }

}
