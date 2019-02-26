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

package org.panda_lang.panda.interpreter.parser.implementation.general.expression.fixed;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeInstance;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparsersLoader;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserException;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractScope;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class ExpressionParserTest {

    private static ExpressionParser expressionParser;
    private ParserData data;

    @BeforeAll
    public static void load() throws Exception {
        expressionParser = new ExpressionParser(new ExpressionSubparsersLoader().load());
    }

    @BeforeEach
    public void emptyLine() {
        System.out.println(StringUtils.EMPTY);

        this.data = new PandaParserData();
        this.prepareScope();
    }

    @Test
    public void parseUnknown() {
        parse("u n k n o w n", "Cannot read the expression");
    }

    @Test
    public void parseSequences() {
        parse("'hello panda'");
        parse("'hello panda' 'hello expressions'", "Source contains 2 expressions");
    }

    @Test
    public void parseLiterals() {
        parse("null");
        parse("true false", "Source contains 2 expressions");
    }

    @Test
    public void parseSection() {
        parse("('chance')");
        parse("('random') true");
        parse("()", "Expression expected");
    }

    @Test
    public void parseVariable() {
        parse("variable");
        parse("variable.field");
    }

    private void prepareScope() {
        Scope scope = new AbstractScope() {
            @Override
            public ScopeInstance createInstance(ExecutableBranch branch) {
                return null;
            }
        };

        ScopeLinker linker = new PandaScopeLinker(scope);
        data.setComponent(UniversalComponents.SCOPE_LINKER, linker);
    }

    private void parse(String source, String message) {
        Throwable throwable = Assertions.assertThrows(ExpressionParserException.class, () -> parse(source));
        Assertions.assertEquals(message, throwable.getMessage());
    }

    private void parse(String source) {
        System.out.println(source + ": " + expressionParser.parse(data, PandaLexerUtils.convert(source)));
    }

}
