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

package org.panda_lang.panda.language.resource.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.dynamic.AbstractLivingFrame;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructorFrame;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor;
import org.panda_lang.framework.language.architecture.parameter.ParameterUtils;
import org.panda_lang.framework.language.architecture.prototype.PrototypeFrame;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.language.interpreter.parser.ScopeParser;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

import java.util.List;

@Registrable(pipeline = UniversalPipelines.PROTOTYPE_LABEL)
public class ConstructorParser extends ParserBootstrap {

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
    void parse(Context context, LocalData local, @Component PrototypeFrame classScope, @Src("parameters") @Nullable Snippet parametersSource) {
        Prototype prototype = classScope.getPrototype();
        List<Parameter> parameters = PARAMETER_PARSER.parse(context, parametersSource);

        PandaConstructorFrame constructorScope = local.allocated(new PandaConstructorFrame(parameters));
        constructorScope.addParameters(parameters);

        PrototypeConstructor constructor = PandaConstructor.builder()
                .type(prototype.getReference())
                .parameters(parameters)
                .callback((stack, instance, arguments) -> {
                    LivingFrame classInstance = classScope.revive(stack, instance);

                    AbstractLivingFrame constructorInstance = constructorScope.revive(stack, classInstance);
                    ParameterUtils.assignValues(constructorInstance, arguments);

                    stack.call(classInstance, constructorInstance);
                    return classInstance;
                })
                .build();

        classScope.getPrototype().getConstructors().declare(constructor);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parseBody(Context context, @Local PandaConstructorFrame pandaConstructorFrame, @Src("body") @Nullable Snippet body) throws Exception {
        SCOPE_PARSER.parse(context, pandaConstructorFrame, body);
    }

}