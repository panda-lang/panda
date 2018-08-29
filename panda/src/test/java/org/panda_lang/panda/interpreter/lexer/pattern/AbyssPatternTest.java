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

package org.panda_lang.panda.interpreter.lexer.pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokenizedSource;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class AbyssPatternTest {

    private static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "test [;] source")
            .build();

    private static final TokenizedSource FULL_SOURCE = new PandaTokenizedSource(
            PandaTokenRepresentation.of(TokenType.UNKNOWN, "test"),
            PandaTokenRepresentation.of(Separators.SEMICOLON),
            PandaTokenRepresentation.of(TokenType.UNKNOWN, "source")
    );

    private static final TokenizedSource OPTIONAL_SOURCE = new PandaTokenizedSource(
            PandaTokenRepresentation.of(TokenType.UNKNOWN, "test"),
            PandaTokenRepresentation.of(TokenType.UNKNOWN, "source")
    );

    @Test
    public void testOptional() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(PATTERN.match(FULL_SOURCE)),
                () -> Assertions.assertNotNull(PATTERN.match(OPTIONAL_SOURCE))
        );
    }

}
