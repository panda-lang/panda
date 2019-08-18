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

package org.panda_lang.panda.framework.language.resource.prototype.method;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.PrototypeVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Inter;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.MethodScope;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.PandaMethod;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.PandaMethodCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.ScopeParser;
import org.panda_lang.panda.framework.language.resource.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.resource.prototype.parameter.ParameterParser;

import java.util.List;
import java.util.Optional;

@Registrable(pipeline = UniversalPipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_METHOD)
public class MethodParser extends ParserBootstrap {

    private static final String VISIBILITY = "v";
    private static final String LOCAL = "l";
    private static final String STATIC = "s";

    private final ParameterParser parameterParser = new ParameterParser();
    private final ScopeParser scopeParser = new ScopeParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer.pattern("v:[(l:local|h:hidden)] s:[static] method <*signature> parameters:~( body:~{");
    }

    @Autowired(order = 1, cycle = GenerationCycles.TYPES_LABEL)
    boolean parse(Context context, LocalData local, @Inter ExtractorResult result, @Src("*signature") Snippet signature, @Src("parameters") Snippet parametersSource) {
        PrototypeVisibility visibility = PrototypeVisibility.PUBLIC;

        if (result.hasIdentifier(VISIBILITY)) {
            visibility = result.hasIdentifier(LOCAL) ? PrototypeVisibility.LOCAL : PrototypeVisibility.HIDDEN;
        }

        ClassPrototypeReference returnType = PandaTypes.VOID.getReference();

        if (signature.size() > 1) {
            ModuleLoader registry = context.getComponent(UniversalComponents.MODULE_LOADER);
            Optional<ClassPrototypeReference> reference = registry.forName(signature.subSource(0, signature.size() - 1).asString());

            if (!reference.isPresent()) {
                throw PandaParserFailure.builder("Unknown type", context)
                        .withStreamOrigin(signature)
                        .withNote("Make sure that the name does not have a typo and module which should contain that class is imported")
                        .build();
            }

            returnType = reference.get();
        }

        String method = signature.getLast().getValue();
        List<PrototypeParameter> parameters = parameterParser.parse(context, parametersSource);

        MethodScope methodScope = local.allocated(new MethodScope(method, parameters));
        methodScope.getVariables().addAll(parameters);

        context.withComponent(UniversalComponents.SCOPE, methodScope);
        ClassPrototype prototype = context.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

        PrototypeMethod prototypeMethod = PandaMethod.builder()
                .prototype(prototype.getReference())
                .parameters(parameters.toArray(new PrototypeParameter[0]))
                .name(method)
                .visibility(visibility)
                .returnType(returnType)
                .isStatic(result.hasIdentifier(STATIC))
                .methodBody(new PandaMethodCallback(methodScope))
                .build();

        prototype.getMethods().declare(prototypeMethod);
        return true;
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parse(Context delegatedContext, @Local MethodScope methodScope, @Src("body") Snippet body) throws Exception {
        scopeParser.parse(delegatedContext, delegatedContext.getComponent(ClassPrototypeComponents.CLASS_SCOPE), methodScope, body);
    }

}
