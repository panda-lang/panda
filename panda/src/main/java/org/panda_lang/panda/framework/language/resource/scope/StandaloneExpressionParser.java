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

package org.panda_lang.panda.framework.language.resource.scope;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.framework.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

@Registrable(pipeline = UniversalPipelines.SCOPE_LABEL, priority = PandaPriorities.CONTAINER_EXPRESSION)
public class StandaloneExpressionParser extends ParserBootstrap {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create().onlyStandalone();

    private ExpressionParser expressionParser;

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        this.expressionParser = context.getComponent(UniversalComponents.EXPRESSION);
        return initializer;
    }

    @Override
    protected Object customHandle(ParserHandler handler, Context context, Channel channel, Snippet source) {
        SourceStream stream = new PandaSourceStream(source);

        try {
            channel.put("expression", expressionParser.parse(context, stream, SETTINGS));
            channel.put("read", stream.getReadLength());
            return true;
        } catch (PandaExpressionParserFailure e) {
            return e;
        }
    }

    @Autowired
    void parseExpression(@Component SourceStream source, @Component Scope scope, @Component Channel channel) {
        StandaloneExpression statement = new StandaloneExpression(channel.get("expression", Expression.class));
        statement.setLocation(source.toSnippet().getLocation());
        scope.addStatement(statement);
        source.read(channel.get("read", int.class));
    }

}
