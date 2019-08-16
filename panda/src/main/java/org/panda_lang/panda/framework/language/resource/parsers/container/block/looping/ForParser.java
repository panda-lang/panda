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

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.Registrable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.language.architecture.dynamic.assigner.VariableAssignerUtils;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.ForBlock;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.variable.VariableParser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockData;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

@Registrable(pipeline = PandaPipelines.BLOCK_LABEL)
public final class ForParser extends BlockSubparserBootstrap {

    private static final VariableParser VARIABLE_PARSER = new VariableParser();
    private static final Expression DEFAULT_CONDITION = new PandaExpression(new PandaStaticValue(PandaTypes.BOOLEAN, true));

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.FOR))
                .pattern("for content:~(");
    }

    @Autowired
    BlockData parseBlock(Context context, @Src("content") Snippet content) {
        Snippet[] forEachElements = content.split(Separators.SEMICOLON);

        if (forEachElements.length != 3) {
            throw PandaParserFailure.builder("Invalid amount of statements in for loop declaration", context)
                    .withStreamOrigin(content)
                    .withNote("The statement should look like: for (<initialization>; <termination>; <increment>)")
                    .build();
        }

        ExpressionParser expressionParser = context.getComponent(UniversalComponents.EXPRESSION);
        Snippet initializationSource = forEachElements[0];
        Executable initialization = null;

        if (!initializationSource.isEmpty()) {
            Snippet[] initializationElements = initializationSource.split(Operators.ASSIGNMENT);

            Scope scope = context.getComponent(UniversalComponents.LINKER).getCurrentScope();
            Variable variable = VARIABLE_PARSER.parseVariable(context, scope, initializationElements[0]);

            Assigner<Variable> assigner = VariableAssignerUtils.of(context, scope, variable, expressionParser.parse(context, initializationElements[1]));
            initialization = assigner.toExecutableStatement();
        }

        Snippet terminationSource = forEachElements[1];
        Expression termination = DEFAULT_CONDITION;

        if (!terminationSource.isEmpty()) {
            termination = expressionParser.parse(context, terminationSource);
        }

        Snippet incrementSource = forEachElements[2];
        Expression increment = null;

        if (!incrementSource.isEmpty()) {
            increment = expressionParser.parse(context, incrementSource);
        }

        return new BlockData(new ForBlock(initialization, termination, increment));
    }

}
