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
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

public class ArrayValueExpressionParser implements ExpressionSubparser {

    private static final Token[] ARRAY_SEPARATORS = ArrayUtils.of(Separators.PERIOD);

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readSeparated(main, source, ARRAY_SEPARATORS, SubparserUtils.NAMES_FILTER, MatchableDistributor::hasNext);

        MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(source));
        matchable.getDistributor().setIndex(selected != null ? selected.size() : 0);

        // at least 4 elements required: <field/variable> [ <index> ]
        if ((source.size() - matchable.getIndex()) < 4) {
            return null;
        }

        // read field name
        matchable.nextVerified();

        // check if the opening section is the square bracket
        if (!matchable.nextVerified().contentEquals(Separators.SQUARE_BRACKET_LEFT)) {
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

        selected = source.subSource(0, matchable.getIndex() + 1);

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
        throw new RuntimeException("Not implemented");
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
