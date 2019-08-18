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

package org.panda_lang.panda.framework.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

public class LiteralExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new SequenceWorker();
    }

    @Override
    public ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String getSubparserName() {
        return "literal";
    }

    private static class SequenceWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (token.getType() != TokenType.LITERAL) {
                return null;
            }

            switch (token.getValue()) {
                case "true":
                    return ExpressionUtils.toExpressionResult(PandaTypes.BOOLEAN, true);
                case "false":
                    return ExpressionUtils.toExpressionResult(PandaTypes.BOOLEAN, false);
                case "this":
                    return ExpressionResult.of(ThisExpressionCallback.of(context.getContext()));
                case "null":
                    return ExpressionUtils.toExpressionResult(null, null);
                default:
                    throw new PandaParserException("Unknown literal: " + token);
            }
        }

    }

}
