/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.parser;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.lexer.token.distributor.SourceStream;
import org.panda_lang.panda.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.panda.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.implementation.interpreter.lexer.token.distributor.PandaSourceStream;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.implementation.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.structure.Script;
import org.panda_lang.panda.implementation.structure.value.Value;
import org.panda_lang.panda.language.structure.prototype.ClassInstance;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.ClassReference;
import org.panda_lang.panda.language.structure.prototype.ClassScope;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.Constructor;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.ConstructorUtils;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
public class ClassPrototypeParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "class")
            .hollow()
            .unit(TokenType.SEPARATOR, "{")
            .hollow()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new ClassPrototypeExtractorCallback(), parserInfo.fork());
    }

    @LocalCallback
    private static class ClassPrototypeExtractorCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            Script script = delegatedInfo.getComponent(Components.SCRIPT);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("class-declaration", "class-body");
            delegatedInfo.setComponent("redactor", redactor);

            TokenizedSource classDeclaration = redactor.get("class-declaration");
            String className = classDeclaration.getToken(0).getTokenValue();

            ClassPrototype classPrototype = new ClassPrototype(className);
            delegatedInfo.setComponent("class-prototype", classPrototype);

            ClassScope classScope = new ClassScope(classPrototype);
            delegatedInfo.setComponent("class-scope", classScope);

            ClassReference classReference = new ClassReference(classPrototype, classScope);
            script.getStatements().add(classReference);

            ScopeLinker classScopeLinker = new PandaScopeLinker(classScope);
            delegatedInfo.setComponent(Components.LINKER, classScopeLinker);

            if (classDeclaration.size() > 1) {
                nextLayer.delegate(new ClassPrototypeDeclarationParserCallback(), delegatedInfo);
            }
            nextLayer.delegate(new ClassPrototypeBodyParserCallback(), delegatedInfo);
            nextLayer.delegateAfter(new ClassPrototypeAfterCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class ClassPrototypeDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            ClassPrototypeParserUtils.readDeclaration(delegatedInfo);
        }

    }

    @LocalCallback
    private static class ClassPrototypeBodyParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            Panda panda = delegatedInfo.getComponent(Components.PANDA);
            PipelineRegistry pipelineRegistry = panda.getPandaComposition().getPipelineRegistry();
            ParserPipeline pipeline = pipelineRegistry.getPipeline(DefaultPipelines.PROTOTYPE);

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource bodySource = redactor.get("class-body");
            SourceStream stream = new PandaSourceStream(bodySource);

            while (stream.hasUnreadSource()) {
                UnifiedParser parser = pipeline.handle(stream);
                parser.parse(delegatedInfo);
            }
        }

    }

    @LocalCallback
    private static class ClassPrototypeAfterCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            ClassPrototype prototype = delegatedInfo.getComponent("class-prototype");
            ClassScope scope = delegatedInfo.getComponent("class-scope");

            if (prototype.getConstructors().size() > 0) {
                return;
            }

            Constructor defaultConstructor = new Constructor() {
                @Override
                public ClassInstance createInstance(Value... value) {
                    return scope.createInstance();
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
