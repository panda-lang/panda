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

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.language.interpreter.parser.pool.Target;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class StandaloneExpressionParser extends AutowiredParser<Object> {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
            .onlyStandalone()
            .build();

    private ExpressionParser expressionParser;

    @Override
    public Target<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.SCOPE_EXPRESSION;
    }

    @Override
    protected AutowiredInitializer<Object> initialize(Context context, AutowiredInitializer<Object> initializer) {
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
        } catch (PandaParserFailure failure) {
            return failure;
        }
    }

    @Autowired(order = 1)
    public void parseExpression(@Ctx SourceStream source, @Ctx Scope parent, @Ctx LocalChannel channel, @Channel Location location) {
        StandaloneExpression statement = new StandaloneExpression(source.getCurrent().getLocation(), channel.get("expression", Expression.class));
        parent.addStatement(statement);
        source.readSilently(channel.get("read", int.class));
    }

}
