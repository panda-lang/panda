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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionContext;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserType;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionUtils;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.ThisExpressionCallback;

public class LiteralExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new SequenceWorker().withSubparser(this);
    }

    @Override
    public ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    private static class SequenceWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private boolean parsed;

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionContext context) {
            TokenRepresentation token = context.getCurrent();

            if (token.getType() != TokenType.LITERAL) {
                return null;
            }

            switch (token.getTokenValue()) {
                case "null":
                    return ExpressionUtils.toExpressionResult(null, null);
                case "true":
                    return ExpressionUtils.toExpressionResult(PandaTypes.BOOLEAN, true);
                case "false":
                    return ExpressionUtils.toExpressionResult(PandaTypes.BOOLEAN, false);
                case "this":
                    return ExpressionResult.of(ThisExpressionCallback.of(context.getData()));
                default:
                    throw new PandaParserException("Unknown literal: " + token);
            }
        }

    }

}
