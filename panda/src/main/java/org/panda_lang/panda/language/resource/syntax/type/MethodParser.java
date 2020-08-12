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
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.type.PropertyParameter;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypeMethod;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SnippetUtils;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.architecture.module.PandaImportsUtils;
import org.panda_lang.language.architecture.type.MethodScope;
import org.panda_lang.language.architecture.type.PandaMethod;
import org.panda_lang.language.architecture.type.utils.TypedUtils;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.stage.Stages;
import org.panda_lang.language.interpreter.pattern.Mappings;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.Phases;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.panda.language.resource.syntax.scope.branching.Returnable;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;

public final class MethodParser extends ParserBootstrap<Void> {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Pipelines.TYPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.PROTOTYPE_METHOD;
    }

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer.functional(pattern -> pattern
                .keyword(Keywords.OVERRIDE).optional()
                .variant("visibility").optional().consume(variant -> variant.content("open", "shared", "internal").map(value -> Visibility.valueOf(value.toString().toUpperCase())))
                .keyword(Keywords.STATIC).optional()
                .wildcard("name").verifyType(TokenTypes.UNKNOWN, TokenTypes.SEQUENCE)
                .section("parameters", Separators.PARENTHESIS_LEFT)
                .subPattern("return-type", sub -> sub
                        .unit("arrow", Operators.ARROW.getValue())
                        .type("type").verifyNextSection(Separators.BRACE_LEFT)
                ).optional()
                .section("body", Separators.BRACE_LEFT).optional());
    }

    @Autowired(order = 1, stage = Stages.TYPES_LABEL)
    public void parseReturnType(Context context, LocalChannel channel, @Ctx Imports imports, @Src("type") Snippet returnTypeName) {
        Option.of(returnTypeName)
                .map(value -> PandaImportsUtils.getTypeOrThrow(context, returnTypeName,
                        "Unknown type {name}",
                        "Make sure that the name does not have a typo and module which should contain that class is imported"
                ))
                .orElse(() -> context.getComponent(Components.IMPORTS).forName("void"))
                .peek(type -> channel.allocated("type", type));
    }

    @Autowired(order = 2, stage = Stages.TYPES_LABEL)
    public void parseParameters(Context context, LocalChannel channel, @Src("name") TokenInfo name, @Src("parameters") Snippet parametersSource) {
        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, parametersSource);
        MethodScope methodScope = new MethodScope(name.getLocation(), parameters);
        channel.allocated("scope", methodScope);
    }

    @Autowired(order = 3, stage = Stages.TYPES_LABEL)
    public void verifyData(
        Context context,
        LocalChannel channel,
        @Ctx Type type,
        @Channel Mappings mappings,
        @Channel Location location,
        @Channel Type returnType,
        @Channel MethodScope scope,
        @Src("name") TokenInfo name
    ) {
        Option<TypeMethod> existingMethod = type.getMethods().getMethod(name.getValue(), TypedUtils.toTypes(scope.getParameters()));

        existingMethod
                .filter(TypeMethod::isNative)
                .peek(method -> channel.allocated("native", true));

        existingMethod
                .filterNot(method -> mappings.has(Keywords.OVERRIDE))
                .peek(method -> {
                    throw new PandaParserFailure(context, name,
                            "Method &b" + name + "&r overrides &b" + existingMethod.get() + "&r but does not contain&b override&r modifier",
                            "Add missing modifier if you want to override that method or rename current method"
                    );
                });

        existingMethod
                .map(TypeMethod::getReturnType)
                .filterNot(existingReturnType -> existingReturnType.isAssignableFrom(returnType))
                .peek(existingReturnType -> {
                    throw new PandaParserFailure(context, name,
                            "&rMethod &b" + name + "&r overrides &b" + existingMethod.get() + "&r but does not return the same type",
                            "Change return type if you want to override that method or rename current method"
                    );
                });

        mappings.get("visibility")
                .orElse(() -> existingMethod.map(TypeMethod::getVisibility))
                .peek(visibility -> channel.allocated("visibility", visibility))
                .orThrow(() -> {
                    throw new PandaParserFailure(context, name, "Missing visibility");
                });
    }

    @Autowired(order = 4, stage = Stages.TYPES_LABEL)
    public void declareMethod(LocalChannel channel, @Ctx Type type, @Channel Mappings mappings, @Src("name") TokenInfo name, @Channel Type returnType, @Channel MethodScope scope, @Src("body") Snippet body) {
        TypeMethod method = PandaMethod.builder()
                .type(type)
                .parameters(scope.getParameters())
                .name(name.getValue())
                .location(scope.getSourceLocation())
                .isAbstract(body == null)
                .visibility(channel.get("visibility"))
                .returnType(returnType)
                .isStatic(mappings.has("static"))
                .isNative(channel.contains("native"))
                .body(scope)
                .build();

        type.getMethods().declare(method);
        channel.allocated("method", method);
    }

    @Autowired(order = 5, phase = Phases.NEXT_DEFAULT)
    public void parse(Context context, @Channel MethodScope methodScope, @Channel TypeMethod method, @Nullable @Src("body") Snippet body) throws Exception {
        if (!SnippetUtils.isEmpty(body)) {
            SCOPE_PARSER.parse(context, methodScope, body);
        }

        if (!method.getReturnType().getAssociatedClass().isAssignableTo(void.class) && !methodScope.hasEffective(Returnable.class)) {
            throw new PandaParserFailure(context, "Missing return statement in method " + method.getName());
        }
    }

}
