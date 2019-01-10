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
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class MathExpressionParser implements ExpressionSubparser {

    private static final MathParser MATH_PARSER = new MathParser();

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readSeparated(main, source, MathUtils.MATH_OPERATORS, null, matchable -> {
            // read operator
            matchable.next();

            if (!matchable.hasNext()) {
                return false;
            }

            Tokens subSource = matchable.currentSubSource();

            if (subSource.isEmpty()) {
                return false;
            }

            Tokens lastExpression = main.read(subSource);

            if (lastExpression == null) {
                return false;
            }

            matchable.getDistributor().next(lastExpression.size());
            return true;
        });

        if (selected == null) {
            return null;
        }

        if (!TokensUtils.contains(selected, MathUtils.MATH_OPERATORS)) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        if (!MathExpressionUtils.isMathExpression(source)) {
            return null;
        }

        MathExpressionCallback expression = MATH_PARSER.parse(source, data);
        return new PandaExpression(expression.getReturnType(), expression);
    }

    @Override
    public int getMinimumLength() {
        return 3;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.ADVANCED_DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.MATH;
    }

}
