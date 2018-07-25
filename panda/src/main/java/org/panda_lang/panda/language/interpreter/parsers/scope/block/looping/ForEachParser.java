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

package org.panda_lang.panda.language.interpreter.parsers.scope.block.looping;

import org.panda_lang.panda.design.architecture.dynamic.looping.*;
import org.panda_lang.panda.design.architecture.module.*;
import org.panda_lang.panda.design.interpreter.parser.*;
import org.panda_lang.panda.design.interpreter.parser.linker.*;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.framework.design.architecture.statement.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.runtime.expression.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;
import org.panda_lang.panda.language.interpreter.*;
import org.panda_lang.panda.language.interpreter.parsers.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.*;
import org.panda_lang.panda.language.interpreter.parsers.scope.block.*;
import org.panda_lang.panda.language.interpreter.parsers.statement.variable.parser.*;

@ParserRegistration(target = PandaPipelines.BLOCK, parserClass = ForEachParser.class, handlerClass = ForEachHandler.class)
public class ForEachParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "foreach ( +* : +* )")
            .maxNestingLevel(1)
            .build();

    @Override
    public void parse(ParserData data) {
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "foreach-var", "foreach-iterable");
        TokenizedSource varSource = redactor.get("foreach-var");
        TokenizedSource iterableSource = redactor.get("foreach-iterable");

        // TODO: Create var
        VarParser varParser = new VarParser();
        VarParserData varData = varParser.toVarParserData(data, varSource);
        VarParserResult result = varParser.parseVariable(varData, data);

        ScopeLinker scopeLinker = data.getComponent(PandaComponents.SCOPE_LINKER);
        Scope scope = scopeLinker.getCurrentScope();
        int variableId = scope.addVariable(result.getVariable());

        ExpressionParser expressionParser = new ExpressionParser();
        Expression expression = expressionParser.parse(data, iterableSource);

        if (expression == null) {
            throw new PandaParserException("Cannot parse expression: " + iterableSource);
        }

        ModuleRegistry registry = data.getComponent(PandaComponents.MODULE_REGISTRY);
        ClassPrototype iterable = PandaModuleRegistryAssistant.forClass(registry, Iterable.class);

        if (!expression.getReturnType().isAssociatedWith(iterable)) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        data.setComponent(BlockComponents.BLOCK, new ForEachBlock(variableId, result.getVariable().getType(), expression));
    }

}
