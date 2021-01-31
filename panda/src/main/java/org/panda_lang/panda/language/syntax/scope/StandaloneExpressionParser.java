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

package org.panda_lang.panda.language.syntax.scope;

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.syntax.PandaPriorities;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;

public final class StandaloneExpressionParser implements ContextParser<Object, StandaloneExpression> {

    private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
            .onlyStandalone()
            .build();

    @Override
    public String name() {
        return "standalone expression";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.SCOPE_EXPRESSION;
    }

    @Override
    public Option<Completable<StandaloneExpression>> parse(Context<?> context) {
        Context<?> delegatedContext = context.forkCreator()
                .withStream(new PandaSourceStream(context.getSource()))
                .toContext();

        Expression expression = delegatedContext.getExpressionParser().parse(delegatedContext, delegatedContext.getStream(), SETTINGS);
        context.getStream().dispose(delegatedContext.getStream().getReadLength());

        StandaloneExpression statement = new StandaloneExpression(delegatedContext, expression);
        context.getScope().addStatement(statement);

        return Option.ofCompleted(statement);
    }

}
