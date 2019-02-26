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
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.parsers.general.number.NumberParser;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class SingleExpressionSubparser implements ExpressionSubparser {

    private static final NumberParser NUMBER_PARSER = new NumberParser();

    @Override
    public @Nullable Tokens read(ExpressionParserOld parent, Tokens source) {
        return SubparserUtils.readFirstOfType(source, TokenType.UNKNOWN);
    }

    @Override
    public Expression parse(ExpressionParserOld parent, ParserData data, Tokens source) {
        Value numericValue = NUMBER_PARSER.parse(data, source);

        if (numericValue != null) {
            return new PandaExpression(numericValue);
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.SINGLE;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.SINGLE;
    }

}
