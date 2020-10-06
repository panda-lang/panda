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

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.type.PandaType;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypeContext;
import org.panda_lang.language.architecture.type.TypeScope;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructor;
import org.panda_lang.language.architecture.type.member.field.TypeField;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.PoolParser;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.parser.stage.Layer;
import org.panda_lang.language.interpreter.parser.stage.Phases;
import org.panda_lang.language.interpreter.parser.stage.StageService;
import org.panda_lang.language.interpreter.token.PandaSnippet;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.CompletableOption;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class TypeParser implements ContextParser<Object, Type> {

    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();
    private PoolParser<TypeContext> typePoolParser;

    @Override
    public String name() {
        return "type";
    }

    @Override
    public Component<?>[] targetPools() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public void initialize(Context<Object> context) {
        this.typePoolParser = context.getPoolService().getPool(Targets.TYPE).toParser();
    }

    @Override
    public Option<CompletableFuture<Type>> parse(Context<Object> context) {
        PandaSourceReader reader = new PandaSourceReader(context.getStream());

        // read optional visibility

        Visibility visibility = reader
                .optionalRead(() -> reader.readVariant(Keywords.OPEN, Keywords.SHARED, Keywords.INTERNAL)
                        .map(Visibility::of))
                .orElseGet(Visibility.INTERNAL);

        // read required model

        Option<TokenInfo> kind = reader.readVariant(Keywords.CLASS, Keywords.TYPE, Keywords.INTERFACE);
        
        if (kind.isEmpty()) {
            return Option.none();
        }

        // read required signature

        Option<SignatureSource> signatureSource = reader.readSignature();

        if (signatureSource.isEmpty()) {
            return Option.none();
        }

        // read optional extended types

        List<SignatureSource> extendedSignatures = new ArrayList<>(3);
        Option<?> extendsOperator = reader.optionalRead(() -> reader.read(Operators.COLON)
                .flatMap(operator -> reader.readSignature()));

        if (extendsOperator.isDefined()) {
            do {
                extendedSignatures.add(reader.readSignature().get());
            } while (reader.optionalRead(() -> reader.read(Operators.COLON)).isDefined());
        }

        // read optional body

        Snippet body = reader.optionalRead(() -> reader.readSection(Separators.BRACE_LEFT))
                .map(bodyToken -> bodyToken.toToken(Section.class).getContent())
                .orElseGet(PandaSnippet.empty());

        // prepare reference and delegate tasks

        CompletableOption<Type> futureType = new CompletableOption<>();
        StageService stageService = context.getStageService();

        Module module = context.getScript().getModule().orThrow(() -> {
            throw new PandaParserFailure(context, signatureSource.get().getName(), "Cannot add type to module, because script does not declare any of it");
        });

        Reference reference = new Reference(
                futureType,
                module,
                signatureSource.get().getName().getValue(),
                visibility,
                kind.get().getValue(),
                context.getSource().getLocation());
        module.add(reference);

        stageService.delegate("parse type signature", Phases.TYPES, Layer.NEXT_DEFAULT, signaturePhase -> {
            Signature signature = SIGNATURE_PARSER.parse(context, signatureSource.get());

            List<Signature> bases = extendedSignatures.stream()
                    .map(extendedSignature -> SIGNATURE_PARSER.parse(context, extendedSignature))
                    .collect(Collectors.toList());

            CompletableOption<Class<?>> associatedType = new CompletableOption<>();

            Type type = PandaType.builder()
                    .module(module)
                    .name(reference.getName())
                    .signature(signature)
                    .associatedType(associatedType)
                    .bases(bases)
                    .kind(kind.get().getValue())
                    .state(State.of(kind.get().getValue()))
                    .location(context.getSource().getLocation())
                    .visibility(visibility)
                    .build();

            TypeScope scope = new TypeScope(reference.getLocation(), type);

            Context<TypeContext> typeContext = context.forkCreator()
                    .withSubject(new TypeContext(type, scope))
                    .withScope(scope)
                    .toContext();

            stageService.delegate("parse body", Phases.CONTENT, Layer.NEXT_DEFAULT, bodyPhase -> {
                typePoolParser.parse(typeContext, new PandaSourceStream(body));
            });

            stageService.delegate("verify properties", Phases.VERIFY, Layer.NEXT_DEFAULT, verifyPhase -> {
                verifyProperties(typeContext);
            });

            stageService.delegate("generate class", Phases.INITIALIZE, Layer.NEXT_DEFAULT, initializePhase -> {
                associatedType.complete(Object.class); // generate class
            });

            stageService.delegate("initialize fields", Phases.INITIALIZE, Layer.NEXT_DEFAULT, initializePhase -> {
                for (TypeField field : type.getFields().getDeclaredProperties()) {
                    if (!field.isInitialized() && !(field.isNillable() && field.isMutable())) {
                        throw new PandaParserFailure(context, field.getLocation(), "Field " + field + " is not initialized");
                    }

                    field.initialize();
                    futureType.complete(type);
                }
            });
        });

        return Option.of(futureType.toFuture());
    }

    private void verifyProperties(Context<TypeContext> context) {
        Type type = context.getSubject().getType();

        for (Signature base : type.getBases()) {
            State.requireInheritance(context, base.getType().get(), context.getSource());
        }

        if (type.getState() != State.ABSTRACT) {
            PandaStream.of(type.getBases())
                    .mapOpt(base -> base.getType().toOption())
                    .flatMapStream(base -> base.getMethods().getProperties().stream())
                    .filter(TypeMethod::isAbstract)
                    .filter(method -> !type.getMethods().getMethod(method.getSimpleName(), method.getParameterTypes()).isDefined())
                    .forEach(method -> {
                        throw new PandaParserFailure(context, type.getLocation(),
                                "Missing implementation of &1" + method + "&r in &1" + type + "&r",
                                "You have to override all non-default methods declared by " + type + " abstract type"
                        );
                    });
        }

        if (type.getConstructors().getDeclaredProperties().isEmpty()) {
            type.getSuperclass().toStream()
                    .flatMapStream(superSignature -> superSignature.getType().get().getConstructors().getProperties().stream())
                    .find(constructor -> constructor.getParameters().length > 0)
                    .peek(constructorWithParameters -> {
                        throw new PandaParserFailure(context, constructorWithParameters.getLocation(),
                                "Type " + type + " does not implement any constructor from the base type " + constructorWithParameters.getSignature(),
                                "Some of the overridden types may contain custom constructors. To properly initialize object, you have to call one of them."
                        );
                    });

            type.getConstructors().declare(PandaConstructor.builder()
                    .type(type)
                    .callback((typeConstructor, frame, instance, arguments) -> {
                        return context.getSubject().getScope().createInstance(frame, instance, typeConstructor, new Class<?>[0], arguments);
                    })
                    .location(type.getLocation())
                    .build());
        }
    }
    
}
