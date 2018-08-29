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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.method;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScope;
import org.panda_lang.panda.framework.language.architecture.prototype.method.MethodScope;
import org.panda_lang.panda.framework.language.architecture.prototype.method.PandaMethod;
import org.panda_lang.panda.framework.language.architecture.prototype.method.PandaMethodCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.ScopeParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE, priority = PandaPriorities.PROTOTYPE_METHOD_PARSER)
public class MethodParser implements UnifiedParser, ParserHandler {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** ( +** ) { +* }")
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        List<TokenizedSource> content = MethodParser.PATTERN.match(reader);
        return content != null && content.size() == 3;
    }

    @Override
    public boolean parse(ParserData delegatedData, GenerationLayer nextLayer) {
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "method-declaration", "method-parameters", "method-body");

        TokenizedSource methodDeclaration = redactor.get("method-declaration");
        ClassPrototype prototype = delegatedData.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        ClassScope classScope = delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

        MethodVisibility visibility = null;
        ClassPrototype returnType = null;
        String methodName = null;
        boolean isStatic = false;

        for (int i = 0; i < methodDeclaration.size(); i++) {
            TokenRepresentation representation = methodDeclaration.get(i);
            Token token = representation.getToken();

            if (token.getType() == TokenType.UNKNOWN && i == methodDeclaration.size() - 1) {
                methodName = token.getTokenValue();
                continue;
            }

            if (token.getType() == TokenType.UNKNOWN && i == methodDeclaration.size() - 2) {
                PandaScript script = delegatedData.getComponent(PandaComponents.PANDA_SCRIPT);
                ModuleLoader registry = script.getModuleLoader();

                String returnTypeName = token.getTokenValue();
                returnType = registry.forClass(returnTypeName);
                continue;
            }

            switch (token.getTokenValue()) {
                case "method":
                    visibility = MethodVisibility.PUBLIC;
                    continue;
                case "local":
                    visibility = MethodVisibility.LOCAL;
                    continue;
                case "hidden":
                    visibility = MethodVisibility.HIDDEN;
                    continue;
                case "static":
                    isStatic = true;
                    visibility = visibility != null ? visibility : MethodVisibility.PUBLIC;
                    continue;
                default:
                    throw new PandaParserException("Unexpected token at line " + (representation.getLine() + 1) + ": " + token.getTokenValue());
            }
        }

        TokenizedSource parametersSource = redactor.get("method-parameters");
        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parameterParser.parse(delegatedData, parametersSource);
        ClassPrototype[] parameterTypes = ParameterUtils.toTypes(parameters);

        MethodScope methodScope = new MethodScope(methodName, parameters);
        ParameterUtils.addAll(methodScope.getVariables(), parameters, 0);
        delegatedData.setComponent(PandaComponents.SCOPE, methodScope);

        PrototypeMethod method = PandaMethod.builder()
                .prototype(prototype)
                .parameterTypes(parameterTypes)
                .methodName(methodName)
                .visibility(visibility)
                .returnType(returnType)
                .isStatic(isStatic)
                .methodBody(new PandaMethodCallback(methodScope))
                .build();
        prototype.getMethods().registerMethod(method);

        nextLayer.delegate(new MethodBodyCasualParserCallback(methodScope, redactor), delegatedData);
        return true;
    }

    @LocalCallback
    private static class MethodBodyCasualParserCallback implements CasualParserGenerationCallback {

        private final MethodScope methodScope;
        private final AbyssRedactor redactor;

        private MethodBodyCasualParserCallback(MethodScope methodScope, AbyssRedactor redactor) {
            this.methodScope = methodScope;
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, GenerationLayer nextLayer) {
            ScopeParser.createParser(methodScope, delegatedData)
                    .initializeLinker(delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE), methodScope)
                    .parse(redactor.get("method-body"));
        }

    }

}
