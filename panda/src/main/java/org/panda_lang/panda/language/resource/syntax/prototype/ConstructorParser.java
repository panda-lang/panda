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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.prototype.PropertyParameter;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.dynamic.AbstractFrame;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor.PandaConstructorScope;
import org.panda_lang.framework.language.architecture.prototype.PrototypeScope;
import org.panda_lang.framework.language.architecture.prototype.utils.ParameterUtils;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.LinearPatternInterceptor;

import java.util.List;

@RegistrableParser(pipeline = Pipelines.PROTOTYPE_LABEL)
public final class ConstructorParser extends ParserBootstrap {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.CONSTRUCTOR))
                .interceptor(new LinearPatternInterceptor())
                .pattern("constructor parameters:(~) body:{~}");
    }

    @Autowired(order = 1)
    void parse(Context context, LocalData local, @Inter SourceLocation location, @Component PrototypeScope prototypeScope, @Src("parameters") @Nullable Snippet parametersSource) {
        Prototype prototype = prototypeScope.getPrototype();
        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, parametersSource);
        PandaConstructorScope constructorScope = local.allocated(new PandaConstructorScope(location, parameters));

        PrototypeConstructor constructor = PandaConstructor.builder()
                .type(prototype)
                .location(location)
                .parameters(parameters)
                .callback((stack, instance, arguments) -> {
                    Frame prototypeInstance = prototypeScope.revive(stack, instance);

                    AbstractFrame<?> constructorInstance = constructorScope.revive(stack, prototypeInstance);
                    ParameterUtils.assignValues(constructorInstance, arguments);

                    stack.callFrame(prototypeInstance, constructorInstance);
                    return prototypeInstance;
                })
                .build();

        prototypeScope.getPrototype().getConstructors().declare(constructor);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parseBody(Context context, @Local PandaConstructorScope pandaConstructorFrame, @Src("body") @Nullable Snippet body) throws Exception {
        SCOPE_PARSER.parse(context, pandaConstructorFrame, body);
    }

}