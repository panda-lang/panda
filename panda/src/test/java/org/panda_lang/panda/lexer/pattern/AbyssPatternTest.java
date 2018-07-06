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

package org.panda_lang.panda.lexer.pattern;

import org.junit.jupiter.api.*;
import org.panda_lang.panda.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.interpreter.token.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.language.interpreter.*;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.*;

public class AbyssPatternTest {

    private final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "test [;] source")
            .build();

    private final TokenizedSource FULL_SOURCE = new PandaTokenizedSource(
            PandaTokenRepresentation.of(TokenType.UNKNOWN, "test"),
            PandaTokenRepresentation.of(Separators.SEMICOLON),
            PandaTokenRepresentation.of(TokenType.UNKNOWN, "source")
    );

    private final TokenizedSource OPTIONAL_SOURCE = new PandaTokenizedSource(
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
