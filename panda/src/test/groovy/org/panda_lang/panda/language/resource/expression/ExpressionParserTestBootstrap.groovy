/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.resource.expression

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.panda_lang.framework.PandaFrameworkException
import org.panda_lang.framework.architecture.expression.Expression
import org.panda_lang.framework.architecture.statement.PandaVariableData
import org.panda_lang.framework.architecture.statement.VariableData
import org.panda_lang.framework.interpreter.lexer.PandaLexerUtils
import org.panda_lang.framework.interpreter.parser.Context
import org.panda_lang.framework.interpreter.parser.PandaParserFailure
import org.panda_lang.framework.interpreter.parser.expression.ExpressionParser
import org.panda_lang.framework.interpreter.parser.expression.PandaExpressionParser
import org.panda_lang.framework.interpreter.token.PandaSourceStream
import org.panda_lang.framework.interpreter.token.Snippet
import org.panda_lang.framework.interpreter.token.SourceStream
import org.panda_lang.panda.language.syntax.expressions.PandaExpressions
import org.panda_lang.panda.utils.PandaContextUtils
import org.panda_lang.panda.utils.PandaUtils
import org.panda_lang.utilities.commons.StringUtils

class ExpressionParserTestBootstrap {

    private static ExpressionParser PARSER;
    private static Context<?> CONTEXT;

    @BeforeAll
    static void load() {
        PARSER = new PandaExpressionParser(PandaExpressions.createExpressionSubparsers())
        CONTEXT = prepareData()
    }

    @BeforeEach
    void emptyLine() {
        System.out.println(StringUtils.EMPTY)
    }

    protected static Context<?> prepareData() {
        return PandaContextUtils.createStubContext(PandaUtils.defaultInstance(), context -> new HashMap<VariableData, Object>() {{
            put(new PandaVariableData(context.getTypeLoader().requireType("panda::String").getSignature(), "variable"), null)
            put(new PandaVariableData(context.getTypeLoader().requireType("panda::String").getSignature(), "array"), null)
            put(new PandaVariableData(context.getTypeLoader().requireType("panda::Int").getSignature(), "i", true, false), null)
        }}).toContext()
    }

    protected static void parse(String source, String message) {
        parse(source, PandaParserFailure.class, message)
    }

    protected static void parse(String source, Class<? extends Throwable> clazz, String message) {
        Throwable throwable = Assertions.assertThrows(clazz, () -> parse(source))
        Assertions.assertEquals(message, throwable.getLocalizedMessage())
        System.out.println(source + ": " + message)
    }

    protected static void parse(String src) {
        Snippet source = PandaLexerUtils.convert(ExpressionParserTestBootstrap.class.getSimpleName(), src)
        SourceStream stream = new PandaSourceStream(source)

        CONTEXT = CONTEXT.forkCreator()
                .withScriptSource(source)
                .withSource(source)
                .withStream(stream)
                .toContext()

        Expression expression = PARSER.parse(CONTEXT, stream)

        if (stream.hasUnreadSource()) {
            throw new PandaFrameworkException("Unread source: " + stream.toSnippet())
        }

        System.out.println(source + ": " + expression)
    }

}
