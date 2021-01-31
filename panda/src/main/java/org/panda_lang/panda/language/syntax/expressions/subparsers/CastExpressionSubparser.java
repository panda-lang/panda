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

package org.panda_lang.panda.language.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.expression.PandaDynamicExpression;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.architecture.type.VisibilityComparator;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.utilities.commons.function.Result;

public final class CastExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new CastWorker().withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String name() {
        return "cast";
    }

    private static final class CastWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (!token.contentEquals(Keywords.AS) || !context.hasResults()) {
                return null;
            }

            // TODO: Parent signature
            Result<Signature, ExpressionResult> result = SubparsersUtils.readType(null, context);

            if (result.isErr()) {
                return result.getError();
            }

            Signature signature = result.get();
            VisibilityComparator.requireAccess(signature.toTyped().fetchType(), context.toContext(), token);
            return ExpressionResult.of(new PandaDynamicExpression(signature, context.popExpression()).toExpression());
        }

    }

}
