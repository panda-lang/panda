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

import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.ExpressionParser;
import org.panda_lang.panda.design.architecture.dynamic.looping.ForEachBlock;
import org.panda_lang.panda.language.interpreter.parsers.scope.block.*;
import org.panda_lang.panda.language.interpreter.parsers.statement.variable.parser.VarParser;
import org.panda_lang.panda.language.interpreter.parsers.statement.variable.parser.VarParserData;
import org.panda_lang.panda.language.interpreter.parsers.statement.variable.parser.VarParserResult;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

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

        Environment environment = data.getComponent(UniversalComponents.ENVIRONMENT);
        ModuleRegistry registry = environment.getModuleRegistry();

        // TODO: Create var
        VarParser varParser = new VarParser();
        VarParserData varData = varParser.toVarParserData(data, varSource);
        VarParserResult result = varParser.parseVariable(varData, data);

        ScopeLinker scopeLinker = data.getComponent(PandaComponents.SCOPE_LINKER);
        Scope scope = scopeLinker.getCurrentScope();
        int variableId = scope.addVariable(result.getVariable());

        ExpressionParser expressionParser = new ExpressionParser();
        Expression expression = expressionParser.parse(data, iterableSource);
        ClassPrototype iterable = registry.forClass(Iterable.class);

        if (!expression.getReturnType().isAssociatedWith(iterable)) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        data.setComponent(BlockComponents.BLOCK, new ForEachBlock(variableId, result.getVariable().getType(), expression));
    }

}
