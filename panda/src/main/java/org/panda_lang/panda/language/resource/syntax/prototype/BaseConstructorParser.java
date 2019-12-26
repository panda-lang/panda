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

package org.panda_lang.panda.language.resource.syntax.prototype;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Adjustment;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor.PandaConstructorScope;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.ArgumentsParser;

import java.util.Optional;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL)
public final class BaseConstructorParser extends ParserBootstrap<Void> {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.BASE))
                .interceptor(new LinearPatternInterceptor())
                .pattern("base args:(~)");
    }

    @Autowired
    void parse(Context context, @Component Scope parent, @Component Prototype prototype, @Inter SourceLocation location, @Src("args") Snippet args) {
        if (!(parent instanceof PandaConstructorScope)) {
            throw new PandaParserFailure(context, args, "Cannot use base constructor outside of the constructor");
        }

        Expression[] arguments = ARGUMENTS_PARSER.parse(context, args);
        Optional<Adjustment<PrototypeConstructor>> adjustedConstructor = prototype.getConstructors().getAdjustedConstructor(arguments);

        if (!adjustedConstructor.isPresent()) {
            throw new PandaParserFailure(context, args, "Base type does not contain constructor with requested parameter types");
        }

        adjustedConstructor.ifPresent(adjusted -> {
            parent.addStatement(new BaseConstructor(location, adjusted));
        });
    }

}
