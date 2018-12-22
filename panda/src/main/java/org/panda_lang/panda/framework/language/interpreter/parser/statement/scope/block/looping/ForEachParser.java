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

package org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block.looping;

import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor.TokenPatternInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.ForEachBlock;
import org.panda_lang.panda.framework.language.architecture.prototype.generator.ClassPrototypeGenerator;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.general.expression.old.OldExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block.BlockData;
import org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.statement.variable.VariableInitializer;
import org.panda_lang.panda.framework.language.interpreter.parser.statement.variable.VariableParserUtils;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK_LABEL)
public class ForEachParser extends BlockSubparserBootstrap {

    private static final TokenPattern CONTENT_PATTERN = TokenPattern.builder()
            .compile("<type> <name> : <*iterable>")
            .build();

    {
        super.builder()
                .handler(new TokenHandler(Keywords.FOREACH))
                .interceptor(new TokenPatternInterceptor())
                .pattern("foreach `( <*content> `)");
    }

    @Autowired
    public BlockData parseBlock(ParserData data, @Src("*content") Tokens content) {
        ExtractorResult result = CONTENT_PATTERN.extract(content);
        Tokens name = result.getWildcard("name");
        Tokens type = result.getWildcard("type");
        Tokens iterable = result.getWildcard("*iterable");

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        Scope scope = data.getComponent(PandaComponents.SCOPE_LINKER).getCurrentScope();

        VariableInitializer initializer = new VariableInitializer();
        Variable variable = initializer.createVariable(script.getModuleLoader(), scope, true, true, type.asString(), name.asString());
        int variableId = VariableParserUtils.indexOf(scope, variable);

        OldExpressionParser expressionParser = new OldExpressionParser();
        Expression expression = expressionParser.parse(data, iterable);

        if (expression == null) {
            throw new PandaParserException("Cannot parse expression: " + iterable);
        }

        ModulePath registry = data.getComponent(PandaComponents.MODULE_REGISTRY);
        ClassPrototype iterableType = new ClassPrototypeGenerator().computeIfAbsent(registry, Iterable.class);

        if (!expression.getReturnType().isAssociatedWith(iterableType)) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        return new BlockData(new ForEachBlock(variableId, variable.getType(), expression));
    }

}
