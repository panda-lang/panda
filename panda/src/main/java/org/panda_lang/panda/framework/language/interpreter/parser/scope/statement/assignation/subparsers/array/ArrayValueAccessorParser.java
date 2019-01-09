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

package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.subparsers.array;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.DistributorUtils;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ArrayValueAccessorParser implements Parser {

    public ArrayValueAccessor parse(ParserData data, Tokens source) {
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

        ExpressionParser main = data.getComponent(PandaComponents.EXPRESSION);
        Expression instance = main.parse(data, instanceSource);
        Expression index = main.parse(data, indexSource);

        if (!(instance.getReturnType() instanceof ArrayClassPrototype)) {
            throw new PandaParserFailure("Cannot use index with non-array types", data, source);
        }

        ArrayClassPrototype prototype = (ArrayClassPrototype) instance.getReturnType();
        ClassPrototype type = data.getComponent(PandaComponents.PANDA_SCRIPT).getModuleLoader().forClass(prototype.getType());

        return new ArrayValueAccessor(prototype, type, instance, index, (branch, p, t, array, i) -> new PandaValue(type, array[i.intValue()]));
    }

}
