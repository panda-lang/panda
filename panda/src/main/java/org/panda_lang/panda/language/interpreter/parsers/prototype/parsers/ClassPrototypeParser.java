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

import org.panda_lang.panda.design.architecture.*;
import org.panda_lang.panda.design.architecture.module.*;
import org.panda_lang.panda.design.architecture.prototype.*;
import org.panda_lang.panda.design.architecture.prototype.constructor.*;
import org.panda_lang.panda.design.interpreter.parser.*;
import org.panda_lang.panda.design.interpreter.parser.generation.*;
import org.panda_lang.panda.design.interpreter.parser.linker.*;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.*;
import org.panda_lang.panda.framework.design.architecture.prototype.field.*;
import org.panda_lang.panda.framework.design.architecture.value.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.design.runtime.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;
import org.panda_lang.panda.language.interpreter.*;
import org.panda_lang.panda.language.interpreter.parsers.*;
import org.panda_lang.panda.language.interpreter.parsers.prototype.scope.*;

@ParserRegistration(target = UniversalPipelines.OVERALL, parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
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
            PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
            Module module = script.getModule();

            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "class-declaration", "class-body");
            delegatedData.setComponent(PandaComponents.REDACTOR, redactor);

            TokenizedSource classDeclaration = redactor.get("class-declaration");
            String className = classDeclaration.getToken(0).getTokenValue();

            ClassPrototype classPrototype = new PandaClassPrototype(module, className, Object.class);
            delegatedData.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, classPrototype);
            module.add(classPrototype);

            ModuleRegistry registry = delegatedData.getComponent(PandaComponents.MODULE_REGISTRY);
            classPrototype.getExtended().add(PandaModuleRegistryAssistant.forClass(registry, Object.class));

            delegatedData.setComponent(ClassPrototypeComponents.CLASS_PROTOTYPE, classPrototype);

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
