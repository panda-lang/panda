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
import panda.interpreter.parser.Context;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.architecture.expression.StaticExpression;
import panda.interpreter.architecture.type.VisibilityComparator;
import panda.interpreter.parser.expression.PartialResultSubparser;
import panda.interpreter.resource.syntax.TokenTypes;

public final class StaticParser implements PartialResultSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new StaticWorker().withSubparser(this);
    }

    @Override
    public String name() {
        return "static";
    }

    private static final class StaticWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (token.getType() != TokenTypes.UNKNOWN || context.hasResults() || !context.getSynchronizedSource().hasNext()) {
                return null;
            }

            return context.toContext().getImports().forType(token.getValue())
                    .filter(reference -> VisibilityComparator.requireAccess(reference.fetchType(), context.toContext(), token))
                    .map(reference -> ExpressionResult.of(new StaticExpression(reference.fetchType().getSignature())))
                    .getOrNull();
        }

    }

}
