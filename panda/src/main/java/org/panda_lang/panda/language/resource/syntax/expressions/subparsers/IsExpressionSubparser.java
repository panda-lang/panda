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
import org.panda_lang.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.architecture.expression.DynamicExpression;
import org.panda_lang.framework.language.architecture.prototype.utils.VisibilityComparator;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.utilities.commons.function.Produce;

public final class IsExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new IsWorker(context).withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String getSubparserName() {
        return "is";
    }

    private static final class IsWorker extends AbstractExpressionSubparserWorker {

        private final Prototype boolType;

        private IsWorker(Context context) {
            this.boolType = ModuleLoaderUtils.forClass(context, boolean.class);
        }

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (!context.hasResults() || !token.contentEquals(Keywords.IS)) {
                return null;
            }

            Produce<Reference, ExpressionResult> result = SubparsersUtils.readType(context);

            if (result.hasError()) {
                return result.getError();
            }

            Prototype prototype = result.getResult().fetch();
            VisibilityComparator.requireAccess(prototype, context.getContext(), token);

            DynamicExpression expression = new IsExpression(boolType, context.popExpression(), prototype);
            return ExpressionResult.of(expression.toExpression());
        }

    }

}
