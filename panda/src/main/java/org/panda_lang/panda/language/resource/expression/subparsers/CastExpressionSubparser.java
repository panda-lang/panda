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

package org.panda_lang.panda.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.language.architecture.expression.PandaDynamicExpression;
import org.panda_lang.utilities.commons.function.Produce;

public class CastExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new CastWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String getSubparserName() {
        return "cast";
    }

    private static final class CastWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (!token.contentEquals(Keywords.AS) || !context.hasResults()) {
                return null;
            }

            Produce<Reference, ExpressionResult> result = SubparsersUtils.readType(context);

            if (result.hasError()) {
                return result.getError();
            }

            return ExpressionResult.of(new PandaDynamicExpression(result.getResult().fetch(), context.popExpression()).toExpression());
        }

    }

}
