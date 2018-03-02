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

package org.panda_lang.panda.language.structure.prototype.parsers.prototype;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.constructor.ConstructorUtils;
import org.panda_lang.panda.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.design.architecture.value.Value;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.prototype.scope.ClassReference;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScope;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScopeInstance;
import org.panda_lang.panda.language.syntax.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
public class ClassPrototypeParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "class +** { +* }")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new ClassPrototypeExtractorCasualCallback());
    }

    @LocalCallback
    private static class ClassPrototypeExtractorCasualCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
            Module module = script.getModule();

            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedInfo, "class-declaration", "class-body");
            delegatedInfo.setComponent("redactor", redactor);

            TokenizedSource classDeclaration = redactor.get("class-declaration");
            String className = classDeclaration.getToken(0).getTokenValue();

            ClassPrototype classPrototype = module.createPrototype(Object.class, className);
            classPrototype.getExtended().add(PandaClassPrototype.forClass(Object.class));
            delegatedInfo.setComponent("class-prototype", classPrototype);

            ClassScope classScope = new ClassScope(classPrototype);
            delegatedInfo.setComponent("class-scope", classScope);

            ClassReference classReference = new ClassReference(classPrototype, classScope);
            script.getStatements().add(classReference);

            ScopeLinker classScopeLinker = new PandaScopeLinker(classScope);
            delegatedInfo.setComponent(Components.SCOPE_LINKER, classScopeLinker);

            if (classDeclaration.size() > 1) {
                nextLayer.delegate(new ClassPrototypeDeclarationCasualParserCallback(), delegatedInfo);
            }

            nextLayer.delegate(new ClassPrototypeBodyCasualParserCallback(), delegatedInfo);
            nextLayer.delegateAfter(new ClassPrototypeAfterCasualCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class ClassPrototypeDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            ClassPrototypeParserUtils.readDeclaration(delegatedInfo);
        }

    }

    @LocalCallback
    private static class ClassPrototypeBodyCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            Panda panda = delegatedInfo.getComponent(Components.PANDA);
            ParserPipelineRegistry parserPipelineRegistry = panda.getPandaElements().getPipelineRegistry();
            ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.PROTOTYPE);

            AbyssRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource bodySource = redactor.get("class-body");
            SourceStream stream = new PandaSourceStream(bodySource);

            CasualParserGeneration generation = delegatedInfo.getComponent(Components.GENERATION);
            ParserInfo bodyInfo = delegatedInfo.fork();
            bodyInfo.setComponent(Components.SOURCE_STREAM, stream);

            while (stream.hasUnreadSource()) {
                UnifiedParser parser = pipeline.handle(stream);

                if (parser == null) {
                    throw new PandaParserException("Cannot parse the element of the prototype at line " + TokenUtils.getLine(stream.toTokenizedSource()));
                }

                parser.parse(bodyInfo);
                generation.executeImmediately(delegatedInfo);
            }
        }

    }

    @LocalCallback
    private static class ClassPrototypeAfterCasualCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            ClassPrototype prototype = delegatedInfo.getComponent("class-prototype");
            ClassScope scope = delegatedInfo.getComponent("class-scope");

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
