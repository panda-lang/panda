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
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionType;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.CreaseExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.general.accessor.AccessorParser;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.panda.framework.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

public class CreaseExpressionSubparser implements ExpressionSubparser {

    private static final AccessorParser ACCESSOR_PARSER = new AccessorParser();

    @Override
    public @Nullable Tokens read(ExpressionParser parent, Tokens source) {
        if (source.startsWith(Operators.INCREMENT) || source.startsWith(Operators.DECREMENT)) {
            Tokens pre = parent.read(source.subSource(1, source.size()));

            if (pre == null) {
                return null;
            }

            return new PandaTokens(source.getFirst()).addTokens(pre);
        }

        ExpressionParser postExpression = parent.getSubparsers().fork()
                .removeSubparser(getName())
                .toExpressionParser(null);

        Tokens post = postExpression.read(source);

        if (post == null) {
            return null;
        }

        if (post.size() == source.size()) {
            return null;
        }

        TokenRepresentation operator = source.get(post.size());

        if (!operator.contentEquals(Operators.INCREMENT) && !operator.contentEquals(Operators.DECREMENT)) {
            return null;
        }

        return post.addToken(operator);
    }

    @Override
    public @Nullable Expression parse(ExpressionParser parent, ParserData data, Tokens source) {
        Operator operator = ObjectUtils.cast(Operator.class, source.getFirst().getToken());
        boolean post = OperatorUtils.isMemberOf(operator, OperatorFamilies.INCREMENT_AND_DECREMENT);

        if (operator == null) {
            operator = ObjectUtils.cast(Operator.class, source.getLast().getToken());
        }

        boolean grow = Operators.INCREMENT.equals(operator);
        Tokens expressionSource = post ? source.subSource(1, source.size()) : source.subSource(0, source.size() - 1);

        if (expressionSource.isEmpty()) {
            throw new PandaParserFailure("Variable expected", data, source);
        }

        Accessor<?> accessor = ACCESSOR_PARSER.parse(data, expressionSource);

        if (!PandaTypes.NUMBER.isAssignableFrom(accessor.getVariable().getType())) {
            throw new PandaParserFailure("Incrementation/decrementation operation requires number type", data, source);
        }

        return new PandaExpression(new CreaseExpressionCallback(accessor, grow, post));
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.STANDALONE;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.CREASE;
    }

}
