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
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScope;
import org.panda_lang.panda.framework.language.architecture.prototype.method.MethodScope;
import org.panda_lang.panda.framework.language.architecture.prototype.method.PandaMethod;
import org.panda_lang.panda.framework.language.architecture.prototype.method.PandaMethodCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.TokenPatternHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.ScopeParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractorResult;

import java.util.ArrayList;
import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE, priority = PandaPriorities.PROTOTYPE_METHOD_PARSER)
public class MethodParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new TokenPatternHandler())
                .interceptor(new TokenPatternInterceptor())
                .pattern("(method|local|hidden) static:[static] <return> <name> `( [<*parameters>] `) `{ <*body> `}");
    }

    @Autowired
    @AutowiredParameters(detectTo = 3, value = {
            @Type(with = Src.class, value = "return"),
            @Type(with = Src.class, value = "name"),
            @Type(with = Src.class, value = "*parameters"),
            @Type(with = Component.class)
    })
    boolean parse(ParserData data, LocalData local, TokenExtractorResult result, String type, String method, Tokens parametersSource, PandaScript script) {
        MethodVisibility visibility = MethodVisibility.PUBLIC;
        boolean isStatic = result.getIdentifiers().contains("static");

        ModuleLoader registry = script.getModuleLoader();
        ClassPrototype returnType = registry.forClass(type);

        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parametersSource != null ? parameterParser.parse(data, parametersSource) : new ArrayList<>();
        ClassPrototype[] parameterTypes = ParameterUtils.toTypes(parameters);

        MethodScope methodScope = local.allocateInstance(new MethodScope(method, parameters));
        ParameterUtils.addAll(methodScope.getVariables(), parameters, 0);
        data.setComponent(PandaComponents.SCOPE, methodScope);

        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
        ClassScope classScope = data.getComponent(ClassPrototypeComponents.CLASS_SCOPE);

        PrototypeMethod prototypeMethod = PandaMethod.builder()
                .prototype(prototype)
                .parameterTypes(parameterTypes)
                .methodName(method)
                .visibility(visibility)
                .returnType(returnType)
                .isStatic(isStatic)
                .methodBody(new PandaMethodCallback(methodScope))
                .build();

        prototype.getMethods().registerMethod(prototypeMethod);
        return true;
    }

    @Autowired(order = 1)
    void parse(ParserData delegatedData, @Local MethodScope methodScope, @Src("*body") Tokens body) throws Throwable {
        ScopeParser.createParser(methodScope, delegatedData)
                .initializeLinker(delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE), methodScope)
                .parse(body);
    }

}
