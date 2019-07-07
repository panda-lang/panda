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

package org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.constructor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.ConstructorScope;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.ConstructorScopeFrame;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.PandaConstructor;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeScope;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeScopeFrame;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.resource.parsers.ScopeParser;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

import java.util.List;

@ParserRegistration(pipeline = UniversalPipelines.PROTOTYPE_LABEL)
public class ConstructorParser extends ParserBootstrap {

    private final ParameterParser parameterParser = new ParameterParser();
    private final ScopeParser scopeParser = new ScopeParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.CONSTRUCTOR))
                .pattern("constructor parameters:~( body:~{");
    }

    @Autowired(order = 1)
    void parse(Context context, LocalData local, @Component ClassPrototypeScope classScope, @Src("parameters") @Nullable Snippet parametersSource) {
        ClassPrototype prototype = classScope.getPrototype();
        List<PrototypeParameter> parameters = parameterParser.parse(context, parametersSource);

        ConstructorScope constructorScope = local.allocated(new ConstructorScope(parameters));
        ParameterUtils.addAll(constructorScope.getVariables(), parameters);

        PrototypeConstructor constructor = PandaConstructor.builder()
                .type(prototype.getReference())
                .parameters(parameters)
                .callback((frame, instance, arguments) -> {
                    ClassPrototypeScopeFrame classFrame = classScope.createFrame(frame);
                    Value classInstance = new PandaStaticValue(prototype, classFrame);

                    ConstructorScopeFrame constructorInstance = constructorScope.createFrame(frame);
                    ParameterUtils.assignValues(constructorInstance, arguments);

                    frame.instance(classInstance);
                    frame.call(constructorInstance);

                    return classInstance;
                })
                .build();

        classScope.getPrototype().getConstructors().declare(constructor);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parseBody(Context context, @Local ConstructorScope constructorScope, @Component ClassPrototypeScope classScope, @Src("body") @Nullable Snippet body) throws Exception {
        scopeParser.parse(context, classScope, constructorScope, body);
    }

}