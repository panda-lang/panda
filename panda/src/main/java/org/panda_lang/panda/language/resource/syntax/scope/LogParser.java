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
import org.panda_lang.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ArgumentsElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.initializers.CustomPatternInitializer;

import java.util.Arrays;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL)
public final class LogParser extends ParserBootstrap<Void> {

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LOG))
                .initializer(new CustomPatternInitializer())
                .pattern(CustomPattern.of(
                        KeywordElement.create(Keywords.LOG),
                        ArgumentsElement.create("arguments")
                ));
    }

    @Autowired(order = 1)
    void parse(Context context, @Ctx ExpressionParser parser, @Ctx Scope scope, @Channel Location location, @Src("arguments") ExpressionTransaction[] transactions) {
        Expression[] expressions = Arrays.stream(transactions)
                .map(ExpressionTransaction::getExpression)
                .toArray(Expression[]::new);

        Messenger messenger = context.getComponent(Components.ENVIRONMENT).getMessenger();
        scope.addStatement(new LogStatement(location, messenger, expressions));
    }

}
