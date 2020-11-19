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

import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.TypeContext;
import org.panda_lang.language.architecture.type.TypeInstance;
import org.panda_lang.language.architecture.type.TypeScope;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorFrame;
import org.panda_lang.language.architecture.type.member.constructor.ConstructorScope;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructor;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ConstructorParser implements ContextParser<TypeContext, ConstructorScope> {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();

    private ScopeParser scopeParser;

    @Override
    public String name() {
        return "constructor";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.TYPE);
    }

    @Override
    public void initialize(Context<? extends TypeContext> context) {
        this.scopeParser = new ScopeParser(context.getPoolService());
    }

    @Override
    public Option<CompletableFuture<ConstructorScope>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.CONSTRUCTOR).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> parametersSource = sourceReader.readArguments();

        if (parametersSource.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing constructor parameters");
        }

        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, parametersSource.get());
        ConstructorScope constructorScope = new ConstructorScope(parametersSource.get(), parameters);
        TypeScope typeScope = context.getSubject().getScope();

        TypeConstructor constructor = PandaConstructor.builder()
                .type(typeScope.getType())
                .location(context.getSource())
                .parameters(parameters)
                .baseCall(constructorScope::getBaseCall)
                .callback((typeConstructor, stack, instance, arguments) -> {
                    TypeInstance typeInstance = typeScope.createInstance(stack, instance, typeConstructor, parameters, arguments);
                    ConstructorFrame constructorInstance = constructorScope.revive(stack, typeInstance);
                    return constructorInstance.initialize(stack, typeInstance, arguments);
                })
                .build();

        typeScope.getType().getConstructors().declare(constructor);
        scopeParser.parse(context, constructorScope, sourceReader.readBody().get());

        typeScope.getType().getSuperclass()
                .filterNot(superclass -> superclass.fetchType().is("panda::Object"))
                .filterNot(superclass -> superclass.fetchType().getConstructors().getConstructor(new Signature[0]).isDefined())
                .filterNot(superclass -> constructorScope.getBaseCall().isDefined())
                .peek(superclass -> {
                    throw new PandaParserFailure(context, context.getSource(),
                            "&1Missing call to the base constructor in " + constructor + "&r",
                            "Using the &1base&r statement call one of the base constructors"
                    );
                });

        return null;
    }

}