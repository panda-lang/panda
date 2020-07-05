/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.scope;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_EXPRESSION)
public final class StandaloneExpressionParser extends ParserBootstrap<Object> {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
            .onlyStandalone()
            .build();

    private ExpressionParser expressionParser;

    @Override
    protected BootstrapInitializer<Object> initialize(Context context, BootstrapInitializer<Object> initializer) {
        this.expressionParser = context.getComponent(Components.EXPRESSION);
        return initializer;
    }

    @Override
    protected Object customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        SourceStream stream = new PandaSourceStream(source);

        try {
            channel.allocated("expression", expressionParser.parse(context, stream, SETTINGS).getExpression());
            channel.allocated("read", stream.getReadLength());
            channel.allocated("location", source.getLocation());
            return true;
        } catch (PandaExpressionParserFailure e) {
            return e;
        }
    }

    @Autowired(order = 1)
    void parseExpression(@Ctx SourceStream source, @Ctx Scope parent, @Ctx LocalChannel channel, @Channel Location location) {
        StandaloneExpression statement = new StandaloneExpression(source.getCurrent().getLocation(), channel.get("expression", Expression.class));
        parent.addStatement(statement);
        source.read(channel.get("read", int.class));
    }

}
