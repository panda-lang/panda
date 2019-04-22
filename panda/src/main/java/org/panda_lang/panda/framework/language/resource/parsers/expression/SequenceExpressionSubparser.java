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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

public class SequenceExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new LiteralWorker();
    }

    @Override
    public String getSubparserName() {
        return "sequence";
    }

    private static class LiteralWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionContext context) {
            TokenRepresentation token = context.getCurrentRepresentation();

            if (!token.getTokenName().equals("String")) {
                return null;
            }

            return ExpressionUtils.toExpressionResult(PandaTypes.STRING, token.getTokenValue());
        }

    }

}
