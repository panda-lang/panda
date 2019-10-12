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
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SnippetUtils;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.architecture.prototype.PrototypeScope;
import org.panda_lang.framework.language.architecture.prototype.generator.PrototypeClassGenerator;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SectionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SubPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.TypeElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.WildcardElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.verifiers.TokenTypeVerifier;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.language.resource.internal.java.JavaModule;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.architecture.PandaScript;
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

@RegistrableParser(pipeline = Pipelines.HEAD_LABEL)
public final class PrototypeParser extends ParserBootstrap {

    private static final PrototypeClassGenerator GENERATOR = new PrototypeClassGenerator();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new CustomPatternHandler())
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        VariantElement.create("visibility").content(Keywords.PUBLIC.getValue(), Keywords.SHARED.getValue(), Keywords.LOCAL.getValue()).optional(),
                        KeywordElement.create(Keywords.CLASS),
                        WildcardElement.create("name").verify(new TokenTypeVerifier(TokenType.UNKNOWN)),
                        SubPatternElement.create("extended").optional().of(
                                KeywordElement.create(Keywords.EXTENDS),
                                TypeElement.create("inherited")
                        ),
                        SectionElement.create("body")
                ));
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, @Inter SourceLocation location, @Inter Result result, @Component PandaScript script, @Src("name") String className) throws Exception {
        Module module = script.getModule();
        Visibility visibility = Visibility.LOCAL;

        if (result.has("visibility")) {
            visibility = Visibility.valueOf(result.get("visibility").toString().toUpperCase());
        }

        Prototype prototype = PandaPrototype.builder()
                .name(className)
                .module(module)
                .source(location.getSource())
                .associated(GENERATOR.generateType(className))
                .visibility(visibility)
                .build();

        prototype.addSuper(JavaModule.OBJECT.getReference());
        module.add(className, prototype.getAssociatedClass(), prototype::getReference);
        PrototypeScope scope = new PrototypeScope(location, prototype);

        context.withComponent(Components.SCOPE, scope)
                .withComponent(PrototypeComponents.CLASS_FRAME, scope)
                .withComponent(PrototypeComponents.PROTOTYPE, prototype);
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.CURRENT_AFTER)
    void parseDeclaration(Context context, @Src("declaration") Snippet declaration) {
        if (declaration != null) {
            PrototypeParserUtils.readDeclaration(context, declaration);
        }
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.NEXT_AFTER)
    void parseBody(Context context, @Nullable @Src("body") Snippet body) throws Exception {
        if (SnippetUtils.isEmpty(body)) {
            return;
        }

        Context bodyContext = context.fork().withComponent(Components.STREAM, new PandaSourceStream(body));
        PipelineParser<?> parser = new PipelineParser<>(Pipelines.PROTOTYPE, bodyContext);
        parser.parse(bodyContext, false);
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void parseAfter(Context context) {
        Prototype prototype = context.getComponent(PrototypeComponents.PROTOTYPE);
        PrototypeScope scope = context.getComponent(PrototypeComponents.CLASS_FRAME);

        if (!prototype.getConstructors().getProperties().isEmpty()) {
            return;
        }

        for (PrototypeField field : prototype.getFields().getProperties()) {
            if (!field.hasDefaultValue()) {
                // assign
            }
        }

        PrototypeConstructor defaultConstructor = PandaConstructor.builder()
                .type(prototype.getReference())
                .callback((frame, instance, arguments) -> {
                    return scope.revive(frame, instance);
                })
                .build();

        prototype.getConstructors().declare(defaultConstructor);
    }

}
