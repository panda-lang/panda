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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader.ExpressionSeparatorExtensions;
import org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader.ExpressionSeparatorReader;
import org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader.ReaderFinisher;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.OperationExpressionUtils;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.OperationParser;

public class OperationExpressionSubparser implements ExpressionSubparser, ReaderFinisher {

    private static final OperationParser OPERATION_PARSER = new OperationParser();
    private final ExpressionSeparatorExtensions extensions = new ExpressionSeparatorExtensions(this);

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = ExpressionSeparatorReader.getInstance().readSeparated(main, source, OperationExpressionUtils.OPERATORS, extensions);

        if (selected == null) {
            return null;
        }

        if (!TokensUtils.contains(selected, OperationExpressionUtils.OPERATORS)) {
            return null;
        }

        return selected;
    }

    @Override
    public boolean finish(ExpressionParser parser, MatchableDistributor matchable) {
        // read operator
        matchable.next();

        if (!matchable.hasNext()) {
            return false;
        }

        Tokens subSource = matchable.currentSubSource();

        if (subSource.isEmpty()) {
            return false;
        }

        Tokens lastExpression = parser.read(subSource);

        if (lastExpression == null) {
            return false;
        }

        matchable.getDistributor().next(lastExpression.size());
        return true;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        if (!OperationExpressionUtils.isOperationExpression(source)) {
            return null;
        }

        return OPERATION_PARSER.parse(data, source);
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
