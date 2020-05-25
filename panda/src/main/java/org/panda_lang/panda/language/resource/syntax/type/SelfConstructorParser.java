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

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.type.ConstructorScope;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.literal.Literals;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Int;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.ArgumentsParser;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL)
public final class SelfConstructorParser extends ParserBootstrap<Void> {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .interceptor(new LinearPatternInterceptor())
                .pattern("this args:(~)");
    }

    @Override
    protected Boolean customHandle(Handler handler, Context context, Channel channel, Snippet source) {
        if (source.size() < 2) {
            return false;
        }

        if (!source.getFirst().contentEquals(Literals.THIS)) {
            return false;
        }

        return source.get(1).getType() == TokenTypes.SECTION;
    }

    @Autowired
    void parse(Context context, @Ctx Scope parent, @Ctx Type type, @Int Location location, @Src("args") Snippet args) {
        if (!(parent instanceof ConstructorScope)) {
            throw new PandaParserFailure(context, args, "Cannot use constructor call outside of the constructor");
        }

        type.getConstructors().getAdjustedConstructor(ARGUMENTS_PARSER.parse(context, args))
                .peek(constructor -> parent.addStatement(new SelfConstructor(location, constructor)))
                .onEmpty(() -> {
                    throw new PandaParserFailure(context, args, "Type does not contain constructor with requested parameter types");
                });
    }

}
