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
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Optional;

class LinearPatternTest {

    @Test
    void testCompile() {
        Assertions.assertNotNull((LinearPattern.compile("unit")));

        Throwable linearPatternException = Assertions.assertThrows(LinearPatternException.class, () -> LinearPattern.compile(""));
        Assertions.assertEquals("Cannot compile the pattern", linearPatternException.getMessage());
    }

    @Test
    void testElements() {
        Assertions.assertEquals(1, sizeOf("unit"));
        Assertions.assertEquals(3, sizeOf("unit unit unit"));
        Assertions.assertEquals(2, sizeOf("unit identifier:unit"));

        Assertions.assertEquals(1, sizeOf("*"));
        Assertions.assertEquals(3, sizeOf("* * *"));
        Assertions.assertEquals(2, sizeOf("* identifier:*"));
    }

    private int sizeOf(String pattern) {
        return LinearPattern.compile(pattern).getElements().size();
    }

    @Test
    void testIsMatched() {
        LinearPattern simplePattern = LinearPattern.compile("test");

        Assertions.assertAll(
                () -> Assertions.assertTrue(simplePattern.match(of("test")).isMatched()),
                () -> Assertions.assertFalse(simplePattern.match(of("fail")).isMatched())
        );

        LinearPattern simpleWildcardPattern = LinearPattern.compile("*");

        Assertions.assertAll(
                () -> Assertions.assertTrue(simpleWildcardPattern.match(of("wildcard")).isMatched()),
                () -> Assertions.assertFalse(simpleWildcardPattern.match(of("wildcard wildcardFalse")).isMatched())
        );

        LinearPattern mixedPattern = LinearPattern.compile("unit wildcard:*");

        Assertions.assertAll(
                () -> Assertions.assertTrue(mixedPattern.match(of("unit random")).isMatched()),
                () -> Assertions.assertFalse(mixedPattern.match(of("random random")).isMatched())
        );
    }

    @Test
    void testIdentifiers() {
        Assertions.assertArrayEquals(ArrayUtils.of("a", "b", "c"), identifiersOf("a:unit b:unit c:unit", "unit unit unit"));
        Assertions.assertArrayEquals(ArrayUtils.of("a", "b", "c"), identifiersOf("a:unit b:* c:unit", "unit random unit"));
    }

    private String[] identifiersOf(String pattern, String source) {
        return LinearPattern.compile(pattern).match(of(source)).getIdentifiers().toArray(StringUtils.EMPTY_ARRAY);
    }

    @Test
    void testWildcards() {
        Optional<TokenRepresentation> wildcardValue = LinearPattern.compile("wildcard:*")
                .match(of("random"))
                .getWildcard("wildcard");

        Assertions.assertTrue(wildcardValue.isPresent());
        Assertions.assertEquals("random", wildcardValue.get().getValue());
    }

    @Test
    void testIsOptional() {
        Assertions.assertTrue(LinearPattern.compile("&id:unit").getElements().get(0).isOptional());
        Assertions.assertFalse(LinearPattern.compile("id:unit").getElements().get(0).isOptional());
    }

    @Test
    void testOptional() {
        LinearPattern pattern = LinearPattern.compile("&opt:unit second:test");

        Assertions.assertTrue(pattern.match(of("test")).isMatched());
        Assertions.assertTrue(pattern.match(of("unit test")).isMatched());
        Assertions.assertFalse(pattern.match(of("unit")).isMatched());
    }

    private static Snippet of(String source) {
        return PandaLexerUtils.convert(source);
    }

}