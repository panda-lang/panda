/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.type;

import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.TypeScope;
import panda.interpreter.architecture.type.member.constructor.ConstructorScope;
import panda.interpreter.architecture.type.member.constructor.PandaConstructor;
import panda.interpreter.architecture.type.member.constructor.TypeConstructor;
import panda.interpreter.architecture.type.member.parameter.PropertyParameter;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.parser.stage.Layer;
import panda.interpreter.parser.stage.Phases;
import panda.interpreter.token.Snippet;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.ScopeParser;
import panda.utilities.ArrayUtils;
import panda.interpreter.parser.Component;
import panda.std.Completable;
import panda.std.Option;

import java.util.Collections;
import java.util.List;

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
    public void initialize(Context<?> context) {
        this.scopeParser = new ScopeParser(context.getPoolService());
    }

    @Override
    public Option<Completable<ConstructorScope>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        if (sourceReader.read(Keywords.CONSTRUCTOR).isEmpty()) {
            return Option.none();
        }

        Option<Snippet> parametersSource = sourceReader.readArguments();

        if (parametersSource.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing constructor parameters");
        }

        Option<Snippet> body = sourceReader.readBody();

        if (body.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(), "Missing constructor body");
        }

        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, parametersSource.get());
        ConstructorScope constructorScope = new ConstructorScope(sourceReader.toLocation(), parameters);
        TypeScope typeScope = context.getSubject().getScope();
        Type type = typeScope.getReference().fetchType();

        TypeConstructor constructor = PandaConstructor.builder()
                .type(type)
                .returnType(type.getSignature())
                .location(context.getSource())
                .parameters(parameters)
                .baseCall(constructorScope::getBaseCall)
                .scope(constructorScope)
                .invoker(constructorScope)
                .build();

        type.getConstructors().declare(constructor);

        context.getStageService().delegate("parse constructor body", Phases.CONTENT, Layer.NEXT_DEFAULT, bodyPhase -> {
            scopeParser.parse(context, constructorScope, body.get());
        });

        context.getStageService().delegate("verify base call", Phases.VERIFY, Layer.NEXT_DEFAULT, verifyPhase -> {
            type.getSuperclass()
                    .filterNot(superclass -> superclass.fetchType().is("panda/panda@::Object"))
                    .filterNot(superclass -> superclass.fetchType().getConstructors().getConstructor(Collections.emptyList()).isDefined())
                    .filterNot(superclass -> constructorScope.getBaseCall().isDefined())
                    .peek(superclass -> {
                        throw new PandaParserFailure(context, context.getSource(),
                                "&1Missing call to the base constructor in " + constructor + "&r",
                                "Using the &1base&r statement call one of the base constructors"
                        );
                    });
        });

        return Option.withCompleted(constructorScope);
    }

}