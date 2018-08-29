/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.module.PrimitivePrototypeLiquid;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassReference;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScope;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScopeInstance;
import org.panda_lang.panda.framework.language.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = UniversalPipelines.OVERALL)
public class ClassPrototypeParser implements UnifiedParser, ParserHandler {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "class +** { +* }")
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return TokenUtils.equals(reader.read(), Keywords.CLASS);
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        Module module = script.getModule();

        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "class-declaration", "class-body");
        data.setComponent(PandaComponents.REDACTOR, redactor);

        TokenizedSource classDeclaration = redactor.get("class-declaration");
        String className = classDeclaration.getTokenValue(0);

        if (className == null) {
            throw new PandaParserException("Class name cannot be null");
        }

        ClassPrototype classPrototype = PandaClassPrototype.builder()
                .name(className)
                .build();

        data.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, classPrototype);
        module.add(classPrototype);

        ModulePath registry = data.getComponent(PandaComponents.MODULE_REGISTRY);
        classPrototype.getExtended().add(PrimitivePrototypeLiquid.OBJECT);

        data.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, classPrototype);

        ClassScope classScope = new ClassScope(classPrototype);
        data.setComponent(ClassPrototypeComponents.CLASS_SCOPE, classScope);

        ClassReference classReference = new ClassReference(classPrototype, classScope);
        script.getStatements().add(classReference);

        ScopeLinker classScopeLinker = new PandaScopeLinker(classScope);
        data.setComponent(PandaComponents.SCOPE_LINKER, classScopeLinker);

        if (classDeclaration.size() > 1) {
            nextLayer.delegate(new ClassPrototypeDeclarationCasualParserCallback(), data);
        }

        nextLayer.delegate(new ClassPrototypeBodyCasualParserCallback(redactor), data);
        nextLayer.delegateAfter(new ClassPrototypeAfterCasualCallback(), data);
        return true;
    }

    @LocalCallback
    private static class ClassPrototypeDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            ClassPrototypeParserUtils.readDeclaration(delegatedData);
        }

    }

    @LocalCallback
    private static class ClassPrototypeBodyCasualParserCallback implements CasualParserGenerationCallback {

        private final AbyssRedactor redactor;

        private ClassPrototypeBodyCasualParserCallback(AbyssRedactor redactor) {
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            PipelineRegistry pipelineRegistry = delegatedData.getComponent(UniversalComponents.PIPELINE);
            ParserPipeline pipeline = pipelineRegistry.getPipeline(PandaPipelines.PROTOTYPE);

            TokenizedSource bodySource = redactor.get("class-body");
            SourceStream stream = new PandaSourceStream(bodySource);

            CasualParserGeneration generation = delegatedData.getComponent(UniversalComponents.GENERATION);
            ParserData bodyInfo = delegatedData.fork();
            bodyInfo.setComponent(UniversalComponents.SOURCE_STREAM, stream);

            while (stream.hasUnreadSource()) {
                UnifiedParser parser = pipeline.handle(stream);

                if (parser == null) {
                    throw new PandaParserException("Cannot parse the element of the prototype at line " + TokenUtils.getLine(stream.toTokenizedSource()));
                }

                parser.parse(bodyInfo, nextLayer);
            }
        }

    }

    @LocalCallback
    private static class ClassPrototypeAfterCasualCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            ClassPrototype prototype = delegatedData.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
            ClassScope scope = delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

            if (prototype.getConstructors().getAmountOfConstructors() > 0) {
                return;
            }

            for (PrototypeField field : prototype.getFields().getListOfFields()) {
                // TODO: Do sth
                if (!field.hasDefaultValue()) {

                }
            }

            PrototypeConstructor defaultConstructor = new PrototypeConstructor() {
                @Override
                public ClassScopeInstance createInstance(ExecutableBranch branch, Value... values) {
                    // TODO: assign def values
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

}
