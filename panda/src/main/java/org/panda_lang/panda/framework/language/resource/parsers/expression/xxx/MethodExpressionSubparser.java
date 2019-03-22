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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionType;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.utils.reader.ExpressionSeparatorExtensions;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.utils.reader.ExpressionSeparatorReader;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.utils.reader.ReaderFinisher;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.invoker.MethodInvokerExpressionUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

public class MethodExpressionSubparser implements ExpressionSubparser, ReaderFinisher {

    private static final Token[] METHOD_SEPARATORS = ArrayUtils.of(Separators.PERIOD);
    private final ExpressionSeparatorExtensions extensions = new ExpressionSeparatorExtensions(this, SubparserUtils.NAMES_FILTER);

    @Override
    public boolean finish(ExpressionParserOld parser, MatchableDistributor matchable) {
        // at least 3 elements required: <method-name> ( )
        if ((matchable.getDistributor().size() - matchable.getIndex()) < 3) {
            return false;
        }

        // read dot
        matchable.nextVerified();

        return finish(matchable);
    }

    private boolean finish(MatchableDistributor matchable) {
        // read method name
        matchable.nextVerified();

        if (!matchable.nextVerified().contentEquals(Separators.PARENTHESIS_LEFT)) {
            return false;
        }

        matchable.verify();

        if (matchable.isMatchable()) {
            return matchable.nextVerified().contentEquals(Separators.PARENTHESIS_RIGHT);
        }

        // parameters content
        while (matchable.hasNext() && !matchable.isMatchable()) {
            matchable.nextVerified();
        }

        return matchable.isMatchable();
    }

    @Override
    public @Nullable Snippet read(ExpressionParserOld parent, Snippet source) {
        Snippet selected = ExpressionSeparatorReader.getInstance().readSeparated(parent, source, METHOD_SEPARATORS, extensions);

        // local method, at least 3 elements and contains opening parenthesis
        if (selected == null && source.size() > 3 && source.getToken(1).equals(Separators.PARENTHESIS_LEFT)) {
            MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(source));

            if (!finish(matchable)) {
                return null;
            }

            selected = source.subSource(0, matchable.getIndex());
        }

        // at least 3 elements required: <method-name> ( )
        if (selected == null || selected.size() < 3 ) {
            return null;
        }

        // method source has to end with parenthesis
        if (!selected.getLast().contentEquals(Separators.PARENTHESIS_RIGHT)) {
            return null;
        }

        // verify period-less structure
        if (!selected.contains(Separators.PERIOD) && !selected.get(1).contentEquals(Separators.PARENTHESIS_LEFT)) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParserOld parent, ParserData data, Snippet source) {
        MethodInvokerExpressionParser methodInvokerParser = MethodInvokerExpressionUtils.match(source);

        if (methodInvokerParser == null) {
            return null;
        }

        methodInvokerParser.parse(source, data);
        return new PandaExpression(methodInvokerParser.toCallback());
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.STANDALONE;
    }

    @Override
    public int getMinimumLength() {
        return 3;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.METHOD;
    }

}
