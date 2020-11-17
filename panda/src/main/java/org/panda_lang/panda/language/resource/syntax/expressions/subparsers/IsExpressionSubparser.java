/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.architecture.expression.DynamicExpression;
import org.panda_lang.language.architecture.type.VisibilityComparator;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.utilities.commons.function.Result;

public final class IsExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new IsWorker(context).withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String name() {
        return "is";
    }

    private static final class IsWorker extends AbstractExpressionSubparserWorker {

        private final Type boolType;

        private IsWorker(Context<?> context) {
            this.boolType = context.getTypeLoader().requireType("panda::Bool");
        }

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (!context.hasResults() || !token.contentEquals(Keywords.IS)) {
                return null;
            }


            Result<Signature, ExpressionResult> result = SubparsersUtils.readType(context);

            if (result.isErr()) {
                return result.getError();
            }

            Signature signature = result.get();
            VisibilityComparator.requireAccess(signature.getPrimaryType(), context.toContext(), token);

            DynamicExpression expression = new IsExpression(boolType, context.popExpression(), signature);
            return ExpressionResult.of(expression.toExpression());
        }

    }

}
