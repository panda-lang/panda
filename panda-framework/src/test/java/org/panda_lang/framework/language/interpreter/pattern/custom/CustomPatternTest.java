/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.custom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ExpressionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SectionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SubPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.TypeElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.UnitElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.NextTokenTypeVerifier;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

import java.util.Collections;

class CustomPatternTest {

    @Test
    void method() {
        CustomPattern customPattern = CustomPattern.of(
                VariantElement.create("visibility").content("public", "shared", "internal"),
                UnitElement.create("isStatic").content("static").optional(),
                TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN)),
                WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                SectionElement.create("parameters"),
                SectionElement.create("body")
        );

        Snippet source = convert("shared static String[] of(String a, Int[] b) { /* content */ } another content");
        Result result = customPattern.match(source);

        Assertions.assertTrue(result.isMatched());
        Assertions.assertEquals(convert("shared static String[] of(String a, Int[] b) { /* content */ }"), result.getSource());

        Assertions.assertAll(
                () -> Assertions.assertEquals("shared", result.get("visibility").toString()),
                () -> Assertions.assertTrue(result.has("isStatic")),
                () -> Assertions.assertEquals("String [  ]", result.get("type").toString()),
                () -> Assertions.assertEquals("of", result.get("name").toString()),
                () -> Assertions.assertEquals("String a , Int [  ] b", result.get("parameters").toString()),
                () -> Assertions.assertEquals("/* content */", result.get("body").toString())
        );
    }

    @Test
    void field() {
        Context fakeContext = new PandaContext();
        fakeContext.withComponent(Components.EXPRESSION, new PandaExpressionParser(Collections::emptyList));

        CustomPattern pattern = CustomPattern.of(
                VariantElement.create("visibility").content(Keywords.PUBLIC.getValue(), Keywords.SHARED.getValue(), Keywords.INTERNAL.getValue()),
                KeywordElement.create(Keywords.STATIC).optional(),
                KeywordElement.create(Keywords.MUT).optional(),
                KeywordElement.create(Keywords.NIL).optional(),
                TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN)),
                WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                SubPatternElement.create("assign").optional().of(
                        UnitElement.create("operator").content("="),
                        ExpressionElement.create("assignation").map(ExpressionTransaction::getExpression)
                )
        );

        Result result = pattern.match(convert("internal mut Test testField"));
        Assertions.assertTrue(result.isMatched());
    }

    @Test
    void condition() {
        CustomPattern pattern = CustomPattern.of(
                VariantElement.create("variant").content(
                        SubPatternElement.create("with-condition").of(
                                VariantElement.create("type").content("if", "else if"),
                                SectionElement.create("section")
                        ),
                        KeywordElement.create(Keywords.ELSE)
                )
        );

        Result result = pattern.match(convert("if ( )"));
        Assertions.assertTrue(result.isMatched());
    }

    private Snippet convert(String source) {
        return PandaLexerUtils.convert(CustomPatternTest.class.getSimpleName(), source);
    }

}