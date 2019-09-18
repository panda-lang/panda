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

package org.panda_lang.panda.language.resource.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SnippetUtils;
import org.panda_lang.framework.language.architecture.prototype.PandaConstructor;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.generator.ClassPrototypeTypeGenerator;
import org.panda_lang.framework.language.architecture.prototype.PrototypeFrame;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.language.resource.PandaTypes;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.panda.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;

@Registrable(pipeline = UniversalPipelines.HEAD_LABEL)
public class ClassPrototypeParser extends ParserBootstrap {

    private static final ClassPrototypeTypeGenerator GENERATOR = new ClassPrototypeTypeGenerator();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.CLASS))
                .pattern("class <name> [extends <inherited>] body:~{");
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, @Src("name") String className) throws Exception {
        PandaScript script = context.getComponent(PandaComponents.PANDA_SCRIPT);
        Module module = script.getModule();

        if (className == null) {
            throw new PandaParserException("Class name cannot be null");
        }

        Prototype prototype = PandaPrototype.builder()
                .module(module)
                .associated(GENERATOR.generateType(className))
                .name(className)
                .build();

        context.withComponent(PrototypeComponents.CLASS_PROTOTYPE, prototype);
        module.add(prototype.getReference());

        prototype.addExtended(PandaTypes.OBJECT.getReference());
        context.withComponent(PrototypeComponents.CLASS_PROTOTYPE, prototype);

        PrototypeFrame scope = new PrototypeFrame(prototype);
        context.withComponent(PrototypeComponents.CLASS_FRAME, scope)
                .withComponent(UniversalComponents.SCOPE, scope);
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.CURRENT_AFTER)
    void parseDeclaration(Context context, @Src("declaration") Snippet declaration) {
        if (declaration != null) {
            ClassPrototypeParserUtils.readDeclaration(context, declaration);
        }
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL, delegation = Delegation.NEXT_AFTER)
    void parseBody(Context context, @Nullable @Src("body") Snippet body) throws Exception {
        if (SnippetUtils.isEmpty(body)) {
            return;
        }

        Context bodyContext = context.fork()
                .withComponent(UniversalComponents.STREAM, new PandaSourceStream(body));

        PipelineParser<?> parser = new PipelineParser<>(UniversalPipelines.PROTOTYPE, bodyContext);
        parser.parse(bodyContext, false);
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    void parseAfter(Context context) {
        Prototype prototype = context.getComponent(PrototypeComponents.CLASS_PROTOTYPE);
        PrototypeFrame scope = context.getComponent(PrototypeComponents.CLASS_FRAME);

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
