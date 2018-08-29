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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.constructor;

import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScope;
import org.panda_lang.panda.framework.language.architecture.prototype.constructor.ConstructorScope;
import org.panda_lang.panda.framework.language.architecture.prototype.constructor.PandaConstructor;
import org.panda_lang.panda.framework.language.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.FirstTokenHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.ScopeParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.prototype.parameter.ParameterParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

import java.util.List;

@ParserRegistration(target = PandaPipelines.PROTOTYPE)
public class ConstructorParser extends BootstrapParser {

    {
        bootstrapParser = PandaParserBootstrap.builder()
                .handler(new FirstTokenHandler(Keywords.CONSTRUCTOR))
                .pattern("constructor ( +** ) { +* }", "parameters", "body")
                .instance(this)
                .build();
    }

    @Autowired
    private void parse(ParserData data, LocalData local, @Component ClassScope classScope, @Redactor("parameters") TokenizedSource parametersSource) {
        ParameterParser parameterParser = new ParameterParser();
        List<Parameter> parameters = parameterParser.parse(data, parametersSource);

        ConstructorScope constructorScope = local.allocateInstance(new ConstructorScope(parameters));
        ParameterUtils.addAll(constructorScope.getVariables(), parameters, 0);

        PrototypeConstructor constructor = new PandaConstructor(classScope.getPrototype(), classScope, constructorScope);
        classScope.getPrototype().getConstructors().addConstructor(constructor);
    }

    @Autowired(order = 2, value = Delegation.DEFAULT)
    private void parseBody(ParserData data, @Local ConstructorScope constructorScope, @Component ClassScope classScope, @Redactor("body") TokenizedSource body) {
        ScopeParser.createParser(constructorScope, data)
                .initializeLinker(classScope, constructorScope)
                .parse(body);
    }

}