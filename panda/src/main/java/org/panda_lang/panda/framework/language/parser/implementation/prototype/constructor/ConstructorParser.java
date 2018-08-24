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

package org.panda_lang.panda.framework.language.parser.implementation.prototype.constructor;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassScope;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.ConstructorScope;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PandaConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSyntax;
import org.panda_lang.panda.framework.language.parser.implementation.ScopeParser;
import org.panda_lang.panda.framework.language.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.parser.implementation.prototype.parameter.ParameterParser;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE, parserClass = ConstructorParser.class, handlerClass = ConstructorParserHandler.class)
public class ConstructorParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "constructor ( +** ) { +* }")
            .build();

    @Override
    public boolean parse(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "parameters", "constructor-body");

        TokenizedSource parametersSource = redactor.get("parameters");
        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parameterParser.parse(delegatedData, parametersSource);

        ConstructorScope constructorScope = new ConstructorScope(parameters);
        ParameterUtils.addAll(constructorScope.getVariables(), parameters, 0);

        ClassPrototype prototype = delegatedData.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        ClassScope classScope = delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

        PrototypeConstructor constructor = new PandaConstructor(prototype, classScope, constructorScope);
        prototype.getConstructors().addConstructor(constructor);

        nextLayer.delegateAfter(new ConstructorBodyCallbackCasual(constructorScope, redactor), delegatedData);
        return true;
    }

    @LocalCallback
    private static class ConstructorBodyCallbackCasual implements CasualParserGenerationCallback {

        private final ConstructorScope constructorScope;
        private final AbyssRedactor redactor;

        private ConstructorBodyCallbackCasual(ConstructorScope constructorScope, AbyssRedactor redactor) {
            this.constructorScope = constructorScope;
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            delegatedData.setComponent(PandaComponents.SCOPE, constructorScope);

            ScopeParser.createParser(constructorScope, delegatedData)
                    .initializeLinker(delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE), constructorScope)
                    .parse(redactor.get("constructor-body"));
        }

    }

}
