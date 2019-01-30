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
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparsers;
import org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader.ExpressionSeparatorExtensions;
import org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader.ExpressionSeparatorReader;
import org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader.ReaderFinisher;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.logical.EqualsComparator;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.logical.LogicalExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.logical.NotEqualsComparator;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.panda.framework.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

import java.util.Arrays;

public class LogicalExpressionSubparser implements ExpressionSubparser, ReaderFinisher {

    private static final Token[] LOGICAL_OPERATORS = Arrays.stream(Operators.values())
            .filter(operator -> OperatorUtils.isMemberOf(operator, OperatorFamilies.LOGICAL))
            .toArray(Token[]::new);

    private final ExpressionSeparatorExtensions extensions = new ExpressionSeparatorExtensions(this, null);

    @Override
    public boolean finish(ExpressionParser parser, MatchableDistributor matchable) {
        Operator operator = ObjectUtils.cast(Operator.class, matchable.nextVerified().getToken());

        if (!OperatorUtils.isMemberOf(operator, OperatorFamilies.LOGICAL)) {
            return false;
        }

        Tokens source = parser.read(matchable.currentSubSource());

        if (source == null) {
            return false;
        }

        matchable.getDistributor().next(source.size());
        return true;
    }

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        return ExpressionSeparatorReader.getInstance().readSeparated(main, source, LOGICAL_OPERATORS, extensions);
    }

    @Override
    public @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        ExpressionSubparsers subparsers = main.getSubparsers().fork();
        subparsers.removeSubparser(getName());

        ExpressionParser parser = new ExpressionParser(main, subparsers);
        SourceStream stream = new PandaSourceStream(source);

        Tokens left = parser.read(stream);

        if (left == null) {
            return null;
        }

        TokenRepresentation operatorRepresentation = stream.read();
        Operator operator = ObjectUtils.cast(Operator.class, operatorRepresentation.getToken());

        if (operator == null) {
            return null;
        }

        Tokens right = parser.read(source);

        if (right == null) {
            return null;
        }

        Expression leftExpression = main.parse(data, left);
        Expression rightExpression = main.parse(data, right);
        ExpressionCallback callback;

        switch (operator.getTokenValue()) {
            case "==":
                callback = new LogicalExpressionCallback(new EqualsComparator(), leftExpression, rightExpression);
                break;
            case "!=":
                callback = new LogicalExpressionCallback(new NotEqualsComparator(), leftExpression, rightExpression);
                break;
            default:
                throw new PandaParserFailure("Operator not implemented", data, new PandaTokens(operatorRepresentation));
        }

        return new PandaExpression(callback);
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.LOGICAL;
    }

}
