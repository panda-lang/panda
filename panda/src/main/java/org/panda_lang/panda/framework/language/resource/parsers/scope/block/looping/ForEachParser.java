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

package org.panda_lang.panda.framework.language.resource.parsers.scope.block.looping;

import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers.assignation.subparsers.variable.VariableInitializer;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.ForEachBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockData;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK_LABEL)
public class ForEachParser extends BlockSubparserBootstrap {

    private static final TokenPattern CONTENT_PATTERN = TokenPattern.builder()
            .compile("<type:reader type> <name> : <*iterable>")
            .build();

    @Override
    protected BootstrapParserBuilder<BlockData> initialize(ParserData data, BootstrapParserBuilder<BlockData> defaultBuilder) {
        return defaultBuilder
                .handler(new TokenHandler(Keywords.FOREACH))
                .pattern("foreach `( <*content> `)");
    }

    @Autowired
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public BlockData parseBlock(ParserData data, @Component ModuleLoader moduleLoader, @Src("*content") Snippet content) {
        ExtractorResult result = CONTENT_PATTERN.extract(content);
        Snippet name = result.getWildcard("name").get().getValue();
        Snippet type = result.getWildcard("type").get().getValue();
        Snippet iterable = result.getWildcard("*iterable").get().getValue();

        PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
        Scope scope = data.getComponent(UniversalComponents.SCOPE_LINKER).getCurrentScope();
        Expression expression = data.getComponent(PandaComponents.EXPRESSION).parse(data, iterable);

        if (expression == null) {
            throw new PandaParserException("Cannot parse expression: " + iterable);
        }

        VariableInitializer initializer = new VariableInitializer();
        Variable variable = initializer.createVariable(data, moduleLoader, scope, true, true, type.asString(), name.asString());
        int variableId = scope.indexOf(variable);

        if (!PandaTypes.ITERABLE.isAssignableFrom(expression.getReturnType())) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        return new BlockData(new ForEachBlock(variableId, variable.getType(), expression));
    }

}
