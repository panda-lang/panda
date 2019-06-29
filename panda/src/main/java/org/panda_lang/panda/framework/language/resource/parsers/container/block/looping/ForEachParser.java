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

package org.panda_lang.panda.framework.language.resource.parsers.container.block.looping;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.DescriptivePattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.ForEachBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.variable.VariableParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockData;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(pipeline = PandaPipelines.BLOCK_LABEL)
public class ForEachParser extends BlockSubparserBootstrap {

    private static final DescriptivePattern CONTENT_PATTERN = DescriptivePattern.builder()
            .compile("<type:reader type> <name> : <*iterable>")
            .build();

    private final VariableParser initializer = new VariableParser();

    @Override
    protected BootstrapInitializer<BlockData> initialize(ParserData data, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.FOREACH))
                .pattern("foreach content:~(");
    }

    @Autowired
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    BlockData parseBlock(ParserData data, @Src("content") Snippet content) {
        ExtractorResult result = CONTENT_PATTERN.extract(data, content);
        Snippet name = result.getWildcard("name").get().getValue();
        Snippet type = result.getWildcard("type").get().getValue();
        Snippet iterable = result.getWildcard("*iterable").get().getValue();

        Scope scope = data.getComponent(UniversalComponents.SCOPE_LINKER).getCurrentScope();
        Expression expression = data.getComponent(UniversalComponents.EXPRESSION).parse(data, iterable);

        if (expression == null) {
            throw new PandaParserException("Cannot parse expression: " + iterable);
        }

        Variable variable = initializer.createVariable(data, scope, true, true, type, name);
        int variableId = scope.indexOf(variable);

        if (!PandaTypes.ITERABLE.isAssignableFrom(expression.getReturnType())) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        return new BlockData(new ForEachBlock(variableId, variable.getType(), expression));
    }

}
