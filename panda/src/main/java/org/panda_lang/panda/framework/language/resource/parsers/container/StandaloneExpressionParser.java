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

package org.panda_lang.panda.framework.language.resource.parsers.container;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.ExpressionExecutable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Optional;

@ParserRegistration(target = PandaPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_EXPRESSION)
public class StandaloneExpressionParser extends ParserBootstrap {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create().onlyStandalone();

    private ExpressionParser expressionParser;
    private Expression expression;
    private int read;

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        this.expressionParser = data.getComponent(UniversalComponents.EXPRESSION);
        return defaultBuilder;
    }

    @Override
    public boolean customHandle(ParserHandler handler, ParserData data, Snippet source) {
        SourceStream stream = new PandaSourceStream(source);
        Optional<Expression> expression = expressionParser.parseSilently(data, stream, SETTINGS);

        if (!expression.isPresent()) {
            return false;
        }

        this.expression = expression.get();
        this.read = stream.getReadLength();
        return true;
    }

    @Autowired
    void parseExpression(@Component SourceStream source, @Component Container container) {
        ExpressionExecutable statement = new ExpressionExecutable(expression);
        statement.setLocation(source.toSnippet().getCurrentLocation());

        expression = null;
        source.read(read);
        read = 0;

        container.addStatement(statement);
    }

}
