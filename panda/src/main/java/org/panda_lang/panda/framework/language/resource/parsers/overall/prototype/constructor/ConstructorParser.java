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

package org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.constructor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.ConstructorScope;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor.PandaConstructor;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.structure.ClassPrototypeScope;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.parsers.ScopeParserUtils;
import org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE_LABEL)
public class ConstructorParser extends ParserBootstrap {

    private final ParameterParser parameterParser = new ParameterParser();

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder
                .handler(new TokenHandler(Keywords.CONSTRUCTOR))
                .pattern("constructor parameters:~( body:~{");
    }

    @Autowired(order = 1)
    void parse(ParserData data, LocalData local, @Component ClassPrototypeScope classScope, @Src("parameters") @Nullable Snippet parametersSource) {
        List<Parameter> parameters = parameterParser.parse(data, parametersSource);

        ConstructorScope constructorScope = local.allocated(new ConstructorScope(parameters));
        ParameterUtils.addAll(constructorScope.getVariables(), parameters, 0);

        PrototypeConstructor constructor = new PandaConstructor(classScope.getPrototype(), classScope, constructorScope);
        classScope.getPrototype().getConstructors().addConstructor(constructor);
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_DEFAULT)
    void parseBody(ParserData data, @Local ConstructorScope constructorScope, @Component ClassPrototypeScope classScope, @Src("body") @Nullable Snippet body) throws Exception {
        ScopeParserUtils.parse(classScope, constructorScope, data, body);
    }

}