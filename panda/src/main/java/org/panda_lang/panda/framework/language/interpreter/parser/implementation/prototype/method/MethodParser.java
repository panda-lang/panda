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
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
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
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.ScopeParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE, priority = PandaPriorities.PROTOTYPE_METHOD_PARSER)
public class MethodParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .pattern("+** ( +** ) { +* }", "declaration", "parameters", "body");
    }

    @Autowired
    boolean parse(ParserData data, LocalData local, @Redactor("declaration") TokenizedSource methodDeclaration, @Redactor("parameters") TokenizedSource parametersSource) {
        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        ClassScope classScope = data.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

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
                PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
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

        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parameterParser.parse(data, parametersSource);
        ClassPrototype[] parameterTypes = ParameterUtils.toTypes(parameters);

        MethodScope methodScope = local.allocateInstance(new MethodScope(methodName, parameters));
        ParameterUtils.addAll(methodScope.getVariables(), parameters, 0);
        data.setComponent(PandaComponents.SCOPE, methodScope);

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
        return true;
    }

    @Autowired(order = 1)
    void parse(ParserData delegatedData, @Local MethodScope methodScope, @Redactor("body") TokenizedSource body) throws Throwable {
        ScopeParser.createParser(methodScope, delegatedData)
                .initializeLinker(delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE), methodScope)
                .parse(body);
    }

}
