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

package org.panda_lang.panda.language.resource.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.statement.PandaVariableData;
import org.panda_lang.panda.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.panda.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.resource.PandaTypes;
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
        return ExpressionContextUtils.createFakeContext(context -> new HashMap<VariableData, Object>() {{
            put(new PandaVariableData(PandaTypes.STRING.getReference(), "variable"), null);
            put(new PandaVariableData(PandaTypes.STRING.toArray(context.getComponent(UniversalComponents.MODULE_LOADER)), "array"), null);
            put(new PandaVariableData(PandaTypes.INT.getReference(), "i", true, false), null);
        }});
    }

    protected static void parse(String source, String message) {
        parse(source, PandaExpressionParserFailure.class, message);
    }

    protected static void parse(String source, Class<? extends Throwable> clazz, String message) {
        Throwable throwable = Assertions.assertThrows(clazz, () -> parse(source));
        Assertions.assertEquals(message, throwable.getLocalizedMessage());
        System.out.println(source + ": " + message);
    }

    protected static void parse(String source) {
        SourceStream stream = new PandaSourceStream(PandaLexerUtils.convert(source));

        DATA.withComponent(UniversalComponents.SOURCE, stream.toSnippet());
        DATA.withComponent(UniversalComponents.STREAM, stream);

        Expression expression = PARSER.parse(DATA, stream).getExpression();

        if (stream.hasUnreadSource()) {
            throw new RuntimeException("Unread source: " + stream.toSnippet());
        }

        System.out.println(source + ": " + expression);
    }

}
