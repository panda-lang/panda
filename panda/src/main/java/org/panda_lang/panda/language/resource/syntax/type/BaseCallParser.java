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

package org.panda_lang.panda.language.resource.syntax.type;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.type.BaseCall;
import org.panda_lang.framework.language.architecture.type.ConstructorScope;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.ArgumentsParser;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL)
public final class BaseCallParser extends ParserBootstrap<Void> {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.BASE))
                .linear("base args:(~)");
    }

    @Autowired(order = 1)
    void parse(Context context, @Ctx Scope parent, @Ctx Type type, @Channel Location location, @Channel Snippet src, @Src("args") Snippet args) {
        if (!(parent instanceof ConstructorScope)) {
            throw new PandaParserFailure(context, src, src, "Cannot use base constructor outside of the constructor");
        }

        if (!parent.getStatements().isEmpty()) {
            throw new PandaParserFailure(context, src, src, "Base constructor has to be the first statement in the scope");
        }

        if (type.getSuperclass().isEmpty()) {
            throw new PandaParserFailure(context, src, src, "Cannot use base call without superclass");
        }

        Expression[] expressions = ARGUMENTS_PARSER.parse(context, args);

        type.getSuperclass().get().getConstructors().getAdjustedConstructor(expressions)
                .peek(constructor -> parent.addStatement(new BaseCall(location, expressions)))
                .onEmpty(() -> {
                    throw new PandaParserFailure(context, src, src, "Base type does not contain constructor with the given parameters");
                });
    }
}