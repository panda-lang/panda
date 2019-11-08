/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.PropertyParameter;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.architecture.prototype.Referencable;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.architecture.prototype.MethodScope;
import org.panda_lang.framework.language.architecture.prototype.PandaMethod;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
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
import org.panda_lang.framework.language.resource.internal.java.JavaModule;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.CustomPatternHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RegistrableParser(pipeline = Pipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_METHOD)
public final class MethodParser extends ParserBootstrap {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new CustomPatternHandler())
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        KeywordElement.create(Keywords.OVERRIDE).optional(),
                        VariantElement.create("visibility").content("public", "shared", "local").map(value -> Visibility.valueOf(value.toString().toUpperCase())),
                        UnitElement.create("static").content("static").optional(),
                        TypeElement.create("type").optional().verify(new NextTokenTypeVerifier(TokenTypes.UNKNOWN)),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                        SectionElement.create("parameters"),
                        SectionElement.create("body").optional()
                ));
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, LocalData local, @Component Prototype prototype, @Inter SourceLocation location, @Inter Result result, @Src("type") Snippet type, @Src("body") Snippet body) {
        Referencable returnTypeReferencable = Optional.ofNullable(type)
                .map(value ->  context.getComponent(Components.IMPORTS)
                        .forName(type.asSource())
                        .map(reference -> (Referencable) reference)
                        .orElseThrow((Supplier<? extends PandaParserFailure>) () -> {
                                throw new PandaParserFailure(context, type,
                                        "Unknown type",
                                        "Make sure that the name does not have a typo and module which should contain that class is imported"
                                );
                        }))
                .orElse(JavaModule.VOID);

        TokenRepresentation name = result.get("name");
        List<PropertyParameter> parameters = PARAMETER_PARSER.parse(context, result.get("parameters"));
        MethodScope methodScope = local.allocated(new MethodScope(name.getLocation(), parameters));

        PandaMethod method = PandaMethod.builder()
                .prototype(prototype)
                .parameters(parameters)
                .name(name.getValue())
                .location(location)
                .isAbstract(body == null)
                .visibility(result.get("visibility"))
                .returnType(returnTypeReferencable.toReference().fetch())
                .isStatic(result.has("static"))
                .methodBody(methodScope)
                .build();

        Optional<PrototypeMethod> existingMethod = prototype.getMethods().getMethod(method.getSimpleName(), method.getParameterTypes());

        if (existingMethod.isPresent() && !result.has(Keywords.OVERRIDE.getValue())) {
            throw new PandaParserFailure(context, name,
                    "Method &b" + method.getPropertyName() + "&r overrides &b" + existingMethod.get() + "&r but does not contain&b override&r modifier",
                    "Add missing modifier if you want to override that method or rename current method"
            );
        }

        prototype.getMethods().declare(method);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parse(Context delegatedContext, @Local MethodScope methodScope, @Nullable @Src("body") Snippet body) throws Exception {
        if (body != null) {
            SCOPE_PARSER.parse(delegatedContext, methodScope, body);
        }
    }

}
