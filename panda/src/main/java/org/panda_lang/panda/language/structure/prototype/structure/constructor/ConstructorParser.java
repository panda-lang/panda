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

package org.panda_lang.panda.language.structure.prototype.structure.constructor;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.core.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScope;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.variant.PandaConstructor;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.Parameter;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.ParameterParser;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.ParameterUtils;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.PROTOTYPE, parserClass = ConstructorParser.class, handlerClass = ConstructorParserHandler.class)
public class ConstructorParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "constructor")
            .unit(TokenType.SEPARATOR, "(")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, ")")
            .unit(TokenType.SEPARATOR, "{")
            .hollow()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(CasualParserGenerationType.HIGHER)
                .delegateImmediately(new ConstructorExtractorCallbackCasual(), info.fork());
    }

    @LocalCallback
    private static class ConstructorExtractorCallbackCasual implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("parameters", "constructor-body");
            delegatedInfo.setComponent("redactor", redactor);

            TokenizedSource parametersSource = redactor.get("parameters");
            ParameterParser parameterParser = new ParameterParser();
            List<Parameter> parameters = parameterParser.parse(delegatedInfo, parametersSource);

            ConstructorScope constructorScope = new ConstructorScope(parameters);
            ParameterUtils.addAll(constructorScope.getVariables(), parameters, 0);
            delegatedInfo.setComponent("constructor-scope", constructorScope);

            ClassPrototype prototype = delegatedInfo.getComponent("class-prototype");
            ClassScope classScope = delegatedInfo.getComponent("class-scope");

            PrototypeConstructor constructor = new PandaConstructor(prototype, classScope, constructorScope);
            delegatedInfo.setComponent("constructor", constructor);
            prototype.getConstructors().add(constructor);

            nextLayer.delegate(new ConstructorBodyCallbackCasual(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class ConstructorBodyCallbackCasual implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            ClassScope classScope = delegatedInfo.getComponent("class-scope");

            ConstructorScope constructorScope = delegatedInfo.getComponent("constructor-scope");
            delegatedInfo.setComponent("scope", constructorScope);

            ScopeLinker linker = new PandaScopeLinker(classScope);
            linker.pushScope(constructorScope);
            delegatedInfo.setComponent(Components.SCOPE_LINKER, linker);

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("constructor-body");

            ScopeParser scopeParser = new ScopeParser(constructorScope);
            scopeParser.parse(delegatedInfo, body);
        }

    }

}
