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

package org.panda_lang.panda.language.structure.prototype.parsers.constructor;

import org.panda_lang.panda.framework.design.architecture.detach.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.constructor.ConstructorScope;
import org.panda_lang.panda.framework.design.architecture.detach.PrototypeConstructor;
import org.panda_lang.panda.design.architecture.prototype.constructor.implementation.PandaConstructor;
import org.panda_lang.panda.framework.design.architecture.detach.Parameter;
import org.panda_lang.panda.design.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.design.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.structure.prototype.parsers.parameter.ParameterParser;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScope;
import org.panda_lang.panda.language.syntax.PandaSyntax;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.PROTOTYPE, parserClass = ConstructorParser.class, handlerClass = ConstructorParserHandler.class)
public class ConstructorParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "constructor ( +** ) { +* }")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGenerationAssistant.delegateImmediately(info, new ConstructorExtractorCallbackCasual());
    }

    @LocalCallback
    private static class ConstructorExtractorCallbackCasual implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedInfo, "parameters", "constructor-body");
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

            AbyssRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("constructor-body");

            ScopeParser scopeParser = new ScopeParser(constructorScope);
            scopeParser.parse(delegatedInfo, body);
        }

    }

}
