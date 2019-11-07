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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.architecture.expression.StaticExpression;
import org.panda_lang.framework.language.architecture.prototype.utils.VisibilityComparator;
import org.panda_lang.framework.language.interpreter.parser.expression.PartialResultSubparser;

public final class StaticExpressionSubparser implements PartialResultSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new StaticWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "static";
    }

    private static final class StaticWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (token.getType() != TokenTypes.UNKNOWN || context.hasResults() || !context.getSynchronizedSource().hasNext()) {
                return null;
            }

            return context.getContext().getComponent(Components.IMPORTS)
                    .forName(token.getValue())
                    .map(Reference::fetch)
                    .filter(reference -> VisibilityComparator.requireAccess(reference, context.getContext(), token))
                    .map(prototype -> ExpressionResult.of(new StaticExpression(prototype)))
                    .orElse(null);
        }

    }

}
