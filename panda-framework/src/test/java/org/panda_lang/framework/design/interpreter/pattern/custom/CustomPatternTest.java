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

package org.panda_lang.framework.design.interpreter.pattern.custom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.SectionElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.TypeElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.UnitElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.design.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;

class CustomPatternTest {

    @Test
    void testCustomPattern() {
        CustomPattern customPattern = CustomPattern.of(
                VariantElement.create("visibility").content("public", "shared", "local"),
                UnitElement.create("isStatic").content("static").optional(true),
                TypeElement.create("type").optional(true),
                WildcardElement.create("name").verify(new TokenTypeVerifier(TokenType.UNKNOWN)),
                SectionElement.create("parameters"),
                SectionElement.create("body")
        );

        Snippet source = PandaLexerUtils.convert("shared static String[] of(String a, Int[] b) { /* content */ } another content");
        Result result = customPattern.match(source);

        Assertions.assertTrue(result.isMatched());
        Assertions.assertEquals(PandaLexerUtils.convert("shared static String[] of(String a, Int[] b) { /* content */ }"), result.getSource());

        Assertions.assertAll(
                () -> Assertions.assertEquals("shared", result.getValue("visibility").toString()),
                () -> Assertions.assertTrue(result.has("isStatic")),
                () -> Assertions.assertEquals("String [  ]", result.getValue("type").toString()),
                () -> Assertions.assertEquals("of", result.getValue("name").toString()),
                () -> Assertions.assertEquals("String a , Int [  ] b", result.getValue("parameters").toString()),
                () -> Assertions.assertEquals("/* content */", result.getValue("body").toString())
        );


    }

}