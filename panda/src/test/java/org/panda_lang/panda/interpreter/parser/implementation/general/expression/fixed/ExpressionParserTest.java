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
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParserException;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparsersLoader;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractScope;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
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
        parse("u n k n o w n", "Cannot parse the expression: Cannot find variable or field called 'u'");
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
        parse("('random') true", RuntimeException.class, "Unread source: true");
        parse("()", "Cannot parse the expression: Expression expected");
    }

    @Test
    public void parseNumber() {
        parse("10");
        parse("10.0");
        parse("10.0F");
        parse("10_000");
    }

    @Test
    public void parseVariable() {
        parse("variable");
        parse("variable.field", "Cannot parse the expression, the latest error: Cannot find field called 'field'");
    }

    @Test
    public void parseArrayAssignation() {
        parse("array[0]");

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

        ModulePath path = new PandaModulePath();
        ModuleLoader loader = new PandaModuleLoader(new PandaTypes().fill(path));
        loader.include(path.getDefaultModule());

        data.setComponent(UniversalComponents.MODULE_LOADER, loader);

        scope.addVariable(new PandaVariable(PandaTypes.STRING.getReference(), "variable"));
        scope.addVariable(new PandaVariable(PandaTypes.STRING.toArray(), "array"));
    }

    private void parse(String source, String message) {
        parse(source, ExpressionParserException.class, message);
    }

    private void parse(String source, Class<? extends Throwable> clazz, String message) {
        Throwable throwable = Assertions.assertThrows(clazz, () -> parse(source));
        Assertions.assertEquals(message, throwable.getMessage());
        System.out.println(source + ": " + message);
    }

    private void parse(String source) {
        SourceStream stream = new PandaSourceStream(PandaLexerUtils.convert(source));
        System.out.println(source + ": " + expressionParser.parse(data, stream));

        if (stream.hasUnreadSource()) {
            throw new RuntimeException("Unread source: " + stream.toTokenizedSource());
        }
    }

}
