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

package org.panda_lang.panda.language.resource.prototype;

import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.prototype.PrototypeVisibility;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.MethodFrame;
import org.panda_lang.framework.language.architecture.prototype.PandaMethod;
import org.panda_lang.framework.language.architecture.prototype.PandaMethodCallback;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.language.resource.PandaTypes;
import org.panda_lang.framework.language.interpreter.parser.ScopeParser;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Registrable(pipeline = UniversalPipelines.PROTOTYPE_LABEL, priority = PandaPriorities.PROTOTYPE_METHOD)
public class MethodParser extends ParserBootstrap {

    private static final ParameterParser PARAMETER_PARSER = new ParameterParser();
    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    private static final String VISIBILITY = "v";
    private static final String LOCAL = "l";
    private static final String STATIC = "s";

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

        PrototypeReference returnType = PandaTypes.VOID.getReference();

        if (signature.size() > 1) {
            ModuleLoader registry = context.getComponent(UniversalComponents.MODULE_LOADER);
            Optional<PrototypeReference> reference = registry.forName(signature.subSource(0, signature.size() - 1).asSource());

            if (!reference.isPresent()) {
                throw PandaParserFailure.builder("Unknown type", context)
                        .withStreamOrigin(signature)
                        .withNote("Make sure that the name does not have a typo and module which should contain that class is imported")
                        .build();
            }

            returnType = reference.get();
        }

        String method = Objects.requireNonNull(signature.getLast()).getValue();
        List<Parameter> parameters = PARAMETER_PARSER.parse(context, parametersSource);

        MethodFrame methodScope = local.allocated(new MethodFrame(parameters));
        context.withComponent(UniversalComponents.SCOPE, methodScope);
        Prototype prototype = context.getComponent(PrototypeComponents.CLASS_PROTOTYPE);

        PrototypeMethod prototypeMethod = PandaMethod.builder()
                .prototype(prototype.getReference())
                .parameters(parameters.toArray(new Parameter[0]))
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
    void parse(Context delegatedContext, @Local MethodFrame methodScope, @Src("body") Snippet body) throws Exception {
        SCOPE_PARSER.parse(delegatedContext, methodScope, body);
    }

}
