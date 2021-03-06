/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.expression.ThisExpression;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.TypeContext;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserException;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionParserUtils;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserType;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.TokenTypes;

public final class LiteralParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new SequenceWorker(context).withSubparser(this);
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String name() {
        return "literal";
    }

    private static final class SequenceWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private final Signature boolType;

        public SequenceWorker(Context<?> context) {
            this.boolType = context.getTypeLoader().requireType("panda/panda@::Bool").getSignature();
        }

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (token.getType() != TokenTypes.LITERAL) {
                return null;
            }

            switch (token.getValue()) {
                case "true":
                    return ExpressionParserUtils.toExpressionResult(boolType, true);
                case "false":
                    return ExpressionParserUtils.toExpressionResult(boolType, false);
                // case "null":
                //    return ExpressionParserUtils.toExpressionResult(null, null);
                case "this":
                    if (context.toContext().getSubject() instanceof TypeContext) {
                        //noinspection unchecked
                        return ExpressionResult.of(ThisExpression.of((Context<TypeContext>) context.toContext()));
                    }
                    else {
                        throw new PandaParserFailure(context, "Cannot use 'this' outside of the type object");
                    }
                default:
                    throw new PandaParserException("Unknown literal: " + token);
            }
        }

    }

}
