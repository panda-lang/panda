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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.assignation.subparsers.array;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DistributorUtils;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ArrayValueAccessorParser implements Parser {

    public ArrayValueAccessor parse(ParserData data, Snippet source, ArrayValueAccessor.ArrayValueAccessorAction action) {
        Snippet reversed = source.reverse();

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

        Snippet instanceSource = reversed.subSource(matchable.getIndex(), reversed.size()).reverse();
        Snippet indexSource = reversed.subSource(1, matchable.getIndex() - 1).reverse();

        ExpressionParser parser = data.getComponent(UniversalComponents.EXPRESSION);
        Expression instance = parser.parse(data, instanceSource);
        Expression index = parser.parse(data, indexSource);

        if (!PandaTypes.INT.isAssignableFrom(index.getReturnType())) {
            throw new PandaParserFailure("The specified index is not an integer", data);
        }

        return ArrayValueAccessorUtils.of(data, source, instance, index, action);
    }

}
