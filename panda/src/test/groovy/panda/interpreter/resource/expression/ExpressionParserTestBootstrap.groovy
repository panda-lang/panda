/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.interpreter.resource.expression

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import panda.interpreter.PandaFrameworkException
import panda.interpreter.architecture.expression.Expression
import panda.interpreter.architecture.statement.PandaVariableData
import panda.interpreter.architecture.statement.VariableData
import panda.interpreter.lexer.PandaLexerUtils
import panda.interpreter.parser.Context
import panda.interpreter.parser.PandaParserFailure
import panda.interpreter.parser.expression.ExpressionParser
import panda.interpreter.parser.expression.PandaExpressionParser
import panda.interpreter.syntax.expressions.PandaExpressions
import panda.interpreter.token.PandaSourceStream
import panda.interpreter.token.Snippet
import panda.interpreter.token.SourceStream
import panda.interpreter.utils.PandaContextUtils
import panda.interpreter.utils.PandaUtils

import static org.junit.jupiter.api.Assertions.assertEquals
import static panda.utilities.StringUtils.EMPTY

class ExpressionParserTestBootstrap {

    private static ExpressionParser PARSER
    private static Context<?> CONTEXT

    @BeforeAll
    static void load() {
        PARSER = new PandaExpressionParser(PandaExpressions.createExpressionSubparsers())
        CONTEXT = prepareData()
    }

    @BeforeEach
    void emptyLine() {
        System.out.println(EMPTY)
    }

    protected static Context<?> prepareData() {
        return PandaContextUtils.createStubContext(PandaUtils.defaultInstance(), context -> new HashMap<VariableData, Object>() {{
            put(new PandaVariableData(context.getTypeLoader().requireType("panda/panda@::String").getSignature(), "variable"), null)
            put(new PandaVariableData(context.getTypeLoader().requireType("panda/panda@::String").getSignature(), "array"), null)
            put(new PandaVariableData(context.getTypeLoader().requireType("panda/panda@::Int").getSignature(), "i", true, false), null)
        }}).toContext()
    }

    protected static void parse(String source, String message) {
        parse(source, PandaParserFailure.class, message)
    }

    protected static void parse(String source, Class<? extends Throwable> clazz, String message) {
        Throwable throwable = Assertions.assertThrows(clazz, () -> parse(source))
        assertEquals(message, throwable.getLocalizedMessage())
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
