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

package org.panda_lang.panda.language.interpreter.parsers.prototype.parsers.method;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodCallback;
import org.panda_lang.panda.design.architecture.prototype.method.MethodScope;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.design.architecture.prototype.method.PandaMethod;
import org.panda_lang.panda.design.architecture.prototype.method.PandaMethodCallback;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.design.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.design.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parsers.PandaPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.language.interpreter.parsers.prototype.parsers.parameter.ParameterParser;
import org.panda_lang.panda.language.interpreter.parsers.prototype.scope.ClassScope;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE, parserClass = MethodParser.class, handlerClass = MethodParserHandler.class, priority = PandaPriorities.PROTOTYPE_METHOD_PARSER)
public class MethodParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** ( +** ) { +* }")
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new MethodDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class MethodDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "method-declaration", "method-parameters", "method-body");
            delegatedData.setComponent("redactor", redactor);

            TokenizedSource methodDeclaration = redactor.get("method-declaration");
            ClassPrototype prototype = delegatedData.getComponent("class-prototype");

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
                    PandaScript script = delegatedData.getComponent(PandaComponents.SCRIPT);
                    ImportRegistry registry = script.getImportRegistry();

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

            MethodScope scope = new MethodScope(methodName, parameters);
            ParameterUtils.addAll(scope.getVariables(), parameters, 0);
            delegatedData.setComponent("method-scope", scope);

            MethodCallback callback = new PandaMethodCallback(scope);
            delegatedData.setComponent("method-callback", callback);

            PrototypeMethod method = PandaMethod.builder()
                    .prototype(prototype)
                    .parameterTypes(parameterTypes)
                    .methodName(methodName)
                    .visibility(visibility)
                    .returnType(returnType)
                    .methodBody(callback)
                    .isStatic(isStatic)
                    .build();

            prototype.getMethods().registerMethod(method);
            delegatedData.setComponent("method", method);

            nextLayer.delegate(new MethodBodyCasualParserCallback(), delegatedData);
        }

    }

    @LocalCallback
    private static class MethodBodyCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            ClassScope classScope = delegatedData.getComponent("class-scope");
            MethodScope methodScope = delegatedData.getComponent("method-scope");
            delegatedData.setComponent("scope", methodScope);

            ScopeLinker linker = new PandaScopeLinker(classScope);
            linker.pushScope(methodScope);
            delegatedData.setComponent(PandaComponents.SCOPE_LINKER, linker);

            AbyssRedactor redactor = delegatedData.getComponent("redactor");
            TokenizedSource body = redactor.get("method-body");

            ScopeParser scopeParser = new ScopeParser(methodScope);
            scopeParser.parse(delegatedData, body);
        }

    }

}
