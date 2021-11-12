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

import javassist.CannotCompileException;
import javassist.NotFoundException;
import panda.interpreter.architecture.module.Module;
import panda.interpreter.architecture.type.PandaType;
import panda.interpreter.architecture.type.Reference;
import panda.interpreter.architecture.type.State;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.architecture.type.TypeScope;
import panda.interpreter.architecture.type.Visibility;
import panda.interpreter.architecture.type.generator.ClassGenerator;
import panda.interpreter.architecture.type.member.constructor.ConstructorScope;
import panda.interpreter.architecture.type.member.constructor.PandaConstructor;
import panda.interpreter.architecture.type.member.field.TypeField;
import panda.interpreter.architecture.type.member.method.TypeMethod;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.signature.TypedSignature;
import panda.interpreter.parser.Component;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.ContextParser;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.pool.PoolParser;
import panda.interpreter.parser.pool.Targets;
import panda.interpreter.parser.stage.Layer;
import panda.interpreter.parser.stage.Phases;
import panda.interpreter.parser.stage.RetryException;
import panda.interpreter.parser.stage.StageService;
import panda.interpreter.token.PandaSnippet;
import panda.interpreter.token.PandaSourceStream;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.auxiliary.Section;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.resource.syntax.operator.Operators;
import panda.interpreter.resource.syntax.separator.Separators;
import panda.interpreter.syntax.PandaSourceReader;
import panda.utilities.ArrayUtils;
import panda.std.reactive.Completable;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class TypeParser implements ContextParser<Object, Type> {

    private static final ClassGenerator TYPE_GENERATOR = new ClassGenerator();
    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();
    private PoolParser<TypeContext> typePoolParser;

    @Override
    public String name() {
        return "type";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.HEAD);
    }

    @Override
    public void initialize(Context<?> context) {
        this.typePoolParser = context.getPoolService().getPool(Targets.TYPE).toParser();
    }

    @Override
    public Option<Completable<Type>> parse(Context<?> context) {
        PandaSourceReader reader = new PandaSourceReader(context.getStream());

        // read optional visibility

        Visibility visibility = reader.optionalRead(reader::readVisibility).orElseGet(Visibility.INTERNAL);

        // read required model

        Option<TokenInfo> kind = reader.readVariant(Keywords.TYPE, Keywords.INTERFACE);
        
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
        Option<?> extendsOperator = reader.optionalRead(() -> reader.read(Operators.COLON));

        if (extendsOperator.isDefined()) {
            do {
                extendedSignatures.add(reader.readSignature().get());
            } while (reader.optionalRead(() -> reader.read(Separators.COMMA)).isDefined());
        }

        // read optional body

        Snippet body = reader.optionalRead(() -> reader.readSection(Separators.BRACE_LEFT))
                .map(bodyToken -> bodyToken.toToken(Section.class).getContent())
                .orElseGet(PandaSnippet.empty());

        // prepare reference and delegate tasks

        Completable<Type> futureType = new Completable<>();
        StageService stageService = context.getStageService();
        Module module = context.getScript().getModule();

        Reference reference = new Reference(
                futureType,
                module,
                signatureSource.get().getName().getValue(),
                visibility,
                kind.get().getValue(),
                context.getSource().getLocation());
        module.add(reference);

        stageService.delegate("parse " + reference.getName() + " type signature", Phases.TYPES, Layer.NEXT_DEFAULT, signaturePhase -> {
            Signature signature = SIGNATURE_PARSER.parse(context, signatureSource.get(), true, null);

            Completable<Class<?>> associatedType = new Completable<>();
            TypeScope scope = new TypeScope(reference.getLocation(), reference);

            Type type = PandaType.builder()
                    .module(module)
                    .name(reference.getSimpleName())
                    .signature(signature)
                    .associatedType(associatedType)
                    .typeScope(scope)
                    .kind(kind.get().getValue())
                    .state(State.of(kind.get().getValue()))
                    .location(context.getSource().getLocation())
                    .visibility(visibility)
                    .build();
            futureType.complete(type);

            TYPE_GENERATOR.allocate(type);

            Context<TypeContext> typeContext = context.forkCreator()
                    .withSubject(new TypeContext(type, scope))
                    .withScope(scope)
                    .toContext();

            stageService.delegate("parse bases", Phases.TYPES, Layer.NEXT_DEFAULT, basePhase -> {
                List<TypedSignature> bases = PandaStream.of(extendedSignatures)
                        .map(extendedSignature -> SIGNATURE_PARSER.parse(context, extendedSignature, false, signature))
                        .throwIfNot(Signature::isTyped, unsupported -> new PandaParserFailure(context, unsupported.getSource(), "Unknown type " + unsupported.toGeneric().getLocalIdentifier()))
                        .map(Signature::toTyped)
                        .toList();

                if (bases.isEmpty()) {
                    bases.add(context.getTypeLoader().requireType("panda/panda@::Object").getSignature());
                }

                bases.forEach(base -> type.addBase(base.toTyped()));
            });

            stageService.delegate("parse " + type.getName() + " type body", Phases.DEFAULT, Layer.NEXT_DEFAULT, bodyPhase -> {
                typePoolParser.parse(typeContext, new PandaSourceStream(body));

                if (type.getConstructors().getDeclaredProperties().isEmpty()) {
                    type.getSuperclass().toStream()
                            .flatMapStream(superSignature -> superSignature.fetchType().getConstructors().getProperties().stream())
                            .find(constructor -> constructor.getParameters().size() > 0)
                            .peek(constructorWithParameters -> {
                                throw new PandaParserFailure(context, constructorWithParameters.getLocation(),
                                        "Type " + type + " does not implement any constructor from the base type " + constructorWithParameters.getType(),
                                        "Some of the overridden types may contain custom constructors. To properly initialize object, you have to call one of them."
                                );
                            });

                    ConstructorScope defaultConstructorScope = new ConstructorScope(type.getLocation(), Collections.emptyList());

                    type.getConstructors().declare(PandaConstructor.builder()
                            .type(type)
                            .invoker(defaultConstructorScope)
                            .baseCall(defaultConstructorScope::getBaseCall)
                            .location(type.getLocation())
                            .returnType(type.getSignature())
                            .build());
                }
            });

            stageService.delegate("verify " + type.getName() + "type properties", Phases.VERIFY, Layer.NEXT_DEFAULT, verifyPhase -> {
                for (TypedSignature base : type.getBases()) {
                    State.requireInheritance(context, base.fetchType(), context.getSource());
                }

                if (type.getState() != State.ABSTRACT) {
                    PandaStream.of(type.getBases())
                            .map(TypedSignature::fetchType)
                            .flatMapStream(base -> base.getMethods().getProperties().stream())
                            .filter(TypeMethod::isAbstract)
                            .filter(method -> !type.getMethods().getMethod(method.getSimpleName(), method.getParameterSignatures()).isDefined())
                            .forEach(method -> {
                                throw new PandaParserFailure(context, type.getLocation(),
                                        "Missing implementation of &1" + method + "&r in &1" + type + "&r",
                                        "You have to override all non-default methods declared by " + type + " abstract type"
                                );
                            });
                }
            });

            stageService.delegate("generate type class", Phases.INITIALIZE, Layer.NEXT_DEFAULT, initializePhase -> {
                Option<? extends TypedSignature> superclass = type.getSuperclass();

                if (superclass.isDefined() && !superclass.get().fetchType().getAssociated().isReady()) {
                    throw new RetryException();
                }

                try {
                    TYPE_GENERATOR.generate(type);
                    associatedType.complete(TYPE_GENERATOR.complete(type));
                } catch (CannotCompileException exception) {
                    throw new PandaParserFailure(exception, context, context.getSource(), "Cannot generate associated java type" + exception.getReason(), "");
                } catch (NotFoundException exception) {
                    throw new PandaParserFailure(exception, context, context.getSource(), "Cannot generate associated java type", "");
                }
            });

            stageService.delegate("initialize fields", Phases.INITIALIZE, Layer.NEXT_DEFAULT, initializePhase -> {
                for (TypeField field : type.getFields().getDeclaredProperties()) {
                    if (!field.isInitialized() && !(field.isNillable() && field.isMutable())) {
                        throw new PandaParserFailure(context, field.getLocation(), "Field " + field + " is not initialized");
                    }

                    field.initialize();
                }
            });
        });

        return Option.of(futureType);
    }
    
}
