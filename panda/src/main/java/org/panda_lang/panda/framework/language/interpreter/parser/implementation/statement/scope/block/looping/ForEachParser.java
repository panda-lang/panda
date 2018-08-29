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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block.looping;

import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.ForEachBlock;
import org.panda_lang.panda.framework.language.architecture.prototype.generator.ClassPrototypeGenerator;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block.BlockComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.variable.parser.VarParserResult;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternAssistant;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK)
public class ForEachParser implements UnifiedParser, ParserHandler {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "foreach ( +* : +* )")
            .maxNestingLevel(1)
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return TokenUtils.equals(reader.read(), Keywords.FOREACH);
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "foreach-var", "foreach-iterable");
        TokenizedSource varSource = redactor.get("foreach-var");
        TokenizedSource iterableSource = redactor.get("foreach-iterable");

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

        ModulePath registry = data.getComponent(PandaComponents.MODULE_REGISTRY);
        ClassPrototype iterable = new ClassPrototypeGenerator().computeIfAbsent(registry, Iterable.class);

        if (!expression.getReturnType().isAssociatedWith(iterable)) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        data.setComponent(BlockComponents.BLOCK, new ForEachBlock(variableId, result.getVariable().getType(), expression));
        return true;
    }

}
