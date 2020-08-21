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
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorScope;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorFrame;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructor;
import org.panda_lang.language.architecture.type.TypeInstance;
import org.panda_lang.language.architecture.type.TypeScope;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.language.interpreter.parser.stage.Phases;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.autowired.handlers.TokenHandler;
import org.panda_lang.utilities.commons.ArrayUtils;

import java.util.List;

public final class ConstructorParser extends AutowiredParser<Void> {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Pipelines.TYPE);
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.CONSTRUCTOR))
                .linear("constructor parameters:(~) body:{~}");
    }

    @Autowired(order = 1)
    public void parse(Context context, LocalChannel channel, @Channel Location location, @Ctx TypeScope typeScope, @Src("parameters") @Nullable Snippet parametersSource) {
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

    @Autowired(order = 2, phase = Phases.NEXT_DEFAULT)
    public void parse(Context context, @Ctx TypeScope typeScope, @Channel ConstructorScope scope, @Channel TypeConstructor constructor, @Channel Snippet src, @Src("body") @Nullable Snippet body) {
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