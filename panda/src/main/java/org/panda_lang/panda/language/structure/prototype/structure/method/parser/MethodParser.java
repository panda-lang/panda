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

package org.panda_lang.panda.language.structure.prototype.structure.method.parser;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.core.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScope;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodScope;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;
import org.panda_lang.panda.language.structure.prototype.structure.method.variant.PandaMethod;
import org.panda_lang.panda.language.structure.prototype.structure.method.variant.PandaMethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.Parameter;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.ParameterParser;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.ParameterUtils;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.PROTOTYPE, parserClass = MethodParser.class, handlerClass = MethodParserHandler.class, priority = DefaultPriorities.PROTOTYPE_METHOD_PARSER)
public class MethodParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .simpleHollow()
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
                .delegateImmediately(new MethodDeclarationCasualParserCallback(), info.fork());
    }

    @LocalCallback
    private static class MethodDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("method-declaration", "method-parameters", "method-body");
            delegatedInfo.setComponent("redactor", redactor);

            TokenizedSource methodDeclaration = redactor.get("method-declaration");
            ClassPrototype prototype = delegatedInfo.getComponent("class-prototype");

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
                    String returnTypeName = token.getTokenValue();

                    PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
                    ImportRegistry registry = script.getImportRegistry();

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
            List<Parameter> parameters = parameterParser.parse(delegatedInfo, parametersSource);

            MethodScope scope = new MethodScope(methodName, parameters);
            ParameterUtils.addAll(scope.getVariables(), parameters, 0);
            delegatedInfo.setComponent("method-scope", scope);

            MethodCallback callback = new PandaMethodCallback(scope);
            delegatedInfo.setComponent("method-callback", callback);

            Method method = PandaMethod.builder()
                    .prototype(prototype)
                    .methodName(methodName)
                    .visibility(visibility)
                    .returnType(returnType)
                    .methodBody(callback)
                    .isStatic(isStatic)
                    .build();

            prototype.getMethods().registerMethod(method);
            delegatedInfo.setComponent("method", method);

            nextLayer.delegate(new MethodBodyCasualParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class MethodBodyCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            ClassScope classScope = delegatedInfo.getComponent("class-scope");

            MethodScope methodScope = delegatedInfo.getComponent("method-scope");
            delegatedInfo.setComponent("scope", methodScope);

            ScopeLinker linker = new PandaScopeLinker(classScope);
            linker.pushScope(methodScope);
            delegatedInfo.setComponent(Components.SCOPE_LINKER, linker);

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("method-body");

            ScopeParser scopeParser = new ScopeParser(methodScope);
            scopeParser.parse(delegatedInfo, body);
        }

    }

}
