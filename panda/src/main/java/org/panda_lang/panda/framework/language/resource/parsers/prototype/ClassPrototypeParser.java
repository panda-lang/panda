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

package org.panda_lang.panda.framework.language.resource.parsers.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.design.architecture.prototype.structure.ClassPrototypeReferenceStatement;
import org.panda_lang.panda.framework.design.architecture.prototype.structure.ClassPrototypeScope;
import org.panda_lang.panda.framework.design.architecture.prototype.structure.ClassPrototypeScopeInstance;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.generator.ClassPrototypeTypeGenerator;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = UniversalPipelines.OVERALL_LABEL)
public class ClassPrototypeParser extends UnifiedParserBootstrap {

    private static final ClassPrototypeTypeGenerator GENERATOR = new ClassPrototypeTypeGenerator();

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder
                .handler(new TokenHandler(Keywords.CLASS))
                .pattern("class <name> [extends <inherited>] `{ [<*body>] `}");
    }

    @Autowired(type = org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.PandaTypes.TYPES_LABEL)
    public void parse(ParserData data, Generation generation, @Src("name") String className) throws Exception {
        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        Module module = script.getModule();

        if (className == null) {
            throw new PandaParserException("Class name cannot be null");
        }

        ClassPrototype prototype = PandaClassPrototype.builder()
                .module(module)
                .associated(GENERATOR.generateType(className))
                .name(className)
                .build();

        data.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, prototype);
        module.add(new PandaClassPrototypeReference(prototype));

        ModulePath path = data.getComponent(PandaComponents.MODULE_REGISTRY);
        prototype.addExtended(new PandaClassPrototypeReference(PandaTypes.OBJECT));

        data.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, prototype);

        ClassPrototypeScope scope = new ClassPrototypeScope(prototype);
        data.setComponent(ClassPrototypeComponents.CLASS_SCOPE, scope);

        ClassPrototypeReferenceStatement referenceStatement = new ClassPrototypeReferenceStatement(prototype, scope);
        script.getStatements().add(referenceStatement);

        ScopeLinker linker = new PandaScopeLinker(scope);
        data.setComponent(PandaComponents.SCOPE_LINKER, linker);
    }

    @Autowired(type = org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.PandaTypes.TYPES_LABEL, delegation = Delegation.CURRENT_AFTER)
    public void parseDeclaration(ParserData data, @Src("declaration") Tokens declaration) {
        if (declaration != null) {
            ClassPrototypeParserUtils.readDeclaration(data, declaration);
        }
    }

    @Autowired(type = org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.PandaTypes.TYPES_LABEL, delegation = Delegation.NEXT_AFTER)
    public void parseBody(ParserData data, Generation generation, @Nullable @Src("*body") Tokens body) throws Throwable {
        if (TokensUtils.isEmpty(body)) {
            return;
        }

        PipelineRegistry pipelineRegistry = data.getComponent(UniversalComponents.PIPELINE);
        ParserPipeline<UnifiedParser> pipeline = pipelineRegistry.getPipeline(PandaPipelines.PROTOTYPE);

        ParserData bodyInfo = data.fork();
        SourceStream stream = new PandaSourceStream(body);
        bodyInfo.setComponent(UniversalComponents.SOURCE_STREAM, stream);

        while (stream.hasUnreadSource()) {
            UnifiedParser parser = pipeline.handle(bodyInfo, stream.toTokenizedSource());

            if (parser == null) {
                throw new PandaParserFailure("Cannot parse the element of the prototype", data.setComponent(UniversalComponents.SOURCE_STREAM, stream.updateCachedSource()));
            }

            parser.parse(bodyInfo);
        }
    }

    @Autowired(order = 1, type = org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.PandaTypes.TYPES_LABEL)
    public void parseAfter(ParserData data) {
        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        ClassPrototypeScope scope = data.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

        if (prototype.getConstructors().getAmountOfConstructors() > 0) {
            return;
        }

        for (PrototypeField field : prototype.getFields().getListOfFields()) {
            if (!field.hasDefaultValue()) {
                // assign
            }
        }

        PrototypeConstructor defaultConstructor = new PrototypeConstructor() {
            @Override
            public ClassPrototypeScopeInstance createInstance(ExecutableBranch branch, Value... values) {
                return scope.createInstance(branch);
            }

            @Override
            public ClassPrototype[] getParameterTypes() {
                return ConstructorUtils.PARAMETERLESS;
            }
        };

        prototype.getConstructors().addConstructor(defaultConstructor);
    }

}
