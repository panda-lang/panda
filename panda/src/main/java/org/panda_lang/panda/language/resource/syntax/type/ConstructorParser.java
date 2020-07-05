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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.type.PropertyParameter;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeConstructor;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.type.ConstructorScope;
import org.panda_lang.framework.language.architecture.type.ConstructorScope.ConstructorFrame;
import org.panda_lang.framework.language.architecture.type.PandaConstructor;
import org.panda_lang.framework.language.architecture.type.TypeInstance;
import org.panda_lang.framework.language.architecture.type.TypeScope;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.Delegation;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.initializers.LinearPatternInitializer;

import java.util.List;

@RegistrableParser(pipeline = Pipelines.TYPE_LABEL)
public final class ConstructorParser extends ParserBootstrap<Void> {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.CONSTRUCTOR))
                .initializer(new LinearPatternInitializer())
                .pattern("constructor parameters:(~) body:{~}");
    }

    @Autowired(order = 1)
    void parse(Context context, LocalChannel channel, @Channel Location location, @Ctx TypeScope typeScope, @Src("parameters") @Nullable Snippet parametersSource) {
        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, parametersSource);
        ConstructorScope constructorScope = channel.allocated("scope", new ConstructorScope(location, parameters));

        Class<?>[] parameterTypes = parameters.stream()
                .map(PropertyParameter::getType)
                .map(parameterType -> parameterType.getAssociatedClass().fetchStructure())
                .toArray(Class[]::new);

        TypeConstructor constructor = channel.allocated("constructor", PandaConstructor.builder()
                .type(typeScope.getType())
                .location(location)
                .parameters(parameters)
                .baseCall(constructorScope::getBaseCall)
                .callback((typeConstructor, stack, instance, arguments) -> {
                    TypeInstance typeInstance = typeScope.createInstance(stack, instance, typeConstructor, parameterTypes, arguments);
                    ConstructorFrame constructorInstance = constructorScope.revive(stack, typeInstance);
                    return constructorInstance.initialize(stack, typeInstance, arguments);
                })
                .build());

        typeScope.getType().getConstructors().declare(constructor);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parse(Context context, @Ctx TypeScope typeScope, @Channel ConstructorScope scope, @Channel TypeConstructor constructor, @Channel Snippet src, @Src("body") @Nullable Snippet body) throws Exception {
        SCOPE_PARSER.parse(context, scope, body);

        typeScope.getType().getSuperclass()
                .filterNot(superclass -> superclass.getName().equals("java::Object"))
                .filterNot(superclass -> superclass.getConstructors().getConstructor(new Type[0]).isDefined())
                .filterNot(superclass -> scope.getBaseCall().isDefined())
                .peek(superclass -> {
                    throw new PandaParserFailure(context, src, src,
                            "&1Missing call to the base constructor in " + constructor + "&r",
                            "Using the &1base&r statement call one of the base constructors"
                    );
                });
    }

}