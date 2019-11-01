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

package org.panda_lang.panda.language.interpreter.parser.scope;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.panda.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.language.interpreter.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Component;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_EXPRESSION)
public final class StandaloneExpressionParser extends ParserBootstrap {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
            .onlyStandalone()
            .build();

    private ExpressionParser expressionParser;

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        this.expressionParser = context.getComponent(Components.EXPRESSION);
        return initializer;
    }

    @Override
    protected Object customHandle(Handler handler, Context context, Channel channel, Snippet source) {
        SourceStream stream = new PandaSourceStream(source);

        try {
            channel.put("expression", expressionParser.parse(context, stream, SETTINGS).getExpression());
            channel.put("read", stream.getReadLength());
            return true;
        } catch (PandaExpressionParserFailure e) {
            return e;
        }
    }

    @Autowired
    void parseExpression(@Component SourceStream source, @Component Scope parent, @Component Channel channel, @Inter SourceLocation location) {
        StandaloneExpression statement = new StandaloneExpression(source.getCurrent().getLocation(), channel.get("expression", Expression.class));
        parent.addStatement(statement);
        source.read(channel.get("read", int.class));
    }

}
