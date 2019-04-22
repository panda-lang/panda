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

package org.panda_lang.panda.framework.design.interpreter.pattern.linear;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;

class LinearPatternTest {

    @Test
    void testElements() {
        Assertions.assertEquals(1, LinearPattern.compile("test").getElements().size());
        Assertions.assertEquals(3, LinearPattern.compile("test test test").getElements().size());
    }

    @Test
    void testIsMatched() {
        LinearPattern simplePattern = LinearPattern.compile("test");

        Assertions.assertAll(
                () -> Assertions.assertTrue(simplePattern.match(of("test")).isMatched()),
                () -> Assertions.assertFalse(simplePattern.match(of("fail")).isMatched())
        );
    }

    private static Snippet of(String source) {
        return PandaLexerUtils.convert(source);
    }

}