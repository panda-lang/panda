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

package org.panda_lang.panda.framework.language.resource.parsers.expression.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.architecture.prototype.method.invoker.MethodInvoker;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_METHOD_INVOKER)
public class MethodInvokerParser extends UnifiedParserBootstrap {

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder.pattern("[<instance:reader expression exclude method, field> .] <name> `( [<*args>] `) [;]");
    }

    @Autowired(order = 1)
    public void parse(ParserData data, LocalData localData) {
        localData.allocateInstance(data.getComponent(PandaComponents.CONTAINER).reserveCell());
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_AFTER)
    public void parse(ParserData data, @Local StatementCell cell, @Src("instance") Tokens instance, @Src("name") Tokens name, @Nullable @Src("*args") Tokens arguments) {
        if (arguments == null) {
            arguments = new PandaTokens();
        }

        MethodInvokerExpressionParser methodInvokerParser = new MethodInvokerExpressionParser(instance, name, arguments);
        methodInvokerParser.setVoids(true);

        methodInvokerParser.parse(null, data);
        MethodInvoker invoker = methodInvokerParser.getInvoker();

        cell.setStatement(methodInvokerParser.getInvoker());
    }

}
