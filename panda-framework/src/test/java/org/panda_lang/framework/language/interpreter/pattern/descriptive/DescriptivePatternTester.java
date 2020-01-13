/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.descriptive;

import org.junit.jupiter.api.Assertions;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.framework.language.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.framework.language.interpreter.source.PandaSource;
import org.panda_lang.framework.language.resource.syntax.PandaSyntax;

final class DescriptivePatternTester {

    private static final PandaSyntax SYNTAX = new PandaSyntax();

    protected static void test(String patternContent, String source, Wildcard... expected) {
        DescriptivePattern pattern = DescriptivePattern.builder()
                .compile(patternContent)
                .build();

        LexicalPatternElement content = pattern.getPatternContent();
        Assertions.assertNotNull(content);

        Snippet tokenizedSource = PandaLexer.of(SYNTAX).build().convert(new PandaSource("Test", source));
        ExtractorResult result = pattern.extract(new PandaContext(), tokenizedSource);
        Assertions.assertNotNull(result);

        if (result.hasErrorMessage()) {
            System.out.println("Error message: " + result.getErrorMessage());
        }

        Assertions.assertTrue(result.isMatched());
        Assertions.assertNotNull(result.getWildcards());

        System.out.println(result.getWildcards());
        Assertions.assertEquals(expected.length, result.getWildcards().size());

        for (Wildcard wildcard : expected) {
            Assertions.assertEquals(wildcard.expected, ((Snippet) result.get(wildcard.name).getValue()).asSource());
        }
    }

    static final class Wildcard {

        private final String name;
        private final String expected;

        private Wildcard(String name, String expected) {
            this.name = name;
            this.expected = expected;
        }

        public static Wildcard of(String name, String expected) {
            return new Wildcard(name, expected);
        }

    }

}
