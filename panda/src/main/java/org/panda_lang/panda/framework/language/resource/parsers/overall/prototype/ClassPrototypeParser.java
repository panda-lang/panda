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

package org.panda_lang.panda.framework.language.resource.parsers.overall.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.SnippetUtils;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.PandaClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.PandaConstructor;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.generator.ClassPrototypeTypeGenerator;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeReferenceStatement;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeScope;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.PipelineParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(pipeline = UniversalPipelines.HEAD_LABEL)
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

        ClassPrototype prototype = PandaClassPrototype.builder()
                .module(module)
                .associated(GENERATOR.generateType(className))
                .name(className)
                .build();

        context.withComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, prototype);
        module.add(prototype.getReference());

        prototype.addExtended(PandaTypes.OBJECT.getReference());
        context.withComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, prototype);

        ClassPrototypeScope scope = new ClassPrototypeScope(prototype);
        context.withComponent(ClassPrototypeComponents.CLASS_SCOPE, scope);

        ClassPrototypeReferenceStatement referenceStatement = new ClassPrototypeReferenceStatement(prototype, scope);
        script.addStatement(referenceStatement);

        ScopeLinker linker = new PandaScopeLinker(scope);
        context.withComponent(UniversalComponents.LINKER, linker);
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
        ClassPrototype prototype = context.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        ClassPrototypeScope scope = context.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

        if (prototype.getConstructors().getAmountOfConstructors() > 0) {
            return;
        }

        for (PrototypeField field : prototype.getFields().getListOfFields()) {
            if (!field.hasDefaultValue()) {
                // assign
            }
        }

        PrototypeConstructor defaultConstructor = PandaConstructor.builder()
                .type(prototype.getReference())
                .callback((frame, instance, arguments) -> {
                    return new PandaStaticValue(prototype, scope.createFrame(frame));
                })
                .build();

        prototype.getConstructors().addConstructor(defaultConstructor);
    }

}
