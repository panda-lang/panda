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
import panda.interpreter.architecture.type.Type;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.parser.expression.ExpressionParserUtils;
import panda.interpreter.token.TokenUtils;

public final class SequenceParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new SequenceWorker(context).withSubparser(this);
    }

    @Override
    public String name() {
        return "sequence";
    }

    private static final class SequenceWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private final Type stringType;

        private SequenceWorker(Context<?> context) {
            this.stringType = context.getTypeLoader().requireType("panda/panda@::String");
        }

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (TokenUtils.hasName(token, "String")) {
                return ExpressionParserUtils.toExpressionResult(stringType.getSignature(), token.getValue());
            }

            return null;
        }

    }

}
