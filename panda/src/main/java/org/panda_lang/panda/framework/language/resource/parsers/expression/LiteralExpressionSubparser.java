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
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class LiteralExpressionSubparser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser parent, Tokens source) {
        return SubparserUtils.readFirstOfType(source, TokenType.LITERAL);
    }

    @Override
    public Expression parse(ExpressionParser parent, ParserData data, Tokens source) {
        TokenRepresentation token = source.get(0);

        switch (token.getTokenValue()) {
            case "null":
                return new PandaExpression(new PandaValue(null, null));
            case "true":
                return SubparserUtils.toSimpleKnownExpression(PandaTypes.BOOLEAN, true);
            case "false":
                return SubparserUtils.toSimpleKnownExpression(PandaTypes.BOOLEAN, false);
            case "this":
                return ThisExpressionCallback.asExpression(data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE));
            default:
                throw new PandaParserException("Unknown literal: " + token);
        }
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.SINGLE;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.LITERAL;
    }

}
