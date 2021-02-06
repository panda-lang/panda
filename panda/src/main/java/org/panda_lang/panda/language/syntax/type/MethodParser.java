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

package org.panda_lang.panda.language.syntax.type;

import org.panda_lang.framework.architecture.expression.ThisExpression;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.TypeContext;
import org.panda_lang.framework.architecture.type.TypedUtils;
import org.panda_lang.framework.architecture.type.Visibility;
import org.panda_lang.framework.architecture.type.member.method.MethodScope;
import org.panda_lang.framework.architecture.type.member.method.PandaMethod;
import org.panda_lang.framework.architecture.type.member.method.TypeMethod;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.ContextParser;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.pool.Targets;
import org.panda_lang.framework.interpreter.parser.stage.Layer;
import org.panda_lang.framework.interpreter.parser.stage.Phases;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.syntax.PandaSourceReader;
import org.panda_lang.panda.language.syntax.ScopeParser;
import org.panda_lang.panda.language.syntax.PandaPriorities;
import org.panda_lang.panda.language.syntax.scope.StandaloneExpression;
import org.panda_lang.panda.language.syntax.scope.branching.Returnable;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.framework.interpreter.parser.Component;
import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.List;

public final class MethodParser implements ContextParser<TypeContext, TypeMethod> {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();
    private ScopeParser scopeParser;
    private Type voidType;

    @Override
    public String name() {
        return "method";
    }

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.TYPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.PROTOTYPE_METHOD;
    }

    @Override
    public void initialize(Context<?> context) {
        this.scopeParser = new ScopeParser(context.getPoolService());
        this.voidType = context.getTypeLoader().requireType("panda/panda@::Void");
    }

    @Override
    public Option<Completable<TypeMethod>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        // read visibility, optional if overridden

        boolean overrides = sourceReader.optionalRead(() -> sourceReader.read(Keywords.OVERRIDE)).isDefined();

        Option<Visibility> visibility = sourceReader.optionalRead(() -> sourceReader.readVariant(Keywords.OPEN, Keywords.SHARED, Keywords.INTERNAL))
                .map(Visibility::of);

        if (visibility.isEmpty() && !overrides) {
            return Option.none();
        }

        // read optional static modifier

        boolean isStatic = sourceReader.optionalRead(() -> sourceReader.read(Keywords.STATIC)).isDefined();

        // read name

        Option<String> name = sourceReader.optionalRead(() -> sourceReader.read(TokenTypes.UNKNOWN))
                .orElse(() -> sourceReader.optionalRead(() -> sourceReader.read(TokenTypes.SEQUENCE)))
                .map(TokenInfo::getValue);

        if (name.isEmpty()) {
            return Option.none();
        }

        // read parameters

        Option<List<PropertyParameter>> parameters = sourceReader.readArguments()
                .map(parametersSource -> PARAMETER_PARSER.parse(context, parametersSource));

        if (parameters.isEmpty()) {
            return Option.none();
        }

        // read return type

        Type type = context.getSubject().getType();
        Signature returnType;

        if (sourceReader.optionalRead(() -> sourceReader.read(Operators.ARROW)).isDefined()) {
            returnType = sourceReader.optionalRead(() -> sourceReader.read(Keywords.SELF))
                    .map(self -> (Signature) context.getSubject().getType().getSignature())
                    // TODO: parent signature
                    .orElse(() -> sourceReader.readSignature().map(signatureSource -> SIGNATURE_PARSER.parse(context, signatureSource, false, type.getSignature())))
                    .orThrow(() -> {
                        throw new PandaParserFailure(context, "Missing return signature");
                    });
        }
        else {
            returnType = voidType.getSignature();
        }

        // read body

        Option<Snippet> body = sourceReader.optionalRead(sourceReader::readBody);

        // create method

        MethodScope methodScope = new MethodScope(context, parameters.get());

        TypeMethod method = PandaMethod.builder()
                .type(type)
                .parameters(parameters.get())
                .name(name.get())
                .location(context)
                .isAbstract(body.isEmpty())
                .visibility(visibility.orElseGet(Visibility.OPEN))
                .returnType(returnType)
                .isStatic(isStatic)
                .body(methodScope)
                .build();
        type.getMethods().declare(method);

        context.getStageService().delegate("parse method body", Phases.CONTENT, Layer.NEXT_DEFAULT, contentPhase -> {
            body.peek(source -> scopeParser.parse(context, methodScope, source));
        });

        context.getStageService().delegate("verify return statement", Phases.VERIFY, Layer.NEXT_DEFAULT, verifyPhase -> {
            if (method.isAbstract()) {
                return;
            }

            if (!context.getTypeLoader().requireType("panda/panda@::Void").isAssignableFrom(method.getReturnType().getKnownType()) && !methodScope.hasEffective(Returnable.class)) {
                if (method.getReturnType().equals(type.getSignature())) {
                    methodScope.addStatement(new StandaloneExpression(context, ThisExpression.of(type.getSignature())));
                    return;
                }

                throw new PandaParserFailure(context, body.isDefined() ? body.get().getLastLine() : context.getSource(), "Missing return statement in method " + method.getName());
            }
        });

        context.getStageService().delegate("verify method", Phases.VERIFY, Layer.NEXT_DEFAULT, verifyPhase -> {
            Option<TypeMethod> existingMethod = PandaStream.of(type.getBases())
                    .flatMap(base -> base.fetchType().getMethods().getMethod(name.get(), TypedUtils.toTypes(parameters.get())))
                    .any();

            if (overrides && existingMethod.isEmpty()) {
                throw new PandaParserFailure(context, context.getSource(),
                        "&1Method &b" + name + "&1 is defined as overridden, but there is no such a method in parent type",
                        "Compare method signature with a target signature and apply required changes or remove this modifier to create independent method"
                );
            }

            existingMethod
                    .filterNot(property -> overrides)
                    .peek(property -> {
                        throw new PandaParserFailure(context, context.getSource(),
                                "&rMethod &b" + name.get() + "&r overrides &b" + existingMethod.get() + "&r but does not contain&b override&r modifier",
                                "Add missing modifier if you want to override that method or rename current method"
                        );
                    });

            existingMethod
                    .map(TypeMethod::getReturnType)
                    .filterNot(parentReturnType -> parentReturnType.isAssignableFrom(returnType))
                    .peek(existingReturnType -> {
                        throw new PandaParserFailure(context, context.getSource(),
                                "&rMethod &b" + method + "&r overrides &b" + existingMethod.get() + "&r but does not return the same type",
                                "Change return type if you want to override that method or rename current method"
                        );
                    });
        });

        return Option.ofCompleted(method);
    }

}
