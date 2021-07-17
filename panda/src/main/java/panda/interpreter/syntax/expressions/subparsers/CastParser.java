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
import panda.interpreter.architecture.expression.PandaDynamicExpression;
import panda.interpreter.architecture.type.VisibilityComparator;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.std.Result;

public final class CastParser implements ExpressionSubparser {

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
