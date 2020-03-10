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

import io.vavr.collection.Stream;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.Script;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.architecture.type.State;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.design.architecture.type.TypeModels;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.architecture.type.PandaConstructor;
import org.panda_lang.framework.language.architecture.type.PandaType;
import org.panda_lang.framework.language.architecture.type.TypeComponents;
import org.panda_lang.framework.language.architecture.type.TypeScope;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.CustomElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SectionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SubPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.UnitElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Int;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.CustomPatternHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;

import java.util.Collection;

@RegistrableParser(pipeline = Pipelines.HEAD_LABEL)
public final class TypeParser extends ParserBootstrap<Void> {

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new CustomPatternHandler())
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        VariantElement.create("visibility").content(Keywords.PUBLIC, Keywords.SHARED, Keywords.INTERNAL).optional(),
                        VariantElement.create("model").content(Keywords.CLASS, Keywords.TYPE, Keywords.INTERFACE),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenTypes.UNKNOWN)),
                        SubPatternElement.create("extended").optional().of(
                                UnitElement.create("extends").content(":"),
                                CustomElement.create("inherited").custom((data, source) -> TypeParserUtils.readTypes(source))
                        ),
                        SectionElement.create("body")
                ));
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, @Int SourceLocation location, @Int Result result, @Ctx Script script, @Src("model") String model, @Src("name") String name) throws Exception {
        Visibility visibility = result.has("visibility") ? Visibility.of(result.get("visibility")) : Visibility.INTERNAL;

        if (Keywords.TYPE.getValue().equals(model)) {
            model = Keywords.CLASS.getValue();
        }

        Type type = PandaType.builder()
                .name(name)
                .location(location)
                .module(script.getModule())
                .model(model)
                .state(State.of(model))
                .visibility(visibility)
                .build();

        TypeScope typeScope = new TypeScope(location, type);
        script.getModule().add(type);

        context.withComponent(Components.SCOPE, typeScope)
                .withComponent(TypeComponents.PROTOTYPE_SCOPE, typeScope)
                .withComponent(TypeComponents.PROTOTYPE, type);

        System.out.println("parse " + type);
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.CURRENT_AFTER)
    void parseDeclaration(Context context, @Ctx Type type, @Ctx TypeLoader loader, @Nullable @Src("inherited") Collection<Snippetable> inherited) {
        System.out.println("parse declaration " + type);

        if (inherited != null) {
            inherited.forEach(typeSource -> TypeParserUtils.appendExtended(context, type, typeSource));
        }

        if (TypeModels.isClass(type) && type.getBases().stream().noneMatch(TypeModels::isClass)) {
            type.addBase(loader.requireType(Object.class));
        }
    }

    @Autowired(order = 2, cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.NEXT_BEFORE)
    Object parseBody(Context context, @Ctx Type type, @Src("body") Snippet body) throws Exception {
        System.out.println("parse body " + type);
        return new PipelineParser<>(Pipelines.TYPE, new PandaSourceStream(body), context).parse();
    }

    @Autowired(order = 3, cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.CURRENT_AFTER)
    void verifyProperties(Context context, @Ctx Type type, @Ctx TypeScope scope) {
        System.out.println("verify " + type);

        if (type.getState() != State.ABSTRACT) {
            type.getBases().stream()
                    .flatMap(base -> base.getMethods().getProperties().stream())
                    .filter(TypeMethod::isAbstract)
                    .filter(method -> !type.getMethods().getMethod(method.getSimpleName(), method.getParameterTypes()).isDefined())
                    .forEach(method -> {
                        throw new PandaParserFailure(context, "Missing implementation of &1" + method + "&r in &1" + type + "&r");
                    });
        }

        if (type.getConstructors().getDeclaredProperties().isEmpty()) {
            type.getSuperclass().peek(superclass -> Stream.ofAll(superclass.getConstructors().getDeclaredProperties())
                    .find(constructor -> constructor.getParameters().length > 0)
                    .peek(constructorWithParameters -> {
                        throw new PandaParserFailure(context, "Type " + type + " does not implement any constructor from the base type " + constructorWithParameters.getType());
                    })
            );

            type.getConstructors().declare(PandaConstructor.builder()
                    .type(type)
                    .callback((frame, instance, arguments) -> scope.createInstance(frame, new Class<?>[0], arguments))
                    .location(type.getLocation())
                    .build());
        }
    }

    @Autowired(order = 4, cycle = GenerationCycles.CONTENT_LABEL, delegation = Delegation.CURRENT_AFTER)
    void verifyContent(Context context, @Ctx Type type) {
        for (TypeField field : type.getFields().getDeclaredProperties()) {
            if (!field.isInitialized() && !(field.isNillable() && field.isMutable())) {
                throw new PandaParserFailure(context, "Field " + field + " is not initialized");
            }

            field.initialize();
        }
    }

}
