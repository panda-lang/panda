/*
 * Copyright (c) 2015-2020 Dzikoysk
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

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.architecture.type.PropertyParameter;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SnippetUtils;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.architecture.type.MethodScope;
import org.panda_lang.framework.language.architecture.type.PandaMethod;
import org.panda_lang.framework.language.architecture.type.utils.TypedUtils;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SectionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.TypeElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.UnitElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.NextTokenTypeVerifier;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Int;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.CustomPatternHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.panda.language.resource.syntax.scope.branching.Returnable;

import java.util.List;
import java.util.function.Supplier;

@RegistrableParser(pipeline = Pipelines.TYPE_LABEL, priority = PandaPriorities.PROTOTYPE_METHOD)
public final class MethodParser extends ParserBootstrap<Void> {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new CustomPatternHandler())
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        KeywordElement.create(Keywords.OVERRIDE).optional(),
                        VariantElement.create("visibility").content("public", "shared", "internal").map(value -> Visibility.valueOf(value.toString().toUpperCase())),
                        UnitElement.create("static").content("static").optional(),
                        TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN)),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                        SectionElement.create("parameters"),
                        SectionElement.create("body").optional()
                ));
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, LocalData local, @Ctx Type type, @Int SourceLocation location, @Int Result result, @Src("type") Snippet typeName, @Src("body") Snippet body) {
        Type returnType = Option.of(typeName)
                .map(value -> context.getComponent(Components.IMPORTS)
                        .forName(typeName.asSource())
                        .getOrElseThrow((Supplier<? extends PandaParserFailure>) () -> {
                            throw new PandaParserFailure(context, typeName,
                                    "Unknown type",
                                    "Make sure that the name does not have a typo and module which should contain that class is imported"
                            );
                        }))
                .getOrElse(() -> {
                    TypeLoader typeLoader = context.getComponent(Components.TYPE_LOADER);
                    return typeLoader.requireType(void.class);
                });

        TokenRepresentation name = result.get("name");
        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, result.get("parameters"));
        MethodScope methodScope = local.allocated(new MethodScope(name.getLocation(), parameters));

        Option<TypeMethod> existingMethod = type.getMethods().getMethod(name.getValue(), TypedUtils.toTypes(parameters));

        if (existingMethod.isDefined() && !result.has(Keywords.OVERRIDE.getValue())) {
            throw new PandaParserFailure(context, name,
                    "Method &b" + name + "&r overrides &b" + existingMethod.get() + "&r but does not contain&b override&r modifier",
                    "Add missing modifier if you want to override that method or rename current method"
            );
        }

        PandaMethod method = local.allocated(PandaMethod.builder()
                .type(type)
                .parameters(parameters)
                .name(name.getValue())
                .location(location)
                .isAbstract(body == null)
                .visibility(result.get("visibility"))
                .returnType(returnType)
                .isStatic(result.has("static"))
                .isNative(existingMethod.isDefined() && existingMethod.get().isNative())
                .body(methodScope)
                .build());

        type.getMethods().declare(method);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parse(Context context, @Local MethodScope methodScope, @Local TypeMethod method, @Nullable @Src("body") Snippet body) throws Exception {
        if (!SnippetUtils.isEmpty(body)) {
            SCOPE_PARSER.parse(context, methodScope, body);
        }

        if (!method.getReturnType().getAssociatedClass().isAssignableTo(void.class) && !methodScope.hasEffective(Returnable.class)) {
            throw new PandaParserFailure(context, "Missing return statement in method " + method.getName());
        }
    }

}
