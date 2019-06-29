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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.HashMap;

class ExpressionParserTestBootstrap {

    private static ExpressionParser PARSER;
    private static Context DATA;

    @BeforeAll
    public static void load() {
        PARSER = new PandaExpressionParser(PandaExpressionUtils.collectSubparsers());
        DATA = prepareData();
    }

    @BeforeEach
    public void emptyLine() {
        System.out.println(StringUtils.EMPTY);
    }

    protected static Context prepareData() {
        return PandaParserDataUtils.createFakeData(new HashMap<Variable, Object>() {{
            put(new PandaVariable(PandaTypes.STRING.getReference(), "variable"), null);
            put(new PandaVariable(PandaTypes.STRING.toArray(), "array"), null);
            put(new PandaVariable(PandaTypes.INT.getReference(), "i"), null);
        }});
    }

    protected static void parse(String source, String message) {
        parse(source, ExpressionParserException.class, message);
    }

    protected static void parse(String source, Class<? extends Throwable> clazz, String message) {
        Throwable throwable = Assertions.assertThrows(clazz, () -> parse(source));
        Assertions.assertEquals(message, throwable.getLocalizedMessage());
        System.out.println(source + ": " + message);
    }

    protected static void parse(String source) {
        SourceStream stream = new PandaSourceStream(PandaLexerUtils.convert(source));

        DATA.withComponent(UniversalComponents.SOURCE, stream.toSnippet());
        DATA.withComponent(UniversalComponents.SOURCE_STREAM, stream);

        Expression expression = PARSER.parse(DATA, stream);

        if (stream.hasUnreadSource()) {
            throw new RuntimeException("Unread source: " + stream.toSnippet());
        }

        System.out.println(source + ": " + expression);
    }

}
