/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.resource.parsers.prototype.method;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodScope;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PandaMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PandaMethodCallback;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.GenerationTypes;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.ScopeParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.parameter.ParameterParser;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_METHOD_PARSER)
public class MethodParser extends UnifiedParserBootstrap {

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder.pattern("v:[(l:local|h:hidden)] s:[static] method <*signature> `( [<*parameters>] `) `{ [<*body>] `}");
    }

    @Autowired(order = 1, type = GenerationTypes.TYPES_LABEL)
    @AutowiredParameters(skip = 3, value = {
            @Type(with = Src.class, value = "*signature"),
            @Type(with = Src.class, value = "*parameters")
    })
    boolean parse(ParserData data, LocalData local, ExtractorResult result, Tokens signature, Tokens parametersSource) {
        MethodVisibility visibility = MethodVisibility.PUBLIC;

        if (result.hasIdentifier("v")) {
            visibility = result.hasIdentifier("l") ? MethodVisibility.LOCAL : MethodVisibility.HIDDEN;
        }

        ClassPrototypeReference returnType = PandaTypes.VOID.getReference();

        if (signature.size() > 1) {
            ModuleLoader registry = data.getComponent(PandaComponents.PANDA_SCRIPT).getModuleLoader();
            returnType = registry.forClass(signature.subSource(0, signature.size() - 1).asString());
        }

        List<Parameter> parameters =  new ParameterParser().parse(data, parametersSource);
        ClassPrototypeReference[] parameterTypes = ParameterUtils.toTypes(parameters);

        String method = signature.getLast().getTokenValue();
        MethodScope methodScope = local.allocateInstance(new MethodScope(method, parameters));
        ParameterUtils.addAll(methodScope.getVariables(), parameters, 0);

        data.setComponent(PandaComponents.SCOPE, methodScope);
        ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

        PrototypeMethod prototypeMethod = PandaMethod.builder()
                .prototype(prototype.getReference())
                .parameterTypes(parameterTypes)
                .methodName(method)
                .visibility(visibility)
                .returnType(returnType)
                .isStatic(result.getIdentifiers().contains("s"))
                .methodBody(new PandaMethodCallback(methodScope))
                .build();

        prototype.getMethods().registerMethod(prototypeMethod);
        return true;
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parse(ParserData delegatedData, @Local MethodScope methodScope, @Src("*body") Tokens body) throws Throwable {
        ScopeParser.createParser(methodScope, delegatedData)
                .initializeLinker(delegatedData.getComponent(ClassPrototypeComponents.CLASS_SCOPE), methodScope)
                .parse(body);
    }

}
