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

package org.panda_lang.panda.language.interpreter.parsers.prototype.parsers;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.interpreter.parsers.prototype.scope.ClassReference;
import org.panda_lang.panda.language.interpreter.parsers.prototype.scope.ClassScope;
import org.panda_lang.panda.language.interpreter.parsers.prototype.scope.ClassScopeInstance;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

@ParserRegistration(target = PandaPipelines.OVERALL, parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
public class ClassPrototypeParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "class +** { +* }")
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new ClassPrototypeExtractorCasualCallback());
    }

    @LocalCallback
    private static class ClassPrototypeExtractorCasualCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            Environment environment = delegatedData.getComponent(UniversalComponents.ENVIRONMENT);
            ModuleRegistry registry = environment.getModuleRegistry();

            PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
            Module module = script.getModule();

            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "class-declaration", "class-body");
            delegatedData.setComponent(PandaComponents.REDACTOR, redactor);

            TokenizedSource classDeclaration = redactor.get("class-declaration");
            String className = classDeclaration.getToken(0).getTokenValue();

            ClassPrototype classPrototype = new PandaClassPrototype(module, className, Object.class);
            classPrototype.getExtended().add(registry.forClass(Object.class));
            delegatedData.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, classPrototype);
            module.add(classPrototype);

            ClassScope classScope = new ClassScope(classPrototype);
            delegatedData.setComponent(ClassPrototypeComponents.CLASS_SCOPE, classScope);

            ClassReference classReference = new ClassReference(classPrototype, classScope);
            script.getStatements().add(classReference);

            ScopeLinker classScopeLinker = new PandaScopeLinker(classScope);
            delegatedData.setComponent(PandaComponents.SCOPE_LINKER, classScopeLinker);

            if (classDeclaration.size() > 1) {
                nextLayer.delegate(new ClassPrototypeDeclarationCasualParserCallback(), delegatedData);
            }

            nextLayer.delegate(new ClassPrototypeBodyCasualParserCallback(redactor), delegatedData);
            nextLayer.delegateAfter(new ClassPrototypeAfterCasualCallback(), delegatedData);
        }

    }

    @LocalCallback
    private static class ClassPrototypeDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
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
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
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

                parser.parse(bodyInfo);
                generation.executeImmediately(delegatedData);
            }
        }

    }

    @LocalCallback
    private static class ClassPrototypeAfterCasualCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            ClassPrototype prototype = delegatedData.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
            ClassScope scope = delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

            if (prototype.getConstructors().size() > 0) {
                return;
            }

            for (PrototypeField field : prototype.getFields()) {
                if (!field.hasDefaultValue()) {
                    // TODO: Do sth
                }
            }

            PrototypeConstructor defaultConstructor = new PrototypeConstructor() {
                @Override
                public ClassScopeInstance createInstance(ExecutableBranch branch, Value... values) {
                    ClassScopeInstance instance = scope.createInstance(branch);

                    // TODO: assign def values

                    return instance;
                }

                @Override
                public ClassPrototype[] getParameterTypes() {
                    return ConstructorUtils.PARAMETERLESS;
                }
            };

            prototype.getConstructors().add(defaultConstructor);
        }

    }

}
