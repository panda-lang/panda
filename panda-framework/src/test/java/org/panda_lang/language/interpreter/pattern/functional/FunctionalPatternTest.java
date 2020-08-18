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

package org.panda_lang.language.interpreter.pattern.functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.language.interpreter.parser.PandaContext;
import org.panda_lang.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.language.interpreter.pattern.functional.elements.ExpressionElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.KeywordElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.SectionElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.SubPatternElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.TypeElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.UnitElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.VariantElement;
import org.panda_lang.language.interpreter.pattern.functional.elements.WildcardElement;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.NextTokenTypeVerifier;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.TokenTypeVerifier;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;

import java.util.Collections;

class FunctionalPatternTest {

    @Test
    void method() {
        FunctionalPattern functionalPattern = FunctionalPattern.of(
                VariantElement.create("visibility").content("open", "shared", "internal"),
                UnitElement.create("isStatic").content("static").optional(),
                TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN, TokenTypes.SEQUENCE)),
                WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN, TokenTypes.SEQUENCE)),
                SectionElement.create("parameters", Separators.PARENTHESIS_LEFT),
                SectionElement.create("body", Separators.BRACE_LEFT)
        );

        Snippet source = convert("shared static String[] 'of'(String a, Int[] b) { /* content */ } another content");
        FunctionalResult functionalResult = functionalPattern.match(source);

        Assertions.assertTrue(functionalResult.isMatched());
        Assertions.assertEquals(convert("shared static String[] 'of'(String a, Int[] b) { /* content */ }"), functionalResult.toSnippet());

        Assertions.assertAll(
                () -> Assertions.assertEquals("shared", functionalResult.get("visibility").get().toString()),
                () -> Assertions.assertTrue(functionalResult.has("isStatic")),
                () -> Assertions.assertEquals("String [  ]", functionalResult.get("type").get().toString()),
                () -> Assertions.assertEquals("of", functionalResult.get("name").get().toString()),
                () -> Assertions.assertEquals("String a , Int [  ] b", functionalResult.get("parameters").get().toString()),
                () -> Assertions.assertEquals("/* content */", functionalResult.get("body").get().toString())
        );
    }

    @Test
    void field() {
        Context fakeContext = new PandaContext();
        fakeContext.withComponent(Components.EXPRESSION, new PandaExpressionParser(Collections::emptyList));

        FunctionalPattern pattern = FunctionalPattern.of(
                VariantElement.create("visibility").content(Keywords.OPEN.getValue(), Keywords.SHARED.getValue(), Keywords.INTERNAL.getValue()),
                KeywordElement.create(Keywords.STATIC).optional(),
                KeywordElement.create(Keywords.MUT).optional(),
                KeywordElement.create(Keywords.NIL).optional(),
                TypeElement.create("type").verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN)),
                WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                SubPatternElement.create("assign").optional().of(
                        UnitElement.create("operator").content("="),
                        ExpressionElement.create("assignation").map(ExpressionTransaction::getExpression)
                )
        );

        FunctionalResult functionalResult = pattern.match(convert("internal mut Test testField"));
        Assertions.assertTrue(functionalResult.isMatched());
    }

    @Test
    void condition() {
        FunctionalPattern pattern = FunctionalPattern.of(
                VariantElement.create("variant").content(
                        SubPatternElement.create("with-condition").of(
                                VariantElement.create("type").content("if", "else if"),
                                SectionElement.create("section", Separators.PARENTHESIS_LEFT)
                        ),
                        KeywordElement.create(Keywords.ELSE)
                )
        );

        FunctionalResult functionalResult = pattern.match(convert("if ( )"));
        Assertions.assertTrue(functionalResult.isMatched());
    }

    private Snippet convert(String source) {
        return PandaLexerUtils.convert(FunctionalPatternTest.class.getSimpleName(), source);
    }

}