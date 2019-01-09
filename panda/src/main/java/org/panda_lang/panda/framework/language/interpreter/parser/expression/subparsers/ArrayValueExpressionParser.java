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

package org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DistributorUtils;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

public class ArrayValueExpressionParser implements ExpressionSubparser {

    private static final Token[] ARRAY_SEPARATORS = ArrayUtils.of(Separators.PERIOD);

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        SourceStream stream = new PandaSourceStream(source);

        ExpressionSubparsers subparsers = main.getSubparsers().fork();
        subparsers.removeSubparser(getName());

        ExpressionParser parser = new ExpressionParser(main, subparsers);
        Tokens value = parser.read(stream);

        if (TokensUtils.isEmpty(value)) {
            return null;
        }

        MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(source));
        matchable.getDistributor().setIndex(source.size() - stream.getUnreadLength());

        // at least 3 elements required: [ <index> ]
        if (matchable.getUnreadLength() < 3) {
            return null;
        }

        // check if the opening section is the square bracket
        if (!matchable.nextVerified().contentEquals(Separators.SQUARE_BRACKET_LEFT)) {
            return null;
        }

        matchable.verify();

        // require content in the [ ] section
        if (matchable.isMatchable()) {
            return null;
        }

        // parameters content
        while (matchable.hasNext() && !matchable.isMatchable()) {
            matchable.nextVerified();
        }

        // check if the [ ] section is closed
        if (!matchable.isMatchable()) {
            return null;
        }

        // check if the closing character was square brace
        if (!matchable.current().contentEquals(Separators.SQUARE_BRACKET_RIGHT)) {
            return null;
        }

        Tokens selected = source.subSource(0, matchable.getIndex());

        // at least 4 elements required: <field-name> [ <index> ]
        if (selected == null || selected.size() < 4 ) {
            return null;
        }

        // array value source has to end with parenthesis
        if (!selected.getLast().contentEquals(Separators.SQUARE_BRACKET_RIGHT)) {
            return null;
        }

        return selected;
    }

    @Override
    public @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        Tokens reversed = source.reverse();

        MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(reversed));
        matchable.withReplaced(DistributorUtils.REVERSED_OPERATORS);

        if (!matchable.nextVerified().contentEquals(Separators.SQUARE_BRACKET_RIGHT)) {
            return null;
        }

        // update matchable reference
        matchable.verify();

        // read the [ ] section
        while (matchable.hasNext() && !matchable.isMatchable()) {
            matchable.nextVerified();
        }

        matchable.verify();

        // check if the section is closed
        if (!matchable.isMatchable()) {
            return null;
        }

        Tokens instanceSource = reversed.subSource(matchable.getIndex(), reversed.size()).reverse();
        Tokens indexSource = reversed.subSource(1, matchable.getIndex() - 1).reverse();

        Expression instance = main.parse(data, instanceSource);
        Expression index = main.parse(data, indexSource);

        if (!(instance.getReturnType() instanceof ArrayClassPrototype)) {
            throw new PandaParserFailure("Cannot use index with non-array types", data, source);
        }

        ArrayClassPrototype prototype = (ArrayClassPrototype) instance.getReturnType();
        ClassPrototype type = data.getComponent(PandaComponents.PANDA_SCRIPT).getModuleLoader().forClass(prototype.getType());

        return new PandaExpression(type, (expression, branch) -> {
            Object[] array = instance.getExpressionValue(branch).getValue();
            Number i = index.getExpressionValue(branch).getValue();
            return new PandaValue(type, array[i.intValue()]);
        });
    }

    @Override
    public int getMinimumLength() {
        return 4;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.ARRAY_VALUE;
    }

}
