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

package org.panda_lang.panda.language.interpreter.parser.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.Script;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.architecture.prototype.State;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.architecture.prototype.PrototypeScope;
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
import org.panda_lang.framework.language.resource.internal.java.JavaModule;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.bootstraps.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.bootstraps.context.handlers.CustomPatternHandler;
import org.panda_lang.panda.language.interpreter.bootstraps.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;

import java.util.Collection;
import java.util.Optional;

@RegistrableParser(pipeline = Pipelines.HEAD_LABEL)
public final class PrototypeParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new CustomPatternHandler())
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        VariantElement.create("visibility").content(Keywords.PUBLIC, Keywords.SHARED, Keywords.LOCAL).optional(),
                        VariantElement.create("state").content(Keywords.CLASS, Keywords.INTERFACE),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenType.UNKNOWN)),
                        SubPatternElement.create("extended").optional().of(
                                UnitElement.create("extends").content(":"),
                                CustomElement.create("inherited").custom((data, source) -> PrototypeParserUtils.readTypes(source))
                        ),
                        SectionElement.create("body")
                ));
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, @Inter SourceLocation location, @Inter Result result, @Component Script script, @Src("state") String state, @Src("name") String name) throws Exception {
        Visibility visibility = result.has("visibility") ? Visibility.of(result.get("visibility")) : Visibility.LOCAL;

        Prototype prototype = PandaPrototype.builder()
                .name(name)
                .location(location)
                .module(script.getModule())
                .type(state)
                .state(State.of(state))
                .visibility(visibility)
                .associated(PrototypeParserUtils.generateType(name))
                .build();

        PrototypeScope prototypeScope = new PrototypeScope(location, prototype);
        context.withComponent(Components.SCOPE, prototypeScope)
                .withComponent(PrototypeComponents.PROTOTYPE_SCOPE, prototypeScope)
                .withComponent(PrototypeComponents.PROTOTYPE, prototype);

        prototype.getModule().add(prototype);
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.CURRENT_AFTER)
    void parseDeclaration(Context context, @Component Prototype prototype, @Nullable @Src("inherited") Collection<Snippetable> inherited) {
        Optional.ofNullable(inherited)
                .ifPresent(classes -> classes.forEach(typeSource -> PrototypeParserUtils.appendExtended(context, prototype, typeSource)));

        if (prototype.getBases().stream().noneMatch(base -> base.getType().equals("class"))) {
            prototype.addBase(JavaModule.OBJECT);
        }
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.NEXT_AFTER)
    Object parseBody(Context context, @Src("body") Snippet body) throws Exception {
        return new PipelineParser<>(Pipelines.PROTOTYPE, new PandaSourceStream(body), context).parse();
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void verifyProperties(Context context, @Component Prototype prototype, @Component PrototypeScope scope) {
        if (prototype.getState() != State.ABSTRACT) {
            prototype.getBases().stream()
                    .flatMap(base -> base.getMethods().getProperties().stream())
                    .filter(PrototypeMethod::isAbstract)
                    .filter(method -> !prototype.getMethods().getMethod(method.getSimpleName(), method.getParameterTypes()).isPresent())
                    .forEach(method -> {
                        throw new PandaParserFailure(context, "Missing implementation of &1" + method + "&r in &1" + prototype + "&r");
                    });
        }

        if (prototype.getConstructors().getDeclaredProperties().isEmpty()) {
            prototype.getConstructors().declare(PandaConstructor.builder()
                    .type(prototype)
                    .callback((frame, instance, arguments) -> scope.revive(frame, instance))
                    .location(prototype.getLocation())
                    .build());
        }
    }

    @Autowired(order = 2, cycle = GenerationCycles.CONTENT_LABEL, delegation = Delegation.CURRENT_AFTER)
    void verifyContent(Context context, @Component Prototype prototype) {
        for (PrototypeField field : prototype.getFields().getDeclaredProperties()) {
            if (field.isInitialized() || (field.isNillable() && field.isMutable())) {
                field.initialize();
                continue;
            }

            throw new PandaParserFailure(context, "Field " + field + " is not initialized");
        }
    }

}
