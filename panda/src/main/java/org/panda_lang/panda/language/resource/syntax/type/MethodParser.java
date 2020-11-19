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

import org.panda_lang.language.architecture.expression.ThisExpression;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypeContext;
import org.panda_lang.language.architecture.type.TypedUtils;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.member.method.MethodScope;
import org.panda_lang.language.architecture.type.member.method.PandaMethod;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.parser.stage.Layer;
import org.panda_lang.language.interpreter.parser.stage.Phases;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.panda.language.resource.syntax.scope.StandaloneExpression;
import org.panda_lang.panda.language.resource.syntax.scope.branching.Returnable;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public void initialize(Context<? extends TypeContext> context) {
        this.scopeParser = new ScopeParser(context.getPoolService());
        this.voidType = context.getTypeLoader().requireType("panda::Void");
    }

    @Override
    public Option<CompletableFuture<TypeMethod>> parse(Context<? extends TypeContext> context) {
        PandaSourceReader sourceReader = new PandaSourceReader(context.getStream());

        boolean overrides = sourceReader.optionalRead(() -> sourceReader.read(Keywords.OVERRIDE)).isDefined();
        Option<Visibility> visibility = sourceReader.readVariant(Keywords.OPEN, Keywords.SHARED, Keywords.INTERNAL).map(Visibility::of);

        if (visibility.isDefined()) {
            return Option.none();
        }

        boolean isStatic = sourceReader.optionalRead(() -> sourceReader.read(Keywords.STATIC)).isDefined();

        Option<String> name = sourceReader.optionalRead(() -> sourceReader.read(TokenTypes.UNKNOWN))
                .orElse(() -> sourceReader.optionalRead(() -> sourceReader.read(TokenTypes.SEQUENCE)))
                .map(TokenInfo::getValue);

        if (name.isEmpty()) {
            return Option.none();
        }

        Option<Snippet> parametersSource = sourceReader.readArguments();

        if (parametersSource.isEmpty()) {
            return Option.none();
        }

        Signature returnType = voidType.getSignature();

        if (sourceReader.optionalRead(() -> sourceReader.read(Operators.ARROW)).isDefined()) {
            returnType = sourceReader.optionalRead(() -> sourceReader.read(Keywords.SELF))
                    .map(self -> (Signature) context.getSubject().getType().getSignature())
                    // TODO: parent signature
                    .orElse(() -> sourceReader.readSignature().map(signatureSource -> SIGNATURE_PARSER.parse(null, context, signatureSource)))
                    .orThrow(() -> {
                        throw new PandaParserFailure(context, "Missing return signature");
                    });
        }

        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, parametersSource.get());
        MethodScope methodScope = new MethodScope(context, parameters);

        Option<Snippet> body = sourceReader.optionalRead(sourceReader::readBody);

        Type type = context.getSubject().getType();
        Option<TypeMethod> existingMethod = type.getMethods().getMethod(name.get(), TypedUtils.toTypes(parameters));
        boolean isNative = existingMethod.filter(TypeMethod::isNative).isDefined();

        if (overrides && existingMethod.isEmpty()) {
            throw new PandaParserFailure(context, context.getSource(),
                    "&1Method &b" + name + "&1 is defined as overridden, but there is no such a method in parent type",
                    "Compare method signature with a target signature and apply required changes or remove this modifier to create independent method"
            );
        }

        existingMethod
                .filterNot(method -> overrides)
                .peek(method -> {
                    throw new PandaParserFailure(context, context.getSource(),
                            "&rMethod &b" + name + "&r overrides &b" + existingMethod.get() + "&r but does not contain&b override&r modifier",
                            "Add missing modifier if you want to override that method or rename current method"
                    );
                });

        existingMethod
                .map(TypeMethod::getReturnType)
                .filterNot(returnType::isAssignableFrom)
                .peek(existingReturnType -> {
                    throw new PandaParserFailure(context, context.getSource(),
                            "&rMethod &b" + name + "&r overrides &b" + existingMethod.get() + "&r but does not return the same type",
                            "Change return type if you want to override that method or rename current method"
                    );
                });

        TypeMethod method = PandaMethod.builder()
                .type(type)
                .parameters(parameters)
                .name(name.get())
                .location(context)
                .isAbstract(body == null)
                .visibility(visibility.get())
                .returnType(returnType)
                .isStatic(isStatic)
                .isNative(isNative)
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

            if (!method.getReturnType().toTyped().fetchType().is("panda::Void") && !methodScope.hasEffective(Returnable.class)) {
                if (method.getReturnType().equals(type.getSignature())) {
                    methodScope.addStatement(new StandaloneExpression(context, ThisExpression.of(type.getSignature())));
                    return;
                }

                throw new PandaParserFailure(context, body != null ? body.get().getLastLine() : context.getSource(), "Missing return statement in method " + method.getName());
            }
        });

        return Option.of(CompletableFuture.completedFuture(method));
    }

}
