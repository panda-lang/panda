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
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.Registrable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.ExpressionExecutable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionParserException;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

@Registrable(pipeline = UniversalPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_EXPRESSION)
public class StandaloneExpressionParser extends ParserBootstrap {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create().onlyStandalone();

    private ExpressionParser expressionParser;
    private Expression expression;
    private int read;

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        this.expressionParser = context.getComponent(UniversalComponents.EXPRESSION);
        return initializer;
    }

    @Override
    public Object customHandle(ParserHandler handler, Context context, Snippet source) {
        SourceStream stream = new PandaSourceStream(source);

        try {
            this.expression = expressionParser.parse(context, stream, SETTINGS);
            this.read = stream.getReadLength();
            return true;
        } catch (PandaExpressionParserException e) {
            return e;
        }
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
