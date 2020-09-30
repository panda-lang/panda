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
import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.PandaType;
import org.panda_lang.language.architecture.type.State;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.TypeComponents;
import org.panda_lang.language.architecture.type.TypeModels;
import org.panda_lang.language.architecture.type.TypeScope;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.member.constructor.PandaConstructor;
import org.panda_lang.language.architecture.type.member.field.TypeField;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.parser.stage.Phases;
import org.panda_lang.language.interpreter.parser.stage.Stages;
import org.panda_lang.language.interpreter.pattern.Mappings;
import org.panda_lang.language.interpreter.pattern.functional.elements.GenericsElement;
import org.panda_lang.language.interpreter.pattern.functional.verifiers.TokenVerifier;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.Collection;

public final class TypeParser extends AutowiredParser<Void> {

    private static final PipelineParser<?> TYPE_PIPELINE_PARSER = new PipelineParser<>(Pipelines.TYPE);

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Pipelines.HEAD);
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        return initializer.functional(pattern -> pattern
                .variant("visibility").consume(variant -> variant.content(Keywords.OPEN, Keywords.SHARED, Keywords.INTERNAL)).optional()
                .variant("model").consume(variant -> variant.content(Keywords.CLASS, Keywords.TYPE, Keywords.INTERFACE))
                .wildcard("name").verifyType(TokenTypes.UNKNOWN)
                .element(GenericsElement.create("generics"))
                .subPattern("extended", sub -> sub
                        .unit("extends", ":")
                        .custom("inherited").consume(custom -> custom.reader((data, source) -> TypeParserUtils.readTypes(source)))
                ).optional()
                .section("body", Separators.BRACE_LEFT));
    }

    @Autowired(order = 0, stage = Stages.TYPES_LABEL)
    public void parse(Context context, @Channel Location location, @Channel Mappings mappings, @Ctx Script script, @Src("model") String model, @Src("name") String name) {
        Visibility visibility = mappings.get("visibility")
                .map(Visibility::of)
                .orElseGet(Visibility.INTERNAL);

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
    }

    @Autowired(order = 1, stage = Stages.TYPES_LABEL, phase = Phases.CURRENT_AFTER)
    public void parseDeclaration(Context context, @Ctx Type type, @Ctx TypeLoader loader, @Nullable @Src("inherited") Collection<Snippetable> inherited) {
        if (inherited != null) {
            inherited.forEach(typeSource -> TypeParserUtils.appendExtended(context, type, typeSource));
        }

        if (TypeModels.isClass(type) && type.getBases().stream().noneMatch(TypeModels::isClass)) {
            type.addBase(loader.requireType(Object.class));
        }
    }

    @Autowired(order = 2, stage = Stages.TYPES_LABEL, phase = Phases.NEXT_BEFORE)
    public Object parseBody(Context context, @Ctx Type type, @Src("body") Snippet body) {
        return TYPE_PIPELINE_PARSER.parse(context, new PandaSourceStream(body));
    }

    @Autowired(order = 3, stage = Stages.TYPES_LABEL, phase = Phases.CURRENT_AFTER)
    public void verifyProperties(Context context, @Ctx Type type, @Ctx TypeScope scope) {
        if (type.getState() != State.ABSTRACT) {
            type.getBases().stream()
                    .flatMap(base -> base.getMethods().getProperties().stream())
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
            type.getSuperclass().peek(superclass -> PandaStream.of(superclass.getConstructors().getDeclaredProperties())
                    .find(constructor -> constructor.getParameters().length > 0)
                    .peek(constructorWithParameters -> {
                        throw new PandaParserFailure(context, constructorWithParameters.getLocation(),
                                "Type " + type + " does not implement any constructor from the base type " + constructorWithParameters.getSignature(),
                                "Some of the overridden types may contain custom constructors. To properly initialize object, you have to call one of them."
                        );
                    })
            );

            type.getConstructors().declare(PandaConstructor.builder()
                    .type(type)
                    .callback((typeConstructor, frame, instance, arguments) -> scope.createInstance(frame, instance, typeConstructor, new Class<?>[0], arguments))
                    .location(type.getLocation())
                    .build());
        }
    }

    @Autowired(order = 4, stage = Stages.CONTENT_LABEL, phase = Phases.CURRENT_AFTER)
    public void verifyContent(Context context, @Ctx Type type) {
        for (TypeField field : type.getFields().getDeclaredProperties()) {
            if (!field.isInitialized() && !(field.isNillable() && field.isMutable())) {
                throw new PandaParserFailure(context, field.getLocation(), "Field " + field + " is not initialized");
            }

            field.initialize();
        }
    }

}
