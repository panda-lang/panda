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
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.ConstructorExpressionParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.InstanceExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.util.List;

public class InstanceExpressionParser implements ExpressionSubparser {

    private static final GappedPattern INSTANCE_PATTERN = new GappedPatternBuilder()
            .unit(TokenType.KEYWORD, "new")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, ")")
            .simpleHollow()
            .build();

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        if (!source.getFirst().contentEquals(Keywords.NEW)) {
           return null;
        }

        Tokens args = SubparserUtils.readBetweenSeparators(source.subSource(2, source.size()), Separators.PARENTHESIS_LEFT);

        if (args == null) {
            return null;
        }

        return source.subSource(0, 2 + args.size());
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        List<Tokens> constructorMatches = INSTANCE_PATTERN.match(new PandaTokenReader(source));

        if (constructorMatches != null && constructorMatches.size() == 3 && constructorMatches.get(2).size() == 0) {
            ConstructorExpressionParser callbackParser = new ConstructorExpressionParser();

            callbackParser.parse(source, data);
            InstanceExpressionCallback callback = callbackParser.toCallback();

            return new PandaExpression(callback);
        }

        return null;
    }

    @Override
    public int getMinimumLength() {
        return 4;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.Dynamic.CONSTRUCTOR_CALL;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.INSTANCE;
    }

}
